CREATE OR REPLACE PROCEDURE synData_liubo (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN

-------------添加判断是本地存储还是分布式文件存储的系统参数-------2016-11-27 by liubo---------------------------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'IS_FILE_FASTDFS';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4001','IS_FILE_FASTDFS','0','0','采用分布式文件存储方式FASTDFS进行文件上传，下载，删除操作。1表示是，0表示否');
END IF;

-------------通过该参数来设置沟通是是否产生代表任务，"1"表示产生，"2"表示不产生。-------2017-02-13 by liubo---------------------------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'IS_PRODUCE_TASK';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4003','IS_PRODUCE_TASK','0','1','通过该参数来设置沟通是是否产生代表任务，"1"表示产生，"2"表示不产生。');
END IF;

-------判断流程中沟通采用邮件形式------2016-11-29 by liubo-----------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'IS_COMMUNICATE_EMAIL';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4002','IS_COMMUNICATE_EMAIL','0','1#1#1','流程中沟通采用形式。以#号区分，分别代表邮件、站内消息、RTX消息，1代表可以选择，0代表不可以选择。必须保证有一种沟通形式为1');
END IF;

-------附件是否按照密级显示--2017-3-3 by liubo-----------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'IS_DISPLAY_SECURITY';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4004','IS_DISPLAY_SECURITY','0','0','文件上传组件中是否显示密级选择权限和附件列表中是否按照密级筛选，默认为0。0：不显示；1：显示。');
END IF;

 -------更新之前的附件系统变量--2017-3-3 by liubo-----------------------------
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE NAME = 'IS_DISPLAY_SECURITY';
	IF(countt>0) THEN  
		update CWM_SYS_PARAMETER set DESCRIPTION='文件上传组件中是否显示密级选择权限和附件列表中是否按照密级筛选，默认为0。0：不显示；1：显示。' where NAME='IS_DISPLAY_SECURITY';
END IF;

-------账号策略表新增基本信息------2017-03-30 by liubo-----------
select count(*) into countt from ibms_account_strategy WHERE ID='1';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('1','密码最小长度','密码的最小长度，值不能小于6','0','6','0');
END IF;

select count(*) into countt from ibms_account_strategy WHERE ID='2';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('2','密码复杂性要求','','0',null,'0');
END IF;

select count(*) into countt from ibms_account_strategy WHERE ID='3';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('3','密码最长使用期限（天）','超过密码最长使用期限必须修改密码。','0','2','0');
END IF;
select count(*) into countt from ibms_account_strategy WHERE ID='4';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('4','密码最短使用期限（天）','在最短使用期限内不能修改密码，如果“密码最长使用期限”启用，则“密码最短使用期限”必须小于“密码最长使用期限”。','0','1','0');
END IF;

select count(*) into countt from ibms_account_strategy WHERE ID='5';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('5','帐户锁定阈值','设置几次无效登陆后锁定账户','0','5','1');
END IF;

select count(*) into countt from ibms_account_strategy WHERE ID='6';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('6','帐户锁定时间（分钟）','帐户锁定的时间长度，如果定义了帐户锁定阈值，则帐户锁定时间必须大于或等于重置时间。','0','30','1');
END IF;

select count(*) into countt from ibms_account_strategy WHERE ID='7';
 	IF(countt=0) THEN
	  	insert into ibms_account_strategy(ID,STRATEGY_NAME,STRATEGY_EXPLAIN,IS_ENABLE,STRATEGY_VALUE,STRATEGY_TYPE) 
	  	values('7','复位帐户锁定计数器（分钟）','在此后复位帐户锁定计数器，如果定义了帐户锁定阈值，此重置时间必须小于或等于帐户锁定时间。','0','20','1');
END IF;

-- 新增功能菜单账户策略设置-----------------------------------------------
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'zhcl';
IF(countt=0) THEN
	-- 插入功能账户策略节点--zhcl-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('4005','账户策略','zhcl',1,'/styles/default/images/resicon/9.png',3,'/oa/system/accountStrategy/list.do',0,1,1,0,null);
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4005 FROM CWM_SYS_ROLE_RES;

END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sjypzgl';
IF(countt=0) THEN
	-- 数据源配置节点-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(4006,'数据源配置管理','sjypzgl',12,'/styles/default/images/resicon/data.png',3,null,1,1,1,0,null);
	-- 插入数据源模板子节点-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(4007,'数据源模板','sjymb',1,'/images/function/mid/computerjanitor.png',4006,'/oa/system/sysDataSourceTemplate/list.do',1,1,1,0,null);
	-- 插入数据源子节点-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(4008,'数据源','sjy',2,'/styles/default/images/resicon/icon_tab3.png',4006,'/oa/system/sysDataSourceDef/list.do',1,1,1,0,null);
	
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4006 FROM CWM_SYS_ROLE_RES;
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4007 FROM CWM_SYS_ROLE_RES;
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4008 FROM CWM_SYS_ROLE_RES;
	
END IF;

-- 数据源增加默认模板-------2017-04-15 by liubo-----------------------------------------------
SELECT COUNT(*) into countt FROM ibms_sys_data_source_template WHERE ID = '1';
IF(countt=0) THEN
	insert into ibms_sys_data_source_template(ID,NAME,ALIAS,CLASS_PATH,SETTING_JSON,IS_SYSTEM,INIT_METHOD,CLOSE_METHOD)
	values('1','BasicDataSource','BasicDataSource','org.apache.commons.dbcp.BasicDataSource','[{"name":"driverClassName","comment":"链接类型","type":"java.lang.String","baseAttr":true},{"name":"url","comment":"链接地址","type":"java.lang.String","baseAttr":true},{"name":"username","comment":"用户名","type":"java.lang.String","baseAttr":false},{"name":"password","comment":"密码","type":"java.lang.String","baseAttr":false}]','1',null,null);
END IF;
SELECT COUNT(*) into countt FROM ibms_sys_data_source_template WHERE ID = '2';
IF(countt=0) THEN
	insert into ibms_sys_data_source_template(ID,NAME,ALIAS,CLASS_PATH,SETTING_JSON,IS_SYSTEM,INIT_METHOD,CLOSE_METHOD)
	values('2','ProxoolDataSource','ProxoolDataSource','org.logicalcobwebs.proxool.ProxoolDataSource','[{"name":"alias","comment":"别名(唯一)","type":"java.lang.String","baseAttr":true},{"name":"driver","comment":"类型","type":"java.lang.String","baseAttr":true},{"name":"houseKeepingTestSql","comment":"未知houseKeepingTestSql","type":"java.lang.String","baseAttr":false},{"name":"maximumActiveTime","comment":"未知maximumActiveTime","type":"long","baseAttr":false},{"name":"maximumConnectionCount","comment":"最大连接数","type":"int","baseAttr":true},{"name":"minimumConnectionCount","comment":"最少连接数","type":"int","baseAttr":true},{"name":"password","comment":"密码","type":"java.lang.String","baseAttr":true},{"name":"simultaneousBuildThrottle","comment":"未知simultaneousBuildThrottle","type":"int","baseAttr":false},{"name":"driverUrl","comment":"链接地址","type":"java.lang.String","baseAttr":true},{"name":"user","comment":"用户名","type":"java.lang.String","baseAttr":true}]','0',null,'org.logicalcobwebs.proxool.ProxoolFacade|shutdown');
END IF;

-------------判断是否是门户系统-------2017-4-20 by liubo---------------------------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'IS_SHOW_DOOR';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4009','IS_SHOW_DOOR','0','0','是否为门户系统，1表示是，2表示否');
END IF;

------------文件密级属性的设置-------2017-4-21 by liubo---------------------------
select count(*) into countt from CWM_SYS_PARAMETER WHERE NAME = 'FILE_SECURITY_MAP';
 	IF(countt=0) THEN
	  	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION) values('4010','FILE_SECURITY_MAP','0','3:公开-6:秘密-9:机密-12:绝密','文件密级属性键值对的配置，需要与业务表定义中的“密级管理”控制保持一致，格式为“3:公开-6:秘密-9:机密-12:绝密”');
