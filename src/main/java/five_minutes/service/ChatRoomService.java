package five_minutes.service;

import five_minutes.model.dao.ChatRoomDao;
import five_minutes.model.dao.ChatRoomUserDao;
import five_minutes.model.dao.UsersDao;
import five_minutes.model.dto.ChatRoomDto;
import five_minutes.model.dto.ChatRoomUserDto;
import five_minutes.model.dto.UsersDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <p> info =======================
 * <p> Chat 방 정보
 *
 * @author OngTK
 * @since 20250909
 */

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomDao chatRoomDao;
    private final ChatRoomUserDao chatRoomUserDao;
    private final UsersDao usersDao;

    // 채팅방 생성자와 초대자의 no를 받아 기존 채팅방 존재여부 확인 후 채팅방을 생성
    public int createOrGetOneToOneRoom(int loginUserNo, int targetUserNo) {
        Integer existingRoomNo = chatRoomUserDao.findOneToOneRoom(loginUserNo, targetUserNo);
        if (existingRoomNo != null) {
            // 기존 채팅방이 있으면 기존 채팅방 NO 반환
            return existingRoomNo;
        }

        // 새 채팅방 생성
        ChatRoomDto roomDto = ChatRoomDto.builder()
                .roomName("1:1 채팅")
                .creatorUserNo(loginUserNo)
                .isGroup(false)
                .build();
        int newRoomNo = chatRoomDao.insertChatRoom(roomDto);
        // 참여자 등록
        chatRoomUserDao.insertChatRoomUser(ChatRoomUserDto.builder()
                .roomNo(newRoomNo)
                .userNo(loginUserNo)
                .joinDate(roomDto.getCreatedDate())
                .build());
        chatRoomUserDao.insertChatRoomUser(ChatRoomUserDto.builder()
                .roomNo(newRoomNo)
                .userNo(targetUserNo)
                .joinDate(roomDto.getCreatedDate())
                .build());
        return newRoomNo;
    } // func end

    // 특정 유저가 참여하고 있는 채팅방 목록을 조회
    public List<ChatRoomDto> getChatRoomsByUser(int userNo) {
        List<ChatRoomDto> chatRoomList = chatRoomDao.selectChatRoomsByUserNo(userNo);
        for(ChatRoomDto chatRoomDto : chatRoomList){
            List<Map<Integer, String>> participants = chatRoomUserDao.searchUserNoAtRoom(chatRoomDto.getRoomNo());
            chatRoomDto.setParticipant(participants);
        } // for end
        return chatRoomList;
    } // func end

    // 그룹 채팅방 만들기
    public int createGroupChatRoom(List<Integer> participantUserNoList, int creatorUserNo) {
        // 방이름 만들기 func 실행
        String roomName = generateRoomName(participantUserNoList);
        // 방정보DTO
        ChatRoomDto roomDto = ChatRoomDto.builder()
                .roomName(roomName)
                .creatorUserNo(creatorUserNo)
                .isGroup(true)
                .build();
        // 방 만들기 = 방번호 발급 func
        int roomNo = chatRoomDao.insertChatRoom(roomDto);
        // 참여 인원에 대한 dao를 만들어서 room 참여자 목록에 삽입
        for (int userNo : participantUserNoList) {
            chatRoomUserDao.insertChatRoomUser(ChatRoomUserDto.builder()
                    .roomNo(roomNo)
                    .userNo(userNo)
                    .joinDate(roomDto.getCreatedDate())
                    .build());
        }
        return roomNo;
    }// func

    // 채팅방 이름 만들기 func
    /**
     * 1:1 - OOO과의 채팅
     * 그룹 - OOO 외 0명
     * */
    public String generateRoomName(List<Integer> userNoList) {
        // 이름을 모아두는 배열
        List<String> names = new ArrayList<>();
        // 반복문으로 이름 조회하여 이름 배열에 삽입
        for (int userNo : userNoList) {
            UsersDto usersDto = usersDao.getUserInfo(userNo); // DAO에서 이름 조회
            names.add(usersDto.getUserName());
        }
        if (names.size() == 2) { // 1:1 채팅
            return names.get(1) + "님과의 채팅"; // 본인은 get(0) 이므로 상대방이 get(1) 로그인 유저 제외
        } else if (names.size() <= 3) { // 그룹 채팅
            return String.join(", ", names);
        } else {
            return names.get(0) + ", " + names.get(1) + " 외 " + (names.size() - 2) + "명";
        }
    } // func end

} // func end
