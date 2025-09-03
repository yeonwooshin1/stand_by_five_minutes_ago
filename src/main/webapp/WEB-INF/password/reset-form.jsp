<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>비밀번호 재설정</title>
    <!-- 부트스트랩 CDN CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">
    <!-- 공통 CSS -->
    <link rel="stylesheet" href="/css/index.css">

    <!-- 글꼴 Noto Sans -->
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
    </style>
</head>

<body>
    <!-- header 연결 -->
    <jsp:include page="/header.jsp"></jsp:include>

    <!-- 본문 영역 -->
    <main class="container my-5" style="max-width: 520px;">
        <!-- 안내 -->
        <div class="alert alert-dark border-0 small mb-3" role="alert">
            <strong>새 비밀번호 설정</strong> · 새로운 비밀번호를 입력하세요.
        </div>

        <!-- 카드 -->
        <div class="card shadow-sm">
            <div class="card-body">
                <form id="resetForm" class="row g-3" onsubmit="return false;">
                    <!-- 새 비밀번호 -->
                    <div class="col-12">
                        <label for="pw1" class="form-label">새 비밀번호</label>
                        <div class="input-group">
                            <input id="pw1" name="newPassword" type="password" class="form-control" minlength="8"
                                required placeholder="대/소문자 포함 8글자 이상" autocomplete="new-password" />
                            <button class="btn btn-outline-secondary" type="button" data-toggle-eye="#pw1"
                                aria-label="비밀번호 표시/숨김">표시</button>
                        </div>
                    </div>

                    <!-- 비밀번호 확인 -->
                    <div class="col-12">
                        <label for="pw2" class="form-label">비밀번호 확인</label>
                        <div class="input-group">
                            <input id="pw2" name="confirmPassword" type="password" class="form-control" minlength="8"
                                required placeholder="새 비밀번호 확인" autocomplete="new-password" />
                            <button class="btn btn-outline-secondary" type="button" data-toggle-eye="#pw2"
                                aria-label="비밀번호 표시/숨김">표시</button>
                        </div>
                    </div>

                    <!-- 결과 메시지 -->
                    <div id="msg" class="mt-2 small"></div>

                    <!-- 제출 버튼 -->
                    <div class="col-12 d-grid">
                        <button type="submit" class="btn btn-primary">비밀번호 변경</button>
                    </div>
                </form>
            </div>
        </div>
    </main>

    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!-- 부트스트랩 CDN JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
    <!-- 전용 JS -->
    <script src="/js/password/reset.js"></script>
</body>

</html>