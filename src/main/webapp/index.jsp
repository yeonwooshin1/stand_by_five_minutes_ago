<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>사이드메뉴</title>
    <!--부트스트랩 CDN CSS-->

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">

    <link rel='stylesheet' href='/css/common.css'>
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
    <section class="hero-section">
        <img src="/img/heroNonText.png" alt="Hero Image">
        <div class="content">
            <h1 class="main-title">
                공연 프로젝트. 제가 도와드릴게요.
                <span class="typing-text" id="typing-text"></span>
            </h1>

            <p class="subtitle">기획부터 출장까지 원스톱 솔루션</p>

            <!-- 기능 프리뷰 카드 -->
            <div class="feature-cards">
                <div class="feature-card" onclick="showProcess('template')">
                    <h3>📋 템플릿 관리</h3>
                    <p>표준화된 공연 기획서</p>
                </div>
                <div class="feature-card" onclick="showProcess('project')">
                    <h3>📂 프로젝트 관리</h3>
                    <p>일정부터 정산까지</p>
                </div>
            </div>

            <!-- CTA 버튼들 -->
            <div class="cta-buttons">
                <button class="cta-button cta-primary" onclick="window.location.href='/user/login.jsp'">지금
                    시작하기</button>
            </div>

        </div>
    </section>


    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
</body>

</html>