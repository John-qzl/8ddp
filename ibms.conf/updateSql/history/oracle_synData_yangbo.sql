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

CREATE OR REPLACE PROCEDURE synData_yangbo (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN

 
--------------------------CWM_SYS_TYPEKEY添加数据------2016-8-27----------------------------------------------- 
BEGIN
select count(*) into countt from CWM_SYS_TYPEKEY WHERE TYPEKEY = 'ATTACH_TYPE';
 IF(countt=0) THEN
 	Insert into CWM_SYS_TYPEKEY (TYPEKEYID,TYPEKEY,TYPENAME,SN,FLAG,TYPE) values(6,'ATTACH_TYPE','附件分类',1,6,1); 
   	commit;
 END IF;
END;

BEGIN
UPDATE  CWM_SYS_TYPEKEY SET TYPE='1' WHERE TYPEKEY = 'ATTACH_TYPE';
END;

BEGIN	  
 select count(*) into countt from CWM_SYS_TYPEKEY WHERE TYPEKEY = 'INDEX_COLUMN_TYPE';
 IF(countt=0) THEN
	Insert into CWM_SYS_TYPEKEY (TYPEKEYID,TYPEKEY,TYPENAME,SN,FLAG,TYPE) values(7,'INDEX_COLUMN_TYPE','首页栏目分类',1,7,0);
   	commit;
  END IF; 
END;

--------------------------CWM_SYS_RES更新字段------2016-8-30----------------------------------------------- 
BEGIN
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/resources/tree.do' WHERE ALIAS = 'function'; 
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/sysUser/list.do' WHERE ALIAS = 'sysuser';
DELETE  from CWM_SYS_RES WHERE  ALIAS='PortalView';  
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/sysRole/list.do' WHERE ALIAS = 'sysrole';   
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/serialNumber/list.do' WHERE ALIAS = 'SerialNumberView';
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/sysTypeKey/list.do' WHERE ALIAS = 'sysTypeKey';  
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/globalType/tree.do' WHERE ALIAS = 'GlobalTypeManager';  
UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/dictionary/tree.do' WHERE ALIAS = 'DicManager';  
UPDATE  CWM_SYS_RES  SET DEFAULTURL='/oa/system/sysBackUpRestore/list.do' WHERE ALIAS = 'BackAndRestore';
UPDATE  CWM_SYS_RES SET DEFAULTURL='',ISFOLDER=1 WHERE ALIAS = 'SysLogView';
END;
 -----------------------------------------添加系统日志功能点--2016-09-01-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'subLog';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS,SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(184,'系统日志','subLog',2,'/styles/default/images/resicon/s_s_13.png',177,'/oa/system/sysLog/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=184;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1282,-1,184);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加系统日志功能点  exception others');
END;

-----------------------------------------添加错误日志功能点--2016-09-01-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sysErrorLog';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(185,'错误日志','sysErrorLog',3,'/styles/default/images/resicon/icon_xml.png',177,'/oa/system/sysErrorLog/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=185;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1283,-1,185);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加错误日志功能点  exception others');
END;


-----------------------------------------添加日志开关功能点--2016-09-01-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sysLogSwitch';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(186,'日志开关','sysLogSwitch',1,'/styles/default/images/resicon/s_s_20.png',177,'/oa/system/sysLogSwitch/management.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=186;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1284,-1,186);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加日志开关功能点  exception others');
END;

-----------------------------------------添加组织人员属性功能点--2016-09-10-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sys_param';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(187,'组织人员属性','sys_param',6,'/styles/default/images/resicon/9.png',3,'/oa/system/sysParam/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=187;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1285,-1,187);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加组织人员属性功能点  exception others');
END;


-----------------------------------------添加分级组织管理功能点--2016-09-20-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'gradeManager';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(188,'分级组织管理','gradeManager',7,'/styles/default/images/resicon/s_o_1.png',3,'/oa/system/grade/manage.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=188;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1286,-1,188);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加分级组织管理功能点  exception others');
END;

