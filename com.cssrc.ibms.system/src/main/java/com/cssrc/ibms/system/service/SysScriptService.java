package com.cssrc.ibms.system.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.system.dao.SysScriptDao;
import com.cssrc.ibms.system.model.SysScript;
 
/**
 * 对象功能:自定义表代码模版 Service类
 * @author zhulongchao
 *
 */
@Service
public class SysScriptService extends BaseService<SysScript>
{
	@Resource
	private SysScriptDao dao;
	
	public SysScriptService()
	{
	}
	
	@Override
	protected IEntityDao<SysScript, Long> getEntityDao() 
	{
		return dao;
	}
	
	public List<String> getDistinctCategory() {
		return this.dao.getDistinctCategory();
	}
	/**
	 * 初始化加载代码生成器模板
	 * @throws Exception
	 */
	public void initAllTemplate() throws Exception {
		delByTemplateType((short)1);
		addSysScripts();
	}
	
	/**
	 * 根据模板类型删除模板 0 :自定义模板 1：系统模板
	 * @param templateType
	 */
	private void delByTemplateType(Short templateType) {
		
	}
	
	/**
     * 根据 脚本名称查找 脚本
     * @param name
     * @return
     */
    public SysScript getByName(String name)
    {
        return dao.getByName(name);
    }
	
	/**
	 * 初始化添加form下的模版数据到数据库。
	 * @throws Exception
	 */
	private void addSysScripts()  throws Exception{
		String templatePath=getFormTemplatePath();
		String xml= FileOperator.readFile(templatePath +"codeTemplates.xml");
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		List<Element> list=root.elements();
		for(Element element:list){
			if("templates".equals(element.getName())){
				List<Element> tempList=element.elements();
				for(Element temp:tempList){
					SysScript SysScript=new SysScript();
					String name=temp.attributeValue("name");
					String script=temp.attributeValue("script");
					String category=temp.attributeValue("category");
					String memo=temp.attributeValue("memo");
					SysScript.setName(name);
					SysScript.setScript(script);
					SysScript.setCategory(category);
					SysScript.setMemo(memo);
					SysScript.setId(UniqueIdUtil.genId());
					dao.add(SysScript);
				}
			}
		}
	}
	
	/**
	 * 返回模版物理的路径。
	 * @return
	 */
	private static  String getFormTemplatePath(){
		return FileOperator.getClassesPath() + "template" + File.separator +"code" + File.separator;
	}
	
	/**
	 * 根据模板别名获取模板
	 * @param alias
	 * @return
	 */
	public SysScript getByTemplateAlias(String alias) {
		///return dao.getByTemplateAlias(alias);
		return null;
	}
    
    public void importXml(InputStream inputStream)
        throws Exception
    {
        Document doc = Dom4jUtil.loadXml(inputStream);
        Element root = doc.getRootElement();
        List<Element> itemLists = root.elements();
        if (BeanUtils.isNotEmpty(itemLists))
            for (Element elm : itemLists)
            {
                String itemName = elm.element("name").getText();
                Long scriptId = Long.valueOf(UniqueIdUtil.genId());
                String scriptText = elm.element("content").getText();
                String memoText = elm.element("Memo").getText();
                String scriptType = elm.element("type").getText();
                SysScript script = new SysScript();
                script.setId(scriptId);
                script.setName(itemName);
                script.setScript(scriptText);
                script.setMemo(memoText);
                script.setCategory(scriptType);
                this.dao.add(script);
            }
    }
    
    public String exportXml(Long[] aryScriptId)
        throws FileNotFoundException, IOException
    {
        String strXml = "";
        Document doc = DocumentHelper.createDocument();
        Element root = doc.addElement("List");
        for (int i = 0; i < aryScriptId.length; i++)
        {
            SysScript script = (SysScript)this.dao.getById(aryScriptId[i]);
            if (!BeanUtils.isNotEmpty(script))
                continue;
            exportTable(script, root, "Script");
        }
        
        strXml = doc.asXML();
        return strXml;
    }
    
    private void exportTable(SysScript script, Element root, String nodeName)
    {
        Element elScript = root.addElement(nodeName);
        
        if (StringUtil.isNotEmpty(script.getName()))
        {
            elScript.addElement("name").addText(script.getName());
        }
        if (StringUtil.isNotEmpty(script.getScript()))
        {
            Element elContent = elScript.addElement("content");
            elContent.addCDATA(script.getScript());
        }
        if (BeanUtils.isNotEmpty(script.getCategory()))
        {
            elScript.addElement("type").addText(script.getCategory());
        }
        if (BeanUtils.isNotEmpty(script.getMemo()))
            elScript.addElement("Memo").addText(script.getMemo());
    }
}
