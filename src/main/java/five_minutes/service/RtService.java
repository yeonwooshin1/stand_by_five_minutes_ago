package five_minutes.service;

import five_minutes.model.dao.RtDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Info =========================
// RoleTemplate Service
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// 작성자 : OngTK


@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class RtService { // class start

    private final RtDao rtDao;


}   // class end