-----------------------------------------添加站内信功能点--2016-09-28-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'InnerMessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(230,'站内信','InnerMessage',1,'/styles/default/images/resicon/icon_tab3.png',281,'/oa/system/messageSend/form.do',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=230;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1287,-1,230);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加站内信功能点  exception others');
END;

-----------------------------------------添加收到的消息功能点--2016-09-28-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'readmessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(231,'收到的消息','readmessage',1,'/styles/default/images/resicon/vote.gif',230,'/oa/system/messageReceiver/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=231;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1288,-1,231);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加收到的消息功能点  exception others');
END;


-----------------------------------------添加已发送消息功能点--2016-09-28-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendmessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(232,'已发送消息','sendmessage',2,'/styles/default/images/resicon/o_16.png',230,'/oa/system/messageSend/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=232;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1289,-1,232);
	    	commit;
          END IF;
   	 COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加已发送消息功能点  exception others');
END;

-----------------------------------------添加发送消息功能点--2016-09-28-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendMsg';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(233,'发送消息','sendMsg',3,'/styles/default/images/resicon/o_16.png',230,'/oa/system/messageSend/edit.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=233;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1290,-1,233);
	    	commit;
 		  END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加发送消息功能点  exception others');
END;


-----------------------------------------添加邮件管理功能点--2016-09-30-----------------------------
BEGIN
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(240,'邮件管理','mail',2,'/styles/default/images/resicon/site.gif',281,'/oa/system/messageSend/form.do',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=240;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1291,-1,240);
	    	commit;
     	  END IF;
  	END IF;
  		exception
			when others then
   			dbms_output.put_line('添加邮件管理功能点  exception others');
END;
-----------------------------------------添加外部邮件功能点--2016-09-30----------------------------- 
BEGIN 
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'outMailAll';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(241,'外部邮件','outMailAll',1,'/styles/default/images/resicon/o_14.png',240,'/oa/mail/outMail/warn.do',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=241;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1292,-1,241);
	    	commit;
    	  END IF;
 	 END IF;
 		exception
			when others then
   			dbms_output.put_line('添加外部邮件功能点  exception others');
END;   			
   			
-----------------------------------------添加查看邮件功能点--2016-09-30-----------------------------
BEGIN   
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'lookMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(242,'查看邮件','lookMail',1,'/styles/default/images/resicon/ico_113.gif',241,'/oa/mail/outMail/get.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=242;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1293,-1,242);
	    	commit;
     	  END IF;
    END IF;
   		 exception
			when others then
   			dbms_output.put_line('添加查看邮件功能点  exception others');
END;

-----------------------------------------添加删除邮件功能点--2016-09-30-----------------------------
BEGIN      
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'delMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(243,'删除邮件','delMail',2,'/styles/default/images/resicon/del.png',241,'/oa/mail/outMail/del.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=243;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1294,-1,243);
	    	commit;
          END IF;
    END IF;
	    exception
				when others then
	   			dbms_output.put_line('添加删除邮件功能点  exception others');
END;
 
-----------------------------------------添加新建邮件功能点--2016-09-30-----------------------------
BEGIN     
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mailAdd';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(244,'新建邮件','mailAdd',2,'/styles/default/images/resicon/0_15.png',240,'/oa/mail/outMail/edit.do',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=244;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1295,-1,244);
	    	commit;
    	  END IF;
    END IF;
    	 exception
				when others then
	   			dbms_output.put_line('添加新建邮件功能点  exception others');
END;

-----------------------------------------添加保存邮件功能点--2016-09-30-----------------------------
BEGIN     
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'saveOutmail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(245,'保存邮件','saveOutmail',1,'/styles/default/images/resicon/tree_file.gif',244,'/oa/mail/outMail/send.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=245;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1296,-1,245);
	    	commit;
     	  END IF;
    END IF;
    	exception
				when others then
	   			dbms_output.put_line('添加保存邮件功能点  exception others');
END;

