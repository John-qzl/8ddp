CREATE OR REPLACE PROCEDURE SYNSTRUCT_ZHANGXG (owneruser IN VARCHAR DEFAULT 'ibms')
AS
oldcols number;
newcols number;
new_tabs number;
result number;
nullable_ varchar2(10);

BEGIN

select count(*) into oldcols  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('SECURITY') AND OWNER=upper(owneruser);
select count(*) into newcols  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('SECURITY_') AND OWNER=upper(owneruser);
IF(oldcols=1 AND newcols=0)THEN
execute immediate'ALTER TABLE "CWM_SYS_FILE" RENAME COLUMN "SECURITY" TO "SECURITY_"';
END IF;

select count(*) into oldcols  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('DESCRIBE') AND OWNER=upper(owneruser);
select count(*) into newcols  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_file') AND COLUMN_NAME =upper('DESCRIBE_') AND OWNER=upper(owneruser);
IF(oldcols=1 AND newcols=0)THEN
execute immediate'ALTER TABLE "CWM_SYS_FILE" RENAME COLUMN "DESCRIBE" TO "DESCRIBE_"';
END IF;

-- 表单模板表增加子表排序信息字段-----------------------------------------------
select count(*) into newcols FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('SUBSORTFIELD') AND OWNER=upper(owneruser);
IF(newcols=0)THEN
execute immediate'ALTER TABLE "IBMS_DATA_TEMPLATE" ADD ("SUBSORTFIELD" CLOB NULL )';
END IF;

-- 表单模板表增加关联表排序信息字段-----------------------------------------------
select count(*) into newcols FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('RELSORTFIELD') AND OWNER=upper(owneruser);
IF(newcols=0)THEN
execute immediate'ALTER TABLE "IBMS_DATA_TEMPLATE" ADD ("RELSORTFIELD" CLOB NULL )';
END IF;



-- 增加报表管理表-----------------------------------------------
SELECT COUNT(*) INTO new_tabs FROM tabs WHERE  TABLE_NAME=upper('CWM_SYS_REPORT_TEMPLATE') AND TABLESPACE_NAME=upper(owneruser);
IF(new_tabs=0)THEN
execute immediate '
CREATE TABLE cwm_sys_report_template (
REPORTID NUMBER(20,0) NOT NULL,
TITLE VARCHAR2(128) NOT NULL,
DESCP VARCHAR2(500) NOT NULL,
REPORTLOCATION VARCHAR2(128) NOT NULL,
CREATETIME DATE NOT NULL,
UPDATETIME DATE NOT NULL,
REPORTTYPE VARCHAR2(100) NOT NULL,
TYPEID NUMBER(20,0) NOT NULL,
PRIMARY KEY (REPORTID) 
)';

execute immediate 'CREATE UNIQUE INDEX unique_report_template_title ON cwm_sys_report_template (TITLE)';
execute immediate 'COMMENT ON TABLE CWM_SYS_REPORT_TEMPLATE IS ''报表模板管理表''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.REPORTID IS ''报表编号''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.TITLE IS ''标题''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.DESCP IS ''描述''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.REPORTLOCATION IS ''报表模块的cpt文件的路径''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.CREATETIME IS ''创建时间''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.UPDATETIME IS ''修改时间''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.REPORTTYPE IS ''报表类型：finerreport pageoffice''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_TEMPLATE.TYPEID IS ''报表分类ID''';

END IF;



--增加报表模板参数表-----------------------------------------------
SELECT COUNT(*) INTO new_tabs FROM tabs WHERE  TABLE_NAME=upper('CWM_SYS_REPORT_PARAM') AND TABLESPACE_NAME=upper(owneruser);
IF(new_tabs=0)THEN
execute immediate '
CREATE TABLE cwm_sys_report_param (
PARAMID NUMBER(20,0) NOT NULL,
REPORTID NUMBER(20,0) NOT NULL,
NAME VARCHAR2(100) NOT NULL,
VALUE_ VARCHAR2(100) NOT NULL,
PARAMTYPE VARCHAR2(100) DEFAULT NULL NULL,
DESCP VARCHAR2(500) DEFAULT NULL NULL,
CREATETIME DATE NOT NULL,
UPDATETIME DATE DEFAULT NULL NULL,
PRIMARY KEY (PARAMID) 
)';

