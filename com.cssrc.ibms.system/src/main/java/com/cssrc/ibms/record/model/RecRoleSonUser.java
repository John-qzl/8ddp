package com.cssrc.ibms.record.model;


import com.cssrc.ibms.api.rec.model.IRecRoleSonUser;
import org.apache.commons.lang.builder.EqualsBuilder;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class RecRoleSonUser  extends BaseModel  implements Cloneable,IRecRoleSonUser{
	protected Long roleSonId;//表单角色id		
	protected Long userId;//用户id
	
	protected String fullname;	//中文名称
	protected String username; //账号，登陆用户名就是user表的username
	
	protected String orgName; //组织名称
	
	protected String phone; //电话
	
	protected String mobile; //手机	
	
	public Long getRoleSonId() {
		return roleSonId;
	}
	

	public void setRoleSonId(Long roleSonId) {
		this.roleSonId = roleSonId;
	}

	public Long getUserId() {
		return userId;
	}


	public void setUserId(Long userId) {
		this.userId = userId;
	}


	public String getFullname() {
		return fullname;
	}


	public void setFullname(String fullname) {
		this.fullname = fullname;
	}


	public String getUsername() {
		return username;
	}


	public void setUsername(String username) {
		this.username = username;
	}


	public String getOrgName() {
		return orgName;
	}


	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}


	public Object clone()
	{
		RecRoleSonUser obj = null;
		try {
			obj = (RecRoleSonUser)super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public boolean equals(Object o){
		if (!(o instanceof RecRoleSonUser)) {
			return false;
		}
		RecRoleSonUser rhs = (RecRoleSonUser) o;
		return new EqualsBuilder().append(this.userId, rhs.userId).isEquals();	
	}
}
