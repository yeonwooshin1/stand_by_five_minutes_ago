package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data @AllArgsConstructor @NoArgsConstructor
public class ScheduledDto { // class start

    /* ===== 메일 받는 유저 정보 ===== */
    /** 사용자 이름 (Users.userName) */
    private String userName;
    /** 사용자 이메일 (Users.email) */
    private String userEmail;

    /* ===== 메일 주는 회사 정보 ===== */
    /** 회사명 (BusinessUser.bnName) */
    private String bnName;
    /** 회사 담당자명 (BusinessUser.managerName) */
    private String managerName;
    /** 회사 연락처 (BusinessUser.managerPhone) */
    private String managerPhone;

    /* ===== 프로젝트 정보 ===== */
    /** 프로젝트명 (ProjectInfo.pjName) */
    private String pjName;
    /** 도로명 주소 (ProjectInfo.roadAddress) */
    private String projectRoadAddress;
    /** 상세 주소 (ProjectInfo.detailAddress) */
    private String projectDetailAddress;
    /** 프로젝트 시작일  */
    private String pjStartDate;
    /** 프로젝트 종료일 */
    private String pjEndDate;

    /* ===== 역할 정보(프로젝트 멤버 스냅샷) ===== */
    /** 역할 이름 (pjWorker.pjRoleName) */
    private String roleName;
    /** 역할 설명 (pjWorker.pjRoleDescription) */
    private String roleDescription;
    /** 역할 레벨 1~5 (pjWorker.pjRoleLv) */
    private int roleLevel;

    /* ===== 할 일(체크 항목 사본) ===== */
    /** 체크리스트 제목 (PjChecklistItem.pjChklTitle) */
    private String todoTitle;
    /** 체크리스트 도움말 (PjChecklistItem.pjHelpText) */
    private String todoHelpText;

    /* ===== 스케줄(메일 발송 판단용) ===== */
    /** 알림 타입 0~4 (pjPerform.notifyType) */
    private int notifyType;
    /** 알림 설정 분 (pjPerform.notifySetMins) */
    private int notifySetMins;
    /** 시작 시각 TIME (pjPerform.pfStart) */
    private LocalDateTime pfStart;
    /** 종료 시각 TIME (pjPerform.pfEnd) */
    private LocalDateTime pfEnd;
    /** 진행 상태 1~5 (pjPerform.pfStatus) */
    private int pfStatus;

    /* ==== 메일 발송 후 상태 변환에 필요한 pfNo === */
    private int pfNo;

}   // class end
