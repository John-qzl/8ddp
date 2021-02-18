package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.job.intf.IJobService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation;
import com.cssrc.ibms.core.flow.intf.INodeUserCalculation.PreViewModel;
import com.cssrc.ibms.core.flow.model.NodeUser;
import com.cssrc.ibms.core.flow.service.NodeUserCalculationSelector;
import com.cssrc.ibms.core.flow.service.NodeUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 对象功能: NodeUser控制器类 
 * 开发人员: zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeUser/")
public class NodeUserController extends BaseController
{
	@Resource
	private NodeUserService nodeUserService;
	@Resource 
	private IFormFieldService formFieldService;
	@Resource
	private NodeUserCalculationSelector nodeUserCalculationSelector;
	@Resource
    private IJobService jobService;
	
	/**
	 * 取得分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		List<NodeUser> list=nodeUserService.getAll(new QueryFilter(request,"bpmNodeUserItem"));
		ModelAndView mv=this.getAutoView().addObject("bpmNodeUserList",list);
		return mv;
	}
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		Long[] lAryId =RequestUtil.getLongAryByStr(request, "userNodeId");
		nodeUserService.delByIds(lAryId);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{
		Long userNodeId=RequestUtil.getLong(request,"userNodeId");
		String returnUrl=RequestUtil.getPrePage(request);
		NodeUser bpmNodeUser=null;
		if(userNodeId!=null){
			 bpmNodeUser= nodeUserService.getById(userNodeId);
		}else{
			bpmNodeUser=new NodeUser();
		}
		return getAutoView().addObject("bpmNodeUser",bpmNodeUser).addObject("returnUrl", returnUrl);
	}

	/**
	 * 取得InnoDB free: 11264 kB明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"userNodeId");
		NodeUser bpmNodeUser = nodeUserService.getById(id);		
		return getAutoView().addObject("bpmNodeUser", bpmNodeUser);
	}
	
	
	
	/**
	 * 对节点数据进行预览。
	 * <pre>
	 * </pre>
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("preview")
	@Action(description="查看节点用户设置")
	@ResponseBody
	@SuppressWarnings("unused")
	public List<?extends ISysUser> preview(HttpServletRequest request, HttpServletResponse response){
		
		
		Long defId = RequestUtil.getLong(request, "defId");
		Long startUserId=RequestUtil.getLong(request, "startUserId",0);
		Long preUserId=RequestUtil.getLong(request, "preUserId",0);
		
		String formUserId=RequestUtil.getString(request, "formUserId");
		String formOrgId=RequestUtil.getString(request, "formOrgId");
		String formPosId=RequestUtil.getString(request, "formPosId");
		String formRoleId=RequestUtil.getString(request, "formRoleId");
		String startOrgId=RequestUtil.getString(request, "startOrgId");
		String startPosId=RequestUtil.getString(request, "startPosId");
		String startJobId=RequestUtil.getString(request, "startJobId");
		String preOrgId=RequestUtil.getString(request, "preOrgId");
		
		
		String nodeUser=RequestUtil.getString(request, "nodeUser");
		JSONArray jsonArray=JSONArray.fromObject(nodeUser);
		
		List<NodeUser> bpmNodeUsers = new ArrayList<NodeUser>();
		//解析NodeUser配置。
		for (int i = 0; i < jsonArray.size(); i++) {
			String userJson = jsonArray.getJSONObject(i).toString();
			JSONObject jsonObj= JSONObject.fromObject(userJson);
			String cmpIds=jsonObj.getString("cmpIds");
			jsonObj.remove("cmpIds");
			NodeUser bpmNodeUser = (NodeUser) JSONObject.toBean(jsonObj, NodeUser.class);
			bpmNodeUser.setCmpIds(cmpIds);
		//	bpmNodeUser.setActDefId(bpmDefintion.getActDefId());
			bpmNodeUsers.add(bpmNodeUser);
		}
		//构建表单流程变量。
		Map<String,Object> vars=new HashMap<String, Object>();
		
		if(StringUtil.isNotEmpty(formUserId)){
			vars.put(BpmConst.PREVIEW_FORMUSER, formUserId);
		}
		if(StringUtil.isNotEmpty(formOrgId)){
			vars.put(BpmConst.PREVIEW_FORMORG, formOrgId);
		}
		if(StringUtil.isNotEmpty(formPosId)){
			vars.put(BpmConst.PREVIEW_FORMPOS, formPosId);
		}
		if(StringUtil.isNotEmpty(formRoleId)){
			vars.put(BpmConst.PREVIEW_FORMROLE, formRoleId);
		}
		if(StringUtil.isNotEmpty(startOrgId)){
			vars.put(BpmConst.START_ORG_ID, startOrgId);
		}
		if(StringUtil.isNotEmpty(startPosId)){
			vars.put(BpmConst.START_POS_ID, startPosId);
		}
		if(StringUtil.isNotEmpty(startJobId)){
			vars.put(BpmConst.START_JOB_ID, startJobId);
		}
		
		if(StringUtil.isNotEmpty(preOrgId)){
			vars.put(BpmConst.PRE_ORG_ID, preOrgId);
		}
		List<?extends ISysUser> resultList=new ArrayList<ISysUser>();
		//创建返回值。
		Map<Short,List<ISysUser>> userMap=new HashMap<Short, List<ISysUser>>();
		for(NodeUser bpmNodeUser:bpmNodeUsers){
			List<?extends ISysUser> userList =nodeUserService.getPreviewNodeUser(bpmNodeUser, startUserId, preUserId, null,  vars);
			if(BeanUtils.isEmpty(userList) ){
				continue;
			} 
			resultList=userSetCal(bpmNodeUser.getCompType(),resultList,userList);
		}
		return resultList;
	}
	
	
//	@RequestMapping("relativeCal")
//	@Action(description="组合人员计算")
//	public ModelAndView relativeCal(HttpServletRequest request, HttpServletResponse response) throws Exception{
//		Long defId=RequestUtil.getLong(request, "defId",0);
//		long userNodeId=RequestUtil.getLong(request,"nodeUserId",0);
//		
//		//显示表单变量。
//		List<IFormField> fieldList= bpmFormFieldService.getFlowVarByFlowDefId(defId);
//		List<IFormField> userVarList=new ArrayList<IFormField>();
//		List<IFormField> orgVarList=new ArrayList<IFormField>();
//		
//		for(IFormField field:fieldList){
//			Short controlType=field.getControlType();
//			if(controlType==null) continue;
//			if(field.getIsHidden()==1) continue;
//			if(controlType==IFormField.Selector_User || controlType==IFormField.Selector_UserMulti ){
//				userVarList.add(field);
//			}
//			if(controlType==IFormField.Selector_Org || controlType==IFormField.Selector_Org_single ){
//				orgVarList.add(field);
//			}
//		}
//		
//		BpmComsiteUser bpmComsiteUser=new BpmComsiteUser();
//		//获取流程变量。
//		if(userNodeId>0){
//			bpmComsiteUser=  bpmComsiteUserDao.getByNodeUserId(userNodeId);
//		}
//		ModelAndView mv=getAutoView();
//		mv.addObject("user", bpmComsiteUser);
//		mv.addObject("userVarList", userVarList);
//		mv.addObject("orgVarList", orgVarList);
//		return mv;
//	}
	
	@RequestMapping("getByUserParams")
	@Action(description="预览数据信息计算")
	@ResponseBody
	public List<PreViewModel> getByUserParams(HttpServletRequest request, HttpServletResponse response){
		List<PreViewModel> list=new ArrayList<PreViewModel>();
		String params=RequestUtil.getString(request, "paramsJson");
		JSONArray jsonArray=JSONArray.fromObject(params);
		Set<PreViewModel> set =new HashSet<PreViewModel>();
		
		for(Object obj:jsonArray){
			JSONObject jsonObj=(JSONObject)obj;
			//cmpIds有可能问JSON对象，先获取字符串。
			String str= jsonObj.getString("cmpIds");
			jsonObj.remove("cmpIds");
			NodeUser bpmNodeUser=(NodeUser)JSONObject.toBean(jsonObj, NodeUser.class);
			bpmNodeUser.setCmpIds(str);
			if(bpmNodeUser.getAssignType().equals(NodeUser.ASSIGN_TYPE_SAME_NODE)) continue;
			
			Map<String,INodeUserCalculation> map= nodeUserCalculationSelector.getNodeUserCalculation();
			INodeUserCalculation calc=map.get(bpmNodeUser.getAssignType());
			if(calc.supportMockModel()){
				List<PreViewModel> preViewModelList= calc.getMockModel(bpmNodeUser);
				if(preViewModelList!=null&&preViewModelList.size()>0)
				set.addAll(preViewModelList);
			}
		}
		if(set.size()>0){
			list.addAll(set);
		}
		return list;
	}
	
	
	@RequestMapping("formVar")
	@Action(description="用户表单变量设置")
	public ModelAndView formVar(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		Long defId=RequestUtil.getLong(request, "defId",0);
		String cmpIds=RequestUtil.getString(request, "cmpIds");
		
		int varType=0;
		String varName="";
		String userType="";
		
		if(StringUtil.isNotEmpty(cmpIds)){
			JSONObject jsonObject=JSONObject.fromObject(cmpIds);
			varType=jsonObject.getInt("type");
			varName=jsonObject.getString("varName");
			if(varType==6){
				userType=jsonObject.getString("userType");
			}
		}
			
		
		/*//显示表单变量。
		List<?extends IFormField> fieldList= formFieldService.getFlowVarByFlowDefId(defId);
		
