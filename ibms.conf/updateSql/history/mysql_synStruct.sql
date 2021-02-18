DROP PROCEDURE IF EXISTS prod_struct;
CREATE PROCEDURE prod_struct(spacename varchar(100))
BEGIN
DECLARE col varchar(100); 
DECLARE num NUMERIC; 
-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;	


-- ---开启事务 -----
START TRANSACTION;


-- --------------do my sql------------------

SELECT IS_NULLABLE into col FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_ORG' )AND COLUMN_NAME=upper('ISDELETE');
IF(col='YES')THEN
ALTER TABLE CWM_SYS_ORG ALTER COLUMN ISDELETE  drop default;
ALTER TABLE CWM_SYS_ORG ALTER COLUMN ISDELETE  set default 0;
END IF;

SELECT IS_NULLABLE into col FROM information_schema.`COLUMNS` WHERE  TABLE_SCHEMA=spacename AND TABLE_NAME=upper('CWM_SYS_JOB' )AND COLUMN_NAME=upper('ISDELETE');
IF(col='YES')THEN
ALTER TABLE CWM_SYS_JOB ALTER COLUMN ISDELETE  drop default;
ALTER TABLE CWM_SYS_JOB ALTER COLUMN ISDELETE  set default 0;
END IF;

-- 删除索引--act_ru_execution_ibfk_7
SELECT count(*) into num FROM information_schema.KEY_COLUMN_USAGE  
WHERE TABLE_NAME= UPPER('ACT_RU_EXECUTION') AND  CONSTRAINT_NAME=UPPER('act_ru_execution_ibfk_7') AND CONSTRAINT_SCHEMA=UPPER(spacename);
IF(num=1)THEN
alter table ACT_RU_EXECUTION drop foreign key   act_ru_execution_ibfk_7;
END IF;

-- 删除索引--act_ru_execution_ibfk_5
SELECT count(*) into num FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_NAME= UPPER('ACT_RU_EXECUTION') AND  CONSTRAINT_NAME=UPPER('act_ru_execution_ibfk_5') AND CONSTRAINT_SCHEMA=UPPER(spacename);
IF(num=1)THEN
alter table ACT_RU_EXECUTION drop foreign key   act_ru_execution_ibfk_5;
END IF;

-- 创建新的索引 ACT_FK_EXE_PARENT
SELECT count(*) into num FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_NAME= UPPER('ACT_RU_EXECUTION') AND  CONSTRAINT_NAME=UPPER('ACT_FK_EXE_PARENT')
AND CONSTRAINT_SCHEMA=UPPER(spacename);
IF(num=0)THEN
alter table ACT_RU_EXECUTION
add constraint ACT_FK_EXE_PARENT foreign key (PARENT_ID_)
references ACT_RU_EXECUTION (ID_) on delete cascade;
END IF;

-- 创建新的索引 ACT_FK_EXE_PROCINST
SELECT count(*) into num FROM information_schema.KEY_COLUMN_USAGE 
WHERE TABLE_NAME= UPPER('ACT_RU_EXECUTION') AND  CONSTRAINT_NAME=UPPER('ACT_FK_EXE_PROCINST')
AND CONSTRAINT_SCHEMA=UPPER(spacename);
IF(num=0)THEN
alter table ACT_RU_EXECUTION
add constraint ACT_FK_EXE_PROCINST foreign key (PROC_INST_ID_)
references ACT_RU_EXECUTION (ID_) on delete cascade;
END IF;







-- --------------do my sql end--------------


-- ---提交脚本-----
COMMIT;
END;
CALL prod_struct(@spacename);





-- 查询 索引是否存在通用function 
-- param schemaName 表空间
-- param tabName 表名
-- param indexName 索引名
DROP FUNCTION
IF EXISTS selectIndexCount;

CREATE FUNCTION selectIndexCount (
	schemaName VARCHAR (100),
	tabName VARCHAR (100),
	indexName VARCHAR (100)
) RETURNS INT (10)
BEGIN
DECLARE result INT (10);
SELECT
	count(*) INTO result
FROM
	information_schema.STATISTICS
WHERE
	INDEX_NAME = indexName
AND TABLE_NAME = tabName
AND TABLE_SCHEMA = schemaName;
RETURN result;
END;

-- 查询 表是否存在是否存在通用function 
-- param schemaName 表空间
-- param tabName 表名
DROP FUNCTION
IF EXISTS selectTableCount;

CREATE FUNCTION selectTableCount (
	schemaName VARCHAR (100),
	tabName VARCHAR (100)
) RETURNS INT (10)
BEGIN
DECLARE result INT (10);
SELECT
	COUNT(*) INTO result
FROM
	information_schema.`TABLES`
WHERE
	TABLE_SCHEMA = schemaName
AND TABLE_NAME = upper(tabName);
RETURN result;
END;


-- 查询 表字段是否存在是否存在通用function 
-- param schemaName 表空间
-- param tabName 表名
-- param columnName 列名
DROP FUNCTION
IF EXISTS selectTableFieldCount;
CREATE FUNCTION selectTableFieldCount (
	schemaName VARCHAR (100),
	tabName VARCHAR (100),
	columnName VARCHAR (100)
) RETURNS INT (10)
BEGIN
DECLARE result INT (10);
SELECT
	COUNT(*) INTO result
FROM
	information_schema.`COLUMNS`
WHERE
	TABLE_SCHEMA = schemaName
AND TABLE_NAME = upper(tabName)
AND COLUMN_NAME = upper(columnName);
RETURN result;
END;
;

