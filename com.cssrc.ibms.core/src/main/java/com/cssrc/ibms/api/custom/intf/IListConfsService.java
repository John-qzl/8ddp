package com.cssrc.ibms.api.custom.intf;

import java.util.Map;

import com.cssrc.ibms.api.custom.model.IListConfs;


public interface IListConfsService {
	public String getDataListHtml(Long userId,Map<String,Object> params);
	public void del();
	public IListConfs getListConfs();
}
