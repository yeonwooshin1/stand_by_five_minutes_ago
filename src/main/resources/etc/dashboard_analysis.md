# PDF 다운로드 기능 구현 가이드 (이미지 포함)

이 가이드는 `PdfGeneratorUtil.java`를 기반으로 프로젝트 업무 리스트 전체 및 개별 상세 정보를 PDF로 다운로드하는 기능을 구현하는 방법을 안내합니다. '개별 정보' PDF에는 로고 이미지를 포함합니다.

## 1. 전체 흐름

1.  **`performcheck.js`**: 사용자가 'PDF' 버튼을 클릭하면 새로운 Controller 엔드포인트를 호출합니다.
2.  **`DashboardController.java`**: 요청을 받아 `DashboardService`에 PDF 생성을 위임하고, 생성된 PDF를 `HttpServletResponse`를 통해 클라이언트에게 전송합니다.
3.  **`DashboardService.java`**: DB에서 PDF에 필요한 데이터를 조회한 후, `PdfGeneratorUtil`을 호출하여 PDF 생성을 요청합니다.
4.  **`PdfGeneratorUtil.java`**: `Service`로부터 받은 데이터를 기반으로 OpenPDF를 사용하여 PDF 문서를 생성하고 `OutputStream`에 씁니다.

## 2. `PdfGeneratorUtil.java` 리팩토링

`C:\Users\tj-bu-702-07\Desktop\cording\fiveMinutes\src\main\java\five_minutes\util\PdfGeneratorUtil.java` 파일을 아래 내용으로 수정하세요.

```java
package five_minutes.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import five_minutes.model.dto.DashboardDto;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

@Component
public class PdfGeneratorUtil {

    private Font titleFont;
    private Font tableHeaderFont;
    private Font tableBodyFont;

    public PdfGeneratorUtil() {
        try {
            BaseFont baseFont = BaseFont.createFont("font/NotoSansKR-VariableFont_wght.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.titleFont = new Font(baseFont, 18, Font.BOLD);
            this.tableHeaderFont = new Font(baseFont, 12, Font.BOLD);
            this.tableBodyFont = new Font(baseFont, 11);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    public void generateAllPerformancesPdf(List<DashboardDto> performances, OutputStream outputStream) throws DocumentException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        document.add(new Paragraph("프로젝트 업무 관리 전체 리스트", titleFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{1, 2, 3, 4, 3, 3, 2});

        addTableHeader(table, "No", "근무자", "역할명", "체크리스트명", "시작시간", "종료시간", "상태");

        int index = 1;
        for (DashboardDto dto : performances) {
            table.addCell(new PdfPCell(new Paragraph(String.valueOf(index++), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(dto.getUsersDto().getUserName(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(dto.getPjWorkerDto().getPjRoleName(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(dto.getPjCheckDto().getPjChklTitle(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfStart(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfEnd(), tableBodyFont)));
            table.addCell(new PdfPCell(new Paragraph(getStatusString(dto.getPjPerDto().getPfStatus()), tableBodyFont)));
        }

        document.add(table);
        document.close();
    }

    public void generateSinglePerformancePdf(DashboardDto performance, OutputStream outputStream) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, outputStream);
        document.open();

        try {
            Image logo = Image.getInstance("src/main/resources/static/img/logo.png"); // 로고 경로
            logo.scaleToFit(120, 60);
            document.add(logo);
        } catch (IOException e) {
            System.err.println("PDF 로고 이미지를 찾을 수 없습니다: " + e.getMessage());
        }

        document.add(new Paragraph("근무 정보 상세보기", titleFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(2);
        table.setWidthPercentage(80);
        table.setWidths(new float[]{1, 3});

        addDetailRow(table, "근무자", performance.getUsersDto().getUserName());
        addDetailRow(table, "역할", performance.getPjWorkerDto().getPjRoleName());
        addDetailRow(table, "체크리스트", performance.getPjCheckDto().getPjChklTitle());
        addDetailRow(table, "시작시간", performance.getPjPerDto().getPfStart());
        addDetailRow(table, "종료시간", performance.getPjPerDto().getPfEnd());
        addDetailRow(table, "상태", getStatusString(performance.getPjPerDto().getPfStatus()));
        addDetailRow(table, "메모", performance.getPjPerDto().getNote());

        document.add(table);
        document.close();
    }

    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, tableHeaderFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            table.addCell(cell);
        }
    }

    private void addDetailRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, tableHeaderFont));
        PdfPCell valueCell = new PdfPCell(new Paragraph(value, tableBodyFont));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    private String getStatusString(int status) {
        switch (status) {
            case 1: return "시작전";
            case 2: return "진행중";
            case 3: return "완료됨";
            case 4: return "취소됨";
            case 5: return "보류중";
            default: return "알 수 없음";
        }
    }
}
```

