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

    <link rel='stylesheet' href='../CSS/sideMenu.css'>
</head>

<body>

    <div id="sideMenuContainer" class="row">
        <div class="sideMenu col">
            <div class="menuHead">템플릿 관리</div>
            <div class="middleMenu"> <a href="/template/roleTem.jsp">역할 관리</a></div>
            <div class="smallMenu"><a href="/template/roleTem.jsp">역할 템플릿</a></div>
            <div class="middleMenu"> <a href="/template/checkTem.jsp">체크리스트 관리</a></div>
            <div class="smallMenu"><a href="/template/checkTem.jsp">체크리스트 템플릿</a></div>
        </div>
    </div>

    <script src="../JS/sideMenu.js"></script>
    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
</body>

</html>