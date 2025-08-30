package five_minutes.controller;

import five_minutes.model.dto.RtiDto;
import five_minutes.service.RtiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Info =========================
// RoleTemplateItem Controller
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// 작성자 : OngTK

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class RtiController {  // class start

    private final RtiService rtiService;

    // [ RTI-01 ] 상세 역할 템플릿 생성
    public int createRTItem(){
        return 0;
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

