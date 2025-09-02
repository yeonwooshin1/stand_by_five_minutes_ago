console.log("header js exe")
// [공통] userNo 저장
let userNo;
// [공통] businessNo 저장
let businessNo;
// 이름 저장
let managerName;
let userName;

// [1] 메인 메뉴 

const mainMenu = async () => {
    // 1. 로그인 여부 판단
    try{
        // 1. fetch 실행
        const option = { method : "GET"}
        const response = await fetch( "/user/find/info" , option );
        const data = await response.json();
        userNo = data.userNo;
        userName = data.userName;
    }catch{
        userNo = 0;
    }

    // 2. 사업자/일반 계정 판단
    if(userNo != 0){
        try{
        // 1. fetch 실행
        const option = { method : "GET"}
        const response = await fetch( "/business/find/info" , option );
        const data = await response.json();
        businessNo = data.bnNo;
        managerName = data.managerName;
        }catch{
            businessNo = null ;
        }
    }   // if end
    
    // 3. html 노출 판단
    const mainMenu = document.querySelector(".main-menu")
    let html = '';
    if (userNo == 0) {
        html = ''
    } else if (businessNo != null ) { // 기업 담당자
        html += `<li><a href="#">인력 관리</a></li>
                 <li><a href="/template/roleTem.jsp">템플릿 관리</a></li>
                 <li><a href="#">프로젝트 관리</a></li>`
    } else if (userNo > 1 ) { // 일반회원
        html += `<li><a href="#">메뉴1</a></li>
                 <li><a href="#">메뉴2</a></li>
                 <li><a href="#">메뉴3</a></li>`
    }
    mainMenu.innerHTML = html;
}

const subMenu = async () => {


    const subMenu = document.querySelector(".sub-menu")
    let html = ''
    if (userNo == 0) {
        html = `<ul class='main-menu' ><li><a href="/user/login.jsp" style="color:black">로그인</a></li>
                    <li><a href="#" style="color:black">회원가입(추후 구현)</a></li></ul>`

    } else if (businessNo != null ) { // 기업 담당자
        html += `<div class="headText">${managerName}님 환영합니다. <br />(기업 담당자)</div>
                    <ul><li><a href="/user/info" class="myPage">마이페이지</a></li>
                    <li class="headerbar"> | </li>
                    <li><li><a href="#" class="myPageNlogout" onclick="logout()">로그아웃</a></li></ul>`

    } else if (userNo > 0) { // 일반회원
        html += `<div class="headText" >${userName}님 환영합니다. <br />(일반 회원)</div>
                    <ul><li><a href="/user/info" class="myPage">마이페이지</a></li>
                    <li class="headerbar"> | </li>
                    <li><li><a href="#" class="myPageNlogout" onclick="logout()">로그아웃</a></li></ul>`
    }
    subMenu.innerHTML = html
} // func end


const logout= async() => {
      try{
        // 1. fetch 실행 
        const option = { method : "GET"}
        const response = await fetch( "/user/logout" , option );
        const data = await response.json();
        // 2. fetch 통신 결과
        if( data == 1 ){
            alert('로그아웃 했습니다');
            // 로그아웃 성공시 메인페이지로 이동
            location.href="/index.jsp";
        }else{
            alert('관리자에게문의');
        }
    }catch{ }
}

const initHeader = async () => {
    await mainMenu(); // mainMenu fetch 끝날 때까지 대기
    await subMenu();  // 이제 userNo가 준비된 상태에서 실행
};

initHeader();