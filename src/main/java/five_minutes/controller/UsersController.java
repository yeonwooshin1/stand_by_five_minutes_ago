package five_minutes.controller;

import five_minutes.model.dto.UsersDto;
import five_minutes.service.UsersService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/user")
public class UsersController {  // class start
    // service
    private final UsersService usersService;


//    // 로그인
//    @PostMapping("/login")
//    public int login(@ResponseBody UsersDto usersDto , HttpSession httpSession ) {
//
//        int result = usersService.login( usersDto );
//        // if ( )
//
//
//
//        // 1.매개변수로 받은 요청정보내 세션객체를 확인 해서 없으면 비로그인상태
//        if( session == null || session.getAttribute("loginUserNo") == null ) return -1;
//        // 2.
//        int loginMno = (int)session.getAttribute("loginMno");
//        // 3.
//        boolean result = memberService.delete( loginMno , oldpwd );
//
//        if(result)  session.removeAttribute("loginMno");
//
//        return result;
//
//
//    }   // func end


}   // class end

