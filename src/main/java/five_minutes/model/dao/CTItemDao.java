package five_minutes.model.dao;

import five_minutes.model.dto.CTItemDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CTItemDao extends Dao {
    // CheckTemplateItem

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
    public List<CTItemDto> getCTItem() {
        List<CTItemDto> list = new ArrayList<>();
        try {
            String sql = "select * from checktemplateitem where ctStatus = 1 ";
            PreparedStatement ps = conn.prepareStatement(sql);
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
        } catch (Exception e) {
            System.out.println(e);
        } // catch end
        return list;
    }// func end

    // [3] 상세 체크리스트 템플릿 개별조회

    // [4] 상세 체크리스트 템플릿 수정

    // [5] 상세 체크리스트 템플릿 삭제


} // class end
