package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTemDto {
    // CheckTemplate
    // DB 멤버변수
    private int ctNo;
    private String ctName;
    private String ctDescription;
    private int ctStatus;
    private String createDate;
    private String updateDate;
    private String bnNo;

    // DB 없는 멤버변수
    private String status;  // 세션 실패, 쿼리스트링 실패를 체크할 수 있도록 하는 멤버변수

} // class end
