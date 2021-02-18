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

CREATE OR REPLACE PROCEDURE synData_weilei (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN
	
-----------------------------------------添加印章管理功能点--2016-08-22-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SealManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID, DEFAULTURL, SN, ICON, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(70, 'SealManage', '印章管理', 7, '/oa/system/seal/list.do',  13, '/images/function/mid/dbom.png',  0,1,1,null,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=70;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1304,-1,70);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加印章管理功能点  exception others');
END;

END synData_weilei;
