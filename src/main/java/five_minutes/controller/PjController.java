package five_minutes.controller;

import five_minutes.model.dto.PjDto;
import five_minutes.service.PjService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

///  **info** ================
///
/// 프로젝트 정보를 처리하는 컨트를러
///
/// @author yeonwooshin1
/// @author OngTk

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/project/info")
public class PjController { // class start

    // PjService 의존성 주입
    private final PjService pjService;

    // [PJ-01] 프로젝트 info 생성
    @PostMapping
    public int createProjectInfo(@ModelAttribute PjDto pjDto , HttpSession httpSession ) {
        System.out.println("PjController.createProjectInfo");
        System.out.println("pjDto = " + pjDto + ", httpSession = " + httpSession);

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginBnNo")== null ){
            return -1;
        }   // if end

        // bnNo 를 가져온다.
        String bnNo = (String) httpSession.getAttribute("loginBnNo");
        pjDto.setBnNo(bnNo);

        // 서비스 호출 후 값을 보낸다.
        return pjService.createProjectInfo( pjDto );

    }   // func end

    // [PJ-02] 프로젝트 정보 전체 조회
    @GetMapping
    public List<PjDto> getPJinfo(HttpSession session) {
        System.out.println("PjController.getPJinfo");
        System.out.println("session = " + session);

        // [02-1] session에서 로그인정보와 사업자번호 존재 여부 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null){
            return null;
        }
        // [02-2] session에서 사업자번호를 추출
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;

        // [02-4] pjService 의 readAll 매소드 실행
        return pjService.readAll(bnNo);
    } // func end

    // [PJ-03] 프로젝트 개별 조회
    @GetMapping("/indi")
    public PjDto getIndiPjInfo(@RequestParam int pjNo, HttpSession session){
        PjDto pjDto = new PjDto();
        // [03-1] session에서 로그인정보와 사업자번호 존재 여부 확인
        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null || pjNo == 0){
            pjDto.setPjNo(0); // 로그인 정보 없음
            return pjDto;
        }
        // [03-2] session에서 사업자번호 추출
        String bnNo =  (String)session.getAttribute("loginBnNo");
        
        // [03-3] pjService read 메소드 실행
        return pjService.read(pjNo, bnNo);
    } // func end

    // [PJ-04] 프로젝트 정보 수정
    @PutMapping
    public int updatePJInfo(@ModelAttribute PjDto pjDto, HttpSession session){
        System.out.println("PjController.updatePJInfo");
        System.out.println("pjDto = " + pjDto);

        // [04-1] session 에서 로그인 정보 조회
        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null ){
            return 0;
        }
        // [04-2] session에서 사업자번호 추출
        String bnNo =  (String)session.getAttribute("loginBnNo");
        pjDto.setBnNo(bnNo);

        // [04-3] pjService update 메소드 실행
        return pjService.updatePJInfo(pjDto);
    } // func end

    // [PJ-05]
    @DeleteMapping
    public int deletePJInfo(@RequestParam int pjNo, HttpSession session){
        System.out.println("PjController.deletePJInfo");
        System.out.println("pjNo = " + pjNo + ", session = " + session);
        // [05-1] session 에서 로그인 정보 조회
        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null ){
            return 0;
        }
        // [05-2] session에서 사업자번호 추출
        String bnNo =  (String)session.getAttribute("loginBnNo");

        // [05-3] service 로 전달
        return pjService.delete(pjNo, bnNo);

    } // func end

}   // class end
