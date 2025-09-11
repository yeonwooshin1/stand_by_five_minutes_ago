<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>프로젝트 목록</title>
    <!--부트스트랩 CDN CSS-->

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">

    <link rel='stylesheet' href='/css/common.css'>
    <link rel='stylesheet' href='/css/index.css'>
    <link rel='stylesheet' href='/css/project/list.css'>

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
        <div class="titleArea">
            <div class="title1">프로젝트 목록</div>
            <button type="button" class="btn btn-primary createBtn"
                onclick="location.href='/project/create.jsp'">생성</button>
        </div>
        <div class="ContentBox">
            <table class="table table-striped table-hover pjList">
                <thead class="pjListHead">
                    <tr>
                        <th> 번호 </th>
                        <th> 프로젝트명 </th>
                        <th> 클라이언트 </th>
                        <th> 담당자 </th>
                        <th> 연락처 </th>
                        <th> 시작일 </th>
                        <th> 종료일 </th>
                        <th> 최근수정일 </th>
                    </tr>
                </thead>
                <tbody class="pjListTbody">
                    <tr>
                        <td colspan="8"> ※ 표시할 정보가 없습니다.</td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>


    <script src="/js/project/list.js"></script>

    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
</body>

</html>