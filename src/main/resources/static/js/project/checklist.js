/*
00. 로그인 체크
01. 저장하기 이전 pjCheckDto를 저장하기위한 임시 배열 TemporarySaveWorker + 전역변수
02. pjcheck 전체 조회 (PJC-02번 기능)
/*
00. 로그인 체크
01. 전역 변수 및 임시 저장 배열
02. pjcheck 전체 조회 (PJC-02)
03. 템플릿 선택 시 행 추가 (PJC-08)
04. 행 추가 버튼 클릭 시 자유 입력 행 생성 (PJC-01)
05. 체크리스트 템플릿 대분류 조회 (PJC-06)
06. 체크리스트 템플릿 소분류 조회 (PJC-07)
07. 설명보기 버튼 클릭 (PJC-03)
08. 설명 저장 (PJC-04)
09. 체크리스트명 직접 수정
10. 삭제 버튼 (PJC-05)
11. 저장 버튼
12. 다음 버튼
*/

console.log("checklist.js loaded");

// 전역 변수
const pjNo = new URLSearchParams(location.search).get("pjNo");
const TemporarySaveChecklist = [];
let currentCtName = "";
let currentCtDescription = "";
let tempCheckNoCounter = -1; // 임시 ID 카운터 (음수로 시작)

// 페이지 로드 시 실행
window.onHeaderReady = async () => {
    await loginCheck();
    await readAllpjcheck();
};

// [00] 로그인 체크
async function loginCheck() {
    if (!userNo) {
        alert("[경고] 로그인 후 이용 가능합니다.");
        location.href = "/index.jsp";
    } else if (!businessNo) {
        alert("[경고] 기업 회원만 사용 가능한 메뉴입니다.");
        location.href = "/index.jsp";
    }
}

// [01] Summernote 초기화
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR',
        minHeight: 300,
        placeholder: '체크리스트에 대한 상세 설명을 입력하세요.'
    });
});

// [02] pjcheck 전체 조회
async function readAllpjcheck() {
    const pjchecklistTbody = document.querySelector("#pjchecklistTbody");
    try {
        const response = await fetch(`/project/checklist?pjNo=${pjNo}`);
        const data = await response.json();

        let html = '';
        TemporarySaveChecklist.length = 0; // 배열 초기화

        if (data.length > 0 && data[0].status !== "NOT_FOUND") {
            data.forEach((dto) => {
                html += `
                    <tr data-pjchkitemno="${dto.pjChkItemNo}">
                        <td> ${dto.pjChkItemNo}</td>
                        <td contenteditable="true">${dto.pjChklTitle}</td>
                        <td>
                            <button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick="viewDescription(${dto.pjChkItemNo})">
                                설명보기
                            </button>
                        </td>
                        <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
                    </tr>`;

                TemporarySaveChecklist.push({
                    pjChkItemNo: dto.pjChkItemNo,
                    pjNo: dto.pjNo,
                    pjChklTitle: dto.pjChklTitle,
                    pjHelpText: dto.pjHelpText,
                    changeStatus: 0 // 0: 원본
                });
            });
        }
        pjchecklistTbody.innerHTML = html;
    } catch (error) {
        console.error("Error fetching checklist:", error);
    }
}

// [03] 템플릿 선택 시 행 추가
document.addEventListener("click", async function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        const ctiNo = e.target.dataset.ctino;
        
        // 템플릿 데이터 불러오기 및 저장
        const response = await fetch(`/project/checklist/tem`, {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ ctiNo: parseInt(ctiNo), pjNo: parseInt(pjNo) })
        });
        const newPjChkItemNo = await response.json();

        if (newPjChkItemNo > 0) {
            alert('템플릿이 추가되었습니다. 저장 버튼을 눌러야 최종 반영됩니다.');
            await readAllpjcheck(); // 목록 새로고침
        } else {
            alert('템플릿 불러오기에 실패했습니다.');
        }
        
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('checkTemplateModal'));
        modal.hide();
    }
});


