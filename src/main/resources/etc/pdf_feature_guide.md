# PDF 생성 기능 구현 가이드

## 1. 개요

이 문서는 개인별 시간 순으로 정렬된 체크리스트를 PDF로 생성하는 기능 구현에 대한 가이드입니다. `DashboardController`에 새로운 API 엔드포인트를 추가하고, `DashboardService`와 `DashboardMapper`를 수정하여 필요한 데이터를 조회합니다.

## 2. `DashboardController.java` 수정

`DashboardController.java`에 개인별 PDF 다운로드를 처리하는 새로운 엔드포인트를 추가합니다.

### 추가할 코드

아래 코드를 `DashboardController` 클래스 내부에 추가하세요. 기존 `downloadFullReportPdf` 메서드 아래에 추가하는 것을 권장합니다.

```java
    // [11] 프로젝트 대시보드 - 개인별 시간순 체크리스트 PDF 다운로드
    @GetMapping("/pdf/personal-checklist")
    public void downloadPersonalChecklistPdf(@RequestParam int pjNo, @RequestParam int userNo, HttpSession session, HttpServletResponse response) throws IOException {
        // 1. 세션에서 관리자 또는 본인 확인
        Integer sessionUserNo = (Integer) session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");

        // 관리자가 아니면서, 요청한 userNo가 세션의 userNo와 다른 경우 권한 없음
        boolean isAdmin = (bnNo != null);
        boolean isOwner = (sessionUserNo != null && sessionUserNo.equals(userNo));

        if (!isAdmin && !isOwner) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
            return;
        }

        // 2. PDF 생성에 필요한 데이터 조회
        // 프로젝트 정보 (bnNo는 관리자일 경우에만 유효, 개인 사용자는 null일 수 있음)
        PjDto projectInfo = pjService.read(pjNo, bnNo);
        if (projectInfo == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND, "프로젝트를 찾을 수 없습니다.");
            return;
        }

        // 근무자 정보
        UsersDto workerInfo = dashboardService.getUserInDash(pjNo, userNo);
        if (workerInfo == null) {
            response.sendError(HttpServletServletResponse.SC_NOT_FOUND, "근무자를 찾을 수 없습니다.");
            return;
        }

        // 개인별 시간순 정렬된 업무 리스트 (서비스에 새로운 메소드 필요)
        List<DashboardDto> performances = dashboardService.getPersonalPerformancesSorted(pjNo, userNo);

        // 3. HTTP 헤더 설정
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"Personal_Checklist_" + workerInfo.getUserName() + "_" + pjNo + ".pdf\"");

        // 4. PDF 생성 및 전송
        try {
            pdfGeneratorUtil.generatePersonalChecklistPdf(projectInfo, workerInfo.getUserName(), performances, response.getOutputStream());
        } catch (DocumentException e) {
            throw new IOException("PDF 생성 중 오류가 발생했습니다.", e);
        }
    }
```

## 3. `DashboardService.java` 수정 제안

`DashboardService` 인터페이스와 구현체에 컨트롤러에서 호출할 새로운 메서드를 추가해야 합니다. 이 메서드는 Mapper를 호출하여 특정 사용자의 업무 목록을 시간순으로 정렬하여 가져옵니다.

### 추가할 메서드 (예시)

`DashboardService`에 아래 메서드를 추가하세요.

```java
public List<DashboardDto> getPersonalPerformancesSorted(int pjNo, int userNo) {
    // 매퍼를 호출하여 userNo에 해당하는 사용자의 pjPerform 레코드를 시간순으로 정렬하여 가져옵니다.
    // pjWorker와 Users 테이블을 조인하여 userNo에 해당하는 pjRoleNo를 찾아야 합니다.
    return dashboardMapper.getPersonalPerformancesSorted(pjNo, userNo);
}
```

## 4. `DashboardMapper` 수정 제안

`DashboardMapper` 인터페이스와 해당 XML 파일에 새로운 쿼리를 추가해야 합니다.

### `DashboardMapper.java` (인터페이스)

```java
List<DashboardDto> getPersonalPerformancesSorted(@Param("pjNo") int pjNo, @Param("userNo") int userNo);
```

### `DashboardMapper.xml` (SQL 쿼리)

아래와 같은 SQL 쿼리를 `DashboardMapper.xml` 파일에 추가해야 합니다. `pj_perform` 테이블을 기준으로 관련 테이블들과 조인하고, 특정 `userNo`의 데이터를 `pf_start` 시간순으로 정렬합니다.

**주의**: 아래 쿼리는 예시이며, 실제 테이블/컬럼 구조와 `DashboardDto`의 필드 구성에 맞게 SELECT 절의 컬럼들을 수정해야 할 수 있습니다.

```xml
<select id="getPersonalPerformancesSorted" resultType="five_minutes.model.dto.DashboardDto">
    SELECT
        p.pf_no         AS "pjPerDto.pfNo",
        p.pf_start      AS "pjPerDto.pfStart",
        p.pf_end        AS "pjPerDto.pfEnd",
        p.pf_status     AS "pjPerDto.pfStatus",
        w.pj_role_name  AS "pjWorkerDto.pjRoleName",
        c.pj_chkl_title AS "pjCheckDto.pjChklTitle",
        u.user_name     AS "usersDto.userName"
    FROM
        pj_perform p
    INNER JOIN
        pj_worker w ON p.pj_role_no = w.pj_role_no
    INNER JOIN
        users u ON w.user_no = u.user_no
    INNER JOIN
        pj_checklist_item c ON p.pj_chk_item_no = c.pj_chk_item_no
    WHERE
        p.pj_no = #{pjNo} AND u.user_no = #{userNo}
    ORDER BY
        p.pf_start ASC;
</select>
```

## 5. `PdfGeneratorUtil.java` (참고)

`PdfGeneratorUtil.java`의 `generatePersonalChecklistPdf` 메서드는 현재 로직을 그대로 사용해도 괜찮습니다. 컨트롤러에서 이미 시간순으로 정렬된 `performances` 리스트를 전달해주기 때문입니다.

