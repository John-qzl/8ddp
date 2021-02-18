package com.cssrc.ibms.index.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * @author Yangbo 2016-7-20
 *
 */
public class SysIndexLayout extends BaseModel {
	protected Long id;
	protected String name;
	protected String memo;
	protected String templateHtml;
	protected Long sn;

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

	public void setTemplateHtml(String templateHtml) {
		this.templateHtml = templateHtml;
	}

	public String getTemplateHtml() {
		return this.templateHtml;
	}

	public void setSn(Long sn) {
		this.sn = sn;
	}

	public Long getSn() {
		return this.sn;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysIndexLayout)) {
			return false;
		}
		SysIndexLayout rhs = (SysIndexLayout) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name,
				rhs.name).append(this.memo, rhs.memo).append(this.templateHtml,
				rhs.templateHtml).append(this.sn, rhs.sn).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.memo).append(this.templateHtml)
				.append(this.sn).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name",
				this.name).append("memo", this.memo).append("templateHtml",
				this.templateHtml).append("sn", this.sn).toString();
	}
}
