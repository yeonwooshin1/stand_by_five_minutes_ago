package five_minutes.service;

import five_minutes.model.dao.RtiDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Info =========================
// RoleTemplateItem Service
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// 작성자 : OngTK


@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class RtiService { // class start

    private final RtiDao rtiDao;


}   // class end
