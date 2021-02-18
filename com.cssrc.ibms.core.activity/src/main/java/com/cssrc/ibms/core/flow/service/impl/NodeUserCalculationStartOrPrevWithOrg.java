package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.bean.BeanUtils;

import net.sf.json.JSONObject;

/**
 * 根据发起人或上一任务执行人的组织（包括组织、岗位、职务）计算人员。
 * <pre>
 * 人员的JSON格式如下：
 * {type:'org',userType:'start',varName:'name'}
 * type:可能的值为：
 * org 组织
 * pos 岗位
 * job 职务
 * userType:可能的值为 start、prev
 * </pre>
 * @author zhulongchao
 *
 */
public class NodeUserCalculationStartOrPrevWithOrg implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private IPositionService positionService;
	@Resource
	private IUserPositionService userPositionService;
	
	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Long userId = 0L;//发起人或上一任务发起人
		Long orgId=0L;
		List<?extends ISysUser> sysUserList=new ArrayList<ISysUser>();
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		String type=jsonObject.getString("type");
		String userType=jsonObject.getString("userType");
		
		if("start".equals(userType)){//发起人
			userId=vars.getStartUserId();
		}
        if("prev".equals(userType)){//上一任务执行人
        	userId=vars.getPrevExecUserId();
        	if(userId==null||userId==0L)userId=vars.getStartUserId();
		}
        if(userId==null  || userId.intValue()==0) 
        	return new ArrayList<ISysUser>();
        
		//组织
        if("org".equals(type)){
        	IUserPosition userOrg = this.userPositionService.getPrimaryByUserId(userId);
			if(userOrg!=null){
				orgId=userOrg.getOrgId();
			}
			sysUserList = this.sysUserService.getByOrgId(orgId);
        }
        //职务
		if("job".equals(type)){
			sysUserList = this.sysUserService.getSameJobUsersByUserId(userId);
		}
		//岗位
		if("pos".equals(type)){
			sysUserList = this.sysUserService.getSamePositionUsersByUserId(userId);
		}
		if(type.indexOf("selectjob")>=0){
            //表示用户相同组织下的某个职务
            IUserPosition userOrg = this.userPositionService.getPrimaryByUserId(userId);
            if(userOrg!=null){
                orgId=userOrg.getOrgId();
                String jobId=type.replace("selectjob_", "");
                sysUserList = this.sysUserService.getSameOrgJobUsers(orgId,Long.valueOf(jobId));
            }

        
		}
		return sysUserList;
	}
	
	
	

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser,CalcVars vars) {
		Long userId = 0L;//发起人或上一任务发起人
	
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		String type=jsonObject.getString("type");
		String userType=jsonObject.getString("userType");
		
		int extractUser=bpmNodeUser.getExtractUser();
		Set<TaskExecutor> uIdSet = new LinkedHashSet<TaskExecutor>();
		
		if("start".equals(userType)){//发起人
			userId=vars.getStartUserId();
		}
        if("prev".equals(userType)){//上一任务执行人
        	userId=vars.getPrevExecUserId();
		}
        if(userId==null  || userId.intValue()==0) 
        	return uIdSet;
        
		//组织
        if("org".equals(type)){
        	handOrg(userId, extractUser, uIdSet);
        }
        //职务
		if("job".equals(type)){
			//得到用户的职务
			//handJob(userId, extractUser, uIdSet);
		    handPos(userId, extractUser, uIdSet);
		}
		//岗位
		if("pos".equals(type)){
			//得到用户的岗位
		    handPos(userId, extractUser, uIdSet);
		}
		
		if(type.indexOf("selectjob")>=0){
            //表示用户相同组织下的某个职务，这里涉及到两个属性，组织下的职务，目前直接抽取用户
            IUserPosition userOrg = this.userPositionService.getPrimaryByUserId(userId);
            if(userOrg!=null){
                Long orgId=userOrg.getOrgId();
                String jobId=type.replace("selectjob_", "");
                List<?extends ISysUser> sysUserList = this.sysUserService.getSameOrgJobUsers(orgId,Long.valueOf(jobId));
                for (ISysUser sysUser : sysUserList) {
                    TaskExecutor taskExcutor = TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname());
                    uIdSet.add(taskExcutor);
                }
            }

        }
		return uIdSet;
	}

	private void handPos(Long userId, int extractUser, Set<TaskExecutor> uIdSet) {
		List<?extends IPosition> positionList = this.positionService.getByUserId(userId);
		ISysUser user = sysUserService.getById(userId);
		if(BeanUtils.isEmpty(positionList)) return;
		
		switch (extractUser) {
			//不抽取
			case TaskExecutor.EXACT_NOEXACT:
				for(IPosition position:positionList){
				TaskExecutor executor=TaskExecutor.getTaskPos(position.getPosId().toString(), position.getPosName());
				uIdSet.add(executor);
				}
				break;
			//抽取
			case TaskExecutor.EXACT_EXACT_USER:
				 //得到相同岗位的用户
				 List<?extends ISysUser> list = this.sysUserService.getSamePositionUsersByUserId(userId);
				 for (ISysUser sysUser : list) {
					 TaskExecutor taskExcutor = TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname());
					 uIdSet.add(taskExcutor);
				 }
				break;
			//二次抽取
			case TaskExecutor.EXACT_EXACT_SECOND:
				for(IPosition position:positionList){
				TaskExecutor taskexecutor=TaskExecutor.getTaskOrg(position.getPosId().toString(), position.getPosName());
				taskexecutor.setExactType(extractUser);
				uIdSet.add(taskexecutor);
				}
				break;
		}

	}




	/*private void handJob(Long userId, int extractUser, Set<TaskExecutor> uIdSet) {
		List<Job> jobList=jobDao.getByUserId(userId);
		if(BeanUtils.isEmpty(jobList)) return;
		
		switch (extractUser) {
			//不抽取
			case TaskExecutor.EXACT_NOEXACT:
				for(Job job:jobList){
				TaskExecutor executor=TaskExecutor.getTaskJob(job.getJobid().toString(), job.getJobname());
				uIdSet.add(executor);
				}
				break;
			//抽取
			case TaskExecutor.EXACT_EXACT_USER:
				//得到相同职务的用户
				List<ISysUser> list= sysUserService.getSamePositionUsersByUserId(userId);
				for(ISysUser sysUser:list){
					TaskExecutor taskExcutor=TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname());
					uIdSet.add(taskExcutor);
				}
				break;
			//二次抽取
			case TaskExecutor.EXACT_EXACT_SECOND:
				for(Job job:jobList){
				TaskExecutor taskexecutor=TaskExecutor.getTaskOrg(job.getJobid().toString(), job.getJobname());
				taskexecutor.setExactType(extractUser);
				uIdSet.add(taskexecutor);
				}
				break;
		
		}
	}*/



	
	private void handOrg(Long userId, int extractUser, Set<TaskExecutor> uIdSet) {
		IUserPosition userPosition = this.userPositionService.getPrimaryByUserId(userId);
		if (userPosition == null) return;
		Long orgId = userPosition.getOrgId();
		ISysOrg sysOrg = (ISysOrg)this.sysOrgService.getById(orgId);
		switch (extractUser) {
			//不抽取
			case TaskExecutor.EXACT_NOEXACT:
				TaskExecutor executor = TaskExecutor.getTaskOrg(orgId.toString(), sysOrg.getOrgName());
				uIdSet.add(executor);
				break;
			//抽取
			case TaskExecutor.EXACT_EXACT_USER:
				List<?extends ISysUser> list = this.sysUserService.getByOrgId(orgId);
				for (ISysUser sysUser : list) {
					TaskExecutor taskExcutor = TaskExecutor.getTaskUser(sysUser.getUserId().toString(), sysUser.getFullname());
					uIdSet.add(taskExcutor);
				}
				break;
			//二次抽取
			case TaskExecutor.EXACT_EXACT_SECOND:
				TaskExecutor taskexecutor = TaskExecutor.getTaskOrg(orgId.toString(), sysOrg.getOrgName());
				taskexecutor.setExactType(extractUser);
				uIdSet.add(taskexecutor);
		}
		
	}

	

	@Override
	public String getTitle() {
		return "与发起人或上一任务执行人具有相同组织的人";
	}

	@Override
	public boolean supportMockModel() {
		
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		List<PreViewModel> list=new ArrayList<PreViewModel>();
		PreViewModel preViewModel=new PreViewModel();
		preViewModel.setType(PreViewModel.START_USER);
		list.add(preViewModel);
			
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}

}