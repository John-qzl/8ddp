CREATE OR REPLACE PROCEDURE synstruct_wjj (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
isNull VARCHAR2(1 BYTE);
BEGIN
	-- 增加系统布局应用表
	SELECT COUNT(*) INTO countt FROM tabs WHERE TABLE_NAME=upper('cwm_sys_layout') AND TABLESPACE_NAME=upper(owneruser);
	IF(countt=0)THEN
		execute immediate 'CREATE TABLE cwm_sys_layout (
		  ID NUMBER(20) NOT NULL,
		  LAYOUTNAME VARCHAR2(128 BYTE) NOT NULL,
		  APPID NUMBER(20) NOT NULL
		)'; 
		execute immediate ' COMMENT ON TABLE cwm_sys_layout IS ''系统布局应用表'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_layout.LAYOUTNAME IS ''布局名称'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_layout.APPID IS ''布局应用（个人、组织、全局）'' ';
		execute immediate 'ALTER TABLE cwm_sys_layout ADD (
	      CONSTRAINT cwm_sys_layout_PK
	      PRIMARY KEY
	     (ID))';
	    commit;
	END IF;
	
	-- 增加系统皮肤管理表
	SELECT COUNT(*) INTO countt FROM tabs WHERE TABLE_NAME=upper('cwm_sys_skin') AND TABLESPACE_NAME=upper(owneruser);
	IF(countt=0)THEN
		execute immediate 'CREATE TABLE cwm_sys_skin (
		  ID NUMBER(20) NOT NULL,
		  SKINNAME VARCHAR2(128 BYTE) NOT NULL,
		  SKIN_FILEPATH  VARCHAR2(100 BYTE) NOT NULL,
		  DESCRIPTION VARCHAR2(200 BYTE),
		  CREATOR VARCHAR2(100 BYTE) NOT NULL,
		  CREATORID NUMBER(20) NOT NULL,
		  CREATETIME DATE,
		  APPID NUMBER(20) NOT NULL
		)'; 
		execute immediate ' COMMENT ON TABLE cwm_sys_skin IS ''系统皮肤管理表'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.SKINNAME IS ''皮肤名称'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.SKIN_FILEPATH IS ''皮肤文件路径'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.DESCRIPTION IS ''描述'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.CREATOR IS ''创建人'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.CREATORID IS ''创建人ID'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.CREATETIME IS ''创建时间'' ';
		execute immediate ' COMMENT ON COLUMN cwm_sys_skin.APPID IS ''布局应用（个人、组织、全局）'' ';
		execute immediate 'ALTER TABLE cwm_sys_skin ADD (
	      CONSTRAINT cwm_sys_skin_PK
	      PRIMARY KEY
	     (ID))';
	    commit;
	END IF;
	
	-- 系统布局应用表增加字段
	select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_layout') AND COLUMN_NAME ='APPTYPE' AND OWNER=upper(owneruser);
	IF(countt=0) THEN
	 	 execute immediate 'ALTER TABLE  cwm_sys_layout  ADD (APPTYPE  NUMBER(20) NOT NULL)';  
	    commit;
	     execute immediate 'COMMENT ON COLUMN cwm_sys_layout.APPTYPE IS '' 布局应用（个人2、组织1、全局0）'' ';
	END IF;
	
	select NULLABLE into isNull  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_layout') AND COLUMN_NAME ='APPID' AND OWNER=upper(owneruser);
	IF(isNull='N') THEN
	 	 execute immediate 'ALTER TABLE cwm_sys_layout  modify APPID NULL'; 
	END IF;

commit;
END synstruct_wjj;