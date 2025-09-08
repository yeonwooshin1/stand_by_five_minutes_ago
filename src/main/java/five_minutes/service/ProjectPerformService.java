package five_minutes.service;

import five_minutes.model.dao.ProjectPerformDao;
import five_minutes.model.dto.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class ProjectPerformService {    // class start

    // dao 호출
    private final ProjectPerformDao projectPerformDao ;

    // 프로젝트 행 목록 조회
    public List<ProjectPerformDto> findByProject ( int pjNo ) {

        // 호출한다.
        return projectPerformDao.findByProject( pjNo );
    }   // func end

    // 프로젝트 내 역할 리스트 조회
    public List<RoleLookupDto> findRolesLookup (int pjNo ) {

        // 호출한다.
        return projectPerformDao.findRolesLookup( pjNo );
    }   // func end

    // 프로젝트 내 역할 리스트 조회
    public List<ChkItemLookupDto> findChkItemsLookup (int pjNo ) {

        // 호출한다.
        return projectPerformDao.findChkItemsLookup( pjNo );
    }   // func end


    // 값 저장 서비스
    public List<ProjectPerformDto> saveAll(ReturnProjectPerformDto req, int pjNo) {
        // 1) 생성
        // 값 없으면 넘어가라
        if (req.getCreates() != null) {
            for (ProjectPerformDto d : req.getCreates()) {
                // 수정할 값이니까 pfNo 없으면 continue 오류기 때문
                if (d.getPjRoleNo() <= 0 || d.getPjChkItemNo() <= 0) continue;
                projectPerformDao.insert(d);
            }   // for end
        }   // if end

        // 2) 수정
        // 값 없으면 넘어가라
        if (req.getUpdates() != null) {
            for (ProjectPerformDto d : req.getUpdates()) {
                // 수정할 값이니까 pfNo 없으면 continue 오류기 때문
                if (d.getPfNo() <= 0) continue;
                // 수정할 값이니까 pfNo 없으면 continue 오류기 때문
                if (d.getPjRoleNo() <= 0 || d.getPjChkItemNo() <= 0) continue;
                projectPerformDao.update(d);
            }   // for end
        }   // if end

        // 3) 삭제
        // 값 없으면 넘어가라
        if (req.getDeletes() != null && !req.getDeletes().isEmpty()) {
            projectPerformDao.deleteByIds(req.getDeletes());
        }   // if end

        // 4) 최신 목록 리턴 (방법 C)
        return projectPerformDao.findByProject(pjNo);

    }   // func end

    // @Author OngTK
    // 엑셀출력용 메소드
    public List<ProjectPerformDto> readAllforExcel(int pjNo){
        return projectPerformDao.readAllforExcel(pjNo);
    }


}   // class end
