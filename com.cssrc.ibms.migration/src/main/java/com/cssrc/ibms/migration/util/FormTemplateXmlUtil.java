package com.cssrc.ibms.migration.util;

import java.util.Iterator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.migration.model.tdm.Schema;
import com.cssrc.ibms.migration.model.tdm.table.Table;
public class FormTemplateXmlUtil {

	private IFormTableService  formTableService;
	private IFormDefService    formDefService;
	private IGlobalTypeService globalTypeService;
	private boolean hasInit = false;
	public void init(IFormTableService formTableService,IFormDefService formDefService,
			IGlobalTypeService globalTypeService){
		this.formTableService = formTableService;
		this.formDefService = formDefService;
		this.globalTypeService = globalTypeService;
		hasInit = true;
	}
	public FormTemplateXmlUtil(){
		
	}
	public FormTemplateXmlUtil(IFormTableService formTableService,IFormDefService formDefService,
			IGlobalTypeService globalTypeService){
		init(formTableService,formDefService,globalTypeService);
	}
	
	public  String getXml(Schema schema) {
		List<Table> tables = schema.getTables();
		Element root = new DOMElement("ibms");
		for(Iterator it = tables.iterator();it.hasNext();){
			Table table = (Table)it.next();
			if(table.isExceptTable()){
				continue;
			}
			Element formTemplate = new DOMElement("formTemplate");
			//formTemplate.addAttribute("templateName", table.getTableDesc());
			formTemplate.addAttribute("alias", table.getTableName()+"_"+schema.getAlias());
			formTemplate.addAttribute("systemAlias", "dataTemplateList_qms");
			formTemplate.addAttribute("templateName", table.getDisplayName()+"-"+schema.getName());
			formTemplate.addAttribute("templateType", IFormTemplate.DATA_TEMPLATE);
			formTemplate.addAttribute("templateDesc", table.getDescription());
			root.add(formTemplate);
		}
		Document doc = DocumentHelper.createDocument(root);
		return doc.asXML();
	}

}
