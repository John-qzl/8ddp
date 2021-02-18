/**************************************************
1.查询数据库中是否有该表：select count(*) into countt  from tabs where table_name = upper('CWM_LOGIN_LOG');
2.查询数据库中是否有该字段：  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_USER' AND COLUMN_NAME ='SIGN_PIC';
3.查询数据库中是否有该序列号：  select count(*) into countt from user_sequences where SEQUENCE_NAME = 'SEQ_CWM_LOGIN_LOG'; 
4.加备注：execute immediate 'COMMENT ON TABLE CWM_SYS_INDEX_COLUMN IS ''首页布局''';
 ----------------------赋权限-----------------------
--如无建表权限   execute immediate 'grant create table to ibms';
--如无创建存储权限   execute immediate 'grant create procedure to ibms';
--如无创建序列号权限  execute immediate 'grant create sequence to ibms';

脚本参考oracle_synStruct.sql写。

如因为脚本执行顺序造成的执行错误问题，如先修改表，后定义该表的错误，
可将定义表的sql移到oracle_synStruct.sql中。
**************************************************/

CREATE OR REPLACE PROCEDURE synstruct_yangbo (owneruser IN VARCHAR DEFAULT 'ibms')
AS
countt number;
countt1 number;
version1 varchar2(64);
sqlll varchar2(1000);
BEGIN
 ------------------------创建密码策略表--2016-8-23-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_PWD_STRATEGY') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_PWD_STRATEGY
    (
      ID            NUMBER(20)  NOT NULL ,
	  INIT_PWD        VARCHAR2(50 BYTE),
      FORCE_CHANGE_INIT_PWD  NUMBER(6),
	  PWD_RULE  NUMBER(6),
	  PWD_LENGTH  NUMBER(6),
	  VALIDITY  NUMBER(6),
	  HANDLE_OVERDUE NUMBER(6),
	  OVERDUE_REMIND  NUMBER(6),
	  VERIFY_CODE_APPEAR  NUMBER(6),
	  ERR_LOCK_ACCOUNT  NUMBER(6),
	  ENABLE_  NUMBER(6),
	  DESC_  VARCHAR(256 BYTE)
    )';
    execute immediate 'ALTER TABLE CWM_SYS_PWD_STRATEGY ADD (
      CONSTRAINT CWM_SYS_PWD_STRATEGY_PK
      PRIMARY KEY
      (ID))';
  END IF;
END;



 ------------------------更新CWM_SYS_TYPEKEY表字段 -----2016-8-27-------------------
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_TYPEKEY' AND COLUMN_NAME ='FLAG'  AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_TYPEKEY  ADD  FLAG NUMBER(20) DEFAULT NULL';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_TYPEKEY.FLAG IS ''1系统默认,0自定义''';
   execute immediate 'update CWM_SYS_TYPEKEY set FLAG=1';
   commit;
  END IF;
END;

BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_TYPEKEY' AND COLUMN_NAME ='TYPE' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_TYPEKEY  ADD  TYPE NUMBER(20) DEFAULT NULL';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_TYPEKEY.TYPE IS ''0平铺\r\n ,1树形''';
   execute immediate 'update CWM_SYS_TYPEKEY set TYPE=1';
    commit;
  END IF;
END;

------------------------创建CWM_SYS_LOG_SWITH表，日志开关--2016-8-29-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_LOG_SWITH') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_LOG_SWITH(
					 	ID            NUMBER(20)         NOT NULL,
					  MODEL         VARCHAR2(50 BYTE),
					  STATUS        NUMBER(6),
					  CREATETIME    TIMESTAMP  DEFAULT sysdate  NOT NULL,
					  CREATOR  	    VARCHAR2(20 BYTE),
					  CREATORID    NUMBER(20),
					  UPDBY  		VARCHAR2(256 BYTE),
					  UPDBYID       NUMBER(20),
					  MEMO         VARCHAR2(300 BYTE),
					  LASTUPTIME     TIMESTAMP DEFAULT sysdate  NOT NULL
						)';
    execute immediate 'ALTER TABLE CWM_SYS_LOG_SWITH ADD (
					  CONSTRAINT PK_LOG_SWITH
					  PRIMARY KEY
					  (ID))';
	execute immediate 'COMMENT ON TABLE CWM_SYS_LOG_SWITH IS ''日志开关''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.ID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.MODEL IS ''模块名''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.STATUS IS ''状态''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.CREATETIME IS ''创建时间''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.CREATOR IS ''创建人''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.CREATORID IS ''创建人ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.UPDBY IS ''更新人''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.UPDBYID IS ''更新人ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.MEMO IS ''备注''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG_SWITH.LASTUPTIME IS''最后更新时间''';
	COMMIT;
  END IF;
END;
------------------------创建CWM_SYS_USER_PARAM表，用户参数表--2016-8-19-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_USER_PARAM') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_USER_PARAM
					(
		  			  VALUEID    NUMBER(20) NOT NULL,
					  USERID     NUMBER(20),
					  PARAMID    NUMBER(20),
					  PARAMVALUE     VARCHAR2(200 BYTE),
					  PARAMDATEVALUE DATE,
					  PARAMINTVALUE    NUMBER(18,2)
					)';
    execute immediate 'ALTER TABLE CWM_SYS_USER_PARAM ADD (
					  CONSTRAINT CWM_SYS_USER_PARAM_PK
					  PRIMARY KEY
					  (VALUEID))';
					  
	execute immediate 'ALTER TABLE CWM_SYS_USER_PARAM
						ADD CONSTRAINT USER_PARAM_USERID_FK
						FOREIGN KEY(USERID) REFERENCES CWM_SYS_USER';
					  
   execute immediate 'ALTER TABLE CWM_SYS_USER_PARAM 
						ADD CONSTRAINT USER_PARAM_PARAMID_FK
						FOREIGN KEY(PARAMID) REFERENCES CWM_SYS_PARAM';
						
	execute immediate 'COMMENT ON TABLE CWM_SYS_USER_PARAM IS ''用户自定义参数''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.VALUEID IS ''主键ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.USERID IS ''用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.PARAMID IS ''属性ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.PARAMVALUE IS ''属性值''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.PARAMDATEVALUE IS ''日期类型属性值''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.PARAMINTVALUE IS ''数值型属性值''';
	COMMIT;
  END IF;
END;

