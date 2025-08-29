package five_minutes.service;

import five_minutes.model.dao.UsersDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class UsersService { // class start

    private final UsersDao usersDao;


}   // class end
