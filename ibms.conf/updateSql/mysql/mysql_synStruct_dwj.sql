DROP PROCEDURE IF EXISTS prod_struct_dwj;
CREATE PROCEDURE prod_struct_dwj(spacename varchar(100))
BEGIN
DECLARE old_cols NUMERIC;
DECLARE new_cols NUMERIC;
DECLARE new_tabs NUMERIC;

-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;


-- --------------do my sql------------------
-- 1、表单类别表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_type');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_type` (
  `TYPEID`   bigint(20) NOT NULL,
  `TYPENAME` varchar(128) NOT NULL COMMENT '表单类别名称',
   `ALIAS` 	 varchar(128) DEFAULT NULL COMMENT '别名',
  `TYPEDESC` varchar(128) DEFAULT NULL COMMENT '表单类别描述',
  PRIMARY KEY (`TYPEID`)
) ENGINE=InnoDB  COMMENT='表单类别表';
END IF;

-- 2、表单功能点表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_fun');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_fun` (
  `TYPEID` bigint(20) NOT NULL COMMENT '外键（ibms_rec_type）',
  `FUNID` bigint(20) NOT NULL,
  `FUNNAME`  varchar(128) NOT NULL COMMENT '表单功能点名称',
  `ALIAS` 	 varchar(128) DEFAULT NULL COMMENT '别名',
  `FUNDESC` 	 varchar(128) DEFAULT NULL COMMENT '描述',
  `SN` 		 bigint(20) DEFAULT NULL COMMENT '同级排序',
  `ICON` 	 varchar(100) DEFAULT NULL COMMENT '图标',
  `PARENTID` bigint(20) DEFAULT NULL COMMENT '父ID',
  `DEFAULTURL` varchar(256) DEFAULT NULL COMMENT '默认地址',
  `ISFOLDER` bigint(6) DEFAULT NULL COMMENT '栏目',
  `ISDISPLAYINMENU` bigint(6) DEFAULT NULL COMMENT '显示到菜单',
  `ISOPEN` bigint(6) DEFAULT NULL COMMENT '默认打开',
  `ISNEWOPEN` bigint(6) DEFAULT NULL COMMENT '是否打开新窗口：0否,1是',
  `PATH` varchar(500) DEFAULT NULL COMMENT '资源路径',
  `BUTTONARR` text DEFAULT NULL COMMENT '顶部按钮、管理列按钮信息',
  PRIMARY KEY (`FUNID`)
) ENGINE=InnoDB COMMENT='表单功能点表';
END IF;


-- 4.表单角色表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_role');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_role` (
  `TYPEID` bigint(20) NOT NULL COMMENT '外键（ibms_rec_type）',
  `ROLEID` bigint(18) NOT NULL,
  `ROLENAME` varchar(128) NOT NULL COMMENT '角色名称',
  `ALIAS` varchar(128) NOT NULL COMMENT '别名',
  `ROLEDESC` varchar(128) DEFAULT NULL COMMENT '角色描述',
  `STATUS` bigint(38) NOT NULL COMMENT '是否启用',
  `ALLOWDEL` bigint(18) DEFAULT NULL,
  `ALLOWEDIT` bigint(1) DEFAULT NULL,
  `ALLOWSET` bigint(1)  DEFAULT NULL COMMENT '是否允许设置；0：不可以设置，1：可以设置',
  PRIMARY KEY (`ROLEID`)
) ENGINE=InnoDB COMMENT='表单角色表';
END IF;


-- 5.表单角色与表单功能点关联表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_role_fun');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_role_fun` (
  `ROLEID` bigint(20) NOT NULL COMMENT '外键（ibms_rec_role）',
  `FUNID`  bigint(20) NOT NULL COMMENT '外键（ibms_rec_fun）',
  `ROLEFUNID` bigint(20) NOT NULL,
  `BUTTONS` text,
  PRIMARY KEY (`ROLEFUNID`)
) ENGINE=InnoDB  COMMENT='表单角色与表单功能点关联表';
END IF;

