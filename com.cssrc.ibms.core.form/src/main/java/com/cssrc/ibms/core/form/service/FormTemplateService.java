package com.cssrc.ibms.core.form.service;


import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.form.dao.FormTemplateDao;
import com.cssrc.ibms.core.form.model.FormQuery;
import com.cssrc.ibms.core.form.model.FormTemplate;
import com.cssrc.ibms.core.form.xml.form.FormTemplateXml;
import com.cssrc.ibms.core.form.xml.form.FormTemplateXmlList;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

/**
 * 对象功能:表单模板 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class FormTemplateService extends BaseService<FormTemplate> implements IFormTemplateService
{
	private static Log logger = LogFactory.getLog(FormTemplateService.class);
	@Resource
	private FormTemplateDao dao;
	
	public FormTemplateService()
	{
	}
	
	@Override
	protected IEntityDao<FormTemplate, Long> getEntityDao() 
	{
		return dao;
	}
	
	/**
	 * 返回模版物理的路径。
	 * @return
	 */
	public static  String getFormTemplatePath(){
		
		return SysConfConstant.CONF_ROOT+File.separator + "template" + File.separator +"form" + File.separator;
	}
	
	/**
	 * 根据模版别名取得模版。
	 * @param alias
	 * @return
	 */
	public FormTemplate getByTemplateAlias(String alias){
		return dao.getByTemplateAlias(alias);
	}
	
	/**
	 * 获取所有的系统原始模板
	 * @return
	 * @throws Exception 
	 */
	public void initAllTemplate() throws Exception{
		dao.delSystem();
		addTemplate();
	}
	
	/**
	 * 当模版数据为空时，将form目录下的模版添加到数据库中。
	 */
	public void init()  throws Exception{
		Integer amount=dao.getHasData();
		if(amount==0){
			addTemplate();
		}
	}
	
	/**
	 * 初始化模版，在系统启用的时候进行调用。
	 */
	public void initTemplate(){
		try {
			this.init();
		} catch (Exception e) {
			logger.debug(e.getMessage());
		}
	}
	
	/**
	 * 初始化添加form下的模版数据到数据库。
	 */
	private void addTemplate()  throws Exception{
		TransactionAspectSupport.currentTransactionStatus();
		String templatePath=SysConfConstant.FTL_ROOT+"form"+File.separator;
		String xml= FileOperator.readFile(templatePath +"templates.xml");
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		List<Element> list=root.elements();
		for(Element element:list){
			String alias=element.attributeValue("alias");
			String name=element.attributeValue("name");
			String type=element.attributeValue("type");
			String templateDesc=element.attributeValue("templateDesc");
			String macroAlias=element.attributeValue("macroAlias");
			
			String fileName=alias+".ftl";
			String html= FileOperator.readFile(templatePath +fileName);
		
			FormTemplate bpmFormTemplate=new FormTemplate();
			bpmFormTemplate.setTemplateId(UniqueIdUtil.genId());
			bpmFormTemplate.setMacroTemplateAlias(macroAlias);
			bpmFormTemplate.setHtml(html);
			bpmFormTemplate.setTemplateName(name);
			bpmFormTemplate.setAlias(alias);
			bpmFormTemplate.setCanEdit(0);
			bpmFormTemplate.setTemplateType(type);
			bpmFormTemplate.setTemplateDesc(templateDesc);
			dao.add(bpmFormTemplate);
		}
	}
	
	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	public boolean isExistAlias(String alias){
		List<FormTemplate>list=dao.getAll();
		for(FormTemplate bpmFormTemplate:list){
			if(bpmFormTemplate.getAlias().equals(alias)){
				return true;
			}
		}
		return false;
	}
	
	
	
	
	/**
	 * 将用户自定义模板备份
	 * @param id
	 */
	public void backUpTemplate(Long id){
		FormTemplate bpmFormTemplate=dao.getById(id);
		String alias=bpmFormTemplate.getAlias();
		String name=bpmFormTemplate.getTemplateName();
		String desc=bpmFormTemplate.getTemplateDesc();
		String html=bpmFormTemplate.getHtml();
		String type=bpmFormTemplate.getTemplateType();
		String macroAlias=bpmFormTemplate.getMacroTemplateAlias();
		
		String templatePath=getFormTemplatePath();
		
		String xmlPath=templatePath +"templates.xml";
		String xml= FileOperator.readFile(xmlPath);
		
		Document document=Dom4jUtil.loadXml(xml);
		Element root=document.getRootElement();
		
		Element e=root.addElement("template");
		e.addAttribute("alias", alias);
		e.addAttribute("name", name);
		e.addAttribute("type", type);
		e.addAttribute("templateDesc", desc);
		e.addAttribute("macroAlias",macroAlias);
		String content=document.asXML();
		
		FileOperator.writeFile(xmlPath, content);
		FileOperator.writeFile(templatePath +alias+".ftl", html);
		
		bpmFormTemplate.setCanEdit(0);
		dao.update(bpmFormTemplate);
		
	}
	
	//根据别名判断记录是否已经存在，区别isExistAlias（提高效率）
	public boolean isExistAlias2(String alias) {
		FormTemplate formTemplate = dao.getByTemplateAlias(alias);
		if (BeanUtils.isEmpty(formTemplate)) {
			return false;
		}
		return true;
	}
	/**
	 * 根据模版类型取得模版列表。
	 * @param type
	 * @return
	 */
	public List<FormTemplate> getTemplateType(String type){
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("templateType",type);
		return dao.getAll( params);
	}
	/**
	 * xml to bean
	 */
	public void readXml(MultipartFile file,ResultMessage msg) throws Exception {
		Document document = Dom4jUtil.loadXml(file.getInputStream(), "UTF-8");
		List<FormTemplate> list = xmlToFormTemplate(document);
		try {
		//通过for循环，导入模板
		for (int i = 0; i < list.size(); i++) {
			FormTemplate formt = list.get(i);
			// 根据别名的唯一性来判断进行添加还是更新操作
			String alias = formt.getAlias().split("@")[0];
			String sysAlias = formt.getAlias().split("@")[1];
			formt.setAlias(alias);
			boolean isExist = this.isExistAlias2(alias);
			if (isExist) {
				// 进行更新操作
				FormTemplate formt2 = dao.getByTemplateAlias(alias);
				formt2.setTemplateName(formt.getTemplateName());
				formt2.setTemplateType(formt.getTemplateType());
				formt2.setTemplateDesc(formt.getTemplateDesc());
				if(BeanUtils.isEmpty(formt2.getHtml())){
					FormTemplate sysformtemplate = dao.getByTemplateAlias(sysAlias);
					formt2.setHtml(sysformtemplate.getHtml());
				}
				// 最终完整数据为formt2
				dao.update(formt2);
			} else {
				// 进行添加操作
				FormTemplate sysformtemplate = dao.getByTemplateAlias(sysAlias);
				formt.setCanEdit(1);
				formt.setHtml(sysformtemplate.getHtml());
				formt.setMacroTemplateAlias(sysformtemplate.getMacroTemplateAlias());
				formt.setTemplateId(UniqueIdUtil.genId());
				// 最终完整数据为 formt
				dao.add(formt);
			}
		} //for循环结束
		msg.setResult(ResultMessage.Success);
		msg.setMessage("模板批量导入成功！");
	} catch(Exception e){
		msg.setResult(ResultMessage.Fail);
		msg.setMessage(e.getMessage());
	}
	}

	private List<FormTemplate> xmlToFormTemplate(Document document) {
		Element root = document.getRootElement();
		List<FormTemplate> fts = new ArrayList<FormTemplate>();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			FormTemplate ft = new FormTemplate();
			ft.setTemplateName(element.attributeValue("templateName"));
			ft.setAlias(element.attributeValue("alias")+"@"+element.attributeValue("systemAlias"));
			ft.setTemplateType(element.attributeValue("templateType"));
			ft.setTemplateDesc(element.attributeValue("templateDesc"));
			fts.add(ft);
		}
		return fts;
	}
	
	/**
	 * 获取主表模版
	 * @return
	 */
	public List<FormTemplate> getAllMainTableTemplate() {
		return getTemplateType(FormTemplate.MAIN_TABLE);
	}
	
	/**
	 * 获取子表模版。
	 * @return
	 */
	public List<FormTemplate> getAllSubTableTemplate() {
		return getTemplateType(FormTemplate.SUB_TABLE);
	}
	/**
	 * 获取关系表模版。
	 * @return
	 */
	public List<FormTemplate> getAllRelTableTemplate() {
		return getTemplateType(FormTemplate.REL_TABLE);
	}

	/**
	 * 获取宏模版。
	 * @return
	 */
	public List<FormTemplate> getAllMacroTemplate() {
		return getTemplateType(FormTemplate.MACRO);
	}
	
	/**
	 * 获取表管理模版。
	 * @return
	 */
	public List<FormTemplate> getAllTableManageTemplate() {
		return getTemplateType(FormTemplate.TABLE_MANAGE);
	}
	
	/**
	 * 获取列表模版。
	 * @return
	 */
	public List< FormTemplate> getListTemplate() {
		return getTemplateType(FormTemplate.LIST);
	}
	
	/**
	 * 获取明细模版。
	 * @return
	 */
	public List< FormTemplate> getDetailTemplate() {
		return getTemplateType(FormTemplate.DETAIL);
	}
	
	/**
	 * 获取数据模版。
	 * @return
	 */
	public List< FormTemplate> getDataTemplate() {
		return getTemplateType(FormTemplate.DATA_TEMPLATE);
	}
	  public List<FormTemplate> getQueryDataTemplate()
	  {
	    return getTemplateType("queryDataTemplate");
	  }
	  
	  
	/**
	 * headhtml
	 * @param alias
	 * @return
	 */
	public String getHeadHtml(String alias){
		FormTemplate temp = this.getByTemplateAlias(alias);
		return temp.getHeadHtml();
	}
	/**
	 * 进行el表达式替换后的headhtml
	 * @param alias
	 * @param map
	 * @return
	 */
	public String getHeadHtmlDealed(String alias,Map map){
		String headHtml = this.getHeadHtml(alias);
		headHtml = headHtml==null?"":headHtml;
		return StringUtil.elReplace(headHtml, map);
	}
	
    /**
     * 导出表单模板XML
     * @param formTemplateIds
     * @return
     * @throws Exception
     */
    public String exportXml(Long[] formTemplateIds) throws Exception{
    	FormTemplateXmlList formTemplateXmlList = new FormTemplateXmlList();
    	List list = new ArrayList();
    	for (int i=0; i<formTemplateIds.length; i++){
    		FormTemplate formTemplate = (FormTemplate)this.dao.getById(formTemplateIds[i]);
    		FormTemplateXml formTemplateXml = exportFormTemplateXml(formTemplate);
    		list.add(formTemplateXml);
    	}
    	formTemplateXmlList.setFormTemplateXmlList(list);
    	return XmlBeanUtil.marshall(formTemplateXmlList, FormTemplateXmlList.class);
    }
    
	private FormTemplateXml exportFormTemplateXml(FormTemplate formTemplate)
			throws Exception {
		FormTemplateXml formTemplateXml = new FormTemplateXml();
		Long id = formTemplate.getTemplateId();
		if (BeanUtils.isNotEmpty(id)) {
			formTemplateXml.setFormTemplate(formTemplate);
		}

		return formTemplateXml;
	}
	
    /**
     * 导入表单模板XML
     * @param inputStream
     * @throws Exception
     */
	
	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();
		XmlUtil.checkXmlFormat(root, "form", "formTemplates");
		String xmlStr = root.asXML();
		FormTemplateXmlList formTemplateXmlList = (FormTemplateXmlList) XmlBeanUtil
				.unmarshall(xmlStr, FormTemplateXmlList.class);
		List<FormTemplateXml> list = formTemplateXmlList.getFormTemplateXmlList();
		for (FormTemplateXml formTemplateXml : list) {
			importFormTemplateXml(formTemplateXml);
		}
	}
	
	private void importFormTemplateXml(FormTemplateXml formTemplateXml)
			throws Exception {
		Long queryId = Long.valueOf(UniqueIdUtil.genId());
		FormTemplate formTemplate = formTemplateXml.getFormTemplate();
		if (BeanUtils.isEmpty(formTemplate)) {
			throw new Exception();
		}
		String alias = formTemplate.getAlias();
		FormTemplate query = this.dao.getByTemplateAlias(alias);
		if (BeanUtils.isNotEmpty(query)) {
			MsgUtil.addMsg(2, "别名为‘" + alias + "’的自定义表单模板已经存在，请检查你的xml文件！");
			return;
		}
		formTemplate.setTemplateId(queryId);
		this.dao.add(formTemplate);
		MsgUtil.addMsg(1, "别名为" + alias + "的自定义表单模板导入成功！");
	}
}
