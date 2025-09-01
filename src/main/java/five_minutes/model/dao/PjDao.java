package five_minutes.model.dao;

import five_minutes.model.dto.PjDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

@Repository     // Dao 어노테이션
public class PjDao extends Dao {    // class start


    // 프로젝트 info
    public int createProjectInfo(PjDto pjDto , String bnNo ) {
        try{
            // 프로젝트에 필요한 정보들 insert
            String sql = "insert into ProjectInfo( pjName, pjMemo, pjStartDate, pjEndDate, roadAddress, detailAddress" +
                    ", clientName, clientPhone, clientMemo , bnNo )values(?,?,?,?,?,?,?,?,?,?)";

            // SQL 기재한다. + ***auto_increment(자동 PK)값 결과를 반환 설정***
            PreparedStatement ps = conn.prepareStatement( sql , Statement.RETURN_GENERATED_KEYS );

            ps.setString( 1 , pjDto.getPjName() );     ps.setString( 2 , pjDto.getPjMemo() );
            ps.setString( 3 , pjDto.getPjStartDate() );   ps.setString( 4, pjDto.getPjEndDate() );
            ps.setString( 5 , pjDto.getRoadAddress() );   ps.setString( 6, pjDto.getDetailAddress() );
            ps.setString( 7 , pjDto.getClientName() );   ps.setString( 8, pjDto.getClientPhone() );
            ps.setString( 9 , pjDto.getClientMemo() );   ps.setString( 10, bnNo );

            // 4. 기재된 sql 실행 한 결과 레코드 저장 개수 반환
            int count = ps.executeUpdate();
            if( count == 1 ){
                // 5. auto_increment 로 자동 할당된 pk값 반환하여 rs 로 조작하기
                ResultSet rs = ps.getGeneratedKeys();
                if( rs.next() ){ // 자동 할당된 pk 값중에 첫번째 pk값 으로 이동
                    int pjNo = rs.getInt( 1 ); // pk값 가져오기
                    return pjNo; // 프로젝트 번호 반환
                }   // if end
            }   // if end
        } catch (Exception e) { System.out.println("예외발생"); }
            return 0; // 프로젝트 insert 실패시 0 반환한다.
    }   // func end

}   // class end
