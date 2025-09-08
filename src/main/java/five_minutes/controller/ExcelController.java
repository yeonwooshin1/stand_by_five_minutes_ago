package five_minutes.controller;

import five_minutes.service.ExcelService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * **info**========================
 * <p> Apache POI 라이브러리를 활용한 엑셀처리
 * @author OngTK
 */

@RestController
@RequiredArgsConstructor
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;

    // 엑셀 출력 func
    @GetMapping("/download")
    public void downloadProjcetExcel(@RequestParam int pjNo, HttpServletResponse response, HttpSession session){
        System.out.println("ExcelController.downloadProjcetExcel");
        System.out.println("pjNo = " + pjNo + ", response = " + response);

        String bnNo = (String) session.getAttribute("loginBnNo");
        excelService.generateExcel(pjNo, response, bnNo);
    } // fucn end

} // class end