/**************************************************
 *  
	重置三员的功能点权限    并初始化系统实施人员的功能点权限  by liubo 2017-8-18
**************************************************/
CREATE OR REPLACE PROCEDURE resetRoleFunction(owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
BEGIN
----删除三员的功能点权限------
execute immediate 'delete from CWM_SYS_ROLE_RES WHERE ROLEID IN (-1,-2,-3,-4)';
COMMIT;
----往表CWM_SYS_ROLE中插入三条三员权限角色------
BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -1;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ROLEDESC,STATUS)VALUES(-1,''系统管理员'',''三员'',1)';
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -2;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ROLEDESC,STATUS)VALUES(-2,''安全保密员'',''三员'',1)';
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -3;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ROLEDESC,STATUS)VALUES(-3,''安全审计员'',''三员'',1)';
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -4;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ROLEDESC,STATUS)VALUES(-4,''系统实施人员'',''系统实施人员'',1)';
   commit;
 END IF;
END;
----往表CWM_SYS_ROLE_USER中插入三条三员角色用户记录------
BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -1 AND USERID = -1;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-1,-1,-1)';
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -2 AND USERID = -2;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-2,-2,-2)';
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -3 AND USERID = -3;
IF(countt=0) THEN
   execute immediate 'INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-3,-3,-3)';
   commit;
 END IF;
END;

------------------------------------------给-1system系统管理员 账户加权限------------------------
 -----execute immediate 'INSERT INTO这里不能将 INSERT INTO 换到下行写 否则报错
execute immediate 'INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)
SELECT DISTINCT 10000+rownum, -1,FUNC.RESID
FROM 
(
--------------加系统管理------------------------------------
(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN(''system'',''Develop'')) 
union all 
--------------加系统监控及以下菜单\门户管理\维度管理\日志管理\职务管理\组织管理\用户管理\功能点管理\参数管理------
SELECT * FROM CWM_SYS_RES F 
WHERE　F.ALIAS NOT IN (''idexPortal'',''columnType'',''portalColumn'',''subLog'',''sysLogSwitch'') 
START WITH F.RESID IN 
(SELECT RESID FROM CWM_SYS_RES WHERE ALIAS IN (''xtjk'',''SysLogView'',''indexManage'',''DemensionView'',''OrganizationView'',''SysJobView'',''function'',''ParametersView'',''sysuser'')) 
CONNECT BY PRIOR F.RESID = F.PARENTID
) FUNC';
COMMIT;


------------------------------------------给 -2right权限管理员 账户加权限------------------------
execute immediate 'INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)
SELECT DISTINCT 20000+rownum, -2,FUNC.RESID
FROM 
(
--------------加系统管理------------------------------------
(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN(''system'',''flow'')) 
union all 
--------------流程操作日志\流程人员分配\组织角色授权\用户角色分配\项目权限管理\角色管理\日志管理------------------------------------
SELECT * FROM CWM_SYS_RES F 
WHERE　F.ALIAS NOT IN (''sysLogSwitch'') 
START WITH F.RESID IN 
(SELECT RESID FROM CWM_SYS_RES WHERE ALIAS IN (''bpmRunLog'',''lcryfp'',''zzjssq'',''yhjsfp'',''zhcl'',''xmqxgl'',''sysrole'',''SysLogView'')) 
CONNECT BY PRIOR F.RESID = F.PARENTID
) FUNC';
COMMIT;

-----------------------------------------给 -3check 审计员 账户加权限------------------------
execute immediate 'INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)
SELECT DISTINCT 30000+rownum,-3,FUNC.RESID
FROM 
(
--------------加系统管理------------------------------------
(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN (''system'')) 
union all 
--------------加日志管理及以下菜单------------------------------------
SELECT * FROM CWM_SYS_RES F 
WHERE　F.ALIAS NOT IN (''sysErrorLog'') 
START WITH F.RESID IN 
(SELECT RESID FROM CWM_SYS_RES WHERE ALIAS IN 
(''SysLogView'')) 

CONNECT BY PRIOR F.RESID = F.PARENTID
) FUNC';
COMMIT;

------------------------------------------给系统实施员账户加权限------------------------
execute immediate 'INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)   
  SELECT DISTINCT 40000+rownum,-4,FUNC.RESID
  FROM 
  (
  (SELECT * FROM CWM_SYS_RES WHERE ALIAS IN (''root'',''webserver'')) 

  union all 

  SELECT * FROM CWM_SYS_RES F 
  WHERE　F.ALIAS NOT IN (''lcryfp'',''zzjssq'',''yhjsfp'') 
  START WITH F.RESID IN 
  (SELECT RESID FROM CWM_SYS_RES WHERE ALIAS IN 
  (''system'',''flowform'',''settingcenter'',''flow'',''processRun'',''Develop'',''otherTools'')) 

  CONNECT BY PRIOR F.RESID = F.PARENTID
  ) FUNC';      -----这里不能写成) FUNC;';  多一个分号 会报错
COMMIT;

END resetRoleFunction;
