DROP PROCEDURE IF EXISTS prod_struct_zxg;
CREATE PROCEDURE prod_struct_zxg(spacename varchar(100))
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

-- 修改 表cwm_sys_file 字段  SECURITY 为SECURITY_
SELECT COUNT(*) INTO old_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('SECURITY');
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('SECURITY_');
IF(old_cols=1 AND new_cols=0)THEN
ALTER TABLE `cwm_sys_file`
CHANGE COLUMN `SECURITY` `SECURITY_`  varchar(50)  NULL DEFAULT NULL AFTER `FOLDERPATH`;
END IF;

-- 修改 表cwm_sys_file 字段  DESCRIBE DESCRIBE_
SELECT COUNT(*) INTO old_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('DESCRIBE');
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_file' )AND COLUMN_NAME=upper('DESCRIBE_');
IF(old_cols=1 AND new_cols=0)THEN
ALTER TABLE `cwm_sys_file`
CHANGE COLUMN `DESCRIBE` `DESCRIBE_`  text  NULL AFTER `SECURITY_`;
END IF;

-- 表单模板表增加子表排序信息字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_data_template' )AND COLUMN_NAME=upper('SUBSORTFIELD');
IF(new_cols=0)THEN
ALTER TABLE `ibms_data_template` ADD COLUMN `SUBSORTFIELD`  text NULL AFTER `SORTFIELD`;
END IF;


-- 表单模板表增加关联表排序信息字段
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_data_template' )AND COLUMN_NAME=upper('RELSORTFIELD');
IF(new_cols=0)THEN
ALTER TABLE `ibms_data_template` ADD COLUMN `RELSORTFIELD`  text NULL AFTER `SUBSORTFIELD`;
END IF;

-- 增加报表管理表
DROP TABLE IF EXISTS `ibms_form_reports`;
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_report_template');
IF(new_tabs=0)THEN
CREATE TABLE `cwm_sys_report_template` (
  `REPORTID` bigint(20) NOT NULL COMMENT '报表编号',
  `TITLE` varchar(128) NOT NULL COMMENT '标题',
  `DESCP` varchar(500) NOT NULL COMMENT '描述',
  `REPORTLOCATION` varchar(128) NOT NULL COMMENT '报表模块的cpt文件的路径',
  `CREATETIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATETIME` datetime NOT NULL COMMENT '修改时间',
  `REPORTTYPE` varchar(100) NOT NULL COMMENT '报表类型：finerreport pageoffice',
  `TYPEID` bigint(20) NOT NULL COMMENT '报表分类ID',
  PRIMARY KEY (`REPORTID`)
) ENGINE=InnoDB COMMENT='报表模板管理表';
END IF;
-- 增加报表模板标题唯一索引
ALTER TABLE `cwm_sys_report_template`
ADD UNIQUE INDEX `unique_report_template_title` (`TITLE`) ;

-- 增加报表参数管理表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_report_param');
IF(new_tabs=0)THEN
CREATE TABLE `cwm_sys_report_param` (
  `PARAMID` bigint(20) NOT NULL COMMENT '参数编号',
  `REPORTID` bigint(20) NOT NULL COMMENT '报表ID',
  `NAME` varchar(100) NOT NULL COMMENT '参数名',
  `VALUE_` varchar(100) NOT NULL COMMENT '参数值',
  `PARAMTYPE` varchar(100) NULL COMMENT '参数类型',
  `DESCP` varchar(500)  NULL COMMENT '描述',
  `CREATETIME` datetime NOT NULL COMMENT '创建时间',
  `UPDATETIME` datetime NULL COMMENT '修改时间',
  PRIMARY KEY (`PARAMID`)
) ENGINE=InnoDB COMMENT='报表参数管理表';
END IF;


-- 增加IBMS_NODE_BTN表字段PARAMSCRIPT
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_node_btn' )AND COLUMN_NAME=upper('PARAMSCRIPT');
IF(new_cols=0)THEN
ALTER TABLE `ibms_node_btn`
ADD COLUMN `PARAMSCRIPT`  text NULL COMMENT '参数脚本' AFTER `AFTERSCRIPT`;
END IF;






