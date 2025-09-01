package five_minutes.util;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;



// 비밀번호 재설정에서  필요한 "임시 상태" 를 인메모리에 보관하기 위한 캐시 빈 유틸 클래스
// issuedTokens  : 1회용 토큰이 아직 "미사용"인지(=TRUE) 관리하여 재사용을 차단한다.
// actionCooldown: 같은 이메일로 짧은 시간 내 반복 요청을 차단한다.

// 의존성 주입 가능한 빈 메모리를 수동으로 만드는 것 => Bean을 쓸 거면 반드시 있어야 하는 어노테이션
@Configuration
public class CacheUtil {    // class start

    // 1회성 토큰(JTI) 상태 캐시를 만든다.
    // - key: jti(UUID 등), value: TRUE(미사용), 없거나 FALSE면 사용 불가로 판단.
    // Bean을 사용해 수동으로 빈을 등록해 줄 때는 메서드 이름으로 빈 이름이 결정된다.
    @Bean
    public Cache<String ,Boolean> issuedTokens(){
        // Caffeine.newBuilder() : 캐시 설정을 위한 빌더 객체 설정
        // expireAfterWrite(Duration)는 put 한 시점으로부터 지정 시간 경과 후 자동으로 만료됨
        return Caffeine.newBuilder().expireAfterWrite(Duration.ofMinutes(20)) // JWT 15분 만료보다 약간 여유 있게 20분 설정
                .maximumSize(100_000)   // maximumSize() : 캐시에 저장 가능한 최대 공간임
                .build();               // 실제 Cache를 생성.
    }   // func end


    // 전송 쿨다운 캐시를 만드는 메소드
    // Bean을 사용해 수동으로 빈을 등록해 줄 때는 메서드 이름으로 빈 이름이 결정된다.
    @Bean
    public Cache<String, Long> actionCooldown() {
        // Caffeine.newBuilder() : 캐시 설정을 위한 빌더 객체 설정
        // expireAfterWrite(Duration)는 put 한 시점으로부터 지정 시간 경과 후 자동으로 만료됨
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofMinutes(5)) // 최근 5분 내 기록 자동 정리
                .maximumSize(100_000)                    // maximumSize() : 캐시에 저장 가능한 최대 공간임
                .build();                                // 실제 Cache를 생성.
    }   // func end


}   // class end