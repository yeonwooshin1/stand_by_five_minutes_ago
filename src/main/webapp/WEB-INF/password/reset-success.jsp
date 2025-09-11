<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>비밀번호 변경 완료</title>

    <!-- 부트스트랩 CDN CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- 공통 CSS -->
    <link rel='stylesheet' href='/css/index.css'>

    <!-- 글꼴 Noto Sans -->
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
    </style>
</head>

<body>
    <!-- header 연결 -->
    <jsp:include page="/header.jsp"></jsp:include>

    <!-- 본문 영역 -->
    <main class="indexContainer my-5" style="max-width: 520px;">
        <!-- 안내 -->
        <div class="alert alert-success border-0 small mb-3" role="alert">
            <strong>비밀번호 변경 성공</strong> · 새 비밀번호로 로그인할 수 있습니다.
        </div>

        <!-- 카드 -->
        <div class="card shadow-sm">
            <div class="card-body text-center">
                <h5 class="card-title mb-3">비밀번호가 변경되었습니다.</h5>
                <p class="text-muted mb-4">이제 새 비밀번호로 로그인해 주세요.</p>
                <a href="/user/login.jsp" class="btn btn-primary">로그인 하러 가기</a>
            </div>
        </div>
    </main>

    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!-- 부트스트랩 CDN JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
