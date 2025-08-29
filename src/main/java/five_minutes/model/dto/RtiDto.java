package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

// Info =========================
// RoleTemplateItem DTO
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// 작성자 : OngTK

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class RtiDto {

    // 멤버변수
    private int rtiNo;                  // 상세 역할 템플릿 번호
    private String rtiName;             // 상세 역할 템플릿 명
    private String rtiDescription;      // 상세 역할 템플릿 설명
    private int rtiStatus;              // 상태 : 0 - 비활성화 / 1 - 활성화
    private String createDate;          // 생성일
    private String updateDate;          // 수정일
    private int rtNo;                   // 역할 템플릿 번호 (FK-RoleTemplate table)

}   // class end
