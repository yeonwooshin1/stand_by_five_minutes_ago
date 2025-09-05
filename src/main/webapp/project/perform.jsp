<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <meta charset='utf-8'>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>프로젝트 업무 배정/관리</title>
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

        <jsp:include page="/project/navigation.jsp"></jsp:include>

        <!-- 본문 영역 -->
        <div class="indexContainer">


            <div class="titleArea">
                <div class="title1">프로젝트 업무 배정/관리</div>
                <div>
                    <button type="button" class="btn btn-primary">저장</button>
                    <button type="button" class="btn btn-primary">다음</button>
                    <button type="button" class="btn btn-danger">삭제</button>
                </div>
            </div>
            <div class="ContentBox">
                <div id="pfSection" class="p-3 border rounded-3">
                    <div class="d-flex justify-content-between align-items-center mb-2">
                        <div class="fw-semibold">프로젝트 업무 배정/관리</div>
                        <div class="d-flex gap-2">
                            <button id="btnAdd" type="button" class="btn btn-primary btn-sm">행 추가</button>
                            <button id="btnSave" type="button" class="btn btn-success btn-sm">저장</button>
                        </div>
                    </div>

                    <div class="table-responsive">
                        <table id="pfTable" class="table table-sm align-middle">
                            <thead class="table-light">
                                <tr>
                                    <th style="width:56px;" class="text-center">No</th>
                                    <th style="min-width:160px;">역할</th>
                                    <th style="min-width:160px;">체크리스트</th>
                                    <th style="width:120px;">시작시간</th>
                                    <th style="width:120px;">종료시간</th>
                                    <th style="min-width:120px;">알림발송<br>여부</th>
                                    <th style="width:120px;">알림발송<br>시간(min)</th>
                                    <th style="min-width:120px;">진행상태</th>
                                    <th style="min-width:220px;">비고</th>
                                    <th style="width:84px;"></th>
                                </tr>
                            </thead>
                            <tbody id="pfBody">
                                <!-- rows go here -->
                            </tbody>
                        </table>
                    </div>

                    <div id="pfError" class="text-danger small"></div>
                </div>
            </div>
        </div>

        <!-- footer 연결 -->
        <jsp:include page="/footer.jsp"></jsp:include>

        <!--부트스트랩 CDN JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>
    </body>
    </html>