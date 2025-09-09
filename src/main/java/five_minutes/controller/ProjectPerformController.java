package five_minutes.controller;

import five_minutes.model.dto.ProjectPerformDto;
import five_minutes.model.dto.ReturnProjectPerformDto;
import five_minutes.service.ProjectPerformService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController             // 컨트롤러 컴포넌트
@RequiredArgsConstructor    // 의존성 주입
@RequestMapping("/project")
public class ProjectPerformController { // class start

    // 서비스 호출
    private final ProjectPerformService projectPerformService;

    // pjPerform 목록 가져오기
    @GetMapping("/perform")
    public List<ProjectPerformDto> list(@RequestParam int pjNo) {   // class start
        return projectPerformService.findByProject(pjNo);
    }   // func end

    // FK 하는 값들 필요한 것만 가져오기
    @GetMapping("/lookup")
    public Map<String, Object> lookups(@RequestParam int pjNo) {
        return Map.of("roles", projectPerformService.findRolesLookup(pjNo),
                "chkItems", projectPerformService.findChkItemsLookup(pjNo)
        );
    }   // func end

    @PostMapping("/perform")
    public List<ProjectPerformDto> save(@RequestBody ReturnProjectPerformDto req,
                                        @RequestParam int pjNo) {
        return projectPerformService.saveAll(req, pjNo);
    }

}   // class end
