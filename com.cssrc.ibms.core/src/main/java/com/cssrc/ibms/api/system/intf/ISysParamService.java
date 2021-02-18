package com.cssrc.ibms.api.system.intf;

import java.util.List;
import com.cssrc.ibms.api.system.model.ISysParam;

public interface ISysParamService{

	ISysParam getById(Long valueOf);

	List getDistinctCategory(Integer type, Long dimId);

	List getOrgParam(long demId);

	List<? extends ISysParam> getAll();

	List getOrgParam();

	List getStatusParam();

	List getUserParam();

}