console.log('checkTem XXOK');

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#creatertDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#ctDescriptionUpdate').summernote({ // ctDescriptionUpdate로 변경
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});


// [1] 체크리스트 생성
const createCT = async () => {
    console.log("createCT func exe")

    // [1.1] HTML 에서 입력받은 정보 불러오기
    const ctName = document.querySelector("#ctNameInput").value;
    const ctDescription = document.querySelector("#creatertDescription").value

    // [1.2] Fetch
    try {
        const obj = { ctName, ctDescription }
        const opt = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch("/checktem", opt)
        const d = await r.json()

        if (d > 0) {
            alert("템플릿 저장 성공")
        } else {
            alert("템플릿 저장 실패")
        }
    } catch (error) {
        console.log(error)
    }
    // [1.3] 저장 후 리스트 조뢰
    getRT()
} // func end

// [2] 체크리스트 전체 조회
const getCT = async () => {
    console.log("getCT func exe")
    // [2.1] HTML에 표시할 영역 정보
    const checkTemplateTbody = document.querySelector(".checkTemplateTbody")
    let html = '';

    // [2.2] Fetch
    try {
        const r = await fetch("/checktem")
        const d = await r.json()
        console.log(d)

        d.forEach((dto) => {
            html += `<tr>
                    <td>${dto.ctNo}</td>
                    <td><a href="/template/checkTemItem.jsp?rtNo=${dto.ctNo}">${dto.ctName}</a></td>
                    <td>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#reviewCheckTem"
                            onclick="getIndiCT(${dto.ctNo})">미리보기</button>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#updateCheckTem" 
                            onclick="getIndiCT(${dto.ctNo})">수정하기</button>
                    </td>
                    <td>${dto.createDate}</td>
                    <td>${dto.updateDate}</td>
                    <td><button type="button" class="btn btn-danger" onclick="deleteCT(${dto.rtNo})">삭제</button></td>
                </tr>`
        });

        // [2.3] 화면 표시
        checkTemplateTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end
getCT(); // 접속, 새로고침 초기화

// [3] 체크리스트 개별 조회
const getIndiCT = async (ctNo) => {
    console.log("getIndiCT func exe")

    // [3.1] 모달 내 표시할 영역 가져오기
    // 미리보기 모달 구역
    const ctNamePreview = document.querySelector("#ctNamePreview")
    const ctDescriptionPreview = document.querySelector("#ctDescriptionPreview")

    // 수정하기 모달 구역
    const ctNameUpdate = document.querySelector("#ctNameUpdate")
    const ctDescriptionUpdate = document.querySelector(".updateCTContent .note-editable")

    // [3.2] Fetch
    try {
        const r = await fetch(`/checktem/indi?ctNo=${ctNo}`)
        const d = await r.json()

        // [3.3] 화면에 표시
        ctNamePreview.value = d.ctName
        ctDescriptionPreview.innerHTML = d.ctDescription
        ctNameUpdate.value = d.ctName
        ctDescriptionUpdate.innerHTML = d.ctDescription

        // [3.4] 수정하기 버튼에 rtNo를 매개변수로 삽입해놓기
        const updateBox = document.querySelector(".updateBox")
        const html = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
    <button type="button" class="btn btn-primary " onclick="updateCT(${ctNo})"
                        data-bs-dismiss="modal">수정</button>`;
        updateBox.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [4] 체크리스트 수정
const updateCT = async (ctNo) => {
    // [4.1] 수정할 정보 가져오기
    const ctName = document.querySelector("#ctNameUpdate").value
    const ctDescription = document.querySelector("#ctDescriptionUpdate").value


    // [4.2] fetch
    try {
        const obj = { ctNo, ctName, ctDescription }
        const opt = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch(`/checktem`, opt)
        const d = await r.json()

        // [4.3] 결과 표시 + update
        if (d > 0) {
            alert("템플릿 저장 성공")
            getCT()
        } else {
            alert("템플릿 저장 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [5] 체크리스트 삭제
const deleteCT = async (ctNo) => {
    console.log("deleteCT func exe")
    console.log(ctNo)

    try {
        // [5.1] 확인 여부 확인
        let result = confirm(`[경고] 삭제한 템플릿은 복구할 수 없습니다. <br/> 정말로 삭제하시겠습니까?`)
        if (result == false) { return }

        // [5.2] Fetch
        const opt = { method: "DELETE" }
        const r = await fetch(`/checktem?ctNo=${ctNo}`, opt)
        const d = await r.json()

        if (d > 0) {
            alert("템플릿 삭제 성공")
            getCT()
        } else {
            alert("템플릿 삭제 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end