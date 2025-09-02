package five_minutes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication  // 스프링부트 어노테이션
@EnableAsync // 비동기 사용 활성화
public class AppStart { // class start
    public static void main(String[] args) {    // main start
        SpringApplication.run(AppStart.class);
    }   // main end
}   // class end
