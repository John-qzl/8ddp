package com.cssrc.ibms.api.migration.model;

import java.util.List;

public interface IOutDicGlobalType{
	String getTypeName();
	Long getSn();	
	String getNodeKey();
	List<IOutDictionary> getDicList();
	Long getDepId();
	Long getParentId();
}
