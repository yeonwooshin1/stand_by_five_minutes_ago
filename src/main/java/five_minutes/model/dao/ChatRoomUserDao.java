package five_minutes.model.dao;

import five_minutes.model.dto.ChatRoomUserDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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
    public List<ChatRoomUserDto> selectUsersByRoomNo(int roomNo) {
        List<ChatRoomUserDto> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ChatRoomUser WHERE roomNo = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatRoomUserDto dto = ChatRoomUserDto.builder()
                        .roomNo(rs.getInt("roomNo"))
                        .userNo(rs.getInt("userNo"))
                        .joinDate(rs.getString("joinDate")).build();
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("ChatRoomUserDao.selectUsersByRoomNo " + e);
        }
        return list;
    }  //func end

} // class end
