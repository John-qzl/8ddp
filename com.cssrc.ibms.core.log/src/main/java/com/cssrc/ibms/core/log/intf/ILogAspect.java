package com.cssrc.ibms.core.log.intf;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.cssrc.ibms.api.system.model.ISysLog;
import com.cssrc.ibms.core.util.annotion.DataNote;

/**
 *@author vector
 *@date 2017年9月16日 下午2:11:45
 *@Description 数据获取和比对
 */
public interface ILogAspect {
	/**
	 * 比对数据的不同
	 * 已有单个bean的比对方法，使用例在SysLogAspectService.checkData方法中的[matchDataDiff(...)]
	 * @param oldData 方法执行前的数据
	 * @param newData 方法执行后的数据
	 * @return 比较的结果
	 */
	public String checkData(List<Map<String, Object>> oldData, List<Map<String, Object>> newData,DataNote methodDataNote ,HttpServletRequest request);
	/**
	 * 获取bean和主键名PKName,根据Request和PKName获取需要的每行的ID
	 * 根据ID获取每行数据，存储，返回
	 * @param isNew 
	 * 		false: 获取方法体执行前的数据 oldData
	 * 		true:  获取方法体执行后的数据 newData
	 * @return
	 */
	public List<Map<String, Object>> getTableData( Boolean isNew,DataNote methodDataNote ,HttpServletRequest request);
	

	/** 
	* @Title: callBack 
	* @Description: TODO(回调函数接口) 
	* @param @param log 系统日志
	* @param @param data 回调需要的参数    
	* @return void    返回类型 
	* @throws 
	*/
	public void callBack(ISysLog log,Map<String, Object> data);
}
