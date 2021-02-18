package com.cssrc.ibms.record.dao;

import java.util.HashMap;
import java.util.Map;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.record.model.RecType;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

/**
 * Description:
 * <p>RecFunTypeDao.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
@Repository
public class RecTypeDao  extends BaseDao<RecType> {
	public Class getEntityClass() {
		return RecType.class;
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
	public RecType getByAlias(String alias){
		return (RecType)getOne("getByAlias", alias);
	}
}