------------------------创建CWM_SYS_USERUNDER表，用户参数表--2016-8-20-----------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_USERUNDER') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_SYS_USERUNDER
						(
						  ID             NUMBER(18)                     NOT NULL,
						  USERID         NUMBER(18),
						  UNDERUSERID    NUMBER(18),
						  UNDERUSERNAME  VARCHAR2(50 BYTE)
						)';
    execute immediate 'ALTER TABLE CWM_SYS_USERUNDER ADD (
						  CONSTRAINT CWM_SYS_USERUNDER_PK
						  PRIMARY KEY
						  (ID))';
			
	execute immediate 'COMMENT ON TABLE CWM_SYS_USERUNDER IS ''下属管理''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USERUNDER.ID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USER_PARAM.USERID IS ''用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USERUNDER.UNDERUSERID IS ''下属用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_USERUNDER.UNDERUSERNAME IS ''下属用户名''';
	COMMIT;
  END IF;
END;

-----------------------将CWM_SYS_USER_ORG表迁移到CWM_SYS_USER_POSITION表--2016-9-9-----------------------------------------------
BEGIN
   select count(*) into countt from tabs where table_name = upper('CWM_SYS_USER_ORG') AND TABLESPACE_NAME=upper(owneruser);
   IF(countt>0) THEN
   		select count(*) into countt from tabs where table_name = upper('CWM_SYS_USER_POSITION') AND TABLESPACE_NAME=upper(owneruser);
	   		IF(countt>0) THEN
	   		execute immediate 'insert into CWM_SYS_USER_POSITION (USERPOSID,POSID,USERID,ISPRIMARY,ORGID,ISCHARGE,ISDELETE,JOBID)
							select  USERORGID,null,USERID,ISPRIMARY,ORGID,ISCHARGE,0,null from CWM_SYS_USER_ORG';
	   		execute immediate 'drop table CWM_SYS_USER_ORG';
	   		commit;
   		END IF;
 	END IF;
 END;

 --------------------------sysFile表增加FILEBLOB字段----------2016-9-30 by yangbo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_FILE') AND COLUMN_NAME =upper('FILEBLOB') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  FILEBLOB BLOB DEFAULT NULL';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.FILEBLOB IS ''附件内容''';
    commit;
  END IF;

END;

------------------------创建CWM_OUT_MAIL表，外部邮件--2016-9-30-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_OUT_MAIL') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_OUT_MAIL(
					  MAILID NUMBER(20) NOT NULL ,
					  TITLE VARCHAR2(512 BYTE) ,
					  CONTENT CLOB   ,
					  SENDERADDRESSES VARCHAR2(128 BYTE)   ,
					  SENDERNAME VARCHAR2(128 BYTE)   ,
					  RECEIVERADDRESSES VARCHAR2(2000 BYTE)   ,
					  RECEIVERNAMES VARCHAR2(2000 BYTE)   ,
					  CCADDRESSES VARCHAR2(2000 BYTE)   ,
					  BCCANAMES VARCHAR2(2000 BYTE)   ,
					  BCCADDRESSES VARCHAR2(2000 BYTE)  ,
					  CCNAMES VARCHAR2(2000 BYTE)  ,
					  EMAILID VARCHAR2(128 BYTE) ,
					  TYPES NUMBER(20)   ,
					  USERID NUMBER(20),
					  ISREPLY NUMBER(20) ,
					  MAILDATE DATE ,
					  FILEIDS VARCHAR2(512 BYTE) ,
					  ISREAD NUMBER(20),
					  SETID  NUMBER(20),
					  PRIMARY KEY (MAILID))';
	execute immediate 'COMMENT ON TABLE CWM_OUT_MAIL IS ''外部邮件''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.MAILID IS ''自增列''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.TITLE IS ''主题''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.CONTENT IS ''内容''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.SENDERADDRESSES IS ''发件人地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.SENDERNAME IS ''发件人地址别名''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.RECEIVERADDRESSES IS ''收件人地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.RECEIVERNAMES IS ''收件人地址别名''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.CCADDRESSES IS ''抄送人地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.BCCANAMES IS ''暗送人地址别名''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.BCCADDRESSES IS''暗送人地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.CCNAMES IS''抄送人地址别名''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.EMAILID IS''邮件ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.TYPES IS'' 邮件类型 1:收件箱;2:发件箱;3:草稿箱;4:垃圾箱''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.USERID IS''用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.ISREPLY IS''是否回复''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.MAILDATE IS''日期''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.FILEIDS IS''附件ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL.ISREAD IS''是否已读''';
	COMMIT;
  END IF;
END;

------------------------创建CWM_CWM_OUT_MAIL_ATTACHMENT表，外部邮件附件表--2016-9-30-----------------------------------------------
 BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_CWM_OUT_MAIL_ATTACHMENT') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_CWM_OUT_MAIL_ATTACHMENT(
					  FILEID NUMBER(20) NOT NULL ,
					  FILENAME VARCHAR2(128 BYTE) ,
					  FILEPATH VARCHAR2(128 BYTE) ,
					  MAILID NUMBER(20),
					  PRIMARY KEY (FILEID))';					  
	execute immediate 'COMMENT ON TABLE CWM_CWM_OUT_MAIL_ATTACHMENT IS ''外部邮件附件表''';
	execute immediate 'COMMENT ON COLUMN CWM_CWM_OUT_MAIL_ATTACHMENT.FILEID IS ''主键,文件ID''';
	execute immediate 'COMMENT ON COLUMN CWM_CWM_OUT_MAIL_ATTACHMENT.FILENAME IS ''文件名''';
	execute immediate 'COMMENT ON COLUMN CWM_CWM_OUT_MAIL_ATTACHMENT.FILEPATH IS ''文件存放路径''';
	execute immediate 'COMMENT ON COLUMN CWM_CWM_OUT_MAIL_ATTACHMENT.MAILID IS ''邮件ID''';
	COMMIT;
  END IF;
END;

