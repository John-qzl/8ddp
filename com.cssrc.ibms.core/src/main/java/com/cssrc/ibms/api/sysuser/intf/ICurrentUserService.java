package com.cssrc.ibms.api.sysuser.intf;

import java.util.List;
import java.util.Map;

public interface ICurrentUserService {

	public abstract Map<String, List<Long>> getUserRelation();

}