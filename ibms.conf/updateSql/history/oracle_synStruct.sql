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

CREATE OR REPLACE PROCEDURE synstruct (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
countt1 number;
sqlll varchar2(1000);
BEGIN
----------------------检查数据库默认表空间名是否与用户一致，不一致修改--------------------
BEGIN
select count(*) into countt from USER_USERS where DEFAULT_TABLESPACE=upper(owneruser);
IF(countt=0) THEN
    sqlll:='alter user '||upper(owneruser)||' default tablespace '||upper(owneruser);
     execute immediate sqlll;
    commit;
END IF;
END;
------------------------创建CWM_LOGIN_LOG表用来记录登录的状况--------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_LOGIN_LOG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_LOGIN_LOG
    (
      ID          NUMBER(18)       NOT NULL,
      ACCOUNT      VARCHAR2(256 BYTE),
      LOGINTIME      DATE,
      IP  VARCHAR2(256 BYTE),
      STATUS   NUMBER(18),
      DESC_    VARCHAR2(64 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_LOGIN_LOG ADD (
      CONSTRAINT CWM_LOGIN_LOG_PK
     PRIMARY KEY
     (ID))';
    commit;
END IF;
END;
----------------------------SEQUENCE_NAME-----SEQ_CWM_LOGIN_LOG----------------------------------------------
BEGIN
 select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_CWM_LOGIN_LOG';
 IF(countt=0) THEN
    execute immediate 'CREATE SEQUENCE SEQ_CWM_LOGIN_LOG
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 999999999999999999999999999
    CACHE 10
    NOCYCLE
    NOORDER';
  commit;
 END IF;
END;
BEGIN
    execute immediate 'COMMENT ON TABLE CWM_LOGIN_LOG IS ''登陆日志''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.ID IS ''主键''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.ACCOUNT IS ''登陆用户''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.LOGINTIME IS ''登录时间''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.IP IS ''登陆IP''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.STATUS IS ''登陆状态''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.DESC_  IS ''登陆说明''';
END;
------------------CWM_SYS_USER表中添加电子签名图片存储字段------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='SIGN_PIC' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
  execute immediate 'ALTER TABLE  CWM_SYS_USER   ADD (SIGN_PIC  VARCHAR2(128 BYTE))';
    commit;
END IF;
END;
------------------CWM_SYS_USER表中添加电子签名图片存储字段------------------
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='ORIGINALSIGN_PIC' AND OWNER=upper(owneruser);
 IF(countt=0) THEN
  execute immediate 'ALTER TABLE  CWM_SYS_USER  ADD (ORIGINALSIGN_PIC  VARCHAR2(128 BYTE))';
  commit;
 END IF;
END;
------------------报表设计模板管理------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('IBMS_FORM_REPORTS') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
  execute immediate 'CREATE TABLE  IBMS_FORM_REPORTS
  (
    ID            VARCHAR2(38)                    NOT NULL,
    FORMKEY       VARCHAR2(200),
    CONTENT       CLOB,
    CONTENT_SPYJ  CLOB,
    FILEPATH      VARCHAR2(200),
    CREATE_USER   VARCHAR2(200),
    CREATE_TIME   DATE,
    NAME          VARCHAR2(200)
  )';

  execute immediate 'ALTER TABLE IBMS_FORM_REPORTS ADD (
    CONSTRAINT IBMS_FORM_REPORTS_PK
   PRIMARY KEY
   (ID))';

 commit;
 END IF;
END;
--------------------SEQUENCE_NAME--SEQ_IBMS_FORM_REPORTS---------------------
BEGIN
 select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_IBMS_FORM_REPORTS';
 IF(countt=0) THEN
  execute immediate 'CREATE SEQUENCE SEQ_IBMS_FORM_REPORTS
  START WITH 1
  INCREMENT BY 1
  MINVALUE 1
  MAXVALUE 999999999999999999999999999
  CACHE 10
  NOCYCLE
  NOORDER';
  commit;
 END IF;
END;
--------------------添加报表类型-----------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_REPORTS' AND COLUMN_NAME ='TYPE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE IBMS_FORM_REPORTS ADD (TYPE  VARCHAR2(200))';
    commit;
 END IF;
END;

 --------------------流程表单设计：增加初始化脚本字段------------------
BEGIN
select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_NODE_SET' AND COLUMN_NAME ='INITSCRIPTHANDLER' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'alter table IBMS_NODE_SET add INITSCRIPTHANDLER VARCHAR2 (1000 Byte)';
    commit;
 END IF;
END;
---------------------------系统参数表------------------------------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_PARAMETER') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
execute immediate 'CREATE TABLE CWM_SYS_PARAMETER

(
  ID           NUMBER(18)                        NOT NULL,
  NAME         VARCHAR2(50 BYTE),
  DATATYPE     VARCHAR2(50 BYTE),
  VALUE        VARCHAR2(1000 BYTE),
  DESCRIPTION  VARCHAR2(150 BYTE)
)';

execute immediate 'ALTER TABLE CWM_SYS_PARAMETER ADD (
  CONSTRAINT SYS_C0011242
 PRIMARY KEY
 (ID)
)';

commit;
END IF;
END;
--------------------SEQUENCE_NAME--SEQ_CWM_SYS_PARAMETER---------------------
BEGIN
 select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_CWM_SYS_PARAMETER';
 IF(countt=0) THEN
 execute immediate 'CREATE SEQUENCE SEQ_CWM_SYS_PARAMETER
  START WITH 1
  MAXVALUE 999999999999999999999999999
  MINVALUE 1
  NOCYCLE
  CACHE 20
  NOORDER';
  commit;
 END IF;
IF(countt=1) THEN
execute immediate 'comment on column  CWM_SYS_PARAMETER.ID                         is  ''主键''';
execute immediate 'comment on column  CWM_SYS_PARAMETER.NAME                       is  ''参数名称''';
execute immediate 'comment on column  CWM_SYS_PARAMETER.DATATYPE                   is  ''数据类型''';
execute immediate 'comment on column  CWM_SYS_PARAMETER.VALUE                      is  ''参数值''';
execute immediate 'comment on column  CWM_SYS_PARAMETER.DESCRIPTION                is  ''描述''';
commit;
END IF;
END;
---------------------------备份还原表---------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_BackUpRestore') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_BackUpRestore
    (
      ID        NUMBER(18)      NOT NULL,
      NAME      VARCHAR2(50 BYTE),
      DATETIME  DATE,
      USERNAME  VARCHAR2(50 BYTE),
      COMMENTS  VARCHAR2(200 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_BackUpRestore ADD (
      CONSTRAINT CWM_SYS_C0011241
     PRIMARY KEY
     (ID))';

    commit;
END IF;
END;
--------------------SEQUENCE_NAME--SEQ_CWM_SYS_BackUpRestore---------------------
BEGIN
 select count(*) into countt from user_sequences where SEQUENCE_NAME = upper('SEQ_CWM_SYS_BackUpRestore');
 IF(countt=0) THEN
    execute immediate 'CREATE SEQUENCE SEQ_CWM_SYS_BackUpRestore
      START WITH 1
      MAXVALUE 999999999999999999999999999
      MINVALUE 1
      NOCYCLE
      CACHE 20
      NOORDER';
  commit;
 END IF;
END;
 ------------------------delete no used tables -------------------------------
BEGIN
 select count(*)  into countt  from tabs where table_name = upper('CWM_TABLES') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=1) THEN
    execute immediate 'DROP TABLE CWM_TABLES';
    execute immediate 'DROP TABLE CWM_TABLE_ENUM';
    execute immediate 'DROP TABLE CWM_TABLE_COLUMN';
    execute immediate 'DROP TABLE CWM_RELATION_TABLE_ENUM';
    execute immediate 'DROP TABLE CWM_SCHEMA';
    execute immediate 'DROP TABLE CWM_ARITH_ATTRIBUTE';
    execute immediate 'DROP TABLE CWM_ARITH_VIEW_CANSHUXIANG';
    execute immediate 'DROP TABLE CWM_CONS_EXPRESSION';
    execute immediate 'DROP TABLE CWM_ENUM';
    execute immediate 'DROP TABLE CWM_RELATION_COLUMNS';
    execute immediate 'DROP TABLE CWM_RELATION_DATA';
    execute immediate 'DROP TABLE CWM_RESTRICTION';
    execute immediate 'DROP TABLE CWM_SEQGENERATOR';
    execute immediate 'DROP TABLE CWM_TAB_COLUMNS';
    execute immediate 'DROP TABLE CWM_VIEW_PAIXU_COLUMN';
    execute immediate 'DROP TABLE CWM_VIEW_RETURN_COLUMN';
    execute immediate 'DROP TABLE T_HT_1';
    commit;
 END IF;
END;

 ------------------------添加唯一约束列ISUNIQUE ------------------------
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='ISUNIQUE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE IBMS_FORM_FIELD add ISUNIQUE NUMBER(38)';
    commit;
 END IF;
 execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.ISUNIQUE IS ''唯一约束''';
END;
---------------------------添加表 CWM_SYS_QUERY_SQL------------------------------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_QUERY_SQL') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
execute immediate 'create table CWM_SYS_QUERY_SQL
(
  id         NUMBER(18) not null,
  sql_       VARCHAR2(2000),
  name       VARCHAR2(100),
  url_params VARCHAR2(2000),
  categoryid NUMBER(18),
  alias      VARCHAR2(50)
)';
execute immediate 'alter table CWM_SYS_QUERY_SQL add primary key (ID)';
commit;
END IF;
execute immediate 'comment on table CWM_SYS_QUERY_SQL  is ''自定义列表''';
execute immediate 'comment on column CWM_SYS_QUERY_SQL.alias is ''别名''';
END;
------------------------CWM_SYS_QUERY_SQL表加列------------------------

BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_QUERY_SQL' AND COLUMN_NAME ='DSNAME' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_QUERY_SQL add dsName VARCHAR(50)';
    commit;
 END IF;
 execute immediate 'COMMENT ON COLUMN   CWM_SYS_QUERY_SQL.dsName IS ''数据源''';
END;
 ------------------------ 别名------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_QUERY_SQL' AND COLUMN_NAME ='ALIAS' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_QUERY_SQL add alias VARCHAR(50)';
    commit;
 END IF;
  execute immediate 'COMMENT ON COLUMN   CWM_SYS_QUERY_SQL.alias IS ''别名''';
END;
---2016-05-06 by weilei--


-------------------------用户表：添加密级字段---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='SECURITY' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER ADD (SECURITY  VARCHAR2(8 BYTE))';
    commit;
 END IF;
END;
-------------------------业务表：更改OPTIONS字段长度为2000---
BEGIN
    execute immediate 'ALTER TABLE IBMS_FORM_FIELD MODIFY(OPTIONS VARCHAR2(2000 BYTE))';
END;
---2016-06-14 by weilei--

-------------------------附件表：添加密级、描述字段---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='SECURITY' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_FILE ADD (SECURITY  VARCHAR2(50 BYTE))';
    commit;
 END IF;
END;
----------------------------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='DESCRIBE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_FILE ADD (DESCRIBE  VARCHAR2(500 BYTE))';
    commit;
 END IF;
END;
COMMIT;

------------------------2016-06-13 by liqing  start 增加关联关系类型、关联关系表ID、是否主键显示值
--------------------------关系表类型:0-一对一、2-多对一-----------------------
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='RELTABLETYPE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (RELTABLETYPE  NUMBER(38))';
    commit;
 END IF;
   execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.RELTABLETYPE IS ''关联关系表类型0一对一2多对一''';
END;
--------------------------关系表ID-----------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='RELTABLEID' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (RELTABLEID  NUMBER(18))';
    commit;
 END IF;
  execute immediate ' COMMENT ON COLUMN IBMS_FORM_FIELD.RELTABLEID IS ''关联关系表ID''';
END;
--------------------------是否主键显示值-----------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='ISPKSHOW' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE IBMS_FORM_FIELD  ADD (ISPKSHOW  NUMBER(38))';
    commit;
 END IF;
   execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.ISPKSHOW IS ''是否主键显示值0-否1-是''';
 COMMIT;
END;
---2016-06-13 by liqing  end

-------------------------2016-06-21 lq end 参数表中增加Rtx消息同步-----------------------
--------------------------2016-07-01 by honghuajun   -----------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='RELDELTYPE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (RELDELTYPE  NUMBER(38))';
    commit;
 END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.RELDELTYPE IS ''rel关联表记录删除类型，1-直接删除，0-取消关联''';
END;
--------------------------2016-07-01 by honghuajun   -----------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_FORM_FIELD' AND COLUMN_NAME ='RELDELLMTYPE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (RELDELLMTYPE  NUMBER(38))';
    commit;
 END IF;

  execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.RELDELLMTYPE IS ''主表记录删除，rel关联表记录删除类型，1-直接删除，0-取消关联''';
 COMMIT;
END;


---2016-07-01 by yangbo--
----------------------------Definition表加上"节点跳过设定"字段 --------------------------
BEGIN
   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_DEFINITION' AND COLUMN_NAME ='SKIPSETTING' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE IBMS_DEFINITION add SKIPSETTING VARCHAR(50)';
    commit;
  END IF;
     execute immediate 'COMMENT ON COLUMN   IBMS_DEFINITION.SKIPSETTING IS ''节点跳过设定''';
END;
---2016-06-30 by yangbo--
-----------------------------IBMS_NODE_SET表加上"opinionField"字段 ---------------------------
BEGIN
   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='IBMS_NODE_SET' AND COLUMN_NAME =upper('opinionField') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE IBMS_NODE_SET add opinionField VARCHAR(50)';
    commit;
  END IF;
    execute immediate 'COMMENT ON COLUMN   IBMS_NODE_SET.opinionField IS ''意见区域''';
END;
-----------------------------add by hhj 2016-07-11---------------------------
BEGIN
    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_pro_run') AND COLUMN_NAME =upper('RELRUNID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE  IBMS_pro_run ADD (RELRUNID  NUMBER(38))';
    commit;
  END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_pro_run.RELRUNID IS ''关联实例ID''';
END;
 -----------------------------add by hhj 2016-07-11---------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_pro_run') AND COLUMN_NAME =upper('GLOBALFLOWNO') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE  IBMS_pro_run ADD (GLOBALFLOWNO  VARCHAR2(128 BYTE))';
    commit;
  END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_pro_run.GLOBALFLOWNO IS ''全局流程变量''';
END;
 -----------------------------add by hhj 2016-07-11---------------------------
BEGIN
    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_PRO_RUN_HIS') AND COLUMN_NAME =upper('RELRUNID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE  IBMS_PRO_RUN_HIS ADD (RELRUNID  NUMBER(38))';
    commit;
  END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_pro_run.RELRUNID IS ''关联实例ID''';
END;
 -----------------------------add by hhj 2016-07-11---------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_PRO_RUN_HIS') AND COLUMN_NAME =upper('GLOBALFLOWNO') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE  IBMS_PRO_RUN_HIS ADD (GLOBALFLOWNO  VARCHAR2(128 BYTE))';
    commit;
  END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_pro_run.GLOBALFLOWNO IS ''全局流程变量''';
 commit;
END;
 -------------------------------add by hhj 2016-07-11---------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_FORM_FIELD') AND COLUMN_NAME =upper('RELFORMDIALOG') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_FORM_FIELD add (RELFORMDIALOG  VARCHAR2(2000 BYTE))';
    commit;
  END IF;
  execute immediate 'COMMENT ON COLUMN   IBMS_FORM_FIELD.RELFORMDIALOG IS ''外键列默认关联的自定义对话框信息''';
 commit;
END;

------------------------创建CWM_LOGIN_LOG表用来记录登录的状况--------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_LOGIN_LOG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_LOGIN_LOG
    (
      ID          NUMBER(18)       NOT NULL,
      ACCOUNT      VARCHAR2(256 BYTE),
      LOGINTIME      DATE,
      IP  VARCHAR2(256 BYTE),
      STATUS   NUMBER(18),
      DESC_    VARCHAR2(64 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_LOGIN_LOG ADD (
      CONSTRAINT CWM_LOGIN_LOG_PK
     PRIMARY KEY
     (ID))';

    execute immediate 'COMMENT ON TABLE CWM_LOGIN_LOG IS ''登陆日志''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.ID IS ''主键''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.ACCOUNT IS ''登陆用户''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.LOGINTIME IS ''登录时间''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.IP IS ''登陆IP''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.STATUS IS ''登陆状态''';
    execute immediate 'COMMENT ON COLUMN   CWM_LOGIN_LOG.DESC_  IS ''登陆说明''';
    commit;
END IF;
END;
--------------------SEQUENCE_NAME--SEQ_CWM_LOGIN_LOG---------------------
BEGIN
 select count(*) into countt from user_sequences where SEQUENCE_NAME = upper('SEQ_CWM_LOGIN_LOG');
 IF(countt=0) THEN
    execute immediate 'CREATE SEQUENCE SEQ_CWM_LOGIN_LOG
    START WITH 1
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 999999999999999999999999999
    CACHE 10
    NOCYCLE
    NOORDER';
  commit;
 END IF;
END;
 ---2016-07-19 by yangbo---
-------------------创建CWM_SYS_PAUR表用来记录用户绑定皮肤主题等信息---------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_PAUR') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_PAUR

    (
       PAURID      NUMBER (18) NOT NULL,
       PAURNAME    VARCHAR2 (256 BYTE),
       ALIASNAME   VARCHAR2 (256 BYTE),
       PAURVALUE   VARCHAR2 (256 BYTE),
       USERID      NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_PAUR ADD (
      CONSTRAINT CWM_SYS_PAUR_PK
     PRIMARY KEY
     (PAURID))';

    execute immediate 'COMMENT ON COLUMN   CWM_SYS_PAUR.PAURID IS ''主键''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_PAUR.PAURNAME IS ''名称''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_PAUR.ALIASNAME IS ''别名''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_PAUR.PAURVALUE IS ''值''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_PAUR.USERID IS ''所属用户''';
    commit;
END IF;
END;
---2016-07-20 by yangbo--
---------------------创建 CWM_SYS_MY_LAYOUT "主页用户个性化布局"--------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_MY_LAYOUT') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_MY_LAYOUT
    (
       ID      NUMBER (18) NOT NULL,
       USER_ID    NUMBER (18),
       TEMPLATE_HTML   CLOB,
       DESIGN_HTML   CLOB,
       LAYOUT_ID      NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_MY_LAYOUT ADD (
      CONSTRAINT CWM_SYS_MY_LAYOUT_PK
     PRIMARY KEY
     (ID))';
    execute immediate 'COMMENT ON TABLE CWM_SYS_MY_LAYOUT IS ''我的布局''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_MY_LAYOUT.ID IS ''主键''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_MY_LAYOUT.USER_ID IS ''用户ID''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_MY_LAYOUT.TEMPLATE_HTML IS ''模版内容''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_MY_LAYOUT.DESIGN_HTML IS ''设计模版''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_MY_LAYOUT.LAYOUT_ID IS ''布局管理ID''';
    commit;
END IF;
END;
--2016-7-21 by yangbo--
----------------------创建首页布局表CWM_SYS_INDEX_COLUMN，用于给首页添加栏目-------------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_INDEX_COLUMN') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_INDEX_COLUMN
    (
       ID      NUMBER (18) NOT NULL,
       NAME    VARCHAR2 (256 BYTE),
       ALIAS   VARCHAR2 (256 BYTE),
       CATALOG    NUMBER (18),
       COL_TYPE      NUMBER (18),
       DATA_MODE NUMBER (18) ,
       DATA_FROM VARCHAR2 (256 BYTE),
       DS_ALIAS VARCHAR2 (256 BYTE),
       DS_NAME  VARCHAR2 (256 BYTE),
       COL_HEIGHT NUMBER (18),
       COL_URL VARCHAR2 (256 BYTE),
       TEMPLATE_HTML CLOB ,
       IS_PUBLIC  NUMBER (18),
       ORG_ID NUMBER (18),
       SUPPORT_REFESH NUMBER (18),
       REFESH_TIME NUMBER (18),
       SHOW_EFFECT NUMBER (18),
       MEMO VARCHAR2 (256 BYTE),
       DATA_PARAM VARCHAR2 (256 BYTE),
       NEEDPAGE NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_INDEX_COLUMN ADD (
      CONSTRAINT CWM_SYS_INDEX_COLUMN_PK
     PRIMARY KEY
     (ID))';
    execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_COLUMN IS ''首页布局''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.ID IS ''主键''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.NAME IS ''栏目名称''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.ALIAS IS ''栏目别名''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.CATALOG IS ''栏目分类''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.COL_TYPE IS ''栏目类型0般栏目1图表栏目2滚动栏目''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.DATA_MODE IS ''数据加载方式0服务方法1自定义查询''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.DATA_FROM IS ''数据来源''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.DS_ALIAS IS ''数据别名''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.DS_NAME IS ''数据源名称''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.COL_HEIGHT IS ''栏目高度''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.COL_URL IS ''栏目URL''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.TEMPLATE_HTML IS ''栏目模版''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.IS_PUBLIC IS ''是否公共栏目''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.ORG_ID IS ''所属组织ID''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.SUPPORT_REFESH IS ''是否支持刷新''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.REFESH_TIME IS ''刷新时间''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.SHOW_EFFECT IS ''展示效果''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.MEMO IS ''描述''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.DATA_PARAM IS ''数据参数''';
    execute immediate 'COMMENT ON COLUMN   CWM_SYS_INDEX_COLUMN.NEEDPAGE IS ''首页分页''';
    commit;
END IF;
END;
------------------------创建布局管理表CWM_SYS_INDEX_MANAGE，首页布局管理---------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_INDEX_MANAGE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_INDEX_MANAGE(
      ID NUMBER (18) NOT NULL ,
      NAME VARCHAR2 (256 BYTE) ,
      MEMO VARCHAR2 (256 BYTE) ,
      ORG_ID NUMBER (18) ,
      TEMPLATE_HTML CLOB,
      DESIGN_HTML CLOB ,
      IS_DEF NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_INDEX_MANAGE ADD (
      CONSTRAINT CWM_SYS_INDEX_MANAGE_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_MANAGE IS ''布局管理''';

     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.NAME IS ''布局名称''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.MEMO IS ''布局描述''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.ORG_ID IS ''组织ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.TEMPLATE_HTML IS ''模版内容''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.DESIGN_HTML IS ''设计模版''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_MANAGE.IS_DEF IS ''是否是默认''';
     commit;
END IF;
END;
------------------------------创建CWM_SYS_OBJRIGHTS权限对象表--------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_OBJRIGHTS') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_OBJRIGHTS(
      ID NUMBER (18) NOT NULL ,
      OBJ_TYPE VARCHAR2 (256 BYTE) ,
      OBJECT_ID NUMBER (18) ,
      OWNER_ID NUMBER (18) ,
      OWNER VARCHAR2 (256 BYTE),
      RIGHT_TYPE VARCHAR2 (256 BYTE)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_OBJRIGHTS ADD (
      CONSTRAINT CWM_SYS_OBJRIGHTS_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE CWM_SYS_OBJRIGHTS IS ''权限对象表''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.OBJ_TYPE IS ''对象类型''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.OBJECT_ID IS ''权限对象ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.OWNER_ID IS ''授权人ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.OWNER IS ''授权人''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_OBJRIGHTS.RIGHT_TYPE IS ''权限类型''';
      commit;
END IF;
END;
 -------------------------------创建CWM_SYS_INDEX_LAYOUT首页布局表-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_INDEX_LAYOUT') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_INDEX_LAYOUT(
      ID NUMBER (18) NOT NULL ,
      NAME VARCHAR2 (256 BYTE) ,
      MEMO VARCHAR2 (256 BYTE) ,
      TEMPLATE_HTML CLOB ,
      SN NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_INDEX_LAYOUT ADD (
      CONSTRAINT CWM_SYS_INDEX_LAYOUT_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_LAYOUT IS ''首页布局''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_LAYOUT.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_LAYOUT.NAME IS ''布局名称''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_LAYOUT.MEMO IS ''布局描述''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_LAYOUT.TEMPLATE_HTML IS ''模版内容''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_INDEX_LAYOUT.SN IS ''排序''';
      commit;
END IF;
END;
--------------------------创建CWM_SYS_MSGREAD表，读取信息状态--------------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_MSGREAD') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_MSGREAD(
      ID NUMBER (18) NOT NULL ,
      MESSAGEID NUMBER (18) ,
      RECEIVERID NUMBER (18) ,
      RECEIVER VARCHAR2 (256 BYTE) ,
      RECEIVETIME DATE
    )';

    execute immediate 'ALTER TABLE CWM_SYS_MSGREAD ADD (
      CONSTRAINT CWM_SYS_MSGREAD_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE  CWM_SYS_MSGREAD IS ''读取信息状态''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREAD.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREAD.MESSAGEID IS ''消息ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREAD.RECEIVERID IS ''接收人Id''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREAD.RECEIVER IS ''接收人''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREAD.RECEIVETIME IS ''接收时间''';
       commit;
END IF;
END;

 ----------------------------创建CWM_SYS_MSGREPLY表，消息回复-----------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_MSGREPLY') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_MSGREPLY(
      ID NUMBER (18) NOT NULL ,
      MESSAGEID NUMBER (18) ,
      CONTENT CLOB ,
      REPLYID NUMBER (18) ,
      REPLY VARCHAR2 (256 BYTE),
      REPLYTIME DATE,
      ISPRIVATE NUMBER (18)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_MSGREPLY ADD (
      CONSTRAINT CWM_SYS_MSGREPLY_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE  CWM_SYS_MSGREPLY IS ''消息回复''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.MESSAGEID IS ''消息ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.CONTENT IS ''内容''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.REPLYID IS ''回复人ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.REPLY IS ''回复人''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.REPLYTIME IS ''回复时间''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_MSGREPLY.ISPRIVATE IS ''私密回复''';
        commit;
END IF;
END;

  ----------------------------------创建CWM_SYS_ERRORLOG表，操作错误日志表----------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ERRORLOG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
     execute immediate 'CREATE TABLE CWM_SYS_ERRORLOG(
      ID NUMBER (18) NOT NULL ,
      HASHCODE VARCHAR2 (256 BYTE) ,
      ACCOUNT VARCHAR2 (256 BYTE),
      IP VARCHAR2 (256 BYTE),
      ERRORURL VARCHAR2 (256 BYTE),
      ERROR CLOB,
      ERRORDATE DATE
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ERRORLOG ADD (
      CONSTRAINT CWM_SYS_ERRORLOG_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE  CWM_SYS_ERRORLOG IS ''错误日志表''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.HASHCODE IS ''错误哈希码''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.ACCOUNT IS ''帐号''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.IP IS ''ip''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.ERRORURL IS ''错误地址''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.ERROR IS ''错误详细信息''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ERRORLOG.ERRORDATE IS ''时间''';
 END IF;
END;
---------------------------创建cwm_sys_bus_event业务数据按钮保存配置表-------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('cwm_sys_bus_event') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE cwm_sys_bus_event (
      ID NUMBER(18) NOT NULL,
      FORMKEY varchar(50) DEFAULT NULL,
      JS_PRE_SCRIPT varchar(4000) DEFAULT NULL,
      JS_AFTER_SCRIPT varchar(2000) DEFAULT NULL,
      PRE_SCRIPT varchar(2000) DEFAULT NULL,
      AFTER_SCRIPT varchar(2000) DEFAULT NULL
    )';


  execute immediate 'COMMENT ON TABLE  cwm_sys_bus_event IS ''业务数据按钮保存配置''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event.ID IS ''主键''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event.FORMKEY IS ''表单key''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event.JS_PRE_SCRIPT IS ''JS前置脚本''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event.JS_AFTER_SCRIPT IS ''js后置脚本''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event.PRE_SCRIPT IS ''JAVA前置脚本''';
  execute immediate 'COMMENT ON COLUMN  cwm_sys_bus_event. AFTER_SCRIPT IS ''JAVA后置脚本''';
 END IF;
END;
  ------------------------------------创建CWM_SYS_ORGAUTH表，组织授权表-------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORGAUTH') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORGAUTH(
      ID NUMBER (18) NOT NULL ,
      USER_ID NUMBER (18),
      ORG_ID NUMBER (18),
      DIM_ID NUMBER (18),
      ORG_PERMS VARCHAR2 (256 BYTE),
      USER_PERMS VARCHAR2 (256 BYTE),
      POS_PERMS VARCHAR2 (256 BYTE),
      ORGAUTH_PERMS VARCHAR2 (256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORGAUTH ADD (
      CONSTRAINT CWM_SYS_ORGAUTH_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE  CWM_SYS_ORGAUTH IS ''组织授权''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.USER_ID IS ''管理员ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORG_ID IS ''组织ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.DIM_ID IS ''维度ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORG_PERMS IS ''针对子用户组的添加更新和删除''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.USER_PERMS IS ''针对用户与组关系的添加更新和删除''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.POS_PERMS IS ''岗位权限''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORGAUTH_PERMS IS ''分级管理权限''';
 END IF;
END;

-------------------------------------添加DBOM分类表------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_DBOM') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_DBOM
    (
      ID             NUMBER(16),
      CODE           VARCHAR2(50),
      NAME           VARCHAR2(100),
      USERNAME       VARCHAR2(50),
      MODIFIED_TIME  DATE,
      DESCRIPTION    VARCHAR2(500)
    )';
    commit;

execute immediate 'COMMENT ON TABLE CWM_DBOM IS ''DBom分类表''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.CODE IS ''代号''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.NAME IS ''名称''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.USERNAME IS ''修改人''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.MODIFIED_TIME IS ''修改时间''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM.DESCRIPTION IS ''描述''';
    commit;


 END IF;
END;
---------------------------------添加DBOM节点表---------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_DBOM_NODE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_DBOM_NODE
    (
      ID                 NUMBER(16),
      CODE               VARCHAR2(100),
      NAME               VARCHAR2(100),
      NODE_TYPE          VARCHAR2(2),
      DATA_SCOURCE       VARCHAR2(100),
      DYNAMIC_NODE       VARCHAR2(100),
      SUB_DATA_SOURCE    VARCHAR2(500),
      NODE_RELATION      VARCHAR2(500),
      DISPLAY            VARCHAR2(10),
      URL                VARCHAR2(4000),
      PCODE              VARCHAR2(50),
      DESCRIPTION        VARCHAR2(500)
    )';
    commit;

execute immediate 'COMMENT ON TABLE CWM_DBOM_NODE IS ''DBom节点表''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.CODE IS ''代号''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.NAME IS ''名称''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.NODE_TYPE IS ''节点类型0静态节点1动态节点''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.DATA_SCOURCE IS ''节点数据源''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.DYNAMIC_NODE IS ''动态子节点定义''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.SUB_DATA_SOURCE IS ''子节点数据源''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.NODE_RELATION IS ''父子节点关联关系''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.DISPLAY IS ''节点显示方式动态Tab页树形''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.URL IS ''节点链接地址即业务模板链接地址''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.PCODE IS ''父节点代号''';
execute immediate 'COMMENT ON COLUMN  CWM_DBOM_NODE.DESCRIPTION IS ''节点描述''';
    commit;

 END IF;
END;
---2016-08-09 by weilei end----

---2016-08-19 by weilei start--

--添加子节点自定义函数列
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_DBOM_NODE') AND COLUMN_NAME =upper('DATA_FORMAT') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (DATA_FORMAT  VARCHAR2(100 BYTE))';
    commit;
  END IF;
    commit;
     execute immediate 'COMMENT ON COLUMN   CWM_DBOM_NODE.DATA_FORMAT IS ''子节点自定义函数''';
    commit;
END;
---2016-08-19 by weilei end----

---------------------------------创建CWM_SYS_ORGAUTH表，组织授权表--------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORGAUTH') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
   execute immediate 'CREATE TABLE CWM_SYS_ORGAUTH(
      ID NUMBER (18) NOT NULL ,
      USER_ID NUMBER (18),
      ORG_ID NUMBER (18),
      DIM_ID NUMBER (18),
      ORG_PERMS VARCHAR2 (256 BYTE),
      USER_PERMS VARCHAR2 (256 BYTE),
      POS_PERMS VARCHAR2 (256 BYTE),
      ORGAUTH_PERMS VARCHAR2 (256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORGAUTH ADD (
      CONSTRAINT CWM_SYS_ORGAUTH_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE CWM_SYS_ORGAUTH IS ''组织授权''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.USER_ID IS ''管理员ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORG_ID IS ''组织ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.DIM_ID IS ''维度ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORG_PERMS IS ''针对子用户组的添加更新和删除''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.USER_PERMS IS ''针对用户与组关系的添加更新和删除''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.POS_PERMS IS ''岗位权限''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_ORGAUTH.ORGAUTH_PERMS IS ''分级管理权限''';
 END IF;
END;
  ---------------------------------------创建CWM_SYS_AUTH_ROLE表，授权角色表(授权表和角色关联表)-------------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_AUTH_ROLE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_AUTH_ROLE(
      ID NUMBER (18) NOT NULL ,
      AUTH_ID NUMBER (18),
      ROLE_ID NUMBER (18)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_AUTH_ROLE ADD (
      CONSTRAINT CWM_SYS_AUTH_ROLE_PK
     PRIMARY KEY
     (ID))';
     execute immediate 'COMMENT ON TABLE CWM_SYS_AUTH_ROLE IS ''授权角色''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_AUTH_ROLE.ID IS ''主键''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_AUTH_ROLE.AUTH_ID IS ''授权ID''';
     execute immediate 'COMMENT ON COLUMN    CWM_SYS_AUTH_ROLE.ROLE_ID IS ''角色ID''';
  END IF;
END;
----------------------------------------创建CWM_BUS_QUERY_FILTER表，查询过滤表-----------------------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_BUS_QUERY_FILTER') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
     execute immediate 'CREATE TABLE CWM_BUS_QUERY_FILTER
    (
      ID              NUMBER(20)                    NOT NULL,
      RULEID          NUMBER(20),
      TABLENAME       VARCHAR2(256 BYTE),
      FILTERNAME      VARCHAR2(256 BYTE),
      FILTERDESC      CLOB,
      FILTERKEY       VARCHAR2(256 BYTE),
      QUERYPARAMETER  CLOB,
      SORTPARAMETER   CLOB,
      ISSHARE         NUMBER(6)                     DEFAULT 0,
      CREATETIME      TIMESTAMP(6)                  DEFAULT sysdate               NOT NULL,
      CREATEBY        NUMBER(20)
    )';
    execute immediate 'ALTER TABLE CWM_BUS_QUERY_FILTER ADD (
      CONSTRAINT CWM_BUS_QUERY_FILTER_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_BUS_QUERY_FILTER IS ''查询过滤''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.RULEID IS ''规则ID''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.TABLENAME IS ''表名''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.FILTERNAME IS ''过滤名称''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.FILTERDESC IS ''过滤描述''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.FILTERKEY IS ''过滤条件Key''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.QUERYPARAMETER IS ''查询参数''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.SORTPARAMETER IS ''排序参数''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.ISSHARE IS ''是否共享''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.CREATETIME IS ''创建时间''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_FILTER.CREATEBY IS ''创建人ID''';
  END IF;
END;
------------------------------创建CWM_BUS_QUERY_RULE表，高级查询规则表----------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_BUS_QUERY_RULE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_BUS_QUERY_RULE
    (
      ID            NUMBER(20)                      NOT NULL,
      TABLENAME     VARCHAR2(128 BYTE),
      NEEDPAGE      NUMBER(11),
      PAGESIZE      NUMBER(20),
      ISQUERY       NUMBER(11),
      ISFILTER      NUMBER(11),
      DISPLAYFIELD  CLOB,
      FILTERFIELD   CLOB,
      SORTFIELD     CLOB,
      EXPORTFIELD   CLOB,
      PRINTFIELD    CLOB,
      CREATETIME    TIMESTAMP(6)                    DEFAULT sysdate               NOT NULL,
      CREATEBY      NUMBER(20),
      UPDATETIME    TIMESTAMP(6),
      UPDATEBY      NUMBER(20)
    )';
    execute immediate 'ALTER TABLE CWM_BUS_QUERY_RULE ADD (
      CONSTRAINT CWM_BUS_QUERY_RULE_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_BUS_QUERY_RULE IS ''高级查询规则''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.TABLENAME IS ''表名''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.NEEDPAGE IS ''是否需要分页0不分页1分页''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.PAGESIZE IS ''分页大小''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.ISQUERY IS ''初始是否进行查询0是1否''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.ISFILTER IS ''是否过滤''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.DISPLAYFIELD IS ''显示字段''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.FILTERFIELD IS ''过滤器字段''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.SORTFIELD IS ''排序字段''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.EXPORTFIELD IS ''导出字段''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.PRINTFIELD IS ''打印字段''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.CREATETIME IS ''创建时间''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.CREATEBY IS ''创建人ID''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.UPDATETIME IS ''更新时间''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_RULE.UPDATEBY IS ''更新人ID''';
  END IF;
END;
----------------------------------------创建CWM_BUS_QUERY_SETTING表，查询设置表-----------------------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_BUS_QUERY_SETTING') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_BUS_QUERY_SETTING
    (
      ID            NUMBER(20)                      NOT NULL,
      TABLENAME     VARCHAR2(256 BYTE),
      DISPLAYFIELD  CLOB,
      USERID        NUMBER(20)
    )';
    execute immediate 'ALTER TABLE CWM_BUS_QUERY_SETTING ADD (
      CONSTRAINT CWM_BUS_QUERY_SETTING_PK
      PRIMARY KEY
      (ID))';

execute immediate 'COMMENT ON TABLE  CWM_BUS_QUERY_SETTING IS ''查询设置''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SETTING.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SETTING.TABLENAME IS ''表名''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SETTING.DISPLAYFIELD IS ''显示区域''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SETTING.USERID IS ''用户''';
  END IF;
END;
----------------------------------------创建CWM_BUS_QUERY_SHARE表，查询过滤共享表-----------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_BUS_QUERY_SHARE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_BUS_QUERY_SHARE
    (
      ID          NUMBER(20)                        NOT NULL,
      FILTERID    NUMBER(20),
      SHARERIGHT  CLOB,
      SHARERID    NUMBER(20),
      CREATETIME  TIMESTAMP(6)                      DEFAULT sysdate               NOT NULL
    )';
    execute immediate 'ALTER TABLE CWM_BUS_QUERY_SHARE ADD (
      CONSTRAINT CWM_BUS_QUERY_SHARE_PK
      PRIMARY KEY
      (ID))';

execute immediate 'COMMENT ON TABLE CWM_BUS_QUERY_SHARE IS ''查询过滤共享''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SHARE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SHARE.FILTERID IS ''过滤ID''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SHARE.SHARERIGHT IS ''共享权限''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SHARE.SHARERID IS ''共享人ID''';
execute immediate 'COMMENT ON COLUMN  CWM_BUS_QUERY_SHARE.CREATETIME IS ''创建时间''';
  END IF;
END;
------------------------创建CWM_SYS_DEMENSION表，维度表----------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_DEMENSION') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_DEMENSION
    (
      DEMID    NUMBER(18)                           NOT NULL,
      DEMNAME  VARCHAR2(256 BYTE),
      DEMDESC  VARCHAR2(256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_DEMENSION ADD (
      CONSTRAINT CWM_SYS_DEMENSION_PK
      PRIMARY KEY
      (DEMID))';

execute immediate 'COMMENT ON TABLE CWM_SYS_DEMENSION IS ''人员维度表''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_DEMENSION.DEMID IS ''维度编号''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_DEMENSION.DEMNAME IS ''维度名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_DEMENSION.DEMDESC IS ''维度描述''';
  END IF;
END;
----------------------------------------更新CWM_SYS_DIC表，数据字典表-------------------------------------------
---2016-8-2 by yangbo---
BEGIN
    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('DICID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  RENAME COLUMN ID TO DICID';
    commit;
  END IF;

   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('TYPEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  RENAME COLUMN PROTYPEID TO TYPEID';
    commit;
  END IF;

   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('ITEMKEY') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  ADD  ITEMKEY VARCHAR2(64 BYTE) DEFAULT NULL';
    commit;
  END IF;

   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('NODEPATH') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_DIC  ADD  NODEPATH VARCHAR2(100 BYTE) DEFAULT NULL';
    commit;
  END IF;

   select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_DIC') AND COLUMN_NAME =upper('PARENTID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_DIC  ADD  PARENTID NUMBER(20) DEFAULT NULL';
    commit;
  END IF;

 execute immediate 'COMMENT ON COLUMN   CWM_SYS_DIC.ITEMKEY IS ''项Key''';
 execute immediate 'COMMENT ON COLUMN   CWM_SYS_DIC.NODEPATH IS ''节点路径''';
 execute immediate 'COMMENT ON COLUMN   CWM_SYS_DIC.PARENTID IS ''父节点''';
END;
---2016-8-2 by yangbo---
------------------------------------------更新CWM_SYS_JOB表，职务表-------------------------------------------
BEGIN
     select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_JOB') AND COLUMN_NAME =upper('SETID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_JOB  ADD  SETID NUMBER(18) DEFAULT 0 not null';
    commit;
  END IF;
     select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_JOB') AND COLUMN_NAME =upper('GRADE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_JOB  ADD  GRADE NUMBER(18) DEFAULT 0 not null';
    commit;
  END IF;

  execute immediate 'COMMENT ON COLUMN   CWM_SYS_JOB.SETID IS ''设置码，对应组织ID，说明是这个组织私有的职务，公有职务SETID=0''';
  execute immediate 'COMMENT ON COLUMN   CWM_SYS_JOB.GRADE IS ''等级''';
END;
-------------------------------新增CWM_SYS_MSGREPLY表，消息回复表--------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_MSGREPLY') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_MSGREPLY
    (
      ID         NUMBER(18)                         NOT NULL,
      MESSAGEID  NUMBER(18),
      CONTENT    CLOB,
      REPLYID    NUMBER(18),
      REPLY      VARCHAR2(256 BYTE),
      REPLYTIME  DATE,
      ISPRIVATE  NUMBER(18)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_MSGREPLY ADD (
      CONSTRAINT CWM_SYS_MSGREPLY_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_MSGREPLY IS ''消息回复''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.MESSAGEID IS ''消息ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.CONTENT IS ''内容''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.REPLYID IS ''回复人ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.REPLY IS ''回复人''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.REPLYTIME IS ''回复时间''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_MSGREPLY.ISPRIVATE IS ''私密回复''';
  END IF;
END;
------------------------------更新CWM_SYS_MSGSEND表，消息发送记录表-------------------------------
---2016-8-2 by yangbo---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_MSGSEND') AND COLUMN_NAME =upper('ATTACHMENT') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_MSGSEND  ADD  ATTACHMENT VARCHAR2(2000 BYTE)  DEFAULT NULL';
    commit;
  END IF;

 execute immediate 'COMMENT ON COLUMN   CWM_SYS_MSGSEND.ATTACHMENT IS ''附件''';
END;
-------------------------------新增cwm_sys_pos表，岗位表(替代Position)---------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_POS') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_POS
    (
      POSID     NUMBER(18)                          NOT NULL,
      POSCODE   VARCHAR2(256 BYTE),
      POSNAME   VARCHAR2(256 BYTE),
      POSDESC   VARCHAR2(256 BYTE),
      ORGID     NUMBER(18),
      JOBID     NUMBER(18),
      ISDELETE  NUMBER(18)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_POS ADD (
      CONSTRAINT CWM_SYS_POS_PK
      PRIMARY KEY
      (POSID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_POS IS ''岗位表''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.POSID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.POSCODE IS ''别名''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.POSNAME IS ''岗位名组织职务''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.POSDESC IS ''备注''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.ORGID IS ''组织''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.JOBID IS ''职务''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_POS.ISDELETE IS ''0:未删除 1：删除''';
  END IF;
END;

-----------------------------新增CWM_SYS_ERRORLOG表，错误日志表--------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ERRORLOG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ERRORLOG
    (
      ID         NUMBER(18)                         NOT NULL,
      HASHCODE   VARCHAR2(256 BYTE),
      ACCOUNT    VARCHAR2(256 BYTE),
      IP         VARCHAR2(256 BYTE),
      ERRORURL   VARCHAR2(256 BYTE),
      ERROR      CLOB,
      ERRORDATE  DATE
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ERRORLOG ADD (
      CONSTRAINT CWM_SYS_ERRORLOG_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ERRORLOG IS ''错误日志表''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.HASHCODE IS ''错误哈希码''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.ACCOUNT IS ''帐号''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.IP IS ''ip''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.ERRORURL IS ''错误地址''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.ERROR IS ''错误详细信息''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ERRORLOG.ERRORDATE IS ''时间';
  END IF;
END;
----------------------------------------新增cwm_sys_joblog表，后台任务执行日志表------------------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_JOBLOG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_JOBLOG
    (
      LOGID      NUMBER(18)                         NOT NULL,
      JOBNAME    VARCHAR2(50 BYTE),
      TRIGNAME   VARCHAR2(50 BYTE),
      STARTTIME  DATE,
      ENDTIME    DATE,
      CONTENT    CLOB,
      STATE      NUMBER(18),
      RUNTIME    NUMBER(18)
    )';
   execute immediate 'ALTER TABLE CWM_SYS_JOBLOG ADD (
      CONSTRAINT CWM_SYS_JOBLOG_PK
      PRIMARY KEY
      (LOGID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_JOBLOG IS ''后台任务执行日志''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.LOGID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.JOBNAME IS ''执行名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.TRIGNAME IS ''触发名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.STARTTIME IS ''开始时间''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.ENDTIME IS ''结束时间''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.CONTENT IS ''日志内容''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.STATE IS ''状态''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_JOBLOG.RUNTIME IS ''运行时间持续时间''';
  END IF;
END;

-----------------------------------------新增CWM_SYS_OBJRIGHTS表，权限对象表------------------------------------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_OBJRIGHTS') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
   execute immediate 'CREATE TABLE CWM_SYS_OBJRIGHTS
    (
      ID          NUMBER(18)                        NOT NULL,
      OBJ_TYPE    VARCHAR2(256 BYTE),
      OBJECT_ID   NUMBER(18),
      OWNER_ID    NUMBER(18),
      OWNER       VARCHAR2(256 BYTE),
      RIGHT_TYPE  VARCHAR2(256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_OBJRIGHTS ADD (
      CONSTRAINT CWM_SYS_OBJRIGHTS_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_OBJRIGHTS IS ''权限对象表''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.OBJ_TYPE IS ''对象类型''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.OBJECT_ID IS ''权限对象ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.OWNER_ID IS ''授权人ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.OWNER IS ''授权人''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_OBJRIGHTS.RIGHT_TYPE IS ''权限类型''';
  END IF;
END;

----------------------------------------新增CWM_SYS_ORG表，组织架构表,替代department表----------------

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORG
    (
      ORGID        NUMBER(18)                       NOT NULL,
      DEMID        NUMBER(18),
      ORGNAME      VARCHAR2(128 BYTE),
      ORGDESC      VARCHAR2(500 BYTE),
      ORGSUPID     NUMBER(18),
      PATH         VARCHAR2(256 BYTE),
      DEPTH        NUMBER(18),
      ORGTYPE      NUMBER(18),
      CREATORID    NUMBER(18),
      CREATETIME   DATE,
      UPDATEID     NUMBER(18),
      UPDATETIME   DATE,
      SN           NUMBER(18),
      FROMTYPE     NUMBER(18),
      ORGPATHNAME  VARCHAR2(2000 BYTE),
      ISDELETE     NUMBER(18),
      CODE         VARCHAR2(256 BYTE),
      COMPANYID    NUMBER(18),
      COMPANY      VARCHAR2(256 BYTE),
      ORGSTAFF     NUMBER(18)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORG ADD (
      CONSTRAINT CWM_SYS_ORG_PK
      PRIMARY KEY
      (ORGID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORG IS ''组织架构''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGID IS ''组织ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.DEMID IS ''维度编号''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGNAME IS ''名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGDESC IS ''描述''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGSUPID IS ''上级''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.PATH IS ''路径''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.DEPTH IS ''层次''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGTYPE IS ''类型0集团1公司2部门3其他组织''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.CREATORID IS ''建立人''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.CREATETIME IS ''建立时间''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.UPDATEID IS ''修改人''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.UPDATETIME IS ''修改时间''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.SN IS ''排序''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.FROMTYPE IS ''数据来源''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGPATHNAME IS ''组织名称路径''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ISDELETE IS ''删除标志0正常1删除''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.CODE IS ''组织代码自动继承父级代码''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.COMPANYID IS ''公司''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.COMPANY IS ''分公司''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG.ORGSTAFF IS ''编制人数''';
  END IF;
END;
---------------------------------------新增CWM_SYS_ORG_PARAM表，组织参数值表----

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORG_PARAM') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORG_PARAM
    (
      VALUEID         NUMBER(18)                    NOT NULL,
      ORGID           NUMBER(18),
      PARAMID         NUMBER(18),
      PARAMVALUE      VARCHAR2(256 BYTE),
      PARAMDATEVALUE  DATE,
      PARAMINTVALUE   NUMBER(18,2)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORG_PARAM ADD (
      CONSTRAINT CWM_SYS_ORG_PARAM_PK
      PRIMARY KEY
      (VALUEID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORG_PARAM IS ''组织参数值''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.VALUEID IS ''主键ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.ORGID IS ''组织ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.PARAMID IS ''属性ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.PARAMVALUE IS ''字符串属性值''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.PARAMDATEVALUE IS ''日期型属性值''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_PARAM.PARAMINTVALUE IS ''数值型属性值''';
  END IF;
END;
-----------------------------------------新增CWM_SYS_ORG_ROLE表，组织和角色授权表----

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORG_ROLE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORG_ROLE
    (
      ID      NUMBER(20)                            NOT NULL,
      ORGID   NUMBER(20),
      ROLEID  NUMBER(20),
      CANDEL  NUMBER(6)
    )';

    execute immediate 'ALTER TABLE CWM_SYS_ORG_ROLE ADD (
      CONSTRAINT CWM_SYS_ORG_ROLE_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORG_ROLE IS ''组织和角色授权''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLE.ORGID IS ''组织ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLE.ROLEID IS ''角色ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLE.CANDEL IS ''是否可以删除(在组织授权的不可以删除0不可以删除1可以删除)''';
  END IF;
END;
-----------------------------------------新增CWM_SYS_ORG_ROLEMANAGE表，组织可以授权的角色范围表----

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORG_ROLEMANAGE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORG_ROLEMANAGE
    (
      ID      NUMBER(20)                            NOT NULL,
      ORGID   NUMBER(20),
      ROLEID  NUMBER(20),
      CANDEL  NUMBER(6)
    )';
   execute immediate 'ALTER TABLE CWM_SYS_ORG_ROLEMANAGE ADD (
      CONSTRAINT CWM_SYS_ORG_ROLEMANAGE_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORG_ROLEMANAGE IS ''组织可以授权的角色范围(用于分级授权)''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLEMANAGE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLEMANAGE.ORGID IS ''组织ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLEMANAGE.ROLEID IS ''角色ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_ROLEMANAGE.CANDEL IS ''是否可以删除''';
  END IF;
END;
-----------------------------------------新增CWM_SYS_ORGTACTIC表，组织策略表----

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORGTACTIC') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORGTACTIC
    (
      ID             NUMBER(20)                     NOT NULL,
      ORG_TACTIC     NUMBER(6),
      DEM_ID         NUMBER(20),
      ORG_TYPE       NUMBER(20),
      ORG_SELECT_ID  CLOB
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORGTACTIC ADD (
      CONSTRAINT CWM_SYS_ORGTACTIC_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORGTACTIC IS ''组织策略''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORGTACTIC.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORGTACTIC.ORG_TACTIC IS ''策略0无策略1按照级别2手工选择3按照级别手工选择''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORGTACTIC.DEM_ID IS ''维度ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORGTACTIC.ORG_TYPE IS ''组织级别''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORGTACTIC.ORG_SELECT_ID IS ''组织选择ID''';
  END IF;
END;

-----------------------------------------新增CWM_SYS_ORG_TYPE表，组织结构类型表----
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_ORG_TYPE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_ORG_TYPE
    (
      ID      NUMBER(18)                            NOT NULL,
      DEMID   NUMBER(18),
      NAME    VARCHAR2(256 BYTE),
      LEVELS  NUMBER(18),
      MEMO    VARCHAR2(256 BYTE),
      ICON    VARCHAR2(256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_ORG_TYPE ADD (
      CONSTRAINT CWM_SYS_ORG_TYPE_PK
      PRIMARY KEY
      (ID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_ORG_TYPE IS ''组织结构类型''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.DEMID IS ''维度ID''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.NAME IS ''名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.LEVELS IS ''级别''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.MEMO IS ''备注''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_ORG_TYPE.ICON IS ''图标''';
  END IF;
END;
-----------------------------------------新增CWM_SYS_PARAM表，用户或组织自定义属性表----
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_PARAM') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_PARAM
    (
      PARAMID      NUMBER(18)                       NOT NULL,
      PARAMKEY     VARCHAR2(32 BYTE),
      PARAMNAME    VARCHAR2(50 BYTE),
      DATATYPE     VARCHAR2(20 BYTE),
      EFFECT       NUMBER(18),
      BELONGDEM    NUMBER(20),
      SOURCETYPE   VARCHAR2(20 BYTE),
      SOURCEKEY    VARCHAR2(100 BYTE),
      DESCRIPTION  VARCHAR2(256 BYTE),
      STATUS_      NUMBER(18),
      CATEGORY     VARCHAR2(256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_PARAM ADD (
      CONSTRAINT CWM_SYS_PARAM_PK
      PRIMARY KEY
      (PARAMID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_PARAM IS ''用户或组织自定义属性''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.PARAMID IS ''主键''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.PARAMKEY IS ''属性key值''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.PARAMNAME IS ''属性名称''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.DATATYPE IS ''数据类型''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.EFFECT IS ''参数类型1个人2组织''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.BELONGDEM IS ''所属维度''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.SOURCETYPE IS ''数据来源''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.SOURCEKEY IS ''数据来源key''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.DESCRIPTION IS ''参数描述''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.STATUS_ IS ''参数状态''';
execute immediate 'COMMENT ON COLUMN  CWM_SYS_PARAM.CATEGORY IS ''参数分类''';
  END IF;
END;
---删除CWM_SYS_PROPERTY表，系统属性表----
-----------------------------------------2016-8-6 by yangbo---
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_PROPERTY') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt>0) THEN
    execute immediate 'drop table CWM_SYS_PROPERTY';
  END IF;
END;
-----------------------------------------新增CWM_SYS_SHARE_RIGHTS表----

BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_SHARE_RIGHTS') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
execute immediate 'CREATE TABLE CWM_SYS_SHARE_RIGHTS
(
  ID         NUMBER(20)                         NOT NULL,
  NAME       VARCHAR2(250 BYTE),
  SHAREITEM  VARCHAR2(250 BYTE),
  ENABLE     NUMBER(20),
  SYNC       NUMBER(20),
  ISALL      NUMBER(20),
  PKID       NUMBER(20),
  SOURCE     VARCHAR2(250 BYTE),
  TARGET     VARCHAR2(250 BYTE),
  SHARES     VARCHAR2(250 BYTE)
)';
execute immediate 'ALTER TABLE CWM_SYS_SHARE_RIGHTS ADD (
  CONSTRAINT CWM_SYS_SHARE_RIGHTS_PK
  PRIMARY KEY
  (ID))';
  END IF;
END;
 -----------------------------------------更新CWM_SYS_USER_POSITION表的字段------------------------------------------
---2016-8-8 by yangbo---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('USERPOSID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  RENAME COLUMN USER_POS_ID TO USERPOSID';
    commit;
  END IF;

    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('POSID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  RENAME COLUMN POS_ID TO POSID';
    commit;
  END IF;

    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('ORGID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  RENAME COLUMN ORG_ID TO ORGID';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('ISCHARGE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  ADD  ISCHARGE NUMBER(18) DEFAULT NULL';
    commit;
  END IF;

    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('ISDELETE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  ADD  ISDELETE  NUMBER(18) DEFAULT 0';
    commit;
  END IF;

    select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_POSITION') AND COLUMN_NAME =upper('JOBID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_POSITION  ADD  JOBID NUMBER(18) DEFAULT NULL';
    commit;
  END IF;

  execute immediate 'COMMENT ON COLUMN   CWM_SYS_USER_POSITION.ISDELETE IS ''删除与否''';
  execute immediate 'COMMENT ON COLUMN   CWM_SYS_USER_POSITION.JOBID IS ''职务ID''';
END;
 -------------------------------------更新CWM_SYS_GLTYPE表的字段 ,总分类表用于显示树层次结构的分类可以允许任何层次结构----
---2016-8-9 by yangbo---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('TYPEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  RENAME COLUMN PROTYPEID TO TYPEID';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('NODEPATH') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  RENAME COLUMN PATH TO NODEPATH';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('TYPE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  TYPE NUMBER(18) DEFAULT 1  NOT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('ISLEAF') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  ISLEAF NUMBER(18) DEFAULT 0 NOT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('NODECODE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  NODECODE  VARCHAR2(20 BYTE) DEFAULT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_GLTYPE') AND COLUMN_NAME =upper('NODECODETYPE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_GLTYPE  ADD  NODECODETYPE NUMBER(18) DEFAULT 0 NOT NULL';
    commit;
  END IF;

END;
-----------------------------------新增CWM_SYS_ROLE表的字段 ，有数据的情况下ALLOWDEL和 ALLOWEDIT两个字段值为1----
---2016-8-10 by yangbo---
BEGIN

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ROLE') AND COLUMN_NAME =upper('ALIAS') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_ROLE  ADD  ALIAS  VARCHAR2(256 BYTE)  DEFAULT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ROLE') AND COLUMN_NAME =upper('ALLOWDEL') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_ROLE  ADD  ALLOWDEL NUMBER(18)  DEFAULT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ROLE') AND COLUMN_NAME =upper('ALLOWEDIT') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_ROLE  ADD  ALLOWEDIT  NUMBER(18) DEFAULT NULL';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ROLE') AND COLUMN_NAME =upper('CATEGORY') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
     execute immediate 'ALTER TABLE CWM_SYS_ROLE  ADD  CATEGORY VARCHAR2(50 BYTE) DEFAULT NULL';
    commit;
  END IF;
END;

-----------------------------------更新CWM_SYS_USER_ORG表的字段 ----
---2016-8-10 by yangbo---
BEGIN
	select count(*) into countt FROM tabs WHERE TABLE_NAME = upper('CWM_SYS_USER_ORG') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt>0) THEN
  	select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_ORG') AND COLUMN_NAME =upper('USERORGID') AND OWNER=upper(owneruser);
  	IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_ORG  RENAME COLUMN USER_ORG_ID TO USERORGID';
    commit;
  END IF; 

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_ORG') AND COLUMN_NAME =upper('ORGID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_ORG  RENAME COLUMN ORG_ID TO ORGID';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_ORG') AND COLUMN_NAME =upper('ISPRIMARY') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_ORG  RENAME COLUMN IS_PRIMARY TO ISPRIMARY';
    commit;
  END IF;

  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_ORG') AND COLUMN_NAME =upper('ISCHARGE') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER_ORG  RENAME COLUMN IS_CHARGE TO ISCHARGE';
    commit;
  END IF;
 END IF;  
END;

 -------------添加CWM_SYS_USER_ORG表新字段,是否管理员级别------2016-8-17 by yangbo-------------
BEGIN
select count(*) into countt FROM tabs WHERE TABLE_NAME = upper('CWM_SYS_USER_ORG') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt>0) THEN	
	select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER_ORG') AND COLUMN_NAME =upper('ISCHARGE') AND OWNER=upper(owneruser);
	 IF(countt=0) THEN
	 	 execute immediate 'Alter table CWM_SYS_USER_ORG add isGradeManage NUMBER(18) default 0 not null'; 
	   	commit;
	 END IF;
END IF;	 
END; 
-----------------------------------更新CWM_SYS_RES表字段---
---2016-8-15 by yangbo---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_FUNCTION')  AND OWNER=upper(owneruser);
 IF(countt>0) THEN
  	select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_FUNCTION') AND COLUMN_NAME =upper('DEFAULTURL') AND OWNER=upper(owneruser);
  	IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_FUNCTION  RENAME COLUMN URL TO DEFAULTURL';
    commit;
 END IF; 
  END IF;
commit;
END;

------------------------------------加入功能点URL表CWM_SYS_RESURL------2016-8-17 by yangbo---
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_RESURL') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
        execute immediate 'CREATE TABLE CWM_SYS_RESURL
        (
          RESURLID NUMBER(20)NOT NULL,
          RESID NUMBER(20),
          NAME VARCHAR2(100 BYTE),
          URL VARCHAR2(200 BYTE)
        )';
        execute immediate 'ALTER TABLE CWM_SYS_RESURL ADD (
          CONSTRAINT  CWM_SYS_RESURL_PK
          PRIMARY KEY
          (RESURLID))';
        commit;
        execute immediate 'COMMENT ON TABLE  CWM_SYS_RESURL IS ''功能URL表''';
        execute immediate 'COMMENT ON COLUMN    CWM_SYS_RESURL.RESURLID IS ''主键''';
        execute immediate 'COMMENT ON COLUMN    CWM_SYS_RESURL.RESID IS ''功能点ID''';
        execute immediate 'COMMENT ON COLUMN    CWM_SYS_RESURL.NAME IS ''名称''';
        execute immediate 'COMMENT ON COLUMN    CWM_SYS_RESURL.URL IS ''功能点URL''';
        commit;
        select count(*) into countt1   from tabs where table_name = upper('CWM_SYS_FUNCTIONURL') AND TABLESPACE_NAME=upper(owneruser);
         IF(countt1>0) THEN
        execute immediate 'drop table CWM_SYS_FUNCTIONURL';
        END IF;
	commit;
  END IF;
END;
 ------------------------------------2016-8-8 by yangbo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ROLE_USER') AND COLUMN_NAME =upper('USERROLEID') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_USER drop CONSTRAINT PK_ROLE_USER';
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_USER  ADD  USERROLEID NUMBER(20) DEFAULT NULL';
    execute immediate 'update CWM_SYS_ROLE_USER set USERROLEID=rownum';
    commit;
  END IF;

END;
-------------------------------------加主键------------------------------------
BEGIN
  select count(*)  into countt  from user_constraints where table_name = upper('CWM_SYS_ROLE_USER') and constraint_name=upper('CWM_SYS_ROLE_USER_PK');
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_USER ADD (CONSTRAINT CWM_SYS_ROLE_USER_PK PRIMARY KEY (USERROLEID))';
    commit;
  END IF;
 END;

-----------------------------------增加unique key约束 2016-8-23 by hhj -----------------------------------
BEGIN
  select count(*)  into countt  from user_constraints where table_name = upper('CWM_SYS_PARAMETER') and constraint_name=upper('CWM_SYS_PARAMETER_UK');
  IF(countt=0) THEN
  ---------先删除重复name相同的记录，且只保留一条
   execute immediate 'delete from CWM_SYS_PARAMETER where name in (
   select distinct name from CWM_SYS_PARAMETER group by name having count(name)>1)
   and id not in (select min(id) from CWM_SYS_PARAMETER group by name having count(name)>1 )';

   execute immediate 'ALTER TABLE CWM_SYS_PARAMETER ADD CONSTRAINT CWM_SYS_PARAMETER_UK UNIQUE (NAME)';
   commit;
  END IF;
 END;
 ------------------------CWM_SYS_ROLE_FUNCTION表更新为CWM_SYS_ROLE_RES--2016-8-23--by yangbo---------------------------------------------
 BEGIN
  select count(*) into countt  FROM tabs WHERE TABLE_NAME =upper('CWM_SYS_ROLE_RES') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
  	select count(*) into countt1  FROM tabs WHERE TABLE_NAME =upper('CWM_SYS_ROLE_FUNCTION') AND TABLESPACE_NAME=upper(owneruser);
 	IF(countt1>0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_FUNCTION drop CONSTRAINT PK_ROLE_FUNCTION';
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_FUNCTION  RENAME COLUMN FUNCTION_ID TO RESID';
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_FUNCTION  ADD  ROLERESID NUMBER(20) DEFAULT NULL';
    execute immediate 'update CWM_SYS_ROLE_FUNCTION set ROLERESID=rownum';
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_FUNCTION  RENAME TO CWM_SYS_ROLE_RES';
    execute immediate 'ALTER TABLE CWM_SYS_ROLE_RES ADD (CONSTRAINT ROLE_RES_PK PRIMARY KEY (ROLERESID))';
    commit;
    END IF;
  END IF;
END; 


 
 -----------------------------------------新增CWM_SYS_RES表，用户或组织自定义属性表----
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_RES') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_RES
	(
	  RESID       NUMBER(20)         NOT NULL,
	  RESNAME     VARCHAR2(128 BYTE) NOT NULL,
	  ALIAS       VARCHAR2(128 BYTE),
	  SN   		  NUMBER(20),
	  ICON  	  VARCHAR2(100 BYTE),
	  PARENTID    NUMBER(20),
	  DEFAULTURL  VARCHAR2(256 BYTE),
	  ISFOLDER    NUMBER(6),
	  ISDISPLAYINMENU    NUMBER(6),
	  ISOPEN     NUMBER(6),
	  ISNEWOPEN  NUMBER(6),
	  PATH   	VARCHAR2(500 BYTE)
	)';
    execute immediate 'ALTER TABLE CWM_SYS_RES ADD (
  		CONSTRAINT PK_SYS_RES
  		PRIMARY KEY
  		(RESID))';
execute immediate 'COMMENT ON TABLE CWM_SYS_RES IS ''功能点''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.RESID IS ''主键''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.RESNAME IS ''资源名称''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ALIAS IS ''别名（系统中唯一)''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.SN IS ''同级排序''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ICON IS ''图标''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.PARENTID IS ''父ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.DEFAULTURL IS ''默认地址''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ISFOLDER IS ''栏目''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ISDISPLAYINMENU IS ''显示到菜单''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ISOPEN IS ''默认打开''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.ISNEWOPEN IS ''是否打开新窗口：0否,1是''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_RES.PATH IS ''资源路径''';
commit;
  END IF;
END;
------------------------更新CWM_SYS_FUNCTION表，合并为defaulturl--2016-8-27-by yangbo----------------------------------------------
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_FUNCTION') AND COLUMN_NAME =upper('DEFAULTURL') AND OWNER=upper(owneruser);
  IF(countt>0) THEN
    execute immediate 'update CWM_SYS_FUNCTION set DEFAULTURL=SEAURL where DEFAULTURL is null';
    commit;
  END IF;
END; 
--------------------------将CWM_SYS_FUNCTION表数据迁移到CWM_SYS_RES------2016-8-27-----------------------------------------------
BEGIN
   select count(*) into countt from tabs where table_name = 'CWM_SYS_FUNCTION' AND TABLESPACE_NAME=upper(owneruser);
   IF(countt>0) THEN
   	execute immediate 'insert into CWM_SYS_RES (RESID,RESNAME,ALIAS,SN,ICON,PARENTID,DEFAULTURL,ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
						select  FUNCTIONID,NAME,CODE,POSITION,MIDICON,PARENTID,DEFAULTURL,1,1,1,null,null from CWM_SYS_FUNCTION';
   	commit;
   	execute immediate 'update CWM_SYS_RES t1
						set t1.ISFOLDER=0
						where t1.RESID not in(select DISTINCT t2.PARENTID from CWM_SYS_RES t2)';
   	execute immediate 'drop table CWM_SYS_FUNCTION';
   	commit;
   END IF;

 END;
---------------------------用户表CWM_SYS_USER中acceiontime默认为空-2016-9-10---by yangbo--------------
BEGIN
	select NULLABLE into sqlll FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('ACCESSIONTIME') AND OWNER=upper(owneruser);
  	IF(sqlll ='N') THEN
    	execute immediate 'ALTER TABLE CWM_SYS_USER MODIFY (ACCESSIONTIME DATE NULL)';
   	END IF;
    commit;
END;

---------------------------用户表CWM_SYS_ORG中ISDELETE默认为0-2016-9-20---by yangbo--------------
BEGIN
    select NULLABLE into sqlll  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_ORG') AND COLUMN_NAME =upper('ISDELETE') AND OWNER=upper(owneruser);
  	IF(sqlll ='Y') THEN
   		execute immediate 'ALTER TABLE CWM_SYS_ORG MODIFY (ISDELETE  INTEGER default 0 NOT NULL)';
   	END IF;
    commit;
END;

---------------------------用户表CWM_SYS_JOB中ISDELETE默认为0-2016-9-20---by yangbo--------------
BEGIN
    select NULLABLE into sqlll  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_JOB') AND COLUMN_NAME =upper('ISDELETE') AND OWNER=upper(owneruser);
  	IF(sqlll ='Y') THEN
   		execute immediate 'ALTER TABLE CWM_SYS_JOB MODIFY (ISDELETE  number(38) default 0 NOT NULL)';
  	END IF;
    commit;
END;

-- 增加office模板表-------by-zhangxg---------------------------------------
BEGIN
	SELECT COUNT(*) INTO countt FROM tabs WHERE  TABLE_NAME=upper('CWM_SYS_OFFICE_TEMPLATE') AND TABLESPACE_NAME=upper(owneruser);
	IF(countt=0)THEN
	execute immediate 'CREATE TABLE cwm_sys_office_template (
	OFFICEID VARCHAR2(38) NOT NULL,
	TABLE_ID VARCHAR2(200) DEFAULT NULL NULL,
	VIEWS_ID VARCHAR2(38) DEFAULT NULL NULL,
	CONTENT CLOB DEFAULT NULL NULL,
	FILEPATH VARCHAR2(200) DEFAULT NULL NULL,
	TYPE_ID NUMBER(20,0) DEFAULT NULL NULL,
	OFFICE_TYPE VARCHAR2(100) DEFAULT NULL NULL,
	TITLE VARCHAR2(100) DEFAULT NULL NULL,
	DATA_ENTRY VARCHAR2(38) DEFAULT NULL NULL,
	CREATE_USER VARCHAR2(100) DEFAULT NULL NULL,
	CREATE_TIME DATE DEFAULT NULL NULL,
	PRIMARY KEY (OFFICEID) 
	)';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.OFFICEID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.TABLE_ID IS ''数据表ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.VIEWS_ID IS ''视图ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.CONTENT IS ''office标签''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.FILEPATH IS ''模板文件''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.TYPE_ID IS ''模板global分类''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.OFFICE_TYPE IS ''模板类型''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.TITLE IS ''模板标题''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.DATA_ENTRY IS ''主表入口''';
	END IF;
	commit;
END;



 commit;
END synstruct;