------------------------创建CWM_OUT_MAIL_LINKMAN表，外部邮件最近联系--2016-9-30-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_OUT_MAIL_LINKMAN') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_OUT_MAIL_LINKMAN(
					  LINKID NUMBER(20) NOT NULL ,
					  USERID NUMBER(20) ,
					  MAILID NUMBER(20)  ,
					  SENDTIME DATE ,
					  LINKNAME varchar(20)  ,
					  LINKADDRESS varchar(2000)  ,
					  SENDTIMES NUMBER(20)  ,
					  PRIMARY KEY (LINKID))';		  
	execute immediate 'COMMENT ON TABLE CWM_OUT_MAIL_LINKMAN IS ''外部邮件最近联系''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.LINKID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.USERID IS ''用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.MAILID IS ''邮件ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.SENDTIME IS ''发送时间''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.LINKNAME IS ''联系人名称''';	
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.LINKADDRESS IS ''联系人地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_LINKMAN.SENDTIMES IS ''发送次数''';
	COMMIT;
  END IF;
END;

------------------------创建CWM_OUT_MAIL_USETING表，外部邮件用户设置--2016-9-30-----------------------------------------------
 BEGIN
 select count(*) into countt from tabs where table_name = upper('CWM_OUT_MAIL_USETING') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_OUT_MAIL_USETING(
					  ID NUMBER(20) NOT NULL,
					  USERID NUMBER(20),
					  USERNAME VARCHAR2(128 BYTE),
					  MAILADDRESS VARCHAR2(128 BYTE),
					  MAILPASS VARCHAR2(128 BYTE),
					  SMTPHOST VARCHAR2(128 BYTE),
					  SMTPPORT VARCHAR2(64 BYTE),
					  POPHOST VARCHAR2(128 BYTE),
					  POPPORT VARCHAR2(64 BYTE),
					  IMAPHOST VARCHAR2(128 BYTE),
					  IMAPPORT VARCHAR2(128 BYTE),
					  ISDEFAULT NUMBER(6),
					  MAILTYPE VARCHAR2(50 BYTE),
					  USESSL NUMBER(6) DEFAULT ''0'' ,
					  ISVALIDATE NUMBER(6) DEFAULT ''1'' ,
					  ISDELETEREMOTE NUMBER(6) DEFAULT ''0'' ,
					  ISHANDLEATTACH NUMBER(6) DEFAULT ''1'' ,
					  PRIMARY KEY (ID))';
	execute immediate 'COMMENT ON TABLE CWM_OUT_MAIL_USETING IS ''外部邮件用户设置''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.ID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.USERID IS ''用户ID''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.USERNAME IS ''用户名称''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.MAILADDRESS IS ''外部邮箱地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.MAILPASS IS ''外部邮箱密码''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.SMTPHOST IS ''smt主机''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.SMTPPORT IS ''smt端口''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.POPHOST IS ''pop主机''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.POPPORT IS ''pop端口''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.IMAPHOST IS ''imap主机''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.IMAPPORT IS ''imap端口''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.ISDEFAULT IS ''是否默认''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.MAILTYPE IS ''接收邮件服务器类型''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.USESSL IS ''是否使用SSL认证。0：否；1：是''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.ISVALIDATE IS ''是否需要身份验证。0：否；1：是''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.ISDELETEREMOTE IS ''下载时，是否删除远程邮件。0：否；1：是''';
	execute immediate 'COMMENT ON COLUMN CWM_OUT_MAIL_USETING.ISHANDLEATTACH IS ''是否下载附件。0：否；1：是''';
	COMMIT;
  END IF;
END;

------------------------创建CWM_OA_LINKMAN表，邮箱联系人--2016-9-30-----------------------------------------------
 BEGIN
 select count(*) into countt from tabs where table_name = upper('CWM_OA_LINKMAN') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE CWM_OA_LINKMAN(
					  ID NUMBER(20) NOT NULL,
					  NAME VARCHAR2(50 BYTE),
					  SEX VARCHAR2(8 BYTE),
					  PHONE VARCHAR2(50 BYTE),
					  EMAIL VARCHAR2(50 BYTE),
					  COMPANY VARCHAR2(50 BYTE),
					  JOB VARCHAR2(20 BYTE),
					  ADDRESS VARCHAR2(50 BYTE),
					  CREATETIME DATE,
					  STATUS NUMBER(6),
					  USERID NUMBER(20),
					  PRIMARY KEY (ID))';
	execute immediate 'COMMENT ON TABLE CWM_OA_LINKMAN IS ''邮箱联系人''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.ID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.NAME IS ''姓名''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.SEX IS ''性别''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.PHONE IS ''电话''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.EMAIL IS ''邮箱''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.COMPANY IS ''公司''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.JOB IS ''工作''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.ADDRESS IS ''地址''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.CREATETIME IS ''创建时间''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.STATUS IS ''状态,1为启用，0为禁用''';
	execute immediate 'COMMENT ON COLUMN CWM_OA_LINKMAN.USERID IS ''用户ID''';

	COMMIT;
  END IF;
END;

 --------------------------IBMS_DATA_TEMPLATE表增加FILETEMPHTML字段----------2016-10-15 by yangbo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('FILETEMPHTML') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_DATA_TEMPLATE  ADD  FILETEMPHTML CLOB ';
    execute immediate 'COMMENT ON COLUMN IBMS_DATA_TEMPLATE.FILETEMPHTML IS ''文件夹树HTML''';
    commit;
  END IF;

END;

