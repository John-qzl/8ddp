package com.cssrc.ibms.core.flow.tag;
 

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;

import net.sf.json.JSONArray;

import org.apache.commons.lang.StringUtils;

import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.flow.service.TaskUserService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 用户标签
 * @author zhulongchao
 *
 */
public class UserNameTag extends BodyTagSupport {	
	/** */
	private static final long serialVersionUID = 1L;

	/** 用户ID */
	private Long userId=0L;
	
	/** 任务ID */
	private String taskId="";
	
	/**是否显示图标 */
	private Boolean isIcon= true;
	

	public int doStartTag() throws JspTagException {
		return EVAL_BODY_BUFFERED;
	}
	
	
	public int doEndTag() throws JspTagException {
		try {
			String str = getUserName();
			pageContext.getOut().print(str);
		} catch (Exception e) {
			e.printStackTrace();
			//throw new JspTagException(e.getMessage());
		}
		return SKIP_BODY;
	}
	

	/**
	 * 获取用户的名称
	 * @return
	 */
	private String getUserName(){
		HttpServletRequest request=(HttpServletRequest) pageContext.getRequest() ;
		String ctx=request.getContextPath();
		String noData = "暂无";
	
		StringBuffer sb= new StringBuffer();     
		String imgSrc = "<span class=\"iconfont icon-bussinessman\"></span>";
		//String imgSrc = "<img src='" + ctx + "/styles/images/user.png'>&nbsp;" ;
		
		if(this.userId!=null&&this.userId!=0){	//通过用户Id获取用户名称
			ISysUserService sysUserService=(ISysUserService)AppUtil.getBean("sysUserService");
			ISysUser user=sysUserService.getById(new Long(userId));	
			if(user == null)
				return  noData;
			String userName = user.getFullname();
			if(StringUtils.isEmpty(user.getFullname()))
				userName = user.getUsername();
			String url = ctx+"/oa/system/sysUser/get.do?userId="+this.userId+"&canReturn=1&hasClose=true";	
		
			if(isIcon)//是否显示图标
				sb.append(imgSrc);
			sb.append("<a  href='#' onclick=\"javasrcipt:$.ligerDialog.open({height:800,width: 1000,title : '用户明细',url:'").append(url).append("', isResize: true})\">").append(userName).append("</a>");	
		}else if(StringUtil.isNotEmpty(this.taskId)){// 取到任务的侯选人员
			TaskUserService taskUserService = (TaskUserService)AppUtil.getBean("taskUserService") ;
			Set<TaskExecutor> candidateUsers = taskUserService.getCandidateExecutors(taskId);
			if(candidateUsers.size()>5){
				sb.append("<a  href='#' onclick='return false;' taskId='"+
						taskId+"' >执行人</a><input type='hidden' name='candidateUsersJson' value='"+
						JSONArray.fromObject(candidateUsers)+"'>");
			}else{
				String orgUrl  =  ctx+"/oa/system/userPosition/getUserListByOrgId.do?orgId=";
				String posUrl  =  ctx+"/oa/system/sysUser/getUserListByPosId.do?posId=";
				String roleUrl =  ctx+"/oa/system/userRole/getUserListByRoleId.do?roleId=";	
				String jobUrl =  ctx+"/oa/system/sysUser/getUserListByJobId.do?jobId=";
				
				for(TaskExecutor te:candidateUsers){
					String targetUrl = "" ;
					if(isIcon)//是否显示图标
						sb.append(imgSrc);
					if(TaskExecutor.USER_TYPE_ORG.equals(te.getType())){
						targetUrl = orgUrl+te.getExecuteId();
					}else if(TaskExecutor.USER_TYPE_POS.equals(te.getType())){
						targetUrl = posUrl+te.getExecuteId();
					}else if(TaskExecutor.USER_TYPE_ROLE.equals(te.getType())){
						targetUrl = roleUrl+te.getExecuteId();
					}else if(TaskExecutor.USER_TYPE_JOB.equals(te.getType())){
						targetUrl = jobUrl+te.getExecuteId();
					}else if(TaskExecutor.USER_TYPE_USER.equals(te.getType())){
						sb.append("<a href='"+ctx+"/oa/system/sysUser/get.do?userId="+te.getExecuteId()+"&canReturn=1&hasClose=true' target='_blank'>"+te.getExecutor()+"</a>");
					}
					if(StringUtils.isNotEmpty(targetUrl))
						sb.append("<a href='####' onclick='return false;' taskId='"+taskId+"' targetUrl='"+targetUrl+"'>"+te.getExecutor()+"</a>");
				}
			}
		}
		return StringUtils.isEmpty(sb.toString())?noData:sb.toString();
	}

	//=========getting and setting=============

	public void setTaskId(String taskId) {
		this.taskId = taskId;
	}
	
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	
	public void setIsIcon(Boolean isIcon) {
		this.isIcon = isIcon;
	}
}

