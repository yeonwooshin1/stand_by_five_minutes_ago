package five_minutes.util;

import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;

// CSV 암호화 복호화 하는 UTIL 클래스
@Component
public class CsvCryptoUtil {

    // 암호화 복호화 해주는 클래스
    // application.properties 에서 평문 키/알고리즘을 받음.
    // ==> 실제 개발환경에서는 export 해서 유출 위험을 줄여야함.
    private final StandardPBEStringEncryptor enc;

    // 생성자
    public CsvCryptoUtil(
            // Value 어노테이션으로 가져오기
            @Value("${jasypt.file.password}") String password,
            // 알고리즘은 값 누락 시 안전한 기본값으로 미리 설정해줌
            @Value("${jasypt.file.algorithm:PBEWithMD5AndDES}") String algorithm
    ) {
        // 암호화 복호화 해주는 클래스 객체 넣어주기
        enc = new StandardPBEStringEncryptor();
        // 키와 알고리즘을 각각 넣어준다.
        enc.setPassword(password);   // properties에 있는 평문 키
        enc.setAlgorithm(algorithm); // properties에 있는 알고리즘
        // 출력 타입(Base64)은 StandardPBEStringEncryptor 기본값 유지(Starter 없어도 OK)
    }   // 생성자 end



    // (읽기) 암호화된 CSV(.enc)을 복호화하여 Reader로 반환해주는 메소드
    // Reader 읽기 타입으로 반환한다.
    // 매개변수는 enc 화된 파일을 받는다.
    // 예외를 던진다. IOException => 파일 읽기 쓰기 과정에 대한 예외
    public Reader openDecryptedReader(File encFile) throws IOException {
        // enc 파일을 통째로 문자열로 읽는다.
        // 읽는건 아래에 헬퍼 메소드가 해준다.
        String cipherText = readAll(encFile);
        // 복호화하여 평문 문자열을 가져온다
        String plain = enc.decrypt(cipherText);
        // 평문 문자열을 메모리 Reader로 감싸 반환한다. CSVReader가 그대로 사용 가능하다.
        return new StringReader(plain);
    }   // func end


    // (쓰기) 평문 CSV 전체 문자열을 암호화하여 .enc 파일에 저장한다.
    // 반환값 없음
    // 매개변수는 File encFile 암호화된 CSV를 저장할 대상 파일 객체이고, String plainCsv는 평문 csv 이다.
    // 예외를 던진다. IOException => 파일 읽기 쓰기 과정에 대한 예외
    public void writeEncrypted(File encFile, String plainCsv) throws IOException {
        // 평문 CSV 전체를 암호화
        String cipherText = enc.encrypt(plainCsv);
        // 암호문을 enc 파일에 UTF-8로 기록
        // new FileOutputStream(encFile) 매개변수로 받은 enFile을 열고 쓸 준비하는 것
        // new OutputStreamWriter : 바이트 스트림을 문자 스트림으로 변환해줌. UTF-8 인코딩 지정해줌
        try (Writer w = new OutputStreamWriter(new FileOutputStream(encFile), StandardCharsets.UTF_8)) {
            // w.write(cipherText): 암호화된 Base64 문자열을 파일에 저장.
            w.write(cipherText);
        }   // try end
    }   // func end

    // 파일 전체를 UTF-8 문자열로 읽는 헬퍼 메소드
    private static String readAll(File f) throws IOException {
        // 파일을 바이트 단위로 읽는 InputStream
        try (InputStream in = new FileInputStream(f)) {
            // ByteArrayOutputStream => 메모리에 데이터를 쌓아둬서 모아두는 스트림이다.
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            // 8KB 짜리 바이트 배열을 만든다.
            byte[] buf = new byte[8192];
            // 실제로 읽은 바이트 수를 저장하는 n 변수 지정
            int n;
            // inputStream.read(바이트배열) buf 바이트 배열의 크기(= 8192) 만큼에 읽고 그 읽은 수를 n에 저장하고
            // 그 저장된 buf(바이트배열) 을  0부터 읽은 수(=n)개까지 bos 에 추가(write) 한다.
            // 반복은 읽을게 없을 때까지 반복(즉, 다 읽을 때까지) => in.read(buf) 파일 끝이면 -1을 반환하기 때문.
            while ((n = in.read(buf)) >= 0) bos.write(buf, 0, n);
            // 모아둔 바이트를 문자열로 바꾸는데 UTF_8 문자로 변환해서 리턴해준다.
            return bos.toString(StandardCharsets.UTF_8);
        }   // try end
    }   // func end


}   // class end