-- 创建office模板管理表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_OFFICE_TEMPLATE');
IF(new_tabs=0)THEN
CREATE TABLE `CWM_SYS_OFFICE_TEMPLATE` (
`OFFICEID` varchar(38) NOT NULL COMMENT '主键',
`TABLE_ID` varchar(200) NULL COMMENT '数据表ID',
`VIEWS_ID` varchar(38) NULL COMMENT '视图ID',
`CONTENT` longtext NULL COMMENT 'office标签',
`FILEPATH` varchar(200) NULL COMMENT '模板文件',
`TYPE_ID` bigint(20) NULL COMMENT '模板global分类',
`OFFICE_TYPE` varchar(100) NULL COMMENT '模板类型',
`TITLE` varchar(100) NULL COMMENT '模板标题',
`DATA_ENTRY` varchar(38) NULL COMMENT '主表入口',
`CREATE_USER` varchar(100) NULL,
`CREATE_TIME` date NULL,
PRIMARY KEY (`OFFICEID`) 
);
-- 增加报表模板标题唯一索引
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE`
ADD UNIQUE INDEX `unique_office_template_name` (`TITLE`) ;
END IF;

-- 创建office模板标签表
SELECT COUNT(*) INTO new_tabs FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_OFFICE_ITEMS');
IF(new_tabs=0)THEN
CREATE TABLE `CWM_SYS_OFFICE_ITEMS` (
`ID` varchar(38) NOT NULL COMMENT'标签ID',
`OFFICE_ID` varchar(38) NULL COMMENT'模板ID',
`TABLE_ID` varchar(38) NULL COMMENT'表ID',
`COLUMN_NAME` varchar(38) NULL COMMENT'列名',
`COLUMN_ID` varchar(38) NULL COMMENT'列ID',
`RELATIONS` varchar(100) NULL COMMENT'关系',
`TYPE` varchar(100) NULL COMMENT'类型',
PRIMARY KEY (`ID`) 
);
END IF;



-- dbom 表 code 字段新增unique key
IF(selectIndexCount(spacename,'cwm_dbom','unique_dbom_code')=0)THEN
ALTER TABLE `cwm_dbom`
ADD UNIQUE INDEX `unique_dbom_code` (`CODE`) ;
END IF;

-- 新增报表参数长度字段
IF(selectTableFieldCount(spacename,'cwm_sys_report_param','PARAM_SIZE')=0)THEN
ALTER TABLE `cwm_sys_report_param`
ADD COLUMN `PARAM_SIZE`  bigint(20) NULL COMMENT '参数长度' AFTER `PARAMTYPE`;
END IF;
-- --------------do my sql end--------------

-- 新增印章表
IF(selectTableCount(spacename,'CWM_SYS_SIGN_ITEM')=0)THEN
CREATE TABLE `cwm_sys_sign_item` (
`ID` decimal(20,0) NOT NULL,
`CODE` decimal(20,0) NOT NULL COMMENT '序列号',
`IMAGE` longtext NOT NULL COMMENT '签章字节流',
`SIGN_ID` decimal(20,0) NOT NULL COMMENT '印章模板ID',
`FORM_ID` decimal(20,0) NOT NULL COMMENT '表单ID',
`TEMPLATE_ID` decimal(20,0) NOT NULL COMMENT '表单模板ID',
`ACTIVITI_ID` decimal(20,0) NOT NULL COMMENT '流程实例ID',
`DATA` LONGTEXT NOT NULL COMMENT '原始数据',
`ENCRY_DATA` LONGTEXT COMMENT '加密数据',
`SIGN_DATA` varchar(255) DEFAULT NULL COMMENT '印章信息',
`POSITION` varchar(1000) COMMENT '印章坐标',
`PASSWORD` varchar(100) DEFAULT NULL COMMENT '印章密码',
PRIMARY KEY (`ID`)
);
END IF;

-- 新增印章模型表
IF(selectTableCount(spacename,'CWM_SYS_SIGN_MODEL')=0)THEN
CREATE TABLE `cwm_sys_sign_model` (
`ID` bigint(20) NOT NULL,
`IMG_PATH` text NOT NULL COMMENT '印模图片',
`USER_ID` bigint(20) NOT NULL COMMENT '所属用户ID',
`CODE` varchar(100) NOT NULL COMMENT '印章编号',
`ORG_ID` bigint(20) NOT NULL COMMENT '所属组织',
`NAME` varchar(100) NOT NULL COMMENT '印章名称',
`IS_DEFAULT` int(1) NOT NULL DEFAULT '0' COMMENT '是否为默认印章',
`DESC_` varchar(200) DEFAULT NULL COMMENT '印章描述',
`TYPE_` bigint(20) NOT NULL COMMENT '印章类型',
`START_DATE` date NOT NULL COMMENT '起始时间',
`END_DATE` date NOT NULL COMMENT '结束时间',
`STATUS` bigint(20) DEFAULT NULL COMMENT '状态',
`PASSW` varchar(255) NOT NULL COMMENT '印章密码',
`PATH_TYPE` INT(1) NOT NULL COMMENT '印章图片存储类型',
`VERSION` varchar(100) NOT NULL COMMENT '版本号',
PRIMARY KEY (`ID`)
);
END IF;



-- 添加流程审批意见 task_opsition表印章ID字段
IF(select selectTableFieldCount(spacename,'IBMS_TASK_OPINION','SIGN_ID')=0)THEN
ALTER TABLE `ibms_task_opinion`
ADD COLUMN `SIGN_ID`  varchar(1000) NULL COMMENT '签名Id' AFTER `SUPEREXECUTION`;
END IF;


-- 修改表CWM_SYS_SIGN_MODEL 组织可以为空
IF(select selectTableFieldCount(spacename,'CWM_SYS_SIGN_MODEL','ORG_ID')=1)THEN
ALTER TABLE `cwm_sys_sign_model`
MODIFY COLUMN `ORG_ID`  bigint(20) NULL COMMENT '所属组织' AFTER `CODE`;
END IF;


-- 常用语设置表增加字段流程节点key
IF(select selectTableFieldCount(spacename,'IBMS_APPROVAL_ITEM','DEFNODEKEY')=0)THEN
ALTER TABLE `IBMS_APPROVAL_ITEM`
ADD COLUMN `DEFNODEKEY`  varchar(255) NULL COMMENT '流程节点key' AFTER `TYPEID`;
END IF;


-- 常用语设置表增加字段code
IF(select selectTableFieldCount(spacename,'IBMS_APPROVAL_ITEM','CODE')=0)THEN
ALTER TABLE `IBMS_APPROVAL_ITEM`
ADD COLUMN `CODE`  varchar(255) NULL COMMENT '审批意见模板COD编号' AFTER `TYPEID`;
END IF;
-- 常用语设置表增加字段default_ 是否默认
IF(select selectTableFieldCount(spacename,'IBMS_APPROVAL_ITEM','DEFAULT_')=0)THEN
ALTER TABLE `IBMS_APPROVAL_ITEM`
ADD COLUMN `DEFAULT_`  INT NULL COMMENT '是否默认' AFTER `TYPEID`;
END IF;


-- 会签task 审批意见添加一个字段存储审批意见模板
IF(select selectTableFieldCount(spacename,'IBMS_TKSIGN_DATA','VOTE_CODE')=0)THEN
ALTER TABLE `IBMS_TKSIGN_DATA`
ADD COLUMN `VOTE_CODE`  varchar(255) NULL COMMENT '审批意见模板code' AFTER `CONTENT`;
END IF;

-- 会签task  content字段修改成TEXT
IF(select selectTableFieldCount(spacename,'IBMS_TKSIGN_DATA','CONTENT')=1)THEN
ALTER TABLE `IBMS_TKSIGN_DATA`
MODIFY COLUMN `CONTENT`  text COMMENT '投票意见内容';
END IF;

-- 审批意见添加字段：审批意见模板code
IF(select selectTableFieldCount(spacename,'IBMS_TASK_OPINION','VOTE_CODE')=0)THEN
ALTER TABLE `IBMS_TASK_OPINION`
ADD COLUMN `VOTE_CODE`  varchar(255) NULL COMMENT '审批意见模板code' AFTER `OPINION`;
END IF;

-- PageOffice模板管理添加状态、发送时间两个字段--2016-12-20----------------------
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','STATUS')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE`
ADD COLUMN `STATUS`  varchar(10) NULL COMMENT '状态';
END IF;
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','PUBLISHED_TIME')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE`
ADD COLUMN `PUBLISHED_TIME`  DATE NULL COMMENT '发布时间';
END IF;


