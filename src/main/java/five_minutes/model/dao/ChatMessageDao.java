package five_minutes.model.dao;

import five_minutes.model.dto.ChatMessageDto;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * <p> info =======================
 * <p> Chat 메세지 정보
 *
 * @author OngTK
 * @since 20250909
 */

@Repository
public class ChatMessageDao extends Dao {

    // 메세지 이력 기록
    public void insertChatMessage(ChatMessageDto dto) {
        try {
            String sql = "INSERT INTO ChatMessage (roomNO, sendUserNo, message) VALUES (?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, dto.getRoomNO());
            ps.setInt(2, dto.getSendUserNo());
            ps.setString(3, dto.getMessage());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("ChatMessageDao.insertChatMessage " + e);
        }
    } //func end

    // 해당 방에 대화 이력 조회
    public List<ChatMessageDto> selectMessagesByRoomNo(int roomNo) {
        List<ChatMessageDto> list = new ArrayList<>();
        try {
            String sql = "SELECT * FROM ChatMessage WHERE roomNO = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNo);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                ChatMessageDto dto = ChatMessageDto.builder().messageNo(rs.getInt("messageNo")).roomNO(rs.getInt("roomNO")).sendUserNo(rs.getInt("sendUserNo")).message(rs.getString("message")).sentDate(rs.getString("sentDate")).build();
                list.add(dto);
            }
        } catch (Exception e) {
            System.out.println("ChatMessageDao.selectMessagesByRoomNo " +e);
        }
        return list;
    }  //func end

} // class end
