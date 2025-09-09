<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>
<% String userNo = request.getParameter("userNo"); %>

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>채팅</title>
    <!--부트스트랩 CDN CSS-->

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">


    <script>
        // JSP에서 받은 userNo를 JS 변수로 전달
        const userNo = "<%= userNo %>";
        console.log("userNo from JSP:", userNo);
    </script>

</head>

<body>
    
    <div class="Container">
        채팅차아아아아아아앙
    </div>

    <script src = "/js/chat/chat.js"> </script>

    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
</body>

</html>