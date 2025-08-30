package five_minutes.model.dao;

import five_minutes.model.dto.RtiDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.function.Predicate;

// Info =========================
// RoleTemplateItem Dao
// 역할 템플릿 대분류를 참조하여 역할 템플릿 소분류를 상세히 정의
// Google sheet > 21.테이블 > 2-2
// Writer : OngTK

@Repository     // Dao 어노테이션
public class RtiDao extends Dao implements CommonDao<RtiDto, Integer, String>{

    // [ RTI-01 ] 상세 역할 템플릿 생성 create()
    @Override
    public int create(RtiDto rtiDto) {
        try{
            // [01-1] sql 작성
            String sql = "insert into RoleTemplateItem(rtNo, rtiName, rtiDescription) values (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, rtiDto.getRtNo());
            ps.setString(2,rtiDto.getRtiName());
            ps.setString(3, rtiDto.getRtiDescription());
            // [01-2] sql 실행
            int count = ps.executeUpdate();
            if(count == 1 ){
                ResultSet rs = ps.getGeneratedKeys();
                // [01-3] 결과 PK 반환
                if(rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            System.out.println("RtiDao.create " + e);
        }
        return 0;
    } // [ RTI-01 ] func end

    // [ RTI-02 ] 상세 역할 템플릿 전체 조회
    @Override
    public List<RtiDto> readAll(String s) {
        try{
            // [02-1] sql 작성
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtiDao.readAll " + e);
        }
        return List.of();
    } // [ RTI-02 ] func end

    // [ RTI-03 ] 상세 역할 템플릿 개별 조회
    @Override
    public RtiDto read(Integer i, String s) {
        try{
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtiDao.read " + e);
        }
        return null;
    } // [ RTI-03 ] func end

    // [ RTI-04 ] 상세 역할 템플릿 수정
    @Override
    public int update(RtiDto dto) {
        try{
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtiDao.update " + e);
        }
        return 0;
    } // [ RTI-04 ] func end

    // [ RTI-05 ] 상세 역할 템플릿 삭제(비활성화)
    @Override
    public int delete(Integer i, String s) {
        try{
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtiDao.delete " + e);
        }
        return 0;
    } // [ RTI-05 ] func end

    // [06] 사업자 등록 번호 조회
    // 해당 레코드의 작성
}   // class end
