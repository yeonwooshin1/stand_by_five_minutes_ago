# PjCheck 기능 개선 방안 (ProjectWorker 패턴 적용)

## 1. 개요

현재 `PjCheck` 관련 기능은 화면 UI가 정상적으로 동작하지 않는 문제가 있습니다. 이는 프론트엔드에서 발생한 여러 변경 사항(추가, 수정, 삭제)을 백엔드에서 한 번에 처리하는 로직이 부재하기 때문입니다.

성공적으로 구현된 `ProjectWorker` 기능의 패턴을 `PjCheck`에 동일하게 적용하여 문제를 해결할 수 있습니다. 핵심은 **프론트엔드에서 변경된 데이터 목록을 관리하고, '저장' 시점에 이 목록 전체를 서버에 보내 일괄 처리(Batch Process)**하는 것입니다.

---

## 2. 파일별 개선 필요 사항

### 2.1. `PjCheckDto.java` (DTO 개선)

- **문제점**: 프론트엔드에서 각 체크리스트 항목의 변경 상태(신규, 수정, 삭제)를 백엔드에 전달할 표준화된 방법이 없습니다.
- **개선 방안**: `ProjectWorkerDto`와 동일하게, 데이터의 변경 상태를 관리할 `changeStatus` 필드를 추가해야 합니다.
    ```java
    // PjCheckDto.java 내에 추가
    private int changeStatus; // 0: 변경 없음, 1: 신규, 3: 수정, 4: 삭제
    ```
    - 이 필드는 프론트엔드(`checklist.js`)에서 사용자가 데이터를 변경할 때마다 해당 상태값으로 설정되어야 합니다.

### 2.2. `PjCheckService.java` (서비스 로직 추가)

- **문제점**: `ProjectWorkerService`의 `savePJWorker`처럼, `List<PjCheckDto>`를 인자로 받아 상태별로 CRUD 작업을 수행하는 일괄 처리 서비스 메소드가 없습니다.
- **개선 방안**: `savePJChecklist(List<PjCheckDto> list)` 메소드를 새로 구현해야 합니다.
    - 이 메소드는 컨트롤러로부터 DTO 리스트를 전달받습니다.
    - 리스트를 순회(loop)하면서 각 `PjCheckDto` 객체의 `changeStatus` 값을 확인합니다.
    - `changeStatus` 값에 따라 아래와 같이 적절한 DAO 메소드를 호출합니다.
        - `status == 1` (신규) → `pjCheckDao.createPJCheck(dto)` 호출
        - `status == 3` (수정) → `pjCheckDao.updatePJCheck(dto)` 호출
        - `status == 4` (삭제) → `pjCheckDao.deletePJCheck(dto.getPjChkItemNo())` 호출
    - 각 처리 결과를 `List<Map<String, Integer>>` 형태로 만들어 컨트롤러에 반환합니다.

### 2.3. `PjCheckController.java` (API 엔드포인트 추가)

- **문제점**: 현재 컨트롤러는 단일 데이터 처리를 위한 API(`@PostMapping`, `@PutMapping`, `@DeleteMapping`)만 존재합니다. `checklist.js`에서 변경된 모든 데이터를 한 번에 보낼 수 있는 창구가 없습니다.
- **개선 방안**: `ProjectWorkerController`의 `savePJWorker`처럼, `List<PjCheckDto>` 전체를 Request Body로 받는 새로운 API 엔드포인트를 추가해야 합니다.
    ```java
    // PjCheckController.java 내에 추가
    @PostMapping("/save")
    public List<Map<String, Integer>> savePJChecklist(@RequestBody List<PjCheckDto> list, HttpSession session) {
        // ... 세션 검증 로직 ...
        return pjCheckService.savePJChecklist(list);
    }
    ```
    - 이 엔드포인트는 위에서 만든 `pjCheckService.savePJChecklist()` 메소드를 호출하여 일괄 처리를 위임합니다.

### 2.4. `PjCheckDao.java` (DAO 검토)

- **문제점**: 특이사항 없음.
- **개선 방안**: 현재 `PjCheckDao`에는 `createPJCheck`, `updatePJCheck`, `deletePJCheck` 등 단일 데이터를 처리하는 메소드들이 이미 잘 구현되어 있습니다. 새로 만들 `PjCheckService.savePJChecklist` 메소드가 이들을 활용하면 되므로 **DAO는 수정할 필요가 없습니다.**

### 2.5. 프론트엔드 (`checklist.js`, `checklist.jsp`)

- **문제점**:
    1.  **UI와 데이터 불일치**: JSP의 테이블 헤더(`<thead>`)와 JS에서 동적으로 생성하는 행(`<tr>`)의 컬럼 구조가 맞지 않아 화면이 깨져 보일 수 있습니다.
    2.  **잘못된 저장 방식**: '저장' 버튼 클릭 시, 변경된 모든 데이터를 모아 새로 만든 `/project/checklist/save` API로 전송하는 로직이 필요합니다.
- **개선 방안**:
    1.  **`checklist.jsp`**: 테이블의 `<thead>` 구조를 `checklist.js`가 생성하는 데이터 컬럼 순서(예: `체크리스트명`, `설명보기 버튼`, `삭제 버튼`)에 맞게 수정해야 합니다.
    2.  **`checklist.js`**:
        -   `ProjectWorker.js`의 `TemporarySaveWorker` 배열처럼, `TemporarySaveChecklist` 배열을 운영하여 모든 변경사항(신규 추가, 수정, 삭제)을 `changeStatus`와 함께 기록해야 합니다.
        -   `savePJchecklist` 함수는 `fetch`를 사용하여 `TemporarySaveChecklist` 배열 전체를 `/project/checklist/save` 엔드포인트로 POST 방식으로 전송하도록 수정해야 합니다.
