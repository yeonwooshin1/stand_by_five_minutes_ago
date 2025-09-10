package five_minutes.service;

import five_minutes.model.dao.ChatMessageDao;
import five_minutes.model.dto.ChatMessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p> info =======================
 * <p> Chat 메세지 정보
 * @author OngTK
 * @since 20250909
 */

@Service
@RequiredArgsConstructor
public class ChatMessageService {

    private final ChatMessageDao chatMessageDao;

    // [chat-01] 특정 채팅방의 메세지 목록 조회
    public List<ChatMessageDto> getMessagesByRoomNo(int roomNo) {
        return chatMessageDao.selectMessagesByRoomNo(roomNo);
    } // func end

    // [chat] handler > 메세지 저장
    public void saveMessage(ChatMessageDto dto) {
        chatMessageDao.insertChatMessage(dto);
    }


}
