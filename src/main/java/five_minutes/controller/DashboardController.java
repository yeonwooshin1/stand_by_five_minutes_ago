package five_minutes.controller;

import five_minutes.model.dto.*;
import five_minutes.service.DashboardService;
import five_minutes.service.FileService;
import five_minutes.service.PjService;
import five_minutes.util.PdfGeneratorUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

/// **Info** =========================
///
/// project dashboard Controller
///
/// 프로젝트별 업무를 상세히 확인할 수 있는 대시보드를 구현합니다.
///
/// Google sheet > 21.테이블 > 1-1, 3 , 4
///
/// @author dongjin

@RestController
@RequestMapping("/project/perform/check")
@RequiredArgsConstructor
public class DashboardController {

    // DI
    private final DashboardService dashboardService;
    private final PdfGeneratorUtil pdfGeneratorUtil;

    // [1] 프로젝트 대시보드  - 기본정보 조회
    // URL : /project/perform/check?pjNo=6000001
    /*
        * 로직 안내
        1. bnNo를 확인한다(로그인 세션)
        2. pjNo를 확인한다(쿼리스트링)
        3. pjNo가 일치하는 ProjectInfo 레코드를 가져온다
     */
    @GetMapping("")
    public DashboardDto getInfoPJDash (@RequestParam int pjNo , HttpSession session) {
        // 1. 세션 확인
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (bnNo == null){
            return null;
        }
        // 3. 리턴
        return dashboardService.getInfoPJDash(pjNo , bnNo);
    }

    // [2] 프로젝트 대시보드  - 근무리스트 전체 조회
    // URL : /project/perform/check/list?pjNo=6000001
    /*
        pjNo가 일치하는 pjPerform 테이블의 모든 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * (관리자/근무자 체크) loginUserNo를 확인하여, null이 아닐 경우 모든 레코드 조회. null일 경우 해당 userNo를 pjRoleNo로 inner  join할 수 있는 레코드만 조회.
     */
    @GetMapping("/list")
    public List<DashboardDto> getListPJDash (@RequestParam int pjNo , HttpSession session){
        // 1. 세션 확인
        Integer userNo = (Integer)  session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (userNo == null && bnNo == null){
            return null;
        }
        return dashboardService.getListPJDash(pjNo, userNo, bnNo);
    }

    // [3] 프로젝트 대시보드  - 근무리스트 개별 조회
    // URL : /project/perform/check/indi?pjNo=6000001&pfNo=9000001
    /*
        pjNo와 pfNo를 확인하여 pjPerform 테이블에서 일치하는 레코드를 조회한다.
        * 프론트 표현 위한 DAO
        1. pjRoleNo를 pjWorker 테이블과 inner join하여 같은 pjRoleNo를 쓰는 레코드의 pjRoleName를 찾는다.(역할No를 역할명으로 표시)
        2. pjChkItemNo를 pjChecklistItem 테이블과 inner join하여 같은 pjChkItemNo를 쓰는 레코드의 pjChklTitle를 찾는다.(체크리스트No를 체크리스트이름으로 표시)
        * 이동시간 측정 메소드 서비스단에 추가.
        사용자와 프로젝트의 위치 정보 기반으로 대중교통, 도보, 차량 이용 시 소요 시간과 출발 시간을 출력해주고, 시간 데이터로 크롬 푸시 해주기
     */
    @GetMapping("/indi")
    public DashboardDto getIndiListPJDash(@RequestParam int pjNo , @RequestParam int pfNo , HttpSession session ){
        Integer userNo = (Integer)  session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if(userNo == null && bnNo == null){
            return null;
        }
        return dashboardService.getIndiListPJDash(pjNo, pfNo , userNo, bnNo);
    }

    // [4] 프로젝트 대시보드 - 파일 업로드
    // URL : /project/perform/check/file?pfNo=9000002
    // Headers : multipart/form-data
    /*
        pfNo, fileName을 입력받아 pjPerformFile 테이블에 저장한다.
        * pjNo, bnNo를 확인하기
     */
    @PostMapping("/file")
    public int uploadFilePJDash(@RequestParam("file") MultipartFile file , @RequestParam("pfNo") int pfNo , HttpSession session){
        // 1. 세션 확인
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        if (userNo == null) {
            return -1;
        }

        // 2. 대시보드에 리턴
        return dashboardService.uploadFilePJDash(file, pfNo, userNo);
    }

