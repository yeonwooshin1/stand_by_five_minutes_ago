package five_minutes.model.dao;

import five_minutes.model.dto.RtDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

// Info =========================
// RoleTemplate Dao
// 역할 템플릿 대분류를 정의
// Google sheet > 21.테이블 > 2-1
// 작성자 : OngTK

@Repository     // Dao 어노테이션
public class RtDao extends Dao {

    // [ RT-01 ] 역할 템플릿 생성 createRT()
    public int createRT(RtDto rtDto) {
        try {
            String sql = "insert into RoleTemplete(rtName, rtDescription, bnNo) values (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1,rtDto.getRtName());
            ps.setString(2, rtDto.getRtDescription());
            ps.setInt(3,rtDto.getBnNo());
            int count = ps.executeUpdate();
            if (count == 1 ){

            }
        } catch (Exception e) {
            System.out.println("RtDao.createRT " + e);
        }
        return 0;
    }// [ RT-01 ]  func end

    // [ RT-02 ] 역할 템플릿 전체 조회 getRT()
    public List<RtDto> getRT() {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.getRT " + e);
        }
        return null;
    }// [ RT-02 ]  func end

    // [ RT-03 ] 역할 템플릿 개별 조회 getIndiRT()
    public RtDto getIndiRT() {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.getIndiRT " +e );
        }
        return null;
    }// [ RT-03 ]  func end

    // [ RT-04 ] 역할 템플릿 수정 updateRT()
    public int updateRT() {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.updateRT " +e);
        }
        return 0;
    }// [ RT-04 ]  func end

    // [ RT-05 ] 역할 템플릿 삭제(비활성화) deleteRT()
    public int deleteRT() {
        try {
            String sql = "";
        } catch (Exception e) {
            System.out.println("RtDao.deleteRT " +e);
        }
        return 0;
    }// [ RT-05 ]  func end

}   // class end
