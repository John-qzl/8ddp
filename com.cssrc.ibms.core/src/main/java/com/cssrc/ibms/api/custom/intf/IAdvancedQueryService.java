package com.cssrc.ibms.api.custom.intf;

import java.util.List;

import com.cssrc.ibms.api.custom.model.IAdvancedQuery;

public interface IAdvancedQueryService {
	public List<IAdvancedQuery> getAdvancedQuery(String displayId);
	public  IAdvancedQuery getAdvancedQuery(String displayId,String queryKey);
}
