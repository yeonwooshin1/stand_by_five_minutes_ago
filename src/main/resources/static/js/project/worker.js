console.log("Pjworker func exe")

window.onHeaderReady = () => {
    loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
};

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

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



// 역할 템플릿 선택 버튼 클릭 시 메인 테이블에 행 추가
document.querySelectorAll(".selectTemplateBtn").forEach(btn => {
    btn.addEventListener("click", function () {
        const row = this.closest("tr");

        // 데이터 속성으로부터 정보 추출
        const rtName = this.dataset.rtname;
        const rtiName = this.dataset.rtiname;
        const rtDesc = this.dataset.rtdescription;
        const rtiDesc = this.dataset.rtidescription;

        const fullRoleName = `${rtName}_${rtiName}`;
        const fullDescription = `${rtDesc}<br/>${rtiDesc}`;

        const newRow = document.createElement("tr");
        newRow.innerHTML = `
            <td>${fullRoleName}</td>
            <td><button class="btn btn-sm btn-outline-secondary viewDescBtn">설명보기</button></td>
            <td></td> <!-- 이름 -->
            <td></td> <!-- 연락처 -->
            <td></td> <!-- 주소 -->
            <td>
                <select class="form-select">
                    <option value="1">전문가</option>
                    <option value="2">상급</option>
                    <option value="3">중급</option>
                    <option value="4">초급</option>
                    <option value="5">입문자</option>
                </select>
            </td>
            <td><button class="btn btn-sm btn-outline-primary assignBtn">배정하기</button></td>
            <td></td>
            <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
        `;
        newRow.dataset.description = fullDescription;
        document.querySelector("#mainTable tbody").appendChild(newRow);
        roleTemplateModal.hide();
    });
});

// 설명보기 버튼 클릭 시 모달에 설명 표시
document.querySelector("#mainTable tbody").addEventListener("click", function (e) {
    if (e.target.classList.contains("viewDescBtn")) {
        const row = e.target.closest("tr");
        const desc = row.dataset.description || "설명이 없습니다.";
        const modalBody = document.querySelector("#viewModal .modal-body");
        modalBody.innerHTML = desc;
        const viewModalInstance = new bootstrap.Modal(document.getElementById("viewModal"));
        viewModalInstance.show();
    }
});
