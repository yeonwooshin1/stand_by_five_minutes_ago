package five_minutes.controller;

import five_minutes.model.dto.PjDto;
import five_minutes.service.PjService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/project")
public class PjController { // class start

    // PjService 의존성 주입
    private final PjService pjService;

    // 프로젝트 info 생성
    @PostMapping("/info")
    public int createProjectInfo(@RequestBody PjDto pjDto , HttpSession httpSession ) {

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginBnNo")== null ){
            return -1;
        }   // if end

        // bnNo 를 가져온다.
        String bnNo = (String) httpSession.getAttribute("loginBnNo");

        // 서비스 호출 후 값을 보낸다.
        return pjService.createProjectInfo( pjDto , bnNo );

    }   // func end
}   // class end