--------------------------CWM_SYS_PARAM表SOURCEKEY字段值变大----------2016-10-17 by yangbo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_PARAM') AND COLUMN_NAME =upper('SOURCEKEY') AND OWNER=upper(owneruser);
  IF(countt>0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_PARAM  MODIFY(SOURCEKEY VARCHAR2(2000 BYTE)) ';
    commit;
  END IF;

END;

 --------------------------IBMS_DATA_TEMPLATE表增加ATTACTEMPHTML字段----------2016-10-15 by yangbo---
 BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('IBMS_DATA_TEMPLATE') AND COLUMN_NAME =upper('ATTACTEMPHTML') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE IBMS_DATA_TEMPLATE  ADD  ATTACTEMPHTML CLOB ';
    execute immediate 'COMMENT ON COLUMN IBMS_DATA_TEMPLATE.ATTACTEMPHTML IS ''附件列表HTML''';
    commit;
  END IF;

END;


 --------------------------CWM_SYS_RES表更改‘职位管理’为‘职务管理’----------2016-10-31 by yangbo---
 BEGIN
  select count(*) into countt  FROM CWM_SYS_RES  WHERE ALIAS = 'SysJobView';
  IF(countt>0) THEN
    UPDATE CWM_SYS_RES set RESNAME='职务管理' WHERE  ALIAS='SysJobView';
    commit;
  END IF;

END;
 --------------------------CWM_SYS_FILE表添加“归档”字段----------2016-10-31 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='FILING' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  FILING NUMBER(20) DEFAULT 0';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.FILING IS ''1归档,0未归档''';
   execute immediate 'update CWM_SYS_FILE set FILING=0';
   commit;
  END IF;
END;

 --------------------------CWM_SYS_FILE表添加“PARENTID”字段----------2016-11-01 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='PARENTID' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  PARENTID NUMBER(20)';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.PARENTID IS ''该附件第一版本的id''';
   execute immediate 'update CWM_SYS_FILE set PARENTID=FILEID';
   commit;
  END IF;
END;

 --------------------------CWM_SYS_FILE表添加“ISNEW”字段----------2016-11-01 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='ISNEW' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  ISNEW NUMBER(20)';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.ISNEW IS ''是否最新 1是''';
   execute immediate 'update CWM_SYS_FILE set ISNEW=1';
   commit;
  END IF;
END;


 --------------------------CWM_SYS_FILE表添加“version”字段----------2016-11-01 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='VERSION' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   version1:='1.0.0';
   execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  VERSION VARCHAR2(64 BYTE)';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.VERSION IS ''初始版本 1.0.0''';
   sqlll:='update CWM_SYS_FILE set VERSION='''||version1||'''';
   commit;
  END IF;
END;

 --------------------------CWM_SYS_LOG表添加“jsonData”字段----------2016-11-26 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_LOG' AND COLUMN_NAME ='JSONDATA' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_LOG  ADD  jsonData CLOB';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_LOG.jsonData IS ''记录操作后数据''';
   commit;
  END IF;
END;

--------------------------CWM_SYS_LOG表添加“STOREWAY”存储方式字段----------2016-11-26 by yangbo---
BEGIN
 select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME ='CWM_SYS_FILE' AND COLUMN_NAME ='STOREWAY' AND OWNER=upper(owneruser);
  IF(countt=0) THEN
   execute immediate 'ALTER TABLE CWM_SYS_FILE  ADD  STOREWAY NUMBER(20)';
   execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE.STOREWAY IS ''存储方式 1代表分布式 0代表存储本地服务器''';
   execute immediate 'update CWM_SYS_FILE set STOREWAY=0';
   commit;
  END IF;
END;
------------------------创建自定义表单历史表--2017-01-09-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('IBMS_FORM_DEF_HI') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
    execute immediate 'CREATE TABLE IBMS_FORM_DEF_HI(
					  HISID NUMBER(20) NOT NULL,
					  FORMDEFID  NUMBER(20),
					  CATEGORYID  NUMBER(20),
					  FORMKEY VARCHAR2(200 BYTE),
					  SUBJECT VARCHAR2(128 BYTE),
					  FORMDESC VARCHAR2(200 BYTE),
					  HTML CLOB,
					  TEMPLATE CLOB,
					  ISDEFAULT NUMBER(6),
					  VERSIONNO NUMBER(20),
					  TABLEID NUMBER(20),
					  ISPUBLISHED NUMBER(6),
					  PUBLISHEDBY VARCHAR2(20 BYTE),
					  PUBLISHTIME DATE,
					  TABTITLE VARCHAR2(500 BYTE),
					  DESIGNTYPE NUMBER(6),
					  TEMPLATESID VARCHAR2(300 BYTE),
					  CREATEBY NUMBER(20),
					  CREATOR  VARCHAR2(50 BYTE),
					  CREATETIME  DATE,
					  PRIMARY KEY (HISID))';
	execute immediate 'COMMENT ON TABLE IBMS_FORM_DEF_HI IS ''流程表单定义历史记录''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.HISID IS ''主键''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.FORMDEFID IS ''表单ID''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.CATEGORYID IS ''表单分类''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.FORMKEY IS ''表单KEY''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.SUBJECT IS ''表单标题''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.FORMDESC IS ''描述''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.HTML IS ''定义HTML''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.TEMPLATE IS ''FREEMAKER模版''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.ISDEFAULT IS ''是否缺省''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.VERSIONNO IS ''版本号''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.TABLEID IS ''表ID''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.ISPUBLISHED IS ''是否发布''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.PUBLISHEDBY IS ''发布人''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.PUBLISHTIME IS ''发布时间''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.TABTITLE IS ''TAB标题''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.DESIGNTYPE IS ''设计类型''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.TEMPLATESID IS ''模板表对应ID''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.CREATEBY IS ''创建人ID''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.CREATOR IS ''创建人''';
	execute immediate 'COMMENT ON COLUMN IBMS_FORM_DEF_HI.CREATETIME IS ''创建时间''';
	COMMIT;
  END IF;
END;

------------------------创建文件分类记录表--2017-01-10-----------------------------------------------
 BEGIN
 select count(*) into countt   from tabs where table_name = upper('CWM_SYS_FILE_TYPE') AND TABLESPACE_NAME=upper(owneruser);
 IF(countt=0) THEN
 	execute immediate 'CREATE TABLE CWM_SYS_FILE_TYPE(
				  TYPEID        NUMBER(18)                      NOT NULL,
				  TYPENAME      VARCHAR2(128 BYTE)              NOT NULL,
				  NODEPATH      VARCHAR2(64 BYTE),
				  DEPTH         NUMBER(18)                      NOT NULL,
				  PARENTID      NUMBER(18),
				  NODEKEY       VARCHAR2(64 BYTE)               NOT NULL,
				  CATKEY        VARCHAR2(64 BYTE)               NOT NULL,
				  SN            NUMBER(18)                      NOT NULL,
				  USERID        NUMBER(18),
				  DEPID         NUMBER(18),
				  TYPE          NUMBER(18)                      DEFAULT 1                     NOT NULL,
				  ISLEAF        NUMBER(18)                      DEFAULT NULL,
				  NODECODE      VARCHAR2(20 BYTE)               DEFAULT NULL,
				  NODECODETYPE  NUMBER(18)                      DEFAULT 0                     NOT NULL,
				  TABLEID     VARCHAR2(38 BYTE),
  				  DATAID      VARCHAR2(38 BYTE)
				)';
	execute immediate 'COMMENT ON TABLE CWM_SYS_FILE_TYPE IS ''文件分类记录表''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.TYPEID IS ''分类节点Id''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.TYPENAME IS''名称''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.NODEPATH IS ''路径''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.DEPTH IS ''层次''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.PARENTID IS ''父节点''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.NODEKEY IS ''节点的分类Key''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.CATKEY IS ''节点分类的Key，如产品分类Key为PT''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.SN IS ''序号''';
	execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.USERID IS ''所属用户当为空则代表为公共分类''';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.DEPID IS ''部门ID''';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.TABLEID IS ''业务表Id''';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_FILE_TYPE.DATAID IS ''记录Id''';
	COMMIT;
  END IF;
