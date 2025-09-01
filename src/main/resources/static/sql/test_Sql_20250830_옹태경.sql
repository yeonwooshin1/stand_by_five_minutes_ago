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