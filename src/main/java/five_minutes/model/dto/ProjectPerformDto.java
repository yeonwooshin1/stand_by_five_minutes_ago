package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
@Builder        // builder pattern 삽입
// 프로젝트 실행 관련 테이블
public class ProjectPerformDto {    // class start

    private int pfNo;          // PK (기존 데이터면 존재, 신규는 없음)
    private int pjRoleNo;      // FK → 프로젝트에서의 역할
    private int pjChkItemNo;   // FK → 프로젝트 상세 체크
    private String pfStart;     // 시작시간 "HH:mm"
    private String pfEnd;       // 종료시간 "HH:mm"
    private int notifyType; // 알림 타입 (1=시작전, 2=시작후 ...)
    private int notifySetMins; // 알림 분 수 (예: 5분 전)
    private int pfStatus;       // 진행 상태

    // 추가 변수_@author OngTK
    private String note;        // 메모
    private String createDate;  // 작성일
    private String updateDate;  // 수정일
    private String pjRoleName;  // 역할명
    private String userName; // 근무자 이름_엑셀출력용

}   // class end
