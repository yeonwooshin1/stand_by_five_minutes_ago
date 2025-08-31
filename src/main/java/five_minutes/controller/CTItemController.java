package five_minutes.controller;

import five_minutes.model.dto.CTItemDto;
import five_minutes.service.CTItemService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/checkitem")
public class CTItemController {
    // CheckTemplateItem
    // * DI
    private final CTItemService ctItemService;
    // [1] 상세 체크리스트 템플릿 생성
    // URL : http://localhost:8080/checkitem
    // BODY:
    /*
    {
      	"ctNo" : 4000001 ,
     	"ctiTitle" : "출퇴근 세부업무 1" ,
     	"ctiHelpText" : "특이사항 발생 시 반드시 담당자에게 연락 할 수 있도록 합니다."
    }
    */
    @PostMapping("")
    public int createCTItem(@RequestBody CTItemDto ctItemDto , HttpSession session){
        // 1. 로그인상태 확인
        if (session.getAttribute("loginUserNo") == null){
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. 로그인 중이면 세션에서 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        ctItemService.searchCtNo(ctItemDto.getCtNo() , bnNo);
        // 3. 리턴
        ctItemDto.setStatus("ACCESS_OK");
        return ctItemService.createCTItem(ctItemDto);
    }

    // [2] 상세 체크리스트 템플릿 전체조회
    @GetMapping("")
    public List<CTItemDto> getCTItem (HttpSession session){
        List<CTItemDto> list = new ArrayList<>();
        // TODO
        // 1. 로그인상태 확인

        return list;
    }

    // [3] 상세 체크리스트 템플릿 개별조회

    // [4] 상세 체크리스트 템플릿 수정

    // [5] 상세 체크리스트 템플릿 삭제

} // class end
