package five_minutes.controller;

import five_minutes.service.PasswordResetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller     // rest => api 컨트롤러가 아니라 HTML뷰 를 반환하는 컨트롤러 => @ResponseBody 를 넣으면 @RestController와 동일하게 rest를 한다.
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/password/reset")
public class PasswordResetPageController {
    // passwordResetService 의존성 주입
    private final PasswordResetService passwordResetService;

    // redirect 는 클라이언트 브라우저에게 "다른 URL로 다시 요청해라"라는 것. forward 는 강제로 지정 설정해준다.
    // 보통 redirect랑 forward는 @Controller 에만 해준다. @RestController는 데이터(JSON/XML) 를 주는 용이기 때문.
    // @Controller 는 HTML 뷰 링크를 줄 수 있는 용이다. 참고하시라.

    // 메일 클릭시 토큰 검증 후 세션을 저장하는 controller 그 다음은 redirect 해서 비밀번호 재설정 페이지로 가게 하는 거임
    @GetMapping("/verify")
    public String verifyAndRedirect(@RequestParam String token, HttpSession session) {
        int userNo = passwordResetService.verifyResetToken(token);
        if (userNo > 0) {
            // 토큰을 세션에 저장(주소창에서 감추기 위함)
            session.setAttribute("pwResetToken", token);
            return "redirect:/password/reset/form";
        }
        return "/reset-invalid";
    }
    // form 확인 후 실패면 실패페이지 , 확인 되면 재설정 페이지로 이동하는 controller
    @GetMapping("/form")
    public String showResetForm(HttpSession session) {
        // 토큰이 세션이 있는지 확인한다.
        String token = (String) session.getAttribute("pwResetToken");
        // 있으면 비밀번호 재설정 페이지으로 이동한다. 없으면 실패 페이지로 간다.
        return (token != null) ? "/reset-form" : "/reset-invalid";
    }   // func end

    // 비밀번호 설정 완료 페이지로 이동
    @GetMapping("/success")
    public String successPage() {
        return "/reset-success";
    }   // func end

    // 토큰이 무효하거나 없거나 만료 됐을 때 페이지로 이동.
    @GetMapping("/invalid")
    public String invalidPage() {
        return "/reset-invalid";
    }   // func end

}   // class end