// [04] 행 추가 버튼 (자유 입력)
function addClearRow() {
    const newRow = document.createElement("tr");
    const tempId = tempCheckNoCounter--;
    newRow.dataset.pjchkitemno = tempId;

    newRow.innerHTML = `
        <td contenteditable="true">새 체크리스트</td>
        <td>
            <button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick="viewDescription(${tempId})">
                설명보기
            </button>
        </td>
        <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
    `;

    document.querySelector("#pjchecklistTbody").appendChild(newRow);

    TemporarySaveChecklist.push({
        pjChkItemNo: tempId,
        pjNo: parseInt(pjNo),
        pjChklTitle: "새 체크리스트",
        pjHelpText: "",
        changeStatus: 1 // 1: 신규
    });
}

// [05] & [06] 체크리스트 템플릿 모달 관련
$('#checkTemplateModal').on('show.bs.modal', async function () {
    const modalCheckTemplate = document.querySelector(".modalCheckTemplate");
    
    // 대분류 데이터가 이미 로드되었는지 확인
    if (modalCheckTemplate.options.length > 1) return;

    try {
        // 임의의 ctNo 배열 또는 서버에서 목록을 가져오는 로직 필요
        // 여기서는 예시로 4000001부터 4000005까지를 가정합니다.
        const ctNoList = [4000001, 4000002, 4000003, 4000004, 4000005];
        let optionsHtml = '';

        for (const ctNo of ctNoList) {
            const response = await fetch(`/project/checklist/tem?ctNo=${ctNo}`);
            const dto = await response.json();
            if (dto && dto.ctName) {
                optionsHtml += `<option value="${dto.ctNo}" data-ctname="${dto.ctName}" data-ctdescription="${dto.ctDescription}">${dto.ctName}</option>`;
            }
        }
        modalCheckTemplate.innerHTML += optionsHtml;

    } catch (error) {
        console.error("Error fetching template categories:", error);
    }
});

document.querySelector(".modalCheckTemplate").addEventListener("change", async function () {
    const ctNo = this.value;
    const modalCheckTemTbody = document.querySelector("#modalCheckTemTbody");

    if (ctNo === "0") {
        modalCheckTemTbody.innerHTML = '<tr><td colspan="3">대분류를 선택하세요.</td></tr>';
        return;
    }

    try {
        const response = await fetch(`/project/checklist/item?ctNo=${ctNo}`);
        const data = await response.json();
        let html = '';
        if (data.length > 0) {
            data.forEach((dto, index) => {
                html += `
                    <tr>
                        <td>${index + 1}</td>
                        <td>${dto.ctiTitle}</td>
                        <td>
                            <button class="btn btn-sm btn-success selectTemplateBtn"
                                data-ctino="${dto.ctiNo}">선택</button>
                        </td>
                    </tr>`;
            });
        } else {
            html = '<tr><td colspan="3">해당 분류에 항목이 없습니다.</td></tr>';
        }
        modalCheckTemTbody.innerHTML = html;
    } catch (error) {
        console.error("Error fetching template items:", error);
    }
});


// [07] 설명 보기
function viewDescription(pjChkItemNo) {
    const item = TemporarySaveChecklist.find(d => d.pjChkItemNo == pjChkItemNo);
    if (item) {
        $('#descriptionArea').summernote('code', item.pjHelpText);
    }
    // 저장 버튼에 현재 항목의 ID를 데이터로 설정
    document.querySelector("#saveDescBtn").dataset.pjchkitemno = pjChkItemNo;
    
    const viewModal = new bootstrap.Modal(document.getElementById('viewModal'));
    viewModal.show();
}

