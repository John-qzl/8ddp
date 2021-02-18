DROP PROCEDURE IF EXISTS prod_struct_songchen;
CREATE PROCEDURE prod_struct_songchen(spacename varchar(100))
BEGIN
	
	-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;


END;
CALL prod_struct_songchen(@spacename);