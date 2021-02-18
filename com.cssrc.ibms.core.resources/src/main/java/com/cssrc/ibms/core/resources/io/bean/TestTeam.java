package com.cssrc.ibms.core.resources.io.bean;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author wenjie
 *  工作队
 */
@XmlRootElement(name = "testTeam")
@XmlAccessorType(XmlAccessType.FIELD)
public class TestTeam {
	public TestTeam() {}
	public TestTeam(Map<String,Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.gzdmc = CommonTools.Obj2String(map.get("F_GZDMC"));
		this.cy = CommonTools.Obj2String(map.get("F_CY"));
		this.cyID = CommonTools.Obj2String(map.get("F_CYID"));
		this.sssjb = CommonTools.Obj2String(map.get("F_SSSJB"));
	}
	@XmlAttribute
	private String id;	//工作队名称
	
	@XmlAttribute(name="name")
	private String gzdmc;	//工作队名称
	
	@XmlAttribute(name="users")
	private String cy;	//成员
	
	@XmlAttribute(name="usersID")
	private String cyID;	//成员ID
	
	@XmlAttribute
	private String sssjb;  //所属数据包
	public String getGzdmc() {
		return gzdmc;
	}
	public void setGzdmc(String gzdmc) {
		this.gzdmc = gzdmc;
	}
	public String getCy() {
		return cy;
	}
	public void setCy(String cy) {
		this.cy = cy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getCyID() {
		return cyID;
	}
	public void setCyID(String cyID) {
		this.cyID = cyID;
	}
	public String getSssjb() {
		return sssjb;
	}
	public void setSssjb(String sssjb) {
		this.sssjb = sssjb;
	}
}
