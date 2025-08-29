drop database if exists project;
create database project;
use project;

 -- 1) 유저부분 --
 
/* =========================================================
 * 1-1) Users table
 * Users 관련 테이블
 * ========================================================= */
CREATE TABLE Users (							
  userNo     		INT UNSIGNED NOT NULL AUTO_INCREMENT,	-- 사용자번호 PK
  email      		VARCHAR(40)  NOT NULL UNIQUE,			-- 이메일 UNIQUE
  passwordHash		VARCHAR(60)  NOT NULL,      			-- 해시화 비밀번호 (해시화를 위한 varchar 길이 60으로 수정)
  userName     		VARCHAR(20)  NOT NULL,					-- 이름 
  userPhone     	VARCHAR(15)  NOT NULL UNIQUE, 			-- 폰번호 UNIQUE
  userStatus		int			not null default 1,			--  유저 상태 0 삭제 1 활성
  roadAddress   	text NOT NULL,					-- 도로명 주소
  detailAddress 	VARCHAR(100) NOT NULL,					-- 상세 주소
  createDate 		DATETIME     NOT NULL default now(),	-- 가입일
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT PRIMARY KEY (userNo)							-- PK
) ENGINE=InnoDB AUTO_INCREMENT=1000001; -- PK 1000001부터 시작


/* =========================================================
 * 1-2) BusinessUser table
 * 사업자 등록 번호를 입력한 [기업 담당자]의 USER TABLE 외 추가 정보 테이블
 * ========================================================= */
CREATE TABLE BusinessUser (	
  bnNo  	 VARCHAR(30)  NOT NULL,  			-- 사업자등록번호 예: '123-45-67890' UNIQUE , primary key 
  bnName    	 VARCHAR(100) NOT NULL,				-- 기업명 
  managerName    VARCHAR(50)  NOT NULL,				-- 담당자명
  managerPhone   VARCHAR(20)  NOT NULL UNIQUE,		-- 담당자번호 
  bnDocuImg  	 varchar(250) NOT NULL,  			-- 사업자이미지 예: '/img/docs/alpha.png'
  bnType   		 VARCHAR(50)  NOT NULL,  			-- 업태
  bnStatus		 int NOT NULL DEFAULT 1,                 				-- 활성/비활성 삭제시에는 delete 사용. 0 은 비활성 1 은 활성
  bnItem   		 VARCHAR(50)  NOT NULL,  			-- 종목
  userNo         INT UNSIGNED NOT NULL,  			-- FK(User)	UNSIGNED -> INT 음수값 없애고 양수값만 두 배로 범위
  createDate 		DATETIME     NOT NULL default now(),	-- 가입일
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT PRIMARY KEY (bnNo),				-- primary key 
  CONSTRAINT 
    FOREIGN KEY (userNo) REFERENCES Users(userNo)	-- 참조 FK
) ENGINE=InnoDB;


 -- 2) 템플릿부분 --
 
/* =========================================================
 * 2-1) RoleTemplete table
 * 역할 템플릿 헤더(역할군의 묶음)
 * ========================================================= */
CREATE TABLE RoleTemplete (
  rtNo 				INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,   				-- 역할 템플릿 식별자 부분 PK
  bnNo 			VARCHAR(30) not null,                             		-- 이 템플릿을 소유/배포하는 사업자 식별 FK
  rtName    		VARCHAR(120) NOT NULL,                         			-- 템플릿 이름 (예: "콘서트_기본_역할군_v1")
  rtDescription    	text NULL,                             					-- 템플릿 설명부분
  rtStatus   		int NOT NULL DEFAULT 1,                 				-- 활성/비활성 삭제시에는 delete 사용. 0 은 비활성 1 은 활성
  createDate  		DATETIME NOT NULL DEFAULT NOW(),						-- 생성 날짜
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT 
    FOREIGN KEY (bnNo) REFERENCES BusinessUser(bnNo)
) ENGINE=InnoDB AUTO_INCREMENT=2000001; 					 				-- PK 2000001부터 시작




/* =========================================================
 * 2-2) RoleTempleteItem table
 * 역할 템플릿 항목(실제 역할들: 예 "무대매니저", "안내_입구", "안전요원")
 * ========================================================= */
