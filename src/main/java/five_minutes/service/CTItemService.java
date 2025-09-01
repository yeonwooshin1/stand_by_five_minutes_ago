package five_minutes.service;

import five_minutes.model.dao.CTItemDao;
import five_minutes.model.dto.CTItemDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CTItemService {
    // CheckTemplateItem
    // * DI
    private final CTItemDao ctItemDao;

    // [*] 해당 ctNo가 로그인한 사용자의 bnNo인지 확인
    public boolean searchCtNo(int ctNo, String bnNo) {
        return ctItemDao.searchCtNo(ctNo, bnNo);
    }

    // [1] 상세 체크리스트 템플릿 생성
    public int createCTItem(CTItemDto ctItemDto) {
        return ctItemDao.createCTItem(ctItemDto);
    }

    // [2] 상세 체크리스트 템플릿 전체조회
    public List<CTItemDto> getCTItem(int ctNo){
        return ctItemDao.getCTItem(ctNo);
    }

    // [3] 상세 체크리스트 템플릿 개별조회
    public CTItemDto getIndiCTItem(int ctNo , int ctiNo){
        return ctItemDao.getIndiCTItem(ctNo, ctiNo);
    }

    // [4] 상세 체크리스트 템플릿 수정
    public int updateCTItem(CTItemDto ctItemDto){
        return ctItemDao.updateCTItem(ctItemDto);
    }

    // [5] 상세 체크리스트 템플릿 삭제
    public int deleteCTItem(int ctiNo){
        return ctItemDao.deleteCTItem(ctiNo);
    }

} // class end
