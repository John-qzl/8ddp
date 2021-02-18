package com.cssrc.ibms.core.log.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.system.model.ISysLog;
import com.cssrc.ibms.core.log.intf.AbsLogAspect;
import com.cssrc.ibms.core.log.intf.ILogAspect;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;

/**
 *@author vector
 *@date 2017年9月16日 下午3:45:40
 *@Description 系统常用Controller的拦截处理
 */
@Service
public class SysLogAspectService extends AbsLogAspect implements ILogAspect {

	@Override
	public String checkData(List<Map<String, Object>> oldData,List<Map<String, Object>> newData,DataNote methodDataNote ,HttpServletRequest request) {
		String detail="";
		StringBuffer detailSb=new StringBuffer();
		for(int i=0;i<oldData.size();i++){
			//初始化表数据map
			Map<String,Object> oldMap=oldData.get(i);
			Map<String,Object> newMap=newData.get(i);
			detailSb.append(matchDataDiff(oldMap,newMap,methodDataNote.beanName()[i]));
		}
		//获取人员相关表变化
		Long userId=RequestUtil.getLong(request, "userId");
		detail = sysLogCustomService.getUserDataListByBean(detailSb.toString(), methodDataNote, userId);
		return detail;
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
			Long[] pkIds = RequestUtil.getLongAry(request, pkNames[i]);
			if (pkIds == null) {
				continue;
			}
			// 获取Bean
			Class<?> beanName = methodDataNote.beanName()[i];
			// 人员的组织等信息 --特殊处理
			if (!isNew && pkIds[0] != null) {
				sysLogCustomService.getUserDataOldList(methodDataNote, pkIds[0]);
			}

			// 根据主键id和beanName获取对应表记录
			for (int n = 0; n < pkIds.length; n++) {
				if (pkIds[n] == null || pkIds[n] == 0L) {
					continue;
				}
				List<Map<String, Object>> list = sysLogService.getDataListByBean(beanName, pkIds[n]);
				// 表数据进行遍历添加的dataList中
				for (int j = 0; j < list.size(); j++) {
					Map<String, Object> dataMap = MapUtil.transBean2Map(list
							.get(j));
					dataList.add(dataMap);
				}
			}
		}
		return dataList;

	}


}
