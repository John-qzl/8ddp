package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "productInfo")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductInfo {
	
	
	
	public ProductInfo(Map<String, Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.sscppc=CommonTools.Obj2String(map.get("F_SSCPPC"));
		this.sscplb=CommonTools.Obj2String(map.get("F_SSCPLB"));
		this.cplbdh=CommonTools.Obj2String(map.get("F_SPLBDH"));
		this.cppcdh=CommonTools.Obj2String(map.get("F_CPPCDH"));
		this.cpmc=CommonTools.Obj2String(map.get("F_CPMC"));
		this.cpbh=CommonTools.Obj2String(map.get("F_CPBH"));
		this.ssxh=CommonTools.Obj2String(map.get("F_SSXH"));
		this.xhjd=CommonTools.Obj2String(map.get("F_XHJD"));
		this.ssxhfc=CommonTools.Obj2String(map.get("F_SSXHFC"));
		this.ssxhpc=CommonTools.Obj2String(map.get("F_SSXHPC"));
		this.ssxhjd=CommonTools.Obj2String(map.get("F_SSXHJD"));
		this.sspcch=CommonTools.Obj2String(map.get("F_SSPCCH"));
		this.pcchbh=CommonTools.Obj2String(map.get("F_PCCHBH"));
		this.planId=CommonTools.Obj2String(map.get("F_PLANID"));
		this.sfhg=CommonTools.Obj2String(map.get("F_SFHG"));
		this.ssslId=CommonTools.Obj2String(map.get("F_SSSLID"));
		this.jl=CommonTools.Obj2String(map.get("F_JL"));
	}
	
	
	
	public ProductInfo() {
		// TODO Auto-generated constructor stub
	}
	
	
	@XmlAttribute
	String id;
	
	@XmlAttribute
	String sscppc;
	
	@XmlAttribute
	String sscplb;
	
	@XmlAttribute
	String cplbdh;
	
	@XmlAttribute
	String cppcdh;
	
	@XmlAttribute
	String cpmc;
	
	@XmlAttribute
	String cpbh;
	
	@XmlAttribute
	String ssxh;
	
	@XmlAttribute
	String xhjd;
	
	@XmlAttribute
	String ssxhfc;
	
	@XmlAttribute
	String ssxhpc;
	
	@XmlAttribute
	String ssxhjd;
	
	@XmlAttribute
	String sspcch;
	
	@XmlAttribute
	String pcchbh;
	
	@XmlAttribute
	String planId;
	
	@XmlAttribute
	String sfhg;
	
	@XmlAttribute
	String ssslId;
	
	@XmlAttribute
	String jl;
	

	public String getId() {
		return id;
	}
	public void setId(String id) {
		id = id;
	}
	public String getSscppc() {
		return sscppc;
	}
	public void setSscppc(String sscppc) {
		this.sscppc = sscppc;
	}
	public String getSscplb() {
		return sscplb;
	}
	public void setSscplb(String sscplb) {
		this.sscplb = sscplb;
	}
	public String getCplbdh() {
		return cplbdh;
	}
	public void setCplbdh(String cplbdh) {
		this.cplbdh = cplbdh;
	}
	public String getCppcdh() {
		return cppcdh;
	}
	public void setCppcdh(String cppcdh) {
		this.cppcdh = cppcdh;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getCpbh() {
		return cpbh;
	}
	public void setCpbh(String cpbh) {
		this.cpbh = cpbh;
	}
	public String getSsxh() {
		return ssxh;
	}
	public void setSsxh(String ssxh) {
		this.ssxh = ssxh;
	}
	public String getXhjd() {
		return xhjd;
	}
	public void setXhjd(String xhjd) {
		this.xhjd = xhjd;
	}
	public String getSsxhfc() {
		return ssxhfc;
	}
	public void setSsxhfc(String ssxhfc) {
		this.ssxhfc = ssxhfc;
	}
	public String getSsxhpc() {
		return ssxhpc;
	}
	public void setSsxhpc(String ssxhpc) {
		this.ssxhpc = ssxhpc;
	}
	public String getSsxhjd() {
		return ssxhjd;
	}
	public void setSsxhjd(String ssxhjd) {
		this.ssxhjd = ssxhjd;
	}
	public String getSspcch() {
		return sspcch;
	}
	public void setSspcch(String sspcch) {
		this.sspcch = sspcch;
	}
	public String getPcchbh() {
		return pcchbh;
	}
	public void setPcchbh(String pcchbh) {
		this.pcchbh = pcchbh;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getSfhg() {
		return sfhg;
	}
	public void setSfhg(String sfhg) {
		this.sfhg = sfhg;
	}
	public String getSsslId() {
		return ssslId;
	}
	public void setSsslId(String ssslId) {
		this.ssslId = ssslId;
	}
	public String getJl() {
		return jl;
	}
	public void setJl(String jl) {
		this.jl = jl;
	}

}
