# 체크리스트 페이지 기능 수정 가이드

안녕하세요. 체크리스트 페이지의 '검색(템플릿 불러오기)'과 '추가' 기능의 문제를 해결하기 위한 코드 수정 가이드입니다. 각 문제의 원인과 해결 방법을 상세한 주석과 함께 안내해 드립니다.

## 문제 1: 템플릿 검색(불러오기) 기능 오류

### 원인

현재 템플릿을 불러오는 모달창은 서버에서 전체 템플릿 목록을 가져오는 대신, 코드에 미리 정해진(하드코딩된) ID 목록을 사용하여 템플릿을 하나씩 불러오고 있습니다. 이는 비효율적이며, 새로운 템플릿이 추가되어도 목록에 나타나지 않는 문제를 야기합니다.

### 해결 방안

`checklist.js` 파일을 수정하여, 서버에 한 번의 요청으로 로그인한 사용자가 사용할 수 있는 모든 템플릿 목록을 가져와 동적으로 드롭다운 메뉴를 채우도록 변경합니다.

### 수정할 파일: `src/main/resources/static/js/project/checklist.js`

#### 기존 코드

```javascript
// [05] 체크리스트 템플릿 모달 불러오기
$('#checkTemplateModal').on('show.bs.modal', async function () {
    const modalCheckTemplate = document.querySelector(".modalCheckTemplate");

    // 대분류 데이터가 이미 로드되었는지 확인
    if (modalCheckTemplate.options.length > 1) return;

    try {
        // 임의의 ctNo 배열 또는 서버에서 목록을 가져오는 로직 필요
        // 여기서는 예시로 4000001부터 4000005까지를 가정합니다.
        const ctNoList = [4000001, 4000002, 4000003, 4000004, 4000005];
        let optionsHtml = '';

        for (const ctNo of ctNoList) {
            try {
                const response = await fetch(`/project/checklist/tem?ctNo=${ctNo}`);
                const dto = await response.json();
                if (dto && dto.ctName) {
                    optionsHtml += `<option value="${dto.ctNo}" data-ctname="${dto.ctName}" data-ctdescription="${dto.ctDescription || ''}">${dto.ctName}</option>`;
                }
            } catch (error) {
                console.warn(`템플릿 ${ctNo} 로딩 실패 :`, error)
            }
        }
        modalCheckTemplate.innerHTML += optionsHtml;

    } catch (error) {
        console.error("Error fetching template categories:", error);
    }
});
```

#### 수정된 코드

```javascript
// [05] 체크리스트 템플릿 모달 불러오기 (수정됨)
$('#checkTemplateModal').on('show.bs.modal', async function () {
    const modalCheckTemplate = document.querySelector(".modalCheckTemplate");

    // 모달을 열 때마다 새로 불러오는 것을 방지하기 위해, 이미 옵션이 2개 이상 있다면(기본 옵션 포함) 함수를 종료합니다.
    if (modalCheckTemplate.options.length > 1) return;

    try {
        // CTemController에 있는 GET /checktem 엔드포인트를 호출하여
        // 현재 로그인한 사업자(business)가 생성한 모든 체크리스트 템플릿 목록을 한 번에 가져옵니다.
        const response = await fetch(`/checktem`);
        const data = await response.json();
        let optionsHtml = '';

        // 서버에서 받은 데이터가 있고, 로그인 되지 않은 상태가 아니라면
        if (data.length > 0 && data[0].status !== "NOT_LOGGED_IN") {
            // 각 템플릿(dto)에 대해 반복하면서 <option> 태그를 생성합니다.
            data.forEach(dto => {
                if (dto && dto.ctName) {
                    // 각 옵션에 value로 ctNo(템플릿 번호)를,
                    // data-* 속성을 이용해 나중에 사용될 이름(ctname)과 설명(ctdescription)을 저장합니다.
                    optionsHtml += `<option value="${dto.ctNo}" data-ctname="${dto.ctName}" data-ctdescription="${dto.ctDescription || ''}">${dto.ctName}</option>`;
                }
            });
            // 생성된 옵션들을 드롭다운 메뉴에 추가합니다.
            modalCheckTemplate.innerHTML += optionsHtml;
        }
    } catch (error) {
        // 템플릿 목록을 불러오는 중 오류가 발생하면 콘솔에 에러를 출력합니다.
        console.error("체크리스트 템플릿 대분류를 불러오는 중 오류 발생:", error);
    }
});
```

