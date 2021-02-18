package com.cssrc.ibms.system.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:印章授权 Model对象.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-8-22 上午11:13:20 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-8-22 上午11:13:20
 * @see
 */
@XmlRootElement(name = "SealRight")
@XmlAccessorType(XmlAccessType.NONE)
public class SealRight extends BaseModel implements Cloneable{
	
	private static final long serialVersionUID = 1L;
	
	/** 控件类型 印章控件:0*/
	public static final Short CONTROL_TYPE_SEAL = Short.valueOf((short) 0);
	
	/** 控件类型 office控件:1*/
	public static final Short CONTROL_TYPE_OFFICE = Short.valueOf((short) 1);
	
	protected Long id;
	
	/** 印章ID*/
	protected Long sealId;
	
	/** 授权类型*/	
	protected String rightType;
	
	/** 被授权者ID*/
	protected Long rightId;
	
	/** 被授权者名称*/
	protected String rightName;
	
	/** 授权类型*/
	protected Long createUser;
	
	/** 创建时间*/
	private Date createTime;
	
	/** 控件类型 印章控件:0 office控件:1*/
	private Short controlType;

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getSealId() {
		return this.sealId;
	}

	public void setSealId(Long sealId) {
		this.sealId = sealId;
	}

	public String getRightType() {
		return this.rightType;
	}

	public void setRightType(String rightType) {
		this.rightType = rightType;
	}

	public Long getRightId() {
		return this.rightId;
	}

	public void setRightId(Long rightId) {
		this.rightId = rightId;
	}

	public String getRightName() {
		return this.rightName;
	}

	public void setRightName(String rightName) {
		this.rightName = rightName;
	}

	public Long getCreateUser() {
		return this.createUser;
	}

	public void setCreateUser(Long createUser) {
		this.createUser = createUser;
	}

	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public Short getControlType() {
		return this.controlType;
	}

	public void setControlType(Short controlType) {
		this.controlType = controlType;
	}
}
