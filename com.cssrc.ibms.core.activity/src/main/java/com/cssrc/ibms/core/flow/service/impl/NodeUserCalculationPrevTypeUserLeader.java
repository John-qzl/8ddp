package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;

import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 根据节点用户设置为“部门的上级类型部门的负责人”，计算执行人员。
 * 
 * 问题1:
 * 使用发起人，需要获取发起人发起流程时的组织，测试的时候是没有发起组织的，这个时候取模拟发起人的主组织。
 * 
 * 问题2:
 * 	计算方法，获取当前组织后，不使用递归查询，使用path 进行查询：
 * 	111,222,333,444.
 * 
 * @author zhulongchao
 */
public class NodeUserCalculationPrevTypeUserLeader implements
		INodeUserCalculation {
	@Resource
	private IUserPositionService userPositionService;
	@Resource
	private ISysOrgService sysOrgService;

	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		Long startUserId = vars.getStartUserId();
		Long prevUserId = vars.getPrevExecUserId();
		String actInsId = vars.getActInstId();
		String startOrgId = vars.getStartOrgId();
		List<?extends ISysUser> users = new ArrayList<ISysUser>();
		String expandParams = bpmNodeUser.getCmpIds();
		if (StringUtil.isEmpty(expandParams)) {
			return users;
		}
		JSONObject json = JSONObject.fromObject(expandParams);
		
		ISysOrg sysOrg = null;
		if(StringUtils.isEmpty(actInsId)){//判断为模拟预览状态
			if("0".equals(json.get("orgSource"))){//发起人
				sysOrg = this.sysOrgService.getPrimaryOrgByUserId(startUserId);
			}
			else {//上一部执行人
				sysOrg = this.sysOrgService.getPrimaryOrgByUserId(prevUserId);
			}
		}
		else {//判断为流程运行时的状态
			if("0".equals(json.get("orgSource"))) {//发起人
				if(StringUtils.isEmpty(startOrgId)) return users;
				sysOrg = sysOrgService.getById(Long.valueOf(startOrgId));
			}
			else{//上一步执行人
				if (prevUserId.equals(UserContextUtil.getCurrentUserId())) {
					sysOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
				} else {
					sysOrg = this.sysOrgService.getDefaultOrgByUserId(prevUserId);
				}
			}
		}
		
		if (sysOrg == null) {
			return users;
		}
		
		//获取字符串中的组织类型
		Object levelObjct = json.get("level");
		// 根据条件取相关部门,如果组织类型为空就传对应参数为null
		if(!levelObjct.equals(""))
			users = getPrveTypeLeader(levelObjct.toString(), json.get("stategy").toString(), sysOrg);
		else
			users = getPrveTypeLeader(null, json.get("stategy").toString(), sysOrg);
		return users;
	}

	
	/**
	 * 获取部门的指定类型部门的负责人，一直获取下去直到最顶级为空或取得指定一类型的负责人。
	 * 
	 * @param level 组织类型
	 * @param stategy
	 * @param sysOrg
	 * @return
	 */
	private List<?extends ISysUser> getPrveTypeLeader(String level,
			String stategy, ISysOrg sysOrg) {
		String pathArr[] = sysOrg.getPath().split("\\.");
		int currentDepth = pathArr.length;
		//为什么减2 
		for(int len = currentDepth-1; len>0; len--) {
			Long orgId = Long.valueOf(pathArr[len]);
			ISysOrg parentOrg = sysOrgService.getById(orgId);
			if(BeanUtils.isEmpty(parentOrg)) continue;
			//如果组织类型不为空，将其转为Long类型
			Long LevelLong = null;
			if(level!=null){
				LevelLong = Long.parseLong(level);
			}
			//组织类型不为空就与父组织类型进行比较，相同才执行其他条件查询人员
			//组织类型为空就不筛选，执行其他条件查询人员
			if(level==null||(parentOrg.getOrgType()).equals(LevelLong)){
				if("1".equals(stategy)){//根据查找策略stategy=1只查指定类型部门 
					/*return sysUserOrgService.getUsersByOrgIdandIsCharge(parentOrg.getOrgId());*/
					return this.userPositionService.getLeaderUserByOrgId(parentOrg.getOrgSupId(), false);
				}
				else {//stategy=0指定类型为空继续往上查询部门负责人
					/*List<ISysUser> list = sysUserOrgService.getUsersByOrgIdandIsCharge(parentOrg.getOrgId());*/
					List<?extends ISysUser> list = this.userPositionService.getLeaderUserByOrgId(parentOrg.getOrgSupId(), false);
					if(list.size()==0) continue;//如果为空，继续找
					else return list;
				}
			}
		}
		return new ArrayList<ISysUser>();
	}

	@Override
	public String getTitle() {
		return "部门的上级类型部门的负责人";
	}

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser,
			CalcVars vars) {
		Set<TaskExecutor> uIdSet = new HashSet<TaskExecutor>();
		List<?extends ISysUser> sysUsers = this.getExecutor(bpmNodeUser, vars);
		for (ISysUser sysUser : sysUsers) {
			uIdSet.add(TaskExecutor.getTaskUser(sysUser.getUserId().toString(),
					sysUser.getFullname()));
		}
		return uIdSet;
	}



	@Override
	public boolean supportMockModel() {
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		String orgSource=jsonObject.getString("orgSource");
		List< PreViewModel> list=new ArrayList<PreViewModel>();
		if("0".equals(orgSource)) {
			PreViewModel preViewModelStartUser=new PreViewModel();
			preViewModelStartUser.setTitle("发起人");
			preViewModelStartUser.setType(PreViewModel.START_USER);
			list.add(preViewModelStartUser);
		}
		else {
			PreViewModel preViewModelPreViewUser=new PreViewModel();
			preViewModelPreViewUser.setTitle("上一步执行人");
			preViewModelPreViewUser.setType(PreViewModel.PRE_VIEW_USER);
			list.add(preViewModelPreViewUser);
		}
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}

}
