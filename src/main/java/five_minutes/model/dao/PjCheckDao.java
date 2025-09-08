package five_minutes.model.dao;

import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import five_minutes.model.dto.PjCheckDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class PjCheckDao extends Dao {

    // [*] pjNo(프로젝트PK)이 로그인한 사용자의 bnNo(세션)인지 확인
    public boolean checkPjNo(int pjNo, String bnNo) {
        try {
            String sql = "SELECT COUNT(*) FROM ProjectInfo WHERE pjNo = ? AND bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setString(2, bnNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
            rs.close(); ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // [1] 프로젝트 체크리스트 추가
    public int createPJCheck(PjCheckDto pjCheckDto) {
        try {
            String sql = "insert into PjChecklistItem ( pjNo , pjChklTitle , pjHelpText ) values ( ? , ? , ? ) ";
            PreparedStatement ps = conn.prepareStatement(sql , PreparedStatement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pjCheckDto.getPjNo());
            ps.setString(2, pjCheckDto.getPjChklTitle());
            ps.setString(3, pjCheckDto.getPjHelpText());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // [2] 프로젝트 체크리스트 목록조회
    public List<PjCheckDto> getPJCheck(int pjNo) {
        List<PjCheckDto> list = new ArrayList<>();
        try {
            String sql = "select * from PjChecklistItem where pjChkIStatus != 0 and pjNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                PjCheckDto pjCheckDto = new PjCheckDto();
                pjCheckDto.setPjChkItemNo(rs.getInt("pjChkItemNo"));
                pjCheckDto.setPjNo(rs.getInt("pjNo"));
                pjCheckDto.setPjChklTitle(rs.getString("pjChklTitle"));
                pjCheckDto.setPjHelpText(rs.getString("pjHelpText"));
                pjCheckDto.setPjChkIStatus(rs.getInt("pjChkIStatus"));
                pjCheckDto.setCreateDate(rs.getString("createDate"));
                pjCheckDto.setUpdateDate(rs.getString("updateDate"));
                list.add(pjCheckDto); // <-- 수정된 부분: 리스트에 DTO 추가
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    // [3] 프로젝트 체크리스트 설명 조회
    public PjCheckDto getInfoPJCheck(int pjNo, int pjChkItemNo) {
        try {
            String sql = "select * from PjChecklistItem where pjChkIStatus != 0 and pjNo = ? and pjChkItemNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjNo);
            ps.setInt(2, pjChkItemNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                PjCheckDto pjCheckDto = new PjCheckDto();
                pjCheckDto.setPjChkItemNo(rs.getInt("pjChkItemNo"));
                pjCheckDto.setPjNo(rs.getInt("pjNo"));
                pjCheckDto.setPjChklTitle(rs.getString("pjChklTitle"));
                pjCheckDto.setPjHelpText(rs.getString("pjHelpText"));
                pjCheckDto.setPjChkIStatus(rs.getInt("pjChkIStatus"));
                pjCheckDto.setCreateDate(rs.getString("createDate"));
                pjCheckDto.setUpdateDate(rs.getString("updateDate"));
                return pjCheckDto;
            }
            rs.close();
            ps.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // [4] 프로젝트 체크리스트 수정
    public int updatePJCheck(PjCheckDto pjCheckDto) {
        try {
            String sql = "update PjChecklistItem set pjChklTitle = ? , pjHelpText = ? where pjChkItemNo = ? and pjChkIStatus != 0 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, pjCheckDto.getPjChklTitle());
            ps.setString(2, pjCheckDto.getPjHelpText());
            ps.setInt(3, pjCheckDto.getPjChkItemNo());
            if (ps.executeUpdate() == 1) {
                return pjCheckDto.getPjChkItemNo();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // [5] 프로젝트 체크리스트 삭제
    public int deletePJCheck(int pjChkItemNo) {
        try {
            String sql = "update PjChecklistItem set pjChkIStatus = 0 where pjChkItemNo = ? and pjChkIStatus != 0 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, pjChkItemNo);
            if (ps.executeUpdate() == 1) {
                return pjChkItemNo;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // [6] 프로젝트 체크리스트 템플릿 전체조회 - 대분류
    public CTemDto getPJCheckTem(int ctNo) {
        try {
            String sql = "select * from checktemplate where ctStatus = 1 and ctNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctNo);
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // [7] 프로젝트 체크리스트 템플릿 전체조회 - 상세
    public List<CTItemDto> getPJCheckItem(int ctNo) {
        List<CTItemDto> list = new ArrayList<>();
        try{
            String sql = "select * from CheckTemplateItem cti join CheckTemplate ct on cti.ctNo = ct.ctNo where cti.ctNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                CTItemDto ctItemDto = new CTItemDto();
                ctItemDto.setCtiNo(rs.getInt("ctiNo"));
                ctItemDto.setCtiTitle(rs.getString("ctiTitle"));
                ctItemDto.setCtiHelpText(rs.getString("ctiHelpText"));
                ctItemDto.setCtiStatus(rs.getInt("ctiStatus"));
                ctItemDto.setCreateDate(rs.getString("createDate"));
                ctItemDto.setUpdateDate(rs.getString("updateDate"));
                ctItemDto.setCtNo(rs.getInt("ctNo"));
                list.add(ctItemDto);
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return list;
    }

    // [8-1] 프로젝트 체크리스트 템플릿 불러오기 (조회)
    public PjCheckDto sumPJCheckTem(int ctiNo) {
        try{
            // 'cti.ctiNo'로 오타 수정
            String sql = "SELECT concat(ct.ctName, '_' , cti.ctiTitle) " +
                            "as pjCheckTitle, ct.ctDescription , cti.ctiHelpText FROM CheckTemplateItem cti JOIN " +
                            "CheckTemplate ct ON cti.ctNo = ct.ctNo " +
                            "WHERE cti.ctiNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctiNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()){
                PjCheckDto pjCheckDto = new PjCheckDto();
                pjCheckDto.setPjChklTitle(rs.getString("pjCheckTitle"));
                pjCheckDto.setCtDescription(rs.getString("ctDescription"));
                pjCheckDto.setCtiHelpText(rs.getString("ctiHelpText"));
                return pjCheckDto;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return null;
    }

    // [8-2] 프로젝트 체크리스트 템플릿 불러오기 (저장)
    public int loadPJCheckTem(PjCheckDto pjCheckDto){
        try{
            String sql = "insert into PjChecklistItem (pjNo, pjChklTitle, pjHelpText) values (?, ?, ?) ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, pjCheckDto.getPjNo());
            // getPjCheckTitle로 수정
            ps.setString(2, pjCheckDto.getPjCheckTitle());
            ps.setString(3, pjCheckDto.getPjHelpText());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return 0;
    }
}