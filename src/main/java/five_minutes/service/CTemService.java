package five_minutes.service;

import five_minutes.model.dao.CTemDao;
import five_minutes.model.dto.CTemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class CTemService {
    // CheckTemplate
    // * DI
    private final CTemDao cTemDao;

    // [1] 체크리스트 템플릿 생성
    public int createCTem(CTemDto cTemDto) {
        return cTemDao.createCTem(cTemDto);
    }

    // [2] 체크리스트 템플릿 전체조회
    public List<CTemDto> getCTem(String bnNo) {
        return cTemDao.getCTem(bnNo);
    }

    // [3] 체크리스트 템플릿 개별조회

    // [4] 체크리스트 템플릿 수정

    // [5] 체크리스트 템플릿 삭제

} // class end