CREATE TABLE RoleTempleteItem (
  rtiNo 			INT UNSIGNED PRIMARY KEY AUTO_INCREMENT, 		-- 역할 템플릿 실제 항목 부분 PK
  rtNo 				INT UNSIGNED NOT NULL,                        	-- 소속 역할템플릿 FK
  rtiName  			VARCHAR(120) NOT NULL,                          -- 역할 표시명(한글명)
  rtiDescription  	text NULL,                              -- 역할 설명/자격요건 등
  rtiStatus			int NOT NULL DEFAULT 1,                 		-- 활성/비활성 삭제시에는 delete 사용. 0 은 비활성 1 은 활성
  createDate 		DATETIME NOT NULL DEFAULT NOW(),				-- 생성 시간
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT
    FOREIGN KEY (rtNo) REFERENCES RoleTemplete(rtNo)
) ENGINE=InnoDB AUTO_INCREMENT=3000001; 					   		-- PK 3000001부터 시작


/* =========================================================
 * 2-3) CheckTemplete table
 *  체크리스트 템플릿 헤더
 * ========================================================= */
CREATE TABLE CheckTemplete (
  ctNo  		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,    	-- 체크리스트 템플릿 식별자 PK
  bnNo 		VARCHAR(30) NOT NULL,                           -- 이 템플릿을 소유/배포하는 사업자 식별 FK
  ctName    		VARCHAR(120) NOT NULL,                          -- 이름 (예: "개장_체크리스트_v2")
  ctDescription    longtext NULL,                              		-- 템플릿 설명
  ctStatus   		TINYINT(1) NOT NULL DEFAULT 1,					-- 활성/비활성 삭제시에는 delete 사용. 0 은 비활성 1 은 활성
  createDate  		DATETIME NOT NULL DEFAULT NOW(),				-- 생성 날짜
  updateDate DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		-- 변경 날짜
 CONSTRAINT 
    FOREIGN KEY (bnNo) REFERENCES BusinessUser(bnNo)
) ENGINE=InnoDB  AUTO_INCREMENT=4000001 ;							-- PK 4000001부터 시작



/* =========================================================
 * 2-4) CheckTempleteItem table
 *  체크리스트 템플릿 아이템 항목
 * ========================================================= */
CREATE TABLE CheckTempleteItem (
  ctINo 		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT, 		 		-- [PK] 체크리스트 템플릿 아이템 번호 
  ctNo 			INT UNSIGNED NOT NULL,                        	 		-- [FK] 체크리스트헤더 템플릿 번호
  ctiTitle 		VARCHAR(200) NOT NULL,                           		-- 체크리스트 제목
  ctiHelpText  		longtext  NULL,                               		-- 도움말 (선택으로 넣기)
  ctIStatus TINYINT(1) NOT NULL DEFAULT 1,					
  createDate		DATETIME NOT NULL DEFAULT NOW(),					-- 생성시간 
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),	-- 변경 날짜 (이거 최신템플릿 동기화할 시 필요한 항목) (선택으로 넣기)
  CONSTRAINT
    FOREIGN KEY (ctNo) REFERENCES CheckTemplete(ctNo)
) ENGINE=InnoDB AUTO_INCREMENT=5000001 ;							 	-- PK 5000001부터 시작



 -- 3) 프로젝트부분 --

/* =========================================================
 * 3-1) ProjectInfo table
 *  프로젝트 헤더
 * ========================================================= */
CREATE TABLE ProjectInfo (
  pjNo  	   		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,    	-- 프로젝트번호 PK
  bnNo 				VARCHAR(30) NOT NULL,                         	-- 이 프로젝트의 소유 사업자
  pjName 			VARCHAR(160) NOT NULL,                        	-- 프로젝트명
  pjMemo 			longtext NULL,                            	-- 당사 메모
  pjstartDate 		DATE NULL,                                  -- 시작날짜
  pjEndDate   		DATE NULL,                                  -- 종료날짜
  roadAddress   	text NOT NULL,							-- 도로명 주소
  detailAddress 	VARCHAR(255)  NOT NULL,							-- 상세 주소
  clientName		VARCHAR(20)  NOT NULL,							-- 클라이언트명	
  clientPhone		VARCHAR(15)  NOT NULL, 							-- 클라이언트연락처
  clientMemo		longtext NULL,                            	-- 클라이언트 업무요청사항
  pjStatus			TINYINT NOT NULL DEFAULT 1,						-- 상태 초기값 '1' => 1: 작성중() , 2: 작성완료 3: 진행중 4: 수행완료
  createDate  		DATETIME NOT NULL DEFAULT NOW(),				-- 생성시간 
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT
    FOREIGN KEY (bnNo) REFERENCES BusinessUser(bnNo)
) ENGINE=InnoDB AUTO_INCREMENT=6000001 ;							-- PK 6000001부터 시작




