package com.cssrc.ibms.core.flow.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 对象功能:常用语管理 Model对象 
 */
@XmlRootElement(name = "taskApprovalItems")
@XmlAccessorType(XmlAccessType.NONE)
public class TaskApprovalItems extends BaseModel
{	

	public final static Short global = 1; 
	
	public static final Short TYPE_FLOWTYPE = 2;

	public static final Short TYPE_FLOW = 3;

	public static final Short TYPE_USER = 4;
	
	public final static Short notGlobal = 0; 
	
	// 主键
	@XmlAttribute
	protected Long itemId;
	// 节点设置ID
	@XmlAttribute
	protected Long typeId;
	// actDefId
	@XmlAttribute
	protected String defKey; 
	// 
	@XmlAttribute
	protected Short type; 
	//流程节点KEY
	@XmlAttribute
	protected String defNodeKey; 

	//审批意见模板code
    @XmlAttribute
    protected String code;
    
    //流程节点KEY
    @XmlAttribute
    protected int default_;
    
	@XmlAttribute
	protected String expression;

	@XmlAttribute
	protected Long userId;

	public Long getItemId() {
		return itemId;
	}

	public void setItemId(Long itemId) {
		this.itemId = itemId;
	}

	 
	
   
   	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) 
	{
		if (!(object instanceof TaskApprovalItems)) 
		{
			return false;
		}
		TaskApprovalItems rhs = (TaskApprovalItems) object;
		return new EqualsBuilder()
		.append(this.itemId, rhs.itemId)
		.append(this.typeId, rhs.typeId)
		.append(this.defKey, rhs.defKey)
		.append(this.type, rhs.type) 
		.append(this.expression, rhs.expression)
        .append(this.userId, rhs.userId)
        .append(this.defNodeKey,rhs.defNodeKey)
		.isEquals();
	}
	
	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() 
	{
		return new HashCodeBuilder(-82280557, -700257973)
		.append(this.itemId) 
		.append(this.typeId) 
		.append(this.defKey)
		.append(this.type) 
	    .append(this.expression)
        .append(this.userId)
        .append(this.defNodeKey)
		.toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() 
	{
		return new ToStringBuilder(this)
		.append("itemId", this.itemId) 
		.append("typeId", this.typeId) 
		.append("defKey", this.defKey) 
		.append("type", this.type)  
		.append("expression", this.expression)
        .append("userId", this.userId)
        .append(this.defNodeKey)
		.toString();
	}

	public String getExpression() {
		return expression;
	}

	public void setExpression(String expression) {
		this.expression = expression;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public String getDefKey() {
		return defKey;
	}

	public void setDefKey(String defKey) {
		this.defKey = defKey;
	}

	public Short getType() {
		return type;
	}

	public void setType(Short type) {
		this.type = type;
	}

    public String getDefNodeKey()
    {
        return defNodeKey;
    }

    public void setDefNodeKey(String defNodeKey)
    {
        this.defNodeKey = defNodeKey;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public int getDefault_()
    {
        return default_;
    }

    public void setDefault_(int default_)
    {
        this.default_ = default_;
    }
   
}