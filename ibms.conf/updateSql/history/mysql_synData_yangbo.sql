DROP PROCEDURE IF EXISTS prod_data_yangbo;
CREATE PROCEDURE prod_data_yangbo(spacename varchar(100))
BEGIN
DECLARE countt NUMERIC;


DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;
-- --------------do my sql------------------
update CWM_SYS_GLTYPE A set A.PARENTID=(select B.TYPEKEYID from CWM_SYS_TYPEKEY B where A.CATKEY=B.TYPEKEY) where A.DEPTH=1;

 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE CWM_SYS_RES.RESNAME='我的邮箱');
 DELETE FROM CWM_SYS_RES WHERE RESNAME='我的邮箱';
 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE CWM_SYS_RES.RESNAME='发邮件');
 DELETE FROM CWM_SYS_RES WHERE RESNAME='发邮件';
 
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'gradeManager';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(188,'分级组织管理','gradeManager',7,'/styles/default/images/resicon/s_o_1.png',3,'/oa/system/grade/manage.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=188;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1286,-1,188);
     END IF;
  END IF;
  

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'InnerMessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(230,'站内信','InnerMessage',1,'/styles/default/images/resicon/icon_tab3.png',281,'/oa/system/messageSend/form.do',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=230;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1287,-1,230);
     END IF;
  END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'readmessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(231,'收到的消息','readmessage',1,'/styles/default/images/resicon/vote.gif',230,'/oa/system/messageReceiver/list.do',0,1,1,0,null);
	  SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=231;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1288,-1,231);
     END IF;
  END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendmessage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(232,'已发送消息','sendmessage',2,'/styles/default/images/resicon/o_16.png',230,0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=232;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1289,-1,232);
     END IF;
  END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendMsg';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(233,'发送消息','sendMsg',3,'/styles/default/images/resicon/o_16.png',230,'/oa/system/messageSend/edit.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=233;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1290,-1,233);
     END IF;
 END IF;



 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(240,'邮件管理','mail',2,'/styles/default/images/resicon/site.gif',281,'/oa/system/messageSend/form.do',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=240;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1291,-1,240);
     END IF;
  END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'outMailAll';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(241,'外部邮件','outMailAll',1,'/styles/default/images/resicon/o_14.png',240,'/oa/mail/outMail/warn.do',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=241;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1292,-1,241);
     END IF;
 END IF;  
 
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'lookMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(242,'查看邮件','lookMail',1,'/styles/default/images/resicon/ico_113.gif',241,'/oa/mail/outMail/get.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=242;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1293,-1,242);
     END IF;
 END IF;  
 
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'delMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(243,'删除邮件','delMail',2,'/styles/default/images/resicon/del.png',241,'/oa/mail/outMail/del.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=243;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1294,-1,243);
     END IF;
 END IF;  

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mailAdd';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(244,'新建邮件','mailAdd',2,'/styles/default/images/resicon/0_15.png',240,'/oa/mail/outMail/edit.do',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=244;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1295,-1,244);
     END IF;
 END IF;
 
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'saveOutmail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(245,'保存邮件','saveOutmail',1,'/styles/default/images/resicon/tree_file.gif',244,'/oa/mail/outMail/send.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=245;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1296,-1,245);
     END IF;
 END IF;

 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sendMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(246,'发送邮件','sendMail',2,'/styles/default/images/resicon/ico_117.gif',244,'/oa/mail/outMail/send.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=246;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1297,-1,246);
     END IF;
 END IF;
  
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'mailManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(247,'邮箱配置管理','mailManage',3,'/styles/default/images/resicon/o_13.png',240,'/oa/mail/outMailUserSeting/list.do',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=247;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1298,-1,247);
     END IF;
 END IF; 
  
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'addMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(248,'添加邮箱','addMail',1,'/styles/default/images/resicon/add.png',247,'/oa/mail/outMailUserSeting/edit.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=248;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1299,-1,248);
     END IF;
 END IF;  
  
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'setDefault';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(249,'设置默认邮箱','setDefault',2,'/styles/default/images/resicon/3.png',247,'/oa/mail/outMailUserSeting/del.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=249;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1300,-1,249);
     END IF;
 END IF;    
  
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'editOutmail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(250,'编辑邮箱配置','editOutmail',3,'/styles/default/images/resicon/2.png',247,'/oa/mail/outMailUserSeting/edit.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=250;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1301,-1,250);
     END IF;
 END IF;    
  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'deloutMail';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(251,'删除邮箱','deloutMail',4,'/styles/default/images/resicon/del.png',247,'/oa/mail/outMailUserSeting/del.do',0,0,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=251;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1302,-1,251);
     END IF;
 END IF;    
  
  
