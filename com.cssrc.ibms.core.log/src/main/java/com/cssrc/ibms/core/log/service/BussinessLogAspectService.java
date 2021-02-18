package com.cssrc.ibms.core.log.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IRelTable;
import com.cssrc.ibms.api.form.model.ISubTable;
import com.cssrc.ibms.core.log.intf.AbsLogAspect;
import com.cssrc.ibms.core.log.intf.ILogAspect;
import com.cssrc.ibms.core.log.model.FieldNVD;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 *@author vector
 *@date 2017年9月20日 下午4:44:43
 *@Description 业务数据日志处理
 */
@Service
public class BussinessLogAspectService extends AbsLogAspect implements ILogAspect {
	
	@Override
	public String checkData(List<Map<String, Object>> oldDatas,List<Map<String, Object>> newDatas,DataNote methodDataNote ,HttpServletRequest request) {
		StringBuffer detailSb=new StringBuffer();
		if(oldDatas!=null&&newDatas!=null&&oldDatas.size()>0&&newDatas.size()>0){
			IFormData oldData=(IFormData) oldDatas.get(0).get("data");
			IFormData newData=(IFormData) newDatas.get(0).get("data");
			IFormTable mainTable=oldData.getFormTable();
			
			//主表数据对比
			detailSb.append(checkTableFieldData(oldData.getMainFields(),newData.getMainFields(),mainTable));
			//子表数据对比
			detailSb.append(checkSubTableData(oldData.getSubTableList(),newData.getSubTableList(),convertTableMap(mainTable.getSubTableList())));
			//关联表数据对比
			detailSb.append(checkRelTableData(oldData.getRelTableList(),newData.getRelTableList()));
			
		}
		
		return detailSb.toString();
	}
	/**
	 * 比较所有的关联表数据
	 * @param oldSubTableList
	 * @param newSubTableList
	 * @param map 关联表信息
	 * @return
	 */
	private String checkRelTableData(List<? extends IRelTable> oldRelTableList,List<? extends IRelTable> newRelTableList) {
		//转换成固定格式
		Map<String,Map<String,FieldNVD>> oldRelTableMap=convertRelTableToMapedFieldNVD(oldRelTableList);
		Map<String,Map<String,FieldNVD>> newRelTableMap=convertRelTableToMapedFieldNVD(newRelTableList);
		return checkOtherData(oldRelTableMap,newRelTableMap);
	}
	
