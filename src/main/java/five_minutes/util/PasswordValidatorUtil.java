package five_minutes.util;

// 비밀번호 유효성 검사 클래스
public class PasswordValidatorUtil {    // class start

    // 비밀번호 유효성 검사
    // 대문자 1개 이상 포함
    // 소문자 1개 이상 포함
    // 길이 8자 이상
    // 특수문자/숫자는 선택사항
    public static boolean isValid(String password) {

        // 길이 체크
        if (password.length() < 8) {
            return false;
        }

        // 대문자, 소문자 포함 여부 플래그
        boolean hasUpper = false;
        boolean hasLower = false;

        for (char ch : password.toCharArray()) {
            if (Character.isUpperCase(ch)) hasUpper = true;
            if (Character.isLowerCase(ch)) hasLower = true;

            // 두 조건 모두 만족하면 루프 종료
            if (hasUpper && hasLower) break;
        }

        // 두 조건 모두 만족해야 true
        return hasUpper && hasLower;
    }   // func end
}   // class end