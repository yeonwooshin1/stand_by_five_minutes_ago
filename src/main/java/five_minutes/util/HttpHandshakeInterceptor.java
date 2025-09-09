package five_minutes.util;


import jakarta.servlet.http.HttpSession;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

/**
 * 채팅과정에서 userNo 정보확인이 필요
 * WebSocketSession 에서 HttpSession 에 삽입되어있는 "loginUserNo" 을 복사해오기 위함
 *
 * @author OngTK
 * @since 2025.09.09
 */
public class HttpHandshakeInterceptor implements HandshakeInterceptor {
    /**
     * 목적: WebSocket 연결 전에 기존 HTTP 세션에서 로그인 사용자 정보를 가져와 WebSocketSession에 전달.
     * 사용 이유: WebSocket은 stateless이므로, 기존 로그인 정보를 세션에서 복사해 WebSocket 핸들러에서 사용할 수 있도록 함.
     * attributes: WebSocketSession에 전달되는 Map 객체로, 이후 메시지 처리 시 사용자 정보를 식별하는 데 사용됨.
     * */

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {

        // ServerHttpRequest가 Servlet 기반의 요청인지 확인
        // WebSocket 요청이 HTTP 기반일 경우 ServletServerHttpRequest로 캐스팅 가능
        if (request instanceof ServletServerHttpRequest servletRequest) {

            // 기존 HTTP 세션을 가져옴 (false: 세션이 없으면 null 반환)
            HttpSession httpSession = servletRequest.getServletRequest().getSession(false);

            // 세션이 존재할 경우
            if (httpSession != null) {
                // 세션에서 "loginUserNo" 속성을 가져와 WebSocketSession의 attributes에 저장
                // 이후 WebSocket 핸들러에서 해당 정보를 활용 가능
                attributes.put("loginUserNo", httpSession.getAttribute("loginUserNo"));
                System.out.println("HttpHandshakeInterceptor.beforeHandshake 세션 확인 : "+ httpSession.getAttribute("loginUserNo"));
            }
        }
        // 핸드셰이크를 계속 진행하도록 true 반환
        return true;
    } // func end

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Exception exception) {
        // 생략
    }
}
