package five_minutes.service;

import five_minutes.model.dao.CommonDao;
import five_minutes.model.dao.RtiDao;
import five_minutes.model.dto.RtiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// Info =========================
// RoleTemplateItem Service
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// Writer : OngTK

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class RtiService extends AbstractService<RtiDto, Integer, String>{ // class start

    private final RtiDao rtiDao;

    // AbstractService로 부터 getDao 메소드를 Override
    // Controller의 CRUD 요청을 추상메소드를 통해 바로 Dao 단으로 전달
    @Override
    protected CommonDao<RtiDto, Integer, String> getDao() {
        return (CommonDao<RtiDto, Integer, String>) rtiDao;
    } // func end

}   // class end
