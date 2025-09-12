console.log("header js exe")
// [공통] userNo 저장
let userNo;
// [공통] businessNo 저장
let businessNo;
// 이름 저장
let managerNameHeader;
let userNameHeader;

// [1] 메인 메뉴 

const mainMenu = () => {
    return new Promise(async (resolve, reject) => {
    // 1. 로그인 여부 판단
    try {
        // 1. fetch 실행
        const option = { method: "GET" }
        const response = await fetch("/user/find/info", option);
        const data = await response.json();
        userNo = data.userNo;
        userNameHeader = data.userName;
    } catch {
        userNo = 0;
    }

    // 2. 사업자/일반 계정 판단
    if (userNo != 0) {
        try {
            // 1. fetch 실행
            const option = { method: "GET" }
            const response = await fetch("/business/find/info", option);
            const data = await response.json();
            businessNo = data.bnNo;
            managerNameHeader = data.managerName;
        } catch {
            businessNo = null;
        }
    }   // if end

    // 3. html 노출 판단
    const mainMenu = document.querySelector(".main-menu")
    let html = '';
    if (userNo == 0) {
        html = ''
        document.getElementById("logoImgBox").href = "/index.jsp"
    } else if (businessNo != null) { // 기업 담당자
        html += `<li style = "display:none;"><a href="#" style="color: #A6A6A6; ">인력 관리</a></li>
                 <li><a href="/template/roleTem.jsp">템플릿 관리</a></li>
                 <li><a href="/project/list.jsp">프로젝트 관리</a></li>`
        document.getElementById("logoImgBox").href = "/project/list.jsp"
    } else if (userNo > 1) { // 일반회원
        html += `<li><a href="/project/list.jsp">프로젝트 관리</a></li>
                 <li style = "display:none;"><a href="#">메뉴2</a></li>
                 <li style = "display:none;"><a href="#">메뉴3</a></li>`
        document.getElementById("logoImgBox").href = "/project/list.jsp"
    }
    mainMenu.innerHTML = html;
    resolve();
    });
};

const subMenu = async () => {

    const subMenu = document.querySelector(".sub-menu")
    let html = ''
    if (userNo == 0) {
        html = `<ul class='main-menu' >
                    <li><a href="/user/login.jsp" style="color:black">로그인</a></li>
                    <li style = "display:none;" ><a href="#" style="color:black">회원가입(추후 구현)</a></li></ul>`

    } else if (businessNo != null) { // 기업 담당자
        html += `
        <div class="headText">${managerNameHeader}님 환영합니다.<br/>(기업 담당자)</div>
                    <ul>
                        <li style="ms-1">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-chat-text chatIcon" viewBox="0 0 20 20" style="cursor:pointer;">
                                <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
                                <path d="M4 5.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8m0 2.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5"/>
                            </svg>
                        </li>
                        <li class="headerbar"> | </li>
                        <li><a href="/user/info.jsp" class="myPage">마이페이지</a></li>
                        <li class="headerbar"> | </li>
                        <li><a href="#" class="myPageNlogout" onclick="logout()">로그아웃</a></li>
                    </ul>`

    } else if (userNo > 0) { // 일반회원
        html += `
        <div class="headText" >${userNameHeader}님 환영합니다. <br />(일반 회원)</div>
                    <ul>
                        <li style="ms-1">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="currentColor" class="bi bi-chat-text chatIcon" viewBox="0 0 20 20" style="cursor:pointer;">
                                <path d="M2.678 11.894a1 1 0 0 1 .287.801 11 11 0 0 1-.398 2c1.395-.323 2.247-.697 2.634-.893a1 1 0 0 1 .71-.074A8 8 0 0 0 8 14c3.996 0 7-2.807 7-6s-3.004-6-7-6-7 2.808-7 6c0 1.468.617 2.83 1.678 3.894m-.493 3.905a22 22 0 0 1-.713.129c-.2.032-.352-.176-.273-.362a10 10 0 0 0 .244-.637l.003-.01c.248-.72.45-1.548.524-2.319C.743 11.37 0 9.76 0 8c0-3.866 3.582-7 8-7s8 3.134 8 7-3.582 7-8 7a9 9 0 0 1-2.347-.306c-.52.263-1.639.742-3.468 1.105"/>
                                <path d="M4 5.5a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7a.5.5 0 0 1-.5-.5M4 8a.5.5 0 0 1 .5-.5h7a.5.5 0 0 1 0 1h-7A.5.5 0 0 1 4 8m0 2.5a.5.5 0 0 1 .5-.5h4a.5.5 0 0 1 0 1h-4a.5.5 0 0 1-.5-.5"/>
                            </svg>
                        </li>
                        <li class="headerbar"> | </li>
                        <li><a href="/user/info.jsp" class="myPage">마이페이지</a></li>
                        <li class="headerbar"> | </li>
                        <li><a href="#" class="myPageNlogout" onclick="logout()">로그아웃</a></li></ul>`
    }
    subMenu.innerHTML = html
    bindChatIconClick()
} // func end


const logout = async () => {
    try {
        // 1. fetch 실행 
        const option = { method: "GET" }
        const response = await fetch("/user/logout", option);
        const data = await response.text();
        // 2. fetch 통신 결과
        if (data == 1) {
            alert('로그아웃 했습니다');
            // 로그아웃 성공시 메인페이지로 이동
            location.href = "/index.jsp";
        } else {
            alert('관리자에게문의');
        }
    } catch { }
}

const initHeader = async () => {
    await mainMenu(); // mainMenu fetch 끝날 때까지 대기
    await subMenu();  // 이제 userNo가 준비된 상태에서 실행
};

initHeader();

// chat을 위해서는 userNo가 필요
// 다만 URL에 userNo가 노출될 경우, 다른 user에 채팅에 접근이 가능함.
// 이를 방지하기 위해 임시form을 만들어 userNo를 팝업창에 전송
function openPopupWithPost(userNo, managerNameHeader, userNameHeader) {
    
    // 1. 팝업창 열기 및 변수에 저장
    const popup = window.open("", "popupWindow", "width=1000,height=800,left=100,top=100");

    
    // 2. 폼 생성
    const form = document.createElement("form");
    form.method = "POST";
    form.action = "/chat/chat.jsp";
    form.target = "popupWindow";

    // 3. 숨겨진 input 생성
    const input1 = document.createElement("input");
    input1.type = "hidden";
    input1.name = "userNo";
    input1.value = userNo;
    const input2 = document.createElement("input");
    input2.type = "hidden";
    input2.name = "managerNameHeader";
    input2.value = managerNameHeader;
    const input3 = document.createElement("input");
    input3.type = "hidden";
    input3.name = "userNameHeader";
    input3.value = userNameHeader;
    form.appendChild(input1);
    form.appendChild(input2);
    form.appendChild(input3);

    // 4. 폼을 body에 추가
    document.body.appendChild(form);

    // 5. 팝업에 포커스 후 폼 제출
    if (popup) {
        popup.focus();
        form.submit();
    } else {
        alert("팝업이 차단되었거나 열리지 않았습니다.");
    }

    // 6. 폼 제거
    document.body.removeChild(form);
}

function bindChatIconClick() {
    const chatIcon = document.querySelector('.chatIcon');
    if (chatIcon) {
        chatIcon.addEventListener('click', function () {
            console.log("bindChatIconClick");
            console.log(userNo);
            openPopupWithPost(userNo, managerNameHeader, userNameHeader);
        });
    }
}