// [08] 설명 저장
document.querySelector("#saveDescBtn").addEventListener("click", function() {
    const pjChkItemNo = this.dataset.pjchkitemno;
    const newDescription = $('#descriptionArea').summernote('code');
    
    const item = TemporarySaveChecklist.find(d => d.pjChkItemNo == pjChkItemNo);
    if (item) {
        item.pjHelpText = newDescription;
        if (item.changeStatus === 0) { // 원본 데이터인 경우
            item.changeStatus = 3; // 3: 수정
        }
    }
    
    const viewModal = bootstrap.Modal.getInstance(document.getElementById('viewModal'));
    viewModal.hide();
    alert("설명이 임시 저장되었습니다. 최종 저장을 위해 '저장' 버튼을 눌러주세요.");
});


// [09] 체크리스트명 직접 수정
document.querySelector("#pjchecklistTbody").addEventListener("input", function (e) {
    if (e.target.tagName === "TD" && e.target.isContentEditable) {
        const tr = e.target.closest("tr");
        const pjChkItemNo = parseInt(tr.dataset.pjchkitemno);
        const newTitle = e.target.textContent;

        const item = TemporarySaveChecklist.find(d => d.pjChkItemNo === pjChkItemNo);
        if (item) {
            item.pjChklTitle = newTitle;
            if (item.changeStatus === 0) {
                item.changeStatus = 3; // 3: 수정
            }
        }
    }
});

// [10] 삭제 버튼
document.querySelector("#pjchecklistTbody").addEventListener("click", (e) => {
    if (!e.target.classList.contains("deleteBtn")) return;

    const tr = e.target.closest("tr");
    const pjChkItemNo = parseInt(tr.dataset.pjchkitemno);

    if (!confirm("정말로 삭제하시겠습니까?")) return;

    const item = TemporarySaveChecklist.find(d => d.pjChkItemNo === pjChkItemNo);
    if (item) {
        if (item.changeStatus === 1) { // 프론트에서만 생성된 항목
            const index = TemporarySaveChecklist.indexOf(item);
            TemporarySaveChecklist.splice(index, 1);
        } else {
            item.changeStatus = 4; // 4: 삭제
        }
    }
    tr.remove();
});

// [11] 저장 버튼
async function savePJchecklist() {
    try {
        const response = await fetch("/project/checklist/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(TemporarySaveChecklist)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();
        
        let successCount = 0;
        let failCount = 0;

        result.forEach(res => {
            if (res.Result > 0) {
                successCount++;
            } else {
                failCount++;
            }
        });

        alert(`저장 완료: 성공 ${successCount}건, 실패 ${failCount}건`);
        await readAllpjcheck(); // 목록 새로고침

    } catch (error) {
        console.error("Error saving checklist:", error);
        alert("저장 중 오류가 발생했습니다.");
    }
}

// [12] 다음 버튼
function nextStage() {
    const hasUnsavedChanges = TemporarySaveChecklist.some(item => item.changeStatus !== 0);
    if (hasUnsavedChanges) {
        if (!confirm("[경고] 저장하지 않은 변경사항이 있습니다. 저장하지 않고 이동하시겠습니까?")) {
            return;
        }
    }
    // 다음 페이지 URL을 여기에 입력하세요. 예:
    // location.href = `/project/nextpage.jsp?pjNo=${pjNo}`;
    alert("다음 단계로 이동합니다.");
}

/*
04. 행 추가 버튼 클릭 시 자유 입력 행 생성 , 프로젝트 체크리스트 추가 (PJC-01번 기능)
05. 역할 템플릿 모달 내 대분류-소분류 불러오기 (PJC-06번 기능)
06. 역할템플릿검색 모달 내에서 대분류 선택시 소분류 table이 업데이트 (PJC-07번 기능)
07. 설명보기 버튼 클릭 시 모달에 설명 표시 (PJC-03번 기능)
08. 설명 저장 (Description 모달 내용 저장) (PJC-04번 기능)
09. 체크리스트명 직접 수정 시, 임시배열 저장 

11. 프로젝트 체크리스트 설명 조회 (PJC-03번 기능)

13. 인력 선택 버튼-모달 선택 시 정보 삽입 및 TemporarySavecheck 업데이트 (PJC-08번 기능)
14. 프로젝트 체크리스트 삭제 버튼 (PJC-05번 기능)
15. 저장 버튼
16. 다음 버튼
*/

console.log("Pjworker func exe")

window.onHeaderReady = async () => {
    await loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    await readAllpjcheck();
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

// [ 체크리스트템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#descriptionArea').summernote({
        lang: 'ko-KR',
        // 부가 기능
        minHeight: 600
    });
});