-----------------------------------------添加发送邮件功能点--2016-09-30----------------------------- 
BEGIN   
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(246,'发送邮件','sendMail',2,'/styles/default/images/resicon/ico_117.gif',244,'/oa/mail/outMail/send.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=246;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1297,-1,246);
	    	commit;
  		  END IF;
 	END IF;
 		exception
				when others then
	   			dbms_output.put_line('添加发送邮件功能点  exception others');
END; 

-----------------------------------------添加邮箱配置管理功能点--2016-09-30-----------------------------
BEGIN   
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mailManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(247,'邮箱配置管理','mailManage',3,'/styles/default/images/resicon/o_13.png',240,'/oa/mail/outMailUserSeting/list.do',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=247;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1298,-1,247);
	    	commit;
     	  END IF;
 	 END IF;
 	 	exception
				when others then
	   			dbms_output.put_line('添加邮箱配置管理功能点  exception others');
END; 

-----------------------------------------添加添加邮箱功能点--2016-09-30-----------------------------
BEGIN  
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'addMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(248,'添加邮箱','addMail',1,'/styles/default/images/resicon/add.png',247,'/oa/mail/outMailUserSeting/edit.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=248;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1299,-1,248);
	    	commit;
 		  END IF;
 	 END IF;
 	 	exception
				when others then
	   			dbms_output.put_line('添加添加邮箱功能点  exception others');
END; 

-----------------------------------------添加设置默认邮箱功能点--2016-09-30-----------------------------
BEGIN 
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'setDefault';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(249,'设置默认邮箱','setDefault',2,'/styles/default/images/resicon/3.png',247,'/oa/mail/outMailUserSeting/del.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=249;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1300,-1,249);
	    	commit;
 		  END IF;
 	 END IF;
 	 	exception
				when others then
	   			dbms_output.put_line('添加设置默认邮箱功能点  exception others');
END;

-----------------------------------------添加编辑邮箱配置功能点--2016-09-30-----------------------------
BEGIN   
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'editOutmail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(250,'编辑邮箱配置','editOutmail',3,'/styles/default/images/resicon/2.png',247,'/oa/mail/outMailUserSeting/edit.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=250;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1301,-1,250);
	    	commit;
  		  END IF;
 	 END IF;
 	 	exception
				when others then
	   			dbms_output.put_line('添加编辑邮箱配置功能点  exception others');
END; 
 
-----------------------------------------添加删除邮箱功能点--2016-09-30----------------------------- 
BEGIN   
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'deloutMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(251,'删除邮箱','deloutMail',4,'/styles/default/images/resicon/del.png',247,'/oa/mail/outMailUserSeting/del.do',0,0,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=251;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1302,-1,251);
	    	commit;
     	  END IF;
 	END IF;
 		exception
				when others then
	   			dbms_output.put_line('添加删除邮箱功能点  exception others');
END;

-----------------------------------------添加删除邮箱功能点--2016-09-30----------------------------- 
BEGIN  
SELECT COUNT(*) into countt FROM cwm_sys_org WHERE ORGID = '1';
	IF(countt=0) THEN  
			insert into cwm_sys_org(ORGID,DEMID,ORGNAME, ORGDESC,ORGSUPID, PATH, DEPTH, ORGTYPE,CREATORID, CREATETIME, UPDATEID, UPDATETIME, SN, FROMTYPE, ORGPATHNAME, ISDELETE, CODE, COMPANY, ORGSTAFF, COMPANYID)
			values(1, 1, '奥蓝托', '无锡奥蓝托', 0, '1.', 1, 1, NULL, to_date('2013-01-21','yyyy-MM-dd'), NULL, to_date('2013-01-21','yyyy-MM-dd'), 100, 0, '奥蓝托', 0, 'A001', NULL, NULL, NULL);
	END IF;
END;

--------------------------------------插入cwm_sys_parameter数据---2016-11-14-----------------------
BEGIN  
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showPos';
	IF(countt=0) THEN  
			insert into CWM_SYS_PARAMETER (ID,NAME,DATATYPE,VALUE,DESCRIPTION) 
			values(22,'userDialog.showPos','0','1','显示岗位查找(1:显示,0:不显示)');
	END IF;
