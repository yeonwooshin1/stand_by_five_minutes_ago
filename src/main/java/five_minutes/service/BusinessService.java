package five_minutes.service;

import five_minutes.model.dao.BusinessDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class BusinessService { // class start

    private final BusinessDao businessDao;

}   // class end
