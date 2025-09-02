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
const createCTem = async () => {

}

// [2] 체크리스트 전체 조회

// [3] 체크리스트 개별 조회

// [4] 체크리스트 수정

// [5] 체크리스트 삭제