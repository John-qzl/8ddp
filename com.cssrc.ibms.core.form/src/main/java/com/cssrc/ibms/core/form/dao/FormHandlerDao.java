
package com.cssrc.ibms.core.form.dao;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.cssrc.ibms.api.activity.intf.IBusLinkService;
import com.cssrc.ibms.api.activity.intf.ISubtableRightsService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.ISubtableRights;
import com.cssrc.ibms.api.form.intf.IFiledEncryptService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.sync.intf.ISyncRelTableDataService;
import com.cssrc.ibms.api.system.intf.ISolrService;
import com.cssrc.ibms.api.system.intf.ISysDataSourceDefService;
import com.cssrc.ibms.api.system.model.ISysDataSourceDef;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.encrypt.FiledEncryptFactory;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.form.intf.ITableOperator;
import com.cssrc.ibms.core.form.model.BusBak;
import com.cssrc.ibms.core.form.model.CustomRecord;
import com.cssrc.ibms.core.form.model.DataTemplate;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.form.model.RelTable;
import com.cssrc.ibms.core.form.model.SqlModel;
import com.cssrc.ibms.core.form.model.SubTable;
import com.cssrc.ibms.core.form.service.BusBakService;
import com.cssrc.ibms.core.form.service.FormTableService;
import com.cssrc.ibms.core.form.util.FormDataUtil;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;


/**
 * 对象功能:自定义表单数据处理 
 * 开发人员:zhulongchao 
 */
@Repository
public class FormHandlerDao {

	private Log logger = LogFactory.getLog(FormHandlerDao.class);

	@Resource
	private FormTableDao formTableDao; 
	@Resource
	private FormTableService formTableService;
	@Resource
	private ISubtableRightsService subtableRightsService;
	@Resource
	private JdbcTemplate jdbcTemplate;
	@Resource
	private IBusLinkService busLinkService;
	@Resource
    private BusBakService busBakService;
	@Resource
	private ITableOperator tableOperator;
	@Resource
	private DataTemplateDao dataTemplateDao;
	@Resource
    private ISyncRelTableDataService syncRelTableDataService;
	@Resource
	private ISysDataSourceDefService sysDataSourceDefService;
	@Resource
	IFiledEncryptService filedEncryptService;
	@Resource
	ISolrService solrService;
	/**
	 * 处理动态表单数据
	 * @param parmas // 从dataTemplate中取设置sub表的的排序
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void  handFormData(Map<String,Object>params,FormData bpmFormData,IProcessRun processRun,String nodeId) throws Exception {
		JdbcTemplate jdbcTemplate =null;
		FormTable bpmFormTable=bpmFormData.getFormTable();
		//判断数据源
		if(bpmFormTable!=null){
			if(bpmFormTable.getIsExternal() == FormTable.EXTERNAL){
				if(bpmFormTable.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
					jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
				}else{//TODO 多数据源
					ISysDataSourceDef sysDataSourceDef=sysDataSourceDefService.getByAlias(bpmFormTable.getDsAlias());
					if(sysDataSourceDef==null){
						logger.error("datasource" + bpmFormTable.getDsAlias() + " is null, maybe has deleted...");
						return ;
					}
					jdbcTemplate = JdbcTemplateUtil.getNewJdbcTemplate(bpmFormTable.getDsAlias());
				}
			}else{
				jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}
		}
		String actDefId="";
		if(processRun!=null){
			actDefId=processRun.getActDefId();
		}
		//传入formData对象，解析成sqlmodel列表数据。(包含主表、sub子表、rel关系表)
		List<SqlModel> list = FormDataUtil.parseSql(params,bpmFormData,actDefId,nodeId);
		//获取需要备份的数据
	    List<BusBak> busBaks = FormDataUtil.parseBusBak(params,bpmFormData,actDefId,nodeId);
		
		for (SqlModel sqlModel : list) {
			String sql = sqlModel.getSql();
			if (StringUtil.isEmpty(sql)) continue;
			Object[] obs = sqlModel.getValues();
			jdbcTemplate.update(sql, obs);
		}
		//建立索引
		solrService.createSqlDataIndex(bpmFormData);
		//处理关联数据。
		handlerBusLinkData(list,processRun);
		//处理备份数据
	    handlerBusBakData(busBaks,processRun,params);
	    //发布mq消息订阅，用于数据同步
	    syncRelTableDataService.handlerSyncData(bpmFormData);
	}
	/**
	 * 处理关联数据。
	 * @param list
	 * @param processRun
	 */
	private void handlerBusLinkData(List<SqlModel> list,IProcessRun processRun){
		for (SqlModel sqlModel : list) {
			String sql = sqlModel.getSql();
			if (StringUtil.isEmpty(sql) ) continue; 
			switch(sqlModel.getSqlType()){
				case SqlModel.SQLTYPE_INSERT:
					busLinkService.add(sqlModel.getPk(),processRun,sqlModel.getFormTable() );
					break;
				case SqlModel.SQLTYPE_UPDATE:
					busLinkService.updBusLink(sqlModel.getPk(), sqlModel.getFormTable());
					break;
				case SqlModel.SQLTYPE_DEL:
					busLinkService.delBusLink(sqlModel.getPk(), sqlModel.getFormTable());
					break;
			}
		}
	}


