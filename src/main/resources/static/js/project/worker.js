/*
00. 로그인 체크
01. 저장하기 이전 pkworkerDto를 저장하기위한 임시 배열 TemporarySaveWorker + 전역변수
02. pjworker 전체 조회
03. 역할템플릿 모달 내에서 선택 클릭시 행 추가 이벤트
04. 행 추가 버튼 클릭 시 자유 입력 행 생성
05. 역할 템플릿 모달 내 대분류-소분류 불러오기
06. 역할템플릿검색 모달 내에서 대분류 선택시 소분류 table이 업데이트
07. 설명보기 버튼 클릭 시 모달에 설명 표시
08. 설명 저장 (Description 모달 내용 저장)
09. 역할명 직접 수정 시, 임시배열 저장 
10. 숙련도 수정 시, 임시배열 저장
11. 배정하기 버튼 클릭 시 인력 검색 모달 활성화

*/

console.log("Pjworker func exe")

window.onHeaderReady = async () => {
    await loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    await readAllpjworker();
};

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR',
        // 부가 기능
        minHeight: 600
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

// [1] 저장하기 이전 pjWorkDto를 저장 관리하기 위한 배열
const TemporarySaveWorker = [];
let currentRtName = "";
let currentRtDescription = "";

// [2] pjworker 전체 조회
const readAllpjworker = async () => {
    console.log("readAllpjworker func exe")
    // [1.1] 표시 영역
    const pjworkerTbody = document.querySelector("#pjworkerTbody")

    // [1.2] fetch
    const r = await fetch(`/project/worker?pjNo=${pjNo}`, { method: "GET" })
    const d = await r.json()
    console.log(d)
    let html = '';
    try {
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<tr data-pjRoleNo="${dto.pjRoleNo}">
                <td>${dto.pjRoleName}</td>
                <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${dto.pjRoleNo})" data-bs-toggle="modal"
                        data-bs-target="#viewModal" >설명보기</button></td>
                <td data-userNo="${dto.userNo}">${dto.userName}</td>
                <td>${dto.userPhone}</td>
                <td>${dto.roadAddress}</td>
                <td><select class="form-select">
                    <option value="1" ${dto.pjRoleLv == 1 ? 'selected' : ''}>전문가</option>
                    <option value="2" ${dto.pjRoleLv == 2 ? 'selected' : ''}>상급</option>
                    <option value="3" ${dto.pjRoleLv == 3 ? 'selected' : ''}>중급</option>
                    <option value="4" ${dto.pjRoleLv == 4 ? 'selected' : ''}>초급</option>
                    <option value="5" ${dto.pjRoleLv == 5 ? 'selected' : ''}>입문자</option>
                    </select></td>
                <td><button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#workerModal">배정하기</button></td>
                <td>${dto.updateDate}</td>
                <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
                </tr>`

                TemporarySaveWorker.push({
                    pjRoleNo: dto.pjRoleNo,
                    pjNo: dto.pjNo,
                    pjRoleName: dto.pjRoleName,
                    pjRoleDescription: dto.pjRoleDescription,
                    userNo: dto.userNo,
                    pjRoleLv: dto.pjRoleLv,
                    createDate: dto.createDate,
                    updateDate: dto.updateDate,
                    changeStatus: 0
                });
            })
        }
        pjworkerTbody.innerHTML = html;
        console.log(TemporarySaveWorker)
    } catch (error) {
        console.log(error)
    }
} // func end

// [03-1] 행 추가 시, 임시 PK 삽입
let tempRoleNoCounter = 100000;
function generateTempRoleNo() {
    return tempRoleNoCounter--;
}

// [03] 역할템플릿 모달 내에서 선택 클릭시 행 추가 이벤트 ============================================
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        const rtiName = e.target.dataset.rtiname;
        const rtiDesc = e.target.dataset.rtidescription;

        const fullRoleName = `${currentRtName}_${rtiName}`;
        const fullDescription = `${currentRtDescription}<br/>${rtiDesc}`;

        const tempRoleNo = generateTempRoleNo();

        const newRow = document.createElement("tr");
        newRow.setAttribute("data-pjRoleNo", tempRoleNo)

        newRow.innerHTML = `
            <td>${fullRoleName}</td>
            <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${tempRoleNo})" 
            data-bs-toggle="modal" data-bs-target="#viewModal" >설명보기</button></td>
            <td data-userNo=""></td>
            <td></td>
            <td></td>
            <td>
                <select class="form-select">
                    <option value="1">전문가</option>
                    <option value="2">상급</option>
                    <option value="3">중급</option>
                    <option value="4">초급</option>
                    <option value="5" selected>입문자</option>
                </select>
            </td>
            <td><button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#workerModal">배정하기</button></td>
            <td></td>
            <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>`;

        document.querySelector("#pjworkerTbody").appendChild(newRow);

        TemporarySaveWorker.push({
            pjRoleNo: tempRoleNo,
            pjNo: pjNo*1 ,
            pjRoleName: fullRoleName,
            pjRoleDescription: fullDescription,
            userNo: null,
            pjRoleLv: 5,
            createDate: null,
            changeStatus: 1
        });
    }
});

// [04] 행 추가 버튼 클릭 시 자유 입력 행 생성 =========================================
const addClearRow = async () => {
    console.log("addClearRow func exe")
    const newRow = document.createElement("tr");

    // 임시 PK 생성
    const tempRoleNo = generateTempRoleNo();
    newRow.setAttribute("data-pjRoleNo", tempRoleNo)

    newRow.innerHTML = `
        <td contenteditable="true">직접입력</td>
        <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${tempRoleNo})" 
            data-bs-toggle="modal" data-bs-target="#viewModal">설명보기</button></td>
        <td data-userNo=""></td>
        <td></td>
        <td></td>
        <td>
            <select class="form-select">
                <option value="1">전문가</option>
                <option value="2">상급</option>
                <option value="3">중급</option>
                <option value="4">초급</option>
                <option value="5" selected>입문자</option>
            </select>
        </td>
        <td><button class="btn btn-sm btn-outline-primary" data-bs-toggle="modal" data-bs-target="#workerModal>배정하기</button></td>
        <td></td>
        <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
    `;
    document.querySelector("#pjworkerTbody").appendChild(newRow);

    TemporarySaveWorker.push({
        pjRoleNo: tempRoleNo,
        pjNo: pjNo*1 ,
        pjRoleName: "",
        pjRoleDescription: "",
        userNo: 0,
        pjRoleLv: 5,
        createDate: "",
        changeStatus: 1
    });
}


// [05] 역할 템플릿 모달 내 대분류-소분류 불러오기===========================================
const chooseRoleTemp = async () => {
    console.log("chooseRoleTemp func exe")
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
                html += ` <option value=${dto.rtNo} 
                data-rtname="${dto.rtName}" 
                data-rtdescription="${dto.rtDescription}">${dto.rtName}</option>`
            });
        }
        modalRoleTemplate.innerHTML += html;
    } catch (error) {
        console.log(error)
    }
} // func end
chooseRoleTemp();

// [06] 역할템플릿검색 모달 내에서 대분류 선택시 상세분류 표시====================================
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
                            <td>
                            <button class="btn btn-sm btn-success selectTemplateBtn"
                            data-rtiname="${dto.rtiName}"
                            data-rtidescription="${dto.rtiDescription}" data-bs-dismiss="modal"
                            >선택</button>
                            </td>
                        </tr>`
                i++
            });
        }
        modalRoleTemTbdoy.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [06-1] 역할템플릿 모달 내에서 대분류 명을 선택하면 소분류 table이 업데이트 될 수 있도록 함 =====================
