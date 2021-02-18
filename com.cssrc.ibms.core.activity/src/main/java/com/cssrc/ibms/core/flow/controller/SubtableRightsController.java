package com.cssrc.ibms.core.flow.controller;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.SubtableRights;
import com.cssrc.ibms.core.flow.service.SubtableRightsService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 对象功能:子表权限 控制器类
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/subtableRights/")
public class SubtableRightsController extends BaseController
{
	@Resource
	private SubtableRightsService subtableRightsService;
	
	/**
	 * 添加或更新子表权限。
	 * @param request
	 * @param response
	 * @param SubtableRights 添加或更新的实体
	 * @param bindResult
	 * @param viewName
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@Action(description="添加或更新子表权限")
	public void save(HttpServletRequest request, HttpServletResponse response, SubtableRights bpmSubtableRights) throws Exception
	{
		String resultMsg=null;
		 String parentActDefId = RequestUtil.getString(request, "parentActDefId", "");
//		SubtableRights bpmSubtableRights1=getFormObject(request);
		try{
			SubtableRights bpmSubtableRights2 = null;
			if (StringUtil.isEmpty(parentActDefId)){
				bpmSubtableRights2 = 
					subtableRightsService.getByDefIdAndNodeId(
							bpmSubtableRights.getActdefid(), 
							bpmSubtableRights.getNodeid(), 
							bpmSubtableRights.getTableid());
			}else{
				bpmSubtableRights2 = 
					subtableRightsService.getByDefIdAndNodeId(
							bpmSubtableRights.getActdefid(), 
							bpmSubtableRights.getNodeid(), 
							bpmSubtableRights.getTableid(),parentActDefId);
			} 
			
			if(bpmSubtableRights2==null){
				bpmSubtableRights.setId(UniqueIdUtil.genId());
				subtableRightsService.add(bpmSubtableRights);
				resultMsg=getText("record.added",getText("controller.bpmSubtableRights"));
			}else{
				bpmSubtableRights.setId(bpmSubtableRights2.getId());
				subtableRightsService.update(bpmSubtableRights);
				resultMsg=getText("record.updated",getText("controller.bpmSubtableRights"));
			}
			writeResultMessage(response.getWriter(),resultMsg,ResultMessage.Success);
		}catch(Exception e){
			writeResultMessage(response.getWriter(),resultMsg+","+e.getMessage(),ResultMessage.Fail);
		}
	}
	
	/**
	 * 取得 SubtableRights 实体 
	 * @param request
	 * @return
	 * @throws Exception
	 */
//    protected SubtableRights getFormObject(HttpServletRequest request, SubtableRights bpmSubtableRights) throws Exception {
//    
//    	JSONUtils.getMorpherRegistry().registerMorpher(new DateMorpher((new String[] { "yyyy-MM-dd" })));
//    	
//		
//		
//		return SubtableRights;
//    }
	
	/**
	 * 取得子表权限分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看子表权限分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<SubtableRights> list=subtableRightsService.getAll(new QueryFilter(request,"SubtableRightsItem"));
		ModelAndView mv=this.getAutoView().addObject("SubtableRightsList",list);
		
		return mv;
	}
	
	/**
	 * 删除子表权限
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除子表权限")
	public void del(HttpServletRequest request, HttpServletResponse response, SubtableRights bpmSubtableRights) throws Exception
	{
//		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage message=null;
		try{
			
			subtableRightsService.delById(bpmSubtableRights.getId());
			
			message=new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.bpmSubtableRights")));
		}catch(Exception ex){
			message=new ResultMessage(ResultMessage.Fail,  getText("record.delete.fail",getText("controller.bpmSubtableRights")) + ex.getMessage());
		}
//		addMessage(message, request);
//		response.sendRedirect(preUrl);
//		writeResultMessage(response.getWriter(),message,ResultMessage.Success);
		response.getWriter().append(message.toString());
	}
	
	/**
	 * 	编辑子表权限
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description="编辑子表权限")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long id=RequestUtil.getLong(request,"id");
		String returnUrl=RequestUtil.getPrePage(request);
		SubtableRights SubtableRights=subtableRightsService.getById(id);
		
		return getAutoView().addObject("SubtableRights",SubtableRights).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得子表权限明细
	 * @param request   
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看子表权限明细")
	public void get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String actdefid = RequestUtil.getString(request,"actdefid");
		String nodeid = RequestUtil.getString(request,"nodeid");
		long tableid=RequestUtil.getLong(request,"tableid");
		String parentActDefId = RequestUtil.getString(request, "parentActDefId", "");
		SubtableRights bpmSubtableRights =null;
		if (StringUtil.isEmpty(parentActDefId)){
			bpmSubtableRights = subtableRightsService.getByDefIdAndNodeId(actdefid, nodeid, tableid);
		}else{
			bpmSubtableRights = subtableRightsService.getByDefIdAndNodeId(actdefid, nodeid, tableid,parentActDefId);
		}
		
		StringBuffer sb = new StringBuffer("{success:true");
		if(bpmSubtableRights!=null){
			sb.append(",\"id\":\"").append(bpmSubtableRights.getId()).append("\",")
			.append("\"permissiontype\":").append(bpmSubtableRights.getPermissiontype()).append(",")
			.append("\"permissionseting\":\"")
			.append(bpmSubtableRights.getPermissionseting()!=null?
					bpmSubtableRights.getPermissionseting().replaceAll("\n", "<br>").replaceAll("[\"]", "<032>"):"")
			.append("\"");
		}
		sb.append("}");
		response.getWriter().print(sb.toString());
	}
}
