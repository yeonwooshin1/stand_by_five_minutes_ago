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
    // [*] Dto에서 미리 초기화시켜놓으면 Dao에서 바로 불러와 쓸 수 있다.
    // 1-1 사용자
    private UsersDto usersDto = new UsersDto();
    // 3-1 프로젝트 정보
    private PjDto pjDto = new PjDto();
    // 3-2 프로젝트 인력
    private ProjectWorkerDto pjWorkerDto = new ProjectWorkerDto();
    // 3-3 프로젝트 체크리스트
    private PjCheckDto pjCheckDto = new PjCheckDto();
    // 4-1 프로젝트 실행
    private ProjectPerformDto pjPerDto = new ProjectPerformDto();
    // 4-2 프로젝트 파일
    private ProjectPerformFileDto pjFileDto = new ProjectPerformFileDto();
    

} // class end
