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
    $('#updatertDescription').summernote({
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
    const rtDescription = document.querySelector("#rtDescriptionInput").value

    // [1.2] Fetch
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
    const r = await fetch("/roleTem")
    const d = await r.json()
    console.log(d)

    d.forEach((dto) => {
        html += `<tr>
                    <td>
                        <input class="form-check-input" type="checkbox" value="${dto.rtNo}">
                    </td>
                    <td>${dto.rtNo}</td>
                    <td>${dto.rtName}</td>
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
                </tr>`
    });
    // [2.3] 화면 표시
    roleTemplateTbody.innerHTML = html;
} // func end
getRT()

// [RT-03] 역할템플릿 개별 조회	- 미리보기
// [미리보기] 버튼 클릭 시, 발생하는 모달 내에 템플릿명과 템플릿 설명을 기재
const getIndiRT = async (rtNo) => {
    console.log("rtNo func exe")
    
    // [3.1] 모달 내 표시할 영역 가져오기
    const rtNampePreview = document.querySelector("#rtNampePreview")
    const rtDescriptionPreview = document.querySelector("#rtDescriptionPreview")

    // [3.2] Fetch
    const r = await fetch(`/roleTem/indi?rtNo=${rtNo}`)
    const d = await r.json()

    // [3.3] 화면에 표시
    rtNampePreview.value = `${d.rtName}`
    rtDescriptionPreview.innerHTML = `${d.rtDescription}`
} // func end

// [RT-04] 역할템플릿 수정	updateRT()
// [RT-05]  역할템플릿 삭제(비활성화)	deleteRT()