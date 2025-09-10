package five_minutes.model.dao;

import five_minutes.model.dto.ChatRoomDto;
import five_minutes.model.dto.ChatRoomUserDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p> info =======================
 * <p> Chat방에 들어온 user을 관리
 *
 * @author OngTK
 * @since 20250909
 */

@Repository
public class ChatRoomUserDao extends Dao {

    // 채팅방에 user를 삽입하기.
    public void insertChatRoomUser(ChatRoomUserDto dto) {
        String sql = "INSERT INTO ChatRoomUser (roomNo, userNo) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, dto.getRoomNo());
            pstmt.setInt(2, dto.getUserNo());
            pstmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("ChatRoomUserDao.insertChatRoomUser " + e);
        }
    }  //func end

    // 특정 방번호로 해당 방에 멤버를 불러옴
    public List<Map<Integer, String>> searchUserNoAtRoom(int roomNo) {
        List<Map<Integer, String>> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ChatRoomUser c inner join users u on u.userno = c.userno WHERE roomNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Map<Integer, String> map = new HashMap<>();
                map.put(rs.getInt("userNo"), rs.getString("userName"));
                list.add(map);
            }
        } catch (Exception e) {
            System.out.println("ChatRoomUserDao.selectUsersByRoomNo " + e);
        }
        return list;
    }  //func end

    // 1:1 채팅방이 중복으로 있는지 확인
    public Integer findOneToOneRoom(int userA, int userB) {
        try {
            /** ChatRoomUser 테이블을 두 번 조인 (cru1, cru2)하여 같은 채팅방에 속한 두 사용자를 찾음
             * WHERE 조건에서:
             *      cru1.userNo = ? → 첫 번째 사용자
             *      cru2.userNo = ? → 두 번째 사용자
             *      cr.isGroup = false → 1:1 채팅방만 대상
             * GROUP BY cru1.roomNo → 채팅방별로 그룹화
             * HAVING COUNT(*) = 2 → 해당 채팅방에 정확히 두 명만 있어야 함 (1:1 조건)
             */
            String sql = """
                        SELECT cru1.roomNo
                        FROM ChatRoomUser cru1
                        JOIN ChatRoomUser cru2 ON cru1.roomNo = cru2.roomNo
                        JOIN ChatRoom cr ON cr.roomNo = cru1.roomNo
                        WHERE cru1.userNo = ? AND cru2.userNo = ? AND cr.isGroup = false
                        GROUP BY cru1.roomNo;
                    """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userA);
            ps.setInt(2, userB);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("roomNo");
            }
        } catch (Exception e) {
            System.out.println("ChatRoomUserDao.findOneToOneRoom " + e );
        }
        return null;
    } // func end


} // class end
