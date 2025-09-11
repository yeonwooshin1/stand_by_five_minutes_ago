<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <meta charset='utf-8'>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>프로젝트 인력 관리</title>
        <!--부트스트랩 CDN CSS-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
        <!--부트스트랩 CDN JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>


        <link rel='stylesheet' href='/CSS/index.css'>
        <link rel='stylesheet' href='/css/common.css'>
    <link rel='stylesheet' href='/css/project/worker.css'>

        <!-- jquery 최신버전 -->
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>

        <!-- 썸머노트 0.9.1 최신버전 css/js , https://cdnjs.com/ -->
        <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.css" rel="stylesheet">
        <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.js"></script>

        <!-- 썸머노트 한글 js-->
        <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/lang/summernote-ko-KR.min.js"></script>

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
        <div class="indexContainer mt-4">
            <div class="titleArea mb-3">
                <div class="title1">프로젝트 인력 관리</div>
                <div>
                    <button type="button" class="btn btn-primary" onclick="savePJworker()">저장</button>
                    <button type="button" class="btn btn-primary" onclick="nextStage()">다음</button>
                </div>
            </div>
            <div class="ContentBox border rounded">
                <div class="rowBox mb-2">
                    <button id="templateSearchBtn" class="btn btn-primary" data-bs-toggle="modal"
                        data-bs-target="#roleTemplateModal">역할 템플릿 검색</button>
                    <button id="addRowBtn" class="btn btn-secondary" onclick="addClearRow()">행 추가</button>
                </div>
                <table id="mainTable" class="table table-striped table-hover">
                    <thead>
                        <tr>
                            <th rowspan="2">역할명</th>
                            <th rowspan="2">역할설명</th>
                            <th colspan="5">근무자 정보</th>
                            <th rowspan="2">수정일</th>
                            <th rowspan="2">비고</th>
                        </tr>
                        <tr>
                            <th>이름</th>
                            <th>연락처</th>
                            <th>주소</th>
                            <th>숙련도</th>
                            <th>근무자 배정</th>
                        </tr>
                    </thead>
                    <tbody id="pjworkerTbody">

                    </tbody>
                </table>
            </div>
        </div>

        <!-- 역할 템플릿 모달 -->
        <div class="modal fade" id="roleTemplateModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">역할 템플릿 가져오기</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <select class="form-select modalRoleTemplate mb-3" aria-label="Default select">
                            <option selected value="0">대분류를 선택하세요.</option>
                        </select>
                    </div>
                    <table id="templateTable" class="table">
                        <thead>
                            <tr>
                                <th>번호</th>
                                <th>템플릿명</th>
                                <th>선택</th>
                            </tr>
                        </thead>
                        <tbody id="modalRoleTemTbdoy">
                            <tr>
                                <td colspan="3">대분류를 선택하세요.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
        </div>

        <!-- 인력 검색 모달 -->
        <div class="modal fade" id="workerModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">인력 검색</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body">
                        <div class="mb-3" style="display: flex;">
                            <div class="col-auto">
                                <input type="text" id="workerSearchInput" placeholder="이름 검색" class="form-control">
                              </div>
                              <div class="col-auto">
                                <button type="button" class="btn btn-primary userSearchBtn" onclick="searchUser()">검색</button>
                              </div>
                        </div>
                        <table id="workerTable" class="table">
                            <thead>
                                <tr>
                                    <th>No</th>
                                    <th>이름</th>
                                    <th>연락처</th>
                                    <th>주소</th>
                                    <th>선택</th>
                                </tr>
                            </thead>
                            <tbody id="workerTbody">
                                <tr>
                                    <td>1</td>
                                    <td>김xx</td>
                                    <td>010-1234-5678</td>
                                    <td>경기도 광주시</td>
                                    <td><button class="btn btn-sm btn-info selectWorkerBtn">선택</button></td>
                                </tr>
                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
        </div>

        <!-- 역할 설명 보기 모달 -->
        <div class="modal fade" id="viewModal" tabindex="-1">
            <div class="modal-dialog modal-lg">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">역할 설명 보기 및 수정</h5>
                        <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                    </div>
                    <div class="modal-body" id="fullDescriptBody">
                        <div id="fullDescriptBtnBox" style="display: flex; justify-content: flex-end;" class="mb-2"></div>
                        <textarea class="descriptionArea" id="descriptionArea" name="editordata"></textarea>
                    </div>
                </div>
            </div>
        </div>

        <!-- footer 연결 -->
        <jsp:include page="/footer.jsp"></jsp:include>

        <script src="/js/project/worker.js"></script>

    </body>

    </html>