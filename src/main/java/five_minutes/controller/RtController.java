package five_minutes.controller;

import five_minutes.model.dto.RtDto;
import five_minutes.service.RtService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

// Info =========================
// RoleTemplate Controller
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// 작성자 : OngTK

// TODO 옹태경

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
public class RtController {  // class start

    private final RtService rtService;

    // [ RT-01 ] 역할 템플릿 생성 createRT()
    public int createRT(){
        return  0;
    }// [ RT-01 ]  func end

    // [ RT-02 ] 역할 템플릿 전체 조회 getRT()
    public List<RtDto> getRT(){
        return null;
    }// [ RT-02 ]  func end

    // [ RT-03 ] 역할 템플릿 개별 조회 getIndiRT()
    public RtDto getIndiRT(){
        return null;
    }// [ RT-03 ]  func end

    // [ RT-04 ] 역할 템플릿 수정 updateRT()
    public int updateRT(){
        return  0;
    }// [ RT-04 ]  func end

    // [ RT-05 ] 역할 템플릿 삭제(비활성화) deleteRT()
    public int deleteRT(){
        return  0;
    }// [ RT-05 ]  func end



}   // class end

