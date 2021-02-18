package com.cssrc.ibms.statistics.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.statistics.model.Tool;


/**
 * <p>ToolDao.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
@Repository
public class ToolDao  extends BaseDao<Tool> {
	public Class getEntityClass() {
		return Tool.class;
	}
	/**
	 * 别名是否存在
	 * @param alias
	 * @return
	 */
	public Integer isAliasExists(String alias) {
		Map params = new HashMap();
		params.put("alias", alias);
		return (Integer) getOne("isAliasExists", params);
	}
	public Tool getByAlias(String alias){
		return (Tool)getOne("getByAlias", alias);
	}
}
