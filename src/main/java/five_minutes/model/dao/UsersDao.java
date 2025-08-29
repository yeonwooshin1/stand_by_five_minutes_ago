package five_minutes.model.dao;

import org.springframework.stereotype.Repository;

@Repository     // Dao 어노테이션
public class UsersDao extends Dao { // class start


//    // 로그인 - 이메일로 유저번호 찾기
//    public int findUserNo( String getEmail ) {
//         try{
//            String sql = "select userNo from member where mid = ? ";
//            Connection conn = getConnection();
//            PreparedStatement ps = conn.prepareStatement(sql);
//
//            ps.setString(1 , memberDto.getMid());
//            ps.setString(2 , memberDto.getMpwd());
//
//            ResultSet rs = ps.executeQuery();
//
//            if (rs.next()) {
//                return rs.getInt("mno");
//            }
//        } catch (Exception e) {
//            System.out.println(e);
//        }
//        return 0;
//    }
}   // class end