END;


-----------------------栏目分类表--ins_col_type---2017-3-18------------------------------------------------
BEGIN
 select count(*) into countt   from tabs where table_name = upper('ins_col_type') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE ins_col_type(
		  TYPE_ID_     	NUMBER(18)                     NOT NULL,
		  NAME_         VARCHAR(50)                     NOT NULL,
		  KEY_          VARCHAR(50)                     NOT NULL,
		  URL_          VARCHAR(100)                    NOT NULL,
		  MORE_URL_     VARCHAR(100),
		  LOAD_TYPE_    VARCHAR(20),
		  TEMP_ID_      VARCHAR(64),
		  TEMP_NAME_    VARCHAR(64),
		  ICON_CLS_     VARCHAR(20),
		  MEMO_         VARCHAR(512),
		  CREATE_TIME_  DATE,
		  CREATE_BY_    VARCHAR(64),
		  UPDATE_TIME_  DATE,
		  UPDATE_BY_    VARCHAR(64),
		  ORG_ID_       VARCHAR(64),
		PRIMARY KEY (TYPE_ID_))';
		
		execute immediate 'COMMENT ON TABLE ins_col_type IS ''栏目分类表''';
		execute immediate 'COMMENT ON COLUMN  ins_col_type.TYPE_ID_ IS ''栏目模板主键''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.NAME_ IS ''栏目类型名称''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.KEY_ IS ''栏目类型Key''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.URL_ IS ''栏目加载URL''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.MORE_URL_ IS ''更多路径''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.LOAD_TYPE_ IS ''加载类型\r\n            URL=URL\r\n            TEMPLATE=模板''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.TEMP_ID_ IS ''模板ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.TEMP_NAME_ IS ''模板名称''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.MEMO_ IS ''栏目分类描述''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.UPDATE_TIME_ IS ''更新时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_type.ORG_ID_ IS ''组织ID''';
	COMMIT;
	
  END IF;
END;

---------------------布局栏目表--ins_column----2017-3-18------------------------------------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_column') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_column(
		  COL_ID_        NUMBER(18)                    NOT NULL,
		  TYPE_ID_       NUMBER(18),
		  NAME_          VARCHAR(80)                    NOT NULL,
		  KEY_           VARCHAR(50)                    NOT NULL,
		  URL_           VARCHAR(255),
		  ENABLED_       VARCHAR(20)                    NOT NULL,
		  NUMS_OF_PAGE_  INTEGER,
		  ALLOW_CLOSE_   VARCHAR(20),
		  COL_TYPE_      VARCHAR(50),
		  ORG_ID_        VARCHAR(64),
		  CREATE_BY_     VARCHAR(64),
		  CREATE_TIME_   DATE,
		  UPDATE_BY_     VARCHAR(64),
		  UPDATE_TIME_   DATE,
		  HEIGHT_        INTEGER,
		PRIMARY KEY (COL_ID_))';
		
		execute immediate 'COMMENT ON TABLE  ins_column IS ''布局栏目表''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.COL_ID_ IS ''栏目ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.TYPE_ID_ IS ''栏目模板ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.NAME_ IS ''栏目名称''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.KEY_ IS ''栏目Key''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.ENABLED_ IS ''是否启用''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.NUMS_OF_PAGE_ IS ''每页记录数''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.ALLOW_CLOSE_ IS ''是否允许关闭''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.COL_TYPE_ IS ''信息栏目类型\r\n            公告\r\n            公司或单位新闻\r\n            部门新闻''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_column.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;

  END IF;
END;

--------------个人组织门户定义表--ins_portal---2017-3-18-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_portal') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_portal(
		  PORT_ID_      NUMBER(18)                     NOT NULL,
		  NAME_         VARCHAR(128)                    NOT NULL,
		  KEY_          VARCHAR(64),
		  COL_NUMS_     INTEGER,
		  COL_WIDTHS_   VARCHAR(50),
		  IS_DEFAULT_   VARCHAR(20)                     NOT NULL,
		  DESC_         VARCHAR(512),
		  USER_ID_      VARCHAR(64),
		  ORG_ID_       VARCHAR(64),
		  CREATE_BY_    VARCHAR(64),
		  CREATE_TIME_  DATE,
		  UPDATE_BY_    VARCHAR(64),
		  UPDATE_TIME_  DATE,
		PRIMARY KEY (PORT_ID_))';
		
		
		execute immediate 'COMMENT ON TABLE  ins_portal IS ''个人组织门户定义''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.PORT_ID_ IS ''门户ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.NAME_ IS ''门户名称''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.KEY_ IS ''门户KEY\r\n            个人门户\r\n            公司门户\r\n            部门门户\r\n            知识门户''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.COL_NUMS_ IS ''列数''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.COL_WIDTHS_ IS ''栏目宽\r\n            三列格式如250,100%,400''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.IS_DEFAULT_ IS ''是否为系统缺省''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.DESC_ IS ''描述''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.USER_ID_ IS ''用户ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_portal.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;
  END IF;
END;

--------------门户栏目配置表--ins_port_col---2017-3-18-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_port_col') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_port_col(
		  CONF_ID_      NUMBER(18)                     NOT NULL,
		  PORT_ID_      NUMBER(18)                    NOT NULL,
		  COL_ID_       NUMBER(18)                   NOT NULL,
		  WIDTH_        INTEGER,
		  HEIGHT_       INTEGER                         NOT NULL,
		  WIDTH_UNIT_   VARCHAR(8),
		  HEIGHT_UNIT_  VARCHAR(8)                      NOT NULL,
		  COL_NUM_      INTEGER,
		  SN_           INTEGER,
		  ORG_ID_       VARCHAR(64),
		  CREATE_BY_    VARCHAR(64),
		  CREATE_TIME_  DATE,
		  UPDATE_BY_    VARCHAR(64),
		  UPDATE_TIME_  DATE,
		PRIMARY KEY (CONF_ID_))';
		
		execute immediate 'COMMENT ON TABLE  ins_port_col IS ''门户栏目配置表''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.CONF_ID_ IS ''配置ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.PORT_ID_ IS ''门户ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.COL_ID_ IS ''栏目ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.WIDTH_ IS ''宽度''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.HEIGHT_ IS ''高度''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.WIDTH_UNIT_ IS ''宽度单位\r\n            百份比=%\r\n            像数=px''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.HEIGHT_UNIT_ IS ''高度单位\r\n            百份比=%\r\n            像数=px''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.COL_NUM_ IS ''列号''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.SN_ IS ''列中序号''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_port_col.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;
	
  END IF;
