package com.cssrc.ibms.core.user.model;

import java.util.Date;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
/**
 * 下属管理
 * <p>Title:UserUnder</p>
 * @author Yangbo 
 * @date 2016-8-4下午09:18:47
 */
public class UserUnder extends BaseModel implements IUserUnder{
	@SysFieldDescription(detail="ID")
	protected Long id;
	@SysFieldDescription(detail="用户ID")
	protected Long userid;
	@SysFieldDescription(detail="下属ID")
	protected Long underuserid; //下属id
	@SysFieldDescription(detail="下属名")
	protected String underusername;//下属名
	@SysFieldDescription(detail="领导名称")
	protected String leaderName = "";
	@SysFieldDescription(detail="下属管理表创建人ID")
	protected Long userunder_creatorId;// 创建人ID
	@SysFieldDescription(detail="下属管理表创建时间")
	protected Date userunder_createTime;// 创建时间
	@SysFieldDescription(detail="下属管理表更改人ID")
	protected Long userunder_updateId;// 更改人ID
	@SysFieldDescription(detail="下属管理表更改时间")
	protected Date userunder_updateTime;// 更改时间
	@SysFieldDescription(detail="下属管理表是否删除")
	protected Short userunder_delFlag;// 是否删除
	
	public Long getUserunder_creatorId() {
		return userunder_creatorId;
	}

	public void setUserunder_creatorId(Long userunder_creatorId) {
		this.userunder_creatorId = userunder_creatorId;
	}

	public Date getUserunder_createTime() {
		return userunder_createTime;
	}

	public void setUserunder_createTime(Date userunder_createTime) {
		this.userunder_createTime = userunder_createTime;
	}

	public Long getUserunder_updateId() {
		return userunder_updateId;
	}

	public void setUserunder_updateId(Long userunder_updateId) {
		this.userunder_updateId = userunder_updateId;
	}

	public Date getUserunder_updateTime() {
		return userunder_updateTime;
	}

	public void setUserunder_updateTime(Date userunder_updateTime) {
		this.userunder_updateTime = userunder_updateTime;
	}

	public Short getUserunder_delFlag() {
		return userunder_delFlag;
	}

	public void setUserunder_delFlag(Short userunder_delFlag) {
		this.userunder_delFlag = userunder_delFlag;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getId() {
		return this.id;
	}

	public void setUserid(Long userid) {
		this.userid = userid;
	}

	public Long getUserid() {
		return this.userid;
	}

	public void setUnderuserid(Long underuserid) {
		this.underuserid = underuserid;
	}

	public Long getUnderuserid() {
		return this.underuserid;
	}

	public void setUnderusername(String underusername) {
		this.underusername = underusername;
	}

	public String getUnderusername() {
		return this.underusername;
	}

	public String getLeaderName() {
		return this.leaderName;
	}

	public void setLeaderName(String leaderName) {
		this.leaderName = leaderName;
	}

	public boolean equals(Object object) {
		if (!(object instanceof UserUnder)) {
			return false;
		}
		UserUnder rhs = (UserUnder) object;
		return new EqualsBuilder().append(this.id, rhs.id).append(this.userid,
				rhs.userid).append(this.underuserid, rhs.underuserid).append(
				this.underusername, rhs.underusername).isEquals();
	}

	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973).append(this.id)
				.append(this.userid).append(this.underuserid).append(
						this.underusername).toHashCode();
	}

	public String toString() {
		return new ToStringBuilder(this).append("id", this.id).append("userid",
				this.userid).append("underuserid", this.underuserid).append(
				"underusername", this.underusername).toString();
	}
}
