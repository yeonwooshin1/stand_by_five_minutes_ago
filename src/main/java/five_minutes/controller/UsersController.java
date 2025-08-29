package five_minutes.controller;

import five_minutes.model.dto.UsersDto;
import five_minutes.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/user")
public class UsersController {  // class start

    // service
    private final UsersService usersService;


    // 로그인 컨트롤러
    @PostMapping("/login")
    public int login(@RequestBody UsersDto usersDto , HttpSession httpSession ) {

        // 서비스 호출하여 유효한 로그인인지 검사
        Map<String, Object> loginResult = usersService.login(usersDto);

        int loginUserNo = (int) loginResult.get("loginUserNo");

        // 로그인 실패할시 0을 반환
        if (loginUserNo == 0) {
            return 0;
        }   // if end

        // 로그인 성공 → 세션에 정보 저장
        // 유저 번호 저장
        httpSession.setAttribute("loginUserNo", loginUserNo);
        // 사업자번호 없으면 null 값 저장
        httpSession.setAttribute("loginBnNo", loginResult.get("loginBnNo"));

        // 확인용 print
        System.out.println(httpSession.getAttribute("loginUserNo"));
        System.out.println(httpSession.getAttribute("loginBnNo"));

        // 성공 시 userNo 반환
        return loginUserNo;
    }   // func end


}   // class end