execute immediate 'COMMENT ON TABLE cwm_sys_report_param IS ''报表参数管理表''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.PARAMID IS ''参数编号''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.REPORTID IS ''报表ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.NAME IS ''参数名''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.VALUE_ IS ''参数值''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.PARAMTYPE IS ''参数类型''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.DESCP IS ''描述''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.CREATETIME IS ''创建时间''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.UPDATETIME IS ''修改时间''';

END IF;

select count(*) into newcols FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_NODE_BTN') AND COLUMN_NAME =upper('PARAMSCRIPT') AND OWNER=upper(owneruser);
IF(newcols=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_BTN ADD (PARAMSCRIPT VARCHAR2(2000) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_BTN.PARAMSCRIPT IS ''参数脚本''';
END IF;




-- 增加office模板书签设置表-----------------------------------------------
SELECT COUNT(*) INTO newcols FROM tabs WHERE  TABLE_NAME=upper('CWM_SYS_OFFICE_ITEMS') AND TABLESPACE_NAME=upper(owneruser);
IF(newcols=0)THEN
execute immediate '
CREATE TABLE cwm_sys_office_items (
ID VARCHAR2(38) NOT NULL,
OFFICE_ID VARCHAR2(38) DEFAULT NULL NULL,
TABLE_ID VARCHAR2(38) DEFAULT NULL NULL,
COLUMN_NAME VARCHAR2(38) DEFAULT NULL NULL,
COLUMN_ID VARCHAR2(38) DEFAULT NULL NULL,
RELATIONS VARCHAR2(100) DEFAULT NULL NULL,
TYPE VARCHAR2(100) DEFAULT NULL NULL,
PRIMARY KEY (ID) 
)
';

execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.ID IS ''标签ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.OFFICE_ID IS ''模板ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.TABLE_ID IS ''表ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.COLUMN_NAME IS ''列名''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.COLUMN_ID IS ''列ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.RELATIONS IS ''关系''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.TYPE IS ''类型''';

END IF;

--增加报表参数长度字段-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'cwm_sys_report_param','PARAM_SIZE') into result FROM dual;
IF(result=0)THEN
execute immediate'ALTER TABLE "CWM_SYS_REPORT_PARAM" ADD ("PARAM_SIZE" CLOB NULL )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_REPORT_PARAM.PARAM_SIZE IS ''参数长度''';
END IF;

-- 增加索引-----------------------------------------------
select SELECTINDEXCOUNT(owneruser,'cwm_dbom','unique_dbom_code') into result FROM dual;
IF(result=0)THEN
execute immediate 'CREATE UNIQUE INDEX "unique_dbom_code" ON "CWM_DBOM" ("CODE" ASC)';
END IF;


-- 创建签章表-----------------------------------------------
select SELECTTABLECOUNT(owneruser,'CWM_SYS_SIGN_ITEM') into result FROM dual;
IF(result=0)THEN
execute immediate '
CREATE TABLE CWM_SYS_SIGN_ITEM (
ID NUMBER(20,0) NOT NULL ,
CODE NUMBER(20,0) NOT NULL ,
IMAGE CLOB NOT NULL ,
SIGN_ID NUMBER(20,0) NOT NULL ,
FORM_ID NUMBER(20,0) NULL ,
TEMPLATE_ID NUMBER(20,0) NULL ,
ACTIVITI_ID NUMBER(20,0) NOT NULL ,
DATA CLOB NOT NULL ,
ENCRY_DATA CLOB NULL ,
SIGN_DATA VARCHAR2(255 BYTE) NULL ,
POSITION VARCHAR2(400 BYTE) NULL ,
PASSWORD VARCHAR2(100 BYTE) NULL ,
PRIMARY KEY (ID) 
)
';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.CODE IS ''序列号''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.IMAGE IS ''签章字节流''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.SIGN_ID IS ''印章模板ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.FORM_ID IS ''表单ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.TEMPLATE_ID IS ''表单模板ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.ACTIVITI_ID IS ''流程实例ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.DATA IS ''原始数据''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.ENCRY_DATA IS ''加密数据''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.SIGN_DATA IS ''印章信息''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.POSITION IS ''印章坐标''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_ITEM.PASSWORD IS ''印章密码''';

END IF;


-- 创建签章模板表-----------------------------------------------
select SELECTTABLECOUNT(owneruser,'CWM_SYS_SIGN_MODEL') into result FROM dual;
IF(result=0)THEN
execute immediate '
CREATE TABLE CWM_SYS_SIGN_MODEL (
ID NUMBER(20,0) NOT NULL,
IMG_PATH CLOB NOT NULL,
USER_ID NUMBER(20,0) NOT NULL,
CODE VARCHAR2(100) NOT NULL,
ORG_ID NUMBER(20,0) NOT NULL,
NAME VARCHAR2(100) NOT NULL,
IS_DEFAULT NUMBER(11,0) DEFAULT 0 NOT NULL,
DESC_ VARCHAR2(200) DEFAULT NULL NULL,
TYPE_ NUMBER(20,0) NOT NULL,
START_DATE DATE NOT NULL,
END_DATE DATE NOT NULL,
STATUS NUMBER(20,0) DEFAULT NULL,
PASSW VARCHAR2(255 BYTE) NOT NULL ,
PATH_TYPE NUMBER DEFAULT 0  NOT NULL ,
VERSION VARCHAR2(100) NOT NULL,
PRIMARY KEY (ID) 
)
';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.IMG_PATH IS ''印模图片''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.USER_ID IS ''所属用户ID''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.CODE IS ''印章编号''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.ORG_ID IS ''所属组织''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.NAME IS ''印章名称''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.IS_DEFAULT IS ''是否为默认印章''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.DESC_ IS ''印章描述''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.TYPE_ IS ''印章类型''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.START_DATE IS ''起始时间''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.END_DATE IS ''结束时间''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.STATUS IS ''状态''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.PASSW IS ''印章密码''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.PATH_TYPE IS ''印章图片存储类型''';
execute immediate 'COMMENT ON COLUMN CWM_SYS_SIGN_MODEL.VERSION IS ''版本号''';

END IF;

-- 添加流程审批意见 task_opsition表印章ID字段-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_TASK_OPINION','SIGN_ID') into result FROM dual;
IF(result=0)THEN
execute immediate'ALTER TABLE "IBMS_TASK_OPINION" ADD ( "SIGN_ID" VARCHAR2(1000) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_TASK_OPINION.SIGN_ID IS ''印章ID''';
END IF;

-- 修改表CWM_SYS_SIGN_MODEL 组织可以为空-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_SIGN_MODEL','ORG_ID') into result FROM dual;
IF(result=1)THEN
SELECT nullable INTO nullable_ FROM user_tab_columns WHERE TABLE_NAME='CWM_SYS_SIGN_MODEL' AND COLUMN_NAME='ORG_ID';
IF(nullable_='N')THEN
execute immediate'
ALTER TABLE "CWM_SYS_SIGN_MODEL"
MODIFY ( "ORG_ID" NUMBER(20) NULL  )';
END IF;

END IF;


-- 常用语设置表增加字段流程节点key-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_APPROVAL_ITEM','DEFNODEKEY') into result FROM dual;
IF(result=0)THEN
execute immediate'
ALTER TABLE "IBMS_APPROVAL_ITEM"
ADD ( "DEFNODEKEY" VARCHAR2(255) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_APPROVAL_ITEM.DEFNODEKEY IS ''流程节点key''';
END IF;

-- 常用语设置表增加字段code-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_APPROVAL_ITEM','CODE') into result FROM dual;
IF(result=0)THEN
execute immediate'
ALTER TABLE "IBMS_APPROVAL_ITEM"
ADD ( "CODE" VARCHAR2(255) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_APPROVAL_ITEM.CODE IS ''审批意见模板COD编号''';
END IF;

-- 常用语设置表增加字段default_ 是否默认-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_APPROVAL_ITEM','DEFAULT_') into result FROM dual;
IF(result=0)THEN
execute immediate'
ALTER TABLE "IBMS_APPROVAL_ITEM"
ADD ( "DEFAULT_" INT NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_APPROVAL_ITEM.DEFAULT_ IS ''是否默认''';
END IF;



-- 会签task 审批意见添加一个字段存储审批意见模板CODE-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_TKSIGN_DATA','VOTE_CODE') into result FROM dual;
IF(result=0)THEN
execute immediate'
ALTER TABLE "IBMS_TKSIGN_DATA"
ADD ( "VOTE_CODE" VARCHAR2(255) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_TKSIGN_DATA.VOTE_CODE IS ''审批意见模板code''';
END IF;

-- 会签task  content字段修改成TEXT-----------------------------------------------
SELECT DATA_TYPE INTO nullable_ FROM ALL_TAB_COLUMNS WHERE TABLE_NAME = upper('IBMS_TKSIGN_DATA') AND COLUMN_NAME = upper('CONTENT') AND OWNER = upper(owneruser);
IF(nullable_='VARCHAR2')THEN
execute immediate'ALTER TABLE "IBMS_TKSIGN_DATA"ADD ( "CONTENT1" BLOB NULL  )';
execute immediate'UPDATE IBMS_TKSIGN_DATA SET "CONTENT1"="CONTENT"';
execute immediate'ALTER TABLE IBMS_TKSIGN_DATA DROP COLUMN content';
execute immediate'ALTER TABLE IBMS_TKSIGN_DATA RENAME COLUMN CONTENT1 TO CONTENT';
END IF;

-- 审批意见添加字段：审批意见模板code-----------------------------------------------
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_TASK_OPINION','VOTE_CODE') into result FROM dual;
IF(result=0)THEN
execute immediate'
ALTER TABLE "IBMS_TASK_OPINION"
ADD ( "VOTE_CODE" VARCHAR2(255) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_TASK_OPINION.VOTE_CODE IS ''审批意见模板code''';
END IF;

-- PageOffice模板管理添加状态、发送时间两个字段--2016-12-20----------------------
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_TEMPLATE','STATUS') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (STATUS  VARCHAR2(20 BYTE))';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.STATUS IS ''状态''';
END IF;
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_TEMPLATE','PUBLISHED_TIME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (PUBLISHED_TIME DATE)';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.PUBLISHED_TIME IS ''发布时间''';
END IF;




--表单设计中新增数据备份配置属性
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_DATA_TEMPLATE','ISBAKDATA') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_DATA_TEMPLATE ADD ( ISBAKDATA NUMBER(1) DEFAULT 0  NOT NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_DATA_TEMPLATE.ISBAKDATA IS ''修改表单数据时是否默认启动备份机制''';
END IF;


-- 新增表 业务数据备份表
select SELECTTABLECOUNT(owneruser,'IBMS_BUS_BAK') into result FROM dual;
IF(result=0)THEN
execute immediate '
CREATE TABLE ibms_bus_bak (
BUS_ID NUMBER NOT NULL,
BUS_PK NUMBER DEFAULT NULL NULL,
BAK_DATE DATE NOT NULL,
VERSION VARCHAR2(100) NOT NULL,
SWICH_DATA_BAK CLOB DEFAULT NULL NULL,
DATA_BAK CLOB DEFAULT NULL NULL,
TABLE_ID NUMBER NOT NULL,
PRIMARY KEY (BUS_ID) 
)';

execute immediate 'COMMENT ON TABLE IBMS_BUS_BAK IS ''业务数据备份表''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.BUS_ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.BUS_PK IS ''对应关联表主键''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.BAK_DATE IS ''备份时间''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.VERSION IS ''备份版本号''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.SWICH_DATA_BAK IS ''变化数据''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.DATA_BAK IS ''备份数据''';
execute immediate 'COMMENT ON COLUMN IBMS_BUS_BAK.TABLE_ID IS ''关联表ID''';

END IF;

-- 流程表单配置中新增配置项，指定驳回节点 配置属性
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_NODE_SET','BACK_NODE') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_SET ADD ( BACK_NODE VARCHAR2(100) NULL  )';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_SET.BACK_NODE IS ''驳回指定节点''';
END IF;

-- dbom 重构 添加字段 dbom_code
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','DBOM_CODE') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (DBOM_CODE VARCHAR2(100) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.DBOM_CODE IS ''dbom 编号''';
END IF;

-- dbom 重构 添加字段 SHOW_FILED
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','SHOW_FILED') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (SHOW_FILED VARCHAR2(500) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.SHOW_FILED IS ''节点显示列''';
END IF;

-- dbom 重构 添加字段 NODE_KEY
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','NODE_KEY') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (NODE_KEY VARCHAR2(500) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.NODE_KEY IS ''节点KEY值''';
END IF;

-- dbom 重构 添加字段 PNODE_KEY
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','PNODE_KEY') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (PNODE_KEY VARCHAR2(500) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.PNODE_KEY IS ''父节点关联KEY''';
END IF;

-- dbom 重构 添加字段 TARGET_DATA_SOURCE
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','TARGET_DATA_SOURCE') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (TARGET_DATA_SOURCE VARCHAR2(500) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.TARGET_DATA_SOURCE IS ''目标数据源''';
END IF;


-- dbom 重构 添加字段 TARGET_DATA_RELATION
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_DBOM_NODE','TARGET_DATA_RELATION') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_DBOM_NODE ADD (TARGET_DATA_RELATION VARCHAR2(500) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_DBOM_NODE.TARGET_DATA_RELATION IS ''目标数据源 与 节点数据源关联关系''';
END IF;






-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加 table_name
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_TEMPLATE','TABLE_NAME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (TABLE_NAME VARCHAR2(1000) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.TABLE_NAME IS ''数据源表名称''';
END IF;


-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加 views_name
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_TEMPLATE','VIEWS_NAME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (VIEWS_NAME VARCHAR2(1000) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.VIEWS_NAME IS ''数据源视图名称''';
END IF;


-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加  删除标志位 IS_DELETE
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_TEMPLATE','IS_DELETE') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_TEMPLATE ADD (IS_DELETE INT DEFAULT 0  )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_TEMPLATE.IS_DELETE IS ''1:已删除，0：未删除''';
END IF;


-- CWM_SYS_OFFICE_ITEMS表修改 增加 table_name
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_ITEMS','TABLE_NAME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_ITEMS ADD (TABLE_NAME varchar(1000) NULL  )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.TABLE_NAME IS ''数据源表名称''';
END IF;


-- CWM_SYS_OFFICE_ITEMS 表修改 增加 删除标志位 IS_DELETE
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_OFFICE_ITEMS','IS_DELETE') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_OFFICE_ITEMS ADD (IS_DELETE INT DEFAULT 0  )';
execute immediate 'COMMENT ON COLUMN CWM_SYS_OFFICE_ITEMS.IS_DELETE IS ''1:已删除，0：未删除''';
END IF;




-- 组织 表新增字段 组织分管领导
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_ORG','LEADER') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_ORG ADD (LEADER VARCHAR2(1000))';
execute immediate 'COMMENT ON COLUMN CWM_SYS_ORG.LEADER IS ''分管主领导''';
END IF;



-- 组织 表新增字段 组织分管副领导
select SELECTTABLEFIELDCOUNT(owneruser,'CWM_SYS_ORG','VICE_LEADER') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE CWM_SYS_ORG ADD (VICE_LEADER VARCHAR2(1000))';
execute immediate 'COMMENT ON COLUMN CWM_SYS_ORG.VICE_LEADER IS ''分管副领导''';
END IF;

-- 平台字段表新增字段，关联表名
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_FORM_FIELD','RELTABLENAME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (RELTABLENAME VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_FORM_FIELD.RELTABLENAME IS ''关联表name''';
END IF;


-- 新增字段 ibms_form_field 表中 判断是否需要加密内容
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_FORM_FIELD','ENCRYPT') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_FORM_FIELD ADD (ENCRYPT VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_FORM_FIELD.ENCRYPT IS ''加密算法''';
END IF;

-- 新增字段 USER_LABEL 设置自由跳转人员选择的描述信息
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_NODE_SET','USER_LABEL') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_SET ADD (USER_LABEL VARCHAR2(1000))';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_SET.USER_LABEL IS ''用户人员选择label提示信息配置''';
END IF;


-- 新增字段 USER_LABEL 设置自由跳转人员选择的描述信息
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_NODE_SET','JUMP_SETTING') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_SET ADD (JUMP_SETTING CLOB)';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_SET.JUMP_SETTING IS ''用户人员选择label提示信息配置''';
END IF;

-- 新增字段 PENDING_SETTING 代办已办模板配置
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_DEFINITION','PENDING_SETTING') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_DEFINITION ADD (PENDING_SETTING CLOB)';
execute immediate 'COMMENT ON COLUMN IBMS_DEFINITION.PENDING_SETTING IS ''代办已办模板配置''';
END IF;

-- 新增字段 IBMS_PRO_RUN ROOT_BUSINESSKEY 根流程的业务表主键值
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_PRO_RUN','ROOT_BUSINESSKEY') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_PRO_RUN ADD (ROOT_BUSINESSKEY VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_PRO_RUN.ROOT_BUSINESSKEY IS ''根流程的业务表主键值''';
END IF;


-- 新增字段 IBMS_PRO_RUN_HIS ROOT_BUSINESSKEY 根流程的业务表主键值
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_PRO_RUN_HIS','ROOT_BUSINESSKEY') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_PRO_RUN_HIS ADD (ROOT_BUSINESSKEY VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_PRO_RUN_HIS.ROOT_BUSINESSKEY IS ''根流程的业务表主键值''';
END IF;


-- 新增字段 ibms_definition key_path 根流程的业务表主键值
select SELECTTABLEFIELDCOUNT(owneruser,'ibms_definition','KEY_PATH') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE ibms_definition ADD (KEY_PATH VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN ibms_definition.KEY_PATH IS ''流程定义父子流程关系path''';
END IF;


-- 新增字段 IBMS_PRO_RUN IS_SUB 是否是内部子流程实例
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_PRO_RUN','IS_SUB') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_PRO_RUN ADD (IS_SUB INTEGER DEFAULT 0  NULL)';
execute immediate 'COMMENT ON COLUMN IBMS_PRO_RUN.IS_SUB IS ''是否是内部子流程实例''';
END IF;
-- 新增字段 IBMS_PRO_RUN_HIS IS_SUB 是否是内部子流程实例
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_PRO_RUN_HIS','IS_SUB') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_PRO_RUN_HIS ADD (IS_SUB INTEGER DEFAULT 0  NULL)';
execute immediate 'COMMENT ON COLUMN IBMS_PRO_RUN_HIS.IS_SUB IS ''是否是内部子流程实例''';
END IF;

-- 新增字段 IBMS_TASK_DUE warningSetJson 预警配置
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_TASK_DUE','warningSetJson') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_TASK_DUE ADD (warningSetJson VARCHAR2(1000))';
execute immediate 'COMMENT ON COLUMN IBMS_TASK_DUE.warningSetJson IS ''预警配置''';
END IF;

-- 新增字段 IBMS_TASK_DUE quarzCron 预警配置
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_TASK_DUE','quarzCron') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_TASK_DUE ADD (quarzCron VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_TASK_DUE.quarzCron IS ''催办消息发送的cron表达式''';
END IF;


-- 增加报表管理表-----------------------------------------------
SELECT COUNT(*) INTO new_tabs FROM tabs WHERE  TABLE_NAME=upper('ibms_definition_data') AND TABLESPACE_NAME=upper(owneruser);
IF(new_tabs=0)THEN
execute immediate '
CREATE TABLE IBMS_DEFINITION_DATA (
ID VARCHAR2(100) NOT NULL,
DEF_ID VARCHAR2(100) NOT NULL,
DEF_XML CLOB NOT NULL,
BPM_XML CLOB NOT NULL,
PRIMARY KEY (ID) 
)';

execute immediate 'COMMENT ON TABLE IBMS_DEFINITION_DATA IS ''流程定义XML数据''';
execute immediate 'COMMENT ON COLUMN IBMS_DEFINITION_DATA.DEF_ID IS ''ibms_definition id''';
execute immediate 'COMMENT ON COLUMN IBMS_DEFINITION_DATA.DEF_XML IS ''流程设计xml文件''';
execute immediate 'COMMENT ON COLUMN IBMS_DEFINITION_DATA.BPM_XML IS ''activiti 流程定义以及ibms 扩展 定义 xml 文件 ''';

END IF;


-- ibms_node_btn 表新增字段 action_name
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_NODE_BTN','ACTION_NAME') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_BTN ADD (ACTION_NAME VARCHAR2(100))';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_BTN.ACTION_NAME IS ''按钮 action name''';
END IF;



-- ibms_node_set 表新增字段 informConf
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_NODE_SET','INFORMCONF') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_NODE_SET ADD (INFORMCONF CLOB)';
execute immediate 'COMMENT ON COLUMN IBMS_NODE_SET.INFORMCONF IS ''消息配置''';
END IF;


-- IBMS_RUN_LOG 表新增字段 DETAIL
select SELECTTABLEFIELDCOUNT(owneruser,'IBMS_RUN_LOG','DETAIL') into result FROM dual;
IF(result=0)THEN
execute immediate 'ALTER TABLE IBMS_RUN_LOG ADD (DETAIL VARCHAR2(1000))';
execute immediate 'COMMENT ON COLUMN IBMS_RUN_LOG.DETAIL IS ''日志详情''';
END IF;



-- 事务回滚机制-----------------------------------------------
EXCEPTION WHEN others THEN
dbms_output.put_line('synstruct_zhangxg 执行错误');
rollback;

commit;
END SYNSTRUCT_ZHANGXG;
