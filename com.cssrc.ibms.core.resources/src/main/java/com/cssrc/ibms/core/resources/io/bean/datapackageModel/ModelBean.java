package com.cssrc.ibms.core.resources.io.bean.datapackageModel;

import java.util.Date;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
@XmlRootElement(name = "modelBean")
@XmlAccessorType(XmlAccessType.FIELD)
public class ModelBean {
	public ModelBean() {
		
	}
	public ModelBean(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.xhdh = CommonTools.Obj2String(map.get("F_xhdh"));
		this.xhmc = CommonTools.Obj2String(map.get("F_xhmc"));
		this.mj = CommonTools.Obj2String(map.get("F_mj"));
		this.zxzt = CommonTools.Obj2String(map.get("F_zxzt"));
		this.kssj =map.get("F_kssj")==null?null:(Date)map.get("F_kssj");
		this.jssj =map.get("F_jssj")==null?null:(Date)map.get("F_kssj");
		this.rwly = CommonTools.Obj2String(map.get("F_rwly"));
		this.htjfhzgbm = CommonTools.Obj2String(map.get("F_htjfhzgbm"));
		this.xmb = CommonTools.Obj2String(map.get("F_xmb"));
		this.cjbm = CommonTools.Obj2String(map.get("F_cjbm"));
		this.xhzs = CommonTools.Obj2String(map.get("F_xhzs"));
		this.xhzzh = CommonTools.Obj2String(map.get("F_xhzzh"));
		this.sm = CommonTools.Obj2String(map.get("F_sm"));
		this.zgbmld = CommonTools.Obj2String(map.get("F_zgbmld"));
		this.xhfzs = CommonTools.Obj2String(map.get("F_xhfzs"));
		this.htjfhzgbmId = CommonTools.Obj2String(map.get("F_htjfhzgbmId"));
		this.xmbId = CommonTools.Obj2String(map.get("F_xmbId"));
		this.cjbmId = CommonTools.Obj2String(map.get("F_cjbmId"));
		this.xhzsId = CommonTools.Obj2String(map.get("F_xhzsId"));
		this.xhzzhId = CommonTools.Obj2String(map.get("F_xhzzhId"));
		this.zgbmldId = CommonTools.Obj2String(map.get("F_zgbmldId"));
		this.xhfzsId = CommonTools.Obj2String(map.get("F_xhfzsId"));
		
		this.xzxhgl=CommonTools.Obj2String(map.get("F_XZXHGL"));
		this.xzxhglId=CommonTools.Obj2String(map.get("F_XZXHGLID"));
		this.xzxhdw=CommonTools.Obj2String(map.get("F_XZXHDW"));
		this.xzxhdwId=CommonTools.Obj2String(map.get("F_XZXHDWID"));
	}
	private String id;
	private String xhdh;
	private String xhmc;
	private String mj;
	private String zxzt;
	private Date kssj;
	private Date jssj;
	private String rwly;
	private String htjfhzgbm;
	private String xmb;
	private String cjbm;
	private String xhzs;
	private String xhzzh;
	private String sm;
	private String zgbmld;
	private String xhfzs;
	private String htjfhzgbmId;
	private String xmbId;
	private String cjbmId;
	private String xhzsId;
	private String xhzzhId;
	private String zgbmldId;	
	private String xhfzsId;
	private String xzxhgl;
	private String xzxhglId;
	private String xzxhdw;
	private String xzxhdwId;
	
	
	
	
	public String getXzxhgl() {
		return xzxhgl;
	}
	public void setXzxhgl(String xzxhgl) {
		this.xzxhgl = xzxhgl;
	}
	public String getXzxhglId() {
		return xzxhglId;
	}
	public void setXzxhglId(String xzxhglId) {
		this.xzxhglId = xzxhglId;
	}
	public String getXzxhdw() {
		return xzxhdw;
	}
	public void setXzxhdw(String xzxhdw) {
		this.xzxhdw = xzxhdw;
	}
	public String getXzxhdwId() {
		return xzxhdwId;
	}
	public void setXzxhdwId(String xzxhdwId) {
		this.xzxhdwId = xzxhdwId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getXhdh() {
		return xhdh;
	}
	public void setXhdh(String xhdh) {
		this.xhdh = xhdh;
	}
	public String getXhmc() {
		return xhmc;
	}
	public void setXhmc(String xhmc) {
		this.xhmc = xhmc;
	}
	public String getMj() {
		return mj;
	}
	public void setMj(String mj) {
		this.mj = mj;
	}
	public String getZxzt() {
		return zxzt;
	}
	public void setZxzt(String zxzt) {
		this.zxzt = zxzt;
	}
	public Date getKssj() {
		return kssj;
	}
	public void setKssj(Date kssj) {
		this.kssj = kssj;
	}
	public Date getJssj() {
		return jssj;
	}
	public void setJssj(Date jssj) {
		this.jssj = jssj;
	}
	public String getRwly() {
		return rwly;
	}
	public void setRwly(String rwly) {
		this.rwly = rwly;
	}
	public String getHtjfhzgbm() {
		return htjfhzgbm;
	}
	public void setHtjfhzgbm(String htjfhzgbm) {
		this.htjfhzgbm = htjfhzgbm;
	}
	public String getXmb() {
		return xmb;
	}
	public void setXmb(String xmb) {
		this.xmb = xmb;
	}
	public String getCjbm() {
		return cjbm;
	}
	public void setCjbm(String cjbm) {
		this.cjbm = cjbm;
	}
	public String getXhzs() {
		return xhzs;
	}
	public void setXhzs(String xhzs) {
		this.xhzs = xhzs;
	}
	public String getXhzzh() {
		return xhzzh;
	}
	public void setXhzzh(String xhzzh) {
		this.xhzzh = xhzzh;
	}
	public String getSm() {
		return sm;
	}
	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getZgbmld() {
		return zgbmld;
	}
	public void setZgbmld(String zgbmld) {
		this.zgbmld = zgbmld;
	}
	public String getXhfzs() {
		return xhfzs;
	}
	public void setXhfzs(String xhfzs) {
		this.xhfzs = xhfzs;
	}
	public String getHtjfhzgbmId() {
		return htjfhzgbmId;
	}
	public void setHtjfhzgbmId(String htjfhzgbmId) {
		this.htjfhzgbmId = htjfhzgbmId;
	}
	public String getXmbId() {
		return xmbId;
	}
	public void setXmbId(String xmbId) {
		this.xmbId = xmbId;
	}
	public String getCjbmId() {
		return cjbmId;
	}
	public void setCjbmId(String cjbmId) {
		this.cjbmId = cjbmId;
	}
	public String getXhzsId() {
		return xhzsId;
	}
	public void setXhzsId(String xhzsId) {
		this.xhzsId = xhzsId;
	}
	public String getXhzzhId() {
		return xhzzhId;
	}
	public void setXhzzhId(String xhzzhId) {
		this.xhzzhId = xhzzhId;
	}
	public String getZgbmldId() {
		return zgbmldId;
	}
	public void setZgbmldId(String zgbmldId) {
		this.zgbmldId = zgbmldId;
	}
	public String getXhfzsId() {
		return xhfzsId;
	}
	public void setXhfzsId(String xhfzsId) {
		this.xhfzsId = xhfzsId;
	}
	
}
