package com.cssrc.ibms.core.resources.io.bean.pack;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.resources.io.bean.DataObject;
import com.cssrc.ibms.core.resources.io.bean.TestTeam;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;

/**
 * @author user
 *　数据包节点表（w_package）
 */
@XmlRootElement(name = "package")
@XmlAccessorType(XmlAccessType.FIELD)
public class SimplePackage {
	public SimplePackage() {}
	public SimplePackage(Map<String,Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.parentID = CommonTools.Obj2String(map.get("F_PARENTID"));
		this.jdmc = CommonTools.Obj2String(map.get("F_JDMC"));
		this.jdlx = CommonTools.Obj2String(map.get("F_JDLX"));
		this.fzr = CommonTools.Obj2String(map.get("F_FZR"));
		this.fzrID = CommonTools.Obj2String(map.get("F_FZRID"));
		this.cjsj = CommonTools.Obj2String(map.get("F_CJSJ"));
		this.chspzt = CommonTools.Obj2String(map.get("F_CHSPZT"));
		this.tzzt = CommonTools.Obj2String(map.get("F_TZZT"));
		this.ssxh = CommonTools.Obj2String(map.get("F_SSXH"));
		this.ssfc = CommonTools.Obj2String(map.get("F_SSFC"));
		this.parentName = CommonTools.Obj2String(map.get("F_PARENTNAME"));
		this.sm = CommonTools.Obj2String(map.get("F_SM"));
		
		this.desi_jhkssj = CommonTools.Obj2String(map.get("F_DESI_JHKSSJ"));
		this.desi_jhkssj = DateUtil.getDateStr(desi_jhkssj, "yyyy-MM-dd");		
		this.desi_jhjssj = CommonTools.Obj2String(map.get("F_DESI_JHJSSJ"));
		this.desi_jhjssj = DateUtil.getDateStr(desi_jhjssj, "yyyy-MM-dd");
		
		this.part_cpdh = CommonTools.Obj2String(map.get("F_PART_CPDH"));
		this.part_cpmc = CommonTools.Obj2String(map.get("F_PART_CPMC"));
		this.part_zt = CommonTools.Obj2String(map.get("F_PART_ZT"));
		this.part_zrdw = CommonTools.Obj2String(map.get("F_PART_ZRDW"));
		
		this.soft_rjdh = CommonTools.Obj2String(map.get("F_SOFT_RJDH"));
		this.soft_rjmc = CommonTools.Obj2String(map.get("F_SOFT_RJMC"));
		this.soft_zt = CommonTools.Obj2String(map.get("F_SOFT_ZT"));
		this.soft_zrdw = CommonTools.Obj2String(map.get("F_SOFT_ZRDW"));
		this.soft_bbh = CommonTools.Obj2String(map.get("F_SOFT_BBH"));
		
		this.test_sydd = CommonTools.Obj2String(map.get("F_TEST_SYDD"));
		this.test_csdw = CommonTools.Obj2String(map.get("F_TEST_CSDW"));
		this.test_jhkssj = CommonTools.Obj2String(map.get("F_TEST_JHKSSJ"));
		this.test_jhkssj = DateUtil.getDateStr(test_jhkssj, "yyyy-MM-dd");		
		this.test_jhjssj = CommonTools.Obj2String(map.get("F_TEST_JHJSSJ"));
		this.test_jhjssj = DateUtil.getDateStr(test_jhjssj, "yyyy-MM-dd");
	}
	/**
	 * 节点ID,主键
	 */
	@XmlAttribute(name="id",required=true)
	private String id;
	/**
	 * 	父节点ID
	 */
	@XmlAttribute(name="parentID",required=true)
	private String parentID;
	/**
	 * 	节点名称
	 */
	@XmlAttribute
	private String jdmc;
	/**
	 * 	节点类型
	 */
	@XmlAttribute
	private String jdlx;
	/**
	 * 	负责人
	 */
	@XmlAttribute
	private String fzr;
	
	/**
	 * 	负责人ID
	 */
	@XmlAttribute
	private String fzrID;
	
	/**
	 * 	创建时间
	 */
	@XmlAttribute
	private String cjsj;
	
	/**
	 * 	策划审批状态
	 */
	@XmlAttribute
	private String chspzt;
	
	/**
	 * 	通知状态
	 */
	@XmlAttribute
	private String tzzt;
	
	/**
	 * 	所属型号
	 */
	@XmlAttribute
	private String ssxh;
	
	/**
	 * 	所属发次
	 */
	@XmlAttribute
	private String ssfc;
	
	/**
	 * 	父节点名称
	 */
	@XmlAttribute
	private String parentName;
	
	/**
	 * 	说明
	 */
	@XmlAttribute
	private String sm;
		
	@XmlElement(name ="package")
	private List<SimplePackage> list;
	
	//设计属性
	@XmlAttribute(name="desiJhkssj")
	private String desi_jhkssj;	//计划开始时间
	
	@XmlAttribute(name="desiJhjssj")
	private String desi_jhjssj;	//计划结束时间
	
	//单机属性
	@XmlAttribute(name="partCpdh")
	private String part_cpdh;	//产品代号
	
	@XmlAttribute(name="partCpmc")
	private String part_cpmc;	//产品名称
	
	@XmlAttribute(name="partZt")
	private String part_zt;	//状态
	
	@XmlAttribute(name="partZrdw")
	private String part_zrdw;	//责任单位
	//软件属性
	@XmlAttribute(name="softRjdh")
	private String soft_rjdh;	//软件代号
	
	@XmlAttribute(name="softRjmc")
	private String soft_rjmc;	//软件名称
	