END IF;

--------------------------清除默认的定时计划任务-----2017-5-11 by liubo---------------------------------------------- 
DELETE FROM qrtz_job_details WHERE JOB_NAME='同步记录' and JOB_GROUP='group1';  

-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第一类  基本参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYS_THEME' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYS_UITYPE' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='COMPANY_NAME' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYSTEM_TITLE_LOGO' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYSTEM_TITLE' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYS_LOGIN_PNG' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='SYS_LOGIN_LOG' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='基本参数' WHERE NAME='IS_SHOW_DOOR' AND (TYPE is null or TYPE='');
	
-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第二类  选择器参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='选择器参数' WHERE NAME='userDialog.showPos' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='选择器参数' WHERE NAME='userDialog.showRole' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='选择器参数' WHERE NAME='userDialog.showOnlineUser' AND (TYPE is null or TYPE='');
 
-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第三类  辅助参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='辅助参数' WHERE NAME='task.WarnLevel' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='辅助参数' WHERE NAME='user.menu.redisset' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='辅助参数' WHERE NAME='user.custom.infoset' AND (TYPE is null or TYPE='');

-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第四类  文件参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='UploadFileFolder' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='IS_DISPLAY_SECURITY' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='IS_SAVE_SECURITY' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='FILE_KEY_NAME' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='IS_FILE_FASTDFS' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='文件参数' WHERE NAME='FILE_SECURITY_MAP' AND (TYPE is null or TYPE='');

