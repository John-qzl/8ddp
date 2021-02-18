package com.cssrc.ibms.core.form.service;

import javax.annotation.Resource;

import com.cssrc.ibms.core.form.intf.IFormWebService;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormQuery;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.form.model.QueryResult;
import com.cssrc.ibms.core.form.util.FormDataUtil;
import com.cssrc.ibms.core.util.json.JacksonMapper;
import com.cssrc.ibms.core.util.string.StringUtil;
 
public class FormWebServiceImpl implements IFormWebService {
	@Resource
	private FormTableService formTableService;
	@Resource
	private FormHandlerService formHandlerService;
	@Resource
	private DataTemplateService dataTemplateService;
	@Resource
	private FormQueryService formQueryService;
	
	public String getFormData(String tableName,String pkValue){
		FormTable table = formTableService.getByTableName(tableName,1);
		JacksonMapper mapper = new JacksonMapper();
		String res = "";
		try {
			FormData data = formHandlerService.getByKey(null,table.getTableId(), pkValue);
		    res = mapper.toJson(data);
		} catch (Exception e) { 
			e.printStackTrace();
			res = "获取失败:"+e.getMessage();
		}
		return res;
	}
	
	public boolean saveFormData(String tableName,String data,String pkId){
	    boolean saveOk = false;
	    FormTable table = formTableService.getByTableName(tableName,1);
	    try{
	    	if(StringUtil.isEmpty(pkId)){
	 	    	FormData formData=FormDataUtil.parseJson(data,table);
	 	    	formHandlerService.handFormData(formData);
	 	    }else{
	 			PkValue pkValue=new PkValue(table.getPkField(), pkId);
	 			pkValue.setIsAdd(false);
	 			FormData formData=FormDataUtil.parseJson(data,pkValue,table);
	 			formHandlerService.handFormData(formData);
	 		}
	    	saveOk = true;
	    }catch(Exception e){
	    	e.printStackTrace();
	    }
	   
	    return saveOk;
	}
	 
	public boolean deleteFormData(String tableName,String pkId){
		boolean delOk = false;
		FormTable table = formTableService.getByTableName(tableName,1);
		try {
			dataTemplateService.deleteData(table.getTableId(), pkId);
			delOk = true;
		} catch (Exception e) { 
			e.printStackTrace();
		}
	    return delOk;
	}
	
	public String queryFormData(String queryName,String queryData){
		FormQuery formQuery = formQueryService.getByAlias(queryName);
		JacksonMapper mapper = new JacksonMapper();
		String res = "";
		try {
			QueryResult data = formQueryService.getData(formQuery, queryData, -1, -1);
			res = mapper.toJson(data);
		} catch (Exception e) { 
			e.printStackTrace();
			res = "查询失败:"+e.getMessage();
		}
		return res;
		
	}

}
