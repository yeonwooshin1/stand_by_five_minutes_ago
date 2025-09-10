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
11. 인력 조회
12. 인력 검색
13. 인력 선택 버튼-모달 선택 시 정보 삽입 및 TemporarySaveWorker 업데이트
14. 삭제 버튼
15. 저장 버튼
16. 다음 버튼
*/

console.log("Pjworker func exe")

window.onHeaderReady = async () => {
    await loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    await readAllpjworker();
};

// [0] 로그인 체크
const loginCheck = async () => {
    // console.log("loginCheck func exe")
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    } else if (businessNo == null || businessNo === 0) {
        alert("[경고] 일반회원은 사용불가능한 메뉴입니다.")
        location.href = "/index.jsp"
    }
}

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR',
        // 부가 기능
        minHeight: 600
    });
});

// [1] 저장하기 이전 pjWorkDto를 저장 관리하기 위한 임시배열
const TemporarySaveWorker = [];
let currentRtName = "";
let currentRtDescription = "";

// [2] pjworker 전체 조회
const readAllpjworker = async () => {
    // console.log("readAllpjworker func exe")
    // [1.1] 표시 영역
    const pjworkerTbody = document.querySelector("#pjworkerTbody")

    // [1.2] fetch
    const r = await fetch(`/project/worker?pjNo=${pjNo}`, { method: "GET" })
    const d = await r.json()
    // console.log(d)
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
                <td><button class="btn btn-sm btn-outline-primary choiceWorkerBtn" data-bs-toggle="modal" data-bs-target="#workerModal">배정하기</button></td>
                <td>${dto.updateDate}</td>
                <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
                </tr>`
                // dto를 임시배열에 삽입
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
        // 화면 출력
        pjworkerTbody.innerHTML = html;
        // console.log(TemporarySaveWorker)
    } catch (error) {
        console.log(error)
    }
} // func end

// [03-1] 행 추가 시, 임시 PK 삽입
let tempRoleNoCounter = 100000;
function generateTempRoleNo() {
    return tempRoleNoCounter--;
}

// [03] 역할템플릿 모달 내에서, [선택] 버튼 클릭시 행 추가 이벤트 ============================================
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        // 선택 버튼에 속성 dataset을 가져옴
        const rtiName = e.target.dataset.rtiname;
        const rtiDesc = e.target.dataset.rtidescription;

        // 본문 입력을 위한 값 생성
        const fullRoleName = `${currentRtName}_${rtiName}`;
        const fullDescription = `${currentRtDescription}<br/>${rtiDesc}`;

        // 임시 PK 발급
        const tempRoleNo = generateTempRoleNo();

        // 본문 행 추가를 위한 tr 생성
        const newRow = document.createElement("tr");
        // 신규 tr에 dataset 주입
        newRow.setAttribute("data-pjRoleNo", tempRoleNo)
        // 신규 tr에 html 작성
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
            <td><button class="btn btn-sm btn-outline-primary choiceWorkerBtn" data-bs-toggle="modal" data-bs-target="#workerModal">배정하기</button></td>
            <td></td>
            <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>`;

        // 신규 tr을 본문에 append! 추가!
        document.querySelector("#pjworkerTbody").appendChild(newRow);

        // 임시배열 등록
        TemporarySaveWorker.push({
            pjRoleNo: tempRoleNo,
            pjNo: pjNo * 1,
            pjRoleName: fullRoleName,
            pjRoleDescription: fullDescription,
            userNo: null,
            pjRoleLv: 5,                // 입문자
            createDate: null,
            changeStatus: 1             // 신규
        });
    }
});