---

## 문제 2: 템플릿 추가 기능 오류

### 원인

템플릿을 선택하여 체크리스트에 추가할 때, 백엔드(서버)는 결과로 숫자(새로 생성된 항목의 ID)를 반환합니다. 하지만 프론트엔드(브라우저)의 자바스크립트는 JSON 형태의 데이터를 기대하고 있어 데이터 형식 불일치로 오류가 발생합니다.

### 해결 방안

1.  **백엔드 수정:** `PjCheckController.java`에서 숫자 대신 `{"newPjChkItemNo": 123}`과 같은 JSON 형태로 데이터를 반환하도록 수정합니다.
2.  **프론트엔드 수정:** `checklist.js`에서 수정된 JSON 응답을 올바르게 처리하도록 코드를 변경하고, 사용자에게 혼란을 주던 알림 메시지도 명확하게 수정합니다.

### 2.1. 백엔드 수정

#### 수정할 파일: `src/main/java/five_minutes/controller/PjCheckController.java`

#### 기존 코드

```java
    // [8] 프로젝트 체크리스트 템플릿 불러오기
    @PostMapping("/tem")
    public int loadAndSaveTemplate(@RequestBody Map<String, Integer> request, HttpSession session) {
        int ctiNo = request.get("ctiNo");
        int pjNo = request.get("pjNo");
        // 1. 세션 확인
        if (session.getAttribute("loginUserNo") == null) {
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_LOGGED_IN");
            return -1; // 비로그인시 -1 리턴 및 setStatus 전송
        }
        // 2. pjNo 맞으면 사업자번호 조회
        String bnNo = (String) session.getAttribute("loginBnNo");
        boolean isValidProject = pjCheckService.checkPjNo(pjNo, bnNo);
        if (!isValidProject) {
            CTItemDto dto = new CTItemDto();
            dto.setStatus("NOT_FOUND");
            return 0; // 프로젝트 접근 권한 없음
        }
        // 3. 리턴
        return pjCheckService.loadAndSaveTemplate(ctiNo, pjNo);
    }
```

#### 수정된 코드

```java
    // [8] 프로젝트 체크리스트 템플릿 불러오기 (수정됨)
    @PostMapping("/tem")
    // 반환 타입을 int에서 Map<String, Object>로 변경하여 JSON 객체를 반환하도록 합니다.
    public Map<String, Object> loadAndSaveTemplate(@RequestBody Map<String, Integer> request, HttpSession session) {
        int ctiNo = request.get("ctiNo");
        int pjNo = request.get("pjNo");
        // 프론트엔드에 전달할 응답 데이터를 담을 Map 객체를 생성합니다.
        Map<String, Object> response = new HashMap<>();

        // 1. 세션에서 로그인 정보를 확인합니다.
        if (session.getAttribute("loginUserNo") == null) {
            // 로그인되지 않은 경우, 실패 상태와 메시지를 담아 반환합니다.
            response.put("success", false);
            response.put("message", "NOT_LOGGED_IN");
            response.put("newPjChkItemNo", -1); // JS에서 로그인 실패를 식별할 수 있도록 -1을 담습니다.
            return response;
        }

        // 2. 로그인된 사용자가 해당 프로젝트에 접근할 권한이 있는지 확인합니다.
        String bnNo = (String) session.getAttribute("loginBnNo");
        boolean isValidProject = pjCheckService.checkPjNo(pjNo, bnNo);
        if (!isValidProject) {
            // 권한이 없는 경우, 실패 상태와 메시지를 담아 반환합니다.
            response.put("success", false);
            response.put("message", "ACCESS_DENIED");
            response.put("newPjChkItemNo", 0); // JS에서 권한 없음을 식별할 수 있도록 0을 담습니다.
            return response;
        }

        // 3. 서비스 로직을 호출하여 템플릿을 불러와 저장하고, 새로 생성된 체크리스트 항목의 ID를 받습니다.
        int newPjChkItemNo = pjCheckService.loadAndSaveTemplate(ctiNo, pjNo);

        // 4. 서비스 처리 결과에 따라 성공 또는 실패 응답을 구성합니다.
        if (newPjChkItemNo > 0) {
            // 성공 시, 성공 상태와 새로 생성된 ID를 담아 반환합니다.
            response.put("success", true);
            response.put("newPjChkItemNo", newPjChkItemNo);
        } else {
            // 실패 시, 실패 상태를 담아 반환합니다.
            response.put("success", false);
            response.put("newPjChkItemNo", 0);
        }
        return response;
    }
```

