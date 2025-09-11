console.log('CTI XXOK');

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

// QueryString 전역변수
const params = new URL(location.href).searchParams;
const ctNo = params.get("ctNo")
console.log(ctNo)
const ctInfo = { ctNo, ctName: "", ctDescription: "" }

// [ 상세 체크리스트 템플릿 생성 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#ctiHelpText').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [ 체크리스트템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#updateCtiHelpText').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [*] ctNo에서 ctName 반환하기
const getIndiCT = async () => {
    console.log("getIndiCT func exe")

    // [1.1] 표시할 영역 가져오기
    // 상단 타이틀 옆에 대분류 명을 표시
    const ctNameBox = document.querySelector(".title1 span")
    const ctName01 = document.querySelector(".ctName01");
    const ctDescription01 = document.querySelector(".ctDescription01");
    const ctName02 = document.querySelector(".ctName02");
    const ctDescription02 = document.querySelector(".ctDescription02");
    const ctName03 = document.querySelector(".ctName03");
    const ctDescription03 = document.querySelector(".ctDescription03");

    // 세부 템플릿 미리보기/수정하기 내에 대분류 탬플릿도 함께 표시

    // [1.2] Fetch
    const r = await fetch(`/checktem/indi?ctNo=${ctNo}`)
    const d = await r.json()
    console.log(d)

    // 전역변수에 데이터 저장
    ctInfo.ctName = d.ctName;
    ctInfo.ctDescription = d.ctDescription;

    // [3.3] 화면에 표시
    ctNameBox.innerHTML = `- ${d.ctName}`
    ctName01.value = d.ctName;
    ctDescription01.innerHTML = d.ctDescription;
    ctName02.value = d.ctName;
    ctDescription02.innerHTML = d.ctDescription;
    ctName03.value = d.ctName;
    ctDescription03.innerHTML = d.ctDescription;
}
getIndiCT(); // 초기화

