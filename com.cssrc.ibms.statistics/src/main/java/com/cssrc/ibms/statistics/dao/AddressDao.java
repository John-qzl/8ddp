package com.cssrc.ibms.statistics.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.statistics.model.Address;


/**
 * <p>AddressDao.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
@Repository
public class AddressDao  extends BaseDao<Address> {
	public Class getEntityClass() {
		return Address.class;
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
	public Address getByAlias(String alias){
		return (Address)getOne("getByAlias", alias);
	}
	
	/**
	 * @param toolId : 统计工具ID
	 * @return ： 统计工具ID下所有地址
	 */
	public List<Address> getByToolId(Long toolId){
		return this.getBySqlKey("getByToolId", toolId);
	}
	
	/**
	 * @param alias : 地址别名
	 * @param toolId ：统计工具ID
	 * @return
	 */
	public Address getByAliasToolId(String alias,Long toolId){
		Map map = new HashMap();
		map.put("alias", alias);
		map.put("toolId", toolId);
		return (Address)this.getOne("getByAliasToolId", map);
	}
}
