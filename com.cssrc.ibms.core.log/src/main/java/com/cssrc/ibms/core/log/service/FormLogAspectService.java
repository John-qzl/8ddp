package com.cssrc.ibms.core.log.service;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;








import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.core.log.intf.AbsLogAspect;
import com.cssrc.ibms.core.log.intf.ILogAspect;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 *@author vector
 *@date 2017年9月18日 上午8:55:37
 *@Description 表单模块日志
 */
@Service
public class FormLogAspectService extends AbsLogAspect implements ILogAspect {
	@Resource
	private IFormTableService formTableService;
	@Override
	public String checkData(List<Map<String, Object>> oldData,List<Map<String, Object>> newData,DataNote methodDataNote ,HttpServletRequest request) {
		StringBuffer detailSb=new StringBuffer();
		for(int i=0;i<oldData.size();i++){
			Map<String,Object> oldMap=oldData.get(i);
			Map<String,Object> newMap=newData.get(i);
			detailSb.append(matchDataDiff(oldMap,newMap,methodDataNote.beanName()[i],null));
		}
		return detailSb.toString();
	}
	/**
	 * 根据bean的新旧数据，比对数据的不同
	 * @param oldMap
	 * @param newMap
	 * @param clazz
	 * @return 新旧数据的比对结果
	 * @author scc 
	 * @date 2017年09月15日下午7:11:58
	 */
	@SuppressWarnings("rawtypes")
	protected String matchDataDiff(Map<String, Object> oldMap, Map<String, Object> newMap, Class<?> clazz,String methodDes) {
		StringBuffer detail=new StringBuffer();
		//新增
		if(oldMap==null){
			if(methodDes!=null){
				detail.append("新增字段【"+methodDes+"】");
			}
			detail.append("");
		}
		//删除
		else if(newMap==null){
			if(methodDes!=null){
				detail.append("删除字段【"+methodDes+"】");
			}
			detail.append("");
		}else{
			for(String fieldName:oldMap.keySet()){
				Object oldValue=oldMap.get(fieldName);
				Object newValue=newMap.get(fieldName);
				//bean内的List特殊处理
				if(oldValue instanceof ArrayList){//例如一个表中包括的多个列字段信息
					ArrayList value_old=(ArrayList)oldValue;
					ArrayList value_new=(ArrayList)newValue;
					int maxSize=value_old.size()>value_new.size()?value_old.size():value_new.size();
					
					for(int i=0;i<maxSize;i++){
						Map<String, Object> oldMapTemp=null;
						Map<String, Object> newMapTemp=null;
						Class<?> clazzTemp=null;
						if(i<value_old.size()){
							oldMapTemp=MapUtil.transBean2Map(value_old.get(i));
							clazzTemp=value_old.get(i).getClass();
						}
						if(i<value_new.size()){
							newMapTemp=MapUtil.transBean2Map(value_new.get(i));
							clazzTemp=value_new.get(i).getClass();
						}
						detail.append(matchDataDiff(oldMapTemp,newMapTemp,clazzTemp,getFieldDescriptionName(fieldName,clazz)));
					}
				}
				if(CommonTools.isNoEqual(oldValue,newValue)){
					detail.append(check(oldValue,newValue,fieldName,clazz));
				}
			}
			if(!detail.toString().equals("")){
				if(methodDes!=null){
					detail=new StringBuffer("修改字段【"+methodDes+"】"+detail.toString());
				}
			}
		}
		
		return detail.toString();
	}
	private String getFieldDescriptionName(String fieldName,Class<?> clazz){
		
		String fieldDescriptionName=null;
		try {
			Field field=clazz.getDeclaredField(fieldName);
			SysFieldDescription sysFieldDescription=field.getAnnotation(SysFieldDescription.class);
			if(sysFieldDescription==null||BeanUtils.isEmpty(sysFieldDescription.detail())){
				System.err.println(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
				logger.error(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
				return null;
			}
			//字段描述信息。 例如 字段：userName 描述信息：用户名 
			fieldDescriptionName=sysFieldDescription.detail();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return fieldDescriptionName;
	}
	/**
	 * 此处比较的是某个类的具体fieldName(属性名)的两个值是否相同
	 * @param oldValue
	 * @param newValue
	 * @param fieldName
	 * @param clazz
	 * @return
	 */
	private String check(Object oldValue,Object newValue,String fieldName,Class<?> clazz) {
		StringBuffer detail=new StringBuffer();
		try {
			Field field=clazz.getDeclaredField(fieldName);
			SysFieldDescription sysFieldDescription=field.getAnnotation(SysFieldDescription.class);
			if(sysFieldDescription==null||BeanUtils.isEmpty(sysFieldDescription.detail())){
				System.err.println(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
				logger.error(clazz.getSimpleName()+"."+fieldName+"未加属性描述信息!");
				return "";
			}
			//字段描述信息。 例如 字段：userName 描述信息：用户名 
			String fieldDescriptionName=sysFieldDescription.detail();
			
			//数据转换、说明。例如 字段：sex  转换：1男 2女
			if(StringUtil.isNotEmpty(sysFieldDescription.maps())){
				net.sf.json.JSONObject fieldMaps= net.sf.json.JSONObject.fromObject(sysFieldDescription.maps());
				oldValue=fieldMaps.get(oldValue.toString());
				newValue=fieldMaps.get(newValue.toString());
			}
			//新值为空时
			if(BeanUtils.isEmpty(newValue)){
				detail.append("将字段【"+fieldDescriptionName+"】的值由"+oldValue.toString()+"<span style=\"color:green;\">改为了</span>NULL；");
			}
			//旧值为空时
			else if(BeanUtils.isEmpty(oldValue)){
				detail.append("将字段【"+fieldDescriptionName+"】的值由NULL<span style=\"color:green;\">改为了</span>"+newValue.toString()+"；");
			}else{
				detail.append("将字段【"+fieldDescriptionName+"】的值由"+oldValue.toString()+"<span style=\"color:green;\">改为了</span>"+newValue.toString()+"；");
			}
				
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return detail.toString();
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Map<String, Object>> getTableData(Boolean isNew,DataNote methodDataNote ,HttpServletRequest request) {

		// DataNote配置不正确时，直接返回
		if (isCorrectDataNote(methodDataNote) == false) {
			return null;
		}
		// 主键名
		String[] pkNames = methodDataNote.pkName().split(",");
		// 获取数据 存入dataList
		List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
		for (int i = 0; i < pkNames.length; i++) {
			// 根据主键名和参数，获取需要处理的ID-即一行数据的ID
			Long[] pkIds;
			if(pkNames[i].contains("_")){
				String clazz=RequestUtil.getString(request, pkNames[i].split("_")[0]);
				JSONObject t=JSONObject.parseObject(clazz);
				String ids=t.get(pkNames[i].split("_")[1]).toString();
				pkIds=StringUtil.getLongFromString(ids,",");
			}else{
				pkIds= RequestUtil.getLongAry(request, pkNames[i]);
			}
			
			if (pkIds == null) {
				continue;
			}
			// 获取Bean
			Class<?> beanName = methodDataNote.beanName()[i];

			// 根据主键id和beanName获取对应表记录
			for (int n = 0; n < pkIds.length; n++) {
				if (pkIds[n] == null || pkIds[n] == 0L) {
					continue;
				}
				if(beanName.getName().equals("com.cssrc.ibms.core.form.model.FormTable")){
					Object o=formTableService.getTableById(pkIds[n]);
					Map<String, Object> dataMap = MapUtil.transBean2Map(o);
					dataList.add(dataMap);
				}else{
					List<Map<String, Object>> list = sysLogService.getDataListByBean(beanName, pkIds[n]);
					
					//for (int j = 0; j < list.size(); j++) {
						if(list!=null&&list.size()>0){
							Map<String, Object> dataMap = MapUtil.transBean2Map(list.get(0));
							dataList.add(dataMap);
						}
					//}
				}
				
			}
		}
		return dataList;

	
	}

}
