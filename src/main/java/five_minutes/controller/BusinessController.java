package five_minutes.controller;

import five_minutes.model.dto.BusinessDto;
import five_minutes.model.dto.EmailRecoverDto;

import five_minutes.service.BusinessService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/business")
public class BusinessController {  // class start
    // 비지니스서비스 의존성 주입
    private final BusinessService businessService;

    // 회사 정보 조회
    @GetMapping("/find/info")
    public BusinessDto getBusinessInfo(HttpSession httpSession ){

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginBnNo")== null ){
            return null;
        }   // if end

        // bnNo 를 가져온다.
        String bnNo = (String) httpSession.getAttribute("loginBnNo");

        // 서비스 호출 후 값 반환
        return businessService.getBusinessInfo(bnNo);
    }   // func end

}   // class end
