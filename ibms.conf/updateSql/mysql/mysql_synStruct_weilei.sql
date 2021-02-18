DROP PROCEDURE IF EXISTS prod_struct_weilei;
CREATE PROCEDURE prod_struct_weilei(spacename varchar(100))
BEGIN
DECLARE countt NUMERIC;

-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;

-- ---------------------------------------PageOffice模板管理添加状态、发送时间两个字段--2016-12-20----------------------
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','STATUS')=0)THEN
	ALTER TABLE `cwm_sys_office_template` 
	ADD COLUMN `STATUS` varchar(20) NULL COMMENT '状态' AFTER `CREATE_TIME`;
END IF;
IF(select selectTableFieldCount(spacename,'CWM_SYS_OFFICE_TEMPLATE','PUBLISHED_TIME')=0)THEN
	ALTER TABLE `cwm_sys_office_template` 
	ADD COLUMN `PUBLISHED_TIME` date NULL COMMENT '发布时间' AFTER `STATUS`;
END IF;

-- ---------------------------------------ibms_tksign_data表中投票意见内容字段长度调整--2017-1-8----------------------
IF(select selectTableFieldCount(spacename,'ibms_tksign_data','ISFLOW')!=0)THEN
	ALTER TABLE  `ibms_tksign_data` 
	MODIFY COLUMN `CONTENT`  varchar(2000) CHARACTER SET utf8 COLLATE utf8_general_ci NULL DEFAULT NULL COMMENT '投票意见内容' AFTER `ISAGREE`;

END IF;

-- ---------------------------------------ibms_opinion添加备注字段--2017-2-27----------------------
IF(select selectTableFieldCount(spacename,'ibms_task_opinion','MEMO')=0)THEN
	ALTER TABLE `ibms_task_opinion` 
	ADD COLUMN `MEMO` varchar(255) NULL COMMENT '状态' AFTER `SIGN_ID`;
END IF;


COMMIT;
END;
CALL prod_struct_weilei(@spacename);

