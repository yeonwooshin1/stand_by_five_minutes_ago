package five_minutes.service;


import five_minutes.model.dao.ProjectWorkerDao;
import five_minutes.model.dto.ProjectWorkerDto;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK

@Service
@RequiredArgsConstructor
public class ProjectWorkerService {

    private final ProjectWorkerDao projectWorkerDao;

    public List<ProjectWorkerDto> getAllPJWorker(int pjNo){
        String s = String.valueOf(pjNo);
        return projectWorkerDao.readAll(s);
    }

} // class end
