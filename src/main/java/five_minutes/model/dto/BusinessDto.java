package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class BusinessDto { // class start
    private String bnNo;                // 사업자등록번호
    private String bnName;              // 기업명
    private String managerName;         // 담당자명
    private String managerPhone;        // 담당자번호
    private MultipartFile uploadBnImg ;   // 사업자이미지 업로드용
    private String bnDocuImg;       // 사업자이미지 조회용
    private String bnType;              // 업태
    private String bnItem;              // 종목
    private int userNo;                 // 사용자번호
    private String createDate;          // 기업등록일
    private String updateDate;          // 기업정보수정일
    private String bnStatus;            // 상태

}   // class end
