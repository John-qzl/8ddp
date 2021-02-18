/**************************************************
1.查询数据库中是否有该表：select count(*) into countt  from tabs where table_name = upper('CWM_LOGIN_LOG');
2.查询数据库中是否有该字段：  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='SIGN_PIC';
3.查询数据库中是否有该序列号：  select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_CWM_LOGIN_LOG'; 
4.加备注：execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_COLUMN IS ''首页布局''';
 ----------------------赋权限-----------------------
--如无建表权限   execute immediate 'grant create table to ibms';
--如无创建存储权限   execute immediate 'grant create procedure to ibms';
--如无创建序列号权限  execute immediate 'grant create sequence to ibms';


**************************************************/
CREATE OR REPLACE PROCEDURE synstruct_dwj (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN

-------------------------------------1.创建表单类别表ibms_rec_type----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_TYPE') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_TYPE(
		TYPEID   NUMBER(20) NOT NULL,
	    TYPENAME VARCHAR2(128 BYTE) NOT NULL  ,
	  	ALIAS 	 VARCHAR2(128 BYTE) DEFAULT NULL,
	  	TYPEDESC VARCHAR2(128 BYTE) DEFAULT NULL
    )';
    
	execute immediate ' COMMENT ON TABLE  IBMS_REC_TYPE IS ''表单类别表'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_TYPE.TYPENAME IS ''表单类别名称'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_TYPE.ALIAS IS ''别名'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_TYPE.TYPEDESC IS ''表单类别描述'' ';
	
    execute immediate 'ALTER TABLE IBMS_REC_TYPE ADD (
      CONSTRAINT IBMS_REC_TYPE_PK
      PRIMARY KEY
     (TYPEID))';
    commit;
END IF;
END;
-------------------------------------2.表单功能点表IBMS_REC_FUN----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_FUN') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_FUN(
  	  TYPEID 		NUMBER(20) NOT NULL,
	  FUNID  		NUMBER(20) NOT NULL,
	  FUNNAME  		VARCHAR2(128 BYTE) NOT NULL,
	  ALIAS    		VARCHAR2(128 BYTE) DEFAULT NULL,
	  FUNDESC  		VARCHAR2(128 BYTE) DEFAULT NULL,
	  SN 	   		NUMBER(20) DEFAULT NULL ,
	  ICON	 		VARCHAR2(128 BYTE) DEFAULT NULL,
	  PARENTID 		NUMBER(20) DEFAULT NULL ,
	  DEFAULTURL 	VARCHAR2(256 BYTE) DEFAULT NULL,
	  ISFOLDER 		NUMBER(6) DEFAULT NULL ,
	  ISDISPLAYINMENU NUMBER(6) DEFAULT NULL ,
	  ISOPEN 		NUMBER(6) DEFAULT NULL ,
	  ISNEWOPEN 	NUMBER(6) DEFAULT NULL ,
	  PATH 			VARCHAR2(500 BYTE) DEFAULT NULL,
	  BUTTONARR 	VARCHAR2 (2000 Byte) DEFAULT NULL
    )'; 
       
	execute immediate ' COMMENT ON TABLE IBMS_REC_FUN IS ''表单功能点表'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.TYPEID IS ''外键（ibms_rec_type）'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.FUNID IS ''主键'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.FUNNAME IS ''表单功能点名称'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.ALIAS IS ''别名'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.FUNDESC IS ''描述'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.SN IS ''同级排序'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.ICON IS ''图标'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.PARENTID IS ''父ID'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.DEFAULTURL IS ''默认地址'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.ISFOLDER IS ''默认打开'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.ISDISPLAYINMENU IS ''显示到菜单'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.ISNEWOPEN IS ''是否打开新窗口：0否,1是'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.PATH IS ''资源路径'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_FUN.BUTTONARR IS ''顶部按钮、管理列按钮信息'' ';
	
    execute immediate 'ALTER TABLE IBMS_REC_FUN ADD (
      CONSTRAINT IBMS_REC_FUN_PK
      PRIMARY KEY
     (FUNID))';
    commit;
