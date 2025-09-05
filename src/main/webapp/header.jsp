<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">

    <!--부트스트랩 CDN CSS-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">

    <!-- header CSS -->
    <link rel='stylesheet' href='/CSS/header.css'>
</head>

<body>

    <div class="headerContainer">
        <div class="header-inner row">
            <a href="/project/list.jsp" class="col imgBox">
                <img class="logoImg" src="/img/logo.png">
            </a>
            <div class="menu-area col-6">
                <ul class="main-menu">
                    <!-- 관리자 메뉴 -->
                    <li><a href="#">인력 관리</a></li>
                    <li><a href="#">템플릿 관리</a></li>
                    <li><a href="#">프로젝트 관리</a></li>
                    <!-- 일반사용자 메뉴 -->
                    <li><a href="#">메뉴1</a></li>
                    <li><a href="#">메뉴2</a></li>
                    <li><a href="#">메뉴3</a></li>
                </ul>
            </div>
            <div class="sub-menu col-4">
                <div class="headText">
                    <div>OOO님 환영합니다. <br />(기업 담당자)</div>
                    <div>OOO님 환영합니다. <br />(일반 회원)</div>
                </div>
                <ul class="menu">
                    <li><a href="#">로그인</a></li>
                    <li><a href="#">회원가입</a></li>

                    <li><a href="#">마이페이지</a></li>
                    <li><a href="#">로그아웃</a></li>
                </ul>
            </div>


        </div>
    </div>

    <script src="/JS/header.js"></script>
    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
</body>

</html>