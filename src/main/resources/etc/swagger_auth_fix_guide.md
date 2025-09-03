# Swagger Authorize 인증 문제 해결 가이드

현재 생성된 JWT 토큰으로 다른 API에 접근할 수 없는 문제를 해결하고, Swagger UI에서 원활하게 API를 테스트할 수 있도록 안내하는 가이드입니다.

## 문제 원인

1.  **로그인 API의 잘못된 반환 값**: `/user/login` API가 JWT 토큰을 생성하지만, 실제로는 클라이언트(Swagger UI)에게 토큰을 반환하지 않고 `userNo`라는 숫자만 반환하고 있습니다. 따라서 테스트에 사용할 토큰을 얻을 수 없습니다.
2.  **인증 방식의 불일치**: `JwtAuthenticationFilter`는 헤더의 토큰을 검증하여 `request` 객체에 사용자 정보를 담아줍니다. 하지만 다른 API 컨트롤러들은 여전히 오래된 방식인 `HttpSession`에서 사용자 정보를 찾고 있어, 토큰이 유효하더라도 인증이 통과되지 않습니다.

---

## 해결 방법

아래 3단계에 따라 코드를 수정하면 문제를 해결할 수 있습니다.

### 1단계: 로그인 API가 JWT 토큰을 반환하도록 수정

`UsersController`의 `login` 메소드가 세션을 저장하는 대신, 생성된 토큰을 JSON 형태로 반환하도록 변경해야 합니다.

**파일**: `UsersController.java`

**수정 후 `login` 메소드 예시:**
아래 코드로 `login` 메소드 전체를 교체해 주세요.

```java
import org.springframework.http.ResponseEntity; // 임포트 추가

// UsersController 클래스 밖에 임시 응답 DTO 클래스를 추가하거나 별도 파일로 생성합니다.
class LoginResponse {
    private String token;
    public LoginResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}

// ... UsersController 클래스 내부 ...

@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody UsersDto usersDto) { // ★ 반환 타입을 ResponseEntity<?>로 변경
    // 서비스 호출하여 유효한 로그인인지 검사
    Map<String, Object> loginResult = usersService.login(usersDto);
    int loginUserNo = (int) loginResult.get("loginUserNo");

    // 로그인 실패할시 401 응답 반환
    if (loginUserNo == 0) {
        return ResponseEntity.status(401).body("로그인 실패");
    }

    // ★ 로그인 성공 시 토큰 생성
    String token = createLoginToken(loginUserNo);

    // ★ 세션 저장 대신, 생성된 토큰을 JSON 객체에 담아 반환
    return ResponseEntity.ok(new LoginResponse(token));
}
```

### 2단계: 다른 API들이 토큰 기반으로 인증하도록 수정

다른 컨트롤러들이 `HttpSession`이 아닌, `JwtAuthenticationFilter`가 요청(request)에 담아준 사용자 정보를 사용하도록 변경해야 합니다. `JwtAuthenticationFilter`는 토큰 검증 후 `request.setAttribute("loginUserNo", ...)` 코드를 통해 사용자 번호를 `request` 객체에 저장해 줍니다.

**수정 예시**: `CTemController`의 `getCTem` 메소드를 예시로 보여드리겠습니다.

**파일**: `CTemController.java`

**수정 후 예시:**
`HttpSession` 대신 `HttpServletRequest`를 파라미터로 받고, `request.getAttribute()`로 사용자 정보를 확인합니다.

```java
import jakarta.servlet.http.HttpServletRequest; // 임포트 추가

// ... CTemController 클래스 내부 ...

@GetMapping("")
public List<CTemDto> getCTem(HttpServletRequest request) { // ★ 파라미터를 HttpServletRequest로 변경
    List<CTemDto> list = new ArrayList<>();

    // 1. 로그인상태 확인 (request attribute 확인)
    Object userNoObject = request.getAttribute("loginUserNo");

    if (userNoObject == null) {
        CTemDto dto = new CTemDto();
        dto.setStatus("NOT_LOGGED_IN");
        list.add(dto);
        return list;
    }

    // ★ 참고: bnNo(사업자번호)는 현재 세션에서 가져오고 있으나,
    // JWT로 전환 시 이 정보도 토큰에 포함(claim)하거나,
    // userNo로 DB에서 다시 조회해야 합니다. 이 부분은 추가적인 수정이 필요합니다.
    String bnNo = "..."; // DB에서 userNoObject를 이용해 조회하는 로직 필요
    list = cTemService.getCTem(bnNo);

    // ... 이후 로직 ...
    return list;
}
```

> **중요**: 위 예시처럼, 프로젝트의 **모든 컨트롤러**에서 `HttpSession`을 확인하는 부분을 `HttpServletRequest`에서 속성을 가져오는 방식으로 변경해야 합니다. 또한, `loginBnNo`처럼 세션에 저장했던 다른 정보들도 JWT에 담거나 DB에서 조회하는 방식으로 변경이 필요합니다.

### 3단계: 최종 테스트 방법

1.  위 코드 수정사항들을 프로젝트에 적용하고 서버를 재시작합니다.
2.  Swagger UI에서 `/user/login` API를 실행하고, 응답으로 받은 **token 문자열을 복사**합니다.
3.  우측 상단의 **"Authorize"** 버튼을 클릭하고, `Value` 입력창에 `Bearer {복사한 토큰 값}`을 붙여넣고 `Authorize` 버튼을 누릅니다.
4.  이제 다른 API들을 "Try it out"으로 실행하면 정상적으로 인증되어 동작하는 것을 확인할 수 있습니다.
