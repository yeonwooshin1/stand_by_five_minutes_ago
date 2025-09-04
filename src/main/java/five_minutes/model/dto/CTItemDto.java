package five_minutes.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CTItemDto {
    // CheckTemplateItem
    // DB 멤버변수
    @Schema(description = "상세 체크리스트 템플릿 번호", example = "5000001" , minimum = "5000001" , maximum = "5999999")
    @Positive(message = "상세 체크리스트 템플릿 번호는 양수만 가능합니다.")
    private int ctiNo;
    @Schema(description = "상세 체크리스트 템플릿 제목", example = "현장도착")
    @NotBlank(message = "상세 체크리스트 템플릿 제목은 필수 입력 항목입니다.")
    private String ctiTitle;
    @Schema(description = "상세 도움말", example = "현장 도착 후 출근 업무를 위한 세부 작업 1 수행")
    private String ctiHelpText;
    @Schema(description = "상태(1은 활성화, 0은 비활성화)", example = "1" , defaultValue = "1")
    @PositiveOrZero(message = "상태는 1(활성화) 또는 0(비활성화)여야 합니다. ")
    private int ctiStatus;
    @Schema(description = "생성일", example = "2025-08-27 12:30:40")
    private String createDate;
    @Schema(description = "수정일", example = "2025-09-02 12:30:40")
    private String updateDate;
    @Schema(description = "체크리스트 템플릿 번호", example = "4000001" , minimum = "4000001" , maximum = "4999999")
    @Positive(message = "체크리스트 템플릿 번호는 양수만 가능합니다.")
    private int ctNo;

    // DB 없는 멤버변수(외래키의 외래키)
    @Schema(description = "고용자번호(작성자번호)", example = "123-45-67890")
    private String bnNo;
    @Schema(description = "상태(성공, 찾기실패, 로그인실패)", example = "ACCESS_OK")
    private String status; // 세션 실패, 쿼리스트링 실패를 체크할 수 있도록 하는 멤버변수
} // class end
