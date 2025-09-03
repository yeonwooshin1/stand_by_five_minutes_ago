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
            </div>
            <!-- 본문 작업 영역 -->
            <div class="mainContent col-10">
                <div class="title1">이메일 찾기</div>
                <div class="ContentBox">
                    <div class="container-fluid p-0" style="max-width: 520px;">
                        <!-- 안내 -->
                        <div class="alert alert-dark border-0 small mb-3" role="alert">
                            <strong>이메일 찾기</strong> · 이름과 연락처를 입력하세요.
                        </div>

                        <!-- 카드 -->
                        <div class="card shadow-sm">
                            <div class="card-body">
                                <form class="row g-3" onsubmit="return false;">
                                    <!-- 이름 -->
                                    <div class="col-12">
                                        <label for="userName" class="form-label">이름</label>
                                        <input id="userName" name="userName" type="text" class="form-control" required
                                            placeholder="이름을 입력하세요" />
                                    </div>

                                    <!-- 전화번호 -->
                                    <div class="col-12">
                                        <label for="userPhone" class="form-label">전화번호</label>
                                        <div class="input-group">
                                            <input id="userPhone" name="userPhone" type="tel" class="form-control"
                                                required placeholder="010-1234-5678" />
                                            <button type="button" class="btn btn-primary" onclick="findEmail()">이메일
                                                찾기</button>
                                        </div>
                                        <div class="form-text">예) 010-1234-5678</div>
                                    </div>
                                </form>

                                <!-- 결과 영역 (기존 id 유지) -->
                                <div id="getDataBox" class="mt-3"></div>
                            </div>
                        </div>
                    </div>
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
    <script src="/js/user/findEmail.js"></script>
</body>

</html>