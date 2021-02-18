package com.cssrc.ibms.core.flow.service;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IRunLogService;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.sysuser.SystemConst;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.flow.dao.ProcessRunDao;
import com.cssrc.ibms.core.flow.dao.RunLogDao;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;


/**
 * 对象功能:流程运行日志 Service类 
 * 开发人员:zhulongchao 
 */
@Service
public class RunLogService extends BaseService<RunLog> implements IRunLogService
{
	@Resource
	private RunLogDao dao;
	@Resource
	private ProcessRunDao processRunDao;
	
	public RunLogService()
	{
	}
	
	@Override
	protected IEntityDao<RunLog, Long> getEntityDao() 
	{
		return dao;
	}	

	/**
	 * 通过用户ID获取用户操作的流程日志
	 * @param userId 用户ID
	 * @return
	 */
	public List<RunLog> getByUserId(Long userId){		
		List list=dao.getByUserId(userId);
		return list;
	}
	
	/**
	 * 通过流程运行ID获取流程的操作日志
	 * @param runId 流程运行ID
	 * @return
	 */
	public List<RunLog> getByRunId(Long runId){		
		List list=dao.getByRunId(runId);
		return list;
	}
	
	/**
	 * 根据流程运行ID删除流程的操作日志
	 * @param runId
	 */
	public void delByRunId(Long runId){
		dao.delByRunId(runId);
	}
	
	/**
	 * 添加流程运行日志
	 * @param opType 操作类型
	 * @param memo 备注
	 */
	public void addRunLog(Long runId,Integer operatortype,String memo){
		ProcessRun processRun= processRunDao.getById(runId);
		if(processRun!=null){
		    this.addRunLog(processRun, operatortype, memo);
		}
	}

	/**
	 * 添加流程运行日志。
	 * @param user
	 * @param runId
	 * @param operatortype
	 * @param memo
	 */
	public void addRunLog(ISysUser user,Long runId,Integer operatortype,String memo){
		ProcessRun processRun= processRunDao.getById(runId);
		this.addRunLog(user,processRun, operatortype, memo);
	}
	
	/**
	 * 添加流程运行日志
	 * @param opType 操作类型
	 * @param memo 备注
	 */
	public void addRunLog(ProcessRun processRun,Integer operatortype,String memo){
		ISysUser user=(ISysUser)UserContextUtil.getCurrentUser();
		Long userId = SystemConst.SYSTEMUSERID;
		String userName = SystemConst.SYSTEMUSERNAME;
		if(user!=null){
			userId = user.getUserId();
			userName = user.getFullname();
		}
		Date now= Calendar.getInstance().getTime();
		RunLog runLog=new RunLog();
		runLog.setId(UniqueIdUtil.genId());
		runLog.setUserid(userId);
		runLog.setUsername(userName);
		runLog.setRunid(processRun.getRunId());
		runLog.setProcessSubject(processRun.getSubject());
		runLog.setCreatetime(now);
		runLog.setOperatortype(operatortype);
		runLog.setMemo(memo);
		dao.add(runLog);
		 //集成平台日志
        LogThreadLocalHolder.putParamerter(LogThreadLocalHolder.ACT_RUN_LOG_ID, runLog.getId());
	}
	
	/**
	 * 添加流程运行日志。
	 * @param user
	 * @param runId
	 * @param operatortype
	 * @param memo
	 */
	public void addRunLog(ISysUser user,ProcessRun processRun,Integer operatortype,String memo){
		RunLog runLog=new RunLog();
		runLog.setId(UniqueIdUtil.genId());
		runLog.setUserid(user.getUserId());
		runLog.setUsername(user.getFullname());
		runLog.setRunid(processRun.getRunId());
		runLog.setProcessSubject(processRun.getSubject());
		runLog.setCreatetime(new Date());
		runLog.setOperatortype(operatortype);
		runLog.setMemo(memo);
		dao.add(runLog);
	}

    /** 
    * @Title: updateDetail 
    * @Description: TODO(更新日志详情) 
    * @param @param string
    * @param @param detail     
    * @return void    返回类型 
    * @throws 
    */
    @Override
    public void updateDetail(String id, String detail)
    {
        Map<String,Object> parmas=new HashMap<String,Object>();
        parmas.put("detail", detail);
        parmas.put("id", id);
        this.dao.update("updateDetail", parmas);
        
    }
}
