<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <meta charset='utf-8'>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>프로젝트 근무 관리</title>
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
        <div class="container mt-4">
            <div class="d-flex justify-content-between align-items-center mb-3">
                <div class='title1'>프로젝트 업무 관리</div>
                <div>
                    <button type="button" class="btn btn-outline-success" id="excelBtn" onclick="downloadExcel()">Excel</button>
                    <button type="button" class="btn btn-outline-danger" id="pdfBtn">PDF</button>
                </div>
            </div>

            <div class="border rounded p-4 mb-4">
                <div class="fs-5 mb-3" style="font-weight: 500;">프로젝트 기본 정보</div>
                <form>
                    <div class="row mb-3 align-items-center">
                        <div class="col-md-6 d-flex">
                            <label for="pjName" class="form-label me-2 mt-2" style="width: 120px;">프로젝트명</label>
                            <input type="text" class="form-control" id="pjName" name="pjName" disabled>
                        </div>
                        <div class="col-md-6 d-flex">
                            <label for="clientName" class="form-label me-2 mt-2" style="width: 120px;">클라이언트명</label>
                            <input type="text" class="form-control" id="clientName" name="clientName" disabled>
                        </div>
                    </div>

                    <div class="row mb-3 align-items-center">
                        <div class="col-md-6 d-flex">
                            <label for="pjStartDate" class="form-label me-2 mt-2" style="width: 120px;">기간</label>
                            <input type="text" class="form-control" id="pjStartDate" name="pjStartDate" disabled>
                        </div>
                        <div class="col-md-6 d-flex">
                            <label for="clientRepresent" class="form-label me-2 mt-2" style="width: 120px;">담당자</label>
                            <input type="text" class="form-control" id="clientRepresent" name="clientRepresent"
                                disabled>
                        </div>
                    </div>
                    <div class="row mb-3 align-items-center">
                        <div class="col-md-6 d-flex">
                            <label for="roadAddress" class="form-label me-2 mt-2" style="width: 120px;">도로명 주소</label>
                            <input type="text" class="form-control" id="roadAddress" name="roadAddress" disabled>
                        </div>
                        <div class="col-md-6 d-flex">
                            <label for="clientPhone" class="form-label me-2 mt-2" style="width: 120px;">연락처</label>
                            <input type="text" class="form-control" id="clientPhone" name="clientPhone" disabled>
                        </div>
                    </div>

                    <div class="row mb-3 align-items-start">
                        <div class="col-md-6 d-flex">
                            <label for="detailAddress" class="form-label me-2 mt-2" style="width: 120px;">상세 주소</label>
                            <input type="text" class="form-control" id="detailAddress" name="detailAddress" disabled>
                        </div>
                        <div class="col-md-6 d-flex" style="justify-content: space-between;">
                            <label class="form-label mb-2 mt-2">지도</label>
                            <div class="border p-3" id="mapArea" style="height: 150px; min-width:31.5rem;"></div>
                        </div>
                    </div>
                </form>
            </div>
            <div class="border rounded p-4 mb-4">
                <div class="fs-5 mb-3" style="font-weight: 500;">체크리스트 관리</div>

                <table class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th>No</th>
                            <th>근무자</th>
                            <th>역할명</th>
                            <th>체크리스트명</th>
                            <th>시작시간</th>
                            <th>종료시간</th>
                            <th>상태</th>
                            <th>비고</th>
                        </tr>
                    </thead>
                    <tbody id="pjWorkerTbody">
                        <tr>
                            <td>1</td>
                            <td>홍길동</td>
                            <td>페인트 일반 페인트</td>
                            <td>축구 도착</td>
                            <td>08:45</td>
                            <td>09:00</td>
                            <td>완료</td>
                            <td><button type="button" class="btn btn-outline-primary" data-bs-toggle="modal"
                                    data-bs-target="#detailPerform">상세보기</button></td>
                        </tr>
                    </tbody>
                </table>

            </div>
        </div>

        <!-- 근무 정보 상세보기 -->
        <div class="modal fade" id="detailPerform" tabindex="-1" aria-labelledby="ModalLabel" aria-hidden="true">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h1 class="modal-title fs-5" id="ModalLabel"> 근무 정보 상세보기</h1>
                        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                    </div>
                    <div class="modal-body detailPerformContent">
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="userName" class="form-label me-2 mt-2" style="width: 120px;">근무자</label>
                                <input type="text" class="form-control" id="userName" name="userName" disabled>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="pjRoleName" class="form-label me-2 mt-2" style="width: 120px;">역할</label>
                                <input type="text" class="form-control" id="pjRoleName" name="pjRoleName" disabled>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="pjCheckList" class="form-label me-2 mt-2"
                                    style="width: 120px;">체크리스트</label>
                                <input type="text" class="form-control" id="pjCheckList" name="pjCheckList" disabled>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="pjCheckList" class="form-label me-2 mt-2" style="width: 60px;">근무시간</label>
                                <input type="text" class="form-control" id="pfStart" name="pfStart" disabled>
                                <input type="text" class="form-control" id="pfEnd" name="pjEnd" disabled>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="pjPerformStatus" class="form-label me-2 mt-2"
                                    style="width: 120px;">상태</label>
                                <select class="form-select pjPerformStatus">
                                    <option value="1" selected>시작전</option>
                                    <option value="2">진행</option>
                                    <option value="3">완료</option>
                                    <option value="4">취소</option>
                                    <option value="5">보류</option>
                                </select>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="memo" class="form-label me-2 mt-2" style="width: 120px;">메모</label>
                                <textarea class="form-control" name="memo" id="memo"></textarea>
                            </div>
                        </div>
                        <div class="mb-3 align-items-center">
                            <div class="d-flex">
                                <label for="memo" class="form-label me-2 mt-2" style="width: 120px;">파일</label>
                                <input type="file" name="fileName" id="fileName" />
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                        <button type="button" class="btn btn-primary" onclick="" data-bs-dismiss="modal">수정</button>
                    </div>
                </div>
            </div>
        </div>


        <!-- 카카오 우편번호/지도API -->
        <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
        <script
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1ac4a57d8a5927d34020a891fcdbbcbd&libraries=services"></script>

        <script src="/js/project/performcheck.js"></script>

        <!-- footer 연결 -->
        <jsp:include page="/footer.jsp"></jsp:include>

        <!--부트스트랩 CDN JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>
    </body>

    </html>