// [2] pjcheck 전체 조회  (PJC-02번 기능)
const readAllpjcheck = async () => {
    // console.log("readAllpjworker func exe")
    // [1.1] 표시 영역
    const pjchecklistTbody = document.querySelector("#pjchecklistTbody")

    // [1.2] fetch
    const r = await fetch(`/project/checklist?pjNo=${pjNo}`, { method: "GET" })
    const d = await r.json()
    // console.log(d)
    let html = '';
    try {
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<tr data-pjChkItemNo="${dto.pjChkItemNo}">
                <td>${dto.pjChklTitle}</td>
                <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${dto.pjChkItemNo})" data-bs-toggle="modal"
                        data-bs-target="#viewModal" >설명보기</button></td>
                <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
                </tr>`

                TemporarySaveChecklist.push({
                    pjChkItemNo: dto.pjChkItemNo,
                    pjNo: dto.pjNo,
                    pjChklTitle: dto.pjChklTitle,
                    pjHelpText: dto.pjHelpText,
                    createDate: dto.createDate,
                    updateDate: dto.updateDate,
                    changeStatus: 0 // ???
                });
            })
        }
        pjchecklistTbody.innerHTML = html;
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

// [03] 역할템플릿 모달 내에서 선택 클릭시 행 추가 이벤트 ============================================
document.addEventListener("click", function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        const rtiName = e.target.dataset.rtiname;
        const rtiDesc = e.target.dataset.rtidescription;

        const fullCheckName = `${currentRtName}_${rtiName}`;
        const fullDescription = `${currentRtDescription}<br/>${rtiDesc}`;

        const tempCheckNo = generateTempRoleNo();

        const newRow = document.createElement("tr");
        newRow.setAttribute("data-pjCheckNo", tempCheckNo)

        newRow.innerHTML =
                `<td>${fullCheckName}</td>
                <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${tempCheckNo})" data-bs-toggle="modal"
                        data-bs-target="#viewModal" >설명보기</button></td>
                <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
                </tr>`;

        document.querySelector("#pjchecklistTbody").appendChild(newRow);

        TemporarySaveChecklist.push({
            pjChkItemNo: tempCheckNo,
            pjNo: pjNo * 1,
            pjChklTitle: fullCheckName,
            pjHelpText: fullDescription,
            createDate: null,
            changeStatus: 1
        });
    }
});

// [04] 행 추가 버튼 클릭 시 자유 입력 행 생성 프로젝트 체크리스트 추가 (PJC-01번 기능) =========================================
const addClearRow = async () => {
    // console.log("addClearRow func exe")
    const newRow = document.createElement("tr");

    // 임시 PK 생성
    const tempCheckNo = generateTempRoleNo();
    newRow.setAttribute("data-pjCheckNo", tempCheckNo)

    newRow.innerHTML = 
            `<td contenteditable="true">직접입력</td>
                <td>${tempCheckNo}</td>
                <td></td>
                <td><button class="btn btn-sm btn-outline-secondary viewDescBtn" onclick = "veiwDescription(${tempCheckNo})" 
                    data-bs-toggle="modal" data-bs-target="#viewModal">설명보기</button></td>
                <td><button class="btn btn-sm btn-danger deleteBtn">삭제</button></td>
            `;


    document.querySelector("#pjchecklistTbody").appendChild(newRow);

    TemporarySaveChecklist.push({
        pjChkItemNo: tempCheckNo,
        pjNo: pjNo * 1,
        pjChklTitle: "",
        pjHelpText: "",
        createDate: "",
        changeStatus: 1
    });
}


// [05] 역할 템플릿 모달 내 대분류-소분류 불러오기  (PJC-06번 기능)===========================================
const chooseCheckTemp = async () => {
    // console.log("chooseRoleTemp func exe")
    // select 표시 영역
    const modalCheckTemplate = document.querySelector(".modalCheckTemplate")
    // 대분류 역할 정보 가져오기
    try {
        const r = await fetch(`/project/checklist/tem?ctNo=${ctNo}`)
        const d = await r.json()
        // console.log(d)

        let html = '';
        if (d.length != 0) {
            d.forEach((dto) => {
                html += ` <option value=${dto.ctNo} 
                data-ctname="${dto.ctName}" 
                data-ctdescription="${dto.ctDescription}">${dto.ctName}</option>`
            });
        }
        modalCheckTemplate.innerHTML += html;
    } catch (error) {
        console.log(error)
    }
} // func end
chooseCheckTemp();

// [06] 역할템플릿검색 모달 내에서 대분류 선택시 상세분류 표시 (PJC-07번 기능) ==================================== 
const chooseRoleTemItem = async (ctNo) => {
    const modalCheckTemTbody = document.querySelector("#modalCheckTemTbody")
    try {
        const r = await fetch(`/project/checklist/item?ctNo=${ctNo}`)
        const d = await r.json()
        // console.log(d)

        let html = '';
        let i = 1;
        if (d.length != 0) {
            d.forEach((dto) => {
                html += `<tr data-ctiNo = "${dto.ctiNo}">
                            <td>${i}</td>
                            <td>${dto.ctiTitle}</td>
                            <td>
                            <button class="btn btn-sm btn-success selectTemplateBtn"
                            data-ctiTitle="${dto.ctiTitle}"
                            data-ctihelptext="${dto.ctiHelpText}" data-bs-dismiss="modal"
                            >선택</button>
                            </td>
                        </tr>`
                i++
            });
        }
        modalCheckTemTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [06-1] 역할템플릿 모달 내에서 대분류 명을 선택하면 소분류 table이 업데이트 될 수 있도록 함 =====================
document.querySelector(".modalCheckTemplate").addEventListener("change", function () {
    const ctNo = this.value;
    // select 된 option을 변수에 저장
    currentCtName = this.options[this.selectedIndex].dataset.ctname;
    currentCtDescription = this.options[this.selectedIndex].dataset.ctdescription;
    // console.log(rtNo)
    if (ctNo != 0) {
        chooseRoleTemItem(ctNo);
    }
});

// [07] 설명보기 버튼 클릭 시 모달에 설명 표시 (PJC-03번 기능) ===============================
const veiwDescription = async (pjChkItemNo) => {
    // 저장버튼 구역 생성
    let html1 = `<button type="button" class="btn btn-primary" data-bs-dismiss="modal" onclick="saveFullTemplate(${pjChkItemNo})">저장</button>`
    document.querySelector("#fullDescriptBtnBox").innerHTML = html1

    // 내용 붙이기
    const fullDescriptEditer = document.querySelector("#fullDescriptBody .note-editable")
    let html = '';
    TemporarySaveChecklist.forEach((value) => {
        if (value.pjChkItemNo == pjChkItemNo) {
            html += value.pjHelpText
        }
    })
    fullDescriptEditer.innerHTML = html;
} // func end

// [08] 설명 저장 (PJC-04번 기능) ==========================================================
const saveFullTemplate = async (pjChkItemNo) => {
    // 저장할 구역 가져오기
    const fullDescription = document.querySelector("#descriptionArea").value
    // 임시배열에 저장
    TemporarySaveChecklist.forEach((value) => {
        if (value.pjChkItemNo == pjChkItemNo) {
            value.pjHelpText = fullDescription;
            value.changeStatus = pjChkItemNo > 8000000 ? 3 : 1;
        }
    })
} // func end

// [09] 체크리스트명 직접 수정 시, 임시배열 저장 ====================================
document.querySelector("#pjchecklistTbody").addEventListener("input", function (e) {
    if (e.target.tagName == "TD" && e.target.isContentEditable) {
        const tr = e.target.closest("tr");
        const pjChkItemNo = parseInt(tr.dataset.pjChkItemNo); // data-pjRoleNo 값 추출
        const newCheckName = e.target.textContent.trim(); // 변경된 역할명

        TemporarySaveWorker.forEach((value) => {
            if (value.pjChkItemNo == pjChkItemNo) {
                value.pjChklTitle = newCheckName;

                value.changeStatus = pjChkItemNo > 8000000 ? 3 : 1;
            }
        });
    }
});

// [11] 프로젝트 체크리스트 설명 조회 (PJC-03번 기능) ===================================================================
// const readAllUser = async () => {
//     // console.log("readAllUser func exe")
//     const workerTbody = document.querySelector("#workerTbody")

//     try {
//         const r = await fetch(`/user/find/search`, { method: "GET" })
//         const d = await r.json()
//         // console.log(d)

//         let html = ''
//         let i = 0;
//         d.forEach((dto) => {
//             html += `            <tr>
//                 <td>${i + 1}</td>
//                 <td>${dto.userName}</td>
//                 <td>${dto.userPhone}</td>
//                 <td>${dto.roadAddress}</td>
//                 <td>
//                     <button class="btn btn-sm btn-success selectWorkerBtn"
//                         data-userno="${dto.userNo}"
//                         data-name="${dto.userName}"
//                         data-phone="${dto.userPhone}"
//                         data-address="${dto.roadAddress}" data-bs-dismiss="modal" >선택</button>
//                 </td>
//             </tr>`;
//             i++
//         })
//         workerTbody.innerHTML = html;
//     } catch (error) {
//         console.log(error)
//     }
// };
// readAllUser()

