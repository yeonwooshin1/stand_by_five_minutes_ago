<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>로그인</title>
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
            <!-- 본문 작업 영역 -->
            <div class="mainContent col-10">
                <div class="container py-5">
                    <div class="row justify-content-center">
                        <div class="col-12 col-sm-10 col-md-8 col-lg-5">
                            <div class="card shadow-sm border-0">
                                <div class="card-body p-4">
                                    <h3 class="text-center mb-4">로그인</h3>

                                    <!-- 로그인 폼 (JS 셀렉터 유지: #emailInput, #pwdInput, onclick="login()") -->
                                    <div class="mb-3">
                                        <label for="emailInput" class="form-label">이메일</label>
                                        <input type="text" class="form-control emailInput" id="emailInput"
                                            placeholder="이메일을 입력하세요." />
                                    </div>

                                    <div class="mb-2">
                                        <label for="pwdInput" class="form-label">패스워드</label>
                                        <input type="password" class="form-control pwdInput" id="pwdInput"
                                            placeholder="패스워드를 입력하세요." />
                                    </div>

                                    <div class="d-grid mt-3">
                                        <button type="button" class="btn btn-primary" onclick="login()">로그인</button>
                                    </div>

                                    <div class="d-flex justify-content-between align-items-center mt-3">
                                        <a href="/user/findPwd.jsp" class="small text-decoration-none">비밀번호 찾기</a>
                                        <a href="/user/findEmail.jsp" class="small text-decoration-none">이메일 찾기</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- // 본문 작업 영역 끝 -->
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