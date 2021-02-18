package com.cssrc.ibms.core.form.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormRuleService;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.dao.FormRuleDao;
import com.cssrc.ibms.core.form.model.FormRule;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;

import freemarker.template.TemplateException;

/**
 * 对象功能:表单验证规则 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class FormRuleService extends BaseService<FormRule> implements IFormRuleService
{
	@Resource
	private FormRuleDao dao;
	
	public FormRuleService()
	{
	}
	
	@Override
	protected IEntityDao<FormRule, Long> getEntityDao() 
	{
		return dao;
	}
	
	@Resource
	private FreemarkEngine freemarkEngine;
	
	/**
	 * 返回规则验证文件。
	 * @return
	 */
	private String getRuleJsPath(){
		return FileOperator.getRootPath() + "jslib" + File.separator +"ibms" 
				+ File.separator +"oa" + File.separator +"form" +  File.separator +"rule.js" ;
	}
	
	/**
	 * 产生JS
	 * @throws IOException 
	 * @throws TemplateException 
	 */
	public void generateJS() throws IOException, TemplateException {
		List<FormRule> list = dao.getAll();
		Map<String,Object> obj=new HashMap<String, Object>();
		obj.put("ruleList", list);
		String content=freemarkEngine.mergeTemplateIntoString("rulejs.ftl",obj);
		String fileName=getRuleJsPath();
		//写入js文件。
		FileOperator.writeFile(fileName, content);
	}

	/**
	 * 导出表单验证规则XML
	 * @param Long[] tableId
	 * @return
	 */
	public String exportXml(Long[] aryScriptId) throws FileNotFoundException, IOException{		
		String strXml="";
		Document doc = DocumentHelper.createDocument();
		Element root = doc.addElement("List");
		for(int i=0;i<aryScriptId.length;i++){
			FormRule bpmFormRule=dao.getById(aryScriptId[i]);
			if(BeanUtils.isNotEmpty(bpmFormRule)){	
				// 生成主表
				exportTable(bpmFormRule, root, "Script");
			}
		}
		strXml=doc.asXML();
		return strXml;
		
	}
	
	/**
	 * 生成表代码
	 * @param bpmFormTable
	 * @param root
	 * @param nodeName
	 */
	private void exportTable(FormRule bpmFormRule, Element root, String nodeName) 
	{		
		Element elScript = root.addElement(nodeName);
		
		if(StringUtil.isNotEmpty(bpmFormRule.getName())){
			elScript.addElement("name").addText(bpmFormRule.getName());
		}
		if(StringUtil.isNotEmpty(bpmFormRule.getRule())){
			Element elContent=elScript.addElement("rule");
			elContent.addCDATA(bpmFormRule.getRule());
		}
		if(BeanUtils.isNotEmpty(bpmFormRule.getTipInfo())){
			elScript.addElement("tipInfo").addText(bpmFormRule.getTipInfo());
		}
		if(BeanUtils.isNotEmpty(bpmFormRule.getMemo())){
			elScript.addElement("Memo").addText(bpmFormRule.getMemo());
		}
	}

	/**
	 * 导入表单验证规则XML
	 * @param fileStr
	 * @throws Exception
	 */
	public void importXml(InputStream inputStream) throws Exception
	{
		Document doc=Dom4jUtil.loadXml(inputStream);
        Element root = doc.getRootElement();
        List<Element> itemLists = root.elements();
        if(BeanUtils.isNotEmpty(itemLists)){
    		for(Element elm : itemLists) {
    			String itemName=elm.element("name").getText();   			
    				Long bpmFormRuleId = UniqueIdUtil.genId(); //产生id
    				String rule=elm.element("rule").getText();
    				String memoText=elm.element("Memo").getText();
    				String tipInfo=elm.element("tipInfo").getText();
    				FormRule bpmFormRule=new FormRule();
    				bpmFormRule.setId(bpmFormRuleId);
    				bpmFormRule.setName(itemName);
    				bpmFormRule.setRule(rule);
    				bpmFormRule.setMemo(memoText);
    				bpmFormRule.setTipInfo(tipInfo);
    				dao.add(bpmFormRule);		
    		}
        }
	}
}
