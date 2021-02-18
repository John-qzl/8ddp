/**
 * 对象功能:流程表单运行情况 Dao类 
 */
package com.cssrc.ibms.core.form.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.cssrc.ibms.core.db.mybatis.dao.BaseDao;
import com.cssrc.ibms.core.form.model.FormRun;

@Repository
public class FormRunDao extends BaseDao<FormRun>
{
	@SuppressWarnings("rawtypes")
	@Override
	public Class getEntityClass()
	{
		return FormRun.class;
	}
	
	/**
	 * 根据流程实例取得表单运行数据。
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public FormRun getByInstanceAndNode(String actInstanceId,String actNodeId){
		Map params=new HashMap();
		params.put("actInstanceId", actInstanceId);
		params.put("actNodeId", actNodeId);
		return this.getUnique("getByInstanceAndNode", params);
	}
	
	/**
	 * 根据流程实例ID，流程实例的运行表单列表
	 * @param actInstanceId
	 * @return
	 */
	public List<FormRun> getByInstanceId(String actInstanceId){
		return this.getBySqlKey("getByInstanceId", actInstanceId);
	}
	
	/**
	 * 根据流程实例取得全局表单。
	 * @param actInstanceId
	 * @return
	 */
	public FormRun getGlobalForm(String actInstanceId){
		return this.getUnique("getGlobalForm", actInstanceId);
	}
	/**
	 * 根据流程实例删除数据。
	 * @param actInstanceId
	 */
	public void delByInstanceId(String actInstanceId){
		this.delBySqlKey("delByInstanceId", actInstanceId);
	}
	/**
	 * 根据act流程定义Id删除数据
	 * @param actDefId
	 */
	public void delByActDefId(String actDefId){
		delBySqlKey("delByActDefId", actDefId);
	}
}