package com.cssrc.ibms.statistics.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.statistics.model.IAddress;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;


/**
 * Description:数据统计访问地址
 * <p>Address.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
public class Address extends BaseModel implements Cloneable,IAddress{
	/*主键*/
	protected Long addressId;
	/*所属统计工具*/
	protected Long toolId;
	/*别名*/
	protected String alias;
	/*访问路径*/
	protected String url;
	/*展示视图定制*/
	protected String viewDef;
	/*访问路径说明*/
	protected String addressDesc;
	
	
	public Long getAddressId() {
		return addressId;
	}

	public void setAddressId(Long addressId) {
		this.addressId = addressId;
	}

	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getViewDef() {
		return viewDef;
	}

	public void setViewDef(String viewDef) {
		this.viewDef = viewDef;
	}

	public String getAddressDesc() {
		return addressDesc;
	}

	public void setAddressDesc(String addressDesc) {
		this.addressDesc = addressDesc;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Address)) 
		{
			return false;
		}
		Address rhs = (Address) object;
		return new EqualsBuilder()
		.append(this.addressId, rhs.addressId)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.addressId) 
		.append(this.toolId) 
		.append(this.url) 
		.append(this.viewDef) 
		.append(this.addressDesc) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("addressId", this.addressId)
		.append("toolId", this.toolId)
		.append("url", this.url)		 
		.append("viewDef", this.viewDef)
		.append("addressDesc", this.addressDesc)
		.toString();
	}
}
