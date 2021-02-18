package com.cssrc.ibms.core.activity.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.beans.factory.UnsatisfiedDependencyException;

import com.cssrc.ibms.api.activity.model.ITaskExecutor;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.util.appconf.AppUtil;

/**
 * 任务执行人
 * @author zhulongchao
 *
 */
public class TaskExecutor implements Serializable,ITaskExecutor{
	private static final long serialVersionUID = 10001L;
	/**
	 * 执行人的类型为。
	 * type:
	 * user，用户
	 * org,组织
	 * role,角色
	 * pos,岗位
	 */
	private String type="user";
	
	//如果type==user 则需要求出每个用户的主组织
	private String mainOrgName;
	
	/**
	 * 执行者ID
	 */
	private String executeId="";
	
	/**
	 * 执行人name。
	 */
	private String executor="";
	
	/** 
	* @Fields kusinessKey : TODO(记录业务数据主键) 
	*/ 
	private String businessKey;
	
	/** 
	* @Fields backSub : TODO(是否驳回子流程) 
	*/ 
	private boolean backSub;

	/**
	 * 抽取类型。
	 * 0:默认不抽取。
	 * 1:抽取
	 * 2:二级抽取
	 * 3:用户分组
	 */
	private int exactType=0;
	
	public TaskExecutor(){}
	
	/**
	 * 构造函数
	 * @param executeId		用户ID
	 */
	public TaskExecutor(String executeId){
		Long userId = Long.parseLong(executeId);
		ISysUserService sysUserService = (ISysUserService)AppUtil.getBean(ISysUserService.class);
		ISysUser sysUser = sysUserService.getById(userId);
		this.executeId = executeId;
		this.executor = sysUser.getFullname();
	}
    
    /**
     * 构造函数
     * @param sysUser
     */
    public TaskExecutor(ISysUser sysUser)
    {
        this.executeId = sysUser.getUserId().toString();
        this.executor = sysUser.getFullname();
    }
	/**
	 * 构造函数
	 * @param type			人员类型(user,pos,org,role)
	 * @param executeId		对应的id
	 * @param name			对应的名称
	 */
	public TaskExecutor(String type,String executeId,String name){
		this.type=type;
		this.executeId=executeId;
		this.executor=name;
		if(USER_TYPE_USERGROUP.equalsIgnoreCase(type)){
			this.exactType=EXACT_USER_GROUP;
		}
	}
	
	/**
	 * 获取任务用户。
	 * @param executeId
	 * @return
	 */
	public static TaskExecutor getTaskUser(String executeId,String name){
		return new TaskExecutor(USER_TYPE_USER,executeId,name);
	}
	
	/**
	 * 获取组织执行人。
	 * @param executeId
	 * @return
	 */
	public static TaskExecutor getTaskOrg(String executeId,String name){
		return new TaskExecutor(USER_TYPE_ORG,executeId, name);
	}
	
	/**
	 * 获取角色任务。
	 * @param executeId
	 * @return
	 */
	public static TaskExecutor getTaskRole(String executeId,String name){
		return new TaskExecutor(USER_TYPE_ROLE,executeId,name);
	}
	
	
	/**
	 * 获取职务任务。
	 * @param executeId
	 * @return
	 */
	
	public static TaskExecutor getTaskJob(String executeId,String name){
		return new TaskExecutor(USER_TYPE_JOB,executeId,name);
	}
	
	
	/**
	 * 获取岗位。
	 * @param executeId
	 * @return
	 */
	public static TaskExecutor getTaskPos(String executeId,String name){
		return new TaskExecutor(USER_TYPE_POS,executeId,name);
	}
	
	
	/**
	 * 获取用户分组。
	 * @param executeId
	 * @param name
	 * @return
	 */
	public static TaskExecutor getTaskUserGroup(String executeId,String name){
		TaskExecutor ex= new TaskExecutor(USER_TYPE_USERGROUP,executeId,name);
		ex.setExactType(EXACT_USER_GROUP);
		return ex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getExecuteId() {
		return executeId;
	}

	public void setExecuteId(String executeId) {
		this.executeId = executeId;
	}
	
	public String getExecutor() {
		return executor;
	}

	public void setExecutor(String executor) {
		this.executor = executor;
	}
	
	

	public int getExactType() {
		return exactType;
	}

	public void setExactType(int exactType) {
		this.exactType = exactType;
	}

	@Override
	public boolean equals(Object obj){
		if(! (obj instanceof TaskExecutor)){
			return false;
		}
		TaskExecutor tmp=(TaskExecutor)obj;
		if(this.type.equals(tmp.getType()) && this.executeId.equals(tmp.getExecuteId())){
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode(){
		String tmp=this.type +this.executeId;
		return tmp.hashCode();
	}
	@JsonIgnore
	public Set<ISysUser> getSysUser() throws UnsatisfiedDependencyException{
	    if(StringUtils.isEmpty(executeId)) {
	        return null;
	    }
		Set<ISysUser> sysUsers = new HashSet<ISysUser>();
		if(AppUtil.getContext()==null){
			throw new UnsatisfiedDependencyException("Convert Executor to ISysUser dependency ApplicationContext", "applicationContext", "", "Convert Executor to ISysUser dependency ApplicationContext");
		}
		if(USER_TYPE_USER.equals(type)){
			ISysUserService sysUserService = (ISysUserService) AppUtil.getBean(ISysUserService.class);
			ISysUser sysUser = sysUserService.getById(Long.valueOf(executeId));
			sysUsers.add(sysUser);
		}else if(USER_TYPE_ORG.equals(type)){
			ISysUserService sysUserService = (ISysUserService) AppUtil.getBean(ISysUserService.class);
			List<?extends ISysUser> users = sysUserService.getByOrgId(Long.valueOf(executeId));
			sysUsers.addAll(users);
		}else if(USER_TYPE_POS.equals(type)){
			ISysUserService sysUserService = (ISysUserService) AppUtil.getBean(ISysUserService.class);
			List<?extends ISysUser> users = sysUserService.getByPosId(Long.valueOf(executeId));
			sysUsers.addAll(users);
		}else if(USER_TYPE_ROLE.equals(type)){
			ISysUserService sysUserService = (ISysUserService) AppUtil.getBean(ISysUserService.class);
			List<?extends ISysUser> users = sysUserService.getByRoleId(Long.valueOf(executeId));
			sysUsers.addAll(users);
		}
		return sysUsers;
	}

	public String getMainOrgName() {
		return mainOrgName;
	}

	public void setMainOrgName(String mainOrgName) {
		this.mainOrgName = mainOrgName;
	}

    public String getBusinessKey()
    {
        return businessKey;
    }

    public void setBusinessKey(String businessKey)
    {
        this.businessKey = businessKey;
    }

    public void setBackSub(boolean backSub)
    {
        this.backSub=backSub;
    }

    public boolean getBackSub()
    {
        return this.backSub;
    }
}
