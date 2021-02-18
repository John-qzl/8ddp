package com.cssrc.ibms.core.resources.ioOld2New.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "signresList")
@XmlAccessorType(XmlAccessType.FIELD)
public class SignResIOModel {
	
	@XmlAttribute
	private String signdefid;
	@XmlAttribute
	private String instanceid;
	@XmlAttribute
	private String signtime;
	
	@XmlElement(name ="sign")
	private SignIOModel sign;
	@XmlElement(name ="signfile")
	private SignFileModel signfile;
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String name;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSigntime() {
		return signtime;
	}
	public void setSigntime(String signtime) {
		this.signtime = signtime;
	}
	public String getSigndefid() {
		return signdefid;
	}
	public void setSigndefid(String signdefid) {
		this.signdefid = signdefid;
	}
	public String getInstanceid() {
		return instanceid;
	}
	public void setInstanceid(String instanceid) {
		this.instanceid = instanceid;
	}
	public SignIOModel getSign() {
		return sign;
	}
	public void setSign(SignIOModel sign) {
		this.sign = sign;
	}
	public SignFileModel getSignfile() {
		return signfile;
	}
	public void setSignfile(SignFileModel signfile) {
		this.signfile = signfile;
	}
	
	
}