END IF;
END;

-------------------------------------3.表单角色表IBMS_REC_ROLE----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLE') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_ROLE(
	  	TYPEID 		NUMBER(20) NOT NULL,
		ROLEID 		NUMBER(18) NOT NULL,
		ROLENAME 	VARCHAR2(128 BYTE) NOT NULL,
		ALIAS 		VARCHAR2(128 BYTE) NOT NULL,
		ROLEDESC 	VARCHAR2(128 BYTE) DEFAULT NULL,
		STATUS 		NUMBER(38) NOT NULL ,
		ALLOWDEL 	NUMBER(18) DEFAULT NULL,
		ALLOWEDIT	NUMBER(1) DEFAULT NULL,
		ALLOWSET 	NUMBER(1)  DEFAULT NULL
    )'; 
    
    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLE IS ''表单角色表'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.TYPEID IS ''外键（ibms_rec_type）'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ROLEID IS ''主键'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ROLENAME IS ''角色名称'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ALIAS IS ''别名'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ROLEDESC IS ''角色描述'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.STATUS IS ''是否启用'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ALLOWDEL IS '' '' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ALLOWEDIT IS '' '' ';
	execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.ALLOWSET IS ''是否允许设置；0：不可以设置，1：可以设置'' ';
	
    execute immediate 'ALTER TABLE IBMS_REC_ROLE ADD (
		CONSTRAINT IBMS_REC_ROLE_PK
		PRIMARY KEY
		(ROLEID))';
    commit;
END IF;
END;

-------------------------------------4.表单角色与表单功能点关联表 IBMS_REC_ROLE_FUN----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLE_FUN') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_ROLE_FUN(
		ROLEID NUMBER(20) NOT NULL ,
		FUNID  NUMBER(20) NOT NULL ,
		ROLEFUNID NUMBER(20) NOT NULL,
		BUTTONS VARCHAR2 (2000 Byte)
    )'; 
    
    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLE_FUN IS ''表单角色与表单功能点关联表'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_FUN.ROLEID IS ''外键（ibms_rec_role） '' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_FUN.FUNID IS '' 外键（ibms_rec_fun）'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_FUN.ROLEFUNID IS '' 主键'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_FUN.BUTTONS IS '' 按钮信息'' ';
    
    execute immediate 'ALTER TABLE IBMS_REC_ROLE_FUN ADD (
       CONSTRAINT IBMS_REC_ROLE_FUN_PK
       PRIMARY KEY
       (ROLEFUNID))';
    commit;
END IF;
END;

---------已废除、使用人员过滤条件
---------------------------------------5.表单角色与系统用户、角色、组织关联表 IBMS_REC_ROLE_META----------------------------
--BEGIN
--	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLE_META');
-- 	IF(countt=0) THEN	  	
--    execute immediate 'CREATE TABLE IBMS_REC_ROLE_META(
--		TYPE  NUMBER(2) NOT NULL,
--		ROLEID NUMBER(20) NOT NULL,
--		USERID  NUMBER(20) ,
--		SYSROLEID  NUMBER(20),
--		SYSORGID  NUMBER(20),
--		ROLEMETAID NUMBER(20) NOT NULL
--    )'; 
--    
--    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLE_META IS ''表单角色与系统用户、角色、组织关联表'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.TYPE IS ''0:user、1:role、2:org '' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.ROLEID IS '' 外键（ibms_rec_role）'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.USERID IS '' 外键（cwm_sys_user）'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.SYSROLEID IS '' 外键（cwm_sys_role）'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.SYSORGID IS '' 外键（cwm_sys_org）'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE_META.ROLEMETAID IS '' 主键'' ';
--    
--    execute immediate 'ALTER TABLE IBMS_REC_ROLE_META ADD (
--       CONSTRAINT IBMS_REC_ROLE_META_PK
--       PRIMARY KEY
--       (ROLEMETAID))';
--    commit;
--END IF;
--END;

