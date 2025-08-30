package five_minutes.service;

import five_minutes.model.dao.BusinessDao;
import five_minutes.model.dto.BusinessDto;
import five_minutes.model.dto.EmailRecoverDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class BusinessService { // class start
    // 비지니스 dao 의존성 주입
    private final BusinessDao businessDao;

    // 회사 정보 조회 서비스
    public BusinessDto getBusinessInfo(String bnNo ){

        return businessDao.getBusinessInfo( bnNo );

    }   // func end

}   // class end