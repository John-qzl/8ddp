package com.cssrc.ibms.core.flow.service.impl;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.CalcVars;
import com.cssrc.ibms.core.util.string.StringUtil;

import net.sf.json.JSONObject;


/**
 * 根据表单变量计算人员。
 * <pre>
 * 人员的JSON格式如下：
 * {type:'user',varName:'name'},若为普通变量，则JSON格式为：{type:'user',userType:'userType',varName:'name'}
 * type:可能的值为
 * 	1,用户
 * 	2,组织
 * 	3,组织负责人
 * 	4,角色
 * 	5,岗位
 *  6,普通变量
 * </pre>
 * @author zhulongchao
 *
 */
public class NodeUserCalculationFormVar implements INodeUserCalculation {
	@Resource
	private ISysUserService sysUserService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ISysRoleService sysRoleService;
	@Resource
	private IPositionService positionService;
	
	
	@Override
	public List<?extends ISysUser> getExecutor(NodeUser bpmNodeUser, CalcVars vars) {
		List<?extends ISysUser> list=new ArrayList<ISysUser>();
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		int type=jsonObject.getInt("type");
		if(type==6) {//普通变量
			type = jsonObject.getInt("userType");
		}
		Object ids = getFormVar(vars, jsonObject, type);
		if(ids==null) return list;
		String executorIds=ids.toString();
		
		switch(type){
			//用户
			case 1:
				list= getByUsers(executorIds);
				break;
			//组织
			case 2:
				list=getByOrgIds(executorIds);
				break;
			//组织负责人
			case 3:
				list=getMgrByOrgIds(executorIds);
				break;
			//角色
			case 4:
				list= getByRoleIds(executorIds);
				break;
			//岗位
			case 5:
				list= getByPos(executorIds);
				break;
		}
		return list;
	}
	
	
	

	@Override
	public Set<TaskExecutor> getTaskExecutor(NodeUser bpmNodeUser,CalcVars vars) {
		int extractUser=bpmNodeUser.getExtractUser().intValue();
		
		Set<TaskExecutor> userSet=new LinkedHashSet<TaskExecutor>();
		
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		Object ids = null;
		int type=jsonObject.getInt("type");
		if(type==6) {//普通变量
			type = jsonObject.getInt("userType");
			String varName=jsonObject.getString("varName");
			ids = vars.getVariable(varName);
		}
		else {
			ids = getFormVar(vars, jsonObject, type);
		}
		if(ids==null) return userSet;
		String executorIds=ids.toString();
		
		switch(type){
			//用户
			case 1:
				List<ISysUser> userList= getByUsers(executorIds);
				userSet=NodeUserUtil.getExcutorsByUsers(userList, extractUser);
				break;
			//组织
			case 2:
				switch (extractUser) {
					//不抽取
					case TaskExecutor.EXACT_NOEXACT :
						userSet =getTaskExecutor(2,executorIds,extractUser);
						break;
					//抽取
					case TaskExecutor.EXACT_EXACT_USER:
						List<?extends ISysUser> orgUserList=getByOrgIds(executorIds);
						userSet=getTaskExecutorByUserList(orgUserList);
						break;
					//二级抽取
					case TaskExecutor.EXACT_EXACT_SECOND:
						userSet =getTaskExecutor(2,executorIds,extractUser);
						break;
				}
				
				break;
			//组织负责人
			case 3:
				List<?extends ISysUser> orgUserList=getMgrByOrgIds(executorIds);
				userSet=getTaskExecutorByUserList(orgUserList);
				break;
			//角色
			case 4:
				switch (extractUser) {
					//不抽取
					case TaskExecutor.EXACT_NOEXACT :
						userSet =getTaskExecutor(4,executorIds,extractUser);
						break;
					//抽取
					case TaskExecutor.EXACT_EXACT_USER:
						List<?extends ISysUser> roleUserList=getByRoleIds(executorIds);
						userSet=getTaskExecutorByUserList(roleUserList);
						break;
					//二级抽取
					case TaskExecutor.EXACT_EXACT_SECOND:
						userSet =getTaskExecutor(4,executorIds,extractUser);
						break;
				}
				break;
			//岗位
			case 5:
				switch (extractUser) {
					//不抽取
					case TaskExecutor.EXACT_NOEXACT :
						userSet =getTaskExecutor(5,executorIds,extractUser);
						break;
					//抽取
					case TaskExecutor.EXACT_EXACT_USER:
						List<?extends ISysUser> posUserList=getByPos(executorIds);
						userSet=getTaskExecutorByUserList(posUserList);
						break;
					//二级抽取
					case TaskExecutor.EXACT_EXACT_SECOND:
						userSet =getTaskExecutor(5,executorIds,extractUser);
						break;
				}
				break;
			
		}
		return userSet;
	}



	private Object getFormVar(CalcVars vars, JSONObject jsonObject, int type) {
	    Object reLPK_=vars.getVariable(BpmConst.SUBFLOW_BUSINESSKEY);
	    if(reLPK_==null) {
	        reLPK_=vars.getVariable(BpmConst.FLOW_BUSINESSKEY);
	    }
	    String reLPK=reLPK_!=null?reLPK_.toString():null;
		String varName=jsonObject.getString("varName")+IFormField.FIELD_HIDDEN;
		Object ids=vars.getVariable(varName);
	    Object relIds=vars.getVariable(reLPK+"."+varName);

		if(ids!=null){
		    return ids;
		}else if(relIds!=null){
		    return relIds;
		} 
		//获取预览参数
		switch(type){
			//用户
			case 1:
				varName=BpmConst.PREVIEW_FORMUSER;
				break;
			//组织
			case 2:
			case 3:
				varName=BpmConst.PREVIEW_FORMORG;
				break;
			//角色
			case 4:
				varName=BpmConst.PREVIEW_FORMROLE;
				break;
			//岗位
			case 5:
				varName=BpmConst.PREVIEW_FORMPOS;
				break;
		}
		ids=vars.getVariable(varName);

		return ids;
	}
	
	

