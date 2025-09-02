<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>상세 체크리스트 템플릿 관리</title>
    <!--부트스트랩 CDN CSS-->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">
    <!--부트스트랩 CDN JS-->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>

    <!-- roleTem Css -->
    <link rel='stylesheet' href='/CSS/template/checkTemItem.css'>

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
                    <div class="title1">상세 체크리스트 템플릿 관리
                        <span></span>
                    </div>
                    <div class="titleBtnBox">
                        <!-- 모달 연결 버튼 -->
                        <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                            data-bs-target="#createCTItem">생성</button> <!-- data-bs-target="#createCTItem" -->
                    </div>
                </div>

                <div class="ContentBox">
                    <!-- 체크리스트 템플릿 대분류를 표시 / CheckTemplateItem table-->
                    <table class="table table-striped table-hover CTIThead"> <!-- class="CTIThead" -->
                        <thead>
                            <tr>
                                <th> 번호 </th>
                                <th> 상세 체크리스트 템플릿명 </th>
                                <th> 상세 체크리스트 템플릿 설명 </th>
                                <th> 생성일 </th>
                                <th> 수정일 </th>
                                <th> 비고 </th>
                            </tr>
                        </thead>
                        <tbody class="CTITbody"> <!-- class="CTITbody" -->
                            <tr>
                                <td>1</td>
                                <td>일반 삐에로</td>
                                <td>
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                        data-bs-target="#reviewCTI">미리보기</button> <!-- data-bs-target="#reviewCTI" -->
                                    <button type="button" class="btn btn-primary" data-bs-toggle="modal"
                                        data-bs-target="#updateCTI">수정하기</button> <!-- data-bs-target="#updateCTI" -->
                                </td>
                                <td>2025-06-07 08:38:54</td>
                                <td>2025-06-07 08:38:54</td>
                                <td><button type="button" class="btn btn-danger" onclick="">삭제</button></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>

    <!-- 생성 모달 --> <!-- id="createCTItem" -->
    <div class="modal fade" id="createCTItem" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">새 상세 체크리스트 템플릿 생성</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body CTItemContent"> <!-- class="CTItemContent" -->
                    <div class="ctContent"> <!-- class="ctContent" -->
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">대분류 체크리스트 템플릿명
                                <span
                                    style="font-size: 0.8rem; color: #A6A6A6; font-weight: 600; margin-left: 0.25rem;">※ 대분류 템플릿 수정은 [ 체크리스트 템플릿 ] 메뉴에서 가능합니다.</span></label>
                            <input class="form-control ctName01" type="text" disabled /> <!-- class="ctName01" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">대분류 체크리스트 템플릿명</label>
                            <div class="form-control ctDescription01" type="text"></div> <!-- class="ctDescription01" -->
                        </div>

                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">상세 체크리스트 템플릿명</label>
                            <input class="form-control" id="ctiNameInput" type="text" /> <!-- id="ctiNameInput" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">상세 체크리스트 템플릿 설명</label>
                            <textarea class="rtiDescription" id="rtiDescription" name="editordata"></textarea>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                    <button type="button" class="btn btn-primary" onclick="createCTI()" 
                        data-bs-dismiss="modal">저장</button> <!-- onclick="createCTI()"  -->
                </div>
            </div>
        </div>
    </div>

    <!-- content 미리보기 모달 --> <!-- id="reviewCTI" -->
    <div class="modal fade" id="reviewCTI" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">상세 체크리스트 템플릿 조회</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body previewRTIContent">
                    <div class="ctContent"> <!-- class="ctContent" -->
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">대분류 체크리스트 템플릿명
                                <span
                                    style="font-size: 0.8rem; color: #A6A6A6; font-weight: 600; margin-left: 0.25rem;">※ 대분류 템플릿 수정은 [ 체크리스트 템플릿 ] 메뉴에서 가능합니다.</span></label>
                            <input class="form-control ctName02" type="text" disabled /> <!-- class="ctName02" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">대분류 체크리스트 템플릿 설명</label>
                            <div class="form-control ctDescription02" type="text"></div> <!-- class="ctDescription02" -->
                        </div>

                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">상세 체크리스트 템플릿명</label>
                            <input class="form-control" id="previewCtiName" type="text" disabled /> <!-- id="previewCtiName" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">상세 체크리스트 템플릿 설명</label>
                            <div id="previewCtiDescription"></div> <!-- id="previewCtiDescription" -->
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- Name / content 수정하기 모달 --> <!-- id="updateCTI" -->
    <div class="modal fade" id="updateCTI" tabindex="-1" aria-labelledby="exampleModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-lg">
            <div class="modal-content">
                <div class="modal-header">
                    <h1 class="modal-title fs-5" id="exampleModalLabel">체크리스트 템플릿 수정</h1>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Close"></button>
                </div>
                <div class="modal-body updateCTIContent"> <!-- class="updateCTIContent" -->
                    <div class="ctContent"> <!-- class="ctContent" -->
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle"> 대분류 체크리스트 템플릿명
                                <span
                                    style="font-size: 0.8rem; color: #A6A6A6; font-weight: 600; margin-left: 0.25rem;">※ 대분류 템플릿 수정은 [ 체크리스트 템플릿 ] 메뉴에서 가능합니다.</span></label>
                            <input class="form-control ctName03" type="text" disabled /> <!-- class="ctName03" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">대분류 체크리스트 템플릿 설명</label>
                            <div class="form-control ctDescription03" type="text" aria-disabled="true"></div> <!-- class="ctDescription03" -->
                        </div>

                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿명</label>
                            <input class="form-control" id="updateCtiName" type="text" /> <!-- id="updateCtiName" -->
                        </div>
                        <div>
                            <label for="recipient-name" class="col-form-label modalMiddleTitle">체크리스트 템플릿 설명</label>
                            <textarea class="updateCtiDescription" id="updateCtiDescription"
                                name="editordata"></textarea> <!-- class="updateCtiDescription" id="updateCtiDescription" -->
                        </div>
                    </div>
                </div>
                <div class="modal-footer updateBox">
                    <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
                </div>
            </div>
        </div>
    </div>

    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!-- CheckTempItem JS 연결 -->
    <script src="/JS/template/checkTemItem.js"></script>


</body>

</html>`