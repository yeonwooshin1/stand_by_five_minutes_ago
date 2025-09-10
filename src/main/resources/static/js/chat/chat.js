/*
01. 서버에서 받은 현재 메시지 렌더링 함수
02. 과거 메시지 렌더링 함수
03. 웹 소켓 연결
04. 채팅방 목록 불러오기 함수
05. 채팅방 이름 표시 함수
06. 채팅방 열기 함수 (선택된 채팅방의 메시지 불러오기 등)
07. 페이지 로드 시 실행 (jsp 실행 = 이벤트)
08. 날짜 포맷 변경 함수
09. 메세지 전송 - 소켓연결, 이벤트 연결

*/

console.log("chat js exe")

// ※ 주의 userNo는 이미 전역변수로 선언되어 있음

// Socket 연결
let socket = null;

// [01] 메시지 렌더링 함수
const renderMessage = (data) => {
    const chatMessages = document.querySelector("#chatMessages");
    console.log("renderMessage func exe")
    console.log(data.sendUserNo)
    console.log(userNo)
    const isMine = data.sendUserNo == userNo ? true : false; // 현재 로그인 유저와 비교 true/false
    console.log(isMine)
    console.log(data.sentDate)

    // 렌더링을 위한 구역 생성
    if (isMine) { // 내가 보낸 메세지
        const row = document.createElement("div");
        row.className = "row"
        chatMessages.appendChild(row);
        row.innerHTML = `<div class="col-6"></div>
                        <div class="mb-3 text-end col-6">
                            <div class="bg-primary text-white p-2 rounded d-inline-block">${data.message}</div>
                            <small class="text-muted d-block">${formatDate(data.sentDate)}</small>
                        </div>`
    } else { // 받은 메세지
        const row = document.createElement("div");
        row.className = "row"
        chatMessages.appendChild(row);
        row.innerHTML = `<div class="mb-3 col-6">
                                <div class="bg-light p-2 rounded d-inline-block">${data.message}</div>
                                <small class="text-muted d-block">${formatDate(data.sentDate)}</small>
                            </div>`
    }
    chatMessages.scrollTop = chatMessages.scrollHeight;
} // func end


// [02] 과거 메시지 렌더링 함수
const renderPreMessage = async (messages) => {
    console.log("renderPreMessage func exe")
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

// [03] 웹 소켓 연결
const connectWebSocket = async (roomNo, userNo) => {
    try {
        // 기존 연결 종료
        if (socket && socket.readyState === WebSocket.OPEN) {
            socket.close();
        }

        // 소켓 설정
        const wsUrl = `ws://localhost:8080/chat?roomNo=${roomNo}&userNo=${userNo}`;
        socket = new WebSocket(wsUrl);

        // 소켓 연결
        socket.onopen = () => {
            console.log(`웹소켓 연결됨: roomNo=${roomNo}`);
        };

        // 메세지 송신
        socket.onmessage = (event) => {
            console.log(event)
            const message = JSON.parse(event.data);
            console.log("받은 메시지:", message);
            renderMessage(message); // 메시지 화면에 자동 렌더링
        };

        // 연결 종료
        socket.onclose = () => {
            console.log("웹소켓 연결 종료");
        };

        // 에러발생
        socket.onerror = (error) => {
            console.error("웹소켓 에러:", error);
        };
    } catch (error) {
        console.log(error)
    }
} // func end

// [04] 채팅방 목록 불러오기 함수
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

// [05] 채팅방 이름 표시 함수
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
        button.setAttribute("data-roomNo", room.roomNo)

        // 채팅방 이름 설정
        let displayName = "";
        if (room.group) {
            // 그룹 채팅일 경우: 채팅방 이름 사용
            displayName = room.roomName;
        } else {
            // console.log("1:1 확인")
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
        button.onclick = () => openChatRoom(room.roomNo, displayName);
        chatRoomList.appendChild(button);
    });
}; // func end

