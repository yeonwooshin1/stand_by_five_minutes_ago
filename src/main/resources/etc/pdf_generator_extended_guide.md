# 신규 PDF 양식 3종 구현 가이드

이 문서는 요청하신 세 가지 새로운 PDF 보고서 양식을 `PdfGeneratorUtil.java`에 구현하는 방법을 안내합니다.

## 1. 목표

1.  **개인별 체크리스트**: 특정 근무자의 시간 순서별 업무 목록 PDF
2.  **업무 요약 보고서**: 프로젝트 기본 정보와 참여 인력 목록을 담은 PDF
3.  **종합 보고서**: 프로젝트, 클라이언트, 인력, 전체 업무 목록을 모두 포함하는 종합 보고서 PDF

## 2. 데이터 구조 분석

PDF 생성을 위해 여러 테이블의 정보가 필요하며, 각 정보는 기존 DAO를 통해 조회할 수 있습니다.

-   **프로젝트/클라이언트 정보**: `PjDao`의 `read(pjNo, bnNo)`를 통해 `PjDto`에서 가져옵니다.
-   **참여 인력 목록**: `ProjectWorkerDao`의 `readAll(pjNo)`를 통해 `List<ProjectWorkerDto>`에서 가져옵니다.
-   **업무 수행 목록**: `DashboardDao`의 `getListPJDash(pjNo)` 또는 `getListPJDashForUser(pjNo, userNo)`를 통해 `List<DashboardDto>`에서 가져옵니다.

이 가이드에서는 `Service`단에서 위 데이터들을 조합하여 `PdfGeneratorUtil`의 각 메소드에 전달하는 것을 전제로 합니다.

## 3. `PdfGeneratorUtil.java` 신규 메소드 가이드

기존 `PdfGeneratorUtil.java` 파일에 아래 세 가지 메소드를 추가합니다. 각 메소드는 특정 양식의 PDF를 생성하는 역할을 합니다.

---

### 3.1. 메소드 1: 개인별 시간 순 체크리스트 PDF

한 근무자의 모든 업무를 시간 순서대로 나열하는 보고서입니다.

```java
// 1. 개인별 시간 순 체크리스트 PDF 생성
public void generatePersonalChecklistPdf(PjDto projectInfo, String workerName, List<DashboardDto> performances, OutputStream outputStream) throws DocumentException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, outputStream);
    document.open();

    // 제목
    Paragraph title = new Paragraph(projectInfo.getPjName() + " - 개인별 업무 리스트", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);
    document.add(new Paragraph("근무자: " + workerName, headFont));
    document.add(new Paragraph(" ")); // 공백

    // 업무 테이블
    PdfPTable table = new PdfPTable(5); // 5 Columns
    table.setWidthPercentage(100);
    table.setWidths(new float[]{3, 3, 4, 5, 2});

    // 테이블 헤더
    addTableHeader(table, "시작시간", "종료시간", "역할명", "체크리스트명", "상태");

    // 테이블 바디 (Service에서 시간순으로 정렬된 performances 리스트를 전달해야 함)
    for (DashboardDto dto : performances) {
        table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfStart(), bodyFont)));
        table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfEnd(), bodyFont)));
        table.addCell(new PdfPCell(new Paragraph(dto.getPjWorkerDto().getPjRoleName(), bodyFont)));
        table.addCell(new PdfPCell(new Paragraph(dto.getPjCheckDto().getPjChklTitle(), bodyFont)));
        table.addCell(new PdfPCell(new Paragraph(getStatusString(dto.getPjPerDto().getPfStatus()), bodyFont)));
    }

    document.add(table);
    document.close();
}
```

---

### 3.2. 메소드 2: 업무 정보 요약 PDF

프로젝트의 기본 정보와 참여 인력 목록을 간략하게 보여주는 보고서입니다.

```java
// 2. 업무 정보 요약 PDF 생성
public void generateSummaryReportPdf(PjDto projectInfo, List<ProjectWorkerDto> workers, OutputStream outputStream) throws DocumentException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, outputStream);
    document.open();

    // 제목
    Paragraph title = new Paragraph("프로젝트 요약 보고서", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);
    document.add(new Paragraph(" "));

    // --- 프로젝트 기본 정보 ---
    document.add(new Paragraph("프로젝트 기본 정보", headFont));
    PdfPTable projectInfoTable = new PdfPTable(2);
    projectInfoTable.setWidthPercentage(100);
    projectInfoTable.setSpacingBefore(10f);
    projectInfoTable.setWidths(new float[]{1, 3});

    addDetailRow(projectInfoTable, "프로젝트명", projectInfo.getPjName());
    addDetailRow(projectInfoTable, "기간", projectInfo.getPjStartDate() + " ~ " + projectInfo.getPjEndDate());
    addDetailRow(projectInfoTable, "장소", projectInfo.getRoadAddress());
    document.add(projectInfoTable);
    document.add(new Paragraph(" "));

    // --- 참여 근무자 리스트 ---
    document.add(new Paragraph("참여 근무자 리스트", headFont));
    PdfPTable workerTable = new PdfPTable(4);
    workerTable.setWidthPercentage(100);
    workerTable.setSpacingBefore(10f);
    workerTable.setWidths(new float[]{1, 2, 3, 3});

    addTableHeader(workerTable, "No", "이름", "역할명", "연락처");

    int workerIndex = 1;
    for (ProjectWorkerDto worker : workers) {
        workerTable.addCell(new PdfPCell(new Paragraph(String.valueOf(workerIndex++), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getUserName(), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getPjRoleName(), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getUserPhone(), bodyFont)));
    }
    document.add(workerTable);

    document.close();
}
```

---

### 3.3. 메소드 3: 종합 보고서 PDF

