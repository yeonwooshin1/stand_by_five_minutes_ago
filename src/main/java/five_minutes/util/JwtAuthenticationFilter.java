package five_minutes.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/// **Info** =========================
///
/// JwtAuthenticationFilter
///
/// JWT 토큰을 검증하는 필터 클래스입니다.
///
/// Swagger 라이브러리 사용을 위한 토큰 발행용
///
/// @author dongjin

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    // JWT 토큰을 검증하는 필터 클래스입니다.

    // DI
    private final JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization"); // 헤더에서 토큰 추출

        if(header != null && header.startsWith("Bearer")) { // 토큰 유효성 검사
            String token = header.substring(7); // "Bearer " 글씨를 빼고 읽어줌
            try{
                Jws<Claims> claimsJws = jwtUtil.parseAndValidateApi(token);
                String userNo = claimsJws.getPayload().getSubject(); // sub 값
                request.setAttribute("loginUserNo" , Integer.parseInt(userNo));
            } catch (Exception e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return;
            }
        }
        filterChain.doFilter(request, response);
    }
} // class end
