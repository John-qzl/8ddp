package com.cssrc.ibms.system.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.system.model.ISysScript;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
 
/**
 * 对象功能:自定义表代码模版 Model对象
 * @author zhulongchao
 *
 */
public class SysScript extends BaseModel implements ISysScript
{
	// 主键
	protected Long  id;
	// 名称
	protected String  name;
	// 
	protected String  script;
	// 
	protected String  category;
	// 
	protected String  memo;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getScript() {
		return script;
	}
	public void setScript(String script) {
		this.script = script;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
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
	
	public void setMemo(String memo) 
	{
		this.memo = memo;
	}
	
	
	
	/**
	 * 返回 模版备注
	 * @return
	 */
	public String getMemo() 
	{
		return this.memo;
	}
	
	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof SysScript)) 
		{
			return false;
		}
		SysScript rhs = (SysScript) object;
		return new EqualsBuilder()
		.append(this.id, rhs.id)
		.append(this.name, rhs.name)
		.append(this.script, rhs.script)
		.append(this.category, rhs.category)
		.append(this.memo, rhs.memo)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.id) 
		.append(this.name) 
		.append(this.script) 
		.append(this.category) 
		.append(this.memo) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("id", this.id) 
		.append("name", this.name) 
		.append("script", this.script) 
		.append("category", this.category) 
		.append("memo", this.memo) 
		.toString();
	}
   
	

}