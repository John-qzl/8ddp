package com.cssrc.ibms.core.resources.io.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author wenjie
 * 型号信息(可导入到当前型号下的任意发次)
 */
@XmlRootElement(name = "product")
@XmlAccessorType(XmlAccessType.FIELD)
public class Product {
	public Product() {
		
	}
	public Product(Map<String, Object> map) {
		this.xhdh = CommonTools.Obj2String(map.get("F_XHDH"));
		this.xhmc = CommonTools.Obj2String(map.get("F_XHMC"));
		this.id = CommonTools.Obj2String(map.get("ID"));
	}
	/**
	 * 主键
	 */
	@XmlAttribute
	private String id;
	/**
	 * 型号代号
	 */
	@XmlAttribute
	private String xhdh;
	/**
	 * 型号名称
	 */
	@XmlAttribute
	private String xhmc;

	/**
	 * 型号
	 */
	@XmlElement(name="project")
	private	Project project;
	
	public String getXhdh() {
		return xhdh;
	}

	public void setXhdh(String xhdh) {
		this.xhdh = xhdh;
	}

	public String getXhmc() {
		return xhmc;
	}

	public void setXhmc(String xhmc) {
		this.xhmc = xhmc;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public Project getProject() {
		return project;
	}
	public void setProject(Project project) {
		this.project = project;
	}
}
