package com.cssrc.ibms.migration.util;

import java.util.Iterator;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.dom.DOMElement;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.migration.model.MigrationGlobalType;
import com.cssrc.ibms.migration.model.tdm.Schema;
import com.cssrc.ibms.migration.model.tdm.table.Table;

public class FormDefXmlUtil {
	public static String ADD_BUTTON = "{\"desc\":\"新增\",\"unique\":\"101\",\"name\":\"add\",\"paramscript\":\"\",\"prescript\":\"\",\"afterscript\":\"\",\"right\":[{\"s\":4,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}";
	public static String EDIT_BUTTON = "{\"desc\":\"编辑\",\"unique\":\"102\",\"name\":\"edit\",\"paramscript\":\"\",\"prescript\":\"\",\"afterscript\":\"\",\"right\":[{\"s\":4,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}";
	public static String DEL_BUTTON = "{\"desc\":\"删除\",\"unique\":\"103\",\"name\":\"del\",\"paramscript\":\"\",\"prescript\":\"\",\"afterscript\":\"\",\"right\":[{\"s\":4,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}";
	public static String DETAIL_BUTTON = "{\"desc\":\"明细\",\"unique\":\"104\",\"name\":\"detail\",\"paramscript\":\"\",\"prescript\":\"\",\"afterscript\":\"\",\"right\":[{\"s\":4,\"type\":\"everyone\",\"id\":\"\",\"name\":\"\",\"script\":\"\"}]}";
	
	private IFormTableService  formTableService;
	private IFormDefService    formDefService;
	private IGlobalTypeService globalTypeService;
    private IDataTemplateService dataTemplateService;
	private boolean hasInit = false;
	public void init(IFormTableService formTableService,IFormDefService formDefService,
			IGlobalTypeService globalTypeService,IDataTemplateService dataTemplateService){
		this.formTableService = formTableService;
		this.formDefService = formDefService;
		this.globalTypeService = globalTypeService;
		this.dataTemplateService = dataTemplateService;
		hasInit = true;
	}
	public FormDefXmlUtil(){
		
	}
	public FormDefXmlUtil(IFormTableService formTableService,IFormDefService formDefService,
			IGlobalTypeService globalTypeService,IDataTemplateService dataTemplateService){
		init(formTableService,formDefService,globalTypeService,dataTemplateService);
	}
	public  String getXml(Schema schema) {
		String nodeKey = createFormType(schema);
		List<Table> tables = schema.getTables();
		if(!hasInit){
			Element root = new DOMElement("ibms");
			root.addText("未进行初始化，无法生成xml文件！");
			Document doc = DocumentHelper.createDocument(root);
			return doc.asXML();
		}
		Element root = new DOMElement("ibms");
		for(Iterator it = tables.iterator();it.hasNext();){
			Table table = (Table)it.next();
			if(table.isExceptTable()){
				continue;
			}
			IFormTable formtable = formTableService.getByTableName(table.getTableName());
			Element formDef = new DOMElement("formDef");
			formDef.addAttribute("subject", table.getTableDesc()+"-"+schema.getName());
			formDef.addAttribute("formAlias", table.getTableName()+"_"+schema.getAlias());
			formDef.addAttribute("categoryKey", nodeKey);
			formDef.addAttribute("formDesc", table.getDescription());
			formDef.addAttribute("tableName",table.getTableName()+"_"+schema.getAlias());
			formDef.addAttribute("templateAlias", "twoColumnDiv");
			formDef.addAttribute("isDefault",String.valueOf(IFormDef.IS_DEFAULT));
			formDef.addAttribute("isPublished", String.valueOf(IFormDef.IS_PUBLISHED));//已发布
			formDef.addAttribute("versionNo", "1");
			formDef.addAttribute("tabTitle", "页面一");
			formDef.add(getDataTemplate(table));
			root.add(formDef);
		}
		Document doc = DocumentHelper.createDocument(root);
		return doc.asXML();
	}
	private String createFormType(Schema schema){
		MigrationGlobalType gl = new MigrationGlobalType();
		gl.setCatKey(IGlobalType.CAT_FORM);
		gl.setNodeKey(schema.getAlias());
		gl.setTypeName(schema.getName());
		gl.setTypeId(UniqueIdUtil.genId());
		String nodeKey = globalTypeService.createFormGlobalType(JSONObject.fromObject(gl));
		return nodeKey;
	}
	private  Element getDataTemplate(Table table){
		IFormTable formTable = formTableService.getByTableName(table.getTableName());
		Element datatemplate = new DOMElement("dataTemplate");
		datatemplate.addAttribute("needPage", "1");
		datatemplate.addAttribute("pageSize", "20");
		datatemplate.addAttribute("isQuery", "1");
		datatemplate.addAttribute("isFilter", "1");
		datatemplate.addAttribute("isBakData", "0");
		datatemplate.addAttribute("templateAlias", table.getTableName());
		datatemplate.addAttribute("displayField", "");
		datatemplate.addAttribute("conditionField", "");
		datatemplate.addAttribute("sortField", "");
		datatemplate.addAttribute("manageField", getManageField(table,formTable));
		datatemplate.addAttribute("filterField", "");
		datatemplate.addAttribute("exportField","");	
		return datatemplate;
	}
	private String getManageField(Table table,IFormTable formTable){
		
		return getDefaultManageField();
	}
	/**
	 * 增加默认按钮：新增、修改、删除、详细
	 * 权限：所有人
	 * @return
	 */
	public static String getDefaultManageField(){
		JSONArray arr =	new JSONArray();
		JSONObject add = JSONObject.fromObject(ADD_BUTTON);
		JSONObject edit = JSONObject.fromObject(EDIT_BUTTON);
		JSONObject del = JSONObject.fromObject(DEL_BUTTON);
		JSONObject detail = JSONObject.fromObject(DETAIL_BUTTON);
		arr.add(add);arr.add(edit);arr.add(del);arr.add(detail);
		return arr.toString();
	}
}
