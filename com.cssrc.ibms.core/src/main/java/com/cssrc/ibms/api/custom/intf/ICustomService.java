package com.cssrc.ibms.api.custom.intf;

import java.util.Map;

import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;

/**
 * 本接口的实现类
 * <p>ICustomService.java</p>
 * @author dengwenjie 
 * @date 2017年6月15日
 */
public interface ICustomService {
	/**
	 * DataTemplateService.class-getData()方法
	 * 对业务数据模板-列表sql,进行项目级定制
	 * notice: 如果在方法体内有数据的查询，请使用jdbcHelper的queryForList方法
	 * */
	public String getCustomDataTemplateSql(JdbcHelper jdbcHelper,IDataTemplate bpmDataTemplate,IFormTable bpmFormTable,
			Map<String, Object> params,String sql);
	/**
	 * DataTemplateService.class-getData()方法
	 * 对业务数据模板-freemark的map参数,进行项目级定制
	 * */
	public Map getCustomDataTemplateMap(IDataTemplate bpmDataTemplate,IFormTable bpmFormTable,
			Map<String, Object> params,Map<String, Object> map);
	
	/**
	 * FormDialogService.class-getData()方法
	 * 对自定义对话框sql,进行项目级定制
	 * bpmFormDialog:自定义对话框对象
	 * formtable：表对象
	 * params:前端传的参数
	 * notice: 如果在方法体内有数据的查询，请使用jdbcHelper的queryForList方法
	 * */
	public String getCustomFormDialogSql(JdbcHelper jdbcHelper,IFormDialog bpmFormDialog,IFormTable formtable,
			Map<String, Object> params,Map<String,Object> map,String sql);
}