	/**
	 * 将每个关联表记录转换成 Map key=主键:主键值 value= Map<属性名，属性名：值：描述>
	 * @param relTableList 关联表【包含数据】
	 * @param map 关联表【包括字段描述】
	 * @return 
	 */
	private Map<String, Map<String, FieldNVD>> convertRelTableToMapedFieldNVD(List<? extends IRelTable> relTableList) {
		Map<String, Map<String, FieldNVD>> relTables=new HashMap<String, Map<String,FieldNVD>>();
		Map<String,FieldNVD> field;
		for(IRelTable relTable:relTableList){
			Map<String,IFormField> mapFields=getMapFields(relTable.getRelFormTable());//转换一个表的字段定义
			for(Map<String,Object> data:relTable.getRelTableDataList()){//获取一个实例
				field=new HashMap<String, FieldNVD>();
				for(String name:data.keySet()){//针对一个实例进行判断
					if(mapFields.containsKey(name)){
						FieldNVD f=new FieldNVD();
						f.setName(name);
						f.setValue(data.get(name));
						f.setDesc(mapFields.get(name).getFieldDesc());
						field.put(name, f);
					}
				}
				relTables.put(data.get(relTable.getRelFormTable().getPkField().toLowerCase()).toString(), field);
			}
		}
		return relTables;
	}
	/**
	 * 将每个子表记录转换成 Map   key=主键:主键值  value= Map<属性名，属性名：值：描述>
	 * @param subTableList
	 * @param map 
	 * @return
	 */
	private Map<String, Map<String, FieldNVD>> convertToMapedFieldNVD(List<? extends ISubTable> subTableList, Map<String, IFormTable> map) {
		Map<String,Map<String,FieldNVD>> subTableMap=new HashMap<String, Map<String,FieldNVD>>();
		Map<String,FieldNVD> field;
		for(int i=0;i<subTableList.size();i++){//多个子表
			ISubTable subTable=subTableList.get(i);//获取其中的一个子表 
			Map<String,IFormField> mapFields=getMapFields(map.get(subTable.getTableName().toLowerCase()));
			List<Map<String, Object>>  dataList=  subTable.getDataList();//获取一个子表的数据【包括多个实例】
			for(Map<String, Object> data:dataList){//对一个子表的多个实例遍历
				field=new HashMap<String, FieldNVD>();
				for(String name:data.keySet()){//针对一个实例进行判断
					if(mapFields.containsKey(name)){
						FieldNVD f=new FieldNVD();
						f.setName(name);
						f.setValue(data.get(name));
						f.setDesc(mapFields.get(name).getFieldDesc());
						field.put(name, f);
					}
				}
				subTableMap.put(data.get(subTable.getPkName().toLowerCase()).toString(), field);
			}
		}
		return subTableMap;
	}
	/**
	 * 将列表定义转换成Map对象。
	 * @param list
	 * @return
	 */
	private static Map<String, IFormTable> convertTableMap(List<? extends IFormTable> list){
		Map<String,IFormTable> map=new HashMap<String, IFormTable>();
		for(IFormTable tb:list){
			map.put(tb.getTableName().toLowerCase(), tb);
		}
		return map;
	}
	/**
	 * 比较所有的子表数据
	 * @param oldSubTableList
	 * @param newSubTableList
	 * @param map
	 * @return
	 */
	private String checkSubTableData(List<? extends ISubTable> oldSubTableList,List<? extends ISubTable> newSubTableList, Map<String, IFormTable> map) {
		
		//转换成固定格式
		Map<String,Map<String,FieldNVD>> oldSubTableMap=convertToMapedFieldNVD(oldSubTableList,map);
		Map<String,Map<String,FieldNVD>> newSubTableMap=convertToMapedFieldNVD(newSubTableList,map);
		return checkOtherData(oldSubTableMap,newSubTableMap);
	}
	private String checkOtherData(Map<String, Map<String, FieldNVD>> oldTableMap, Map<String, Map<String, FieldNVD>> newTableMap) {
		StringBuffer detailsSb=new StringBuffer();
		//首先以老数据为基础，判断删了哪些数据和更改了某些数据
		if(oldTableMap!=null)
		for(String pk:oldTableMap.keySet()){
			//修改了这行数据
			if(newTableMap!=null&&newTableMap.containsKey(pk)){
				Map<String,FieldNVD> oldSubData=oldTableMap.get(pk);
				Map<String,FieldNVD> newSubData=newTableMap.get(pk);
				detailsSb.append(checkTableFieldData(oldSubData,newSubData));
			}else{//删了这行数据
				detailsSb.append("删除了数据："+getTableFieldData(oldTableMap.get(pk)));
			}
		}
		//然后以新数据为基础，判断新增了哪些数据
		if(newTableMap!=null)
		for(String pk:newTableMap.keySet()){
			if(oldTableMap==null||!oldTableMap.containsKey(pk)){//新增了这行数据
				detailsSb.append("新增了数据："+getTableFieldData(newTableMap.get(pk)));
			}
		}
		return detailsSb.toString();
	}
	/**
	 * 获取表数据
	 * @param data
	 * @return
	 */
	private String getTableFieldData(Map<String, FieldNVD> data) {
		StringBuffer detailsSb=new StringBuffer();
		int i=1;
		for(String fieldName:data.keySet()){
			FieldNVD f=data.get(fieldName);
			if(i<data.size()){
				detailsSb.append(f.getDesc()+":"+f.getValue().toString()+" ");
			}else{
				detailsSb.append(f.getDesc()+":"+f.getValue().toString()+"；");
			}
			i++;
		}
		return detailsSb.toString();
	}
	/**
	 * 比较表数据
	 * @param oldSubData
	 * @param newSubData
	 * @return
	 */
	private String checkTableFieldData(Map<String, FieldNVD> oldSubData,Map<String, FieldNVD> newSubData) {
		StringBuffer detailsSb=new StringBuffer();
		for(String fieldName:oldSubData.keySet()){
			FieldNVD oldValue=oldSubData.get(fieldName);
			FieldNVD newValue=newSubData.get(fieldName);
			if(CommonTools.isNoEqual(oldValue.getValue(),newValue.getValue())){
				detailsSb.append(getChangeDetail(oldValue.getValue(),newValue.getValue(), oldValue.getDesc()));
			}
		}
		return detailsSb.toString();
	}
	/**
	 * 字段数据对比
	 * @param mainTable 
	 * @param mainFields
	 * @param mainFields2
	 * @return
	 */
	private String checkTableFieldData(Map<String, Object> oldFields,Map<String, Object> newFields, IFormTable mainTable) {
		StringBuffer detailsSb=new StringBuffer();
		Map<String,IFormField> mapFields=getMapFields(mainTable);
		for(String fieldName:oldFields.keySet()){
			Object oldValue=oldFields.get(fieldName);
			Object newValue=newFields.get(fieldName);
			if(CommonTools.isNoEqual(oldValue,newValue)){
				detailsSb.append(getChangeDetail(oldValue, newValue, mapFields.get(fieldName).getFieldDesc()));
			}
		}
		return detailsSb.toString();
	}
	/**
	 * 获取给定tableId的表的字段及对应的描述信息
	 * @param table
	 * @return
	 */
	protected Map<String,IFormField> getMapFields(IFormTable table) {
		List<? extends IFormField> fields=table.getFieldList();
		Map<String,IFormField> mapFields=new HashMap<String,IFormField>();
		for(IFormField field:fields){
			mapFields.put(field.getFieldName().toLowerCase(), field);
		}
		return mapFields;
	}
	@Override
	public List<Map<String, Object>> getTableData(Boolean isNew,DataNote methodDataNote ,HttpServletRequest request) {
		try {
			// 只有业务数据修改的方法加了DataNote
			if (isCorrectDataNote(methodDataNote) == false) {
				return null;
			}
			Long tableId=RequestUtil.getLong(request, "tableId");
			IFormTable bpmFormTable=formTableService.getByTableId(tableId, 1);
			String businessKey = request.getParameter(bpmFormTable.getPkField());
			if(StringUtil.isEmpty(businessKey)){
				return null;
			}
			IFormData data =null;
			try {
				data = formHandlerService.getBpmFormData(null, tableId, businessKey,null, null, "", false);
			} catch (Exception e) {
				e.printStackTrace();
				return null;
			}
			if(data==null||data.getFormTable()==null){
				return null;
			}
			// 获取数据 存入dataList
			List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
			
			Map<String, Object> datas=new HashMap<String, Object>();
			datas.put("data", data);
			dataList.add(datas);
			return dataList;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
