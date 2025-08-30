package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
public class EmailRecoverDto {  // class start
    private boolean checkSuccess;   // 성공여부
    private String getEmail;        // 성공일 경우 이메일
}   // class end
