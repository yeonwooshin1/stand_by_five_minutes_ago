package five_minutes.service;

import five_minutes.model.dao.CsvPasswordService;
import five_minutes.model.dao.UsersDao;
import five_minutes.model.dto.UsersDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.Map;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class UsersService { // class start
    // dao
    private final UsersDao usersDao;

    // csv 비밀번호 확인용 service 호출
    private final CsvPasswordService csvPasswordService;



    // 세션에 임시 loginUserNo, loginBnNo 저장
    @GetMapping("/testLogin")
    public void login(HttpSession session) {
        // 강제로 로그인 값 세팅
        session.setAttribute("loginUserNo", 1000001);  // 테스트용 회원 번호
        session.setAttribute("loginBnNo", "100-00-00001"); // 테스트용 사업자 번호
    }


//    public Map<String , Object > login(UsersDto usersDto ) {
//
//        // 반환값 객체 생성
//        Map<String , Object> result = new HashMap<>();
//
//        // 이메일 유효성 검사 후 변수에 저장, email이 null이면 null 값 , 아니면 공백제거
//        String email = usersDto.getEmail() == null ? null : usersDto.getEmail().trim();
//        // 평문 비밀번호 변수에 저장
//        String plainPassword = usersDto.getPasswordHash();
//
//        // email과 평문비밀번호가 null 이거나 공백제거 후에 빈 값이라면 실패 반환 - 1차 유효성 검사
//        // isBlank() : 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 true 반환
//        if( email == null || email.isBlank() || plainPassword == null || plainPassword.isBlank() ) {
//            // 실패 반환
//            result.put("loginUserNo" , 0 );
//            return result;
//        }   // if end
//
//        int userNo = usersDao.findUserNo(email);
//
//    }   // func end



}   // class end