-- 表单设计中新增数据备份配置属性
IF(select selectTableFieldCount(spacename,'ibms_data_template','ISBAKDATA')=0)THEN
ALTER TABLE `ibms_data_template`
ADD COLUMN `ISBAKDATA`  tinyint(1) NOT NULL DEFAULT 0 COMMENT '修改表单数据时是否默认启动备份机制' AFTER `ISFILTER`;
END IF;


-- 新增表 业务数据备份表
IF(selectTableCount(spacename,'ibms_bus_bak')=0)THEN
CREATE TABLE `ibms_bus_bak` (
  `BUS_ID` decimal(18,0) NOT NULL COMMENT '主键',
  `BUS_PK` decimal(18,0) DEFAULT NULL COMMENT '对应关联表主键',
  `BAK_DATE` date NOT NULL COMMENT '备份时间',
  `VERSION` varchar(100) NOT NULL COMMENT '备份版本号',
  `SWICH_DATA_BAK` text DEFAULT NULL COMMENT '变化数据',
  `DATA_BAK` text DEFAULT NULL COMMENT '备份数据',
  `TABLE_ID`  decimal(18,0) NOT NULL COMMENT '关联表ID',
  PRIMARY KEY (`BUS_ID`)
  
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='业务数据备份表';
END IF;

-- 流程表单配置中新增配置项，指定驳回节点 配置属性
IF(select selectTableFieldCount(spacename,'IBMS_NODE_SET','BACK_NODE')=0)THEN
ALTER TABLE `IBMS_NODE_SET`
ADD COLUMN `BACK_NODE`  varchar(100) NULL COMMENT '驳回节点配置' AFTER `OPINIONFIELD`;
END IF;


-- dbom 重构 添加字段dbom_code
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','DBOM_CODE')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` MODIFY COLUMN `DBOM_CODE`  varchar(100) NULL COMMENT 'dbom code编号' AFTER `NODE_TYPE`;
END IF;


-- dbom 重构 添加字段SHOW_FILED
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','SHOW_FILED')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` ADD COLUMN `SHOW_FILED`  varchar(1000) NULL COMMENT '节点显示列' AFTER `DATA_SOURCE`;
END IF;

-- dbom 重构 添加字段 NODE_KEY
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','NODE_KEY')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` ADD COLUMN `NODE_KEY`  varchar(100) NULL COMMENT '节点KEY值' AFTER `SHOW_FILED`;
END IF;

-- dbom 重构 添加字段 PNODE_KEY
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','PNODE_KEY')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` ADD COLUMN `PNODE_KEY`  varchar(100) NULL COMMENT '父节点关联KEY' AFTER `NODE_KEY`;
END IF;

-- dbom 重构 添加字段 TARGET_DATA_SOURCE
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','TARGET_DATA_SOURCE')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` ADD COLUMN `TARGET_DATA_SOURCE`  varchar(1000) NULL COMMENT '目标数据源' AFTER `PNODE_KEY`;
END IF;

-- dbom 重构 添加字段 TARGET_DATA_RELATION
IF(select selectTableFieldCount(spacename,'CWM_DBOM_NODE','TARGET_DATA_RELATION')=0)THEN
ALTER TABLE `CWM_DBOM_NODE` ADD COLUMN `TARGET_DATA_RELATION`  varchar(1000) NULL COMMENT '目标数据源 与 节点数据源关联关系' AFTER `TARGET_DATA_SOURCE`;
END IF;





-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加 table_name
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','TABLE_NAME')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE` ADD COLUMN `TABLE_NAME`  varchar(1000) NULL COMMENT '数据源表名称' AFTER `TABLE_ID`;
END IF;


-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加 views_name
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','VIEWS_NAME')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE` ADD COLUMN `VIEWS_NAME`  varchar(1000) NULL COMMENT '数据源视图名称' AFTER `VIEWS_ID`;
END IF;

-- CWM_SYS_OFFICE_TEMPLATE 表修改 增加  删除标志位 IS_DELETE
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','IS_DELETE')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_TEMPLATE`
ADD COLUMN `IS_DELETE`  int(1) NULL DEFAULT 0 COMMENT '1:已删除，0：未删除' AFTER `STATUS`;
END IF;


-- CWM_SYS_OFFICE_ITEMS表修改 增加 table_name
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_ITEMS','TABLE_NAME')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_ITEMS` ADD COLUMN `TABLE_NAME`  varchar(1000) NULL COMMENT '数据源表名称' AFTER `TABLE_ID`;
END IF;
-- CWM_SYS_OFFICE_ITEMS 表修改 增加 删除标志位 IS_DELETE
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_ITEMS','IS_DELETE')=0)THEN
ALTER TABLE `CWM_SYS_OFFICE_ITEMS`
ADD COLUMN `IS_DELETE`  int(1) NULL DEFAULT 0 COMMENT '1:已删除，0：未删除' AFTER `TYPE`;
END IF;

-- 给 table name 赋值
UPDATE CWM_SYS_OFFICE_ITEMS office,ibms_form_table tab SET office.TABLE_NAME=tab.TABLENAME WHERE office.TABLE_ID=tab.TABLEID and TABLE_NAME IS NULL;

-- 重建索引 删除需要逻辑删除，不能建uniquen索引
ALTER TABLE `cwm_sys_office_template`
DROP INDEX `unique_office_template_name` ,
ADD INDEX `unique_office_template_name` (`TITLE`) USING BTREE ;


-- 组织 表新增字段 组织分管领导
IF(select selectTableFieldCount(spacename,'CWM_SYS_ORG','LEADER')=0)THEN
ALTER TABLE `CWM_SYS_ORG`
ADD COLUMN `LEADER`  varchar(1000) NULL COMMENT '分管领导' AFTER `ORGSUPID`;
END IF;

-- 组织 表新增字段 组织分管副领导
IF(select selectTableFieldCount(spacename,'CWM_SYS_ORG','VICE_LEADER')=0)THEN
ALTER TABLE `CWM_SYS_ORG`
ADD COLUMN `VICE_LEADER`  varchar(1000) NULL COMMENT '分管副领导' AFTER `LEADER`;
END IF;

-- 平台字段表新增字段，关联表名
IF(select selectTableFieldCount(spacename,'IBMS_FORM_FIELD','RELTABLENAME')=0)THEN
ALTER TABLE `IBMS_FORM_FIELD`
ADD COLUMN `RELTABLENAME`  varchar(100) NULL COMMENT '关联表name' AFTER `RELTABLEID`;
END IF;


-- 新增字段 ibms_form_field 表中 判断是否需要加密内容
IF(select selectTableFieldCount(spacename,'IBMS_FORM_FIELD','ENCRYPT')=0)THEN
ALTER TABLE `IBMS_FORM_FIELD`
ADD COLUMN `ENCRYPT`  varchar(100) NULL COMMENT '加密算法' AFTER `RELTABLENAME`;
END IF;


-- 新增字段 USER_LABEL 设置自由跳转人员选择的描述信息
IF(select selectTableFieldCount(spacename,'IBMS_NODE_SET','USER_LABEL')=0)THEN
ALTER TABLE `IBMS_NODE_SET`
ADD COLUMN `USER_LABEL`  varchar(1000) NULL COMMENT '用户人员选择label提示信息配置' AFTER `BACK_NODE`;
END IF;

-- 新增字段 JUMP_SETTING 设置自由跳转人员选择的描述信息
IF(select selectTableFieldCount(spacename,'IBMS_NODE_SET','JUMP_SETTING')=0)THEN
ALTER TABLE `IBMS_NODE_SET`
ADD COLUMN `JUMP_SETTING`  TEXT NULL COMMENT '自定义跳转设置' AFTER `USER_LABEL`;
END IF;

-- 新增字段 PENDING_SETTING 代办已办模板配置
IF(select selectTableFieldCount(spacename,'IBMS_DEFINITION','PENDING_SETTING')=0)THEN
ALTER TABLE `IBMS_DEFINITION`
ADD COLUMN `PENDING_SETTING`  TEXT NULL COMMENT '代办已办模板配置' AFTER `SKIPSETTING`;
END IF;


-- 新增字段 IBMS_PRO_RUN ROOT_BUSINESSKEY 根流程的业务表主键值
IF(select selectTableFieldCount(spacename,'IBMS_PRO_RUN','ROOT_BUSINESSKEY')=0)THEN
ALTER TABLE `IBMS_PRO_RUN`
ADD COLUMN `ROOT_BUSINESSKEY`  varchar(100) COMMENT '根流程的业务表主键值' AFTER `BUSINESSKEY`;
END IF;

-- 新增字段 IBMS_PRO_RUN_HIS ROOT_BUSINESSKEY 根流程的业务表主键值
IF(select selectTableFieldCount(spacename,'IBMS_PRO_RUN_HIS','ROOT_BUSINESSKEY')=0)THEN
ALTER TABLE `IBMS_PRO_RUN_HIS`
ADD COLUMN `ROOT_BUSINESSKEY`  varchar(100) COMMENT '根流程的业务表主键值' AFTER `BUSINESSKEY`;
END IF;

-- 新增字段 ibms_definition key_path 根流程的业务表主键值
IF(select selectTableFieldCount(spacename,'ibms_definition','KEY_PATH')=0)THEN
ALTER TABLE `ibms_definition`
ADD COLUMN `KEY_PATH`  varchar(100) COMMENT '流程定义父子流程关系path' AFTER `DEFKEY`;
END IF;


-- 新增字段 IBMS_PRO_RUN IS_SUB 是否是内部子流程实例
IF(select selectTableFieldCount(spacename,'IBMS_PRO_RUN','IS_SUB')=0)THEN
ALTER TABLE `IBMS_PRO_RUN`
ADD COLUMN `IS_SUB`  int(1) NULL DEFAULT 0 COMMENT '是否是内部子流程实例' AFTER `STATUS`;
END IF;
-- 新增字段 IBMS_PRO_RUN_HIS IS_SUB 是否是内部子流程实例
IF(select selectTableFieldCount(spacename,'IBMS_PRO_RUN_HIS','IS_SUB')=0)THEN
ALTER TABLE `IBMS_PRO_RUN_HIS`
ADD COLUMN `IS_SUB`  int(1) NULL DEFAULT 0 COMMENT '是否是内部子流程实例' AFTER `STATUS`;
END IF;


-- 新增字段 IBMS_TASK_DUE warningSetJson 预警配置
IF(select selectTableFieldCount(spacename,'IBMS_TASK_DUE','warningSetJson')=0)THEN
ALTER TABLE `IBMS_TASK_DUE`
ADD COLUMN `warningSetJson`  varchar(1000) COMMENT '预警配置' AFTER `ASSIGNERNAME`;
END IF;

-- 新增字段 IBMS_TASK_DUE quarzCron 预警配置
IF(select selectTableFieldCount(spacename,'IBMS_TASK_DUE','quarzCron')=0)THEN
ALTER TABLE `IBMS_TASK_DUE`
ADD COLUMN `quarzCron`  varchar(100) COMMENT '催办消息发送的cron表达式' AFTER `warningSetJson`;
END IF;

ALTER TABLE `ins_col_type`
MODIFY COLUMN `URL_`  varchar(100)  NULL COMMENT '栏目映射URL' AFTER `KEY_`;


-- 新增表 流程定义data 表
IF(selectTableCount(spacename,'ibms_definition_data')=0)THEN
CREATE TABLE `ibms_definition_data` (
`ID`  varchar(255) NOT NULL ,
`DEF_ID`  varchar(255) NOT NULL COMMENT 'ibms_definition id',
`DEF_XML`  text NOT NULL COMMENT '流程设计xml文件' ,
`BPM_XML`  text NOT NULL COMMENT 'activiti 流程定义以及ibms 扩展 定义 xml 文件 ' ,
PRIMARY KEY (`ID`)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='流程定义XML数据';
END IF;


-- ibms_node_btn 表新增字段 action_name
IF(select selectTableFieldCount(spacename,'ibms_node_btn','action_name')=0)THEN
ALTER TABLE `ibms_node_btn`
ADD COLUMN `action_name`  varchar(100) COMMENT '按钮 action name' AFTER `BTNNAME`;
END IF;

-- ibms_node_set 表新增字段 informConf
IF(select selectTableFieldCount(spacename,'ibms_node_set','INFORMCONF')=0)THEN
ALTER TABLE `ibms_node_set`
ADD COLUMN `INFORMCONF`  text NULL COMMENT '消息配置' AFTER `INFORMTYPE`;
END IF;

-- IBMS_RUN_LOG 表新增字段 DETAIL
IF(select selectTableFieldCount(spacename,'IBMS_RUN_LOG','DETAIL')=0)THEN
ALTER TABLE `IBMS_RUN_LOG`
ADD COLUMN `DETAIL`  varchar(1000) NULL COMMENT '日志详情' AFTER `MEMO`;
END IF;

-- ---提交脚本-----
COMMIT;
END;


CALL prod_struct_zxg(@spacename);

