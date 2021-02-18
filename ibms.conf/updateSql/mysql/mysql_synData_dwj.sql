DROP PROCEDURE IF EXISTS prod_data_dwj;
CREATE PROCEDURE prod_data_dwj(spacename varchar(100))
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

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmqxgl';
IF(countt=0) THEN
	-- 项目权限管理--
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5001','项目权限管理','xmqxgl',1,'security',3,'',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'xmqxgl';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
END IF;

 SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmgndlb';
IF(countt=0) THEN
	-- 项目功能点类别--
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5002','项目功能点类别','xmgndlb',1,'edit',5001,'/oa/system/recType/list.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'xmgndlb';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
END IF;

SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmjslb';
IF(countt=0) THEN
	-- 项目权限管理--
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5003','项目角色类别','xmjslb',2,'edit',5001,'/oa/system/recRole/manage.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'xmjslb';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
END IF;

-- 修改图标
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmqxgl' and ICON not like 'security';
IF(countt=1)THEN
	update CWM_SYS_RES set ICON='security' where ALIAS = 'xmqxgl';
END IF;	
-- 修改图标
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmgndlb' and ICON not like 'edit';
IF(countt=1)THEN
	update CWM_SYS_RES set ICON='edit' where ALIAS = 'xmgndlb';
END IF;	
-- 修改图标
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'xmjslb' and ICON not like 'edit';
IF(countt=1)THEN
	update CWM_SYS_RES set ICON='edit' where ALIAS = 'xmjslb';
END IF;	

-- 1、 新增参数配置，是否将用户菜单放入到redis
SELECT COUNT(*) into countt FROM CWM_SYS_PARAMETER WHERE ID = '5004';
IF(countt=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,`VALUE`,DESCRIPTION)value(5004,'user.custom.infoset',0,1,'用户个性化设置是否放入到redis中参数配置');
END IF;

-- 2017年7月4日：数据分析
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'statistics';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5005','工具集成','statistics',10,'productfeatures',2,'',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'statistics';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;
	
-- 修改图标
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'statistics' and ICON not like 'productfeatures';
IF(countt=1)THEN
	update CWM_SYS_RES set ICON='productfeatures' where ALIAS = 'statistics';
END IF;
-- 修改顺序	
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'statistics' and SN not like '10';
IF(countt=1)THEN
	update CWM_SYS_RES set SN='10' where ALIAS = 'statistics';
END IF;

-- 2017年7月4日：数据分析-分析工具
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'staTools';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5006','工具管理','staTools',1,'form',5005,'/oa/statistics/tool/list.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'staTools';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;
-- 2017年7月4日：数据分析-访问链接配置
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'staAddress';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5007','访问链接配置','staAddress',2,'form',5005,'/oa/statistics/address/tree.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'staAddress';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;
	
-- 2017年7月18日：数据迁移
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'dataMigration';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5008','数据迁移','dataMigration',2,'form',191,'',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'dataMigration';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;
	
-- 修改顺序	
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'dataMigration' and SN not like '10';
IF(countt=1)THEN
	update CWM_SYS_RES set SN='10' where ALIAS = 'dataMigration';
END IF;	

-- 2017年7月18日：数据迁移-业务表结构
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'businessTableStruct';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5009','业务表结构','businessTableStruct',1,'form',5008,'/oa/migration/config.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'businessTableStruct';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;
		
-- 2017年7月18日：数据迁移-表单xml生成
SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'crateFormXml';
IF(countt=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('5010','生成表单xml','crateFormXml',1,'form',5008,'/oa/migration/xml.do',0,1,1,0,null);
	-- 给系统管理员分配权限
	SELECT RESID into @res_id FROM CWM_SYS_RES WHERE ALIAS = 'crateFormXml';
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,@res_id FROM CWM_SYS_ROLE_RES;
	END IF;	
	
-- 2017年8月26日：表单校验：修改手机号验证规则
SELECT COUNT(*) into countt FROM ibms_form_rule WHERE name like '手机号码' and REGULATION like '^1[0-9]{10}$';
IF(countt=0)THEN
	update ibms_form_rule set REGULATION='^1[0-9]{10}$' where name like '手机号码';
END IF;

-- ：表单校验：修改手机号验证规则
SELECT COUNT(*) into countt FROM ibms_form_rule WHERE name like '手机号码' and REGULATION like '^1[0-9]{10}$';
IF(countt=0)THEN
	update ibms_form_rule set REGULATION='^1[0-9]{10}$' where name like '手机号码';
END IF;

-- 2017年9月21日：列表数据预览
SELECT COUNT(*) into countt FROM ins_col_type WHERE KEY_ like 'listingPreview';
IF(countt=0)THEN
	INSERT INTO `ins_col_type` VALUES (5011, '列表数据预览', 'listingPreview', '/oa/portal/insPortal/getDataListHtml.do', '无', 'URL', '', '', '', '', null, '-4', '2017-09-21 11:05:38', '-4', '0');	
END IF;
SELECT COUNT(*) into countt FROM ins_column WHERE KEY_ like 'listingPreview';
IF(countt=0)THEN
	INSERT INTO `ins_column` VALUES (5012, 5011, '列表数据预览', 'listingPreview', null, 'ENABLED', '20', 'YES', '列表数据预览', '0', '-4', null, '-4', '2017-09-21 10:33:01', '500');
END IF;

-- 2017年9月25日： 日历管理
SELECT COUNT(*) into countt FROM cwm_sys_res WHERE ALIAS like 'duty' and  ISOPEN = 1;
IF(countt=0)THEN
	DELETE from `cwm_sys_res` where RESID in('5013','5014','5015','5016','5017','5018');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5013', '日历管理', 'duty', '17', 'form', '3', '', '1', '1', '1', NULL, '');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5014', '法定假期设置', 'legalHoliday', '1', 'form', '5013', '/oa/worktime/vacation/list.do', '0', '1', '1', NULL, '');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5015', '班次设置管理', 'workTimeSetting', '2', 'form', '5013', '/oa/worktime/workTimeSetting/list.do', '0', '1', '1', NULL, '');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5016', '工作日历设置', 'workCalendarSet', '3', 'form', '5013', '/oa/worktime/sysCalendar/list.do', '0', '1',  '1', NULL, '');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5017', '工作日历分配', 'workCalAssign', '4', 'form', '5013', '/oa/worktime/calendarAssign/list.do', '0', '1','1', NULL, '');
	INSERT INTO `cwm_sys_res` (`RESID`, `RESNAME`, `ALIAS`, `SN`, `ICON`, `PARENTID`, `DEFAULTURL`, `ISFOLDER`, `ISDISPLAYINMENU`, `ISOPEN`,  `ISNEWOPEN`, `PATH`) VALUES ('5018', '加班请假管理', 'overTime', '5', 'form', '5013', '/oa/worktime/overTime/list.do', '0', '1', '1', NULL, '');
	END IF;
-- --------------do my sql end--------------

COMMIT;
END;


CALL prod_data_dwj(@spacename);