package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "actualData")
@XmlAccessorType(XmlAccessType.FIELD)
public class ActualData {
	@XmlAttribute
	String id;
	
	@XmlAttribute
	String ysxm;
	
	@XmlAttribute
	String sscp;
	
	@XmlAttribute
	String dw;
	
	@XmlAttribute
	String yqz;
	
	@XmlAttribute
	String scz;
	
	@XmlAttribute
	String cpmc;
	
	@XmlAttribute
	String slid;
	
	@XmlAttribute
	String jldw;
	
	@XmlAttribute
	String czyq;
	
	@XmlAttribute
	String sfhg;
	
	
	public ActualData(Map<String,Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.ysxm=CommonTools.Obj2String(map.get("F_ysxm"));
		this.sscp=CommonTools.Obj2String(map.get("F_sscp"));
		this.dw=CommonTools.Obj2String(map.get("F_dw"));
		this.yqz=CommonTools.Obj2String(map.get("F_yqz"));
		this.scz=CommonTools.Obj2String(map.get("F_scz"));
		this.cpmc=CommonTools.Obj2String(map.get("F_cpmc"));
		this.slid=CommonTools.Obj2String(map.get("F_slid"));
		this.jldw=CommonTools.Obj2String(map.get("F_jldw"));
		this.czyq=CommonTools.Obj2String(map.get("F_czyq"));
		this.sfhg=CommonTools.Obj2String(map.get("F_sfhg"));
	}
	
	public ActualData() {
		// TODO Auto-generated constructor stub
	}
	
	public String getSfhg() {
		return sfhg;
	}

	public void setSfhg(String sfhg) {
		this.sfhg = sfhg;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		id = id;
	}
	public String getYsxm() {
		return ysxm;
	}
	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}
	public String getSscp() {
		return sscp;
	}
	public void setSscp(String sscp) {
		this.sscp = sscp;
	}
	public String getDw() {
		return dw;
	}
	public void setDw(String dw) {
		this.dw = dw;
	}
	public String getYqz() {
		return yqz;
	}
	public void setYqz(String yqz) {
		this.yqz = yqz;
	}
	public String getScz() {
		return scz;
	}
	public void setScz(String scz) {
		this.scz = scz;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getSlid() {
		return slid;
	}
	public void setSlid(String slid) {
		this.slid = slid;
	}
	public String getJldw() {
		return jldw;
	}
	public void setJldw(String jldw) {
		this.jldw = jldw;
	}
	public String getCzyq() {
		return czyq;
	}
	public void setCzyq(String czyq) {
		this.czyq = czyq;
	}
	
}
