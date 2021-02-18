CREATE OR REPLACE PROCEDURE synstruct_liubo (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
nullable_ varchar2(10);
BEGIN

 --------------------------IBMS_DATA_TEMPLATE表增加PROCESSTEMPHTML字段----------2017-01-19 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('PROCESSTEMPHTML') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_DATA_TEMPLATE  ADD  PROCESSTEMPHTML CLOB ';
    execute immediate 'COMMENT ON COLUMN IBMS_DATA_TEMPLATE.PROCESSTEMPHTML IS ''流程监控HTML''';
    commit;
  END IF;

END;

 --------------------------IBMS_DATA_TEMPLATE表增加PROCESSCONDITION字段----------2017-01-19 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('PROCESSCONDITION') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_DATA_TEMPLATE  ADD  PROCESSCONDITION CLOB ';
    execute immediate 'COMMENT ON COLUMN IBMS_DATA_TEMPLATE.PROCESSCONDITION IS ''流程监控条件字段''';
    commit;
  END IF;

END;

------------------------创建ibms_node_sql表，节点sql表--2017-2-17-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('ibms_node_sql') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE ibms_node_sql
						(
						  ID             NUMBER(18)                     NOT NULL,
						  NAME         VARCHAR2(100 BYTE),
						  DSALIAS    VARCHAR2(100 BYTE),
						  ACTDEFID        VARCHAR2(100 BYTE),
						  NODEID    VARCHAR2(50 BYTE),	
						  ACTION_    VARCHAR2(50 BYTE),
						  SQL_         VARCHAR2(4000 BYTE),
						  DESC_    VARCHAR2(400 BYTE)
						)';
			
	execute immediate 'COMMENT ON TABLE ibms_node_sql IS ''节点sql''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.NAME IS ''名称''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.DSALIAS IS ''数据源别名''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.ACTDEFID IS ''流程id''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.NODEID IS ''节点ID''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.ACTION_ IS ''触发时机：Submit Agree Opposite Reject delete save end''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.SQL_ IS ''SQL语句''';
	execute immediate 'COMMENT ON COLUMN ibms_node_sql.DESC_ IS ''描述''';
	COMMIT;
  END IF;
END;

 --------------------------cwm_sys_user表增加USER_CREATORID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('USER_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER  ADD  USER_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_USER.USER_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_user表增加USER_CREATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('USER_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER  ADD  USER_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_USER.USER_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_user表增加USER_UPDATEID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('USER_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER  ADD  USER_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_USER.USER_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_user表增加USER_UPDATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('USER_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER  ADD  USER_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_USER.USER_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_gltype表增加GLTYPE_CREATORID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('GLTYPE_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  GLTYPE_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_GLTYPE.GLTYPE_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_gltype表增加GLTYPE_CREATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('GLTYPE_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  GLTYPE_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_GLTYPE.GLTYPE_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_gltype表增加GLTYPE_UPDATEID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('GLTYPE_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE ADD  GLTYPE_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_GLTYPE.GLTYPE_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_gltype表增加GLTYPE_UPDATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('GLTYPE_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  GLTYPE_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_GLTYPE.GLTYPE_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_gltype表增加GLTYPE_DELFLAG字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('GLTYPE_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  GLTYPE_DELFLAG INT ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_GLTYPE.GLTYPE_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update CWM_SYS_GLTYPE set GLTYPE_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_dic表增加DIC_CREATORID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DIC_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  ADD  DIC_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_DIC.DIC_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_dic表增加DIC_CREATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DIC_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  ADD  DIC_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_DIC.DIC_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_dic表增加DIC_UPDATEID字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DIC_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC ADD  DIC_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_DIC.DIC_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_dic表增加DIC_UPDATETIME字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DIC_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC ADD  DIC_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_DIC.DIC_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_dic表增加DIC_DELFLAG字段----------2017-02-20 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DIC_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC ADD  DIC_DELFLAG INT ';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_DIC.DIC_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update CWM_SYS_DIC set DIC_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_typekey表增加TYPEKEY_CREATORID字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_typekey') AND COLUMN_NAME =upper('TYPEKEY_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_typekey  ADD  TYPEKEY_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_typekey.TYPEKEY_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_typekey表增加TYPEKEY_CREATETIME字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_typekey') AND COLUMN_NAME =upper('TYPEKEY_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_typekey  ADD  TYPEKEY_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_typekey.TYPEKEY_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_typekey表增加TYPEKEY_UPDATEID字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_typekey') AND COLUMN_NAME =upper('TYPEKEY_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_typekey ADD  TYPEKEY_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_typekey.TYPEKEY_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_typekey表增加TYPEKEY_UPDATETIME字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_typekey') AND COLUMN_NAME =upper('TYPEKEY_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_typekey ADD  TYPEKEY_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_typekey.TYPEKEY_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_office_template表增加TEMPLATE_UPDATEID字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_template') AND COLUMN_NAME =upper('TEMPLATE_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_template ADD  TEMPLATE_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_template.TEMPLATE_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_office_template表增加TEMPLATE_UPDATETIME字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_template') AND COLUMN_NAME =upper('TEMPLATE_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_template ADD  TEMPLATE_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_template.TEMPLATE_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_office_items表增加ITEMS_CREATORID字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_items') AND COLUMN_NAME =upper('ITEMS_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_items  ADD  ITEMS_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_items.ITEMS_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_office_items表增加ITEMS_CREATETIME字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_items') AND COLUMN_NAME =upper('ITEMS_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_items  ADD  ITEMS_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_items.ITEMS_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_office_items表增加ITEMS_UPDATEID字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_items') AND COLUMN_NAME =upper('ITEMS_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_items ADD  ITEMS_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_items.ITEMS_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 --------------------------cwm_sys_office_items表增加ITEMS_UPDATETIME字段----------2017-03-06 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_office_items') AND COLUMN_NAME =upper('ITEMS_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_office_items ADD  ITEMS_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_office_items.ITEMS_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

 --------------------------ibms_form_field表增加ISMAINDATA字段----------2017-03-15 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('ibms_form_field') AND COLUMN_NAME =upper('ISMAINDATA') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE ibms_form_field ADD ISMAINDATA INT ';
    execute immediate 'COMMENT ON COLUMN ibms_form_field.ISMAINDATA IS ''是否为主数据，1代表是，0代表否''';
    execute immediate 'update ibms_form_field set ISMAINDATA=0';
    commit;
  END IF;

END;

 --------------------------ibms_form_def表增加FORMALIAS字段----------2017-03-15 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('ibms_form_def') AND COLUMN_NAME =upper('FORMALIAS') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE ibms_form_def ADD  FORMALIAS VARCHAR2(100) ';
    execute immediate 'COMMENT ON COLUMN ibms_form_def.FORMALIAS IS ''表单别名''';
    commit;
  END IF;

END;

------------------------新增账户策略表ibms_account_strategy--2017-3-30-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('ibms_account_strategy') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE ibms_account_strategy
						(
						  ID             NUMBER(18)                     NOT NULL,
						  STRATEGY_NAME         VARCHAR2(100 BYTE),
						  STRATEGY_EXPLAIN    VARCHAR2(1000 BYTE),
						  IS_ENABLE        VARCHAR2(1 BYTE),
						  STRATEGY_VALUE    VARCHAR2(100 BYTE),	
						  STRATEGY_TYPE    VARCHAR2(1 BYTE) 
						)';
			
	execute immediate 'COMMENT ON TABLE ibms_account_strategy IS ''账户策略表''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.STRATEGY_NAME IS ''策略名称''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.STRATEGY_EXPLAIN IS ''策略说明''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.IS_ENABLE IS ''启用状态''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.STRATEGY_VALUE IS ''策略值''';
	execute immediate 'COMMENT ON COLUMN ibms_account_strategy.STRATEGY_TYPE IS ''策略类型''';
	COMMIT;
  END IF;
END;

 --------------------------cwm_sys_user表新增最后一次登录失败时间字段lastFailureTime----------2017-03-30 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('lastFailureTime') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user ADD  lastFailureTime DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user.lastFailureTime IS ''最后一次登录失败时间''';
    commit;
  END IF;

END;

 -----------------cwm_sys_user表新增登录失败次数字段loginFailures----------2017-03-30 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('loginFailures') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user ADD  loginFailures VARCHAR2(2) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user.loginFailures IS ''登录失败次数''';
    execute immediate 'update cwm_sys_user set loginFailures=0';
    commit;
  END IF;

END;

 -----------------cwm_sys_user表新增锁定状态字段LockState----------2017-03-30 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('lockState') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user ADD  lockState VARCHAR2(2) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user.lockState IS ''锁定状态--1表示已锁定，0表示未锁定''';
    execute immediate 'update cwm_sys_user set lockState=0';
    commit;
  END IF;

END;

 ----------------cwm_sys_user表新增锁定时间字段LockTime----------2017-03-30 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('lockTime') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user ADD  lockTime DATE';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user.lockTime IS ''锁定时间''';
    commit;
  END IF;

END;

 ----------------cwm_sys_user表新增密码更改时间字段passwordSetTime----------2017-03-30 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('passwordSetTime') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user ADD  passwordSetTime DATE';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user.passwordSetTime IS ''密码更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_pos表增加POS_CREATORID字段----------2017-04-08 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_pos') AND COLUMN_NAME =upper('POS_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_pos  ADD  POS_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_pos.POS_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;

--------------------------cwm_sys_pos表增加POS_CREATETIME字段----------2017-04-08 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_pos') AND COLUMN_NAME =upper('POS_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_pos  ADD  POS_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_pos.POS_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_pos表增加POS_UPDATEID字段----------2017-04-08 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_pos') AND COLUMN_NAME =upper('POS_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_pos ADD  POS_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_pos.POS_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;

 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_pos') AND COLUMN_NAME =upper('POS_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_pos ADD  POS_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_pos.POS_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

  --------------------------cwm_sys_demension----------2017-04-10 by liubo---
END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_demension') AND COLUMN_NAME =upper('DEMENSION_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_demension  ADD  DEMENSION_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_demension.DEMENSION_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_demension') AND COLUMN_NAME =upper('DEMENSION_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_demension  ADD  DEMENSION_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_demension.DEMENSION_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_demension') AND COLUMN_NAME =upper('DEMENSION_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_demension ADD  DEMENSION_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_demension.DEMENSION_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_demension') AND COLUMN_NAME =upper('DEMENSION_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_demension ADD  DEMENSION_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_demension.DEMENSION_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_demension') AND COLUMN_NAME =upper('DEMENSION_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_demension ADD  DEMENSION_DELFLAG INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_demension.DEMENSION_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update cwm_sys_demension set DEMENSION_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_user_position----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_position') AND COLUMN_NAME =upper('POSITION_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_position  ADD  POSITION_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_position.POSITION_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_position') AND COLUMN_NAME =upper('POSITION_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_position  ADD  POSITION_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_position.POSITION_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_position') AND COLUMN_NAME =upper('POSITION_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_position ADD  POSITION_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_position.POSITION_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_position') AND COLUMN_NAME =upper('POSITION_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_position ADD  POSITION_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_position.POSITION_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_job----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_job') AND COLUMN_NAME =upper('JOB_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_job  ADD  JOB_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_job.JOB_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_job') AND COLUMN_NAME =upper('JOB_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_job  ADD  JOB_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_job.JOB_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_job') AND COLUMN_NAME =upper('JOB_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_job ADD  JOB_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_job.JOB_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_job') AND COLUMN_NAME =upper('JOB_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_job ADD  JOB_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_job.JOB_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_org_type----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_type') AND COLUMN_NAME =upper('ORGTYPE_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_type  ADD ORGTYPE_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_type.ORGTYPE_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_type') AND COLUMN_NAME =upper('ORGTYPE_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_type  ADD  ORGTYPE_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_type.ORGTYPE_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_type') AND COLUMN_NAME =upper('ORGTYPE_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_type ADD  ORGTYPE_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_type.ORGTYPE_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_type') AND COLUMN_NAME =upper('ORGTYPE_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_type ADD  ORGTYPE_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_type.ORGTYPE_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_type') AND COLUMN_NAME =upper('ORGTYPE_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_type ADD  ORGTYPE_DELFLAG INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_type.ORGTYPE_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update cwm_sys_org_type set ORGTYPE_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_org_param----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_param') AND COLUMN_NAME =upper('ORGPARAM_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_param  ADD ORGPARAM_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_param.ORGPARAM_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_param') AND COLUMN_NAME =upper('ORGPARAM_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_param  ADD  ORGPARAM_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_param.ORGPARAM_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_param') AND COLUMN_NAME =upper('ORGPARAM_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_param ADD ORGPARAM_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_param.ORGPARAM_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_param') AND COLUMN_NAME =upper('ORGPARAM_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_param ADD  ORGPARAM_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_param.ORGPARAM_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org_param') AND COLUMN_NAME =upper('ORGPARAM_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org_param ADD  ORGPARAM_DELFLAG INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org_param.ORGPARAM_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update cwm_sys_org_param set ORGPARAM_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_param----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_param') AND COLUMN_NAME =upper('SYSPARAM_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_param  ADD SYSPARAM_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_param.SYSPARAM_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_param') AND COLUMN_NAME =upper('SYSPARAM_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_param  ADD  SYSPARAM_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_param.SYSPARAM_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_param') AND COLUMN_NAME =upper('SYSPARAM_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_param ADD SYSPARAM_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_param.SYSPARAM_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_param') AND COLUMN_NAME =upper('SYSPARAM_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_param ADD  SYSPARAM_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_param.SYSPARAM_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;

--------------------------cwm_sys_user_param----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_param') AND COLUMN_NAME =upper('USERPARAM_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_param  ADD USERPARAM_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_param.USERPARAM_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_param') AND COLUMN_NAME =upper('USERPARAM_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_param  ADD  USERPARAM_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_param.USERPARAM_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_param') AND COLUMN_NAME =upper('USERPARAM_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_param ADD USERPARAM_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_param.USERPARAM_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_param') AND COLUMN_NAME =upper('USERPARAM_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_param ADD  USERPARAM_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_param.USERPARAM_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user_param') AND COLUMN_NAME =upper('USERPARAM_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_user_param ADD USERPARAM_DELFLAG INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_user_param.USERPARAM_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update cwm_sys_user_param set USERPARAM_DELFLAG=0';
    commit;
  END IF;

END;

--------------------------cwm_sys_userunder----------2017-04-10 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_userunder') AND COLUMN_NAME =upper('USERUNDER_CREATORID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_userunder  ADD USERUNDER_CREATORID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_userunder.USERUNDER_CREATORID IS ''创建人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_userunder') AND COLUMN_NAME =upper('USERUNDER_CREATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_userunder  ADD  USERUNDER_CREATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_userunder.USERUNDER_CREATETIME IS ''创建时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_userunder') AND COLUMN_NAME =upper('USERUNDER_UPDATEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_userunder ADD USERUNDER_UPDATEID NUMBER(18) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_userunder.USERUNDER_UPDATEID IS ''更改人ID''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_userunder') AND COLUMN_NAME =upper('USERUNDER_UPDATETIME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_userunder ADD  USERUNDER_UPDATETIME DATE ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_userunder.USERUNDER_UPDATETIME IS ''更改时间''';
    commit;
  END IF;

END;
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_userunder') AND COLUMN_NAME =upper('USERUNDER_DELFLAG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_userunder ADD USERUNDER_DELFLAG INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_userunder.USERUNDER_DELFLAG IS ''是否删除，1代表删除，0代表不删除''';
    execute immediate 'update cwm_sys_userunder set USERUNDER_DELFLAG=0';
    commit;
  END IF;

END;

------------------------创建ibms_sys_data_source_def表，数据源定义表--2017-3-24-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('ibms_sys_data_source_def') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE ibms_sys_data_source_def
						(
						  ID             NUMBER(18)                     NOT NULL,
						  NAME         VARCHAR2(100 BYTE),
						  ALIAS    VARCHAR2(100 BYTE),
						  DB_TYPE        VARCHAR2(100 BYTE),
						  SETTING_JSON    CLOB,	
						  INIT_CONTAINER    INT,
						  IS_ENABLED         INT,
						  CLASS_PATH    VARCHAR2(100 BYTE),
						  INIT_METHOD    VARCHAR2(100 BYTE),	
						  CLOSE_METHOD    VARCHAR2(100 BYTE)
						)';
			
	execute immediate 'COMMENT ON TABLE ibms_sys_data_source_def IS ''数据源定义表''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.NAME IS ''名称''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.ALIAS IS ''数据源别名''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.DB_TYPE IS ''数据库类型''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.SETTING_JSON IS ''设置信息''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.INIT_CONTAINER IS ''是否初始化容器''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.IS_ENABLED IS ''是否生效''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.CLASS_PATH IS ''选择模板的路径''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.INIT_METHOD IS ''初始化方法''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_def.CLOSE_METHOD IS ''关闭方法''';
	COMMIT;
  END IF;
END;

------------------------创建ibms_sys_data_source_template表，数据源模板表--2017-3-24-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('ibms_sys_data_source_template') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE ibms_sys_data_source_template
						(
						  ID             NUMBER(18)                     NOT NULL,
						  NAME         VARCHAR2(100 BYTE),
						  ALIAS    VARCHAR2(100 BYTE),
						  CLASS_PATH        VARCHAR2(100 BYTE),
						  SETTING_JSON    CLOB,	
						  IS_SYSTEM    INT,
						  INIT_METHOD    VARCHAR2(100 BYTE),	
						  CLOSE_METHOD    VARCHAR2(100 BYTE)
						)';
			
	execute immediate 'COMMENT ON TABLE ibms_sys_data_source_template IS ''数据源模板表''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.NAME IS ''名称''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.ALIAS IS ''模板别名''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.CLASS_PATH IS ''模板路径''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.SETTING_JSON IS ''设置信息''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.IS_SYSTEM IS ''是否系统默认''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.INIT_METHOD IS ''初始化方法''';
	execute immediate 'COMMENT ON COLUMN ibms_sys_data_source_template.CLOSE_METHOD IS ''关闭方法''';
	COMMIT;
  END IF;
END;

---------------日历分配表-----by liubo----2017-5-12------------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('sys_calendar_assign') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE sys_calendar_assign
						(
						  ID             NUMBER(20)                     NOT NULL,
						  CANLENDARID    NUMBER(20),
						  ASSIGNTYPE    INT,
						  ASSIGNID      NUMBER(20)
						)';
			
	execute immediate 'COMMENT ON TABLE sys_calendar_assign IS ''日历分配表''';
	execute immediate 'COMMENT ON COLUMN sys_calendar_assign.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN sys_calendar_assign.CANLENDARID IS ''日历ID''';
	execute immediate 'COMMENT ON COLUMN sys_calendar_assign.ASSIGNTYPE IS ''分配者类型:1,用户 2.组织''';
	execute immediate 'COMMENT ON COLUMN sys_calendar_assign.ASSIGNID IS ''分配者ID''';
	COMMIT;
  END IF;
END;

---------------系统日历表-----by liubo----2017-5-12------------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('sys_calendar') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE sys_calendar
						(
						  ID             NUMBER(20)                     NOT NULL,
						  NAME    VARCHAR2(50 BYTE),
						  MEMO    VARCHAR2(400 BYTE),
						  ISDEFAULT      NUMBER(20)
						)';
			
	execute immediate 'COMMENT ON TABLE sys_calendar IS ''系统日历表''';
	execute immediate 'COMMENT ON COLUMN sys_calendar.ID IS ''id''';
	execute immediate 'COMMENT ON COLUMN sys_calendar.NAME IS ''日历名称''';
	execute immediate 'COMMENT ON COLUMN sys_calendar.MEMO IS ''描述''';
	execute immediate 'COMMENT ON COLUMN sys_calendar.ISDEFAULT IS ''1=默认日历 0=非默认''';
	COMMIT;
  END IF;
END;

------------系统文件表新增是否加密存储字段-----by liubo----2017-6-27-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('ISENCRYPT') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_file ADD  ISENCRYPT INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_file.ISENCRYPT IS ''是否加密存储，1：加密 ，0：不加密''';
    execute immediate 'update cwm_sys_file set ISENCRYPT=0';
    commit;
  END IF;

END;

------------自定义对话框新增对话框类型字段-----by liubo----2017-7-13-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('ibms_form_dialog') AND COLUMN_NAME =upper('DIALOGTYPE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE ibms_form_dialog ADD  DIALOGTYPE INT DEFAULT 0 ';
    execute immediate 'COMMENT ON COLUMN ibms_form_dialog.DIALOGTYPE IS ''对话框类型，0：弹出框 ，1：下拉框''';
    execute immediate 'update ibms_form_dialog set DIALOGTYPE=0';
    commit;
  END IF;

END;

----------系统参数表新增分类属性-----by liubo----2017-8-4-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_parameter') AND COLUMN_NAME =upper('TYPE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_parameter ADD  TYPE VARCHAR2(100 BYTE) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_parameter.TYPE IS ''系统参数分类类型''';
    commit;
  END IF;

END;

----------系统日志表新增操作结果属性-----by liubo----2017-8-31------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_log') AND COLUMN_NAME =upper('RESULT') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_log ADD  RESULT INT ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_log.RESULT IS ''操作结果''';
    commit;
  END IF;

END;

----------系统日志表新增执行人姓名属性-----by liubo----2017-8-31-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_log') AND COLUMN_NAME =upper('EXECUTORNAME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_log ADD  EXECUTORNAME VARCHAR2(100 BYTE) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_log.EXECUTORNAME IS ''执行人姓名''';
    commit;
  END IF;

END;

----------系统错误日志表新增执行人姓名属性----by liubo----2017-8-31-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_errorlog') AND COLUMN_NAME =upper('NAME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_errorlog ADD  NAME VARCHAR2(100 BYTE) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_errorlog.NAME IS ''执行人姓名''';
    commit;
  END IF;

END;

----------系统日志开关表新增日志类型属性----by liubo----2017-8-31-----
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_log_swith') AND COLUMN_NAME =upper('EXECTYPES') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_log_swith ADD  EXECTYPES VARCHAR2(2000 BYTE) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_log_swith.EXECTYPES IS ''日志类型''';
    commit;
  END IF;

END;

 -------------------------cwm_sys_org表新增组织简称字段orgShortName----------2017-09-21 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_org') AND COLUMN_NAME =upper('ORGSHORTNAME') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE cwm_sys_org  ADD  ORGSHORTNAME VARCHAR2(128 BYTE) ';
    execute immediate 'COMMENT ON COLUMN cwm_sys_org.ORGSHORTNAME IS ''组织简称''';
    commit;
  END IF;

END;

-----------cwm_sys_file  FILENAME字段长度修改为500----------2017-9-22 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('FILENAME') AND OWNER=upper(owneruser);
  IF(countt>0) THEN
    execute immediate 'ALTER TABLE cwm_sys_file  MODIFY(FILENAME VARCHAR2(500 BYTE)) ';
    commit;
  END IF;

END;

-----------cwm_sys_file  FILEPATH字段长度修改为200----------2017-9-22 by liubo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('FILEPATH') AND OWNER=upper(owneruser);
  IF(countt>0) THEN
    execute immediate 'ALTER TABLE cwm_sys_file  MODIFY(FILEPATH VARCHAR2(200 BYTE)) ';
    commit;
  END IF;

END;

commit;	
END synstruct_liubo;