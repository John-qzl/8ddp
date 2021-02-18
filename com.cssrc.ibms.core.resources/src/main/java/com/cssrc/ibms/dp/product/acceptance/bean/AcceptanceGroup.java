package com.cssrc.ibms.dp.product.acceptance.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * W_CPYSZB
 */
@XmlRootElement(name = "acceptanceGroup")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcceptanceGroup {
	
	
	public AcceptanceGroup() {
		// TODO Auto-generated constructor stub
	}
	
	public AcceptanceGroup(Map<String,Object> map) {
		this.id=CommonTools.Obj2String(map.get("ID"));
		this.zw=CommonTools.Obj2String(map.get("F_zw"));
		this.xm=CommonTools.Obj2String(map.get("F_xm"));
		this.dw=CommonTools.Obj2String(map.get("F_dw"));
		this.fzxm=CommonTools.Obj2String(map.get("F_fzxm"));
		this.sscp=CommonTools.Obj2String(map.get("F_sscp"));
		this.ssyszj=CommonTools.Obj2String(map.get("F_ssyszj"));
		this.qsId=CommonTools.Obj2String(map.get("F_qsID"));
		this.xmId=CommonTools.Obj2String(map.get("F_xmID"));
		this.dwId=CommonTools.Obj2String(map.get("F_dwID"));
		this.SSBCCH=CommonTools.Obj2String(map.get("F_SSBCCH"));
		this.SSBCSJQR=CommonTools.Obj2String(map.get("F_SSBCSJQR"));
	}
	@XmlAttribute
	private String id; //主键

	@XmlAttribute
	private String zw; 
	
	@XmlAttribute
	private String xm; 
	
	@XmlAttribute
	private String dw; 
	
	@XmlAttribute
	private String fzxm; 
	
	@XmlAttribute
	private String sscp; 
	
	@XmlAttribute
	private String ssyszj; 

	@XmlAttribute
	private String qsId; 
	

	@XmlAttribute
	private String xmId; 
	

	@XmlAttribute
	private String dwId;

	@XmlElement(name = "ssbcch", required = false)
	private String SSBCCH;

	@XmlElement(name = "ssbccsjqr", required = false)
	private String SSBCSJQR;

	public String getSSBCCH() {
		return SSBCCH;
	}

	public void setSSBCCH(String SSBCCH) {
		this.SSBCCH = SSBCCH;
	}

	public String getSSBCSJQR() {
		return SSBCSJQR;
	}

	public void setSSBCSJQR(String SSBCSJQR) {
		this.SSBCSJQR = SSBCSJQR;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getZw() {
		return zw;
	}

	public void setZw(String zw) {
		this.zw = zw;
	}

	public String getXm() {
		return xm;
	}

	public void setXm(String xm) {
		this.xm = xm;
	}

	public String getDw() {
		return dw;
	}

	public void setDw(String dw) {
		this.dw = dw;
	}

	public String getFzxm() {
		return fzxm;
	}

	public void setFzxm(String fzxm) {
		this.fzxm = fzxm;
	}

	public String getSscp() {
		return sscp;
	}

	public void setSscp(String sscp) {
		this.sscp = sscp;
	}

	public String getSsyszj() {
		return ssyszj;
	}

	public void setSsyszj(String ssyszj) {
		this.ssyszj = ssyszj;
	}

	public String getQsId() {
		return qsId;
	}

	public void setQsId(String qsId) {
		this.qsId = qsId;
	}

	public String getXmId() {
		return xmId;
	}

	public void setXmId(String xmId) {
		this.xmId = xmId;
	}

	public String getDwId() {
		return dwId;
	}

	public void setDwId(String dwId) {
		this.dwId = dwId;
	} 
}
