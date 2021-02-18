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
 * 模板文件夹  W_TEMP_FILE
 */
@XmlRootElement(name = "templateFLoder")
@XmlAccessorType(XmlAccessType.FIELD)
public class TemplateFLoder {
	public static String TABLENAME = "W_TEMP_FILE";
	
	public TemplateFLoder() {}
	public TemplateFLoder(Map<String, Object> map) {
		this.id = CommonTools.Obj2String(map.get("ID"));
		this.name = CommonTools.Obj2String(map.get("F_NAME"));
		this.desc = CommonTools.Obj2String(map.get("F_DESC"));
		this.project_id = CommonTools.Obj2String(map.get("F_PROJECT_ID"));
		this.temp_file_id = CommonTools.Obj2String(map.get("F_TEMP_FILE_ID"));
	}
	@XmlAttribute
	private String id; //主键
	@XmlAttribute
	private String name; //名称
	@XmlAttribute
	private String desc; //描述
	@XmlAttribute(name="projectId")
	private String project_id; //所属发次
	@XmlAttribute(name="tempFileId")
	private String temp_file_id; //所属父文件夹
	
	/**
	 * 表单模板集合
	 */
	@XmlElement(name ="tableTemp")
	public List<TableTemp> tableTempList;
	
	/**
	 * 表单模板集合
	 */
	@XmlElement(name ="templateFLoder")
	public List<TemplateFLoder> sonFloderList;

	public static String getTABLENAME() {
		return TABLENAME;
	}
	public static void setTABLENAME(String tABLENAME) {
		TABLENAME = tABLENAME;
	}
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
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String getProject_id() {
		return project_id;
	}
	public void setProject_id(String project_id) {
		this.project_id = project_id;
	}
	public String getTemp_file_id() {
		return temp_file_id;
	}
	public void setTemp_file_id(String temp_file_id) {
		this.temp_file_id = temp_file_id;
	}
	public List<TableTemp> getTableTempList() {
		return tableTempList;
	}
	public void setTableTempList(List<TableTemp> tableTempList) {
		this.tableTempList = tableTempList;
	}
	public List<TemplateFLoder> getSonFloderList() {
		return sonFloderList;
	}
	public void setSonFloderList(List<TemplateFLoder> sonFloderList) {
		this.sonFloderList = sonFloderList;
	}
}
