-- -----------数据库方法：通过别名获取子节点数据---by liubo 2017-8-18 ------------------------- --
DROP FUNCTION IF EXISTS getChildFunIdByCode;

CREATE FUNCTION getChildFunIdByCode(_code VARCHAR(100))
 RETURNS varchar(4000)
BEGIN
	DECLARE sChildList VARCHAR(4000); 
	DECLARE sChildTemp VARCHAR(4000); 
	SELECT RESID into sChildTemp FROM CWM_SYS_RES WHERE ALIAS=_code;
	WHILE sChildTemp is not null DO 
	IF (sChildList is not null) THEN 
	SET sChildList = concat(sChildList,',',sChildTemp); 
	ELSE 
	SET sChildList = concat(sChildTemp); 
	END IF; 
	SELECT group_concat(RESID) INTO sChildTemp FROM CWM_SYS_RES where FIND_IN_SET(PARENTID,sChildTemp)>0; 
	END WHILE; 
	
	IF (sChildList is not null) THEN 
	RETURN sChildList; 
	ELSE
	RETURN 'null';
	END IF;
END;
