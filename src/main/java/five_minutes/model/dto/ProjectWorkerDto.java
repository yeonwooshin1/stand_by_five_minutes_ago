package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectWorkerDto {

    // 멤버변수
    private int pjRoleNo;               // 역할번호
    private String pjRoleName;          // 역할명
    private String pjRoleDescription;   // 역할설명
    private int userNo;                 // 근무자_FK(Users)
    private int pjNo;                   // 프로젝트 번호_FK(Project)
    private int pjRoleLv;               // 숙련도 (1 매우우수 ~ 5 신입 )
    private int pjRoleStatus;           // 프로젝트 상태 : 1 활성화, 0 비활성화
    private String createDate;          // 생성일
    private String updateDate;          // 수정일

    // 추가
    private int changeStatus; // JS에서 변경 여부 정보를 받아올 변수

} // class end
