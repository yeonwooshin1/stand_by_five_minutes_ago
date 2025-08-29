package five_minutes.model.dao;

import five_minutes.model.dto.CTemDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CTemDao extends Dao {
    // CheckTemplate
    // [1] 체크리스트 템플릿 생성
    public int createCTem(CTemDto cTemDto) {
        try {
            String sql = "insert into checktemplate (ctName , ctDescription , bnNo) values (? , ? , ?) ";
            // 생성된 PK값을 반환하기 위해 제너레이트키 반환
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cTemDto.getCtName());
            ps.setString(2, cTemDto.getCtDescription());
            ps.setString(3, cTemDto.getBnNo());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1);
                }
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    } // func end

    // [2] 체크리스트 템플릿 전체조회
    public List<CTemDto> getCTem(String bnNo) {
        List<CTemDto> list = new ArrayList<>();
        try {
            String sql = "select * from checktemplate where ctStatus = 1 and bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CTemDto cTemDto = new CTemDto();
                cTemDto.setCtNo(rs.getInt("ctNo"));
                cTemDto.setCtName(rs.getString("ctName"));
                cTemDto.setCtDescription(rs.getString("ctDescription"));
                cTemDto.setCtStatus(rs.getInt("ctStatus"));
                cTemDto.setCreateDate(rs.getString("createDate"));
                cTemDto.setUpdateDate(rs.getString("updateDate"));
                cTemDto.setBnNo(rs.getString("bnNo"));
                list.add(cTemDto);
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return list;
    } // func end


    // [3] 체크리스트 템플릿 개별조회
    public CTemDto getIndiCtem(String bnNo, int ctNo) {
        try {
            String sql = "select * from checktemplate where ctStatus = 1 and bnNo = ? and ctNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ps.setInt(2, ctNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CTemDto cTemDto = new CTemDto();
                cTemDto.setCtNo(rs.getInt("ctNo"));
                cTemDto.setCtName(rs.getString("ctName"));
                cTemDto.setCtDescription(rs.getString("ctDescription"));
                cTemDto.setCtStatus(rs.getInt("ctStatus"));
                cTemDto.setCreateDate(rs.getString("createDate"));
                cTemDto.setUpdateDate(rs.getString("updateDate"));
                cTemDto.setBnNo(rs.getString("bnNo"));
                return cTemDto;
            }
            rs.close();
            ps.close();
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return null;
    } // func end

    // [4] 체크리스트 템플릿 수정
    public int updateCTem(CTemDto cTemDto) {
        try {
            String sql = "update checktemplate set ctName = ? , ctDescription = ? where bnNo = ? and ctNo = ? and ctStatus = 1 ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cTemDto.getCtName());
            ps.setString(2, cTemDto.getCtDescription());
            ps.setInt(3, cTemDto.getCtNo());
            ps.setString(4, cTemDto.getBnNo());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    rs.getInt(1);
                }
                rs.close();
                ps.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return 0;
    }

    // [5] 체크리스트 템플릿 삭제
    public int deleteCTem(String bnNo , int ctNo) {
        try{
            String sql = "update checktemplate set ctStatus = 0 where bnNo =? and ctNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql );
            ps.setString(1, bnNo);
            ps.setInt(2, ctNo);
            return ps.executeUpdate();
        } catch (Exception e){
            System.out.println(e);
        } // catch end
        return 0;
    } // func end


} // class end