### 2.2. 프론트엔드 수정

#### 수정할 파일: `src/main/resources/static/js/project/checklist.js`

#### 기존 코드

```javascript
// [03] 템플릿 선택 시 행 추가
document.addEventListener("click", async function (e) {
    if (e.target.classList.contains("selectTemplateBtn")) {
        const ctiNo = e.target.dataset.ctino;

        // 템플릿 데이터 불러오기 및 저장
        try {
            const response = await fetch(`/project/checklist/tem`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ ctiNo: parseInt(ctiNo), pjNo: parseInt(pjNo) })
            });

            if(!response.ok){
                throw new Error(`HTTP error! status : ${response.status}`);
            }

            const newPjChkItemNo = await response.json();

            if (newPjChkItemNo > 0) {
                alert('템플릿이 추가되었습니다. 저장 버튼을 눌러야 최종 반영됩니다.');
                await readAllpjcheck(); // 목록 새로고침
            } else {
                alert('템플릿 불러오기에 실패했습니다.');
            }
        } catch (error) {
            console.error('템플릿 추가 오류:' , error);
            alert('템플릿 추가 중 오류가 발생했습니다.')
        }
        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('checkTemplateModal'));
        modal.hide();
    }
});
```

#### 수정된 코드

```javascript
// [03] 템플릿 선택 시 행 추가 (수정됨)
document.addEventListener("click", async function (e) {
    // '선택' 버튼(.selectTemplateBtn)이 클릭되었는지 확인합니다.
    if (e.target.classList.contains("selectTemplateBtn")) {
        const ctiNo = e.target.dataset.ctino;

        try {
            // 백엔드에 템플릿 추가를 요청합니다. (POST /project/checklist/tem)
            const response = await fetch(`/project/checklist/tem`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ ctiNo: parseInt(ctiNo), pjNo: parseInt(pjNo) })
            });

            // HTTP 응답이 성공적이지 않으면 오류를 발생시킵니다.
            if (!response.ok) {
                throw new Error(`HTTP error! status: ${response.status}`);
            }

            // 백엔드에서 보낸 JSON 응답을 객체로 변환합니다.
            // ex) { success: true, newPjChkItemNo: 8000005 }
            const result = await response.json();

            // 응답 결과에 따라 분기 처리합니다.
            if (result.success && result.newPjChkItemNo > 0) {
                // 성공 시, 사용자에게 알리고 목록을 새로고침합니다.
                // "저장 버튼을 눌러야..." 라는 오해의 소지가 있는 메시지를 수정했습니다.
                alert('템플릿이 추가되었습니다.');
                await readAllpjcheck(); // 목록을 다시 불러와 화면을 갱신합니다.
            } else if (result.newPjChkItemNo === -1) {
                // 백엔드에서 보낸 '로그인 필요' 상태를 처리합니다.
                alert('로그인이 필요합니다.');
            } else {
                // 그 외의 실패 사례를 처리합니다.
                alert('템플릿 불러오기에 실패했습니다.');
            }
        } catch (error) {
            // fetch 과정이나 JSON 파싱 중 오류가 발생하면 콘솔에 에러를 출력하고 사용자에게 알립니다.
            console.error('템플릿 추가 오류:', error);
            alert('템플릿 추가 중 오류가 발생했습니다.');
        }

        // 처리가 끝나면 모달창을 닫습니다.
        const modal = bootstrap.Modal.getInstance(document.getElementById('checkTemplateModal'));
        modal.hide();
    }
});
```

---

위 가이드를 따라 코드를 수정하시면 체크리스트 페이지의 기능이 정상적으로 동작할 것입니다. 혹시 진행하시면서 궁금한 점이 있다면 언제든지 다시 질문해주세요.
