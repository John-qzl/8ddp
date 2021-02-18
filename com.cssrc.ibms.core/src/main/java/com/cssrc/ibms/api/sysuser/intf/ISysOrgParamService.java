package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.ISysOrgParam;

public interface ISysOrgParamService {

	public abstract List<?extends ISysOrgParam> getByOrgId(Long orgId);

	public abstract List<?extends ISysOrgParam> getListByOrgId(Long orgId);

	public abstract ISysOrgParam getByParamKeyAndOrgId(String paramKey,
			Long orgId);

}