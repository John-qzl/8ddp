package com.cssrc.ibms.api.activity.intf;

import java.util.List;
import java.util.Map;

import com.cssrc.ibms.api.activity.model.ITaskOpinion;

public interface ITaskOpinionService{

	/**
	 * 取得对应该任务的执行
	 * 
	 * @param taskId
	 * @return
	 */
	public abstract ITaskOpinion getByTaskId(Long taskId);

	/**
	 * 取得某个任务的所有审批意见 按时间排序
	 * 
	 * @param actInstId
	 * @return
	 */
	public abstract List<? extends ITaskOpinion> getByActInstId(String actInstId,
			boolean isAsc);

	/**
	 * 取得某个任务的所有审批意见 
	 * 
	 * @param actInstId
	 * @return
	 */
	public abstract List<? extends ITaskOpinion> getByActInstId(String actInstId);

	public abstract List<? extends ITaskOpinion> getFormOptionsByInstance(String instanceId);

	public abstract List<? extends ITaskOpinion> getFormOpinionByActInstId(String actInstId);

	/**
	 * 根据act流程定义Id删除对应在流程任务审批意见
	 * 
	 * @param actDefId
	 */
	public abstract void delByActDefIdTaskOption(String actDefId);

	/**
	 * 根据流程实例Id及任务定义Key取得审批列表
	 * 
	 * @param actInstId
	 * @param taskKey
	 * @return
	 */
	public abstract List<? extends ITaskOpinion> getByActInstIdTaskKey(Long actInstId,
			String taskKey);


	/**
	 * 取得某个流程实例中，某用户最新的完成的审批记录
	 * 
	 * @param actInstId
	 * @param exeUserId
	 * @return
	 */
	public abstract ITaskOpinion getLatestUserOpinion(String actInstId,
			Long exeUserId);

	/**
	 * 按任务ID删除
	 * 
	 * @param taskId
	 */
	public abstract void delByTaskId(Long taskId);
	/**
	 * 获取正在审批的意见。
	 * @param actInstId
	 * @return
	 */
	public abstract List<? extends ITaskOpinion> getCheckOpinionByInstId(Long actInstId);


	/**
	 * 根据实例节点获取任务实例状态。
	 * @param actInstId
	 * @param taskKey
	 * @param checkStatus
	 * @return
	 */
	public abstract List<? extends ITaskOpinion> getByActInstIdTaskKeyStatus(
			String actInstId, String taskKey, Short checkStatus);

	public abstract ITaskOpinion getOpinionByTaskId(Long taskId, Long userId);

	/**
	 * 根据actInstId更新。
	 * @param actInstId
	 * @param oldActInstId
	 * @return
	 */
	public abstract int updateActInstId(String actInstId, String oldActInstId);

    /**
     * finer report 报表模板获取审批意见接口
     * @param dataKey
     * @param nodeSetKey
     */
    public abstract String getOpinion(Map<String,String> parmas);

    /** 
    * @Title: getLatestTaskOpinion 
    * @Description: TODO(取到最新的某个节点的审批记录) 
    * @param @param instanceId
    * @param @param nodeid
    * @param @return    
    * @return ITaskOpinion    返回类型 
    * @throws 
    */
    public abstract ITaskOpinion getLatestTaskOpinion(Long instanceId, String nodeid);

}