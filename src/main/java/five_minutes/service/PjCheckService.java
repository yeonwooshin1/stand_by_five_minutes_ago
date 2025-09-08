package five_minutes.service;

import five_minutes.model.dao.PjCheckDao;
import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import five_minutes.model.dto.PjCheckDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/// **Info** =========================
///
/// pjChecklistItem Service
///
/// 프로젝트 체크리스트 업무를 상세하게 정의하고 미리 만들어둔 체크리스트 템플릿을 사용한다
///
/// Google sheet > 21.테이블 > 3-4
///
/// @author dongjin

@Service
@RequiredArgsConstructor
public class PjCheckService {
    // DI
    private final PjCheckDao pjCheckDao;

    // [*] pjNo(프로젝트PK)이 로그인한 사용자의 bnNo(세션)인지 확인
    public boolean checkPjNo(int pjNo, String bnNo) {
        return pjCheckDao.checkPjNo(pjNo, bnNo);
    }

    // [1] 프로젝트 체크리스트 추가
    /*
        * 로직 안내
        1. 일치하는 pjNo를 확인한다.
        2. pjChklTitle, pjHelpText을 입력 받는다.
        3. 세션에서 bnNo를 확인 한다.
        4. 프로젝트 체크리스트 DB에 저장한다.
     */

    public int createPJCheck(PjCheckDto pjCheckDto) {
        return pjCheckDao.createPJCheck(pjCheckDto);
    }

    // [2] 프로젝트 체크리스트 목록조회
    /*
        * 로직 안내
        1. pjNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 프로젝트체크리스트 테이블의 DB를 모두 불러온다.
     */

    public List<PjCheckDto> getPJCheck(int pjNo) {
        return pjCheckDao.getPJCheck(pjNo);
    }

    // [3] 프로젝트 체크리스트 설명 조회
    /*
        * 로직 안내
        1. pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. 일치하는 프로젝트체크리스트 테이블의 DB를 불러온다.
        * 체크리스트 설명 보기 클릭 했을 때 출력
     */

    public PjCheckDto getInfoPJCheck(int pjNo, int pjChkItemNo) {
        return pjCheckDao.getInfoPJCheck(pjNo, pjChkItemNo);
    }

    // [4] 프로젝트 체크리스트 수정
    /*
        * 로직 안내
        1. 일치하는 pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChklTitle와 pjhelpText를 입력 받는다.
        4. 프로젝트 체크리스트 DB를 수정한다.
     */
    public int updatePJCheck(PjCheckDto pjCheckDto) {
        return pjCheckDao.updatePJCheck(pjCheckDto);
    }

    // [5] 프로젝트 체크리스트 삭제
    /*
        * 로직 안내
        1. 일치하는 pjNo와 pjChkItemNo를 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. pjChkIStatus(상태)를 0으로 변경한다.
     */
    public int deletePJCheck(int pjChkItemNo) {
        return pjCheckDao.deletePJCheck(pjChkItemNo);
    }

    // [6] 프로젝트 체크리스트 템플릿 전체조회 - 대분류
    /*
        * 로직 안내
        1. CTemDto의 ctNo 값을 확인한다.
        2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
        3. CTemDto DB의 ctNo와, ctNo와 같은 레코드의 ctName을 꺼내온다.
        * 프론트에서는 체크리스트 추가 버튼 -> 대분류 셀렉트로 처리
     */
    public CTemDto getPJCheckTem(int ctNo) {
        return pjCheckDao.getPJCheckTem(ctNo);
    }

    // [7] 프로젝트 체크리스트 템플릿 전체조회 - 상세
    /*
        * 로직 안내
        1. CTItemDto의 ctNo 값이 CTemDto의 ctNo와 일치하는지 확인한다.
        2. CTItemDto의 DB에서 ctiNo, ctiTitle를 불러온다.
        * ctiNo의 값에서 500000을 빼고 프론트에 송출해서 1, 2, 3번 식으로 출력하기
     */
    public List<CTItemDto> getPJCheckItem(int ctNo) {
        return pjCheckDao.getPJCheckItem(ctNo);
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
    public int loadAndSaveTemplate(int ctiNo, int pjNo) {
        // 1. ctiNo로 템플릿 정보 조회
        PjCheckDto pjCheckDto = pjCheckDao.sumPJCheckTem(ctiNo);
        // 2. 템플릿 정보 조회 성공 시
        if (pjCheckDto != null) {
            // 3. DTO에 pjNo와 pjHelpText 설정
            pjCheckDto.setPjNo(pjNo);
            // 4. ctiHelpText를 pjHelpText에 넣기
            pjCheckDto.setPjHelpText(pjCheckDto.getCtiHelpText());
            // 5. DAO 호출하여 DB에 저장 및 결과 반환
            return pjCheckDao.loadPJCheckTem(pjCheckDto);
        }
        // 템플릿 정보 조회 실패 시 0 반환
        return 0;
    }

}
