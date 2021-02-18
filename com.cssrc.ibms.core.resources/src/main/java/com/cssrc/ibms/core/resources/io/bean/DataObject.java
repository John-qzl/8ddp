package com.cssrc.ibms.core.resources.io.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

/**
 * @author wenjie
 * 数据包详细信息
 */
@XmlRootElement(name = "dataObject")
@XmlAccessorType(XmlAccessType.FIELD)
public class DataObject {
	public DataObject() {}
	public DataObject(Map<String,Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.sjmc = CommonTools.Obj2String(map.get("F_SJMC"));
		this.sjlx = CommonTools.Obj2String(map.get("F_SJLX"));
		this.gw = CommonTools.Obj2String(map.get("F_GW"));
		this.mj = CommonTools.Obj2String(map.get("F_MJ"));
		this.bmqx = CommonTools.Obj2String(map.get("F_BMQX"));
		this.scr = CommonTools.Obj2String(map.get("F_SCR"));
		this.scrID = CommonTools.Obj2String(map.get("F_SCRID"));
		this.sjz = CommonTools.Obj2String(map.get("F_SJZ"));
		
		this.scsj = CommonTools.Obj2String(map.get("F_SCSJ"));
		this.scsj = DateUtil.getDateStr(scsj, "yyyy-MM-dd");
		
		this.bb = CommonTools.Obj2String(map.get("F_BB"));
		this.sm = CommonTools.Obj2String(map.get("F_SM"));
		this.spjd = CommonTools.Obj2String(map.get("F_SPJD"));
		this.zxzt = CommonTools.Obj2String(map.get("F_ZXZT"));
		
		this.wcsj = CommonTools.Obj2String(map.get("F_WCSJ"));	
		this.wcsj = DateUtil.getDateStr(wcsj, "yyyy-MM-dd");
		
		this.sssjb = CommonTools.Obj2String(map.get("F_SSSJB"));
		this.ssmb = CommonTools.Obj2String(map.get("F_SSMB"));
		this.ssmbmc = CommonTools.Obj2String(map.get("F_SSMBMC"));
	}
	@XmlAttribute
	private String id;
	
	@XmlAttribute
	private String sjmc;	//数据名称
	
	@XmlAttribute
	private String sjlx;	//数据类型
	
	@XmlAttribute
	private String sjz;	//数据值
	
	@XmlAttribute
	private String gw;	//岗位
	
	@XmlAttribute
	private String mj;	//密级
	
	@XmlAttribute
	private String bmqx;	//保密期限
	
	@XmlAttribute
	private String scr;	//上传人
	
	@XmlAttribute
	private String scrID;	//上传人ID
	
	@XmlAttribute
	private String scsj;	//上传时间
	
	@XmlAttribute
	private String bb;	//版本
	
	@XmlAttribute
	private String sm;	//说明
	
	@XmlAttribute
	private String spjd;	//审批进度
	
	@XmlAttribute
	private String zxzt;	//执行状态
	
	@XmlAttribute
	private String wcsj;	//完成时间
	
	@XmlElement(name ="dbFile")
	private DpFile file;//文件信息
	
	@XmlAttribute(name ="sssjb")
	private String sssjb;//所属数据包
	
	@XmlAttribute(name ="ssmb")
	private String ssmb;//所属表单实例ID
	
	@XmlAttribute(name ="ssmbmc")
	private String ssmbmc;//
	
	@XmlAttribute(name ="insName")
	private String insName;//所属表单实例名称
	
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
	public String getScr() {
		return scr;
	}
	public void setScr(String scr) {
		this.scr = scr;
	}
	public String getScsj() {
		return scsj;
	}
	public void setScsj(String scsj) {
		this.scsj = scsj;
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
	public String getWcsj() {
		return wcsj;
	}
	public void setWcsj(String wcsj) {
		this.wcsj = wcsj;
	}
	public DpFile getFile() {
		return file;
	}
	public void setFile(DpFile file) {
		this.file = file;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getScrID() {
		return scrID;
	}
	public void setScrID(String scrID) {
		this.scrID = scrID;
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
	public String getSssjb() {
		return sssjb;
	}
	public void setSssjb(String sssjb) {
		this.sssjb = sssjb;
	}
	public String getSjz() {
		return sjz;
	}
	public void setSjz(String sjz) {
		this.sjz = sjz;
	}
	public String getInsName() {
		return insName;
	}
	public void setInsName(String insName) {
		this.insName = insName;
	}
}
