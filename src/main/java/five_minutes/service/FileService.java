package five_minutes.service;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Objects;
import java.util.UUID;

// 파일 서비스
@Service // 해당 클래스를 스프링 컨테이너의 빈 등록
public class FileService {

    // [*] 업로드 경로
    // 방법1) 프로젝트내 src : 개발자가 코드를 작성하는 폴더
    // 방법2) 프로젝트내 build : 서버 실행 했을때 컴파일된 코드 즉] 서버(24시간) 의 갖는 실행 된 폴더
    // 1. 현재 프로젝트의 최상위 디렉토리(폴더) 경로 찾기
    private String baseDir = System.getProperty("user.dir");
    // 2. 방법2 처럼 개발자폴더가 아닌 실행된 서버의 폴더로 업로드 경로 지정하기 , *개발환경* 에 따라 달라진다.
    // 체크리스트 경로
    private String uploadPathChk = baseDir + "/build/resources/main/static/upload/check/";
    // 사업자등록증 경로
    private String uploadPathBn = baseDir + "/build/resources/main/static/upload/bnDocu/";

    // [1] 파일 업로드 : 스프링에서는 MultipartFile 인터페이스 지원( 대용량 바이트 조작 )
    public String fileUpload(int type, MultipartFile multipartFile) {
        // * type 이 1 이면 사업자번호 , 2이면 체크리스트이다. 나머지는 오류고, 나중에 다른 경로 추가를 위해 남겨둠
        // uploadPath 선언
        String uploadPath;
        if (type == 1) {
            uploadPath = uploadPathBn;  // type이 1이면 사업자번호 경로
        } else if (type == 2) {
            uploadPath = uploadPathChk; // type이 2이면 체크리스트 경로
        } else uploadPath = null;   // 이건 오류

        // 1. 업로드한 파일명이 중복일때, 다른 파일 이지만 파일명은 같을 수 있다.
        // 방법1) 파일명 앞에 PK번호를 붙인다.
        // 방법2) 파일명 앞에 날짜/시간 붙인다.
        // 방법3) UUID(네트워크식별번호) 라이브러리 사용한다. UUID 란? 난수 문자열 생성 (고유성 보장)
        // (1) 방법3 처럼 UUID를 생성한다.
        String uuid = UUID.randomUUID().toString();
        // (2) 업로드된 파일명과 합치기 , .getOriginalFilename(); : 업로드된 파일명
        // - 업로드된 파일명에 _언더바가 존재하면 모두 -하이픈 변경 , 예] 짱구_사진.jpg -> 짱구-사진.jpg
        // 이유 : uuid 와 파일명 구분을 _언더바 사용하기 위한 내부적인 규칙, 예상 : 9b27988a-deba-40e0-b77e-6a155418db35 _ 짱구-사진.jpg  , UUID_파일명
        // .replaceAll( "기존문자" , "새로운문자") : 기존문자를 새로운문자로 변환 메소드
        String fileName = uuid + "_" + multipartFile.getOriginalFilename().replaceAll("_", "-");
        // 2. 업로드 경로 와 파일명 합치기
        String uploadFilePath = uploadPath + fileName;
        // 3. 만약에 업로드한 경로가 upload 폴더가 존재하지 않으면 폴더생성
        File pathFile = new File(Objects.requireNonNull(uploadPath)); // File 클래스 : 다양한 파일/폴더 함수를 제공
        if (!pathFile.exists()) { // .exists() : 지정한 경로가 존재하면 true 존재하지 않으면 false 반환 메소드
            pathFile.mkdir(); // .mkdir() : 지정한 경로 생성 메소드
        }
        // 4. 업로드 할 파일의 경로를 file 클래스 생성
        File uploadFile = new File(uploadFilePath);
        // 5. 업로드( 파일/바이트 이동) 하기, .transferTo( file객체 ) : 지정한 file객체의 경로로 업로드 파일을 이동한다.
        try {
            multipartFile.transferTo(uploadFile); // 일반예외 발생
        } catch (IOException e) {
            System.out.println(e);
            return null;
        } // 업로드 실패시 null 반환
        // 6. 만일 업로드 성공시 파일이름 반환하기
        return fileName;
    }

