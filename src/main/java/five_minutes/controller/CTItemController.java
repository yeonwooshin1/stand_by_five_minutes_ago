package five_minutes.controller;

import five_minutes.service.CTItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checktem/item")
public class CTItemController {
    // CheckTemplateItem
    // * DI
    private final CTItemService ctItemService;

    // [1] 상세 체크리스트 템플릿 생성

    // [2] 상세 체크리스트 템플릿 전체조회

    // [3] 상세 체크리스트 템플릿 개별조회

    // [4] 상세 체크리스트 템플릿 수정

    // [5] 상세 체크리스트 템플릿 삭제

} // class end