END;

BEGIN  
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showRole';
	IF(countt=0) THEN  
			insert into CWM_SYS_PARAMETER (ID,NAME,DATATYPE,VALUE,DESCRIPTION) 
			values(23,'userDialog.showRole','0','1','显示角色查找(1:显示,0:不显示)');
	END IF;
END;

BEGIN  
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showOnlineUser';
	IF(countt=0) THEN  
			insert into CWM_SYS_PARAMETER (ID,NAME,DATATYPE,VALUE,DESCRIPTION) 
			values(24,'userDialog.showOnlineUser','0','1','显示在线用户(1:显示,0:不显示)');
	END IF;
END;

BEGIN  
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'task.WarnLevel';
	IF(countt=0) THEN  
			insert into CWM_SYS_PARAMETER (ID,NAME,DATATYPE,VALUE,DESCRIPTION) 
			values(28,'task.WarnLevel','0','[{level:1,name:"黄色预警",color:"yellow"},{level:2,name:"红色预警",color:"red"}]','配置任务预警类型');
	END IF;
END;


BEGIN  
SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '根据外键获取关联表的数据。';
	IF(countt=0) THEN  
			insert into cwm_sys_script(ID,NAME,SCRIPT,CATEGORY,MEMO) 
			values('10000000520007', '根据外键获取关联表的数据。', 'return scriptImpl.getRelDataByFk( String relTableName,String fkFieldName,Object fkFieldValue,String orderFieldName); ', '1', '根据外键获取关联表的数据。');
	END IF;
END;

BEGIN  
SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '根据主表记录id，获取关联表记录中，用户字段值集合。';
	IF(countt=0) THEN  
			insert into cwm_sys_script(ID,NAME,SCRIPT,CATEGORY,MEMO) 
			values('10000000520008', '根据主表记录id，获取关联表记录中，用户字段值集合。', 'return scriptImpl.getRelTableUser( String relTableName, String fkFieldName, String businessKey,String userField);', '1', '根据主表记录id，获取关联表记录中，用户字段值集合。');
	END IF;
END;

BEGIN  
SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '获取当前流程变量，中存储的关联表的用户字段的值。';
	IF(countt=0) THEN  
			insert into cwm_sys_script(ID,NAME,SCRIPT,CATEGORY,MEMO) 
			values('10000000520009', '获取当前流程变量，中存储的关联表的用户字段的值。', 'return scriptImpl.getRelFieldUserByVrocessCmdVars( String relTableName, String field);', '1', '获取当前流程变量，中存储的关联表的用户字段的值。');
	END IF;
END;


 -----------------------------------------添加我的草稿功能点--2016-11-16-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'myDrafts';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS,SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(157,'我的草稿','myDrafts',2,'/styles/default/images/resicon/o_5.png',151,'/oa/flow/processRun/myForm.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=157;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1400,-1,157);
    	commit;
     END IF;
    COMMIT;
  END IF;
	exception
		when others then
   		dbms_output.put_line('添加我的草稿功能点  exception others');
END;

BEGIN
	update cwm_sys_paur set PAURVALUE='default' where PAURVALUE='blue';
	commit;
END;

 -----------------------------------------添加系统主题参数--2016-11-17-----------------------------
BEGIN
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'SYS_THEME';
 IF(countt=0) THEN
 	Insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values(6,'SYS_THEME','0','default','默认为default。可填入blue，gray，green三色定制。一旦更改该系统固定主题色'); 
   	commit;
 END IF;
END;
 -----------------------------------------附件管理功能--2016-12-16-----------------------------
BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SysFileAttachView';
	IF(countt>0) THEN  
		update CWM_SYS_RES set DEFAULTURL='/oa/system/sysFile/list.do',RESNAME='附件管理' where DEFAULTURL='/js/system/AdminSysFileAttachView.do';
		commit;
     END IF;
END;

 -----------------------------------------添加系统UI参数--2017-02-23-----------------------------
BEGIN
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'SYS_UITYPE';
 IF(countt=0) THEN
 	Insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values(8,'SYS_UITYPE','0','0','系统UI界面：0为旧风格，1为HZY风格'); 
   	commit;
 END IF;
END;

-----------------------------------------添加门户管理功能点--2017-03-07-----------------------------
BEGIN
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'indexManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3200,'门户管理','indexManage',6,'',3,'',1,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3200;
	 		IF(countt=0) THEN
	    		insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3200,-1,3200);
	    		commit;
     	 	 END IF;
  	END IF;
