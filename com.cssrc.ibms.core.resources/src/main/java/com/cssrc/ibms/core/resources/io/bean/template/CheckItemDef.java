package com.cssrc.ibms.core.resources.io.bean.template;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * 检查项定义W_ITEMDEF
 */
@XmlRootElement(name = "CheckItemDef")
@XmlAccessorType(XmlAccessType.FIELD)
public class CheckItemDef {
	public CheckItemDef() {}
	public CheckItemDef(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.name = CommonTools.Obj2String(map.get("F_NAME"));
		this.shortname = CommonTools.Obj2String(map.get("F_SHORTNAME"));
		this.type = CommonTools.Obj2String(map.get("F_TYPE"));
		this.description = CommonTools.Obj2String(map.get("F_DESCRIPTION"));
		this.ildd = CommonTools.Obj2String(map.get("F_ILDD"));
		this.iildd = CommonTools.Obj2String(map.get("F_IILDD"));
		this.ycn = CommonTools.Obj2String(map.get("F_YCN"));
		this.njljyq = CommonTools.Obj2String(map.get("F_NJLJYQ"));
		this.zhycdz = CommonTools.Obj2String(map.get("F_ZHYCDZ"));
		this.ifmedia = CommonTools.Obj2String(map.get("F_IFMEDIA"));
		this.table_temp_id = CommonTools.Obj2String(map.get("F_TABLE_TEMP_ID"));
	}
	@XmlAttribute
	private String  id; //主键
	@XmlAttribute
	private String  name; //名称
	@XmlAttribute
	private String  shortname; //简称
	@XmlAttribute
	private String  type; //检查类型
	@XmlAttribute
	private String  description; //检查项描述
	@XmlAttribute
	private String  ildd; //一类单点
	@XmlAttribute
	private String  iildd; //二类单点
	@XmlAttribute
	private String  ycn; //易错难
	@XmlAttribute
	private String  njljyq; //拧紧力矩要求
	@XmlAttribute
	private String  zhycdz; //最后一次动作
	@XmlAttribute
	private String  ifmedia; //是否多媒体项目
	@XmlAttribute(name="tableTempId")
	private String  table_temp_id; //所属检查表模板
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getShortname() {
		return shortname;
	}
	public void setShortname(String shortname) {
		this.shortname = shortname;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getIldd() {
		return ildd;
	}
	public void setIldd(String ildd) {
		this.ildd = ildd;
	}
	public String getIildd() {
		return iildd;
	}
	public void setIildd(String iildd) {
		this.iildd = iildd;
	}
	public String getYcn() {
		return ycn;
	}
	public void setYcn(String ycn) {
		this.ycn = ycn;
	}
	public String getNjljyq() {
		return njljyq;
	}
	public void setNjljyq(String njljyq) {
		this.njljyq = njljyq;
	}
	public String getZhycdz() {
		return zhycdz;
	}
	public void setZhycdz(String zhycdz) {
		this.zhycdz = zhycdz;
	}
	public String getIfmedia() {
		return ifmedia;
	}
	public void setIfmedia(String ifmedia) {
		this.ifmedia = ifmedia;
	}
	public String getTable_temp_id() {
		return table_temp_id;
	}
	public void setTable_temp_id(String table_temp_id) {
		this.table_temp_id = table_temp_id;
	}
	
}
