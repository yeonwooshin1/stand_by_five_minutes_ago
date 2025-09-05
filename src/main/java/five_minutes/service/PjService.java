package five_minutes.service;

import five_minutes.model.Repository.CommonRepository;
import five_minutes.model.dao.PjDao;
import five_minutes.model.dto.PjDto;

import five_minutes.util.DateValidatorUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

///  **info** ================
///
/// 프로젝트 정보를 처리하는 서비스
///
/// @author yeonwooshin1
/// @author OngTk


@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class PjService extends AbstractService<PjDto, Integer, String> {    // class start

    // PjDao 의존성 주입
    private final PjDao pjDao;

    // 프로젝트 info 서비스
    public int createProjectInfo(PjDto pjDto , String bnNo ) {

        // 유효성 검사해야 할 날짜만 getter
        String pjStartDate = pjDto.getPjStartDate();
        String pjEndDate = pjDto.getPjEndDate();

        // 시작날짜가 종료날짜보다 늦을 시 -2 반환.
        // DateValidatorUtil 클래스 사용 (util 패키지)
        if( !DateValidatorUtil.isValidRange( pjStartDate , pjEndDate) ) return -2;
        
        // pjDao 생성 메소드 실행 및 결과 반환
        return pjDao.create(pjDto);
    }   // func end

    // 기본 CRUD를 위해 추상메소드 override
    @Override
    protected CommonRepository<PjDto, Integer, String> getDao() {
        return pjDao;
    }

}   // class end
