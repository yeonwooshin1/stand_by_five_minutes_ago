package five_minutes.model.dao;

import five_minutes.model.Repository.CommonRepository;
import five_minutes.model.dto.ProjectWorkerDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

/// **info** ============================
///
/// 프로젝트 인력 관리
///
/// @author OngTK
///

@Repository
public class ProjectWorkerDao extends Dao implements CommonRepository<ProjectWorkerDto, Integer, String> {

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
        List<ProjectWorkerDto> list = new ArrayList<>();
        try{
            String sql = "select w.pjNo, w.pjRoleNo, w.userNo, w.pjroleName, w.pjroleDescription, w.pjRoleLv, w.createDate, w.updateDate, u.userName, u.userPhone, u.roadAddress from pjworker w inner join users u on w.userNo=u.userNo where w.pjno=? and w.pjRoleStatus=1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1,s);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                ProjectWorkerDto projectWorkerDto = new ProjectWorkerDto();
                projectWorkerDto.setPjNo(rs.getInt("pjNo"));
                projectWorkerDto.setPjRoleNo(rs.getInt("pjRoleNo"));
                projectWorkerDto.setUserNo(rs.getInt("userNo"));
                projectWorkerDto.setPjRoleName(rs.getString("pjRoleName"));
                projectWorkerDto.setPjRoleDescription(rs.getString("pjRoleDescription"));
                projectWorkerDto.setPjRoleLv(rs.getInt("pjRoleLv"));
                projectWorkerDto.setCreateDate(rs.getString("createDate"));
                projectWorkerDto.setUpdateDate(rs.getString("updateDate"));
                projectWorkerDto.setUserName(rs.getString("userName"));
                projectWorkerDto.setUserPhone(rs.getString("userPhone"));
                projectWorkerDto.setRoadAddress(rs.getString("roadAddress"));

                list.add(projectWorkerDto);
            }
            return list;
        }catch (Exception e ){
            System.out.println("UsersDao.readUserInfo " +e);
        }
        return null;
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
