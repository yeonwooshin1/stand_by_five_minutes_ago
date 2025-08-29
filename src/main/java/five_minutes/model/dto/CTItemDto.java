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
} // class end