/* =========================================================
 * 3-2)  pjWorker  table
 *  프로젝트 사원(멤버) 배치
 * ⚠️ 요구사항상: "역할은 참조하지 않고, 선택 당시의 값을 저장해서 뿌리기"
 * ========================================================= */
CREATE TABLE  pjWorker  (
  pjNo 				INT UNSIGNED NOT NULL,                					-- [PK-1] 프로젝트명
  userNo    		INT UNSIGNED NOT NULL,                					-- [PK-2] 사원 
  partName    	 		VARCHAR(120) NOT NULL,                          	-- 체크리스트 이름 -> 사본이라면 복사 시점에 넣어주기
  partDescription    	VARCHAR(500) NULL,									-- 체크리스트 설명 -> 사본이라면 복사 시점에 넣어주기
  partLv				int not null default 5,								-- 1~5  >> 1 : 엘리트 2 : 베테랑 3 : 숙련자 4 : 초급자 5 : 신입생 
  partStatus			TINYINT NOT NULL DEFAULT 1,							-- 상태 		
  createDate  		DATETIME NOT NULL DEFAULT NOW(),	
  updateDate 		DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		 -- 변경 날짜
  CONSTRAINT
    FOREIGN KEY (userNo) REFERENCES Users(userNo),
  CONSTRAINT
    FOREIGN KEY (pjNo) REFERENCES ProjectInfo(pjNo)	 		 -- 동시복합 FK PK를 써서 중복을 방지하는 것.
) ENGINE=InnoDB;


/* =========================================================
 * 3-3) PjChecklist table
 *  프로젝트 체크리스트 "사본" 헤더
 * ========================================================= */
CREATE TABLE PjChecklist (
  pjChkNo   	 		INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,        	-- 체크리스트 헤더 번호 PK
  pjNo 			 		INT UNSIGNED NOT NULL,                       		-- 소속 프로젝트 FK
  pjChkName    	 		VARCHAR(120) NOT NULL,                          	-- 체크리스트 이름 -> 사본이라면 복사 시점에 넣어주기
  pjChkDescription    	longtext NULL,									-- 체크리스트 설명 -> 사본이라면 복사 시점에 넣어주기
  pjchkStatus			TINYINT NOT NULL DEFAULT 1,							-- 상태 		
  createDate  			DATETIME NOT NULL DEFAULT NOW(),					-- 생성 날짜
  updateDate 			DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),	-- 변경 날짜 -> 변경시점이랑 템플릿에서 변경된 거랑 업데이트 하려고
  CONSTRAINT
    FOREIGN KEY (pjNo) REFERENCES ProjectInfo(pjNo)
) ENGINE=InnoDB AUTO_INCREMENT=7000001 ;									-- PK 7000001부터 시작



/* =========================================================
 * 3-4) PjChecklistItem table
 *  프로젝트 체크리스트 "사본" 항목
 * ========================================================= */
CREATE TABLE PjChecklistItem (
  pjChkItemNo    	 	INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,     	 		-- 체크리스트 아이템 번호 PK
  pjChkNo   	 	 	INT UNSIGNED NOT NULL,                       	 		-- 프로젝트내 체크리스트 사본 FK
  -- seq        		INT NOT NULL,                               			-- 템플릿 내 표시 순서 (DnD용, 100/200 간격 권장)
  chklTitle 			VARCHAR(200) NOT NULL,                           		-- 체크리스트 제목
  helpText				longtext,
  pjChkIStatus			TINYINT NOT NULL DEFAULT 1,							-- 상태 		
  createDate  			DATETIME NOT NULL DEFAULT NOW(),						-- 생성 날짜
  updateDate 			DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),		-- 변경 날짜 -> 변경시점이랑 템플릿에서 변경된 거랑 업데이트 하려고
  CONSTRAINT
    FOREIGN KEY (pjChkNo) REFERENCES PjChecklist(pjChkNo)
) ENGINE=InnoDB AUTO_INCREMENT=8000001 ;										-- PK 8000001부터 시작



 -- 4) 역할을 가진 사원에게 체크 업무 부여 + 파일 첨부 --
 