END;

--------------新闻公告表--ins_news---2017-3-18-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_news') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_news(
		  NEW_ID_       NUMBER(18)                    NOT NULL,
		  SUBJECT_      VARCHAR(120)                    NOT NULL,
		  TAG_          VARCHAR(80),
		  KEYWORDS_     VARCHAR(255),
		  CONTENT_      CLOB,
		  IS_IMG_       VARCHAR(20),
		  IMG_FILE_ID_  VARCHAR(64),
		  READ_TIMES_   INTEGER                         NOT NULL,
		  AUTHOR_       VARCHAR(50),
		  ALLOW_CMT_    VARCHAR(20),
		  STATUS_       VARCHAR(20)                     NOT NULL,
		  ORG_ID_       VARCHAR(64),
		  CREATE_BY_    VARCHAR(64),
		  CREATE_TIME_  DATE,
		  UPDATE_BY_    VARCHAR(64),
		  UPDATE_TIME_  DATE,
		PRIMARY KEY (NEW_ID_))';
		
		execute immediate 'COMMENT ON TABLE  ins_news IS ''新闻公告表''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.NEW_ID_ IS ''新闻信息ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.SUBJECT_ IS ''标题''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.TAG_ IS ''标签''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.KEYWORDS_ IS ''关键字''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.CONTENT_ IS ''内容''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.IS_IMG_ IS ''是否为图片新闻''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.IMG_FILE_ID_ IS ''图片文件ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.READ_TIMES_ IS ''阅读次数''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.AUTHOR_ IS ''作者''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.ALLOW_CMT_ IS ''是否允许评论''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.STATUS_ IS ''状态''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;
  END IF;
END;



--------------公告或新闻评论--ins_news_cm---2017-3-18-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_news_cm') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_news_cm
		(
		  COMM_ID_      NUMBER(18)                     NOT NULL,
		  NEW_ID_       NUMBER(18)                     NOT NULL,
		  FULL_NAME_    VARCHAR(50)                     NOT NULL,
		  CONTENT_      VARCHAR(1024)                   NOT NULL,
		  AGREE_NUMS_   INTEGER                         NOT NULL,
		  REFUSE_NUMS_  INTEGER                         NOT NULL,
		  IS_REPLY_     VARCHAR(20)                     NOT NULL,
		  REP_ID_       VARCHAR(64),
		  ORG_ID_       VARCHAR(64),
		  CREATE_BY_    VARCHAR(64)                     NOT NULL,
		  CREATE_TIME_  DATE,
		  UPDATE_BY_    VARCHAR(64),
		  UPDATE_TIME_  DATE,
		PRIMARY KEY (COMM_ID_))';
		
		execute immediate 'COMMENT ON TABLE  ins_news_cm IS ''公告或新闻评论''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.COMM_ID_ IS ''评论ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.NEW_ID_ IS ''信息ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.FULL_NAME_ IS ''评论人名''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.CONTENT_ IS ''评论内容''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.AGREE_NUMS_ IS ''赞同与顶''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.REFUSE_NUMS_ IS ''反对与鄙视次数''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.IS_REPLY_ IS ''是否为回复''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.REP_ID_ IS ''回复评论ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_news_cm.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;
  END IF;
END;



--------------新闻栏目表---ins_col_new---2017-3-18-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('ins_col_new') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE  ins_col_new
		(
		  ID_             NUMBER(18)                   NOT NULL,
		  COL_ID_         NUMBER(18)                   NOT NULL,
		  NEW_ID_         NUMBER(18)                   NOT NULL,
		  SN_             INTEGER                       NOT NULL,
		  START_TIME_     DATE                          NOT NULL,
		  END_TIME_       DATE                          NOT NULL,
		  IS_LONG_VALID_  VARCHAR(20),
		  ORG_ID_         VARCHAR(64),
		  CREATE_BY_      VARCHAR(64),
		  CREATE_TIME_    DATE,
		  UPDATE_BY_      VARCHAR(64),
		  UPDATE_TIME_    DATE,
		PRIMARY KEY (ID_))';
		
		execute immediate 'COMMENT ON TABLE  ins_col_new IS ''新闻栏目表''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.ID_ IS ''ID_''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.COL_ID_ IS ''栏目ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.NEW_ID_ IS ''新闻ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.SN_ IS ''序号''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.START_TIME_ IS ''有效开始时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.END_TIME_ IS ''有效结束时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.IS_LONG_VALID_ IS ''是否长期有效''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.ORG_ID_ IS ''组织ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.CREATE_BY_ IS ''创建人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.CREATE_TIME_ IS ''创建时间''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.UPDATE_BY_ IS ''更新人ID''';
		
		execute immediate 'COMMENT ON COLUMN  ins_col_new.UPDATE_TIME_ IS ''更新时间''';
	COMMIT;
  END IF;
END;



