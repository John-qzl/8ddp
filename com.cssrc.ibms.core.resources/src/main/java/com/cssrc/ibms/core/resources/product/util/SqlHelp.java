package com.cssrc.ibms.core.resources.product.util;

import java.util.ArrayList;
import java.util.List;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
public class SqlHelp {
	public static String[] getFieldKeys(Class cls,String tableName) {
		Field[] fields =  cls.getDeclaredFields();
		List<String> l = new ArrayList<>();		
		for(Field field : fields) {
			if(field.getType().equals(String.class)||field.getType().equals(Integer.class)
					||field.getType().equals(Date.class)||field.getType().equals(BigDecimal.class)) {
				l.add(field.getName());
			}
		}
		List<String> removeList =  new ArrayList<>();	
		//绉婚櫎涓嶅瓨鍦ㄥ搴斿瓧娈电殑key
		SqlHelp help = new SqlHelp();
		List<String> dbkeys = help.getKeysFromDb(tableName);
		for(String beanKey : l) {
			String beanKeyLower = beanKey.toLowerCase();
			if(beanKeyLower.equals("id")||beanKeyLower.equals("refid")) {
				if(!dbkeys.contains(beanKeyLower)) {
					removeList.add(beanKey);
				}
			}else {
				beanKeyLower="f_"+beanKeyLower;
				if(!dbkeys.contains(beanKeyLower)) {
					removeList.add(beanKey);
				}
			}
		}
		l.removeAll(removeList);
		return l.toArray(new String[]{});
	}
	public List<String> getKeysFromDb(String tableName){
		List<String> list = new ArrayList<>();
		try {
			BaseTableMeta meta = TableMetaFactory.getMetaData(BpmConst.LOCAL_DATASOURCE);
			TableModel tableModel = meta.getTableByName(tableName);
			List<ColumnModel> columns = tableModel.getColumnList();
			for(ColumnModel column : columns) {
				list.add(column.getName().toLowerCase());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	public static String getUpdateSql(Class cls,String tableName) {
		Field[] fields =  cls.getDeclaredFields();
		/*String[] keys = SqlHelp.getFieldKeys(cls,tableName);*/
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(tableName).append(" set ");
		for(int i=0;i<fields.length;i++) {
			Field field = fields[i];
			String key  = field.getName();
			if(key.equals("id")) {
				continue;				
			}
			if(key.toLowerCase().equals("refid")) {
				sql.append("").append(key).append("=:").append(key).append(" ");
				if(i!=fields.length-1) {
					sql.append(",\r\n");
				}
				continue;
			}
			if(i==fields.length-1) {
				if (field.getType().equals(Date.class)) {
					sql.append("F_").append(key).append("=to_date(:").append(key).append(", 'YYYY-MM-DD HH24:MI:SS')").append("\r\n");
				}
				else {
					sql.append("F_").append(key).append("=:").append(key).append("\r\n");
				}
				
			}else {
				if (field.getType().equals(Date.class)) {
					sql.append("F_").append(key).append("=to_date(:").append(key).append(", 'YYYY-MM-DD HH24:MI:SS')").append(",\r\n");
				}
				else {
					sql.append("F_").append(key).append("=:").append(key).append(",\r\n");
				}
				
			}
		}
		sql.append(" where id=:id");
		return sql.toString();
	}
	public static String getInsertSql(Class cls,String tableName) {
		Field[] fields =  cls.getDeclaredFields();
//		String[] keys = SqlHelp.getFieldKeys(cls,tableName);
		StringBuffer sql = new StringBuffer();
		sql.append(" insert into ").append(tableName).append("(");
		for(int i=0;i<fields.length;i++) {
			Field field = fields[i];
			String key  = field.getName();
			if(key.equals("id")) {
				sql.append("ID,");	
				continue;
			}	
			if(key.toLowerCase().equals("refid")) {
				if(i!=fields.length-1) {
					sql.append("REFID,");
				}else {
					sql.append("REFID").append(")");
				}
				continue;
			}
			if(i==fields.length-1) {
				sql.append("F_").append(key).append(")");
			}else {
				sql.append("F_").append(key).append(",");
			}
		}
		sql.append("values (");
		for(int i=0;i<fields.length;i++) {
			Field field = fields[i];
			String key  = field.getName();
			if(i==fields.length-1) {
				if (field.getType().equals(Date.class)) {
					sql.append("to_date(:").append(key).append(", 'YYYY-MM-DD HH24:MI:SS')").append(")");
				}else {
					sql.append(":").append(key).append(")");
				}
			}else {
				if (field.getType().equals(Date.class)) {
					sql.append("to_date(:").append(key).append(", 'YYYY-MM-DD HH24:MI:SS')").append(",");
				}else {
					sql.append(":").append(key).append(",");
				}
				
			}
		}		
		return sql.toString();
	}
}
