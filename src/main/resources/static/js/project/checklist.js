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
// async function loginCheck() {
//     if (!userNo) {
//         alert("[경고] 로그인 후 이용 가능합니다.");
//         location.href = "/index.jsp";
//     } else if (!businessNo) {
//         alert("[경고] 기업 회원만 사용 가능한 메뉴입니다.");
//         location.href = "/index.jsp";
//     }
// }

// [00] 로그인 체크 - 수정된 버전
async function loginCheck() {
    console.log("loginCheck 실행됨"); // 디버깅용
    
    // 세션 정보를 서버에서 확인하는 방식으로 변경
    try {
        const response = await fetch('/project/checklist/session-check');
        const sessionData = await response.json();
        
        if (!sessionData.loggedIn) {
            alert("[경고] 로그인 후 이용 가능합니다.");
            location.href = "/index.jsp";
            return false;
        }
        
        if (!sessionData.isBusiness) {
            alert("[경고] 기업 회원만 사용 가능한 메뉴입니다.");
            location.href = "/index.jsp";
            return false;
        }
        
        return true;
    } catch (error) {
        console.error("세션 확인 오류:", error);
        // 기존 방식으로 폴백
        if (typeof userNo === 'undefined' || !userNo) {
            alert("[경고] 로그인 후 이용 가능합니다.");
            location.href = "/index.jsp";
            return false;
        }
        if (typeof businessNo === 'undefined' || !businessNo) {
            alert("[경고] 기업 회원만 사용 가능한 메뉴입니다.");
            location.href = "/index.jsp";
            return false;
        }
        return true;
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
            data.forEach((dto, index) => {
                html += `
                    <tr data-pjchkitemno="${dto.pjChkItemNo}">
                        <td> ${index} </td>
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


// [04] 행 추가 버튼 (자유 입력)
function addClearRow() {
    const newRow = document.createElement("tr");
    const tempId = tempCheckNoCounter--;
    newRow.dataset.pjchkitemno = tempId;

    newRow.innerHTML = `
        <td> ${TemporarySaveChecklist.length + 1} </td>
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

// [05] 체크리스트 템플릿 모달 불러오기
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
            try {
                const response = await fetch(`/project/checklist/tem?ctNo=${ctNo}`);
                const dto = await response.json();
                if (dto && dto.ctName) {
                    optionsHtml += `<option value="${dto.ctNo}" data-ctname="${dto.ctName}" data-ctdescription="${dto.ctDescription || ''}">${dto.ctName}</option>`;
                }
            } catch (error) {
                console.warn(`템플릿 ${ctNo} 로딩 실패 :`, error)
            }
        }
        modalCheckTemplate.innerHTML += optionsHtml;

    } catch (error) {
        console.error("Error fetching template categories:", error);
    }
});

// [06] 대분류 선택 시 소분류 불러오기
document.querySelector(".modalCheckTemplate").addEventListener("change", async function () {
    const ctNo = this.value;
    const modalCheckTemTbody = document.querySelector("#modalCheckTemTbody");

    if (ctNo === "0") {
        modalCheckTemTbody.innerHTML = '<tr><td colspan="3">대분류를 선택하세요.</td></tr>';
        return;
    }

    // option data 저장
    const selectedOption = this.options[this.selectedIndex];
    currentCtName = selectedOption.dataset.ctname || '';
    currentCtDescription = selectedOption.dataset.ctDescription || '';

    try {
        const response = await fetch(`/project/checklist/item?ctNo=${ctNo}`);
        const data = await response.json();
        let html = '';

        if (data.length > 0 && data[0].status != "NOT_LOGGED_IN") {
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
        modalCheckTemTbody.innerHTML = `<tr><td colspan="3"> 데이터 로딩 중 오류가 발생했습니다.</td></tr>`;
    }
});


// [07] 설명 보기
function viewDescription(pjChkItemNo) {
    const item = TemporarySaveChecklist.find(d => d.pjChkItemNo == pjChkItemNo);
    if (item) {
        $('#descriptionArea').summernote('code', item.pjHelpText || '');
    }
    // 저장 버튼에 현재 항목의 ID를 데이터로 설정
    document.querySelector("#saveDescBtn").dataset.pjchkitemno = pjChkItemNo;

    const viewModal = new bootstrap.Modal(document.getElementById('viewModal'));
    viewModal.show();
}

// [03] 템플릿 선택 시 행 추가
document.addEventListener("click", async function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        const ctiNo = e.target.dataset.ctino;

        // 템플릿 데이터 불러오기 및 저장
        try {
            const response = await fetch(`/project/checklist/tem`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ ctiNo: parseInt(ctiNo), pjNo: parseInt(pjNo) })
            });

            if(!response.ok){
                throw new Error(`HTTP error! status : ${response.status}`);
            }

            const newPjChkItemNo = await response.json();

            if (newPjChkItemNo > 0) {
                alert('템플릿이 추가되었습니다. 저장 버튼을 눌러야 최종 반영됩니다.');
                await readAllpjcheck(); // 목록 새로고침
            } else {
                alert('템플릿 불러오기에 실패했습니다.');
            }
        } catch (error) {
            console.error('템플릿 추가 오류:' , error);
            alert('템플릿 추가 중 오류가 발생했습니다.')
        }
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('checkTemplateModal'));
        modal.hide();
    }
});



// [08] 설명 저장
document.querySelector("#saveDescBtn").addEventListener("click", function () {
    const pjChkItemNo = parseInt(this.dataset.pjchkitemno);
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
        const newTitle = e.target.textContent.trim();

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
    // 번호 다시 쓰기
    const rows = document.querySelectorAll('#pjchecklistTbody tr');
    rows.forEach((row ,index) => {
        row.querySelector("td:first-child").textContent = index + 1;
    });
});



// [11] 저장 버튼
async function savePJchecklist() {
    try {
        // 변경 사항 있는 항목 전송
        const changedItems = TemporarySaveChecklist.filter(item => item.changeStatus != 0);

        if (changedItems.length == 0){
            alert("변경된 내용이 없습니다.");
            return;
        }

        const response = await fetch("/project/checklist/save", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(changedItems)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const result = await response.json();

        let successCount = 0;
        let failCount = 0;

        result.forEach(res => {
            if (res.Result && res.Result > 0) {
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