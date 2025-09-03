package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
public class PjDto {    // class start
    private int pjNo;               // 프로젝트번호
    private String pjName;          // 프로젝트명
    private String pjMemo;          // 당사 메모
    private String pjStartDate;     // 시작날짜
    private String pjEndDate;       // 종료날짜
    private String roadAddress;     // 도로명주소
    private String detailAddress;   // 상세 주소
    private String clientName;      // 클라이언트명
    private String clientRepresent; // 클라이언트 담당자
    private String clientPhone;     // 클라이언트연락처
    private String clientMemo;      // 업무요청사항
    private String pjStatus;        // 상태
    private String createDate;      // 생생일
    private String updateDate;      // 변경일
    private String bnNo;            // 담당자(회사정보)
}   // class end