## 3. `DashboardController.java`에 PDF 다운로드 엔드포인트 추가

`DashboardController.java` 파일에 PDF 다운로드 관련 코드를 추가합니다.

```java
// ... 기존 Controller 코드 ...
import five_minutes.util.PdfGeneratorUtil;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

// ...

@RequiredArgsConstructor
public class DashboardController {

    private final DashboardService dashboardService;
    private final FileService fileService;
    private final PdfGeneratorUtil pdfGeneratorUtil; // PdfGeneratorUtil 주입

    // ... 기존 메소드 ...

    @GetMapping("/pdf/all")
    public void downloadAllPerformancesPdf(@RequestParam int pjNo, HttpSession session, HttpServletResponse response) throws IOException {
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (userNo == null && bnNo == null) { response.sendError(HttpServletResponse.SC_UNAUTHORIZED); return; }

        List<DashboardDto> list = dashboardService.getListPJDash(pjNo, userNo, bnNo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"performances_" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + ".pdf\"");

        try {
            pdfGeneratorUtil.generateAllPerformancesPdf(list, response.getOutputStream());
        } catch (DocumentException e) { throw new IOException(e.getMessage()); }
    }

    @GetMapping("/pdf/single")
    public void downloadSinglePerformancePdf(@RequestParam int pjNo, @RequestParam int pfNo, HttpSession session, HttpServletResponse response) throws IOException {
        Integer userNo = (Integer) session.getAttribute("loginUserNo");
        String bnNo = (String) session.getAttribute("loginBnNo");
        if (userNo == null && bnNo == null) { response.sendError(HttpServletResponse.SC_UNAUTHORIZED); return; }

        DashboardDto dto = dashboardService.getIndiListPJDash(pjNo, pfNo, userNo, bnNo);
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=\"performance_" + pfNo + ".pdf\"");

        try {
            pdfGeneratorUtil.generateSinglePerformancePdf(dto, response.getOutputStream());
        } catch (DocumentException e) { throw new IOException(e.getMessage()); }
    }
}
```

## 4. `performcheck.js` 및 `performcheck.jsp` 수정

**`performcheck.js`** 파일 하단에 아래 함수들을 추가합니다.

```javascript
function downloadAllPdf() {
    window.location.href = `/project/perform/check/pdf/all?pjNo=${pjNo}`;
}

function downloadSinglePdf() {
    if (!currentPfNo) { alert("먼저 상세보기를 통해 근무 정보를 선택해주세요."); return; }
    window.location.href = `/project/perform/check/pdf/single?pjNo=${pjNo}&pfNo=${currentPfNo}`;
}
```

**`performcheck.jsp`** 에서 버튼 부분을 수정합니다.

```html
<!-- 전체 다운로드 버튼 -->
<button type="button" class="btn btn-outline-danger" onclick="downloadAllPdf()">PDF</button>

<!-- 모달 창 안의 개별 다운로드 버튼 -->
<div class="modal-footer">
    <button type="button" class="btn btn-info" onclick="downloadSinglePdf()">PDF 저장</button>
    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
    <button type="button" class="btn btn-primary" onclick="updatePJPerform()" data-bs-dismiss="modal">수정</button>
</div>
```

## 5. 폰트 및 이미지 파일 준비

- **폰트**: `src/main/resources/font/` 디렉터리에 `NotoSansKR-VariableFont_wght.ttf` 파일을 위치시켜 주세요.
- **이미지**: `src/main/resources/static/img/` 디렉터리에 `logo.png` 파일을 위치시켜 주세요. (경로와 파일명은 실제 사용하는 리소스에 맞게 `PdfGeneratorUtil.java`에서 수정할 수 있습니다.)