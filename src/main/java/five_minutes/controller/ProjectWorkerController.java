package five_minutes.controller;


import five_minutes.model.dto.ProjectWorkerDto;
import five_minutes.service.ProjectWorkerService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.graphql.GraphQlProperties;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK

@RestController
@RequestMapping("/project/worker")
@RequiredArgsConstructor
public class ProjectWorkerController {

    private final ProjectWorkerService projectWorkerService;

    // todo OngTK pjWorker


    // [PJW-01] 프로젝트 인력 정보 저장
    // front에서 ProjectWorkerDto 와 변경여부 changeStatus 함께 받아옴
    // 이를 service로 이동한 후 처리결과를 반환
    public List<ProjectWorkerDto> savePJWorker() {
        return null;
    } // func end

    // [PJW-02] 프로젝트 인력 개별 조회

    // [PJW-03] 프로젝트 인력 전체 조회
    @GetMapping
    public List<ProjectWorkerDto> getAllPJWorker(@RequestParam int pjNo, HttpSession session) {
        System.out.println("ProjectWorkerController.getAllPJWorker");
        System.out.println("pjNo = " + pjNo + ", session = " + session);

        // 로그인 확인
        if( session.getAttribute("loginUserNo") == null ||session.getAttribute("loginBnNo") == null){
            return null;
        }
        return projectWorkerService.getAllPJWorker(pjNo);
    } // func end

} // class end
