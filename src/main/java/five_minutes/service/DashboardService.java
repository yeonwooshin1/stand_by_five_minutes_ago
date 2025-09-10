package five_minutes.service;

import five_minutes.model.dao.DashboardDao;
import five_minutes.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final FileService fileService;

    // performInfo 안의 pfNo 가져오기
    // Long pfNo = response.getPerformInfo().getPfNo();
    // performInfo 안의 시작시간 가져오기
    // LocalTime startTime = response.getPerformInfo().getStartTime();

    // [*] 사용자 확인
    public boolean checkUser(int userNo) {
        return dashboardDao.checkUser(userNo);
    }

    // [*] pjNo 확인
    public boolean checkBusiness(String bnNo) {
        return dashboardDao.checkBusiness(bnNo);
    }

    // [*] pjNo 확인
    public boolean checkPjNo(int pjNo) {
        return dashboardDao.checkPjNo(pjNo);
    }

    // [1] 프로젝트 대시보드  - 기본정보 조회
    /*
        * 로직 안내
        1. bnNo를 확인한다(로그인 세션)
        2. pjNo를 확인한다(쿼리스트링)
        3. pjNo가 일치하는 ProjectInfo 레코드를 가져온다
     */
    public DashboardDto getInfoPJDash(int pjNo, Integer userNo) {
        if (userNo != null && dashboardDao.checkUser(userNo)) {
            return dashboardDao.getInfoPJDash(pjNo);
        }
        return new DashboardDto();
    }

    // [2] 프로젝트 대시보드  - 근무리스트 전체 조회
    /*
        pjNo가 일치하는 pjPerform 테이블의 모든 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * (관리자/근무자 체크) loginUserNo를 확인하여, null이 아닐 경우 모든 레코드 조회. null일 경우 해당 userNo를 pjRoleNo로 inner  join할 수 있는 레코드만 조회.
     */
    public List<DashboardDto> getListPJDash(int pjNo, Integer userNo, String bnNo) {
        // 관리자 확인
        if (bnNo != null && dashboardDao.checkBusiness(bnNo)) {
            return dashboardDao.getListPJDash(pjNo);
        }
        // 사용자 확인
        if (userNo != null && dashboardDao.checkUser(userNo)) {
            return dashboardDao.getListPJDashForUser(pjNo, userNo);
        }
        return new ArrayList<>();
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
    public DashboardDto getIndiListPJDash(int pjNo, int pfNo, Integer userNo, String bnNo) {
        // 관리자 확인
        if (bnNo != null && dashboardDao.checkBusiness(bnNo)) {
            return dashboardDao.getIndiListPJDash(pjNo, pfNo);
        }
        // 사용자 확인
        if (userNo != null && dashboardDao.checkUser(userNo)) {
            return dashboardDao.getIndiListPJDashForUser(pjNo, pfNo, userNo);
        }
        return new DashboardDto();
    }


    // [4] 프로젝트 대시보드 - 파일 업로드
    /*
        pfNo, fileName을 입력받아 pjPerformFile 테이블에 저장한다.
        * pjNo, userNo를 확인하기
     */
    public int uploadFilePJDash(MultipartFile file, int pfNo, Integer userNo) {
        try {
            // 1. 파일명 리턴
            String checkFile = fileService.fileUpload(2, file);

            // 2. DTO 구성
            DashboardDto dto = new DashboardDto();
            ProjectPerformFileDto fileDto = new ProjectPerformFileDto();
            fileDto.setPfNo(pfNo);
            fileDto.setFileName(checkFile); // uuid 붙은 파일명이 이렇게 됨
            dto.setPjFileDto(fileDto);

            // 3. 사용자 확인 후 DB 저장
            if (userNo != null && dashboardDao.checkUser(userNo)) {
                return dashboardDao.uploadFilePJDash(dto);
            }

        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    // [5] 프로젝트 대시보드 - 업로드 파일 삭제
    /*
        fileNo를 입력받아 일치하는 pjPerformFile 테이블 레코드를 삭제한다.
        * pjNo, userNo를 확인하기
     */
    public int deleteFilePJDash(int fileNo, Integer userNo) {
        try {
            // 1. 사용자 확인 후 DB 저장
            if (userNo != null && dashboardDao.checkUser(userNo)) {
                return dashboardDao.deleteFilePJDash(fileNo);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }

    // [6] 프로젝트 대시보드 - 근무 정보 메모 수정
    /*
        pjPerform 테이블의 pfNo, pfStatus, note를 입력받아 수정한다.
        * pjNo, bnNo를 확인하기
     */
    public int updateNotePJDash(Integer pfNo, int userNo, DashboardDto dashboardDto) {
        // 1. 사용자 확인 후 DB 저장
        if (userNo > 0 && pfNo != null ) {
            return dashboardDao.updateNotePJDash(dashboardDto);
        }
        System.out.println("userNo = " + userNo);

        // 2. 없으면 리턴
        return 0;
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
    public UsersDto getUserInDash(int pjNo, int userNo) {
        return dashboardDao.getUserInDash(pjNo, userNo);
    }

    // [8] 프로젝트 대시보드 - 역할명 상세 정보
    /*
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 역할명 텍스트를 클릭할 시 화면에 띄우기
     */
    public ProjectWorkerDto getWorkerInDash(int pjNo, int pjRoleNo) {
        return dashboardDao.getWorkerInDash(pjNo, pjRoleNo);
    }

    // [9] 프로젝트 대시보드 - 체크리스트 상세 정보
    /*
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 체크리스트명 텍스트를 클릭할 시 화면에 띄우기
     */
    public PjCheckDto getCheckInDash(int pjNo, int pjChkItemNo) {
        return dashboardDao.getCheckInDash(pjNo, pjChkItemNo);
    }

    // [10] 프로젝트 대시보드 - PDF 생성
    public List<DashboardDto> getPersonalPerformancesSorted(int pjNo, int userNo) {
        // 매퍼를 호출하여 userNo에 해당하는 사용자의 pjPerform 레코드를 시간순으로 정렬하여 가져옵니다.
        // pjWorker와 Users 테이블을 조인하여 userNo에 해당하는 pjRoleNo를 찾아야 합니다.
        return dashboardDao.getPersonalPerformancesSorted(pjNo, userNo);
    }


} // class end
