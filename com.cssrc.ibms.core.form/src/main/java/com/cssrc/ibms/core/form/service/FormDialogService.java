package com.cssrc.ibms.core.form.service;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.custom.intf.ICustomService;
import com.cssrc.ibms.api.form.intf.IFormDialogService;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.db.mybatis.page.PagingBean;
import com.cssrc.ibms.core.db.util.JdbcHelperUtil;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.form.dao.FormDialogDao;
import com.cssrc.ibms.core.form.model.DialogField;
import com.cssrc.ibms.core.form.model.FormDialog;
import com.cssrc.ibms.core.form.model.FormDialogXml;
import com.cssrc.ibms.core.form.model.FormDialogXmlList;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.util.SqlUtil;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.IDbView;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;

/**
 * 对象功能:通用表单对话框 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class FormDialogService extends BaseService<FormDialog> implements IFormDialogService{
	@Resource
	private FormDialogDao dao;
	@Resource
	private GroovyScriptEngine  groovyScriptEngine;
	@Resource
	private FormTableService formTableService;
	@Resource
	private DataTemplateService dataTemplateService;


	public FormDialogService()
	{
	}

	@Override
	protected IEntityDao<FormDialog, Long> getEntityDao() 
	{
		return dao;
	}

	/**
	 * 检查模板别名是否唯一
	 * @param alias
	 * @return
	 */
	public boolean isExistAlias(String alias){
		return dao.isExistAlias(alias)>0;
	}

	/**
	 * 检查模板别名是否唯一。
	 * @param alias
	 * @return
	 */
	public boolean isExistAliasForUpd(Long id, String alias){
		return dao.isExistAliasForUpd(id,alias)>0;
	}


	/**
	 * 根据别名获取对话框对象。
	 * @param alias
	 * @return
	 */
	public FormDialog getByAlias(String alias){
		return dao.getByAlias(alias);
	}

	/**
	 * 返回树型结构的数据。
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	public List getTreeData(String alias) throws Exception{
		FormDialog bpmFormDialog= dao.getByAlias(alias);
		String sql=getTreeSql(bpmFormDialog);
		List list=dao.queryForList(sql);
		return list;
	}

	/**
	 * 返回树型结构的数据。
	 * 
	 * @param alias
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "rawtypes" })
	public List getTreeData(String alias,Map<String,Object> params,boolean isRoot) throws Exception {
		FormDialog bpmFormDialog = dao.getByAlias(alias);
		String sql=null;
		sql = getTreeSql(bpmFormDialog,params,isRoot);
		
		List list = dao.queryForList(sql);
		return list;
	}

	/**
	 * {"id":"id","pid":"fatherId","displayName":"name"}
	 * 
	 * @param displayField
	 * @param objName
	 * @return
	 * @throws Exception 
	 */
	private String getTreeSql(FormDialog bpmFormDialog,Map<String,Object> nodeMap,boolean isRoot) throws Exception{
		String objName = bpmFormDialog.getObjname();		
		List<DialogField> conditionList = bpmFormDialog.getConditionList();
		Map<String, Object> params = new HashMap<String, Object>();
		if(BeanUtils.isNotEmpty(nodeMap)){
			params = nodeMap ;
		}
		String pname = (String) nodeMap.get("pname") ;
		String pvalue = (String) nodeMap.get("pvalue") ;
		String displayField = bpmFormDialog.getDisplayfield();
		JSONObject jsonObj = JSONObject.fromObject(displayField);
		String id = jsonObj.getString("id");
		String pid = jsonObj.getString("pid");
		if(StringUtil.isNotEmpty(pname) && !isRoot){
			DialogField pfield =  getDailogField(bpmFormDialog,pname);
			if(pfield!=null){
				pfield.setCondition("=");
				conditionList.add(pfield);
				params.put(pname, pvalue);
			}
		}
		// 获取条件的SQL语句
		String sqlWhere = SqlUtil.getWhere(conditionList, params);
		if(!isRoot){
			if(sqlWhere.indexOf(pid)<0 && StringUtil.isNotEmpty(pvalue)){
				//若父节点ID的值不为空，则添加查找条件
				if(StringUtil.isEmpty(sqlWhere))
					sqlWhere += " where " + pid + "=" + pvalue ;
				else
					sqlWhere += " and " + pid + "=" + pvalue ;
			}
		}else{
			//父节点ID的值，可为空字符串。在设置树形对话框时可对其进行赋值，以获取指定父节点的树
			pvalue = jsonObj.getString("pvalue") ;
			String isScript = jsonObj.getString("isScript");

			if(StringUtil.isNotEmpty(pvalue) && !"1".equals(pvalue)){
				if("true".equals(isScript)){
					//父节点ID的值为脚本表达式
					pvalue = groovyScriptEngine.executeObject(pvalue, null).toString();
				}
				//若父节点ID的值不为空，则添加查找条件
				if(StringUtil.isNotEmpty(sqlWhere)){
					if(sqlWhere.indexOf(pid)<0)
						sqlWhere+=" AND "+pid+"="+pvalue;
				}else{
					sqlWhere+=" WHERE "+pid+"="+pvalue;
				}
			}
			if(StringUtil.isEmpty(sqlWhere)  && "1".equals(pvalue)){
				sqlWhere+=" WHERE "+pid+"="+pvalue;
			}
		}
		String sqlSelect = getSelectSQl(bpmFormDialog);
		String isParent = ", ( case (select count(*)  from "+objName+" p where p."+pid+"=o."+id+" and p."+id+"!=p."+pid+") when 0 then 'false' else 'true' end )isParent ";
		sqlSelect+=isParent;
		List<DialogField> sortList = bpmFormDialog.getSortList() ;
		String orderBy = "" ;
		for(int i=0;i<sortList.size();i++){
			DialogField df = sortList.get(i);
			if(i==0){
				orderBy = " order by ";
			}
			orderBy += df.getFieldName() + " " + df.getComment();
			if(i!=sortList.size()-1){
				orderBy +=  ",";
			}else{
				sqlWhere += orderBy ;
			}
		}
		String sql = "SELECT " + sqlSelect + " FROM " + objName +" o "+ sqlWhere;
		return sql;
	}

	private DialogField getDailogField(FormDialog bpmFormDialog,String fieldname) throws Exception{
		TableModel tableModel;
		int istable = bpmFormDialog.getIstable();
		String dsName = bpmFormDialog.getDsalias();
		String objectName = bpmFormDialog.getObjname();
		// 表
		if (istable == 1) {
			BaseTableMeta meta = TableMetaFactory.getMetaData(dsName);
			tableModel = meta.getTableByName(objectName);
		}
		// 视图处理
		else {
			IDbView dbView = TableMetaFactory.getDbView(dsName);
			tableModel = dbView.getModelByViewName(objectName);
		}
		List<ColumnModel> columns =  tableModel.getColumnList();
		for(ColumnModel column:columns){
			if(column.getName().equalsIgnoreCase(fieldname)){
				DialogField field = new DialogField();
				field.setComment(column.getComment());
				field.setFieldName(column.getName());
				field.setFieldType(column.getColumnType());
				return field;
			}
		}
		return null;
	}


	/**
	 * 根据别名获取对应对话框的数据。
	 * @param alias		对话框别名。
	 * @param params	参数集合。
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public FormDialog getData(String alias,Map<String, Object> params) throws Exception{
		FormDialog bpmFormDialog= dao.getByAlias(alias);
		JdbcHelper jdbcHelper= JdbcHelperUtil.getJdbcHelper(bpmFormDialog.getDsalias());
		FormTable formTable = null;
		String objName = bpmFormDialog.getObjname();
		if(objName.indexOf("w_")>=0){
			formTable = formTableService.getByTableName(objName.replace("w_", ""), FormTable.IS_MAIN);
		}
		List<DialogField> displayList=bpmFormDialog.getDisplayList();
		List<DialogField> conditionList=bpmFormDialog.getConditionList();
		String  objectName=bpmFormDialog.getObjname();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("objectName", objectName);
		map.put("displayList", displayList);
		map.put("conditionList", conditionList);
		map.put("sortList", bpmFormDialog.getSortList());
		//是否是列表
		if(bpmFormDialog.getStyle()==0){
			//是否需要分页。
			if(bpmFormDialog.getNeedpage()==1){
				int currentPage=1;
				Object pageObj=params.get(FormDialog.Page);
				if(pageObj!=null){
					currentPage=Integer.parseInt( params.get(FormDialog.Page).toString());
				}
				int pageSize=bpmFormDialog.getPagesize();
				Object pageSizeObj=params.get(FormDialog.PageSize);
				if(pageSizeObj!=null){
					pageSize=Integer.parseInt(params.get(FormDialog.PageSize).toString());
				}
				String sql = SqlUtil.getSql(map, params);
				
				//调用项目中的实现，没有则跳过
				ICustomService customService=(ICustomService)AppUtil.getProjectBean(ICustomService.class);
				if(BeanUtils.isNotEmpty(customService)){
					
					String customSql = customService.getCustomFormDialogSql(jdbcHelper,bpmFormDialog,formTable, params, map,sql);
					if(BeanUtils.isNotEmpty(customSql)){
						sql = customSql;
					}
				}
				
				logger.debug("bpmFormDialog query sql: " + sql);
				PagingBean pageBean=new PagingBean(currentPage,pageSize);

				List<Map<String, Object>> list= jdbcHelper.getPage(currentPage, pageSize, sql, params,pageBean);
				//对附件表中的一些字段做特殊处理
				if(objName.equals(ISysFile.FILE_SECURITY_TABLE)){
					for(int i=0;i<list.size();i++){
						Map<String, Object> fileObject = list.get(i);
						if(fileObject.containsKey(ISysFile.FILE_SECURITY_FIELD)){//密级管理字段
							Map<String, String> securityMap = ISysFile.SECURITY_CHINESE_MAP;
							fileObject.put(ISysFile.FILE_SECURITY_FIELD, securityMap.get(fileObject.get(ISysFile.FILE_SECURITY_FIELD)));
						}
						if(fileObject.containsKey(ISysFile.FILE_FILING_FIELD)){//是否归档字段
							Map<Long, String> securityMap = ISysFile.FILING_STATUS;
							fileObject.put(ISysFile.FILE_FILING_FIELD, securityMap.get(fileObject.get(ISysFile.FILE_FILING_FIELD)));
						}
						if(fileObject.containsKey(ISysFile.FILE_STOREWAY_FIELD)){//文件存储方式字段
							Map<Long, String> securityMap = ISysFile.STORE_WAY;
							fileObject.put(ISysFile.FILE_STOREWAY_FIELD, securityMap.get(fileObject.get(ISysFile.FILE_STOREWAY_FIELD)));
						}
					}
				}
				//对人员表中的密级管理字段做特殊处理
				if(objName.equals(ISysUser.USER_SECURITY_TABLE)){
					for(int i=0;i<list.size();i++){
						Map<String, Object> fileObject = list.get(i);
						if(fileObject.containsKey(ISysUser.USER_SECURITY_FIELD)){
							Map<String, String> securityMap = ISysUser.SECURITY_USER_MAP;
							if(fileObject.get(ISysUser.USER_SECURITY_FIELD)==null){
								fileObject.put(ISysUser.USER_SECURITY_FIELD, securityMap.get(ISysUser.SECURITY_FEIMI));
							}else{
								fileObject.put(ISysUser.USER_SECURITY_FIELD, securityMap.get(fileObject.get(ISysUser.USER_SECURITY_FIELD)));
							}
						}
					}
				}
				//list格式整理，当formTable不为空时(即为用户自定义表时)
				if(!BeanUtils.isEmpty(formTable)){
					Map<String, Object> formatData = dataTemplateService.getFormatDataMap(formTable);
					list = dataTemplateService.getDataListForDialog(list, formTable, formatData, true);
				}
				bpmFormDialog.setList(list);
				bpmFormDialog.setPagingBean(pageBean);
			}
			else{
				String sql = SqlUtil.getSql(map, params);
				
				//调用项目中的实现，没有则跳过
				ICustomService customService=(ICustomService)AppUtil.getProjectBean(ICustomService.class);
				if(BeanUtils.isNotEmpty(customService)){
					
					String customSql = customService.getCustomFormDialogSql(jdbcHelper,bpmFormDialog,formTable, params, map,sql);
					if(BeanUtils.isNotEmpty(customSql)){
						sql = customSql;
					}
				}
				List<Map<String, Object>> list=jdbcHelper.queryForList(sql, params);
				//list格式整理，当formTable不为空时(即为用户自定义表时)
				if(!BeanUtils.isEmpty(formTable)){
					Map<String, Object> formatData = dataTemplateService.getFormatDataMap(formTable);
					list = dataTemplateService.getOrderDataListForDialog(list, formTable, formatData, true);
				}
				bpmFormDialog.setList(list);
			}
		}

		return bpmFormDialog;
	}
	
	/**
	 * 获取外键显示值数据
	 * @param alias		对话框别名。
	 * @param params	参数集合。
	 * @return
	 * @throws Exception 
	 */
	@SuppressWarnings("unchecked")
	public Map<String, Object> getFKColumnShowData(String alias,Map<String, Object> dataMap) throws Exception{
		FormDialog bpmFormDialog= dao.getByAlias(alias);
		if(bpmFormDialog == null){
			//没有查找到关联关系列配置的 自定义对话框
			return null;
		}
		JdbcHelper<?,?> jdbcHelper= JdbcHelperUtil.getJdbcHelper(bpmFormDialog.getDsalias());
		FormTable formTable = null;
		String objName = bpmFormDialog.getObjname();
		if(objName.indexOf("w_")>=0){
			formTable = formTableService.getByTableName(objName.replace("w_", ""), FormTable.IS_MAIN);
		}
		String  objectName=bpmFormDialog.getObjname();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("objectName", objectName);
		//TODO 从params中移除非字段参数。

		//是否是列表
		String sql = SqlUtil.getFKColumnShowDataSql(map, dataMap);
		List<Map<String, Object>> list=jdbcHelper.queryForList(sql, dataMap);
		//list格式整理，当formTable不为空时
		if(!BeanUtils.isEmpty(formTable)){
			Map<String, Object> formatData = dataTemplateService.getFormatDataMap(formTable);
			list = dataTemplateService.getDataListForDialog(list, formTable, formatData, true);
		}
		if(list!=null && list.size()>0){
			return list.get(0);
		}else{
			return null;
		}
	}

	/**
	 *  {"id":"id","pid":"fatherId","displayName":"name"}
	 * @param displayField
	 * @param objName
	 * @return
	 */
	private String getTreeSql(FormDialog bpmFormDialog){

		String objName=bpmFormDialog.getObjname();
		List<DialogField> conditionList=bpmFormDialog.getConditionList();
		Map<String,Object> params=new HashMap<String, Object>();
		//获取条件的SQL语句
		String sqlWhere=SqlUtil.getWhere(conditionList, params);

		String sqlSelect = getSelectSQl(bpmFormDialog);
		String sql="SELECT "+sqlSelect +" FROM " + objName + sqlWhere;

		return sql;
	}



	/**
	 * 从DisplayField和ReturnField中取得Select字段，用于拼接SQL语句
	 * @param bpmFormDialog
	 * @return
	 */
	private String getSelectSQl(FormDialog bpmFormDialog) {
		String displayField=bpmFormDialog.getDisplayfield();
		JSONObject jsonObj=JSONObject.fromObject(displayField);
		String id=jsonObj.getString("id");
		String pid=jsonObj.getString("pid");
		String displayName=jsonObj.getString("displayName");
		List<DialogField> returnFields=bpmFormDialog.getReturnList();
		String sqlSelect= id +","+ pid +"," +displayName;
		for(DialogField field:returnFields){
			String name = field.getFieldName();
			if(name.equalsIgnoreCase(id)||name.equalsIgnoreCase(pid)||name.equalsIgnoreCase(displayName)){
				continue;
			}
			sqlSelect+=","+name;
		}
		return sqlSelect;
	}

	public String exportXml(List<FormDialog> bpmFormDialogs)throws Exception
	{
		FormDialogXmlList bpmFormDialogXmlList = new FormDialogXmlList();
		List list = new ArrayList();
		for (FormDialog bpmFormDialog : bpmFormDialogs)
		{
			FormDialogXml bpmFormDialogXml = exportBpmFormDialogXml(bpmFormDialog);
			list.add(bpmFormDialogXml);
		}
		bpmFormDialogXmlList.setBpmFormDialogXmlList(list);
		return XmlBeanUtil.marshall(bpmFormDialogXmlList, FormDialogXmlList.class);
	}

	public String exportXml(Long[] tableIds)
			throws Exception
	{
		FormDialogXmlList bpmFormDialogXmlList = new FormDialogXmlList();
		List list = new ArrayList();
		for (int i = 0; i < tableIds.length; i++) {
			FormDialog bpmFormDialog = (FormDialog)this.dao.getById(tableIds[i]);
			FormDialogXml bpmFormDialogXml = exportBpmFormDialogXml(bpmFormDialog);
			list.add(bpmFormDialogXml);
		}
		bpmFormDialogXmlList.setBpmFormDialogXmlList(list);
		return XmlBeanUtil.marshall(bpmFormDialogXmlList, FormDialogXmlList.class);
	}


	public void importXml(InputStream inputStream)
			throws Exception
	{
		Document doc = Dom4jUtil.loadXml(inputStream);
		Element root = doc.getRootElement();

		XmlUtil.checkXmlFormat(root, "bpm", "formDialogs");

		String xmlStr = root.asXML();
		FormDialogXmlList bpmFormDialogXmlList = (FormDialogXmlList)
				XmlBeanUtil.unmarshall(xmlStr, FormDialogXmlList.class);

		List<FormDialogXml> list = bpmFormDialogXmlList
				.getBpmFormDialogXmlList();

		for (FormDialogXml bpmFormDialogXml : list)
		{
			importBpmFormDialogXml(bpmFormDialogXml);
		}
	}

	public FormDialogXml exportBpmFormDialogXml(FormDialog bpmFormDialog)
			throws Exception
	{
		FormDialogXml bpmFormDialogXml = new FormDialogXml();
		Long id = bpmFormDialog.getId();
		if (BeanUtils.isNotEmpty(id))
		{
			bpmFormDialogXml.setBpmFormDialog(bpmFormDialog);
		}
		return bpmFormDialogXml;
	}

	private void importBpmFormDialogXml(FormDialogXml bpmFormDialogXml)
			throws Exception
	{
		Long dialogId = Long.valueOf(UniqueIdUtil.genId());
		FormDialog bpmFormDialog = bpmFormDialogXml.getBpmFormDialog();
		if (BeanUtils.isEmpty(bpmFormDialog)) {
			throw new Exception();
		}
		String alias = bpmFormDialog.getAlias();
		FormDialog dialog = this.dao.getByAlias(alias);
		if (BeanUtils.isNotEmpty(dialog)) {
			MsgUtil.addMsg(2, "别名为" + alias + 
					"的自定义对话框已存在，请检查你的xml文件！");
			return;
		}
		bpmFormDialog.setId(dialogId);
		this.dao.add(bpmFormDialog);
		MsgUtil.addMsg(1, "别名为" + alias + "的自定义对话框导入成功！");
	}
	
	
    
    /**
     * 根据dialog 别名获取 对话框数据源的表名以及表ID
     * @param alias
     */
    @Override
    public FormTable getFormDilaogTableInfo(String alias)
    {      
        FormDialog formDialog = this.getByAlias(alias);
        FormTable formTable=this.formTableService.getByTableName(formDialog.getObjname().replace(ITableModel.CUSTOMER_TABLE_PREFIX, ""));
        return formTable;
    }

}