    /**
     * handlerBusBakData
     * @param list
     * @param processRun
     */
    private void handlerBusBakData(List<BusBak> busBaks,IProcessRun processRun,Map<String,Object> params){
        Object __bakData__=params.get("__bakData__");
        DataTemplate dataTemplate=params==null?null:(DataTemplate)params.get("dataTemplate");
        short isBackData=0;
        if(__bakData__!=null&&dataTemplate!=null){
            try{
                isBackData=Short.valueOf(__bakData__.toString());
            }catch(Exception e){
                logger.warn("__bakData__ is error");
            }
            dataTemplate.setIsBakData(isBackData);
        }
        if(dataTemplate!=null&&dataTemplate.getIsBakData()!=1){
            return;
        }
        busBakService.setBakInfo();
        for (BusBak busBak : busBaks) {
            busBakService.addBusBak(busBak, processRun);
        }
    }
    
	/**
	 * 保存表单数据
	 * @param bpmFormData
	 * @throws Exception
	 */
	public void handFormData(Map<String,Object>params,FormData bpmFormData,IProcessRun processRun) throws Exception {
		handFormData(params, bpmFormData,processRun, "");
	}
	
	public void handFormData(Map<String,Object>params,FormData bpmFormData) throws Exception {
		handFormData(params, bpmFormData,null, "");
	}

