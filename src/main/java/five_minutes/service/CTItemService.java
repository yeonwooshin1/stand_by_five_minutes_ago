package five_minutes.service;

import five_minutes.model.dao.CTItemDao;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CTItemService {
    // CheckTemplateItem
    // * DI
    private final CTItemDao ctItemDao;

    // [1] 상세 체크리스트 템플릿 생성

    // [2] 상세 체크리스트 템플릿 전체조회

    // [3] 상세 체크리스트 템플릿 개별조회

    // [4] 상세 체크리스트 템플릿 수정

    // [5] 상세 체크리스트 템플릿 삭제

} // class end
