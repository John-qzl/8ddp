package com.cssrc.ibms.core.form.util;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.cssrc.ibms.api.system.model.ISysFile;

public class FileGridUtil {
	public static JSONArray getColumns(){
		JSONArray columns = new JSONArray();
		JSONObject fileId = JSONObject.fromObject("{display:\"文件ID\",name:\"fileId\",width:0,hide:\"true\"}");
		JSONObject fileName = JSONObject.fromObject("{display:\"文件名\",name:\"filename\",width:400}");
		JSONObject createtime = JSONObject.fromObject("{display:\"文件版本\",name:\"version\",width:100,type:\"date\"}");
		JSONObject creator = JSONObject.fromObject("{display:\"上传人\",name:\"creator\",width:100}");
		JSONObject totalBytes = JSONObject.fromObject("{display:\"文件大小\",name:\"totalBytes\",width:100}");
		columns.add(fileId);	
		columns.add(fileName);	
		columns.add(createtime);	
		columns.add(creator);	
		columns.add(totalBytes);
		return columns;
	}
	public static JSONObject getData(List<? extends ISysFile> list){
		int total = list.size();
		JSONArray rows = new JSONArray();
		for(int i = 0;i<list.size();i++){
			JSONObject row = transSysFile(list.get(i));
			rows.add(row);
		}
		JSONObject data = JSONObject.fromObject("{Rows:\"\",Total:\""+total+"\"}");
		data.put("Rows", rows);
		return data;
	}
	private static JSONObject transSysFile(ISysFile sysFile){
		JSONObject file = JSONObject.fromObject(sysFile);
		JSONObject row = new JSONObject();
		JSONArray columns  = getColumns();
		for(Iterator it = columns.iterator();it.hasNext();){
			JSONObject column = JSONObject.fromObject(it.next());
			String key = column.getString("name");
			String value = file.getString(key);	
			value = calculationData(key,value);
			
			row.put(key, value);
		}
		return row;
	}
	public static String calculationData(String key,String value){
		String newValue = value;
		if(key.equals("totalBytes")){
			newValue = Integer.valueOf(value)/1024 +"KB";
		}
		return newValue;
	}
}
