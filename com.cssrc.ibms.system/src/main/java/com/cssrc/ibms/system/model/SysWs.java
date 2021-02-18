package com.cssrc.ibms.system.model;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

 
public class SysWs extends BaseModel
{
	// 主键
	protected Long  id;
	// 别名
	protected String  alias;
	// wsdl地址
	protected String  wsdlUrl;
	// webservice设置
	protected String  document;
	//通用webservice调用参数列表
	protected List<SysWsParams> sysWsParamsList=new ArrayList<SysWsParams>();
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * 返回 主键
	 * @return
	 */
	public Long getId() 
	{
		return this.id;
	}
	public void setAlias(String alias) 
	{
		this.alias = alias;
	}
	/**
	 * 返回 别名
	 * @return
	 */
	public String getAlias() 
	{
		return this.alias;
	}
	public void setWsdlUrl(String wsdlUrl) 
	{
		this.wsdlUrl = wsdlUrl;
	}
	/**
	 * 返回 wsdl地址
	 * @return
	 */
	public String getWsdlUrl() 
	{
		return this.wsdlUrl;
	}
	public void setDocument(String document) 
	{
		this.document = document;
	}
	/**
	 * 返回 webservice设置
	 * @return
	 */
	public String getDocument() 
	{
		return this.document;
	}
	public List<SysWsParams> getSysWsParamsList() {
		return sysWsParamsList;
	}
	public void setSysWsParamsList(List<SysWsParams> sysWsParamsList) {
		this.sysWsParamsList = sysWsParamsList;
	}

   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysWs)) 
		{
			return false;
		}
		SysWs rhs = (SysWs) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.alias, rhs.alias)
		.append(this.wsdlUrl, rhs.wsdlUrl)
		.append(this.document, rhs.document)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.alias) 
		.append(this.wsdlUrl) 
		.append(this.document) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("alias", this.alias) 
		.append("wsdlUrl", this.wsdlUrl) 
		.append("document", this.document) 
		.toString();
	}

   
  

}