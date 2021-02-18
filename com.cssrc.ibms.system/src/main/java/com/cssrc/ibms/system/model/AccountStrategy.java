package com.cssrc.ibms.system.model;

import com.cssrc.ibms.api.system.model.IAccountStrategy;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * AccountStrategy
 * @author liubo
 * @date 2017年3月30日
 */
public class AccountStrategy extends BaseModel implements IAccountStrategy{
	
	//策略id（序号）
	@SysFieldDescription(detail="策略id（序号）")
	private Long id;
	//策略名称
	@SysFieldDescription(detail="策略名称")
	private String strategy_name;
	//策略说明
	@SysFieldDescription(detail="策略说明")
	private String strategy_explain;
	//策略是否有效---1表示有效；0表示无效
	@SysFieldDescription(detail="策略是否有效",maps="{\"1\":\"有效\",\"0\":\"无效\"}")
	private String is_enable;
	//策略值
	@SysFieldDescription(detail="策略值")
	private String strategy_value;
	//策略的类型---1表示账户策略；0表示密码策略
	@SysFieldDescription(detail="策略的类型",maps="{\"1\":\"账户策略\",\"0\":\"密码策略\"}")
	private String strategy_type;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getStrategy_name() {
		return strategy_name;
	}
	public void setStrategy_name(String strategy_name) {
		this.strategy_name = strategy_name;
	}
	public String getStrategy_explain() {
		return strategy_explain;
	}
	public void setStrategy_explain(String strategy_explain) {
		this.strategy_explain = strategy_explain;
	}
	public String getIs_enable() {
		return is_enable;
	}
	public void setIs_enable(String is_enable) {
		this.is_enable = is_enable;
	}
	public String getStrategy_value() {
		return strategy_value;
	}
	public void setStrategy_value(String strategy_value) {
		this.strategy_value = strategy_value;
	}
	public String getStrategy_type() {
		return strategy_type;
	}
	public void setStrategy_type(String strategy_type) {
		this.strategy_type = strategy_type;
	}
	
}