	@Override
	public String getTitle() { 
		return "人员表单变量";
	}

	@Override
	public boolean supportMockModel() {
		
		return true;
	}

	@Override
	public List<PreViewModel> getMockModel(NodeUser bpmNodeUser) {
		String cmpIds=bpmNodeUser.getCmpIds();
		JSONObject jsonObject=JSONObject.fromObject(cmpIds);
		int type=jsonObject.getInt("type");
		if(type==6){//普通变量
			type = jsonObject.getInt("userType");
		}
		List<PreViewModel> list=new ArrayList<PreViewModel>();
		switch (type) {
			case 1:
				PreViewModel userVar=new PreViewModel();
				userVar.setType(PreViewModel.FORM_USER);
				list.add(userVar);
				break;
			case 2:
			case 3:
				PreViewModel orgVar=new PreViewModel();
				orgVar.setType(PreViewModel.FORM_ORG);
				list.add(orgVar);
				break;
			case 4:
				PreViewModel roleVar=new PreViewModel();
				roleVar.setType(PreViewModel.FORM_ROLE);
				list.add(roleVar);
				break;
			case 5:
				PreViewModel posVar=new PreViewModel();
				posVar.setType(PreViewModel.FORM_POS);
				list.add(posVar);
				break;
			case 7:
				PreViewModel jobVar=new PreViewModel();
				jobVar.setType(PreViewModel.FORM_JOB);
				list.add(jobVar);
				break;	
		}
		return list;
	}

	@Override
	public boolean supportPreView() {
		return true;
	}
	
	private List<ISysUser> getByUsers(String uids){
		String[] aryUid = uids.split("[,]");
//		Set<String> uidSet = new LinkedHashSet<String>(Arrays.asList(aryUid));
//		return sysUserService.getByIdSet(uidSet);
		List<ISysUser> sysUsers = new ArrayList<ISysUser>();
		for(String userId : aryUid){
			sysUsers.add(sysUserService.getById(Long.valueOf(userId)));
		}
		return sysUsers;
	}
	/**
	 * 根据组织ID获取人员。
	 * @param ids
	 * @return
	 */
	private List<?extends ISysUser> getByOrgIds(String ids){
		//过滤重复orgid
		List list = StringUtil.getListByStr(ids);
		return sysUserService.getByOrgIds(list); 
	}
	
	/**
	 * 根据组织id获取组织负责人。
	 * @param ids
	 * @return
	 */
	private List<?extends ISysUser> getMgrByOrgIds(String ids){
		List list = StringUtil.getListByStr(ids);
		return this.sysUserService.getMgrByOrgIds(list);
	}
	
	/**
	 * 根据角色Id获取用户列表。
	 * @param ids
	 * @return
	 */
	private List<?extends ISysUser> getByRoleIds(String ids){
		List list = StringUtil.getListByStr(ids);
		return sysUserService.getByRoleIds(list);
	}

	private List<?extends ISysUser> getByPos(String ids){
		List list = StringUtil.getListByStr(ids);
		return sysUserService.getByPos(list);
	}
	
	/**
	 * 根据用户列表获取执行人。
	 * @param list
	 * @return
	 */
	private Set<TaskExecutor> getTaskExecutorByUserList(List<?extends ISysUser> list){
		
		Set<TaskExecutor> set=new  LinkedHashSet<TaskExecutor>();
		
		for(ISysUser sysUser:list){
			TaskExecutor taskExecutor=TaskExecutor.getTaskUser(sysUser.getUserId().toString(),sysUser.getFullname()) ;
			set.add(taskExecutor);
		}
		return set;
	}
	
	
	
	
	/**
	 * 取得任务执行人。
	 * @param type			执行人类型(2,组织,4,角色,5,岗位)
	 * @param cmpIds		id数据。
	 * @param extractType	抽取类型。
	 * @return
	 */
	private Set<TaskExecutor> getTaskExecutor(int type,String cmpIds,int extractType){
		Set<TaskExecutor> set=new  LinkedHashSet<TaskExecutor>();
		String[] aryIds = cmpIds.split("[,]");
		TaskExecutor taskExecutor=null;
		//2 组织,4角色,5 岗位
		for (int i = 0; i < aryIds.length; i++) {
			if(type==2){
				ISysOrg org=sysOrgService.getById(new Long(aryIds[i]));
				taskExecutor=TaskExecutor.getTaskOrg(aryIds[i],org.getOrgName()) ;
			}
			else if(type==4){
				ISysRole role=sysRoleService.getById(new Long(aryIds[i]));
				taskExecutor=TaskExecutor.getTaskRole(aryIds[i],role.getRoleName()) ;
			}
			else if(type==5){
				IPosition pos=positionService.getById(new Long(aryIds[i]));
				taskExecutor=TaskExecutor.getTaskPos(aryIds[i],pos.getPosName()) ;
			}
			taskExecutor.setExactType(extractType);
			set.add(taskExecutor);
		}
		return set;
	}

	
}