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
    } // func end

    // [2] 클라이언트 소켓 - 서버 소켓 연동 종료 이벤트
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {

        System.out.println("웹소켓 연결 종료: " + session.getId());

        String roomNo = getRoomNo(session);
        List<WebSocketSession> sessions = roomSessionMap.get(roomNo);
        if (sessions != null) {
            sessions.remove(session);
        }
    } // func end

    // [3] 클라이언트 소켓 - 서버 소켓 메시지 인입 이벤트
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {

        String payload = message.getPayload();

        try {
            // JSON 파싱
            ObjectMapper objectMapper = new ObjectMapper();
            ChatMessageDto chatMessage = objectMapper.readValue(payload, ChatMessageDto.class);

            System.out.println("메시지 수신: " + chatMessage.getMessage());

            // DB에 메세지 저장
            chatMessageService.saveMessage(chatMessage);

            // 해당 채팅방의 모든 세션에 메시지 전송
            String roomNo = String.valueOf(chatMessage.getRoomNO());
            for (WebSocketSession ws : roomSessionMap.getOrDefault(roomNo, new ArrayList<>())) {
                if (ws.isOpen()) {
                    ws.sendMessage(new TextMessage(payload)); // 그대로 브로드캐스트
                }
            }
        } catch (Exception e) {
            System.err.println("JSON 파싱 오류: " + e.getMessage());
            session.close(CloseStatus.BAD_DATA); // 잘못된 데이터로 세션 종료
        }
    } // func end

    // [4] session IO 여부에 따른 메세지 전달
    public void alarmMessage(String roomNo, String message) throws Exception {
        List<WebSocketSession> sessions = roomSessionMap.getOrDefault(roomNo, new ArrayList<>());
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage("[알림] " + message));
            }
        }
    } // func end


    // [유틸] roomNo 추출 (예: ws://localhost:8080/chat?roomNo=11000001)
    private String getRoomNo(WebSocketSession session) {
        // 클라이언트가 WebSocket을 연결할 때 사용한 URI를 문자열로 가져옴
        String uri = session.getUri().toString();
        // usi에 "roomNo=" 포함여부를 확인
        if (uri.contains("roomNo=")) {
            // roomNo= 다음부터 끝까지 잘라서 roomNo 값을 추출
            return uri.substring(uri.indexOf("roomNo=") + 7);
        }
        return "default";
    } // func end

} // func end
