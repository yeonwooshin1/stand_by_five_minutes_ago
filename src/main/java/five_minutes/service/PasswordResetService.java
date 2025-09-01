package five_minutes.service;

import com.github.benmanes.caffeine.cache.Cache;
import five_minutes.model.dao.UsersDao;
import five_minutes.model.dto.UsersDto;
import five_minutes.util.JwtUtil;
import five_minutes.util.PasswordValidatorUtil;
import five_minutes.util.PhoneNumberUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PasswordResetService { // class start

    // userDao 의존성 주입
    private final UsersDao usersDao;
    // JWT Util 의존성 주입
    private final JwtUtil jwtUtil;
    // 이메일 서비스 의존성 주입
    private final EmailService emailService;
    // 발급된 토큰이 사용이 됐는지 확인하는 인메모리 Caffeine 메소드
    private final Cache<String, Boolean> issuedTokens;
    // 토큰이 발급됐는데 너무 자주 발급되지 않게 쿨타임을 주는 Caffeine 메소드
    private final Cache<String, Long> actionCooldown;

    // application.properties 에서 가져올 메일에서 실제로 링크로 보낼 링크를 저장해서 @Value로 가져온다.
    @Value("${app.link.reset-base-url}")
    private String resetBaseUrl;  // 예: https://YOUR-HOST/account/password/reset/verify....

    // 재설정 링크 발송하는 서비스
    public int sendResetLink( UsersDto usersDto ) {

        // 빈 값 들어오는 거 방어용 유효성 검사 추가해야함.
        String email = usersDto.getEmail() == null ? null : usersDto.getEmail().trim();
        String userName = usersDto.getUserName() == null ? null : usersDto.getUserName().trim();
        String userPhone = usersDto.getUserPhone() == null ? null : usersDto.getUserPhone().trim();

        // 만약 들어온 필수 값들이 null 이거나 trim 했는데 null 일시
        if(email == null || userName == null || userPhone == null || email.isBlank() || userName.isBlank() || userPhone.isBlank()){
            // -2 값 누락을 반환
            return -2;
        }   // if end

        // 전화번호 형식이 다를 경우
        if(PhoneNumberUtil.isValid(userPhone)){
            // -3 반환 전화번호 형식 오류
            return -3;
        }   // if end

        // 값 다시 재정의
        UsersDto dto = new UsersDto();
        dto.setEmail(email);    dto.setUserPhone(userPhone);    dto.setUserName(userName);

        // userNo 가져오는 dao를 호출 후 저장
        int userNo = usersDao.findUserNoResetPwd(dto);

        // 현재 시간을 초로 변환하는 변수 now=>  Instant : 시간 관련 클래스,  now() : 현재 시간
        // getEpochSecond : 그 시간을 초로 나타낸다.
        long now = Instant.now().getEpochSecond();

        // 검증 키를 생성하는 것.
        String cooldownKey = "pw:" + usersDto.getEmail();

        // 60초 검증용 인메모리에 해당 키를 넣어서 있는지 확인한다.
        // getIfPresent : 단순 조회용 메서드로, 데이터가 없으면 null을 반환함.
        Long last = actionCooldown.getIfPresent(cooldownKey);

        // 만약 last 변수가 null 이 아니고 둘 값의 차가 60초 보다 적다면?
        if(last != null && (now - last) < 60) {
            // 아직 60초가 안 지났기 때문에 false 반환
            return -1;  // 발급 쿨타임 실패
        }   // if end

        // 만약 userNo가 0이라면 => 존재하지 않는 계정이어도 "성공처럼" 응답만 함
        // 이유는 실패 반환하면 공격자가 이메일이나 전화번호가 존재하는지 안하는지 확인할 수 있음.
        if(userNo == 0){
            // 실제로 메일 보내기 전에 return을 시키는 것.
            return 1;   // 성공 but 실제 메일 발송 안 함.
        }   // if end

        // jti 생성 : 무작위 랜덤 식별자 생성함.
        String jti = UUID.randomUUID().toString();

        // JwtUtil 에서 토큰 발급을 한다.
        String token = jwtUtil.createToken(String.valueOf(userNo) , jti);

        // Caffeine 인메모리에 해당 식별자와 아직 한 번도 이 식별자를 사용해서 비밀번호 링크를 들어가지 않았다는 표시를 하는 true 값 세팅
        issuedTokens.put( jti , true );

        // Caffeine 인메모리에 해당 시간(초로 나타낸 것)과 요청한 email을 넣는다. => 1분 쿨타임을 주는 것
        // put : 캐시에 값을 수동으로 저장한다.
        actionCooldown.put(cooldownKey , now);

        // 메일 링크를 만든다.
        // UriComponentsBuilder : URI를 구성하는 Components 들을 효과적으로 다룰 수 있도록 하는 클래스
        // fromHttpUrl : http 부분을 구성한다 => 내가 설정해둔 resetBaseUrl을 넣기
        // queryParam : param 쿼리스트링 부분에 ?"token"=token 같이 쿼리스트링을 구성하는 부분에 생성한 token의 값을 넣는다.
        // build : 만든다.         // toUriString : 최종 안전한 URL 문자열로 생성한다.
        String link = UriComponentsBuilder.fromHttpUrl(resetBaseUrl)
                .queryParam("token" ,token ).build().toUriString();

        // 메일 전송한다. link 부분을 꼭 넣어준다. 아직 구현중 추가해야함.

        // 발송 다 했으면 true 값 반환
        return 1;   // 진짜 성공

    }   // func end

    // 토큰 (jti)이 아직 미사용인지 확인하고 만료인지 확인하고 막아주는 서비스
    // 여기서는 jti를 "소모"하지 않는다(최종 제출 단계에서 소모) => 여기는 검증용
    public int verifyResetToken( String token ) {
        try {
            // JJWT parser로 서명이 맞는 지 만료가 되진 않았는지 검증 한다.
            // 만약 성공하면 parser 할 수 있는 Jws<Claims> 타입의 jms 변수 호출 아니라면 예외 발생하기 때문에 예외처리로 실패를 처리해준다.
            Jws<Claims> jws = jwtUtil.parseAndValidate(token);

            // JWT 로 이루어진(header, payload , signature ) 파일을 getPayload()
            // => 파서해서 Claims(우리가 얻을 수 있는 내용물 => jti = id , sub = userNo, expire = 만료시간)
            // 를 가진 Claims 객체의 변수를 얻어온다.
            Claims c = jws.getPayload();

            // jti = (고유 id <= UUID) 를 issuedTokens (사용된 토큰인가?) 확인 => getIfPresent 가져오는거 없으면 null.
            Boolean ok = issuedTokens.getIfPresent(c.getId());
            // 토큰 인메모리가 없거나 false가 뜰 경우 실패 반환
            if (ok == null || !ok ) return 0;

            // String 으로 저장된 userNo를 parseInt 해서 int로 변환후 반환한다.
            return Integer.parseInt(c.getSubject());

        }   catch ( JwtException | NumberFormatException e) {
            // jws<claim> 파싱 중서명이 불일치 하거나 만료 되거나 형식 오류면 실패
            // String 으로 토큰에 저장된 UserNo를 int로 타입변환할 때 NumberFormatException 뜨면 실패
            return 0;
        }   // try end
    }   // func end



}   // class end
