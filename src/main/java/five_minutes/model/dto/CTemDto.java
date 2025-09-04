package five_minutes.model.dto;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.PathVariable;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTemDto {
    // CheckTemplate
    // DB 멤버변수
    // 스웨거는 자동 스트링 타입. example에는 int값도 따옴표 넣기
    @Schema(description = "체크리스트 템플릿 번호", example = "4000001" , minimum = "4000001" , maximum = "4999999")
    @Positive(message = "체크리스트 템플릿 번호는 양수만 가능합니다.")
    private int ctNo;
    @Schema(description = "체크리스트 템플릿 이름", example = "출근확인")
    @NotBlank(message = "체크리스트 템플릿 이름은 필수 입력 항목입니다.")
    private String ctName;
    @Schema(description = "체크리스트 템플릿 설명", example = "출근 최소 5분 전까지는 출장장소에 출근 여부 확인")
    private String ctDescription;
    @Schema(description = "상태(1은 활성화, 0은 비활성화)", example = "1" , defaultValue = "1")
    @PositiveOrZero(message = "상태는 1(활성화) 또는 0(비활성화)여야 합니다. ")
    private int ctStatus;
    @Schema(description = "생성일", example = "2025-08-25 12:30:40")
    private String createDate;
    @Schema(description = "수정일", example = "2025-08-30 12:30:40")
    private String updateDate;
    @Schema(description = "고용자번호(작성자번호)", example = "123-45-67890")
    private String bnNo;

    // DB 없는 멤버변수
    @Schema(description = "상태(성공, 찾기실패, 로그인실패)", example = "ACCESS_OK")
    private String status;  // 세션 실패, 쿼리스트링 실패를 체크할 수 있도록 하는 멤버변수

} // class end
