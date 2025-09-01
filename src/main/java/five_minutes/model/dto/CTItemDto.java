package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTItemDto {
    // CheckTemplateItem
    // DB 멤버변수
    private int ctiNo;
    private String ctiTitle;
    private String ctiHelpText;
    private int ctiStatus;
    private String createDate;
    private String updateDate;
    private int ctNo;

    // DB 없는 멤버변수(외래키의 외래키)
    private String bnNo;
    private String status; // 세션 실패, 쿼리스트링 실패를 체크할 수 있도록 하는 멤버변수
} // class end
