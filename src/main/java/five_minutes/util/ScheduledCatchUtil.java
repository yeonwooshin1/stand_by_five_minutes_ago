package five_minutes.util;

import five_minutes.model.dto.ScheduledDto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;

// 시간이 지났는지 안 지났는지 알림을 주는 걸 판단하는 util 클래스
public final class ScheduledCatchUtil {   // class start

    private ScheduledCatchUtil() {}

    // 현재 시간을 초로 나타내는 메소드
    public static long nowSecond(){
        return System.currentTimeMillis() / 1000L; // PC 로컬 시간 기준 밀리초로 받는다 근데 비교는 초니까 1000을 나눠서 단위를 맞춘다.
    }   // func end

    // LocalDateTime 을 주면 초로 만들어주는 메소드 값이 null 이면 -1 반환
    public static long second( LocalDateTime dt ) {
        if (dt == null) return -1L;
        return dt.atZone(ZoneId.systemDefault()).toEpochSecond();
    }   // func end

    // 불러온 시간이 현재 시간 보다 같거나 과거면 true 줌
    public static boolean isDueOrPast( Long daoSec, long nowSec ) {
        // 값이 null이 아니면서 -1보다 크거나 nowSec이 더 많으면 지났다는 거니까 true
        return daoSec != null && daoSec >= 0 && daoSec <= nowSec;
    }   // func end

    // notifyType 기준 분기 나눠서 로직 판정
    // 1= 시작 전, 2= 시작 후, 3= 종료 전, 4= 종료 후
    public static Long targetSecond ( ScheduledDto dto ) {
        // 단위가 분이니 60을 곱해줌
        int sec= dto.getNotifySetMins() * 60;

        LocalDateTime start = dto.getPfStart();  // dto.getPjStart()
        LocalDateTime end   = dto.getPfEnd();    // dto.getPjEnd()

        switch (dto.getNotifyType()) {
            case 1 -> { // 시작전이니까 빼줌
                if (start == null) return null;
                return second(start) - sec;
            }   // case
            case 2 -> { // 시작후니까 더해줌
                if (start == null) return null;
                return second(start) + sec;
            }   // case
            case 3 -> { // 종료전이니까 빼줌
                if (end == null) return null;
                return second(end) - sec;
            }   // case
            case 4 -> { // 종료후니까 더해줌
                if (end == null) return null;
                return second(end) + sec;
            }   // case
            default -> {
                return null; // 0, -1 등은 발송 대상 아님
            }   // 유효하지 않는 값은 null 처리
        }   // switch end
    }   // func end

}   // class end