-------------------------------------6.业务数据模板-数据模板与表单权限关联字段  IBMS_REC_ROLE_FUN----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_DATA_TEMPLATE' AND COLUMN_NAME ='RECRIGHTFIELD' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_DATA_TEMPLATE   ADD (RECRIGHTFIELD  VARCHAR2 (2000 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_DATA_TEMPLATE.RECRIGHTFIELD IS ''数据模板与表单权限关联字段'' ';
END IF;
END;

-------------------------------------7.业务数据模板-明细多TAb HTML字段  IBMS_REC_ROLE_FUN----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_DATA_TEMPLATE' AND COLUMN_NAME ='MULTITABTEMPHTML' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_DATA_TEMPLATE   ADD (MULTITABTEMPHTML  CLOB)';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_DATA_TEMPLATE.MULTITABTEMPHTML IS '' 明细多TAb HTML字段'' ';
END IF;
END;

-------------------------------------8.记录角色表 IBMS_REC_ROLESON----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLESON') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_ROLESON(
		ROLESONID NUMBER(20) NOT NULL,
		TYPEID  NUMBER(20) NOT NULL,
		TYPENAME  VARCHAR2(128 BYTE) DEFAULT NULL,
		DATATEMPLATEID NUMBER(20) NOT NULL,
		DATAID  NUMBER(20) DEFAULT NULL,
		ROLESONNAME VARCHAR2(128 BYTE) NOT NULL,
		ALIAS VARCHAR2(128 BYTE) NOT NULL,
		ROLESONDESC VARCHAR2(128 BYTE) DEFAULT NULL
    )'; 
    
    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLESON IS ''记录角色表'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.ROLESONID IS ''主键 '' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.TYPEID IS '' 外键（ibms_rec_type）'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.TYPENAME IS '' 类型名称'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.DATATEMPLATEID IS '' 外键（ibms_data_template）'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.DATAID IS ''当前记录的ID'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.ROLESONNAME IS '' 角色名称'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.ALIAS IS '' 别名'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.ROLESONDESC IS '' 角色描述'' ';
    
    execute immediate 'ALTER TABLE IBMS_REC_ROLESON ADD (
       CONSTRAINT IBMS_REC_ROLESON_PK
       PRIMARY KEY
       (ROLESONID))';
    commit;
END IF;
END;

-------------------------------------9.记录角色与表单功能点关联表 IBMS_REC_ROLESON_FUN----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLESON_FUN') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_REC_ROLESON_FUN(
		ROLESONFUNID NUMBER(20) DEFAULT NULL,	
		ROLESONID NUMBER(20) NOT NULL,
		FUNID  NUMBER(20) NOT NULL,
		BUTTONS VARCHAR2 (2000 Byte)
    )'; 
    
    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLESON_FUN IS ''记录角色与表单功能点关联表'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_FUN.ROLESONFUNID IS ''主键 '' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_FUN.ROLESONID IS '' 外键（ibms_rec_roleson）'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_FUN.FUNID IS '' 外键（ibms_rec_fun）'' ';
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_FUN.BUTTONS IS ''按钮权限信息'' ';
    
    execute immediate 'ALTER TABLE IBMS_REC_ROLESON_FUN ADD (
       CONSTRAINT IBMS_REC_ROLESON_FUN_PK
       PRIMARY KEY
       (ROLESONFUNID))';
    commit;
END IF;
END;
---------已废除、使用人员过滤条件
---------------------------------------10.记录角色与系统用户关联表 IBMS_REC_ROLESON_USER----------------------------
--BEGIN
--	select count(*) into countt  from tabs where table_name = upper('IBMS_REC_ROLESON_USER');
-- 	IF(countt=0) THEN	  	
--    execute immediate 'CREATE TABLE IBMS_REC_ROLESON_USER(
--		ROLESONID  NUMBER(20) NOT NULL ,
--		USERID  NUMBER(20) ,
--		ROLESONUSERID NUMBER(20) NOT NULL
--    )'; 
--    
--    execute immediate ' COMMENT ON TABLE IBMS_REC_ROLESON_USER IS ''记录角色与系统用户关联表'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_USER.ROLESONID IS ''外键（ibms_rec_roleson） '' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_USER.USERID IS '' 外键（cwm_sys_user'' ';
--    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON_USER.ROLESONUSERID IS '' 主键'' ';
--    
--    execute immediate 'ALTER TABLE IBMS_REC_ROLESON_USER ADD (
--       CONSTRAINT IBMS_REC_ROLESON_USER_PK
--       PRIMARY KEY
--       (ROLESONUSERID))';
--    commit;
--END IF;
--END;

