package com.cssrc.ibms.core.form.service;

import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.CodeUtil;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.BaseFormTableXml;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IFormTableXml;
import com.cssrc.ibms.api.migration.model.IOutField;
import com.cssrc.ibms.api.migration.model.IOutTable;
import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.intf.ISerialNumberService;
import com.cssrc.ibms.api.system.model.BaseSerialNumber;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.constant.SqlTypeConst;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.form.dao.FormDefDao;
import com.cssrc.ibms.core.form.dao.FormFieldDao;
import com.cssrc.ibms.core.form.dao.FormHandlerDao;
import com.cssrc.ibms.core.form.dao.FormTableDao;
import com.cssrc.ibms.core.form.intf.ITableOperator;
import com.cssrc.ibms.core.form.model.FileAttachRights;
import com.cssrc.ibms.core.form.model.FormDef;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.util.FormUtil;
import com.cssrc.ibms.core.form.util.ParseReult;
import com.cssrc.ibms.core.form.xml.table.FormTableXml;
import com.cssrc.ibms.core.form.xml.table.FormTableXmlList;
import com.cssrc.ibms.core.table.BaseTableMeta;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.table.impl.TableMetaFactory;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.XmlUtil;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
/**
 * <pre>
 * 对象功能:自定义表 Service类  
 * 开发人员:zhulongchao  
 * </pre>
 * 
 */

@Service
public class FormTableService extends BaseService<FormTable> implements IFormTableService{
	@Resource
	private FormTableDao dao;
	@Resource
	private FormFieldDao formFieldDao;
	@Resource
	private ITableOperator tableOperator; 
	@Resource
	private FormDefDao formDefDao;
	@Resource
	private FormHandlerDao formHandlerDao;
	@Resource
	private ISerialNumberService serialNumberService; 
	@Resource
	FormHandlerService formHandlerService;
	@Resource
	FreemarkEngine freemarkEngine;
	
	protected IEntityDao<FormTable, Long> getEntityDao() {
		return this.dao;
	}

	/**
	 * 添加外部表。
	 * 
	 * @param table
	 * @param fields
	 * @throws Exception
	 */
	public void addExt(FormTable table) throws Exception {
		List<FormField> fields = table.getFieldList();
		long tableId = UniqueIdUtil.genId();
		// 添加表
		table.setTableId(tableId);
		table.setIsPublished((short) 1);
		table.setIsExternal(1);
		// 设置注释
		if (StringUtil.isEmpty(table.getTableDesc())) {
			table.setTableDesc(table.getTableName());
		}
		// 获取数据源名称。
		String dsAlias = table.getDsAlias();
		if (dsAlias.equals(BpmConst.LOCAL_DATASOURCE)) {
			table.setDsName("本地数据库");
		} /*else {
			SysDataSource sysDataSource = sysDataSourceDao.getByAlias(dsAlias);
			table.setDsName(sysDataSource.getName());
		}*/
		// 设定主键字段的数据类型。
		setKeyDataType(table);
		dao.add(table);
		// 添加字段。
		addFields(tableId, fields, true);
	}

	/**
	 * 设定主键字段类型那个。
	 * 
	 * @param table
	 */
	private void setKeyDataType(FormTable table) {
		String pkField = table.getPkField();
		Short pkDataType = FormTable.PKTYPE_NUMBER;
		List<FormField> fields = table.getFieldList();
		for (FormField field : fields) {
			if (field.getFieldName().equalsIgnoreCase(pkField)) {
				if (field.getFieldType().contains(FormField.DATATYPE_NUMBER)) {
					pkDataType = FormTable.PKTYPE_NUMBER;
				} else {
					pkDataType = FormTable.PKTYPE_STRING;
				}
			}
		}
		table.setKeyDataType(pkDataType);
	}
	
	/**
	 * 添加表数据定义。
	 * 
	 * @param table
	 * @param fields
	 * @return -1 表示用户表字段已经存在。
	 * @throws Exception
	 */
	public int addFormTable(FormTable table) throws Exception {
		List<FormField> fields = table.getFieldList();

		long tableId = UniqueIdUtil.genId();
		// 添加表
		table.setTableId(tableId);
		table.setDsAlias("LOCAL");
		table.setDsName("LOCAL");
		table.setIsExternal(0);
		// 设置注释
		if (StringUtil.isEmpty(table.getTableDesc())) {
			table.setTableDesc(table.getTableName());
		}
		ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();

		table.setCreateBy(sysUser.getUserId());
		table.setCreator(sysUser.getFullname());
		table.setCreatetime(new Date());
		//保存业务表到数据库
		dao.add(table);
		//添加字段。
		addFields(tableId, fields, false);

		return 0;

	}

	/**
	 * 计算字段，这种情况用于在表字段有变化时获取变化的字段，包括添加的字段和删除的字段。
	 * 
	 * @param fields //网页上传来的列
	 * @param orginFieldList //获取数据库中现有的列
	 * @param isDelete //是否要做删除标记。
	 * @return
	 */
	private Map<String, List<FormField>> caculateFields(
			List<FormField> fields, List<FormField> orginFieldList) {
		return caculateFields(fields, orginFieldList, true);
	}

	/**
	 * 计算字段，这种情况用于在表字段有变化时获取变化的字段，包括添加的字段和删除的字段。
	 * 
	 * <pre>
	 * 需要注意的是：
	 * 	这种情况在表创建之后才会用到。
	 *  更新时：
	 *  数据库中已有的字段，字段名，字段类型不能修改。
	 *  可以添加字段。
	 *  返回值类型：Map&lt;String, List&lt;FormField>>
	 *  
	 *  获取更新的字段：
	 *  Mapp&lt;String, Listp&lt;FormField>> paraTypeMap= caculateFields(fields,orginFieldList);
	 *  
	 *  新添加的列：
	 *  Listp&lt;FormField> addFields=paraTypeMap.get("add");
	 *  更新的列:
	 *  Listp&lt;FormField> updFields=paraTypeMap.get("upd");
	 *  
	 *  更新列时将原来的tableid和fieldid放到新列中。
	 * 
	 * </pre>
	 * 
	 * @param fields
	 * @param orginFieldList
	 * @param isDelete
	 * @return
	 */
	private Map<String, List<FormField>> caculateFields(
			List<FormField> fields, List<FormField> orginFieldList,
			boolean isDelete) {
		Map<String, FormField> orginMap = new HashMap<String, FormField>();
		Map<String, FormField> curMap = new HashMap<String, FormField>();

		Map<String, List<FormField>> resultMap = new HashMap<String, List<FormField>>();
		//获取数据库中现有的列
		for (FormField field : orginFieldList) {
			String fieldName = field.getFieldName().toLowerCase();
			orginMap.put(fieldName, field);
		}
		//网页上传来的列
		int i = 1;
		for (FormField field : fields) {
			String fieldName = field.getFieldName().toLowerCase();

			curMap.put(fieldName, field);
			field.setSn(i);
			i++;
		}
		//区分出新增的列和修改的列。
		for (FormField field : fields) {
			String fieldName = field.getFieldName().toLowerCase();
			if (orginMap.containsKey(fieldName)) {
				FormField orginField = orginMap.get(fieldName);
				field.setFieldId(orginField.getFieldId());
				field.setTableId(orginField.getTableId());
				addField("upd", resultMap, field);
			} else {
				addField("add", resultMap, field);
			}
		}
		// 如果现有的字段中不包含上次的字段则将字段做删除标记。
		int deleteNum = 0;
		if (isDelete) { // 是否要做标记删除字段的
			deleteNum = 1;
		}
		for (FormField field : orginFieldList) {
			String fieldName = field.getFieldName().toLowerCase();
			if (!curMap.containsKey(fieldName)) {
				field.setIsDeleted((short) deleteNum);
				addField("upd", resultMap, field);
			}
		}
		// 如果现有的字段中出现重复字段则将字段做删除标记。（fieldName重复）
		String fieldNames = ",";
		for (FormField field : orginFieldList) {
			String fieldName = field.getFieldName().toLowerCase();
			if (fieldNames.contains("," + fieldName + ",")) {
				field.setIsDeleted((short) 1);// 只删除formfield中的，不删除物理表的
				addField("repeat", resultMap, field);
			}
			fieldNames += field.getFieldName().toLowerCase() + ",";
		}
		return resultMap;
	}

	private void addField(String key,
			Map<String, List<FormField>> resultMap, FormField field) {
		List<FormField> list;
		if (resultMap.containsKey(key)) {
			list = resultMap.get(key);
		} else {
			list = new ArrayList<FormField>();
			resultMap.put(key, list);
		}
		list.add(field);
	}

	public void updExtTable(FormTable bpmFormTable) {

		List<FormField> fields = bpmFormTable.getFieldList();
		for (FormField field : fields) {
			formFieldDao.update(field);
		}
		// 更新表
		dao.update(bpmFormTable);

	}