		List<IFormField> userVarList=new ArrayList<IFormField>();
		List<IFormField> orgVarList=new ArrayList<IFormField>();
		//角色
		List<IFormField> roleVarList=new ArrayList<IFormField>();
		//岗位
		List<IFormField> posVarList=new ArrayList<IFormField>();
		
		List<IFormField> otherList=new ArrayList<IFormField>();
		for(IFormField field:fieldList){
			//if(field.getIsHidden()==1) continue;
			Short controlType=field.getControlType();
			if(controlType==null) continue;
			if( controlType==IFieldPool.SELECTOR_USER_SINGLE || controlType==IFieldPool.SELECTOR_USER_MULTI ){
				if(field.getIsHidden()!=1 )
					userVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_ORG_SINGLE || controlType==IFieldPool.SELECTOR_ORG_MULTI ){
				if(field.getIsHidden()!=1 )
					orgVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_ROLE_SINGLE || controlType==IFieldPool.SELECTOR_ROLE_MULTI ){
				if(field.getIsHidden()!=1 )
					roleVarList.add(field);
			}
			else if(controlType==IFieldPool.SELECTOR_POSITION_SINGLE || controlType==IFieldPool.SELECTOR_POSITION_MULTI ){
				if(field.getIsHidden()!=1 )
					posVarList.add(field);
			}
			else{//普通变量
				if(field.getIsFlowVar()==1) {
					otherList.add(field);
				}
			}
		}*/
		List<IFormField> userVarList=new ArrayList<IFormField>();
		List<IFormField> orgVarList=new ArrayList<IFormField>();
		//角色
		List<IFormField> roleVarList=new ArrayList<IFormField>();
		//岗位
		List<IFormField> posVarList=new ArrayList<IFormField>();
		
