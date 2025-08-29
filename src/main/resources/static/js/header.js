console.log("header js exe")
// [공통] userNo 저장
const userNo = 2;


// [1] 메인 메뉴 

const mainMenu = async () => {
    // 1. 로그인 여부 판단

    // 2. 사업자/일반 계정 판단

    // 3. html 노출 판단
    const mainMenu = document.querySelector(".main-menu")
    let html = '';
    if (userNo == 0) {
        html = ''
    } else if (userNo == 1) { // 기업 담당자
        html += `<li><a href="#">인력 관리</a></li>
                 <li><a href="#">템플릿 관리</a></li>
                 <li><a href="#">프로젝트 관리</a></li>`
    } else if (userNo == 2) { // 일반회원
        html += `<li><a href="#">메뉴1</a></li>
                 <li><a href="#">메뉴2</a></li>
                 <li><a href="#">메뉴3</a></li>`
    }
    mainMenu.innerHTML = html
}
mainMenu()

const subMenu = async () => {


    const subMenu = document.querySelector(".sub-menu")
    let html = ''
    if (userNo == 0) {
        html = `<ul class='main-menu' ><li><a href="#" style="color:black">로그인</a></li>
                    <li><a href="#" style="color:black">회원가입</a></li></ul>`

    } else if (userNo == 1) { // 기업 담당자
        html += `<div class="headText">OOO님 환영합니다. <br />(기업 담당자)</div>
                    <ul><li><a href="#">마이페이지</a></li>
                    <li> | </li>
                    <li><a href="#">로그아웃</a></li></ul>`

    } else if (userNo == 2) { // 일반회원
        html += `<div class="headText" >OOO님 환영합니다. <br />(일반 회원)</div>
                    <ul><li><a href="#">마이페이지</a></li>
                    <li> | </li>
                    <li><a href="#">로그아웃</a></li></ul>`
    }
    subMenu.innerHTML = html
} // func end
subMenu()