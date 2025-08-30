select * from RoleTemplate where bnNo = "100-00-00001" ;
update RoleTemplate set rtName = "무대관리Test", rtDescription = "무대설치 test...." where rtNo = 2000016;
update RoleTemplate set rtStatus = 1 where rtNo = 2000016;

select * from RoleTemplateItem;
insert into RoleTemplateItem(rtNo, rtiName, rtiDescription) values (2000001,"무대관리",rtiDescription:"무대설치....");

{ rtNo : 2000001,
rtiName : "무대관리",
rtiDescription:"무대설치...."
}