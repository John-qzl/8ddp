/**************************************************
 *  脚本参考oracle_synData.sql写
 
如下 通过 exception 捕获异常的方式，可以避免抛出错误。
BEGIN
select count(*) into countt from tabs where table_name = 'POSITION';
 IF(countt>0) THEN
  execute immediate 'insert into CWM_SYS_POS (POSID ,POSNAME,POSDESC,ORGID,JOBID,ISDELETE)
   select POS_ID,POS_NAME,POS_DESC,ORG_ID,JOB_ID,0 from POSITION';
   commit;
 END IF;
 exception
   when others then
   dbms_output.put_line('删表POSITION   exception others');
 END;
 
 BEGIN
  execute immediate 'drop table POSITION_SUB';
  execute immediate 'drop table POSITION';
  commit;
   exception
   when others then
   dbms_output.put_line('删表POSITION_SUB  POSITION   exception others');
 END;

**************************************************/

CREATE OR REPLACE PROCEDURE synData_hhj (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN
 ------------------------start plsql-----------------------------------------------
 
 BEGIN
  update   CWM_SYS_ORG SET   PATH = REPLACE(PATH,',','.');
  update   CWM_SYS_ROLE  set ALIAS = ROLENAME||ROLEID where ALIAS is null;
 END;
  
 commit;
END synData_hhj;
