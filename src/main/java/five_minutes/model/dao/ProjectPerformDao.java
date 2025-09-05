package five_minutes.model.dao;

import five_minutes.model.dto.ChkItemLookupDto;
import five_minutes.model.dto.ProjectPerformDto;

import five_minutes.model.dto.RoleLookupDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ProjectPerformDao extends Dao{    // class start

    // pjPerform 가져오기
    public List<ProjectPerformDto> findByProject (int pjNo ){
        // 빈 리스트 생성
        List<ProjectPerformDto> list = new ArrayList<>();
        try{

            String sql = "select perform.pfNo, perform.pjRoleNo, perform.pjChkItemNo, perform.pfStart, perform.pfEnd, perform.notifyType, " +
                    "perform.notifySetMins, perform.pfStatus from pjPerform as perform " +
                    "inner join pjWorker as role on perform.pjRoleNo = role.pjRoleNo inner join pjChecklistItem as chkItem on perform.pjChkItemNo = chkItem.pjChkItemNo " +
                    "where role.pjNo = ? and chkItem.pjNo = ? order by perform.pfNo asc";

            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , pjNo );
            ps.setInt(2, pjNo);
            ResultSet rs = ps.executeQuery();

            while( rs.next() ){
                ProjectPerformDto dto = new ProjectPerformDto();
                dto.setPfNo(pjNo);
                dto.setPjRoleNo( rs.getInt( "pjRoleNo" ) );
                dto.setPjChkItemNo( rs.getInt( "pjChkItemNo" ) );
                dto.setPfStart( rs.getString( "pfStart" ) );
                dto.setPfEnd( rs.getString( "pfEnd" ) );
                dto.setNotifyType( rs.getInt( "notifyType" ) );
                dto.setNotifySetMins( rs.getInt( "notifySetMins" ) );

                list.add(dto);
            }   // while end
        } catch (Exception e) {   System.out.println("예외 발생"); }
        return list;
    }   // func end


    // 프로젝트에 맞는 역할 레코드 가져오기
    public List<RoleLookupDto> findRolesLookup (int pjNo ){
        // 빈 리스트 생성
        List<RoleLookupDto> list = new ArrayList<>();

        try{
            String sql = " select pjRoleNo, userName as pjUserName , pjRoleName " +
                    "from pjWorker as pj inner join Users as u on pj.userNo = u.userNo where pjNo = ? ";

            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , pjNo );
            ResultSet rs = ps.executeQuery();

            while( rs.next() ){
                RoleLookupDto dto = new RoleLookupDto();
                dto.setPjRoleNo( rs.getInt( "pjRoleNo" ) );
                dto.setPjUserName( rs.getString( "pjUserName" ) );
                dto.setPjRoleName( rs.getString( "pjRoleName" ) );
                list.add(dto);
            }   // while end
        } catch (Exception e) {   System.out.println("예외 발생"); }
        return list;
    }   // func end


    // 프로젝트에 맞는 체크리스트 레코드 가져오기
    public List<ChkItemLookupDto> findChkItemsLookup (int pjNo ){
        // 빈 리스트 생성
        List<ChkItemLookupDto> list = new ArrayList<>();

        try{
            String sql = " select pjChkItemNo , pjChklTitle from PjChecklistItem where pjNo = ? ";

            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , pjNo );
            ResultSet rs = ps.executeQuery();

            while( rs.next() ){
                ChkItemLookupDto dto = new ChkItemLookupDto();
                dto.setPjChkItemNo( rs.getInt( "pjChkItemNo" ) );
                dto.setPjChklTitle( rs.getString( "pjChklTitle" ) );

                list.add(dto);
            }   // while end
        } catch (Exception e) {   System.out.println("예외 발생"); }
        return list;
    }   // func end

}   // class end
