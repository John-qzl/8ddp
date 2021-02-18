/**************************************************
1.查询数据库中是否有该表：select count(*) into countt  from tabs where table_name = upper('CWM_LOGIN_LOG');
2.查询数据库中是否有该字段：  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='SIGN_PIC';
3.查询数据库中是否有该序列号：  select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_CWM_LOGIN_LOG'; 
4.加备注：execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_COLUMN IS ''首页布局''';
 ----------------------赋权限-----------------------
--如无建表权限   execute immediate 'grant create table to ibms';
--如无创建存储权限   execute immediate 'grant create procedure to ibms';
--如无创建序列号权限  execute immediate 'grant create sequence to ibms';

脚本参考oracle_synStruct.sql写。

如因为脚本执行顺序造成的执行错误问题，如先修改表，后定义该表的错误，
可将定义表的sql移到oracle_synStruct.sql中。
**************************************************/

CREATE OR REPLACE PROCEDURE synstruct_hhj (owneruser IN VARCHAR DEFAULT 'ibms')
AS
oldcols number;
newcols number;
new_tabs number;
result number;
nullable_ varchar2(4);
BEGIN
 ------------------------start plsql-----------------------------------------------
 -- 表单树结构对话框
SELECT COUNT(*) INTO new_tabs FROM tabs WHERE  TABLE_NAME=upper('ibms_form_def_tree') AND TABLESPACE_NAME=upper(owneruser);
IF(new_tabs=0)THEN
execute immediate '
CREATE TABLE ibms_form_def_tree (
  ID NUMBER(18) NOT NULL,
  NAME varchar(100) DEFAULT NULL,
  ALIAS varchar(50) DEFAULT NULL,
  TREE_ID varchar(100) DEFAULT NULL,
  PARENT_ID varchar(100) DEFAULT NULL,
  DISPLAY_FIELD varchar(100) DEFAULT NULL,
  LOAD_TYPE NUMBER(6) DEFAULT NULL,
  ROOT_ID varchar(255) DEFAULT NULL,
  FORM_KEY NUMBER(18) DEFAULT NULL,
  PRIMARY KEY (ID) 
)';

execute immediate 'COMMENT ON TABLE  ibms_form_def_tree IS ''表单定义处，表单树结构对话框''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.ID IS ''主键''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.NAME IS ''名称''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.ALIAS IS ''别名''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.TREE_ID IS ''树主键字段''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.PARENT_ID IS ''树父主键字段''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.DISPLAY_FIELD IS ''显示字段''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.LOAD_TYPE IS ''加载方式''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.ROOT_ID IS ''根节点id''';
execute immediate 'COMMENT ON COLUMN ibms_form_def_tree.FORM_KEY IS ''表单设计id''';

END IF;
-- 事务回滚机制
EXCEPTION WHEN others THEN
dbms_output.put_line('synstruct_hhj 执行错误');
rollback;
 commit;
 
END synstruct_hhj;
