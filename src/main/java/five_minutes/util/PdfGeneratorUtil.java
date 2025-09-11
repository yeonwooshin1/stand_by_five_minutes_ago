package five_minutes.util;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import five_minutes.model.dto.DashboardDto;
import five_minutes.model.dto.PjDto;
import org.apache.jasper.tagplugins.jstl.core.Out;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalTime;
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

    // [*] 멤버변수
    private Font titleFont;
    private Font headFont;
    private Font bodyFont;

    // [*] 생성자에서 폰트 초기화
    public PdfGeneratorUtil() {
        try {
            // [*] 폰트 스타일 지정. noto sans kr가 한글 영문을 모두 지원하므로, 하나로 통일합니다.
            BaseFont baseFont = BaseFont.createFont("/font/NotoSansKR-VariableFont_wght.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            this.titleFont = new Font(baseFont , 18, Font.BOLD);
            this.headFont = new Font(baseFont, 12, Font.BOLD);
            this.bodyFont = new Font(baseFont , 11);
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    } // func end

    // [*] 테이블 헤더 만들기
    private void addTableHeader(PdfPTable table, String... headers) {
        for (String header : headers) {
            PdfPCell cell = new PdfPCell(new Paragraph(header, headFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER); // 가로 가운데
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);   // 세로 가운데
            cell.setBackgroundColor(new Color(230, 230, 250)); // 연보라색 배경
            cell.setPadding(5f);
            table.addCell(cell);
        }
    }

    // [*] 대시보드 상태 표현
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

    // [*] 테이블 열 표현
    private void addDetailRow(PdfPTable table, String label, String value) {
        PdfPCell labelCell = new PdfPCell(new Paragraph(label, headFont));
        PdfPCell valueCell = new PdfPCell(new Paragraph(value, bodyFont));
        table.addCell(labelCell);
        table.addCell(valueCell);
    }

    // [*] 가운데 정렬용 셀 생성 함수
    private PdfPCell createBodyCell(String text) {
        PdfPCell cell = new PdfPCell(new Paragraph(text, bodyFont));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setPadding(5f);
        return cell;
    }

//    // [0] 개인별 시간 순 체크리스트 PDF 생성
//    public void generatePersonalChecklistPdf(PjDto projectInfo, String workerName, List<DashboardDto> performances, OutputStream outputStream) throws DocumentException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, outputStream);
//        document.open();
//
//        // 제목
//        Paragraph title = new Paragraph(projectInfo.getPjName() + " - 개인별 업무 리스트", titleFont);
//        title.setAlignment(Element.ALIGN_CENTER);
//        document.add(title);
//        document.add(new Paragraph("근무자: " + workerName, headFont));
//        document.add(new Paragraph(" ")); // 공백
//
//        // 업무 테이블
//        PdfPTable table = new PdfPTable(5); // 5 Columns
//        table.setWidthPercentage(100);
//        table.setWidths(new float[]{3, 3, 4, 5, 2});
//
//        // 테이블 헤더
//        addTableHeader(table, "시작시간", "종료시간", "역할명", "체크리스트명", "상태");
//
//        // 테이블 바디 (Service에서 시간순으로 정렬된 performances 리스트를 전달해야 함)
//        for (DashboardDto dto : performances) {
//            table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfStart(), bodyFont)));
//            table.addCell(new PdfPCell(new Paragraph(dto.getPjPerDto().getPfEnd(), bodyFont)));
//            table.addCell(new PdfPCell(new Paragraph(dto.getPjWorkerDto().getPjRoleName(), bodyFont)));
//            table.addCell(new PdfPCell(new Paragraph(dto.getPjCheckDto().getPjChklTitle(), bodyFont)));
//            table.addCell(new PdfPCell(new Paragraph(getStatusString(dto.getPjPerDto().getPfStatus()), bodyFont)));
//        }
//
//        document.add(table);
//        document.close();
//    }
    
//    // [1] 프로젝트 기본 정보 PDF 생성
//    public void generateInfoPdf(DashboardDto info , OutputStream outputStream) throws DocumentException {
//        Document document = new Document(PageSize.A4);
//        PdfWriter.getInstance(document, outputStream);
//        document.open();
//
//        document.add(new Paragraph("프로젝트 기본정보" , titleFont));
//        document.add(new Paragraph(" ")); // 공백 한 줄
//
//
//    }
//
    // [2] 전체 근무 리스트 PDF 생성
    public void generateAllPerPdf(List<DashboardDto> performances , OutputStream outputStream) throws DocumentException {
        try {
            // PDF 문서 속성
            
            Document document = new Document(PageSize.A4);
            PdfWriter.getInstance(document, outputStream);
            document.open();

            // 이미지 생성
            Image logo = Image.getInstance("src/main/resources/static/img/logoNonText.png");
            logo.scaleAbsolute(50 , 50);
            logo.setAlignment(Element.ALIGN_RIGHT); // 오른쪽 정렬

            // 타이틀
            Paragraph title = new Paragraph("프로젝트 타임라인", titleFont);
            title.setAlignment(Element.ALIGN_CENTER); // 가운데 정렬
            document.add(title);
            document.add(new Paragraph(" ")); // 공백 한 줄
            document.add(logo);

            // 시간 순으로 출력
            performances.sort(Comparator.comparing(dto -> LocalTime.parse(dto.getPjPerDto().getPfStart())));

            PdfPTable table = new PdfPTable(7);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{1, 2, 2, 5, 3, 3, 2}); // td가 차지하는 길이
            table.setSpacingBefore(10f); // 장평

            // 테이블 헤더
            addTableHeader(table, "No", "근무자", "역할명", "체크리스트명", "시작시간", "종료시간", "상태");

            // 테이블 바디
            int index = 1;
            for (DashboardDto dto : performances) {
                table.addCell(createBodyCell(String.valueOf(index++)));
                table.addCell(createBodyCell(dto.getUsersDto().getUserName()));
                table.addCell(createBodyCell(dto.getPjWorkerDto().getPjRoleName()));
                table.addCell(createBodyCell(dto.getPjCheckDto().getPjChklTitle()));
                table.addCell(createBodyCell(dto.getPjPerDto().getPfStart()));
                table.addCell(createBodyCell(dto.getPjPerDto().getPfEnd()));
                table.addCell(createBodyCell(getStatusString(dto.getPjPerDto().getPfStatus())));
            }
            document.add(table);
            document.close();
        } catch (Exception e){
            System.out.println(e);
        }
    } // func end
//
    // [3] 개별 근무 정보 PDF 생성
//    public void generateDetailPerPdf(DashboardDto performance , OutputStream outputStream) throws DocumentException {
//        try {
//            Document document = new Document(PageSize.A4);
//            PdfWriter.getInstance(document, outputStream);
//            document.open();
//
//            // 이미지 생성
//            Image logo = Image.getInstance("src/main/resources/static/img/logo.png");
//            logo.scaleAbsolute(120, 60);
//            logo.setAlignment(Element.ALIGN_LEFT); // 왼쪽 정렬
//
//            document.add(new Paragraph("근무 정보 상세보기", titleFont));
//            document.add(new Paragraph(" "));
//            document.add(logo);
//
//            // 테이블을 사용하여 깔끔하게 표시
//            PdfPTable table = new PdfPTable(2);
//            table.setWidthPercentage(80);
//            table.setWidths(new float[]{1, 3});
//
//            addDetailRow(table, "근무자", performance.getUsersDto().getUserName());
//            addDetailRow(table, "역할", performance.getPjWorkerDto().getPjRoleName());
//            addDetailRow(table, "체크리스트", performance.getPjCheckDto().getPjChklTitle());
//            addDetailRow(table, "시작시간", performance.getPjPerDto().getPfStart());
//            addDetailRow(table, "종료시간", performance.getPjPerDto().getPfEnd());
//            addDetailRow(table, "상태", getStatusString(performance.getPjPerDto().getPfStatus()));
//            addDetailRow(table, "메모", performance.getPjPerDto().getNote());
//
//            document.add(table);
//            document.close();
//        } catch (Exception e){
//            System.out.println(e);
//        }
//    }



} // class end
