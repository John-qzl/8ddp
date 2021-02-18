DROP PROCEDURE IF EXISTS prod_struct_lm;
CREATE PROCEDURE prod_struct_lm(spacename varchar(100))
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

-- 首页portal
SELECT COUNT(*) INTO new_cols FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ins_portal' )AND COLUMN_NAME=upper('LAYOUT_INFO_');
IF(new_cols=0)THEN
	ALTER TABLE ins_portal  ADD  LAYOUT_INFO_ text COMMENT '首页布局排版';
END IF;

COMMIT;
END;
CALL prod_struct_lm(@spacename);
