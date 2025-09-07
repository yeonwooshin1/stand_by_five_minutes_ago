package five_minutes.service;

import five_minutes.model.dao.UsersDao;
import five_minutes.model.dto.ChangePasswordDto;
import five_minutes.model.dto.EmailRecoverDto;
import five_minutes.model.dto.UsersDto;

import five_minutes.util.JwtUtil;
import five_minutes.util.PasswordValidatorUtil;
import five_minutes.util.PhoneNumberUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
        String email = usersDto.getEmail() == null ? null : usersDto.getEmail().trim().toLowerCase();
        // 평문 비밀번호 변수에 저장
        String plainPassword = usersDto.getPasswordHash() == null ? null : usersDto.getPasswordHash().trim();

        // email과 평문비밀번호가 null 이거나 공백제거 후에 빈 값이라면 실패 반환 - 1차 유효성 검사
        // isBlank() : 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 true 반환
        if( email == null || email.isBlank() || plainPassword == null || plainPassword.isBlank() ) {
            // 실패 반환
            result.put("loginUserNo" , 0 );
            return result;
        }   // if end

        // userNo 찾는 dao 호출
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

    // 이메일찾기 서비스
    public EmailRecoverDto recoverUserEmail( UsersDto usersDto ) {

        // 유효성 검사 => userDto가 null 인지 전화번호 형식이 맞는지 확인, userName이 유효한 값인지 확인
        if ( usersDto == null || !PhoneNumberUtil.isValid(usersDto.getUserPhone()) || usersDto.getUserName() == null || usersDto.getUserName().trim().isEmpty()) {
            return new EmailRecoverDto(false , "" );
        }   // if end

        // dao 호출 후 email 값 받기
        String getEmail = usersDao.recoverUserEmail(usersDto);

        // email이 null 이면 실패반환 null 이 아니라면 true에 getEmail 반환
        return (getEmail == null || getEmail.trim().isEmpty())?
                new EmailRecoverDto(false , "" ) : new EmailRecoverDto(true , getEmail );

    }   // func end


    // 비밀번호 변경 서비스
    public int updatePassword(ChangePasswordDto changePasswordDto , int userNo){

        // 멤버변수 공백제거 후 변수 지정
        String currentPassword = changePasswordDto.getCurrentPassword() == null? null : changePasswordDto.getCurrentPassword().trim();
        String newPassword = changePasswordDto.getNewPassword() == null? null : changePasswordDto.getNewPassword().trim();
        String confirmPassword = changePasswordDto.getConfirmPassword() == null? null : changePasswordDto.getConfirmPassword().trim();

        // 얘네들 값이 null 이거나 비어있으면 실패 반환
        if(newPassword == null || newPassword.isBlank() || confirmPassword == null || confirmPassword.isBlank()) {
            return 0;
        }   // if end

        // 새 비밀번호랑 일치용 새 비밀번호랑 일치하는지 확인
        if(!newPassword.equals(confirmPassword)){
            // 일치 안하면 -1 => 두 개 일치 하지 않는다 반환
            return -1;
        }   // if end

        // 확인용 비밀번호 형식 유효성 검사
        // isBlank() : 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 0 반환
        if( currentPassword == null || currentPassword.trim().isBlank() ) {
            // 실패 반환
            return 0;
        }   // if end

        // 기존 비밀번호와 새 비밀번호가 일치하면 못 변경하게 함.
        if(currentPassword.equals(newPassword)) return -4;

        // 비밀번호 8글자 대소문자인지 확인하는 유효성 검사
        if (!PasswordValidatorUtil.isValid(newPassword)) return -3;

        // csv 파일에 있는 해시화된 비밀번호 확인하는 service 호출
        boolean ok = csvPasswordService.changePassword(userNo, currentPassword , newPassword);

        // boolean 반환값에 따라 다른 반환
        if (ok) { return 1; }
        else { return -2; } // 비밀번호가 다름. -2 반환

    }   // func end


    // 유저정보 조회 서비스
    public UsersDto getUserInfo( int userNo ){
        return usersDao.getUserInfo(userNo);
    }   // func end


    // 유저정보 수정 서비스
    public int updateUserInfo(UsersDto usersDto , int userNo ) {

        // 가져온 dto 값들 유효성 검사용 , null 이면 null 값 주기 아니라면 공백제거해서 가져오기
        String userName = usersDto.getUserName() == null ? null : usersDto.getUserName().trim();
        String roadAddress = usersDto.getRoadAddress() == null ? null : usersDto.getRoadAddress().trim();
        String detailAddress = usersDto.getDetailAddress() == null ? null : usersDto.getDetailAddress().trim();
        String userPhone = usersDto.getUserPhone() == null? null : usersDto.getUserPhone().trim();

        // 값이 없으면 -3 반환 => 유저 정보가 모두 있어야 한다는 전제조건이 깔림.
        if ( userName == null || roadAddress == null || detailAddress == null || userPhone == null ||
            userName.isBlank() || roadAddress.isBlank() ||  detailAddress.isBlank() || userPhone.isBlank() ) {
            return -3;
        }   // if end

        // 번호가 null 이 아니면서 형식 검사에서 틀리면 -2 반환 => 형식 오류
        if( !PhoneNumberUtil.isValid(userPhone) ) return -2;

        // 보낼 dto 새로 만들기 => 생성자에 넣기
        UsersDto dto = new UsersDto(userNo , null , null , userName , userPhone , roadAddress
                , detailAddress , 1 , null , null);

        // dao 호출해서 값 반환 => true면 1 , 아니면 0 반환
        return usersDao.updateUserInfo(dto) ? 1 : 0 ;

    }   // func end

    // [*] JwtUtil DI
    private final JwtUtil jwtUtil;

    // [*] 로그인 시 토큰 생성 메소드
    public String createLoginToken(int loginUserNo) {
        // UUID를 jti로 사용
        String jti = UUID.randomUUID().toString();
        // 컨트롤러에서 받은 토큰 확인
        String token = jwtUtil.createToken(String.valueOf(loginUserNo), jti);
        // 토큰 값 확인
        System.out.println("발급된 토큰 : " + token);
        // 토큰 반환
        return token;
    } // func end

    // [US-07] 사용자 정보 조회(검색)
    // @author OngTK
    // pjWorker 단에서 인력정보 검색을 위하여 생성
    // businessNo만 일반회원 조회·검색이 가능하며, businessNo 가 존재하는 user은 포함하지 않는다.
    // todo OngTK 사용자 정보 조회(검색)
    public List<UsersDto> readUserInfo(String keyword) {
        return usersDao.readUserInfo(keyword);
    } // func end

}   // class end
