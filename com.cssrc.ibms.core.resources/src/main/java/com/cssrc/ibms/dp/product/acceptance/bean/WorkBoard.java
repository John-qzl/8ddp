package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * W_GZXKB
 */
@XmlRootElement(name = "workBoard")
@XmlAccessorType(XmlAccessType.FIELD)
public class WorkBoard {

	public WorkBoard() {
		
	}
	
	public WorkBoard(Map<String,Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.zjID=CommonTools.Obj2String(map.get("F_zjID"));
		this.lx=CommonTools.Obj2String(map.get("F_lx"));
		this.gzm=CommonTools.Obj2String(map.get("F_gzm"));
		this.dqzt=CommonTools.Obj2String(map.get("F_dqzt"));
		this.xyb=CommonTools.Obj2String(map.get("F_xyb"));
		this.zz=CommonTools.Obj2String(map.get("F_zz"));
		this.zzId=CommonTools.Obj2String(map.get("F_zzId"));
		this.fqr=CommonTools.Obj2String(map.get("F_FQR"));
		this.fqrId=CommonTools.Obj2String(map.get("F_FQRID"));

	}
	
	@XmlAttribute
	String id;
	
	@XmlAttribute
	String zjID;
	
	@XmlAttribute
	String lx;
	
	@XmlAttribute
	String gzm;
	
	@XmlAttribute
	String dqzt;
	
	@XmlAttribute
	String xyb;
	
	@XmlAttribute
	String zz;

	@XmlAttribute
	String zzId;

	@XmlElement(name = "fqr", required = false)
	String fqr;

	@XmlElement(name = "fqrId", required = false)
	String fqrId;

	public String getFqr() {
		return fqr;
	}

	public void setFqr(String fqr) {
		this.fqr = fqr;
	}

	public String getFqrId() {
		return fqrId;
	}

	public void setFqrId(String fqrId) {
		this.fqrId = fqrId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZjID() {
		return zjID;
	}

	public void setZjID(String zjID) {
		this.zjID = zjID;
	}

	public String getLx() {
		return lx;
	}

	public void setLx(String lx) {
		this.lx = lx;
	}

	public String getGzm() {
		return gzm;
	}

	public void setGzm(String gzm) {
		this.gzm = gzm;
	}

	public String getDqzt() {
		return dqzt;
	}

	public void setDqzt(String dqzt) {
		this.dqzt = dqzt;
	}

	public String getXyb() {
		return xyb;
	}

	public void setXyb(String xyb) {
		this.xyb = xyb;
	}

	public String getZz() {
		return zz;
	}

	public void setZz(String zz) {
		this.zz = zz;
	}

	public String getZzId() {
		return zzId;
	}

	public void setZzId(String zzId) {
		this.zzId = zzId;
	}
}
