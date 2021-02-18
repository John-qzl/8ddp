package com.fr.function;  
  

import java.io.IOException;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import com.fr.script.AbstractFunction;  
  
public class SCWJNAME extends AbstractFunction {  
  
    public Object run(Object[] args) { 
    	String jsonstr = (String) args[0];
    	//判断是否为null或者空
    	if(jsonstr==""){
    		return "参数为空";
    	}
    	try{
    		System.out.println("======="+jsonstr);
           // 获取公式中的参数  
        JSONArray parasArray = JSONArray.fromObject(args[0].toString()); 
        JSONObject jsonobject = new JSONObject();
        if (!parasArray.isEmpty()) { 
           for (int i = 0; i < parasArray.size(); i++) { 
           	jsonobject = parasArray.getJSONObject(i); 
           	String name=(String) jsonobject.get("name");  
           	return name; 
           }  
            }  
    	}catch(Exception e){
    		return "error";
    	}
         return "空";
    }  
    public static void main(String arg[]) throws IOException {
    	String[] ss = new String[1];
    	ss[0] = "[{\"id\":\"1302\",\"name\":\"2016-4-8.docx\"}]";
    	
    	SCWJNAME tt = new SCWJNAME();
    	Object aa = tt.run(ss);
	

	}
    
}