SELECT COUNT(*) into countt FROM CWM_SYS_TYPEKEY WHERE TYPEKEY = 'ATTACH_TYPE';
IF(countt>0) THEN  
	UPDATE  CWM_SYS_TYPEKEY SET TYPE='1' WHERE TYPEKEY = 'ATTACH_TYPE';
END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showPos';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)
		values(22,'userDialog.showPos','0','1','显示岗位查找(1:显示,0:不显示)');
 	END IF;
 	
 	
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showRole';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)
		values(23,'userDialog.showRole','0','1','显示角色查找(1:显示,0:不显示)');
 	END IF; 	

SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'userDialog.showOnlineUser';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)
		values(24,'userDialog.showOnlineUser','0','1','显示在线用户(1:显示,0:不显示)');
 	END IF;
 	
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'task.WarnLevel';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)
		values(28,'task.WarnLevel','0','[{level:1,name:"黄色预警",color:"yellow"},{level:2,name:"红色预警",color:"red"}]','配置任务预警类型');
 	END IF; 
 	
 	
SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '根据外键获取关联表的数据。';
	IF(countt=0) THEN  
		insert into cwm_sys_script(`ID`, `NAME`, `SCRIPT`, `CATEGORY`, `MEMO`)
		values('10000000520007', '根据外键获取关联表的数据。', 'return scriptImpl.getRelDataByFk( String relTableName,String fkFieldName,Object fkFieldValue,String orderFieldName); ', '1', '根据外键获取关联表的数据。');
 	END IF;

SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '根据主表记录id，获取关联表记录中，用户字段值集合。';
	IF(countt=0) THEN  
		insert into cwm_sys_script(`ID`, `NAME`, `SCRIPT`, `CATEGORY`, `MEMO`)
		values('10000000520008', '根据主表记录id，获取关联表记录中，用户字段值集合。', 'return scriptImpl.getRelTableUser( String relTableName, String fkFieldName, String businessKey,String userField);', '1', '根据主表记录id，获取关联表记录中，用户字段值集合。');
 	END IF;
SELECT COUNT(*) into countt FROM cwm_sys_script WHERE NAME = '获取当前流程变量，中存储的关联表的用户字段的值。';
	IF(countt=0) THEN  
		insert into cwm_sys_script(`ID`, `NAME`, `SCRIPT`, `CATEGORY`, `MEMO`)
		values('10000000520009', '获取当前流程变量，中存储的关联表的用户字段的值。', 'return scriptImpl.getRelFieldUserByVrocessCmdVars( String relTableName, String field);', '1', '获取当前流程变量，中存储的关联表的用户字段的值。');
 	END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'myDrafts';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(157,'我的草稿','myDrafts',2,'/styles/default/images/resicon/o_5.png',151,'/oa/flow/processRun/myForm.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=157;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1400,-1,157);
     END IF;
 END IF;
 
 
update cwm_sys_paur set PAURVALUE='default' where PAURVALUE='blue';
 
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'SYS_THEME';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER values(6,'SYS_THEME','0','default','默认为default。可填入blue，gray，green三色定制。一旦更改该系统固定主题色');
 	END IF;
-- --------------附件管理功能--2016-12-16-----------------------------
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE NAME = 'SysFileAttachView';
	IF(countt>0) THEN  
		update CWM_SYS_RES set DEFAULTURL='/oa/system/sysFile/list.do',RESNAME='附件管理' where DEFAULTURL='/js/system/AdminSysFileAttachView.do';
	END IF;
 	
-- --------------添加系统UI参数--2017-02-23----------------------------- 	
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'SYS_UITYPE';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER values(8,'SYS_UITYPE','0','0','系统UI界面：0为旧风格，1为HZY风格');
 	END IF;
 	
 	
 	
-- ---------------------------------------添加门户管理功能点--2017-03-07-----------------------------
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'indexManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3200,'门户管理','indexManage',6,'',3,'',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3200;
	 		IF(countt=0) THEN
	    		insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3200,-1,3200);
     	 	 END IF;
  	END IF;