-------------------------------------11.文件表-维度  CWM_SYS_FILE----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='DIMENSION' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  CWM_SYS_FILE   ADD (DIMENSION  VARCHAR2 (256 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN CWM_SYS_FILE.DIMENSION IS '' 维度'' ';
END IF;
END;

-------------------------------------12.默认角色表	-新增FILTER字段----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLE' AND COLUMN_NAME ='FILTER' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLE   ADD (FILTER  VARCHAR2 (2000 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLE.FILTER IS '' 角色过滤条件'' ';
END IF;
END;

-------------------------------------13.记录角色表 -新增FILTER、USERADD、USERDEL、ROLEID字段----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='FILTER' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (FILTER  VARCHAR2 (2000 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.FILTER IS '' 角色过滤条件'' ';
END IF;
END;
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='USERADD' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (USERADD  VARCHAR2 (2000 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.USERADD IS '' 用户增加的人员'' ';
END IF;
END;
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='USERDEL' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (USERDEL  VARCHAR2 (2000 Byte))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.USERDEL IS '' 用户删除的人员'' ';
END IF;
END;

BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='ROLEID' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (ROLEID  NUMBER(20))';  
    commit;
    execute immediate ' COMMENT ON COLUMN IBMS_REC_ROLESON.ROLEID IS '' 存储项目角色的主键'' ';
END IF;
END;
-------------------------------------14.项目角色表 -新增isHide字段   2017年5月31日----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLE' AND COLUMN_NAME ='ISHIDE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLE   ADD (ISHIDE  NUMBER(1))';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_REC_ROLE.ISHIDE IS '' 对应的记录角色是否隐藏(0,不隐藏,1,隐藏)'' ';
END IF;
END;
-------------------------------------15.项目角色表 -项目记录角色表 -新增allowDel\isHide\def_filter字段   2017年5月31日----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='ALLOWDEL' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (ALLOWDEL  NUMBER(1))';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_REC_ROLESON.ALLOWDEL IS '' 允许删除(0,不允许,1,允许)'' ';
END IF;
END;

BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='ISHIDE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (ISHIDE  NUMBER(1))';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_REC_ROLESON.ISHIDE IS '' 是否隐藏(0,不隐藏,1,隐藏)'' ';
END IF;
END;

BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_REC_ROLESON' AND COLUMN_NAME ='DEF_FILTER' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_REC_ROLESON   ADD (DEF_FILTER  VARCHAR2 (2000 Byte))';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_REC_ROLESON.DEF_FILTER IS '' 用于开发人员添加过滤条件，格式与filter相同'' ';
END IF;
END;

-------------------------------------16.新增用户个性化信息设置表 CWM_SYS_USER_CUST----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('CWM_SYS_USER_CUST') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE CWM_SYS_USER_CUST(
		USERID NUMBER(20) NOT NULL,
		CUSTOMINFO  CLOB
    )';     
    execute immediate ' COMMENT ON TABLE CWM_SYS_USER_CUST IS ''用户个性化信息设置表'' ';
    execute immediate ' COMMENT ON COLUMN CWM_SYS_USER_CUST.USERID IS ''主键 '' ';
    execute immediate ' COMMENT ON COLUMN CWM_SYS_USER_CUST.CUSTOMINFO IS ''用户个性化信息'' ';
    
    execute immediate 'ALTER TABLE CWM_SYS_USER_CUST ADD (
       CONSTRAINT CWM_SYS_USER_CUST_PK
       PRIMARY KEY
       (USERID))';
    commit;
END IF;
END;

-------------------------------------17.新增数据统计工具表   2017年7月4日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_STA_TOOL') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_STA_TOOL(
		TOOLID   NUMBER(20) NOT NULL,
	    NAME 	 VARCHAR2(128 BYTE) DEFAULT NULL,
	  	ALIAS 	 VARCHAR2(128 BYTE) DEFAULT NULL,
	  	TOOLDESC VARCHAR2(1000 BYTE) DEFAULT NULL
    )';
    
	execute immediate ' COMMENT ON TABLE  IBMS_STA_TOOL IS ''数据统计工具表'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_TOOL.NAME IS ''工具名称'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_TOOL.ALIAS IS ''别名'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_TOOL.TOOLDESC IS ''说明'' ';
	
    execute immediate 'ALTER TABLE IBMS_STA_TOOL ADD (
      CONSTRAINT IBMS_STA_TOOL_PK
      PRIMARY KEY
     (TOOLID))';
    commit;
END IF;
END;
-------------------------------------18.新增数据统计访问地址表   2017年7月4日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('IBMS_STA_ADDRESS') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE IBMS_STA_ADDRESS(
		ADDRESSID   NUMBER(20) NOT NULL,
		TOOLID  	NUMBER(20) NOT NULL,
		ALIAS 	 	VARCHAR2(128 BYTE) NOT NULL,
	    URL 	 	VARCHAR2(1000 BYTE) DEFAULT NULL,
	  	VIEWDEF  	CLOB DEFAULT NULL,
	  	ADDRESSDESC 	VARCHAR2(1000 BYTE) DEFAULT NULL
    )';
    
	execute immediate ' COMMENT ON TABLE  IBMS_STA_ADDRESS IS ''数据统计访问地址表'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_ADDRESS.TOOLID IS ''所属工具'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_ADDRESS.ALIAS IS ''别名'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_ADDRESS.URL IS ''访问路径'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_ADDRESS.VIEWDEF IS ''展示视图定制'' ';
	execute immediate ' COMMENT ON COLUMN IBMS_STA_ADDRESS.ADDRESSDESC IS ''访问路径说明'' ';
    execute immediate 'ALTER TABLE IBMS_STA_ADDRESS ADD (
      CONSTRAINT IBMS_STA_ADDRESS_PK
      PRIMARY KEY
      (ADDRESSID))';
    commit;
END IF;
END;

-------------------------------------19.ibms_form_template 新增  HEADHTML字段   (2017年7月12日)----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_TEMPLATE' AND COLUMN_NAME ='HEADHTML' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_FORM_TEMPLATE  ADD (HEADHTML  CLOB)';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_FORM_TEMPLATE.HEADHTML IS '' 业务数据列表、自定义sql列表中自定义js，css'' ';
END IF;
END;
-------------------------------------20.ibms_form_def 新增  HEADHTML字段   (2017年7月12日)----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_DEF' AND COLUMN_NAME ='HEADHTML' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_FORM_DEF  ADD (HEADHTML  CLOB)';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF.HEADHTML IS '' 表单设计中自定义js，css'' ';
END IF;
END;
-------------------------------------21.ibms_form_def_hi 新增  HEADHTML字段   (2017年7月12日)----------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_DEF_HI' AND COLUMN_NAME ='HEADHTML' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
 	 execute immediate 'ALTER TABLE  IBMS_FORM_DEF_HI  ADD (HEADHTML  CLOB)';  
    commit;
     execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.HEADHTML IS '' 历史表单设计中自定义js，css'' ';
END IF;
END;

-------------------------------------22.日历设置2017年9月25日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('SYS_CALENDAR_SETTING') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE SYS_CALENDAR_SETTING  (
	   ID                 NUMBER(18)                         NOT NULL,
	   CALENDARID         NUMBER(18),
	   YEARS              SMALLINT,
	   MONTHS             SMALLINT,
	   DAYS               SMALLINT,
	   TYPE               SMALLINT,
	   WORKTIMEID         NUMBER(18)                         NOT NULL,
	   CALDAY             VARCHAR2(20),
	   PRIMARY KEY (ID)
	)'; 
	execute immediate 'COMMENT ON TABLE SYS_CALENDAR_SETTING IS ''日历设置''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.CALENDARID IS ''日历ID''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.YEARS IS ''年份''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.MONTHS IS ''月份''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.DAYS IS ''天数''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.TYPE IS ''上班类型\r\n            1,上班\r\n            2,休息''';
	execute immediate 'COMMENT ON COLUMN SYS_CALENDAR_SETTING.WORKTIMEID IS ''班次ID''';
    commit;
END IF;
END;


-------------------------------------23.加班情况表2017年9月25日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('SYS_OVERTIME') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE SYS_OVERTIME  (
	   ID                 NUMBER(18)                         NOT NULL,
	   SUBJECT            VARCHAR2(50),
	   USERID             NUMBER(18),
	   STARTTIME          DATE,
	   ENDTIME            DATE,
	   WORKTYPE           SMALLINT,
	   MEMO               VARCHAR2(200),
	   PRIMARY KEY (ID)
	)'; 
	execute immediate 'COMMENT ON TABLE SYS_OVERTIME IS ''加班情况''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.SUBJECT IS ''主题''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.USERID IS ''用户ID''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.STARTTIME IS ''开始时间''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.ENDTIME IS ''结束时间''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.WORKTYPE IS ''类型''';
	execute immediate 'COMMENT ON COLUMN SYS_OVERTIME.MEMO IS ''备注''';
    commit;
END IF;
END;

-------------------------------------24.法定假期设置表2017年9月25日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('SYS_VACATION') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE SYS_VACATION  (
	   ID                 NUMBER(18)                         NOT NULL,
	   NAME               VARCHAR2(50),
	   YEARS              SMALLINT,
	   STATTIME           DATE,
	   ENDTIME            DATE,
	   PRIMARY KEY (ID)
	)'; 
	execute immediate 'COMMENT ON TABLE SYS_VACATION IS ''法定假期设置''';
	execute immediate 'COMMENT ON COLUMN SYS_VACATION.NAME IS ''假日名称''';
	execute immediate 'COMMENT ON COLUMN SYS_VACATION.YEARS IS ''年份''';
	execute immediate 'COMMENT ON COLUMN SYS_VACATION.STATTIME IS ''开始时间''';
	execute immediate 'COMMENT ON COLUMN SYS_VACATION.ENDTIME IS ''结束时间''';
    commit;
END IF;
END;

-------------------------------------25.班次时间表2017年9月25日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('SYS_WORKTIME') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE SYS_WORKTIME  (
	   ID                 NUMBER(18)                         NOT NULL,
	   SETTINGID          NUMBER(18),
	   STARTTIME          VARCHAR2(10),
	   ENDTIME            VARCHAR2(10),
	   MEMO               VARCHAR2(200),
	   PRIMARY KEY (ID)
	)'; 
	execute immediate 'COMMENT ON TABLE SYS_WORKTIME IS ''班次时间''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME.SETTINGID IS ''设置ID''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME.STARTTIME IS ''开始时间''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME.ENDTIME IS ''结束时间''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME.MEMO IS ''备注''';
    commit;
END IF;
END;


-------------------------------------26.班次设置表2017年9月25日----------------------------
BEGIN
	select count(*) into countt  from tabs where table_name = upper('SYS_WORKTIME_SETTING') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt=0) THEN	  	
    execute immediate 'CREATE TABLE SYS_WORKTIME_SETTING  (
	   ID                 NUMBER(18)                         NOT NULL,
	   NAME               VARCHAR2(50),
	   MEMO               VARCHAR2(200),
	   PRIMARY KEY (ID)
	)'; 
	execute immediate 'COMMENT ON TABLE SYS_WORKTIME_SETTING IS ''班次设置''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME_SETTING.NAME IS ''班次名''';
	execute immediate 'COMMENT ON COLUMN SYS_WORKTIME_SETTING.MEMO IS ''描述''';
    commit;
END IF;
END 

COMMIT;
END synstruct_dwj;

















