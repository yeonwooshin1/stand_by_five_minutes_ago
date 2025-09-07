console.log("Pjworker func exe")

window.onHeaderReady = async () => {
    await loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    await readAllpjworker();
};

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
// $(document).ready(function () {
//     $('#descriptionArea').summernote({
//         lang: 'ko-KR', // default: 'en-US'
//         // 부가 기능
//         minHeight: 300
//     });
// });

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

// [1] pjworker 전체 조회
const readAllpjworker = async () => {
    console.log("readAllpjworker func exe")
    // [1.1] 표시 영역
    const pjworkerTbody = document.querySelector("#pjworkerTbody")

    // [1.2] fetch
    const r = await fetch(`/project/worker?pjNo=${pjNo}`,{method:"GET"})
    const d = await r.json()
    console.log(d)
    let html = '';
    try{
        if(d.length!=0){
            d.forEach( (dto) => {
                html += `<tr>
                <td>${dto.pjRoleName}</td>
                <td>버튼 ${dto.pjRoleDescription}</td>
                <td data-userNo="${dto.userNo}">${dto.userName}</td>
                <td>${dto.userPhone}</td>
                <td>${dto.roadAddress}</td>
                <td>select${dto.pjRoleLv}</td>
                <td>버튼</td>
                <td>${dto.updateDate}</td>
                <td>삭제버튼</td>
                </tr>`
            } )
        }
        pjworkerTbody.innerHTML = html;

    } catch(error ){
        console.log(error)
    }
} // func end

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
                            <td><button class="btn btn-sm btn-success selectTemplateBtn">선택</button></td>
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
