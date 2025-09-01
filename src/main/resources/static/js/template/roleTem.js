console.log("roleTem js exe")

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#creatertDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight : 300
    });
});

// [ 역할템플릿 만들기 모달 내 Summer Note 연동 ]
$(document).ready(function () {
    $('#updatertDescription').summernote({
        lang: 'ko-KR', // default: 'en-US'
        // 부가 기능
        minHeight : 300
    });
});

// [RT-01] 역할템플릿 생성
const createRT = async () => {
    console.log("createRT func exe")

    // [1.1] HTML 에서 입력받은 정보 불러오기
    const rtName = document.querySelector("#rtNameInput").value;
    const rtDescription = document.querySelector("#creatertDescription").value
    console.log(rtName)
    console.log(rtDescription)

    // [1.2] Fetch
    const obj = { rtName, rtDescription}
    const opt = { method : "POST",
        headers : {"Content-Type":"application/json"},
        body : JSON.stringify(obj)
    }
    const r = await fetch("/roleTem", opt)
    const d = await r.json()

    if(d > 0 ) {
        alert("템플릿 저장 성공")
    } else {
        alert("템플릿 저장 실패")
    }
    
    // [1.3] 저장 후 리스트 update
    

}


// "역할템플릿 
// 전체 조회"	getRT()
// "역할템플릿 
// 개별 조회"	getIndiRT()
// 역할템플릿 수정	updateRT()
// 역할템플릿 삭제(비활성화)	deleteRT()