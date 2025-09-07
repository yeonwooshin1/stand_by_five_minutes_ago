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
    const createDate = document.querySelector("#createDate");
    const updateDate = document.querySelector("#updateDate");
    const pjStartDate = document.querySelector("#pjStartDate");
    const pjEndDate = document.querySelector("#pjEndDate");
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
        pjStartDate.value = d.pjStartDate
        pjEndDate.value = d.pjEndDate
        roadAddress.value = d.roadAddress
        detailAddress.value = d.detailAddress
        pjMemo.value = d.pjMemo
        clientName.value = d.clientName
        clientRepresent.value = d.clientRepresent
        clientPhone.value = d.clientPhone
        clientMemo.value = d.clientMemo

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
} // func end

// [4] 저장 - 수정
const updatePJInfo = async () => {
    console.log("updatePJInfo func exe")

    // [4.1] PJ 정보를 가진 form 가져오기
    const PJinfoForm = document.querySelector('#pjForm')
    const createDate = document.querySelector("#createDate").innerHTML

    // [4.2] FormData를 multipart form으로 변환
    const pjFormData = new FormData(PJinfoForm);
    pjFormData.append("pjNo", pjNo)
    pjFormData.append("createDate", createDate)
    pjFormData.append("pjStatus", 1)

    try {
        const opt = { method: "PUT", body: pjFormData };
        const r = await fetch(`/project/info`, opt)
        const d = await r.json()
        console.log(d)

        if (d > 0) {
            alert("프로젝트 수정 성공")
            readPJinfo()
        } else {
            alert("프로젝트 수정 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

// [5] 삭제 - 비활성화
const deletePJInfo = async () => {
    // [5.1] 사용자 확인

    let result = confirm(`[경고] 삭제한 템플릿은 복구할 수 없습니다. \n정말로 삭제하시겠습니까?`)
    if (result == false) { return }

    try {
        // [5.2] fetch
        const r = await fetch(`/project/info?pjNo=${pjNo}`,{ method: "DELETE" })
        const d = await r.json()
        console.log(d)
        if (d > 0) {
            alert("프로젝트 삭제 성공")
            location.href = "/project/list.jsp"
        } else {
            alert("프로젝트 삭제 실패")
        }
    } catch (error) {
        console.log(error)
    }
} // func end

const nextStage = async () => {
    let result = confirm(`[경고] 저장을 하지 않고 다음 페이지로 이동하시면, 변경된 내용은 삭제되며 복구할 수 없습니다. \n계속 진행하시겠습니까?`)
    if (result == false) { return }

    location.href = `/project/worker.jsp?pjNo=${pjNo}`;
} // func end
