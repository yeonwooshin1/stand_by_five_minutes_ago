<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>

    <!DOCTYPE html>
    <html>

    <head>
        <meta charset='utf-8'>
        <meta name="viewport" content="width=device-width, initial-scale=1">
        <title>프로젝트 정보</title>
        <!--부트스트랩 CDN CSS-->
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
            integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">

        <link rel='stylesheet' href='/CSS/index.css'>
        <link rel='stylesheet' href='/CSS/project/info.css'>

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
                <div class="title1">프로젝트 정보</div>
                <div>
                    <button type="button" class="btn btn-primary" onclick="updatePJInfo()">저장</button>
                    <button type="button" class="btn btn-primary" onclick="nextStage()">다음</button>
                    <button type="button" class="btn btn-danger" onclick="deletePJInfo()">삭제</button>
                </div>
            </div>
            <div class="ContentBox">
                <form id="pjForm">
                    <div class="mb-3 text-end text-muted">
                        <small>생성일 : <span id="createDate"></span>
                            | 최근 수정일 : <span id="updateDate"></span></small>
                    </div>
                    <div class="card mb-4">
                        <div class="card-header bg-primary text-white">프로젝트 기본 정보</div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="pjName" class="form-label">프로젝트명</label>
                                <input type="text" class="form-control" id="pjName" name="pjName">
                            </div>
                            <div class="mb-3 pjdateArea row">
                                <div class="col">
                                    <label for="pjStartDate" class="form-label">시작일</label>
                                    <input type="date" class="form-control" id="pjStartDate" name="pjStartDate">
                                </div>
                                <div class="col">
                                    <label for="pjEndDate" class="form-label">종료일</label>
                                    <input type="date" class="form-control" id="pjEndDate" name="pjEndDate">
                                </div>
                            </div>
                            <div class="row">
                                <div class="col">
                                    <div class="mb-3">
                                        <label for="roadAddress " class="form-label"> 도로명 주소</label>
                                        <button class="btn btn-outline-secondary mb-1 btn-sm" type="button"
                                            onclick="Postcode()">검색</button>
                                        <input type="text" class="form-control" id="roadAddress" name="roadAddress"
                                            readonly style="background-color: #eeeeee;">
                                    </div>
                                    <div class="mb-3">
                                        <label for="detailAddress" class="form-label">상세 주소</label>
                                        <input type="text" class="form-control" id="detailAddress" name="detailAddress">
                                    </div>
                                    <div class="mb-3">
                                        <label for="projectMemo" class="form-label">메모</label>
                                        <textarea class="form-control" id="pjMemo" rows="3" name="pjMemo"></textarea>
                                    </div>
                                </div>
                                <div class="col">
                                    <label class="form-label mb-2">지도</label>
                                    <div class="border mapArea" id="mapArea"> </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="card mb-4">
                        <div class="card-header bg-secondary text-white">클라이언트 정보</div>
                        <div class="card-body">
                            <div class="row">
                                <div class="col">
                                    <div class="mb-3">
                                        <label for="clientName" class="form-label">클라이언트명</label>
                                        <input type="text" class="form-control" id="clientName" name="clientName">
                                    </div>
                                    <div class="mb-3">
                                        <label for="clientManager" class="form-label">담당자</label>
                                        <input type="text" class="form-control" id="clientRepresent"
                                            name="clientRepresent">
                                    </div>
                                    <div class="mb-3">
                                        <label for="clientContact" class="form-label">연락처</label>
                                        <input type="text" class="form-control" id="clientPhone" name="clientPhone">
                                    </div>
                                </div>
                                <div class="col">
                                    <div class="mb-3">
                                        <label for="clientRequest" class="form-label">요청사항</label>
                                        <textarea class="form-control" id="clientMemo" name="clientMemo"
                                            rows="5"></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>


        <!-- 카카오 우편번호/지도API -->
        <script src="//t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
        <script
            src="//dapi.kakao.com/v2/maps/sdk.js?appkey=1ac4a57d8a5927d34020a891fcdbbcbd&libraries=services"></script>

        <script src="/js/project/info.js"></script>

        <!-- footer 연결 -->
        <jsp:include page="/footer.jsp"></jsp:include>

        <!--부트스트랩 CDN JS-->
        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
            integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
            crossorigin="anonymous"></script>
    </body>

    </html>