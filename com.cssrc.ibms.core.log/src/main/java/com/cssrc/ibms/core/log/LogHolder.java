package com.cssrc.ibms.core.log;

import java.util.Map;

import com.cssrc.ibms.core.log.model.SysLog;


public class LogHolder {
	SysLog syslog;
	boolean needParse = false;
	Map<String, Object> parseDataModel;
    Map<String, Object> callbackData;

	public boolean isNeedParse() {
		return this.needParse;
	}

	public void setNeedParse(boolean needParse) {
		this.needParse = needParse;
	}

	public Map<String, Object> getParseDataModel() {
		return this.parseDataModel;
	}

	public void setParseDataModel(Map<String, Object> parseDataModel) {
		this.parseDataModel = parseDataModel;
	}

	public SysLog getSyslog() {
		return syslog;
	}

	public void setSyslog(SysLog syslog) {
		this.syslog = syslog;
	}

    public void setCallBackData(Map<String, Object> param)
    {
        this.callbackData=param;
    }
    
    public Map<String, Object> getCallBackData()
    {
       return this.callbackData;
    }
}