// // [12-1] 인력 검색 ================================================================
// const searchUser = async (event) => {
//     const keyword = document.querySelector("#workerSearchInput").value

//     try {
//         const r = await fetch(`/user/find/search?keyword=${keyword}`);
//         const d = await r.json();

//         const tbody = document.querySelector("#workerTable tbody");
//         let html = '';
//         let i = 0;
//         d.forEach((dto) => {
//             html += `
//             <tr>
//                 <td>${i + 1}</td>
//                 <td>${dto.userName}</td>
//                 <td>${dto.userPhone}</td>
//                 <td>${dto.roadAddress}</td>
//                 <td>
//                     <button class="btn btn-sm btn-success selectWorkerBtn"
//                         data-userno="${dto.userNo}"
//                         data-name="${dto.userName}"
//                         data-phone="${dto.userPhone}"
//                         data-address="${dto.roadAddress}" data-bs-dismiss="modal" >선택</button>
//                 </td>
//             </tr>`;
//             i++
//         });
//         tbody.innerHTML = html;
//     } catch (error) { 
//         console.log(error) 
//     }
// }

// [12-2] 엔터키 검색
// document.querySelector("#workerSearchInput").addEventListener("keydown", function (e) {
//     if (e.key === "Enter") {
//         searchUser(); // 원하는 함수 실행
//     }
// });

