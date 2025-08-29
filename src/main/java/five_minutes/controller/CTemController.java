package five_minutes.controller;

import five_minutes.service.CTemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class CTemController {  // class start
    private final CTemService cTemService;

} // class end

