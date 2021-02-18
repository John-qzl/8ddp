package com.cssrc.ibms.migration.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.migration.model.IMigrationConfigure;
import com.cssrc.ibms.core.util.date.DateUtil;

public class MigrationConfigure implements IMigrationConfigure{
	public String type = "";
	public String version = "";	
	public String xmlType = "";
	private MultipartFile schemaFile;
	private MultipartFile defXmlFile;
	
	public static List<Map<String,String>> getTypes(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map map = new HashMap();
		map.put("name", "TDM");
		map.put("value","tdm");
		list.add(map);		
		return list;
	}	
	
	public static List<Map<String,String>> getVersions(String dataType){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		if(dataType.equals(MIGRATION_TYPE_TDM)||dataType.equals("")){
			Map map = new HashMap();
			map.put("name", "QMS");
			map.put("value","qms");
			list.add(map);
		}else{
			
		}
		return list;
	}
	
	public static List<Map<String,String>> getXmlTypes(){
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		Map map = new HashMap();
		map.put("name", "表单模板");
		map.put("value",XML_FORM_TEMPLATE);
		list.add(map);	
		
		Map map2 = new HashMap();
		map2.put("name", "表单设计（含业务数据模板）");
		map2.put("value",XML_FORM_DEF);
		list.add(map2);
		return list;
	}
	
	public String getXmlName(){
		String name = "";
		String time = DateUtil.getCurrentDate("yyyyMMddHHmmss");
		String schemaFileName = schemaFile.getOriginalFilename().replace(".schema", "");
		switch(xmlType){
		case XML_FORM_DEF:
			name = schemaFileName+"_表单设计_"+time+".xml";
			break;
		case XML_FORM_TEMPLATE:
			name = schemaFileName+"_表单模板_"+time+".xml";
			break;
		}
		return name;
	}
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public MultipartFile getSchemaFile() {
		return schemaFile;
	}
	public void setSchemaFile(MultipartFile schemaFile) {
		this.schemaFile = schemaFile;
	}
	public MultipartFile getDefXmlFile() {
		return defXmlFile;
	}
	public void setDefXmlFile(MultipartFile defXmlFile) {
		this.defXmlFile = defXmlFile;
	}
	public String getXmlType() {
		return xmlType;
	}
	public void setXmlType(String xmlType) {
		this.xmlType = xmlType;
	}
}
