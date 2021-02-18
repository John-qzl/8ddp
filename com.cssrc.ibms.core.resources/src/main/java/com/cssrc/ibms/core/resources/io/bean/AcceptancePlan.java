package com.cssrc.ibms.core.resources.io.bean;





import java.text.ParseException;
import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

@XmlRootElement(name = "dataObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class AcceptancePlan {
	public AcceptancePlan() {}
	public AcceptancePlan(Map<String,Object> map) throws ParseException {
		this.id     = CommonTools.Obj2String(map.get("ID"));
		this.cpmc   = CommonTools.Obj2String(map.get("F_cpmc"));
		this.cpdh   = CommonTools.Obj2String(map.get("F_cpdh"));
		this.ssxh   = CommonTools.Obj2String(map.get("F_ssxh"));
		this.ssxhID = CommonTools.Obj2String(map.get("F_ssxhID"));
 		this.yzjd   = CommonTools.Obj2String(map.get("F_yzjd"));
		this.yzdw   = CommonTools.Obj2String(map.get("F_yzdw"));
		
		this.ysrq=map.get("F_ysrq")==null?null:(Date)map.get("F_ysrq");
		
		this.cpbh   = CommonTools.Obj2String(map.get("F_cpbh"));
		
		this.ysyjwj = CommonTools.Obj2String(map.get("F_ysyjwj"));
		this.ysxm   = CommonTools.Obj2String(map.get("F_ysxm"));
		
		this.qtsm   = CommonTools.Obj2String(map.get("F_qtsm"));
		this.jsfzr  = CommonTools.Obj2String(map.get("F_jsfzr"));
		this.zgbmld = CommonTools.Obj2String(map.get("F_zgbmld"));
		this.sscppc = CommonTools.Obj2String(map.get("F_sscppc"));
		

		this.ysjssj =map.get("F_ysjssj")==null?null:(Date)map.get("F_ysjssj");
		
		this.cpsl   = CommonTools.Obj2String(map.get("F_cpsl"));	
		                                              
		this.chbgbbh= CommonTools.Obj2String(map.get("F_chbgbbh"));
		this.yszzz  = CommonTools.Obj2String(map.get("F_yszzz"));
		                                              
		this.cppc   = CommonTools.Obj2String(map.get("F_cppc"));
		this.ysdd   = CommonTools.Obj2String(map.get("F_ysdd"));
		this.yszz   = CommonTools.Obj2String(map.get("F_yszz"));
		this.spzt   = CommonTools.Obj2String(map.get("F_spzt"));
		this.sfwdcp = CommonTools.Obj2String(map.get("F_sfwdcp"));
		this.sfyxfyjwj = CommonTools.Obj2String(map.get("F_sfyxfyjwj"));
		this.gdlczt = CommonTools.Obj2String(map.get("F_gdlczt"));
		this.ysbghcsj = CommonTools.Obj2String(map.get("F_ysbghcsj"));
		this.yzdwId = CommonTools.Obj2String(map.get("F_yzdwId"));
		this.jsfzrId = CommonTools.Obj2String(map.get("F_jsfzrId"));
		this.zgbmldId = CommonTools.Obj2String(map.get("F_zgbmldId"));
		this.yszzzId = CommonTools.Obj2String(map.get("F_yszzzId"));
		this.yszzfId = CommonTools.Obj2String(map.get("F_yszzfId"));
		this.yszzId = CommonTools.Obj2String(map.get("F_yszzId"));
		this.wdwry= CommonTools.Obj2String(map.get("F_yszzId"));
		this.wdwryId= CommonTools.Obj2String(map.get("F_wdwryId"));
		this.gdsj=CommonTools.Obj2String(map.get("F_GDSJ"));
	}
	
	@XmlAttribute
	private String id;
	
	@XmlAttribute
	
	private String cpmc;
	
	@XmlAttribute
	private String cpdh;
	
	@XmlAttribute
	private String ssxh;
	
	@XmlAttribute
	private String ssxhID;
	
	@XmlAttribute
	private String yzjd;

	
	@XmlAttribute
	private String yzdw;
	
	@XmlAttribute
	private Date ysrq;
	
	@XmlAttribute
	private String cpbh;
	
	@XmlAttribute
	private String ysyjwj;
	
	@XmlAttribute
	private String ysxm;
	
	@XmlAttribute
	private String qtsm;
	
	@XmlAttribute
	private String jsfzr;
	
	@XmlAttribute
	private String zgbmld;
	
	@XmlAttribute
	private String sscppc;
	
	@XmlAttribute
	private Date ysjssj;
	
	@XmlAttribute
	private String cpsl;
	
	@XmlAttribute
	private String chbgbbh;
	
	@XmlAttribute
	private String yszzz;
	
	@XmlAttribute
	private String yszzf;
	
	@XmlAttribute
	private String cppc;
	
	
	@XmlAttribute
	private String ysdd;
	
	@XmlAttribute
	private String yszz;
	
	@XmlAttribute
	private String spzt;
	
	@XmlAttribute
	private String sfwdcp;
	
	@XmlAttribute
	private String sfyxfyjwj;
	
	@XmlAttribute
	private String gdlczt;
	
	@XmlAttribute
	private String ysbghcsj;
	
	@XmlAttribute
	private String yzdwId;
	
	@XmlAttribute
	private String jsfzrId;
	
	@XmlAttribute
	private String zgbmldId;
	
	@XmlAttribute
	private String yszzzId;

	@XmlAttribute
	private String yszzfId;
	
	@XmlAttribute
	private String yszzId;
	@XmlElement(name = "wdwryId", required = false)
	private String wdwryId;
	@XmlAttribute
	private String wdwry;

	@XmlElement(name = "gdsj", required = false)
	private String gdsj;	//归档时间

	public String getGdsj() {
		return gdsj;
	}

	public void setGdsj(String gdsj) {
		this.gdsj = gdsj;
	}

	public String getWdwryId() {
		return wdwryId;
	}
	public void setWdwryId(String wdwryId) {
		this.wdwryId = wdwryId;
	}
	public String getWdwry() {
		return wdwry;
	}
	public void setWdwry(String wdwry) {
		this.wdwry = wdwry;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCpmc() {
		return cpmc;
	}
	public void setCpmc(String cpmc) {
		this.cpmc = cpmc;
	}
	public String getCpdh() {
		return cpdh;
	}
	public void setCpdh(String cpdh) {
		this.cpdh = cpdh;
	}
	public String getSsxh() {
		return ssxh;
	}
	public void setSsxh(String ssxh) {
		this.ssxh = ssxh;
	}
	public String getSsxhID() {
		return ssxhID;
	}
	public void setSsxhID(String ssxhID) {
		this.ssxhID = ssxhID;
	}
	public String getYzjd() {
		return yzjd;
	}
	public void setYzjd(String yzjd) {
		this.yzjd = yzjd;
	}
	public String getYzdw() {
		return yzdw;
	}
	public void setYzdw(String yzdw) {
		this.yzdw = yzdw;
	}
	public Date getYsrq() {
		return ysrq;
	}
	public void setYsrq(Date ysrq) {
		this.ysrq = ysrq;
	}
	public String getCpbh() {
		return cpbh;
	}
	public void setCpbh(String cpbh) {
		this.cpbh = cpbh;
	}
	public String getYsyjwj() {
		return ysyjwj;
	}
	public void setYsyjwj(String ysyjwj) {
		this.ysyjwj = ysyjwj;
	}
	public String getYsxm() {
		return ysxm;
	}
	public void setYsxm(String ysxm) {
		this.ysxm = ysxm;
	}
	public String getQtsm() {
		return qtsm;
	}
	public void setQtsm(String qtsm) {
		this.qtsm = qtsm;
	}
	public String getJsfzr() {
		return jsfzr;
	}
	public void setJsfzr(String jsfzr) {
		this.jsfzr = jsfzr;
	}
	public String getZgbmld() {
		return zgbmld;
	}
	public void setZgbmld(String zgbmld) {
		this.zgbmld = zgbmld;
	}
	public String getSscppc() {
		return sscppc;
	}
	public void setSscppc(String sscppc) {
		this.sscppc = sscppc;
	}
	public Date getYsjssj() {
		return ysjssj;
	}
	public void setYsjssj(Date ysjssj) {
		this.ysjssj = ysjssj;
	}
	public String getCpsl() {
		return cpsl;
	}
	public void setCpsl(String cpsl) {
		this.cpsl = cpsl;
	}
	public String getChbgbbh() {
		return chbgbbh;
	}
	public void setChbgbbh(String chbgbbh) {
		this.chbgbbh = chbgbbh;
	}
	public String getYszzz() {
		return yszzz;
	}
	public void setYszzz(String yszzz) {
		this.yszzz = yszzz;
	}
	public String getYszzf() {
		return yszzf;
	}
	public void setYszzf(String yszzf) {
		this.yszzf = yszzf;
	}
	public String getCppc() {
		return cppc;
	}
	public void setCppc(String cppc) {
		this.cppc = cppc;
	}
	public String getYsdd() {
		return ysdd;
	}
	public void setYsdd(String ysdd) {
		this.ysdd = ysdd;
	}
	public String getYszz() {
		return yszz;
	}
	public void setYszz(String yszz) {
		this.yszz = yszz;
	}
	public String getSpzt() {
		return spzt;
	}
	public void setSpzt(String spzt) {
		this.spzt = spzt;
	}
	public String getSfwdcp() {
		return sfwdcp;
	}
	public void setSfwdcp(String sfwdcp) {
		this.sfwdcp = sfwdcp;
	}
	public String getSfyxfyjwj() {
		return sfyxfyjwj;
	}
	public void setSfyxfyjwj(String sfyxfyjwj) {
		this.sfyxfyjwj = sfyxfyjwj;
	}
	public String getGdlczt() {
		return gdlczt;
	}
	public void setGdlczt(String gdlczt) {
		this.gdlczt = gdlczt;
	}
	public String getYsbghcsj() {
		return ysbghcsj;
	}
	public void setYsbghcsj(String ysbghcsj) {
		this.ysbghcsj = ysbghcsj;
	}
	public String getYzdwId() {
		return yzdwId;
	}
	public void setYzdwId(String yzdwId) {
		this.yzdwId = yzdwId;
	}
	public String getJsfzrId() {
		return jsfzrId;
	}
	public void setJsfzrId(String jsfzrId) {
		this.jsfzrId = jsfzrId;
	}
	public String getZgbmldId() {
		return zgbmldId;
	}
	public void setZgbmldId(String zgbmldId) {
		this.zgbmldId = zgbmldId;
	}
	public String getYszzzId() {
		return yszzzId;
	}
	public void setYszzzId(String yszzzId) {
		this.yszzzId = yszzzId;
	}
	public String getYszzfId() {
		return yszzfId;
	}
	public void setYszzfId(String yszzfId) {
		this.yszzfId = yszzfId;
	}
	public String getYszzId() {
		return yszzId;
	}
	public void setYszzId(String yszzId) {
		this.yszzId = yszzId;
	}
}
