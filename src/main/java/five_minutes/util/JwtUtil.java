package five_minutes.util;


import io.jsonwebtoken.Claims; // JWT의 payload(내용물) 타입
import io.jsonwebtoken.Jws;    // 서명된 JWT의 래퍼 (header+payload+signature)
import io.jsonwebtoken.Jwts;  // JJWT 유틸 클래스(빌더/파서의 엔트리 포인트)
import io.jsonwebtoken.security.Keys; // 서명용 Key 생성 헬퍼
import org.springframework.beans.factory.annotation.Value; // @Value로 properties 값 주입
import org.springframework.stereotype.Component;           // @Component: 스프링 빈 등록

import java.nio.charset.StandardCharsets; // 문자열을 바이트로 변환할 때 사용
import javax.crypto.SecretKey;                  // 서명에 사용할 Key 타입
import java.time.Instant;                 // 현재 시각
import java.util.Date;                    // JWT 표준 클레임에 쓰이는 Date 타입


// 토큰 발급/검증을 도와주는 유틸리티 클래스
// - 이 클래스는 jti + sub + exp + signWith 만 사용하여 "최소 구성"으로 JWT를 만든다.
// @Component 는 클래스 자체를 빈으로 만듦.
@Component
public class JwtUtil {  // class start

    // application.properties의 값을 @Value로 주입
    @Value("${app.jwt.secret}") private String secret;   // 최소 32바이트(HS256 256bit 필요)
    @Value("${app.jwt.exp-min}") private long expMin; // 만료(분). 기본 15

    // KEY 를 만드는 메소드
    private SecretKey key() {
        // getBytes => 문자열을 바이트 배열로 변환
        // Keys.hmacShaKeyFor : 바이트 배열을 HS256/HS384/HS512용 비밀키(Key 객체) 로 변환해줌
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }   // func end


    // -------------------------------------------------------------------------------------------------
    // [public String createToken(String userNo, String jti)]
    // - 목적: "이 사용자가 비밀번호 재설정을 할 수 있도록" 임시 JWT를 발급한다.
    // - 최소 구성 요소만 넣는다: jti(고유ID), sub(대상 사용자 번호), exp(만료 시간) + signWith(서명).
    //
    // 파라미터:
    //  - userNo: "누구의" 토큰인지 나타내는 사용자 번호(문자열). JWT의 sub(subject)에 들어간다.
    //  - jti   : "이 토큰의 고유 ID". 1회성 보장을 위해 캐시(Caffeine)에서 관리할 때 사용된다.
    //
    // 반환:
    //  - 완성된 JWT 문자열(예: "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...").
    //
    // 동작:
    //  1) 현재 시각을 구한다 (Instant.now()).
    //  2) Jwts.builder()로 builder를 시작한다.
    //  3) .id(jti)       : jti 클레임(고유 ID) 설정.
    //  4) .subject(...)  : sub 클레임(누구) 설정.
    //  5) .expiration(...) : exp 클레임(만료 시각) 설정. 현재 시각 + expMin(분).
    //  6) .signWith(key(), Jwts.SIG.HS256) : 이 payload를 비밀키로 HS256 알고리즘으로 서명. (위조 방지)
    //  7) .compact()     : header.payload.signature 의 최종 JWT 문자열 생성.
    // -------------------------------------------------------------------------------------------------
    public String createToken(String userNo, String jti) {

        Instant now = Instant.now(); // 현재 시각

        return Jwts.builder()
                .id(jti)         // jti: 토큰 고유 식별자
                .subject(userNo) // subject : 사용자 번호
                .expiration(Date.from(now.plusSeconds(expMin * 60))) // exp: 만료 시간 15분.
                .signWith(key(), Jwts.SIG.HS256 )    // KEY 값과 해당 키에 HS256 알고리즘을 쓰겠다는 거임.
                .compact(); // 최종 문자열 생성
    }   // func end

    // -------------------------------------------------------------------------------------------------
    // [public Jws<Claims> parseAndValidate(String token)]
    // - 목적: 들어온 JWT 문자열이 "우리 키로 서명된, 아직 만료되지 않은" 정상 토큰인지 검증한다.
    //
    // 파라미터:
    //  - token: 브라우저가 이메일 링크로 전달한 JWT 문자열.
    //
    // 반환:
    //  - Jws<Claims>: 검증에 성공하면 "payload(Claims)를 꺼낼 수 있는" 래퍼 객체를 반환한다.
    //                 → 이걸 통해 c.getId()(=jti), c.getSubject()(=sub), c.getExpiration()(=exp) 등에 접근 가능.
    //
    // 실패 시:
    //  - JJWT가 JwtException(서명 불일치, 만료, 형식 오류 등)을 던진다.
    //
    // 동작:
    //  1) Jwts.parser()로 파서 빌더를 연다.
    //  2) .verifyWith(key()) : 서명 검증에 사용할 키 지정(우리가 발급한 키와 일치해야 함).
    //  3) .build()           : 파서 완성.
    //  4) .parseSignedClaims(token) : 파싱 + 서명/만료 검증 한꺼번에 수행. 성공 시 Jws<Claims> 반환.
    // -------------------------------------------------------------------------------------------------

    public Jws<Claims> parseAndValidate(String token) {
        // Jwts.parser(): Jwts parse 빌더 시작
        return Jwts.parser()
                .verifyWith(key())  // verifyWith: "이 키로 서명됐는지" 확인하도록 파서에 알려줌.
                .build()            // .build(): 파서 완성
                .parseSignedClaims(token);  // parseSignedClaims: 실제 파싱+검증. 실패하면 예외, 성공하면 Jws<Claims> 반환.

    }   // func end


}   // class end
