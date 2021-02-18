package com.cssrc.ibms.system.model;

import java.util.Date;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
/**
 * 备份还原by yangbo
 * SysBackUpRestore entity.
 */

public class SysBackUpRestore  extends BaseModel { 
	protected Long backid;
	protected String backname;
	protected Date datetime;
	protected String username;
	protected String comments;

	public Long getBackid() {
		return backid;
	}


	public void setBackid(Long backid) {
		this.backid = backid;
	}


	public String getBackname() {
		return backname;
	}


	public void setBackname(String backname) {
		this.backname = backname;
	}


	public Date getDatetime() {
		return datetime;
	}


	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getComments() {
		return comments;
	}


	public void setComments(String comments) {
		this.comments = comments;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}
	
	

	public boolean equals(Object object) {
		if (!(object instanceof SysBackUpRestore)) {
			return false;
		}
		SysBackUpRestore rhs = (SysBackUpRestore) object;
		return new EqualsBuilder().append(this.backid, rhs.backid).append(
				this.backname, rhs.backname).append(this.datetime, rhs.datetime)
				.append(this.comments, rhs.comments)
				.append(this.username, rhs.username).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.backid)
				.append(this.backname).append(this.datetime).append(this.comments)
				.append(this.username).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("backid", this.backid).append(
				"backname", this.backname).append("datetime", this.datetime)
				.append("comments", this.comments).append("username", this.username).toString();
	}
	


	

     


    




   

    
    



}