console.log('info js exe')

window.onHeaderReady = () => {
    loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
    userDisplay();
};

// [0] 로그인 체크
const loginCheck = async () => {
    console.log("loginCheck func exe")
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    } 
}

// [0] 일반회원일 경우, 네비게이션바 비활성화
const userDisplay = async () => {
    const naviBar = document.querySelector('#projectNav');
    const excelBtn = document.querySelector('#excelBtn')
    if ((!userNo&&!businessNo) ||(userNo && !businessNo)) {
        // 조건 만족 시 숨김 처리
        naviBar.style.display = 'none';
        excelBtn.style.display = 'none';
    } else {
        // 조건 불만족 시 다시 보이게
        naviBar.style.display = '';
        excelBtn.style.display = '';
    }
}

// [1] 카카오 우편번호 검색 / 도로명 주소 검색 
var mapContainer = document.getElementById('mapArea'), // 지도를 표시할 div
    mapOption = {
        center: new daum.maps.LatLng(37.490996, 126.720572), // 지도의 중심좌표
        level: 5 // 지도의 확대 레벨
    };

//지도를 미리 생성
var map = new daum.maps.Map(mapContainer, mapOption);
//주소-좌표 변환 객체를 생성
var geocoder = new daum.maps.services.Geocoder();
//마커를 미리 생성
var marker = new daum.maps.Marker({
    position: new daum.maps.LatLng(37.537187, 127.005476),
    map: map
});

// [2] PJ 정보 조회
const readPJinfo = async () => {
    // [2.1] 표시할 영역 가져오기

    const pjName = document.querySelector("#pjName");
    const pjStartDate = document.querySelector("#pjStartDate");
    const roadAddress = document.querySelector("#roadAddress");
    const detailAddress = document.querySelector("#detailAddress");
    const clientName = document.querySelector("#clientName");
    const clientRepresent = document.querySelector("#clientRepresent");
    const clientPhone = document.querySelector("#clientPhone");

    try {
        // [2.2] fetch
        const r = await fetch(`/project/perform/check?pjNo=${pjNo}`)
        const d = await r.json()
        console.log(d)

        // [2.3] 화면 표시
        pjName.value = d.pjDto.pjName
        pjStartDate.value = `${d.pjDto.pjStartDate} ~ ${d.pjDto.pjEndDate}`
        roadAddress.value = d.pjDto.roadAddress
        detailAddress.value = d.pjDto.detailAddress
        clientName.value = d.pjDto.clientName
        clientRepresent.value = d.pjDto.clientRepresent
        clientPhone.value = d.pjDto.clientPhone

        // [2.4] 카카오맵 마커표시 
        geocoder.addressSearch(d.pjDto.roadAddress, function (results, status) {
            if (status === daum.maps.services.Status.OK) {
                const result = results[0];
                const coords = new daum.maps.LatLng(result.y, result.x);
                map.setCenter(coords);
                marker.setPosition(coords);
            }
        });

    } catch (error) {
        console.log(error)
    }
} // func end
readPJinfo()

// [03] excle 다운로드
const downloadExcel = async () => {
    window.location.href = `/excel/download?pjNo=${pjNo}`;
}

// [04] 프로젝트 근무리스트 전체 조회
const readAllPJworker = async () => {

    console.log("readAllPJworker() 1 ")

    // 마크 다운
    const pjWorkerTbody = document.querySelector('#pjWorkerTbody');
    let html = ``;

    // fetch
    try {
        console.log("readAllPJworker() 2 ")
        const response = await fetch(`/project/perform/check/list?pjNo=${pjNo}`)
        const data = await response.json();

        console.log(data)

        if (data.length > 0) {
            for (let i = 0; i < data.length; i++) {

                const d = data[i]
                console.log(d)

                if ( d.pjPerDto.pfStatus == 3){
                    d.pjPerDto.pfStatus = '완료됨';
                } else if ( d.pjPerDto.pfStatus == 2 ){
                    d.pjPerDto.pfStatus = '진행중'; 
                } else if ( d.pjPerDto.pfStatus == 1 ){
                    d.pjPerDto.pfStatus = '시작전';
                } else if ( d.pjPerDto.pfStatus == 4 ){
                    d.pjPerDto.pfStatus = '취소됨';
                } else if (d.pjPerDto.pfStatus == 5 ){
                    d.pjPerDto.pfStatus = '보류중'
                }

                html += `<tr>
                            <td>${i + 1}</td>
                            <td>${d.usersDto.userName}</td>
                            <td>${d.pjWorkerDto.pjRoleName}</td>
                            <td>${d.pjCheckDto.pjChklTitle}</td>
                            <td>${d.pjPerDto.pfStart}</td>
                            <td>${d.pjPerDto.pfEnd}</td>
                            <td>${d.pjPerDto.pfStatus}</td>
                            <td><button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                                data-bs-target="#detailPerform" onclick="readPJworker(${d.pjPerDto.pfNo})"">상세보기</button></td>
                        </tr>
                    `;
            } // for end

        } else {
            html += `<tr>
                        <td colspan="8"> ※ 표시할 정보가 없습니다.</td>
                    </tr>`
        }

        // 화면 표시
        pjWorkerTbody.innerHTML = html;

    } catch (error) {
        console.log(error)
    }

}
readAllPJworker(); // 초기화

// [05] 프로젝트 근무리스트 개별 조회(모달)
const readPJworker = async ( pfNo ) => {

    // 마크다운
    const userName = document.querySelector('#userName');
    const pjRoleName = document.querySelector('#pjRoleName');
    const pjChklTitle = document.querySelector('#pjCheckList');
    const pfStart = document.querySelector('#pfStart');
    const pfEnd = document.querySelector('#pfEnd');
    const pfStatus = document.querySelector('.pjPerformStatus');
    const note = document.querySelector('#memo');

    // fetch
    try {
        const response = await fetch(`/project/perform/check/indi?pjNo=${pjNo}&pfNo=${pfNo}`)
        const data = await response.json();

        console.log(data)

        // 화면 표시
        userName.value = data.usersDto.userName;
        pjRoleName.value = data.pjWorkerDto.pjRoleName;
        pjChklTitle.value = data.pjCheckDto.pjChklTitle;
        pfStart.value = data.pjPerDto.pfStart;
        pfEnd.value = data.pjPerDto.pfEnd;
        pfStatus.value = data.pjPerDto.pfStatus;
        note.value = data.pjPerDto.note;

    } catch (error) {
        console.log(error)
    }
}


// [06] 파일 업로드

// [07] 파일 삭제

// [08] 근무 정보 메모 수정
