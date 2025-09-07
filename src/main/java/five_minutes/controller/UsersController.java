package five_minutes.controller;

import five_minutes.model.dto.ChangePasswordDto;
import five_minutes.model.dto.EmailRecoverDto;
import five_minutes.model.dto.UsersDto;
import five_minutes.service.UsersService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;
import java.util.Map;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/user")
public class UsersController {  // class start

    // service
    private final UsersService usersService;


    // 로그인
    @PostMapping("/login")
    public int login(@RequestBody UsersDto usersDto , HttpSession httpSession ) {

        // 서비스 호출하여 유효한 로그인인지 검사
        Map<String, Object> loginResult = usersService.login(usersDto);

        int loginUserNo = (int) loginResult.get("loginUserNo");

        // 로그인 실패할시 0을 반환
        if (loginUserNo == 0) {
            return 0;
        }   // if end

        // 로그인 성공 → 세션에 정보 저장
        // 유저 번호 저장
        httpSession.setAttribute("loginUserNo", loginUserNo);
        // 사업자번호 없으면 null 값 저장
        httpSession.setAttribute("loginBnNo", loginResult.get("loginBnNo"));

        // 확인용 print
        System.out.println(httpSession.getAttribute("loginUserNo"));
        System.out.println(httpSession.getAttribute("loginBnNo"));

        // [*] 로그인 시 토큰 발행
        String token = usersService.createLoginToken(loginUserNo);
        httpSession.setAttribute("loginToken", token);

        // 성공 시 userNo 반환
        return loginUserNo;
    }   // func end

    // 로그아웃
    @GetMapping("/logout")
    public int logout( HttpSession httpSession ){

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginUserNo")== null ){
            return 0;
        }   // if end

        // 로그인 중이라면 세션 값을 제거한다.
        httpSession.removeAttribute("loginUserNo");

        // 사업자번호도 null이 아니면 같이 제거한다.
        if( httpSession.getAttribute("loginBnNo") != null ){
            httpSession.removeAttribute("loginBnNo");
        }   // if end

        // 로그아웃 성공 반환
        return 1;
    }   // func end

    // 이메일찾기 (=id)
    @GetMapping("/recoverEmail")
    public EmailRecoverDto recoverUserEmail( UsersDto usersDto ){

        // 서비스 호출해서 유효성 검사 후 반환
        return usersService.recoverUserEmail(usersDto);

    }   // func end

    // 비밀번호 변경
    @PutMapping("/update/password")
    public int updatePassword(@RequestBody ChangePasswordDto changePasswordDto , HttpSession httpSession){

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginUserNo")== null ){
            return -2;
        }   // if end

        // userNo 를 가져온다.
        int userNo = (int) httpSession.getAttribute("loginUserNo");

        // 서비스 호출해서 유효성 검사 후 반환
        return usersService.updatePassword( changePasswordDto ,userNo );

    }   // func end

    // 유저 정보 조회
    @GetMapping("/find/info")
    public UsersDto getUserInfo( HttpSession httpSession ){

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if( httpSession == null || httpSession.getAttribute("loginUserNo")== null ){
            return null;
        }   // if end

        // userNo 를 가져온다.
        int userNo = (int) httpSession.getAttribute("loginUserNo");

        // 서비스 호출 후 값을 반환한다.
        return usersService.getUserInfo( userNo );

    }   // func end


    // 유저 정보 변경
    @PutMapping("/update/info")
    public int updateUserInfo( @RequestBody UsersDto usersDto  ,HttpSession httpSession ){

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if (httpSession == null || httpSession.getAttribute("loginUserNo") == null) {
            // -1 : 세션 없음
            return -1;
        }   // if end

        // userNo 를 가져온다.
        int userNo = (int) httpSession.getAttribute("loginUserNo");

        // 서비스 호출 후 값을 반환한다.
        return usersService.updateUserInfo(usersDto, userNo);

    }   // func end

    // [US-07] 사용자 정보 전체 조회(검색)
    // @author OngTK
    // pjWorker 단에서 인력정보 검색을 위하여 생성
    // businessNo만 일반회원 조회·검색이 가능하며, businessNo 가 존재하는 user은 포함하지 않는다.
    // 250907_추후 채팅기능을 만들 경우, 일반회원끼리 검색이 가능해야하므로 비즈니스session 검색은 삭제
    @GetMapping("/find/search")
    public List<UsersDto> readAllUserInfo(@RequestParam(required = false) String keyword, HttpSession session) {
        System.out.println("UsersController.readUserInfo");
        System.out.println("keyword = " + keyword + ", session = " + session);

        // 로그인 확인
        if( session.getAttribute("loginUserNo") == null){
            return null;
        }
        return usersService.readAllUserInfo(keyword);
    } // func end

    // [US-08] 사용자 정보 개별 조회
    // @author OngTK
    @GetMapping("/find/search/indi")
    public UsersDto readUserInfo(@RequestParam int userNo, HttpSession session){

        // 로그인 확인
        if( session.getAttribute("loginUserNo") == null){
            return null;
        }

        return usersService.readUserInfo(userNo);
    } // func end

}   // class end