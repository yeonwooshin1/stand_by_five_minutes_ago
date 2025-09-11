console.log("list js exe")


window.onHeaderReady = async () => {
    await loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    await createBtnDisplay()
    await readAllPj();
};

// [0] 로그인 체크
const loginCheck = async () => {
    console.log("loginCheck func exe")
    console.log(userNo)
    console.log(businessNo)
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    }
}

// [0] 일반회원일 경우, 생성버튼 비활성화
const createBtnDisplay = async () => {
    const createBtn = document.querySelector('.createBtn');
    if (userNo && !businessNo) {
        // 조건 만족 시 숨김 처리
        createBtn.style.display = 'none';
    } else {
        // 조건 불만족 시 다시 보이게
        createBtn.style.display = '';
    }
}

// [1] pjList 전체 조회
const readAllPj = async () => {
    // [1.1] html 표시 영역
    const pjListTbody = document.querySelector(".pjListTbody")

    let r;
    let d;
    try {
        // [1.2] fetch
        if (  // 사업자 회원
            businessNo !== null &&
            businessNo !== 0 &&
            userNo !== null &&
            userNo !== 0
        ) {
            console.log("사업자 회원")
            r = await fetch("/project/info");
        } else if ( // 일반 회원
            userNo !== null &&
            userNo !== 0 &&
            (businessNo === null || businessNo === 0)
        ) {
            console.log("일반 회원")
            r = await fetch("/project/info/user");
        }
        d = await r.json()

        // [1.3] html 표시
        let html = '';
        let i = 1
        if (d.length != 0) {
            if (  // 사업자 회원
                businessNo !== null &&
                businessNo !== 0 &&
                userNo !== null &&
                userNo !== 0
            ) {
                d.forEach(pjDto => {
                    html += `<tr>
                        <td>${i}</td>
                        <td><a href="/project/info.jsp?pjNo=${pjDto.pjNo}">${pjDto.pjName}</td>
                        <td>${pjDto.clientName}</td>
                        <td>${pjDto.clientRepresent}</td>
                        <td>${pjDto.clientPhone}</td>
                        <td>${pjDto.pjStartDate}</td>
                        <td>${pjDto.pjEndDate}</td>
                        <td>${pjDto.updateDate}</td>
                    </tr>`
                    i++
                });
            } else if ( // 일반 회원
                userNo !== null &&
                userNo !== 0 &&
                (businessNo === null || businessNo === 0)
            ) {
                d.forEach(pjDto => {
                    html += `<tr>
                        <td>${i}</td>
                        <td><a href="/project/performcheck.jsp?pjNo=${pjDto.pjNo}">${pjDto.pjName}</td>
                        <td>${pjDto.clientName}</td>
                        <td>${pjDto.clientRepresent}</td>
                        <td>${pjDto.clientPhone}</td>
                        <td>${pjDto.pjStartDate}</td>
                        <td>${pjDto.pjEndDate}</td>
                        <td>${pjDto.updateDate}</td>
                    </tr>`
                    i++
                });
            }
        } else {
            html += `<tr>
                <td colspan="8"> ※ 표시할 정보가 없습니다.</td>
             </tr>`
        }
        pjListTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

