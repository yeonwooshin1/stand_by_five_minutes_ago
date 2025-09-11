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

            /* 페이지 가로폭 */
            .indexContainer {
                max-width: 1440px;
                width: 95%;
                margin: 0 auto;
            }

            /* 테이블 고정 레이아웃 */
            #pfTable {
                table-layout: fixed;
                width: 100%;
                font-size: 0.9rem;
                /* 전체 폰트 크기 축소 */
            }

            /* 비고 */

            /* 모든 셀 중앙정렬 */
            #pfTable th,
            #pfTable td {
                text-align: center;
                vertical-align: middle;
                word-break: break-all;
            }

            /* 폼 컨트롤 중앙정렬 */
            #pfTable .form-control,
            #pfTable .form-select {
                text-align: center;
                font-size: 0.9rem;
                /* 내부 폰트 크기 미세 조정 */
            }

            /* No */
            #pfTable th:nth-child(1) { width: 4%; }
            /* 역할 */
            #pfTable th:nth-child(2) { width: 15%; }
            /* 체크리스트 */
            #pfTable th:nth-child(3) { width: 25%; }
            /* 시작시간 */
            #pfTable th:nth-child(4) { width: 14%; }
            /* 종료시간 */
            #pfTable th:nth-child(5) { width: 14%; }
            /* 알림조건 */
            #pfTable th:nth-child(6) { width: 10%; }
            /* 시간(분) */
            #pfTable th:nth-child(7) { width: 7%; }
            /* 진행상태 */
            #pfTable th:nth-child(8) { width: 5%; }
            /* 비고 */
            #pfTable th:nth-child(9) { width: 6%; }

            /* input/select 폭 꽉 채우기 */
            .pf-start,
            .pf-end,
            .pf-notify-mins {
                width: 10px;
            }

            .table-responsive {
                overflow-x: auto;
                /* 테이블만 가로 스크롤 가능 */
            }

            td .d-flex>.form-control {
                flex: 0 0 80px;
                /* 최소/최대/기본값 */
            }
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

                <div class="d-flex justify-content-between align-items-center mb-2">
                    <div class="d-flex gap-2">
                        <button id="btnAdd" type="button" class="btn btn-primary btn-sm">행 추가</button>
                    </div>
                </div>

                <div class="table-responsive">
                    <table id="pfTable" class="table table-striped table-hover">
                        <thead>
                            <tr>
                                <th style="width:40px;" class="text-center">No</th>
                                <th style="width:200px;">역할</th>
                                <th style="width:320px;">체크리스트</th>
                                <th style="width:230px;">시작시간</th>
                                <th style="width:230px;">종료시간</th>
                                <th style="width:130px;">알림조건</th>
                                <th style="width:100px;">시간(분)</th>
                                <th style="width:60px;">진행상태</th>
                                <th style="width:60px;"> 비고</th>
                            </tr>
                        </thead>
                        <tbody id="pfBody"><!-- rows go here --></tbody>
                    </table>
                </div>

                <div id="pfError" class="text-danger small"></div>

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

        <jsp:include page="/footer.jsp"></jsp:include>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>

        <!-- ★ 본 JS 연결 -->
        <script src="/JS/project/perform.js"></script>

    </body>

    </html>ap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>

        <!-- ★ 본 JS 연결 -->
        <script src="/JS/project/perform.js"></script>

    </body>

    </html>