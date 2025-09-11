<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>체크리스트 템플릿 관리</title>
    <!--부트스트랩 CDN CSS-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>

    <!-- checkTem Css -->
    <link rel='stylesheet' href='/css/common.css'>
    <link rel='stylesheet' href='/css/index.css'>
    <link rel='stylesheet' href='/css/template/checkTem.css'>

    <!-- jquery 최신버전 -->
    <script src="http://code.jquery.com/jquery-latest.min.js"></script>

    <!-- 썸머노트 0.9.1 최신버전 css/js , https://cdnjs.com/ -->
    <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.css" rel="stylesheet">
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/summernote-bs5.min.js"></script>

    <!-- 썸머노트 한글 js-->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.9.1/lang/summernote-ko-KR.min.js"></script>

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
                <jsp:include page="/template/sideMenu.jsp"></jsp:include>
            </div>
            <!-- 본문 작업 영역 -->
            <div class="mainContent col-10">
                <div class="contentHeader">
                    <div class="title1">체크리스트 템플릿 관리</div>
                    <div class="titleBtnBox">
                        <!-- 모달 연결 버튼 -->
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#createCheckTem">생성</button> <!-- data-bs-target="#createCheckTem"으로 변경 -->
                    </div>
                </div>
                <!--data-bs-target에 연결할 모달을 연결-->

                <div class="ContentBox">
                    <!-- 체크리스트 템플릿 대분류를 표시 / checkTemplate table-->
                    <table class="table table-striped table-hover checkTemplateThead">
                        <!-- table class="checkTemplateThead"으로 변경 -->
                        <thead>
                            <tr> <!-- 선택 체크박스는 뺍니다 -->
                                <th> 번호 </th>
                                <th> 체크리스트 템플릿명 </th>
                                <th> 체크리스트 템플릿 설명 </th>
                                <th> 생성일 </th>
                                <th> 수정일 </th>
                                <th> 비고 </th>
                            </tr>
                        </thead>
                        <tbody class="checkTemplateTbody"> <!-- class="checkTemplateTbody" 으로 변경 -->
                            <tr>
                                <!-- 선택 체크박스는 뺍니다 -->
                                <td colspan="6"> ※ 표시할 정보가 없습니다.</td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- 생성 모달 --> <!-- id="createCheckTem"으로 변경 -->
    <div class="modal fade" id="createCheckTem" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">체크리스트 템플릿 생성</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body createCTContent"> <!-- class="modal-body createCTContent"으로 변경 -->
                    <div>
                        <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿명</label>
                        <input class="form-control" id="ctNameInput" type="text" /> <!-- ctNameInput으로 변경 -->
                    </div>
                    <div>
                        <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿 설명</label>
                        <textarea class="ctDescription" id="createctDescription" name="editordata"></textarea>
                        <!-- class="ctDescription" id="createctDescription" -->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    <button type="button" class="btn btn-primary" onclick="createCT()"
                        data-bs-dismiss="modal">저장</button> <!-- onclick="createCT()" 으로 변경 -->
                </div>
            </div>
        </div>
    </div>

    <!-- content 미리보기 모달 --> <!-- id="reviewCheckTem" 으로 변경 -->
    <div class="modal fade" id="reviewCheckTem" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">체크리스트 템플릿 조회</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body previewCTContent"> <!-- class="previewCTContent" -->
                    <div>
                        <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿명</label>
                        <input class="form-control" id="ctNamePreview" type="text" value="출근확인" disabled />
                    </div> <!-- id="ctNamePreview" -->
                    <div>
                        <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿 설명</label>
                        <div id="ctDescriptionPreview"></div> <!-- id="ctDescriptionPreview" -->
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Name / content 수정하기 모달 --> <!-- id="updateCheckTem" -->
    <div class="modal fade" id="updateCheckTem" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">역할 템플릿 수정</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body updateCTContent"> <!-- class="updateCTContent"-->
                    <div>
                        <label for="recipient-name" class="col-form-label">역할 템플릿명</label>
                        <input class="form-control" id="ctNameUpdate" type="text" /> <!-- id="ctNameUpdate" -->
                    </div>
                    <div>
                        <label for="recipient-name" class="col-form-label">역할 템플릿 설명</label>
                        <textarea class="ctDescription" id="ctDescriptionUpdate" name="editordata"></textarea>
                    </div> <!-- class="ctDescription" id="ctDescriptionUpdate" -->
                </div>
                <div class="modal-footer updateBox">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!-- CheckTem JS 연결 -->
    <script src="/JS/template/checkTem.js"></script>


</body>

</html>`