package five_minutes.controller;

import five_minutes.model.dto.CTemDto;
import five_minutes.service.CTemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/checktem")
public class CTemController {  // class start
    // CheckTemplate
    // * DI
    private final CTemService cTemService;

    // 로그인체크
    // URL : http://localhost:8080/user/login
    // BODY :   { "email": "mx2ur43n@example.com" , "passwordHash": "NyEUIQeE4N" }

    // [1] 체크리스트 템플릿 생성
    // URL : http://localhost:8080/checktem
    // BODY : { "ctName" : "출퇴근 돌파" , "ctDescription" : "네이버 지도를 활용하도록 합니다."}
    @PostMapping("")
    public int createCTem(@RequestBody CTemDto cTemDto, HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중이면 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        cTemDto.setBnNo(bnNo);
        // 3. 리턴
        cTemDto.setStatus("ACCESS_OK");
        return cTemService.createCTem(cTemDto);
    }

    // [2] 체크리스트 템플릿 전체조회
    // URL : http://localhost:8080/checktem
    @GetMapping("")
    public List<CTemDto> getCTem(HttpSession session) {
        List<CTemDto> list = new ArrayList<>();

        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            list.add(dto);
            return list; // 비로그인시 status에서 NOT_LOGGED_IN 전송
        }
        // 2. 로그인 중일 때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        list = cTemService.getCTem(bnNo);

        // 3. 리턴
        for (CTemDto dto : list) {
            dto.setStatus("ACCESS_OK");
        }
        return list;
    }

    // [3] 체크리스트 템플릿 개별조회
    // URL : http://localhost:8080/checktem/indi?ctNo=4000001
    @GetMapping("/indi")
    public CTemDto getIndiCTem(@RequestParam int ctNo, HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return dto; // 비로그인시 setStatus 전송
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        CTemDto dto = cTemService.getIndiCtem(bnNo, ctNo);
        // 3. DTO에서 사용자에게 입력받은 ctNo를 찾을 수 없을 경우
        if (dto == null) {
            dto = new CTemDto();
            dto.setStatus("NOT_FOUND");
            return dto; // ctNo 없을 시 setStatus 전송
        }
        // 4. 리턴
        dto.setStatus("ACCESS_OK");
        return dto;
    }

    // [4] 체크리스트 템플릿 수정
    // URL : http://localhost:8080/checktem
    // { "ctName" : "출퇴근 돌파" , "ctDescription" : "네이버 지도를 활용하도록 합니다." , "ctNo" : 4000002 }
    @PutMapping("")
    public int updateCTem(@RequestBody CTemDto cTemDto, HttpSession session) {
        // 1. 로그인 상태 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        cTemDto.setBnNo(bnNo);
        // 3. 리턴
        cTemDto.setStatus("ACCESS_OK");
        return cTemService.updateCTem(cTemDto);
    }

    // [5] 체크리스트 템플릿 삭제
    // URL : http://localhost:8080/checktem?ctNo=4000001
    @DeleteMapping("")
    public int deleteCTem(@RequestParam int ctNo , HttpSession session){
        // 1. 로그인 상태 확인
        if(session.getAttribute("loginUserNo") == null){
            CTemDto dto = new CTemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1;
        }
        // 2. 로그인 중일때 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        // 3. DTO에서 사용자에게 입력받은 ctNo를 찾지 못했을 경우
        CTemDto dto = new CTemDto();
        if(dto == null){
            dto = new CTemDto();
            dto.setStatus("NOT_FOUND");
            return 0;
        }
        // 4. 리턴
        dto.setStatus("ACCESS_OK");
        return cTemService.deleteCTem(bnNo , ctNo);
    }


} // class end