// [13] 체크리스트 선택 버튼 (PJC-08번 기능) ====================================================================
// [13-1] 배정하기 클릭시, 배정하기 버튼이 포함된 tr에 target 설정하기
document.querySelector("#pjchecklistTbody").addEventListener("click", function (e) {
    if (e.target.classList.contains("choiceChecklistBtn")) {
        const tr = e.target.closest("tr");
        tr.classList.add("selectedTarget");
    }
});

// // [13-2] 모달 선택 시 정보 삽입 및 TemporarySaveWorker 업데이트
// document.addEventListener("click", function (e) {

//     if (e.target.classList.contains("selectChecklistBtn")) {
//         const userNo = parseInt(e.target.dataset.userno);
//         const userName = e.target.dataset.name;
//         const userPhone = e.target.dataset.phone;
//         const roadAddress = e.target.dataset.address;

//         const targetRow = document.querySelector(".selectedTarget");
//         if (targetRow) {
//             targetRow.querySelector("td:nth-child(3)").textContent = userName;
//             targetRow.querySelector("td:nth-child(3)").dataset.userno = userNo;
//             targetRow.querySelector("td:nth-child(4)").textContent = userPhone;
//             targetRow.querySelector("td:nth-child(5)").textContent = roadAddress;

//             const pjChkItemNo = parseInt(targetRow.dataset.pjChkItemNo);
//             TemporarySaveWorker.forEach((value) => {
//                 if (value.pjRoleNo === pjRoleNo) {
//                     value.userNo = userNo;
//                     value.changeStatus = pjRoleNo > 7000000 ? 3 : 1;
//                 }
//             });

