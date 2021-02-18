DROP PROCEDURE IF EXISTS prod_data_xiechen;
CREATE PROCEDURE prod_data_xiechen(spacename varchar(100))
BEGIN
DECLARE countt NUMERIC;

-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;
-- --------------do my sql------------------

-- 2017年9月21日：列表数据预览
SELECT COUNT(*) into countt FROM ins_col_type WHERE KEY_ like 'linkListPreview';
IF(countt=0)THEN
	INSERT INTO `ins_col_type` VALUES (7002, '链接看板预览', 'linkListPreview', '/oa/portal/insPortal/getLinkListHtml.do', '无', 'URL', '', '', '', '', null, '-4', '2017-09-21 11:05:38', '-4', '0');	
END IF;
SELECT COUNT(*) into countt FROM ins_column WHERE KEY_ like 'linkListPreview';
IF(countt=0)THEN
	INSERT INTO `ins_column` VALUES (7002, 7002, '链接看板预览', 'linkListPreview', null, 'ENABLED', '20', 'YES', '链接看板预览', '0', '-4', null, '-4', '2017-09-21 10:33:01', '500');
END IF;
-- --------------do my sql end--------------
COMMIT;
END;


CALL prod_data_xiechen(@spacename);
