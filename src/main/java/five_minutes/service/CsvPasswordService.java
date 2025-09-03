package five_minutes.service;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;



@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class CsvPasswordService {   // class start

    // 파일 경로 지정
    private final String path = "src/main/resources/csv/password.csv";
    // BCrypt 라이브러리
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // -- 로그인할 시에 userNo와 평문 비밀번호를 받은 걸 csv 파일에 저장된 해시 비밀번호와 일치하는지 검증하는 메소드 --
    // 유의 할 점 : 절대 평문 비밀번호를 직접 해시해서 문자열 비교하지 않는다.
    // bcrypt 는 같은 비밀번호도 매번 다른 해시를 생성하기 때문에 matches( 맞는지만 확인 ) 메소드 이용.
    public boolean matches ( int userNo , String plainPassword ){

        // 비밀번호 한 번 더 방어용 검사, 혹시나 모르니까
        // isBlank() : 문자열이 비어 있거나, 빈 공백으로만 이루어져 있으면 true 반환
        if( plainPassword == null || plainPassword.isBlank()) {
            return false;
        }   // if end

        // userNo와 일치하는 해시화 비밀번호 찾는 메소드 ( 아래 참고 )
        String getHashPwd = findHashByUserNo(userNo);

        // 해시 비밀번호가 null 이거나 공백이라면 false 반환
        if (getHashPwd == null || getHashPwd.isBlank()) {
            return false;
        }   // if end

        try{
            // encoder.matches => 해시비밀번호에 사용된 솔트 + 라운드를 읽어와서 입력한 평문비밀번호을 같은 방식으로 다시 해시한 뒤 비교한다.
            // 참고로 encoder 변수는 메소드 밖의 전역변수로, BCryptPasswordEncoder 클래스 객체의 변수이다.
            boolean check = encoder.matches( plainPassword , getHashPwd);
            // 일치하면 true , 불일치하면 false 반환
            return check;
        } catch (IllegalArgumentException e) {    // IllegalArgumentException => 유효하지 않은 인자가 왔다는 의미를 가진 런타임 예외
            // 해시 문자열이 bcrypt 포맷이 아닐 때 ( 파일이 깨졌거나 잘못 저장된 경우에 ) 이런 예외가 발생할 수 있고, 그 경우 false를 반환한다.
            return false;
        }   // try end
    }   // func end


    // CSV 파일에서 특정 userNo에 해당하는 bcrypt 해시를 찾아 반환하는 매소드 ( matches 메소드의 헬퍼 메소드 )
    private String findHashByUserNo(int putUserNo) {
        // file 객체 생성
        File file = new File(path);

        // 파일이 존재하지 않으면?
        if (!file.exists()) {
            // null 반환
            return null;
        }   // if end
        try {
            // 한국어로 인코딩하고 내가 위에 설정한 경로로 file을 읽어온다.
            FileReader fileReader = new FileReader(path, StandardCharsets.UTF_8);

            // CSV 파일 읽어온다.
            CSVReader csvReader = new CSVReader(fileReader);

            // 파일 행 읽어올 문자열 배열 변수
            String[] row;

            // readNext() : 다음 줄을 읽는다. 없으면 null 반환이라 null이 아닐 때까지 무한 반복문을 돌린다. => 파일을 끝까지 읽는다.
            while( (row = csvReader.readNext()) != null ) {
                // 행이 두 개 미만이면 애초에 잘못 이루어진 csv 이니까 continue;
                if( (row.length < 2) ) continue;


                // userNo 변수 할당
                int userNo;

                try {
                    // csv의 첫 행을 int로 변환하고 공백 제거해서 userNo로 가져온다.
                    userNo = Integer.parseInt(row[0].trim());
                } catch (NumberFormatException e) {
                    // 숫자 형식으로 형태변환 때 예외 터지면 예외 처리로 continue 해준다. => 웬만해서 안 그러지만 파일이 깨질 시 이럴 수 있음.
                    continue;
                }   // try end

                // 일치하는지 확인한다.
                if(userNo == putUserNo){
                    // 비밀번호가 혹여나 null 일 경우 trim()을 하면 NullPointerException 예외 발생하기 때문에 null 인지 유효성 검사하여 null 이라면 ""를 변수에 저장한다.
                    return row[1] == null ? "" : row[1].trim();
                }   // if end
            }   // while end

            // 파일을 다 돌았는데 없으면 null 반환
            return null;

        } catch (Exception e) {
            return null;    // 읽기/파싱 중의 에러는 null 처리해준다.
        }   // try end
    }   // func end


    // 기존 비밀번호를 새 비밀번호로 바꾸는 메소드
    public boolean changePassword(int userNo, String oldPassword, String newPassword) {

        // 기존에 만든 matches 메소드 재활용해서 비밀번호가 기존 비밀번호와 맞는지 확인한다.
        if (!matches(userNo, oldPassword)) {
            // 현재 비번 불일치하면 false 반환
            return false;
        }   // if end

        // 새 비밀번호 bcrypt 해시 생성 한다.
        final String newPwdHash;
        try{
            newPwdHash = encoder.encode(newPassword);
        } catch ( RuntimeException e){
            // encoder 내부 예외가 날 가능성은 낮지만, 안전하게 실패 처리
            return false;
        }   // try end

        // file 객체 생성
        File file = new File(path);

        // 파일이 존재하지 않으면?
        if (!file.exists()) {
            // null 반환
            return false;
        }   // if end

        // 읽기 쓰기 다 가능한 RandomAccessFile 클래스
        // "rw" 는 읽기 쓰기 다 하겠단 것.
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path , "rw") ){
            // ADD: 첫 줄(헤더)만 건너뛰기 위해서 필요한 것 => NumberFormatException 예외 때문에 필요하다 => String을 int로 파서할 때 'userNo' 는 int로 파서할 수 없어서 뜨는 예외를 막아줌
            boolean skipHeader = true;
            String line;            // 현재 읽은 한 줄 문자열
            long pointerLine;       // 현재 줄이 시작되는 파일 위치를 기억하기 위한 변수

            while(true) {

                // 현재 파일 행 위치를 기억하는 변수
                pointerLine = randomAccessFile.getFilePointer();

                // readLine() 현재 위치에서 한 줄을 읽는 걸 기억하는 변수
                line = randomAccessFile.readLine();

                // 첫 줄(헤더) continue 해주는 것.
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }   // if end

                // null 이면 다 읽었다는 뜻이니까 반복문 종료
                if(line == null) break;

                // 그 읽어온 행을 ,을 기준으로 나누고 배열에 저장한다.
                String[] cols = line.split(",");

                // userNo랑 비밀번호 두 개의 행이 쪼개지는 거니까 최소 length 2개의 배열은 나와야한다. 만약 안나오면 잘못된 형식이니까 continue
                if(cols.length < 2) continue;


                try{
                    // cols 변수의 0번째 열은 userNo의 정보니까 그것을 변수로 저장
                    int rowUserNo = Integer.parseInt(cols[0].trim());

                    // 그것이 찾는 userNo와 같다면 이 줄이 우리가 수정할 대상임.
                    if (rowUserNo == userNo) {
                        // newLine의 변수에 해당 userNo와 , 새로운 비밀번호 해시화 한 것을 담아서 저장
                        String newLine = rowUserNo + "," + newPwdHash;

                        // 기존 줄이랑 길이가 다르면 형식 오류임 : 왜냐면 BCrypt는 길이가 다 동일하다.
                        if( newLine.length() != line.length() ){
                            System.out.println("확인용 : 기존 줄이랑 길이가 달라서 덮어쓰기 안되는 오류");
                            return false;
                        }   // if end

                        // 파일 포인터를 그 행의 시작으로 돌린다.
                        randomAccessFile.seek(pointerLine);
                        // 현재 포인터 위치(= 줄 시작)에 바뀐 비밀번호가 포함한 새 문자열을 그대로 덮어쓴다
                        randomAccessFile.writeBytes(newLine);

                        // 덮어쓰면 성공처리
                        return true;
                    }   // if end
                } catch ( NumberFormatException e ){
                    // Integer 할 때 숫자가 아닌 것을 숫자로 변환할 때 생기는 예외 처리
                    System.out.println("int로 타입변환 중 생기는 예외");
                }   // try end
            }   // while end
            // 못 찾으면 없으니까 실패 반환
            return false;
        } catch (Exception e){
            // 파일 읽어오는데 예외처리
            return false;
        }   // try end

    }   // func end

    // 비밀번호 변경 csv 에 해시화 비밀번호 넣기
    public boolean changePassword(int userNo, String newPassword) {

        // 새 비밀번호 bcrypt 해시 생성 한다.
        final String newPwdHash;
        try{
            newPwdHash = encoder.encode(newPassword);
        } catch ( RuntimeException e){
            // encoder 내부 예외가 날 가능성은 낮지만, 안전하게 실패 처리
            return false;
        }   // try end

        // file 객체 생성
        File file = new File(path);

        // 파일이 존재하지 않으면?
        if (!file.exists()) {
            // null 반환
            return false;
        }   // if end

        // 읽기 쓰기 다 가능한 RandomAccessFile 클래스
        // "rw" 는 읽기 쓰기 다 하겠단 것.
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(path , "rw") ){
            // ADD: 첫 줄(헤더)만 건너뛰기 위해서 필요한 것 => NumberFormatException 예외 때문에 필요하다 => String을 int로 파서할 때 'userNo' 는 int로 파서할 수 없어서 뜨는 예외를 막아줌
            boolean skipHeader = true;
            String line;            // 현재 읽은 한 줄 문자열
            long pointerLine;       // 현재 줄이 시작되는 파일 위치를 기억하기 위한 변수

            while(true) {

                // 현재 파일 행 위치를 기억하는 변수
                pointerLine = randomAccessFile.getFilePointer();

                // readLine() 현재 위치에서 한 줄을 읽는 걸 기억하는 변수
                line = randomAccessFile.readLine();

                // 첫 줄(헤더) continue 해주는 것.
                if (skipHeader) {
                    skipHeader = false;
                    continue;
                }   // if end

                // null 이면 다 읽었다는 뜻이니까 반복문 종료
                if(line == null) break;

                // 그 읽어온 행을 ,을 기준으로 나누고 배열에 저장한다.
                String[] cols = line.split(",");

                // userNo랑 비밀번호 두 개의 행이 쪼개지는 거니까 최소 length 2개의 배열은 나와야한다. 만약 안나오면 잘못된 형식이니까 continue
                if(cols.length < 2) continue;


                try{
                    // cols 변수의 0번째 열은 userNo의 정보니까 그것을 변수로 저장
                    int rowUserNo = Integer.parseInt(cols[0].trim());

                    // 그것이 찾는 userNo와 같다면 이 줄이 우리가 수정할 대상임.
                    if (rowUserNo == userNo) {
                        // newLine의 변수에 해당 userNo와 , 새로운 비밀번호 해시화 한 것을 담아서 저장
                        String newLine = rowUserNo + "," + newPwdHash;

                        // 기존 줄이랑 길이가 다르면 형식 오류임 : 왜냐면 BCrypt는 길이가 다 동일하다.
                        if( newLine.length() != line.length() ){
                            System.out.println("확인용 : 기존 줄이랑 길이가 달라서 덮어쓰기 안되는 오류");
                            return false;
                        }   // if end

                        // 파일 포인터를 그 행의 시작으로 돌린다.
                        randomAccessFile.seek(pointerLine);
                        // 현재 포인터 위치(= 줄 시작)에 바뀐 비밀번호가 포함한 새 문자열을 그대로 덮어쓴다
                        randomAccessFile.writeBytes(newLine);
                        // 덮어쓰면 성공처리
                        return true;
                    }   // if end
                } catch ( NumberFormatException e ){
                    // Integer 할 때 숫자가 아닌 것을 숫자로 변환할 때 생기는 예외 처리
                    System.out.println("int로 타입변환 중 생기는 예외");
                }   // try end
            }   // while end
            // 못 찾으면 없으니까 실패 반환
            return false;
        } catch (Exception e){
            // 파일 읽어오는데 예외처리
            return false;
        }   // try end

    }   // func end


}   // class end
