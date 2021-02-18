CREATE OR REPLACE PROCEDURE synstruct_weilei (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN

-----------------------------------------创建电子印章表--2016-08-22----------------------
BEGIN
	select count(*) into countt from tabs where table_name = upper('CWM_SYS_SEAL') AND TABLESPACE_NAME=upper(owneruser);
	IF(countt=0) THEN
		execute immediate 'CREATE TABLE CWM_SYS_SEAL (
			SEALID  NUMBER(20) NOT NULL ,
			SEALNAME  VARCHAR2(128) ,
			SEALPATH  VARCHAR2(128) ,
			BELONGID  NUMBER(20) ,
			BELONGNAME  VARCHAR2(128) ,
			ATTACHMENTID  NUMBER(20) ,
			SHOWIMAGEID  NUMBER(20) 
		)';
		commit;
		execute immediate ' COMMENT ON TABLE  CWM_SYS_SEAL IS ''电子印章'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.SEALID IS ''主键'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.SEALNAME IS ''印章名'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.SEALPATH IS ''印章路径'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.BELONGID IS ''印章持有者ID'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.BELONGNAME IS ''印章持有者姓名'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL.ATTACHMENTID IS ''印章附件'' ';
		commit;
	END IF;
END;

-----------------------------------------创建电子印章授权表--2016-08-22----------------------
BEGIN
	select count(*) into countt from tabs where table_name = upper('CWM_SYS_SEAL_RIGHT') AND TABLESPACE_NAME=upper(owneruser);
	IF(countt=0) THEN
		execute immediate 'CREATE TABLE CWM_SYS_SEAL_RIGHT (
			ID NUMBER(20) NOT NULL ,
			SEALID NUMBER(20),
			RIGHTTYPE VARCHAR2(20),
			RIGHTID NUMBER(20),
			RIGHTNAME VARCHAR2(100),
			CREATEUSER NUMBER(20),
			CREATETIME DATE NOT NULL ,
			CONTROLTYPE NUMBER(6) 
		)';
		commit;
		execute immediate ' COMMENT ON TABLE  CWM_SYS_SEAL_RIGHT IS ''印章授权表'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.SEALID IS ''印章ID'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.RIGHTTYPE IS ''授权类型'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.RIGHTID IS ''被授权者ID'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.RIGHTNAME IS ''被授权者名称'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.CREATEUSER IS ''创建人'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.CREATETIME IS ''创建时间'' ';
		execute immediate ' COMMENT ON COLUMN CWM_SYS_SEAL_RIGHT.CONTROLTYPE IS ''控件类型 印章控件:0 office控件:1'' ';
		commit;
	END IF;
END;

-----------------------------------------修改表单定义表的数据模板ID字段的长度--2016-08-25----------------------
BEGIN
	select count(*) into countt from all_tab_columns where table_name = upper('IBMS_FORM_DEF') AND column_name = 'TEMPLATESID'  AND OWNER=upper(owneruser);
	IF(countt>0) THEN
		execute immediate 'ALTER TABLE IBMS_FORM_DEF MODIFY(TEMPLATESID VARCHAR2(500 BYTE))';
		commit;
	END IF;
END;

-----------------------------------------重命名DBOM静态/动态节点表的数据源字段--2016-08-27----------------------
BEGIN
    select count(*) into countt from all_tab_columns where table_name = upper('CWM_DBOM_NODE') AND column_name = 'DATA_SCOURCE' AND OWNER=upper(owneruser);
 	IF(countt>0) THEN
    	execute immediate 'ALTER TABLE CWM_DBOM_NODE RENAME COLUMN DATA_SCOURCE TO DATA_SOURCE';
   		commit;
 	END IF;
END;

-----------------------------------------业务数据按钮保存配置表添加js后置脚本字段--2016-09-10----------------------
BEGIN
    select count(*) into countt from all_tab_columns where table_name = upper('cwm_sys_bus_event') AND column_name = 'JS_AFTER_SCRIPT' AND OWNER=upper(owneruser);
 	IF(countt=0) THEN
    	execute immediate 'ALTER TABLE CWM_SYS_BUS_EVENT ADD JS_AFTER_SCRIPT VARCHAR2(2000 BYTE)';
    	commit;
    	execute immediate ' COMMENT ON COLUMN cwm_sys_bus_event.JS_AFTER_SCRIPT IS ''js后置脚本''';
   		commit;
 	END IF;
END;

-----------------------------------------用户表中Email更改为可以为空--2016-09-10----------------------
BEGIN
    select count(*) into countt from all_tab_columns where table_name = upper('CWM_SYS_USER') AND column_name = 'EMAIL' AND OWNER=upper(owneruser);
 	IF(countt=0) THEN
    	execute immediate 'ALTER TABLE CWM_SYS_USER MODIFY(EMAIL NULL)';
   		commit;
 	END IF;
END;


-----------------------------------------PageOffice模板管理添加状态、发送时间两个字段--2016-12-20----------------------
BEGIN
    select count(*) into countt from all_tab_columns where table_name = upper('CWM_SYS_OFFICE_TEMPLATE') AND column_name = 'STATUS' AND OWNER=upper(owneruser);
 	IF(countt=0) THEN
    	execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (STATUS  VARCHAR2(20 BYTE))';
   		commit;
   		execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.STATUS IS ''状态''';
   		commit;
 	END IF;
 	
 	select count(*) into countt from all_tab_columns where table_name = upper('CWM_SYS_OFFICE_TEMPLATE') AND column_name = 'PUBLISHED_TIME' AND OWNER=upper(owneruser);
 	IF(countt=0) THEN
    	execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (PUBLISHED_TIME DATE)';
   		commit;
   		execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.PUBLISHED_TIME IS ''发布时间''';
   		commit;
 	END IF;
END;

-----------------------------------------ibms_opinion添加备注字段--2017-2-27----------------------
BEGIN
    select count(*) into countt from all_tab_columns where table_name = upper('ibms_task_opinion') AND column_name = 'MEMO' AND OWNER=upper(owneruser);
 	IF(countt=0) THEN
    	execute immediate 'ALTER TABLE ibms_task_opinion ADD (MEMO  VARCHAR2(255 BYTE))';
   		commit;
   		execute immediate 'COMMENT ON COLUMN ibms_task_opinion.MEMO IS ''状态''';
   		commit;
 	END IF;
END;

commit;
END synstruct_weilei;