END;
-----------------------------------------添加门户布局管理功能点--2017-03-07-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'idexPortal';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3210,'门户布局管理','idexPortal',1,'',3200,'/oa/portal/insPortal/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3210;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3210,-1,3210);
	    	commit;
     	  END IF;
 	END IF;
END;
-----------------------------------------添加栏目类型功能点--2017-03-07-----------------------------
BEGIN    
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'columnType';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3220,'栏目类型','columnType',2,'',3200,'/oa/portal/insColType/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3220;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3220,-1,3220);
	    	commit;
     	  END IF;
 	END IF;
END;
-----------------------------------------添加布局栏目功能点--2017-03-07-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'portalColumn';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3230,'布局栏目','portalColumn',3,'',3200,'/oa/portal/insColumn/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3230;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3230,-1,3230);
	    	commit;
     	  END IF;
 	END IF;	
END;
-----------------------------------------添加新闻公告管理功能点--2017-03-13-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'newsManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3240,'新闻公告管理','newsManage',4,'',3200,'/oa/portal/insNews/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3240;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3240,-1,3240);
	    	commit;
     	  END IF;
 	END IF;	
END;

-----------------------------------------添加新闻公告评论功能点--2017-03-13-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'newsComment';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3250,'新闻评论管理','newsComment',5,'',3200,'/oa/portal/insNewsCm/list.do',0,1,1,0,null);
		commit;
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3250;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3250,-1,3250);
	    	commit;
     	  END IF;
 	END IF;	
END;


