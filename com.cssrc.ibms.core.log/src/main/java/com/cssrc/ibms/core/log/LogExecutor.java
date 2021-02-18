package com.cssrc.ibms.core.log;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.log.intf.AbsLogAspect;
import com.cssrc.ibms.core.log.intf.ILogAspect;
import com.cssrc.ibms.core.log.model.SysLog;
import com.cssrc.ibms.core.log.service.SysLogService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import freemarker.template.TemplateException;

public class LogExecutor implements Runnable {
	private Log logger = LogFactory.getLog(LogExecutor.class);
	private LogHolder logHolder;
	private FreemarkEngine freemarkEngine;
	private SysLogService sysLogService;
	private ILogAspect service;
	public LogExecutor(ILogAspect service)
    {
	    this.service=service;
    }
    public LogExecutor()
    {
    }

    public void setLogHolders(LogHolder logHolder) {
		this.logHolder = logHolder;
		this.sysLogService = (SysLogService) AppUtil.getBean(SysLogService.class);
		this.freemarkEngine = (FreemarkEngine) AppUtil.getBean(FreemarkEngine.class);
	}

	private void doLog() throws TemplateException, IOException {
		SysLog log = logHolder.getSyslog();
		if (logHolder.isNeedParse()) {
			String template=log.getDetail().replace("SysAuditLinkService", "sysAuditLinkService");
			String detail =  freemarkEngine.parseByStringTemplate(logHolder.getParseDataModel(), template);
			log.setDetail(detail);
		}
		if(StringUtil.isNotEmpty(log.getDetail())){
			sysLogService.add(log);
		}
		service.callBack(log,logHolder.getCallBackData());
	}

	@Override
	public void run() {
		try {
			doLog();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

}