    // [5] 프로젝트 대시보드 - 업로드 파일 삭제
    // URL : /project/perform/check/file?fileNo=10000001
    /*
        fileNo를 입력받아 일치하는 pjPerformFile 테이블 레코드를 삭제한다.
        * pjNo, bnNo를 확인하기
     */
    @DeleteMapping("/file")
    public int deleteFilePJDash(@RequestParam int fileNo , HttpSession session) {
        // 1. 세션 확인
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        if (userNo == null) {
            return -1;
        }

        // 2. 대시보드에 리턴
        return dashboardService.deleteFilePJDash(fileNo, userNo);
    }

    // [6] 프로젝트 대시보드 - 근무 정보 메모 수정
    // URL : /project/perform/check
    // BODY :        {
    //          "pjPerDto": {
    //            "pfNo": 9000001,
    //            "pfStatus": 3,
    //            "note": "특이사항 없이 행사 완료."
    //          }
    //        }
    /*
        pjPerform 테이블의 pfNo, pfStatus, note를 입력받아 수정한다.
        * pjNo, bnNo를 확인하기
     */

    @PutMapping("")
    public int updateNotePJDash (@RequestBody DashboardDto dashboardDto, HttpSession session){
        // 1. 세션 확인
        int userNo = (int) session.getAttribute("loginUserNo");
        System.out.println("userNo = " + userNo);
        if (userNo <= 0){
            return -1;
        }
        return dashboardService.updateNotePJDash(dashboardDto.getPjPerDto().getPfNo(), userNo , dashboardDto);
    }

    // [7] 프로젝트 대시보드 - 근무자 상세 정보
    // URL : /project/perform/check/user?pjNo=6000001&userNo=1000001
    /*
        1. pjPerform의 pjRoleNo를 확인하여, pjWorker 테이블에서 일치하는 레코드를 확인한다.
        2. 일치하는 레코드의 userNo를 확인하여, Users 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        1. pjPerform의 pjRoleNo를 확인하여, pjWorker 테이블에서 일치하는 레코드를 확인한다.
        2. 일치하는 레코드의 userNo를 확인하여, Users 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 근무자 텍스트를 클릭할 시 화면에 띄우기
     */
    @GetMapping("/user")
    public UsersDto getUserInDash (int pjNo , int userNo){
        return dashboardService.getUserInDash(pjNo, userNo);
    }

    // [8] 프로젝트 대시보드 - 역할명 상세 정보
    // URL : /project/perform/check/role?pjNo=6000001&pjRoleNo=7000001
    /*
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjRoleNo를 확인하여 pjWorker 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 역할명 텍스트를 클릭할 시 화면에 띄우기
     */
    @GetMapping("/role")
    public ProjectWorkerDto getWorkerInDash (int pjNo , int pjRoleNo){
        return dashboardService.getWorkerInDash(pjNo, pjRoleNo);
    }

    // [9] 프로젝트 대시보드 - 체크리스트 상세 정보
    // URL : /project/perform/check/checklist?pjNo=6000001&pjChkItemNo=8000001
    /*
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 로직 안내
        pjPerform의 pjChkItemNo를 확인하여 pjChecklistItem 테이블에서 일치하는 레코드를 조회한다.
        * 프론트에서 체크리스트명 텍스트를 클릭할 시 화면에 띄우기
     */
    @GetMapping("/checklist")
    public PjCheckDto getCheckInDash (int pjNo , int pjChkItemNo){
        return dashboardService.getCheckInDash(pjNo, pjChkItemNo);
    }

    // [10] 프로젝트 대시보드 - 전체 근무 리스트 PDF 다운로드
    @GetMapping("/pdf/all")
    public void downloadAllPerPdf (@RequestParam int pjNo , HttpSession session , HttpServletResponse response) throws IOException {
        // 세션 확인
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (userNo == null && bnNo == null) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "로그인이 필요합니다.");
            return;
        }
    }


} // class end
