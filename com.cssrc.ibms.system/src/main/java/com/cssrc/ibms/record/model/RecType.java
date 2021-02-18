package com.cssrc.ibms.record.model;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.rec.model.IRecType;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;

/**
 * Description:表单类型
 * <p>RecType.java</p>
 * @author dengwenjie 
 * @date 2017年3月11日
 */
public class RecType extends BaseModel implements Cloneable,IRecType{
	// 表单类型id
	@SysFieldDescription(detail="表单类型id")
	protected Long typeId;
	// 表单类型名称
	@SysFieldDescription(detail="表单类型名称")
	protected String typeName;
	// 表单类型别名
	@SysFieldDescription(detail="表单类型别名")
	protected String alias;
	// 表单类型描述
	@SysFieldDescription(detail="表单类型描述")
	protected String typeDesc;
	// 所有的RecType的parentId = 0，平级
	@SysFieldDescription(detail="父级id")
	protected Long parentId = Long.valueOf(0L);
	
	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getTypeDesc() {
		return typeDesc;
	}

	public void setTypeDesc(String typeDesc) {
		this.typeDesc = typeDesc;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof RecType)) 
		{
			return false;
		}
		RecType rhs = (RecType) object;
		return new EqualsBuilder()
		.append(this.typeId, rhs.typeId)
		.append(this.typeName, rhs.typeName)
		.append(this.alias, rhs.alias)		 
		.append(this.typeDesc, rhs.typeDesc)
		.isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.typeId) 
		.append(this.typeName) 
		.append(this.alias) 
		.append(this.typeDesc) 
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("typeId", this.typeId)
		.append("typeName", this.typeName)
		.append("alias", this.alias)		 
		.append("typeDesc", this.typeDesc)
		.toString();
	}
}
