package com.cssrc.ibms.core.form.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.sf.json.JSONArray;
import net.sf.json.JSONNull;
import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.ISubTable;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.core.constant.form.Constants;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.encrypt.FiledEncryptFactory;
import com.cssrc.ibms.core.encrypt.IFiledEncrypt;
import com.cssrc.ibms.core.form.dao.FormHandlerDao;
import com.cssrc.ibms.core.form.model.BusBak;
import com.cssrc.ibms.core.form.model.FormData;
import com.cssrc.ibms.core.form.model.FormField;
import com.cssrc.ibms.core.form.model.FormTable;
import com.cssrc.ibms.core.form.model.PkValue;
import com.cssrc.ibms.core.form.model.RelTable;
import com.cssrc.ibms.core.form.model.SqlModel;
import com.cssrc.ibms.core.form.model.SubTable;
import com.cssrc.ibms.core.form.service.FormFieldService;
import com.cssrc.ibms.core.table.ColumnModel;
import com.cssrc.ibms.core.table.TableModel;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 将json数据转换为FormData对象数据。
 * @author zhulongchao
 *
 */
public class FormDataUtil {
	private static Log logger = LogFactory.getLog(FormDataUtil.class);
	
	/**
	 * 传入formData对象，解析成sqlmodel列表数据。(包含主表、sub子表、rel关系表)
	 * @param parmas // 从dataTemplate中取设置sub表的排序
	 * @param formData
	 * @param actDefId
	 * @param nodeId
	 * @return
	 */
	public static List<SqlModel> parseSql(Map<String,Object>params,FormData formData,String actDefId,String nodeId){
		//获取主表main的主键对象
		PkValue pkValue=formData.getPkValue();
		boolean isAdd=pkValue.getIsAdd();
		List<SqlModel> list=new ArrayList<SqlModel>();
		//获取main主表
		FormTable mainTalble= formData.getFormTable();
		//获取sub子表map
		Map<String,FormTable> subTableMap=convertTableMap(mainTalble.getSubTableList());
		//获取rel关系表map
		Map<String,FormTable> relTableMap=convertTableMap(mainTalble.getRelTableList());
		//判断是否为新增。
		if(isAdd){
			//获取插入"主表"数据的sqlModel，sqlModel中包含field、value、pk、sql。
			SqlModel sqlModel=getInsert(mainTalble, formData.getMainFields());
			//sqlModel中set main主表的主键Pk
			sqlModel.setPk(pkValue.getValue().toString());
			//sqlModel中set main主表
			sqlModel.setFormTable(mainTalble);
			//放入sqlModel列表list中
			list.add(sqlModel);
			//插入子表sub数据
			for(ISubTable subTable:formData.getSubTableList()){
				//获取子表sub表名
				String tableName=subTable.getTableName().toLowerCase();
				//获取子表sub的FormTable对象
				FormTable bpmFormTable=subTableMap.get(tableName);
				subTable.setTableId(bpmFormTable.getTableId());
				//获取子表数据。
				List<Map<String,Object>> dataList= subTable.getDataList();
				for(Map<String,Object> row:dataList){
					//获取主键。
					String pk=row.get(subTable.getPkName().toLowerCase()).toString();
					////获取插入"子表"数据的sqlModel，sqlModel中包含field、value、pk、sql。
					SqlModel subSqlModel=getInsert(bpmFormTable, row);
					//sqlModel中set sub子表的主键Pk
					subSqlModel.setPk(pk);
					//sqlModel中set sub子表
					subSqlModel.setFormTable(bpmFormTable);
					//放入sqlModel列表list中
					list.add(subSqlModel);
				}
			}
		}else{
			//"更新" 主表数据Model对象
			SqlModel sqlModel=getUpdate(formData);
			if(sqlModel!=null){
				//放入sqlModel列表list中
				list.add(sqlModel);
				//sqlModel中set main主表的主键Pk
				sqlModel.setPk(formData.getPkValue().getValue().toString());
				//sqlModel中set main主表
				sqlModel.setFormTable(mainTalble);
			}
			//处理子表sub数据。
			for(ISubTable subTable:formData.getSubTableList()){
				//获取子表sub表名
				String tableName=subTable.getTableName().toLowerCase();
				//获取子表sub的FormTable对象
				FormTable table=subTableMap.get(tableName);
				
				FormHandlerDao formHandlerDao=(FormHandlerDao)AppUtil.getBean(FormHandlerDao.class);
				//查询出当前数据库中，子表sub数据记录。
				List<Map<String, Object>> subDataList= formHandlerDao.getByFk(params,table, pkValue.getValue().toString(), actDefId, nodeId,true, table.getRelation());
				//获取当前网页上传来的子表sub记录。
				List<Map<String, Object>> curDataList=subTable.getDataList();
				// 通过数据库中sub子表记录和网页上传来的子表sub记录，判断是新增记录还是修改记录还是删除记录。
				List<SqlModel> updDelList= getUpdDelByList( table, curDataList,subDataList);
				//放入sqlModel列表list中
				list.addAll(updDelList);
			}
		}
		
		//处理rel表数据。
		for(RelTable relTable:formData.getRelTableList()){
			//获取从表rel表名
			String tableName=relTable.getRelTableName().toLowerCase();
			//获取从表rel的主键列名  
			List<FormField> relTableFields=relTable.getRelTableFieldList();
			//获取外键field
			FormField fkField = getFKFieldByMainAndRelTable(mainTalble,relTableFields);
			//获取数据库中的外键列名，含F_
			String fkName = TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase()+fkField.getFieldName();
			//获取关联的rel表对象
			FormTable relFormTable=relTableMap.get(tableName);
			
			FormHandlerDao formHandlerDao=(FormHandlerDao)AppUtil.getBean(FormHandlerDao.class);
			//原来的数据。
			//构建rel数据   //查询出当前数据库中，从表rel数据记录。
			List<Map<String, Object>> relDataList= formHandlerDao.getDataByFk(params,relFormTable, pkValue.getValue().toString(),true, fkName);
			//获取当前网页上传来的从表rel记录。
			List<Map<String, Object>> curDataList=relTable.getRelTableDataList();
			
			//通过数据库中rel从表记录和网页上传来的rel从表记录，判断是新增记录还是修改记录还是删除记录。
			
			List<SqlModel> updDelList= getUpdDelRefByList( relFormTable, curDataList,relDataList, fkName,fkField);
			
			list.addAll(updDelList);
		}
		
		
		return list;
	}
	
    
    /**
     * 传入formData对象，解析成sqlmodel列表数据。(包含主表、sub子表、rel关系表)
     * 
     * @param parmas // 从dataTemplate中取设置sub表的排序
     * @param formData
     * @param actDefId
     * @param nodeId
     * @return
     */
    public static List<BusBak> parseBusBak(Map<String, Object> params, FormData formData, String actDefId, String nodeId)
    {
        // 获取主表main的主键对象
        PkValue pkValue = formData.getPkValue();
        boolean isAdd = pkValue.getIsAdd();
        List<BusBak> list = new ArrayList<BusBak>();
        // 获取main主表
        FormTable mainTalble = formData.getFormTable();
        // 获取sub子表map
        Map<String, FormTable> subTableMap = convertTableMap(mainTalble.getSubTableList());
        // 获取rel关系表map
        Map<String, FormTable> relTableMap = convertTableMap(mainTalble.getRelTableList());
        // 判断是否为新增。
        if (!isAdd)
        {
            // 备份主表数据
            FormHandlerDao formHandlerDao = (FormHandlerDao)AppUtil.getBean(FormHandlerDao.class);
            Map<String, Object> dbData = null;
            try
            {
                dbData =
                    formHandlerDao.getByKey(null, mainTalble.getTableId(), pkValue.getValue().toString(), true)
                        .getMainCommonFields();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            Map<String, Object> mapData = formData.getMainCommonFields();
            BusBak busBak = BusBak.getInstance(mainTalble.getTableId(),dbData, mapData,params);
            
            if (busBak != null)
            {
                busBak.setTableId(mainTalble.getTableId());
                busBak.setBusPk(Long.valueOf(pkValue.getValue().toString()));
                list.add(busBak);
            }
            
            // 处理子表sub数据。
            for (ISubTable subTable : formData.getSubTableList())
            {
                // 获取子表sub表名
                String tableName = subTable.getTableName().toLowerCase();
                // 获取子表sub的FormTable对象
                FormTable subTableFrom = subTableMap.get(tableName);
                // 查询出当前数据库中，子表sub数据记录。
                List<Map<String, Object>> curDataList =
                    formHandlerDao.getByFk(params,
                        subTableFrom,
                        pkValue.getValue().toString(),
                        actDefId,
                        nodeId,
                        true,
                        subTableFrom.getRelation());
                // 获取当前网页上传来的子表sub记录。
                List<Map<String, Object>> editDataList = subTable.getDataList();
                list.addAll(BusBak.getInstanceList(subTableFrom, curDataList, editDataList,params));
            }
        }
        
        // 处理rel表数据。
        for (RelTable relTable : formData.getRelTableList())
        {
            // 获取从表rel表名
            String tableName = relTable.getRelTableName().toLowerCase();
            // 获取从表rel的主键列名
            List<FormField> relTableFields = relTable.getRelTableFieldList();
            // 获取外键field
            FormField fkField = getFKFieldByMainAndRelTable(mainTalble, relTableFields);
            // 获取数据库中的外键列名，含F_
            String fkName = TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase() + fkField.getFieldName();
            // 获取关联的rel表对象
            FormTable relFormTable = relTableMap.get(tableName);
            FormHandlerDao formHandlerDao = (FormHandlerDao)AppUtil.getBean(FormHandlerDao.class);
            // 原来的数据。
            // 构建rel数据 //查询出当前数据库中，从表rel数据记录。
            List<Map<String, Object>> curDataList =
                formHandlerDao.getDataByFk(params,relFormTable, pkValue.getValue().toString(), true, fkName);
            // 获取当前网页上传来的从表rel记录。
            List<Map<String, Object>> editDataList = relTable.getRelTableDataList();
            list.addAll(BusBak.getInstanceList(relFormTable, curDataList, editDataList,params));
            
        }
        return list;
    }
	

    
	/**
	 * 获取更新或者删除的SqlModel列表。
	 * 通过数据库中rel从表记录和网页上传来的rel从表记录，判断是新增记录还是修改记录还是删除记录。
	 * @param tableName
	 * @param curList 网页上传来的从表rel记录。
	 * @param originList 查询出当前数据库中，从表rel数据记录。
	 * @param  fkName  关联的外键列名称
	 * @param  fkField    关联的外键列 
	 * @return
	 */
	private static List<SqlModel> getUpdDelRefByList(FormTable bpmFormTable ,List<Map<String, Object>> curList,List<Map<String, Object>> originList, String fkName,FormField fkField){
		String tableName=bpmFormTable.getFactTableName();
		//获取主键field
		String pkField=bpmFormTable.getPkField();
	
		List<SqlModel> rtnList=new ArrayList<SqlModel>();
		Map<String,Map<String, Object>> curMap=convertMap(pkField,curList);
		Map<String,Map<String, Object>> originMap=convertMap(pkField,originList);
		Set<Entry<String, Map<String, Object>>> curSet= curMap.entrySet();
		//遍历当前数据
		//1.如果当前数据包含原来的数据，那么这个数据进行更新。
		//2.如果当前数据不包含原来的数据，那么添加这个数据。
		FormHandlerDao formHandlerDao=(FormHandlerDao)AppUtil.getBean(FormHandlerDao.class);
		for(Iterator<Entry<String, Map<String, Object>>> it=curSet.iterator();it.hasNext();){
			Entry<String, Map<String, Object>> ent=it.next();
			Map<String, Object> map=ent.getValue();
			//原数据包含当前的数据，则更新。
			//if(originMap.containsKey(ent.getKey())){
			//根据表名和主键值，是否有数据。
			if(formHandlerDao.isHasDataByPk(tableName, ent.getKey())){
				//获取更新的数据model。
				SqlModel updSqlModel=getUpd(bpmFormTable,pkField, map);
				if(updSqlModel!=null){
					updSqlModel.setFormTable(bpmFormTable);
					rtnList.add(updSqlModel);
				}
			}
			else{
				//获取插入的数据model。
				SqlModel model= getInsert(bpmFormTable, ent.getValue());
				
				model.setFormTable(bpmFormTable);
				rtnList.add(model);
			}
		}
		Set<Entry<String, Map<String, Object>>> originSet= originMap.entrySet();
		
		//根据设置是否级联删除，先判断  relDelType:  rel关联表记录删除类型，1-直接删除，0-取消关联。
		Short  relDelType = fkField.getRelDelType();
		if(relDelType!=null && relDelType.shortValue()== 1){// 判断是否要级联删除。
			//遍历原来的数据，当前数据不包含原来的数据，那么需要删除。
			for(Iterator<Entry<String, Map<String, Object>>> it=originSet.iterator();it.hasNext();){
				Entry<String, Map<String, Object>> ent=it.next();
				//当前数据不包含之前的数据，则需要删除
				if(!curMap.containsKey(ent.getKey())){
					String delSql="delete from " + tableName +" where "+pkField+ "=?";
					SqlModel sqlModel=new SqlModel(delSql, new Object[]{ent.getKey()} ,SqlModel.SQLTYPE_DEL);
					sqlModel.setPk(ent.getKey());
					sqlModel.setFormTable(bpmFormTable);
					rtnList.add(sqlModel);
				}
			}
		}else{
			//遍历原来的数据，当前数据不包含原来的数据，那么需要解除关系
			for(Iterator<Entry<String, Map<String, Object>>> it=originSet.iterator();it.hasNext();){
				Entry<String, Map<String, Object>> ent=it.next();
				//当前数据不包含之前的数据，则需要解除关系
				if(!curMap.containsKey(ent.getKey())){
					String delSql="update " + tableName +" set " + fkName+ "=null where "+pkField+ "=?";
					SqlModel sqlModel=new SqlModel(delSql, new Object[]{ent.getKey()} ,SqlModel.SQLTYPE_DEL);
					sqlModel.setPk(ent.getKey());
					sqlModel.setFormTable(bpmFormTable);
					rtnList.add(sqlModel);	
				}
			}
		}
		return rtnList;
	}
	
	/**
	 * 获取更新或者删除的SqlModel列表。
	 * 通过数据库中sub子表记录和网页上传来的子表sub记录，判断是新增记录还是修改记录还是删除记录。
	 * @param tableName
	 * @param curList
	 * @param originList
	 * @return
	 */
	private static List<SqlModel> getUpdDelByList(FormTable bpmFormTable ,List<Map<String, Object>> curList,List<Map<String, Object>> originList){
		String tableName=bpmFormTable.getFactTableName();
		//获取主键field
		String pkField=bpmFormTable.getPkField();
	
		List<SqlModel> rtnList=new ArrayList<SqlModel>();
		Map<String,Map<String, Object>> curMap=convertMap(pkField,curList);
		Map<String,Map<String, Object>> originMap=convertMap(pkField,originList);
		Set<Entry<String, Map<String, Object>>> curSet= curMap.entrySet();
		//遍历当前数据
		//1.如果当前数据包含原来的数据，那么这个数据进行更新。
		//2.如果当前数据不包含原来的数据，那么添加这个数据。
		for(Iterator<Entry<String, Map<String, Object>>> it=curSet.iterator();it.hasNext();){
			Entry<String, Map<String, Object>> ent=it.next();
			Map<String, Object> map=ent.getValue();
			//原数据包含当前的数据，则更新。
			if(originMap.containsKey(ent.getKey())){
				//构造更新的SqlModel
				SqlModel updSqlModel=getUpd(bpmFormTable,pkField, map);
				if(updSqlModel!=null){
					updSqlModel.setFormTable(bpmFormTable);
					rtnList.add(updSqlModel);
				}
			}
			else{
				//构造插入的SqlModel
				SqlModel model= getInsert(bpmFormTable, ent.getValue());
				
				model.setFormTable(bpmFormTable);
				rtnList.add(model);
			}
		}
		//遍历原来的数据，当前数据不包含原来的数据，那么需要删除。
		Set<Entry<String, Map<String, Object>>> originSet= originMap.entrySet();
		for(Iterator<Entry<String, Map<String, Object>>> it=originSet.iterator();it.hasNext();){
			Entry<String, Map<String, Object>> ent=it.next();
			//当前数据不包含之前的数据，则需要删除
			if(!curMap.containsKey(ent.getKey())){
				String delSql="delete from " + tableName +" where "+pkField+ "=?";
				SqlModel sqlModel=new SqlModel(delSql, new Object[]{ent.getKey()} ,SqlModel.SQLTYPE_DEL);
				sqlModel.setPk(ent.getKey());
				sqlModel.setFormTable(bpmFormTable);
				rtnList.add(sqlModel);	
			}
		}
		return rtnList;
	}
	
	/**
	 * 将列表转化为map对象。
	 * <pre>
	 * 主键和一行数据进行关联。
	 * </pre>
	 * @param list
	 * @return
	 */
	private static Map<String,Map<String, Object>> convertMap(String pkField, List<Map<String, Object>> list){
		pkField=pkField.toLowerCase();
		Map<String,Map<String, Object>> rtnMap=new HashMap<String, Map<String,Object>>();
		for(Map<String, Object> map:list){
			if(!map.containsKey(pkField)) {
				continue;
			}
			String value=map.get(pkField).toString();
			rtnMap.put(value, map);
		}
		return rtnMap;
	}
	
	
	/**
	 * 处理主表的数据
	 * @param jsonObj
	 * @param bpmFormData
	 * @throws Exception
	 */
	private static void handleMain(JSONObject jsonObj,FormData bpmFormData) throws Exception{
		//主表 main
		JSONObject mainTable=jsonObj.getJSONObject("main");
		FormTable mainTableDef=bpmFormData.getFormTable();
		String colPrefix=mainTableDef.isExtTable()?"":TableModel.CUSTOMER_COLUMN_PREFIX;
		List<FormField> mainFields=mainTableDef.getFieldList();
		Map<String, FormField> mainFieldTypeMap= convertFieldToMap(mainFields);
		//主表字段
		JSONObject mainFieldJson=mainTable.getJSONObject("fields");
		//将主表JSON转换成map数据。
		Map<String, Object> mainFiledsData=handleRow(mainTableDef, mainFieldTypeMap, mainFieldJson);
		//添加主表数据
		bpmFormData.addMainFields(mainFiledsData);
		
		PkValue pkValue=bpmFormData.getPkValue();
		
		bpmFormData.addMainFields(pkValue.getName().toLowerCase(), pkValue.getValue());
		//只有在添加的时候才进行计算。
		if(pkValue.getIsAdd()){
			//获取需要通过脚本结算的字段。(包括流水号)
			List<FormField> mapFormField=getFieldsFromScript(mainFields);
			//通过脚本引擎计算字段。
			Map<String, Object> map= caculateField(colPrefix,mapFormField,bpmFormData.getMainFields());
			
			bpmFormData.addMainFields(map);
		}
	
		//设置流程变量
		Map<String, Object> variables=getVariables(mainTableDef,mainFieldJson, pkValue);
		bpmFormData.setVariables(variables);
		
	}
	
	/**
	 * 获取流程变量。
	 * @param jsonObject
	 * @param list
	 * @return
	 */
	private static Map<String, Object> getVariables(FormTable mainTable,JSONObject jsonObject,PkValue pkValue){
		Map<String,Object> map=new HashMap<String, Object>();
        FormFieldService formFieldService=AppUtil.getBean(FormFieldService.class);
        List<FormField> fields =formFieldService.getFieldsByTableId(mainTable.getTableId());
        Map<String, Object> querymap = new HashMap<String, Object>();
        JdbcDao jdbcDao = AppUtil.getBean(JdbcDao.class);

        String pk="";
        if(pkValue!=null&&pkValue.getValue()!=null) {
            pk=pkValue.getValue().toString();
        }
		for(FormField field:fields) {
		    if(field.getIsFlowVar() != 1) {
		        continue;
		    }
		    Object value=jsonObject.get(field.getFieldName());
            if(StringUtils.isNotEmpty(pk)&&value==null){
                String sql="";
                if (field.isExecutorSelector())
                {
                    if(field.getIsHidden() == 1) {
                        sql = "SELECT " + field.getFactFiledName() + " as \""
                            + field.getFieldName() + "\" FROM " + mainTable.getFactTableName()
                            + " WHERE ID=:ID";
                    }else {
                        sql = "SELECT " + field.getFactFiledName() + IFormField.FIELD_HIDDEN + " as \""
                            + field.getFieldName() + "\" FROM " + mainTable.getFactTableName()
                            + " WHERE ID=:ID";
                    }
                   querymap.put("ID", pk);
                }
                else
                {
                    sql = "SELECT " + field.getFactFiledName()+ " as \"" + field.getFieldName() + "\" FROM "
                        + mainTable.getFactTableName() + " WHERE ID=:ID";
                    querymap.put("ID", pk);
                }
                Map<String, Object> data = jdbcDao.queryForMap(sql,querymap);
                if(data!=null) {
                    value = data.get(field.getFieldName());
                    if(BeanUtils.isNotEmpty(value)&&!map.containsKey(field.getFieldName())) {
                        map.put(field.getFieldName(), value);
                    }
                }
            }else if(value!=null){
                Object obj= convertType(value.toString(), field);
                if(BeanUtils.isNotEmpty(obj)&&!map.containsKey(field.getFieldName())) {
                    map.put(field.getFieldName(), obj);
                }
            }
            
		}
		return map;
	}

    /** 
    * @Title: getVariables 
    * @Description: TODO(这里用一句话描述这个方法的作用) 
    * @param @param jsonObject
    * @param @param list
    * @param @return     
    * @return Map<String,Object>    返回类型 
    * @throws 
    */
    private static Map<String, Object> getVariables(RelTable relTable)
    {
        JdbcDao jdbcDao = AppUtil.getBean(JdbcDao.class);
        FormFieldService formFieldService = AppUtil.getBean(FormFieldService.class);
        Map<String, Object> querymap = new HashMap<String, Object>();
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> relTabData = relTable.getRelTableDataList();
        List<FormField> relTabFields = formFieldService.getFieldsByTableId(relTable.getRelFormTable().getTableId());
        for (Map<String, Object> realData : relTabData)
        {
            Object pk = realData.get("id");
            for (FormField field : relTabFields)
            {
                Object val = null;
                String key = "";
                if (field.getIsFlowVar() == 1)
                {
                    String sql = "";
                    if (field.isExecutorSelector())
                    {
                        if(field.getIsHidden() == 1) {
                            sql = "SELECT " + field.getFactFiledName() + " as \""
                                + field.getFieldName() + "\" FROM " + relTable.getRelFormTable().getFactTableName()
                                + " WHERE ID=:ID";
                            key = field.getFieldName();

                        }else {
                            sql = "SELECT " + field.getFactFiledName() + IFormField.FIELD_HIDDEN + " as \""
                                + field.getFieldName() + "\" FROM " + relTable.getRelFormTable().getFactTableName()
                                + " WHERE ID=:ID";
                            key = field.getFieldName().toLowerCase() + IFormField.FIELD_HIDDEN;

                        }
                        querymap.put("ID", pk);
                        if (!realData.containsKey(field.getFactFiledName().toLowerCase() + "ID"))
                        {
                            val = realData.get(field.getFactFiledName().toLowerCase() + "id");
                        }
                        else
                        {
                            val = realData.get(field.getFactFiledName().toLowerCase() + "ID");
                        }
                    }
                    else
                    {
                        sql = "SELECT " + field.getFactFiledName() + " as \"" + field.getFieldName() + "\" FROM "
                            + relTable.getRelFormTable().getFactTableName() + " WHERE ID=:ID";
                        querymap.put("ID", pk);
                        key = field.getFieldName().toLowerCase();
                        val = realData.get(field.getFactFiledName().toLowerCase());
                    }
                    
                    if (val == null && pk != null)
                    {
                        // 从数据库中取值
                        Map<String, Object> data = jdbcDao.queryForMap(sql, querymap);
                        if (data != null)
                        {
                            val = data.get(field.getFieldName());
                        }
                        
                    }
                    
                    if (!map.containsKey(pk + "." + key) && val != null)
                    {
                        map.put(pk + "." + key, val);
                    }
                }
                
            }
        }
        
        return map;
    }
	   
    
	/**
	 * 处理子表数据。
	 * @param jsonObj
	 * @param subTableMap
	 * @param bmpFormFieldDao
	 * @param mainTableDef
	 * @param bpmFormData
	 * @param pkValue
	 * @throws Exception 
	 */
	private static void handSub(JSONObject jsonObj,FormData bpmFormData) throws Exception{
		FormTable mainTable=bpmFormData.getFormTable();
		List<FormTable> listTable=mainTable.getSubTableList();
		//将表名消息并作为键和表对象进行关联。
		Map<String,FormTable> formTableMap= convertTableMap(listTable);
		
		boolean isExternal=mainTable.isExtTable();
	
		String colPrefix=isExternal?"" : TableModel.CUSTOMER_COLUMN_PREFIX;
	
		//子表
		JSONArray arySub=jsonObj.getJSONArray("sub");
		//子表
		for(int i=0;i<arySub.size();i++){
			SubTable subTable=new SubTable();
			JSONObject subTableObj=arySub.getJSONObject(i);
			String tableName=subTableObj.getString("tableName").toLowerCase();
			//根据子表名称获取子表对象
			FormTable subFormTable=formTableMap.get(tableName);
			//获取子表的列元数据。
			List<FormField> subTableFields=subFormTable.getFieldList();
			//将子表的"字段名称"作为键，"字段对象"作为值放到map对象当中。
			Map<String,FormField> subTableTypeMap= convertFieldToMap(subTableFields);
			//获取需要计算的脚本数据。
			List<FormField> scriptFields=getFieldsFromScript(subTableFields);
			//设置子表名称
			subTable.setTableName(tableName);
			//设置子表的主键和外键名称。
			String fkName = "";
			if(isExternal){
				String pk=subFormTable.getPkField();
				fkName = subFormTable.getRelation();
				subTable.setPkName(pk);
				subTable.setFkName(fkName);
			}
			else{
				fkName = TableModel.FK_COLUMN_NAME;
				subTable.setPkName(TableModel.PK_COLUMN_NAME);
				subTable.setFkName(fkName);
			}
			JSONArray arySubFields=subTableObj.getJSONArray("fields");
			for(int j=0;j<arySubFields.size();j++){
				JSONObject subFieldObj= arySubFields.getJSONObject(j);
				boolean isEmpty = subFieldObj.isEmpty();//判断是否为空，如为空，则返回。
				if(isEmpty){continue;}
				//将json转换为Map对象。
				Map<String, Object> subRow = handleRow(subFormTable,subTableTypeMap, subFieldObj);
				//计算脚本字段。
				Map<String, Object> map= caculateField(colPrefix,scriptFields,subRow);
				
				subRow.putAll(map);
				//处理主键数据
				handFkRow(subFormTable,subRow,bpmFormData.getPkValue(), fkName);
				//处理需要计算的数据。
				subTable.addRow(subRow);
			}
			bpmFormData.addSubTable(subTable);
		}
	}
	
	
	/**
	 * 处理意见数据。
	 * @param bpmFormData
	 * @param jsonObj
	 */
	private static void handOpinion(FormData bpmFormData,  JSONObject jsonObj){
		//意见
		JSONArray aryOpinion=jsonObj.getJSONArray("opinion");
		//意见 opinion
		for(int i=0;i<aryOpinion.size();i++){
			JSONObject opinion=aryOpinion.getJSONObject(i);
			String formName=opinion.getString("name");
			String value=opinion.getString("value");
			bpmFormData.addOpinion(formName, value);
		}
	}
	
	/**
	 * 处理子表行数据的主键和外键。
	 * <pre>
	 * 添加子表的主键和外键。
	 * </pre>
	 * @param mainTabDef		主表定义。
	 * @param bpmFormTable		子表定义。
	 * @param rowData			子表一行数据。
	 * @param pkValue			主键数据。
	 * @throws Exception
	 */
	public static void handFkRow(FormTable bpmFormTable, Map<String, Object> rowData,PkValue pkValue, String fkColumnName) throws Exception{
		boolean isExternal=bpmFormTable.isExtTable();
		//外部表数据
		if(isExternal){//如下代码在外键关系表中没有经过测试。
			String pkField= bpmFormTable.getPkField().toLowerCase();
			if(!rowData.containsKey(pkField)){
				PkValue pk=generatePk(bpmFormTable);
				rowData.put(pk.getName(), pk.getValue());
			}
			else{
				Object obj=rowData.get(pkField);
				if(obj==null || "".equals(obj.toString().trim())){
					PkValue pk=generatePk(bpmFormTable);
					rowData.put(pk.getName(), pk.getValue());
				}
			}
			String fk=bpmFormTable.getRelation();
			rowData.put(fk, pkValue.getValue());
		}
		//本地表数据
		else{
			String pkField= bpmFormTable.getPkField().toLowerCase();
			//没有包含主键则添加一个。
			if(!rowData.containsKey(pkField)){
				Long pk=UniqueIdUtil.genId();
				rowData.put(TableModel.PK_COLUMN_NAME.toLowerCase() , pk);
			}
			
			//rowData.put(TableModel.FK_COLUMN_NAME.toLowerCase(), pkValue.getValue());
			rowData.put(fkColumnName, pkValue.getValue());
		}
	
	}
	
	/**
	 * 取得值需要结算的字段。
	 * @param list
	 * @return
	 */
	private static List<FormField> getFieldsFromScript(List<FormField> list){
		List<FormField> map=new ArrayList<FormField>();
		for(FormField field:list){
			//通过后台运算
			if(field.getValueFrom()==FormField.VALUE_FROM_SCRIPT_HIDDEN
				||field.getValueFrom()==FormField.VALUE_FROM_IDENTITY)
				map.add(field);
		}
		return map;
	}
	
	
	
	/**
	 * 计算值从脚本或者从流水号计算出来的值。
	 * @param colPrefix		列前缀。
	 * @param fields		子表字段。
	 * @param data			子表的一行数据。
	 * @return
	 */
	private static Map<String, Object> caculateField(String colPrefix,  List<FormField> fields,Map<String,Object> rowData){
		
		Map<String, Object> result=new HashMap<String, Object>();
		for(FormField field:fields){
			//实际字段名称
			String name=colPrefix + field.getFieldName();
			if(field.getValueFrom()==FormField.VALUE_FROM_SCRIPT_HIDDEN){				
				//获取字段脚本。
				String script=field.getScript();
				Object value= FormUtil.calcuteField(script, rowData,colPrefix);
				result.put(name.toLowerCase(), value);
			}else if(field.getValueFrom()==FormField.VALUE_FROM_IDENTITY){//判断是否为流水号，
			    //取得是否为界面显示
			    String prop = field.getCtlProperty();
			    //判断是否wie空
			    if (!StringUtil.isEmpty(prop)) {
			       //格式化字符串
			       JSONObject jsonObject = JSONObject.fromObject(prop);
			       	//判断是否存在isShowidentity值
			       if (jsonObject.containsKey("isShowidentity")) {
			    	   //如果存在则取得isShowidentity值
			           String isShowidentity = jsonObject.getString("isShowidentity");
			           //判断是否不显示
			           if ("0".equals(isShowidentity)) {
			        	   //如果不显示的话，则取得序列号下一个值
			        	  try {
							Object value = FormUtil.getKey(2, field.getSerialNumber());
							result.put(name.toLowerCase(), value);
						} catch (Exception e) { 
							e.printStackTrace();
						}
			          }
			       }
			   }
			}
		}
		return result;
	}

	
	/**
	 * 直接产生新的主键。
	 * @param bpmFormTable
	 * @return
	 * @throws Exception
	 */
	public static PkValue generatePk(FormTable bpmFormTable) throws Exception{
		Object pkValue=null;
		String pkField=bpmFormTable.getPkField().toLowerCase();
		
		//外部表根据规则创建主键。
		if(bpmFormTable.isExtTable()){
			Short keyType=bpmFormTable.getKeyType();
			String keyValue=bpmFormTable.getKeyValue();
			pkValue=FormUtil.getKey(keyType, keyValue);
		}
		else{
			pkValue=UniqueIdUtil.genId();
		}
	
		PkValue pk=new PkValue();
		pk.setIsAdd(true);
		pk.setName(pkField);
		pk.setValue(pkValue);
		return pk;
	}

	/**
	 * 将json转换为Map对象。
	 * @param colPrefix			列前缀。
	 * @param subTableTypeMap	字段和类型映射。
	 * @param subFieldObj		json对象。
	 * @return
	 */
	private static Map<String, Object> handleRow(FormTable bpmFormTable,Map<String, FormField> fieldTypeMap, JSONObject fieldsJsonObj) {
		boolean isExternal=bpmFormTable.isExtTable();
		int keyType=  bpmFormTable.getKeyDataType();
		String colPrefix=(isExternal?"":TableModel.CUSTOMER_COLUMN_PREFIX).toLowerCase();
		String pkField=bpmFormTable.getPkField();
		Map<String, Object> row=new HashMap<String, Object>();
	
		//对字段名称进行遍历
		for(Iterator<String> it=fieldsJsonObj.keys();it.hasNext();){
			String key=it.next();
			Object obj=fieldsJsonObj.get(key);
			String value="";
			if(obj ==JSONNull.getInstance()){
				value="";
			}
			else if(obj instanceof JSONArray || obj instanceof JSONObject) {
				value=obj.toString();
			}
			else{
				value=CommonTools.Obj2String(obj);
			}
			if(pkField.equalsIgnoreCase(key) && StringUtil.isNotEmpty(value)){
				if(keyType==0){
				   List<Long> vaList=StringUtil.getListByStr(value);
				   if(vaList!=null&&vaList.size()==1) {
				      row.put(pkField.toLowerCase(), vaList.get(0));
				   }
				}
				else{
					row.put(pkField.toLowerCase(), value);
				}
				
			}
		
			FormField bpmFormField=fieldTypeMap.get(key.toLowerCase());
			if(bpmFormField==null) continue;
			//转换数据类型和数据字典类型数据
			Object convertValue=convertType(value, bpmFormField);
			String fieldName=key.toLowerCase();
			if(!isExternal && !fieldName.equalsIgnoreCase(TableModel.PK_COLUMN_NAME)){
				fieldName=(colPrefix +key).toLowerCase();
			}
			row.put(fieldName, convertValue);
		}
		return row;
	}
	
	/**
	 * 转换数据类型和数据字典类型数据。
	 * @param strValue
	 * @param type
	 * @return
	 */
	public static Object convertType(String strValue, FormField formField){
		
		String type=formField.getFieldType();
		if(StringUtil.isEmpty(strValue)) return null;
		Object value = strValue;
		//数据字典的数据类型都是varchar类型，dicType不为空
		String dicType = formField.getDictType();
		if(StringUtil.isNotEmpty(dicType)){
			value = getDicValueByName(value,dicType);
		}
		if (ColumnModel.COLUMNTYPE_DATE.equals(type)){
			value = DateUtil.parseDate((String) strValue);
			if(null==value&&strValue.length()>=21){
				value =  DateUtil.parseDate(strValue.substring(0,strValue.length()-2));
			}
		}
		else if(ColumnModel.COLUMNTYPE_NUMBER.equals(type)) {
			String json = formField.getCtlProperty();
			JSONObject jsonObj = JSONObject.fromObject(json);
			//替换货币符号
			if(BeanUtils.isNotEmpty(jsonObj.get("coinValue"))){
				strValue = StringUtil.trimPrefix(strValue, String.valueOf(jsonObj.get("coinValue")));
			}
			//替换千分位(1:true;0:false)
			if("1".equals(String.valueOf(jsonObj.get("isShowComdify")))){
				strValue = strValue.replaceAll(",", "");
			}
			//属于小数类型。
			if(formField.getDecimalLen()>0){
				value=Double.parseDouble(strValue);
			}
			//整数型的处理。
			else{
				//判断是否包含小数点
				if(strValue.contains("."))
					strValue = StringUtils.substringBefore(strValue, ".");
				
				if(formField.getIntLen()<=10){
					value=Integer.parseInt(strValue);
				}
				else{
					value=Long.parseLong(strValue);
				}
			}
		} 
		
		return value;
	}
	
	
	/**
	 * 将字段列表转成字段map。
	 * @param list
	 * @return
	 */
	public static Map<String, FormField> convertFieldToMap(List<FormField>  list){
		Map<String, FormField> map=new HashMap<String, FormField>();
		for(Iterator<FormField> it=list.iterator();it.hasNext();){
			FormField field=it.next();
			map.put(field.getFieldName().toLowerCase(), field);
		}
		return map;
	}

	
	
	
	/**
	 * 将列表定义转换成Map对象。
	 * @param list
	 * @return
	 */
	private static Map<String, FormTable> convertTableMap(List<FormTable> list){
		Map<String,FormTable> map=new HashMap<String, FormTable>();
		for(FormTable tb:list){
			map.put(tb.getTableName().toLowerCase(), tb);
		}
		return map;
	}
	
	/**
	 * 取得插入的数据的sqlmodel对象。
	 * @param tableName
	 * @param mapData
	 * @return
	 */
	private static SqlModel getInsert(FormTable formTable,Map<String,Object> mapData){
		//获取 数据库真正的表名 W_
		String tableName=formTable.getFactTableName();
		//获取主键field
		String pkField=formTable.getPkField().toLowerCase();
		
		StringBuffer fieldNames = new StringBuffer();
		StringBuffer params = new StringBuffer();
		final List<Object> values = new ArrayList<Object>();
		//构建插入sql语句的三个部分：列名、参数、列值。
		for (Map.Entry<String, Object> entry : mapData.entrySet()){
			fieldNames.append(entry.getKey()).append(",");
			params.append("?,");
			//values.add(entry.getValue());
			values.add(getFiledValue(formTable,entry));
			
		}
		StringBuffer sql = new StringBuffer();
		//构建插入sql语句
		sql.append(" INSERT INTO ");
		sql.append(tableName);
		sql.append("(");
		sql.append(fieldNames.substring(0, fieldNames.length() - 1));
		sql.append(")");
		sql.append(" VALUES (");
		sql.append(params.substring(0, params.length() - 1));
		sql.append(")");
	
		//构建sqlmodel
		SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_INSERT);
		//获取map中的主键field
		Object obj=mapData.get(pkField);
		if(BeanUtils.isEmpty(obj)){
			obj=mapData.get(pkField.toUpperCase());
		}
		//赋值Pk主键值
		sqlModel.setPk(obj.toString());
		//赋值formTable
		sqlModel.setFormTable(formTable);
		
		return sqlModel;
	}
	
