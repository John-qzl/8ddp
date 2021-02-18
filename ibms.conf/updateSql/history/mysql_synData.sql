DROP PROCEDURE IF EXISTS prod_data;
CREATE PROCEDURE prod_data(spacename varchar(100))
BEGIN
DECLARE num NUMERIC;


DECLARE CONTINUE HANDLER FOR SQLEXCEPTION
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;
-- --------------do my sql------------------

SELECT COUNT(*) into num FROM CWM_SYS_GLTYPE WHERE TYPENAME = '密级' and NODEKEY = 'SECURITY' and CATKEY = 'DIC';
IF(num=0) THEN
	SELECT MAX(TYPEID)+1 INTO @nextval FROM CWM_SYS_GLTYPE;
	IF(@nextval IS NULL)THEN
		SET @nextval=1;
	END IF;
	INSERT INTO CWM_SYS_GLTYPE(TYPEID, TYPENAME, NODEPATH, DEPTH, PARENTID, NODEKEY, CATKEY, SN, USERID)
	SELECT @nextval ,'密级', CONCAT_WS('.','22',@nextval,''), 1, 22, 'SECURITY', 'DIC', 2, -1;


	SELECT TYPEID INTO @type_id  FROM CWM_SYS_GLTYPE WHERE TYPENAME = '密级' and NODEKEY = 'SECURITY' and CATKEY = 'DIC';
	SELECT MAX(DICID)+1 INTO @nextval FROM CWM_SYS_DIC;
	IF(@nextval IS NULL)THEN
		SET @nextval=1;
	END IF;
	IF(@type_id IS NOT NULL)THEN
		Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN, ITEMKEY) SELECT @nextval, @type_id, '内部', 'nb', '内部', 1, 0;
		Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN, ITEMKEY) SELECT @nextval+1, @type_id, '秘密', 'mm', '秘密', 2, 1;
		Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN, ITEMKEY) SELECT @nextval+2, @type_id, '机密', 'jm', '机密', 3, 2;
		Insert into CWM_SYS_DIC(DICID, TYPEID, ITEMNAME, ITEMVALUE, DESCP, SN, ITEMKEY) SELECT @nextval+3, @type_id, '绝密', 'jm', '绝密', 4, 3;
    END IF;
END IF;



-- --------------do my sql end--------------
COMMIT;
END;
CALL prod_data(@spacename);

