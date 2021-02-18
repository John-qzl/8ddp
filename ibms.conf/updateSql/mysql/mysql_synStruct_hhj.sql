DROP PROCEDURE IF EXISTS prod_struct_hhj;
CREATE PROCEDURE prod_struct_hhj(spacename varchar(100))
BEGIN
DECLARE cols   NUMERIC;
DECLARE countt NUMERIC;
-- ---定义异常处理HANDLER-----
DECLARE CONTINUE HANDLER FOR SQLEXCEPTION 
BEGIN 
ROLLBACK;
END;
-- ---开启事务 -----
START TRANSACTION;
-- --------------do my sql------------------
SELECT COUNT(*) INTO countt FROM information_schema.`TABLES` WHERE TABLE_SCHEMA=spacename AND TABLE_NAME=upper('ibms_form_def_tree');
IF(countt=0)THEN
CREATE TABLE `ibms_form_def_tree` (
`ID`  bigint(18) NOT NULL ,
`NAME`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '名称' ,
`ALIAS`  varchar(50) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '别名' ,
`TREE_ID`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '树主键字段' ,
`PARENT_ID`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '树父主键字段' ,
`DISPLAY_FIELD`  varchar(100) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '显示字段' ,
`LOAD_TYPE`  bigint(6) NULL DEFAULT NULL COMMENT '加载方式' ,
`ROOT_ID`  varchar(255) CHARACTER SET utf8 COLLATE utf8_unicode_ci NULL DEFAULT NULL COMMENT '根节点id' ,
`FORM_KEY`  bigint(18) NULL DEFAULT NULL COMMENT '表单设计id' ,
PRIMARY KEY (`ID`)
)
ENGINE=InnoDB DEFAULT CHARACTER SET=utf8 COLLATE=utf8_unicode_ci COMMENT='表单定义处，表单树结构对话框' ROW_FORMAT=COMPACT;

END IF;
-- --------------do my sql end--------------
COMMIT;
END;
CALL prod_struct_hhj(@spacename);