// [06] 채팅방 열기 함수 (선택된 채팅방의 메시지 불러오기 등)
let currentRoomNo = null;
const openChatRoom = async (roomNo, roomName) => {
    console.log("openChatRoom", roomNo);
    const chatTilte = document.querySelector("#chatTilte")
    chatTilte.innerHTML = roomName

    currentRoomNo = roomNo;

    // 메시지 불러오기 API 호출
    try {
        const response = await fetch(`/chat/messages?roomNo=${roomNo}`);
        // 결과 =  실패
        if (!response.ok) throw new Error("메시지를 불러오지 못했습니다.");
        // 결과 = 성공
        const messages = await response.json();
        console.log(messages)
        renderPreMessage(messages);
        connectWebSocket(roomNo, userNo); // WebSocket 연결
    } catch (error) {
        console.error("메시지 불러오기 실패:", error);
    }

}

// [07] 페이지 로드 시 실행 (jsp 실행 = 이벤트)
document.addEventListener("DOMContentLoaded", () => {
    if (userNo) {
        loadChatRooms(userNo);
    } else {
        alert("[경고] 로그인 정보가 없습니다.");
    }
});


// [08] 날짜 포맷 변경 함수
function formatDate(timestamp) {
    const date = new Date(timestamp);
    return `${date.getMonth() + 1}월 ${date.getDate()}일 ${date.getHours()}:${String(date.getMinutes()).padStart(2, '0')}`;
}


// [09] 메세지 전송 - 소켓연결, 이벤트 연결
// socket 초기화
socket = null;
// DOMContentLoaded : HTMl 전체를 확인
document.addEventListener("DOMContentLoaded", function () {

    const input = document.querySelector(".chat-input input");
    const sendBtn = document.querySelector(".chat-input button");
    const chatMessages = document.getElementById("chatMessages");

    // [09] 메시지 전송 
    function sendMessage() {
        // trim() : 문자열 좌우에서 공백을 제거하는 함수
        const message = input.value.trim();
        // 메세지 내용이 없거나, 방을 선택하지 않으면 return
        if (message == "" | !currentRoomNo) return;

        const payload = {
            messageNo: 0, // 임의 메세지 no
            roomNo: currentRoomNo,
            sendUserNo: parseInt(userNo),
            message: message,
            sentDate: new Date().toISOString()
        };
        console.log(payload)

        socket.send(JSON.stringify(payload));
        input.value = "";
    }

    // [11] 클릭과 엔터에 대한 이벤트 함수
    sendBtn.addEventListener("click", sendMessage);
    input.addEventListener("keydown", function (e) {
        if (e.key == "Enter") {
            e.preventDefault();
            sendMessage()
        }
    });
});

// =========================================================
let selectedUserNos = [];

// [10] 일반 회원 목록 조회
const fetchUserList = async () => {
    try {
        // 사업자 제외 일반 회원 조회
        const r = await fetch(`/user/find/search`, { method: "GET" })
        const d = await r.json()
        console.log(d)
        const container = document.querySelector("#userListContainer > div");
        container.innerHTML = "";
        let html = ''
        d.forEach((dto) => { 
            html +=`<button type="button" class="btn btn-outline-secondary m-1" data-bs-toggle="button"
            data-userNo="${dto.userNo}"> ${dto.userName}</button>`                              
        })
        container.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
}


// function closeUserSelectModal() {
//     document.getElementById("userSelectModal").style.display = "none";
//     selectedUserNos = [];
// }


// function createChatRoom() {
//     if (selectedUserNos.length === 0) {
//         alert("최소 1명을 선택해주세요.");
//         return;
//     }

//     const payload = {
//         creatorUserNo: userNo, // 로그인 사용자
//         participantUserNos: selectedUserNos
//     };

//     fetch("/api/chat/create", {
//         method: "POST",
//         headers: { "Content-Type": "application/json" },
//         body: JSON.stringify(payload)
//     })
//         .then(res => {
//             if (!res.ok) throw new Error("채팅방 생성 실패");
//             return res.json();
//         })
//         .then(room => {
//             alert("채팅방이 생성되었습니다.");
//             closeUserSelectModal();
//             loadChatRooms(userNo); // 목록 새로고침
//             openChatRoom(room.roomNo); // 자동 입장
//         })
//         .catch(err => {
//             alert("이미 존재하는 1:1 채팅방이거나 오류가 발생했습니다.");
//             console.error(err);
//         });
// }
