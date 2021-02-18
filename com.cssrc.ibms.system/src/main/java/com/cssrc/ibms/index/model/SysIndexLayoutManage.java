package com.cssrc.ibms.index.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * @author Yangbo 2016-7-20
 *
 */
public class SysIndexLayoutManage extends BaseModel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected Long id;
	protected String name;
	protected String memo;
	protected Long orgId;
	protected String templateHtml;
	protected String designHtml;
	protected Short isDef;
	protected String orgName;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return this.memo;
	}

	public void setOrgId(Long orgId) {
		this.orgId = orgId;
	}

	public Long getOrgId() {
		return this.orgId;
	}

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getTemplateHtml() {
		return this.templateHtml;
	}

	public void setDesignHtml(String designHtml) {
		this.designHtml = designHtml;
	}

	public String getDesignHtml() {
		return this.designHtml;
	}

	public void setIsDef(Short isDef) {
		this.isDef = isDef;
	}

	public Short getIsDef() {
		return this.isDef;
	}

	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysIndexLayoutManage)) {
			return false;
		}
		SysIndexLayoutManage rhs = (SysIndexLayoutManage) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name,
				rhs.name).append(this.memo, rhs.memo).append(this.orgId,
				rhs.orgId).append(this.templateHtml, rhs.templateHtml).append(
				this.designHtml, rhs.designHtml).append(this.isDef, rhs.isDef)
				.isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.memo).append(this.orgId).append(
						this.templateHtml).append(this.designHtml).append(
						this.isDef).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name",
				this.name).append("memo", this.memo)
				.append("orgId", this.orgId).append("templateHtml",
						this.templateHtml)
				.append("designHtml", this.designHtml).append("isDef",
						this.isDef).toString();
	}
}
