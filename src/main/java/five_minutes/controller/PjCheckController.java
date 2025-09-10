package five_minutes.controller;

import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import five_minutes.model.dto.PjCheckDto;
import five_minutes.service.PjCheckService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// **Info** =========================
///
/// pjChecklistItem Contoller
///
/// 프로젝트 체크리스트 업무를 상세하게 정의하고 미리 만들어둔 체크리스트 템플릿을 사용한다
///
/// Google sheet > 21.테이블 > 3-4
///
/// @author dongjin

@RestController
@RequestMapping("/project/checklist")
@RequiredArgsConstructor
public class PjCheckController {
    // [*] DI
    private final PjCheckService pjCheckService;

    // [1] 프로젝트 체크리스트 추가
    /*
        * 로직 안내
        1. 일치하는 pjNo를 확인한다.
        2. pjChklTitle, pjHelpText을 입력 받는다.
        3. 세션에서 bnNo를 확인 한다.
        4. 프로젝트 체크리스트 DB에 저장한다.
     */
    /*
        URL : http://localhost:8080/project/checklist
        BODY :    { "pjNo" : 6000001 , "pjChklTitle" : "출근확인" , "pjHelpText" : "출근 최소 5분전에 출근 장소 도착" }
     */
    @PostMapping("")
    public int createPJCheck(@RequestBody PjCheckDto pjCheckDto, HttpSession session) {
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            pjCheckDto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo가 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        pjCheckService.checkPjNo(pjCheckDto.getPjNo(), bnNo);

        // 3. 리턴
        pjCheckDto.setStatus("ACCESS_OK");
        return pjCheckService.createPJCheck(pjCheckDto);
    }

    // [2] 프로젝트 체크리스트 목록조회
    // URL : http://localhost:8080/project/checklist?pjNo=6000001
    /*
        * 로직 안내
        1. pjNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 프로젝트체크리스트 테이블의 DB를 모두 불러온다.
     */

    @GetMapping("")
    public List<PjCheckDto> getPJCheck(@RequestParam int pjNo, HttpSession session) {
        List<PjCheckDto> list = pjCheckService.getPJCheck(pjNo);
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            list = new ArrayList<>();
            PjCheckDto dto = new PjCheckDto();
            dto.setStatus("NOT_LOGGED_IN");
            list.add(dto);
            return list;
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        boolean loginCheck = pjCheckService.checkPjNo(pjNo, bnNo);
        if (!loginCheck) {
            list = new ArrayList<>();
            PjCheckDto dto = new PjCheckDto();
            dto.setStatus("ACCESS_DENIED");
            list.add(dto);
            return list;
        }
        // 3. pjNo 못 찾았으면
        if (list.isEmpty()) {
            PjCheckDto dto = new PjCheckDto();
            dto.setStatus("NOT_FOUND");
            list.add(dto);
            return list;
        }
        // 4. 리턴
        for (PjCheckDto dto : list) {
            dto.setStatus("ACCESS_OK");
            dto.setBnNo(bnNo); // 세션에서 가져온 bnNo 직접 넣기
        }
        return list;
    }

    // [3] 프로젝트 체크리스트 설명 조회
    // URL : http://localhost:8080/project/checklist/info?pjNo=6000001&pjChkItemNo=8000001
    /*
        * 로직 안내
        1. pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 일치하는 프로젝트체크리스트 테이블의 DB를 불러온다.
        * 체크리스트 설명 보기 클릭 했을 때 출력
     */

    @GetMapping("/info")
    public PjCheckDto getInfoPJCheck(@RequestParam int pjNo, @RequestParam int pjChkItemNo, HttpSession session) {
        PjCheckDto dto = pjCheckService.getInfoPJCheck(pjNo, pjChkItemNo);
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            dto.setStatus("NOT_LOGGED_IN");
            return dto;
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        boolean loginCheck = pjCheckService.checkPjNo(pjNo, bnNo);
        if (!loginCheck) {
            dto = new PjCheckDto();
            dto.setStatus("ACCESS_DENIED");
            return dto;
        }
        // 3. DTO에서 pjNo 찾을 수 없는 경우
        if (dto == null) {
            dto.setStatus("NOT_FOUND");
        }
        // 3. 리턴
        dto.setStatus("ACCESS_OK");
        return dto;
    }

    // [4] 프로젝트 체크리스트 수정
    // URL : http://localhost:8080/project/checklist
    // BODY : {"pjChklTitle" : "출근확인 세부업무 1" , "pjHelpText" : "출근확인 수행을 위한 세부 zzz작업 " , "pjChkItemNo" : 8000001 }
    /*
        * 로직 안내
        1. 일치하는 pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChklTitle와 pjhelpText를 입력 받는다.
        4. 프로젝트 체크리스트 DB를 수정한다.
     */
    @PutMapping("")
    public int updatePJCheck(@RequestBody PjCheckDto pjCheckDto, HttpSession session) {
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            pjCheckDto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        pjCheckService.checkPjNo(pjCheckDto.getPjNo(), bnNo);
        // 3. 리턴
        pjCheckDto.setStatus("ACCESS_OK");
        return pjCheckService.updatePJCheck(pjCheckDto);
    }

    // [5] 프로젝트 체크리스트 삭제
    // URL : http://localhost:8080/project/checklist?pjNo=6000001&pjChkItemNo=8000001
    /*
        * 로직 안내
        1. 일치하는  pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChkIStatus(상태)를 0으로 변경한다.
     */
    @DeleteMapping("")
    public int deletePJCheck(@RequestParam int pjNo, @RequestParam int pjChkItemNo, HttpSession session) {
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            PjCheckDto dto = new PjCheckDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        PjCheckDto dto = new PjCheckDto();
        pjCheckService.checkPjNo(pjNo, bnNo);

        // 3. 리턴
        dto.setStatus("ACCESS_OK");
        dto.setBnNo(bnNo);
        return pjCheckService.deletePJCheck(pjChkItemNo);
    }

