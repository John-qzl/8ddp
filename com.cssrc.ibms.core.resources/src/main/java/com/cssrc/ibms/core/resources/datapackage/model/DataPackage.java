package com.cssrc.ibms.core.resources.datapackage.model;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "dataPackage")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataPackage {
	public DataPackage() {
		
	}
	public DataPackage(Map<String, Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.sjmc=CommonTools.Obj2String(map.get("F_sjmc"));
		this.sjlx=CommonTools.Obj2String(map.get("F_sjlx"));
		this.sjz=CommonTools.Obj2String(map.get("F_sjz"));
		this.gw=CommonTools.Obj2String(map.get("F_gw"));
		this.mj=CommonTools.Obj2String(map.get("F_mj"));
		this.bmqx=CommonTools.Obj2String(map.get("F_bmqx"));
		this.bb=CommonTools.Obj2String(map.get("F_bb"));
		this.sm=CommonTools.Obj2String(map.get("F_sm"));
		this.spjd=CommonTools.Obj2String(map.get("F_spjd"));
		this.zxzt=CommonTools.Obj2String(map.get("F_zxzt"));
		this.wcsj=map.get("F_wcsj")==null?null:(Date)map.get("F_wcsj");
		this.sssjb=CommonTools.Obj2String(map.get("F_sssjb"));
		this.ssmb=CommonTools.Obj2String(map.get("F_ssmb"));
		this.ssmbmc=CommonTools.Obj2String(map.get("F_ssmbmc"));
		this.ssrw=CommonTools.Obj2String(map.get("F_ssrw"));
		this.acceptancePlanId=CommonTools.Obj2String(map.get("F_acceptancePlanId"));
		this.productBatchId=CommonTools.Obj2String(map.get("F_productBatchId"));
		this.templateId=CommonTools.Obj2String(map.get("F_templateId"));
		this.templateName=CommonTools.Obj2String(map.get("F_templateName"));
		this.productCategoryId=CommonTools.Obj2String(map.get("F_productCategoryId"));
		this.productId=CommonTools.Obj2String(map.get("F_productId"));
		this.scr=CommonTools.Obj2String(map.get("F_scr"));
		this.scsj=map.get("F_scsj")==null?null:(Date)map.get("F_scsj");
		this.scrId=CommonTools.Obj2String(map.get("F_scrId"));
	}

	@XmlAttribute
	private String id; 
	
	@XmlAttribute
	private String sjmc; 
	
	@XmlAttribute
	private String sjlx; 
	
	@XmlAttribute
	private String sjz;

    @XmlAttribute
	private String gw; 
	
	@XmlAttribute
	private String mj;

    @XmlAttribute
	private String bmqx; 
	
	@XmlAttribute
	private String bb;

    @XmlAttribute
	private String sm; 
	
	@XmlAttribute
	private String spjd;

    @XmlAttribute
	private String zxzt; 
	
	@XmlAttribute
	private Date wcsj;

    @XmlAttribute
	private String sssjb; 
	
	@XmlAttribute
	private String ssmb;

    @XmlAttribute
	private String ssmbmc; 
	
	@XmlAttribute
	private String ssrw;

    @XmlAttribute
	private String acceptancePlanId; 
	
	@XmlAttribute
	private String productBatchId;

    @XmlAttribute
	private String templateId; 
	
	@XmlAttribute
	private String templateName;

    @XmlAttribute
	private String productCategoryId; 
	
	@XmlAttribute
	private String productId;

    @XmlAttribute
	private String scr; 
	
	@XmlAttribute
	private Date scsj;

    @XmlAttribute
	private String scrId;

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSjmc() {
		return sjmc;
	}
	public void setSjmc(String sjmc) {
		this.sjmc = sjmc;
	}
	public String getSjlx() {
		return sjlx;
	}
	public void setSjlx(String sjlx) {
		this.sjlx = sjlx;
	}
	public String getSjz() {
		return sjz;
	}
	public void setSjz(String sjz) {
		this.sjz = sjz;
	}
	public String getGw() {
		return gw;
	}
	public void setGw(String gw) {
		this.gw = gw;
	}
	public String getMj() {
		return mj;
	}
	public void setMj(String mj) {
		this.mj = mj;
	}
	public String getBmqx() {
		return bmqx;
	}
	public void setBmqx(String bmqx) {
		this.bmqx = bmqx;
	}
	public String getBb() {
		return bb;
	}
	public void setBb(String bb) {
		this.bb = bb;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getSpjd() {
		return spjd;
	}
	public void setSpjd(String spjd) {
		this.spjd = spjd;
	}
	public String getZxzt() {
		return zxzt;
	}
	public void setZxzt(String zxzt) {
		this.zxzt = zxzt;
	}
	public Date getWcsj() {
		return scsj;
	}
	public void setWcsj(Date wcsj) {
		this.wcsj = wcsj;
	}
	public String getSssjb() {
		return sssjb;
	}
	public void setSssjb(String sssjb) {
		this.sssjb = sssjb;
	}
	public String getSsmb() {
		return ssmb;
	}
	public void setSsmb(String ssmb) {
		this.ssmb = ssmb;
	}
	public String getSsmbmc() {
		return ssmbmc;
	}
	public void setSsmbmc(String ssmbmc) {
		this.ssmbmc = ssmbmc;
	}
	public String getSsrw() {
		return ssrw;
	}
	public void setSsrw(String ssrw) {
		this.ssrw = ssrw;
	}
	public String getAcceptancePlanId() {
		return acceptancePlanId;
	}
	public void setAcceptancePlanId(String acceptancePlanId) {
		this.acceptancePlanId = acceptancePlanId;
	}
	public String getProductBatchId() {
		return productBatchId;
	}
	public void setProductBatchId(String productBatchId) {
		this.productBatchId = productBatchId;
	}
	public String getTemplateId() {
		return templateId;
	}
	public void setTemplateId(String templateId) {
		this.templateId = templateId;
	}
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public String getProductCategoryId() {
		return productCategoryId;
	}
	public void setProductCategoryId(String productCategoryId) {
		this.productCategoryId = productCategoryId;
	}
	public String getProductId() {
		return productId;
	}
	public void setProductId(String productId) {
		this.productId = productId;
	}
	public String getScr() {
		return scr;
	}
	public void setScr(String scr) {
		this.scr = scr;
	}
	public Date getScsj() {
		return scsj;
	}
	public void setScsj(Date scsj) {
		this.scsj = scsj;
	}
	public String getScrId() {
		return scrId;
	}
	public void setScrId(String scrId) {
		this.scrId = scrId;
	} 

    
}
