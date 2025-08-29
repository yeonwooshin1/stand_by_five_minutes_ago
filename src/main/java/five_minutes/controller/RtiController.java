package five_minutes.controller;

import five_minutes.service.RtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

// Info =========================
// RoleTemplateItem Controller
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// 작성자 : OngTK

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class RtiController {  // class start

    private final RtiService rtiService;



}   // class end

