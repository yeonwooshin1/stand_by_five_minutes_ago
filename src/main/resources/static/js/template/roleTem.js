console.log("roleTem js exe")

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
    $('#rtDescriptionUpdate').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [RT-01] 역할템플릿 생성
const createRT = async () => {
    console.log("createRT func exe")

    // [1.1] HTML 에서 입력받은 정보 불러오기
    const rtName = document.querySelector("#rtNameInput").value;
    const rtDescription = document.querySelector("#creatertDescription").value

    // [1.2] Fetch
    try {
        const obj = { rtName, rtDescription }
        const opt = {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch("/roleTem", opt)
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

// [RT-02] 역할템플릿 전체조회
const getRT = async () => {
    console.log("getRT func exe")
    // [2.1] HTML에 표시할 영역 정보
    const roleTemplateTbody = document.querySelector(".roleTemplateTbody")
    let html = '';

    // [2.2] Fetch
    try {
        const r = await fetch("/roleTem")
        const d = await r.json()
        console.log(d)

        d.forEach((dto) => {
            html += `<tr>
                    <td>${dto.rtNo}</td>
                    <td><a href="/template/roleTemItem.jsp?rtNo=${dto.rtNo}">${dto.rtName}</a></td>
                    <td>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#reviewRoleTem"
                            onclick="getIndiRT(${dto.rtNo})">미리보기</button>
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#updateRoleTem" 
                            onclick="getIndiRT(${dto.rtNo})">수정하기</button>
                    </td>
                    <td>${dto.createDate}</td>
                    <td>${dto.updateDate}</td>
                    <td><button type="button" class="btn btn-danger" onclick="deleteRT(${dto.rtNo})">삭제</button></td>
                </tr>`
        });

        // [2.3] 화면 표시
        roleTemplateTbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }

} // func end
getRT()

// [RT-03] 역할템플릿 개별 조회
// [미리보기]/[수정하기] 버튼 클릭 시, 발생하는 모달 내에 템플릿명과 템플릿 설명을 기재
const getIndiRT = async (rtNo) => {
    console.log("getIndiRT func exe")

    // [3.1] 모달 내 표시할 영역 가져오기
    // 미리보기 모달 구역
    const rtNampePreview = document.querySelector("#rtNampePreview")
    const rtDescriptionPreview = document.querySelector("#rtDescriptionPreview")

    // 수정하기 모달 구역
    const rtNampeUpdate = document.querySelector("#rtNampeUpdate")
    const rtDescriptionUpdate = document.querySelector(".updateRTContent .note-editable")

    // [3.2] Fetch
    try {
        const r = await fetch(`/roleTem/indi?rtNo=${rtNo}`)
        const d = await r.json()

        // [3.3] 화면에 표시
        rtNampePreview.value = d.rtName
        rtDescriptionPreview.innerHTML = d.rtDescription
        rtNampeUpdate.value = d.rtName
        rtDescriptionUpdate.innerHTML = d.rtDescription

        // [3.4] 수정하기 버튼에 rtNo를 매개변수로 삽입해놓기
        const updateBox = document.querySelector(".updateBox")
        const html = `<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
    <button type="button" class="btn btn-primary " onclick="updateRT(${rtNo})"
                        data-bs-dismiss="modal">수정</button>`;
        updateBox.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end

// [RT-04] 역할템플릿 수정	updateRT()
const updateRT = async (rtNo) => {
    // [4.1] 수정할 정보 가져오기
    const rtName = document.querySelector("#rtNampeUpdate").value
    const rtDescription = document.querySelector("#rtDescriptionUpdate").value


    // [4.2] fetch
    try {
        const obj = { rtNo, rtName, rtDescription }
        const opt = {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        }
        const r = await fetch(`/roleTem`, opt)
        const d = await r.json()

        // [4.3] 결과 표시 + update
        if (d > 0) {
            alert("템플릿 저장 성공")
            getRT()
        } else {
            alert("템플릿 저장 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [RT-05]  역할템플릿 삭제(비활성화)	
const deleteRT = async (rtNo) => {
    console.log("deleteRT func exe")
    console.log(rtNo)

    try {
        // [5.1] 확인 여부 확인
        let result = confirm(`[경고] 삭제한 템플릿은 복구할 수 없습니다. <br/> 정말로 삭제하시겠습니까?`)
        if (result == false) { return }

        // [5.2] Fetch
        const opt = { method: "DELETE" }
        const r = await fetch(`/roleTem?rtNo=${rtNo}`, opt)
        const d = await r.json()

        if (d > 0) {
            alert("템플릿 삭제 성공")
            getRT()
        } else {
            alert("템플릿 삭제 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end