DROP PROCEDURE IF EXISTS prod_struct_liubo;
CREATE PROCEDURE prod_struct_liubo(spacename varchar(100))
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

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('PROCESSTEMPHTML');
IF(cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  PROCESSTEMPHTML longtext COMMENT'流程监控HTML';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('IBMS_DATA_TEMPLATE' )AND COLUMN_NAME=upper('PROCESSCONDITION');
IF(cols=0)THEN
	ALTER TABLE IBMS_DATA_TEMPLATE  ADD  PROCESSCONDITION longtext COMMENT'流程监控条件字段';
END IF;

-- 新增节点sql表
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_node_sql');
IF(countt=0)THEN
CREATE TABLE `ibms_node_sql` (
`ID` bigint(20) NOT NULL,
`NAME` varchar(100) NULL COMMENT '名称',
`DSALIAS` varchar(100) NULL COMMENT '数据源别名',
`ACTDEFID` varchar(100) NULL COMMENT '流程id',
`NODEID` varchar(50) NULL COMMENT '节点ID',
`ACTION_` varchar(50) NULL COMMENT '触发时机：Submit Agree Opposite Reject delete save end',
`SQL_` varchar(4000) NULL COMMENT 'SQL语句',
`DESC_` varchar(400) NULL COMMENT '描述',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='节点sql表';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('USER_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  USER_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('USER_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  USER_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('USER_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  USER_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('USER_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  USER_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_dic' )AND COLUMN_NAME=upper('DIC_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_dic ADD  DIC_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_dic' )AND COLUMN_NAME=upper('DIC_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_dic ADD  DIC_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_dic' )AND COLUMN_NAME=upper('DIC_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_dic  ADD  DIC_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_dic' )AND COLUMN_NAME=upper('DIC_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_dic  ADD  DIC_DELFLAG INTEGER(1) COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_dic set DIC_DELFLAG=0;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_dic' )AND COLUMN_NAME=upper('DIC_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_dic  ADD  DIC_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_gltype' )AND COLUMN_NAME=upper('GLTYPE_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_gltype ADD  GLTYPE_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_gltype' )AND COLUMN_NAME=upper('GLTYPE_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_gltype ADD  GLTYPE_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_gltype' )AND COLUMN_NAME=upper('GLTYPE_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_gltype  ADD  GLTYPE_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_gltype' )AND COLUMN_NAME=upper('GLTYPE_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_gltype  ADD  GLTYPE_DELFLAG INTEGER(1) COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_gltype set GLTYPE_DELFLAG=0;
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_gltype' )AND COLUMN_NAME=upper('GLTYPE_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_gltype  ADD  GLTYPE_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_typekey' )AND COLUMN_NAME=upper('TYPEKEY_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_typekey ADD  TYPEKEY_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_typekey' )AND COLUMN_NAME=upper('TYPEKEY_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_typekey ADD  TYPEKEY_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_typekey' )AND COLUMN_NAME=upper('TYPEKEY_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_typekey  ADD  TYPEKEY_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_typekey' )AND COLUMN_NAME=upper('TYPEKEY_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_typekey  ADD  TYPEKEY_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_template' )AND COLUMN_NAME=upper('TEMPLATE_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_template  ADD  TEMPLATE_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_template' )AND COLUMN_NAME=upper('TEMPLATE_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_template  ADD  TEMPLATE_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_items' )AND COLUMN_NAME=upper('ITEMS_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_items ADD  ITEMS_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_items' )AND COLUMN_NAME=upper('ITEMS_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_items ADD  ITEMS_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_items' )AND COLUMN_NAME=upper('ITEMS_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_items  ADD  ITEMS_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_office_items' )AND COLUMN_NAME=upper('ITEMS_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_office_items  ADD  ITEMS_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_field' )AND COLUMN_NAME=upper('ISMAINDATA');
IF(cols=0)THEN
	ALTER TABLE ibms_form_field ADD  ISMAINDATA INTEGER(1) NOT NULL DEFAULT '0'  COMMENT'是否为主数据，1代表是，0代表否';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_def' )AND COLUMN_NAME=upper('FORMALIAS');
IF(cols=0)THEN
	ALTER TABLE ibms_form_def ADD  FORMALIAS varchar(100) COMMENT'表单别名';
END IF;

-- 新增账户策略表
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_account_strategy');
IF(countt=0)THEN
CREATE TABLE `ibms_account_strategy` (
`ID` bigint(18) NOT NULL,
`STRATEGY_NAME` varchar(100) NULL COMMENT '策略名称',
`STRATEGY_EXPLAIN` varchar(1000) NULL COMMENT '策略说明',
`IS_ENABLE` varchar(1) NULL COMMENT '启用状态',
`STRATEGY_VALUE` varchar(100) NULL COMMENT '策略值',
`STRATEGY_TYPE` varchar(1) NULL COMMENT '策略类型',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='账户策略表';
END IF;

-- cwm_sys_user表新增最后一次登录失败时间字段lastFailureTime
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('lastFailureTime');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user ADD  lastFailureTime datetime COMMENT'最后一次登录失败时间';
END IF;

-- cwm_sys_user表新增登录失败次数字段loginFailures
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('loginFailures');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user ADD  loginFailures varchar(2) NOT NULL DEFAULT '0' COMMENT'登录失败次数';
END IF;

-- cwm_sys_user表新增锁定状态字段LockState
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('lockState');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user ADD  lockState INTEGER(1) NOT NULL DEFAULT '0'  COMMENT'锁定状态--1表示已锁定，0表示未锁定';
END IF;

-- cwm_sys_user表新增锁定时间字段LockTime
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('lockTime');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user ADD  lockTime datetime COMMENT'锁定时间';
END IF;

-- cwm_sys_user表新增密码更改时间字段passwordSetTime
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('passwordSetTime');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user ADD  passwordSetTime datetime COMMENT'密码更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_pos' )AND COLUMN_NAME=upper('POS_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_pos ADD  POS_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_pos' )AND COLUMN_NAME=upper('POS_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_pos ADD  POS_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_pos' )AND COLUMN_NAME=upper('POS_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_pos  ADD  POS_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_pos' )AND COLUMN_NAME=upper('POS_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_pos  ADD  POS_UPDATETIME datetime COMMENT'更改时间';
END IF;

-- cwm_sys_demension
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_demension' )AND COLUMN_NAME=upper('DEMENSION_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_demension ADD  DEMENSION_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_demension' )AND COLUMN_NAME=upper('DEMENSION_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_demension ADD  DEMENSION_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_demension' )AND COLUMN_NAME=upper('DEMENSION_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_demension  ADD  DEMENSION_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_demension' )AND COLUMN_NAME=upper('DEMENSION_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_demension  ADD  DEMENSION_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_demension' )AND COLUMN_NAME=upper('DEMENSION_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_demension  ADD  DEMENSION_DELFLAG INTEGER(1) DEFAULT 0 COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_demension set DEMENSION_DELFLAG=0;
END IF;

-- cwm_sys_user_position
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_position' )AND COLUMN_NAME=upper('POSITION_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_position ADD  POSITION_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_position' )AND COLUMN_NAME=upper('POSITION_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_position ADD  POSITION_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_position' )AND COLUMN_NAME=upper('POSITION_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_position  ADD  POSITION_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_position' )AND COLUMN_NAME=upper('POSITION_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_position  ADD  POSITION_UPDATETIME datetime COMMENT'更改时间';
END IF;

-- cwm_sys_job
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_job' )AND COLUMN_NAME=upper('JOB_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_job ADD  JOB_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_job' )AND COLUMN_NAME=upper('JOB_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_job ADD  JOB_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_job' )AND COLUMN_NAME=upper('JOB_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_job  ADD  JOB_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_job' )AND COLUMN_NAME=upper('JOB_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_job  ADD  JOB_UPDATETIME datetime COMMENT'更改时间';
END IF;

-- cwm_sys_org_type
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_type' )AND COLUMN_NAME=upper('ORGTYPE_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_type ADD  ORGTYPE_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_type' )AND COLUMN_NAME=upper('ORGTYPE_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_type ADD  ORGTYPE_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_type' )AND COLUMN_NAME=upper('ORGTYPE_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_type  ADD  ORGTYPE_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_type' )AND COLUMN_NAME=upper('ORGTYPE_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_type  ADD  ORGTYPE_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_type' )AND COLUMN_NAME=upper('ORGTYPE_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_type  ADD  ORGTYPE_DELFLAG INTEGER(1) DEFAULT 0 COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_org_type set ORGTYPE_DELFLAG=0;
END IF;

-- cwm_sys_org_param
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_param' )AND COLUMN_NAME=upper('ORGPARAM_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_param ADD  ORGPARAM_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_param' )AND COLUMN_NAME=upper('ORGPARAM_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_param ADD  ORGPARAM_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_param' )AND COLUMN_NAME=upper('ORGPARAM_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_param  ADD  ORGPARAM_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_param' )AND COLUMN_NAME=upper('ORGPARAM_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_param  ADD  ORGPARAM_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org_param' )AND COLUMN_NAME=upper('ORGPARAM_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org_param  ADD  ORGPARAM_DELFLAG INTEGER(1) DEFAULT 0 COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_org_param set ORGPARAM_DELFLAG=0;
END IF;

-- cwm_sys_param
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_param' )AND COLUMN_NAME=upper('SYSPARAM_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_param ADD  SYSPARAM_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_param' )AND COLUMN_NAME=upper('SYSPARAM_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_param ADD  SYSPARAM_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_param' )AND COLUMN_NAME=upper('SYSPARAM_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_param  ADD  SYSPARAM_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_param' )AND COLUMN_NAME=upper('SYSPARAM_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_param  ADD  SYSPARAM_UPDATETIME datetime COMMENT'更改时间';
END IF;

-- cwm_sys_user_param
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_param' )AND COLUMN_NAME=upper('USERPARAM_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_param ADD  USERPARAM_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_param' )AND COLUMN_NAME=upper('USERPARAM_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_param ADD  USERPARAM_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_param' )AND COLUMN_NAME=upper('USERPARAM_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_param  ADD  USERPARAM_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_param' )AND COLUMN_NAME=upper('USERPARAM_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_param  ADD  USERPARAM_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user_param' )AND COLUMN_NAME=upper('USERPARAM_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_user_param  ADD  USERPARAM_DELFLAG INTEGER(1) DEFAULT 0 COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_user_param set USERPARAM_DELFLAG=0;
END IF;

-- cwm_sys_userunder
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_userunder' )AND COLUMN_NAME=upper('USERUNDER_CREATORID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_userunder ADD  USERUNDER_CREATORID bigint(18) COMMENT'创建人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_userunder' )AND COLUMN_NAME=upper('USERUNDER_CREATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_userunder ADD  USERUNDER_CREATETIME datetime COMMENT'创建时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_userunder' )AND COLUMN_NAME=upper('USERUNDER_UPDATEID');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_userunder  ADD  USERUNDER_UPDATEID bigint(18) COMMENT'更改人ID';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_userunder' )AND COLUMN_NAME=upper('USERUNDER_UPDATETIME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_userunder  ADD  USERUNDER_UPDATETIME datetime COMMENT'更改时间';
END IF;

SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_userunder' )AND COLUMN_NAME=upper('USERUNDER_DELFLAG');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_userunder  ADD  USERUNDER_DELFLAG INTEGER(1) DEFAULT 0 COMMENT'是否删除，1代表删除，0代表不删除';
	update cwm_sys_userunder set USERUNDER_DELFLAG=0;
END IF;

-- 新增数据源定义表
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_sys_data_source_def');
IF(countt=0)THEN
CREATE TABLE `ibms_sys_data_source_def` (
`ID` bigint(18) NOT NULL,
`NAME` varchar(100) NULL COMMENT '名称',
`ALIAS` varchar(100) NULL COMMENT '数据源别名',
`DB_TYPE` varchar(100) NULL COMMENT '数据库类型',
`SETTING_JSON` longtext NULL COMMENT '设置信息',
`INIT_CONTAINER` INTEGER(1) NULL COMMENT '是否初始化容器',
`IS_ENABLED` INTEGER(1) NULL COMMENT '是否生效',
`CLASS_PATH` varchar(100) NULL COMMENT '选择模板的路径',
`INIT_METHOD` varchar(100) NULL COMMENT '初始化方法',
`CLOSE_METHOD` varchar(100) NULL COMMENT '关闭方法',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='数据源定义表';
END IF;

-- 新增数据源模板表
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_sys_data_source_template');
IF(countt=0)THEN
CREATE TABLE `ibms_sys_data_source_template` (
`ID` bigint(18) NOT NULL,
`NAME` varchar(100) NULL COMMENT '名称',
`ALIAS` varchar(100) NULL COMMENT '模板别名',
`CLASS_PATH` varchar(100) NULL COMMENT '模板路径',
`SETTING_JSON` longtext NULL COMMENT '设置信息',
`IS_SYSTEM` INTEGER(1) NULL COMMENT '是否系统默认',
`INIT_METHOD` varchar(100) NULL COMMENT '初始化方法',
`CLOSE_METHOD` varchar(100) NULL COMMENT '关闭方法',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='数据源模板表';
END IF;

-- 日历分配表-----by liubo----2017-5-12-----
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_calendar_assign');
IF(countt=0)THEN
CREATE TABLE `sys_calendar_assign` (
`ID` bigint(20) NOT NULL,
`CANLENDARID` bigint(20) NULL COMMENT '日历ID',
`ASSIGNTYPE` INTEGER(1) NULL COMMENT '分配者类型:1,用户 2.组织',
`ASSIGNID` bigint(20) NULL COMMENT '分配者ID',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='日历分配表';
END IF;

-- 系统日历表-----by liubo----2017-5-12-----
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('sys_calendar');
IF(countt=0)THEN
CREATE TABLE `sys_calendar` (
`ID` bigint(20) NOT NULL,
`NAME` varchar(50) NULL COMMENT '日历名称',
`MEMO`  varchar(400) NULL COMMENT '描述',
`ISDEFAULT` bigint(20) NULL COMMENT '1=默认日历 0=非默认',
PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='系统日历表';
END IF;

-- 自定义对话框新增对话框类型字段-----by liubo----2017-6-24-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_dialog' )AND COLUMN_NAME=upper('DIALOGTYPE');
IF(cols=0)THEN
	ALTER TABLE ibms_form_dialog  ADD  DIALOGTYPE INTEGER(1) DEFAULT 0 COMMENT'对话框类型，0：弹出框 ，1：下拉框';
	update ibms_form_dialog set DIALOGTYPE=0;
END IF;

-- 系统文件表新增是否加密存储字段-----by liubo----2017-6-27-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('ISENCRYPT');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_file  ADD  ISENCRYPT INTEGER(1) DEFAULT 0 COMMENT'是否加密存储，1：加密 ，0：不加密';
	update cwm_sys_file set ISENCRYPT=0;
END IF;

-- 系统参数表新增分类属性-----by liubo----2017-8-4-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_parameter' )AND COLUMN_NAME=upper('TYPE');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_parameter  ADD  TYPE varchar(100) COMMENT'系统参数分类类型';
END IF;

-- 系统日志表新增操作结果属性-----by liubo----2017-8-31-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_log' )AND COLUMN_NAME=upper('RESULT');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_log  ADD  RESULT INTEGER(1) COMMENT'操作结果';
END IF;

-- 系统日志表新增执行人姓名属性-----by liubo----2017-8-31-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_log' )AND COLUMN_NAME=upper('EXECUTORNAME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_log  ADD  EXECUTORNAME varchar(100) COMMENT'执行人姓名';
END IF;

-- 系统错误日志表新增执行人姓名属性-----by liubo----2017-8-31-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_errorlog' )AND COLUMN_NAME=upper('NAME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_errorlog  ADD  NAME varchar(100) COMMENT'执行人姓名';
END IF;

-- 系统日志开关表新增日志类型属性-----by liubo----2017-8-31-----
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_log_swith' )AND COLUMN_NAME=upper('EXECTYPES');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_log_swith  ADD  EXECTYPES varchar(2000) COMMENT'日志类型';
END IF;

-- cwm_sys_org表新增组织简称字段orgShortName
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_org' )AND COLUMN_NAME=upper('ORGSHORTNAME');
IF(cols=0)THEN
	ALTER TABLE cwm_sys_org ADD  ORGSHORTNAME varchar(128) COMMENT'组织简称';
END IF;

-- cwm_sys_file  FILENAME字段长度修改为500
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('FILENAME');
IF(cols>0)THEN
	ALTER TABLE cwm_sys_file MODIFY FILENAME varchar(500);
END IF;

-- cwm_sys_file  FILEPATH字段长度修改为200
SELECT COUNT(*) INTO cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('FILEPATH');
IF(cols>0)THEN
	ALTER TABLE cwm_sys_file MODIFY FILEPATH varchar(200);
END IF;

-- --------------do my sql end--------------
COMMIT;
END;
CALL prod_struct_liubo(@spacename);