package com.cssrc.ibms.core.db.datakey.impl;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.datakey.IKeyGenerator;


/**
 * 时间序列产生器。
 * @author zhulongchao
 *
 */
public class TimeGenerator implements IKeyGenerator {

	@Override
	public Object nextId() throws Exception {
		// TODO Auto-generated method stub
		return UniqueIdUtil.genId();
	}

	@Override
	public void setAlias(String alias) {
	}

}
