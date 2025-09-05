package five_minutes.model.dao;

import five_minutes.model.dto.CTItemDto;
import five_minutes.model.dto.CTemDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CTItemDao extends Dao {
    // CheckTemplateItem 테스트

    // [*] 해당 ctNo가 로그인한 사용자의 bnNo인지 확인
    public boolean searchCtNo(int ctNo, String bnNo) {
        try {
            String sql = "select count(*) from checktemplate where ctNo = ? and bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctNo);
            ps.setString(2, bnNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0; // 체크템플릿아이템 테이블에 레코드를 등록하려면 ctNo가 1개 이상 있어야함
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    // [1] 상세 체크리스트 템플릿 생성
    public int createCTItem(CTItemDto ctItemDto) {
        try {
            String sql = "insert into checktemplateitem ( ctNo , ctiTitle, ctiHelpText ) values ( ? , ? , ? ) ";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, ctItemDto.getCtNo());
            ps.setString(2, ctItemDto.getCtiTitle());
            ps.setString(3, ctItemDto.getCtiHelpText());
            int count = ps.executeUpdate();
            if (count == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    return rs.getInt(1); // ctiNo를 반환
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return 0;
    }

    // [2] 상세 체크리스트 템플릿 전체조회
    public List<CTItemDto> getCTItem(int ctNo) {
        List<CTItemDto> list = new ArrayList<>();
        try {
            String sql = "select * from checktemplateitem where ctiStatus = 1 and ctNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
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
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return list;
    }// func end

    // [3] 상세 체크리스트 템플릿 개별조회
    public CTItemDto getIndiCTItem(int ctNo, int ctiNo) {
        try {
            String sql = "select * from checktemplateitem where ctiStatus = 1 and ctNo = ? and ctiNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctNo);
            ps.setInt(2, ctiNo);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                CTItemDto ctItemDto = new CTItemDto();
                ctItemDto.setCtiNo(rs.getInt("ctiNo"));
                ctItemDto.setCtiTitle(rs.getString("ctiTitle"));
                ctItemDto.setCtiHelpText(rs.getString("ctiHelpText"));
                ctItemDto.setCtiStatus(rs.getInt("ctiStatus"));
                ctItemDto.setCreateDate(rs.getString("createDate"));
                ctItemDto.setUpdateDate(rs.getString("updateDate"));
                ctItemDto.setCtNo(rs.getInt("ctNo"));
                return ctItemDto;
            }
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return null;
    } // func end

    // [4] 상세 체크리스트 템플릿 수정
    // 1. ctiNo(상세체크리스트템플릿번호), ctiTitle, ctiHelpText를 입력받는다.
    // 2. ctiNo가 일치하는 레코드의 DB를 확인한다.
    // 3. 세션에서 bnNo(고용자번호/작성자번호)를 확인 후 DB 레코드를 수정한다.
    public int updateCTItem(CTItemDto ctItemDto){
        try{
            String sql = "update checktemplateitem set ctiTitle = ? , ctiHelpText = ? where ctiNo = ? and ctiStatus = 1 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, ctItemDto.getCtiTitle());
            ps.setString(2, ctItemDto.getCtiHelpText());
            ps.setInt(3, ctItemDto.getCtiNo());
            if (ps.executeUpdate() == 1){
                ps.close();
                return ctItemDto.getCtiNo(); // ctiNo 반환
            }
        } catch (Exception e){
            System.out.println(e);
        }
        return 0;
    } // func end

    // [5] 상세 체크리스트 템플릿 삭제
    // 1. ctiNo를 입력받는다.
    // 2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
    // 3. ctiStatus(상태)를 0으로 변경한다.
    public int deleteCTItem(int ctiNo){
        try{
            String sql = "update checktemplateitem set ctiStatus = 0 where ctiNo = ? and ctiStatus = 1 ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, ctiNo);
            if (ps.executeUpdate() == 1){
                ps.close();
                return ctiNo;
            }
        } catch (Exception e){
            System.out.println(e);
        } // catch end
        return 0;
    } // func end


} // class end
