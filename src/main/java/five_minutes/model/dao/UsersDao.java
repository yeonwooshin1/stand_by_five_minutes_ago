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

    // 유저정보 찾기
    public UsersDto getUserInfo( int userNo ){
        try{
            // 이메일 , 이름 , 폰 , 도로명주소 , 상세주소 , 가입날짜 보여줌
            String sql = "select email, userName, userPhone, roadAddress, detailAddress, createDate from Users where userNo = ?";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setInt( 1 , userNo );
            ResultSet rs = ps.executeQuery();
            if( rs.next() ){
                UsersDto usersDto  = new UsersDto(); // 패스워드 제외한
                usersDto.setEmail( rs.getString( "email" ) );
                usersDto.setUserName( rs.getString( "userName" ) );
                usersDto.setUserPhone( rs.getString( "userPhone" ) );
                usersDto.setRoadAddress( rs.getString( "roadAddress" ) );
                usersDto.setDetailAddress( rs.getString( "detailAddress" ) );
                usersDto.setCreateDate( rs.getString( "createDate" ) );
                return usersDto;
            }   // if end
        } catch (Exception e) {   System.out.println("예외 발생");   }
        // 없으면 null 반환
        return null;
    }   // func end

    // 유저 정보 수정
    public boolean updateUserInfo( UsersDto usersDto ){
        try{
            String sql ="update Users set userName = ?, roadAddress = ? , detailAddress = ? , userPhone = ? where userNo = ? ";
            PreparedStatement ps = conn.prepareStatement( sql );
            ps.setString( 1 , usersDto.getUserName() );
            ps.setString( 2 , usersDto.getRoadAddress());
            ps.setString( 3 , usersDto.getDetailAddress());
            ps.setString( 4 , usersDto.getUserPhone());

            ps.setInt( 5, usersDto.getUserNo() );

            int count = ps.executeUpdate();
            // 업데이트 된 행이 1개면 성공
            return count == 1;
        } catch (Exception e) { System.out.println(e);  }
        // 아니라면 실패
        return false;
    }   // func end



}   // class end
