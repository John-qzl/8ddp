package com.cssrc.ibms.statistics.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.statistics.model.ITool;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;


/**
 * Description:数据统计工具
 * <p>Tool.java</p>
 * @author dengwenjie 
 * @date 2017年7月4日
 */
public class Tool extends BaseModel implements Cloneable,ITool{
	/*id*/
	protected Long toolId;
	/*工具名称*/
	protected String name;
	/*别名*/
	protected String alias;
	/*说明*/
	protected String toolDesc;
	/**
	 * 仅用于生成树结构
	 */
	protected Long parentId = Tool.ROOT_TOOL_ID;
	
	public Long getToolId() {
		return toolId;
	}

	public void setToolId(Long toolId) {
		this.toolId = toolId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getToolDesc() {
		return toolDesc;
	}

	public void setToolDesc(String toolDesc) {
		this.toolDesc = toolDesc;
	}
	
	/**
	 * @param parentId
	 * @return
	 */
	public static Tool getRootTool() {
		Tool root = new Tool();
		root.setToolId(Tool.ROOT_TOOL_ID);
		root.setName(Tool.ROOT_TOOL_NAME);
		root.setAlias(Tool.ROOT_TOOL_ALIAS);
		root.setParentId(-1L);
		return root;
	}
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof Tool)) 
		{
			return false;
		}
		Tool rhs = (Tool) object;
		return new EqualsBuilder()
		.append(this.toolId, rhs.toolId)
		.append(this.alias, rhs.alias)
		.isEquals();
	}
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.toolId) 
		.append(this.name) 
		.append(this.alias) 
		.append(this.toolDesc) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("toolId", this.toolId)
		.append("name", this.name)
		.append("alias", this.alias)		 
		.append("toolDesc", this.toolDesc)
		.toString();
	}
}
