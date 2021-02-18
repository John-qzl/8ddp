package com.cssrc.ibms.core.form.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
import com.cssrc.ibms.api.form.model.BaseFormRights;
import com.cssrc.ibms.api.form.model.IFormRights;

/**
 * 对象功能:字段权限 Model对象 开发人员:zhulongchao
 */
@XmlRootElement(name = "formRights")
@XmlAccessorType(XmlAccessType.NONE)
public class FormRights extends BaseFormRights implements Cloneable, IFormRights {

    private static final long serialVersionUID = 8734533541139921672L;
    // id
	@XmlAttribute
	protected Long id;
	// 表单定义KEY
	@XmlAttribute
	protected Long formDefId;
	// 字段名
	@XmlAttribute
	protected String name;
	// 权限
	@XmlAttribute
	protected String permission = "";
	// 权限类型(1,字段FieldRights ,2,子表TableRights,,3,意见OpinionRights,
	// 4.子表是否显示TableShowRights,5.rel表TableRelRights ,6.
	// rel表是否显示TableRelShowRights ,7.附件文件列表权限AttachFileRights)
	@XmlAttribute
	protected short type = 1;
	// 流程定义ID
	@XmlAttribute
	protected String actDefId = "";

	@XmlAttribute
	protected String parentActDefId = "";

	// 流程任务ID
	@XmlAttribute
	protected String nodeId = "";
	@XmlAttribute
	protected Integer sn = 0;

	@XmlAttribute
	protected Integer platform = Integer.valueOf(0);

	public FormRights() {
	}

	public FormRights(Long id, Long formDefId, String name, String permission,
			short type) {
		super();
		this.id = id;
		this.formDefId = formDefId;
		this.name = name;
		this.permission = permission;
		this.type = type;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * 返回 id
	 * 
	 * @return
	 */
	public Long getId() {
		return id;
	}

	public void setFormDefId(Long formDefId) {
		this.formDefId = formDefId;
	}

	/**
	 * 返回 表单定义ID
	 * 
	 * @return
	 */
	public Long getFormDefId() {
		return formDefId;
	}

	public void setName(String fieldName) {
		this.name = fieldName;
	}

	/**
	 * 返回 字段名
	 * 
	 * @return
	 */
	public String getName() {
		return name;
	}

	public void setPermission(String permission) {
		this.permission = permission;
	}

	/**
	 * 返回 权限
	 * 
	 * @return
	 */
	public String getPermission() {
		return permission;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getActDefId() {
		return actDefId;
	}

	public void setActDefId(String actDefId) {
		this.actDefId = actDefId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getSn() {
		return sn;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	public String getParentActDefId() {
		return this.parentActDefId;
	}

	public void setParentActDefId(String parentActDefId) {
		this.parentActDefId = parentActDefId;
	}

	public Integer getPlatform() {
		return this.platform;
	}

	public void setPlatform(Integer platform) {
		this.platform = platform;
	}

	/**
	 * @see java.lang.Object#equals(Object)
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FormRights)) {
			return false;
		}
		FormRights rhs = (FormRights) object;
		return new EqualsBuilder().append(this.id, rhs.id)
				.append(this.formDefId, rhs.formDefId)
				.append(this.name, rhs.name)
				.append(this.permission, rhs.permission).isEquals();
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.formDefId).append(this.name)
				.append(this.permission).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("id", this.id)
				.append("formDefId", this.formDefId)
				.append("fieldName", this.name)
				.append("permission", this.permission).toString();
	}

	public Object clone() {
		FormRights obj = null;
		try {
			obj = (FormRights) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

}