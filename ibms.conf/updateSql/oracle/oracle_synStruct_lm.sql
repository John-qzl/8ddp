CREATE OR REPLACE PROCEDURE synstruct_lm (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN

	-------------------------------------1.首页布局----------------------------
	BEGIN
		select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('ins_portal') AND COLUMN_NAME ='LAYOUT_INFO_' AND OWNER=upper(owneruser);
			IF(countt=0) THEN
			 	 execute immediate 'ALTER TABLE  ins_portal  ADD (LAYOUT_INFO_  VARCHAR2(4000))';  
			     execute immediate 'COMMENT ON COLUMN ins_portal.LAYOUT_INFO_ IS '' 首页布局排版'' ';
		commit;
		END IF;
	END;

COMMIT;
END synstruct_lm;

















