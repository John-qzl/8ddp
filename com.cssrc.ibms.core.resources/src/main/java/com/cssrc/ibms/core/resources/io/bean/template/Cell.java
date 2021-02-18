package com.cssrc.ibms.core.resources.io.bean.template;

import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * CELL
 */
@XmlRootElement(name = "Cell")
@XmlAccessorType(XmlAccessType.FIELD)
public class Cell {
	public Cell(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.content = CommonTools.Obj2String(map.get("F_CONTENT"));
		this.rnumber = CommonTools.Obj2String(map.get("F_RNUMBER"));
		this.ifresult = CommonTools.Obj2String(map.get("F_IFRESULT"));
		this.resulttype = CommonTools.Obj2String(map.get("F_RESULTTYPE"));
		this.itemdeid = CommonTools.Obj2String(map.get("F_ITEMDEF_ID"));
		this.header_id = CommonTools.Obj2String(map.get("F_HEADER_ID"));
		this.table_temp_id = CommonTools.Obj2String(map.get("F_TABLE_TEMP_ID"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String content; //内容
	@XmlAttribute
	private String rnumber; //行号
	@XmlAttribute
	private String ifresult;  //是否结果项
	@XmlAttribute
	private String resulttype;  //结果项类型
	@XmlAttribute
	private String itemdeid; //所属检查项定义
	@XmlAttribute(name="headerId")
	private String header_id; // 所属表头
	@XmlAttribute(name="tableTempId")
	private String table_temp_id; //所属模板
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRnumber() {
		return rnumber;
	}
	public void setRnumber(String rnumber) {
		this.rnumber = rnumber;
	}
	public String getIfresult() {
		return ifresult;
	}
	public void setIfresult(String ifresult) {
		this.ifresult = ifresult;
	}
	public String getResulttype() {
		return resulttype;
	}
	public void setResulttype(String resulttype) {
		this.resulttype = resulttype;
	}
	public String getItemdeid() {
		return itemdeid;
	}
	public void setItemdeid(String itemdeid) {
		this.itemdeid = itemdeid;
	}
	public String getHeader_id() {
		return header_id;
	}
	public void setHeader_id(String header_id) {
		this.header_id = header_id;
	}
	public String getTable_temp_id() {
		return table_temp_id;
	}
	public void setTable_temp_id(String table_temp_id) {
		this.table_temp_id = table_temp_id;
	}
}