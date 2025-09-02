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


    <!-- 본문 영역 -->
    <div class="indexContainer">
        <div class="row">
            <!-- 사이드 메뉴바 영역 -->
            <div class="side col-2">
                <jsp:include page="/sideMenu.jsp"></jsp:include>
            </div>
            <!-- 본문 작업 영역 -->
            <div class="mainContent col-10">
                <div class="title1">새 비밀번호 설정</div>
                <div class="ContentBox">
                     <form id="resetForm">
                        <div>
                          <label>새 비밀번호</label>
                          <input id="pw1" name="newPassword" type="password" minlength="8" required>
                        </div>
                        <div>
                          <label>비밀번호 확인</label>
                          <input id="pw2" name="confirmPassword" type="password" minlength="8" required>
                        </div>
                        <button type="submit">비밀번호 변경</button>
                     </form>
                </div>
            </div>
        </div>
    </div>


    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
    <script src="/js/password/reset-request.js"></script>
</body>

</html>