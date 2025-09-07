package five_minutes.controller;


import five_minutes.model.dto.ProjectWorkerDto;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK

@RestController
@RequestMapping("/project/worker")
public class ProjectWorkerController {

    // todo OngTK pjWorker

    // [PJW-01] 프로젝트 인력 정보 저장
    // front에서 ProjectWorkerDto 와 변경여부 changeStatus 함께 받아옴
    // 이를 service로 이동한 후 처리결과를 반환
    public List<ProjectWorkerDto> savePJWorker() {
        return null;
    } // func end

    // [PJW-02] 프로젝트 인력 개별 조회

    // [PJW-03] 프로젝트 인력 전체 조회


} // class end
