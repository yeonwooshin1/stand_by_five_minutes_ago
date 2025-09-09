package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
public class ChkItemLookupDto {     // class start

    private int pjChkItemNo;           // -- 체크리스트 아이템 번호 PK
    private String pjChklTitle;        // -- 체크리스트 제목
    private String pjHelpText;

}   // class end
