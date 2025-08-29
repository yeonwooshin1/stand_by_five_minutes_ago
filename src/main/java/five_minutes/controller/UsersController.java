package five_minutes.controller;

import five_minutes.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class UsersController {  // class start

    private final UsersService usersService;

}   // class end

