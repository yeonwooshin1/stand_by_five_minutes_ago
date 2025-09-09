console.log("chat js exe")

// userNo는 이미 전역변수로 선언되어 있음


const roomNo = "11000001"; // 사용자가 선택한 채팅방 번호
const socket = new WebSocket(`ws://localhost:8080/chat?roomNo=${roomNo}`);


const message = {
    roomNO: 11000001,
    sendUserNo: 1000001,
    message: "안녕하세요!",
    sentDate: new Date().toISOString()
};
socket.send(JSON.stringify(message));

// // chatroom 클릭시 과거 메세지 이력을 가져오는 func

// function enterChatRoom(roomNo) {
//     fetch(`/chat/messages?roomNo=${roomNo}`)
//         .then(response => response.json())
//         .then(messages => {
//             renderMessages(messages); // 채팅창에 메시지 렌더링
//         });
// }

// // 메세지 렌더링

// function renderMessages(messages) {
//     const chatBox = document.getElementById("chatBox");
//     chatBox.innerHTML = "";

//     messages.forEach(msg => {
//         const div = document.createElement("div");
//         div.textContent = `[${msg.sentDate}] ${msg.sendUserNo}: ${msg.message}`;
//         chatBox.appendChild(div);
//     });
// }

// 특정 사용자의 채팅방 목록 조회

// fetch("/chat/rooms?userNo=1000001")
//     .then(response => response.json())
//     .then(rooms => {
//         renderRoomList(rooms);
//     });

// function renderRoomList(rooms) {
//     const listBox = document.getElementById("roomList");
//     listBox.innerHTML = "";
//     rooms.forEach(room => {
//         const li = document.createElement("li");
//         li.textContent = room.roomName;
//         li.onclick = () => enterChatRoom(room.roomNo);
//         listBox.appendChild(li);
//     });
// }

// // 채팅방 만들기

// function startChatWith(userNo) {
//     fetch(`/chat/room/one-to-one?loginUserNo=1000001&targetUserNo=${userNo}`, {
//         method: "POST"
//     })
//     .then(res => res.json())
//     .then(roomNo => {
//         enterChatRoom(roomNo); // 채팅방 입장
//     });
// }


// // 그룹채팅방 만들기

// const requestBody = {
//     participantUserNos: [1000001, 1000002, 1000003],
//     creatorUserNo: 1000001
// };

// fetch("/chat/room/group", {
//     method: "POST",
//     headers: { "Content-Type": "application/json" },
//     body: JSON.stringify(requestBody)
// })
// .then(res => res.json())
// .then(roomNo => {
//     enterChatRoom(roomNo); // 채팅방 입장
// });