	/**
	 * 更新表的设计。
	 * 
	 * @param table
	 * @param fields
	 * @return -1 表示当前字段中存在字段 curentUserId。-2 有数据字段不能设置为非空。
	 * @throws Exception
	 */
	public int upd(FormTable table, int generate) throws Exception {
	  try{
		List<FormField> fields = new ArrayList<FormField>();
		fields.addAll(table.getFieldList());

		Long tableId = table.getTableId();
		String tableName = table.getTableName().trim();
		// 获取表定义。
		FormTable originTable = dao.getById(tableId);

		Long mainTableId = tableId;
		int isMain = table.getIsMain();
		if (isMain == 0) {
			mainTableId = table.getMainTableId();
		}
		// 修改之前的字段列表
		List<FormField> originFieldList = formFieldDao
				.getFieldsByTableId(tableId);

		// 设置注释
		if (StringUtil.isEmpty(table.getTableDesc())) {
			table.setTableDesc(tableName);
		}
		//判断注释是否相同，不相同，则修改注释
		if(!originTable.getTableDesc().equals(table.getTableDesc())){
			tableOperator.updateTableComment(TableModel.CUSTOMER_TABLE_PREFIX + tableName, table.getTableDesc());
		}
		
		// 该表已经有表单定义了。
		boolean hasFormDef = false;
		if (tableId > 0) {
			hasFormDef = formDefDao.isTableHasFormDef(mainTableId);
		}

		// 该表已经有数据了。
		boolean hasData = false;
		if (StringUtil.isNotEmpty(tableName)) {
		    //不同jdk版本处理方式不一样，统一比较值，处理业务表修改字段会删除数据问题
			if (table.getIsPublished().intValue() == FormTable.IS_PUBLISH.shortValue()) {
				Map<String, String> map = new HashMap<String, String>();
				map.put("tableName", TableModel.CUSTOMER_TABLE_PREFIX
						+ tableName);
				hasData = formDefDao.isTableHasData(map);
			}
		}

		// 已经有表单定义，
		// 1.可以删除字段
		// 2.可以更新字段
		// 3.可以添加字段。
		if (hasFormDef || hasData) {
			if (BeanUtils.isNotEmpty(fields)) {
				fields = convertFields(fields, false);
			}

			Map<String, List<FormField>> resultMap = caculateFields(fields,
					originFieldList);

			// 处理新增的字段
			List<FormField> addList = resultMap.get("add")==null?new ArrayList():resultMap.get("add");
			// 更新表
			dao.update(table);
			// 需要更新的字段
			List<FormField> updList = resultMap.get("upd")==null?new ArrayList():resultMap.get("upd");
			// 需要删除的字段、但物理表不删除
			List<FormField> repList = resultMap.get("repeat")==null?new ArrayList():resultMap.get("repeat");				
			for (FormField field : updList) {
			    
			    if(hasData){
			        //如果表单已经有数据，字段加密不能修改。否则需要对旧数据全部重新加密。
			        field.setEncrypt(this.formFieldDao.getById(field.getFieldId()).getEncrypt());
			    }
			    //更新表字段之前先，获取对话框的表ID以及表name
	            this.setRelTableIdAndName(field);
				formFieldDao.update(field);
			}
			for (FormField field : repList) {

				if (hasData) {
					// 如果表单已经有数据，字段加密不能修改。否则需要对旧数据全部重新加密。
					field.setEncrypt(this.formFieldDao.getById(
							field.getFieldId()).getEncrypt());
				}
				// 更新表字段之前先，获取对话框的表ID以及表name
				this.setRelTableIdAndName(field);
				formFieldDao.update(field);
			}	
			// 没有增加的列就直接返回。
			// By weilei: 没有增加列继续执行。
			if(!BeanUtils.isEmpty(addList)){
				int i = updList.size();
				int k = 0;
				// 添加字段
				for (FormField field : addList) {
					k++;
					//更新表字段之前先，获取对话框的表ID以及表name
		            this.setRelTableIdAndName(field);
					field.setFieldId(UniqueIdUtil.genId());
					field.setTableId(tableId);
					field.setSn(k + i);
					formFieldDao.add(field);
					ColumnModel columnModel = getByField(field, 2);
					tableOperator.addColumn(TableModel.CUSTOMER_TABLE_PREFIX
							+ tableName, columnModel);
				}
			}
			
			//By weilei:删除字段
			for (FormField field : updList) {
				if(field.getIsDeleted() == 1 ){
					//删除业务表中无效字段
					formFieldDao.delById(field.getFieldId());
					//物理表删除字段
					ColumnModel columnModel = getByField(field, 2);
					tableOperator.dropColumn(TableModel.CUSTOMER_TABLE_PREFIX + tableName, columnModel);
				}
			}

		}
		// 没有表单定义的情况。
		else {
			// 已发布的情况
			if (table.getIsPublished() == 1) {
				// 主表的情况
				if (table.getIsMain() == 1) {
					// 获取所有的字表
					List<FormTable> tableList = dao
							.getSubTableByMainTableId(tableId);
					// 删除子表的外键
					for (FormTable subTable : tableList) {
						String tabName = TableModel.CUSTOMER_TABLE_PREFIX
								+ subTable.getTableName();
						//新增字表外键是"fk_"+ tabName,删除是要保持一致，不然报外键不存在 zxg
						//String shortTabName = subTable.getTableName();
						tableOperator.dropForeignKey(tabName, "fk_"
								+ tabName);
					}
					dao.update(table);
					// 删除列的定义
					formFieldDao.delByTableId(tableId);
					// 添加表列的定义
					addFields(tableId, fields, false);
					// 删除原来的表
					tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
							+ originTable.getTableName());
					// 重新创建物理表
					List<FormField> fieldList = convertFields(fields, false);
					table.setFieldList(fieldList);
					createTable(table);
					String pkTableName = TableModel.CUSTOMER_TABLE_PREFIX
							+ table.getTableName();
					// 重新添加子表外键
					for (FormTable subTable : tableList) {
						String tabName = TableModel.CUSTOMER_TABLE_PREFIX
								+ subTable.getTableName();
						tableOperator.addForeignKey(pkTableName, tabName,
								TableModel.PK_COLUMN_NAME,
								TableModel.FK_COLUMN_NAME);
					}
				} else {
					// 更新表定义，添加表列的定义。
					// 删除物理表重新创建表。
					dao.update(table);
					// 删除列的定义
					formFieldDao.delByTableId(tableId);
					// 添加表列的定义
					addFields(tableId, fields, false);
					// 删除原来的表
					tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
							+ originTable.getTableName());
					// 重新创建物理表
					List<FormField> fieldList = convertFields(fields, false);

					table.setFieldList(fieldList);
					createTable(table);
				}
			}
			// 未发布
			else {
				if (generate == 1) {
					table.setIsPublished((short) 1);
					ISysUser sysUser = (ISysUser)UserContextUtil.getCurrentUser();
					table.setPublishedBy(sysUser.getFullname());
					table.setPublishedBy(sysUser.getFullname());
				}
				// 直接该表的定义。
				dao.update(table);
				// 删除字段，重新加入字段
				formFieldDao.delByTableId(tableId);
				// 添加字段
				addFields(tableId, fields, false);
				if (table.getIsMain() == 0 && generate == 1) {
					FormTable mainTable = dao.getById(mainTableId);
					if (mainTable.getIsPublished().shortValue() == 0) {
						List<FormField> fieldList = convertFields(fields,
								false);
						table.setFieldList(fieldList);
						createTable(table);
					}
				}
			}
		}
		return 0;
	  }catch (Exception e) {
			throw new RuntimeException(e);
	  }
	}
	/**
	 * 添加选择器ID隐藏字段
	 * 
	 * @param field
	 * @return
	 */
	private FormField addHiddenFormField(FormField field) {
		FormField fieldHidden = (FormField) field.clone();
		fieldHidden.setFieldId(UniqueIdUtil.genId());

		fieldHidden.setFieldName(fieldHidden.getFieldName()
				+ FormField.FIELD_HIDDEN);
		fieldHidden.setFieldDesc(field.getFieldDesc()
				+ FormField.FIELD_HIDDEN);
		fieldHidden.setIsHidden(FormField.HIDDEN);
		short valueFrom = fieldHidden.getValueFrom().shortValue();
		// 设置人员选择器的ID脚本。
		if (valueFrom == FormField.VALUE_FROM_SCRIPT_HIDDEN
				|| valueFrom == FormField.VALUE_FROM_SCRIPT_SHOW) {
			fieldHidden.setScript(fieldHidden.getScriptID());
		}

		// TODO
		return fieldHidden;
	}

	/**
	 * 添加自定义表字段。
	 * 
	 * @param tableId
	 *            表名。
	 * @param fields
	 *            字段数组
	 * @param isExternal
	 *            是否外部表。
	 * @throws Exception
	 */
	private void addFields(Long tableId, List<FormField> fields,
			boolean isExternal) throws Exception {
		//转换字段: 增加隐藏字段，并设置字段的主键，数据类型，名称，注释信息。
		List<FormField> fieldList = convertFields(fields, isExternal);
		for (int i = 0; i < fieldList.size(); i++) {
			FormField field = fieldList.get(i);
	        //更新表字段之前先，获取对话框的表ID以及表name
	        this.setRelTableIdAndName(field);
			Long fieldId = UniqueIdUtil.genId();
			field.setFieldId(fieldId);
			field.setSn(i);
			field.setTableId(tableId);
			formFieldDao.add(field);
			//处理关联关系字段
			//this.dealRelationField(field.getFieldId(),table);
			
		}
	}
	
    /**
     * 设置字段的 关联表ID以及关联表Name
     * @param field
     */
    private void setRelTableIdAndName(FormField field)
    {
        if(field.getControlType()!=IFieldPool.RELATION_COLUMN_CONTROL){
            return;
        }
        String relData = field.getRelFormDialogStripCData();
        if(StringUtil.isEmpty(relData)){
            return;
        }
        JSONObject rel = JSONObject.fromObject(relData);
        Object rpcrefname = rel.getString("rpcrefname");
        String dialog = rel.getString("name");
        
        if (BeanUtils.isNotEmpty(rpcrefname))
        {
            CommonService commonService = (CommonService) AppUtil.getBean(rpcrefname.toString());
            IFormTable formTable = commonService.getFormDilaogTableInfo(dialog);
            field.setRelTableId(formTable.getTableId());
            field.setRelTableName(formTable.getTableName());
        }
        else
        {
            IFormTable formTable = this.getById(field.getRelTableId());
            field.setRelTableName(formTable.getTableName());
        }
    }

	/**
	 * 转换字段。
	 * 
	 * <pre>
	 * 增加隐藏字段，并设置字段的主键，数据类型，名称，注释信息。
	 * </pre>
	 * 
	 * @param fields
	 * @return
	 * @throws Exception
	 */
	private List<FormField> convertFields(List<FormField> fields,
			boolean isExternal) throws Exception {
		List<FormField> list = new ArrayList<FormField>();
		int i = 1;
		for (FormField field : fields) {
			field.setSn(i);
			i++;
			if (StringUtil.isEmpty(field.getFieldDesc()))
				field.setFieldDesc(field.getFieldName());

			list.add(field);
			if (isExternal)
				continue;
			//设置"控件类型"
			if (field.getControlType() == null) {
				field.setControlType(FormField.HIDDEN);
				continue;
			}
			//判断控件类型是否选择器
			if (isExecutorSelector(field.getControlType())
					&& field.getIsHidden().shortValue() == FormField.NO_HIDDEN) {
			//添加选择器ID隐藏字段
				FormField fieldHidden = this.addHiddenFormField(field);
				list.add(fieldHidden);
			}
		}
		return list;
	}

	/**
	 * 获取选择器关联的字段
	 * 
	 * @param tableId
	 * @return
	 */
	public Map<String, FormField[]> getExecutorSelectorField(Long tableId) {
		Map<String, FormField[]> selectorFieldMap = new HashMap<String, FormField[]>();
		FormTable bpmFormTable = getTableByIdContainHidden(tableId);
		List<FormField> fields = bpmFormTable.getFieldList();
		Map<String, FormField> fieldsMap = new HashMap<String, FormField>();
		for (FormField field : fields) {
			fieldsMap.put(field.getFieldName(), field);
		}
		for (FormField field : fields) {
			if (isExecutorSelector(field.getControlType())
					&& field.getIsHidden().shortValue() == FormField.NO_HIDDEN) {
				if (fieldsMap.containsKey(field.getFieldName()
						+ FormField.FIELD_HIDDEN)) {
					FormField[] fds = new FormField[2];
					fds[0] = field;
					fds[1] = fieldsMap.get(field.getFieldName()
							+ FormField.FIELD_HIDDEN);
					selectorFieldMap.put(field.getFieldName(), fds);
				}
			}
		}

		return selectorFieldMap;
	}

	/**
	 * 根据表名判断是否该表在系统中已经定义。
	 * 
	 * @param tableName
	 * @return
	 */
	public boolean isTableNameExisted(String tableName) {
		return dao.isTableNameExisted(tableName);
	}

	/**
	 * 判断表是否已存在，在更新时使用。
	 * 
	 * @param tableId
	 * @param tableName
	 * @return
	 */
	public boolean isTableNameExistedForUpd(Long tableId, String tableName) {
		return dao.isTableNameExistedForUpd(tableId, tableName);
	}
	
	/**
	 * 判断表名是否存在。
	 * 
	 * @param tableName
	 * @param dsAlias
	 * @return
	 */
	public boolean isTableNameExternalExisted(String tableName, String dsAlias) {
		return dao.isTableNameExternalExisted(tableName, dsAlias);
	}

	/**
	 * 通过mainTableId获得所有字表
	 * 
	 * @param mainTableId
	 * @return
	 */
	public List<FormTable> getSubTableByMainTableId(Long mainTableId) {
		return dao.getSubTableByMainTableId(mainTableId);
	}
	/**
	 * 获取mainTable被其他表引用的所有外键列。
	 * 
	 * @param mainTableId
	 * @return
	 */
	public List<FormTable> getRelTableByMainTableId(Long mainTableId) {
		return dao.getRelTableByMainTableId(mainTableId);
	}

	/**
	 * 获得所有未分配的子表
	 * 
	 * @param tableName
	 * @return
	 */
	public List<FormTable> getAllUnassignedSubTable() {
		return dao.getAllUnassignedSubTable();
	}

	/**
	 * 根据数据源名称取得子表。
	 * 
	 * @param dsName
	 * @return
	 */
	public List<FormTable> getByDsSubTable(String dsName) {
		return dao.getByDsSubTable(dsName);
	}

	/**
	 * 将表定义进行发布。
	 * 
	 * @param tableId
	 *            表定义ID
	 * @param operator
	 *            发布人
	 * @throws Exception
	 */
	public void generateTable(Long tableId, String operator) throws Exception {
		FormTable mainTable = dao.getById(tableId);
		// 将主表设为已发布
		publish(tableId, operator);

		// 将主表设为默认版本
		List<FormField> mainFields = formFieldDao
				.getAllByTableId(tableId);
		// 添加
		mainFields = convertFields(mainFields, false);
		mainTable.setFieldList(mainFields);

		createTable(mainTable);

		List<FormTable> subTables = dao.getSubTableByMainTableId(tableId);
		for (FormTable subTable : subTables) {
			// 将子表设为已发布
			publish(subTable.getTableId(), operator);

			List<FormField> subFields = formFieldDao
					.getAllByTableId(subTable.getTableId());
			subFields = convertFields(subFields, false);
			subTable.setFieldList(subFields);
			// 改表结构
			createTable(subTable);
		}
	}

	/**
	 * 创建业务数据历史表
	 * 
	 * @Methodname: generateHistoryTable
	 * @Discription:
	 * @param tableId
	 * @throws Exception
	 * 
	 */
	public void ensureHistoryTableExixts(Long tableId) throws Exception {
		FormTable mainTable = dao.getById(tableId);

		// 将主表设为默认版本
		List<FormField> mainFields = formFieldDao
				.getAllByTableId(tableId);
		// 添加
		mainFields = convertFields(mainFields, false);
		mainTable.setFieldList(mainFields);

		// 给历史表的表名加上后缀
		String hisTableName = mainTable.getTableName()
				+ TableModel.CUSTOMER_TABLE_HIS_SUFFIX;
		mainTable.setTableName(hisTableName);

		String fullTableName = TableModel.CUSTOMER_TABLE_PREFIX + hisTableName;

		if (!formHandlerDao.tableExists(fullTableName)) {
			createTable(mainTable);
		}
	}

	/**
	 * 设置表为发布状态。
	 * 
	 * @param tableId
	 * @param operator
	 */
	private void publish(Long tableId, String operator) {
		FormTable table = new FormTable();
		table.setTableId(tableId);
		table.setPublishedBy(operator);
		table.setIsPublished((short) 1);
		table.setPublishTime(new Date());
		dao.updPublished(table);
	}

	/**
	 * 关联子表。
	 * 
	 * @param mainTableId
	 * @param subTableId
	 * @throws Exception
	 */
	public void linkSubtable(Long mainTableId, Long subTableId, String fkField)
			throws Exception {
		// 获取主表
		FormTable mainTable = dao.getById(mainTableId);
		FormTable subTable = dao.getById(subTableId);

		// 建立关系
		// StringBuffer relationSb=new StringBuffer("<relation pk='"+
		// mainTable.getPkField() + "'>");
		// relationSb.append("<table name='").append(mainTable.getTableName()).append("' fk='"+fkField+"' />");
		// relationSb.append("</relation>");

		subTable.setRelation(fkField);

		// 主表已经发布，则生成子表。
		if (mainTable.getIsPublished() == 1) {
			subTable.setMainTableId(mainTableId);
			dao.update(subTable);
			// 如果表已生成则先删除表。
			if (subTable.getIsPublished() == 1) {
				tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
						+ subTable.getTableName());
			}
			// 发布子表
			publish(subTableId, mainTable.getPublishedBy());

			List<FormField> subFields = formFieldDao
					.getByTableId(subTableId);
			subFields = convertFields(subFields, false);
			subTable.setFieldList(subFields);
			// 生成实体表
			createTable(subTable);
		}
		// 未发布修改字段。
		else {
			subTable.setMainTableId(mainTableId);
			dao.update(subTable);
		}
	}

	/**
	 * 取消关联
	 * 
	 * @param subTableId
	 */
	public void unlinkSubTable(Long subTableId) {
		FormTable table = dao.getById(subTableId);
		table.setMainTableId(null);
		dao.update(table);
	}

	/**
	 * 获取可以关联的主表。 1.没有关联表单。 2.可以是发布或未发布的。
	 * 
	 * @return
	 */
	public List<FormTable> getAssignableMainTable() {

		List<FormTable> list = dao.getAssignableMainTable();

		return list;
	}

	/**
	 * 获取未发布的主表。
	 * 
	 * @return
	 */
	public List<FormTable> getAllUnpublishedMainTable() {
		List<FormTable> list = dao.getAllUnpublishedMainTable();
		return list;
	}

	/**
	 * 查找默认主表列表
	 * 
	 * @param queryFilter
	 * @return
	 */
	public List<FormTable> getAllMainTable(QueryFilter queryFilter) {
		return dao.getAllMainTable(queryFilter);
	}

	public List<FormTable> getMainTables(String tableName) {
		return dao.getMainTables(tableName);
	}

	public FormTable getByTableName(String tableName) {
		return dao.getByTableName(tableName);
	}

	/**
	 * 获取同名表
	 * **/
	public FormTable  getByTableName(String tabName, int need){
		FormTable bpmFormTable = dao.getByTableName(tabName);
		if (bpmFormTable == null){
			return null;
		}
		this.getTabInfo(bpmFormTable, need);
		return bpmFormTable;	
	}
	/**
	 * 根据表字段获取列定义对象。 columnType: 1.主键 2.一般列 3.外键 4.用户字段 5.流程RUN字段 6.流程定义ID
	 * 7.获取默认时间字段 8.组织字段
	 * 
	 * @param field
	 * @return
	 */
	private ColumnModel getByField(FormField field, int columnType) {
		ColumnModel columnModel = new ColumnModel();
		if(BeanUtils.isNotEmpty(field)){
			short contolType = field.getControlType();
			switch(contolType){
			//流程状态字段
			case IFieldPool.FLOW_STATE:
				columnModel.setDefaultValue("'"+IFieldPool.FLOW_START_KEY+"'");
				break;
			}
		}	
		
		switch (columnType) {
		// 主键
		case 1:
			columnModel.setName(TableModel.PK_COLUMN_NAME);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("主键");
			columnModel.setIsPk(true);
			columnModel.setIsNull(false);
			break;
		// 一般列
		case 2:
			columnModel.setName(TableModel.CUSTOMER_COLUMN_PREFIX
					+ field.getFieldName());
			columnModel.setColumnType(field.getFieldType());
			columnModel.setCharLen(field.getCharLen().intValue());
			columnModel.setIntLen(field.getIntLen().intValue());
			columnModel.setDecimalLen(field.getDecimalLen().intValue());
			columnModel.setIsNull(field.getIsRequired() == 0);
			columnModel.setComment(field.getFieldDesc());
			break;
		// 外键
		case 3:
			columnModel.setName(TableModel.FK_COLUMN_NAME);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("外键");
			columnModel.setIsFk(true);
			columnModel.setIsNull(false);
			break;
		// 用户字段
		case 4:
			columnModel.setName(TableModel.CUSTOMER_COLUMN_CURRENTUSERID);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("用户字段");
			columnModel.setIsFk(false);
			columnModel.setIsNull(true);
			break;
		// 流程RUN字段。
		case 5:
			columnModel.setName(TableModel.CUSTOMER_COLUMN_FLOWRUNID);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("流程RUN字段");
			columnModel.setIsFk(false);
			columnModel.setIsNull(true);
			break;
		// 流程定义ID。
		case 6:
			columnModel.setName(TableModel.CUSTOMER_COLUMN_DEFID);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("流程定义ID");
			columnModel.setIsFk(false);
			columnModel.setIsNull(true);
			break;
		// 获取默认时间字段
		case 7:
			String defaultValue = getDefaultCurrentTime();
			columnModel.setName(TableModel.CUSTOMER_COLUMN_CREATETIME);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_DATE);
			// columnModel.setIntLen(20);
			columnModel.setComment("记录插入时间");
			columnModel.setIsFk(false);
			columnModel.setIsNull(true);
			columnModel.setDefaultValue(defaultValue);
			break;
		// 组织字段
		case 8:
			columnModel.setName(TableModel.CUSTOMER_COLUMN_CURRENTORGID);
			columnModel.setColumnType(ColumnModel.COLUMNTYPE_INT);
			columnModel.setIntLen(20);
			columnModel.setComment("组织字段");
			columnModel.setIsFk(false);
			columnModel.setIsNull(true);
			break;

		}

		return columnModel;
	}

	/**
	 * 根据表和表字段创建实体表。
	 * 
	 * @param table
	 *            表对象
	 * @param fields
	 *            表字段
	 * @throws SQLException
	 */
	private void createTable(FormTable table) throws SQLException {
		try{
			TableModel tableModel = getTableModelByFormTable(table); 
			tableOperator.createTable(tableModel);
			// 当前表为子表，添加表关联。
			if (table.getIsMain() == 0) {
				Long mainTableId = table.getMainTableId();
				FormTable bpmFormTable = dao.getById(mainTableId);
				// 添加外键
				tableOperator.addForeignKey(TableModel.CUSTOMER_TABLE_PREFIX
						+ bpmFormTable.getTableName(),
						TableModel.CUSTOMER_TABLE_PREFIX + table.getTableName(),
						TableModel.PK_COLUMN_NAME, TableModel.FK_COLUMN_NAME);
				// 创建索引。
				tableOperator.createIndex(
						TableModel.CUSTOMER_TABLE_PREFIX + table.getTableName(),
						TableModel.FK_COLUMN_NAME);
			}
		}catch(Exception e){
			throw new RuntimeException(e);
		}
		
	}

	/**
	 * 根据FormTable获取TableModel对象。
	 * 
	 * @param table
	 */
	private TableModel getTableModelByFormTable(FormTable table) {
		TableModel tableModel = new TableModel();
		tableModel.setName(TableModel.CUSTOMER_TABLE_PREFIX
				+ table.getTableName());
		tableModel.setComment(table.getTableDesc());

		// 给表添加主键
		ColumnModel pkModel = getByField(null, 1);
		tableModel.addColumnModel(pkModel);

		// // 用户字段。
		// ColumnModel userColumnModel = getByField(null, 4);
		// tableModel.addColumnModel(userColumnModel);
		// // 组织字段。
		// ColumnModel orgColumnModel = getByField(null, 8);
		// tableModel.addColumnModel(orgColumnModel);
		// 主表的情况。
		if (table.getIsMain() == 1) {
			// 添加一个运行列
			// ColumnModel runColumnModel = getByField(null, 5);
			// tableModel.addColumnModel(runColumnModel);
			// // 流程定义ID
			// ColumnModel defColumnModel = getByField(null, 6);
			// tableModel.addColumnModel(defColumnModel);
			// // 默认时间。
			// ColumnModel createTimeColumnModel = getByField(null, 7);
			// tableModel.addColumnModel(createTimeColumnModel);
		}

		// 添加自定义表的字段。
		for (FormField field : table.getFieldList()) {
			field.setFieldName(StringUtil.trim(field.getFieldName(), " "));
			ColumnModel columnModel = getByField(field, 2);
			tableModel.addColumnModel(columnModel);
		}
		// 如果表定义为子表的情况。
		// 添加一个外键的列定义。
		if (table.getIsMain() == 0) {
			ColumnModel fkModel = getByField(null, 3);
			tableModel.addColumnModel(fkModel);
		}
		return tableModel;

	}

	/**
	 * 根据数据库获取时间默认值函数用于创建数据库表。
	 * 
	 * @return
	 */
	private String getDefaultCurrentTime() {
		String dbType = tableOperator.getDbType();
		if (SqlTypeConst.ORACLE.equals(dbType)) {
			return "SYSDATE";
		} else if (SqlTypeConst.MYSQL.equals(dbType)) {
			return "CURRENT_TIMESTAMP";
		} else if (SqlTypeConst.SQLSERVER.equals(dbType)) {
			return "getdate()";
		} else if (SqlTypeConst.DB2.equals(dbType)) {
			return "CURRENT DATE";
		}
		return "";
	}

	/**
	 * 根据数据源和表名获取表对象。
	 * 
	 * @param dsName
	 * @param tableName
	 * @return
	 */
	public FormTable getByDsTablename(String dsName, String tableName) {
		return this.dao.getByDsTablename(dsName, tableName);
	}

	/**
	 * 根据表的Id删除外部表定义。
	 * 
	 * @param tableId
	 */
	public void delExtTableById(Long tableId) {
		FormTable bpmFormTable = this.getById(tableId);
		int isExternal = bpmFormTable.getIsExternal();
		// 自定义表直接返回
		if (isExternal == 0)
			return;
		// 子表的处理情况
		this.dao.delById(tableId);
	}

	/**
	 * 根据表ID删除表定义。
	 * 
	 * <pre>
	 * 1.如果该表是主表，那么先删除其所有的关联的子表。
	 * 2.根据表的ID删除表定义。
	 * </pre>
	 * 
	 * @param tableId
	 */
	public void delByTableId(Long tableId) {
		FormTable bpmFormTable = this.getById(tableId);
		String tableName = bpmFormTable.getTableName();
		if (bpmFormTable.getIsMain() == 1) {
			List<FormTable> subTableList = getSubTableByMainTableId(tableId);
			if (BeanUtils.isNotEmpty(subTableList)) {
				for (FormTable subTable : subTableList) {
					// 删除实体表
					tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
							+ subTable.getTableName());
					dao.delById(subTable.getTableId());
				}
			}
		}
		// 删除实体表。
		tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX + tableName);
		dao.delById(tableId);
	}
	
	/**
	 * 根据表Id删除表所有字段
	 * @param tableId
	 */
	public void delFieldsByTableId(Long tableId){
		formFieldDao.delByTableId(tableId);
	}
	
	/**
	 * 根据表id获取表的表和表字段的信息。
	 * 
	 * <pre>
	 * 	如果输入的是主表id。
	 *  那么将返回主表的信息和字段信息，同时返回子表和字段的信息。
	 *  字段数据不包含删除的字段和隐藏的字段。
	 *  need 是否包含隐藏或者删除字段   0为正常字段   1为正常字段加上隐藏字段    2 为正常字段加上逻辑删除字段
	 * </pre>
	 * 
	 * @param tableId
	 * @return
	 */
	public FormTable getTableById(Long tableId, int need) {
		FormTable bpmFormTable = dao.getById(tableId);
		if (bpmFormTable == null)
			return null;
		this.getTabInfo(bpmFormTable, need);

		return bpmFormTable;
	}

	/**
	 * 根据主表获取整个表的信息包括列。(主表,rel关系表,sub子表)
	 * 
	 * @param bpmFormTable
	 *            自定义表
	 * @param need
	 *            是否包含隐藏或者删除字段 0为正常字段 1为正常字段加上隐藏字段 2 为正常字段加上逻辑删除字段
	 */
	private void getTabInfo(FormTable bpmFormTable, int need) {
		Long tableId = bpmFormTable.getTableId();
		List<FormField> fieldList = getFormFieldList(tableId, need);
		bpmFormTable.setFieldList(fieldList);
		
		//文件附件关系 by YangBo
		FileAttachRights fileAttachRights=new FileAttachRights();
		JSONArray attachJSONArry=fileAttachRights.getAttachRights();
		bpmFormTable.setAttachJSONArry(attachJSONArry);
		
		//rel关系表
		bpmFormTable.setRelTableList(this.getRefTabInfo(need, bpmFormTable));
		//如果当前表不是主表，则返回。
		if (bpmFormTable.getIsMain().shortValue() == FormTable.NOT_MAIN.shortValue()){
		    return;
		}
		//子表
		List<FormTable> subTableList = dao.getSubTableByMainTableId(tableId);
		if (BeanUtils.isEmpty(subTableList)){
		    return;
		}
		for (FormTable table : subTableList) {
			List<FormField> subFieldList = getFormFieldList(
					table.getTableId(), need);
			table.setFieldList(subFieldList);
		}
		bpmFormTable.setSubTableList(subTableList);
	}
	
	/**
	 *获取引用主表的关联表信息
	 */
	public List<FormTable> getRefTabInfo(int need, FormTable mainFormTable){
		List<FormTable> relTableList = new ArrayList<FormTable>();
		Long mainTableId = mainFormTable.getTableId();
		//获取mainTable被其他表引用的所有外键列。
		List<FormTable> formTableList =  getRelTableByMainTableId(mainTableId);
		for (FormTable formTable : formTableList) {
			//获取引用主表的外键表的列信息
			List<FormField> fieldList = getFormFieldList(formTable.getTableId(), need);
			formTable.setFieldList(fieldList);
			relTableList.add(formTable);
		}
		return relTableList;
	}
	//获取表的列信息
	public List<FormField> getFormFieldList(Long tableId, int need) {
		List<FormField> fieldList = null;
		if (need == FormTable.NEED_HIDE) {
			fieldList = formFieldDao.getByTableIdContainHidden(tableId);
		} else if ((need == FormTable.NEED_DELETE)) {
			fieldList = formFieldDao.getAllByTableId(tableId);
		} else {
			fieldList = formFieldDao.getByTableId(tableId);
		}
		return fieldList;
	}
	
	/**
	 * @param tableId
	 * @param controlType ：  控件类型
	 * @return
	 */
	public List<FormField> getControlField(Long tableId, short controlType){
		List<FormField> rtn_f = new ArrayList();
		List<FormField> fieldList = formFieldDao.getByTableId(tableId);
		for(FormField f : fieldList ){
			short clt = f.getControlType();
			if(clt==controlType){
				rtn_f.add(f);
			}
		}
		return rtn_f;
	}
	/**
	 * @param table : formTable
	 * @param controlType : 控件类型
	 * @return
	 */
	public List<FormField> getControlField(FormTable table, short controlType){
		List<FormField> rtn_f = new ArrayList();
		List<FormField> fieldList = table.getFieldList();
		for(FormField f : fieldList ){
			short clt = f.getControlType();
			if(clt==controlType){
				rtn_f.add(f);
			}
		}
		return rtn_f;
	}
	/**
	 * 根据流程定义获取流程表的所有数据。
	 * 
	 * <pre>
	 * 1.包括表的元数据。
	 * 2.表的列元数据列表。
	 * 3.子表数据。
	 * 4.子表列的元数据。
	 * </pre>
	 * 
	 * @param defId
	 * @return
	 * @throws Exception
	 */
	public FormTable getByDefId(Long defId) throws Exception {
		FormTable bpmFormTable = dao.getByDefId(defId);
		if (bpmFormTable == null){
		    return null;
		}
		getTabInfo(bpmFormTable, FormTable.NEED_HIDE);
		return bpmFormTable;
	}
	
    /**
     * 根据流程定义获取流程表的所有数据。
     * 
     * <pre>
     * 1.包括表的元数据。
     * 2.表的列元数据列表。
     * 3.子表数据。
     * 4.子表列的元数据。
     * </pre>
     * 
     * @param defId
     * @return
     * @throws Exception
     */
    public FormTable getByDefIdAndNodeId(Long defId,String nodeId) throws Exception {
        FormTable bpmFormTable = dao.getByDefIdAndNodeId(defId,nodeId);
        if (bpmFormTable == null){
            return null;
        }
        getTabInfo(bpmFormTable, FormTable.NEED_HIDE);
        return bpmFormTable;
    }
	/**
	 * 
	 * @param tableId
	 * @return
	 */
	public FormTable getTableById(Long tableId) {
		return getByTableId(tableId, FormTable.NEED_NORMAL);
	}

	/**
	 * 根据表id获取(主表,rel关系表,sub子表)信息
	 * 
	 * @param tableId
	 *            表ID
	 * @param need
	 *            0为正常字段 1为正常字段加上隐藏字段 2 为正常字段加上逻辑删除字段
	 * @return
	 */
	public FormTable getByTableId(Long tableId, int need) {
		FormTable bpmFormTable = dao.getById(tableId);
		if (bpmFormTable == null)
			return null;
		this.getTabInfo(bpmFormTable, need);
		return bpmFormTable;
	}

	/**
	 * 根据表的ID取得表的列名是否可以编辑。
	 * 
	 * @param tableId
	 * @return
	 */
	public boolean getCanEditColumnNameTypeByTableId(Long tableId) {
		if (tableId == 0)
			return true;
		FormTable bpmFormTable = dao.getById(tableId);
		if (bpmFormTable == null)
			return true;
		boolean hasData = formHandlerDao
				.getHasData(TableModel.CUSTOMER_TABLE_PREFIX
						+ bpmFormTable.getTableName());
		if (hasData) 
			return false;
		int formAmount = formDefDao.getFormDefAmount(tableId);
		if (formAmount > 1) 
			return false;
		return true;
	}

	/**
	 * 保存数据库表。
	 * 
	 * <pre>
	 *  bpmFormTable 需要指定表的主键id，如果没有指定，那么当作新表处理。
	 * 	1.FormTable 表的表id为0。
	 * 		1.isPublish为true，生成表。
	 * 		2.isPublish为false，不生成表。
	 *  2.已经保存了表单数据。
	 *  	1.已经发布。
	 *  		1.是否生成数据。
	 *  			更新表。
	 *  		2.表单数量大于1个的情况。
	 *  			更新表。
	 *  		  否则:
	 *  			删除表定义。
	 * 				删除表。
	 * 				添加表定义。
	 * 				重新生成表。
	 *  	2.未发布。
	 *  		1.删除表定义。
	 *  		2.添加表。
	 *  		3.如果为发布。
	 *  			生成物理表。
	 * </pre>
	 * 
	 * @param bpmFormTable
	 * @throws Exception
	 */
	public Long saveTable(FormTable bpmFormTable) throws Exception {
		Long mainTableId = bpmFormTable.getTableId();
		// 新表的情况。
		if (mainTableId == 0) {
			// 添加表信息
			Long tableId = addTable(bpmFormTable);
			// 生成表。
			genTable(bpmFormTable);
			return tableId;
		}
		// 定义已经添加。
		else {
			// 获取原表定义。
			FormTable orginTable = getTableById(mainTableId, 2); // 包含有删除字段
			// 判断表中是否已有数据。
			boolean hasData = formHandlerDao
					.getHasData(TableModel.CUSTOMER_TABLE_PREFIX
							+ orginTable.getTableName());
			// 如果这个表定义了多个表单的情况，那么不能对表做重新创建的的操作
			// 有数据
			if (hasData) {
				// 更新表
				updTable(bpmFormTable, orginTable);
			}
			// 无数据的情况
			else {
				// 取得表单数量
				int formAmount = formDefDao.getFormDefAmount(mainTableId);
				if (formAmount > 1) {
					// 更新表。
					updTable(bpmFormTable, orginTable);
				}
				// 1.删除表定义信息
				// 2.删除表
				// 3.添加表
				// 4.添加实体表
				else {
					delTable(orginTable);
					dropTable(orginTable);
					Long tableId = addTable(bpmFormTable);
					genTable(bpmFormTable);
					return tableId;
				}
			}
		}
		return mainTableId;
	}

	/**
	 * 添加表。
	 * 
	 * <pre>
	 * 	根据表对象，添加主表和子表。
	 * </pre>
	 * 
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	private Long addTable(FormTable bpmFormTable) throws Exception {
		// 保存主表数据
		Long mainTableId = UniqueIdUtil.genId();
		bpmFormTable.setTableId(mainTableId);
		bpmFormTable.setGenByForm(1);
		bpmFormTable.setMainTableId(0L);
		bpmFormTable.setIsMain((short) 1);
		// 设置发布字段
		bpmFormTable.setIsPublished((short) 1);
		bpmFormTable.setGenByForm(1);

		// 添加主表
		dao.add(bpmFormTable);

		// 添加子表列数据。
		List<FormField> fields = bpmFormTable.getFieldList();
		List<FormField> mainFields = convertFields(fields, false);
		// 重新设置字段。
		bpmFormTable.setFieldList(mainFields);

		for (int i = 0; i < mainFields.size(); i++) {
			FormField field = mainFields.get(i);
			if (field.getFieldId() == 0) {
				field.setFieldId(UniqueIdUtil.genId());
			}
			field.setSn(i + 1);
			field.setTableId(mainTableId);
			formFieldDao.add(field);
		}
		// 子表列表。
		List<FormTable> subTableList = bpmFormTable.getSubTableList();

		if (BeanUtils.isEmpty(subTableList)) {
			return mainTableId;
		}
		// 添加子表
		for (int i = 0; i < subTableList.size(); i++) {
			FormTable subTable = subTableList.get(i);
			// 生成子表主键。
			Long subTableId = UniqueIdUtil.genId();
			subTable.setTableId(subTableId);
			subTable.setMainTableId(mainTableId);
			subTable.setGenByForm(1);
			subTable.setIsMain((short) 0);
			subTable.setIsPublished((short) 1);

			dao.add(subTable);

			List<FormField> subfields = subTable.getFieldList();
			// 字段转换。
			List<FormField> subfieldList = convertFields(subfields, false);

			subTable.setFieldList(subfieldList);

			for (int k = 0; k < subfieldList.size(); k++) {
				FormField field = subfieldList.get(k);

				if (field.getFieldId() == 0) {
					field.setFieldId(UniqueIdUtil.genId());
				}

				field.setSn(k + 1);
				field.setTableId(subTableId);
				formFieldDao.add(field);
			}
		}

		return mainTableId;
	}

	/**
	 * 删除表定义。
	 * 
	 * <pre>
	 * 	删除表定义，如果有子表定义，将子表定义一并删除。
	 * </pre>
	 * 
	 * @param bpmFormTable
	 */
	public void delTable(FormTable bpmFormTable) {
		Long tableId = bpmFormTable.getTableId();
		// 删除子表
		List<FormTable> subTableList = bpmFormTable.getSubTableList();
		if (BeanUtils.isNotEmpty(subTableList)) {
			for (FormTable subTable : subTableList) {
				Long subTableId = subTable.getTableId();
				formFieldDao.delByTableId(subTableId);
				dao.delById(subTableId);
			}
		}
		formFieldDao.delByTableId(tableId);
		dao.delById(tableId);
	}

	/**
	 * 根据FormTable一次性生成表。
	 * 
	 * <pre>
	 * 同时创建主表和子表。
	 * 并创建外键。
	 * </pre>
	 * 
	 * @param table
	 * @throws SQLException
	 */
	private void genTable(FormTable table) throws SQLException {
		// 创建主表
		TableModel mainTableModel = getTableModelByFormTable(table);
		tableOperator.createTable(mainTableModel);
		for (FormTable subTable : table.getSubTableList()) {
			TableModel subTableModel = getTableModelByFormTable(subTable);
			tableOperator.createTable(subTableModel);
			tableOperator.addForeignKey(TableModel.CUSTOMER_TABLE_PREFIX
					+ table.getTableName(), TableModel.CUSTOMER_TABLE_PREFIX
					+ subTable.getTableName(), TableModel.PK_COLUMN_NAME,
					TableModel.FK_COLUMN_NAME);
		}
	}

	/**
	 * 一次性删除表。
	 * 
	 * @param table
	 * @throws SQLException
	 */
	public void dropTable(FormTable table) throws SQLException {
		for (FormTable subTable : table.getSubTableList()) {
			String subTableName = TableModel.CUSTOMER_TABLE_PREFIX
					+ subTable.getTableName();
			tableOperator.dropTable(subTableName);
		}
		tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
				+ table.getTableName());
	}

	/**
	 * 更新表。 bpmFormTable:不需要指定主键信息。
	 * 
	 * <pre>
	 * 	1.更新表数据。
	 * 	2.更新字段数据。
	 * 		1.如果字段为添加。
	 * 			1.添加列定义。
	 * 			2.添加字段。
	 * 		2.更新字段。
	 * 			1.如果删除字段，字段不做真正的删除，只是做删除标记。
	 * 			2.更新字段信息，不更新实体表信息。
	 * 	3.更新子表数据信息。
	 * 		1.添加子表。
	 * 			添加子表定义。
	 * 			添加子表字段定义。
	 * 			添加是体表
	 * 		2.更新表。
	 * 			1.更新列。
	 * 			2.添加列定义，添加列实体列。
	 * 
	 *   注意事项：
	 *   bpmFormTable必须指定tableId。
	 *   子表对象也需要指定。
	 * </pre>
	 * 
	 * @param bpmFormTable
	 *            新表
	 * @param orginTable
	 *            原表。
	 * @throws Exception
	 */
	private void updTable(FormTable bpmFormTable, FormTable orginTable)
			throws Exception {
		Long mainTableId = orginTable.getTableId();
		String tableName = bpmFormTable.getTableName();
		// 设置主键相同
		bpmFormTable.setTableId(mainTableId);
		bpmFormTable.setGenByForm(1);
		bpmFormTable.setIsPublished((short) 1);
		bpmFormTable.setPublishedBy(orginTable.getPublishedBy());
		bpmFormTable.setPublishTime(orginTable.getPublishTime());

		dao.update(bpmFormTable);

		List<FormField> fields = bpmFormTable.getFieldList();
		// 获取原表字段。
		List<FormField> orginFields = orginTable.getFieldList();
		// 取得表单数量
		int formAmount = formDefDao.getFormDefAmount(mainTableId);
		Map<String, List<FormField>> mainFieldMap = new HashMap<String, List<FormField>>();
		if (formAmount > 1) {
			mainFieldMap = caculateFields(fields, orginFields, false);
		} else {
			mainFieldMap = caculateFields(fields, orginFields, true);
		}
		List<FormField> updList = mainFieldMap.get("upd");
		List<FormField> addList = mainFieldMap.get("add");

		// 更新列
		for (int i = 0; i < updList.size(); i++) {
			FormField bpmFormField = updList.get(i);
			formFieldDao.update(bpmFormField);
		}
		if (BeanUtils.isNotEmpty(addList)) {
			addList = convertFields(addList, false);
			// 添加列
			for (int i = 0; i < addList.size(); i++) {
				FormField bpmFormField = addList.get(i);
				// 设置字段id。
				if (bpmFormField.getFieldId() == 0) {
					bpmFormField.setFieldId(UniqueIdUtil.genId());
				}

				bpmFormField.setTableId(mainTableId);
				formFieldDao.add(bpmFormField);
				ColumnModel columnModel = getByField(bpmFormField, 2);
				tableOperator.addColumn(TableModel.CUSTOMER_TABLE_PREFIX
						+ tableName, columnModel);
			}
		}

		// 子表处理
		// 获取原子表
		List<FormTable> originTableList = orginTable.getSubTableList();
		Map<String, FormTable> originTableMap = new HashMap<String, FormTable>();
		for (FormTable orginSubTable : originTableList) {
			originTableMap.put(orginSubTable.getTableName().toLowerCase(),
					orginSubTable);
		}
		// 子表的处理
		List<FormTable> subTableList = bpmFormTable.getSubTableList();
		if (BeanUtils.isEmpty(subTableList))
			return;

		for (int i = 0; i < subTableList.size(); i++) {
			FormTable subTable = subTableList.get(i);
			String subTableName = subTable.getTableName().toLowerCase();
			if (originTableMap.containsKey(subTableName)) {
				FormTable orginSubTable = originTableMap.get(subTableName);
				List<FormField> orginSubFields = orginSubTable
						.getFieldList();

				Long orginTableId = orginSubTable.getTableId();
				// 更新表的信息。
				subTable.setTableId(orginTableId);
				subTable.setMainTableId(mainTableId);
				subTable.setIsPublished((short) 1);
				subTable.setPublishedBy(orginSubTable.getPublishedBy());
				subTable.setPublishTime(orginSubTable.getPublishTime());

				dao.update(subTable);
				List<FormField> subFields = subTable.getFieldList();
				Map<String, List<FormField>> subFieldMap = new HashMap<String, List<FormField>>();
				if (formAmount > 1) {
					subFieldMap = caculateFields(subFields, orginSubFields,
							false);
				} else {
					subFieldMap = caculateFields(subFields, orginSubFields,
							true);
				}
				List<FormField> updSubList = subFieldMap.get("upd");
				List<FormField> addSubList = subFieldMap.get("add");

				// 更新列
				for (int k = 0; k < updSubList.size(); k++) {
					FormField bpmFormField = updSubList.get(k);
					formFieldDao.update(bpmFormField);
				}
				// 添加列不为空
				if (BeanUtils.isNotEmpty(addSubList)) {
					addSubList = convertFields(addSubList, false);
					// 添加列
					for (int k = 0; k < addSubList.size(); k++) {

						FormField bpmFormField = addSubList.get(k);
						if (bpmFormField.getFieldId() == 0) {
							bpmFormField.setFieldId(UniqueIdUtil.genId());
						}
						bpmFormField.setTableId(orginTableId);
						// 设置表
						formFieldDao.add(bpmFormField);
						// 添加字段
						ColumnModel columnModel = getByField(bpmFormField, 2);
						tableOperator
								.addColumn(TableModel.CUSTOMER_TABLE_PREFIX
										+ subTableName, columnModel);
					}
				}
			}
			// 新加的子表。
			else {
				Long newTableId = UniqueIdUtil.genId();
				subTable.setTableId(newTableId);
				subTable.setIsMain((short) 0);
				subTable.setMainTableId(mainTableId);
				subTable.setIsPublished((short) 1);
				dao.add(subTable);

				List<FormField> subFields = subTable.getFieldList();
				subFields = convertFields(subFields, false);
				for (FormField field : subFields) {
					Long newFieldId = UniqueIdUtil.genId();
					field.setFieldId(newFieldId);
					field.setTableId(newTableId);
					formFieldDao.add(field);
				}
				TableModel subTableModel = getTableModelByFormTable(subTable);
				tableOperator.createTable(subTableModel);
				tableOperator.addForeignKey(TableModel.CUSTOMER_TABLE_PREFIX
						+ tableName, TableModel.CUSTOMER_TABLE_PREFIX
						+ subTable.getTableName(), TableModel.PK_COLUMN_NAME,
						TableModel.FK_COLUMN_NAME);
			}
		}
	}

	/**
	 * 生成所有发布表
	 * 
	 * @param queryFilter
	 * @throws Exception
	 */
	public void genAllTable() throws Exception {
		List<FormTable> list = dao.getAllTable();
		for (FormTable bpmFormTable : list) {
			if (bpmFormTable.getIsMain().shortValue() == 1
					&& bpmFormTable.getIsPublished().shortValue() == 1) {
				if (bpmFormTable.getTableName().length() < 20) {
					// genTable(bpmFormTable.getTableId());
				} else {
					System.err.println("表：" + bpmFormTable.getTableName()
							+ " 长度" + bpmFormTable.getTableName().length());
					// bpmFormTable.setIsPublished((short) 0);
					// dao.update(bpmFormTable);
				}
			}
		}
	}

	/**
	 * 生成表
	 * 
	 * @param tableId
	 * @throws Exception
	 */
	public void genTable(Long tableId) throws Exception {
		FormTable mainTable = dao.getById(tableId);

		// 将主表设为默认版本
		List<FormField> mainFields = formFieldDao
				.getAllByTableId(tableId);
		// 添加
		mainFields = convertFields(mainFields, false);
		mainTable.setFieldList(mainFields);
		List<FormTable> subTables = dao.getSubTableByMainTableId(tableId);
		for (FormTable subTable : subTables) {
			if (subTable.getTableName().length() >= 19) {
				return;
			}
		}

		createTable(mainTable);

		for (FormTable subTable : subTables) {

			List<FormField> subFields = formFieldDao
					.getAllByTableId(subTable.getTableId());
			subFields = convertFields(subFields, false);
			subTable.setFieldList(subFields);
			// 改表结构
			createTable(subTable);
		}
	}

	/**
	 * 保存复制的表和字段
	 * 
	 * @param json
	 * @throws Exception
	 */
	public void saveCopy(String json) throws Exception {
		saveCopy(json,false);
	}
	public void saveCopy(String json,boolean addSync) throws Exception {
		JSONArray jsonArray = JSONArray.fromObject(json);
		Map<Long, Long> mainTable = new HashMap<Long, Long>();

		for (Object object : jsonArray) {
			JSONObject jsonObject = JSONObject.fromObject(object);
			String tableId = (String) jsonObject.get("tableId");
			if (BeanUtils.isEmpty(tableId))
				continue;
			String tableName = (String) jsonObject.get("tableName");
			String tableDesc = (String) jsonObject.get("tableDesc");

			FormTable bpmFormTable = dao.getById(new Long(tableId));
			List<FormField> fieldList = formFieldDao
					.getByTableIdContainHidden(bpmFormTable.getTableId());

			Long newTableId = UniqueIdUtil.genId();
			mainTable.put(new Long(tableId), newTableId);

			bpmFormTable.setTableId(newTableId);
			Long mainTableId = bpmFormTable.getMainTableId();
			boolean isMain = true;//主表
			if (BeanUtils.isNotEmpty(mainTableId)
					&& mainTableId.longValue() > 0L){//子表
				isMain = false;
				bpmFormTable.setMainTableId(mainTable.get(mainTableId));
			}
				

			bpmFormTable.setTableName(tableName);
			bpmFormTable.setTableDesc(tableDesc);
			bpmFormTable.setPublishTime(new Date());
			bpmFormTable.setIsPublished(new Short("0"));
			bpmFormTable.setIsExternal(0);
			bpmFormTable.setKeyType(new Short("0"));
			dao.add(bpmFormTable);
			
			if(addSync&&isMain){//添加同步字段,主表添加，子表不需要记录
				List<FormField> syncFields = getByFormFieldJson(FormField.SYNC_FIELD);
				fieldList.addAll(syncFields);
			}
			// 保存字段
			this.saveFormField(fieldList, newTableId);
		}
	}
	private List<FormField> getByFormFieldJson(String fieldsJson){
		JSONArray aryJson = JSONArray.fromObject(fieldsJson);
		List<FormField> list = new ArrayList<FormField>();
		for(Object obj : aryJson){
			JSONObject fieldJObject = (JSONObject)obj;
			String options = "";
			String ctlProperty="";
			if (fieldJObject.containsKey("options")) {
				options = fieldJObject.getString("options");
				fieldJObject.remove("options");
			}
			
			if (fieldJObject.containsKey("ctlProperty")) {
				ctlProperty = fieldJObject.getString("ctlProperty");
				fieldJObject.remove("ctlProperty");
				
			}
			FormField FormField = (FormField)JSONObject.toBean(fieldJObject,FormField.class);
			FormField.setOptions(options);
			FormField.setCtlProperty(ctlProperty);
			FormField.setFieldName(StringUtil.trim(FormField.getFieldName(), " "));
			
			list.add(FormField);
		}
		return list;
	}

	/**
	 * 保存字段
	 * 
	 * @param fieldList
	 * @param tableId
	 */
	private void saveFormField(List<FormField> fieldList, Long tableId) {
		for (FormField bpmFormField : fieldList) {
			bpmFormField.setFieldId(UniqueIdUtil.genId());
			bpmFormField.setTableId(tableId);
			formFieldDao.add(bpmFormField);
		}
	}

	/**
	 * 判断控件类型是否选择器
	 * 
	 * @param controlType
	 * @return
	 */
	public static boolean isExecutorSelector(Short controlType) {
		if (BeanUtils.isEmpty(controlType))
			return false;
		if (controlType.shortValue() == IFieldPool.SELECTOR_USER_SINGLE
				|| controlType.shortValue() == IFieldPool.SELECTOR_USER_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_ORG_SINGLE
				|| controlType.shortValue() == IFieldPool.SELECTOR_ORG_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_POSITION_SINGLE
				|| controlType.shortValue() == IFieldPool.SELECTOR_POSITION_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_ROLE_SINGLE
				|| controlType.shortValue() == IFieldPool.SELECTOR_ROLE_MULTI
				|| controlType.shortValue() == IFieldPool.SELECTOR_PROCESS_INSTANCE)
			return true;
		return false;
	}

	// ======================================导入导出分割线======================================================

	/**
	 * TODO 导入自定义表XML
	 * 
	 * @param fileStr
	 * @throws Exception
	 */
	public void importXml(InputStream inputStream) throws Exception {
		Document doc = Dom4jUtil.loadXml(inputStream);
		if(doc!=null){
			Element root = doc.getRootElement();
			// 验证格式是否正确
			XmlUtil.checkXmlFormat(root, "bpm", "tables");
	
			String xmlStr = root.asXML();
			FormTableXmlList formTableXmlList = (FormTableXmlList) XmlBeanUtil
					.unmarshall(xmlStr, FormTableXmlList.class);
	
			List<FormTableXml> list = formTableXmlList
					.getFormTableXmlList();
			Set<ISerialNumber> serialSet = new HashSet<ISerialNumber>();
			for (FormTableXml formTableXml : list) {
				// 导入表，并解析相关信息
				this.importFormTableXml(formTableXml);
				this.setSerialNumber(formTableXml.getSerialNumberList(), serialSet);
			}
			// 流水号的导入
			this.importSerialNumber(serialSet);
		}
	}

	/**
	 * 设置导入的流水号
	 * 
	 * @param identityList
	 * @param identitySet
	 */
	public void setSerialNumber(List<?extends ISerialNumber> serialList,
			Set<ISerialNumber> serialSet) {
		if (BeanUtils.isNotEmpty(serialList)) {
			for (ISerialNumber serial : serialList) {
				serialSet.add(serial);
			}
		}
	}

	/**
	 * 导入的流水号
	 * 
	 * @param identitySet
	 * @throws Exception
	 */
	public void importSerialNumber(Set<ISerialNumber> serialSet) throws Exception {
		if (BeanUtils.isNotEmpty(serialSet)) {
			for (ISerialNumber serial : serialSet) {
				this.importSerialNumber(serial);
			}
		}
	}

	/**
	 * 转换导入数据
	 * 
	 * @param bpmFormTableXml
	 * @param isMain
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public FormTable convertFormTableXml(FormTableXml bpmFormTableXml,
			Short isMain, Map<Long, Long> map) throws Exception {
		FormTable mainTable = new FormTable();

		mainTable = bpmFormTableXml.getFormTable();
		if (BeanUtils.isNotEmpty(map)) {
			Long tableId = map.get(mainTable.getTableId());
			if (BeanUtils.isNotEmpty(tableId))
				mainTable.setTableId(tableId);
		}

		if (BeanUtils.isNotEmpty(bpmFormTableXml.getFormFieldList())) {
			List<FormField> mainFields = bpmFormTableXml
					.getFormFieldList();
			List<FormField> fieldList = new ArrayList<FormField>();
			for (FormField bpmFormField : mainFields) {
				Long tableId = map.get(bpmFormField.getTableId());
				if (BeanUtils.isNotEmpty(tableId))
					bpmFormField.setTableId(tableId);
				if (BeanUtils.isNotEmpty(bpmFormField.getIsDeleted())
						&& bpmFormField.getIsDeleted().intValue() != 1)
					fieldList.add(bpmFormField);
			}
			mainTable.setFieldList(fieldList);
		}

		if (isMain.shortValue() == FormTable.NOT_MAIN.shortValue())
			return mainTable;
		List<FormTableXml> subTableXmlList = bpmFormTableXml
				.getFormTableXmlList();
		if (BeanUtils.isEmpty(subTableXmlList))
			return mainTable;
		List<FormTable> subTableList = new ArrayList<FormTable>();
		for (FormTableXml subTableXml : subTableXmlList) {
			FormTable subTable = convertFormTableXml(subTableXml,
					FormTable.NOT_MAIN, map);
			subTableList.add(subTable);
		}
		mainTable.setSubTableList(subTableList);

		return mainTable;

	}

	/**
	 * 导入时候生成自定义表
	 * 
	 * @param bpmFormTableXml
	 * @throws Exception
	 */
	public Map<Long, Long> importFormTableXml(FormTableXml bpmFormTableXml)
			throws Exception {
		Short isMain = FormTable.IS_MAIN;
		Map<Long, Long> map = new HashMap<Long, Long>();

		FormTable bpmFormTable = this.convertFormTableXml(
				bpmFormTableXml, isMain, map);
		FormTable bft = dao.getByTableName(bpmFormTable.getTableName());
		FormTable orginTable = new FormTable();
		if (BeanUtils.isNotEmpty(bft))
			orginTable = this.getTableByIdContainHidden(bft.getTableId());
		// 导入的表进行
		this.importFormTable(bpmFormTable, isMain, map);

		FormTable destFormTable = this.convertFormTableXml(
				bpmFormTableXml, isMain, map);

		if (destFormTable.getIsPublished().shortValue() == FormTable.NOT_PUBLISH
				.shortValue()) {
			// parseMap(destFormTable,bpmFormTable,map);
			return map;
		}
		// 对物理表处理
		this.importGenPhysicsTable(destFormTable, orginTable);

		// parseMap(destFormTable,bpmFormTable,map);
		return map;
	}

	/**
	 * 查询包含隐藏的表和字段
	 * 
	 * @param tableId
	 * @return
	 */
	public FormTable getTableByIdContainHidden(Long tableId) {
		FormTable bpmFormTable = dao.getById(tableId);
		if (bpmFormTable == null)
			return null;
		List<FormField> fieldList = formFieldDao
				.getByTableIdContainHidden(tableId);
		bpmFormTable.setFieldList(fieldList);
		if (bpmFormTable.getIsMain().shortValue() == FormTable.NOT_MAIN
				.shortValue())
			return bpmFormTable;

		List<FormTable> subTableList = dao.getSubTableByMainTableId(tableId);
		if (BeanUtils.isEmpty(subTableList))
			return bpmFormTable;

		for (FormTable table : subTableList) {
			List<FormField> subFieldList = formFieldDao
					.getFieldsByTableId(table.getTableId());
			table.setFieldList(subFieldList);
		}
		bpmFormTable.setSubTableList(subTableList);

		return bpmFormTable;
	}

	/**
	 * 导入生成物理表
	 * 
	 * @param bpmFormTable
	 * @param orginTable
	 * @throws Exception
	 */
	private void importGenPhysicsTable(FormTable destFormTable,
			FormTable orginTable) throws Exception {
		if (BeanUtils.isEmpty(destFormTable))
			return;
		boolean isExists = formHandlerDao
				.tableExists(TableModel.CUSTOMER_TABLE_PREFIX
						+ destFormTable.getTableName());
		// 物理表不存在则生
		if (!isExists) {
			this.genTable(destFormTable);
		} else {
			this.importUpdTable(destFormTable, orginTable);
		}
	}

	/**
	 * 导入更新表
	 * 
	 * @param destFormTable
	 * @param orginTable
	 * @throws Exception
	 */
	private void importUpdTable(FormTable destFormTable,
			FormTable orginTable) throws Exception {
		boolean hasData = formHandlerDao
				.getHasData(TableModel.CUSTOMER_TABLE_PREFIX
						+ destFormTable.getTableName());
		if (hasData) {// 如果存在数据
			this.importDataTable(destFormTable, orginTable,
					FormTable.IS_MAIN);
		} else {
			// 删除表
			try {
				dropTable(destFormTable);
			} catch (Exception e) {
				// 存在子表的问题
				FormTable bpmFormTable = this
						.getTableByIdContainHidden(destFormTable.getTableId());
				this.dropTable(bpmFormTable);
				destFormTable.setSubTableList(bpmFormTable.getSubTableList());
			}
			// 生成表
			this.genTable(destFormTable);
		}
	}

	/**
	 * 导入有数据的表 并生成字段
	 * 
	 * @param destFormTable
	 *            现在的表
	 * @param orginTable
	 *            原来的表
	 * @param isMain
	 *            是否主表
	 * @throws Exception
	 */
	private void importDataTable(FormTable destFormTable,
			FormTable orginTable, Short isMain) throws Exception {
		String tableName = destFormTable.getTableName();

		List<FormField> fields = destFormTable.getFieldList();

		if (BeanUtils.isNotEmpty(fields))
			fields = convertFields(fields, false);

		// 处理是否存在的字段
		BaseTableMeta meta = TableMetaFactory.getMetaData(null);
		TableModel tableModel = meta
				.getTableByName(TableModel.CUSTOMER_TABLE_PREFIX + tableName);
		if (BeanUtils.isNotEmpty(tableModel)
				&& BeanUtils.isNotEmpty(tableModel.getColumnList())) {

			Set<FormField> fileSet = new HashSet<FormField>();
			for (FormField bpmFormField : fields) {
				boolean flag = true;
				for (ColumnModel column : tableModel.getColumnList()) {
					if (column
							.getName()
							.toUpperCase()
							.equals(TableModel.CUSTOMER_COLUMN_PREFIX
									+ bpmFormField.getFieldName().toUpperCase())) {
						flag = false;
						break;
					}
				}
				if (flag)
					fileSet.add(bpmFormField);
			}

			if (BeanUtils.isNotEmpty(fileSet)) {
				for (FormField bpmFormField : fileSet) {
					// 设置字段id。
					ColumnModel columnModel = this.getByField(bpmFormField, 2);
					tableOperator.addColumn(TableModel.CUSTOMER_TABLE_PREFIX
							+ tableName, columnModel);
				}
			}

		}

		if (isMain.shortValue() == FormTable.NOT_MAIN.shortValue())
			return;
		// =========子表处理===
		// 子表的处理
		List<FormTable> subTableList = destFormTable.getSubTableList();
		if (BeanUtils.isEmpty(subTableList))
			return;
		// 获取原来子表
		List<FormTable> originTableList = orginTable.getSubTableList();

		Map<String, FormTable> originTableMap = new HashMap<String, FormTable>();
		for (FormTable orginSubTable : originTableList) {
			originTableMap.put(orginSubTable.getTableName().toLowerCase(),
					orginSubTable);
		}

		for (FormTable subTable : subTableList) {
			String subTableName = subTable.getTableName().toLowerCase();
			if (originTableMap.containsKey(subTableName)) {
				// 更新表
				this.importDataTable(destFormTable, orginTable,
						FormTable.NOT_MAIN);
			}
			// 新加的子表。
			else {
				List<FormField> subFields = subTable.getFieldList();
				subFields = this.convertFields(subFields, false);

				TableModel subTableModel = this
						.getTableModelByFormTable(subTable);
				tableOperator.createTable(subTableModel);
				tableOperator.addForeignKey(TableModel.CUSTOMER_TABLE_PREFIX
						+ tableName, TableModel.CUSTOMER_TABLE_PREFIX
						+ subTable.getTableName(), TableModel.PK_COLUMN_NAME,
						TableModel.FK_COLUMN_NAME);
			}
		}
	}

	/**
	 * 导入表，并解析相关信息
	 * 
	 * @param map
	 * @param bpmFormTableXml
	 * @param mainTableId
	 * @throws Exception
	 */
	public void importFormTable(FormTable bpmFormTable, Short isMain,
			Map<Long, Long> map) throws Exception {
		Long tableId = bpmFormTable.getTableId();
		// 导入字段
		List<FormField> bpmFormFieldList = bpmFormTable.getFieldList();
		// 如果存在子表，递归导入子表
		List<FormTable> subTableList = bpmFormTable.getSubTableList();
		// 导入表
		bpmFormTable = this.importFormTable(bpmFormTable);
		if (BeanUtils.isNotEmpty(bpmFormFieldList)) {
			for (FormField bpmFormField : bpmFormFieldList) {
				this.importFormField(bpmFormField, bpmFormTable);
			}
		}
		map.put(tableId, bpmFormTable.getTableId());
		if (isMain.shortValue() == FormTable.NOT_MAIN.shortValue())
			return;

		if (BeanUtils.isNotEmpty(subTableList)) {
			for (FormTable subTable : subTableList) {
				this.importFormTable(subTable, FormTable.NOT_MAIN, map);
			}
		}
	}

	/**
	 * 导入自定义表时插入 FormTable
	 * 
	 * @param t
	 * 
	 * @param xmlStr
	 * @param tableId
	 * @throws Exception
	 */
	private FormTable importFormTable(FormTable bpmFormTable)
			throws Exception {
		FormTable table = dao.getByTableName(bpmFormTable.getTableName());
		String isMain = (bpmFormTable.getIsMain().shortValue() == FormTable.IS_MAIN) ? "主"
				: "从";
		if (BeanUtils.isEmpty(table)) {
			table = new FormTable();
			Long tableId = bpmFormTable.getTableId();
			// bpmFormTable.setCreatetime(new Date());
			// bpmFormTable.setPublishTime(new Date());
			bpmFormTable.setUpdatetime(new Date());
			// BeanUtils.copyProperties(table, bpmFormTable);
			BeanUtils.copyNotNullProperties(table, bpmFormTable);
			table.setTableId(tableId);
			dao.add(table);
			MsgUtil.addMsg(MsgUtil.SUCCESS,
					isMain + "表名为‘" + table.getTableDesc() + "’，该表成功导入！");

			return table;
		} else {
			Long tableId = table.getTableId();
			// table.setUpdatetime(new Date());
			BeanUtils.copyNotNullProperties(bpmFormTable, table);
			bpmFormTable.setTableId(tableId);
			dao.update(bpmFormTable);
			MsgUtil.addMsg(MsgUtil.WARN,
					isMain + "表名为‘" + bpmFormTable.getTableDesc()
							+ "’的已经存在，该表进行更新！");
			return bpmFormTable;
		}

	}

	/**
	 * 导入自定义表时插入 FormField
	 * 
	 * @param bpmFormTable
	 * @param bpmFormTable
	 * 
	 * @param xmlStr
	 * @param tableId
	 * @throws Exception
	 */
	private void importFormField(FormField field, FormTable bpmFormTable)
			throws Exception {
		FormField bpmFormField = formFieldDao.getById(field.getFieldId());
		Long tableId = bpmFormTable.getTableId();
		if (BeanUtils.isEmpty(bpmFormField)) {
			FormField bpmFormField1 = formFieldDao.getFieldByTidFna(
					tableId, field.getFieldName());
			if (BeanUtils.isNotEmpty(bpmFormField1)) {
				Long fieldId = bpmFormField1.getFieldId();
				// field.setCreatetime(new Date());
				field.setUpdatetime(new Date());
				BeanUtils.copyNotNullProperties(bpmFormField1, field);
				bpmFormField1 = this.convertFormField(bpmFormField1);
				bpmFormField1.setFieldId(fieldId);
				bpmFormField1.setTableId(tableId);
				formFieldDao.update(bpmFormField1);
				MsgUtil.addMsg(MsgUtil.WARN, "字段名为‘" + field.getFieldDesc()
						+ "’的已经存在，该字段进行更新！");
			} else {
				field.setTableId(tableId);
				field = this.convertFormField(field);
				formFieldDao.add(field);
				MsgUtil.addMsg(MsgUtil.SUCCESS, "字段名为‘" + field.getFieldDesc()
						+ "’，该字段成功导入！");
			}

		} else {
			Long fieldId = bpmFormField.getFieldId();
			// field.setCreatetime(new Date());
			field.setUpdatetime(new Date());
			BeanUtils.copyNotNullProperties(bpmFormField, field);
			bpmFormField = this.convertFormField(bpmFormField);
			bpmFormField.setFieldId(fieldId);
			bpmFormField.setTableId(tableId);
			formFieldDao.update(bpmFormField);
			MsgUtil.addMsg(MsgUtil.WARN, "字段名为‘" + bpmFormField.getFieldDesc()
					+ "’的已经存在，该字段进行更新！");
		}
	}

	/**
	 * 导入自定义表
	 * 
	 * @param field
	 */
	private FormField convertFormField(FormField field) {
		field.setFieldId(field.getFieldId());
		// 脚本
		field.setScript(StringUtil.convertScriptLine(field.getScript(), false));
		// 下拉框
		field.setOptions(StringUtil.convertLine(field.getOptions(), false));

		if (isExecutorSelector(field.getControlType())) {
			if (field.getFieldName().lastIndexOf(FormField.FIELD_HIDDEN) != -1) {
				field.setIsHidden(FormField.HIDDEN);
			}
			// 处理导入旧数据
			if (field.getFieldName().lastIndexOf("Id") != -1) {
				field.setIsHidden(FormField.HIDDEN);
				field.setFieldName(field.getFieldName().substring(0,
						field.getFieldName().length() - 2)
						+ FormField.FIELD_HIDDEN);
			}

		}
		return field;
	}

	/**
	 * 导入流水号时插入
	 * 
	 * @param serialNumber
	 * @return
	 * @throws Exception
	 */
	private void importSerialNumber(ISerialNumber serial) throws Exception {
	    BaseSerialNumber isExist = (BaseSerialNumber) serialNumberService.getByAlias(serial.getAlias());
		if (isExist == null) {
			serial.setId(UniqueIdUtil.genId());
			serial.setCurDate(DateFormatUtil.format(new Date(), "yyyy-MM-dd"));
			serialNumberService.add(serial);
			MsgUtil.addMsg(MsgUtil.SUCCESS, "流水号为‘" + serial.getName()
					+ "’，该流水号成功导入！");
		} else {
			MsgUtil.addMsg(MsgUtil.ERROR, "流水号为‘" + serial.getName()
					+ "’已经存在，该流水号终止导入！");
		}
	}

	/**
	 * TODO 导出自定义表XML
	 * 
	 * @param Long
	 *            [] tableIds 导出的tableId
	 * @param map
	 * @return
	 */
	public String exportXml(Long[] tableIds, Map<String, Boolean> map)
			throws Exception {
		FormTableXmlList bpmFormTableXmls = new FormTableXmlList();
		List<FormTableXml> list = new ArrayList<FormTableXml>();
		for (int i = 0; i < tableIds.length; i++) {
			FormTable formTable = dao.getById(tableIds[i]);
			FormTableXml bpmFormTableXml = this.exportTable(formTable, map);
			list.add(bpmFormTableXml);
		}
		bpmFormTableXmls.setFormTableXmlList(list);
		return XmlBeanUtil
				.marshall(bpmFormTableXmls, FormTableXmlList.class);
	}

	/**
	 * 导出表的信息
	 * 
	 * @param formTable
	 * @param map
	 * @return
	 */
	public FormTableXml exportTable(FormTable formTable,
			Map<String, Boolean> map) {
		map = XmlUtil.getTableDefaultExportMap(map);

		FormTableXml formTableXml = new FormTableXml();
		// 字段列表
		List<FormField> bpmFormFieldList = new ArrayList<FormField>();
		// 流水号列表
		List<BaseSerialNumber> serialNumberList = new ArrayList<BaseSerialNumber>();
		// 子表
		List<FormTableXml> bpmFormTableXmlList = new ArrayList<FormTableXml>();
		Long tableId = formTable.getTableId();
		if (BeanUtils.isNotEmpty(tableId)) {
			List<FormField> formFieldList = new ArrayList<FormField>();
			// 字段
			if (BeanUtils.isNotEmpty(map)
					&& BeanUtils.isNotEmpty(map.get("formField"))) {
				formFieldList = formFieldDao
						.getByTableIdContainHidden(tableId);
				this.exportFormTable(formFieldList, bpmFormFieldList);
			}
			// 流水号
			if (BeanUtils.isNotEmpty(map)
					&& BeanUtils.isNotEmpty(map.get("identity"))) {
				if (map.get("identity")) {
					this.exportSerialNumber(formFieldList, serialNumberList);
				}
			}
			// 有子表，递归
			if (BeanUtils.isNotEmpty(map)
					&& BeanUtils.isNotEmpty(map.get("subTable"))) {
				if (map.get("subTable")) {
					this.exportSubTable(tableId, map, bpmFormTableXmlList);
				}

			}
		}

		formTableXml.setFormTable(formTable);
		if (BeanUtils.isNotEmpty(bpmFormFieldList))
			formTableXml.setFormFieldList(bpmFormFieldList);
		if (BeanUtils.isNotEmpty(serialNumberList))
			formTableXml.setSerialNumberList(serialNumberList);
		if (BeanUtils.isNotEmpty(bpmFormTableXmlList))
			formTableXml.setFormTableXmlList(bpmFormTableXmlList);
		return formTableXml;
	}

	/**
	 * 导出字段
	 * 
	 * @param formFieldList
	 * @param bpmFormFieldList
	 */
	private void exportFormTable(List<FormField> formFieldList,
			List<FormField> bpmFormFieldList) {
		for (FormField bpmFormField : formFieldList) {
			// 脚本
			bpmFormField.setScript(StringUtil.convertScriptLine(
					bpmFormField.getScript(), true));
			// 下拉框
			bpmFormField.setOptions(StringUtil.convertLine(
					bpmFormField.getOptions(), true));
			bpmFormFieldList.add(bpmFormField);
		}
	}

	/**
	 * 导出流水号
	 * 
	 * @param formFieldList
	 * @param serialList
	 */
	private void exportSerialNumber(List<FormField> formFieldList,
			List<BaseSerialNumber> serialList) {
		for (FormField formField : formFieldList) {
			// 流水号
			if (StringUtil.isNotEmpty(formField.getSerialNumber())) {
				BaseSerialNumber identity = (BaseSerialNumber) serialNumberService.getByAlias(formField
						.getSerialNumber());
				serialList.add(identity);
			}
		}

	}

	/**
	 * 导出子表
	 * 
	 * @param tableId
	 * @param map
	 * @param bpmFormTableXmlList
	 */
	private void exportSubTable(Long tableId, Map<String, Boolean> map,
			List<FormTableXml> bpmFormTableXmlList) {
		List<FormTable> subTables = dao.getSubTableByMainTableId(tableId);
		if (BeanUtils.isNotEmpty(subTables)) {
			for (FormTable bpmFormTable : subTables) {
				bpmFormTableXmlList.add(this.exportTable(bpmFormTable, map));
			}
		}
	}

	/**
	 * 重置自定义表
	 * 
	 * @param tableId
	 * @throws Exception
	 */
	public void reset(Long tableId) {
		FormTable bpmFormTable = getById(tableId);
		if (bpmFormTable.getIsMain() == 1) {
			List<FormTable> subTableList = getSubTableByMainTableId(tableId);
			if (BeanUtils.isNotEmpty(subTableList)) {
				for (FormTable subTable : subTableList) {
					// 删除实体表
					tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
							+ subTable.getTableName());
					resetFormTableInfo(subTable);
				}
			}
		}
		// 删除实体表。
		tableOperator.dropTable(TableModel.CUSTOMER_TABLE_PREFIX
				+ bpmFormTable.getTableName());

		resetFormTableInfo(bpmFormTable);

	}

	private void resetFormTableInfo(FormTable bpmFormTable) {
		if (bpmFormTable.getIsMain() == 0) {
			bpmFormTable.setMainTableId(0L);
		}
		bpmFormTable.setIsPublished(FormTable.NOT_PUBLISH);
		bpmFormTable.setPublishedBy(null);
		bpmFormTable.setPublishTime(null);
		dao.update(bpmFormTable);
		List<FormField> fields = formFieldDao.getByTableId(bpmFormTable
				.getTableId());
		for (FormField field : fields) {
			int isDeleted = field.getIsDeleted() == null ? FormField.IS_DELETE_N
					: field.getIsDeleted();
			if (FormField.IS_DELETE_Y == isDeleted) {
				formFieldDao.delById(field.getFieldId());
			}
		}
	}

	// ======================================导入导出分割线======================================================

	public FormTable getByAliasTableName(String dsAlias, String tableName) {
		return dao.getByAliasTableName(dsAlias, tableName);
	}

	/**
	 * 获取可授权的主表
	 * 
	 * @param tableId
	 *            子表的Id
	 * @return
	 */
	public List<FormTable> getMainTableSubTableId(Long tableId) {
		FormTable subTable = dao.getById(tableId);
		List<FormTable> list = null;
		if (StringUtil.isNotEmpty(subTable.getDsAlias())) {
			list = dao.getMainTableByDsName(subTable.getDsAlias());
		} else {
			list = dao.getAssignableMainTable();
		}
		return list;
	}

	/**
	 * 获取默认的导出的Map
	 * @param map
	 * @return
	 */
	public Map<String, Boolean> getDefaultExportMap(Map<String, Boolean> map) {
		if (BeanUtils.isEmpty(map)) {
			map = new HashMap<String, Boolean>();
			map.put("bpmFormTable", true);
			map.put("bpmFormField", true);
			map.put("subTable", true);
			map.put("identity", true);
		}
		return map;
	}

	/**
	 * 只获取name,id 等几个字段
	 * @param tabid
	 * @return
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, String>> getTabs(Map<String, String> params) {
		return (List<Map<String, String>>)this.dao.getTabs(params);
	}
	
    /**
     * 只获取name,id 等几个字段
     * @param tabid
     * @return
     */
	@Override
	public Map<String, String> getTableMapById(Long tabId) {
		return this.dao.getTableMapById(tabId);
	}
	
	/**
     * 只获取name,id 等几个字段
     * @param tabid
     * @return
     */
	@Override
    public Map<String, String> getTableMapByName(String name) {
        return this.dao.getTableMapByName(name);
    }
	   
	/**
	 * 根据表IDs得到表数据
	 * @param ids 数组 表ID
	 * @return
	 */
	@Override
	public List<FormTable> getByTabIds(String[] ids) {
		return this.dao.getByTabIds(ids);
	}
	
	/**
     * 根据表names得到表数据
     * @param names 数组 表name
     * @return
     */
    @Override
    public List<FormTable> getByTabNames(String[] names) {
        return this.dao.getByTabNames(names);
    }
    
	/**
	 * 查找表的字段
	 * @param tabId
	 * @return
	 */
	@Override
	public List<Map<String, String>> getFiledsByTabId(Long tabId) {
		return this.formFieldDao.getFiledsByTabId(tabId);

	}
	/**
	 * 根据数组ID获取表，只读取部分重要数据
	 * @param ids
	 * @return
	 */
	@Override
	public List<FormField> getFiledsByIds(String[] ids) {
		return this.formFieldDao.getFiledsByIds(ids);
	}
	
	/**
	 * 查找两个表的关系字段
	 * @param mtabId 主表ID
	 * @param reltabId 关系表ID
	 * @return
	 */
	@Override
	public List<FormField> getRelFiledByTableId(Long mtabId, Long reltabId) {
		return this.formFieldDao.getRelFiledByTableId(mtabId,reltabId);
	}

	/** 
	 * 导入自定义表
	 * @param bpmFormTableXmlList
	 * @throws Exception
	 */
	@Override
	public void importFormTableXml(List<BaseFormTableXml> bpmFormTableXmlList) throws Exception {
		if (BeanUtils.isNotEmpty(bpmFormTableXmlList)) {
			Set<ISerialNumber> identitySet = new HashSet<ISerialNumber>();
			for (IFormTableXml bpmFormTableXml : bpmFormTableXmlList) {
				// 导入表，并解析相关信息
				this.importFormTableXml((FormTableXml)bpmFormTableXml);
				this.setSerialNumber(
						bpmFormTableXml.getSerialNumberList(), identitySet);
			}
			this.importSerialNumber(identitySet);
		}
	}

	/**
	 * 导出表的信息
	 * 
	 * @param formTable
	 * @param map
	 * @return
	 */
	@Override
	public BaseFormTableXml exportTable(Long tableId, Map<String, Boolean> map) {
		FormTable formTable=this.dao.getById(tableId);
		return this.exportTable(formTable, map);
	}
	/**
	 * 根据流程定义ID返回表名称。
	 * 如果是外部表则返回 数据源别名 +“_" + 表名
	 * @param defId
	 * @return
	 */
	@Override
	public List<? extends FormTable> getTableNameByDefId(Long defId) {
		return  this.dao.getBySqlKey("getTableNameByDefId",defId);
	}
	

	/**
	 * 根据前端所配置 获得表信息列表
	 * @param request
	 * @return
	 */
	public List<FormTable> getFormtableList(String[] tableIds,String[] classNames,String[] classVars,String[] packageNames,String system,String[]formDefIds){
		List<FormTable> list=new ArrayList<FormTable>();
		List<FormTable> subtables=new ArrayList<FormTable>();
		for(int i=0;i<tableIds.length;i++){
			Long tableId=Long.parseLong(tableIds[i]);
			Map<String,String> vars=new HashMap<String, String>();
			vars.put("class", classNames[i]);
			vars.put("classVar", classVars[i]);
			vars.put("package", packageNames[i]);
			vars.put("system", system);
			FormTable bpmFormTable=this.getById(tableId);
			bpmFormTable.setVariable(vars);
			List<FormField> fieldList=this.formFieldDao.getByTableIdContainHidden(tableId);
			List<FormField> fields=new ArrayList<>();
			//字段值来源为脚本计算时，脚本去掉换行处理
			for(FormField field:fieldList){
				String script="";
				for(String s:field.getScript().split("\n")){
					script+=s.trim();
				}
				field.setScript(script);
				if(bpmFormTable.getIsExternal()==1){
					field.setFieldName(field.getFieldName().toLowerCase());
				}
				try {
					for(String formDefId:formDefIds){
						FormDef bpmFormDef=formDefDao.getById(Long.parseLong(formDefId));
						String options = CodeUtil.getDialogTags(getFormHtml(bpmFormDef,bpmFormTable,true),field); 
						if(StringUtil.isNotEmpty(options)){
							field.setOptions(options);
						}
					}
				}catch(Exception e){}
				fields.add(field);
			}
			if(bpmFormTable.getIsExternal()==1){
				bpmFormTable.setPkField(bpmFormTable.getPkField().toLowerCase());
				if(bpmFormTable.getIsMain()!=1){
					bpmFormTable.setRelation(bpmFormTable.getRelation().toLowerCase());
				}
			}
			bpmFormTable.setFieldList(fields);
			if(bpmFormTable.getIsMain()!=1){
				subtables.add(bpmFormTable);
			}
			list.add(bpmFormTable);
		}
		for(FormTable subtable:subtables){
			for(FormTable table:list){
				if((table.getIsMain()==1)&&(table.getTableId().equals(subtable.getMainTableId()))){
					table.getSubTableList().add(subtable);
				}
			}
		}
		return list;
	}
	
	
	public String getFormHtml(IFormDef bpmFormDef, IFormTable table,
			boolean isEdit) throws Exception{
		String html=bpmFormDef.getHtml();
		if(FormDef.DesignType_CustomDesign==bpmFormDef.getDesignType()){
			ParseReult result = FormUtil.parseHtmlNoTable(html, table.getTableName(), table.getTableDesc());		
			html = formHandlerService.obtainHtml(bpmFormDef.getTabTitle(), result, null,false);
		}
		String template=CodeUtil.getFreeMarkerTemplate(html, table, isEdit);
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("isEdit",isEdit);
		map.put("table", table);
		String output = freemarkEngine.parseByStringTemplate(map, template);
		return output;
		
	}
	
    /**
     * 根据 表ID 字段名获取字段信息，只读取部分重要数据
     * @param tableId
     * @param filedName
     * @return
     */
    @Override
    public List<? extends IFormField> getFileds(Long tableId, String[] filedName)
    {
        return this.formFieldDao.getFileds(tableId,filedName);
    }

    /** 
    * @Title: getParentTableByTableId 
    * @Description: TODO(根据表ID获取父亲表) 
    * @param @param tableId
    * @param @return     
    * @return List<FormTable>    返回类型 
    * @throws 
    */
    public List<FormTable> getParentTableByTableId(Long tableId)
    {
        return dao.getParentTableByTableId(tableId);
    }
    public boolean saveFormTable(IOutTable outTable,StringBuffer log){
    	try{
    		FormTable formTable = new FormTable();
        	formTable.setTableName(outTable.getTableName());
        	formTable.setTableDesc(outTable.getTableDesc());
        	List<FormField> fields = new ArrayList();
        	for(int i=0;i<outTable.getField().size();i++){
        		IOutField col = outTable.getField().get(i);
        		fields.add(FormField.transField(col));
        	}
        	formTable.setFieldList(fields);
        	FormTable formTable_db = this.getByTableName(formTable.getTableName());
        	if(formTable_db != null){
        		this.delByTableId(formTable_db.getTableId());
        		formFieldDao.delByTableId(formTable_db.getTableId());
        	}
        	if(!outTable.isExceptTable()){
        		this.addFormTable(formTable); 
        		log.append("    --->"+outTable.getTableDesc()+"表转换成功").append("\r\n");
        	}
        	return true;
    	}catch(Exception e){
    		e.printStackTrace();
    		log.append("    --->"+outTable.getTableDesc()+"表转换失败，原因如下："+e.getMessage()).append("\r\n");
    		return false;
    	}
    }     
}
