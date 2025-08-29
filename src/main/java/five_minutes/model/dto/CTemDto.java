package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTemDto {
    // DB 멤버변수
    private int ctNo;
    private String ctName;
    private String ctDescription;
    private int ctStatus;
    private String createDate;
    private String updateDate;
    private String bnNo;

    // DB 없는 멤버변수


} // class end
