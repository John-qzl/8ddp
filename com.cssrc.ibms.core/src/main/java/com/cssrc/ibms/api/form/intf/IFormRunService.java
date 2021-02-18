package com.cssrc.ibms.api.form.intf;

import java.util.List;

import com.cssrc.ibms.api.activity.model.INodeSet;
import com.cssrc.ibms.api.form.model.IFormRun;

public interface IFormRunService{

	/**
	 * 添加表单运行情况。
	 * 
	 * <pre>
	 * 将当前最新的表单配置信息添加到表单运行情况。
	 * 之后的流程表单从表单运行情况表中取值。
	 * </pre>
	 * 
	 * @param actDefId
	 *            表单定义ID
	 * @param runId
	 *            process runID
	 * @param actInstanceId
	 *            流程实例ID
	 * @throws Exception
	 */
	public abstract void addFormRun(String actDefId, Long runId,
			String actInstanceId);

	/**
	 * 判断是否可以直接启动。
	 * 
	 * <pre>
	 * 
	 * </pre>
	 * 
	 * @param defId
	 * @return
	 */
	public abstract boolean getCanDirectStart(Long defId);

	/**
	 * 取得流程运行表单情况。
	 * 
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	public abstract IFormRun getByInstanceAndNode(String actInstanceId,
			String actNodeId);

	/**
	 * 取得流程运行表单情况，与getByInstanceAndNode不同，此方法不对取得的表单做任务判断和处理。
	 * 
	 * @param actInstanceId
	 * @param actNodeId
	 * @return
	 */
	public abstract IFormRun getByInstanceAndNodeId(String actInstanceId,
			String actNodeId);

	/**
	 * 根据流程实例ID，流程实例的运行表单列表
	 * 
	 * @param actInstanceId
	 * @return
	 */
	public abstract List<? extends IFormRun> getByInstanceId(
			String actInstanceId);

	/**
	 * 根据流程实例删除数据。
	 * 
	 * @param actInstanceId
	 */
	public abstract int delByInstanceId(String valueOf);

	/**
	 * 根据act流程定义Id删除数据
	 * 
	 * @param actDefId
	 */
	public abstract int delByActDefId(String actDefId);
	/**
	 * 获取启动节点信息
	 * @param actDefId
	 * @param toFirstNode
	 * @return
	 * @throws Exception 
	 */
	public abstract INodeSet getStartNodeSet(Long defId, String actDefId)
			throws Exception;

}