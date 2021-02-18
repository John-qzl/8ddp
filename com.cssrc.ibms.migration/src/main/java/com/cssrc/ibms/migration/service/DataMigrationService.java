package com.cssrc.ibms.migration.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.migration.model.IMigrationConfigure;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.migration.model.MigrationConfigure;
import com.cssrc.ibms.migration.model.tdm.Schema;
import com.cssrc.ibms.migration.model.tdm.restriction.Restriction;
import com.cssrc.ibms.migration.model.tdm.restriction.SimpleRestriciton;
import com.cssrc.ibms.migration.model.tdm.table.Table;
import com.cssrc.ibms.migration.model.tdm.view.View;
import com.cssrc.ibms.migration.util.FormDefXmlUtil;
import com.cssrc.ibms.migration.util.FormTemplateXmlUtil;
import com.cssrc.ibms.migration.util.LogUtil;
import com.cssrc.ibms.migration.util.TDMUtil;

@Service
public class DataMigrationService {	
	
	@Resource
	private IFormTableService formTableService;
	@Resource
	private IFormDefService formDefService;
	@Resource
	private IGlobalTypeService globalTypeService;
	@Resource
	private IDataTemplateService dataTemplateService;
	public String createXml(MigrationConfigure config){
		String xml = "";
		/**根据数据迁移类型及版本进行分别处理*/
		String type = config.getType();
		String version = config.getVersion();
		switch(type){
		case MigrationConfigure.MIGRATION_TYPE_TDM:
			if(version.equals(MigrationConfigure.TDM_VERSION_QMS)){
				xml = createXml_qms(config);
			}
			break;		
		}
		return xml;
	}
	
	private String createXml_qms(MigrationConfigure config){		
		String xml = "";		
		try{
			MultipartFile schemaFile = config.getSchemaFile();
			String schemaXml = FileUtil.inputStream2String(schemaFile.getInputStream(), "GBK");
			schemaXml = TDMUtil.schemaXmlDeal(schemaXml,config,new StringBuffer());
			Schema schema = (Schema)XmlBeanUtil.unmarshall(schemaXml, Schema.class);
			if(BeanUtils.isEmpty(schema.getAlias())){
				return "数据模型别名不能为空，请为此schema文件添加别名！";
			}
			switch(config.getXmlType()){
			case IMigrationConfigure.XML_FORM_TEMPLATE:
				FormTemplateXmlUtil temUtil = new FormTemplateXmlUtil(formTableService,
						formDefService,globalTypeService);
				xml = temUtil.getXml(schema);
				break;
			case IMigrationConfigure.XML_FORM_DEF:
				FormDefXmlUtil defUtil = new FormDefXmlUtil(formTableService,
						formDefService,globalTypeService,dataTemplateService);
				xml = defUtil.getXml(schema);
				break;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return xml;
	}
	
	
	
	/**
	 * 生成业务表结构、数据字典等信息
	 * @param config
	 * @return
	 */
	public String createBusinessTable(MigrationConfigure config){
		String log = "";
		/**根据数据迁移类型及版本进行分别处理*/
		String type = config.getType();
		String version = config.getVersion();
		switch(type){
		case MigrationConfigure.MIGRATION_TYPE_TDM:
			if(version.equals(MigrationConfigure.TDM_VERSION_QMS)){
				log = saveTable_qms(config);
			}
			break;		
		}
		return log;
	}
	/**
	 * tdm-qms版本生成ibms业务表
	 * @param config
	 * @return
	 */
	private String saveTable_qms(MigrationConfigure config){
		boolean flag = true;
		StringBuffer log = new StringBuffer();	
		try{
			MultipartFile schemaFile = config.getSchemaFile();
			String schemaXml = FileUtil.inputStream2String(schemaFile.getInputStream(), "GBK");
			//0.对schemaXml进行处理
			log.append(LogUtil.getStartLine("schemaXml处理"));
			schemaXml = TDMUtil.schemaXmlDeal(schemaXml,config,log);
			log.append(LogUtil.getEndLine("schemaXml处理"));	
			
			//1.将schema文件解析成schema对象
			log.append(LogUtil.getStartLine("schema文件解析成schema对象"));
			Schema schema = (Schema)XmlBeanUtil.unmarshall(schemaXml, Schema.class);
			log.append(LogUtil.getEndLine("schema文件解析成schema对象"));
			
			//2.schema信息微调
			log.append(LogUtil.getStartLine("schema信息调整"));
			TDMUtil.transform(schema,log);
			log.append(LogUtil.getEndLine("schema信息调整"));
			
			//3.调用接口，保存表
			log.append(LogUtil.getStartLine("调用接口，生成业务表"));
			saveTable(schema,log);	
			log.append(LogUtil.getEndLine("调用接口，生成业务表"));
			
			//4.调用接口，保存约束关系
			log.append(LogUtil.getStartLine("调用接口，保存约束关系"));
			saveRestriction(schema.getSchemaRes(),schema.getRestriction(),log);
			log.append(LogUtil.getEndLine("调用接口，保存约束关系"));
			
			//5.调用接口，保存视图
			log.append(LogUtil.getStartLine("调用接口，保存视图"));
			//saveViews(schema.getViews(),log);
			log.append(LogUtil.getEndLine("调用接口，保存视图"));
		}catch(Exception e){
			//e.printStackTrace();
			flag = false;
			log.append(e);
			log.toString();
			return log.toString();
		}
		return log.toString();
	}
	private void saveTable(Schema schema,StringBuffer log){		
		List<Table> tables = schema.getTables();
		for(int i =tables.size()-1;i>=0;i--){
			Table table = tables.get(i);
			log.append(LogUtil.getLS(1, i+1)).append(table.getTableDesc()).append("\r\n");
			boolean flag = formTableService.saveFormTable(table, log);	
			if(!flag){
				break;
			}
		}
	}
	private void saveRestriction(SimpleRestriciton schemaRes,Restriction res,StringBuffer log){
		//生成schema类别		
		Long typeId = globalTypeService.saveSchemaGlobalType(schemaRes, log);		
		for(int i =res.getSimpleRestriction().size()-1;i>=0;i--){
			SimpleRestriciton simple = res.getSimpleRestriction().get(i);
			simple.setParentId(typeId);
			log.append(LogUtil.getLS(1, i+1)).append(simple.getDisplayName()).append("\r\n");
			boolean flag = globalTypeService.saveDicGlobalType(simple, log);	
			if(!flag){
				break;
			}
		}
	}
	private void saveViews(List<View> views,StringBuffer log){
		for(int i =0;i<views.size();i++){
			View view = views.get(i);
		}
	}
}
