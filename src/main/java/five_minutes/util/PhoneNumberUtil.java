package five_minutes.util;

import java.util.regex.Pattern;

public final class PhoneNumberUtil {    // class start

    // ^010- : 010으로 시작
    // \\d{4} : 숫자 4개
    // - : 하이픈
    // \\d{4}$ : 숫자 4개로 끝
    private static final Pattern PATTERN = Pattern.compile("^010-\\d{4}-\\d{4}$");

    private PhoneNumberUtil() {} // 생성자 private → 인스턴스화 방지

    /** 010-xxxx-xxxx 형식인지 검사 */
    public static boolean isValid(String phone) {
        if (phone == null) return false;
        return PATTERN.matcher(phone).matches();
    }   // if end

}   // class end
