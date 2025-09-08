package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor@NoArgsConstructor
@Data
public class ReturnProjectPerformDto {  // class start

    private List<ProjectPerformDto> creates;   // 신규 추가할 행 목록
    private List<ProjectPerformDto> updates;   // 수정할 행 목록 (pfNo 필수)
    private List<Integer>           deletes;   // 삭제할 pfNo 목록 (기존행만)

}   // class end

