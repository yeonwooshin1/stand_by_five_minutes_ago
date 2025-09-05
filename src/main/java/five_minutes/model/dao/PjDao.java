package five_minutes.model.dao;

import five_minutes.model.Repository.CommonRepository;
import five_minutes.model.dto.PjDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

///  **info** ================
///
/// 프로젝트 정보를 처리하는 DAO
///
/// @author yeonwooshin1
/// @author OngTk


@Repository     // Dao 어노테이션
public class PjDao extends Dao implements CommonRepository<PjDto, Integer, String> {    // class start

    @Override
    public int create(PjDto pjDto) {
        try{
            // 프로젝트에 필요한 정보들 insert
            String sql = "insert into ProjectInfo( pjName, pjMemo, pjStartDate, pjEndDate, roadAddress, detailAddress" +
                    ", clientName, clientPhone, clientMemo , bnNo , clientRepresent )values(?,?,?,?,?,?,?,?,?,?,?)";

            // SQL 기재한다. + ***auto_increment(자동 PK)값 결과를 반환 설정***
            PreparedStatement ps = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );

            ps.setString( 1 , pjDto.getPjName() );     ps.setString( 2 , pjDto.getPjMemo() );
            ps.setString( 3 , pjDto.getPjStartDate() );   ps.setString( 4, pjDto.getPjEndDate() );
            ps.setString( 5 , pjDto.getRoadAddress() );   ps.setString( 6, pjDto.getDetailAddress() );
            ps.setString( 7 , pjDto.getClientName() );   ps.setString( 8, pjDto.getClientPhone() );
            ps.setString( 9 , pjDto.getClientMemo() );   ps.setString( 10, pjDto.getBnNo() );
            ps.setString(11 , pjDto.getClientRepresent());

            // 4. 기재된 sql 실행 한 결과 레코드 저장 개수 반환
            int count = ps.executeUpdate();
            if( count == 1 ){
                // 5. auto_increment 로 자동 할당된 pk값 반환하여 rs 로 조작하기
                ResultSet rs = ps.getGeneratedKeys();
                if( rs.next() ){ // 자동 할당된 pk 값중에 첫번째 pk값 으로 이동
                    return rs.getInt( 1 ); // pk값 가져오기
                }   // if end
            }   // if end
        } catch (Exception e) {
            System.out.println("PjDao.create "+e);
        }
        return 0; // 프로젝트 insert 실패시 0 반환한다.
    } // func end

    // 개별조회
    @Override
    public PjDto read(Integer integer, String s) {
        try{
            String sql = "select * from ProjectInfo where bnNo = ?";
        } catch (Exception e) {
            System.out.println("PjDao.read " + e);
        }
        return null;
    }

    // 전체 조회
    @Override
    public List<PjDto> readAll(String bnNo) {
        List<PjDto> list = new ArrayList<>();
        try{
            String sql = "select * from ProjectInfo where bnNo = ? and pjStatus = 1";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, bnNo);
            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                PjDto pjDto = new PjDto();
                pjDto.setPjNo(rs.getInt("pjNo"));                           // 프로젝트번호
                pjDto.setPjName(rs.getString("pjName"));                    // 프로젝트명
                pjDto.setPjMemo(rs.getString("pjMemo"));                    // 당사 메모
                pjDto.setPjStartDate(rs.getString("pjStartDate"));          // 시작날짜
                pjDto.setPjEndDate(rs.getString("pjEndDate"));              // 종료날짜
                pjDto.setRoadAddress(rs.getString("roadAddress"));          // 도로명 주소
                pjDto.setDetailAddress(rs.getString("detailAddress"));      // 상세주소
                pjDto.setClientName(rs.getString("clientName"));            // 클라이언트명
                pjDto.setClientRepresent(rs.getString("clientRepresent"));  // 클라이언트 담당자
                pjDto.setClientPhone(rs.getString("clientPhone"));          // 클라이언트연락처
                pjDto.setClientMemo(rs.getString("clientMemo"));            // 업무요청사항
                pjDto.setPjStatus(rs.getString("pjStatus"));                // 상태
                pjDto.setCreateDate(rs.getString("createDate"));            // 생성일
                pjDto.setUpdateDate(rs.getString("updateDate"));            // 수정일
                pjDto.setBnNo(rs.getString("bnNo"));                        // 사업자 번호
                list.add(pjDto);
            }
            return list;
        } catch (Exception e) {
            System.out.println("PjDao.readAll " + e);
        }
        return null;
    } // func end

    // 수정
    @Override
    public int update(PjDto dto) {
        try{
            String sql = "";
        } catch (Exception e) {
            System.out.println("PjDao.update " + e);
        }
        return 0;
    }

    // 삭제(비활성화)
    @Override
    public int delete(Integer integer, String s) {
        try{
            String sql = "";
        } catch (Exception e) {
            System.out.println("PjDao.delete " + e);
        }
        return 0;
    }
}   // class end
