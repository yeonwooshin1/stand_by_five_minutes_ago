package five_minutes.controller;

import five_minutes.service.BusinessService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class BusinessController {  // class start

    private final BusinessService businessService;

}   // class end
