select * from RoleTemplate where bnNo = "100-00-00001" ;
update RoleTemplate set rtName = "무대관리Test", rtDescription = "무대설치 test...." where rtNo = 2000016;
update RoleTemplate set rtStatus = 1 where rtNo = 2000016;
select * from RoleTemplate ;

select rtNo from RoleTemplate where rtNo = 2000001 and bnNo = "100-00-00001";

select * from RoleTemplateItem;
insert into RoleTemplateItem(rtNo, rtiName, rtiDescription) values (2000001,"무대관리","무대설치....");

select * from RoleTemplateItem where rtiNo=3000001 and rtiStatus = 1;
update RoleTemplateItem set rtiName = "testtest", rtiDescription = "testtesttesttest" where rtiNo=3000047;
update RoleTemplateItem set rtiStatus = 1 where rtiNo = 3000047;

select * from ProjectInfo where bnNo = "100-00-00001" and pjStatus = 1;
select * from ProjectInfo where pjNo = 6000001 and bnNo = "100-00-00001" ;

update ProjectInfo set pjName="test", pjMemo="test", pjStartDate=null, pjEndDate=null, roadAddress="test", detailAddress="test", clientName="test", clientPhone="test", clientMemo=null, clientRepresent="test" where pjNo = 6000011;
select * from projectinfo where pjno=6000011;

update ProjectInfo set pjStatus = "1" where pjNo = 6000011 and bnno = "100-00-00001";

select * from projectInfo;
select * from pjworker;
select p.*, w.userNo from ProjectInfo p inner join pjworker w on p.pjno = w.pjno where userNo = 1000008;

select p.*, w.pjroleName, w.pjNo, u.userName from pjPerform p inner join pjworker w on p.pjroleNo = w.pjroleNo  inner join Users u on w.userNo = u.userNo where w.pjNo = 6000001  order by pfStart;

-- 20250909 chat 기능 추가 테스트

INSERT INTO ChatRoom (roomNo, roomName, creatorUserNo, isGroup, createdDate, updateDate) VALUES (11000001, 'Chat with 1000002', 1000001, FALSE, NOW(), NOW());
INSERT INTO ChatRoom (roomNo, roomName, creatorUserNo, isGroup, createdDate, updateDate) VALUES (11000002, 'Chat with 1000003', 1000001, FALSE, NOW(), NOW());
INSERT INTO ChatRoom (roomNo, roomName, creatorUserNo, isGroup, createdDate, updateDate) VALUES (11000003, 'Chat with 1000004', 1000001, FALSE, NOW(), NOW());
INSERT INTO ChatRoom (roomNo, roomName, creatorUserNo, isGroup, createdDate, updateDate) VALUES (11000004, 'Chat with 1000005', 1000001, FALSE, NOW(), NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000001, 1000001, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000001, 1000002, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000002, 1000001, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000002, 1000003, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000003, 1000001, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000003, 1000004, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000004, 1000001, NOW());
INSERT INTO ChatRoomUser (roomNo, userNo, joineDate) VALUES (11000004, 1000005, NOW());
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000001, 11000001, 1000001, '감사합니다!', '2025-07-01 12:00:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000002, 11000001, 1000002, '잘 지내시죠?', '2025-07-01 12:05:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000003, 11000001, 1000001, '감사합니다!', '2025-07-01 12:10:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000004, 11000001, 1000002, '어떤 기능이 필요할까요?', '2025-07-01 12:15:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000005, 11000001, 1000001, '네, 확인했습니다.', '2025-07-01 12:20:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000006, 11000001, 1000002, '잘 지내시죠?', '2025-07-01 12:25:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000007, 11000002, 1000001, '어떤 기능이 필요할까요?', '2025-07-01 12:30:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000008, 11000002, 1000003, '어떤 기능이 필요할까요?', '2025-07-01 12:35:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000009, 11000002, 1000001, '곧 회의가 있어요.', '2025-07-01 12:40:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000010, 11000002, 1000003, '곧 회의가 있어요.', '2025-07-01 12:45:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000011, 11000002, 1000001, '이 기능 테스트 중이에요.', '2025-07-01 12:50:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000012, 11000003, 1000001, '감사합니다!', '2025-07-01 12:55:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000013, 11000003, 1000004, '안녕하세요!', '2025-07-01 13:00:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000014, 11000003, 1000001, '어떤 기능이 필요할까요?', '2025-07-01 13:05:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000015, 11000003, 1000004, '잘 지내시죠?', '2025-07-01 13:10:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000016, 11000003, 1000001, '채팅방을 만들어봤어요.', '2025-07-01 13:15:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000017, 11000004, 1000001, '안녕하세요!', '2025-07-01 13:20:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000018, 11000004, 1000005, '곧 회의가 있어요.', '2025-07-01 13:25:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000019, 11000004, 1000001, '채팅방을 만들어봤어요.', '2025-07-01 13:30:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000020, 11000004, 1000005, '감사합니다!', '2025-07-01 13:35:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000021, 11000004, 1000001, '네, 확인했습니다.', '2025-07-01 13:40:00');
INSERT INTO ChatMessage (messageNo, roomNO, sendUserNo, message, sentDate) VALUES (15000022, 11000004, 1000005, '그럼 그렇게 진행하겠습니다.', '2025-07-01 13:45:00');

CREATE TABLE chatRoom (
    roomNo int unsigned primary key auto_increment,
    roomName varchar(255),
    creatorUserNo int unsigned not null,
    isGroup boolean not null default false,
    createdDate datetime default now(),
    updateDate datetime default now() on update now(),
    foreign key (creatorUserNo) references users(userNo) ) AUTO_INCREMENT=11000001;
CREATE TABLE ChatRoomUser (
    roomNo INT UNSIGNED NOT NULL,
    userNo INT UNSIGNED NOT NULL,
    joineDate DATETIME DEFAULT NOW(),
    PRIMARY KEY (roomNo, userNo),
    FOREIGN KEY (roomNo) REFERENCES ChatRoom(roomNo),
    FOREIGN KEY (userNo) REFERENCES Users(userNo)
) ;
CREATE TABLE ChatMessage (
    messageNo INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    roomNO INT UNSIGNED NOT NULL,
    sendUserNo INT UNSIGNED NOT NULL,
    message TEXT NOT NULL,
    sentDate DATETIME DEFAULT NOW(),
    FOREIGN KEY (roomNO) REFERENCES ChatRoom(roomNO),
    FOREIGN KEY (sendUserNo) REFERENCES Users(userNo)
) AUTO_INCREMENT=15000001;
select * from chatRoom;
select * from chatRoomUser;
select * from chatMessage;