DROP PROCEDURE IF EXISTS prod_struct_xiechen;
CREATE PROCEDURE prod_struct_xiechen(spacename varchar(100))
BEGIN
	DECLARE new_cols NUMERIC;
-- ----定义异常处理HANDLER--------
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ----开启事物-------
START TRANSACTION;


-- --------------do my sql------------------

-- 1.修改cwm_sys_user表，添加技术职称属性
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('SKILLTITLE');
IF(new_cols=0)THEN
ALTER TABLE cwm_sys_user  ADD  SKILLTITLE varchar(256) COMMENT '技术职称';
END IF;

-- 2.修改cwm_sys_user表，添加专业属性
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('cwm_sys_user' )AND COLUMN_NAME=upper('MAJOR');
IF(new_cols=0)THEN
	ALTER TABLE cwm_sys_user  ADD  MAJOR varchar(256) COMMENT '专业';
END IF;

-- --------------do my sql end--------------
COMMIT;
END;
CALL prod_struct_xiechen(@spacename);