package com.cssrc.ibms.core.resources.io.bean.template;

import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * @author user
 * 表头 header
 */
@XmlRootElement(name = "header")
@XmlAccessorType(XmlAccessType.FIELD)
public class Header {
	public Header() {}
	public Header(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.name = CommonTools.Obj2String(map.get("F_NAME"));
		this.order = CommonTools.Obj2String(map.get("F_ORDER"));
		this.table_temp_id = CommonTools.Obj2String(map.get("F_TABLE_TEMP_ID"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String name; //名称
	@XmlAttribute
	private String order; //顺序
	@XmlAttribute(name="tableTempId")
	private String table_temp_id; //所属模板
	
	/**
	 * 单元格集合
	 */
	@XmlElement(name ="cell")
	private List<Cell> celllist;
	
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
	public String getOrder() {
		return order;
	}
	public void setOrder(String order) {
		this.order = order;
	}
	public String getTable_temp_id() {
		return table_temp_id;
	}
	public void setTable_temp_id(String table_temp_id) {
		this.table_temp_id = table_temp_id;
	}
	public List<Cell> getCelllist() {
		return celllist;
	}
	public void setCelllist(List<Cell> celllist) {
		this.celllist = celllist;
	}
}