	@XmlAttribute(name="softZt")
	private String soft_zt;	//状态
	
	@XmlAttribute(name="softZrdw")
	private String soft_zrdw;	//责任单位
	
	@XmlAttribute(name="softBbh")
	private String soft_bbh;	//版本号
	
	//试验属性
	@XmlAttribute(name="testSydd")
	private String test_sydd;	//试验地点
	
	@XmlAttribute(name="testCsdw")
	private String test_csdw;	//参试单位
	
	@XmlAttribute(name="testJhkssj")
	private String test_jhkssj;	//计划开始时间
	
	@XmlAttribute(name="testJhjssj")
	private String test_jhjssj;	//计划结束时间
	
	@XmlElement(name ="testTeam")
	private List<TestTeam>  teams;//工作队
	
	@XmlElement(name ="dataObject")
	private List<DataObject> datas;//数据包详细信息
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getParentID() {
		return parentID;
	}

	public void setParentID(String parentID) {
		this.parentID = parentID;
	}

	public String getJdmc() {
		return jdmc;
	}

	public void setJdmc(String jdmc) {
		this.jdmc = jdmc;
	}

	public String getJdlx() {
		return jdlx;
	}

	public void setJdlx(String jdlx) {
		this.jdlx = jdlx;
	}

	public String getFzr() {
		return fzr;
	}

	public void setFzr(String fzr) {
		this.fzr = fzr;
	}

	public String getSm() {
		return sm;
	}

	public void setSm(String sm) {
		this.sm = sm;
	}
	public String getFzrID() {
		return fzrID;
	}
	public void setFzrID(String fzrID) {
		this.fzrID = fzrID;
	}
	public String getDesi_jhkssj() {
		return desi_jhkssj;
	}
	public void setDesi_jhkssj(String desi_jhkssj) {
		this.desi_jhkssj = desi_jhkssj;
	}
	public String getDesi_jhjssj() {
		return desi_jhjssj;
	}
	public void setDesi_jhjssj(String desi_jhjssj) {
		this.desi_jhjssj = desi_jhjssj;
	}
	public String getPart_cpdh() {
		return part_cpdh;
	}
	public void setPart_cpdh(String part_cpdh) {
		this.part_cpdh = part_cpdh;
	}
	public String getPart_cpmc() {
		return part_cpmc;
	}
	public void setPart_cpmc(String part_cpmc) {
		this.part_cpmc = part_cpmc;
	}
	public String getPart_zt() {
		return part_zt;
	}
	public void setPart_zt(String part_zt) {
		this.part_zt = part_zt;
	}
	public String getPart_zrdw() {
		return part_zrdw;
	}
	public void setPart_zrdw(String part_zrdw) {
		this.part_zrdw = part_zrdw;
	}
	public String getSoft_rjdh() {
		return soft_rjdh;
	}
	public void setSoft_rjdh(String soft_rjdh) {
		this.soft_rjdh = soft_rjdh;
	}
	public String getSoft_rjmc() {
		return soft_rjmc;
	}
	public void setSoft_rjmc(String soft_rjmc) {
		this.soft_rjmc = soft_rjmc;
	}
	public String getSoft_zt() {
		return soft_zt;
	}
	public void setSoft_zt(String soft_zt) {
		this.soft_zt = soft_zt;
	}
	public String getSoft_zrdw() {
		return soft_zrdw;
	}
	public void setSoft_zrdw(String soft_zrdw) {
		this.soft_zrdw = soft_zrdw;
	}
	public String getSoft_bbh() {
		return soft_bbh;
	}
	public void setSoft_bbh(String soft_bbh) {
		this.soft_bbh = soft_bbh;
	}
	public String getTest_sydd() {
		return test_sydd;
	}
	public void setTest_sydd(String test_sydd) {
		this.test_sydd = test_sydd;
	}
	public String getTest_csdw() {
		return test_csdw;
	}
	public void setTest_csdw(String test_csdw) {
		this.test_csdw = test_csdw;
	}
	public String getTest_jhkssj() {
		return test_jhkssj;
	}
	public void setTest_jhkssj(String test_jhkssj) {
		this.test_jhkssj = test_jhkssj;
	}
	public String getTest_jhjssj() {
		return test_jhjssj;
	}
	public void setTest_jhjssj(String test_jhjssj) {
		this.test_jhjssj = test_jhjssj;
	}
	public List<TestTeam> getTeams() {
		return teams;
	}
	public void setTeams(List<TestTeam> teams) {
		this.teams = teams;
	}
	public List<DataObject> getDatas() {
		return datas;
	}
	public void setDatas(List<DataObject> datas) {
		this.datas = datas;
	}
	public List<SimplePackage> getList() {
		return list;
	}
	public void setList(List<SimplePackage> list) {
		this.list = list;
	}
	public String getCjsj() {
		return cjsj;
	}
	public void setCjsj(String cjsj) {
		this.cjsj = cjsj;
	}
	public String getChspzt() {
		return chspzt;
	}
	public void setChspzt(String chspzt) {
		this.chspzt = chspzt;
	}
	public String getTzzt() {
		return tzzt;
	}
	public void setTzzt(String tzzt) {
		this.tzzt = tzzt;
	}
	public String getSsxh() {
		return ssxh;
	}
	public void setSsxh(String ssxh) {
		this.ssxh = ssxh;
	}
	public String getSsfc() {
		return ssfc;
	}
	public void setSsfc(String ssfc) {
		this.ssfc = ssfc;
	}
	public String getParentName() {
		return parentName;
	}
	public void setParentName(String parentName) {
		this.parentName = parentName;
	}
}
