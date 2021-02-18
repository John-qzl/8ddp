-- -----------重置三员的功能点权限    并初始化系统实施人员的功能点权限  by liubo 2017-8-18------------------------- --
DROP PROCEDURE IF EXISTS resetRoleFunction;
CREATE  
PROCEDURE resetRoleFunction()
BEGIN
DECLARE countt NUMERIC;

delete from CWM_SYS_ROLE_RES WHERE CWM_SYS_ROLE_RES.ROLEID IN (-1,-2,-3,-4);
delete from CWM_SYS_ROLE where ROLEID in (-1,-2,-3,-4);
COMMIT;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -1;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ALIAS,ROLEDESC,STATUS)VALUES(-1,'系统管理员','系统管理员','三员',1);
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -2;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ALIAS,ROLEDESC,STATUS)VALUES(-2,'安全保密员','安全保密员','三员',1);
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -3;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ALIAS,ROLEDESC,STATUS)VALUES(-3,'安全审计员','安全审计员','三员',1);

   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt FROM CWM_SYS_ROLE WHERE ROLEID= -4;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE(ROLEID,ROLENAME,ALIAS,ROLEDESC,STATUS)VALUES(-4,'系统实施人员','系统实施人员','系统实施人员',1);
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -1 AND USERID = -1;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-1,-1,-1);
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -2 AND USERID = -2;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-2,-2,-2);
   commit;
 END IF;
END;

BEGIN
select COUNT(*) INTO countt  FROM CWM_SYS_ROLE_USER WHERE ROLEID = -3 AND USERID = -3;
IF(countt=0) THEN
   INSERT INTO CWM_SYS_ROLE_USER(ROLEID,USERID,USERROLEID)VALUES(-3,-3,-3);
   commit;
 END IF;
END;

INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)   
SELECT DISTINCT 10000+(@rownum:=@rownum+1) as rowno, -1,FUNC.RESID
FROM 
(

(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN('system','Develop')) 
union all 

(
SELECT * FROM CWM_SYS_RES F 
WHERE F.ALIAS NOT IN ('idexPortal','columnType','portalColumn','subLog','sysLogSwitch') 
AND FIND_IN_SET(F.RESID ,
		CONCAT(
					getChildFunIdByCode ('xtjk'),',',
					getChildFunIdByCode ('SysLogView'),',',
					getChildFunIdByCode ('indexManage'),',',
					getChildFunIdByCode ('DemensionView'),',',
					getChildFunIdByCode ('OrganizationView'),',',
					getChildFunIdByCode ('SysJobView'),',',
					getChildFunIdByCode ('function'),',',
					getChildFunIdByCode ('ParametersView'),',',
					getChildFunIdByCode ('sysuser')
			) 
	 )
)

) FUNC,(SELECT(@rownum:=0))b ;
COMMIT;



INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)   
SELECT DISTINCT 20000+(@rownum:=@rownum+1) as rowno, -2,FUNC.RESID 
FROM 
(

(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN('system','flow')) 
union all 

(
SELECT * FROM CWM_SYS_RES F 
WHERE F.ALIAS NOT IN ('sysLogSwitch') 
AND FIND_IN_SET(F.RESID ,
		CONCAT(
					getChildFunIdByCode ('bpmRunLog'),',',
					getChildFunIdByCode ('lcryfp'),',',
					getChildFunIdByCode ('zzjssq'),',',
					getChildFunIdByCode ('yhjsfp'),',',
					getChildFunIdByCode ('zhcl'),',',
					getChildFunIdByCode ('xmqxgl'),',',
					getChildFunIdByCode ('sysrole'),',',
					getChildFunIdByCode ('SysLogView')
		)
	)
)

) FUNC,(SELECT(@rownum:=0))b ;
COMMIT;


INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)   
SELECT DISTINCT 30000+(@rownum:=@rownum+1) as rowno, -3,FUNC.RESID

FROM 
(

(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN ('system')) 
union all 

(
SELECT * FROM CWM_SYS_RES F 
WHERE F.ALIAS NOT IN ('sysErrorLog') 
AND FIND_IN_SET(F.RESID ,getChildFunIdByCode ('SysLogView'))
)

) FUNC,(SELECT(@rownum:=0))b;

COMMIT;


INSERT INTO CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)   
	SELECT DISTINCT 40000+(@rownum:=@rownum+1) as rowno,-4,FUNC.RESID
  FROM 
  (
		(SELECT * FROM CWM_SYS_RES WHERE ALIAS IN ('root', 'webserver')) 
		UNION ALL
		(
			SELECT * FROM CWM_SYS_RES F 
			WHERE F.ALIAS NOT IN ('lcryfp','zzjssq','yhjsfp') 
			AND FIND_IN_SET(F.RESID ,
				CONCAT(
					getChildFunIdByCode ('system'),',',
					getChildFunIdByCode ('flowform'),',',
					getChildFunIdByCode ('settingcenter'),',',
					getChildFunIdByCode ('flow'),',',
					getChildFunIdByCode ('processRun'),',',
					getChildFunIdByCode ('Develop'),',',
					getChildFunIdByCode ('otherTools')
				)
			) 
				
			
			)

  ) FUNC,(SELECT(@rownum:=0))b ;   
COMMIT;

END;

call resetRoleFunction();
-- ---COMMIT;
