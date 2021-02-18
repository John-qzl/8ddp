
CREATE OR REPLACE PROCEDURE synData_xiechen (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN
--------------------------1.链接面板------2017-9-25----------------------------------------------- 
BEGIN
		  -- 2017年9月25日：链接看板预览
    SELECT COUNT(*) into countt FROM ins_col_type WHERE KEY_ like 'linkListPreview';
	IF(countt=0) THEN  
		INSERT INTO ins_col_type VALUES (7002, '链接看板预览', 'linkListPreview', '/oa/portal/insPortal/getLinkListHtml.do', '无', 'URL', '', '', '', '', null, '-4', '', '-4', '0');
    	commit;
    END IF;
    SELECT COUNT(*) into countt FROM ins_column WHERE KEY_ like 'linkListPreview';
	IF(countt=0) THEN  
		INSERT INTO ins_column VALUES (7002, 7002, '链接看板预览', 'linkListPreview', null, 'ENABLED', '20', 'YES', '链接看板预览', '0', '-4', null, '-4', '', '500');
    	commit;
    END IF;
END
COMMIT;
END synData_xiechen;