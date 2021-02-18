package com.cssrc.ibms.bus.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 
 * <p>Title:BusQueryShare</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:14:17
 */
public class BusQueryShare extends BaseModel {
	private static final long serialVersionUID = -6818930311080038741L;
	protected Long id;
	protected Long filterId;
	protected String shareRight;
	protected Long sharerId;
	protected Date createtime;

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setFilterId(Long filterId) {
		this.filterId = filterId;
	}

	public Long getFilterId() {
		return this.filterId;
	}

	public void setShareRight(String shareRight) {
		this.shareRight = shareRight;
	}

	public String getShareRight() {
		return this.shareRight;
	}

	public void setSharerId(Long sharerId) {
		this.sharerId = sharerId;
	}

	public Long getSharerId() {
		return this.sharerId;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}

	public Date getCreatetime() {
		return this.createtime;
	}

	public boolean equals(Object object) {
		if (!(object instanceof BusQueryShare)) {
			return false;
		}
		BusQueryShare rhs = (BusQueryShare) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(
				this.filterId, rhs.filterId).append(this.shareRight,
				rhs.shareRight).append(this.sharerId, rhs.sharerId).append(
				this.createtime, rhs.createtime).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.filterId).append(this.shareRight).append(
						this.sharerId).append(this.createtime).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append(
				"filterId", this.filterId)
				.append("shareRight", this.shareRight).append("shareId",
						this.sharerId).append("createtime", this.createtime)
				.toString();
	}
}
