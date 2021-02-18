package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;

import com.cssrc.ibms.api.sysuser.model.ISysOrgType;

public interface ISysOrgTypeService{

	public abstract Integer getMaxLevel(Long demId);

	public abstract List<?extends ISysOrgType> getByDemId(long demId);

	public abstract ISysOrgType getById(Long orgType);

}