	/**
	 * 获取更新的数据model。
	 * @param tableName
	 * @param mapData
	 * @return
	 */
	private static SqlModel getUpd(FormTable formTable,String pkField, Map<String,Object> mapData){
		final List<Object> values = new ArrayList<Object>();
		
		pkField=pkField.toLowerCase();
	
		
		String pkValue=mapData.get(pkField).toString();
		
		StringBuffer set = new StringBuffer();
		//构建插入sql语句的2个部分：列名set、列值values。
		for (Map.Entry<String, Object> entry : mapData.entrySet()){
			if(!pkField.equals(entry.getKey())){
				set.append(entry.getKey()).append("=?,");
				//values.add(entry.getValue());
				values.add(getFiledValue(formTable,entry));
			}
		}
		if(values.size()==0) return null;
		//构建插入sql语句
		StringBuffer sql = new StringBuffer();
		
		sql.append(" update ");
		sql.append(formTable.getFactTableName());
		sql.append(" set ");
		sql.append(set.substring(0, set.length() - 1));
		sql.append(" where ");
		sql.append(pkField);
		sql.append("=?");
		values.add(pkValue);
		//构建sqlmodel
		SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_UPDATE);
		
		sqlModel.setPk(pkValue);
					
		return sqlModel;
	}
	
	
	/**
	 * 获取主表的更新的sqlmodel对象。
	 * <pre>
	 * 只是更新客户端提交的json数据，如果表单中没有提交任何数据则不更新。
	 * </pre>
	 * @param bpmFormData
	 * @return
	 */
	private static SqlModel getUpdate(FormData bpmFormData){
		PkValue pk=bpmFormData.getPkValue();
		String tableName=bpmFormData.getFormTable().getFactTableName();
		Map<String,Object> mapData=bpmFormData.getMainCommonFields();
		StringBuffer set = new StringBuffer();
		//构建插入sql语句的2个部分：列名set、列值values。
		List<Object> values = new ArrayList<Object>();
		for (Map.Entry<String, Object> entry : mapData.entrySet()){
			set.append(entry.getKey()).append("=?,");
			//values.add(entry.getValue());
			values.add(getFiledValue(bpmFormData.getFormTable(),entry));
		}
		if(values.size()==0) return null;
		//构建插入sql语句
		StringBuffer sql = new StringBuffer();
		if(set.length() > 0) {
			// sql
			sql.append(" update ");
			sql.append(tableName);
			sql.append(" set ");
			sql.append(set.substring(0, set.length() - 1));
			sql.append(" where ");
			sql.append(pk.getName() );
			sql.append("=?");
			values.add(pk.getValue());
		}
		//构建sqlmodel
		SqlModel sqlModel=new SqlModel(sql.toString(), values.toArray(),SqlModel.SQLTYPE_UPDATE);
		return sqlModel;
	}
	
	/**
	 * 将数据
	 * <pre>
	 * {
	 *		main: {
	 *			fields:{"字段1":"1","字段2":"2"}
	 *		},
	 *		sub: [
	 *			{
	 *				tableName: 'TB',
	 *				fields: [
	 *					{"字段1":"1","字段2":"2"},
	 *					{"字段1":"3","字段2":"4"}
	 *				]
	 *			},
	 *			{
	 *				tableName: 'TB',
	 *				fields: [
	 *					{"字段1":"1","字段2":"2"},
	 *					{"字段1":"3","字段2":"4"}
	 *				]
	 *			},
	 *		],
	 *		opinion: [
	 *			{name:"意见表单名1",value:"意见"},
	 *			{name:"意见表单名1",value:"意见"}
	 *		]
	 *	}
     *</pre>
	 * @param json
	 * @param mainTableDef
	 * @return
	 * @throws Exception
	 */
	public static  FormData parseJson(String json,FormTable mainTableDef) throws Exception{
		return parseJson( json,null, mainTableDef);
	}

	/**
	 * 将json数据解析为FormData。
	 * @param json
	 * @param pkValue
	 * @return
	 * @throws Exception
	 */
	public static  FormData parseJson(String json,PkValue pkValue,FormTable mainTableDef) throws Exception{
		JSONObject jsonObj= JSONObject.fromObject(json);
		FormData bpmFormData=new FormData(mainTableDef);
		if(pkValue==null)
			pkValue=generatePk(mainTableDef);
		bpmFormData.setPkValue(pkValue);
		//处理主表
		handleMain(jsonObj,bpmFormData);
		//子表
		handSub(jsonObj,bpmFormData);
		//关系表
		handRef(jsonObj,bpmFormData);
		//意见
		handOpinion(bpmFormData, jsonObj);
		
		
		return bpmFormData;
	}
	
	/**
	 * 处理关系表数据。
	 * @param jsonObj
	 * @param subTableMap
	 * @param bmpFormFieldDao
	 * @param mainTableDef
	 * @param bpmFormData
	 * @param pkValue
	 * @throws Exception 
	 */
	private static void handRef(JSONObject jsonObj,FormData bpmFormData) throws Exception{
		FormTable mainTable=bpmFormData.getFormTable();
		List<FormTable> relTableList=mainTable.getRelTableList();
		if(relTableList==null || relTableList.size()==0){
			return;
		}
		//将表名消息并作为键和表对象进行关联。
		Map<String,FormTable> refFormTableMap= convertTableMap(relTableList);
		
		//是否外部表
		boolean isExternal=mainTable.isExtTable();
	
		String colPrefix=isExternal?"" : TableModel.CUSTOMER_COLUMN_PREFIX;
	
		//获取从网页传过来的外鍵表的数据json串
		JSONArray aryRef=jsonObj.getJSONArray(Constants.FormDataType_Ref);
		//关系表
		for(int i=0;i<aryRef.size();i++){
			RelTable relTable=new RelTable();
			JSONObject relTableObj=aryRef.getJSONObject(i);
			JSONArray relFieldJson=relTableObj.getJSONArray("fields");

			String relTableName=relTableObj.getString("tableName").toLowerCase();
			//根据关系表名称获取关系表对象
			FormTable relFormTable=refFormTableMap.get(relTableName);
			//获取关系表的列元数据。
			List<FormField> relTableFields=relFormTable.getFieldList();
			//将关系表的字段名称作为键，字段对象作为值放到map对象当中。
			Map<String,FormField> relTableTypeMap= convertFieldToMap(relTableFields);
			//获取需要计算的脚本数据。
			List<FormField> scriptFields=getFieldsFromScript(relTableFields);
			//根据主表和关联表构建RelTable
			relTable = FormDataUtil.getRelTableByMainTableAndRelTable(mainTable, relFormTable);
			
			if(isExternal){//TODO 如下代码在外键关系表中没有经过测试。
				String pk=relFormTable.getPkField();
				String fkName = relFormTable.getRelation();
				//关联表  主键名称。
				relTable.setRelTablePkName(pk);
				//主表   外键名称。
				relTable.setMainTableFkName(fkName);
				//TODO 主表   关系列。
				relTable.setMainTableRelField(null);
			}
			
			JSONArray aryRefFields=relTableObj.getJSONArray("fields");
			for(int j=0;j<aryRefFields.size();j++){
				JSONObject refFieldObj= aryRefFields.getJSONObject(j);
				
				Map<String, Object> refRow = handleRow(relFormTable,relTableTypeMap, refFieldObj);
				//计算脚本字段。
				Map<String, Object> map= caculateField(colPrefix,scriptFields,refRow);
				
				refRow.putAll(map);
				//获取主表的外键列
				String fkName = relTable.getMainTableFkName();
				//处理主键数据
				handFkRow(relFormTable,refRow,bpmFormData.getPkValue(), fkName);
				//处理需要计算的数据。
				relTable.addRow(refRow);
			}
			
            bpmFormData.addRelTable(relTable);
            if (relFieldJson.size() > 0)
            {
                Map<String, Object> variables = getVariables(relTable);
                bpmFormData.addVariables(variables);
                
            }
			
		}
	      

	}
	
	public static Map<String,Object> convertToTableData(List<FormField> fieldList,Map<String,Object> mainFields){
		Map<String,Object> res = new HashMap<String,Object>();
		for(FormField f:fieldList){
			if(!f.getFieldName().equalsIgnoreCase(TableModel.PK_COLUMN_NAME)
					&&f.getFieldName().indexOf(TableModel.CUSTOMER_COLUMN_PREFIX)!=0){
				res.put(TableModel.CUSTOMER_COLUMN_PREFIX+f.getFieldName().toUpperCase(), mainFields.get(f.getFieldName())); 
			}
		}   
	 
		return res;
	}
	/**
	 * 获取外键关系field信息。
	 * @param mainTable
	 * @param relTable
	 */
	public static FormField getFKFieldByMainAndRelTable(FormTable mainTable,List<FormField> relfieldList){
		FormField fkField = new FormField();
		
		Long mainTableId = mainTable.getTableId();
		//循环判断获取正确的 外键列。
		for(FormField formField:relfieldList){
			Short controlType = formField.getControlType();
			Long relTableId = formField.getRelTableId();
			if(controlType == IFieldPool.RELATION_COLUMN_CONTROL && relTableId != null && relTableId.longValue() == mainTableId.longValue() ){
				fkField = formField;
				break;
			}
		}
		return fkField;
	}
	/**
	 * 根据主表和关联表构建RelTable
	 * 
	 * @param mainFormTableDef
	 *           主表
	 * @param refFormTable
	 *           关联表
	 * @return
	 */
	public static RelTable getRelTableByMainTableAndRelTable(FormTable mainFormTableDef,FormTable relFormTable){
		RelTable relTable = new RelTable();
		// 获取从表rel的主键列名  
		List<FormField> relTableFields=relFormTable.getFieldList();
		//获取外键field
		FormField fkField = FormDataUtil.getFKFieldByMainAndRelTable(mainFormTableDef,relTableFields);
		//获取数据库中的外键列名，含F_
		String fkName = TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase()+fkField.getFieldName();
		//构造relTable对象数据，用于在freemarker中替换并显示。
		relTable.setRelFormTable(relFormTable);
		relTable.setRelTableName(relFormTable.getTableName().toLowerCase());
		relTable.setRelTablePkName(fkName);
		relTable.setRelTableFieldList(relTableFields);

		relTable.setMainTable(mainFormTableDef);
		relTable.setMainTableRelField(fkField);
		relTable.setMainTableFkName(fkName);
		
		//获取显示列列表
		Map relTablePKColumnMap = new HashMap();
		for(FormField formField :relTableFields){
			Short isPkShow =formField.getIsPkShow();
			if(isPkShow.shortValue() == 1){
				String fieldName = TableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase()+formField.getFieldName();
				relTablePKColumnMap.put(fieldName.toLowerCase(), formField);
			}
		}
		//当没有设置主键显示列，默认显示id列。
		if(relTablePKColumnMap.size() == 0){
			relTablePKColumnMap.put(TableModel.PK_COLUMN_NAME.toLowerCase(), TableModel.PK_COLUMN_NAME.toLowerCase());
		}
		relTable.setRelTablePKColumn(relTablePKColumnMap);
		
		return relTable;
	}

	/**
	 * 外键显示列构造。
	 * 
	 * @param mainTable
	 *           主表
	 * @return
	 */
		public static FormTable appendFKShowColumn(FormTable mainTable){
			if(mainTable!=null){
				//处理列
				List<FormField> formFieldList = mainTable.getFieldList();
				List<FormField> formFieldIncShowColumnList = dealFKShowColumn(formFieldList);
				mainTable.setFieldList(formFieldIncShowColumnList);
				//处理子表的列  
				List<FormTable> subTableList = mainTable.getSubTableList();
				for(FormTable subFormTable:subTableList){
					List<FormField> subFormFieldList = subFormTable.getFieldList();
					List<FormField> subFormFieldIncShowColumnList = dealFKShowColumn(subFormFieldList);
					subFormTable.setFieldList(subFormFieldIncShowColumnList);
				}
				//处理关联表的列
				List<FormTable> relTableList = mainTable.getRelTableList();
				for(FormTable relFormTable:relTableList){
					List<FormField> relFormFieldList = relFormTable.getFieldList();
					List<FormField> relFormFieldIncShowColumnList = dealFKShowColumn(relFormFieldList);
					relFormTable.setFieldList(relFormFieldIncShowColumnList);
				}
				
			}
			return mainTable;
		}
		/**
		 * 外键显示列构造。
		 * 
		 * @param formFieldList
		 *           列
		 * @return
		 */
		public static List<FormField> dealFKShowColumn(List<FormField> formFieldList){
			List<FormField> returnFormFieldList = new ArrayList();
			for(FormField formField:formFieldList){
				returnFormFieldList.add(formField);
				
				FormField fkShowFormField = new FormField();
				Short controlType = formField.getControlType();
				if(controlType.shortValue()== IFieldPool.RELATION_COLUMN_CONTROL){
					fkShowFormField = (FormField)formField.clone();
					fkShowFormField.setFieldName(fkShowFormField.getFieldName()+IFieldPool.FK_SHOWAppCode);
					fkShowFormField.setFieldDesc(fkShowFormField.getFieldDesc()+IFieldPool.FK_SHOWAppNAME);
					returnFormFieldList.add(fkShowFormField);
				}
				
			}
			return returnFormFieldList;
		}
		
		
    /** 
    * @Title: getFiledValue 
    * @Description: TODO(判断字段是否需要 加密) 
    * @param @param formTable
    * @param @param entry
    * @param @return    设定文件 
    * @return Object    返回类型 
    * @throws 
    */
    private static Object getFiledValue(FormTable formTable, Entry<String, Object> entry)
    {
        Long tableId=formTable.getTableId();
        String filedName=entry.getKey();
        Object filedValue=entry.getValue();
        try{
            IFormFieldService formFieldService=AppUtil.getBean(IFormFieldService.class);
            FiledEncryptFactory filedEncryptFactory=AppUtil.getBean(FiledEncryptFactory.class);
            // 根据form table 来判断是否需要加密 处理
            filedName=filedName.replace(ITableModel.CUSTOMER_COLUMN_PREFIX, "").replace(ITableModel.CUSTOMER_COLUMN_PREFIX.toLowerCase(),"");
            IFormField field=formFieldService.getFieldByTidFna(tableId, filedName.replace(ITableModel.CUSTOMER_COLUMN_PREFIX, ""));
            IFiledEncrypt filedEncrypt=filedEncryptFactory.creatEncrypt(field.getEncrypt());
            if(filedEncrypt!=null){
                return filedEncrypt.encrypt(filedValue);
            }else{
                return filedValue;
            }
        }catch(Exception e){
            return filedValue;
        }
    }
    
    /**
     * 通过数据字典的value值获取name
     * @date 2017年9月26日下午6:06:52
     * @param value
     * @param dicType
     * @return
     */
    public static Object getDicValueByName(Object name,String dicType){
    	IDictionaryService dicService=AppUtil.getBean(IDictionaryService.class);
		List<? extends IDictionary> dicList = dicService.getByNodeKey(dicType);
		for(IDictionary dic:dicList){
			if(name.equals(dic.getItemName())){
				name = dic.getItemValue();
				return name;
			}
		}
		return "";
    }
    /**
     * 通过数据字典的name值获取value
     * @date 2017年9月26日下午6:07:52
     * @param name
     * @param dicType
     * @return
     */
    public static String getDicNameByValue(Object value,String dicType){
    	IDictionaryService dicService=AppUtil.getBean(IDictionaryService.class);
		List<? extends IDictionary> dicList = dicService.getByNodeKey(dicType);
		for(IDictionary dic:dicList){
			if(value.equals(dic.getItemValue())){
				value = dic.getItemName();
				return value.toString();
			}
		}
		return "";
    }
}
