package five_minutes.service;

import five_minutes.model.dao.ProjectPerformDao;
import five_minutes.model.dto.ChkItemLookupDto;
import five_minutes.model.dto.ProjectPerformDto;
import five_minutes.model.dto.RoleLookupDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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


}   // class end
