package com.cssrc.ibms.api.form.intf;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.cssrc.ibms.api.form.model.IFormRule;

import freemarker.template.TemplateException;

public interface IFormRuleService {
	
	/**
	 * 产生JS
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void generateJS() throws IOException, TemplateException;

	/**
	 * 导出表单验证规则XML
	 * @param Long[] tableId
	 * @return
	 */
	public String exportXml(Long[] aryScriptId) throws FileNotFoundException, IOException;
	
	

	/**
	 * 导入表单验证规则XML
	 * @param fileStr
	 * @throws Exception
	 */
	public void importXml(InputStream inputStream) throws Exception;
	
	public abstract IFormRule getById(Long valueOf);
}
