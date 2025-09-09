console.log('info js exe')

window.onHeaderReady = () => {
    loginCheck(); // header.js의 userNo, businessNo가 설정된 후 실행됨
};

// [0] 로그인 체크
const loginCheck = async () => {
    console.log("loginCheck func exe")
    if (userNo == null || userNo === 0) {
        alert("[경고] 로그인 후 이용가능합니다.")
        location.href = "/index.jsp"
    } else if (businessNo == null || businessNo === 0) {
        alert("[경고] 일반회원은 사용불가능한 메뉴입니다.")
        location.href = "/index.jsp"
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
        const r = await fetch(`/project/info/indi?pjNo=${pjNo}`)
        const d = await r.json()
        console.log(d)

        // [2.3] 화면 표시
        pjName.value = d.pjName
        pjStartDate.value = `${d.pjStartDate} ~ ${d.pjEndDate}`
        roadAddress.value = d.roadAddress
        detailAddress.value = d.detailAddress
        clientName.value = d.clientName
        clientRepresent.value = d.clientRepresent
        clientPhone.value = d.clientPhone

        // [2.4] 카카오맵 마커표시 
        geocoder.addressSearch(d.roadAddress, function (results, status) {
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