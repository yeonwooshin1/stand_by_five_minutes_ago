package five_minutes.service;

import five_minutes.model.dto.PjDto;
import five_minutes.model.dto.ProjectPerformDto;
import five_minutes.model.dto.ProjectWorkerDto;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
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
                List<ProjectWorkerDto> workers = projectWorkerService.getAllPJWorker(pjNo);
                Sheet sheet2 = workbook.getSheetAt(1);
                writeWorkers(sheet2, workers);

                // 세 번째 시트 작성
                List<ProjectPerformDto> performs = projectPerformService.readAllforExcel(pjNo);
                Sheet sheet3 = workbook.getSheetAt(2);
                writePerforms(sheet3, performs);

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
        sheet.getRow(4).getCell(1).setCellValue(pjDto.getRoadAddress() + " " + pjDto.getDetailAddress());   // B5 - 장소
        sheet.getRow(5).getCell(1).setCellValue(pjDto.getPjMemo());                                         // B6 - 메모
        //클라이언트 정보
        sheet.getRow(7).getCell(1).setCellValue(pjDto.getClientName());                                     // B8 - 클라이언트 명
        sheet.getRow(8).getCell(1).setCellValue(pjDto.getClientRepresent());                                // B9 - 담당자
        sheet.getRow(9).getCell(1).setCellValue(pjDto.getClientPhone());                                    // B10 - 연락처
        sheet.getRow(10).getCell(1).setCellValue(pjDto.getClientMemo());                                    // B11 - 요청사항
    } // func end

    // [03] 인력관리 시트 작성
    private void writeWorkers(Sheet sheet, List<ProjectWorkerDto> workers) {
        int rowIdx = 2;
        for (ProjectWorkerDto dto : workers) {
            Row row = sheet.getRow(rowIdx++);                                // 3행부터 작성
            row.getCell(0).setCellValue(dto.getPjRoleName());               // A열 - 역할명
            row.getCell(1).setCellValue(dto.getUserName());                 // B열 - 이름
            row.getCell(2).setCellValue(dto.getUserPhone());                // C열 - 연락처
            row.getCell(3).setCellValue(dto.getRoadAddress());              // D열 - 주소
            String lv = dto.getPjRoleLv() == 1 ? "입문자" : dto.getPjRoleLv() == 2 ? "초급" : dto.getPjRoleLv() == 3 ? "중급" : dto.getPjRoleLv() == 4 ? "상급" : "전문가";
            row.getCell(4).setCellValue(lv);                                // E열 - 숙련도
            row.getCell(5).setCellValue(dto.getCreateDate());               // F열 - 작성일
            row.getCell(6).setCellValue(dto.getUpdateDate());               // G열 - 수정일
        }
    } // func end

    // [04] 업무관리 시트 작성
    private void writePerforms(Sheet sheet, List<ProjectPerformDto> performs) {
        System.out.println("ExcelService.writePerforms");
        System.out.println("sheet = " + sheet + ", performs = " + performs);
        int rowIdx = 1;
        int index = 1;
        for (ProjectPerformDto dto : performs) {
            Row row = sheet.getRow(rowIdx++);                           // 2행부터 작성
            row.getCell(0).setCellValue(index);                         // A열 - No
            index++;
            row.getCell(1).setCellValue(dto.getPfStart());              // B열 - 시작시간
            row.getCell(2).setCellValue(dto.getPfEnd());                // C열 - 종료시간
            row.getCell(3).setCellValue(dto.getPjRoleName());           // D열 - 역할명
            row.getCell(4).setCellValue(dto.getUserName());             // E열 - 근무자
            int status =dto.getPfStatus();
            String pfStatus = status == 1 ? "시작전" : status == 2 ? "진행중" : status == 3 ? "완료" : status == 4 ? "취소" : "보류";
            row.getCell(5).setCellValue(pfStatus);                      // F열 - 수행여부
            row.getCell(6).setCellValue(dto.getNote());                 // G열 - 비고
            row.getCell(7).setCellValue(dto.getCreateDate());           // H열 - 작성일
            row.getCell(8).setCellValue(dto.getUpdateDate());           // I열 - 수정일
        }
    } // func end
} // class end