// [04] 행 추가 버튼 클릭 시 자유 입력 행 생성 =========================================
const addClearRow = async () => {
    // console.log("addClearRow func exe")

    // 신규 tr 생성
    const newRow = document.createElement("tr");
    // 임시 PK 생성
    const tempRoleNo = generateTempRoleNo();
    // 신규 tr에 dataset 주입
    newRow.setAttribute("data-pjRoleNo", tempRoleNo)
    // 신규 tr에 html 작성
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
        <td>
        <button class="btn btn-sm btn-outline-primary choiceWorkerBtn" data-bs-toggle="modal" data-bs-target="#workerModal">배정하기</button>
        </td>
        <td></td>
        <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
    `;
    // 신규 tr을 본문 Tbody에 append
    document.querySelector("#pjworkerTbody").appendChild(newRow);
    // 임시배열에 삽입
    TemporarySaveWorker.push({
        pjRoleNo: tempRoleNo,
        pjNo: pjNo * 1,
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
    // console.log("chooseRoleTemp func exe")
    // select 표시 영역
    const modalRoleTemplate = document.querySelector(".modalRoleTemplate")
    // 대분류 역할 정보 가져오기
    try {
        const r = await fetch("/roleTem")
        const d = await r.json()
        // console.log(d)

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

// [06] 역할템플릿검색 모달 내에서 대분류 선택시, [ 상세분류 표시 ]====================================
const chooseRoleTemItem = async (rtNo) => {
    // table body 영역
    const modalRoleTemTbdoy = document.querySelector("#modalRoleTemTbdoy")
    try {
        // fetch
        const r = await fetch(`/roleTem/Item?rtNo=${rtNo}`)
        const d = await r.json()
        // console.log(d)

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
// 모달에서 change가 event 발생하면 func 실행
document.querySelector(".modalRoleTemplate").addEventListener("change", function () {
    const rtNo = this.value; //select에서 선택한 option의 value
    // select 된 option을 dataset에 저장
    currentRtName = this.options[this.selectedIndex].dataset.rtname;
    currentRtDescription = this.options[this.selectedIndex].dataset.rtdescription;
    // console.log(rtNo)
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
// java와 통신하는 것이 아닌 임시배열에 변경 내용을 저장
const saveFullTemplate = async (pjRoleNo) => {
    // 저장할 구역 가져오기
    const fullDescription = document.querySelector("#descriptionArea").value
    // 임시배열에 저장
    TemporarySaveWorker.forEach((value) => {
        if (value.pjRoleNo == pjRoleNo) {
            value.pjRoleDescription = fullDescription;
            value.changeStatus = pjRoleNo > 7000000 ? 3 : 1;
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
            if (value.pjRoleNo == pjRoleNo) {
                value.pjRoleName = newRoleName;

                value.changeStatus = pjRoleNo > 7000000 ? 3 : 1;
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
                value.changeStatus = pjRoleNo > 7000000 ? 3 : 1;
            }
        });
    }
});

// [11] 인력 조회 ===================================================================
const readAllUser = async () => {
    // console.log("readAllUser func exe")
    const workerTbody = document.querySelector("#workerTbody")

    try {
        const r = await fetch(`/user/find/search`, { method: "GET" })
        const d = await r.json()
        // console.log(d)

        let html = ''
        let i = 0;
        d.forEach((dto) => {
            html += `            <tr>
                <td>${i + 1}</td>
                <td>${dto.userName}</td>
                <td>${dto.userPhone}</td>
                <td>${dto.roadAddress}</td>
                <td>
                    <button class="btn btn-sm btn-success selectWorkerBtn"
                        data-userno="${dto.userNo}"
                        data-name="${dto.userName}"
                        data-phone="${dto.userPhone}"
                        data-address="${dto.roadAddress}" data-bs-dismiss="modal" >선택</button>
                </td>
            </tr>`;
            i++
        })
        workerTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
};
readAllUser()

// [12-1] 인력 검색 ================================================================
const searchUser = async (event) => {
    const keyword = document.querySelector("#workerSearchInput").value

    try {
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
                    <button class="btn btn-sm btn-success selectWorkerBtn"
                        data-userno="${dto.userNo}"
                        data-name="${dto.userName}"
                        data-phone="${dto.userPhone}"
                        data-address="${dto.roadAddress}" data-bs-dismiss="modal" >선택</button>
                </td>
            </tr>`;
            i++
        });
        tbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
}

// [12-2] 엔터키 검색
document.querySelector("#workerSearchInput").addEventListener("keydown", function (e) {
    if (e.key === "Enter") {
        searchUser(); // 원하는 함수 실행
    }
});

// [13] 인력 선택 버튼 ====================================================================
// [13-1] 배정하기 클릭시, 배정하기 버튼이 포함된 tr에 target 설정하기
document.querySelector("#pjworkerTbody").addEventListener("click", function (e) {
    if (e.target.classList.contains("choiceWorkerBtn")) {
        const tr = e.target.closest("tr");
        tr.classList.add("selectedTarget");
    }
});

