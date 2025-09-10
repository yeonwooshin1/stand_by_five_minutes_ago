# `performcheck.jsp` 및 `performcheck.js` 개선 가이드라인

## 1. 문제점 분석

현재 "상세보기" 버튼을 클릭해도 모달에 해당 항목의 상세 정보가 올바르게 표시되지 않고 있습니다.

- **주요 원인**: `readAllPJworker` 함수에서 동적으로 생성되는 각 "상세보기" 버튼이, 자신이 어떤 항목(고유 ID인 `pfNo`)에 해당하는지에 대한 정보를 가지고 있지 않습니다. 따라서 모달을 여는 기능은 동작하지만, 모달에 어떤 데이터를 채워야 할지 `readPJworker` 함수에 전달하지 못하고 있습니다.
- **부가적인 문제**:
    1.  `readPJworker` 함수가 페이지 로드 시(`readPJworker();`) 한 번만 호출되고, 버튼 클릭 시에는 호출되지 않아 데이터가 갱신되지 않습니다.
    2.  `performcheck.js`에서 프로젝트 번호인 `pjNo`를 사용하고 있지만, JSP 파일에서 이 값을 받아오는 부분이 명확하지 않습니다.
    3.  근무 리스트 테이블의 No.가 항상 1로 표시되는 작은 버그가 있습니다.

## 2. 해결 방안

다음 단계에 따라 JSP와 JavaScript 파일을 수정하여 문제를 해결할 수 있습니다.

1.  **`pjNo` 전역 변수 설정 (`performcheck.jsp`)**: JavaScript가 어떤 프로젝트에 대한 요청을 보내야 하는지 알 수 있도록, JSP 상단에 `pjNo`를 JavaScript 변수로 선언합니다.
2.  **"상세보기" 버튼에 `onclick` 이벤트 추가 (`performcheck.js`)**: `readAllPJworker` 함수를 수정하여 "상세보기" 버튼을 클릭할 때 해당 항목의 `pfNo`를 인자로 `readPJworker` 함수를 호출하도록 변경합니다.
3.  **`readPJworker` 함수 수정 (`performcheck.js`)**: `readPJworker` 함수가 `pfNo`를 인자로 받도록 수정하고, 페이지 로드 시 실행되던 불필요한 호출을 제거합니다.
4.  **(부가) 테이블 인덱스 수정 (`performcheck.js`)**: 테이블의 'No'가 올바르게 증가하도록 수정합니다.

## 3. 코드 수정 상세

### 3.1. `performcheck.jsp` 수정

`<jsp:include page="/project/navigation.jsp"></jsp:include>` 바로 아래에 다음 코드를 추가하여, URL의 쿼리 파라미터로부터 `pjNo`를 받아 JavaScript 전역 변수로 만들어 줍니다.

```jsp
<script>
    const pjNo = <%= request.getParameter("pjNo") %>;
</script>
```

### 3.2. `performcheck.js` 수정

#### 1. `readAllPJworker` 함수 수정

- `onclick="readPJworker(${d.pjPerDto.pfNo})"`를 버튼에 추가하여 클릭 시 `pfNo`와 함께 함수를 호출하도록 합니다.
- 테이블 번호가 `index + 1` 대신 `i + 1`로 표시되도록 수정합니다.

**수정 전:**
```javascript
// ...
let index = 0;
// ...
if (data.length > 0) {
    for (let i = 0; i < data.length; i++) {
        // ...
        html += `<tr>
                    <td>${index + 1}</td>
                    <td>${d.usersDto.userName}</td>
                    <td>${d.pjWorkerDto.pjRoleName}</td>
                    <td>${d.pjCheckDto.pjChklTitle}</td>
                    <td>${d.pjPerDto.pfStart}</td>
                    <td>${d.pjPerDto.pfEnd}</td>
                    <td>${d.pjPerDto.pfStatus}</td>
                    <td><button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                        data-bs-target="#detailPerform">상세보기</button></td>
                </tr>
            `;
    } // for end
// ...
```

**수정 후:**
```javascript
// ...
// let index = 0; // 더 이상 필요 없으므로 삭제하거나 그대로 두어도 무방합니다.
// ...
if (data.length > 0) {
    for (let i = 0; i < data.length; i++) {
        // ...
        html += `<tr>
                    <td>${i + 1}</td>
                    <td>${d.usersDto.userName}</td>
                    <td>${d.pjWorkerDto.pjRoleName}</td>
                    <td>${d.pjCheckDto.pjChklTitle}</td>
                    <td>${d.pjPerDto.pfStart}</td>
                    <td>${d.pjPerDto.pfEnd}</td>
                    <td>${d.pjPerDto.pfStatus}</td>
                    <td><button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                        data-bs-target="#detailPerform" onclick="readPJworker(${d.pjPerDto.pfNo})">상세보기</button></td>
                </tr>
            `;
    } // for end
// ...
```

#### 2. `readPJworker` 함수 수정 및 전역 호출 제거

- 함수가 `pfNo`를 파라미터로 받도록 시그니처를 변경합니다.
- 파일 하단에 있던 `readPJworker();` 호출을 삭제합니다.

**수정 전:**
```javascript
// [05] 프로젝트 근무리스트 개별 조회(모달)
const readPJworker = async () => {

    // ...

    // fetch
    try {
        const response = await fetch(`/project/perform/check/indi?pjNo=${pjNo}&pfNo=${pfNo}`)
        const data = await response.json();
        // ...
    } catch (error) {
        console.log(error)
    }
}
readPJworker();
```

**수정 후:**
```javascript
// [05] 프로젝트 근무리스트 개별 조회(모달)
const readPJworker = async (pfNo) => { // pfNo를 파라미터로 받음

    // 마크다운
    const userName = document.querySelector('#userName');
    const pjRoleName = document.querySelector('#pjRoleName');
    const pjChklTitle = document.querySelector('#pjCheckList');
    const pfStart = document.querySelector('#pfStart');
    const pfEnd = document.querySelector('#pfEnd');
    const pfStatus = document.querySelector('.pjPerformStatus');
    const note = document.querySelector('#memo');
    const fileName = document.querySelector('#fileName');

    // fetch
    try {
        // 파라미터로 받은 pfNo를 fetch URL에 사용
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
        
        // fileName은 input type="file" 이므로 value를 직접 설정할 수 없습니다.
        // 파일명을 텍스트로 보여주려면 별도의 <span>이나 <p> 태그를 사용해야 합니다.
        // 예: document.querySelector('#fileNameDisplay').innerText = data.pjFileDto.fileName;
        // fileName.value = data.pjFileDto.fileName; // 이 코드는 동작하지 않으므로 주석 처리하거나 삭제합니다.

    } catch (error) {
        console.log(error)
    }
}
// readPJworker(); // 페이지 로드 시 호출되던 코드를 삭제
```

---

위와 같이 수정하면 "상세보기" 버튼을 누를 때마다 해당 근무 정보가 모달에 정상적으로 출력될 것입니다.
