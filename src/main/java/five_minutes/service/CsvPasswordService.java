package five_minutes.service;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;

// (파일 전체 복호화 Reader/암호화 Writer 사용을 위한 import
import java.io.Reader;
import java.io.BufferedReader;

import five_minutes.util.CsvCryptoUtil;

@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class CsvPasswordService {   // class start

    // 파일 경로 지정
    private final String path = "src/main/resources/csv/password.csv";
    // 평문 CSV 대신 암호화본 (.enc)만 저장됨
    private final String encPath = path + ".enc"; // src/main/resources/csv/password.csv.enc

    // BCrypt 라이브러리
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // properties 기반 CsvCryptoUtil ==> 암호화 복호화 하는 유틸 클래스 주입
    private final CsvCryptoUtil cryptoUtil;

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

    // 암호화 파일을 복호화 하는 cryptoUtil.openDecryptedReader() 가 추가됨
    // CSV 파일에서 특정 userNo에 해당하는 bcrypt 해시를 찾아 반환하는 매소드 ( matches 메소드의 헬퍼 메소드 )
    private String findHashByUserNo(int putUserNo) {
        // file 객체 생성
        File file = new File(path);

        // 파일이 존재하지 않으면?
        if (!file.exists()) {
            // null 반환
            // 평문 CSV 대신 암호화본 .enc 파일 존재 여부를 확인
            File encFile = new File(encPath);
            if (!encFile.exists()) {
                return null;
            }   // if end
        }   // if end

        try {
            // 파일이 있으면 암호화된 파일을 util 에서 복호화 하고 내가 위에 설정한 경로로 file을 읽어온다.
            Reader decReader = cryptoUtil.openDecryptedReader(new File(encPath));
            // 복호화한 CSV 파일 읽어온다.
            CSVReader csvReader = new CSVReader(decReader);

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
            // (ADD) 암호화 파일을 사용하므로 enc 파일 존재 여부 확인으로 대체
            File encFile = new File(encPath);             // (ADD)
            if (!encFile.exists()) {
                return false;
            }
        }   // if end

        // enc 파일을 복호화해 메모리에서 전체 CSV를 수정 후 다시 암호화하여 enc 파일로 저장하는 헬퍼메소드 호출
        return rewriteFileEnc(userNo, newPwdHash);

    }   // func end

    // 비밀번호 변경 csv 에 해시화 비밀번호 넣기
    public boolean changePassword( int userNo, String newPassword ) {

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
            // (ADD) enc 파일 체크로 대체
            File encFile = new File(encPath);             // (ADD)
            if (!encFile.exists()) {
                return false;
            }
        }   // if end

        //  enc 파일을 복호화해 메모리에서 전체 CSV를 수정 후 다시 암호화하여 enc 파일로 저장하는 헬퍼메소드 호출
        return rewriteFileEnc(userNo, newPwdHash);

    }   // func end


    // 비밀번호 변경에서 전체 재작성 하기 (파일 전체 암호화 , 복호화 버전)
    // .enc 파일을 복호화하여 평문 CSV를 메모리에 올린 뒤, 대상 행만 bcrypt 해시로 교체
    // 전체 평문 CSV 문자열을 다시 암호화하여 enc 파일로 저장한다!
    private boolean rewriteFileEnc( int targetUserNo, String newPwdHash ) {
        try {
            // 1. .enc 파일을 복호화 Reader로 복호화 한다.
            Reader decReader = cryptoUtil.openDecryptedReader(new File(encPath)); // (ADD)
            // reader를 한 줄씩 편하게 읽을 수 있게 하는 BufferedReader 객체 생성
            BufferedReader br = new BufferedReader(decReader);

            // 2. 평문 CSV 전체를 메모리로 읽어와 대상 행만 교체한다.
            // 수정된 CSV 내용을 임시로 담아놓고 연결하는 StringBuilder 객체 생성
            StringBuilder sb = new StringBuilder();
            // 한 행씩 읽을 때 임시로 담는 변수 Line
            String line;
            // 첫 번째 줄이 헤더인지 체크함 (처음에 UserNo , HashPassword 같은 거 말하는 거임)
            boolean isHeader = true;
            // 실제로 비밀번호를 바꿨는지 알려주는 boolean 변수
            boolean updated = false;

            // 한줄씩 읽는 것을 line에 저장하는데 line이 null 이 아닐 때까지 반복한다. => 즉 파일 끝까지 읽겠다.
            while ((line = br.readLine()) != null) {
                // isHeader가 있으면? => 즉 첫줄이면?
                if (isHeader) {
                    // 헤더는 그대로 복사해서 StringBuilder로 복사한다. ( 더해준다가 맞음.)
                    sb.append(line).append("\n");
                    // 이제 헤더 아니니까 false로 바꿈
                    isHeader = false;
                    // continue
                    continue;
                }   // if end
                // 줄이 비어있으면 그냥 유지한다.
                if (line.isEmpty()) {
                    // \n로 유지한다.
                    sb.append("\n");
                    // continue 해줌
                    continue;
                }   // if end
                // 빈줄도 아니고 헤더도 아니면?
                // 문자열 배열을 만든다.
                // 배열.split(분리할 기준점, 숫자) => 숫자에서 0이면 딱 그 값들만 자른다. 양수면 숫자만큼만 자른다.
                // 음수면 userNo , hashPassword , 이런 콤마 뒤에 빈 값까지 가져온다.
                // 왜 음수냐면 userNo,  이렇게 userNo 뒤에 비밀번호가 비어있을 때 그냥 가져오기 위해서 쓴다.
                String[] cols = line.split(",", -1);
                // 배열의 행이 2 미만이면? 오류다. userNo랑 해시 비번 2개의 행이 있어야 함.
                if (cols.length < 2) {
                    // 잘못된 행은 그냥 보존하고 넘어간다.
                    sb.append(line).append("\n");
                    // continue
                    continue;
                }   // if end

                // 이제부터 진짜 행 찾기
                // 그 행의 userNo를 저장하기 위한 변수 rowUserNo
                int rowUserNo;
                //
                try {
                    // 첫 열의 값을 parseInt 해서 숫자 변수로 만들어준다.
                    rowUserNo = Integer.parseInt(cols[0].trim());
                } catch (NumberFormatException e) { // NumberFormatException => 숫자가 아닌걸 parseInt 했을때 생기는 예외
                    // 숫자 변환 불가 라인은 보존한다.
                    sb.append(line).append("\n");
                    // continue
                    continue;
                }   // try end

                // 매개변수로 받은 userNo랑 해당 행의 첫열의 userNo랑 같다면? 바꿔야할 대상임
                if (rowUserNo == targetUserNo) {
                    // 대상 행만 매개변수로 받은 bcrypt 해시된 비밀번호 값 넣어주기.
                    String newLine = rowUserNo + "," + (newPwdHash == null ? "" : newPwdHash);
                    // 그행에 append 해준다.
                    sb.append(newLine).append("\n");
                    // 업데이트 된 걸 true로 바꿔준다.
                    updated = true;
                } else {
                    // 아니라면 그대로 보존해서 append 해준다.
                    sb.append(line).append("\n");
                }   // if end
                // 참고로 여기서 찾았는데 break 안해주는 이유는 전부 업데이트라 다 append 해줘야한다.
            }   // while end
            // 업데이트된 행이 없으면 실패 반환
            if (!updated) return false;

            // 3. 수정된 평문 CSV 전체를 다시 암호화해 같은 .enc 파일에 저장한다.
            // StringBuilder.toString() 임시로 append한 것들 최종 문자열로 바꾼다.
            cryptoUtil.writeEncrypted(new File(encPath), sb.toString());
            // 수정 됐으면 true
            return true;

        } catch (Exception e) {
            // util에서 던진 모든 예외들과 여기서 생긴 예외들 생기면 전부다 실패로 간주
            return false;
        }   // try end
    } // func end

}   // class end
