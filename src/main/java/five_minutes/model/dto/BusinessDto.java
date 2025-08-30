package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class BusinessDto { // class start

    private String bnNo;
    private String bnName;
    private String managerName;
    private String managerPhone;
    private String bnDocuImg ;
    private String bnType;
    private String bnItem;
    private int userNo;
    private String createDate;
    private String updateDate;
    private String bnStatus;

}   // class end