//             targetRow.classList.remove("selectedTarget");
//         }
//     }
// })

// [14] 프로젝트 체크리스트 삭제 버튼 (PJC-05번 기능) ==================================
document.querySelector("#pjchecklistTbody").addEventListener("click", (e) => {
    if (!e.target.classList.contains("deleteBtn")) return;

    const tr = e.target.closest("tr");
    const pjChkItemNo = parseInt(tr.dataset.pjChkItemNo);

    const confirmDelete = confirm("한 번 삭제한 데이터는 복구할 수 없습니다. \n정말로 삭제하시겠습니까?");
    if (!confirmDelete) return;

    // 화면에서 해당 열 제거
    tr.remove();

    // TemporarySaveWorker 업데이트
    TemporarySaveChecklist.forEach((value) => {
        if (value.pjChkItemNo === pjChkItemNo) {
            value.changeStatus = pjChkItemNo > 8000000 ? 4 : 2;
        }
    });
});

// [15] 저장 버튼 =========================
const savePJchecklist = async () => {
    try {
        const r = await fetch("/project/checklist/tem", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(TemporarySaveChecklist)
        });
        const d = await r.json();

        let success = 0;
        let fail = 0;

        d.forEach((value) => {
            TemporarySaveChecklist.forEach((checklist) => {
                if (checklist.pjChkItemNo == value.pjChkItemNo && checklist.changeStatus == 0) {
                    return;
                } else if (checklist.pjChkItemNo == value.pjChkItemNo && checklist.changeStatus > 0) {
                    if (value.pjChkItemNo > 8000000 && value.Result === value.pjChkItemNo) {
                        success++;
                    } else if (value.pjChkItemNo > 7000000 && value.Result < 100) {
                        fail++;
                    } else if (value.pjChkItemNo < 7000000 && value.Result > 7000000) {
                        success++;
                    } else if (value.pjChkItemNo < 7000000 && value.Result < 100) {
                        fail++;
                    }
                }
            })
        });
        // 저장 작업 완료 후 초기화
        TemporarySaveChecklist.length = 0;
        alert(`성공 ${success}건 / 실패 ${fail}건`);
        await readAllpjcheck(); // 전체 조회 다시 실행
    } catch (error) {
        console.log(error)
    }
}

// [16] 다음 버튼 ============================================================
const nextStage = async () => {
    let result = confirm(`[경고] 저장을 하지 않고 다음 페이지로 이동하시면, 변경된 내용은 삭제되며 복구할 수 없습니다. \n계속 진행하시겠습니까?`)
    if (result == false) { return }

    location.href = `/project/checklist.jsp?pjNo=${pjNo}`;
} // func end
