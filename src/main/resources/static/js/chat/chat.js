/*
01. 웹 소켓 연결
02. 채팅방 목록 불러오기 함수
03. 채팅 목록 렌더링 함수
04. 채팅방 열기 함수 (선택된 채팅방의 메시지 불러오기 등)
05. 페이지 로드 시 실행 (jsp 실행 = 이벤트)
06. 채팅방 채팅 내역 가져오기 함수
*/


console.log("chat js exe")

// ※ 주의 userNo는 이미 전역변수로 선언되어 있음

// Socket 연결
let socket = null;

// [01] 웹 소켓 연결
const connectWebSocket = async (roomNo, userNo) => {
    try {
        // 기존 연결 종료
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.close();
        }

        // 새 연결 시작
        const wsUrl = `ws://localhost:8080/chat?roomNo=${roomNo}&userNo=${userNo}`;
        socket = new WebSocket(wsUrl);

        socket.onopen = () => {
            console.log(`웹소켓 연결됨: roomNo=${roomNo}`);
        };

        socket.onmessage = (event) => {
            const message = JSON.parse(event.data);
            console.log("받은 메시지:", message);
            // 메시지 렌더링
        };

        socket.onclose = () => {
            console.log("웹소켓 연결 종료");
        };

        socket.onerror = (error) => {
            console.error("웹소켓 에러:", error);
        };
    } catch (error) {
        console.log(error)
    }
} // func end

// [02] 채팅방 목록 불러오기 함수
const loadChatRooms = async (userNo) => {
    try {
        const response = await fetch(`/chat/rooms?userNo=${userNo}`);
        // 응답이 없을 경우
        if (!response.ok) throw new Error("채팅방 목록을 불러오지 못했습니다.");
        // 응답이 있을 경
        const chatRooms = await response.json();
        console.log(chatRooms)
        renderChatList(chatRooms);
    } catch (error) {
        console.error("에러 발생:", error);
    }
} // func end

// [03] 채팅 목록 렌더링 함수
const renderChatList = (chatRooms) => {
    const chatRoomList = document.querySelector(".chatRoomList");
    chatRoomList.innerHTML = ""; // 기존 목록 초기화

    chatRooms.forEach(room => {
        // 버튼 생성
        const button = document.createElement("button");
        // 클래스 삽입
        button.className = "btn btn-outline-secondary";
        // 타입 지정
        button.type = "button";

        // 채팅방 이름 설정
        let displayName = "";
        if (room.group) {
            // 그룹 채팅일 경우: 채팅방 이름 사용
            displayName = room.roomName;
        } else {
            console.log("1:1 확인")
            // 1:1 채팅이면 participant 배열에서 본인 제외한 상대 찾기
            for (let i = 0; i < room.participant.length; i++) {
                const participantObj = room.participant[i];
                // console.log(participantObj)
                const chatuserNo = Object.keys(participantObj)[0];
                // console.log(chatuserNo)
                // console.log(userNo)

                if (chatuserNo != userNo) {
                    displayName = participantObj[chatuserNo];
                    // console.log(displayName) // 상대방 이름
                    break;
                }
            }
        }

        button.textContent = displayName;
        button.onclick = () => openChatRoom(room.roomNo);
        chatRoomList.appendChild(button);
    });
}; // func end

// [04] 채팅방 열기 함수 (선택된 채팅방의 메시지 불러오기 등)
const openChatRoom = async (roomNo) => {
    console.log("openChatRoom", roomNo);

    // 메시지 불러오기 API 호출
    try {
        const response = await fetch(`/chat/messages?roomNo=${roomNo}`);
        // 결과 =  실패
        if (!response.ok) throw new Error("메시지를 불러오지 못했습니다.");
        // 결과 = 성공
        const messages = await response.json();
        console.log(messages)
        renderMessages(messages);
        connectWebSocket(roomNo, userNo); // WebSocket 연결
    } catch (error) {
        console.error("메시지 불러오기 실패:", error);
    }

}

// [05] 페이지 로드 시 실행 (jsp 실행 = 이벤트)
document.addEventListener("DOMContentLoaded", () => {
    if (userNo) {
        loadChatRooms(userNo);
    } else {
        alert("[경고] 로그인 정보가 없습니다.");
    }
});

// [06] 메시지 렌더링 함수
const renderMessages = async (messages) => {
    const chatMessagesContainer = document.querySelector("#chatMessages");
    chatMessagesContainer.innerHTML = ""; // 기존 메시지 초기화

    messages.forEach(msg => {
        // 새로운 div 구역을 만듦
        const messageRow = document.createElement("div");
        // 배열 처리를 위해 클래스에 row 삽입
        messageRow.className = "row";

        if (msg.sendUserNo == userNo) {
            // 내가 보낸 메시지
            messageRow.innerHTML = `
                <div class="col-6"></div>
                <div class="mb-3 text-end col-6">
                    <div class="bg-primary text-white p-2 rounded d-inline-block">${msg.message}</div>
                    <small class="text-muted d-block">${formatDate(msg.sentDate)}</small>
                </div>
            `;
        } else {
            // 받은 메시지
            messageRow.innerHTML = `
                <div class="mb-3 col-6">
                    <div class="bg-light p-2 rounded d-inline-block">${msg.message}</div>
                    <small class="text-muted d-block">${formatDate(msg.sentDate)}</small>
                </div>
            `;
        }
        // HTML에 삽입
        chatMessagesContainer.appendChild(messageRow);
        chatMessagesContainer.scrollTop = chatMessagesContainer.scrollHeight
    });
}

// [07] 날짜 포맷 변경 함수
function formatDate(timestamp) {
    const date = new Date(timestamp);
    return `${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
}






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
