package five_minutes.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor @NoArgsConstructor @Data    // lombok
public class UsersDto { // class start
    private int userNo; // 사용자번호
    @NotBlank(message = "이메일은 필수 입력 항목입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해주세요.")
    private String email;   //  이메일
    @NotBlank(message = "비밀번호는 필수 입력 항목입니다.")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*)[A-Za-z]{8,}$", // 영문과 숫자 정규 표현식
            message = "비밀번호는 8자 이상이며, 영문과 숫자를 포함해야 합니다.")
    @Size(min = 8, max = 20 , message = "비밀번호는 8자 이상 20자 이하여야 합니다.")
    private String passwordHash;    // 해시화 비밀번호
    @NotBlank(message = "이름은 필수 입력 항목입니다.")
    private String userName;        // 이름
    @NotBlank(message = "연락처는 필수 입력 항목입니다.")
    @Size(min = 11 , max = 13 , message = "(-)하이픈을 포함한 연락처를 입력해주세요.")
    private String userPhone;       // 연락처
    @NotBlank(message = "도로명 주소는 필수 입력 항목입니다.")
    private String roadAddress;     // 도로명 주소
    @NotBlank(message = "상세주소는 필수 입력 항목입니다.")
    private String detailAddress;   // 상세주소
    @PositiveOrZero(message = "상태는 1(활성화) 또는 0(비활성화)여야 합니다. ")
    private int userStatus;         // 상태
    private String createDate;      // 가입일
    private String updateDate;      // 수정일
}   // class end
