CREATE OR REPLACE PROCEDURE synstruct_xiechen (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
BEGIN
-------------------------------------系统表添加字段----------2017-06-08 by xiechen------------------------------------------
-----------------------------------------1.cwm_sys_user表-新增SKILLTITLE字段---------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('SKILLTITLE');
  IF(countt=0) THEN 
    execute immediate ' ALTER TABLE  cwm_sys_user ADD  SKILLTITLE  VARCHAR2(100 BYTE) ';
    execute immediate ' COMMENT ON COLUMN cwm_sys_user.SKILLTITLE IS ''技术职称'' ';
    commit;
   END IF;
END;
-------------------------------------2.cwm_sys_user表-新增MAJOR字段---------------------------
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('cwm_sys_user') AND COLUMN_NAME =upper('MAJOR');
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE  cwm_sys_user   ADD  MAJOR  VARCHAR2(100 BYTE)';
    execute immediate ' COMMENT ON COLUMN cwm_sys_user.MAJOR IS '' 专业'' ';
    commit;
END IF;
END;


-------------------------------------3.修改CWM_SYS_QUERY_SQL表的URL_PARAMS字段类型-------2017.6.9xiechen---------------------
BEGIN
	select count(*) into countt from all_tab_columns where table_name = upper('CWM_SYS_QUERY_SQL') AND column_name = 'URL_PARAMS' AND DATA_TYPE='VARCHAR2' AND DATA_LENGTH=4000;
    IF(countt=0) THEN
		execute immediate 'ALTER TABLE CWM_SYS_QUERY_SQL MODIFY("URL_PARAMS" VARCHAR2(4000 BYTE))';
		commit;
	END IF;
END;

COMMIT;
END synstruct_xiechen;

