CREATE OR REPLACE PROCEDURE synData_songchen (owneruser IN VARCHAR DEFAULT 'ibms')
AS
resnum number;
theme_val VARCHAR(1000);
BEGIN

-- ----------------update cwm_sys_parameter 2017-07-29--------------------
BEGIN
	execute immediate 'ALTER TABLE CWM_SYS_PARAMETER MODIFY ( "DESCRIPTION" VARCHAR2(1000 BYTE) )';
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYS_THEME';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='主题，默认default。项目样式稳定后，可以copy一份default文件夹，改个名，如为zzczglqms,则该项目的主题为zzczglqms（株洲垂直管理qms）。该项目所有样式和图片都在zzczglqms文件夹下。' WHERE NAME='SYS_THEME';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYS_UITYPE';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='系统UI界面：0为旧风格，1为新风格，新平台不支持旧风格。' WHERE NAME='SYS_UITYPE';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYSTEM_TITLE_LOGO';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='首页顶部左侧图标，默认存放路径为 “webapp/styles/主题”路径，如此处填写/images/next.gif，则路径为“webapp/styles/主题/images/next.gif”。高度要求：44px。默认显示图片为webapp/styles/images/logo.png。' WHERE NAME='SYSTEM_TITLE_LOGO';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='COMPANY_NAME';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='公司名称，如奥蓝托软件公司。' WHERE NAME='COMPANY_NAME';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYSTEM_TITLE';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='系统名称，如综合管理系统。' WHERE NAME='SYSTEM_TITLE';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYS_LOGIN_PNG';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='登录页面背景图片。默认存放路径为 “webapp/styles/主题”路径。如此处填写/images/login/loginHZY2.jpg，则路径为“webapp/styles/主题/images/login/loginHZY2.jpg”。' WHERE NAME='SYS_LOGIN_PNG';
	END IF;
	
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE NAME='SYS_LOGIN_LOG';
	IF(resnum!=0) THEN
	UPDATE CWM_SYS_PARAMETER SET DESCRIPTION='登录页面标语，警示语图片。默认存放路径为 “webapp/styles/主题”路径。如此处填写/images/login/banner.png，则路径为“webapp/styles/主题/images/login/banner.png”。' WHERE NAME='SYS_LOGIN_LOG';
	END IF;
	
	-- ----------------配置solr服务是否开启  insert cwm_sys_parameter 2017-08-11----
	-- ----------------                         将是否启动配置为0或1 2017-08-17----
	-- -------     修改配置参数为0#0#doc,docx,pdf,ppt,pptx,xls,xlsx 2017-09-26----
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID=6001;
	IF(resnum=1)THEN
		DELETE FROM CWM_SYS_PARAMETER  WHERE ID=6001;
	END IF;
	SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID=6002;
	IF(resnum=0)THEN
		INSERT into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION,TYPE)values(6002,'SOLRSERVER',1,'0#0#[doc,docx,pdf,ppt,pptx,xls,xlsx,txt]','是否开启文件搜索#是否开启数据库搜索#[可搜索的文件类型]--0不开启，1开启，不需要的文件类型请删除','辅助参数');
	END IF;
	IF(resnum=1)THEN
		SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID=6002 AND DATATYPE='0';
		IF(resnum=1)THEN
			UPDATE CWM_SYS_PARAMETER SET DATATYPE='1',VALUE='0#0#[doc,docx,pdf,ppt,pptx,xls,xlsx,txt]',DESCRIPTION='是否开启文件搜索#是否开启数据库搜索#[可搜索的文件类型]--0不开启，1开启，不需要的文件类型请删除' WHERE ID=6002; 
		END IF;
	END IF;
	-- --------------
	-- ----------------配置界面样式  20170905
	SELECT COUNT(*) INTO resnum FROM CWM_SYS_PARAMETER WHERE ID=6003;
	IF(resnum=0)THEN
		theme_val:='[{"name":"theme_mainColor","value":"#3eaaf5","description":"主题颜色"},{"name":"theme_sideColor","value":"#3B485A","description":"边栏"},{"name":"theme_sideBackColor","value":"#293038","description":"边栏背景"},{"name":"theme_formColor","value":"#C70019","description":"form/table"},{"name":"theme_formManageColor","value":"#0378ca","description":"form管理字体颜色"},{"name":"theme_formManageBackColor","value":"#297dbd","description":"form管理背景颜色"},{"name":"theme_btnColor","value":"#4092D0","description":"tab/btn"},{"name":"theme_btnBorderColor","value":"#48A1E4","description":"btnBorder"}]';
		
		INSERT into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION,TYPE)values(6003,'THEME_COLOR',0,theme_val,'自定义页面颜色','基本参数');
	END IF;
END 
-- --------------do my sql end--------------
COMMIT;
END synData_songchen;