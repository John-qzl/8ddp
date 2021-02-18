package com.cssrc.ibms.core.resources.util.listener;

import java.util.List;

import com.cssrc.ibms.system.model.SysFile;
import com.fr.report.core.A.T;

public class FileMoveEvenet extends BaseEvent<T>{

	private static final long serialVersionUID = 1L;
	private List<SysFile> list;
	private String security;
	private String path;

	public FileMoveEvenet(Object source, String message,List<SysFile> list,String security,String path) {
		super(source, message);
		this.list=list;
		this.security=security;
		this.path=path;
	}

}
