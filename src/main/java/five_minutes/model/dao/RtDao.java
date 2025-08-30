package five_minutes.model.dao;

import five_minutes.model.dto.RtDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// Info =========================
// RoleTemplate Dao
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// Writer : OngTK
// TODO 옹태경

@Repository     // Dao 어노테이션
public class RtDao extends Dao implements CommonDao<RtDto, Integer, String>{

    // [ RT-01 ] 역할 템플릿 생성 create()
    @Override
    public int create(RtDto rtDto) {
        try {
            String sql = "insert into RoleTemplate(rtName, rtDescription, bnNo) values (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,rtDto.getRtName());
            ps.setString(2,rtDto.getRtDescription());
            ps.setString(3,rtDto.getBnNo());
            int count = ps.executeUpdate();
            if (count == 1 ){
                ResultSet rs = ps.getGeneratedKeys();
                if(rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("RtDao.createRT " + e);
        }
        return 0;
    } // [ RT-01 ]  func end

    // [ RT-02 ] 역할 템플릿 전체 조회 readAll()
    @Override
    public List<RtDto> readAll(String bnNo) {
        List<RtDto> list = new ArrayList<RtDto>();
        try {
            String sql = "select * from RoleTemplate where bnNo = ? and rtStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                RtDto rtDto = new RtDto();

                rtDto.setRtNo(rs.getInt("rtNo"));
                rtDto.setBnNo( rs.getString("bnNo"));
                rtDto.setRtName(rs.getString("rtName"));
                rtDto.setRtDescription(rs.getString("rtDescription"));
                rtDto.setRtStatus(rs.getInt("rtStatus"));
                rtDto.setCreateDate(rs.getString("createDate"));
                rtDto.setUpdateDate(rs.getString("updateDate"));

                list.add(rtDto);
            }
            return list;
        } catch (Exception e) {
            System.out.println("RtDao.read" + e);
        }
        return null;
    } // [ RT-02 ] func end

    // [ RT-03 ] 역할 템플릿 개별 조회  read()
    @Override
    public RtDto read(Integer rtNo, String bnNo) {
        try {
            String sql = "select * from RoleTemplate where rtNo = ? and bnNo = ? and rtStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rtNo);
            ps.setString(2, bnNo);
            ResultSet rs = ps.executeQuery();
            RtDto rtDto = new RtDto();
            if(rs.next()){
                rtDto.setRtNo(rs.getInt("rtNo"));
                rtDto.setBnNo( rs.getString("bnNo"));
                rtDto.setRtName(rs.getString("rtName"));
                rtDto.setRtDescription(rs.getString("rtDescription"));
                rtDto.setRtStatus(rs.getInt("rtStatus"));
                rtDto.setCreateDate(rs.getString("createDate"));
                rtDto.setUpdateDate(rs.getString("updateDate"));
            }
            return rtDto;
        } catch (Exception e) {
            System.out.println("RtDao.read" + e);
        }
        return null;
    } // [ RT-03 ]  func end

    // [ RT-04 ] 역할 템플릿 수정 update()
    @Override
    public int update(RtDto dto) {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.update" + e);
        }
        return 0;
    } // [ RT-04 ] func end

    // [ RT-05 ] 역할 템플릿 삭제(비활성화) delete()
    @Override
    public int delete(Integer rtNo, String bnNo) {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.delete" + e);
        }
        return 0;
    }// [ RT-05 ]  func end

}   // class end
