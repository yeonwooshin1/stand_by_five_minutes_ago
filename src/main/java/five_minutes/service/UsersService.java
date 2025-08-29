package five_minutes.service;

import five_minutes.model.dao.UsersDao;
import five_minutes.model.dto.UsersDto;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class UsersService { // class start
    // usersDao 호출
    private final UsersDao usersDao;
    // csv 비밀번호 확인용 service 호출
    private final CsvPasswordService csvPasswordService;


    // 로그인 서비스
    public Map<String , Object > login(UsersDto usersDto ) {

        // 반환값 객체 생성
        Map<String , Object> result = new HashMap<>();

        // 이메일 유효성 검사 후 변수에 저장, email이 null 이면 null 값 , 아니면 공백제거
        String email = usersDto.getEmail() == null ? null : usersDto.getEmail().trim();
        // 평문 비밀번호 변수에 저장
        String plainPassword = usersDto.getPasswordHash();

        // email과 평문비밀번호가 null 이거나 공백제거 후에 빈 값이라면 실패 반환 - 1차 유효성 검사
        // isBlank() : 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 true 반환
        if( email == null || email.isBlank() || plainPassword == null || plainPassword.isBlank() ) {
            // 실패 반환
            result.put("loginUserNo" , 0 );
            return result;
        }   // if end

        // email 찾는 dao 호출
        int userNo = usersDao.findUserNo(email);

        // email에 맞는 userNo 없을 시 0 -> 실패 반환
        if(userNo == 0) {
            result.put("loginUserNo" , 0 );
            return result;
        }   // if end

        // csv 파일에 있는 해시화된 비밀번호 확인하는 service 호출
        boolean ok = csvPasswordService.matches(userNo, plainPassword);

        // false 일시 0 -> 실패 반환
        if (!ok) {
            result.put("loginUserNo" , 0 );
            return result;
        }   // if end

        // 비밀번호 맞다는 거니까 map에 유저번호 put
        result.put("loginUserNo" , userNo );

        // 유저가 사업자일 수 있으니 사업자번호 찾는 dao 한 번 호출하고 그 값을 put ( 값이 없을 수도 있음 그건 controller 에서 처리 )
        result.put("loginBnNo" ,usersDao.findBsNo(userNo));

        // map 반환
        return result;
    }   // func end



}   // class end
