
console.log("Pjworker func exe");

window.onHeaderReady = () => {
    loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
};

// Summernote 초기화
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR',
        minHeight: 300
    });
});

// 로그인 체크
const loginCheck = async () => {
    console.log("loginCheck func exe");
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.");
        location.href = "/index.jsp";
    } else if (businessNo == null || businessNo === 0) {
        alert("[경고] 일반회원은 사용불가능한 메뉴입니다.");
        location.href = "/index.jsp";
    }
};

// 역할 템플릿 모달 열기
document.getElementById("templateSearchBtn").addEventListener("click", () => {
    const roleTemplateModalInstance = new bootstrap.Modal(document.getElementById("roleTemplateModal"));
    roleTemplateModalInstance.show();
});

// 역할 템플릿 대분류 불러오기
const chooseRoleTemp = async () => {
    const modalRoleTemplate = document.querySelector(".modalRoleTemplate");
    try {
        const r = await fetch("/roleTem");
        const d = await r.json();
        let html = '';
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<option value="${dto.rtNo}">${dto.rtName}</option>`;
            });
        }
        modalRoleTemplate.innerHTML += html;
    } catch (error) {
        console.log(error);
    }
};

// 역할 템플릿 소분류 불러오기
const chooseRoleTemItem = async (rtNo) => {
    const modalRoleTemTbdoy = document.querySelector("#modalRoleTemTbdoy");
    try {
        const r = await fetch(`/roleTem/Item?rtNo=${rtNo}`);
        const d = await r.json();
        let html = '';
        let i = 1;
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<tr>
                    <td>${i}</td>
                    <td>${dto.rtiName}</td>
                    <td>
                        <button class="btn btn-sm btn-success selectTemplateBtn"
                            data-rtname="${dto.rtName}"
                            data-rtiname="${dto.rtiName}"
                            data-rtdescription="${dto.rtDescription}"
                            data-rtidescription="${dto.rtiDescription}">
                            선택
                        </button>
                    </td>
                </tr>`;
                i++;
            });
        }
        modalRoleTemTbdoy.innerHTML = html;

        // 선택 버튼 이벤트 등록
        document.querySelectorAll(".selectTemplateBtn").forEach(btn => {
            btn.addEventListener("click", function () {
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
                    <td></td>
                    <td></td>
                    <td></td>
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
                const roleTemplateModalInstance = bootstrap.Modal.getInstance(document.getElementById("roleTemplateModal"));
                roleTemplateModalInstance.hide();
            });
        });

    } catch (error) {
        console.log(error);
    }
};

// 대분류 선택 시 소분류 불러오기
document.querySelector(".modalRoleTemplate").addEventListener("change", function () {
    const rtNo = this.value;
    if (rtNo != 0) {
        chooseRoleTemItem(rtNo);
    }
});

// 설명보기 버튼 클릭 시 모달에 설명 표시
document.querySelector("#mainTable tbody").addEventListener("click", function (e) {
    if (e.target.classList.contains("viewDescBtn")) {
        const row = e.target.closest("tr");
        const desc = row.dataset.description || "";
        $('#descriptionArea').summernote('code', desc);
        const viewModalInstance = new bootstrap.Modal(document.getElementById("viewModal"));
        viewModalInstance.show();
    }
});

// 행 추가 버튼 클릭 시 자유 입력 행 생성
document.getElementById("addRowBtn").addEventListener("click", () => {
    const newRow = document.createElement("tr");
    newRow.innerHTML = `
        <td contenteditable="true">직접입력</td>
        <td><button class="btn btn-sm btn-outline-secondary viewDescBtn">설명보기</button></td>
        <td></td>
        <td></td>
        <td></td>
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
    newRow.dataset.description = "";
    document.querySelector("#mainTable tbody").appendChild(newRow);
});

chooseRoleTemp();
