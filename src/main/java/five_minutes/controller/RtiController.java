package five_minutes.controller;

import five_minutes.model.dto.RtiDto;
import five_minutes.service.RtiService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Info =========================
// RoleTemplateItem Controller
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// Writer : OngTK

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

    // [ RTI-02 ] 상세 역할 템플릿 개별 조회
    public List<RtiDto> getRTItem(){
        return null;
    } // [ RTI-02 ] func end

    // [ RTI-03 ] 상세 역할 템플릿 전체 조회F
    public RtiDto getIndiRTItem(){
        return null;
    } // [ RTI-03 ] func end

    // [ RTI-04 ] 상세 역할 템플릿 수정
    public int updateRTItem(){
        return 0;
    } // [ RTI-04 ] func end

    // [ RTI-05 ] 상세 역할 템플릿 삭제(비활성화)
    public int deleteRTItem(){
        return 0;
    } // [ RTI-05 ] func end


}   // class end

