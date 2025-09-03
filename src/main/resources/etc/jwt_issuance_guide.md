# Spring Boot JWT 토큰 발행 가이드

Swagger UI 설정(`OpenApiConfig`)은 API 문서를 위한 것이며, 실제 JWT 토큰을 발행하는 로직은 별도로 구현해야 합니다. 이 가이드는 사용자가 로그인을 성공했을 때 JWT를 발행하는 전체 과정을 안내합니다.

---

### JWT 토큰 발행 과정

1.  **사용자 로그인 요청**: 클라이언트가 ID/비밀번호로 서버의 로그인 API(예: `/api/login`)를 호출합니다.
2.  **서버 인증**: 서버는 ID/비밀번호가 유효한지 확인합니다.
3.  **JWT 생성**: 인증에 성공하면, 서버는 해당 사용자의 정보(예: 사용자 ID, 역할 등)를 담은 JWT를 생성합니다.
4.  **JWT 전송**: 생성된 JWT를 클라이언트에게 응답으로 보내줍니다.
5.  **클라이언트 저장 및 사용**: 클라이언트는 이 토큰을 저장해두고, 이후 API를 호출할 때마다 HTTP 헤더에 담아 보냅니다.

---

### 구현 방법

#### 1. JWT 라이브러리 의존성 추가

Java에서 JWT를 쉽게 생성하고 검증할 수 있도록 도와주는 `jjwt` 라이브러리를 추가합니다.

**Gradle (`build.gradle`)**
```groovy
implementation 'io.jsonwebtoken:jjwt-api:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-impl:0.11.5'
runtimeOnly 'io.jsonwebtoken:jjwt-jackson:0.11.5'
```

**Maven (`pom.xml`)**
```xml
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.11.5</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.11.5</version>
    <scope>runtime</scope>
</dependency>
```

#### 2. JWT 생성 및 검증 유틸리티 클래스 작성

토큰 생성, 만료 시간 설정, 유효성 검증 등 JWT와 관련된 모든 기능을 담당하는 클래스를 작성합니다.

- **위치**: `five_minutes.util` 또는 `five_minutes.jwt` 패키지 등
- **파일명**: `JwtUtil.java` (예시)

```java
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    private Key key;

    @jakarta.annotation.PostConstruct
    public void init() {
        this.key = Keys.hmacShaKeyFor(secret.getBytes());
    }

    /**
     * 사용자 이름을 기반으로 JWT 토큰을 생성합니다.
     */
    public String generateToken(String username) {
        Claims claims = Jwts.claims().setSubject(username);
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * 토큰에서 사용자 이름을 추출합니다.
     */
    public String getUsernameFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * 토큰의 유효성을 검증합니다.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
```

#### 3. `application.yml` 설정 추가

JWT 생성에 필요한 시크릿 키와 만료 시간을 `application.yml` 파일에 추가합니다.

```yaml
jwt:
  secret: VzZzNDVzNHNkc3Nkc2ZzZGY0NTM0NTM0NTQzNDU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU0MzU... # 시크릿 키 (충분히 길고 복잡하게 만드세요)
  expiration: 86400000 # 24시간 (밀리초 단위)
```

#### 4. 로그인 API 엔드포인트 구현

사용자 인증을 처리하고 성공 시 토큰을 발행하는 컨트롤러를 작성합니다. (실제로는 Spring Security와 연동하는 것이 정석이지만, 여기서는 개념 이해를 위해 간단히 표현합니다.)

- **위치**: `five_minutes.controller`
- **파일명**: `AuthController.java` (예시)

```java
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// 임시 DTO 클래스
class LoginRequest { public String username; public String password; }
class LoginResponse { public String token; public LoginResponse(String t) { this.token = t; } }

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final JwtUtil jwtUtil;
    // private final AuthenticationManager authenticationManager; // 실제로는 주입받아 사용

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        // 1. 사용자 인증 (실제로는 Spring Security의 AuthenticationManager 사용)
        // 여기서는 아이디/비번이 맞다고 가정합니다.
        boolean isAuthenticated = true; // 임시로 true

        if (isAuthenticated) {
            // 2. 인증 성공 시 JWT 토큰 생성
            String token = jwtUtil.generateToken(loginRequest.username);
            
            // 3. 토큰을 응답으로 반환
            return ResponseEntity.ok(new LoginResponse(token));
        } else {
            return ResponseEntity.status(401).body("인증 실패");
        }
    }
}
```

이제 `/api/auth/login`을 호출하여 받은 토큰을 복사한 뒤, Swagger UI의 "Authorize" 버튼을 눌러 붙여넣으면 인증이 필요한 다른 API들을 테스트할 수 있게 됩니다.
