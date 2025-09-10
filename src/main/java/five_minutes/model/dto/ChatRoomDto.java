package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

/**
 * <p> info =======================
 * <p> Chat 방 정보
 *
 * @author OngTK
 * @since 20250909
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoomDto {

    private int roomNo;
    private String roomName;
    private int creatorUserNo;
    private boolean isGroup;
    private String createdDate;
    private String updateDate;

    // 추가 정보
    // 채팅방 참가자 List
    private List<Map<Integer, String>> participant; // userNo, userName

}