// [13-2] 모달 선택 시 정보 삽입 및 TemporarySaveWorker 업데이트
document.addEventListener("click", function (e) {

    if (e.target.classList.contains("selectWorkerBtn")) {
        const userNo = parseInt(e.target.dataset.userno);
        const userName = e.target.dataset.name;
        const userPhone = e.target.dataset.phone;
        const roadAddress = e.target.dataset.address;

        const targetRow = document.querySelector(".selectedTarget");
        if (targetRow) {
            targetRow.querySelector("td:nth-child(3)").textContent = userName;
            targetRow.querySelector("td:nth-child(3)").dataset.userno = userNo;
            targetRow.querySelector("td:nth-child(4)").textContent = userPhone;
            targetRow.querySelector("td:nth-child(5)").textContent = roadAddress;

            const pjRoleNo = parseInt(targetRow.dataset.pjroleno);
            TemporarySaveWorker.forEach((value) => {
                if (value.pjRoleNo === pjRoleNo) {
                    value.userNo = userNo;
                    value.changeStatus = pjRoleNo > 7000000 ? 3 : 1;
                }
            });

            targetRow.classList.remove("selectedTarget");
        }
    }
})

// [14] 삭제 버튼 ==================================
document.querySelector("#pjworkerTbody").addEventListener("click", async (e) => {
    if (!e.target.classList.contains("deleteBtn")) return;

    const tr = e.target.closest("tr");
    const pjRoleNo = parseInt(tr.dataset.pjroleno);

    // 만약 해당 pjRoleNo이 뒤에서 사용중이면 삭제불가처리
    const usedPjRoleNo = [];
    try{
        const r = await fetch(`/project/perform?pjNo=${pjNo}`)
        const d = await r.json()
        d.forEach( (dto) => {
            // dto.pjRoleNo가 이미 존재하지 않으면 push >> 중복방지
            if (!usedPjRoleNo.includes(dto.pjRoleNo)) {
                usedPjRoleNo.push(dto.pjRoleNo);
            }
        })
    }catch(error){
        console.log(error)
    }

    if(usedPjRoleNo.includes(pjRoleNo)){
        alert("[경고] 해당 역할은 이미 업무가 배정되어있습니다. \n '4. 업무배정' 에서 삭제하시려는 역할을 모두 제외시키신 후 역할 삭제를 다시 시도해주시기 바랍니다.")
        return;
    }


    const confirmDelete = confirm("한 번 삭제한 데이터는 복구할 수 없습니다. \n정말로 삭제하시겠습니까?");
    if (!confirmDelete) return;

    // 화면에서 해당 열 제거
    tr.remove();

    // TemporarySaveWorker 업데이트
    TemporarySaveWorker.forEach((value) => {
        if (value.pjRoleNo === pjRoleNo) {
            value.changeStatus = pjRoleNo > 7000000 ? 4 : 2;
        }
    });
});

// [15] 저장 버튼 =========================
const savePJworker = async () => {
    try {
        const r = await fetch("/project/worker", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(TemporarySaveWorker)
        });
        const d = await r.json();

        let success = 0;
        let fail = 0;

        d.forEach((value) => {
            TemporarySaveWorker.forEach((worker) => {
                if (worker.pjRoleNo == value.pjRoleNo && worker.changeStatus == 0) {
                    return;
                } else if (worker.pjRoleNo == value.pjRoleNo && worker.changeStatus > 0) {
                    if (value.pjRoleNo > 7000000 && value.Result === value.pjRoleNo) {
                        success++;
                    } else if (value.pjRoleNo > 7000000 && value.Result < 100) {
                        fail++;
                    } else if (value.pjRoleNo < 7000000 && value.Result > 7000000) {
                        success++;
                    } else if (value.pjRoleNo < 7000000 && value.Result < 100) {
                        fail++;
                    }
                }
            })
        });
        // 저장 작업 완료 후 초기화
        TemporarySaveWorker.length = 0;
        alert(`성공 ${success}건 / 실패 ${fail}건`);
        await readAllpjworker(); // 전체 조회 다시 실행
    } catch (error) {
        console.log(error)
    }
}

// [16] 다음 버튼 ============================================================
const nextStage = async () => {
    let result = confirm(`[경고] 저장을 하지 않고 다음 페이지로 이동하시면, 변경된 내용은 삭제되며 복구할 수 없습니다. \n계속 진행하시겠습니까?`)
    if (result == false) { return }

    location.href = `/project/checkList.jsp?pjNo=${pjNo}`;
} // func end
