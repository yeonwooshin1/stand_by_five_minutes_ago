package five_minutes.controller;

import five_minutes.model.dto.ChangePasswordDto;
import five_minutes.model.dto.EmailRecoverDto;
import five_minutes.model.dto.UsersDto;
import five_minutes.service.UsersService;

import five_minutes.util.JwtUtil;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/user")
public class UsersController {  // class start

    // service
    private final UsersService usersService;

    // @Valid : DTO에서 어노테이션으로 유효성 검사 추가한 내용을 스프링에서 확인할 수 있도록 하는 어노테이션
    // 로그인
    @PostMapping("/login")
    public int login(@Valid @RequestBody UsersDto usersDto, HttpSession httpSession) {
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
        createLoginToken(loginUserNo);

        // 성공 시 userNo 반환
        return loginUserNo;
    }   // func end

    // [*] JwtUtil DI
    private final JwtUtil jwtUtil;

    // [*] 로그인 시 토큰 생성 메소드
    public String createLoginToken(int loginUserNo) {
        // UUID를 jti로 사용
        String jti = UUID.randomUUID().toString();
        // 토큰 값 확인
        System.out.println(jwtUtil.createToken(String.valueOf(loginUserNo), jti));
        // JwtUtil로 토큰 생성
        return jwtUtil.createToken(String.valueOf(loginUserNo), jti);
    } // func end

    // 로그아웃
    @GetMapping("/logout")
    public int logout(HttpSession httpSession) {

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if (httpSession == null || httpSession.getAttribute("loginUserNo") == null) {
            return 0;
        }   // if end

        // 로그인 중이라면 세션 값을 제거한다.
        httpSession.removeAttribute("loginUserNo");

        // 사업자번호도 null이 아니면 같이 제거한다.
        if (httpSession.getAttribute("loginBnNo") != null) {
            httpSession.removeAttribute("loginBnNo");
        }   // if end

        // 로그아웃 성공 반환
        return 1;
    }   // func end

    // 이메일찾기 (=id)
    @GetMapping("/recoverEmail")
    public EmailRecoverDto recoverUserEmail(@Valid UsersDto usersDto) {

        // 서비스 호출해서 유효성 검사 후 반환
        return usersService.recoverUserEmail(usersDto);

    }   // func end

    // 비밀번호 변경
    @PutMapping("/update/password")
    public int updatePassword(@Valid @RequestBody ChangePasswordDto changePasswordDto, HttpSession httpSession) {

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if (httpSession == null || httpSession.getAttribute("loginUserNo") == null) {
            return -2;
        }   // if end

        // userNo 를 가져온다.
        int userNo = (int) httpSession.getAttribute("loginUserNo");

        // 서비스 호출해서 유효성 검사 후 반환
        return usersService.updatePassword(changePasswordDto, userNo);

    }   // func end

    // 유저 정보 조회
    @GetMapping("/find/info")
    public UsersDto getUserInfo(@Valid HttpSession httpSession) {

        // 세션 확인해서 null 이면 애초에 비로그인이니까 세션 없음 반환
        if (httpSession == null || httpSession.getAttribute("loginUserNo") == null) {
            return null;
        }   // if end

        // userNo 를 가져온다.
        int userNo = (int) httpSession.getAttribute("loginUserNo");

        // 서비스 호출 후 값을 반환한다.
        return usersService.getUserInfo(userNo);

    }   // func end


    // 유저 정보 변경
    @PutMapping("/update/info")
    public int updateUserInfo(@Valid @RequestBody UsersDto usersDto, HttpSession httpSession) {

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


}   // class end