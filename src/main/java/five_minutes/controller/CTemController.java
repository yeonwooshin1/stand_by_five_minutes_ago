package five_minutes.controller;

import five_minutes.model.dto.CTemDto;
import five_minutes.service.CTemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/checktem")
public class CTemController {  // class start
    // CheckTemplate
    // * DI
    private final CTemService cTemService;

    // [1] 체크리스트 템플릿 생성
    @PostMapping("")
    public int createCTem(@RequestBody CTemDto cTemDto, HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("bnNo") == null) {
            return 0;   // 비로그인시 0 반환
        }
        // 2. 로그인 중이면 세션에서 사업자번호 조회
        String bnNo = (String)session.getAttribute("bnNo");
        cTemDto.setBnNo(bnNo);
        // 3. 리턴
        return cTemService.createCTem(cTemDto);
    }

    // [2] 체크리스트 템플릿 전체조회
    public List<CTemDto> getCTem(HttpSession session) {
        // 1. 로그인상태 확인
        if (session.getAttribute("bnNo") == null) {
            return null; // 비로그인시 null 반환
        }
        // 2. 로그인 중일 때 세션에서 사업자번호 조회
        String bnNo = (String)session.getAttribute("bnNo");
        // 3. 리턴
        return cTemService.getCTem(bnNo);

    }

    // [3] 체크리스트 템플릿 개별조회

    // [4] 체크리스트 템플릿 수정

    // [5] 체크리스트 템플릿 삭제

} // class end

