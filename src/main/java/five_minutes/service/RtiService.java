package five_minutes.service;

import five_minutes.model.dao.CommonDao;
import five_minutes.model.dao.RtDao;
import five_minutes.model.dao.RtiDao;
import five_minutes.model.dto.RtiDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// **Info** =========================
///
/// RoleTemplateItem Service
///
/// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
///
/// Google sheet > 21.테이블 > 2-2
///
/// @author OngTK

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class RtiService extends AbstractService<RtiDto, Integer, String>{ // class start

    private final RtiDao rtiDao;
    private final RtDao rtDao;

    // AbstractService로 부터 getDao 메소드를 Override
    // Controller의 CRUD 요청을 추상메소드를 통해 바로 Dao 단으로 전달
    @Override
    protected CommonDao<RtiDto, Integer, String> getDao() {
        return (CommonDao<RtiDto, Integer, String>) rtiDao;
    } // func end

    // [00] 참조 rtNo와 로그인 BnNo 검증
    public int checkRtnoBnno(int rtNo, String sessionBnNo) {
        // [00-1] rtDao의 checkRtnoBnno 메소드 실행
        // 반환값 : True : rtNo / false = 0
        return rtDao.checkRtnoBnno(rtNo, sessionBnNo);
    }

    // [ RTI-01 ] 상세 역할템플릿 생성
    public int createRTItem(RtiDto rtiDto, String sessionBnNo) {
        if (checkRtnoBnno(rtiDto.getRtNo(), sessionBnNo) <= 0) {
            return -99; // 권한 없음
        }
        return rtiDao.create(rtiDto);
    } // func end

    // [ RTI-02 ] 상세 역할테플릿 전체 조회
    public List<RtiDto> readAll(int rtNo, String sessionBnNo){
        if (checkRtnoBnno(rtNo, sessionBnNo) <= 0) {
            List<RtiDto> list = new ArrayList<>();
            RtiDto rtiDto = new RtiDto();
            rtiDto.setRtiNo(-99);
            list.add(rtiDto);
            return list; // 권한 없음
        }
        String sRtNo = rtNo+"";
        return rtiDao.readAll(sRtNo);
    } // func end

}   // class end
