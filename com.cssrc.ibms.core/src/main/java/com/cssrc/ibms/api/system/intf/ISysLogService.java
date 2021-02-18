package com.cssrc.ibms.api.system.intf;

import java.util.List;
import com.cssrc.ibms.api.system.model.ISysLog;

public interface ISysLogService{
	
	public abstract List<?extends ISysLog> getAll();
}