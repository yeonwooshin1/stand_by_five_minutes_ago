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

@Repository     // Dao 어노테이션
public class RtDao extends Dao implements CommonDao<RtDto, Integer, String>{

    // [ RT-01 ] 역할 템플릿 생성 create()
    @Override
    public int create(RtDto rtDto) {
        try {
            // [01-1] sql 작성
            String sql = "insert into RoleTemplate(rtName, rtDescription, bnNo) values (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,rtDto.getRtName());
            ps.setString(2,rtDto.getRtDescription());
            ps.setString(3,rtDto.getBnNo());
            // [01-2] sql 실행
            int count = ps.executeUpdate();
            if (count == 1 ){
                // [01-3] PK 추출
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
            // [02-1] sql 작성
            String sql = "select * from RoleTemplate where bnNo = ? and rtStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ResultSet rs = ps.executeQuery();
            // [02-2] sql 실행 
            while(rs.next()){
                // [02-3] dto 생성
                RtDto rtDto = new RtDto();

                rtDto.setRtNo(rs.getInt("rtNo"));
                rtDto.setBnNo( rs.getString("bnNo"));
                rtDto.setRtName(rs.getString("rtName"));
                rtDto.setRtDescription(rs.getString("rtDescription"));
                rtDto.setRtStatus(rs.getInt("rtStatus"));
                rtDto.setCreateDate(rs.getString("createDate"));
                rtDto.setUpdateDate(rs.getString("updateDate"));

                // [02-4] list에 dto 삽입
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
            // [03-1] sql 작성
            String sql = "select * from RoleTemplate where rtNo = ? and bnNo = ? and rtStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rtNo);
            ps.setString(2, bnNo);
            // [03-2] sql 실행
            ResultSet rs = ps.executeQuery();
            // [03-3] dto 생성 및 반환
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
    public int update(RtDto rtDto) {
        try {
            // [04-1] sql 작성
            String sql = "update RoleTemplate set rtName = ?, rtDescription = ? where rtNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,rtDto.getRtName());
            ps.setString(2,rtDto.getRtDescription());
            ps.setInt(3,rtDto.getRtNo());
            // [04-2] sql 실행
            int count = ps.executeUpdate();
            if( count == 1 ) {
                // [04-3] 결과 PK 반환
                return rtDto.getRtNo();
            }
        } catch (Exception e) {
            System.out.println("RtDao.update" + e);
        }
        return 0;
    } // [ RT-04 ] func end

    // [ RT-05 ] 역할 템플릿 삭제(비활성화) delete()
    @Override
    public int delete(Integer rtNo, String bnNo) {
        try {
            // [05-1] sql 작성
            String sql = "update RoleTemplate set rtStatus = 0 where rtNo = ? and bnNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, rtNo);
            ps.setString(2, bnNo);
            // [05-2] sql 실행
            int count = ps.executeUpdate();
            // [05-3] 결과 PK 반환
            if(count == 1) return rtNo;
        } catch (Exception e) {
            System.out.println("RtDao.delete" + e);
        }
        return 0;
    }// [ RT-05 ]  func end

}   // class end