-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第五类  消息参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='消息参数' WHERE NAME='RTX_NOTIFY_LINK' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='消息参数' WHERE NAME='RTX_NOTIFY_ON_OFF' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='消息参数' WHERE NAME='RTX_RECEIVE_TYPE' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='消息参数' WHERE NAME='IS_COMMUNICATE_EMAIL' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='消息参数' WHERE NAME='IS_PRODUCE_TASK' AND (TYPE is null or TYPE='');

-- ------------修改系统参数的默认分类------2017-8-5 by liubo--------第六类  主数据参数----------------- --
UPDATE CWM_SYS_PARAMETER SET TYPE='主数据参数' WHERE NAME='mdm.user.sync' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='主数据参数' WHERE NAME='mdm.role.sync' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='主数据参数' WHERE NAME='mdm.org.sync' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='主数据参数' WHERE NAME='mdm.pos.sync' AND (TYPE is null or TYPE='');
UPDATE CWM_SYS_PARAMETER SET TYPE='主数据参数' WHERE NAME='mdm.userpos.sync' AND (TYPE is null or TYPE='');

-- ------------修改系统参数的访问路径------2017-8-10 by liubo------------------------ --
UPDATE CWM_SYS_RES SET DEFAULTURL='/oa/system/sysParameter/manage.do' WHERE ALIAS='ParametersView';

--------------新增系统监控节点------2017-8-11 by liubo------------------------ --
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xtjk';
IF(countt=0) THEN
	-- 新增系统监控节点--xtjk-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('4011','系统监控','xtjk',6,'electronics',191,null,1,1,1,0,null);
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4011 FROM CWM_SYS_ROLE_RES;

END IF;

--------------新增数据源监控------2017-8-11 by liubo------------------------ --
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'sjyxxjk';
IF(countt=0) THEN
	-- 新增数据源监控--sjyxxjk-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('4012','数据源信息监控','sjyxxjk',2,'browse',4011,'/druid/index.html',0,1,1,0,null);
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,4012 FROM CWM_SYS_ROLE_RES;

END IF;

--------------更改JMS消息监控节点位置------2017-8-11 by liubo------------------------ --
UPDATE CWM_SYS_RES SET RESID='4013',PARENTID='4011',SN=1  WHERE ALIAS='JMSM';

-------------系统中若无三员默认新增三员(system、right、check)------2017-10-10  by liubo-------------------------------
select count(*) into countt from cwm_sys_user WHERE USERID = '-1';
 	IF(countt=0) THEN
	  	insert into cwm_sys_user(USERNAME,PASSWORD,FULLNAME,SEX,EMAIL,STATUS,DELFLAG,USERID,SECURITY) values('system','54B53072540EEEB8F8E9343E71F28176','系统管理员',1,'system@ibms.com',1,0,-1,'12');
END IF;
select count(*) into countt from cwm_sys_user WHERE USERID = '-2';
 	IF(countt=0) THEN
	  	insert into cwm_sys_user(USERNAME,PASSWORD,FULLNAME,SEX,EMAIL,STATUS,DELFLAG,USERID,SECURITY) values('right','7C4F29407893C334A6CB7A87BF045C0D','安全保密员',1,'right@cssrc.com.cn',1,0,-2,'12');
