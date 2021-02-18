DROP PROCEDURE IF EXISTS prod_struct_yangbo;
CREATE PROCEDURE prod_struct_yangbo(spacename varchar(100))
BEGIN
DECLARE cols NUMERIC;
DECLARE countt NUMERIC;
-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;

-- --------------do my sql------------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('FILEBLOB');
IF(cols=0)THEN
ALTER TABLE CWM_SYS_FILE  ADD  FILEBLOB BLOB COMMENT'附件内容';
END IF;

SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_OUT_MAIL');
IF(countt=0)THEN
CREATE TABLE `CWM_OUT_MAIL` (
  `MAILID` bigint(20) NOT NULL COMMENT '自增列',
  `TITLE` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '主题',
  `CONTENT` text COLLATE utf8_unicode_ci COMMENT '内容',
  `SENDERADDRESSES` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发件人地址',
  `SENDERNAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '发件人地址别名',
  `RECEIVERADDRESSES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '收件人地址',
  `RECEIVERNAMES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '收件人地址别名',
  `CCADDRESSES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '抄送人地址',
  `BCCANAMES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '暗送人地址别名',
  `BCCADDRESSES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '暗送人地址',
  `CCNAMES` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '抄送人地址别名',
  `EMAILID` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮件ID',
  `TYPES` bigint(20) DEFAULT NULL COMMENT ' 邮件类型 1:收件箱;2:发件箱;3:草稿箱;4:垃圾箱',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `ISREPLY` bigint(20) DEFAULT NULL COMMENT '是否回复',
  `MAILDATE` datetime DEFAULT NULL COMMENT '日期',
  `FILEIDS` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '附件ID',
  `ISREAD` bigint(20) DEFAULT NULL COMMENT '是否已读',
  `SETID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`MAILID`),
  KEY `IDX_OUTMAIL_USERID` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='外部邮件';
END IF;


SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_CWM_OUT_MAIL_ATTACHMENT');
IF(countt=0)THEN
CREATE TABLE `CWM_CWM_OUT_MAIL_ATTACHMENT` (
  `FILEID` bigint(20) NOT NULL COMMENT '主键,文件ID',
  `FILENAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件名',
  `FILEPATH` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '文件存放路径',
  `MAILID` bigint(20) DEFAULT NULL COMMENT '邮件ID',
  PRIMARY KEY (`FILEID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='外部邮件附件表';
END IF;


SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_OUT_MAIL_LINKMAN');
IF(countt=0)THEN
CREATE TABLE `CWM_OUT_MAIL_LINKMAN` (
  `LINKID` bigint(20) NOT NULL COMMENT '主键',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `MAILID` bigint(20) DEFAULT NULL COMMENT '邮件ID',
  `SENDTIME` datetime DEFAULT NULL COMMENT '送送时间',
  `LINKNAME` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人名称',
  `LINKADDRESS` varchar(2000) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '联系人地址',
  `SENDTIMES` bigint(20) DEFAULT NULL COMMENT '发送次数',
  PRIMARY KEY (`LINKID`),
  KEY `IDX_MAILLINKMAN` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='外部邮件最近联系';

END IF;

SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_OUT_MAIL_USETING');
IF(countt=0)THEN
CREATE TABLE `CWM_OUT_MAIL_USETING` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `USERNAME` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户名称',
  `MAILADDRESS` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '外部邮箱地址',
  `MAILPASS` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '外部邮箱密码',
  `SMTPHOST` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'smt主机',
  `SMTPPORT` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'smt端口',
  `POPHOST` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'pop主机',
  `POPPORT` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'pop端口',
  `IMAPHOST` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'imap主机',
  `IMAPPORT` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT 'imap端口',
  `ISDEFAULT` smallint(6) DEFAULT NULL COMMENT '是否默认',
  `MAILTYPE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '接收邮件服务器类型',
  `USESSL` smallint(6) DEFAULT '0' COMMENT '是否使用SSL认证。0：否；1：是',
  `ISVALIDATE` smallint(6) DEFAULT '1' COMMENT '是否需要身份验证。0：否；1：是',
  `ISDELETEREMOTE` smallint(6) DEFAULT '0' COMMENT '下载时，是否删除远程邮件。0：否；1：是',
  `ISHANDLEATTACH` smallint(6) DEFAULT '1' COMMENT '是否下载附件。0：否；1：是',
  PRIMARY KEY (`ID`),
  KEY `IDX_MAILUSERSETTING` (`USERID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='外部邮件用户设置';

END IF;

SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_0A_LINKMAN');
IF(countt=1)THEN
	DROP TABLE CWM_0A_LINKMAN;
END IF;

SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_OA_LINKMAN');
IF(countt=0)THEN
CREATE TABLE `CWM_OA_LINKMAN` (
  `ID` bigint(20) NOT NULL,
  `NAME` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `SEX` varchar(8) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '性别',
  `PHONE` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '电话',
  `EMAIL` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮箱',
  `COMPANY` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '公司',
  `JOB` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '工作',
  `ADDRESS` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '地址',
  `CREATETIME` datetime DEFAULT NULL COMMENT '创建时间',
  `STATUS` smallint(6) DEFAULT '1' COMMENT '状态,1为启用，0为禁用',
  `USERID` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
END IF;

SELECT COUNT(*) into countt FROM cwm_sys_org WHERE ORGID = '1';
IF(countt=0) THEN  
		insert into cwm_sys_org(ORGID,DEMID,ORGNAME, ORGDESC,ORGSUPID, PATH, DEPTH, ORGTYPE,CREATORID, CREATETIME, UPDATEID, UPDATETIME, SN, FROMTYPE, ORGPATHNAME, ISDELETE, CODE, COMPANY, ORGSTAFF, COMPANYID)
		values('1', '1', '奥蓝托', '无锡奥蓝托', '0', '1.', '1', '1', NULL, '2013-01-21', NULL, '2013-01-21', '100', '0', '/奥蓝托', '0', 'A001', NULL, NULL, NULL);
END IF;


SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('FILETEMPHTML');
IF(cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  FILETEMPHTML longtext COMMENT'文件夹树HTML';
END IF;



SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_PROPERTY');
IF(countt=1)THEN
	DROP TABLE CWM_SYS_PROPERTY;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_PARAM' )AND COLUMN_NAME=upper('SOURCEKEY');
IF(cols>0)THEN
	ALTER TABLE CWM_SYS_PARAM MODIFY COLUMN SOURCEKEY VARCHAR(2000);
END IF;


SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('ATTACTEMPHTML');
IF(cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  ATTACTEMPHTML longtext COMMENT'附件列表HTML';
END IF;

UPDATE CWM_SYS_RES set RESNAME='职务管理' WHERE  RESNAME='职位管理';

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('FILING');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  FILING bigint(20) DEFAULT 0 COMMENT'1归档,0未归档';
	update CWM_SYS_FILE set FILING=0;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('PARENTID');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  PARENTID bigint(20) COMMENT'该附件第一版本的id';
	update CWM_SYS_FILE set PARENTID=FILEID;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('ISNEW');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  ISNEW bigint(20) COMMENT'是否最新 1是';
	update CWM_SYS_FILE set ISNEW=1;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('VERSION');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  VERSION varchar(50) COMMENT'初始版本 1.0.0';
	update CWM_SYS_FILE set VERSION='1.0.0';
END IF;


SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_LOG' )AND COLUMN_NAME=upper('JSONDATA');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_LOG  ADD  JSONDATA longtext COMMENT'记录操作后数据';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('STOREWAY');
IF(cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  STOREWAY bigint(20) COMMENT'存储方式 1代表分布式 0代表存储本地服务器';
	update CWM_SYS_FILE set STOREWAY=0;
END IF;

-- --------------创建自定义表单历史表--2017-01-09--------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_FORM_DEF_HI');
IF(countt=0)THEN
	CREATE TABLE `IBMS_FORM_DEF_HI` (
	  `HISID` bigint(20) NOT NULL COMMENT '主键',
	  `FORMDEFID` bigint(20) NOT NULL COMMENT '表单ID',
	  `CATEGORYID` bigint(20) DEFAULT NULL COMMENT '表单分类',
	  `FORMKEY` varchar(200) DEFAULT NULL COMMENT '表单KEY',
	  `SUBJECT` varchar(128) DEFAULT NULL COMMENT '表单标题',
	  `FORMDESC` varchar(200) DEFAULT NULL COMMENT '描述',
	  `HTML` longtext COMMENT '定义HTML',
	  `TEMPLATE` longtext COMMENT 'FREEMAKER模版',
	  `ISDEFAULT` smallint(6) DEFAULT NULL COMMENT '是否缺省',
	  `VERSIONNO` bigint(20) DEFAULT NULL COMMENT '版本号',
	  `TABLEID` bigint(20) DEFAULT NULL COMMENT '表ID',
	  `ISPUBLISHED` smallint(6) DEFAULT NULL COMMENT '是否发布',
	  `PUBLISHEDBY` varchar(20) DEFAULT NULL COMMENT '发布人',
	  `PUBLISHTIME` datetime DEFAULT NULL COMMENT '发布时间',
	  `TABTITLE` varchar(500) DEFAULT NULL COMMENT 'TAB标题',
	  `DESIGNTYPE` smallint(6) DEFAULT NULL COMMENT '设计类型',
	  `TEMPLATESID` varchar(300) DEFAULT NULL COMMENT '模板表对应ID',
	  `CREATEBY` bigint(20) DEFAULT NULL COMMENT '创建人ID',
	  `CREATOR` varchar(50) DEFAULT NULL COMMENT '创建人',
	  `CREATETIME` datetime DEFAULT NULL COMMENT '创建时间',
	  PRIMARY KEY (`HISID`)
	) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='流程表单定义历史记录';
END IF;

-- --------------创建文件分类记录表--2017-01-10--------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE_TYPE');
IF(countt=0)THEN
	CREATE TABLE `CWM_SYS_FILE_TYPE` (
	  `TYPEID` decimal(18,0) NOT NULL COMMENT '分类节点Id',
	  `TYPENAME` varchar(128) NOT NULL COMMENT '名称',
	  `NODEPATH` varchar(64) DEFAULT NULL COMMENT '路径',
	  `DEPTH` decimal(18,0) NOT NULL COMMENT '层次',
	  `PARENTID` decimal(18,0) DEFAULT NULL COMMENT '父节点',
	  `NODEKEY` varchar(64) NOT NULL COMMENT '节点的分类Key',
	  `CATKEY` varchar(64) NOT NULL COMMENT '节点分类的Key，如产品分类Key为PT',
	  `SN` decimal(18,0) NOT NULL COMMENT '序号',
	  `USERID` decimal(18,0) DEFAULT NULL COMMENT '所属用户\r\n当为空则代表为公共分类',
	  `DEPID` decimal(18,0) DEFAULT NULL COMMENT '部门ID',
	  `TYPE` decimal(18,0) NOT NULL DEFAULT '1',
	  `ISLEAF` decimal(18,0) DEFAULT NULL,
	  `NODECODE` varchar(20) DEFAULT NULL,
	  `NODECODETYPE` decimal(18,0) NOT NULL DEFAULT '0',
	  `DATAID` varchar(38) DEFAULT NULL COMMENT '业务表记录Id',
	  `TABLEID` varchar(38) DEFAULT NULL COMMENT '业务表Id'
	)ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='文件分类记录表';
END IF;
-- --------------CWM_SYS_FILE_TYPE表去掉主键--2017-01-10--------------
SELECT COUNT(*) INTO countt FROM information_schema.`COLUMNS` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE_TYPE')AND COLUMN_NAME=upper('TYPEID') and column_key=upper('PRI');
IF(countt>0)THEN
	alter table CWM_SYS_FILE_TYPE drop primary key;
END IF;
-- --------------CWM_SYS_USER表字段去空--2017-01-16--------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_USER' )AND COLUMN_NAME=upper('ACCESSIONTIME')AND IS_NULLABLE=upper('NO');
IF(cols>0)THEN
	ALTER TABLE CWM_SYS_USER MODIFY ACCESSIONTIME DATE NULL;
END IF;
-- --------------CWM_SYS_RES表父节点ISFOLDER值修改--2017-01-17--------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_RES' )AND COLUMN_NAME=upper('ISFOLDER');
IF(cols>0)THEN
	UPDATE cwm_sys_res SET ISFOLDER=1 WHERE RESID in (SELECT a.PARENTID FROM(select b.PARENTID FROM cwm_sys_res b)a);
END IF;
-- --------------cwm_sys_query_field表CONTROL_TYPE数据类型长度修改--2017-01-17--------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_QUERY_FIELD' )AND COLUMN_NAME=upper('CONTROL_TYPE');
IF(cols>0)THEN
	ALTER TABLE CWM_SYS_QUERY_FIELD MODIFY CONTROL_TYPE decimal(10,0) NULL;
END IF;
-- --------------date类型全改为datetime类型---2017-1-21--------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND DATA_TYPE='DATE';
IF(cols>0)THEN
	alter table cwm_dbom MODIFY COLUMN MODIFIED_TIME DATETIME;
	alter table cwm_login_log MODIFY COLUMN LOGINTIME DATETIME;
	alter table cwm_sys_backuprestore MODIFY COLUMN DATETIME DATETIME;
	alter table cwm_sys_errorlog MODIFY COLUMN ERRORDATE DATETIME;
	alter table cwm_sys_file MODIFY COLUMN CREATETIME DATETIME;
	alter table cwm_sys_file_folder MODIFY COLUMN CREATETIME DATETIME;
	alter table cwm_sys_file_folder MODIFY COLUMN UPDATETIME DATETIME;
	alter table cwm_sys_joblog MODIFY COLUMN STARTTIME DATETIME;
	alter table cwm_sys_joblog MODIFY COLUMN ENDTIME DATETIME;
	alter table cwm_sys_log MODIFY COLUMN OPTIME DATETIME;
	alter table cwm_sys_msglog MODIFY COLUMN SENDTIME DATETIME;
	alter table cwm_sys_msgread MODIFY COLUMN RECEIVETIME DATETIME;
	alter table cwm_sys_msgreply MODIFY COLUMN REPLYTIME DATETIME;
	alter table cwm_sys_msgsend MODIFY COLUMN SENDTIME DATETIME;
	alter table cwm_sys_office_template MODIFY COLUMN CREATE_TIME DATETIME;
	alter table cwm_sys_office_template MODIFY COLUMN PUBLISHED_TIME DATETIME;
	alter table cwm_sys_org MODIFY COLUMN CREATETIME DATETIME;
	alter table cwm_sys_org MODIFY COLUMN UPDATETIME DATETIME;
	alter table cwm_sys_org_param MODIFY COLUMN PARAMDATEVALUE DATETIME;
	alter table cwm_sys_quartzlog MODIFY COLUMN STARTTIME DATETIME;
	alter table cwm_sys_quartzlog MODIFY COLUMN ENDTIME DATETIME;
	alter table cwm_sys_seal_right MODIFY COLUMN CREATETIME DATETIME;
	alter table cwm_sys_sign_model MODIFY COLUMN START_DATE DATETIME;
	alter table cwm_sys_sign_model MODIFY COLUMN END_DATE DATETIME;
	alter table cwm_sys_user MODIFY COLUMN ACCESSIONTIME DATETIME;
	alter table cwm_sys_user MODIFY COLUMN BIRTHDAY DATETIME;
	alter table cwm_sys_user MODIFY COLUMN SAFELEVELTIME DATETIME;
	alter table cwm_sys_user MODIFY COLUMN SBIRTHDAY DATETIME;
	alter table cwm_sys_user MODIFY COLUMN WORKTIME DATETIME;
	alter table cwm_sys_user MODIFY COLUMN JIONTIME DATETIME;
	alter table cwm_sys_user_param MODIFY COLUMN PARAMDATEVALUE DATETIME;
	alter table ibms_agent_setting MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_agent_setting MODIFY COLUMN STARTDATE DATETIME;
	alter table ibms_agent_setting MODIFY COLUMN ENDDATE DATETIME;
	alter table ibms_bus_link MODIFY COLUMN BUS_CREATETIME DATETIME;
	alter table ibms_bus_link MODIFY COLUMN BUS_UPDTIME DATETIME;
	alter table ibms_commu_receiver MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_commu_receiver MODIFY COLUMN RECEIVETIME DATETIME;
	alter table ibms_commu_receiver MODIFY COLUMN FEEDBACKTIME DATETIME;
	alter table ibms_definition MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_definition MODIFY COLUMN UPDATETIME DATETIME;
	alter table ibms_exe_stack MODIFY COLUMN STARTTIME DATETIME;
	alter table ibms_exe_stack MODIFY COLUMN ENDTIME DATETIME;
	alter table ibms_form_def MODIFY COLUMN PUBLISHTIME DATETIME;
	alter table ibms_form_table MODIFY COLUMN PUBLISHTIME DATETIME;
	alter table ibms_pro_cpto MODIFY COLUMN CC_TIME DATETIME;
	alter table ibms_pro_cpto MODIFY COLUMN READ_TIME DATETIME;
	alter table ibms_pro_run MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_pro_run MODIFY COLUMN ENDTIME DATETIME;
	alter table ibms_pro_run_his MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_pro_run_his MODIFY COLUMN ENDTIME DATETIME;
	alter table ibms_pro_status MODIFY COLUMN LASTUPDATETIME DATETIME;
	alter table ibms_refer_definition MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_run_log MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_task_exe MODIFY COLUMN CRATETIME DATETIME;
	alter table ibms_task_exe MODIFY COLUMN EXE_TIME DATETIME;
	alter table ibms_task_fork MODIFY COLUMN FORKTIME DATETIME;
	alter table ibms_task_opinion MODIFY COLUMN STARTTIME DATETIME;
	alter table ibms_task_opinion MODIFY COLUMN ENDTIME DATETIME;
	alter table ibms_task_read MODIFY COLUMN CREATETIME DATETIME;
	alter table ibms_task_reminderstate MODIFY COLUMN REMINDERTIME DATETIME;
	alter table ibms_tksign_data MODIFY COLUMN VOTETIME DATETIME;
	alter table mail MODIFY COLUMN SENDTIME DATETIME;
	alter table mail_box MODIFY COLUMN SENDTIME DATETIME;
END IF;


-- ------------栏目分类表--ins_col_type---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_col_type');
IF(countt=0)THEN
	CREATE TABLE `ins_col_type` (
	  `TYPE_ID_` decimal(18,0) NOT NULL,
	  `NAME_` varchar(50) NOT NULL COMMENT '栏目类型名称',
	  `KEY_` varchar(50) NOT NULL COMMENT '栏目类型Key',
	  `URL_` varchar(100) NOT NULL COMMENT '栏目映射URL',
	  `MORE_URL_` varchar(100) DEFAULT NULL,
	  `LOAD_TYPE_` varchar(20) DEFAULT NULL COMMENT '加载类型\r\n            URL=URL\r\n            TEMPLATE=模板',
	  `TEMP_ID_` varchar(64) DEFAULT NULL COMMENT '模板ID',
	  `TEMP_NAME_` varchar(64) DEFAULT NULL COMMENT '模板名称',
	  `ICON_CLS_` varchar(20) DEFAULT NULL,
	  `MEMO_` varchar(512) DEFAULT NULL COMMENT '栏目分类描述',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `CREATE_BY_` varchar(64) NOT NULL COMMENT '创建人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  PRIMARY KEY (`TYPE_ID_`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='栏目分类表';
	
	INSERT INTO `ins_col_type` VALUES (1, '待办事宜', 'myTask', '/oa/flow/task/pendingMatters.do', '/oa/flow/task/pendingMatters.do', 'TEMPLATE', 'pendingMatters', '待办事宜', 'icon-task', '', null, '-1', '2017-03-03 11:05:38', '-1', '0');
	INSERT INTO `ins_col_type` VALUES (10000000290008, '日常办公', 'Box', '/oa/console/columnBox.do', '/oa/console/columnBox.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (10000000370000, '展示', 'slide', '/oa/console/columnSlide.do', '/oa/console/columnSlide.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (10000000370003, '日常办公', 'dailyWork', '/oa/console/columnBox.do', '/oa/console/columnBox.do', 'URL', '', '', '', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (10000000370016, '任务列表', 'taskList', '/oa/console/columnTask.do', '/oa/console/columnTask.do', 'TEMPLATE', 'taskListData', '任务列表', '', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (2, '计划', 'Plan', '/oa/console/columnPlan.do', '/oa/console/columnPlan.do', 'URL', 'msg', 'msg', 'icon-commute', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (210000000066005, '联系人', 'Linkman', '/oa/console/columnLinkman.do', '/oa/console/columnLinkman.do', 'URL', 'kdDoc', 'kdDoc', 'icon-task', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (210000000202022, '会议', 'Meeting', '/oa/console/columnMeeting.do', '/oa/console/columnMeeting.do', 'URL', 'doc', 'doc', 'icon-document', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (3, '日程', 'Calendar', '/oa/console/columnCalendar.do', '/oa/console/columnCalendar.do', 'URL', 'outMail', 'outMail', 'icon-mail', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (4, '项目', 'Project', '/oa/console/columnProject.do', '/oa/console/columnProject.do', 'URL', 'inMail', 'inMail', 'icon-mail', '', null, '-1', null, '-1', '0');
	INSERT INTO `ins_col_type` VALUES (5, '新闻、公告', 'news', '/oa/portal/insNews/list.do?pageSize={pageSize}', '/oa/portal/insNews/list.do', 'TEMPLATE', 'news', '新闻公告', 'icon-detail', '', null, '-1', '2017-03-17 13:59:18', '-1', '0');
	INSERT INTO `ins_col_type` VALUES (6, '规章制度', 'Rule', '/oa/console/columnRules.do', '/oa/console/columnRules.do', 'URL', 'bpmSolutionPortal', 'bpmSolutionPortal', 'icon-start', '', null, '-1', null, '-1', '0');
END IF;

-- ------------布局栏目表--ins_column---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_column');
IF(countt=0)THEN
	CREATE TABLE `ins_column` (
	  `COL_ID_` decimal(18,0) NOT NULL COMMENT '栏目ID',
	  `TYPE_ID_` decimal(18,0) DEFAULT NULL,
	  `NAME_` varchar(80) NOT NULL COMMENT '栏目名称',
	  `KEY_` varchar(50) NOT NULL COMMENT '栏目Key',
	  `URL_` varchar(255) DEFAULT NULL,
	  `ENABLED_` varchar(20) NOT NULL COMMENT '是否启用',
	  `NUMS_OF_PAGE_` int(11) DEFAULT NULL COMMENT '每页记录数',
	  `ALLOW_CLOSE_` varchar(20) DEFAULT NULL COMMENT '是否允许关闭',
	  `COL_TYPE_` varchar(50) DEFAULT NULL COMMENT '信息栏目类型\r\n            公告\r\n            公司或单位新闻\r\n            部门新闻',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  `HEIGHT_` int(11) DEFAULT NULL,
	  PRIMARY KEY (`COL_ID_`),
	  KEY `COLMN_R_COL_TYPE` (`TYPE_ID_`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='布局栏目';
	
	INSERT INTO `ins_column` VALUES (10000000370001, 10000000370000, '展示', 'slidec', null, 'ENABLED', '10', 'YES', '展示', '0', '-1', null, '-1', '2017-03-17 10:33:01', '360');
	INSERT INTO `ins_column` VALUES (10000000370004, 10000000370003, '日常办公', 'dailWork', null, 'ENABLED', '5', 'YES', '日常办公', '0', '-1', null, '-1', null, '360');
	INSERT INTO `ins_column` VALUES (10000000370017, 10000000370016, '任务列表', 'rwlb', null, 'ENABLED', '6', 'NO', '任务列表', '0', '-1', null, '-1', null, '360');
	INSERT INTO `ins_column` VALUES (20000000004002, 5, '新闻公告', 'news', null, 'ENABLED', '7', 'NO', '新闻、公告', '0', '-1', null, '-1', '2017-03-17 10:33:11', '360');
	INSERT INTO `ins_column` VALUES (20000000007001, 2, '新上计划', 'myMsg', null, 'ENABLED', '7', 'NO', '计划', '0', '-1', null, '-1', null, '300');
	INSERT INTO `ins_column` VALUES (20000000014001, 4, '新上项目', 'innerMail', null, 'ENABLED', '7', 'YES', '项目', '0', '-1', null, '-1', null, '300');
	INSERT INTO `ins_column` VALUES (20000000099097, 6, '规章制度', 'solApply', null, 'ENABLED', '7', 'YES', '规章制度', '0', '-1', null, '-1', null, '300');
	INSERT INTO `ins_column` VALUES (20000000167026, 1, '我的待办', 'MyTask', null, 'ENABLED', '5', 'YES', '待办事宜', '0', '-1', null, '-1', null, '415');
	INSERT INTO `ins_column` VALUES (210000000066006, 210000000066005, '联系人', 'kdDoc', null, 'ENABLED', '5', 'YES', '联系人', '0', '-1', null, '-1', null, '300');
	INSERT INTO `ins_column` VALUES (210000000202023, 210000000202022, '会议安排', 'document', null, 'ENABLED', '7', 'YES', '会议', '0', '-1', null, '-1', null, '300');
	INSERT INTO `ins_column` VALUES (210000000335008, 3, '日程', 'outMail', null, 'ENABLED', '5', 'YES', '日程', '0', '-1', null, '-1', null, '400');
END IF;

-- ------------个人组织门户定义表--ins_portal---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_portal');
IF(countt=0)THEN
	CREATE TABLE `ins_portal` (
	  `PORT_ID_` decimal(18,0) NOT NULL,
	  `NAME_` varchar(128) NOT NULL COMMENT '门户名称',
	  `KEY_` varchar(64) NOT NULL COMMENT '门户KEY\r\n            个人门户\r\n            公司门户\r\n            部门门户\r\n            知识门户',
	  `COL_NUMS_` int(11) DEFAULT NULL COMMENT '列数',
	  `COL_WIDTHS_` varchar(50) DEFAULT NULL COMMENT '栏目宽\r\n            三列格式如250,100%,400',
	  `IS_DEFAULT_` varchar(20) NOT NULL COMMENT '是否为系统缺省',
	  `DESC_` varchar(512) DEFAULT NULL COMMENT '描述',
	  `USER_ID_` varchar(64) DEFAULT NULL,
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  PRIMARY KEY (`PORT_ID_`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='个人组织门户定义';
	
	INSERT INTO `ins_portal` VALUES (1, '公司全局', 'GLOBAL-ORG', '3', '350,100%,350', 'YES', null, null, '0', '-1', null, '-1', null);
	INSERT INTO `ins_portal` VALUES (2, '个人全局', 'GLOBAL-PERSONAL', '2', '800,100%', 'YES', '', null, '0', '-1', null, '-1', null);
END IF;

-- ------------门户栏目配置表--ins_port_col---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_port_col');
IF(countt=0)THEN
	CREATE TABLE `ins_port_col` (
	  `CONF_ID_` decimal(18,0) NOT NULL,
	  `PORT_ID_` decimal(18,0) NOT NULL COMMENT '门户ID',
	  `COL_ID_` decimal(18,0) NOT NULL COMMENT '栏目ID',
	  `WIDTH_` int(11) DEFAULT NULL COMMENT '宽度',
	  `HEIGHT_` int(11) NOT NULL COMMENT '高度',
	  `WIDTH_UNIT_` varchar(8) DEFAULT NULL COMMENT '宽度单位\r\n            百份比=%\r\n            像数=px',
	  `HEIGHT_UNIT_` varchar(8) NOT NULL COMMENT '高度单位\r\n            百份比=%\r\n            像数=px',
	  `COL_NUM_` int(11) DEFAULT NULL COMMENT '列号',
	  `SN_` int(11) NOT NULL COMMENT '列中序号',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  PRIMARY KEY (`CONF_ID_`),
	  KEY `PORT_COL_R_INS_COL` (`COL_ID_`),
	  KEY `PORT_COL_R_INS_PORAL` (`PORT_ID_`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='门户栏目配置表';
	
	INSERT INTO `ins_port_col` VALUES (10000000540004, 1, 210000000335008, null, '400', null, 'px', '0', '11', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540005, 1, 210000000202023, null, '300', null, 'px', '0', '12', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540006, 1, 20000000099097, null, '300', null, 'px', '0', '15', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540007, 1, 20000000014001, null, '300', null, 'px', '0', '16', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540008, 1, 10000000370017, null, '360', null, 'px', '0', '19', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540009, 1, 10000000370004, null, '360', null, 'px', '0', '20', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540010, 1, 10000000370001, null, '360', null, 'px', '0', '21', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540011, 2, 210000000335008, null, '400', null, 'px', '1', '7', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540012, 2, 210000000202023, null, '300', null, 'px', '1', '9', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540013, 2, 210000000066006, null, '300', null, 'px', '0', '3', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540014, 2, 20000000099097, null, '300', null, 'px', '1', '8', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540015, 2, 20000000014001, null, '300', null, 'px', '0', '4', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540016, 2, 20000000007001, null, '300', null, 'px', '1', '10', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540017, 2, 10000000370017, null, '360', null, 'px', '0', '2', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540018, 2, 10000000370004, null, '360', null, 'px', '0', '1', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540019, 2, 10000000370001, null, '360', null, 'px', '0', '0', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (10000000540020, 2, 20000000004002, null, '360', null, 'px', '1', '6', '0', '-1', '2017-03-17 00:00:00', '-1', '2017-03-17 00:00:00');
	INSERT INTO `ins_port_col` VALUES (240000000006044, 1, 20000000004002, null, '600', null, 'px', '0', '1', '0', '-1', '2017-02-17 00:00:00', '-1', null);
	INSERT INTO `ins_port_col` VALUES (240000000006045, 1, 20000000007001, null, '500', null, 'px', '0', '0', '0', '-1', '2017-02-17 00:00:00', '-1', null);
	INSERT INTO `ins_port_col` VALUES (240000000006046, 1, 20000000167026, null, '100', null, 'px', '0', '2', '0', '-1', '2017-02-17 00:00:00', '-1', null);
	INSERT INTO `ins_port_col` VALUES (240000000006047, 1, 210000000066006, null, '300', null, 'px', '1', '3', '0', '-1', '2017-02-17 00:00:00', '-1', null);	
END IF;

-- ------------新闻公告表--ins_news---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_news');
IF(countt=0)THEN
	CREATE TABLE `ins_news` (
	  `NEW_ID_` decimal(18,0) NOT NULL,
	  `SUBJECT_` varchar(120) NOT NULL COMMENT '标题',
	  `TAG_` varchar(80) DEFAULT NULL COMMENT '标签',
	  `KEYWORDS_` varchar(255) DEFAULT NULL COMMENT '关键字',
	  `CONTENT_` longtext COMMENT '内容',
	  `IS_IMG_` varchar(20) DEFAULT NULL COMMENT '是否为图片新闻',
	  `IMG_FILE_ID_` varchar(64) DEFAULT NULL COMMENT '图片文件ID',
	  `READ_TIMES_` int(11) NOT NULL COMMENT '阅读次数',
	  `AUTHOR_` varchar(50) DEFAULT NULL COMMENT '作者',
	  `ALLOW_CMT_` varchar(20) DEFAULT NULL COMMENT '是否允许评论',
	  `STATUS_` varchar(20) NOT NULL COMMENT '状态',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  PRIMARY KEY (`NEW_ID_`)
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻公告表';
END IF;
-- ------------公告或新闻评论--ins_news_cm---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_news_cm');
IF(countt=0)THEN
	CREATE TABLE `ins_news_cm` (
	  `COMM_ID_` decimal(18,0) NOT NULL COMMENT '评论ID',
	  `NEW_ID_` decimal(18,0) NOT NULL COMMENT '信息ID',
	  `FULL_NAME_` varchar(50) NOT NULL COMMENT '评论人名',
	  `CONTENT_` varchar(1024) NOT NULL COMMENT '评论内容',
	  `AGREE_NUMS_` int(11) NOT NULL COMMENT '赞同与顶',
	  `REFUSE_NUMS_` int(11) NOT NULL COMMENT '反对与鄙视次数',
	  `IS_REPLY_` varchar(20) NOT NULL COMMENT '是否为回复',
	  `REP_ID_` varchar(64) DEFAULT NULL COMMENT '回复评论ID',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) NOT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  PRIMARY KEY (`COMM_ID_`),
	  KEY `FK_INS_NEWCM_R_INS_NEW` (`NEW_ID_`),
	  CONSTRAINT `ins_news_cm_ibfk_1` FOREIGN KEY (`NEW_ID_`) REFERENCES `ins_news` (`NEW_ID_`) ON DELETE CASCADE
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='公告或新闻评论';
END IF;



-- ------------新闻栏目表---ins_col_new---2017-3-18-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_col_new');
IF(countt=0)THEN
	CREATE TABLE `ins_col_new` (
	  `ID_` decimal(18,0) NOT NULL COMMENT 'ID_',
	  `COL_ID_` decimal(18,0) NOT NULL COMMENT '栏目ID',
	  `NEW_ID_` decimal(18,0) NOT NULL COMMENT '新闻ID',
	  `SN_` int(11) NOT NULL COMMENT '序号',
	  `START_TIME_` datetime NOT NULL COMMENT '有效开始时间',
	  `END_TIME_` datetime NOT NULL COMMENT '有效结束时间',
	  `IS_LONG_VALID_` varchar(20) DEFAULT NULL COMMENT '是否长期有效',
	  `ORG_ID_` varchar(64) DEFAULT NULL COMMENT '组织ID',
	  `CREATE_BY_` varchar(64) DEFAULT NULL COMMENT '创建人ID',
	  `CREATE_TIME_` datetime DEFAULT NULL COMMENT '创建时间',
	  `UPDATE_BY_` varchar(64) DEFAULT NULL COMMENT '更新人ID',
	  `UPDATE_TIME_` datetime DEFAULT NULL COMMENT '更新时间',
	  PRIMARY KEY (`ID_`),
	  KEY `COL_NEW_R_COLMN` (`COL_ID_`),
	  KEY `IS_CN_R_NEWS` (`NEW_ID_`),
	  CONSTRAINT `ins_col_new_ibfk_1` FOREIGN KEY (`COL_ID_`) REFERENCES `ins_column` (`COL_ID_`) ON DELETE CASCADE,
	  CONSTRAINT `ins_col_new_ibfk_2` FOREIGN KEY (`NEW_ID_`) REFERENCES `ins_news` (`NEW_ID_`) ON DELETE CASCADE
	) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='新闻栏目表';
END IF;

-- ------------日程主表---cwm_sys_agenda---2017-3-25-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_agenda');
IF(countt=0)THEN
CREATE TABLE `cwm_sys_agenda` (
  `AGENDA_ID_` decimal(18,0) NOT NULL COMMENT '日程主键',
  `TYPE_` varchar(255) DEFAULT NULL COMMENT '日程类型',
  `SUBJECT_` varchar(255) DEFAULT NULL COMMENT '标题',
  `CONTENT_` varchar(255) DEFAULT NULL COMMENT '内容',
  `CREATOR_ID_` decimal(18,0) DEFAULT NULL COMMENT '创建人id',
  `CREATOR_` varchar(255) DEFAULT NULL COMMENT '创建人',
  `START_TIME_` datetime DEFAULT NULL COMMENT '开始时间',
  `END_TIME_` datetime DEFAULT NULL COMMENT '结束时间',
  `GRADE_` varchar(255) DEFAULT NULL COMMENT '紧急程度',
  `WARN_WAY_` decimal(18,0) DEFAULT NULL COMMENT '提醒方式：0不提醒，1邮件，2站内信，3手机',
  `FILE_ID_` decimal(18,0) DEFAULT NULL COMMENT '文件ID',
  `RUN_ID_` decimal(18,0) DEFAULT NULL COMMENT '任务ID',
  `STATUS_` decimal(18,0) DEFAULT NULL COMMENT '状态 0：未完成 1：完成',
  PRIMARY KEY (`AGENDA_ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程主表';
END IF;

-- ------------日程执行人员表---cwm_sys_agenda_execut---2017-3-25-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_agenda_execut');
IF(countt=0)THEN
CREATE TABLE `cwm_sys_agenda_execut` (
  `ID_` decimal(18,0) NOT NULL COMMENT '主键',
  `AGENDA_ID_` decimal(18,0) NOT NULL COMMENT '日程ID',
  `EXECUTOR_ID_` decimal(18,0) DEFAULT NULL COMMENT '执行人ID',
  `EXECUTOR_` varchar(255) DEFAULT NULL COMMENT '执行人',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程执行人员';
END IF;
-- ------------日程工作交流---cwm_sys_agenda_msg---2017-3-25-----------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_agenda_msg');
IF(countt=0)THEN
CREATE TABLE `cwm_sys_agenda_msg` (
  `ID_` decimal(18,0) NOT NULL,
  `AGENDA_ID_` decimal(18,0) DEFAULT NULL COMMENT '日程ID',
  `CONTENTS_` longtext COMMENT '评论内容',
  `REPLY_ID_` decimal(18,0) DEFAULT NULL COMMENT '回复人ID',
  `REPLYER_` varchar(255) DEFAULT NULL COMMENT '回复人',
  `SEND_TIME_` datetime DEFAULT NULL COMMENT '发送时间',
  PRIMARY KEY (`ID_`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='日程工作交流';
END IF;

-- ------------用户来源frome_Type_字段添加---2017-3-30-----------------------
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('FROM_TYPE_');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  FROM_TYPE_ varchar(255) COMMENT'数据来源 默认为系统添加';
END IF;

-- ------------更新所有图标---2017-5-24-----------------------
SELECT COUNT(*) INTO cols FROM CWM_SYS_RES WHERE ALIAS = 'root' and ICON='mainpage';
IF(cols=0)THEN
	UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='root';
	UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='webserver';
	UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='system';
	UPDATE CWM_SYS_RES  set ICON='viewgallery' where ALIAS='function';
	UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='flowform';
	UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='formTable';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='formTemplate';
	UPDATE CWM_SYS_RES  set ICON='account' where ALIAS='sysuser';
	UPDATE CWM_SYS_RES  set ICON='atmaway' where ALIAS='sysrole';
	UPDATE CWM_SYS_RES  set ICON='viewgallery' where ALIAS='settingcenter';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='taskApproval';
	UPDATE CWM_SYS_RES  set ICON='set1' where ALIAS='SYS_USER_AGENT';
	UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='client';
	UPDATE CWM_SYS_RES  set ICON='process' where ALIAS='rcp1';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='formDesign';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='admintaskApproval';
	UPDATE CWM_SYS_RES  set ICON='assessedbadge' where ALIAS='formRule';
	UPDATE CWM_SYS_RES  set ICON='training' where ALIAS='formDialog';
	UPDATE CWM_SYS_RES  set ICON='circle1' where ALIAS='flow';
	UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='flowDesign';
	UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='flowInstance';
	UPDATE CWM_SYS_RES  set ICON='electrical' where ALIAS='flowDelegate';
	UPDATE CWM_SYS_RES  set ICON='video' where ALIAS='processrunhistroy';
	UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='bpmRunLog';
	UPDATE CWM_SYS_RES  set ICON='skip' where ALIAS='agentDelePro';
	UPDATE CWM_SYS_RES  set ICON='comments' where ALIAS='processRun';
	UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='pendTask';
	UPDATE CWM_SYS_RES  set ICON='survey1' where ALIAS='alreadyTask';
	UPDATE CWM_SYS_RES  set ICON='templatedefault' where ALIAS='completeTask';
	UPDATE CWM_SYS_RES  set ICON='skip' where ALIAS='accordingTask';
	UPDATE CWM_SYS_RES  set ICON='xml' where ALIAS='myDrafts';
	UPDATE CWM_SYS_RES  set ICON='circle1' where ALIAS='proTrans';
	UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='proCpy';
	UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='DemensionView';
	UPDATE CWM_SYS_RES  set ICON='blueprintagree' where ALIAS='OrganizationView';
	UPDATE CWM_SYS_RES  set ICON='link' where ALIAS='SysFileAttachView';
	UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='SysLogView';
	UPDATE CWM_SYS_RES  set ICON='supplierfeatures' where ALIAS='SysJobView';
	UPDATE CWM_SYS_RES  set ICON='pin' where ALIAS='SysFileAttachView';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='subLog';
	UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='sysErrorLog';
	UPDATE CWM_SYS_RES  set ICON='signboard' where ALIAS='sysLogSwitch';
	UPDATE CWM_SYS_RES  set ICON='compass' where ALIAS='gradeManager';
	UPDATE CWM_SYS_RES  set ICON='discount' where ALIAS='Develop';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='SerialNumberView';
	UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='Sysscript';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='Codetemplate';
	UPDATE CWM_SYS_RES  set ICON='training' where ALIAS='CodeGenal';
	UPDATE CWM_SYS_RES  set ICON='folder' where ALIAS='typekey';
	UPDATE CWM_SYS_RES  set ICON='history' where ALIAS='Quartz';
	UPDATE CWM_SYS_RES  set ICON='service' where ALIAS='WSM';
	UPDATE CWM_SYS_RES  set ICON='inquirytemplate' where ALIAS='JMSM';
	UPDATE CWM_SYS_RES  set ICON='3column' where ALIAS='sysTypeKey';
	UPDATE CWM_SYS_RES  set ICON='navlist' where ALIAS='GlobalTypeManager';
	UPDATE CWM_SYS_RES  set ICON='more1' where ALIAS='DicManager';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='SysScript';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='ConditionScript';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='PersonScript';
	UPDATE CWM_SYS_RES  set ICON='security' where ALIAS='HiddenFunction';
	UPDATE CWM_SYS_RES  set ICON='folder' where ALIAS='ShareFIFile';
	UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='formQuery';
	UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='sqlquery';
	UPDATE CWM_SYS_RES  set ICON='atm' where ALIAS='InnerMessage';
	UPDATE CWM_SYS_RES  set ICON='feedback' where ALIAS='readmessage';
	UPDATE CWM_SYS_RES  set ICON='share' where ALIAS='sendmessage';
	UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='sendMsg';
	UPDATE CWM_SYS_RES  set ICON='email' where ALIAS='mail';
	UPDATE CWM_SYS_RES  set ICON='email' where ALIAS='outMailAll';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='lookMail';
	UPDATE CWM_SYS_RES  set ICON='wrong' where ALIAS='delMail';
	UPDATE CWM_SYS_RES  set ICON='inquirytemplate' where ALIAS='mailAdd';
	UPDATE CWM_SYS_RES  set ICON='save' where ALIAS='saveOutmail';
	UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='sendMail';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='mailManage';
	UPDATE CWM_SYS_RES  set ICON='add' where ALIAS='addMail';
	UPDATE CWM_SYS_RES  set ICON='print' where ALIAS='setDefault';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='editOutmail';
	UPDATE CWM_SYS_RES  set ICON='wrong' where ALIAS='deloutMail';
	UPDATE CWM_SYS_RES  set ICON='feedback' where ALIAS='消息模板管理';
	UPDATE CWM_SYS_RES  set ICON='map' where ALIAS='otherTools';
	UPDATE CWM_SYS_RES  set ICON='reject' where ALIAS='manageTask';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='hysrcck';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='ParametersView';
	UPDATE CWM_SYS_RES  set ICON='save' where ALIAS='BackAndRestore';
	UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='SysIndexManage';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='SysIndexLayoutManage';
	UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='sysIndexMyLayout';
	UPDATE CWM_SYS_RES  set ICON='4column' where ALIAS='SysIndexColumn';
	UPDATE CWM_SYS_RES  set ICON='move' where ALIAS='SysIndexLayout';
	UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='DBomView';
	UPDATE CWM_SYS_RES  set ICON='filter' where ALIAS='SealManage';
	UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='test';
	UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='ReportManage';
	UPDATE CWM_SYS_RES  set ICON='favorite' where ALIAS='FinereportManage';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='PageOfficeManage';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='myStartPro';
	UPDATE CWM_SYS_RES  set ICON='imagetext' where ALIAS='myUndertakePro';
	UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='myRequest';
	UPDATE CWM_SYS_RES  set ICON='templatedefault' where ALIAS='myCompleted';
	UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='myDelegate';
	UPDATE CWM_SYS_RES  set ICON='pic' where ALIAS='indexManage';
	UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='idexPortal';
	UPDATE CWM_SYS_RES  set ICON='4column' where ALIAS='columnType';
	UPDATE CWM_SYS_RES  set ICON='3column' where ALIAS='portalColumn';
	UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='newsManage';
	UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='newsComment';
	UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='agenda';
	UPDATE CWM_SYS_RES  set ICON='table' where ALIAS='myAgenda';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='agendaMsg';
	UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='serachAgenda';
	UPDATE CWM_SYS_RES  set ICON='gerenzhongxin' where ALIAS='zhcl';
	UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='sjypzgl';
	UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='sjymb';
	UPDATE CWM_SYS_RES  set ICON='electrical' where ALIAS='sjy';
	UPDATE CWM_SYS_RES  set ICON='security' where ALIAS='xmqxgl';
	UPDATE CWM_SYS_RES  set ICON='imagetext' where ALIAS='xmgndlb';
	UPDATE CWM_SYS_RES  set ICON='reject' where ALIAS='xmjslb';
	UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='testte';
END IF;

-- -------------- 更新所有图标end--------------


-- --------------do my sql end--------------

COMMIT;
END;
CALL prod_struct_yangbo(@spacename);