		List<IFormField> otherList=new ArrayList<IFormField>();
		formFieldService.getFileds(userVarList,orgVarList,roleVarList,posVarList,otherList,defId);

		ModelAndView mv=getAutoView();
		mv.addObject("userVarList", userVarList);
		mv.addObject("orgVarList", orgVarList);
		
		mv.addObject("roleVarList", roleVarList);
		mv.addObject("posVarList", posVarList);
		
		mv.addObject("varType", varType);
		mv.addObject("varName", varName);
		mv.addObject("userType", userType);
		mv.addObject("otherList", otherList);
		return mv;
		
		
	}
	
	@RequestMapping("startOrPrevDialog")
	@Action(description="用户表单变量设置")
	public ModelAndView startOrPrevDialog(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//Long defId=RequestUtil.getLong(request, "defId",0);
		String cmpIds=RequestUtil.getString(request, "cmpIds");
		
		String type="";
		String userType="";
		
		if(StringUtil.isNotEmpty(cmpIds)){
			JSONObject jsonObject=JSONObject.fromObject(cmpIds);
			type=jsonObject.getString("type");
			userType=jsonObject.getString("userType");
		}
		
		ModelAndView mv=getAutoView();
		mv.addObject("type", type);
		mv.addObject("userType", userType);
		return mv;
	}
	
    @RequestMapping("orgLeaderSetDialog")
    @Action(description = "用户表单变量设置")
    public ModelAndView orgLeaderSetDialog(HttpServletRequest request, HttpServletResponse response)
        throws Exception
    {
        // Long defId=RequestUtil.getLong(request, "defId",0);
        String cmpIds = RequestUtil.getString(request, "cmpIds");
        String type = "";
        String userType = "";
        Integer strategy = null;
        if (StringUtil.isNotEmpty(cmpIds))
        {
            JSONObject jsonObject = JSONObject.fromObject(cmpIds);
            type = jsonObject.getString("type");
            userType = jsonObject.getString("userType");
            strategy = jsonObject.getInt("strategy");
        }
        ModelAndView mv = getAutoView();
        mv.addObject("type", type);
        mv.addObject("strategy", strategy);
        mv.addObject("userType", userType);
        return mv;
    }
	
	@RequestMapping("startOrPrevWithOrgDialog")
	@Action(description="用户表单变量设置")
	public ModelAndView startOrPrevWithOrgDialog(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//Long defId=RequestUtil.getLong(request, "defId",0);
		String cmpIds=RequestUtil.getString(request, "cmpIds");
		
		String type="";
		String userType="";
		
		if(StringUtil.isNotEmpty(cmpIds)){
			JSONObject jsonObject=JSONObject.fromObject(cmpIds);
			type=jsonObject.getString("type");
			userType=jsonObject.getString("userType");
		}
		
		ModelAndView mv=getAutoView();
		mv.addObject("type", type);
	    mv.addObject("jobs", jobService.getAll());
		mv.addObject("userType", userType);
		return mv;
	}
	
	/**
	 * 并集运算
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<ISysUser> unionSysUserList(List<?extends ISysUser> list1,List<?extends ISysUser> list2){
		List<ISysUser> users = new ArrayList<ISysUser>();
		users.addAll(list1);
		for(ISysUser u:list2){
			if(!users.contains(u)){
				users.add(u);
			}
		}
		
		return users;
	}
	/**
	 * 交集运算
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<ISysUser> intersectSysUserList(List<?extends ISysUser> list1,List<?extends ISysUser> list2){
		List<ISysUser> users = new ArrayList<ISysUser>();
		for(ISysUser u:list1){
			if(list2.contains(u)){
				users.add(u);
			}
		}
		return users;
	}
	
	/**
	 * 差集运算
	 * @param list1
	 * @param list2
	 * @return
	 */
	private List<ISysUser> subtractSysUserList(List<?extends ISysUser> list1,List<?extends ISysUser> list2){
		List<ISysUser> users = new ArrayList<ISysUser>();
		users.addAll(list1);
		for(ISysUser u:list2){
			if(users.contains(u)){
				users.remove(u);
			}
		}
		return users;
	}
	
	/**
	 * 根据集合计算类型，进行集合运算
	 * @param type
	 * @param list1
	 * @param userList
	 * @return
	 */
	private List<?extends ISysUser> userSetCal(Short type,List<?extends ISysUser> list1,List<? extends ISysUser> userList){
		if(list1.isEmpty()){
			return userList;
		}
		switch (type) {
			case NodeUser.COMP_TYPE_AND:
				list1 = intersectSysUserList(list1,userList);
				break;
			case NodeUser.COMP_TYPE_OR:
				list1 = unionSysUserList(list1,userList);
				break;
			case NodeUser.COMP_TYPE_EXCLUDE:
				list1 = subtractSysUserList(list1,userList);
			default:
				break;
		}
		return list1;
	}
}
