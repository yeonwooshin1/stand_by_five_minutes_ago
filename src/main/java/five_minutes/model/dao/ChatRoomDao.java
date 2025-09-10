package five_minutes.model.dao;

import five_minutes.model.dto.ChatRoomDto;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> info =======================
 * <p> Chat 방 정보
 *
 * @author OngTK
 * @since 20250909
 */

@Repository
public class ChatRoomDao extends Dao {

    // 신규 채팅방 생성
    public int insertChatRoom(ChatRoomDto dto) {
        try {
            String sql = "INSERT INTO ChatRoom (roomName, creatorUserNo, isGroup) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, dto.getRoomName());
            ps.setInt(2, dto.getCreatorUserNo());
            ps.setBoolean(3, dto.isGroup());
            int count = ps.executeUpdate();
            if(count == 1) {
                ResultSet rs = ps.executeQuery();
                if(rs.next()) return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("ChatRoomDao.insertChatRoom " + e);
        }
        return 0;
    } //func end

    // 전체 채팅방 조회
    public List<ChatRoomDto> selectAllChatRooms() {
        List<ChatRoomDto> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ChatRoom";
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatRoomDto dto = ChatRoomDto.builder()
                        .roomNo(rs.getInt("roomNo"))
                        .roomName(rs.getString("roomName"))
                        .creatorUserNo(rs.getInt("creatorUserNo"))
                        .isGroup(rs.getBoolean("isGroup"))
                        .createdDate(rs.getString("createdDate"))
                        .updateDate(rs.getString("updateDate")).build();
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("ChatRoomDao.selectAllChatRooms "+e );
        }
        return list;
    } //func end

    // 특정 userNo를 기반으로 해당 유저가 포함된 chatroom 정보를 반환
    public List<ChatRoomDto> selectChatRoomsByUserNo(int userNo) {
        List<ChatRoomDto> list = new ArrayList<>();
        try {
            String sql = """
                        SELECT cr.roomNo, cr.roomName, cr.creatorUserNo, cr.isGroup, cr.createdDate, cr.updateDate
                        FROM ChatRoom cr
                        JOIN ChatRoomUser cru ON cr.roomNo = cru.roomNo
                        WHERE cru.userNo = ?
                    """;
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatRoomDto dto = ChatRoomDto.builder()
                        .roomNo(rs.getInt("roomNo"))
                        .roomName(rs.getString("roomName"))
                        .creatorUserNo(rs.getInt("creatorUserNo"))
                        .isGroup(rs.getBoolean("isGroup"))
                        .createdDate(rs.getString("createdDate"))
                        .updateDate(rs.getString("updateDate")).build();
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("ChatRoomUserDao.selectChatRoomsByUserNo " + e);
        }
        return list;
    }


} // class end
