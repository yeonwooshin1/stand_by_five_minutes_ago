package five_minutes.service;

import com.opencsv.CSVReader;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileReader;
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
}   // class end
