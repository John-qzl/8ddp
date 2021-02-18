
CREATE OR REPLACE PROCEDURE synData_wjj (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
sqlll varchar2(1000);
BEGIN
 ------------------------start plsql-----------------------------------------------
 
 -- 添加功能点：平台配置、布局管理---------------------------------------------------------------------------------------------------------------------
 BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'ptpz';
		IF(countt=0) THEN  
			insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
			values(10000004450001,'平台配置','ptpz',1,'all',3,'',1,1,1,0,'2:3:10000004450001');
			 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=10000004450001;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3998,-1,10000004450001);
	     END IF;
	  END IF;
	  
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'bj';
		IF(countt=0) THEN  
			insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
			values(10000004450002,'布局管理','bj',1,'move',10000004450001,'/oa/webPortal/sysLayout/list.do',1,1,1,0,'2:3:10000004450001:10000004450002');
			 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=10000004450002;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(3999,-1,10000004450002);
	     END IF;
	  END IF;
 END;
 
 -- 添加功能点：图标库---------------------------------------------------------------------------------------------------------------------
 BEGIN
	SELECT COUNT(*) into countt FROM CWM_SYS_RES WHERE ALIAS = 'tbk';
		IF(countt=0) THEN  
			insert into CWM_SYS_RES(RESID,RESNAME, ALIAS, SN,ICON, PARENTID, DEFAULTURL, ISFOLDER,ISDISPLAYINMENU,ISOPEN,ISNEWOPEN,PATH)
			values(10000004450004,'图标库','tbk',1,'iconfont icon-smile',10000004450001,'/oa/webPortal/icons/view.do',0,1,1,0,'2:3:10000004450001:10000004450004');
			 SELECT COUNT(*) into countt FROM CWM_SYS_ROLE_RES WHERE ROLEID = -1 and RESID=10000004450004;
	 	  IF(countt=0) THEN
	    	insert into CWM_SYS_ROLE_RES(ROLERESID,ROLEID,RESID)Values(4001,-1,10000004450004);
	     END IF;
	  END IF;
 END;
  
 commit;
END synData_wjj;
