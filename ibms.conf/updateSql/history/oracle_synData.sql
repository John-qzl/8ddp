/**************************************************

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




CREATE OR REPLACE PROCEDURE synData (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
testable varchar2(1000);
BEGIN
 --------------------------删表DEMENSION-----------------------------------------------------
BEGIN
select count(*) into countt from tabs where table_name = 'DEMENSION';
 IF(countt>0) THEN
   execute immediate 'insert into CWM_SYS_DEMENSION (DEMID ,DEMNAME ,DEMDESC)
   select DEM_ID ,DEM_NAME,DEM_DESC from DEMENSION';
   commit;
   execute immediate 'drop table DEMENSION';
   commit;
 END IF;
 END;
 ---------------------------删表POSITION----------------------------------------------------
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
 -------------department表数据迁移到cwm_sys_org表中,同时删除department表-----------

BEGIN
 select count(*) into countt from tabs where table_name = 'CWM_SYS_DEPARTMENT';
 IF(countt>0) THEN
 	select count(*) into countt from tabs where table_name = 'CWM_SYS_ORG';
 	 IF(countt=1) THEN
  execute immediate 'insert into CWM_SYS_ORG ( ORGID ,DEMID , ORGNAME , ORGDESC , ORGSUPID ,PATH  , DEPTH   , ORGTYPE  ,  CREATORID ,CREATETIME  ,UPDATEID,  UPDATETIME , SN , FROMTYPE , ORGPATHNAME, ISDELETE , CODE  , COMPANYID , COMPANY , ORGSTAFF)
  select ID ,DEM_ID,NAME,NOTES,PID ,PATH ,DEPTH ,DEP_TYPE ,CREATOR_ID ,CREATETIME,UPDATE_ID ,UPDATETIME,SN,0,null,0,CODE,null,null,null  from CWM_SYS_DEPARTMENT';
  commit;
  execute immediate 'drop table CWM_SYS_DEPARTMENT';
  commit;
  END IF;
 END IF;
END;

-----------加功能点：首页管理-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SysIndexManage';
 IF(countt=0) THEN
    insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID, SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(9, 'SysIndexManage', '首页管理', 3, 16, '/images/function/mid/gnome-desktop-config.png',  1,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=9;
    IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1290,-1,9);
    	COMMIT;
    END IF;
  END IF;
 exception
   when others then
   dbms_output.put_line('首页管理 SysIndexManage  exception others');
END;

-----------加功能点：布局管理-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SysIndexLayoutManage';
 IF(countt=0) THEN
    insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(26, 'SysIndexLayoutManage', '布局管理',9, '/oa/system/sysIndexLayoutManage/list.do',  3, '/images/function/mid/htop.png', 0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=26;
    IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1291,-1,26);
    	commit;
    END IF;
 END IF;   
 exception
   when others then
   dbms_output.put_line('布局管理 SysIndexLayoutManage  exception others');
END;

-----------加功能点：我的首页布局-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sysIndexMyLayout';
 IF(countt=0) THEN
    insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(28, 'sysIndexMyLayout', '我的首页布局',9, '/oa/system/sysIndexMyLayout/design.do',  4, '/images/function/mid/openofficeorg3.0-calc.png',  0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=28;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1292,-1,28);
    	commit;
     END IF;
 END IF;
 exception
   when others then
   dbms_output.put_line('我的首页布局 sysIndexMyLayout  exception others');
END;

-----------加功能点：首页栏目-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SysIndexColumn';
 IF(countt=0) THEN
    insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(35, 'SysIndexColumn', '首页栏目',9, '/oa/system/sysIndexColumn/list.do',  1, '/images/function/mid/fusion-icon.png',   0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=35;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1293,-1,35);
    	commit;
     END IF;
  END IF;
 exception
   when others then
   dbms_output.put_line('首页栏目 SysIndexColumn  exception others');
END;

-----------加功能点：首页布局-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SysIndexLayout';
 IF(countt=0) THEN
    insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(38, 'SysIndexLayout', '首页布局',9, '/oa/system/sysIndexLayout/list.do',  2, '/images/function/mid/media-player-banshee.png',   0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=38;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1294,-1,38);
    	commit;
     END IF;
  END IF;
 exception
   when others then
   dbms_output.put_line('首页布局 SysIndexLayout  exception others');
END;

-----------加功能点：参数管理-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'ParametersView';
 IF(countt=0) THEN
    Insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    Values(39, 'ParametersView', '参数管理', 3, '/js/system/ParameterView', 11, '/images/function/mid/default.png', 0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=39;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1295,-1,39);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('参数管理 ParametersView  exception others');
END;

-----------加功能点：备份恢复-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'BackAndRestore';
 IF(countt=0) THEN
    Insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,DEFAULTURL,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    Values(45, 'BackAndRestore', '备份恢复', 3, '/js/system/BackAndRestore', 15, '/images/function/big/a7xpg.png',0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=45;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1296,-1,45);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('备份恢复 BackAndRestore  exception others');
END;


-----------加功能点：设置中心-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'settingcenter';
 IF(countt=0) THEN
    INSERT INTO CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID,SN, ICON,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    Values(46, 'settingcenter',  '设置中心',2, 2, '/styles/default/images/resicon/i3.png',  1,1,1,null,null);
     SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=46;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1297,-1,46);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('设置中心 settingcenter  exception others');
END;
-----------加功能点：常用语设置-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'taskApproval';
 IF(countt=0) THEN
    INSERT INTO CWM_SYS_RES (RESID, ALIAS, RESNAME, PARENTID,SN, ICON,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    Values(47,  'taskApproval','常用语设置', 17, 1, '/styles/default/images/resicon/s_p_2.png',  '/oa/flow/taskApprovalItems/list.do?isAdmin=0',0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=47;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1298,-1,47);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('常用语设置 taskApproval  exception others');
END;
-----------加功能点：流程代理授权功能点-----------------

BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'SYS_USER_AGENT';
 IF(countt=0) THEN
    INSERT INTO CWM_SYS_RES (RESID, ALIAS, RESNAME, PARENTID,SN, ICON,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    Values(48,  'SYS_USER_AGENT','流程代理授权', 17, 3, '/styles/default/images/resicon/3.png',  '/oa/flow/agentSetting/list.do', 0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=48;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1299,-1,48);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('流程代理授权 SYS_USER_AGENT  exception others');
END;
-----------加功能点：常用语设置-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'admintaskApproval';
 IF(countt=0) THEN
INSERT INTO CWM_SYS_RES (RESID, RESNAME, ALIAS, SN, ICON,PARENTID,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)Values
(50, '常用语设置', 'admintaskApproval', 1, '/styles/default/images/resicon/f_5.png', 111, '/oa/flow/taskApprovalItems/list.do?isAdmin=1', 0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=50;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1300,-1,50);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('常用语设置 admintaskApproval  exception others');
END;
-----------加功能点：流程历史管理-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'processrunhistroy';
 IF(countt=0) THEN
INSERT INTO CWM_SYS_RES (RESID, RESNAME, ALIAS, SN, ICON,PARENTID,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)Values
(51, '流程历史管理', 'processrunhistroy', 6, '/styles/default/images/resicon/p_3.png', 111, '/oa/flow/processRun/history.do', 0,1,1,null,null);
     SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=51;
     IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1301,-1,51);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('流程历史管理 processrunhistroy  exception others');
END;
-----------加功能点：流程操作日志-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'bpmRunLog';
 IF(countt=0) THEN
INSERT INTO CWM_SYS_RES (RESID, RESNAME, ALIAS, SN, ICON,PARENTID,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)Values
(52, '流程操作日志', 'bpmRunLog', 7, '/styles/default/images/resicon/p_5.png', 111, '/oa/flow/runLog/list.do',  0,1,1,null,null);
      SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=52;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1302,-1,52);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('流程操作日志 bpmRunLog  exception others');
END;
-----------加功能点：转办代理实例-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'agentDelePro';
 IF(countt=0) THEN
    INSERT INTO CWM_SYS_RES (RESID, RESNAME, ALIAS, SN, ICON,PARENTID,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)Values
    (55, '转办代理实例', 'agentDelePro', 8, '/styles/default/images/resicon/p_9.png', 111, '/oa/flow/taskExe/list.do', 0,1,1,null,null);
   SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=55;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1303,-1,55);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('常用语设置 admintaskApproval  exception others');
END;

-----------添加DBOM管理功能点-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'DBomView';
 IF(countt=0) THEN
    Insert into CWM_SYS_RES(RESID, ALIAS, RESNAME, PARENTID, DEFAULTURL, SN, ICON, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
    values(57, 'DBomView', 'DBOM管理', 7, '/js/system/DBomView.do',  10, '/images/function/mid/dbom.png', 0,1,1,null,null);
    SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=57;
 	  IF(countt=0) THEN
    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(1304,-1,57);
    	commit;
     END IF;
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('DBOM管理 DBomView  exception others');
END;

-----------数据分类表：添加密级分类记录-----------------
BEGIN
 SELECT COUNT(*) into countt FROM CWM_SYS_GLTYPE WHERE TYPENAME = '密级' and NODEKEY = 'SECURITY' and CATKEY = 'DIC';
 IF(countt=0) THEN
    Insert into CWM_SYS_GLTYPE(TYPEID, TYPENAME, NODEPATH, DEPTH, PARENTID, NODEKEY, CATKEY, SN, USERID)Values
    (SEQ_CWM_SYS_GLTYPE.NEXTVAL, '密级', '0.'||SEQ_CWM_SYS_GLTYPE.CURRVAL||'.', 1, 0, 'SECURITY', 'DIC', 2, -1);
    COMMIT;
    --数据字典表：添加密级枚举值记录---
    Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN)Values(SEQ_CWM_SYS_DIC.NEXTVAL, SEQ_CWM_SYS_GLTYPE.CURRVAL, '密级', '内部', '内部', 1);
    Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN)Values(SEQ_CWM_SYS_DIC.NEXTVAL, SEQ_CWM_SYS_GLTYPE.CURRVAL, '密级', '秘密', '秘密', 2);
    Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN)Values(SEQ_CWM_SYS_DIC.NEXTVAL, SEQ_CWM_SYS_GLTYPE.CURRVAL, '密级', '机密', '机密', 3);
    Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN)Values(SEQ_CWM_SYS_DIC.NEXTVAL, SEQ_CWM_SYS_GLTYPE.CURRVAL, '密级', '绝密', '绝密', 4);
    COMMIT;
  END IF;
 exception
   when others then
   dbms_output.put_line('密级分类记录 CWM_SYS_GLTYPE  exception others');
END;

-----------更新CWM_SYS_RES表字段-----------------
BEGIN
 UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/demension/list.do' WHERE ALIAS = 'DemensionView';
 UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/job/list.do' WHERE ALIAS = 'SysJobView';
 UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/sysOrg/list.do' WHERE ALIAS = 'OrganizationView';
 UPDATE  CWM_SYS_RES SET DEFAULTURL='/oa/system/sysCodegen/show.do' WHERE ALIAS = 'CodeGenal';
END;
--CWM_SYS_RES表和CWM_SYS_ROLE_RES删除"部门管理"字段和角色功能对应管理，删除该功能
--"组织管理"使用CWM_SYS_DEPARTMENT表代替"部门管理"
BEGIN
 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE CWM_SYS_RES.RESNAME='部门管理');
 DELETE FROM CWM_SYS_RES WHERE RESNAME='部门管理';
END;
--删除老的首页管理--
BEGIN
 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE ALIAS='PortalView' and DEFAULTURL like '%/js/system/PortalView%');
 DELETE FROM CWM_SYS_RES WHERE ALIAS='PortalView' and DEFAULTURL like '%/js/system/PortalView%';
END;
 --------------------------------------------------------------------------------------------------------------------------------------------------

----------------------参数初始化:系统名称--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'SYSTEM_TITLE', '0', '综合管理系统', '系统名称');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER SYSTEM_TITLE ');
END;
----------------------参数初始化:公司名称--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'COMPANY_NAME', '0', 'xxx', '公司名称');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER COMPANY_NAME ');
END;
----------------------参数初始化:系统对应的logo--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'SYSTEM_TITLE_LOGO', '0', '/system/companylogo.png', '默认为系统文件上传路径的system路径下，即参数值;图片要求为logo+项目名称组成的png格式的图片，高度要求：44px');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER SYSTEM_TITLE_LOGO ');
END;
----------------------参数初始化:文件上传路径--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values
(SEQ_CWM_SYS_PARAMETER.nextVal, 'UploadFileFolder', '0', 'D:\\ibms\\attachFile', '文件上传路径；注意，如果需修改该路径，请将tomcat的server.xml的映射路径一并修改；否则文件图片无法访问；');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER UploadFileFolder ');
END;
----------------------参数初始化:文件是否加密存储--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.NEXTVAL, 'IS_SAVE_SECURITY', '0', '否', '文件是否加密存储。是：加密；否：不加密。');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER IS_SAVE_SECURITY ');
END;
----------------------参数初始化:文件加密密钥--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.NEXTVAL, 'FILE_KEY_NAME', '0', 'cssrc@hzy', '文件加密密钥。');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER FILE_KEY_NAME ');
END;
----------------------参数初始化:点击腾讯通消息提醒进入的系统--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'RTX_NOTIFY_LINK', '0', 'http://192.168.8.203:8085/ibms', '点击腾讯通消息提醒进入的系统');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER RTX_NOTIFY_LINK ');
END;
----------------------参数初始化:腾讯通开关--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'RTX_NOTIFY_ON_OFF', '0', '0', '腾讯通开关：0关,1开');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER RTX_NOTIFY_ON_OFF ');
END;
----------------------参数初始化:RTX接收人的账号--------------------------------------
BEGIN
Insert into CWM_SYS_PARAMETER(ID, NAME, DATATYPE, VALUE, DESCRIPTION)Values(SEQ_CWM_SYS_PARAMETER.nextVal, 'RTX_RECEIVE_TYPE', '0', '1', 'RTX接收人的账号，0--账号（username）；1--中文名(fullname)');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PARAMETER RTX_RECEIVE_TYPE ');
END;
----------------------皮肤--------------------------------------
BEGIN
Insert into CWM_SYS_PAUR(PAURID,PAURNAME,ALIASNAME,PAURVALUE,USERID)values(1,'皮肤','skin','default',0);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_PAUR 皮肤 ');
END;

-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入---一列(12)------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(1, '一列(12)', '一列(12)', '<div class="col-md-12 column"></div>', 1);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 一列(12) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入---二列(6,6)------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(2, '二列(6,6)', '二列(6,6)', '<div class=\"col-md-6 column\"></div>\r\n<div class=\"col-md-6 column\"></div>', 2);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(6,6) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入---二列(9,3)------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(3, '二列(9,3)', '二列(9,3)', '<div class=\"col-md-9 column\"></div>\r\n<div class=\"col-md-3 column\"></div>', 3);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(9,3) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入---二列(3,9)------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(4, '二列(3,9)', '二列(3,9)', '<div class=\"col-md-3 column\"></div>\r\n<div class=\"col-md-9 column\"></div>', 4);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(3,9) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入---二列(8,4)------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(5, '二列(8,4)', '二列(8,4)', '<div class=\"col-md-8 column\"></div>\r\n<div class=\"col-md-4 column\"></div>', 5);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(8,4) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--二列(4,8)-------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(6, '二列(4,8)', '二列(4,8)', '<div class=\"col-md-4 column\"></div>\r\n<div class=\"col-md-8 column\"></div>', 6);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(4,8) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--二列(10,2)-------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(7, '二列(10,2)', '二列(10,2)', '<div class=\"col-md-10 column\"></div>\r\n<div class=\"col-md-2 column\"></div>', 7);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(10,2) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--二列(2,10)-------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(8, '二列(2,10)', '二列(2,10)', '<div class=\"col-md-2 column\"></div>\r\n<div class=\"col-md-10 column\"></div>', 8);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 二列(2,10) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--三列(4,4,4)-------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(9, '三列(4,4,4)', '二列(4,4,4)', '<div class=\"col-md-4 column\"></div>\r\n<div class=\"col-md-4 column\"></div>\r\n<div class=\"col-md-4 column\"></div>', 9);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 三列(4,4,4) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--三列(2,6,4)-------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(10, '三列(2,6,4)', '二列(2,6,4)', '<div class=\"col-md-2 column\"></div>\r\n<div class=\"col-md-6 column\"></div>\r\n<div class=\"col-md-4 column\"></div>', 10);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 三列(2,6,4) ');
END;
-----------------------CWM_SYS_INDEX_LAYOUT 首页布局数据插入--三列(4,6,2)---------------------------
BEGIN
Insert into CWM_SYS_INDEX_LAYOUT(ID, NAME, MEMO, TEMPLATE_HTML, SN)Values(11, '三列(4,6,2)', '二列(4,6,2)', '<div class=\"col-md-4 column\"></div>\r\n<div class=\"col-md-6 column\"></div>\r\n<div class=\"col-md-2 column\"></div>', 11);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_INDEX_LAYOUT 三列(4,6,2) ');
END;
COMMIT;

----------------------给CWM_SYS_ORG_TYPE表添加数据-部-----------------------------
BEGIN
INSERT INTO CWM_SYS_ORG_TYPE(ID,DEMID,NAME,LEVELS,MEMO,ICON) Values(1, 1, '部', 1, '', '/styles/default/images/resicon/archSat.png');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_ORG_TYPE 部 ');
END;
----------------------给CWM_SYS_ORG_TYPE表添加数据-所队-----------------------------
BEGIN
INSERT INTO CWM_SYS_ORG_TYPE(ID,DEMID,NAME,LEVELS,MEMO,ICON) Values(2, 1, '所队', 2, '', '/styles/default/images/resicon/ico_116.gif');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_ORG_TYPE 所队 ');
END;
----------------------给CWM_SYS_ORG_TYPE表添加数据-室处-----------------------------
BEGIN
INSERT INTO CWM_SYS_ORG_TYPE(ID,DEMID,NAME,LEVELS,MEMO,ICON) Values(3, 1, '室处', 3, '', '/styles/default/images/resicon/ico_32.gif');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_ORG_TYPE 室处 ');
END;
----------------------给CWM_SYS_ORG_TYPE表添加数据-小组-----------------------------
BEGIN
INSERT INTO CWM_SYS_ORG_TYPE(ID,DEMID,NAME,LEVELS,MEMO,ICON) Values(4, 1, '小组', 4, '', '/styles/default/images/resicon/nav-customer.png');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_ORG_TYPE 小组 ');
END;
----------------------给CWM_SYS_ORG_TYPE表添加数据-其他组织-----------------------------
BEGIN
INSERT INTO CWM_SYS_ORG_TYPE(ID,DEMID,NAME,LEVELS,MEMO,ICON) Values(5, 1, '其他组织', 5, '', '/styles/default/images/resicon/user.gif');
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist CWM_SYS_ORG_TYPE 其他组织 ');
END;
commit;

----------------------初始化邮箱---收件箱--------------------------
BEGIN
Insert into MAIL_FOLDER(FOLDERID,FOLDERNAME,DEPLEVEL,PATH,ISPUBLIC,FOLDERTYPE)  Values(1,'收件箱',1,'0.4',0,1);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist MAIL_FOLDER 收件箱 ');
END;
----------------------初始化邮箱---发件箱--------------------------
BEGIN
Insert into MAIL_FOLDER(FOLDERID,FOLDERNAME,DEPLEVEL,PATH,ISPUBLIC,FOLDERTYPE)  Values(2,'发件箱',1,'0.4',0,2);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist MAIL_FOLDER 发件箱 ');
END;
----------------------初始化邮箱---草稿箱--------------------------
BEGIN
Insert into MAIL_FOLDER(FOLDERID,FOLDERNAME,DEPLEVEL,PATH,ISPUBLIC,FOLDERTYPE)  Values(3,'草稿箱',1,'0.4',0,3);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist MAIL_FOLDER 草稿箱 ');
END;
----------------------初始化邮箱---删除箱--------------------------
BEGIN
Insert into MAIL_FOLDER(FOLDERID,FOLDERNAME,DEPLEVEL,PATH,ISPUBLIC,FOLDERTYPE)  Values(4,'删除箱',1,'0.4',0,4);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist MAIL_FOLDER 删除箱 ');
END;
----------------------初始化邮箱---其他--------------------------
BEGIN
Insert into MAIL_FOLDER(FOLDERID,FOLDERNAME,DEPLEVEL,PATH,ISPUBLIC,FOLDERTYPE)  Values(5,'其他',1,'0.4',0,5);
EXCEPTION
WHEN DUP_VAL_ON_INDEX THEN
dbms_output.put_line('exist MAIL_FOLDER 其他 ');
END;

----2016-04-06 by hhj修改业务数据模板加序号列（sql语句中的空格和换行不能删除）,再在表单设计--业务数据模板 再点击保存一遍。
BEGIN
UPDATE  IBMS_FORM_TEMPLATE
SET HTML =
REPLACE(REPLACE(HTML,'<#-- 显示字段-->',' <#noparse><th>序号</th></#noparse>'||chr(10)||' <#-- 显示字段-->'),
'          <#list displayFields as field>
            <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
              <td>','<#--序号-->'||chr(10)||'<#noparse><td style="width:30px;">${data_index+1}</td> </#noparse>'||chr(10)||'          <#list displayFields as field>
            <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
              <td>')
WHERE templatetype = 'dataTemplate'  AND  HTML NOT LIKE '%<#noparse><th>序号</th></#noparse>%';
COMMIT;
END;
----2016-04-09 by hhj  将数据模板和业务数据模板中的.ht后缀改成.do后缀。
BEGIN
UPDATE IBMS_DATA_TEMPLATE SET templatehtml = REPLACE(templatehtml,'exportData.ht','exportData.do')   where templatehtml like '%exportData.ht%';
UPDATE IBMS_FORM_TEMPLATE SET html = REPLACE(html,'exportData.ht','exportData.do')   where html like '%exportData.ht%';
UPDATE IBMS_DATA_TEMPLATE SET templatehtml = REPLACE(templatehtml,'.ht?','.do?')  where templatehtml like '%.ht?%';
UPDATE IBMS_DATA_TEMPLATE SET templatehtml = REPLACE(templatehtml,'.ht"','.do"')  where templatehtml like '%.ht"%';
UPDATE IBMS_FORM_TEMPLATE SET html = REPLACE(html,'.ht?','.do?')  where html like '%.ht?%';
UPDATE IBMS_FORM_TEMPLATE SET html = REPLACE(html,'.ht"','.do"')  where html like '%.ht"%';
COMMIT;
END;

-------------更改 CWM_SYS_GLTYPE表ISLEAF为null-----2016-8-17 by yangbo--------------------------------
BEGIN	  
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('ISLEAF');
  IF(countt>0) THEN
    select NULLABLE into testable  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('ISLEAF');
  	IF(testable ='Y') THEN
    execute immediate 'Alter table CWM_SYS_GLTYPE  modify ISLEAF NUMBER(18)  DEFAULT null';
    execute immediate 'update CWM_SYS_GLTYPE  set ISLEAF =null';
  	commit;
  	END IF;
  END IF;
END;

---添加维度--------------2016-8-17 by yangbo-----------------------------------------
BEGIN	  
  select count(*) into countt from cwm_sys_demension WHERE DEMID = 1;
  IF(countt=0) THEN
  	insert into cwm_sys_demension(DEMID ,DEMNAME ,DEMDESC) values(1,'行政维度','行政维度');
    commit;
  END IF;
  execute immediate 'update cwm_sys_demension set DEMNAME = ''行政维度'' where demid = 1';
  commit;
END;

--分类父子节点整理--------------2016-9-26 by yangbo-----------------------------------------
BEGIN
update CWM_SYS_GLTYPE A set A.PARENTID=(select B.TYPEKEYID from CWM_SYS_TYPEKEY B where A.CATKEY=B.TYPEKEY) where A.DEPTH=1;
commit;
END;

--删除旧的邮箱消息功能点数据--------------2016-9-27 by yangbo-----------------------------------------
BEGIN
 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE CWM_SYS_RES.RESNAME='我的邮箱');
 DELETE FROM CWM_SYS_RES WHERE RESNAME='我的邮箱';
 commit;
END;

BEGIN
 DELETE FROM CWM_SYS_ROLE_RES WHERE RESID=(SELECT RESID FROM CWM_SYS_RES WHERE CWM_SYS_RES.RESNAME='发邮件');
 DELETE FROM CWM_SYS_RES WHERE RESNAME='发邮件';
 commit;
END;

---------------------------表和列注释-2017-1-2--by yangbo--------------
BEGIN
	select count(*) into countt from tabs where table_name = 'CWM_SYS_USER_POSITION';
	IF(countt>0) THEN
		execute immediate 'comment on column  CWM_SYS_USER_POSITION.POSID                 is  ''职位id'''; 
		execute immediate 'comment on column  CWM_SYS_USER_POSITION.USERID                 is  ''用户id'''; 
		execute immediate 'comment on column  CWM_SYS_USER_POSITION.ISPRIMARY              is  ''是否主职位''';
		execute immediate 'comment on column  CWM_SYS_USER_POSITION.ORGID                 is  ''组织id''';
	 	commit;
  END IF;
END;

BEGIN
	select count(*) into countt from tabs where table_name = 'CWM_SYS_USER';
	 IF(countt>0) THEN
		execute immediate 'comment on column  CWM_SYS_USER.FULLNAME                        is  ''全名''';
		execute immediate 'comment on column  CWM_SYS_USER.DEPID                           is  ''部门''';
		execute immediate 'comment on column  CWM_SYS_USER.ORIGINALSIGN_PIC                is  ''原始照片''';
		execute immediate 'comment on column  CWM_SYS_USER.SIGN_PIC                        is  ''签名图片''';
		execute immediate 'comment on column  CWM_SYS_USER.SECURITY                        is  ''密级''';
 	commit;
  END IF;
END;

BEGIN
execute immediate 'comment on table ACT_GE_BYTEARRAY      is ''二进制数据表''';
execute immediate 'comment on table ACT_GE_PROPERTY       is ''属性数据表存储整个流程引擎级别的数据,初始化表结构时,会默认插入三条记录''';
execute immediate 'comment on table ACT_HI_ACTINST        is ''历史节点表''';
execute immediate 'comment on table ACT_HI_ATTACHMENT     is ''历史附件表''';
execute immediate 'comment on table ACT_HI_COMMENT        is ''历史意见表''';
execute immediate 'comment on table ACT_HI_DETAIL         is ''历史详情表,提供历史变量的查询''';
execute immediate 'comment on table ACT_HI_IDENTITYLINK   is ''历史流程人员表''';
execute immediate 'comment on table ACT_HI_PROCINST       is ''历史流程实例表''';
execute immediate 'comment on table ACT_HI_TASKINST       is ''历史任务实例表''';
execute immediate 'comment on table ACT_HI_VARINST        is ''历史变量表''';
execute immediate 'comment on table ACT_RE_DEPLOYMENT     is ''部署信息表''';
execute immediate 'comment on table ACT_RE_MODEL          is ''流程设计模型部署表''';
execute immediate 'comment on table ACT_RE_PROCDEF        is ''流程定义数据表''';
execute immediate 'comment on table ACT_RU_EVENT_SUBSCR   is ''throwEvent,catchEvent时间监听信息表''';
execute immediate 'comment on table ACT_RU_EXECUTION      is ''运行时流程执行实例表''';
execute immediate 'comment on table ACT_RU_IDENTITYLINK   is ''运行时流程人员表,主要存储任务节点与参与者的相关信息''';
execute immediate 'comment on table ACT_RU_JOB            is ''运行时定时任务数据表''';
execute immediate 'comment on table ACT_RU_TASK           is ''运行时任务节点表''';
execute immediate 'comment on table ACT_RU_VARIABLE       is ''运行时流程变量数据表''';

execute immediate 'comment on column  IBMS_FORM_TABLE.CREATETIME           is  ''创建日期''';
execute immediate 'comment on column  IBMS_FORM_TABLE.CREATOR              is  ''创建人姓名''';
execute immediate 'comment on column  IBMS_FORM_TABLE.CREATEBY             is  ''创建人''';


execute immediate 'comment on column  IBMS_FORM_DEF.FORMKEY                is  ''FORM表单主键''';


execute immediate 'comment on column  IBMS_FORM_RIGHTS.PLATFORM            is  ''平台,可选值:0:PC,1:mobile,默认值为0''';


execute immediate 'comment on column  IBMS_FORM_RUN.FORMTYPE               is  ''表单类型(0:在线表单,1:URL表单)''';
execute immediate 'comment on column  IBMS_FORM_RUN.FORMURL                is  ''表单URL''';
execute immediate 'comment on column  IBMS_FORM_RUN.MOBILEFORMID           is  ''手机端表单定义ID''';
execute immediate 'comment on column  IBMS_FORM_RUN.MOBILEFORMKEY          is  ''手机端表单定义key''';
execute immediate 'comment on column  IBMS_FORM_RUN.MOBILEFORMURL          is  ''手机端表单URL''';


execute immediate 'comment on column  ACT_GE_BYTEARRAY.ID_                 is  ''主键ID''';
execute immediate 'comment on column  ACT_GE_BYTEARRAY.REV_                is  ''Version(版本)''';                             
execute immediate 'comment on column  ACT_GE_BYTEARRAY.NAME_               is  ''部署的文件名称，如：mail.bpmn、mail.png 、mail.bpmn20.xml''';
execute immediate 'comment on column  ACT_GE_BYTEARRAY.DEPLOYMENT_ID_      is  ''部署表ID''';
execute immediate 'comment on column  ACT_GE_BYTEARRAY.BYTES_              is  ''部署文件''';
execute immediate 'comment on column  ACT_GE_BYTEARRAY.GENERATED_          is  ''0为用户生成 1为Activiti生成''';


execute immediate 'comment on column  ACT_GE_PROPERTY.NAME_                is  ''schema.version,schema.history,next.dbid''';
execute immediate 'comment on column  ACT_GE_PROPERTY.VALUE_               is  ''5.*,create(5.*)''';
execute immediate 'comment on column  ACT_GE_PROPERTY.REV_                 is  ''version''';


execute immediate 'comment on column  ACT_HI_ACTINST.ID_                   is  ''主键ID''';
execute immediate 'comment on column  ACT_HI_ACTINST.PROC_DEF_ID_          is  ''流程定义id''';
execute immediate 'comment on column  ACT_HI_ACTINST.PROC_INST_ID_         is  ''流程实例ID''';
execute immediate 'comment on column  ACT_HI_ACTINST.EXECUTION_ID_         is  ''执行实例ID''';
execute immediate 'comment on column  ACT_HI_ACTINST.ACT_ID_               is  ''节点定义ID''';
execute immediate 'comment on column  ACT_HI_ACTINST.ACT_NAME_             is  ''节点定义名称''';
execute immediate 'comment on column  ACT_HI_ACTINST.ACT_TYPE_             is  ''如startEvent,userTask''';
execute immediate 'comment on column  ACT_HI_ACTINST.ASSIGNEE_             is  ''节点签收人''';
execute immediate 'comment on column  ACT_HI_ACTINST.START_TIME_           is  ''2013-09-15 11:30:00''';
execute immediate 'comment on column  ACT_HI_ACTINST.END_TIME_             is  ''2013-09-15 11:30:00''';
execute immediate 'comment on column  ACT_HI_ACTINST.DURATION_             is  ''持续时间毫秒''';
execute immediate 'comment on column  ACT_HI_ACTINST.ISSTART               is  ''是否为第一个节点''';
execute immediate 'comment on column  ACT_HI_ACTINST.TASK_ID_              is  ''任务实例ID 其他节点类型实例ID在这里为空''';
execute immediate 'comment on column  ACT_HI_ACTINST.CALL_PROC_INST_ID_    is  ''调用外部流程的流程实例ID''';
execute immediate 'comment on column  ACT_HI_ACTINST.TENANT_ID_            is  ''毫秒值''';


execute immediate 'comment on column  ACT_HI_ATTACHMENT.ID_                is  ''主键ID''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.REV_               is  ''Version''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.USER_ID_           is  ''用户ID''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.NAME_              is  ''附件名称''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.DESCRIPTION_       is  ''描述''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.TYPE_              is  ''附件类型''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.TASK_ID_           is  ''节点实例ID''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.PROC_INST_ID_      is  ''流程实例ID''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.URL_               is  ''附件地址''';
execute immediate 'comment on column  ACT_HI_ATTACHMENT.CONTENT_ID_        is  ''ACT_GE_BYTEARRAY的ID''';


execute immediate 'comment on column  ACT_HI_DETAIL.VAR_TYPE_              is  ''备注''';


execute immediate 'comment on column  ACT_HI_IDENTITYLINK.GROUP_ID_        is  ''组ID''';
execute immediate 'comment on column  ACT_HI_IDENTITYLINK.TYPE_            is  ''备注7''';
execute immediate 'comment on column  ACT_HI_IDENTITYLINK.USER_ID_         is  ''用户ID''';
execute immediate 'comment on column  ACT_HI_IDENTITYLINK.TASK_ID_         is  ''节点实例ID''';
execute immediate 'comment on column  ACT_HI_IDENTITYLINK.PROC_INST_ID_    is  ''流程实例ID''';


execute immediate 'comment on column  ACT_HI_PROCINST.PROC_INST_ID_                is  ''流程实例ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.BUSINESS_KEY_                is  ''业务记录ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.PROC_DEF_ID_                 is  ''流程定义ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.START_TIME_                  is  ''开始时间''';
execute immediate 'comment on column  ACT_HI_PROCINST.END_TIME_                    is  ''结束时间''';
execute immediate 'comment on column  ACT_HI_PROCINST.DURATION_                    is  ''持续时间''';
execute immediate 'comment on column  ACT_HI_PROCINST.START_USER_ID_               is  ''执行人''';
execute immediate 'comment on column  ACT_HI_PROCINST.START_ACT_ID_                is  ''开始节点实例ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.END_ACT_ID_                  is  ''结束节点实例ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.SUPER_PROCESS_INSTANCE_ID_   is  ''父流程实例ID''';
execute immediate 'comment on column  ACT_HI_PROCINST.DELETE_REASON_               is  ''删除原因''';
execute immediate 'comment on column  ACT_HI_PROCINST.TENANT_ID_                   is  ''毫秒值''';


execute immediate 'comment on column  ACT_HI_TASKINST.CATEGORY_                    is  ''类别''';
execute immediate 'comment on column  ACT_HI_TASKINST.TENANT_ID_                   is  ''毫秒值''';


execute immediate 'comment on column  ACT_HI_VARINST.CREATE_TIME_                  is  ''创建时间''';
execute immediate 'comment on column  ACT_HI_VARINST.LAST_UPDATED_TIME_            is  ''最后更新时间''';


execute immediate 'comment on column  ACT_RE_DEPLOYMENT.TENANT_ID_                 is  ''毫秒值''';


execute immediate 'comment on column  ACT_RE_MODEL.TENANT_ID_                      is  ''毫秒值''';


execute immediate 'comment on column  ACT_RE_PROCDEF.TENANT_ID_                    is  ''毫秒值''';


execute immediate 'comment on column  ACT_RU_EVENT_SUBSCR.TENANT_ID_               is  ''毫秒值''';
execute immediate 'comment on column  ACT_RU_EVENT_SUBSCR.PROC_DEF_ID_             is  ''流程定义ID''';


execute immediate 'comment on column  ACT_RU_EXECUTION.TENANT_ID_                  is  ''毫秒值''';


execute immediate 'comment on column  ACT_RU_IDENTITYLINK.PROC_DEF_ID_             is  ''流程定义ID''';
execute immediate 'comment on column  ACT_RU_IDENTITYLINK.PROC_INST_ID_            is  ''流程实例ID''';


execute immediate 'comment on column  ACT_RU_JOB.PROC_DEF_ID_                      is  ''流程定义ID''';
execute immediate 'comment on column  ACT_RU_JOB.TENANT_ID_                        is  ''毫秒值''';


execute immediate 'comment on column  ACT_RU_TASK.CATEGORY_                        is  ''类别''';
execute immediate 'comment on column  ACT_RU_TASK.TENANT_ID_                       is  ''毫秒值''';


execute immediate 'comment on column  IBMS_DEFINITION.ISPRINTFORM                  is  ''是否开启打印模版功能''';
execute immediate 'comment on column  IBMS_DEFINITION.ALLOWDELDRAF                 is  ''是否允许删除流程图''';
execute immediate 'comment on column  IBMS_DEFINITION.ALLOWMOBILE                  is  ''是否支持手机访问''';


execute immediate 'comment on column  IBMS_NODE_SET.INITSCRIPTHANDLER              is  ''初始化脚本''';


execute immediate 'comment on column  IBMS_REFER_DEFINITION.CREATEID               is  ''创建人''';
execute immediate 'comment on column  IBMS_REFER_DEFINITION.UPDATETIME             is  ''创建时间''';
execute immediate 'comment on column  IBMS_REFER_DEFINITION.REMARK                 is  ''备注''';

  
execute immediate 'comment on column  IBMS_PRO_RUN.BUSINESSKEY                     is  ''业务主键ID''';
execute immediate 'comment on column  IBMS_PRO_RUN.BUSINESSURL                     is  ''外部业务表单URL''';
execute immediate 'comment on column  IBMS_PRO_RUN.FLOWKEY                         is  ''流程key''';
execute immediate 'comment on column  IBMS_PRO_RUN.STARTNODE                       is  ''选择的第一个节点的名称''';


execute immediate 'comment on column  IBMS_PRO_TRANSTO.ASSIGNEE                    is  ''节点签收人''';


execute immediate 'comment on column  IBMS_TASK_EXE.CRATETIME                      is  ''创建时间''';
execute immediate 'comment on column  IBMS_TASK_EXE.EXE_TIME                       is  ''执行时间''';



execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.ID                       is  ''主键''';  
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.SQL_ID                   is  ''SQL_ID''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.NAME                     is  ''字段名''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.TYPE                     is  ''数据类型''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.FIELD_DESC               is  ''字段备注''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.IS_SHOW                  is  ''是否可见''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.IS_SEARCH                is  ''是否搜索''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.CONTROL_TYPE             is  ''控件类型''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.FORMAT                   is  ''日期格式''';
execute immediate 'comment on column  CWM_SYS_QUERY_FIELD.CONTROL_CONTENT          is  ''控件内容''';


execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.ID                     is  ''主键''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.SQL_ID                 is  ''sql'''; 
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.NAME                   is  ''视图名称''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.ALIAS                  is  ''视图别名''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.STYLE                  is  ''过滤器类型''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.NEED_PAGE              is  ''是否分页''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.PAGE_SIZE              is  ''分页大小''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.TEMPLATE_ALIAS         is  ''模板别名''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.TEMPLATE_HTML          is  ''模板html''';      
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.DISPLAY_FIELD          is  ''显示列''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.FILTER_FIELD           is  ''过滤列''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.CONDITION_FIELD        is  ''查询列''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.SORT_FIELD             is  ''排序列''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.EXPORT_FIELD           is  ''导出列''';
execute immediate 'comment on column  CWM_SYS_QUERY_SETTING.IS_QUERY               is  ''是否初始化查询''';


execute immediate 'comment on column  CWM_SYS_QUERY_SQL.ID                         is  ''主键''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.SQL_                       is  ''SQL定义''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.NAME                       is  ''名称''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.URL_PARAMS                 is  ''使用参数''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.CATEGORYID                 is  ''分类ID''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.DSNAME                     is  ''数据源''';
execute immediate 'comment on column  CWM_SYS_QUERY_SQL.ALIAS                      is  ''别名''';






execute immediate 'comment on column  QRTZ_BLOB_TRIGGERS.TRIGGER_NAME             is  ''Trigger名称''';
execute immediate 'comment on column  QRTZ_BLOB_TRIGGERS.TRIGGER_GROUP            is  ''trigger所属组的名字''';
execute immediate 'comment on column  QRTZ_BLOB_TRIGGERS.BLOB_DATA                 is  ''存放持久化trigger对象''';


execute immediate 'comment on column  QRTZ_CALENDARS.CALENDAR_NAME                is  ''Calendar名称''';
execute immediate 'comment on column  QRTZ_CALENDARS.CALENDAR                      is  ''Calendar信息''';


execute immediate 'comment on column  QRTZ_CRON_TRIGGERS.TRIGGER_NAME             is  ''Trigger名称''';
execute immediate 'comment on column  QRTZ_CRON_TRIGGERS.TRIGGER_GROUP           is  ''trigger所属组的名字''';
execute immediate 'comment on column  QRTZ_CRON_TRIGGERS.CRON_EXPRESSION           is  ''cron表达式''';
execute immediate 'comment on column  QRTZ_CRON_TRIGGERS.TIME_ZONE_ID              is  ''时区''';


execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.TRIGGER_NAME             is  ''Trigger名称''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.TRIGGER_GROUP            is  ''trigger所属组的名字''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.INSTANCE_NAME            is  ''之前配置文件中org.quartz.scheduler.instanceId配置的名字,就会写入该字段,如果设置为AUTO,quartz会根据物理机名和当前时间产生一个名字''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.FIRED_TIME               is  ''触发事件''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.PRIORITY                 is  ''优先级''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.STATE                    is  ''状态''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.JOB_NAME                 is  ''集群中job的名字''';
execute immediate 'comment on column  QRTZ_FIRED_TRIGGERS.JOB_GROUP                is  ''集群中job的所属组的名字''';


execute immediate 'comment on column  QRTZ_JOB_DETAILS.JOB_NAME                   is  ''集群中job的名字''';
execute immediate 'comment on column  QRTZ_JOB_DETAILS.JOB_GROUP                  is  ''集群中job的所属组的名字''';
execute immediate 'comment on column  QRTZ_JOB_DETAILS.DESCRIPTION                 is  ''描述''';
execute immediate 'comment on column  QRTZ_JOB_DETAILS.JOB_CLASS_NAME              is  ''集群中个note job实现类的完全包名''';
execute immediate 'comment on column  QRTZ_JOB_DETAILS.IS_UPDATE_DATA              is  ''是否更新数据''';
execute immediate 'comment on column  QRTZ_JOB_DETAILS.JOB_DATA                    is  ''Job数据''';


execute immediate 'comment on column  QRTZ_LOCKS.LOCK_NAME                        is  ''悲观锁的信息''';


execute immediate 'comment on column  QRTZ_PAUSED_TRIGGER_GRPS.TRIGGER_GROUP      is  ''Trigger组的信息''';


execute immediate 'comment on column  QRTZ_SCHEDULER_STATE.INSTANCE_NAME          is  ''之前配置文件中org.quartz.scheduler.instanceId配置的名字,就会写入该字段,如果设置为AUTO,quartz会根据物理机名和当前时间产生一个名字''';
execute immediate 'comment on column  QRTZ_SCHEDULER_STATE.LAST_CHECKIN_TIME       is  ''上次检查时间''';                                                                                
execute immediate 'comment on column  QRTZ_SCHEDULER_STATE.CHECKIN_INTERVAL        is  ''检查间隔时间''';                                                                                


execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.TRIGGER_NAME          is  ''Trigger名称''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.TRIGGER_GROUP         is  ''trigger所属组的名字''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.STR_PROP_1             is  ''字符串属性1''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.STR_PROP_2             is  ''字符串属性2''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.STR_PROP_3             is  ''字符串属性3''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.INT_PROP_1             is  ''整型属性1''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.INT_PROP_2             is  ''整型属性2''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.LONG_PROP_1            is  ''长整型属性1''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.LONG_PROP_2            is  ''长整型属性2''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.DEC_PROP_1             is  ''十进制小数属性1''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.DEC_PROP_2             is  ''十进制小数属性2''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.BOOL_PROP_1            is  ''布朗类型属性1''';
execute immediate 'comment on column  QRTZ_SIMPROP_TRIGGERS.BOOL_PROP_2            is  ''布朗类型属性2''';


execute immediate 'comment on column  QRTZ_TRIGGERS.TRIGGER_NAME                  is  ''Trigger名称''';
execute immediate 'comment on column  QRTZ_TRIGGERS.TRIGGER_GROUP                 is  ''trigger所属组的名字''';
execute immediate 'comment on column  QRTZ_TRIGGERS.JOB_NAME                       is  ''集群中job的名字''';
execute immediate 'comment on column  QRTZ_TRIGGERS.JOB_GROUP                      is  ''集群中job的所属组的名字''';
execute immediate 'comment on column  QRTZ_TRIGGERS.DESCRIPTION                    is  ''描述''';
execute immediate 'comment on column  QRTZ_TRIGGERS.NEXT_FIRE_TIME                 is  ''下次触发时间''';
execute immediate 'comment on column  QRTZ_TRIGGERS.PREV_FIRE_TIME                 is  ''上次触发时间''';
execute immediate 'comment on column  QRTZ_TRIGGERS.PRIORITY                       is  ''优先级''';
execute immediate 'comment on column  QRTZ_TRIGGERS.TRIGGER_STATE                  is  ''触发状态''';
execute immediate 'comment on column  QRTZ_TRIGGERS.TRIGGER_TYPE                   is  ''触发类型''';
execute immediate 'comment on column  QRTZ_TRIGGERS.START_TIME                     is  ''开始时间''';
execute immediate 'comment on column  QRTZ_TRIGGERS.END_TIME                       is  ''结束时间''';
execute immediate 'comment on column  QRTZ_TRIGGERS.CALENDAR_NAME                  is  ''日期类型''';
execute immediate 'comment on column  QRTZ_TRIGGERS.MISFIRE_INSTR                  is  ''失败原因''';
execute immediate 'comment on column  QRTZ_TRIGGERS.JOB_DATA                       is  ''JOB数据''';

execute immediate 'comment on column  SYS_DB_ID.ID                                 is  ''计算机编号''';
execute immediate 'comment on column  SYS_DB_ID.INCREMENTAL                        is  ''ID增量''';
execute immediate 'comment on column  SYS_DB_ID.BOUND                              is  ''边界值''';
	
END;	

END synData;
