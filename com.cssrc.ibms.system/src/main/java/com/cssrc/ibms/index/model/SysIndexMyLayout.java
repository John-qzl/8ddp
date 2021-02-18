package com.cssrc.ibms.index.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * @author Yangbo 2016-7-20
 *
 */
public class SysIndexMyLayout extends BaseModel {
	protected Long id;
	protected Long userId;
	protected String templateHtml;
	protected String designHtml;
	protected Long layoutId;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getUserId() {
		return this.userId;
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

	public void setLayoutId(Long layoutId) {
		this.layoutId = layoutId;
	}

	public Long getLayoutId() {
		return this.layoutId;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysIndexMyLayout)) {
			return false;
		}
		SysIndexMyLayout rhs = (SysIndexMyLayout) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.userId,
				rhs.userId).append(this.templateHtml, rhs.templateHtml).append(
				this.designHtml, rhs.designHtml).append(this.layoutId,
				rhs.layoutId).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.userId).append(this.templateHtml).append(
						this.designHtml).append(this.layoutId).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("userId",
				this.userId).append("templateHtml", this.templateHtml).append(
				"designHtml", this.designHtml)
				.append("layoutId", this.layoutId).toString();
	}
}
