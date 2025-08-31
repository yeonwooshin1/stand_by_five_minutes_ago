package five_minutes.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

// 날짜에 대한 비교 util class
public class DateValidatorUtil {    // class end

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    /// 시작일 ≤ 종료일 인지 검사하는 메소드
    public static boolean isValidRange(String start, String end) {
        try {
            LocalDate startDate = LocalDate.parse(start, FORMATTER);
            LocalDate endDate   = LocalDate.parse(end, FORMATTER);

            return !endDate.isBefore(startDate); // 종료 < 시작이면 false
        } catch (DateTimeParseException e) {
            return false; // 날짜 형식 오류
        }   // try end
    }   // func end


    /**
     * 날짜가 현재보다 과거인지 검사
     * @param date 검사할 날짜 (yyyy-MM-dd)
     * @return true = 과거 날짜, false = 오늘 이후
     */
    public static boolean isPastDate(String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date, FORMATTER);
            return targetDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return true; // 형식 오류는 일단 '유효하지 않음' 취급
        }   // try end
    }   // func end


    /**
     * 날짜가 현재 이후인지 검사
     * @param date 검사할 날짜 (yyyy-MM-dd)
     * @return true = 오늘 또는 미래, false = 과거
     */
    public static boolean isTodayOrFuture(String date) {
        try {
            LocalDate targetDate = LocalDate.parse(date, FORMATTER);
            return !targetDate.isBefore(LocalDate.now());
        } catch (DateTimeParseException e) {
            return false;
        }   // try end
    }   // func end
}   // class end