    // [2] 파일 다운로드
    public void fileDownload(int type, String fileName, HttpServletResponse response) {
        // * type 이 1 이면 사업자번호 , 2이면 체크리스트이다. 나머지는 오류고, 나중에 다른 경로 추가를 위해 남겨둠
        // uploadPath 선언
        String uploadPath;
        if (type == 1) {
            uploadPath = uploadPathBn;  // type이 1이면 사업자번호 경로
        } else if (type == 2) {
            uploadPath = uploadPathChk; // type이 2이면 체크리스트 경로
        } else uploadPath = null;   // 이건 오류

        // 매개변수1 : String fileName 다운로드 받을 파일명
        // 매개변수2 : HttpServletResponse response 다운로드를 요청한 사용자의 *응답* 객체 ,
        // 1. 다운로드 받을 파일명과 업로드 경로 조합
        String downloadPath = uploadPath + fileName;

        // 2. 만약에 지정한 경로에 파일이 없으면 리턴 , File클래스란? 파일/폴더 관련 메소드/기능 제공
        File file = new File(downloadPath);
        if (!file.exists()) {
            return;
        }

        try { // 예외처리 필수
            // 3. 업로드할 파일을 자바(바이트)로 가져오기(파일읽기)
            // 3-1 파일 입력스트림 객체 생성
            FileInputStream fin = new FileInputStream(downloadPath);
            // 3-2 파일 용량 만큼 배열 생성 , 읽어온 바이트들을 저장할 배열변수 선언
            long fileSize = file.length(); // 파일 용량/바이트수
            byte[] bytes = new byte[(int) fileSize]; // 파일 용량만큼 바이트 배열변수 선언
            // 3-3 파일 입력스트림 객체로 파일을 읽어와서 배열에 저장
            fin.read(bytes);
            // 3-4 안전하게 스트림 닫기, 스트림이란? 바이트가 다니는 (이동)통신 경로
            fin.close(); // 필수는 아니지만 대용량에서는 안전하게 입력스트림 객체 을 직접 닫기 권장!

            // ******************* 다운로드 형식 지정 : 브라우저 마다 상이 ************** //
            // ** 1. 파일의 uuid 제거 , split("기준문자") : 문자열내 기준문자 기준으로 문자열 자르기
            String oldFilename = fileName.split("_")[1]; // UUID-파일명 , _언더바 기준으로 잘라서 2번째 문자열 뜻
            // ** 2. 응답 형식 지정하기 , 다운로드 형식과 다운로드파일지정 , URL은 한글 지원하지 않는다. URLEncoder.encode( 파일명 , "UTF-8" );
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(oldFilename, "UTF-8"));

            // 4. 업로드할 파일을 사용자에게 (response객체 이용한) 응답하기.
            // 4-1 파일 출력스트림 객체 생성 , 서블릿이란? 자바회사에서 만든 HTTP 요청/응답 클래스
            ServletOutputStream fout = response.getOutputStream(); // response객체가 출력스트림 제공
            // 4-2 '3-3' 에서 읽어온 바이트 배열을 다운로드요청한(response)사용자에게 출력/쓰기 하기
            fout.write(bytes);
            // 4-3 안전하게 스트림 닫기
            fout.close(); // 필수는 아니지만 대용량에서는 안전하게 출력스트림 객체 을 직접 닫기 권장!
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    // [3] 파일 삭제
    public boolean fileDelete(int type, String fileName) {
        // * type 이 1 이면 사업자번호 , 2이면 체크리스트이다. 나머지는 오류고, 나중에 다른 경로 추가를 위해 남겨둠
        // uploadPath 선언
        String uploadPath;
        if (type == 1) {
            uploadPath = uploadPathBn;  // type이 1이면 사업자번호 경로
        } else if (type == 2) {
            uploadPath = uploadPathChk; // type이 2이면 체크리스트 경로
        } else return false;    // 오류다.

        // 1. 삭제할 파일명과 업로드 경로 조합
        String filePath = uploadPath + fileName;
        // 2. 만약에 경로에 파일이 존재하면 삭제 후 true
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
            return true;
        } // .delete() : 지정한 경로상의 파일 삭제
        // 2. 아니면 false
        return false;
    }
}