<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>프로젝트 업무 배정/관리</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
          integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
    <link rel='stylesheet' href='/CSS/index.css'>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Noto+Sans+KR:wght@100..900&display=swap');
    </style>
</head>

<body>
<!-- header 연결: header.jsp에서 userNo, businessNo, pjNo 전역 세팅 -->
<jsp:include page="/header.jsp"></jsp:include>
<jsp:include page="/project/navigation.jsp"></jsp:include>

<div class="indexContainer">
    <div class="titleArea">
        <div class="title1">프로젝트 업무 배정/관리</div>
        <div>
            <button type="button" class="btn btn-primary" id="btnSaveTop">저장</button>
            <button type="button" class="btn btn-primary" id="btnNextTop">다음</button>
        </div>
    </div>

    <div class="ContentBox">
        <div id="pfSection" class="p-3 border rounded-3">
            <div class="d-flex justify-content-between align-items-center mb-2">
                <div class="fw-semibold">프로젝트 업무 배정/관리</div>
                <div class="d-flex gap-2">
                    <button id="btnAdd" type="button" class="btn btn-primary btn-sm">행 추가</button>
                </div>
            </div>

            <div class="table-responsive">
                <table id="pfTable" class="table table-sm align-middle">
                    <thead class="table-light">
                    <tr>
                        <th style="width:56px;" class="text-center">No</th>
                        <th style="min-width:200px;">역할</th>
                        <th style="min-width:200px;">체크리스트</th>
                        <th style="width:120px;">시작시간</th>
                        <th style="width:120px;">종료시간</th>
                        <th style="min-width:140px;">알림발송 조건</th>
                        <th style="width:140px;">알림발송 시간(분)</th>
                        <th style="min-width:120px;">진행상태</th>
                        <th style="width:84px;"></th>
                    </tr>
                    </thead>
                    <tbody id="pfBody"><!-- rows go here --></tbody>
                </table>
            </div>

            <div id="pfError" class="text-danger small"></div>
        </div>
    </div>
</div>

<!-- ======================= 체크리스트 설명 모달 ======================= -->
<div class="modal fade" id="descModal" tabindex="-1" aria-labelledby="descModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-dialog-centered">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="descModalLabel">체크리스트 설명</h5>
                <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
            </div>
            <div class="modal-body">
                <div class="fw-semibold mb-2" id="descTitle"><!-- 항목 제목 --></div>
                <div class="text-body-secondary" id="descBody"><!-- helpText --></div>
            </div>
            <div class="modal-footer">
                <button class="btn btn-primary" data-bs-dismiss="modal">확인</button>
            </div>
        </div>
    </div>
</div>
<!-- ================================================================ -->

<jsp:include page="/footer.jsp"></jsp:include>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>

<!-- ★ 본 JS 연결 -->
<script src="/JS/project/perform.js"></script>
</body>

</html>