-- 已废除：采用角色过滤条件
-- 6.表单角色与系统用户、角色、组织关联表*
-- SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_role_meta');
-- IF(new_tabs=0)THEN
-- CREATE TABLE `ibms_rec_role_meta` (
--  `TYPE`  bigint(2) NOT NULL COMMENT '0:user,1:role,2:org',
--  `ROLEID` bigint(20) NOT NULL COMMENT '外键（ibms_rec_role）',
--  `USERID`  bigint(20)  COMMENT '外键（cwm_sys_user）',
--  `SYSROLEID`  bigint(20) COMMENT '外键（cwm_sys_role）',
--  `SYSORGID`  bigint(20) COMMENT '外键（cwm_sys_org）',
--  `ROLEMETAID` bigint(20) NOT NULL,
--  PRIMARY KEY (`ROLEMETAID`)
-- ) ENGINE=InnoDB  COMMENT='表单角色与系统用户、角色、组织关联表';
-- END IF;

-- 7.业务数据模板-数据模板与表单权限关联字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('RECRIGHTFIELD');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  RECRIGHTFIELD text COMMENT '数据模板与表单权限关联字段';
END IF;

-- 8.业务数据模板-明细多TAb HTML字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('MULTITABTEMPHTML');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  MULTITABTEMPHTML longtext COMMENT '明细多TAb HTML';
END IF;

-- 9.记录角色表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_roleson');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_roleson` (
  `ROLESONID` bigint(20) NOT NULL,
  `TYPEID`  bigint(20) NOT NULL COMMENT '外键（ibms_rec_type）',
  `TYPENAME`  varchar(128) DEFAULT NULL COMMENT '类型名称',
  `DATATEMPLATEID` bigint(20) NOT NULL COMMENT '外键（ibms_data_template）',
  `DATAID`  bigint(20) DEFAULT NULL COMMENT '当前记录的ID',
  `ROLESONNAME` varchar(128) NOT NULL COMMENT '角色名称',
  `ALIAS` varchar(128) NOT NULL COMMENT '别名',
  `ROLESONDESC` varchar(128) DEFAULT NULL COMMENT '角色描述',
  PRIMARY KEY (`ROLESONID`)
) ENGINE=InnoDB COMMENT='记录角色表';
END IF;

-- 10.记录角色与表单功能点关联表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_roleson_fun');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_rec_roleson_fun` (
  `ROLESONFUNID` bigint(20) NOT NULL,	
  `ROLESONID` bigint(20) NOT NULL COMMENT '外键（ibms_rec_roleson）',
  `FUNID`  bigint(20) NOT NULL COMMENT '外键（ibms_rec_fun）',
  `BUTTONS` text,
  PRIMARY KEY (`ROLESONFUNID`)
) ENGINE=InnoDB  COMMENT='记录角色与表单功能点关联表';
END IF;

-- 已废除：采用角色过滤条件
-- 11.记录角色与系统用户关联表
-- SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_rec_roleson_user');
-- IF(new_tabs=0)THEN
-- CREATE TABLE `ibms_rec_roleson_user` (
--  `ROLESONID`  bigint(20) NOT NULL COMMENT '外键（ibms_rec_roleson）',
--  `USERID`  bigint(20)  COMMENT '外键（cwm_sys_user）',
--  `ROLESONUSERID` bigint(20) NOT NULL,
--  PRIMARY KEY (`ROLESONUSERID`)
-- ) ENGINE=InnoDB  COMMENT='记录角色与系统用户关联表';
-- END IF;

-- 12.修改CWM_SYS_FILE表，添加维度属性
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_FILE' )AND COLUMN_NAME=upper('DIMENSION');
IF(new_cols=0)THEN
	ALTER TABLE CWM_SYS_FILE  ADD  DIMENSION varchar(256) COMMENT '维度属性';
