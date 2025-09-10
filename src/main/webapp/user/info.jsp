<!-- <%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %> -->

<!DOCTYPE html>
<html>

<head>
    <meta charset='utf-8'>
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>마이페이지</title>
    <!--부트스트랩 CDN CSS-->

    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
        integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr" crossorigin="anonymous">

    <link rel='stylesheet' href='/CSS/index.css'>

    <style>
        /* 모달(1050)보다 한단계 높은 z-index */
        .postcode-layer {
            position: fixed;
            z-index: 1060;
            top: 50%;
            left: 50%;
            width: 360px;
            height: 420px;
            transform: translate(-50%, -50%);
            background: #fff;
            border: 1px solid #ddd;
            border-radius: .5rem;
            box-shadow: 0 10px 20px rgba(0, 0, 0, .15);
            overflow: hidden;
            display: none;
        }

        /* 글꼴 Noto Sans */
        @media (min-width: 576px) {
            .postcode-layer {
                width: 480px;
                height: 520px;
            }
        }
    </style>
</head>

<body>

    <!-- header 연결 -->
    <jsp:include page="/header.jsp"></jsp:include>


    <!-- 본문 영역 -->
    <div class="indexContainer">
        <div class="row">
            <!-- 본문 작업 영역 -->
            <div class="mainContent col-10">
                <div class="title1">마이페이지</div>
                <div class="ContentBox">
                    <!-- ▼▼▼ 여기에 그대로 붙여넣기 ▼▼▼ -->
                    <div class="container-fluid p-0">
                        <!-- 안내 -->
                        <div class="alert alert-dark border-0 small mb-3" role="alert">
                            <strong>마이페이지</strong> · 회원 정보
                            (관리자 한정 <b>회사 정보</b>도 함께 표시)
                        </div>

                        <!-- 내 정보 카드 -->
                        <div class="card shadow-sm mb-4">
                            <div class="card-header d-flex align-items-center justify-content-between">
                                <div class="fw-bold">내 정보</div>
                                <div class="d-flex gap-2 align-items-center">
                                    <span class="badge text-bg-secondary" id="userNo-badge">-</span>
                                    <button class="btn btn-sm btn-primary" id="btn-refresh-user">새로고침</button>
                                </div>
                            </div>
                            <div class="card-body">
                                <!-- 로딩 표시 -->
                                <div id="user-loading" class="d-flex align-items-center gap-2 mb-3">
                                    <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true">
                                    </div>
                                    <span class="text-muted small">불러오는 중…</span>
                                </div>
                                <!-- 오류 표시 -->
                                <div id="user-error" class="alert alert-danger py-2 px-3 d-none" role="alert"></div>

                                <!-- 데이터 영역 -->
                                <div id="user-info" class="row gy-3">
                                    <div class="col-md-6">
                                        <div class="form-text mb-1">이메일</div>
                                        <div class="fw-semibold" id="email">-</div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-text mb-1">이름</div>
                                        <div class="fw-semibold" id="userName">-</div>
                                    </div>
                                    <div class="col-md-3">
                                        <div class="form-text mb-1">연락처</div>
                                        <div class="fw-semibold" id="userPhone">-</div>
                                    </div>
                                    <div class="col-12">
                                        <div class="form-text mb-1">주소</div>
                                        <div class="fw-semibold" id="address">-</div>
                                        <div class="fw-semibold" id="address-detail"></div>
                                    </div>
                                    <div class="col-md-4">
                                        <div class="form-text mb-1">가입일</div>
                                        <div class="text-muted" id="createDate">-</div>
                                    </div>
                                </div>

                                <!-- ✅ 내 정보 카드 - 액션 버튼 (오른쪽 하단) -->
                                <div class="d-flex justify-content-end gap-2 mt-3">
                                    <button class="btn btn-outline-secondary btn-sm" id="btn-user-edit">내 정보 수정</button>
                                    <button class="btn btn-warning btn-sm" id="btn-user-password">비밀번호 변경</button>
                                </div>
                            </div>
                        </div>

                        <!-- 회사 정보 카드 (관리자인 경우만 표시; 기본은 숨김) -->
                        <div class="card shadow-sm d-none" id="business-card">
                            <div class="card-header d-flex align-items-center justify-content-between">
                                <div class="fw-bold">회사 정보 (관리자)</div>
                                <button class="btn btn-sm btn-outline-primary" id="btn-refresh-business">새로고침</button>
                            </div>
                            <div class="card-body">
                                <!-- 로딩/오류 -->
                                <div id="biz-loading" class="d-flex align-items-center gap-2 mb-3 d-none">
                                    <div class="spinner-border spinner-border-sm" role="status" aria-hidden="true">
                                    </div>
                                    <span class="text-muted small">불러오는 중…</span>
                                </div>
                                <div id="biz-error" class="alert alert-danger py-2 px-3 d-none" role="alert"></div>

                                <div class="row gy-3">
                                    <div class="col-lg-8">
                                        <div class="row gy-3">
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">사업자번호</div>
                                                <div class="fw-semibold" id="bnNo">-</div>
                                            </div>
                                            <div class="col-md-8">
                                                <div class="form-text mb-1">기업명</div>
                                                <div class="fw-semibold" id="bnName">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">담당자명</div>
                                                <div class="fw-semibold" id="managerName">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">담당자번호</div>
                                                <div class="fw-semibold" id="managerPhone">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">업태</div>
                                                <div class="fw-semibold" id="bnType">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">종목</div>
                                                <div class="fw-semibold" id="bnItem">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">기업등록일</div>
                                                <div class="text-muted" id="bizCreateDate">-</div>
                                            </div>
                                            <div class="col-md-4">
                                                <div class="form-text mb-1">기업정보수정일</div>
                                                <div class="text-muted" id="bizUpdateDate">-</div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div class="d-flex justify-content-end gap-2 mt-3">
                                    <!-- (신규) 사업자 이미지 보기 버튼 -->
                                    <button class="btn btn-outline-secondary btn-sm" id="btn-biz-image"
                                        data-bs-toggle="modal" data-bs-target="#bizImageModal" disabled>
                                        사업자 등록증 보기
                                    </button>

                                    <button class="btn btn-primary btn-sm" id="btn-edit-business" data-bs-toggle="modal"
                                        data-bs-target="#bizEditModal">
                                        사업자 정보 수정
                                    </button>
                                </div>
                            </div>
                        </div>
                    </div>

                    <!-- ▼▼▼ 내 정보 수정 모달 추가 ▼▼▼ -->
                    <div class="modal fade" id="userEditModal" tabindex="-1" aria-labelledby="userEditModalLabel"
                        aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-scrollable">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h6 class="modal-title" id="userEditModalLabel">내 정보 수정</h6>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="닫기"></button>
                                </div>
                                <div class="modal-body">
                                    <form id="userEditForm" class="row g-3">
                                        <div class="col-md-6">
                                            <label class="form-label">이름</label>
                                            <input type="text" class="form-control" id="userEditName"
                                                placeholder="이름" />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">연락처</label>
                                            <input type="text" class="form-control" id="userEditPhone"
                                                placeholder="010-0000-0000" />
                                        </div>
                                        <!-- [교체] 도로명 주소 입력 + 검색버튼 -->
                                        <div class="col-12">
                                            <label class="form-label">도로명 주소</label>
                                            <div class="input-group">
                                                <input type="text" class="form-control" id="userEditRoad"
                                                    placeholder="도로명 주소" readonly>
                                                <button class="btn btn-outline-secondary" type="button"
                                                    id="btn-find-postcode">주소검색</button>
                                            </div>
                                        </div>
                                        <div class="col-12">
                                            <label class="form-label">상세 주소</label>
                                            <input type="text" class="form-control" id="userEditDetail"
                                                placeholder="상세 주소" />
                                            <div id="postcodeLayer" class="postcode-layer"></div>
                                        </div>
                                    </form>
                                    <div id="userEditError" class="alert alert-danger py-2 px-3 d-none mt-2"
                                        role="alert"></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-secondary"
                                        data-bs-dismiss="modal">취소</button>
                                    <button type="button" class="btn btn-primary" id="btn-user-save">저장</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- ▲▲▲ 내 정보 수정 모달 끝 ▲▲▲ -->

                    <!-- ▼▼▼ (업데이트된) 사업자 정보 수정 모달 추가 ▼▼▼ -->
                    <div class="modal fade" id="bizEditModal" tabindex="-1" aria-labelledby="bizEditModalLabel"
                        aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-scrollable">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h6 class="modal-title" id="bizEditModalLabel">사업자 정보 수정</h6>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="닫기"></button>
                                </div>
                                <div class="modal-body">
                                    <!-- BS-04 명세에 맞춘 필드(수정 가능: 담당자명/번호, 이미지, 업태, 종목) -->
                                    <form id="bizEditForm" class="row g-3" enctype="multipart/form-data">
                                        <div class="col-md-6">
                                            <label class="form-label">사업자번호</label>
                                            <input type="text" class="form-control" id="bizEditBnNo" readonly />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">기업명</label>
                                            <input type="text" class="form-control" id="bizEditBnName" readonly />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">담당자명</label>
                                            <input type="text" class="form-control" id="bizEditManagerName"
                                                placeholder="담당자명" />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">담당자번호</label>
                                            <input type="text" class="form-control" id="bizEditManagerPhone"
                                                placeholder="010-0000-0000" />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">업태</label>
                                            <input type="text" class="form-control" id="bizEditBnType"
                                                placeholder="업태" />
                                        </div>
                                        <div class="col-md-6">
                                            <label class="form-label">종목</label>
                                            <input type="text" class="form-control" id="bizEditBnItem"
                                                placeholder="종목" />
                                        </div>
                                        <div class="col-12">
                                            <label class="form-label">사업자등록증 이미지(선택)</label>
                                            <input type="file" class="form-control" id="bizEditDocuFile"
                                                accept="image/*" />
                                            <div class="form-text">기존 이미지는 유지됩니다. 새 파일을 선택하면 교체됩니다.</div>
                                        </div>
                                    </form>
                                    <div id="bizEditError" class="alert alert-danger py-2 px-3 d-none mt-2"
                                        role="alert"></div>
                                </div>
                                <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-secondary"
                                        data-bs-dismiss="modal">취소</button>
                                    <button type="button" class="btn btn-primary" id="btn-biz-save">저장</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- ▲▲▲ 사업자 정보 수정 모달 끝 ▲▲▲ -->


                    <!-- ▼▼▼ 비밀번호 변경 모달 추가 ▼▼▼ -->
                    <div class="modal fade" id="userPwdModal" tabindex="-1" aria-labelledby="userPwdModalLabel"
                        aria-hidden="true">
                        <div class="modal-dialog modal-md modal-dialog-scrollable">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h6 class="modal-title" id="userPwdModalLabel">비밀번호 변경</h6>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="닫기"></button>
                                </div>
                                <div class="modal-body">
                                    <form id="userPwdForm" class="row g-3">
                                        <div class="col-12">
                                            <label for="pwdCurrent" class="form-label">현재 비밀번호</label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="pwdCurrent"
                                                    placeholder="현재 비밀번호" />
                                                <button class="btn btn-outline-secondary" type="button"
                                                    data-toggle-eye="#pwdCurrent">표시</button>
                                            </div>
                                        </div>

                                        <div class="col-12">
                                            <label for="pwdNew" class="form-label">새 비밀번호</label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="pwdNew"
                                                    placeholder="대/소문자 포함 8글자 이상 20글자 이하" />
                                                <button class="btn btn-outline-secondary" type="button"
                                                    data-toggle-eye="#pwdNew">표시</button>
                                            </div>
                                            <div class="form-text">대/소문자 조합 8~20자 필수</div>
                                        </div>

                                        <div class="col-12">
                                            <label for="pwdNew2" class="form-label">새 비밀번호 확인</label>
                                            <div class="input-group">
                                                <input type="password" class="form-control" id="pwdNew2"
                                                    placeholder="새 비밀번호 재입력" />
                                                <button class="btn btn-outline-secondary" type="button"
                                                    data-toggle-eye="#pwdNew2">표시</button>
                                            </div>
                                        </div>
                                    </form>

                                    <!-- 에러/안내 -->
                                    <div id="userPwdError" class="alert alert-danger py-2 px-3 d-none mt-2"
                                        role="alert"></div>
                                    <div id="userPwdOk" class="alert alert-success py-2 px-3 d-none mt-2" role="alert">
                                    </div>
                                </div>

                                <div class="modal-footer">
                                    <button type="button" class="btn btn-outline-secondary"
                                        data-bs-dismiss="modal">취소</button>
                                    <button type="button" class="btn btn-primary" id="btn-pwd-save">변경</button>
                                </div>
                            </div>
                        </div>
                    </div>
                    <!-- ▲▲▲ 비밀번호 변경 모달 끝 ▲▲▲ -->
                    <div class="modal fade" id="bizImageModal" tabindex="-1" aria-labelledby="bizImageModalLabel"
                        aria-hidden="true">
                        <div class="modal-dialog modal-lg modal-dialog-centered">
                            <div class="modal-content">
                                <div class="modal-header">
                                    <h6 class="modal-title" id="bizImageModalLabel">사업자 등록증</h6>
                                    <button type="button" class="btn-close" data-bs-dismiss="modal"
                                        aria-label="닫기"></button>
                                </div>
                                <div class="modal-body d-flex justify-content-center">
                                    <img id="bnDocuImgModal" class="img-fluid rounded" alt="사업자등록증 이미지">
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>


    <!-- footer 연결 -->
    <jsp:include page="/footer.jsp"></jsp:include>

    <!--부트스트랩 CDN JS-->
    <script defer src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
        integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
        crossorigin="anonymous"></script>
    <script src="/js/user/info.js"></script>


</body>

</html>