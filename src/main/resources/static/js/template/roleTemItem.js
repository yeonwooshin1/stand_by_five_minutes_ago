console.log("roleTemItem js func")

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

// QueryString
const params = new URL(location.href).searchParams;
const rtNo = params.get("rtNo")
console.log(rtNo)
const rtInfo = { rtNo: rtNo, rtDescription: "" }

// [ 상세 역할 템플릿 생성 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#rtiDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#updateRtiDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});


// [역할 템플릿 관련 func] =====================================

// [1] getIndiRT()
// rtNo > rtName으로 변환하기 
const getIndiRT = async () => {
    console.log("getIndiRT func exe")

    // [1.1] 표시할 영역 가져오기
    // 상단 타이틀 옆에 대분류 명을 표시
    const rtNameBox = document.querySelector(".titleBox span")
    const rtName01 = document.querySelector(".rtName01");
    const rtDescription01 = document.querySelector(".rtDescription01");
    const rtName02 = document.querySelector(".rtName02");
    const rtDescription02 = document.querySelector(".rtDescription02");
    const rtName03 = document.querySelector(".rtName03");
    const rtDescription03 = document.querySelector(".rtDescription03");

    // 세부 템플릿 미리보기/수정하기 내에 대분류 탬플릿도 함께 표시

    // [1.2] Fetch
    const r = await fetch(`/roleTem/indi?rtNo=${rtNo}`)
    const d = await r.json()
    console.log(d)

    // [3.3] 화면에 표시
    rtNameBox.innerHTML = `- ${d.rtName}`
    rtName01.value = d.rtName;
    rtDescription01.innerHTML = d.rtDescription;
    rtName02.value = d.rtName;
    rtDescription02.innerHTML = d.rtDescription;
    rtName03.value = d.rtName;
    rtDescription03.innerHTML = d.rtDescription;
} // func end
getIndiRT()

// [상세 역할 템플릿 관련 func] =====================================

// [RTI-01] 상세역할템플릿 생성 
const createRTI = async () => {
    console.log("createRTItem func exe")
    // [1.1] 입력할 데이터 가져오기
    const rtiName = document.querySelector("#rtiNameInput").value;
    const rtiDescription = document.querySelector("#rtiDescription").value;

    // [*] 유효성 검사
    if (rtiName.trim().length == 0) {
        alert('상세 역할 템플릿명을 입력해주세요.');
        return;
    }

    try {
        // [1.2] Fetch
        const obj = { rtNo, rtiName, rtiDescription }
        console.log(obj)
        const opt = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch("/roleTem/Item", opt)
        const d = await r.json()
        console.log(d)
        // [1.3] 결과
        if (d > 0) {
            alert("템플릿 저장 성공")
            getRTItem()
        } else {
            alert("템플릿 저장 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [RTI-02] 상세역할템플릿 전체 조회 getRTItem()
const getRTItem = async () => {
    console.log("getRTItem func exe")

    // [2.1] 정보를 표시할 구역
    const RTITbody = document.querySelector(".RTITbody")

    try {
        // [2.2] Fetch
        const r = await fetch(`/roleTem/Item?rtNo=${rtNo}`)
        const d = await r.json()
        console.log(d)


        // [2.3] 결과 처리
        let html = '';
        let i = 1;
        if (d.length != 0) {
            d.forEach(value => {
                html += `<tr data-rtiNo="${value.rtiNo}">
                        <td>${i}</td>
                        <td>${value.rtiName}</td>
                        <td>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#reviewRTI" onclick="getIndiRTItem(${value.rtiNo})">미리보기</button>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#updateRTI" onclick="getIndiRTItem(${value.rtiNo})">수정하기</button>
                        </td>
                        <td>${value.createDate}</td>
                        <td>${value.updateDate}</td>
                        <td><button type="button" class="btn btn-danger" onclick="deleteRTItem(${value.rtiNo})">삭제</button></td>
                    </tr>`
                i++
            });
        } else {
            html += `<tr>
                     <td colspan="6"> ※ 표시할 정보가 없습니다.</td>
                     </tr>`
        }
        RTITbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end
getRTItem()

// [RTI-03] 역할템플릿 개별 조회 getIndiRTItem()

const getIndiRTItem = async (rtiNo) => {
    console.log("getIndiRTItem func exe")

    // [3.1] 정보를 표시할 구역
    // 미리보기 모달 구역
    const previewRtiName = document.querySelector("#previewRtiName")
    const previewRtiDescription = document.querySelector("#previewRtiDescription")

    // 수정하기 모달 구역
    const updateRtiName = document.querySelector("#updateRtiName")
    const updateRtiDescription = document.querySelector(".rtiContent .note-editable")

    try {
        // [3.2] Fetch
        const r = await fetch(`/roleTem/Item/indi?rtiNo=${rtiNo}`)
        const d = await r.json()

        // [3.3] 화면에 표시
        previewRtiName.value = d.rtiName
        previewRtiDescription.innerHTML = d.rtiDescription
        updateRtiName.value = d.rtiName
        updateRtiDescription.innerHTML = d.rtiDescription

        // [3.4] 수정하기 버튼에 rtNo를 매개변수로 삽입해놓기
        const updateBox = document.querySelector(".updateBox")
        const html = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        <button type="button" class="btn btn-primary " onclick="updateRTItem(${rtiNo})"
                        data-bs-dismiss="modal">수정</button>`;
        updateBox.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [RTI-04] 역할템플릿 수정	updateRTItem()

const updateRTItem = async (rtiNo) => {
    console.log("updateRTItem func exe")

    // [4.1] 정보를 표시할 구역
    const rtiName = document.querySelector("#updateRtiName").value
    const rtiDescription = document.querySelector("#updateRtiDescription").value

    // [*] 유효성 검사
    if (rtiName.trim().length == 0) {
        alert('상세 역할 템플릿명을 입력해주세요.');
        return;
    }


    try {
        // [4.2] Fetch
        const obj = { rtiNo, rtiName, rtiDescription }
        const opt = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch(`/roleTem/Item`, opt)
        const d = await r.json()

        // [4.3] 결과 표시 + update
        if (d > 0) {
            alert("템플릿 수정 성공")
            getRTItem()
        } else {
            alert("템플릿 수정 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [RTI-05] 역할템플릿 삭제(비활성화) deleteRTItem()
const deleteRTItem = async (rtiNo) => {
    console.log("deleteRTItem func exe")

    // [5.1] 삭제 여부 확인
    let result = confirm(`[경고] 삭제한 템플릿은 복구할 수 없습니다. \n정말로 삭제하시겠습니까?`)
    if (result == false) { return }

    try {
        // [5.2] Fetch
        const opt = { method: "DELETE" }
        const r = await fetch(`/roleTem/Item?rtiNo=${rtiNo}`, opt)
        const d = await r.json()

        // [5.3] 결과
        if (d > 0) {
            alert("템플릿 삭제 성공")
            getRTItem()
        } else {
            alert("템플릿 삭제 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end
