console.log('info js exe')

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
    const createDate = document.querySelector("#createDate");
    const updateDate = document.querySelector("#updateDate");
    const pjstartDate = document.querySelector("#pjstartDate");
    const pjendDate = document.querySelector("#pjendDate");
    const roadAddress = document.querySelector("#roadAddress");
    const detailAddress = document.querySelector("#detailAddress");
    const pjMemo = document.querySelector("#pjMemo");
    const clientName = document.querySelector("#clientName");
    const clientRepresent = document.querySelector("#clientRepresent");
    const clientPhone = document.querySelector("#clientPhone");
    const clientMemo = document.querySelector("#clientMemo");

    try {
        // [2.2] fetch
        const r = await fetch(`/project/info/indi?pjNo=${pjNo}`)
        const d = await r.json()
        console.log(d)

        // [2.3] 화면 표시
        pjName.value = d.pjName
        createDate.innerHTML = d.createDate
        updateDate.innerHTML = d.updateDate
        pjstartDate.value = d.pjStartDate
        pjendDate.value = d.pjEndDate
        roadAddress.value = d.roadAddress
        detailAddress.value = d.detailAddress
        pjMemo.value = d.pjMemo
        clientName.value = d.clientName
        clientRepresent.value = d.clientRepresent
        clientPhone.value = d.clientPhone
        clientMemo.value = d.clientMemo

        // [2.4] 카카오맵 마커표시 
        geocoder.addressSearch(d.roadAddress, function (results, status) {
            console.log(d.roadAddress)
            console.log(results)
            console.log(status)
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


// [3] 우편번호/도로명 주소 반환 func
function Postcode() {
    new daum.Postcode({
        oncomplete: function (data) {
            var addr = data.address; // 최종 주소 변수

            // 주소 정보를 해당 필드에 넣는다.
            document.getElementById("roadAddress").value = addr;
            // 주소로 상세 정보를 검색
            geocoder.addressSearch(data.address, function (results, status) {
                // 정상적으로 검색이 완료됐으면
                if (status === daum.maps.services.Status.OK) {
                    var result = results[0]; //첫번째 결과의 값을 활용
                    // 해당 주소에 대한 좌표를 받아서
                    var coords = new daum.maps.LatLng(result.y, result.x);
                    // 지도를 보여준다.
                    mapContainer.style.display = "block";
                    map.relayout();
                    // 지도 중심을 변경한다.
                    map.setCenter(coords);
                    // 마커를 결과값으로 받은 위치로 옮긴다.
                    marker.setPosition(coords)
                }
            });
        }
    }).open();
}

