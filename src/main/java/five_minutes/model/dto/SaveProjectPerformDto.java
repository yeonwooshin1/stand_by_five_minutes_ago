package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;


@AllArgsConstructor
@NoArgsConstructor
@Data    // lombok
public class SaveProjectPerformDto {    // class start

    private List<ProjectPerformDto> creates;   // 신규 추가
    private List<ProjectPerformDto> updates;   // 수정
    private List<Integer>           deletes;   // 삭제할 pk 목록

}   // class end