END IF;

-- 13.修改IBMS_REC_ROLE表，添加过滤条件
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLE' )AND COLUMN_NAME=upper('FILTER');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLE  ADD  FILTER text COMMENT '过滤条件（人员、角色、组织等）';
END IF;

-- 14.默认角色表 -新增FILTER字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLE' )AND COLUMN_NAME=upper('FILTER');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLE  ADD  FILTER varchar(2000) COMMENT '角色过滤条件';
END IF;
-- 15.记录角色表 -新增FILTER、USERADD、USERDEL、ROLEID 字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('FILTER');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  FILTER varchar(2000) COMMENT '角色过滤条件';
END IF;
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('USERADD');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  USERADD varchar(2000) COMMENT '用户增加的人员';
END IF;
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('USERDEL');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  USERDEL varchar(2000) COMMENT '用户删除的人员';
END IF;
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('ROLEID');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  ROLEID bigint(20) COMMENT '存储项目角色的主键';
END IF;

-- 16.项目角色表 -新增isHide字段   2017年5月31日
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLE' )AND COLUMN_NAME=upper('ISHIDE');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLE  ADD  ISHIDE bigint(1) COMMENT '对应的记录角色是否隐藏(0,不隐藏,1,隐藏)';
END IF;
-- 17.项目记录角色表 -新增allowDel\isHide\def_filter字段   2017年5月31日
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('ALLOWDEL');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  ALLOWDEL bigint(1) COMMENT '允许删除(0,不允许,1,允许)';
END IF;

SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('ISHIDE');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  ISHIDE bigint(1) COMMENT '是否隐藏(0,不隐藏,1,隐藏)';
END IF;

SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_REC_ROLESON' )AND COLUMN_NAME=upper('DEF_FILTER');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_REC_ROLESON  ADD  DEF_FILTER text COMMENT '用于开发人员添加过滤条件，格式与filter相同';
END IF;

-- 18.新增用户个性化信息设置表   2017年6月8日
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_USER_CUST');
IF(new_tabs=0)THEN
CREATE TABLE `CWM_SYS_USER_CUST` (
  `USERID`   bigint(20) NOT NULL,
  `CUSTOMINFO` longtext COMMENT '用户个性化信息',
  PRIMARY KEY (`USERID`)
) ENGINE=InnoDB  COMMENT='用户个性化信息设置表';
END IF;

-- 19.新增数据统计工具表   2017年7月4日
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_sta_tool');
IF(new_tabs=0)THEN
CREATE TABLE `ibms_sta_tool` (
  `TOOLID` bigint(20) NOT NULL,
  `NAME`  varchar(128) DEFAULT NULL COMMENT '工具名称',
  `ALIAS` varchar(128) NOT NULL COMMENT '别名',
  `TOOLDESC`  varchar(1000) DEFAULT NULL COMMENT '说明',
  PRIMARY KEY (`TOOLID`)
) ENGINE=InnoDB COMMENT='数据统计工具表';
END IF;

-- 20.新增数据统计访问地址表   2017年7月4日
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_STA_ADDRESS');
IF(new_tabs=0)THEN
CREATE TABLE `IBMS_STA_ADDRESS` (
  `ADDRESSID` bigint(20) NOT NULL,
  `TOOLID`    bigint(20) NOT NULL,
  `ALIAS`    varchar(128) NOT NULL COMMENT '别名',
  `URL`       varchar(1000) DEFAULT NULL COMMENT '访问路径',
  `VIEWDEF`   longtext DEFAULT NULL COMMENT '展示视图定制',
  `ADDRESSDESC`  varchar(2000) DEFAULT NULL COMMENT '访问路径说明',
  PRIMARY KEY (`ADDRESSID`)
) ENGINE=InnoDB COMMENT='数据统计访问地址表';
END IF;

