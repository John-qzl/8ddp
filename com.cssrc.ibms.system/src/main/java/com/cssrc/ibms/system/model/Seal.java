package com.cssrc.ibms.system.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:电子印章 Model对象.
 * 
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] WeiLei <br/>
 *         [创建时间] 2016-8-22 上午11:13:01 <br/>
 *         [修改人] WeiLei <br/>
 *         [修改时间] 2016-8-22 上午11:13:01
 * @see
 */
@XmlRootElement(name = "Seal")
@XmlAccessorType(XmlAccessType.NONE)
public class Seal extends BaseModel implements Cloneable {

	private static final long serialVersionUID = 1L;
	
	/** 印章ID*/
	protected Long sealId;
	
	/** 印章名*/
	protected String sealName;
	
	/** 印章路径*/
	protected String sealPath;
	
	/** 印章持有者ID*/
	protected Long belongId;
	
	/** 印章持有者姓名*/
	protected String belongName;
	
	/** 印章附件ID*/
	protected Long attachmentId;
	
	/** 印章图片ID*/
	protected Long showImageId;

	public void setSealId(Long sealId) {
		this.sealId = sealId;
	}

	public Long getSealId() {
		return this.sealId;
	}

	public void setSealName(String sealName) {
		this.sealName = sealName;
	}

	public String getSealName() {
		return this.sealName;
	}

	public void setSealPath(String sealPath) {
		this.sealPath = sealPath;
	}

	public String getSealPath() {
		return this.sealPath;
	}

	public void setBelongId(Long belongId) {
		this.belongId = belongId;
	}

	public Long getBelongId() {
		return this.belongId;
	}

	public void setBelongName(String belongName) {
		this.belongName = belongName;
	}

	public String getBelongName() {
		return this.belongName;
	}

	public void setAttachmentId(Long attachmentId) {
		this.attachmentId = attachmentId;
	}

	public Long getAttachmentId() {
		return this.attachmentId;
	}

	public Long getShowImageId() {
		return this.showImageId;
	}

	public void setShowImageId(Long showImageId) {
		this.showImageId = showImageId;
	}

	public boolean equals(Object object) {
		
		if (!(object instanceof Seal)) {
			return false;
		}
		Seal rhs = (Seal) object;
		return new EqualsBuilder().append(this.sealId, rhs.sealId).append(
				this.sealName, rhs.sealName)
				.append(this.sealPath, rhs.sealPath).append(this.belongId,
						rhs.belongId).append(this.belongName, rhs.belongName)
				.append(this.attachmentId, rhs.attachmentId).append(
						this.showImageId, rhs.showImageId).isEquals();
	}

	public int hashCode() {
		
		return new HashCodeBuilder(-82280557, -700257973).append(this.sealId)
				.append(this.sealName).append(this.sealPath).append(
						this.belongId).append(this.belongName).append(
						this.attachmentId).append(this.showImageId)
				.toHashCode();
	}

	public String toString() {
		
		return new ToStringBuilder(this).append("sealId", this.sealId).append(
				"sealName", this.sealName).append("sealPath", this.sealPath)
				.append("belongId", this.belongId).append("belongName",
						this.belongName).append("attachmentId",
						this.attachmentId).append("showImageId",
						this.showImageId).toString();
	}
}
