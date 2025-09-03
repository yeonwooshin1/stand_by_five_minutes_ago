package five_minutes.util;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 이메일 본문(HTML) 생성 유틸
 * - Password Reset(비밀번호 재설정) 템플릿
 * - 모든 필드는 서버에서 안전하게 생성/검증 후 전달할 것
 */
public final class EmailSendFormat {

    private EmailSendFormat() {}





    /* =========================================================
     * 1-1) 비밀번호 찾기 양식 - 토큰 링크를 이미 만든 상태로 전달받아 HTML 생성
     *    ex) resetUrl = https://your-domain.com/reset/form?token=abc...
     * ========================================================= */
    // EmailSendFormat.java
    public static String passwordResetHtml(
            String userName,          // 수신자 이름(선택)
            String resetUrl,          // 토큰 포함 재설정 링크
            int expireMinutes,        // 만료 분
            String supportEmail       // 문의 메일
    ) {
        final String safeName    = escapeHtml(nullSafe(userName, "회원"));
        final String safeUrl     = escapeHtml(nullSafe(resetUrl, "#"));
        final String safeSupport = escapeHtml(nullSafe(supportEmail, "support@your-domain.com"));

        String nowStr = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));

        return """
            <!DOCTYPE html>
            <html lang="ko">
            <head>
              <meta charset="UTF-8">
              <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
              <title>비밀번호 재설정</title>
            </head>
            <body style="margin:0;padding:0;background:#f6f7fb;font-family:Arial,Helvetica,Apple SD Gothic Neo, sans-serif;">
              <table role="presentation" width="100%%" cellspacing="0" cellpadding="0" style="background:#f6f7fb;padding:24px 0;">
                <tr>
                  <td align="center">
                    <table role="presentation" width="600" cellspacing="0" cellpadding="0" 
                           style="width:600px;max-width:95%%;background:#ffffff;border-radius:12px;overflow:hidden;border:1px solid #eaeaea;">
                      <tr>
                        <td style="background:#111827;color:#ffffff;padding:20px 24px;font-size:18px;font-weight:bold;">
                          Stand by five minutes ago
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:28px 24px 8px 24px;color:#111827;">
                          <div style="font-size:18px;font-weight:700;line-height:1.4;">안녕하세요, %s 님</div>
                          <p style="margin:12px 0 0 0;font-size:14px;line-height:1.7;color:#374151;">
                            비밀번호 재설정을 요청하셔서 이 메일을 보내드립니다.<br/>
                            아래 버튼을 눌러 새로운 비밀번호를 설정해주세요.
                          </p>
                        </td>
                      </tr>
                      <tr>
                        <td align="center" style="padding:20px 24px 8px 24px;">
                          <a href="%s" 
                             style="display:inline-block;text-decoration:none;background:#2563eb;padding:12px 20px;border-radius:8px;
                                    font-size:15px;font-weight:700;color:#ffffff;">
                            비밀번호 재설정하기
                          </a>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:8px 24px 0 24px;color:#6b7280;font-size:12px;line-height:1.6;">
                          버튼이 동작하지 않으면 아래 URL을 브라우저 주소창에 복사/붙여넣기 해주세요.<br/>
                          <span style="word-break:break-all;color:#2563eb;">%s</span>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:16px 24px 0 24px;color:#374151;font-size:13px;line-height:1.7;">
                          <ul style="padding-left:18px;margin:0;">
                            <li>보안상 이 링크는 <strong>%d분</strong> 뒤에 만료됩니다.</li>
                            <li>요청 일시: %s (서버 기준)</li>
                          </ul>
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:16px 24px 0 24px;color:#6b7280;font-size:12px;line-height:1.6;">
                          <strong>본인이 요청하지 않았다면?</strong><br/>
                          누군가 실수로 잘못 입력했을 수 있습니다. 아무 조치도 하지 않으면 계정은 변경되지 않습니다.
                          의심스러운 활동이 계속되면 <a href="mailto:%s" style="color:#2563eb;text-decoration:none;">%s</a>로 문의해주세요.
                        </td>
                      </tr>
                      <tr>
                        <td style="padding:20px 24px 24px 24px;color:#9ca3af;font-size:11px;line-height:1.6;">
                          이 메일은 알림 전용입니다. &copy; %s Stand by five minutes ago. All rights reserved.
                        </td>
                      </tr>
                    </table>
                  </td>
                </tr>
              </table>
            </body>
            </html>
            """.formatted(
                safeName,
                safeUrl,
                safeUrl,
                expireMinutes,
                nowStr,
                safeSupport,
                safeSupport,
                String.valueOf(java.time.LocalDateTime.now().getYear())
        );
    }



    /* =========================================================
     * 1-2) baseUrl 과 token 을 받아 URL을 조립한 뒤 HTML 생성
     *    - baseUrl 예: https://your-domain.com/user/reset/form
     *      => 결과:   https://your-domain.com/user/reset/form?token=... (URL-encode)
     * ========================================================= */
    public static String passwordResetHtmlWithBase(
            String userName,
            String baseUrl,
            String token,
            int expireMinutes,
            String supportEmail
    ) {
        String resetUrl = buildResetUrl(baseUrl, token);
        return passwordResetHtml(userName, resetUrl, expireMinutes, supportEmail);
    }

    /* =========================================================
     * 1-3) Reset URL 조립 유틸
     * ========================================================= */
    public static String buildResetUrl(String baseUrl, String token) {
        String safeBase = nullSafe(baseUrl, "").trim();
        if (safeBase.endsWith("/")) {
            safeBase = safeBase.substring(0, safeBase.length() - 1);
        }
        String encodedToken = URLEncoder.encode(nullSafe(token, ""), StandardCharsets.UTF_8);
        String sep = safeBase.contains("?") ? "&" : "?";
        return safeBase + sep + "token=" + encodedToken;
    }

    /* =========================================================
     * 1-4) HTML Escape (아주 기초적인 수준)
     * ========================================================= */
    private static String escapeHtml(String raw) {
        return raw.replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#39;");
    }

    private static String nullSafe(String v, String def) {
        return (v == null || v.isEmpty()) ? def : v;
    }
}