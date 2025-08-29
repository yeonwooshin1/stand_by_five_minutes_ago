package five_minutes.service;

import five_minutes.model.dao.CTemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class CTemService {
    private final CTemDao cTemDao;

} // class end
