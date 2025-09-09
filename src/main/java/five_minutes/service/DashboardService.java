package five_minutes.service;

import five_minutes.model.dao.DashboardDao;
import five_minutes.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// **Info** =========================
///
/// project dashboard Service
///
/// 프로젝트별 업무를 상세히 확인할 수 있는 대시보드를 구현합니다.
///
/// Google sheet > 21.테이블 > 1-1, 3 , 4
///
/// @author dongjin

@Service
@RequiredArgsConstructor
public class DashboardService {

    // DI
    private final DashboardDao dashboardDao;

    // performInfo 안의 pfNo 가져오기
    // Long pfNo = response.getPerformInfo().getPfNo();
    // performInfo 안의 시작시간 가져오기
    // LocalTime startTime = response.getPerformInfo().getStartTime();

    // [1] 프로젝트 대시보드  - 기본정보 조회
    /*
        * 로직 안내
        1. bnNo를 확인한다(로그인 세션)
        2. pjNo를 확인한다(쿼리스트링)
        3. pjNo가 일치하는 ProjectInfo 레코드를 가져온다
     */
    public PjDto getInfoPJDash (int pjNo) {
        return dashboardDao.getInfoPJDash(pjNo);
    }

    // [2] 프로젝트 대시보드  - 근무리스트 전체 조회
    /*
        pjNo가 일치하는 pjPerform 테이블의 모든 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * (관리자/근무자 체크) loginUserNo를 확인하여, null이 아닐 경우 모든 레코드 조회. null일 경우 해당 userNo를 pjRoleNo로 inner  join할 수 있는 레코드만 조회.
     */
    public List<ProjectPerformDto> getListPJDash (int pjNo){
        return dashboardDao.getListPJDash(pjNo);
    }

    // [3] 프로젝트 대시보드  - 근무리스트 개별 조회
    /*
        pjNo와 pfNo를 확인하여 pjPerform 테이블에서 일치하는 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * 이동시간 측정 메소드 서비스단에 추가.
        사용자와 프로젝트의 위치 정보 기반으로 대중교통, 도보, 차량 이용 시 소요 시간과 출발 시간을 출력해주고, 시간 데이터로 크롬 푸시 해주기
     */
    public ProjectPerformDto getIndiListPJDash(int pjNo , int pfNo){
        return dashboardDao.getIndiListPJDash(pjNo, pfNo);
    }

    // [4] 프로젝트 대시보드 - 파일 업로드
    /*
        pfNo, fileName을 입력받아 pjPerformFile 테이블에 저장한다.
        * pjNo, bnNo를 확인하기
     */
    public int uploadFilePJDash(ProjectPerformFileDto projectPerformFileDto){
        return dashboardDao.uploadFilePJDash(projectPerformFileDto);
    }

    // [5] 프로젝트 대시보드 - 업로드 파일 삭제
    /*
        fileNo를 입력받아 일치하는 pjPerformFile 테이블 레코드를 삭제한다.
        * pjNo, bnNo를 확인하기
     */
    public int deleteFilePJDash(int fileNo) {
        return dashboardDao.deleteFilePJDash(fileNo);
    }

    // [6] 프로젝트 대시보드 - 근무 정보 메모 수정
    /*
        pjPerform 테이블의 pfNo, pfStatus, note를 입력받아 수정한다.
        * pjNo, bnNo를 확인하기
     */
    public int updateNotePJDash (ProjectPerformDto projectPerformDto){
        return dashboardDao.updateNotePJDash(projectPerformDto);
    }

    // [7] 프로젝트 대시보드 - 근무자 상세 정보
    /*
        1. pjPerform의 pjRoleNo를 확인하여, pjWorker 테이블에서 일치하는 레코드를 확인한다.
        2. 일치하는 레코드의 userNo를 확인하여, Users 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        1. pjPerform의 pjRoleNo를 확인하여, pjWorker 테이블에서 일치하는 레코드를 확인한다.
        2. 일치하는 레코드의 userNo를 확인하여, Users 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 근무자 텍스트를 클릭할 시 화면에 띄우기
     */
    public UsersDto getUserInDash (int pjNo , int userNo){
        return dashboardDao.getUserInDash(pjNo, userNo);
    }

    // [8] 프로젝트 대시보드 - 역할명 상세 정보
    /*
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 역할명 텍스트를 클릭할 시 화면에 띄우기
     */
    public ProjectWorkerDto getWorkerInDash (int pjNo , int pjRoleNo){
        return dashboardDao.getWorkerInDash(pjNo, pjRoleNo);
    }

    // [9] 프로젝트 대시보드 - 체크리스트 상세 정보
    /*
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 체크리스트명 텍스트를 클릭할 시 화면에 띄우기
     */
    public PjCheckDto getCheckInDash (int pjNo , int pjChkItemNo){
        return dashboardDao.getCheckInDash(pjNo, pjChkItemNo);
    }

}
