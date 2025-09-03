package five_minutes.controller;

import five_minutes.model.dto.ChangePasswordDto;
import five_minutes.model.dto.UsersDto;
import five_minutes.service.PasswordResetService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/password/reset")
public class PasswordResetApiController {   // class start

    // passwordResetService 의존성 주입
    private final PasswordResetService passwordResetService;

    // 링크 발송 controller (이름과 이메일과 전화번호를 주입한다.)
    @PostMapping("/link")
    public int sendLink(@RequestBody UsersDto usersDto) {
        // 서비스 호출 후 값 반환
        return passwordResetService.sendResetLink(usersDto);
    }   // func end

    // 최종 비밀번호 변경 controller
    @PutMapping("/confirm")
    public int confirm(@RequestBody ChangePasswordDto changePasswordDto, HttpSession session) {
        // 세션에 토큰이 있는지 확인한다.
        String token = (String) session.getAttribute("pwResetToken");

        // 세션에 토큰이 없으면?
        if (token == null) {
            // -4 토큰 없음 반환
            return -4;
        }   // if end
        // 서비스 호출 후 반환한다.
        return passwordResetService.resetPassword(changePasswordDto, token);
    }   // func end

}   // class end