-- ---------------------------------------添加门户布局管理功能点--2017-03-07-----------------------------  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'idexPortal';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3210,'门户布局管理','idexPortal',1,'',3200,'/oa/portal/insPortal/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3210;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3210,-1,3210);
     	  END IF;
 	END IF;

-- ---------------------------------------添加栏目类型功能点--2017-03-07-----------------------------  
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'columnType';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3220,'栏目类型','columnType',2,'',3200,'/oa/portal/insColType/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3220;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3220,-1,3220);
     	  END IF;
 	END IF;

-- ---------------------------------------添加布局栏目功能点--2017-03-07----------------------------- 
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'portalColumn';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3230,'布局栏目','portalColumn',3,'',3200,'/oa/portal/insColumn/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3230;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3230,-1,3230);
     	  END IF;
 	END IF;	

 	
-- ---------------------------------------添加新闻公告管理功能点--2017-03-13-----------------------------
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'newsManage';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3240,'新闻公告管理','newsManage',4,'',3200,'/oa/portal/insNews/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3240;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3240,-1,3240);
     	  END IF;
 	END IF;	

-- ---------------------------------------添加新闻公告评论功能点--2017-03-13-----------------------------
  SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'newsComment';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3250,'新闻评论管理','newsComment',5,'',3200,'/oa/portal/insNewsCm/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3250;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3250,-1,3250);
     	  END IF;
 	END IF;	
 	
 	
-- ---------------------------------------添加日程管理功能点--2017-03-25-----------------------------
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'agenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3260,'日程管理','agenda',3,'',281,'',1,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3260;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3260,-1,3260);
     END IF;
  END IF;
  
 -- ---------------------------------------添加我的日程功能点--2017-03-25-----------------------------
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'myAgenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3270,'我的日程','myAgenda',1,'',3260,'/oa/calendar/agenda/MyCalendar.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3270;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3270,-1,3270);
     END IF;
  END IF;
  
  -- ---------------------------------------添加工作交流功能点--2017-03-25-----------------------------
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'agendaMsg';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3280,'日程管理','agendaMsg',2,'',3260,'/oa/calendar/agendaMsg/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3280;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3280,-1,3280);
     END IF;
  END IF;
  
  -- ---------------------------------------添加查询日程功能点--2017-03-25-----------------------------
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'serachAgenda';
	IF(countt=0) THEN  
		insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
		values(3290,'查询日程','serachAgenda',3,'',3260,'/oa/calendar/agenda/list.do',0,1,1,0,null);
		 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=3290;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3290,-1,3290);
     END IF;
  END IF;
  
SELECT COUNT(*) into countt FROM ins_col_type WHERE KEY_= 'Calendar' AND TEMP_ID_='outMail';
IF(countt>0) THEN  
  	UPDATE `ins_col_type` SET `TYPE_ID_`='3', `NAME_`='日程', `KEY_`='Calendar', `URL_`='/oa/calendar/agendaCalendar.do', `MORE_URL_`='/oa/calendar/agendaCalendar.do', `LOAD_TYPE_`='URL', `TEMP_ID_`='calendar', `TEMP_NAME_`='calendar', `ICON_CLS_`='icon-mail', `MEMO_`='', `CREATE_TIME_`=NULL, `CREATE_BY_`='-1', `UPDATE_TIME_`='2017-03-21 16:41:29', `UPDATE_BY_`='-1', `ORG_ID_`='0' WHERE (`TYPE_ID_`='3');
END IF;
 -- ---------------------------------------修改门户布局管理功能点--2017-04-06-----------------------------
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE DEFAULTURL = '/oa/portal/insPortal/global.do';
	IF(countt>0) THEN  
		UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/portal/insPortal/list.do' WHERE DEFAULTURL = '/oa/portal/insPortal/global.do';
  	END IF;
  	
-- --------------添加登陆页面背景图片参数--2017-05-03----------------------------- 	
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'SYS_LOGIN_PNG';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER values(3001,'SYS_LOGIN_PNG','0','/styles/images/login/loginHZY1.jpg','登陆页面背景图片');
 	END IF;
-- --------------添加登陆页面标语参数--2017-05-03----------------------------- 	
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'SYS_LOGIN_LOG';
	IF(countt=0) THEN  
		insert into CWM_SYS_PARAMETER values(3002,'SYS_LOGIN_LOG','0','/styles/images/login/banner.png','登陆页面标语，警示语图片');
 	END IF; 	
 	
-- --------------do my sql end--------------
COMMIT;
END;
CALL prod_data_yangbo(@spacename);

