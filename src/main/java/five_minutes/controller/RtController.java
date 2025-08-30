package five_minutes.controller;

import five_minutes.model.dto.RtDto;
import five_minutes.service.RtService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// Info =========================
// RoleTemplate Controller
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// Writer : OngTK

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/roleTem")
public class RtController {  // class start

    private final RtService rtService;

    // [ RT-01 ] 역할 템플릿 생성 createRT()
    @PostMapping
    public int createRT(@RequestBody RtDto rtDto, HttpSession session){
        System.out.println("RtController.createRT");
        System.out.println("rtDto = " + rtDto + ", session = " + session);

        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null){
            return -1;
        }
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;
        rtDto.setBnNo(bnNo);
        if(rtDto.getRtName() == null || rtDto.getRtName().isEmpty()){
            return -2;
        } else if (rtDto.getRtDescription() == null|| rtDto.getRtDescription().isEmpty()){
            return -3;
        }
        return rtService.create(rtDto);
    } // [ RT-01 ]  func end

    // [ RT-02 ] 역할 템플릿 전체 조회 getRT()
    @GetMapping
    public List<RtDto> getRT(HttpSession session){
        System.out.println("RtController.getRT");
        System.out.println("session = " + session);

        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null){
            return null;
        }
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;

        return rtService.readAll(bnNo);
    }// [ RT-02 ]  func end

    // [ RT-03 ] 역할 템플릿 개별 조회 getIndiRT()
    @GetMapping("/indi")
    public RtDto getIndiRT(@RequestParam int rtNo, HttpSession session){
        System.out.println("RtController.getIndiRT");
        System.out.println("rtNo = " + rtNo);

        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null || rtNo == 0 ){
            return null;
        }
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;

        return rtService.read(rtNo, bnNo);
    }// [ RT-03 ]  func end

    // [ RT-04 ] 역할 템플릿 수정 updateRT()
    @PutMapping
    public int updateRT(@RequestBody RtDto rtDto, HttpSession session){
        System.out.println("RtController.updateRT");
        System.out.println("rtDto = " + rtDto + ", session = " + session);

        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null ){
            return -1;
        }
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;

        if(rtDto.getRtName() == null || rtDto.getRtName().isEmpty()){
            return -2;
        } else if (rtDto.getRtDescription() == null|| rtDto.getRtDescription().isEmpty()){
            return -3;
        }

        return  rtService.update(rtDto);
    }// [ RT-04 ]  func end

    // [ RT-05 ] 역할 템플릿 삭제(비활성화) deleteRT()
    @DeleteMapping
    public int deleteRT(@RequestParam int rtNo, HttpSession session){
        System.out.println("RtController.deleteRT");
        System.out.println("rtNo = " + rtNo + ", session = " + session);

        if( session.getAttribute("loginUserNo") == null ||
                session.getAttribute("loginBnNo") == null ){
            return -1;
        }
        Object loginBnNo = session.getAttribute("loginBnNo");
        String bnNo = (String) loginBnNo;

        return rtService.delete(rtNo, bnNo);
    }// [ RT-05 ]  func end

}   // class end

