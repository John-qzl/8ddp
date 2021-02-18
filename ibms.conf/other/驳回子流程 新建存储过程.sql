-- oracle 版本
CREATE OR REPLACE 
PROCEDURE PRO_BACKTOSUBFLOW (oldinstId IN VARCHAR,newinstId IN VARCHAR)
AS
BEGIN
execute immediate 'SET FOREIGN_KEY_CHECKS=0';



INSERT INTO act_ru_execution(ID_,
REV_,
PROC_INST_ID_,
BUSINESS_KEY_,
PARENT_ID_,
PROC_DEF_ID_,
SUPER_EXEC_,
ACT_ID_,
IS_ACTIVE_,
IS_CONCURRENT_,
IS_SCOPE_,
IS_EVENT_SCOPE_,
SUSPENSION_STATE_,
CACHED_ENT_STATE_,
TENANT_ID_
) SELECT oldinstId,REV_,
oldinstId,
BUSINESS_KEY_,
PARENT_ID_,
PROC_DEF_ID_,
SUPER_EXEC_,
ACT_ID_,
IS_ACTIVE_,
IS_CONCURRENT_,
IS_SCOPE_,
IS_EVENT_SCOPE_,
SUSPENSION_STATE_,
CACHED_ENT_STATE_,
TENANT_ID_ FROM
act_ru_execution WHERE ID_=newinstId;
-- ScopeExecution[220000000111084]
-- ProcessInstance[newinstId]
-- Task[id=220000000111030, name=用户任务1]

-- 修改 task 流程实例ID 为  oldinstId
UPDATE act_ru_task SET EXECUTION_ID_=oldinstId ,PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;

-- 修改 原有的流程 运行变量 
UPDATE act_ru_variable SET EXECUTION_ID_=oldinstId,PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;
-- 删除 act_hi_procinst
DELETE FROM act_hi_procinst WHERE ID_=newinstId;
-- 删除 act_hi_varinst
DELETE FROM act_hi_varinst WHERE PROC_INST_ID_=newinstId;

-- 修改 act_ru_identitylink
UPDATE act_ru_identitylink SET PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;

-- 删除 act_hi_taskinst
DELETE FROM act_hi_taskinst WHERE PROC_INST_ID_=newinstId;

-- 删除 act_hi_identitylink
DELETE FROM act_hi_identitylink WHERE PROC_INST_ID_=newinstId;

-- 修改流程实例ID为原来已有的实例ID
DELETE FROM act_ru_execution  WHERE ID_=newinstId; 
-- UPDATE act_ru_execution SET ID_=220000000240074,PROC_INST_ID_=220000000240074 WHERE ID_=220000000250071;
-- 删除 ibms_pro_run
DELETE  FROM ibms_pro_run_his WHERE ACTINSTID=newinstId;
UPDATE ibms_pro_run_his SET status=1 WHERE ACTINSTID=oldinstId;
UPDATE ibms_pro_run SET ACTINSTID=oldinstId,status=1 WHERE ACTINSTID=newinstId;
UPDATE  ibms_task_opinion SET ACTINSTID=oldinstId WHERE ACTINSTID=newinstId;


execute immediate 'SET FOREIGN_KEY_CHECKS=1';


commit;
-- 事务回滚机制-----------------------------------------------
EXCEPTION WHEN others THEN
dbms_output.put_line('PRO_BACKTOSUBFLOW 执行错误');
rollback;
END;





-- mysql 版本

-- ----------------------------
-- Procedure structure for PRO_BACKTOSUBFLOW
-- ----------------------------
DROP PROCEDURE IF EXISTS `PRO_BACKTOSUBFLOW`;
DELIMITER ;;
CREATE DEFINER=`root`@`%` PROCEDURE `PRO_BACKTOSUBFLOW`(oldinstId varchar(100),newinstId varchar(100))
BEGIN
-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;

INSERT INTO act_ru_execution(ID_,
REV_,
PROC_INST_ID_,
BUSINESS_KEY_,
PARENT_ID_,
PROC_DEF_ID_,
SUPER_EXEC_,
ACT_ID_,
IS_ACTIVE_,
IS_CONCURRENT_,
IS_SCOPE_,
IS_EVENT_SCOPE_,
SUSPENSION_STATE_,
CACHED_ENT_STATE_,
TENANT_ID_
) SELECT oldinstId,REV_,
oldinstId,
BUSINESS_KEY_,
PARENT_ID_,
PROC_DEF_ID_,
SUPER_EXEC_,
ACT_ID_,
IS_ACTIVE_,
IS_CONCURRENT_,
IS_SCOPE_,
IS_EVENT_SCOPE_,
SUSPENSION_STATE_,
CACHED_ENT_STATE_,
TENANT_ID_ FROM
act_ru_execution WHERE ID_=newinstId;

-- ScopeExecution[220000000111084]
-- ProcessInstance[newinstId]
-- Task[id=220000000111030, name=用户任务1]

-- 修改 task 流程实例ID 为  oldinstId
UPDATE act_ru_task SET EXECUTION_ID_=oldinstId ,PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;

-- 修改 原有的流程 运行变量 
UPDATE act_ru_variable SET EXECUTION_ID_=oldinstId,PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;
-- 删除 act_hi_procinst
DELETE FROM act_hi_procinst WHERE ID_=newinstId;
-- 删除 act_hi_varinst
DELETE FROM act_hi_varinst WHERE PROC_INST_ID_=newinstId;

-- 修改 act_ru_identitylink
UPDATE act_ru_identitylink SET PROC_INST_ID_=oldinstId WHERE PROC_INST_ID_=newinstId;

-- 删除 act_hi_taskinst
DELETE FROM act_hi_taskinst WHERE PROC_INST_ID_=newinstId;

-- 删除 act_hi_identitylink
DELETE FROM act_hi_identitylink WHERE PROC_INST_ID_=newinstId;

-- 修改流程实例ID为原来已有的实例ID

DELETE FROM act_ru_execution  WHERE ID_=newinstId; 
-- UPDATE act_ru_execution SET ID_=220000000240074,PROC_INST_ID_=220000000240074 WHERE ID_=220000000250071;

-- 删除 ibms_pro_run
DELETE  FROM ibms_pro_run_his WHERE ACTINSTID=newinstId;
UPDATE ibms_pro_run_his SET status=1 WHERE ACTINSTID=oldinstId;
UPDATE ibms_pro_run SET ACTINSTID=oldinstId,status=1 WHERE ACTINSTID=newinstId;



UPDATE  ibms_task_opinion SET ACTINSTID=oldinstId WHERE ACTINSTID=newinstId;



-- ---提交脚本-----
COMMIT;

END
;;
DELIMITER ;