CREATE OR REPLACE 
PROCEDURE "SYNDATA_ZHANGXG" (owneruser IN VARCHAR DEFAULT 'ibms')
AS
resnum number;
BEGIN


	
	
	
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE ALIAS = 'ReportManage';
IF(resnum=0) THEN
	-- 插入功能报表管理-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(2001,'报表管理','ReportManage',1,'/styles/default/images/resicon/s_f_4.png',3,null,1,1,1,0,null);
	-- 插入功能报表管理子节点--finereportManage-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(2002,'finereport报表设置','FinereportManage',1,'/styles/default/images/resicon/s_f_1.png',2001,'/oa/system/reportTemplate/manage.do',1,1,1,0,null);
	
	
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,2001 FROM CWM_SYS_ROLE_RES;
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,2002 FROM CWM_SYS_ROLE_RES;
	
	
END IF;

-- 新增功能菜单pageoffice设置-----------------------------------------------
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE ALIAS = 'PageOfficeManage';
IF(resnum=0) THEN
		
	-- 插入功能报表管理子节点--PageOfficeManage-----------------------------------------------
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values(2003,'pageOffice设置','PageOfficeManage',1,'/styles/default/images/resicon/s_o_2.png',2001,'/oa/system/officeTemplate/manage.do',1,1,1,0,null);
	
	-- 给系统管理员分配权限-----------------------------------------------
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID) select MAX(ROLERESID)+1,-1,2003 FROM CWM_SYS_ROLE_RES;

END IF;







-- 新增 我发起的流程节点
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE RESID = '2004';
IF(resnum=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('2004','我发起的流程','myStartPro',1,'/styles/default/images/resicon/s_o_2.png','151',null,1,1,1,0,null);
END IF;
-- 我发起的流程节点 分配权限
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '30006';
IF(resnum=0)THEN
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)values(30006,-1,2004);
END IF;

-- 新增 我承接的流程 节点
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE RESID = '2005';
IF(resnum=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('2005','我承接的流程','myUndertakePro',2,'/styles/default/images/resicon/s_o_2.png','151',null,1,1,1,0,null);
END IF;
-- 我承接的流程节点 分配权限
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '30007';
IF(resnum=0)THEN
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)values(30007,-1,2005);
END IF;


-- 新增我的请求
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE RESID = '2006';
IF(resnum=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('2006','我的请求','myRequest',1,'/styles/default/images/resicon/s_o_3.png','2004','/oa/flow/processRun/myRequest.do',0,1,1,0,null);
END IF;
-- 我的请求 分配权限
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '30008';
IF(resnum=0)THEN
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)values(30008,-1,2006);
END IF;


-- 新增我的办结
SELECT COUNT(*) into resnum FROM CWM_SYS_RES WHERE RESID = '2007';
IF(resnum=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('2007','我的办结','myCompleted',2,'/styles/default/images/resicon/s_o_4.png','2004','/oa/flow/processRun/myCompleted.do',0,1,1,0,null);
END IF;
-- 我的办结 分配权限
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '30009';
IF(resnum=0)THEN
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)values(30009,-1,2007);
END IF;

-- 修改我的草稿 父节点 为我发起的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2004', SN=3 WHERE RESID='157';

-- 修改代办任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='152';
-- 修改已办任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='153';
-- 修改办结任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='154';
-- 修改转办代理任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='155';
-- 修改加签流转任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='161';
-- 修改抄送转发任务 父节点 为我承接的流程节点
UPDATE CWM_SYS_RES SET PARENTID='2005' WHERE RESID='162';


-- 新增我的代理
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '2008';
IF(resnum=0)THEN
	insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
	values('2008','我的代理','myDelegate',3,'/styles/default/images/resicon/o_13.png','151','/oa/flow/agentSetting/list.do',1,1,1,0,null);
END IF;
-- 我的代理 授权给管理员
SELECT COUNT(*) into resnum FROM CWM_SYS_ROLE_RES WHERE ROLERESID = '30010';
IF(resnum=0)THEN
	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)values(30010,-1,2008);
END IF;




-- 新增参数配置，是否将用户菜单放入到redis
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2001';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2001,'user.menu.redisset',0,1,'系统用户菜单是否放入到redis中参数配置');
END IF;



-- 新增参数配置，是否开启用户数据同步功能
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2002';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2002,'mdm.user.sync',0,1,'是否开启用户数据同步功能');
END IF;
-- 新增参数配置，是否开启角色数据同步功能
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2003';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2003,'mdm.role.sync',0,1,'是否开启角色数据同步功能');
END IF;
-- 新增参数配置，是否开启组织数据同步功能
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2004';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2004,'mdm.org.sync',0,1,'是否开启组织数据同步功能');
END IF;
-- 新增参数配置，是否开启岗位数据同步功能
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2005';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2005,'mdm.pos.sync',0,1,'是否开启岗位数据同步功能');
END IF;
-- 新增参数配置，是否开启用户组织岗位 关系数据同步
SELECT COUNT(*) into resnum FROM CWM_SYS_PARAMETER WHERE ID = '2006';
IF(resnum=0)THEN
	insert into CWM_SYS_PARAMETER(ID,NAME,DATATYPE,VALUE,DESCRIPTION)values(2006,'mdm.userpos.sync',0,1,'是否开启用户组织岗位 关系数据同步');
END IF;



-- 事务回滚机制-----------------------------------------------
EXCEPTION WHEN others THEN
dbms_output.put_line('SYNDATA_ZHANGXG 执行错误');
rollback;

commit;
END;
