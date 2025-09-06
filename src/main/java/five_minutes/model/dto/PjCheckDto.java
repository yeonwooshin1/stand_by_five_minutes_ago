package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// **Info** =========================
///
/// pjChecklistItem Dto
///
/// 프로젝트 체크리스트 업무를 상세하게 정의하고 미리 만들어둔 체크리스트 템플릿을 사용한다
///
/// Google sheet > 21.테이블 > 3-4
///
/// @author dongjin

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PjCheckDto {
    // DB 테이블
    private int pjChkItemNo;
    private int pjNo;
    private String pjChklTitle;
    private String pjHelpText;
    private int pjChkIStatus;
    private String createDate;
    private String updateDate;

    // DB 테이블 외
    // [*] 세션 확인용
    private String bnNo;
    // [1] 체크리스트 템플릿
    private int ctNo;
    private String ctName;
    private String ctDescription;
    // [2] 체크리스트 템플릿 아이템
    private int ctiNo;
    private String ctiTitle;
    private String ctiHelpText;

} // class end
