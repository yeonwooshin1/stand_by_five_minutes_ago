package five_minutes.controller;

import five_minutes.service.RtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

// Info =========================
// RoleTemplate Controller
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// 작성자 : OngTK

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class RtController {  // class start

    private final RtService rtService;



}   // class end

