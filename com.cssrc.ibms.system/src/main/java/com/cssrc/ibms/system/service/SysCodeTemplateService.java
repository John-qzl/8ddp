package com.cssrc.ibms.system.service;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.system.dao.SysCodeTemplateDao;
import com.cssrc.ibms.system.model.SysCodeTemplate;
 
/**
 * 对象功能:自定义表代码模版 Service类
 * @author zhulongchao
 *
 */
@Service
public class SysCodeTemplateService extends BaseService<SysCodeTemplate>
{
	@Resource
	private SysCodeTemplateDao dao;
	
	public SysCodeTemplateService()
	{
	}
	
	@Override
	protected IEntityDao<SysCodeTemplate, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 初始化加载代码生成器模板
	 * @throws Exception
	 */
	public void initAllTemplate() throws Exception {
		delByTemplateType((short)1);
		addSysCodeTemplates();
	}
	
	/**
	 * 根据模板类型删除模板 0 :自定义模板 1：系统模板
	 * @param templateType
	 */
	private void delByTemplateType(Short templateType) {
		dao.delByTemplateType(templateType);
	}
	
	
	
	/**
	 * 初始化添加form下的模版数据到数据库。
	 * @throws Exception
	 */
	private void addSysCodeTemplates()  throws Exception{
		String templatePath=getFormTemplatePath();
		String xml= FileOperator.readFile(templatePath +"codeTemplates.xml");
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		List<Element> list=root.elements();
		for(Element element:list){
			if("templates".equals(element.getName())){
				List<Element> tempList=element.elements();
				for(Element temp:tempList){
					SysCodeTemplate sysCodeTemplate=new SysCodeTemplate();
					String alias=temp.attributeValue("alias");
					String name=temp.attributeValue("name");
					String templateDesc=temp.attributeValue("templateDesc");
					String macroAlias=temp.attributeValue("macroAlias");
					String fileName=temp.attributeValue("fileName");
					String dir=temp.attributeValue("dir");
					String isSub=temp.attributeValue("isSub");
					String formEdit=temp.attributeValue("formEdit");
					String formDetail=temp.attributeValue("formDetail");
					String html= FileOperator.readFile(templatePath +alias+".ftl");
					sysCodeTemplate.setTemplateName(name);
					sysCodeTemplate.setTemplateAlias(alias);
					sysCodeTemplate.setTemplateType((short)1);
					sysCodeTemplate.setMemo(templateDesc);
					sysCodeTemplate.setFileDir(dir);
					sysCodeTemplate.setFileName(fileName);
					sysCodeTemplate.setHtml(html);
					sysCodeTemplate.setId(UniqueIdUtil.genId());
					if(StringUtil.isNotEmpty(isSub)){
						if(("true").equals(isSub)){
							sysCodeTemplate.setIsSub((short)1);
						}
					}
					if(StringUtil.isNotEmpty(formEdit)){
						if("true".equals(formEdit)){
							sysCodeTemplate.setFormEdit((short)1);
						}
					}
					if(StringUtil.isNotEmpty(formDetail)){
						if("true".equals(formDetail)){
							sysCodeTemplate.setFormDetail((short)1);
						}
					}
					dao.add(sysCodeTemplate);
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
	public SysCodeTemplate getByTemplateAlias(String alias) {
		return dao.getByTemplateAlias(alias);
	}

}
