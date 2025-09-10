package five_minutes.controller;

import five_minutes.model.dto.ChatMessageDto;
import five_minutes.model.dto.ChatRoomDto;
import five_minutes.model.dto.ChatRoomUserDto;
import five_minutes.service.ChatMessageService;
import five_minutes.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatMessageService chatMessageService;
    private final ChatRoomService chatRoomService;

    // [chat-01] 특정 채팅방의 메세지 목록 조회
    @GetMapping("/messages")
    public List<ChatMessageDto> getMessages(@RequestParam int roomNo) {
        return chatMessageService.getMessagesByRoomNo(roomNo);
    } // func end

    // [chat-02] 특정 유저의 채팅방 목록 조회
    @GetMapping("/rooms")
    public List<ChatRoomDto> getChatRooms(@RequestParam int userNo) {
        return chatRoomService.getChatRoomsByUser(userNo);
    } // func end

    // [chat-03] 1:1 채팅방 생성
    @PostMapping("/room")
    public ResponseEntity<Integer> createOrGetOneToOneRoom(@RequestParam int loginUserNo,@RequestParam int targetUserNo) {
        int roomNo = chatRoomService.createOrGetOneToOneRoom(loginUserNo, targetUserNo);
        return ResponseEntity.status(200).body(roomNo);
    } // func end

    // [chat-04] 그룹 채팅방 생성
    @PostMapping("/room/group")
    public ResponseEntity<Integer> createGroupRoom(@RequestBody ChatRoomUserDto chatRoomUserDto) {
        int roomNo = chatRoomService.createGroupChatRoom(chatRoomUserDto);
        return ResponseEntity.status(200).body(roomNo);
    }


} // class end
