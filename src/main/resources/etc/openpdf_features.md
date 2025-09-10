# OpenPDF 라이브러리 주요 기능 가이드

## 1. OpenPDF란?

OpenPDF는 순수 자바(Java)로 작성된 PDF 문서 생성 및 수정을 위한 라이브러리입니다. 과거 iText 라이브러리의 오픈소스 버전(LGPL)에서 파생되었으며, 상업적 이용에도 제약이 없는 라이선스를 따릅니다.

## 2. 핵심 클래스 및 개념

PDF 문서를 만드는 과정은 주로 아래의 핵심 클래스들을 조합하여 이루어집니다.

| 클래스 | 설명 |
| --- | --- |
| `Document` | PDF 문서 전체를 나타내는 객체입니다. 페이지 크기, 여백 등을 설정할 수 있습니다. |
| `PdfWriter` | `Document` 객체에 담긴 내용을 실제 파일 또는 스트림(Stream)으로 써주는 역할을 합니다. |
| `Paragraph` | 하나의 문단(텍스트 덩어리)을 나타냅니다. 가장 기본적인 내용 단위입니다. |
| `Font` | 텍스트의 글꼴(체, 크기, 스타일, 색상)을 정의합니다. |
| `BaseFont` | TTF, OTF 등 외부 폰트 파일을 시스템에 로드하여 `Font` 객체를 생성할 수 있도록 합니다. **한글 처리의 핵심**입니다. |
| `PdfPTable` | PDF 문서에 표를 생성하는 데 사용됩니다. |
| `PdfPCell` | `PdfPTable`을 구성하는 각각의 셀(칸)을 의미합니다. 셀 안에 `Paragraph`나 `Image` 등을 넣을 수 있습니다. |
| `Image` | 문서에 이미지를 추가할 때 사용합니다. 파일 경로, URL, byte 배열 등으로부터 이미지를 로드할 수 있습니다. |

## 3. 기본 사용법 및 순서

1.  **`Document` 객체 생성**: `Document document = new Document();`
2.  **`PdfWriter` 생성**: `PdfWriter.getInstance(document, outputStream);`
3.  **`Document` 열기**: `document.open();`
4.  **폰트 설정 (한글 처리)**:
    - `BaseFont baseFont = BaseFont.createFont("폰트경로/font.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);`
    - `Font koreanFont = new Font(baseFont, 12);`
5.  **내용 추가**:
    - `document.add(new Paragraph("텍스트 내용", koreanFont));`
    - `document.add(new PdfPTable(컬럼수));`
    - `document.add(Image.getInstance("이미지경로/logo.png"));`
6.  **`Document` 닫기**: `document.close();`

## 4. 주요 기능별 예제 코드

### 4.1. 한글 텍스트 추가

```java
BaseFont baseFont = BaseFont.createFont("font/NotoSansKR-Regular.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
Font bodyFont = new Font(baseFont, 12);
document.add(new Paragraph("안녕하세요, OpenPDF!", bodyFont));
```

### 4.2. 테이블(표) 생성

```java
PdfPTable table = new PdfPTable(2);
table.addCell(new PdfPCell(new Paragraph("항목", headerFont)));
table.addCell(new PdfPCell(new Paragraph("내용", headerFont)));
table.addCell(new PdfPCell(new Paragraph("이름", bodyFont)));
table.addCell(new PdfPCell(new Paragraph("홍길동", bodyFont)));
document.add(table);
```

### 4.3. 이미지 추가

```java
// 1. 파일 경로 또는 URL로부터 Image 객체 생성
Image logo = Image.getInstance("src/main/resources/static/img/logo.png");

// 2. 이미지 크기 조절 (가로 120px, 세로 60px에 맞춤)
logo.scaleToFit(120, 60);

// 3. 정렬 (왼쪽 정렬)
logo.setAlignment(Element.ALIGN_LEFT);

// 4. Document에 이미지 추가
document.add(logo);
```
