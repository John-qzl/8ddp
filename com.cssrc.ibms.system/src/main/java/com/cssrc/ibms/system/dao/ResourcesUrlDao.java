package com.cssrc.ibms.system.dao;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.ResourcesUrl;
import com.cssrc.ibms.system.model.ResourcesUrlExt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;
/**
 * 默认URL方法
 * <p>Title:ResourcesUrlDao</p>
 * @author Yangbo 
 * @date 2016-8-22下午09:33:54
 */
@Repository
public class ResourcesUrlDao extends BaseDao<ResourcesUrl> {
	public Class getEntityClass() {
		return ResourcesUrl.class;
	}

	public List<ResourcesUrl> getByResId(long resId) {
		return getBySqlKey("getByResId", Long.valueOf(resId));
	}

	public void delByResId(long resId) {
		delBySqlKey("delByResId", Long.valueOf(resId));
	}


	public List<ResourcesUrlExt> getUrlAndRoleByUrlSystemAlias(String sysAlias,
			String url) {
		Map params = new HashMap();
		params.put("sysAlias", sysAlias);
		params.put("url", url);

		String statment = getIbatisMapperNamespace()
				+ ".getUrlAndRoleByUrlSystemAlias";
		return getSqlSessionTemplate().selectList(statment, params);
	}
}
