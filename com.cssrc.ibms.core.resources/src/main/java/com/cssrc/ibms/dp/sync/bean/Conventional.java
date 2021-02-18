package com.cssrc.ibms.dp.sync.bean;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

@XmlRootElement(name = "conventional")
@XmlAccessorType(XmlAccessType.FIELD)
public class Conventional {
	@XmlAttribute
	String id;
	@XmlAttribute
	String jcnr;
	@XmlAttribute
	String jcyq;
	@XmlAttribute
	String jcjg;
	@XmlAttribute
	String jl;
	@XmlAttribute
	String bz;
	@XmlAttribute
	String planId;
	@XmlAttribute
	String slId;
	@XmlAttribute
	String 	xh;
	
	
	public Conventional() {
		// TODO Auto-generated constructor stub
	}
	public Conventional(Map<String, Object> map) {
		this.id= CommonTools.Obj2String(map.get("ID"));
		this.jcnr= CommonTools.Obj2String(map.get("F_jcnr"));
		this.jcyq= CommonTools.Obj2String(map.get("F_jcyq"));
		this.jcjg= CommonTools.Obj2String(map.get("F_jcjg"));
		this.jl= CommonTools.Obj2String(map.get("F_jl"));
		this.bz= CommonTools.Obj2String(map.get("F_bz"));
		this.planId= CommonTools.Obj2String(map.get("F_planId"));
		this.slId= CommonTools.Obj2String(map.get("F_slId"));
		this.xh= CommonTools.Obj2String(map.get("F_xh"));
	}
	
	
	public String getJcnr() {
		return jcnr;
	}


	public void setJcnr(String jcnr) {
		this.jcnr = jcnr;
	}


	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getJcyq() {
		return jcyq;
	}
	public void setJcyq(String jcyq) {
		this.jcyq = jcyq;
	}
	public String getJcjg() {
		return jcjg;
	}
	public void setJcjg(String jcjg) {
		this.jcjg = jcjg;
	}
	public String getJl() {
		return jl;
	}
	public void setJl(String jl) {
		this.jl = jl;
	}
	public String getBz() {
		return bz;
	}
	public void setBz(String bz) {
		this.bz = bz;
	}
	public String getPlanId() {
		return planId;
	}
	public void setPlanId(String planId) {
		this.planId = planId;
	}
	public String getSlId() {
		return slId;
	}
	public void setSlId(String slId) {
		this.slId = slId;
	}
	public String getXh() {
		return xh;
	}
	public void setXh(String xh) {
		this.xh = xh;
	}
	
}