	/**
	 * 判断指定的表是否有数据。
	 * 
	 * @param tableName
	 *            数据库表名
	 */
	public boolean getHasData(String tableName) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		int rtn = jdbcTemplate.queryForInt("select count(*) from " + tableName);
		return rtn > 0;
	}
	
	/**
	 * 检查表是否存在，可以是该用户空间下的所有表
	 * @Methodname: tableExists
	 * @param tableName
	 * @return
	 *
	 */
	public boolean tableExists(String tableName){
//		StringBuffer sql  = new StringBuffer();
//		sql.append("select COUNT(1) from user_tables t where t.TABLE_NAME='").append(tableName.toUpperCase()).append("'");
//
//		return	jdbcTemplate.queryForInt(sql.toString())>0?true:false;
		return tableOperator.isTableExist(tableName);
	}

	/**
	 * 根据主键查询列表数据。
	 * 
	 * @param tableId
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	public FormData getByKey(Map<String,Object> parmas,long tableId, String pkValue,boolean isHandleData) throws Exception {
		return getByKey(parmas,tableId, pkValue, null, null,isHandleData);
	}
	
	/**
	 * 根据流程定义id，节点id和表ID获取子表显示条件的SQL片段。
	 * @param tableId
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	private String getLimitSql(Long tableId, String actDefId,String nodeId){
		if(StringUtil.isEmpty(actDefId) || StringUtil.isEmpty(nodeId)) return "";
		String limitSql = "";
		//根据流程ID和节点ID获取子表权限配置
		ISubtableRights rule = subtableRightsService.getByDefIdAndNodeId(actDefId, nodeId, tableId);
		
		if(rule==null) return "";
		int permissionType=rule.getPermissiontype().intValue();
		// 处理权限,生成sql约束片段.
		switch (permissionType) {
			case 0:// 简单配置:判断用户
				//Long userId = ContextUtil.getCurrentUserId();
				//limitSql = " and b.BUS_CREATOR_ID   = " + userId;
				break;
			case 1:// 简单配置:判断组织
				//ISysOrg org = ContextUtil.getCurrentOrg();org
				ISysOrg org=null;
				if (org == null) {
					limitSql = " and 1 = 2";// 强制查询不到
				} else {
					limitSql = " and b.BUS_ORG_ID = " + org.getOrgId();
				}
				break;
			case 2:// 脚本
				GroovyScriptEngine scriptEngine = (GroovyScriptEngine) AppUtil.getBean(GroovyScriptEngine.class);
				limitSql = scriptEngine.executeString(rule.getPermissionseting(), new HashMap<String, Object>());// 不需流程变量入参
				break;
			default:
				break;
		}
		return limitSql;
		
	}

	/**
	 * 根据表ID，当前流程定义ID,流程节点ID查询业务数据。
	 * @param tableId		表ID
	 * @param pkValue		主键
	 * @param actDefId		流程定义ID
	 * @param nodeId		节点ID
	 * @return
	 * @throws Exception
	 */
	public FormData getByKey(Map<String,Object> parmas,long tableId, String pkValue, String actDefId, String nodeId,boolean isHandleData) throws Exception {
		FormTable mainFormTableDef=formTableService.getByTableId(tableId,1);
		// 获取jdbctemplate对象。
		JdbcTemplate jdbcTemplate =JdbcTemplateUtil.getFormTableJdbcTemplate(mainFormTableDef);
		String tableName = mainFormTableDef.getFactTableName();
		String pkField = mainFormTableDef.getPkField();
	
		PkValue pk = new PkValue(pkField, pkValue);
		// 取得主表的数据
		Map<String, Object> mainData = getByKey(jdbcTemplate,  pk, mainFormTableDef,isHandleData);

		// 取子表的数据
		FormData bpmFormData = new FormData(mainFormTableDef);
		List<FormTable> tableList=mainFormTableDef.getSubTableList();
		for (FormTable table : tableList) {
			SubTable subTable = new SubTable();
			//TableRelation tableRelation= table.getTableRelation();			
			String fk=table.getRelation();
			String subPk =table.getPkField();		
			List<Map<String, Object>> list = getByFk(parmas,table, pk.getValue().toString(), actDefId,nodeId,isHandleData, fk);
			subTable.setTableName(table.getTableName().toLowerCase());
			subTable.setDataList(list);
			subTable.setFkName(fk);
			subTable.setPkName(subPk);
			bpmFormData.addSubTable(subTable);
		}
		// 取外键rel表的数据
		//TODO 先判断是否要取外键rel表数据
		
		//取出关联rel表数据E
		List<FormTable> relTableList=mainFormTableDef.getRelTableList();
		for (FormTable relFormTable : relTableList) {
			//根据主表和关联表构建RelTable
			RelTable relTable = FormDataUtil.getRelTableByMainTableAndRelTable(mainFormTableDef, relFormTable);
			//获取数据库中的外键列名，含F_
			String fkName = relTable.getMainTableFkName();
			//根据关系表rel的外键查询列表数据
			List<Map<String, Object>> relTableDataList = getDataByFk(parmas,relFormTable, pkValue.toString(),true, fkName);
			relTable.setRelTableDataList(relTableDataList);
			
			bpmFormData.addRelTable(relTable);
		}
		
		bpmFormData.setTableId(tableId);
		bpmFormData.setTableName(tableName);
		bpmFormData.setPkValue(pk);
		bpmFormData.addMainFields(mainData);

		return bpmFormData;
	}

	
	/**
	 * 根据主键查询一条数据。
	 * 
	 * @param tableName
	 *            需要查询的表名
	 * @param pk
	 *            主键
	 * @param tableId
	 *            表ID
	 * @return
	 */
	public Map<String, Object> getByKey(JdbcTemplate jdbcTemplate,  PkValue pk, FormTable bpmFormTable ,boolean isHandleData) {
		String sql = "select a.* from " + bpmFormTable.getFactTableName() + " a where " + pk.getName() + "=" + pk.getValue();
		List<FormField> fieldList = bpmFormTable.getFieldList();
		//外键显示列构造。
		fieldList = FormDataUtil.dealFKShowColumn(fieldList);
		
		Map<String, FormField> fieldMap = convertToMap(fieldList);
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
			if(isHandleData){
				map = handMap(bpmFormTable, fieldMap, map);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex.getMessage());
			map = new HashMap<String, Object>();
		}
		return map;
	}

	/**
	 * 根据表名和主键获取一行数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public Map<String, Object> getByKey(String tableName, String pk) {
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		String sql = "select a.* from " + tableName + " a where id="  + pk;
		
		Map<String, Object> map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (Exception ex) {
			map = new HashMap<String, Object>();
		}
		return map;
	}
	/**
	 * 
	 * @param bpmFormTable
	 * @param pk
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	public Map<String, Object> getByKey(FormTable bpmFormTable, String pk, String[] fields)
	throws Exception{
		String field = " * ";
		if (BeanUtils.isNotEmpty(fields)) {
			field = StringUtils.join(fields, ",");
		}
		String tableName = bpmFormTable.getFactTableName();
		String pkName = bpmFormTable.getPkField();
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean(bpmFormTable.getDsAlias());
		String sql = "select " + field + " from " + tableName + "  where " + pkName + "=" + pk;

		Map map = null;
		try {
			map = jdbcTemplate.queryForMap(sql);
		} catch (Exception ex) {
			map = new HashMap();
		}
		return map;
	}

	/**
	 * 将list转换为map对象。
	 * 
	 * @param fieldList
	 * @return
	 */
	private Map<String, FormField> convertToMap(List<FormField> fieldList) {
		Map<String, FormField> map = new HashMap<String, FormField>();
		for (FormField field : fieldList) {
			String fieldName = field.getFieldName().toLowerCase();
			map.put(fieldName, field);
		}
		return map;
	}

	/**
	 * 根据外键查询列表数据。
	 * @param parmas // 从dataTemplate中取设置sub表的排序
	 * @param tableName
	 * @param fkValue
	 * @param tableId
	 * @return
	 */
	public List<Map<String, Object>> getByFk(Map<String,Object> parmas,FormTable table, String fkValue,boolean isHandleData) {
		return getByFk(parmas,table, fkValue, null, null,isHandleData, table.getRelation());
	}

	/**
	 * 根据外键查询列表数据。
	 * @param table //sub表对象
	 * @param fkValue //主表main的主键对象
	 * @param actDefId
	 * @param nodeId
	 * * @param isHandleData 判断是否要进行数据转换
	 *   @param fkField外键列名
	 * @return
	 */
	public List<Map<String, Object>> getByFk(Map<String,Object> parmas,FormTable table, String fkValue, String actDefId,String nodeId,boolean isHandleData, String fkField) {
		String subTableName = table.getFactTableName();
		//String fkField=table.getRelation();
		String pkField=table.getPkField();
		short keyDataType= table.getKeyDataType();
		List<FormField> fieldList=table.getFieldList();
		
		Long tableId=table.getTableId();
		JdbcTemplate jdbcTemplate=null;
		if(table.isExtTable()){
			if(table.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
				jdbcTemplate=(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}/*else{
				DriverManagerDataSource dataSource=sysDataSourceDao.getDriverMangerDataSourceByAlias(table.getDsAlias());
				jdbcTemplate=new JdbcTemplate(dataSource);
			}*/
		}else{
			jdbcTemplate= (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		}
		StringBuffer sql=new StringBuffer("");
		//数字类型
		if(keyDataType==0){//BUS_PK IS '对应关联表主键';
			sql.append("select a.* from ").append(subTableName).append(" a,ibms_bus_link b where  a.").append(pkField+"=b.bus_pk and a." ).append( fkField + "=" + fkValue);
		}
		else{//BUS_PKSTR IS '对应关联表主键(字符串)';
			sql.append( "select a.* from ").append(subTableName ).append( " a,ibms_bus_link b where  a.").append(pkField+"=b.bus_pkstr and  a.").append(fkField + "=" + fkValue);
		}
		 
		//根据流程定义id，节点id和表ID获取子表显示条件的SQL片段。
		String limitSql=getLimitSql(tableId, actDefId, nodeId);
		
		// 处理子表权限.2013-1-17,by wwz.
		if (!StringUtil.isEmpty(limitSql)) {
			sql .append(" " + limitSql);
		}
		DataTemplate dataTemplate=parmas==null?null:(DataTemplate)parmas.get("dataTemplate");
		// 取设置的排序
		if (dataTemplate!=null&&StringUtils.isNotEmpty(dataTemplate.getSubSortField())) {
			String subSortField=dataTemplate.getSubSortField();
			String sortSql = this.getSortSQL(subSortField, table.getTableId(),dataTemplate.getSource());
			if (StringUtils.isNotEmpty(sortSql))
				sql.append(" ORDER BY ").append(sortSql);
		}else{
			sql.append(" order by b.BUS_ID ASC");
		}
		//将列list转换为map对象。
		Map<String, FormField> fieldMap = convertToMap(fieldList);
		//查询数据
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql.toString());
		//判断是否要进行数据转换
		if(!isHandleData){
			return list;
			}
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : list) {
			//处理返回的map数据 将key转换成小写。 如果是本地数据库，则替换字段前缀。 如果数据是日期类型，则转换数据格式。
			Map<String, Object> obj = handMap(table,fieldMap, map);
			rtnList.add(obj);
		}
		return rtnList;
	}

	/**
	 * 获取排序的SQL
	 * 
	 * @param sortField
	 * @param source
	 * @return
	 */
	private String getSortSQL(String sortField, Long tabid,String source) {
		StringBuffer sb = new StringBuffer();
		JSONArray jsons = JSONArray.fromObject(sortField);
		for(Object json:jsons){
			JSONObject jsonObj = (JSONObject) json;
			if(jsonObj.get(tabid.toString())!=null){
				JSONArray jsonArray=JSONArray.fromObject(jsonObj.get(tabid.toString()));
				for (int i = 0; i < jsonArray.size(); i++) {
					JSONObject obj = (JSONObject) jsonArray.get(i);
					String name = (String) obj.get("name");
					String sort = (String) obj.get("sort");
					sb.append(this.fixFieldName(name, source, "")).append(" " + sort)
							.append(",");
				}
				if (sb.length() > 0)
					return sb.substring(0, sb.length() - 1);
				return sb.toString();
			}
		}
		return null;
		
	}

	/**
	 * 修正字段名
	 * 
	 * @param fieldName
	 *            字段名
	 * @param source
	 *            数据来源 1.表示自定义表（需要加F_修正）
	 * @param prefix
	 *            前缀修正
	 * @return
	 */
	private String fixFieldName(String fieldName, String source, String prefix) {
		if (StringUtils.isEmpty(fieldName) || StringUtils.isEmpty(source))
			return fieldName;
		if (DataTemplate.SOURCE_CUSTOM_TABLE.equals(source))
			fieldName = TableModel.CUSTOMER_COLUMN_PREFIX + fieldName;
		if (StringUtils.isNotEmpty(prefix))
			fieldName = prefix.toLowerCase() + "." + fieldName;
		return fieldName;
	}
	/**
	 * 处理返回的map数据 将key转换成小写。 如果是本地数据库，则替换字段前缀。 如果数据是日期类型，则转换数据格式。
	 * 
	 * @param isExternal
	 * @param fieldMap
	 * @param map
	 */
	private Map<String, Object> handMap(FormTable table,Map<String, FormField> fieldMap, Map<String, Object> map) {
		String pkField=table.getPkField();
		Map<String, Object> rtnMap = new HashMap<String, Object>();
		for (Map.Entry<String, Object> entry : map.entrySet()) {
			String fieldName = entry.getKey().toLowerCase();
		
			String key = fieldName;
			if(!table.isExtTable() && fieldName.indexOf(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase()) == 0){
				key = fieldName.replaceFirst(TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(), "");
			}
			Object obj = entry.getValue();
			if (obj == null) {
				rtnMap.put(key, "");
				continue;
			}
			if(pkField.equalsIgnoreCase(key)){
				rtnMap.put(key, obj);
				continue;
			}
			FormField formField = fieldMap.get(key);
			
			if(formField==null) continue;
			String dicType = formField.getDictType();
			// 对数据字典单独处理。
			if(StringUtil.isNotEmpty(dicType)){
				obj = FormDataUtil.getDicNameByValue(obj,dicType);
				rtnMap.put(key, obj);
			}
			// 对时间字段单独处理。
			else if (obj instanceof Date) {
				
				String format = "yyyy-MM-dd";
				if(formField!=null){
					String confFormat = formField.getPropertyMap().get("format");
					if (StringUtil.isNotEmpty(confFormat)) {
						format=confFormat;
					}
				}
				String str = DateUtil.getDateString((Date)obj,format);
				rtnMap.put(key, str);
			} 
			else if(obj instanceof Number){
				
				Map<String,String> paraMap=formField.getPropertyMap();
				Object isShowComdify= paraMap.get("isShowComdify");
				Object decimalValue= paraMap.get("decimalValue");
				Object coinValue= paraMap.get("coinValue");
				
				String str =StringUtil.getNumber(obj,isShowComdify,decimalValue,coinValue);
				rtnMap.put(key, str);
			}
			else {
				//附件替换
				if(BeanUtils.isNotEmpty(formField) && BeanUtils.isNotEmpty(formField.getControlType()) && formField.getControlType().shortValue() ==IFieldPool.ATTACHEMENT ){
					obj = ((String) obj).replace("\"", "￥@@￥");
				}	
				//字段解密
				if(StringUtil.isNotEmpty(formField.getEncrypt())){
		            FiledEncryptFactory filedEncryptFactory=AppUtil.getBean(FiledEncryptFactory.class);
		            IFiledEncrypt filedEncrypt=filedEncryptFactory.creatEncrypt(formField.getEncrypt());
		            if(filedEncrypt!=null){
		                obj=filedEncrypt.decrypt(obj);
		            }
				    
				}
				rtnMap.put(key, obj);
			}
		}
		return rtnMap;
	}
	
	/**
	 * 查找
	 * 
	 * @param tableName
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public List<Map<String, Object>> getAll(Long tableId, Map<String, Object> param) throws Exception {
		FormTable table = formTableDao.getById(tableId);
		JdbcTemplate jdbcTemplate = (JdbcTemplate) AppUtil.getBean("jdbcTemplate");

		// 字段名与数据
		StringBuffer where = new StringBuffer();
		List<Object> values = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : param.entrySet()) {
			where.append(entry.getKey()).append(" LIKE ? AND ");
			values.add(entry.getValue());
		}

		String tableName = table.getFactTableName();
		// sql
		StringBuffer sql = new StringBuffer();
		sql.append(" SELECT a.* FROM ");
		sql.append(tableName + " a");
		if (where.length() > 0) {
			sql.append(" WHERE ");
			sql.append(where.substring(0, where.length() - 5));
		}

		RowMapper<Map<String, Object>> rowMapper = new RowMapper<Map<String, Object>>() {

			@Override
			public Map<String, Object> mapRow(ResultSet rs, int num) throws SQLException {
				Map<String, Object> map = new HashMap<String, Object>();
				ResultSetMetaData rsm = rs.getMetaData();
				for (int i = 1; i <= rsm.getColumnCount(); i++) {
					String name = rsm.getColumnName(i);
					Object value = rs.getObject(name);
					map.put(name, value);
				}
				return map;
			}
		};

		// 执行
		return jdbcTemplate.query(sql.toString(), values.toArray(), rowMapper);

	}

	/**
	 * 根据表名和主键是否有数据。
	 * @param tableName		表名
	 * @param pk			主键
	 * @return
	 */
	public boolean isHasDataByPk(String tableName, Long pk ){
		String sql = "select count(1) from " + tableName + " a where id=?"  ;
//		int  rtn=jdbcTemplate.queryForInt(sql, pk);
//		return rtn>0;
//		int  rtn=jdbcTemplate.queryForInt(sql, pk);
		return true;
	}
	
	/**
	 * 根据表名和主键值是否有数据。
	 * @param tableName		表名
	 * @param pk			主键值
	 * @return
	 */
	public boolean isHasDataByPk(String tableName, String pk ){
		String sql = "select count(1) from " + tableName + " a where id=?"  ;
		int  rtn=jdbcTemplate.queryForInt(sql, pk);
		if(rtn>0) return true;
		return false;
	}
	
	/**
	 * 根据主键获取子表的数据。
	 * @param tableName
	 * @param pk
	 * @return
	 */
	public List<Map<String,Object>> getByFk(String tableName,Long fk){
		String sql = "select * from " + tableName + " a where "+TableModel.FK_COLUMN_NAME+"=?"  ;
		List<Map<String,Object>> list= jdbcTemplate.queryForList(sql, fk);
		return list;
	}
	
	/**
	 * 根据Fk 查询列表数据。
	 * @param table  关联的rel表对象
	 * @param fkValue   主表main的主键对象
	 * @param actDefId
	 * @param nodeId
	 * * @param isHandleData 判断是否要进行数据转换
	 *  @param fkField 外键列名
	 * @return
	 */
	public List<Map<String, Object>> getDataByFk(Map<String,Object> parmas,FormTable formTable, Object fieldValue,boolean isHandleData, String fieldName) {
		List<FormField> fieldList=formTable.getFieldList();
		if(formTable.isExtTable()){
			if(formTable.getDsAlias().equals(BpmConst.LOCAL_DATASOURCE)){
				jdbcTemplate=(JdbcTemplate) AppUtil.getBean("jdbcTemplate");
			}/*else{
				DriverManagerDataSource dataSource=sysDataSourceDao.getDriverMangerDataSourceByAlias(table.getDsAlias());
				jdbcTemplate=new JdbcTemplate(dataSource);
			}*/
		}else{
			jdbcTemplate= (JdbcTemplate) AppUtil.getBean("jdbcTemplate");
		}
		String pkName=formTable.isExtTable()?formTable.getPkField() :TableModel.PK_COLUMN_NAME;
		
		DataTemplate dataTemplate=parmas==null?null:(DataTemplate)parmas.get("dataTemplate");
		
		//查询数据
		List<Map<String,Object>>   list = this.getRelDataByFk(dataTemplate,formTable,fieldName,fieldValue,pkName);
		//将列list转换为map对象。
		Map<String, FormField> fieldMap = convertToMap(fieldList);
		//判断是否要进行数据转换
		if(!isHandleData){
			return list;
		   }
		List<Map<String, Object>> rtnList = new ArrayList<Map<String, Object>>();

		for (Map<String, Object> map : list) {
			//处理返回的map数据 将key转换成小写。 如果是本地数据库，则替换字段前缀。 如果数据是日期类型，则转换数据格式。
			Map<String, Object> obj = handMap(formTable,fieldMap, map);
			rtnList.add(obj);
		}
		return rtnList;
	}
	/**
	 * 根据主键获取关联表的数据。
	 * @param tableName
	 * @param fkFieldName 外键字段
	 * @param fkFieldValue外键值
	 * @param orderFieldName 排序字段
	 *   
	 * @return
	 */
	public List<Map<String,Object>> getRelDataByFk(DataTemplate dataTemplate,FormTable formTable,String fkFieldName,Object fkFieldValue,String orderFieldName){
		String tableName = formTable.getFactTableName();
		String sortSql = "";
		// 取设置的排序
		if (dataTemplate!=null&&StringUtils.isNotEmpty(dataTemplate.getRelSortField())) {
			String relSortField=dataTemplate.getRelSortField();
			sortSql = this.getSortSQL(relSortField, formTable.getTableId(),dataTemplate.getSource());
		}
		//关联表的字段类型如果不一致必须加上 '' 关联查询
		String	sql = "select a.* from " + tableName + " a where a." + fkFieldName + "='" + fkFieldValue+"' ";
		if(StringUtils.isEmpty(sortSql)){
			sql+=" order by "+orderFieldName+" ASC ";
		}else{
			sql+=" order by "+sortSql;
		}
		//查询数据
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 * 根据主键获取关联表的数据。
	 * @param tableName
	 * @param fkFieldName 外键字段
	 * @param fkFieldValue外键值
	 * @param orderFieldName 排序字段
	 *   
	 * @return
	 */
	public List<Map<String,Object>> getRelDataByFk(String relTableName,String fkFieldName,Object fkFieldValue,String orderFieldName){
		//关联表的字段类型如果不一致必须加上 '' 关联查询
		String	sql = "select a.* from " + relTableName + " a where a." + fkFieldName + "='" + fkFieldValue+"' ";
		if(StringUtils.isNotEmpty(orderFieldName)){
			sql+=" order by "+orderFieldName+" ASC ";
		}			
		//查询数据
		List<Map<String, Object>> list = jdbcTemplate.queryForList(sql);
		return list;
	}
	/**
	 * @param map：要更新的key-value
	 * @param tablename:表名
	 * @param fkFieldName 外键字段
	 * @param fkFieldValue外键值
	 */
	public void updateDataByPk(Map map,String tableName,String fkFieldName,Object fkFieldValue){
		StringBuffer sql = new StringBuffer();
		sql.append(" update ").append(tableName);
		sql.append(" set ");
		Iterator it = map.keySet().iterator();
		while(it.hasNext()){
			String key = (String)it.next();
			Object value = map.get(key);
			sql.append(key).append("='").append(value).append("',");
		}
		sql.replace(sql.length()-1, sql.length(), "");
		sql.append(" where ");
		sql.append(fkFieldName).append("='").append(fkFieldValue).append("'");
		jdbcTemplate.update(sql.toString());
	}
	/**
	 * 更新用户自定义表数据
	 * @param list
	 */
	public void updateCustomRecord(List<CustomRecord> list) {
		List<String> sqls = new ArrayList<String>();
		for(CustomRecord record : list) {
			sqls.add(record.getUpdateSql());
		}
		jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
	}
	/**
	 * 添加用户自定义表数据
	 * @param list
	 */
	public void addCustomRecord(List<CustomRecord> list) {
		List<String> sqls = new ArrayList<String>();
		for(CustomRecord record : list) {
			sqls.add(record.getAddSql());
		}
		jdbcTemplate.batchUpdate(sqls.toArray(new String[]{}));
	}
}
