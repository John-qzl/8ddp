package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class CustomRecord {
	private String tableName;
	private Long pk;
	private String keyValue;
	private String[] fielName;
	private String[] fieldvalue;
	private String keyType;
	
	public static List<CustomRecord> initList(String record){	
		List<CustomRecord> list = new ArrayList(); 
		JSONArray arr =  JSONArray.fromObject(record);
		for(int i =0;i<arr.size();i++) {
			list.add(new CustomRecord(arr.get(i).toString()));
		}
		return list;
	}
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public CustomRecord(String record) {
		JSONObject obj = JSONObject.fromObject(record);
		this.tableName = CommonTools.Obj2String(obj.getString("tableName"));
		this.pk = CommonTools.Obj2Long(obj.getString("pk"));
		this.keyValue = obj.getString("keyValue");
		this.keyType = CommonTools.Obj2String(obj.get("keyType"));
		JSONObject kv = JSONObject.fromObject(this.keyValue);
		List<String> keyList = new ArrayList();
		List<String> valueList = new ArrayList();
		Set set = kv.keySet();
		for(Iterator it = set.iterator();it.hasNext();) {
			String key = (String)it.next();
			String value = kv.getString(key);
			keyList.add(key);
			valueList.add(value);
		}
		this.fielName = keyList.toArray(new String[]{});
		this.fieldvalue = valueList.toArray(new String[]{});		
	}

	public String getAddSql() {
		StringBuffer sql =new StringBuffer();
		sql.append(" Insert into ").append(getTableName());
		sql.append(" (ID, ").append(StringUtil.getStringFromArray(getFielName(),",")).append(")");
		sql.append(" values ");
		sql.append(" (").append(UniqueIdUtil.genId()+",");
		int i=0;
		for(String value : fieldvalue) {
			sql.append(getDealFieldValue(getFielName()[i],value));
			if(i!=fieldvalue.length-1) {
				sql.append(",");
			}
			i++;
		}
		sql.append(")");
		return sql.toString();
	}
	public String getUpdateSql() {
		StringBuffer sql =new StringBuffer();
		sql.append(" update ").append(getTableName()).append(" set ");
		for(int i=0;i<getFielName().length;i++) {
			String fieldName = getFielName()[i];
			String fieldValue = getFieldvalue()[i];
			sql.append(fieldName).append("=").append(getDealFieldValue(fieldName,fieldValue)).append("");
			if(i<getFielName().length-1) {
				sql.append(",");
			}
		}
		sql.append(" where id=").append(getPk());
		return sql.toString();
	}
	public String getDealFieldValue(String fieldName,String fieldValue) {
		if(keyType.equals("")) {
			return "'"+fieldValue+"'";
		}
		JSONObject obj = JSONObject.fromObject(keyType);
		if(obj.containsKey(fieldName)) {
			String value = "";
			String type = obj.getString(fieldName);
			switch(type) {
			case "number":
				value = fieldValue;
				break;
			case "yyyy-MM-dd HH:mm:ss":
			case "yyyy-MM-dd":
			case "HH:mm:ss":
				value = "to_date('"+fieldValue+"','"+type+"')";
				break;
			}
			return value;
		}else {
			return "'"+fieldValue+"'";
		}
	}
	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public Long getPk() {
		return pk;
	}

	public void setPk(Long pk) {
		this.pk = pk;
	}

	public String getKeyValue() {
		return keyValue;
	}

	public void setKeyValue(String keyValue) {
		this.keyValue = keyValue;
	}

	public String[] getFielName() {
		return fielName;
	}

	public void setFielName(String[] fielName) {
		this.fielName = fielName;
	}

	public String[] getFieldvalue() {
		return fieldvalue;
	}

	public void setFieldvalue(String[] fieldvalue) {
		this.fieldvalue = fieldvalue;
	}
	public String getKeyType() {
		return keyType;
	}
	public void setKeyType(String keyType) {
		this.keyType = keyType;
	}
}
