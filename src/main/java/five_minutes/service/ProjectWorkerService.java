package five_minutes.service;


import five_minutes.model.dao.ProjectWorkerDao;
import five_minutes.model.dto.ProjectWorkerDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK

@Service
@RequiredArgsConstructor
public class ProjectWorkerService {

    private final ProjectWorkerDao projectWorkerDao;

    public List<ProjectWorkerDto> getAllPJWorker(int pjNo) {
        String s = String.valueOf(pjNo);
        return projectWorkerDao.readAll(s);
    }

    public List<Map<String, Integer>> savePJWorker(List<ProjectWorkerDto> list) {
        List<Map<String, Integer>> resultList = new ArrayList<>();

        for (ProjectWorkerDto dto : list) {
            Map<String, Integer> resultMap = new HashMap<>();
            // pjRoleNo가 null일 수 있는 상황 방지
            resultMap.put("pjRoleNo", dto.getPjRoleNo() != 0 ? dto.getPjRoleNo() : 0);

            // changeStatue
            if (dto.getChangeStatus() == 0) {               // 0 원본 유지, 변경없음
                resultMap.put("Result", dto.getPjRoleNo() != 0 ? dto.getPjRoleNo() : 0);
            } else if (dto.getChangeStatus() == 1) {        // 1 신규 데이터-create
                int createdId = projectWorkerDao.create(dto);
                resultMap.put("Result", createdId);
            } else if (dto.getChangeStatus() == 2) {        // 2 신규데이터-delete
                resultMap.put("Result", dto.getPjRoleNo() != 0 ? dto.getPjRoleNo() : 0);
            } else if (dto.getChangeStatus() == 3) {        // 3 기존 DB 수정
                int updatedId = projectWorkerDao.update(dto);
                resultMap.put("Result", updatedId);
            } else if (dto.getChangeStatus() == 4) {        // 4 삭제
                int deletedId = projectWorkerDao.delete( dto.getPjRoleNo() , "");
                resultMap.put("Result", deletedId);
            } else {
                resultMap.put("Result", 0);
            }
            resultList.add(resultMap);
        }
        return resultList;
    }

} // class end
