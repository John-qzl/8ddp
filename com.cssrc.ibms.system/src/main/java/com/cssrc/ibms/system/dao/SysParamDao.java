package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.SysParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;
/**
 * 
 * <p>Title:SysParamDao</p>
 * @author Yangbo 
 * @date 2016-8-3上午09:47:14
 */
@Repository
public class SysParamDao extends BaseDao<SysParam> {
	public Class getEntityClass() {
		return SysParam.class;
	}

	public List<SysParam> getUserParam() {
		Map p = new HashMap();
		p.put("effect", Short.valueOf((short) 1));
		return getBySqlKey("getUserStatus", p);
	}

	public List<SysParam> getStatusParam() {
		Map p = new HashMap();
		p.put("effect", Short.valueOf((short) 1));
		return getBySqlKey("getStatusParam", p);
	}

	public List<SysParam> getOrgParam(long demId) {
		Map p = new HashMap();
		p.put("effect", Short.valueOf((short) 2));
		return getBySqlKey("getDemStatus", Long.valueOf(demId));
	}

	public List<SysParam> getOrgParam() {
		Map p = new HashMap();
		p.put("effect", Short.valueOf((short) 2));
		return getBySqlKey("getAll", p);
	}

	public SysParam getByParamKey(String paramKey) {
		return (SysParam) getUnique("getByParamKey", paramKey);
	}

	public List<String> getDistinctCategory(Integer type, Long dimId) {
		if ((type == null) || (type.intValue() == 1))
			dimId = null;
		HashMap map = new HashMap();

		map.put("effect", type);
		map.put("belongDim", dimId);
		return getBySqlKeyGenericity("getDistinctCategory", map);
	}
}
