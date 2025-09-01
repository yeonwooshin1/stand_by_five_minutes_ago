console.log("roleTemItem js func")

// QueryString
const params = new URL(location.href).searchParams;
const rtNo = params.get("rtNo")
console.log(rtNo)

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

// [1] readRT()
// rtNo > rtName으로 변환하기 



// [상세 역할 템플릿 관련 func] =====================================
