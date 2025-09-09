package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> info =======================
 * <p> Chat방에 들어온 user을 관리
 * @author OngTK
 * @since 20250909
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatRoomUserDto {

    private int roomNo;
    private int userNo;
    private String joinDate;

}