-----------------------------------------初始化所有首页相关数据--2017-03-18-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM INS_COL_TYPE;
	IF(countt=0) THEN
	
	INSERT INTO ins_col_type VALUES (1, '待办事宜', 'myTask', '/oa/flow/task/pendingMatters.do', '/oa/flow/task/pendingMatters.do', 'TEMPLATE', 'pendingMatters', '待办事宜', 'icon-task', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (10000000290008, '日常办公', 'Box', '/oa/console/columnBox.do', '/oa/console/columnBox.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (10000000370000, '展示', 'slide', '/oa/console/columnSlide.do', '/oa/console/columnSlide.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (10000000370003, '日常办公', 'dailyWork', '/oa/console/columnBox.do', '/oa/console/columnBox.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (10000000370016, '任务列表', 'taskList', '/oa/console/columnTask.do', '/oa/console/columnTask.do', 'TEMPLATE', 'taskListData', '任务列表', '', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (2, '计划', 'Plan', '/oa/console/columnPlan.do', '/oa/console/columnPlan.do', 'URL', 'msg', 'msg', 'icon-commute', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (210000000066005, '联系人', 'Linkman', '/oa/console/columnLinkman.do', '/oa/console/columnLinkman.do', 'URL', 'kdDoc', 'kdDoc', 'icon-task', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (210000000202022, '会议', 'Meeting', '/oa/console/columnMeeting.do', '/oa/console/columnMeeting.do', 'URL', 'doc', 'doc', 'icon-document', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (3, '日程', 'Calendar', '/oa/calendar/agendaCalendar.do', '/oa/calendar/agendaCalendar.do', 'URL', 'calendar', 'calendar', 'icon-mail', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (4, '项目', 'Project', '/oa/console/columnProject.do', '/oa/console/columnProject.do', 'URL', 'inMail', 'inMail', 'icon-mail', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (5, '新闻、公告', 'news', '/oa/portal/insNews/list.do?pageSize={pageSize}', '/oa/portal/insNews/list.do', 'TEMPLATE', 'news', '新闻公告', 'icon-detail', '', null, '-1', null, '-1', '0');
	INSERT INTO ins_col_type VALUES (6, '规章制度', 'Rule', '/oa/console/columnRules.do', '/oa/console/columnRules.do', 'URL', 'bpmSolutionPortal', 'bpmSolutionPortal', 'icon-start', '', null, '-1', null, '-1', '0');
	COMMIT;
	
	INSERT INTO ins_column VALUES (10000000370001, 10000000370000, '展示', 'slidec', null, 'ENABLED', '10', 'YES', '展示', '0', '-1', null, '-1', null, '360');
	INSERT INTO ins_column VALUES (10000000370004, 10000000370003, '日常办公', 'dailWork', null, 'ENABLED', '5', 'YES', '日常办公', '0', '-1', null, '-1', null, '360');
	INSERT INTO ins_column VALUES (10000000370017, 10000000370016, '任务列表', 'rwlb', null, 'ENABLED', '6', 'NO', '任务列表', '0', '-1', null, '-1', null, '360');
	INSERT INTO ins_column VALUES (20000000004002, 5, '新闻公告', 'news', null, 'ENABLED', '7', 'NO', '新闻、公告', '0', '-1', null, '-1', null, '360');
	INSERT INTO ins_column VALUES (20000000007001, 2, '新上计划', 'myMsg', null, 'ENABLED', '7', 'NO', '计划', '0', '-1', null, '-1', null, '300');
	INSERT INTO ins_column VALUES (20000000014001, 4, '新上项目', 'innerMail', null, 'ENABLED', '7', 'YES', '项目', '0', '-1', null, '-1', null, '300');
	INSERT INTO ins_column VALUES (20000000099097, 6, '规章制度', 'solApply', null, 'ENABLED', '7', 'YES', '规章制度', '0', '-1', null, '-1', null, '300');
	INSERT INTO ins_column VALUES (20000000167026, 1, '我的待办', 'MyTask', null, 'ENABLED', '5', 'YES', '待办事宜', '0', '-1', null, '-1', null, '415');
	INSERT INTO ins_column VALUES (210000000066006, 210000000066005, '联系人', 'kdDoc', null, 'ENABLED', '5', 'YES', '联系人', '0', '-1', null, '-1', null, '300');
	INSERT INTO ins_column VALUES (210000000202023, 210000000202022, '会议安排', 'document', null, 'ENABLED', '7', 'YES', '会议', '0', '-1', null, '-1', null, '300');
	INSERT INTO ins_column VALUES (210000000335008, 3, '日程', 'calendar', null, 'ENABLED', '5', 'YES', '日程', '0', '-1', null, '-1', null, '400');
	COMMIT;
	
	INSERT INTO ins_portal VALUES (1, '公司全局', 'GLOBAL-ORG', '3', '350,100%,350', 'YES', null, null, '0', '-1', null, '-1', null);
	INSERT INTO ins_portal VALUES (2, '个人全局', 'GLOBAL-PERSONAL', '2', '800,100%', 'YES', '', null, '0', '-1', null, '-1', null);
	COMMIT;  	
	
	INSERT INTO ins_port_col VALUES (10000000540004, 1, 210000000335008, null, '400', null, 'px', '0', '11', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540005, 1, 210000000202023, null, '300', null, 'px', '0', '12', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540006, 1, 20000000099097, null, '300', null, 'px', '0', '15', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540007, 1, 20000000014001, null, '300', null, 'px', '0', '16', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540008, 1, 10000000370017, null, '360', null, 'px', '0', '19', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540009, 1, 10000000370004, null, '360', null, 'px', '0', '20', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540010, 1, 10000000370001, null, '360', null, 'px', '0', '21', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540011, 2, 210000000335008, null, '400', null, 'px', '1', '7', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540012, 2, 210000000202023, null, '300', null, 'px', '1', '9', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540013, 2, 210000000066006, null, '300', null, 'px', '0', '3', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540014, 2, 20000000099097, null, '300', null, 'px', '1', '8', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540015, 2, 20000000014001, null, '300', null, 'px', '0', '4', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540016, 2, 20000000007001, null, '300', null, 'px', '1', '10', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540017, 2, 10000000370017, null, '360', null, 'px', '0', '2', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540018, 2, 10000000370004, null, '360', null, 'px', '0', '1', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540019, 2, 10000000370001, null, '360', null, 'px', '0', '0', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (10000000540020, 2, 20000000004002, null, '360', null, 'px', '1', '6', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (240000000006044, 1, 20000000004002, null, '600', null, 'px', '0', '1', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (240000000006045, 1, 20000000007001, null, '500', null, 'px', '0', '0', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (240000000006046, 1, 20000000167026, null, '100', null, 'px', '0', '2', '0', '-1', null, '-1', null);
	INSERT INTO ins_port_col VALUES (240000000006047, 1, 210000000066006, null, '300', null, 'px', '1', '3', '0', '-1', null, '-1', null);	
	COMMIT;
 	END IF;	
END;



-----------------------------------------添加日程管理功能点--2017-03-25-----------------------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'agenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3260,'日程管理','agenda',3,'',281,'',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3260;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3260,-1,3260);
     END IF;
  END IF;
END;  
-----------------------------------------添加我的日程功能点--2017-03-25-----------------------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'myAgenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3270,'我的日程','myAgenda',1,'',3260,'/oa/calendar/agenda/MyCalendar.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3270;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3270,-1,3270);
     END IF;
  END IF;
END;   
-----------------------------------------添加工作交流功能点--2017-03-25-----------------------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'agendaMsg';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3280,'日程管理','agendaMsg',2,'',3260,'/oa/calendar/agendaMsg/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3280;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3280,-1,3280);
     END IF;
  END IF;
END;   
-----------------------------------------添加查询日程功能点--2017-03-25-----------------------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'serachAgenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3290,'查询日程','serachAgenda',3,'',3260,'/oa/calendar/agenda/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3290;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3290,-1,3290);
     END IF;
  END IF;
END;   
  
BEGIN
SELECT COUNT(*) into countt FROM ins_col_type WHERE KEY_= 'Calendar' AND TEMP_ID_='outMail';
	IF(countt>0) THEN  
	  	UPDATE ins_col_type 
			SET URL_='/oa/calendar/agendaCalendar.do', MORE_URL_='/oa/calendar/agendaCalendar.do', 
			TEMP_ID_='calendar', TEMP_NAME_='calendar' WHERE (KEY_='Calendar');
	END IF;
END;

-----------------------------------------修改门户布局管理功能点--2017-04-06-----------------------------
BEGIN  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE DEFAULTURL = '/oa/portal/insPortal/global.do';
	IF(countt>0) THEN  
		UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/portal/insPortal/list.do' WHERE DEFAULTURL = '/oa/portal/insPortal/global.do';
 	END IF;
END;


 -----------------------------------------添加登陆页面背景图片参数--2017-05-03-----------------------------
BEGIN
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'SYS_LOGIN_PNG';
 IF(countt=0) THEN
 	Insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values(3001,'SYS_LOGIN_PNG','0','/styles/images/login/loginHZY1.jpg','登陆页面背景图片'); 
   	commit;
 END IF;
END;

 -----------------------------------------添加登陆页面标语参数--2017-05-03-----------------------------
BEGIN
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'SYS_LOGIN_LOG';
 IF(countt=0) THEN
 	Insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values(3002,'SYS_LOGIN_LOG','0','/styles/images/login/banner.png','登陆页面标语，警示语图片'); 
   	commit;
 END IF;
END;


COMMIT;
END synData_yangbo;
