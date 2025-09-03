package five_minutes.service;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;


@Service                    // 서비스 어노테이션
@RequiredArgsConstructor    // 의존성 주입
public class EmailService { // class start

    private final JavaMailSender mailSender; // 스프링 메일 오토설정이 만든 전송기. properties 값으로 세팅됨.
    @Async
    public void sendHtml(String to, String subject, String htmlBody) {
        try {
            // 1) 빈 MimeMessage 객체 생성 (HTML, 첨부파일 전송 가능)
            MimeMessage mime = mailSender.createMimeMessage();
            // MimeMessageHelper 로 세부 설정이 가능하다.
            // multipart = true 는 첨부파일 지원하는 걸 결정
            // charset = UTF-8 는 한글 깨짐 방지를 위해서 설정
            MimeMessageHelper helper = new MimeMessageHelper(mime, true, "UTF-8");

            helper.setTo(to);   // 수신자
            helper.setSubject(subject);  // 제목
            helper.setText(htmlBody, true); // 본문이고 true = HTML 모드임.

            // SMTP 서버로 전송하는 send()
            mailSender.send(mime);
        } catch (Exception e) {
            // 모든 예외를 RuntimeException 으로 감싸서 던짐
            System.out.println("메일 발송 실패");
        }   // try end
    }   // func end

}   // class end
