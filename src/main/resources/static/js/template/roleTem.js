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
    // [1.1] HTML 에서 입력받은 정보 불러오기

    // [1.2] Fetch

    // [1.3] 저장 후 리스트 update

}


// "역할템플릿 
// 전체 조회"	getRT()
// "역할템플릿 
// 개별 조회"	getIndiRT()
// 역할템플릿 수정	updateRT()
// 역할템플릿 삭제(비활성화)	deleteRT()