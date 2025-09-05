package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// **Info** =========================
///
/// project dashboard Dto
///
/// 프로젝트별 업무를 상세히 확인할 수 있는 대시보드를 구현합니다.
///
/// Google sheet > 21.테이블 > 1-1, 3 , 4
///
/// @author dongjin

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardDto {
    // 멤버변수(없음)
    // 1-1 사용자
    private UsersDto usersDto;
    // 3-1 프로젝트 정보
    private PjDto pjDto;
    // 3-2 프로젝트 인력
    // 완성되면 넣기
    // 3-3 프로젝트 체크리스트
    private PjCheckDto pjCheckDto;
    // 4-1 프로젝트 실행
    private ProjectPerformDto pjPerDto;
    // 4-2 프로젝트 파일
    private ProjectPerformFileDto pjFileDto;

} // class end
