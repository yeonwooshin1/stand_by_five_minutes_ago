package five_minutes.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.*;
import five_minutes.model.dto.DashboardDto;

import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Comparator;
import java.util.List;

/// **Info** =========================
///
/// PdfGeneratorUtil
///
/// PDF 파일을 생성합니다.
///
/// OpenPDF 라이브러리 사용 테스트
///
/// @author dongjin

@Component
public class PdfGeneratorUtil {

    // [*] 멤버변수 - 색상 테마 추가
    private Font titleFont;
    private Font headFont;
    private Font bodyFont;
    private Font subtitleFont;

    // 프로젝트 색상 테마
    private final Color MAIN_COLOR = new Color(47, 70, 115);      // #2F4673
    private final Color SUB_COLOR_1 = new Color(100, 117, 140);   // #64758c
    private final Color SUB_COLOR_2 = new Color(148, 155, 166);   // #949ba6
    private final Color SUB_COLOR_3 = new Color(217, 217, 217);   // #d9d9d9
    private final Color SUB_COLOR_4 = new Color(242, 242, 242);   // #f2f2f2
    private final Color WHITE = Color.WHITE;

    // [*] 생성자에서 폰트 초기화
    public PdfGeneratorUtil() {
        try {
            // [*] 폰트 스타일 지정. noto sans kr가 한글 영문을 모두 지원하므로, 하나로 통일합니다.
            BaseFont baseFont = BaseFont.createFont("/font/NotoSansKR-VariableFont_wght.ttf",
                    BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.titleFont = new Font(baseFont, 22, Font.BOLD);     // 크기 증가
            this.subtitleFont = new Font(baseFont, 16, Font.BOLD);
            this.headFont = new Font(baseFont, 10, Font.BOLD);      // 크기 증가
            this.bodyFont = new Font(baseFont, 9);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    } // func end

    // [*] 헤더 섹션 생성 (프로젝트 스타일 적용)
    private void addHeaderSection(Document document) throws DocumentException, IOException {
        // 헤더 테이블 (로고와 타이틀을 위한)
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{8, 2}); // 타이틀 7, 로고 3 비율
        headerTable.setSpacingAfter(20f);

        // 타이틀 셀
        Paragraph title = new Paragraph("프로젝트 타임라인", titleFont);
        title.setSpacingBefore(10f);
        PdfPCell titleCell = new PdfPCell(title);
        titleCell.setBorder(Rectangle.NO_BORDER);
        titleCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        titleCell.setPadding(10f);

        // 로고 셀
        PdfPCell logoCell = new PdfPCell();
        try {
            Image logo = Image.getInstance("src/main/resources/static/img/logoNonText.png");
            logo.scaleAbsolute(60, 60);
            logoCell.addElement(logo);
        } catch (Exception e) {
            // 로고 로드 실패시 빈 셀
            logoCell.addElement(new Phrase(" "));
        }
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        logoCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        logoCell.setPadding(5f);
        logoCell.setPaddingRight(0f); // 오른쪽 패딩 완전 제거

        headerTable.addCell(titleCell);
        headerTable.addCell(logoCell);
        document.add(headerTable);

        // 구분선 추가
        PdfPTable separatorTable = new PdfPTable(1);
        separatorTable.setWidthPercentage(100);
        PdfPCell separatorCell = new PdfPCell();
        separatorCell.setFixedHeight(3f);
        separatorCell.setBackgroundColor(MAIN_COLOR);
        separatorCell.setBorder(Rectangle.NO_BORDER);
        separatorTable.addCell(separatorCell);
        separatorTable.setSpacingAfter(25f);
        document.add(separatorTable);
    }

    // [*] 테이블 헤더 만들기 (개선된 스타일)
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(MAIN_COLOR);                    // 메인 색상 사용
            cell.setPadding(8f);
            cell.setBorderWidth(1f);
            cell.setBorderColor(SUB_COLOR_2);

            // 헤더 텍스트를 흰색으로
            Paragraph headerText = new Paragraph(header, new Font(headFont.getBaseFont(), 11, Font.BOLD, Color.WHITE));
            cell = new PdfPCell(headerText);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBackgroundColor(MAIN_COLOR);
            cell.setPadding(8f);
            cell.setBorderWidth(1f);
            cell.setBorderColor(SUB_COLOR_2);

            table.addCell(cell);
        }
    }

    // [*] 가운데 정렬용 셀 생성 함수 (개선된 스타일)
    private PdfPCell createBodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(8f);
        cell.setBorderWidth(0.5f);
        cell.setBorderColor(SUB_COLOR_3);
        cell.setBackgroundColor(WHITE);
        return cell;
    }

    // [*] 체크박스 셀 생성 (개선된 스타일)
    private PdfPCell createCheckboxCell(PdfWriter writer, int index) {
        try {
            PdfPCell checkBoxCell = new PdfPCell();
            checkBoxCell.setHorizontalAlignment(Element.ALIGN_CENTER);
            checkBoxCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            checkBoxCell.setFixedHeight(25);
            checkBoxCell.setPadding(8f);
            checkBoxCell.setBorderWidth(0.5f);
            checkBoxCell.setBorderColor(SUB_COLOR_3);
            checkBoxCell.setBackgroundColor(WHITE);

            // 체크박스 폼필드 추가
            RadioCheckField checkBox = new RadioCheckField(
                    writer,
                    new Rectangle(0, 0, 18, 18), // 체크박스 크기 증가
                    "chk_" + index,
                    "Yes"
            );
            checkBox.setCheckType(RadioCheckField.TYPE_CHECK);
            checkBox.setBorderWidth(BaseField.BORDER_WIDTH_MEDIUM);
            checkBox.setBorderColor(MAIN_COLOR);              // 메인 색상으로 테두리
            checkBox.setBackgroundColor(Color.WHITE);

            PdfFormField field = checkBox.getCheckField();
            writer.addAnnotation(field);

            checkBoxCell.addElement(new Phrase(" "));
            return checkBoxCell;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // [*] 푸터 추가
    private void addFooter(Document document) throws DocumentException {
        // 푸터 구분선
        PdfPTable separatorTable = new PdfPTable(1);
        separatorTable.setWidthPercentage(100);
        separatorTable.setSpacingBefore(30f);
        PdfPCell separatorCell = new PdfPCell();
        separatorCell.setFixedHeight(1f);
        separatorCell.setBackgroundColor(SUB_COLOR_3);
        separatorCell.setBorder(Rectangle.NO_BORDER);
        separatorTable.addCell(separatorCell);
        document.add(separatorTable);

        // 푸터 텍스트
        Paragraph footer = new Paragraph("Generated by Five Minutes Project Management System",
                new Font(bodyFont.getBaseFont(), 10, Font.ITALIC, SUB_COLOR_2));
        footer.setAlignment(Element.ALIGN_CENTER);
        footer.setSpacingBefore(10f);
        document.add(footer);
    }

    // [1] 전체 근무 리스트 PDF 생성 (개선된 버전)
    public void generateAllPerPdf(List<DashboardDto> performances, OutputStream outputStream)
            throws DocumentException {
        try {
            // PDF 문서 속성 (여백 조정)
            Document document = new Document(PageSize.A4, 40, 40, 50, 50);
            PdfWriter writer = PdfWriter.getInstance(document, outputStream);
            document.open();

            // 헤더 섹션 추가
            addHeaderSection(document);

            // 생성일 정보 추가
            Paragraph dateInfo = new Paragraph("생성일: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm")),
                    new Font(bodyFont.getBaseFont(), 10, Font.NORMAL, SUB_COLOR_1));
            dateInfo.setAlignment(Element.ALIGN_RIGHT);
            dateInfo.setSpacingAfter(15f);
            document.add(dateInfo);

            // DateTime 포맷터들
            DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm"); // 연월일 시간:분 형식

            // 시간 순으로 정렬 (DateTime 기준)
            performances.sort(Comparator.comparing(dto -> {
                String startTimeStr = dto.getPjPerDto().getPfStart();
                if (startTimeStr != null && !startTimeStr.isEmpty()) {
                    try {
                        return LocalDateTime.parse(startTimeStr, inputFormatter);
                    } catch (DateTimeParseException e) {
                        return LocalDateTime.MIN;
                    }
                }
                return LocalDateTime.MIN; // null인 경우 가장 앞으로
            }));

            // 메인 테이블 생성
            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 1.8f, 2.5f, 4.2f, 2.8f, 2.8f, 1.3f}); // 비율 조정
            table.setSpacingBefore(10f);

            // 테이블 헤더
            addTableHeader(table, "No", "근무자", "역할명", "체크리스트명", "시작시간", "종료시간", "체크");

            // 테이블 바디
            int index = 1;
            for (DashboardDto dto : performances) {
                // 줄무늬 효과를 위한 배경색 설정
                Color rowColor = (index % 2 == 0) ? SUB_COLOR_4 : WHITE;

                // 각 셀 생성 및 배경색 적용
                PdfPCell noCell = createBodyCell(String.valueOf(index));
                noCell.setBackgroundColor(rowColor);
                table.addCell(noCell);

                PdfPCell nameCell = createBodyCell(dto.getUsersDto().getUserName());
                nameCell.setBackgroundColor(rowColor);
                table.addCell(nameCell);

                PdfPCell roleCell = createBodyCell(dto.getPjWorkerDto().getPjRoleName());
                roleCell.setBackgroundColor(rowColor);
                table.addCell(roleCell);

                PdfPCell titleCell = createBodyCell(dto.getPjCheckDto().getPjChklTitle());
                titleCell.setBackgroundColor(rowColor);
                titleCell.setHorizontalAlignment(Element.ALIGN_LEFT); // 제목은 왼쪽 정렬
                titleCell.setPaddingLeft(10f);
                table.addCell(titleCell);

                // DateTime을 연월일 시간:분 형태로 포맷팅
                String startTime = "";
                String endTime = "";

                try {
                    if (dto.getPjPerDto().getPfStart() != null && !dto.getPjPerDto().getPfStart().isEmpty()) {
                        LocalDateTime startDateTime = LocalDateTime.parse(dto.getPjPerDto().getPfStart(), inputFormatter);
                        startTime = startDateTime.format(outputFormatter);
                    }

                    if (dto.getPjPerDto().getPfEnd() != null && !dto.getPjPerDto().getPfEnd().isEmpty()) {
                        LocalDateTime endDateTime = LocalDateTime.parse(dto.getPjPerDto().getPfEnd(), inputFormatter);
                        endTime = endDateTime.format(outputFormatter);
                    }
                } catch (DateTimeParseException e) {
                    System.out.println("날짜 파싱 오류: " + e.getMessage());
                    // 파싱 실패시 빈 문자열 유지
                }

                PdfPCell startCell = createBodyCell(startTime);
                startCell.setBackgroundColor(rowColor);
                table.addCell(startCell);

                PdfPCell endCell = createBodyCell(endTime);
                endCell.setBackgroundColor(rowColor);
                table.addCell(endCell);

                // 체크박스 셀
                PdfPCell checkboxCell = createCheckboxCell(writer, index);
                checkboxCell.setBackgroundColor(rowColor);
                table.addCell(checkboxCell);

                index++;
            }

            document.add(table);

            // 통계 정보 추가
            addSummarySection(document, performances.size());

            // 푸터 추가
            addFooter(document);

            document.close();
        } catch (Exception e) {
            System.out.println("PDF 생성 오류: " + e.getMessage());
            e.printStackTrace();
        }
    } // func end

    // [*] 요약 정보 섹션 추가
    private void addSummarySection(Document document, int totalCount) throws DocumentException {
        PdfPTable summaryTable = new PdfPTable(3);
        summaryTable.setWidthPercentage(100);
        summaryTable.setWidths(new float[]{1, 1, 1});
        summaryTable.setSpacingBefore(25f);

        // 요약 정보 헤더
        PdfPCell summaryHeader = new PdfPCell(new Paragraph("요약 정보", subtitleFont));
        summaryHeader.setColspan(3);
        summaryHeader.setHorizontalAlignment(Element.ALIGN_CENTER);
        summaryHeader.setBackgroundColor(SUB_COLOR_1);
        summaryHeader.setPadding(10f);
        summaryHeader.setBorder(Rectangle.NO_BORDER);
        summaryTable.addCell(summaryHeader);

        // 통계 정보
        addSummaryCell(summaryTable, "총 작업 수", String.valueOf(totalCount) + "개");
        addSummaryCell(summaryTable, "생성 일시", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
        addSummaryCell(summaryTable, "문서 형식", "PDF");

        document.add(summaryTable);
    }

    // [*] 요약 셀 추가
    private void addSummaryCell(PdfPTable table, String label, String value) {
        PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(5f);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);

        Paragraph content = new Paragraph();
        content.add(new Chunk(label + "\n", new Font(bodyFont.getBaseFont(), 10, Font.BOLD, SUB_COLOR_1)));
        content.add(new Chunk(value, new Font(bodyFont.getBaseFont(), 11, Font.NORMAL, MAIN_COLOR)));

        cell.addElement(content);
        table.addCell(cell);
    }

} // class end