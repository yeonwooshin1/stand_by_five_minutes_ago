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

    <link rel='stylesheet' href='/CSS/index.css'>

    <!-- 글꼴 Noto Sans -->
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
    </style>
</head>

<body>

    <!-- header 연결 -->
    <jsp:include page="/header.jsp"></jsp:include>


    <!-- 본문 작업 영역 -->
    <div class="mainContent col-10">
        <div class="container mt-5" style="max-width: 400px;">
            <h3 class="text-center mb-4">로그인</h3>

            <!-- 로그인 폼 -->

            <div id="container">
                이메일 : <input type="text" class="emailInput" id="emailInput" placeholder="이메일을 입력하세요." /> <br />
                패스워드 : <input type="password" class="pwdInput" id="pwdInput" placeholder="패스워드를 입력하세요." /> <br />
                <button type="button" onclick="login()"> 로그인 </button> <br />
                <a href="/user/findPwd.jsp"> 비밀번호 찾기 </a>
                <a href="/user/findEmail.jsp"> 이메일 찾기 </a>
            </div>

        </div>
    </div>


    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
    <script src="/JS/user/login.js"></script>
</body>

</html>