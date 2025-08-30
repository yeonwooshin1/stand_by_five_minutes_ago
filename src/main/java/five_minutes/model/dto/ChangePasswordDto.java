package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok

public class ChangePasswordDto {

    private String currentPassword; // 현재 비밀번호
    private String newPassword;     // 새 비밀번호
    private String confirmPassword; // 새 비밀번호 일치용

}