document.querySelector(".modalRoleTemplate").addEventListener("change", function () {
    const rtNo = this.value;
    // select 된 option을 변수에 저장
    currentRtName = this.options[this.selectedIndex].dataset.rtname;
    currentRtDescription = this.options[this.selectedIndex].dataset.rtdescription;
    console.log(rtNo)
    if (rtNo != 0) {
        chooseRoleTemItem(rtNo);
    }
});

// [07] 설명보기 버튼 클릭 시 모달에 설명 표시 ===============================
const veiwDescription = async (pjRoleNo) => {
    // 저장버튼 구역 생성
    let html1 = `<button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="saveFullTemplate(${pjRoleNo})">저장</button>`
    document.querySelector("#fullDescriptBtnBox").innerHTML = html1

    // 내용 붙이기
    const fullDescriptEditer = document.querySelector("#fullDescriptBody .note-editable")
    let html = '';
    TemporarySaveWorker.forEach((value) => {
        if (value.pjRoleNo == pjRoleNo) {
            html += value.pjRoleDescription
        }
    })
    fullDescriptEditer.innerHTML = html;
} // func end

// [08] 설명 저장 ==========================================================
const saveFullTemplate = async (pjRoleNo) => {
    // 저장할 구역 가져오기
    const fullDescription = document.querySelector("#descriptionArea").value
    // 임시배열에 저장
    TemporarySaveWorker.forEach((value) => {
        if (value.pjRoleNo == pjRoleNo) {
            value.pjRoleDescription = fullDescription;
            if (pjRoleNo > 7000000) {
                value.changeStatus = 3
            }else{
                value.changeStatus = 1
            }
        }
    })
} // func end

