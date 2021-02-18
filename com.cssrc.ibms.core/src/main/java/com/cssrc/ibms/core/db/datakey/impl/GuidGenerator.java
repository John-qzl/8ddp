package com.cssrc.ibms.core.db.datakey.impl;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.datakey.IKeyGenerator;


public class GuidGenerator implements IKeyGenerator{
	@Override
	public Object nextId() throws Exception {
		return UniqueIdUtil.getGuid();
	}

	@Override
	public void setAlias(String alias) {
	}
}
