# OpenPDF 라이브러리 가이드

Java 애플리케이션에서 PDF 문서를 생성하고 수정하는 방법을 안내합니다.

---

### 1. OpenPDF란?

OpenPDF는 순수 Java로 작성된 오픈소스 라이브러리로, PDF 문서를 프로그래밍 방식으로 생성하고 편집할 수 있는 기능을 제공합니다.

과거에 널리 사용되었던 **iText** 라이브러리의 오래된 버전(iText 4)에서 파생된 포크(fork) 프로젝트입니다. iText가 상업용 라이선스로 변경된 후, OpenPDF는 MPL(Mozilla Public License) 및 LGPL(GNU Lesser General Public License)에 따라 무료로 사용할 수 있는 대안으로 계속 발전하고 있습니다.

### 2. 사용하는 이유

- **동적 PDF 생성:** 사용자의 데이터나 시스템 상태에 따라 보고서, 인보이스, 티켓, 증명서 등 다양한 PDF 문서를 동적으로 생성할 수 있습니다.
- **무료 라이선스:** 상업용 프로젝트에서도 라이선스 비용 부담 없이 자유롭게 사용할 수 있습니다.
- **풍부한 기능:** 텍스트, 이미지, 표, 차트 추가는 물론, 기존 PDF 수정, 암호화, 워터마크 추가 등 다양한 고급 기능을 지원합니다.

---

### 3. 설정 방법 (Maven/Gradle)

프로젝트의 빌드 파일에 아래와 같이 OpenPDF 의존성을 추가합니다.

**Maven (`pom.xml`)**
```xml
<dependency>
    <groupId>com.github.librepdf</groupId>
    <artifactId>openpdf</artifactId>
    <version>1.3.30</version> <!-- 최신 버전은 Maven Central에서 확인 -->
</dependency>
```

**Gradle (`build.gradle`)**
```groovy
implementation 'com.github.librepdf:openpdf:1.3.30' // 최신 버전은 Maven Central에서 확인
```

---

### 4. 사용 방법 - 간단한 PDF 생성하기

가장 기본적인 "Hello, OpenPDF!" 문구와 한글을 포함하는 PDF 파일을 생성하는 예제입니다.

**⚠️ 중요:** 한글과 같은 다국어 문자를 PDF에 쓰기 위해서는 해당 문자를 지원하는 폰트 파일을 시스템에 가지고 있거나, 프로젝트 리소스에 포함시켜야 합니다. 아래 예제는 Windows의 `malgun.ttf`(맑은 고딕) 폰트를 사용하는 것을 가정합니다.

`SimplePdfGenerator.java`
```java
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfWriter;

import java.io.FileOutputStream;
import java.io.IOException;

public class SimplePdfGenerator {

    public static void main(String[] args) {
        // 1. Document 객체 생성
        Document document = new Document();
        String outputPath = "HelloWorld.pdf";

        try {
            // 2. PdfWriter를 사용하여 Document를 파일 스트림에 연결
            PdfWriter.getInstance(document, new FileOutputStream(outputPath));

            // 3. Document 열기
            document.open();

            // 4. 폰트 설정 (영문 기본 폰트)
            Font defaultFont = new Font(Font.HELVETICA, 12, Font.NORMAL);
            
            // 5. 한글을 위한 폰트 설정
            // C:/Windows/Fonts/malgun.ttf 경로에 맑은 고딕 폰트가 있어야 함
            // 다른 OS나 환경에서는 폰트 경로를 맞게 수정해야 함
            BaseFont baseFont = BaseFont.createFont("C:/Windows/Fonts/malgun.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
            Font koreanFont = new Font(baseFont, 12);

            // 6. 내용 추가 (Paragraph 객체 사용)
            document.add(new Paragraph("Hello, OpenPDF!", defaultFont));
            document.add(new Paragraph("안녕하세요! OpenPDF를 사용한 한글 PDF 생성 예제입니다.", koreanFont));

            System.out.println("'" + outputPath + "' 파일이 성공적으로 생성되었습니다.");

        } catch (DocumentException | IOException e) {
            e.printStackTrace();
        } finally {
            // 7. Document 닫기 (필수)
            if (document.isOpen()) {
                document.close();
            }
        }
    }
}
```

#### 실행 결과
위 Java 코드를 실행하면 프로젝트 루트 디렉토리에 `HelloWorld.pdf` 파일이 생성됩니다. 파일을 열면 영문과 한글 텍스트가 정상적으로 표시되는 것을 확인할 수 있습니다.

---

### 5. 주요 기능 및 추가 정보

OpenPDF는 단순 텍스트 추가 외에도 다양한 기능을 제공합니다.

- **이미지 추가:** `Image` 클래스를 사용하여 JPG, PNG 등의 이미지를 PDF에 삽입할 수 있습니다.
- **표 추가:** `PdfPTable`과 `PdfPCell` 클래스를 사용하여 복잡한 구조의 표를 만들 수 있습니다. 데이터 리포트 생성에 매우 유용합니다.
- **헤더 및 푸터:** `PdfPageEventHelper`를 구현하여 모든 페이지에 페이지 번호, 로고, 날짜 등 공통적인 헤더나 푸터를 추가할 수 있습니다.
- **암호화:** `PdfWriter.setEncryption()` 메소드를 사용하여 PDF를 암호로 보호할 수 있습니다.

더 자세한 기능과 예제는 [OpenPDF 공식 Wiki](https://github.com/LibrePDF/OpenPDF/wiki)에서 확인할 수 있습니다.
