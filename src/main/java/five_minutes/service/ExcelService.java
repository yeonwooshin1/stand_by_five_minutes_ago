package five_minutes.service;

import five_minutes.model.dto.PjDto;
import five_minutes.model.dto.ProjectPerformDto;
import five_minutes.model.dto.ProjectWorkerDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * **info**========================
 * <p> Apache POI 라이브러리를 활용한 엑셀처리
 *
 * @author OngTK
 */

@Service
@RequiredArgsConstructor
public class ExcelService {

    private final PjService pjService;
    private final ProjectWorkerService projectWorkerService;
    private final ProjectPerformService projectPerformService;
    private final ResourceLoader resourceLoader;

    // 엑셀 작성 및 다운로드
    public void generateExcel(int pjNo, HttpServletResponse response, String bnNo) {

        try {
            // 1. 템플릿 파일 로드
            Resource template = resourceLoader.getResource("classpath:excel/프로젝트 템플릿.xlsx ");
            File tempFile = File.createTempFile("excel_", ".xlsx");
            Files.copy(template.getInputStream(), tempFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

            // 2. 프로젝트 정보 조회
            PjDto pjDto = pjService.read(pjNo, bnNo);
            String pjName = pjDto.getPjName();

            // 3. 파일명 생성 ( ex: yyyyMMdd_HHmmss_pjName.xlsx )
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = timestamp + "_" + pjName + ".xlsx";
            // 한글파일명 인코딩
            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8).replaceAll("\\+", "%20");
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + encodedFileName);

            // 4. 엑셀 수정
            try (FileInputStream fis = new FileInputStream(tempFile);
                 Workbook workbook = new XSSFWorkbook(fis)) {

                // 첫 번째 시트 작성 - 프로젝트 정보
                Sheet sheet1 = workbook.getSheetAt(0);
                writeProjectInfo(sheet1, pjDto);

                // 두 번째 시트 작성
//                List<ProjectWorkerDto> workers = projectWorkerService.getAllPJWorker(pjNo);
//                Sheet sheet2 = workbook.getSheetAt(1);
//                writeWorkers(sheet2, workers);

                // Todo OngTk - 연우님 기능 완성 후 연결
//                // 세 번째 시트 작성
//                List<ProjectPerformDto> performs = projectPerformService.findByProject(pjNo);
//                Sheet sheet3 = workbook.getSheetAt(2);
//                writePerforms(sheet3, performs);

                // 5. 다운로드 응답
                ServletOutputStream out = response.getOutputStream();
                workbook.write(out);
                out.flush();
                out.close();
                response.getOutputStream().flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("엑셀 생성 중 오류 발생", e);
        }
    } // func end

    // [02] 프로젝트 정보 시트 작성
    private void writeProjectInfo(Sheet sheet, PjDto pjDto) {
        System.out.println("ExcelService.writeProjectInfo");
        System.out.println("sheet = " + sheet + ", pjDto = " + pjDto);

        // 프로젝트 기본정보
        sheet.getRow(1).getCell(1).setCellValue(pjDto.getPjName());                                         // B2 - 프로젝트명
        sheet.getRow(2).getCell(1).setCellValue(pjDto.getPjStartDate());                                    // B3 - 시작일
        sheet.getRow(3).getCell(1).setCellValue(pjDto.getPjEndDate());                                      // B4 - 종료일
        sheet.getRow(4).getCell(1).setCellValue(pjDto.getRoadAddress() + " "+ pjDto.getDetailAddress() );   // B5 - 장소
        sheet.getRow(5).getCell(1).setCellValue(pjDto.getPjMemo());                                         // B6 - 메모
        //클라이언트 정보
        sheet.getRow(7).getCell(1).setCellValue(pjDto.getClientName());                                     // B8 - 클라이언트 명
        sheet.getRow(8).getCell(1).setCellValue(pjDto.getClientRepresent());                                // B9 - 담당자
        sheet.getRow(9).getCell(1).setCellValue(pjDto.getClientPhone());                                    // B10 - 연락처
        sheet.getRow(10).getCell(1).setCellValue(pjDto.getClientMemo());                                    // B11 - 요청사항
    } // func end

//    // [03] 인력관리 시트 작성
//    private void writeWorkers(Sheet sheet, List<ProjectWorkerDto> workers) {
//        int rowIdx = 2;
//        for (ProjectWorkerDto worker : workers) {
//            Row row = sheet.createRow(rowIdx++);
//            row.createCell(0).setCellValue(worker.getName());
//            row.createCell(1).setCellValue(worker.getRole());
//            // ... 필요한 정보 추가
//        }
//    }

    // [04] 업무관리 시트 작성
//    private void writePerforms(Sheet sheet, List<ProjectPerformDto> performs) {
//        int rowIdx = 1;
//        for (ProjectPerformDto perform : performs) {
//            Row row = sheet.createRow(rowIdx++);
//            row.createCell(0).setCellValue(perform.getTask());
//            row.createCell(1).setCellValue(perform.getStatus());
//            // ... 필요한 정보 추가
//        }
//    }


} // class end