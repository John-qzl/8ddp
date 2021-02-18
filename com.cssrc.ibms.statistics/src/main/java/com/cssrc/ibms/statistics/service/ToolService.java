package com.cssrc.ibms.statistics.service;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.statistics.intf.IToolService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.statistics.dao.ToolDao;
import com.cssrc.ibms.statistics.model.Tool;


/**
 * <p>ToolService.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
@Service
public class ToolService  extends BaseService<Tool> implements IToolService{
	@Resource
	private ToolDao toolDao;
	
	protected IEntityDao<Tool, Long> getEntityDao() {
		return this.toolDao;
	}
	
	/**
	 * 是否存在该类别
	 * 
	 * @param function
	 * @return
	 */
	public Integer isAliasExists(String alias) {
		return this.toolDao.isAliasExists(alias);
	}
	
	/**
	 * @param alias : 别名
	 * @return
	 */
	public Tool getByAlias(String alias){
		return this.toolDao.getByAlias(alias);
	}
	
}
