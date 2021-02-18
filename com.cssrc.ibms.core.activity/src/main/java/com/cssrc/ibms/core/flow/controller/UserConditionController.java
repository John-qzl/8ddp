package com.cssrc.ibms.core.flow.controller;


import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.UserCondition;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.NodeUserService;
import com.cssrc.ibms.core.flow.service.UserConditionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 对象功能:用户条件配置  控制器类 
 * 开发人员:zhulongchao 
 */
@Controller

@RequestMapping("/oa/flow/userCondition/")
@Action(ownermodel=SysAuditModelType.FLOWUSER_MANAGEMENT)
public class UserConditionController extends BaseController
{
	@Resource
	private UserConditionService userConditionService;
	@Resource
	private NodeUserService nodeUserService;
	@Resource
	private NodeSetService nodeSetService;
	
	
	
	/**
	 * 添加或更新用户条件配置 。
	 * @param request
	 * @param response
	 * @param bpmUserCondition 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(ownermodel=SysAuditModelType.FLOWUSER_MANAGEMENT,description="添加或更新用户条件配置",
			detail="<#if 0==conditionId!0>添加<#else>更新</#if>" +
					"流程定义【${SysAuditLinkService.getDefinitionLink(actDefId)}】节点【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】的用户条件配置 ",
			exectype = SysAuditExecType.UPDATE_TYPE
	)
	@DataNote(beanName = { UserCondition.class }, pkName = "conditionId")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		Long defId=RequestUtil.getLong(request, "defId");
		Long conditionId=RequestUtil.getLong(request, "conditionId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		String condition = RequestUtil.getString(request, "condition");
		String users = request.getParameter("users");
		String conditionShow = request.getParameter("conditionShow");
		Integer conditionType = RequestUtil.getInt(request, "conditionType",0);
		Long sn = RequestUtil.getLong(request, "sn");
		String formIdentity = RequestUtil.getString(request, "formIdentity");
		 String parentActDefId = RequestUtil.getString(request, "parentActDefId", "");
		if (StringUtil.isEmpty(users)) {
		      users = "";
		    }
		 if (StringUtil.isEmpty(conditionShow)) {
		      conditionShow = "";
		    }
		
		String resultMsg = null;
		UserCondition bpmUserCondition = null;
		try{
			if(conditionId==null||conditionId==0){
				bpmUserCondition=new UserCondition();
				
			}else{
				bpmUserCondition=userConditionService.getById(conditionId);
			}
			
			bpmUserCondition.setNodeid(nodeId);
			bpmUserCondition.setActdefid(actDefId);
			bpmUserCondition.setSn(sn);
			bpmUserCondition.setCondition(condition);
			bpmUserCondition.setFormIdentity(formIdentity);
			//如果节点不为空,获取setId设置到用户条件当中。
			if(StringUtil.isNotEmpty(nodeId)){
				  NodeSet bpmNodeSet = null;
			        if (StringUtil.isEmpty(parentActDefId))
			          bpmNodeSet = nodeSetService.getByDefIdNodeId(defId, nodeId);
			        else {
			          bpmNodeSet = nodeSetService.getByDefIdNodeId(defId, nodeId, parentActDefId);
			        }
				if(BeanUtils.isNotEmpty(bpmNodeSet)){
					bpmUserCondition.setSetId(bpmNodeSet.getSetId());
				}
			}
			
			bpmUserCondition.setConditionShow(conditionShow);
			bpmUserCondition.setConditionType(conditionType);
			
			
			userConditionService.saveConditionAndUser(bpmUserCondition, users);
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			e.printStackTrace();
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 UserCondition 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
    protected UserCondition getFormObject(HttpServletRequest request) throws Exception {
    
    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
    
		String json=RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);
		
		UserCondition bpmUserCondition = (UserCondition)JSONObject.toBean(obj, UserCondition.class);
		
		return bpmUserCondition;
    }
	
	/**
	 * 取得用户条件配置 分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
//	@Action(description="查看用户条件配置分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<UserCondition> list=userConditionService.getAll(new QueryFilter(request,"bpmUserConditionItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmUserConditionList",list);
		
		return mv;
	}
	
	/**
	 * 删除用户条件配置 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(ownermodel=SysAuditModelType.FLOWUSER_MANAGEMENT,description="删除用户条件配置 ",
	execOrder=ActionExecOrder.BEFORE,
	detail="<#list id?split(\",\") as item>" +
				"<#assign entity=userConditionService.getById(Long.valueOf(item))/>" +
				"删除流程定义【${sysAuditLinkService.getDefinitionLink(entity.actdefid)}】" +
				"节点【${sysAuditLinkService.getNodeName(entity.actdefid,entity.nodeid)}】的用户条件配置 "+
			"</#list>",
	exectype = SysAuditExecType.DELETE_TYPE
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			for(int i=0;i<lAryId.length;i++){
				nodeUserService.delByConditionId(lAryId[i]);
			}			
			userConditionService.delByIds(lAryId);
			message=new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.bpmUserCondition")));
		}catch(Exception ex){
			ex.printStackTrace();
			message=new ResultMessage(ResultMessage.Fail, getText("record.delete.fail",getText("controller.bpmUserCondition"))+ ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 	编辑用户条件配置 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑用户条件配置")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		UserCondition bpmUserCondition=userConditionService.getById(id);
		
		return getAutoView().addObject("bpmUserCondition",bpmUserCondition).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得用户条件配置明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
//	@Action(description="查看用户条件配置明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		UserCondition bpmUserCondition = userConditionService.getById(id);	
		return getAutoView().addObject("bpmUserCondition", bpmUserCondition);
	}
	
	/**
	 * 添加或更新用户条件配置 。
	 * @param request
	 * @param response
	 * @param bpmUserCondition 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("updateSn")
	@Action(description="添加或更新用户条件配置")
	public void updateSn(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		
		String conditionIds=RequestUtil.getString(request, "conditionIds");
		String[] aryConditions=conditionIds.split(",");
		String resultMsg=null;		

		try{
			for(int i=0;i<aryConditions.length;i++){
				long lId=Long.parseLong(aryConditions[i]);
				UserCondition bpmUserCondition=userConditionService.getById(lId);
				bpmUserCondition.setSn((long)i);
				userConditionService.update(bpmUserCondition);
			}
			resultMsg=getText("record.updated",getText("controller.bpmUserCondition"));

			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
		
//		Long currentId=RequestUtil.getLong(request, "currentId");
//		Long currentSn=RequestUtil.getLong(request, "currentSn");
//		Long otherId=RequestUtil.getLong(request, "otherId");
//		Long otherSn=RequestUtil.getLong(request, "otherSn");
//		
//		String resultMsg=null;		
//
//		try{
//			
//				UserCondition bpmUserCondition=bpmUserConditionService.getById(currentId);		
//				UserCondition  otherCondition=bpmUserConditionService.getById(otherId);
//				if(bpmUserCondition==null || otherCondition==null)
//					throw new Exception("获取不到对象");
//				if(currentSn==otherSn){
//					otherSn++;
//				}
//				bpmUserCondition.setSn(currentSn);
//			    bpmUserConditionService.update(bpmUserCondition);
//			
//			    otherCondition.setSn(otherSn);
//			    bpmUserConditionService.update(otherCondition);
//			    
//				resultMsg=getText("TT_BPM_USER_CONDITION");
//
//			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
//		}catch(Exception e){
//			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
//		}
	}
	
	/**
	 * 删除用户条件配置 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("delByAjax")
	@ResponseBody
	@Action(description="删除消息抄送中的用户条件配置 ",
			execOrder=ActionExecOrder.BEFORE,
			detail="删除" +
			"<#list StringUtils.split(id,\",\") as item>" +
				"<#assign entity=userConditionService.getById(Long.valueOf(item))/>" +
				" 流程定义【${SysAuditLinkService.getDefinitionLink(entity.actdefid)}】" +
				"<#if StringUtils.isNotEmpty(entity.nodeid)>" +
					"节点【${SysAuditLinkService.getNodeName(entity.actdefid,entity.nodeid)}】" +
				"</#if>" +
				"的用户条件配置 "+
			"</#list>"
	)
	public String delByAjax(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		JSONObject jsonObject = new JSONObject();
		try{
			Long[] lAryId =RequestUtil.getLongAryByStr(request, "id");
			userConditionService.delConditionById(lAryId);
			jsonObject.accumulate("result", true)
					  .accumulate("message", getText("record.deleted",getText("controller.bpmUserCondition")));
		}catch(Exception ex){
			String failMsg = getText("record.delete.fail",getText("controller.bpmUserCondition")) + ex.getMessage();
			jsonObject.accumulate("result", false)
			          .accumulate("message", failMsg);
		}
		return jsonObject.toString();
	}
	
	@RequestMapping("updateGroup")
	@Action(description="添加或更新用户条件配置 分组号",
			detail="更新" +
			"<#list StringUtils.split(conditionIds,\",\") as item>" +
				"<#assign entity=userConditionService.getById(Long.valueOf(item))/>" +
				"流程定义【${SysAuditLinkService.getDefinitionLink(entity.actdefid)}】" +
				"节点【${SysAuditLinkService.getNodeName(entity.actdefid,entity.nodeid)}】的用户条件配置 的分组号"+
			"</#list>"
	)
	public void updateGroup(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		
		String ids = RequestUtil.getString(request, "conditionIds");
		String groupNos = RequestUtil.getString(request, "groupNos");
		String[] aryConditionId=ids.split(",");
		String[] arygroupNo=groupNos.split(",");
		String resultMsg=null;		
		try{
			for(int i=0;i<aryConditionId.length;i++){
				String idStr = aryConditionId[i];
				Long id = Long.parseLong(idStr);
				Integer groupNo = Integer.parseInt(arygroupNo[i]);
			
				UserCondition bpmUserCondition=userConditionService.getById(id);
				bpmUserCondition.setGroupNo(groupNo);
				userConditionService.update(bpmUserCondition);
				resultMsg=getText("record.updated",getText("controller.bpmUserCondition.updateGroup"));
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
}
