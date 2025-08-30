package five_minutes.model.dao;


import five_minutes.model.dto.UsersDto;

import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

@Repository     // Dao 어노테이션
public class UsersDao extends Dao { // class start


    // 로그인 - 이메일로 유저번호 찾기
    public int findUserNo( String getEmail ) {
         try{
            // email에 맞는 userNo 가져오기
            String sql = "select userNo from users where email = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setString(1 , getEmail);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // userNo 반환
                return rs.getInt("userNo");
            }   // if end
        } catch (Exception e) {
            System.out.println("예외 발생");
        }

        // 없다면 0 반환
        return 0;
    }   // func end

    // 로그인 - 유저번호로 사업자번호 찾기 => 로그인 성공시 실행.
    public String findBsNo( int userNo ) {
        try{
            // userNo에 맞는 bnNo 가져오기
            String sql = "select bnNo from BusinessUser where userNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setInt(1 , userNo);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                // bnNo 반환
                return rs.getString("bnNo");
            }   // if end
        } catch (Exception e) {
            System.out.println("예외 발생");
        }

        // 없다면 null 반환
        return null;
    }   // func end

    // 이메일찾기 - 이름과 전화번호에 맞는 이메일 반환.
    public String recoverUserEmail(UsersDto usersDto ) {
        try{
            String sql = "select email from Users where userName = ? and userPhone = ?";

            PreparedStatement ps = conn.prepareStatement( sql );

            ps.setString( 1 , usersDto.getUserName());
            ps.setString( 2 , usersDto.getUserPhone() );

            ResultSet rs = ps.executeQuery();
            // email 반환
            if( rs.next() ) return rs.getString("email");

        } catch (Exception e) { System.out.println("예외 발생"); }
        // 없으면 null 반환
        return null;
    }   // func end


}   // class end
