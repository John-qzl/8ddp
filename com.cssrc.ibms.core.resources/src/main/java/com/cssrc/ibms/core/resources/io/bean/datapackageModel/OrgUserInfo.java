package com.cssrc.ibms.core.resources.io.bean.datapackageModel;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;

@XmlRootElement(name = "OrgUserInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class OrgUserInfo {
	@XmlElement(name="sysUser")
	@XmlElementWrapper(name="userList")
	List<SysUser> userList;
	
	@XmlElement(name="sysOrg")
	@XmlElementWrapper(name="orgList")
	List<SysOrg> orgList;
	
	@XmlElement(name="position")
	@XmlElementWrapper(name="positionList")
	List<Position> positionList;
	
	@XmlElement(name="userPosition")
	@XmlElementWrapper(name="userPositionList")
	List<UserPosition> userPositionList;

	public List<SysUser> getUserList() {
		return userList;
	}

	public void setUserList(List<SysUser> userList) {
		this.userList = userList;
	}

	public List<SysOrg> getOrgList() {
		return orgList;
	}

	public void setOrgList(List<SysOrg> orgList) {
		this.orgList = orgList;
	}

	public List<Position> getPositionList() {
		return positionList;
	}
 
	public void setPositionList(List<Position> positionList) {
		this.positionList = positionList;
	}

	public List<UserPosition> getUserPositionList() {
		return userPositionList;
	}

	public void setUserPositionList(List<UserPosition> userPositionList) {
		this.userPositionList = userPositionList;
	}
	
	
}
