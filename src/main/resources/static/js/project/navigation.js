const param = new URL(location.href).searchParams
const pjNo = param.get("pjNo")

// 상단 네비게이션 바 ==================================
const naviBar = () => {
    const projectNav = document.querySelector("#projectNav")

    let html = `<a class="nav-link" href="/project/info.jsp?pjNo=${pjNo}" data-page="info.jsp">1. 프로젝트 정보</a>
                <a class="nav-link" href="/project/worker.jsp?pjNo=${pjNo}" data-page="worker.jsp">2. 인력 관리</a>
                <a class="nav-link" href="/project/checklist.jsp?pjNo=${pjNo}" data-page="checklist.jsp"> 3. 체크리스트 관리</a>
                <a class="nav-link" href="/project/perform.jsp?pjNo=${pjNo}" data-page="perform.jsp">4. 업무 배정</a>
                <a class="nav-link" href="/project/performcheck.jsp?pjNo=${pjNo}" data-page="performcheck.jsp">5. 업무 관리</a>`;
    projectNav.innerHTML = html;
} // func end
naviBar()

// DOM이 완전히 로드된 후 실행되는 이벤트 리스너
document.addEventListener("DOMContentLoaded", function () {

    // 네비게이션 바 내의 모든 링크 요소를 선택
    const links = document.querySelectorAll("#projectNav .nav-link");

    
    // 현재 페이지의 파일명을 추출 (예: 'info.jsp')
    // window.location.pathname은 '/project/info.jsp'와 같은 경로를 반환
    // split('/')로 나누고 마지막 요소(pop())를 가져오면 'info.jsp'가 됨
    const currentPage = window.location.pathname.split("/").pop();


    // 각 링크에 대해 반복 처리
    links.forEach(link => {
        // 각 링크의 data-page 속성과 현재 페이지 이름을 비교
        // 일치하면 해당 링크에 'active' 클래스를 추가하여 강조
        if (link.getAttribute("data-page") === currentPage) {
            link.classList.add("active");
        } else {
            // 일치하지 않으면 'active' 클래스를 제거하여 일반 상태로 표시
            link.classList.remove("active");

        }
    });
});

