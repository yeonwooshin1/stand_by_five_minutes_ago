package five_minutes.controller;

import com.lowagie.text.DocumentException;
import five_minutes.model.dto.*;
import five_minutes.service.DashboardService;
import five_minutes.service.FileService;
import five_minutes.service.PjService;
import five_minutes.service.ProjectWorkerService;
import five_minutes.util.PdfGeneratorUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private final PjService pjService;
    private final ProjectWorkerService projectWorkerService;

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
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        if (userNo == null){
            return null;
        }
        // 3. 리턴
        return dashboardService.getInfoPJDash(pjNo , userNo);
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

    // [10] 프로젝트 대시보드 - 체크리스트 PDF 다운로드
    // 전체 근무리스트
    @GetMapping("/pdf/all")
    public void downloadAllPerformancesPdf(@RequestParam int pjNo, HttpSession session, HttpServletResponse response) throws IOException {
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (userNo == null && bnNo == null) { response.sendError(HttpServletResponse.SC_UNAUTHORIZED); return; }

        List<DashboardDto> list = dashboardService.getListPJDash(pjNo, userNo, bnNo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"performances_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf\"");

        try {
            pdfGeneratorUtil.generateAllPerPdf(list, response.getOutputStream());
        } catch (DocumentException e) { throw new IOException(e.getMessage()); }
    }
    // 개인 체크리스트
//    @GetMapping("/pdf/single")
//    public void downloadSinglePerformancePdf(@RequestParam int pjNo, @RequestParam int pfNo, HttpSession session, HttpServletResponse response) throws IOException {
//        Integer userNo = (Integer) session.getAttribute("loginUserNo");
//        String bnNo = (String) session.getAttribute("loginBnNo");
//        if (userNo == null && bnNo == null) { response.sendError(HttpServletResponse.SC_UNAUTHORIZED); return; }
//
//        DashboardDto dto = dashboardService.getIndiListPJDash(pjNo, pfNo, userNo, bnNo);
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"performance_" + pfNo + ".pdf\"");
//
//        try {
//            pdfGeneratorUtil.generateDetailPerPdf(dto, response.getOutputStream());
//        } catch (DocumentException e) { throw new IOException(e.getMessage()); }
//    }

//    @GetMapping("/pdf/checklist")
//    public void downloadFullReportPdf(@RequestParam int pjNo, HttpSession session, HttpServletResponse response) throws IOException {
//        // 1. 세션에서 사용자 정보 조회
//        Integer userNo = (Integer) session.getAttribute("loginUserNo");
//        String bnNo = (String) session.getAttribute("loginBnNo");
//
//        // 2. 유효성 검사 : 관리자가 아니면서, 요청한 userNo가 세션의 userNo와 다른 경우 권한 없음
//        boolean isAdmin = (bnNo != null);
//        boolean isOwner = (userNo != null && userNo.equals(userNo));
//
//        if (!isAdmin && !isOwner) {
//            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
//            return;
//        }
//
//        // 2. PDF 생성에 필요한 데이터 조회
//        // 2-1. 프로젝트 정보
//        PjDto projectInfo = pjService.read(pjNo, bnNo); // PjService 주입 필요
//        if (projectInfo == null) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, "프로젝트를 찾을 수 없습니다.");
//            return;
//        }
//
//        // 2-2. 근무자 정보
//        UsersDto workerInfo = dashboardService.getUserInDash(pjNo, userNo);
//        if (workerInfo == null) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND, "근무자를 찾을 수 없습니다.");
//            return;
//        }
//
//        // 개인별 시간순 정렬된 업무 리스트 (서비스에 새로운 메소드 필요)
//        List<DashboardDto> performances = dashboardService.getPersonalPerformancesSorted(pjNo, userNo);
//
//        // 3. HTTP 헤더 설정
//        response.setContentType("application/pdf");
//        response.setHeader("Content-Disposition", "attachment; filename=\"Personal_Checklist_" + workerInfo.getUserName() + "_" + pjNo + ".pdf\"");
//
//        // 4. PDF 생성 및 전송
//        try {
//            pdfGeneratorUtil.generatePersonalChecklistPdf(projectInfo, workerInfo.getUserName(), performances, response.getOutputStream());
//        } catch (DocumentException e) {
//            throw new IOException("PDF 생성 중 오류가 발생했습니다.", e);
//        }
//    }


} // class end
