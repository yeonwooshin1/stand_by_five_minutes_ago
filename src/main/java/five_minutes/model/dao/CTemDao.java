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
    // 1. ctName(체크리스트 템플릿 이름)과 ctDescription(체크리스트 템플릿 설명)을 입력받는다.
    // 2. 세션에서 bnNo(고용자번호/작성자번호)를 확인한다.
    // 3. DB에 저장한다.

    public int createCTem(CTemDto cTemDto) {
        try {
            String sql = "insert into checktemplate (ctName , ctDescription , bnNo) values (? , ? , ?) ";
            // 생성된 PK값을 반환하기 위해 제너레이트키 반환
            PreparedStatement ps = conn.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, cTemDto.getCtName());
            ps.setString(2, cTemDto.getCtDescription());
            ps.setString(3, cTemDto.getBnNo());
            if (ps.executeUpdate() == 1) {
                ResultSet rs = ps.getGeneratedKeys();
                if (rs.next()){
                    return rs.getInt(1);
                }
                rs.close(); ps.close();
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    } // func end

    // [2] 체크리스트 템플릿 전체조회
    public List<CTemDto> getCTem(){
        List<CTemDto> list = new ArrayList<>();
        try{
            String sql = "select * from checktemplate where ctStatus = 1 and bnNo = ? ";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, );
        } catch (Exception e){
            System.out.println(e);
        } // catch end
    } // func end


    // [3] 체크리스트 템플릿 개별조회

    // [4] 체크리스트 템플릿 수정

    // [5] 체크리스트 템플릿 삭제


} // class end
