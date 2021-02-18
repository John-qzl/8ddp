package com.cssrc.ibms.share.model;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;
/**
 * 
 * <p>Title:SysShareRights</p>
 * @author Yangbo 
 * @date 2016-8-8下午02:00:48
 */
public class SysShareRights extends BaseModel {
	private static final long serialVersionUID = 1L;
	public static final short YES = 1;
	public static final short NO = 0;
	protected Long id;
	protected String name;
	protected String shareItem;
	protected short enable;
	protected short sync;
	protected short isAll;
	protected Long pkid;
	protected String source;
	protected String target;
	protected String shares;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

	public void setEnable(short enable) {
		this.enable = enable;
	}

	public short getEnable() {
		return this.enable;
	}

	public void setSync(short sync) {
		this.sync = sync;
	}

	public short getSync() {
		return this.sync;
	}

	public void setIsAll(short isAll) {
		this.isAll = isAll;
	}

	public short getIsAll() {
		return this.isAll;
	}

	public void setPkid(Long pkid) {
		this.pkid = pkid;
	}

	public Long getPkid() {
		return this.pkid;
	}

	public void setSource(String source) {
		this.source = source;
	}

	public String getSource() {
		return this.source;
	}

	public void setTarget(String target) {
		this.target = target;
	}

	public String getTarget() {
		return this.target;
	}

	public void setShares(String shares) {
		this.shares = shares;
	}

	public String getShares() {
		return this.shares;
	}

	public boolean equals(Object object) {
		if (!(object instanceof SysShareRights)) {
			return false;
		}
		SysShareRights rhs = (SysShareRights) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.name,
				rhs.name).append(this.enable, rhs.enable).append(this.sync,
				rhs.sync).append(this.isAll, rhs.isAll).append(this.pkid,
				rhs.pkid).append(this.source, rhs.source).append(this.target,
				rhs.target).append(this.shares, rhs.shares).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.name).append(this.enable).append(this.sync)
				.append(this.isAll).append(this.pkid).append(this.source)
				.append(this.target).append(this.shares).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("name",
				this.name).append("enable", this.enable).append("sync",
				this.sync).append("isAll", this.isAll)
				.append("pkid", this.pkid).append("source", this.source)
				.append("target", this.target).append("shares", this.shares)
				.toString();
	}

	public String getShareItem() {
		return this.shareItem;
	}

	public void setShareItem(String shareItem) {
		this.shareItem = shareItem;
	}
}