--------------日程主表---cwm_sys_agenda---2017-3-25-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_AGENDA') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
			execute immediate 'CREATE TABLE CWM_SYS_AGENDA
			(
			  AGENDA_ID_   NUMBER(18)                           NOT NULL,
			  TYPE_        VARCHAR2(255 BYTE),
			  SUBJECT_     VARCHAR2(255 BYTE),
			  CONTENT_     VARCHAR2(255 BYTE),
			  CREATOR_ID_  NUMBER(18),
			  CREATOR_     VARCHAR2(255 BYTE),
			  START_TIME_  DATE,
			  END_TIME_    DATE,
			  GRADE_       VARCHAR(64),
			  WARN_WAY_    NUMBER(18),
			  FILE_ID_     NUMBER(18),
			  RUN_ID_      NUMBER(18),
			  STATUS_      NUMBER(18),
				PRIMARY KEY (AGENDA_ID_))';
			
			execute immediate 'COMMENT ON TABLE CWM_SYS_AGENDA IS ''日程主表''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.AGENDA_ID_ IS ''日程主键''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.TYPE_ IS ''日程类型''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.SUBJECT_ IS ''标题''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.CONTENT_ IS ''内容''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.CREATOR_ID_ IS ''创建人id''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.CREATOR_ IS ''创建人''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.START_TIME_ IS ''开始时间''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.END_TIME_ IS ''结束时间''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.GRADE_ IS ''紧急程度''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.WARN_WAY_ IS ''提醒方式：0不提醒，1邮件，2站内信，3手机''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.FILE_ID_ IS ''文件ID''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.RUN_ID_ IS ''任务ID''';
			execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA.STATUS_ IS ''状态 0：未完成 1：完成''';
	COMMIT;
  END IF;
END;

-- ------------日程执行人员表---cwm_sys_agenda_execut---2017-3-25-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_AGENDA_EXECUT') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE CWM_SYS_AGENDA_EXECUT
		(
		  ID_           NUMBER(18)                          NOT NULL,
		  AGENDA_ID_    NUMBER(18),
		  EXECUTOR_ID_  NUMBER(18),
		  EXECUTOR_     VARCHAR(64),
		  PRIMARY KEY (ID_))';
		  
		execute immediate 'COMMENT ON TABLE CWM_SYS_AGENDA_EXECUT IS ''日程执行人员''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_EXECUT.ID_ IS ''主键''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_EXECUT.AGENDA_ID_ IS ''日程ID''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_EXECUT.EXECUTOR_ID_ IS ''执行人ID''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_EXECUT.EXECUTOR_ IS ''执行人''';
  	COMMIT;
  END IF;
END;

-- ------------日程工作交流---cwm_sys_agenda_msg---2017-3-25-----------------------
BEGIN
 select count(*) into countt  from tabs where table_name = upper('CWM_SYS_AGENDA_MSG') AND TABLESPACE_NAME=upper(owneruser);
  IF(countt=0) THEN
		execute immediate 'CREATE TABLE CWM_SYS_AGENDA_MSG
		(
		  ID_         NUMBER(18)                            NOT NULL,
		  AGENDA_ID_  NUMBER(18),
		  CONTENTS_   CLOB,
		  REPLY_ID_   NUMBER(18),
		  REPLYER_    VARCHAR(64),
		  SEND_TIME_  DATE,
		  PRIMARY KEY (ID_))';
		
		execute immediate 'COMMENT ON TABLE CWM_SYS_AGENDA_MSG IS ''日程工作交流''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.ID_ IS ''主键''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.AGENDA_ID_ IS ''日程ID''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.CONTENTS_ IS ''评论内容''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.REPLY_ID_ IS ''回复人ID''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.REPLYER_ IS ''回复人''';
		execute immediate 'COMMENT ON COLUMN CWM_SYS_AGENDA_MSG.SEND_TIME_ IS ''发送时间''';
  	COMMIT;
  END IF;
END;


 --------------------------用户来源frome_Type_字段添加---2017-3-30---
BEGIN
  select count(*) into countt  FROM ALL_TAB_COLUMNS WHERE TABLE_NAME =upper('CWM_SYS_USER') AND COLUMN_NAME =upper('FROM_TYPE_') AND OWNER=upper(owneruser);
  IF(countt=0) THEN
    execute immediate 'ALTER TABLE CWM_SYS_USER  ADD  FROM_TYPE_  VARCHAR(64)';
    execute immediate 'COMMENT ON COLUMN CWM_SYS_USER.FROM_TYPE_ IS ''数据来源 默认为系统添加''';
    commit;
  END IF;
