package five_minutes.util;

// 비밀번호 유효성 검사 클래스
public class PasswordValidatorUtil {    // class start

    /**
     * 비밀번호 유효성 검사
     * 조건:
     *  - 길이: 8자 이상 20자 이하
     *  - 대문자 1개 이상 포함
     *  - 소문자 1개 이상 포함
     *  - 한글 포함 불가
     *  - 숫자/특수문자는 선택 사항
     */
    public static boolean isValid(String password) {
        if (password == null) return false;

        // 길이 체크 (8 ~ 20자)
        if (password.length() < 8 || password.length() > 20) {
            return false;
        }

        // 대문자, 소문자 포함 여부 플래그
        boolean hasUpper = false;
        boolean hasLower = false;

        for (char ch : password.toCharArray()) {
            // 한글(유니코드 범위 AC00 ~ D7A3) 금지
            if (ch >= 0xAC00 && ch <= 0xD7A3) {
                return false;
            }

            if (Character.isUpperCase(ch)) hasUpper = true;
            if (Character.isLowerCase(ch)) hasLower = true;

            // 두 조건 모두 만족하면 루프 조기 종료
            if (hasUpper && hasLower) break;
        }

        // 대문자 + 소문자 모두 있어야 true
        return hasUpper && hasLower;
    }   // func end
}   // class end