-- 21.ibms_form_template 新增  HEADHTML字段   (2017年7月12日)
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_template' )AND COLUMN_NAME=upper('HEADHTML');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_FORM_TEMPLATE  ADD  HEADHTML longtext COMMENT '业务数据列表、自定义sql列表中自定义js，css';
END IF;

-- 22.ibms_form_def 新增  HEADHTML字段   (2017年7月13日)
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_def' )AND COLUMN_NAME=upper('HEADHTML');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_FORM_DEF  ADD  HEADHTML longtext COMMENT '表单设计自定义js，css';
END IF;

-- 22.ibms_form_def_hi 新增  HEADHTML字段   (2017年7月13日)
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_def_hi' )AND COLUMN_NAME=upper('HEADHTML');
IF(new_cols=0)THEN
	ALTER TABLE IBMS_FORM_DEF_HI  ADD  HEADHTML longtext COMMENT '历史表单设计自定义js，css';
END IF;


-- 2017年9月25日 日历设置表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_calendar_setting');
IF(new_tabs=0)THEN
CREATE TABLE `sys_calendar_setting` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `CALENDARID` bigint(20) DEFAULT NULL COMMENT '日历ID',
  `YEARS` smallint(6) DEFAULT NULL COMMENT '年份',
  `MONTHS` smallint(6) DEFAULT NULL COMMENT '月份',
  `DAYS` smallint(6) DEFAULT NULL COMMENT '天数',
  `TYPE` smallint(6) DEFAULT NULL COMMENT '上班类型\r\n            1,上班\r\n            2,休息',
  `WORKTIMEID` bigint(20) NOT NULL COMMENT '班次ID',
  `CALDAY` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='日历设置';
END IF;
-- 2017年9月25日 加班情况表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_overtime');
IF(new_tabs=0)THEN
CREATE TABLE `sys_overtime` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `SUBJECT` varchar(50) DEFAULT NULL COMMENT '主题',
  `USERID` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `STARTTIME` datetime DEFAULT NULL COMMENT '开始时间',
  `ENDTIME` datetime DEFAULT NULL COMMENT '结束时间',
  `WORKTYPE` smallint(6) DEFAULT NULL COMMENT '类型\r\n            1.加班\r\n            2.请假',
  `MEMO` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='加班情况';

END IF;
-- 2017年9月25日 法定假期设置
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_vacation');
IF(new_tabs=0)THEN
CREATE TABLE `sys_vacation` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '假日名称',
  `YEARS` smallint(6) DEFAULT NULL COMMENT '年份',
  `STATTIME` datetime DEFAULT NULL COMMENT '开始时间',
  `ENDTIME` datetime DEFAULT NULL COMMENT '结束时间',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='法定假期设置';

END IF;

-- 2017年9月25日 班次时间
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_worktime');
IF(new_tabs=0)THEN
CREATE TABLE `sys_worktime` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `SETTINGID` bigint(20) DEFAULT NULL COMMENT '设置ID',
  `STARTTIME` varchar(10) DEFAULT NULL COMMENT '开始时间',
  `ENDTIME` varchar(10) DEFAULT NULL COMMENT '结束时间',
  `MEMO` varchar(200) DEFAULT NULL COMMENT '备注',
  PRIMARY KEY (`ID`),
  KEY `idx_worktime_SETTINGID` (`SETTINGID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='班次时间';

END IF;

-- 2017年9月25日 班次设置表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_worktime_setting');
IF(new_tabs=0)THEN
CREATE TABLE `sys_worktime_setting` (
  `ID` bigint(20) NOT NULL COMMENT '主键',
  `NAME` varchar(50) DEFAULT NULL COMMENT '班次名',
  `MEMO` varchar(200) DEFAULT NULL COMMENT '描述',
  PRIMARY KEY (`ID`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COMMENT='班次设置';
END IF;



COMMIT;
END;
CALL prod_struct_dwj(@spacename);

