// [1] 상세 체크리스트 템플릿 생성
const createCTI = async () => {
    console.log("createCTItem func exe")
    // [1.1] 입력할 데이터 가져오기
    const ctiTitle = document.querySelector("#ctiTitleInput").value;
    // const ctiHelpText = document.querySelector("#ctiHelpText").value;
    const ctiHelpText = $('#ctiHelpText').summernote('code');

    // [*] 유효성 검사
    if (ctiTitle.length == 0) {
        alert('상세 체크리스트 템플릿명을 입력해주세요.');
        return;
    }

    try {
        // [1.2] Fetch
        const obj = { ctNo, ctiTitle, ctiHelpText }
        console.log(obj)
        const opt = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch("/checkitem", opt)
        const d = await r.json()
        console.log(d)
        // [1.3] 결과
        if (d > 0) {
            alert("템플릿 저장 성공")
            getCTItem()
        } else {
            alert("템플릿 저장 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [2] 상세 체크리스트 템플릿 전체조회
const getCTItem = async () => {
    console.log("getCTItem func exe")

    // [2.1] 정보를 표시할 구역
    const CTITbody = document.querySelector(".CTITbody")
    let index = 1;

    try {
        // [2.2] Fetch
        const r = await fetch(`/checkitem?ctNo=${ctNo}`)
        const d = await r.json()
        console.log(d)

        // [2.3] 결과 처리
        let html = '';
        if (d.length != 0) {
            d.forEach(value => {
                html += `<tr>
                        <td>${index++}</td>
                        <td>${value.ctiTitle}</td>
                        <td>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#reviewCTI" onclick="getIndiCTItem(${value.ctNo} , ${value.ctiNo})">미리보기</button>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#updateCTI" onclick="getIndiCTItem(${value.ctNo} , ${value.ctiNo})">수정하기</button>
                        </td>
                        <td>${value.createDate}</td>
                        <td>${value.updateDate}</td>
                        <td><button type="button" class="btn btn-danger" onclick="deleteCTItem(${value.ctiNo})">삭제</button></td>
                    </tr>`
            });
        } else {
            html += `<tr>
                     <td colspan="6"> ※ 표시할 정보가 없습니다.</td>
                     </tr>`
        }
        CTITbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end
getCTItem()

// [3] 상세 체크리스트 템플릿 개별조회
const getIndiCTItem = async (ctNo, ctiNo) => {
    console.log("getIndiCTItem func exe")

    try {
        // [3.1] Fetch로 정보 가져오기
        const r = await fetch(`/checkitem/indi?ctNo=${ctNo}&ctiNo=${ctiNo}`)
        const d = await r.json()

        // [3.2] 정보를 표시할 구역
        // 미리보기 모달 구역
        const previewCtName = document.querySelector('#reviewCTI .ctName02');
        const previewCtDescription = document.querySelector('#reviewCTI .ctDescription02');
        const previewCtiTitle = document.querySelector("#previewCtiTitle");
        const previewCtiHelpText = document.querySelector("#previewCtiHelpText");

        // 수정하기 모달 구역
        const updateCtName = document.querySelector('#updateCTI .ctName03');
        const updateCtDescription = document.querySelector('#updateCTI .ctDescription03');
        const updateCtiTitle = document.querySelector("#updateCtiTitle")
        const updateCtiHelpText = document.querySelector(".ctiContent .note-editable")

        // [3.3] 화면에 표시
        // 미리보기 모달 데이터 설정
        previewCtName.value = ctInfo.ctName;                     // 대분류 템플릿명 (전역변수 가져오기)
        previewCtDescription.innerHTML = ctInfo.ctDescription;   // 대분류 템플릿 설명 (전역변수 가져오기)
        previewCtiTitle.value = d.ctiTitle;                     // 상세 템플릿 제목
        previewCtiHelpText.innerHTML = d.ctiHelpText;           // 상세 템플릿 도움말

        // 수정하기 모달 데이터 설정
        updateCtName.value = ctInfo.ctName;                     // 대분류 템플릿명 (전역변수 가져오기)
        updateCtDescription.innerHTML = ctInfo.ctDescription;   // 대분류 템플릿 설명 (전역변수 가져오기)
        updateCtiTitle.value = d.ctiTitle;
        $('#updateCtiHelpText').summernote('code', d.ctiHelpText); // Summernote 내용 설정 (최상단 썸머노트로 관리)

        // [3.4] 수정하기 버튼에 ctNo를 매개변수로 삽입해놓기
        const updateBox = document.querySelector(".updateBox")
        const html = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        <button type="button" class="btn btn-primary " onclick="updateCTItem(${ctiNo})"
                        data-bs-dismiss="modal">수정</button>`;
        updateBox.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [4] 상세 체크리스트 템플릿 수정
const updateCTItem = async (ctiNo) => {
    console.log("updateCTItem func exe")

    // [4.1] 정보를 표시할 구역
    const ctiTitle = document.querySelector("#updateCtiTitle").value
    const ctiHelpText = document.querySelector("#updateCtiHelpText").value

    // [*] 유효성 검사
    if (ctiTitle.length == 0) {
        alert('상세 체크리스트 템플릿명을 입력해주세요.');
        return;
    }

    try {
        // [4.2] Fetch
        const obj = { ctiNo, ctiTitle, ctiHelpText }
        const opt = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch(`/checkitem`, opt)
        const d = await r.json()

        // [4.3] 결과 표시 + update
        if (d > 0) {
            alert("템플릿 수정 성공")
            getCTItem()
        } else {
            alert("템플릿 수정 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [5] 상세 체크리스트 템플릿 삭제
const deleteCTItem = async (ctiNo) => {
    console.log("deleteCTItem func exe")

    // [5.1] 삭제 여부 확인
    let result = confirm(`[경고] 삭제한 템플릿은 복구할 수 없습니다. \n정말로 삭제하시겠습니까?`)
    if (result == false) { return }

    try {
        // [5.2] Fetch
        const opt = { method: "DELETE" }
        const r = await fetch(`/checkitem?ctiNo=${ctiNo}`, opt)
        const d = await r.json()

        // [5.3] 결과
        if (d > 0) {
            alert("템플릿 삭제 성공")
            getCTItem()
        } else {
            alert("템플릿 삭제 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end