// [09] 역할명 직접 수정 시, 임시배열 저장 ====================================
document.querySelector("#pjworkerTbody").addEventListener("input", function (e) {
    if (e.target.tagName == "TD" && e.target.isContentEditable) {
        const tr = e.target.closest("tr");
        const pjRoleNo = parseInt(tr.dataset.pjroleno); // data-pjRoleNo 값 추출
        const newRoleName = e.target.textContent.trim(); // 변경된 역할명

        TemporarySaveWorker.forEach((value) => {
            if (value.pjRoleNo === pjRoleNo) {
                value.pjRoleName = newRoleName;

                if (pjRoleNo > 7000000) {
                    value.changeStatus = 3;
                } else {
                    value.changeStatus = 1;
                }
            }
        });
    }
});

// [10] 숙련도 수정 시, 임시배열 저장 ==============================================
document.querySelector("#pjworkerTbody").addEventListener("change", function (e) {
    if (e.target.tagName == "SELECT") {
        const selectedValue = parseInt(e.target.value); // 선택된 숙련도 값
        const tr = e.target.closest("tr");
        const pjRoleNo = parseInt(tr.dataset.pjroleno); // tr의 data-pjRoleNo 값

        TemporarySaveWorker.forEach((value) => {
            if (value.pjRoleNo === pjRoleNo) {
                value.pjRoleLv = selectedValue;
                if (pjRoleNo > 7000000) {
                    value.changeStatus = 3;
                } else {
                    value.changeStatus = 1;
                }
            }
        });
    }
});

// [11] 배정하기 버튼 클릭 시 인력 검색 모달 활성화 =================================
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("assignBtn")) {
        const targetRow = e.target.closest("tr");
        targetRow.classList.add("assignTarget");
        bootstrap.Modal.getOrCreateInstance(document.getElementById("workerModal")).show();
    }
});

// 인력 검색 input 이벤트
document.getElementById("workerSearchInput").addEventListener("input", async function () {
    const keyword = this.value;
    const r = await fetch(`/user/find/search?keyword=${keyword}`);
    const d = await r.json();

    const tbody = document.querySelector("#workerTable tbody");
    let html = '';
    let i = 0;
    d.forEach((dto) => {
        html += `
            <tr>
                <td>${i + 1}</td>
                <td>${dto.userName}</td>
                <td>${dto.userPhone}</td>
                <td>${dto.roadAddress}</td>
                <td>
                    <button class="btn btn-sm btn-info selectWorkerBtn"
                        data-userno="${dto.userNo}"
                        data-name="${dto.userName}"
                        data-phone="${dto.userPhone}"
                        data-address="${dto.roadAddress}">선택</button>
                </td>
            </tr>`;
        i++
    });
    tbody.innerHTML = html;
});

// 선택 버튼 클릭 시 메인 테이블에 인력 정보 삽입
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("selectWorkerBtn")) {
        const name = e.target.dataset.name;
        const phone = e.target.dataset.phone;
        const address = e.target.dataset.address;
        const userNo = e.target.dataset.userno;

        const targetRow = document.querySelector(".assignTarget");
        if (targetRow) {
            targetRow.querySelector("td:nth-child(3)").textContent = name;
            targetRow.querySelector("td:nth-child(3)").dataset.userno = userNo;
            targetRow.querySelector("td:nth-child(4)").textContent = phone;
            targetRow.querySelector("td:nth-child(5)").textContent = address;
            targetRow.classList.remove("assignTarget");
        }
        bootstrap.Modal.getInstance(document.getElementById("workerModal")).hide();
    }
});

// [] 저장 메소드 =========================
const savePJworker = async () => {
    const r = await fetch("/project/worker", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(TemporarySaveWorker)
    });
    const d = await r.json();
    alert(`성공 ${d.success}건 / 실패 ${d.failure}건`);
    await readAllpjworker(); // 전체 조회 다시 실행

}