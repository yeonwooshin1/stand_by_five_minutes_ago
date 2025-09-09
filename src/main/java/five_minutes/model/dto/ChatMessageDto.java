package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p> info =======================
 * <p> Chat 메세지 정보
 * @author OngTK
 * @since 20250909
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ChatMessageDto {

    private int messageNo;
    private int roomNO;
    private int sendUserNo;
    private String message;
    private String sentDate;

}
