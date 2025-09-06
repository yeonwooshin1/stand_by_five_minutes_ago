package five_minutes.model.dao;

import five_minutes.model.Repository.CommonRepository;
import five_minutes.model.dto.ProjectWorkerDto;

import java.util.List;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK
///

public class ProjectWorkerDao implements CommonRepository<ProjectWorkerDto, Integer, String> {

    // 저장 - 생성
    @Override
    public int create(ProjectWorkerDto dto) {
        try{

        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return 0;
    } // func end

    // 개별 조회
    @Override
    public ProjectWorkerDto read(Integer integer, String s) {
        try{

        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return null;
    } // func end

    // 전체 조회
    @Override
    public List<ProjectWorkerDto> readAll(String s) {
        try{

        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return List.of();
    } // func end
    
    // 저장 - 수정
    @Override
    public int update(ProjectWorkerDto dto) {
        try{

        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return 0;
    } // func end

    // 저장 - 삭제(비활성화)
    @Override
    public int delete(Integer integer, String s) {
        try{

        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return 0;
    } // func end

} // class end
