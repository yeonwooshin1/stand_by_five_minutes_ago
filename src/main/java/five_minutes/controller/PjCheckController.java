package five_minutes.controller;

import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import five_minutes.model.dto.PjCheckDto;
import five_minutes.service.PjCheckService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// **Info** =========================
///
/// pjChecklistItem Contoller
///
/// 프로젝트 체크리스트 업무를 상세하게 정의하고 미리 만들어둔 체크리스트 템플릿을 사용한다
///
/// Google sheet > 21.테이블 > 3-4
///
/// @author dongjin

@RestController
@RequestMapping("/project/checklist")
@RequiredArgsConstructor
public class PjCheckController {
    // [*] DI
    private final PjCheckService pjCheckService;

// [1] 프로젝트 체크리스트 추가
    /*
        * 로직 안내
        1. 일치하는 pjNo를 확인한다.
        2. pjChklTitle, pjHelpText을 입력 받는다.
        3. 세션에서 bnNo를 확인 한다.
        4. 프로젝트 체크리스트 DB에 저장한다.
     */
    @PostMapping("")
    public int createPJCheck(PjCheckDto pjCheckDto) {
        return pjCheckService.createPJCheck(pjCheckDto);
    }

    // [2] 프로젝트 체크리스트 목록조회
    /*
        * 로직 안내
        1. pjNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 프로젝트체크리스트 테이블의 DB를 모두 불러온다.
     */

    @GetMapping("")
    public List<PjCheckDto> getPJCheck(int pjNo){
        return pjCheckService.getPJCheck(pjNo);
    }

    // [3] 프로젝트 체크리스트 설명 조회
    /*
        * 로직 안내
        1. pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 일치하는 프로젝트체크리스트 테이블의 DB를 불러온다.
        * 체크리스트 설명 보기 클릭 했을 때 출력
     */

    @GetMapping("/info")
    public PjCheckDto getInfoPJCheck(int pjNo , int pjChkItemNo){
        return pjCheckService.getInfoPJCheck(pjNo, pjChkItemNo);
    }

    // [4] 프로젝트 체크리스트 수정
    /*
        * 로직 안내
        1. 일치하는 pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChklTitle와 pjhelpText를 입력 받는다.
        4. 프로젝트 체크리스트 DB를 수정한다.
     */
    @PutMapping("")
    public int updatePJCheck(PjCheckDto pjCheckDto){
        return pjCheckService.updatePJCheck(pjCheckDto);
    }

    // [5] 프로젝트 체크리스트 삭제
    /*
        * 로직 안내
        1. 일치하는  pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChkIStatus(상태)를 0으로 변경한다.
     */
    @DeleteMapping("")
    public int deletePJCheck(int pjChkItemNo){
        return pjCheckService.deletePJCheck(pjChkItemNo);
    }

    // [6] 프로젝트 체크리스트 템플릿 전체조회 - 대분류
    /*
        * 로직 안내
        1. CTemDto의 ctNo 값을 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. CTemDto DB의 ctNo와, ctNo와 같은 레코드의 ctName을 꺼내온다.
        * 프론트에서는 체크리스트 추가 버튼 -> 대분류 셀렉트로 처리
     */
    @GetMapping("/tem")
    public CTemDto getPJCheckTem(int ctNo){
        return pjCheckService.getPJCheckTem(ctNo);
    }

    // [7] 프로젝트 체크리스트 템플릿 전체조회 - 상세
    /*
        * 로직 안내
        1. CTItemDto의 ctNo 값이 CTemDto의 ctNo와 일치하는지 확인한다.
        2. CTItemDto의 DB에서 ctiNo, ctiTitle를 불러온다.
        * ctiNo의 값에서 500000을 빼고 프론트에 송출해서 1, 2, 3번 식으로 출력하기
     */
    @GetMapping("/item")
    public List<CTItemDto> getPJCheckItem(int ctNo){
        return pjCheckService.getPJCheckItem(ctNo);
    }

    // [8] 프로젝트 체크리스트 템플릿 불러오기
    /*
        * 로직 안내
        1. CTItemDto에서 ctiNo를 입력 받는다.
        2. 서비스에서 ctiNo로 CTItemDto 조회한다.
        3. 조회된 CTItemDto의 ctNo를 기준으로 CTemDto 조회한다.
        4. 두 데이터를 조합해 PjCheckDto에 추가한다.
        * CTemDto_CTItemDto 스네이크 형식으로 데이터를 묶어 저장한다.
     */
    @PostMapping("/tem")
    public int loadPJCheckTem(PjCheckDto pjCheckDto){
        return pjCheckService.loadPJCheckTem(pjCheckDto);
    }


}
