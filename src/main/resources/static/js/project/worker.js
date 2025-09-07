console.log("Pjworker func exe")

window.onHeaderReady = () => {
    loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
};

// [0] 로그인 체크
const loginCheck = async () => {
    console.log("loginCheck func exe")
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    } else if (businessNo == null || businessNo === 0) {
        alert("[경고] 일반회원은 사용불가능한 메뉴입니다.")
        location.href = "/index.jsp"
    }
}

// 역할 템플릿 모달 내 대분류-소분류 불러오기
const chooseRoleTemp = async () => {
    // select 표시 영역
    const modalRoleTemplate = document.querySelector(".modalRoleTemplate")
    // 대분류 역할 정보 가져오기
    try {
        const r = await fetch("/roleTem")
        const d = await r.json()
        console.log(d)

        let html = '';
        if (d.length != 0) {
            d.forEach((dto) => {
                html += ` <option value=${dto.rtNo}>${dto.rtName}</option>`
            });
        }
        modalRoleTemplate.innerHTML += html;
    } catch (error) {
        console.log(error)
    }
} // func end

const chooseRoleTemItem = async (rtNo) => {
    const modalRoleTemTbdoy = document.querySelector("#modalRoleTemTbdoy")
    try {
        const r = await fetch(`/roleTem/Item?rtNo=${rtNo}`)
        const d = await r.json()
        console.log(d)

        let html = '';
        let i = 1;
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<tr data-rtiNo = "${dto.rtiNo}">
                            <td>${i}</td>
                            <td>${dto.rtiName}</td>
                            <td><button class="btn btn-sm btn-success selectTemplateBtn selectTemplateBtn">선택</button></td>
                        </tr>`
                i++
            });
        }
        modalRoleTemTbdoy.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

chooseRoleTemp();

// 대분류 명을 선택하면 소분류명 table이 업데이트 될 수 있도록 함.
document.querySelector(".modalRoleTemplate").addEventListener("change", function () {
    const rtNo = this.value;
    console.log(rtNo)
    if (rtNo != 0) {
        chooseRoleTemItem(rtNo);
    }
});
