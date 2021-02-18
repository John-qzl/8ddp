package com.cssrc.ibms.dbom.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

/**
 * 对象功能:DBom分类管理 Model对象.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-13 上午08:45:25 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-8-20 上午08:42:58
 * @see
 */
@XmlRootElement(name = "DBom")
@XmlAccessorType(XmlAccessType.NONE)
public class DBom extends BaseModel implements Cloneable{

	private static final long serialVersionUID = 1L;

	private Long id;
	@XmlAttribute
	private String code;
	@XmlAttribute
	private String name;
	@XmlAttribute
	private String username;
	@XmlAttribute
	private Date modifiedTime;
	@XmlAttribute
	private String description;

	public DBom() {
	}

	public DBom(Long id, String code, String name,
			String username, Date modifiedTime, String description) {
		this.id = id;
		this.code = code;
		this.name = name;
		this.username = username;
		this.modifiedTime = modifiedTime;
		this.description = description;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCode() {
		return this.code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getUsername() {
		return this.username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public Date getModifiedTime() {
		return this.modifiedTime;
	}

	public void setModifiedTime(Date modifiedTime) {
		this.modifiedTime = modifiedTime;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
