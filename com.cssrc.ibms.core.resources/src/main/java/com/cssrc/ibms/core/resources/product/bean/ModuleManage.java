package com.cssrc.ibms.core.resources.product.bean;

import java.text.ParseException;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "moduleManage")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModuleManage {
	public ModuleManage()
	{}
	
	public ModuleManage(Map<String,Object> map) {
		this.id= CommonTools.Obj2String(map.get("ID"));
		this.ssxh= CommonTools.Obj2String(map.get("F_ssxh"));
		this.qx= CommonTools.Obj2String(map.get("F_qx"));
		this.ry= CommonTools.Obj2String(map.get("F_ry"));
		this.ryId= CommonTools.Obj2String(map.get("F_ryId"));
		this.ssxhID= CommonTools.Obj2String(map.get("F_ssxhID"));
		this.bm= CommonTools.Obj2String(map.get("F_bm"));
		this.bmId= CommonTools.Obj2String(map.get("F_bmId"));
	}
	@XmlAttribute
	private String id;
	@XmlAttribute
	private String ssxh;
	@XmlAttribute
	private String qx;
	@XmlAttribute
	private String ry;
	@XmlAttribute
	private String ryId;
	@XmlAttribute
	private String ssxhID;
	@XmlAttribute
	private String bm;
	@XmlAttribute
	private String bmId;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSsxh() {
		return ssxh;
	}
	public void setSsxh(String ssxh) {
		this.ssxh = ssxh;
	}
	public String getQx() {
		return qx;
	}
	public void setQx(String qx) {
		this.qx = qx;
	}
	public String getRy() {
		return ry;
	}
	public void setRy(String ry) {
		this.ry = ry;
	}
	public String getRyId() {
		return ryId;
	}
	public void setRyId(String ryId) {
		this.ryId = ryId;
	}
	public String getSsxhID() {
		return ssxhID;
	}
	public void setSsxhID(String ssxhID) {
		this.ssxhID = ssxhID;
	}
	public String getBm() {
		return bm;
	}
	public void setBm(String bm) {
		this.bm = bm;
	}
	public String getBmId() {
		return bmId;
	}
	public void setBmId(String bmId) {
		this.bmId = bmId;
	}
}