프로젝트의 모든 정보를 상세하게 포함하는 완전한 형태의 보고서입니다.

```java
// 3. 종합 보고서 PDF 생성
public void generateFullReportPdf(PjDto projectInfo, List<ProjectWorkerDto> workers, List<DashboardDto> performances, OutputStream outputStream) throws DocumentException {
    Document document = new Document(PageSize.A4);
    PdfWriter.getInstance(document, outputStream);
    document.open();

    // 제목
    Paragraph title = new Paragraph("프로젝트 종합 보고서", titleFont);
    title.setAlignment(Element.ALIGN_CENTER);
    document.add(title);
    document.add(new Paragraph(" "));

    // --- 프로젝트 정보 ---
    document.add(new Paragraph("1. 프로젝트 정보", headFont));
    PdfPTable projectInfoTable = new PdfPTable(2);
    projectInfoTable.setWidthPercentage(100);
    projectInfoTable.setSpacingBefore(10f);
    projectInfoTable.setWidths(new float[]{1, 3});
    addDetailRow(projectInfoTable, "프로젝트명", projectInfo.getPjName());
    addDetailRow(projectInfoTable, "기간", projectInfo.getPjStartDate() + " ~ " + projectInfo.getPjEndDate());
    addDetailRow(projectInfoTable, "장소", projectInfo.getRoadAddress() + " " + projectInfo.getDetailAddress());
    document.add(projectInfoTable);
    document.add(new Paragraph(" "));

    // --- 클라이언트 정보 ---
    document.add(new Paragraph("2. 클라이언트 정보", headFont));
    PdfPTable clientInfoTable = new PdfPTable(2);
    clientInfoTable.setWidthPercentage(100);
    clientInfoTable.setSpacingBefore(10f);
    clientInfoTable.setWidths(new float[]{1, 3});
    addDetailRow(clientInfoTable, "클라이언트명", projectInfo.getClientName());
    addDetailRow(clientInfoTable, "담당자", projectInfo.getClientRepresent());
    addDetailRow(clientInfoTable, "연락처", projectInfo.getClientPhone());
    document.add(clientInfoTable);
    document.add(new Paragraph(" "));

    // --- 참여 근무자 리스트 ---
    document.add(new Paragraph("3. 참여 근무자 리스트", headFont));
    PdfPTable workerTable = new PdfPTable(4);
    workerTable.setWidthPercentage(100);
    workerTable.setSpacingBefore(10f);
    workerTable.setWidths(new float[]{1, 2, 3, 3});
    addTableHeader(workerTable, "No", "이름", "역할명", "연락처");
    int workerIndex = 1;
    for (ProjectWorkerDto worker : workers) {
        workerTable.addCell(new PdfPCell(new Paragraph(String.valueOf(workerIndex++), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getUserName(), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getPjRoleName(), bodyFont)));
        workerTable.addCell(new PdfPCell(new Paragraph(worker.getUserPhone(), bodyFont)));
    }
    document.add(workerTable);
    document.add(new Paragraph(" "));

    // --- 시간 순 근무 리스트 ---
    document.add(new Paragraph("4. 시간 순 근무 리스트", headFont));
    PdfPTable performanceTable = new PdfPTable(6);
    performanceTable.setWidthPercentage(100);
    performanceTable.setSpacingBefore(10f);
    performanceTable.setWidths(new float[]{2, 2, 2, 3, 4, 2});
    addTableHeader(performanceTable, "시작시간", "종료시간", "근무자", "역할명", "체크리스트명", "상태");
    for (DashboardDto dto : performances) {
        performanceTable.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfStart(), bodyFont)));
        performanceTable.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfEnd(), bodyFont)));
        performanceTable.addCell(new PdfPCell(new Paragraph(dto.getUsersDto().getUserName(), bodyFont)));
        performanceTable.addCell(new PdfPCell(new Paragraph(dto.getPjWorkerDto().getPjRoleName(), bodyFont)));
        performanceTable.addCell(new PdfPCell(new Paragraph(dto.getPjCheckDto().getPjChklTitle(), bodyFont)));
        performanceTable.addCell(new PdfPCell(new Paragraph(getStatusString(dto.getPjPerDto().getPfStatus()), bodyFont)));
    }
    document.add(performanceTable);

    document.close();
}
```

## 4. Controller 연동 예시

위에서 정의한 메소드들을 사용하기 위해 `DashboardController`에 새로운 엔드포인트를 추가할 수 있습니다. 아래는 **종합 보고서**를 다운로드하는 엔드포인트의 예시입니다.

```java
// In DashboardController.java

@GetMapping("/pdf/full-report")
public void downloadFullReportPdf(@RequestParam int pjNo, HttpSession session, HttpServletResponse response) throws IOException {
    // 1. 세션에서 사용자 정보 조회
    String bnNo = (String) session.getAttribute("loginBnNo");
    if (bnNo == null) {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "권한이 없습니다.");
        return;
    }

    // 2. PDF 생성에 필요한 데이터 조회
    PjDto projectInfo = pjService.read(pjNo, bnNo); // PjService 주입 필요
    List<ProjectWorkerDto> workers = projectWorkerService.getAllPJWorker(pjNo); // ProjectWorkerService 주입 필요
    List<DashboardDto> performances = dashboardService.getListPJDash(pjNo, null, bnNo); // 관리자 권한으로 전체 조회

    // 3. HTTP 헤더 설정
    response.setContentType("application/pdf");
    response.setHeader("Content-Disposition", "attachment; filename=\"Full_Report_" + pjNo + ".pdf\"");

    // 4. PDF 생성 및 전송
    try {
        pdfGeneratorUtil.generateFullReportPdf(projectInfo, workers, performances, response.getOutputStream());
    } catch (DocumentException e) {
        throw new IOException(e.getMessage());
    }
}
```
