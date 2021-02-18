package com.cssrc.ibms.init.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.system.intf.ISystemInitService;
import com.cssrc.ibms.index.service.SysIndexColumnService;

@Service
public class SystemInitService implements ISystemInitService{
	@Resource
	IFormTemplateService formTemplateService;
	@Resource
	SysIndexColumnService sysIndexColumnService;
	
	@Override
	public void initDataTemplate() {
		formTemplateService.initTemplate();
	}

	@Override
	public void initIndex() {
		sysIndexColumnService.initIndex();
	}

	
	
}