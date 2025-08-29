package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Info =========================
// RoleTemplate DTO
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// 작성자 : OngTK

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class RtDto {

    // 멤버변수
    private int rtNo;               // 역할 템플릿 번호
    private String rtName;          // 역할 템플릿 명
    private String rtDescription;   // 역할 템플릿 설명
    private int rtStatus;           // 상태 : 0 - 비활성화 / 1 - 활성화
    private String createDate;      // 생성일
    private String updateDate;      // 수정일
    private String bnNo;            // 사업자등록번호 (FK-BusinessUser table)

}   // class end
