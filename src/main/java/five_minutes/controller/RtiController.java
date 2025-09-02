package five_minutes.controller;

import five_minutes.model.dto.RtiDto;
import five_minutes.service.RtiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// **Info** =========================
///
/// RoleTemplateItem Controller
///
/// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
///
/// Google sheet > 21.테이블 > 2-2
///
/// @author OngTK

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/roleTem/Item")
public class RtiController {  // class start

    private final RtiService rtiService;

    // [ RTI-01 ] 상세 역할 템플릿 생성
    @PostMapping
    public int createRTItem(@RequestBody RtiDto rtiDto, HttpSession session){
        System.out.println("RtiController.createRTItem");
        System.out.println("rtiDto = " + rtiDto + ", session = " + session);

        // [01-1] session에서 로그인정보와 사업자번호 존재 여부 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null){
            return -1;
        }
        // [01-2] 매개변수로 받은 rtiDto의 내용이 비어있지 않은지 확인
        if(rtiDto.getRtiName() == null || rtiDto.getRtiName().isEmpty()){
            return -2;
        } else if (rtiDto.getRtiDescription() == null|| rtiDto.getRtiDescription().isEmpty()){
            return -3;
        }

        // [01-3] session에서 bnNo 추출
        String sessionBnno = (String) session.getAttribute("loginBnNo");

        // [01-3] rtiService의 create 메소드 실행
        return rtiService.createRTItem(rtiDto, sessionBnno);
    } // [ RTI-01 ] func end

    // [ RTI-02 ] 상세 역할 템플릿 전체 조회
    @GetMapping
    public List<RtiDto> getRTItem(@RequestParam int rtNo, HttpSession session){
        System.out.println("RtiController.getRTItem");
        System.out.println("rtNo = " + rtNo + ", session = " + session);
        // [02-1] session에서 로그인정보와 사업자번호 존재 여부 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null) {
            return null;
        }
        // [02-2] session에서 bnNo 추출
        String sessionBnno = (String) session.getAttribute("loginBnNo");
        // [02-3] rtiServcie의 readAll 메소드 실행
        return rtiService.readAll(rtNo, sessionBnno);
    } // [ RTI-02 ] func end

    // [ RTI-03 ] 상세 역할 템플릿 개별 조회
    @GetMapping("/indi")
    public RtiDto getIndiRTItem(@RequestParam int rtiNo, HttpSession session){
        System.out.println("RtiController.getIndiRTItem");
        System.out.println("rtiNo = " + rtiNo + ", session = " + session);

        // [03-1] session에서 로그인정보와 사업자번호 존재 여부 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null) {
            return null;
        }
        // [03-2] session에서 사업자번호를 추출
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;
        // [03-3] service의 read 메소드 호출
        return rtiService.read(rtiNo, bnNo);
    } // [ RTI-03 ] func end

    // [ RTI-04 ] 상세 역할 템플릿 수정
    @PutMapping
    public int updateRTItem(@RequestBody RtiDto rtiDto, HttpSession session){
        System.out.println("RtiController.updateRTItem");
        System.out.println("rtiDto = " + rtiDto + ", session = " + session);

        // [04-1] session 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null) {
            return -1;
        }
        // [04-2] service의 update 메소드 호출
        return rtiService.update(rtiDto);
    } // [ RTI-04 ] func end

    // [ RTI-05 ] 상세 역할 템플릿 삭제(비활성화)
    @DeleteMapping
    public int deleteRTItem(@RequestParam int rtiNo, HttpSession session){
        System.out.println("RtiController.deleteRTItem");
        System.out.println("rtiNo = " + rtiNo + ", session = " + session);

        // [05-1] session 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null) {
            return -1;
        }
        String sessionBnno = session.getAttribute("loginBnNo") +"";
        // [05-2] service의 update 메소드 호출
        return rtiService.delete(rtiNo, sessionBnno);
    } // [ RTI-05 ] func end

}   // class end