END IF;
select count(*) into countt from cwm_sys_user WHERE USERID = '-3';
 	IF(countt=0) THEN
	  	insert into cwm_sys_user(USERNAME,PASSWORD,FULLNAME,SEX,EMAIL,STATUS,DELFLAG,USERID,SECURITY) values('check','0BA4439EE9A46D9D9F14C60F88F45F87','安全审计员',1,'check@cssrc.com.cn',1,0,-3,'12');
END IF;

-------------系统新增实施人员-------2017-8-15  by liubo-------------------------------
select count(*) into countt from cwm_sys_user WHERE USERID = '-4';
 	IF(countt=0) THEN
	  	insert into cwm_sys_user(USERNAME,PASSWORD,FULLNAME,SEX,STATUS,DELFLAG,USERID,SECURITY) values('implement','4B9E4636494461CF31094E9A16F456FE','系统实施人员',1,1,0,-4,'12');
END IF;

------------系统新增实施人员角色-------2017-8-15  by liubo----------------------------
select count(*) into countt from cwm_sys_role WHERE ROLEID = '-4';
 	IF(countt=0) THEN
	  	insert into cwm_sys_role(ROLEID,ROLENAME,ROLEDESC,STATUS,ALIAS) values(-4,'系统实施员','系统实施员',1,'系统实施员');
END IF;

-------------将系统实施人员加入系统实施员角色-------2017-8-15  by liubo-----------------------------
select count(*) into countt from cwm_sys_role_user WHERE ROLEID = '-4';
 	IF(countt=0) THEN
	  	insert into cwm_sys_role_user(ROLEID,USERID,USERROLEID) values(-4,-4,-4);
END IF;


-----------新增用户角色分配功能点--2017-8-16  by liubo----------------------- --
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'yhjsfp';
IF(countt=0) THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH) 
	values(4014,'用户角色分配','yhjsfp',1,'supplierfeatures',3,'/oa/system/sysUser/list.do',0,1,1,0,null);
-----------------给right分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-2,4014 FROM CWM_SYS_ROLE_RES;

END IF;
------------新增组织角色授权功能点--2017-8-16  by liubo---------------------- --
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'zzjssq';
IF(countt=0) THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(4015,'组织角色授权','zzjssq',2,'imagetext',3,'/oa/system/sysOrg/list.do',0,1,1,0,null);
------------------给right分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-2,4015 FROM CWM_SYS_ROLE_RES;

END IF;
-------------新增流程人员分配功能点--2017-8-16  by liubo------------------ --
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'lcryfp';
IF(countt=0) THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(4016,'流程人员分配','lcryfp',2,'zhankai',111,'/oa/flow/definition/manage.do',0,1,1,0,null);
-----------------给right分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-2,4016 FROM CWM_SYS_ROLE_RES;

END IF;
---------------------更改判断是否是门户系统的值-------2017-8-17 by liubo------------------------- --
UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='是否为门户系统，1表示是，0表示否' WHERE NAME='IS_SHOW_DOOR';
-- ------------更改工具集成节点位置------2017-8-19 by liubo------------------------ --
UPDATE CWM_SYS_RES SET PARENTID='191' WHERE ALIAS='statistics';
SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID='-4' AND RESID = '5005';
IF(countt=0) THEN
-- ---------------给right分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-4,5005 FROM CWM_SYS_ROLE_RES;
END IF;
-- -----------权限管理员更名为安全保密员-------2017-9-1 by liubo------------------------- --
UPDATE cwm_sys_user SET FULLNAME='安全保密员' WHERE USERID='-2';

---- ------------系统组织path数据格式修改（path不是以‘.’开头并且不为空）------2017-9-13 by liubo------------------------ --
UPDATE CWM_SYS_ORG SET PATH='.'||PATH  WHERE PATH is not null and PATH not like '.%';
---- ------------系统数据字典path数据格式修改------2017-9-13 by liubo------------------------ --
UPDATE CWM_SYS_DIC SET NODEPATH='.'||NODEPATH  WHERE NODEPATH is not null and NODEPATH not like '.%';
UPDATE CWM_SYS_GLTYPE SET NODEPATH='.'||NODEPATH  WHERE NODEPATH is not null and NODEPATH not like '.%';

-- 事务回滚机制-----------------------------------------------
EXCEPTION WHEN others THEN
dbms_output.put_line('synData_liubo 脚本执行错误');
rollback;

commit;
END synData_liubo;