package com.cssrc.ibms.core.resources.io.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "fileData")
@XmlAccessorType(XmlAccessType.FIELD)
public class FileData {

	
	public FileData() {
	
	}
	
	public FileData(Map<String,Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.mz=CommonTools.Obj2String(map.get("F_MZ"));
		this.planId=CommonTools.Obj2String(map.get("F_PLANID"));
		this.sjId=CommonTools.Obj2String(map.get("F_SJID"));
		this.sjlb=CommonTools.Obj2String(map.get("F_SJLB"));
		this.wjlj=CommonTools.Obj2String(map.get("F_WJLJ"));
	}
	
	
	@XmlAttribute
	String id;
	
	@XmlAttribute
	String mz;
	
	@XmlAttribute
	String planId;
	
	@XmlAttribute
	String sjId;
	
	@XmlAttribute
	String sjlb;
	
	@XmlAttribute
	String wjlj;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getMz() {
		return mz;
	}
	public void setMz(String mz) {
		this.mz = mz;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getSjId() {
		return sjId;
	}
	public void setSjId(String sjId) {
		this.sjId = sjId;
	}
	public String getSjlb() {
		return sjlb;
	}
	public void setSjlb(String sjlb) {
		this.sjlb = sjlb;
	}
	public String getWjlj() {
		return wjlj;
	}
	public void setWjlj(String wjlj) {
		this.wjlj = wjlj;
	}
	
	
	
}
