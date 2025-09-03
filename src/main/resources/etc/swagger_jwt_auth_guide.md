# Swagger Authorize와 JWT 연동 가이드

현재 프로젝트는 로그인 시 세션(Session)을 생성하고, JWT는 비밀번호 재설정용으로만 사용하고 있습니다. Swagger의 "Authorize" 버튼을 제대로 활용하려면, 로그인 성공 시 세션을 생성하는 대신 JWT 토큰을 발급하도록 변경해야 합니다.

이 가이드는 그 방법을 3단계로 나누어 안내합니다.

---

### 1단계: 로그인 응답 변경 (Controller 수정)

가장 먼저 `UsersController`의 `login` 메소드가 `int` (사용자 번호) 대신 **JWT 토큰 문자열을 반환**하도록 수정해야 합니다.

**파일 위치**: `five_minutes.controller.UsersController.java`

**핵심 변경 사항**:
1.  `JwtUtil`을 의존성으로 주입받습니다.
2.  메소드의 반환 타입을 `ResponseEntity<?>`로 변경하여 유연한 응답을 할 수 있도록 합니다.
3.  로그인 성공 시, 세션에 정보를 저장하는 대신 `jwtUtil.createToken()`을 호출하여 토큰을 생성합니다.
4.  생성된 토큰을 JSON 객체에 담아 클라이언트에게 반환합니다.

**수정 후 코드 예시:**

```java
import five_minutes.util.JwtUtil; // JwtUtil 임포트
import java.util.UUID; // UUID 임포트
import org.springframework.http.ResponseEntity; // ResponseEntity 임포트

// 임시 DTO 클래스 (파일 하단이나 별도 파일에 생성)
class LoginResponse {
    private String token;
    public LoginResponse(String token) { this.token = token; }
    public String getToken() { return token; }
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    private final UsersService usersService;
    private final JwtUtil jwtUtil; // ★ 1. JwtUtil 의존성 주입

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsersDto usersDto) { // ★ 2. 반환 타입을 ResponseEntity로 변경

        Map<String, Object> loginResult = usersService.login(usersDto);
        int loginUserNo = (int) loginResult.get("loginUserNo");

        if (loginUserNo == 0) {
            // 로그인 실패 시 401 Unauthorized 응답
            return ResponseEntity.status(401).body("이메일 또는 비밀번호가 올바르지 않습니다.");
        }

        // ★ 3. 로그인 성공 시 JWT 토큰 생성
        // jti(JWT ID)는 토큰마다 고유한 값을 주는 것이 좋으므로 UUID 사용
        String jti = UUID.randomUUID().toString();
        String token = jwtUtil.createToken(String.valueOf(loginUserNo), jti);

        // ★ 4. 세션 저장 대신, 생성된 토큰을 JSON 형태로 반환
        return ResponseEntity.ok(new LoginResponse(token));
    }
    
    // ... (로그아웃 및 다른 메소드들) ...
    // 참고: 로그아웃 로직도 세션 제거가 아닌, 클라이언트 측에서 토큰을 삭제하도록 변경해야 합니다.
}
```

---

### 2단계: `JwtUtil`의 토큰 만료 시간 확인

`JwtUtil`이 생성하는 토큰의 만료 시간이 너무 짧으면 테스트하기 불편할 수 있습니다. 로그인용 토큰은 보통 1시간 이상으로 설정하는 것이 좋습니다.

**파일 위치**: `src/main/resources/application.properties` 또는 `application.yml`

`app.jwt.exp-min` 값을 원하는 분 단위로 설정하세요. (예: 60 = 1시간)

```properties
# application.properties
app.jwt.exp-min=60
```

---

### 3단계: Spring Security 설정 (가장 중요)

이제 서버는 토큰을 발행하지만, 아직 각 API 요청에 대해 토큰을 검증하는 기능이 없습니다. 이를 위해 Spring Security 설정이 필요합니다. 이 단계는 전체 인증 아키텍처를 변경하는 중요한 과정입니다.

**핵심 아이디어:**
1.  **`JwtAuthenticationFilter` 생성**: 모든 API 요청을 가로채는 필터를 만듭니다. 이 필터는 요청의 `Authorization` 헤더에서 JWT 토큰을 꺼내 유효성을 검증합니다.
2.  **`SecurityConfig` 설정**: Spring Security의 설정을 변경하여 세션 대신 JWT를 사용하도록 구성합니다.
    -   로그인 API (`/user/login`)와 Swagger 관련 경로는 누구나 접근할 수 있도록 허용합니다.
    -   나머지 모든 API는 인증(유효한 토큰이 있는 요청)을 거쳐야만 접근할 수 있도록 보호합니다.
    -   위에서 만든 `JwtAuthenticationFilter`를 Spring Security의 필터 체인에 등록합니다.

**개념 코드 예시 (실제로는 더 많은 설정이 필요할 수 있습니다):**

```java
// SecurityConfig.java (예시)
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // csrf 비활성화
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // ★ 세션을 사용하지 않음
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/user/login", "/swagger-ui/**", "/v3/api-docs/**").permitAll() // 로그인과 Swagger는 허용
                .anyRequest().authenticated() // 나머지는 인증 필요
            )
            // ★ 직접 만든 JWT 필터를 UsernamePasswordAuthenticationFilter 앞에 추가
            .addFilterBefore(new JwtAuthenticationFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

// JwtAuthenticationFilter.java (개념 예시)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    // ... 생성자 ...

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        String token = extractTokenFromHeader(request); // 헤더에서 토큰 추출하는 로직

        if (token != null && jwtUtil.validateToken(token)) { // 토큰 유효성 검증
            String userNo = jwtUtil.getUsernameFromToken(token); // 토큰에서 userNo 추출
            
            // Spring Security에 현재 사용자가 인증되었음을 알림
            Authentication auth = new UsernamePasswordAuthenticationToken(userNo, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }
        
        filterChain.doFilter(request, response); // 다음 필터로 요청 전달
    }
}
```

---

### 최종 사용 방법

1.  위의 코드 변경사항을 프로젝트에 적용합니다.
2.  Swagger UI에서 `/user/login` API를 실행하여 ID/비밀번호로 로그인합니다.
3.  응답으로 받은 `token` 값을 복사합니다.
4.  우측 상단의 **"Authorize"** 버튼을 클릭하고, `Value` 입력창에 `Bearer {복사한 토큰 값}` 형식으로 붙여넣은 뒤 `Authorize` 버튼을 누릅니다. (예: `Bearer eyJhbGciOi...`)
5.  이제 다른 API들을 "Try it out"으로 실행하면 요청 헤더에 토큰이 자동으로 포함되어 정상적으로 테스트할 수 있습니다.