/* =========================================================
 * 4-1)  pjPerform table
 *  프로젝트TASKCHECK (업무 배정)
 * assignee(= pjWorker ): 어떤 사원에게 맡기는지 (역할 스냅샷은 project_member 테이블에 이미 보관됨)
 * 일정: 시작/종료 시각(또는 날짜), 알림은 자바 서비스 스케줄러(@Scheduled/TaskScheduler 등)에서 처리
 * ========================================================= */
CREATE TABLE  pjPerform (
  pfNo       INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,      			-- 프로젝트업무번호 PK
  pjNo 				INT UNSIGNED NOT NULL,                         			-- [FK] 보관 조회용 -> 프로젝트 단위 조회/권한체크 편의
  pjChkItemNo	 	INT UNSIGNED NOT NULL,                         			-- [FK] 어떤 프로젝트 체크항목을
  userNo    		INT UNSIGNED NOT NULL,                         			-- [FK] 누구에게(프로젝트 멤버여야 함)
  
  -- 일정(서비스에서 알림 계산용): 4구간(시작전/시작후/종료전/종료후) 알림은 "테이블 추가 없이" 로직에서 처리 가능
  pfStart 	TIME NULL,                               	  		-- 시작 시각(없으면 당일/즉시형 등 정책)
  pfEnd   	TIME NULL,                               	  		-- 종료 시각
  
  -- 알림 발송하는 칼럼
  notifyType TINYINT NOT NULL DEFAULT 1 , 							-- 1 : 시작전 2: 시작 후 3: 종료전 4: 종료후
  notifySetMins INT NULL DEFAULT 0,								-- 몇 분인지 정해주는 것. 
  
  -- 진행 상태/결과
  pfStatus TINYINT NOT NULL DEFAULT 1,								-- 1: 시작전 2: 진행중 3: 완료됨 4: 취소됨 5: 보류
  note       VARCHAR(600) NULL,                               				-- 지시/비고
  createDate  			DATETIME NOT NULL DEFAULT NOW(),					-- 생성 날짜
  updateDate 			DATETIME NOT NULL DEFAULT NOW() ON UPDATE NOW(),	-- 변경 날짜 -> 변경시점이랑 템플릿에서 변경된 거랑 업데이트 하려고
  
  CONSTRAINT 
    FOREIGN KEY (pjNo) REFERENCES ProjectInfo(pjNo)
    ON DELETE CASCADE,										  				-- 프로젝트 삭제시 이 레코드 삭제 
  CONSTRAINT 
    FOREIGN KEY (pjChkItemNo) REFERENCES PjChecklistItem(pjChkItemNo)
    ON DELETE RESTRICT,                                       				-- 항목이 삭제되면 배정 정리 필요(운영 정책상 RESTRICT를 권장)
  CONSTRAINT
    FOREIGN KEY (userNo) REFERENCES Users(userNo)
    ON DELETE RESTRICT                                        				-- 담당자 계정 삭제 제한
) ENGINE=InnoDB AUTO_INCREMENT=9000001 ;									-- PK 9000001부터 시작

-- [4-2] 프로젝트TASKCHECK 파일 첨부(다중)
CREATE TABLE pjPerformFile (
  fileNo 		   	INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,    				-- 파일번호 PK
  pfNo      		INT UNSIGNED NOT NULL,                      				-- [FK] 어떤 배정의 첨부인가
  fileName  		VARCHAR(255) NOT NULL,                         			-- 첨부파일이름
  createDate  		DATETIME NOT NULL DEFAULT NOW(),							-- 생성 날짜
  CONSTRAINT
    FOREIGN KEY (pfNo) REFERENCES  pjPerform(pfNo)
    ON DELETE CASCADE                                        					-- 배정 삭제 시 첨부도 함께 삭제
) ENGINE=InnoDB AUTO_INCREMENT=10000001 ;										-- PK 10000001부터 시작
