package com.cssrc.ibms.statistics.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.statistics.intf.IAddressService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.statistics.dao.AddressDao;
import com.cssrc.ibms.statistics.model.Address;


/**
 * <p>AddressService.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
@Service
public class AddressService  extends BaseService<Address> implements IAddressService{
	@Resource
	private AddressDao addressDao;
	
	protected IEntityDao<Address, Long> getEntityDao() {
		return this.addressDao;
	}
	/**
	 * 是否存在该类别
	 * @param function
	 * @return
	 */
	public Integer isAliasExists(String alias) {
		return this.addressDao.isAliasExists(alias);
	}
	/**
	 * @param toolId : 统计工具ID
	 * @return ： 统计工具ID下所有地址
	 */
	public List<Address> getByToolId(Long toolId){
		return this.addressDao.getByToolId(toolId);
	}
	
	/**
	 * @param alias : 地址别名
	 * @param toolId ：统计工具ID
	 * @return
	 */
	public Address getByAliasToolId(String alias,Long toolId){
		return this.addressDao.getByAliasToolId(alias,toolId);
	}
}
