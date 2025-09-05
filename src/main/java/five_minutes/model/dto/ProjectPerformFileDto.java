package five_minutes.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/// **Info** =========================
///
/// project file Dto
///
/// 프로젝트별 파일을 저장 관리합니다
///
/// Google sheet > 21.테이블 > 4-2
///
/// @author dongjin

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectPerformFileDto {
    private int fileNo;         // 파일번호 10000001~
    private int pfNo;           // 업무No  9000001~
    private String fileName;    // 파일명
    private String createDate;  // 등록일
}
