package five_minutes.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import five_minutes.model.dao.ChatMessageDao;
import five_minutes.model.dto.ChatMessageDto;
import five_minutes.service.ChatMessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.*;

/**
 * **info** =========================
 * <p>TextWebSocketHandler를 상속받아 클라이언트 소켓과 서버 소켓 사이의 이벤트를 관리
 * @author OngTK
 * @since 2025.09.09
 */
@Component
@RequiredArgsConstructor
public class ChatSocketHandler extends TextWebSocketHandler {

    // ※ ObjectMapper : JSON <-> Map 변환 라이브러리
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ChatMessageService chatMessageService;

    // 채팅방별 세션 저장소 (roomNo → session 목록)
    private final Map<String, List<WebSocketSession>> roomSessionMap = new HashMap<>();

    // [1] 클라이언트 소켓 - 서버 소켓 연동 시작 이벤트
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("웹소켓 연결: " + session.getId());
        String roomNo = getRoomNo(session);
        roomSessionMap.putIfAbsent(roomNo, new ArrayList<>());
        roomSessionMap.get(roomNo).add(session);
        System.out.println(roomSessionMap);
    } // func end

    // [2] 클라이언트 소켓 - 서버 소켓 연동 종료 이벤트
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println("웹소켓 연결 종료: " + session.getId());

        // 접속 session에서 roomNo 확인
        String roomNo = getRoomNo(session);
        
        List<WebSocketSession> sessions = roomSessionMap.get(roomNo);
        if (sessions != null) {
            sessions.remove(session);
        }
    } // func end

    // [3] 클라이언트 소켓 - 서버 소켓 메시지 인입 이벤트
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        System.out.println("ChatSocketHandler.handleTextMessage");
        System.out.println("session = " + session + ", message = " + message);
        String payload = message.getPayload();
        System.out.println("payload = " + payload);;


        try {
            // JSON 파싱 : Json형식을 Map 타입으로변환
            ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);
            System.out.println("메시지 수신: " + chatMessage.getMessage());

            // DB에 메세지 저장
            chatMessageService.saveMessage(chatMessage);

            // 해당 채팅방의 모든 세션에 메시지 전송
            String roomNo = String.valueOf(chatMessage.getRoomNo());
            System.out.println("roomNo = " + roomNo);

            System.out.println("브로드캐스트 대상 세션 수: " + roomSessionMap.get(roomNo).size());

            for (WebSocketSession ws : roomSessionMap.getOrDefault(roomNo, new ArrayList<>())) {
                System.out.println("line 77 "+ ws);
                if (ws.isOpen()) {
                    System.out.println("line 79 "+payload);
                    ws.sendMessage(new TextMessage(payload)); // 그대로 브로드캐스트
                }
            }
        } catch (Exception e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
            session.close(CloseStatus.BAD_DATA); // 잘못된 데이터로 세션 종료
        }
    } // func end

    // [유틸] roomNo 추출 (예: ws://localhost:8080/chat?roomNo=11000001)
    private String getRoomNo(WebSocketSession session) {
        // 클라이언트가 WebSocket을 연결할 때 사용한 URI를 문자열로 가져옴
        String uri = session.getUri().toString();
        // usi에 "roomNo=" 포함여부를 확인
        if (uri.contains("roomNo=")) {
            String query = uri.substring(uri.indexOf("?") + 1);
            String[] params = query.split("&");
            // userNo를 가져오지 않기 위해 이중으로 자름
            for (String param : params) {
                if (param.startsWith("roomNo=")) {
                    return param.substring(7);
                }
            }
        }

        return "default";
    } // func end

} // func end
