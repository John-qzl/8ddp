package com.cssrc.ibms.core.resources.io.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "ProductCategoryBath")
@XmlAccessorType(XmlAccessType.FIELD)
public class ProductCategoryBath {

	 public ProductCategoryBath(Map<String, Object> map) {
		 this.id=Long.valueOf(CommonTools.Obj2String(map.get("ID")));
		this.cpdh = CommonTools.Obj2String(map.get("F_cpdh"));
		this.cpmc = CommonTools.Obj2String(map.get("F_cpmc"));
		this.ssxh = CommonTools.Obj2String(map.get("F_ssxh"));
		this.xhjd = CommonTools.Obj2String(map.get("F_xhjd"));
		this.zrbm = CommonTools.Obj2String(map.get("F_zrbm"));
		this.cybm = CommonTools.Obj2String(map.get("F_cybm"));
		this.lbhpc = CommonTools.Obj2String(map.get("F_lbhpc"));
		this.pch = CommonTools.Obj2String(map.get("F_pch"));
		this.sl = CommonTools.Obj2String(map.get("F_sl"));
		this.ssxhfc = CommonTools.Obj2String(map.get("F_ssxhfc"));
		this.ssxhpc = CommonTools.Obj2String(map.get("F_ssxhpc"));
		this.ssxhjd = CommonTools.Obj2String(map.get("F_ssxhjd"));
		this.sscplb = CommonTools.Obj2String(map.get("F_sscplb"));
		this.yzdw = CommonTools.Obj2String(map.get("F_yzdw"));
	}
	 public ProductCategoryBath() {
		 
	 }
	
	@XmlAttribute
	private Long id;
	
	@XmlAttribute
	private String cpdh;
	
	@XmlAttribute
	private String cpmc;
	
	@XmlAttribute
	private String ssxh;
	
	@XmlAttribute
	private String xhjd;
	
	@XmlAttribute
	private String zrbm;
	
	@XmlAttribute
	private String cybm;
	
	@XmlAttribute
	private String lbhpc;
	
	@XmlAttribute
	private String pch;
	
	@XmlAttribute
	private String sl;
	
	@XmlAttribute
	private String ssxhfc;
	
	@XmlAttribute
	private String ssxhpc;
	
	@XmlAttribute
	private String ssxhjd;
	
	@XmlAttribute
	private String sscplb;
	
	@XmlAttribute
	private String yzdw;
	
	@XmlAttribute
	private String zrbmId;
	
	
	@XmlAttribute
	private String cybmId;
	
	@XmlAttribute
	private String yzdwId;

	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getCpdh() {
		return cpdh;
	}
	public void setCpdh(String cpdh) {
		this.cpdh = cpdh;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
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
	public String getZrbm() {
		return zrbm;
	}
	public void setZrbm(String zrbm) {
		this.zrbm = zrbm;
	}
	public String getCybm() {
		return cybm;
	}
	public void setCybm(String cybm) {
		this.cybm = cybm;
	}
	public String getLbhpc() {
		return lbhpc;
	}
	public void setLbhpc(String lbhpc) {
		this.lbhpc = lbhpc;
	}
	public String getPch() {
		return pch;
	}
	public void setPch(String pch) {
		this.pch = pch;
	}
	public String getSl() {
		return sl;
	}
	public void setSl(String sl) {
		this.sl = sl;
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
	public String getSscplb() {
		return sscplb;
	}
	public void setSscplb(String sscplb) {
		this.sscplb = sscplb;
	}
	public String getYzdw() {
		return yzdw;
	}
	public void setYzdw(String yzdw) {
		this.yzdw = yzdw;
	}
	public String getZrbmId() {
		return zrbmId;
	}
	public void setZrbmId(String zrbmId) {
		this.zrbmId = zrbmId;
	}
	public String getCybmId() {
		return cybmId;
	}
	public void setCybmId(String cybmId) {
		this.cybmId = cybmId;
	}
	public String getYzdwId() {
		return yzdwId;
	}
	public void setYzdwId(String yzdwId) {
		this.yzdwId = yzdwId;
	}
	
	
}