    // [6] 프로젝트 체크리스트 템플릿 전체조회 - 대분류
    // URL : http://localhost:8080/project/checklist/tem?ctNo=4000001
    /*
        * 로직 안내
        1. CTemDto의 ctNo 값을 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. CTemDto DB의 ctNo와, ctNo와 같은 레코드의 ctName을 꺼내온다.
        * 프론트에서는 체크리스트 추가 버튼 -> 대분류 셀렉트로 처리
     */
    @GetMapping("/tem")
    public CTemDto getPJCheckTem(@RequestParam int ctNo, HttpSession session) {
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return dto; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        // 3. 리턴
        return pjCheckService.getPJCheckTem(ctNo);
    }

    // [7] 프로젝트 체크리스트 템플릿 전체조회 - 상세
    // URL : /project/checklist/item?ctNo=4000001
    /*
        * 로직 안내
        1. CTItemDto의 ctNo 값이 CTemDto의 ctNo와 일치하는지 확인한다.
        2. CTItemDto의 DB에서 ctiNo, ctiTitle를 불러온다.
        * ctiNo의 값에서 500000을 빼고 프론트에 송출해서 1, 2, 3번 식으로 출력하기
     */
    @GetMapping("/item")
    public List<CTItemDto> getPJCheckItem(@RequestParam int ctNo, HttpSession session) {
        List<CTItemDto> list;
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            list = new ArrayList<>();
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            list.add(dto);
            return list; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        // 3. 리턴
        return pjCheckService.getPJCheckItem(ctNo);
    }

    // [8] 프로젝트 체크리스트 템플릿 불러오기
    // URL : http://localhost:8080/project/checklist/tem
    // BODY : { "ctiNo" : 5000001 , "pjNo" : 6000001 }
    /*
        * 로직 안내
        1. CTItemDto에서 ctiNo를 입력 받는다.
        2. 서비스에서 ctiNo로 CTItemDto 조회한다.
        3. 조회된 CTItemDto의 ctNo를 기준으로 CTemDto 조회한다.
        4. 두 데이터를 조합해 PjCheckDto에 추가한다.
        * CTemDto_CTItemDto 스네이크 형식으로 데이터를 묶어 저장한다.
     */
    @PostMapping("/tem")
    // 반환 타입을 int에서 Map<String, Object>로 변경하여 JSON 객체를 반환하도록 합니다.
    public Map<String, Object> loadAndSaveTemplate(@RequestBody Map<String, Integer> request, HttpSession session) {
        int ctiNo = request.get("ctiNo");
        int pjNo = request.get("pjNo");
        // 프론트엔드에 전달할 응답 데이터를 담을 Map 객체를 생성합니다.
        Map<String, Object> response = new HashMap<>();

        // 1. 세션에서 로그인 정보를 확인합니다.
        if (session.getAttribute("loginUserNo") == null) {
            // 로그인되지 않은 경우, 실패 상태와 메시지를 담아 반환합니다.
            response.put("success", false);
            response.put("message", "NOT_LOGGED_IN");
            response.put("newPjChkItemNo", -1); // JS에서 로그인 실패를 식별할 수 있도록 -1을 담습니다.
            return response;
        }

        // 2. 로그인된 사용자가 해당 프로젝트에 접근할 권한이 있는지 확인합니다.
        String bnNo = (String) session.getAttribute("loginBnNo");
        boolean isValidProject = pjCheckService.checkPjNo(pjNo, bnNo);
        if (!isValidProject) {
            // 권한이 없는 경우, 실패 상태와 메시지를 담아 반환합니다.
            response.put("success", false);
            response.put("message", "ACCESS_DENIED");
            response.put("newPjChkItemNo", 0); // JS에서 권한 없음을 식별할 수 있도록 0을 담습니다.
            return response;
        }

        // 3. 서비스 로직을 호출하여 템플릿을 불러와 저장하고, 새로 생성된 체크리스트 항목의 ID를 받습니다.
        int newPjChkItemNo = pjCheckService.loadAndSaveTemplate(ctiNo, pjNo);

        // 4. 서비스 처리 결과에 따라 성공 또는 실패 응답을 구성합니다.
        if (newPjChkItemNo > 0) {
            // 성공 시, 성공 상태와 새로 생성된 ID를 담아 반환합니다.
            response.put("success", true);
            response.put("newPjChkItemNo", newPjChkItemNo);
        } else {
            // 실패 시, 실패 상태를 담아 반환합니다.
            response.put("success", false);
            response.put("newPjChkItemNo", 0);
        }
        return response;
    }

    // [9] 프로젝트 체크리스트 일괄저장
    @PostMapping("/save")
    public List<Map<String, Integer>> savePJChecklist(@RequestBody List<PjCheckDto> list, HttpSession session) {
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            return new ArrayList<>(); // 비로그인 시 빈 리스트 반환
        }
        // 2. 서비스 호출
        return pjCheckService.savePJChecklist(list);
    }

    // [*] changeStatus에 따른 
    @GetMapping("/session-check")
    public Map<String, Object> checkSession(HttpSession session) {
        Map<String, Object> result = new HashMap<>();
        result.put("loggedIn", session.getAttribute("loginUserNo") != null);
        result.put("isBusiness", session.getAttribute("loginBnNo") != null);
        return result;
    }


}
