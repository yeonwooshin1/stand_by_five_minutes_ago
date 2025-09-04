package five_minutes.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class UsersDto { // class start
    private int userNo; // 사용자번호
    private String email;   //  이메일
    private String passwordHash;    // 해시화 비밀번호
    private String userName;        // 이름
    private String userPhone;       // 연락처
    private String roadAddress;     // 도로명 주소
    private String detailAddress;   // 상세주소
    private int userStatus;         // 상태
    private String createDate;      // 가입일
    private String updateDate;      // 수정일
}   // class end
