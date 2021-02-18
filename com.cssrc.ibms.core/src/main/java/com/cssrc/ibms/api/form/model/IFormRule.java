package com.cssrc.ibms.api.form.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


public interface IFormRule {


	public void setId(Long id);
	/**
	 * 返回 id
	 * @return
	 */
	public Long getId();

	public void setName(String name);
	/**
	 * 返回 规则名
	 * @return
	 */
	public String getName();

	public void setRule(String rule);
	/**
	 * 返回 规则
	 * @return
	 */
	public String getRule();

	public void setMemo(String memo);
	/**
	 * 返回 描述
	 * @return
	 */
	public String getMemo();

	public void setTipInfo(String tipInfo);
	/**
	 * 返回 错误提示信息
	 * @return
	 */
	public String getTipInfo();

   


	
   
  


}
