console.log('CTI XXOK');

// QueryString 전역변수
const params = new URL(location.href).searchParams;
const ctNo = params.get("ctNo")
console.log(ctNo)
const ctInfo = { ctNo, ctDescription: "" }

// [ 상세 체크리스트 템플릿 생성 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#ctiDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight: 300
    });
});

// [ 체크리스트템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#updateCtiDescription').summernote({
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
    const ctNameBox = document.querySelector(".titleBox span")
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
    const ctiName = document.querySelector("#ctiNameInput").value;
    const ctiDescription = document.querySelector("#ctiDescription").value;

    try {
        // [1.2] Fetch
        const obj = { ctNo, ctiName, ctiDescription }
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
            getCT()
        } else {
            alert("템플릿 저장 실패")
        }
        // [1.4] getRTItem() func exe
        getCTItem()
    } catch (error) {
        console.log(error)
    }
} // func end

// [2] 상세 체크리스트 템플릿 전체조회
const getCTItem = async () => {
    console.log("getCTItem func exe")

    // [2.1] 정보를 표시할 구역
    const CTITbody = document.querySelector(".CTITbody")

    try {
        // [2.2] Fetch
        const r = await fetch(`/checkitem?ctNo=${ctNo}`)
        const d = await r.json()
        console.log(d)

        // [2.3] 결과 처리
        let html = '';
        d.forEach(value => {
            html += `<tr>
                        <td>${value.ctiNo}</td>
                        <td>${value.ctiName}</td>
                        <td>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#reviewCTI">미리보기</button>
                            <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                data-bs-target="#updateCTI">수정하기</button>
                        </td>
                        <td>${value.createDate}</td>
                        <td>${value.updateDate}</td>
                        <td><button type="button" class="btn btn-danger" onclick="">삭제</button></td>
                    </tr>`
        });
        CTITbody.innerHTML = html;
    } catch (error) {
        console.log(error)
    }
} // func end
getRTItem()

// [3] 상세 체크리스트 템플릿 개별조회

// [4] 상세 체크리스트 템플릿 수정

// [5] 상세 체크리스트 템플릿 삭제