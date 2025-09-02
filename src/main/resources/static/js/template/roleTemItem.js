console.log("roleTemItem js func")

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
            getRT()
        } else {
            alert("템플릿 저장 실패")
        }
        // [1.4] getRTItem() func exe
        getRTItem()
    } catch (error) {
        console.log(error)
    }
} // func end

// [RTI-02] 상세역할템플릿 전체 조회 getRTItem()
const getRTItem = async () => {
    console.log("getRTItem func exe")

}

// [RTI-03] 역할템플릿 개별 조회 getIndiRTItem()

// [RTI-04] 역할템플릿 수정	updateRTItem()

// [RTI-05] 역할템플릿 삭제(비활성화) deleteRTItem()