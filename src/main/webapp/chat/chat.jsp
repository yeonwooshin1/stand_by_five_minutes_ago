<%@ page language="java" contentType="text/html ; charset=utf-8" pageEncoding="UTF-8" %>
    <% String userNo=request.getParameter("userNo"); %>

        <!DOCTYPE html>
        <html>

        <head>
            <meta charset='utf-8'>
            <meta name="viewport" content="width=device-width, initial-scale=1">
            <title>채팅</title>
            <!--부트스트랩 CDN CSS-->

            <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/css/bootstrap.min.css" rel="stylesheet"
                integrity="sha384-LN+7fdVzj6u52u30Kp6M/trliBMCMKTyK833zpbD+pXdCLuTusPj697FH4R/5mcr"
                crossorigin="anonymous">


            <script>
                // JSP에서 받은 userNo를 JS 변수로 전달
                const userNo = "<%= userNo %>";
                console.log("userNo from JSP:", userNo);
            </script>
            <link rel='stylesheet' href='/css/chat/chat.css' />

        </head>

        <body>
            <div class="container mt-4">
                <div class="chat-wrapper row">
                    <!-- 좌측: 채팅 목록 -->
                    <div class="chat-list pt-3 pb-3 col-3">
                        <div class="d-flex justify-content-end mb-3">
                            <button type="button" class="btn btn-primary" style="width:100%;" data-bs-toggle="modal"
                                data-bs-target="#userSelectModal" onclick="fetchUserList()">채팅방 만들기</button>
                        </div>

                        <div class="btn-group-vertical chatRoomList" role="group" aria-label="Vertical button group"
                            style="width: 100%;">
                            <button class="btn btn-outline-secondary" type="button">홍길동</button>
                            <button class="btn btn-outline-secondary" type="button">가나다</button>
                        </div>
                    </div>

                    <!-- 우측: 채팅방 -->
                    <div class="chat-room col-9 border rounded">
                        <div class="chat-header mx-auto p-2 text-center" id="chatTilte" style="width: 20rem;"><span
                                class="fs-3"></span></div>
                        <div class="chat-messages" id="chatMessages">
                            <!-- 받은 메세지 -->
                            <div class="row">
                                <div class="mb-3 col-6">
                                    <div class="bg-light p-2 rounded d-inline-block"></div>
                                    <small class="text-muted d-block"></small>
                                </div>
                            </div>
                            <!-- 보낸 메세지 -->
                            <div class="row">
                                <div class="col-6"></div>
                                <div class="mb-3 text-end col-6">
                                    <div class="bg-primary text-white p-2 rounded d-inline-block"></div>
                                    <small class="text-muted d-block"></small>
                                </div>
                            </div>
                        </div>
                        <div class="chat-input">
                            <div class="input-group mb-3">
                                <input type="text" class="form-control" placeholder="메시지를 입력하세요">
                                <button class="btn btn-primary">전송</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 채팅방 만들기 모달 -->
            <div id="userSelectModal" class="modal" tabindex="-1" id="choiceUser">
                <div class="modal-dialog">
                    <div class="modal-content">
                        <div class="modal-header">
                            <h5 class="modal-title"> 사용자 선택 </h5>
                            <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
                        </div>
                        <div class="modal-body " id="userListContainer">
                            <!-- 사용자 목록이 여기에 렌더링됨 -->
                            <div class="text-center">

                            </div>


                        </div>
                        <div class="modal-footer">
                            <button type="button" class="btn btn-success" onclick="createChatRoom()"
                                data-bs-dismiss="modal">채팅방 생성</button>
                        </div>
                    </div>
                </div>
            </div>


            <script src="/js/chat/chat.js"> </script>

            <!--부트스트랩 CDN JS-->
            <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.7/dist/js/bootstrap.bundle.min.js"
                integrity="sha384-ndDqU0Gzau9qJ1lfW4pNLlhNTkCfHzAVBReH9diLvGRem5+R9g2FzA8ZGN954O5Q"
                crossorigin="anonymous"></script>
        </body>

        </html>