console.log("project create js exe")

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

// [1] PJ 정보 생성하기
const creatPJInfo = async () =>{
    console.log("createPJinfo")

    // [1.1] PJ 정보를 가진 form 가져오기
    const PJinfoForm = document.querySelector('#pjForm')

    // [1.2] FormData를 multipart form으로 변환
    const pjFormData = new FormData(PJinfoForm);

    try{
        const opt = {method : "POST", body : pjFormData };
        const r = await fetch("/project/info",opt)
        const d = await r.json()
        console.log(d)

        if(d> 0 ){
            alert("프로젝트 등록 성공")
            location.href="/project/list.jsp"
        } else {
            alert("프로젝트 등록 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end