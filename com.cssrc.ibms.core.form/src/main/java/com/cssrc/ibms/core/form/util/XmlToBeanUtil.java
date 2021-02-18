package com.cssrc.ibms.core.form.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.web.multipart.MultipartFile;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;

public class XmlToBeanUtil {
	/**
	 * 将xml文件
	 * @param document
	 * @return
	 */
	public static Map<String,List> xmlToFormDef(MultipartFile file) throws Exception{
		Document document = Dom4jUtil.loadXml(file.getInputStream(), "UTF-8");
		Element root = document.getRootElement();
		Map<String,List> map = new HashMap();
		List<FormDef> formDefList = new ArrayList<FormDef>();
		List<DataTemplate> dataTempList = new ArrayList<DataTemplate>();
		for (Iterator i = root.elementIterator(); i.hasNext();) {
			Element element = (Element) i.next();
			FormDef formDef = new FormDef();
			formDef.setSubject(element.attributeValue("subject"));
			formDef.setFormAlias(element.attributeValue("formAlias"));
			formDef.setCategoryName(element.attributeValue("categoryKey"));//categoryId 变为 categoryKey
			formDef.setFormDesc(element.attributeValue("formDesc"));
			//根据tableName来获取tableId,现将tableName存储起来
 			
			formDef.setTableName(element.attributeValue("tableName"));
			formDef.setTemplatesId(element.attributeValue("templateAlias"));
			
			
			// formDef.setTemplatesId(element.attributeValue("tableId")+","+element.attributeValue("templateAlias")); //拼接
			formDef.setIsDefault(Short.parseShort(element.attributeValue("isDefault")));
			formDef.setIsPublished(Short.parseShort(element.attributeValue("isPublished")));
			formDef.setVersionNo(Integer.parseInt(element.attributeValue("versionNo")));
			formDef.setTabTitle(element.attributeValue("tabTitle"));
			formDef.setPublishedBy(UserContextUtil.getCurrentUser().getFullname());
			formDef.setPublishTime(DateUtil.getCurrentDate());
			formDefList.add(formDef);				
			for(Iterator j = element.elementIterator();j.hasNext();){
				Element formTemplateElement = (Element)j.next();
				DataTemplate dataTemplate = new DataTemplate();
				dataTemplate.setNeedPage(Short.parseShort(formTemplateElement.attributeValue("needPage")));
				dataTemplate.setPageSize(Integer.parseInt(formTemplateElement.attributeValue("pageSize")));
				dataTemplate.setIsQuery(Short.parseShort(formTemplateElement.attributeValue("isQuery")));
				dataTemplate.setIsFilter(Short.parseShort(formTemplateElement.attributeValue("isFilter")));
				dataTemplate.setIsBakData(Short.parseShort(formTemplateElement.attributeValue("isBakData")));
				dataTemplate.setTemplateAlias(formTemplateElement.attributeValue("templateAlias"));
				dataTemplate.setDisplayField(formTemplateElement.attributeValue("displayField"));
				dataTemplate.setConditionField(formTemplateElement.attributeValue("conditionField"));
				dataTemplate.setSortField(formTemplateElement.attributeValue("sortField"));
				dataTemplate.setManageField(formTemplateElement.attributeValue("manageField"));
				dataTemplate.setFilterField(formTemplateElement.attributeValue("filterField"));
				dataTemplate.setExportField(formTemplateElement.attributeValue("exportField"));
				dataTempList.add(dataTemplate);
				break;
			}
		}
		map.put("formdef", formDefList);
		map.put("dataTemplate", dataTempList);
	  return map;
	}
}
