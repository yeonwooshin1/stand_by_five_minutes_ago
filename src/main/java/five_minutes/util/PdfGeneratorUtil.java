package five_minutes.util;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PdfGeneratorUtil {
    public static void main(String[] args) {
        // 1. Document 객체를 생성합니다.
        Document document = new Document();
        // 2. 파일명을 정의합니다.
        String outputPath = "HelloWorld.pdf";
        try {
            // 3. PdfWriter로 Document를 파일 스트림에 연결합니다
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));
            // 4. Document를 엽니다.
            document.open();
            // 5. 영문 폰트는 기본으로 설정합니다. (5번 따라하면 커스텀 가능)
            // Font defaultFont = new Font(Font.HELVETICA, 12 ,Font.NORMAL);
            // 6. 한글 폰트를 쓰고 싶다면, 폰트가 있는 경로에 폰트 파일을 배치
            // * noto sans kr가 한글 영문을 모두 지원하므로, 하나로 통일합니다.
            BaseFont baseFont = BaseFont.createFont("/font/NotoSansKR-VariableFont_wght.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font font = new Font(baseFont, 12);
            // 7. 내용 추가
            document.add(new Paragraph("Hello, OpenPDF!", font));
            document.add(new Paragraph("안녕하세요. OpenPDF를 사용한 한글 PDF 생성 예제입니다.", font));
            System.out.println("'" + outputPath + "' 파일이 성공적으로 생성되었습니다.");

        } catch (Exception e) {
            System.out.println(e);
        } finally {
            // 8. Document 닫기
            if (document.isOpen()) {
                document.close();
            }
        }

    }

}
