package com.cssrc.ibms.system.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.system.model.AccountStrategy;

/**
 * AccountStrategyDao
 * @author liubo
 * @date 2017年3月30日
 */
@Repository
public class AccountStrategyDao extends BaseDao<AccountStrategy>{
    private static final Log log = LogFactory.getLog(AccountStrategyDao.class);

	@Override
	public Class<AccountStrategy> getEntityClass() {
		return AccountStrategy.class;
	}
	/**
	 * 通过策略id获取对应的策略信息
	 * @param strategyId
	 * @return
	 */
	public AccountStrategy getById(Long strategyId) {
		AccountStrategy accountStrategy = getUnique("getById", strategyId);
		return accountStrategy;
	}

	/**
	 * 通过策略名称获取对应的策略信息
	 * @param strategyName
	 * @return
	 */
	public AccountStrategy getByName(String strategyName){
		AccountStrategy accountStrategy = getUnique("getByName", Long.valueOf(strategyName));
		return accountStrategy;
	}
	
	/**
	 * 获取所有的策略信息
	 * @return
	 */
	public List<AccountStrategy> getAll(){
		return getBySqlKey("getAll");
	}
	
	/**
	 * 通过策略类型获取该类型的所有策略
	 * 1表示账户策略；0表示秘密策略
	 * @param strategyType
	 * @return
	 */
	public List<AccountStrategy> getByStrategyType(String strategyType){
		return getBySqlKey("getByStrategyType", strategyType);
	}
	
	/**
	 * 更新策略信息
	 * @param accountStrategy
	 */
	public void updateStrategy(AccountStrategy accountStrategy){
		Map<String,Object> params = new HashMap<String,Object>();
		params.put("strategyId", accountStrategy.getId());
		params.put("strategyExplain", accountStrategy.getStrategy_explain());
		params.put("strategyValue", accountStrategy.getStrategy_value());
		params.put("isEnable", accountStrategy.getIs_enable());
		update("updateStrategy", params);
	}
	
}