END;

 --------------------------更新所有图标---2017-5-24------------
 BEGIN
  select count(*) into countt  FROM CWM_SYS_RES  WHERE ALIAS = 'root' and ICON='mainpage';
  IF(countt=0) THEN
   		UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='root';
		UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='webserver';
		UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='system';
		UPDATE CWM_SYS_RES  set ICON='viewgallery' where ALIAS='function';
		UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='flowform';
		UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='formTable';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='formTemplate';
		UPDATE CWM_SYS_RES  set ICON='account' where ALIAS='sysuser';
		UPDATE CWM_SYS_RES  set ICON='atmaway' where ALIAS='sysrole';
		UPDATE CWM_SYS_RES  set ICON='viewgallery' where ALIAS='settingcenter';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='taskApproval';
		UPDATE CWM_SYS_RES  set ICON='set1' where ALIAS='SYS_USER_AGENT';
		UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='client';
		UPDATE CWM_SYS_RES  set ICON='process' where ALIAS='rcp1';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='formDesign';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='admintaskApproval';
		UPDATE CWM_SYS_RES  set ICON='assessedbadge' where ALIAS='formRule';
		UPDATE CWM_SYS_RES  set ICON='training' where ALIAS='formDialog';
		UPDATE CWM_SYS_RES  set ICON='circle1' where ALIAS='flow';
		UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='flowDesign';
		UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='flowInstance';
		UPDATE CWM_SYS_RES  set ICON='electrical' where ALIAS='flowDelegate';
		UPDATE CWM_SYS_RES  set ICON='video' where ALIAS='processrunhistroy';
		UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='bpmRunLog';
		UPDATE CWM_SYS_RES  set ICON='skip' where ALIAS='agentDelePro';
		UPDATE CWM_SYS_RES  set ICON='comments' where ALIAS='processRun';
		UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='pendTask';
		UPDATE CWM_SYS_RES  set ICON='survey1' where ALIAS='alreadyTask';
		UPDATE CWM_SYS_RES  set ICON='templatedefault' where ALIAS='completeTask';
		UPDATE CWM_SYS_RES  set ICON='skip' where ALIAS='accordingTask';
		UPDATE CWM_SYS_RES  set ICON='xml' where ALIAS='myDrafts';
		UPDATE CWM_SYS_RES  set ICON='circle1' where ALIAS='proTrans';
		UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='proCpy';
		UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='DemensionView';
		UPDATE CWM_SYS_RES  set ICON='blueprintagree' where ALIAS='OrganizationView';
		UPDATE CWM_SYS_RES  set ICON='link' where ALIAS='SysFileAttachView';
		UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='SysLogView';
		UPDATE CWM_SYS_RES  set ICON='supplierfeatures' where ALIAS='SysJobView';
		UPDATE CWM_SYS_RES  set ICON='pin' where ALIAS='SysFileAttachView';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='subLog';
		UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='sysErrorLog';
		UPDATE CWM_SYS_RES  set ICON='signboard' where ALIAS='sysLogSwitch';
		UPDATE CWM_SYS_RES  set ICON='compass' where ALIAS='gradeManager';
		UPDATE CWM_SYS_RES  set ICON='discount' where ALIAS='Develop';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='SerialNumberView';
		UPDATE CWM_SYS_RES  set ICON='survey' where ALIAS='Sysscript';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='Codetemplate';
		UPDATE CWM_SYS_RES  set ICON='training' where ALIAS='CodeGenal';
		UPDATE CWM_SYS_RES  set ICON='folder' where ALIAS='typekey';
		UPDATE CWM_SYS_RES  set ICON='history' where ALIAS='Quartz';
		UPDATE CWM_SYS_RES  set ICON='service' where ALIAS='WSM';
		UPDATE CWM_SYS_RES  set ICON='inquirytemplate' where ALIAS='JMSM';
		UPDATE CWM_SYS_RES  set ICON='3column' where ALIAS='sysTypeKey';
		UPDATE CWM_SYS_RES  set ICON='navlist' where ALIAS='GlobalTypeManager';
		UPDATE CWM_SYS_RES  set ICON='more1' where ALIAS='DicManager';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='SysScript';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='ConditionScript';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='PersonScript';
		UPDATE CWM_SYS_RES  set ICON='security' where ALIAS='HiddenFunction';
		UPDATE CWM_SYS_RES  set ICON='folder' where ALIAS='ShareFIFile';
		UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='formQuery';
		UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='sqlquery';
		UPDATE CWM_SYS_RES  set ICON='atm' where ALIAS='InnerMessage';
		UPDATE CWM_SYS_RES  set ICON='feedback' where ALIAS='readmessage';
		UPDATE CWM_SYS_RES  set ICON='share' where ALIAS='sendmessage';
		UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='sendMsg';
		UPDATE CWM_SYS_RES  set ICON='email' where ALIAS='mail';
		UPDATE CWM_SYS_RES  set ICON='email' where ALIAS='outMailAll';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='lookMail';
		UPDATE CWM_SYS_RES  set ICON='wrong' where ALIAS='delMail';
		UPDATE CWM_SYS_RES  set ICON='inquirytemplate' where ALIAS='mailAdd';
		UPDATE CWM_SYS_RES  set ICON='save' where ALIAS='saveOutmail';
		UPDATE CWM_SYS_RES  set ICON='signout' where ALIAS='sendMail';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='mailManage';
		UPDATE CWM_SYS_RES  set ICON='add' where ALIAS='addMail';
		UPDATE CWM_SYS_RES  set ICON='print' where ALIAS='setDefault';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='editOutmail';
		UPDATE CWM_SYS_RES  set ICON='wrong' where ALIAS='deloutMail';
		UPDATE CWM_SYS_RES  set ICON='feedback' where ALIAS='消息模板管理';
		UPDATE CWM_SYS_RES  set ICON='map' where ALIAS='otherTools';
		UPDATE CWM_SYS_RES  set ICON='reject' where ALIAS='manageTask';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='hysrcck';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='ParametersView';
		UPDATE CWM_SYS_RES  set ICON='save' where ALIAS='BackAndRestore';
		UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='SysIndexManage';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='SysIndexLayoutManage';
		UPDATE CWM_SYS_RES  set ICON='electronics' where ALIAS='sysIndexMyLayout';
		UPDATE CWM_SYS_RES  set ICON='4column' where ALIAS='SysIndexColumn';
		UPDATE CWM_SYS_RES  set ICON='move' where ALIAS='SysIndexLayout';
		UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='DBomView';
		UPDATE CWM_SYS_RES  set ICON='filter' where ALIAS='SealManage';
		UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='test';
		UPDATE CWM_SYS_RES  set ICON='form' where ALIAS='ReportManage';
		UPDATE CWM_SYS_RES  set ICON='favorite' where ALIAS='FinereportManage';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='PageOfficeManage';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='myStartPro';
		UPDATE CWM_SYS_RES  set ICON='imagetext' where ALIAS='myUndertakePro';
		UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='myRequest';
		UPDATE CWM_SYS_RES  set ICON='templatedefault' where ALIAS='myCompleted';
		UPDATE CWM_SYS_RES  set ICON='circle' where ALIAS='myDelegate';
		UPDATE CWM_SYS_RES  set ICON='pic' where ALIAS='indexManage';
		UPDATE CWM_SYS_RES  set ICON='mainpage' where ALIAS='idexPortal';
		UPDATE CWM_SYS_RES  set ICON='4column' where ALIAS='columnType';
		UPDATE CWM_SYS_RES  set ICON='3column' where ALIAS='portalColumn';
		UPDATE CWM_SYS_RES  set ICON='rfq' where ALIAS='newsManage';
		UPDATE CWM_SYS_RES  set ICON='edit' where ALIAS='newsComment';
		UPDATE CWM_SYS_RES  set ICON='calendar' where ALIAS='agenda';
		UPDATE CWM_SYS_RES  set ICON='table' where ALIAS='myAgenda';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='agendaMsg';
		UPDATE CWM_SYS_RES  set ICON='search' where ALIAS='serachAgenda';
		UPDATE CWM_SYS_RES  set ICON='gerenzhongxin' where ALIAS='zhcl';
		UPDATE CWM_SYS_RES  set ICON='data' where ALIAS='sjypzgl';
		UPDATE CWM_SYS_RES  set ICON='text' where ALIAS='sjymb';
		UPDATE CWM_SYS_RES  set ICON='electrical' where ALIAS='sjy';
		UPDATE CWM_SYS_RES  set ICON='security' where ALIAS='xmqxgl';
		UPDATE CWM_SYS_RES  set ICON='imagetext' where ALIAS='xmgndlb';
		UPDATE CWM_SYS_RES  set ICON='reject' where ALIAS='xmjslb';
		UPDATE CWM_SYS_RES  set ICON='set' where ALIAS='testte';
  END IF;
END;


COMMIT;
END synstruct_yangbo;