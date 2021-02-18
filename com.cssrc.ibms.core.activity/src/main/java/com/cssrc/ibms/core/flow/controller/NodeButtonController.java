package com.cssrc.ibms.core.flow.controller;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.dom4j.Document;
import org.dom4j.Element;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.flow.model.Button;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeButton;
import com.cssrc.ibms.core.flow.model.NodeButtonXml;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeButtonService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;
import com.cssrc.ibms.core.util.xml.XmlBeanUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;

/**
 * 对象功能:自定义工具条 控制器类
 * 开发人员:zhulongchao
 */
@Controller
@RequestMapping("/oa/flow/nodeButton/")
@Action(ownermodel=SysAuditModelType.FLOW_MANAGEMENT)
public class NodeButtonController extends BaseController
{
	@Resource
	private NodeButtonService bpmNodeButtonService;
	
	@Resource
	private DefinitionService bpmDefinitionService;
	
	@Resource
	private NodeSetService bpmNodeSetService;
	@Resource
	private IBpmService bpmService; 
	
	/**
	 * 取得自定义工具条分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
//	@Action(description="查看自定义工具条分页列表")
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		String returnUrl=request.getParameter("returnUrl");
		Long defId=RequestUtil.getLong(request, "defId");
		Definition bpmDefinition=bpmDefinitionService.getById(defId);
		
		String actDefId=bpmDefinition.getActDefId();
		
		List<NodeSet> list=bpmNodeSetService.getByDefId(defId);
		
		Map<String,FlowNode> taskMap=NodeCache.getByActDefId(actDefId);
		
		Map<String,List<NodeButton>> btnMap= bpmNodeButtonService.getMapByDefId(defId); 
		//读按钮配置文件button.xml
		String buttonPath = SysConfConstant.CONF_ROOT+ File.separator+"flow"+File.separator;
		//String buttonPath = FlowUtil.getDesignButtonPath(); 
		String xml = FileOperator.readFile(buttonPath + "button.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		NodeButtonXml bpmButtonList = (NodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, NodeButtonXml.class);
		List<Button> btnList = bpmButtonList.getButtons();
		//对按钮进行分类
		List<Button> startBtnList = new ArrayList<Button>();//起始节点按钮
		List<Button> firstNodeBtnList = new ArrayList<Button>();//第一个节点按钮
		List<Button> signBtnList = new ArrayList<Button>();//会签节点按钮
		List<Button> commonBtnList = new ArrayList<Button>();//普通节点按钮
		for(Button button:btnList){
			if(button.getInit()==0) continue;
			if(button.getType()==0){
				startBtnList.add(button);
			}
			else if(button.getType()==1){
				firstNodeBtnList.add(button);
			}
			else if(button.getType()==2){
				signBtnList.add(button);
			}
			else if(button.getType()==3){
				commonBtnList.add(button);
			}
			else if(button.getType()==4){
				signBtnList.add(button);
				commonBtnList.add(button);
			}
		}
		
		return this.getAutoView()
				.addObject("btnMap", btnMap)
				.addObject("taskMap", taskMap)
				.addObject("bpmNodeSetList", list)
				.addObject("bpmDefinition", bpmDefinition)
				.addObject("startBtnList", startBtnList)
				.addObject("firstNodeBtnList", firstNodeBtnList)
				.addObject("signBtnList", signBtnList)
				.addObject("returnUrl", returnUrl)
				.addObject("commonBtnList", commonBtnList);
		
	}
	
	@RequestMapping("getByNode")
//	@Action(description="设置节点的操作按钮")
	public ModelAndView getByNode(HttpServletRequest request,HttpServletResponse response) throws Exception
	{	
		String returnUrl=request.getParameter("returnUrl");
		Boolean buttonFlag =  RequestUtil.getBoolean(request, "buttonFlag",true);
		long defId=RequestUtil.getLong(request,  "defId",0);
		String nodeId=RequestUtil.getString(request, "nodeId");
		if(defId==0){
			throw new Exception(getText("controller.bpmNodeButton.notDefId"));
		}
		
		Definition bpmDefinition=bpmDefinitionService.getById(defId);
		
		
		ModelAndView mv=this.getAutoView();
		if(StringUtil.isEmpty(nodeId)){
			List<NodeButton> list=bpmNodeButtonService.getByStartForm(defId);
			mv.addObject("btnList", list);
			mv.addObject("isStartForm", 1);
		}
		else{
			List<NodeButton> list=bpmNodeButtonService.getByDefNodeId(defId, nodeId);
			mv.addObject("btnList", list);
			mv.addObject("isStartForm", 0);
		}
		
		mv.addObject("bpmDefinition", bpmDefinition)
			.addObject("defId", defId)
			.addObject("nodeId", nodeId)
			.addObject("returnUrl", returnUrl)
			.addObject("buttonFlag",buttonFlag);
		return mv;
	} 
	
	/**
	 * 删除自定义工具条
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除自定义工具条",
			execOrder=ActionExecOrder.BEFORE,
			detail="<#assign __index=0 />"+
					"<#list StringUtils.split(id,\",\") as item>" +
						"<#assign entity = bpmNodeButtonService.getById(Long.valueOf(item))/>" +
						"<#if item_index==0>" +
							"删除流程定义【${SysAuditLinkService.getDefinitionLink(entity.defId)}】的节点" +
							"<#if entity.nodeid!=''>" +
							     "【${SysAuditLinkService.getNodeName(entity.defId,entity.nodeid)}】" +
							"</#if>" +
							"自定义工具按钮：" +
						"</#if>"+
						"【 ${entity.btnname}】 " +
					"</#list>"
	)
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage resultMessage = null;
		try{
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			bpmNodeButtonService.delByIds(lAryId);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}
		catch(Exception ex){
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description="编辑自定义工具条")
	public ModelAndView edit(HttpServletRequest request) throws Exception
	{   
		String returnUrl=request.getParameter("returnUrl");
		Boolean buttonFlag =  RequestUtil.getBoolean(request, "buttonFlag",true);
		Long id=RequestUtil.getLong(request,"id");
		
		NodeButton bpmNodeButton=null;
		
		ModelAndView mv=this.getAutoView();
		
		Long defId=RequestUtil.getLong(request, "defId",0);
		String nodeId=RequestUtil.getString(request, "nodeId");
	    /*button type 和button.xml里的button节点的 type属性对应，需要根据当前节点来判断*/
		int btnType=-1;
		if(id!=0){
			 bpmNodeButton= bpmNodeButtonService.getById(id);
			 Definition bpmDefinition= bpmDefinitionService.getById(bpmNodeButton.getDefId());
			 mv.addObject("bpmDefinition", bpmDefinition);
			 if(!StringUtil.isEmpty(nodeId)){
				 	/*获取当前流程节点*/
					FlowNode node=NodeCache.getNodeByActNodeId(bpmDefinition.getActDefId(), nodeId);
					if(node.getIsFirstNode()){
						/*IsFirstNode true，根据button.xml配置文件，btnType=1，表示第一个节点*/
						btnType=1;
					}else if(node.getIsSignNode()){
						/*IsSignNode true，*/
						btnType=2;
					}else{
						/*根据button.xml配置文件，表示普通按钮*/
						btnType=4;
					}
			}else{
				/*nodeid为空表示起始节点，/*IsFirstNode true，根据button.xml配置文件，btnType=0*/
				btnType=0;
			}
		}else{
			Definition bpmDefinition= bpmDefinitionService.getById(defId);
			mv.addObject("bpmDefinition", bpmDefinition);
			
			String actDefId=bpmDefinition.getActDefId();
			bpmNodeButton=new NodeButton();
			bpmNodeButton.setDefId(defId);
			if(StringUtil.isNotEmpty(nodeId)){
				boolean rtn= bpmService.isSignTask(actDefId,nodeId);
				bpmNodeButton.setNodetype(rtn?1:0);
				bpmNodeButton.setIsstartform(0);
			}
			else{
				bpmNodeButton.setIsstartform(1);
			}
			bpmNodeButton.setActdefid(actDefId);
			bpmNodeButton.setNodeid(nodeId);
		} 
		
		//读按钮配置文件button.xml
		//String buttonPath = FlowUtil.getDesignButtonPath();
		String buttonPath = SysConfConstant.CONF_ROOT+ File.separator+"flow"+File.separator;
		String xml = FileOperator.readFile(buttonPath + "button.xml");
		Document document = Dom4jUtil.loadXml(xml);
		Element root = document.getRootElement();
		String xmlStr = root.asXML();
		NodeButtonXml bpmButtonList = (NodeButtonXml) XmlBeanUtil
				.unmarshall(xmlStr, NodeButtonXml.class);
		List<Button> list = bpmButtonList.getButtons();
		/*遍历button.xml文件中的所有button，找出当前btn对应button节点*/
		for(Button btn:list){
			/*如果bpmNodeButton的operatortype和button.xml中operatortype匹配，
			则可能匹配当前节点，还要继续判断button.xml的type属性，
			因为operatortype是可以重复的*/
			if(btn.getOperatortype().intValue()==bpmNodeButton.getOperatortype().intValue()){
				/* 1：btnType==btn.getType()匹配，
				 * btn.getOperatortype().intValue()==bpmNodeButton.getOperatortype().intValue()匹配
				 * 则匹配出唯一的button元素。get到当前节点的配置属性，
				 * 2：btn.getType()==3表示普通节点专用按钮类型，
				 * <button type="3" operatortype="17" script="1" init="0" text="加签" />，其中operatortype不能和任何其他button节点的
				 * operatortype重复，不然mv.addObject("curbtn", btn);会有重复覆盖值。
				 *
				 * 通过以上规则。get到当前button的button所有属性（button.xml中的属性），jsp页面根据这些属性判断是否展示前置脚本、后置脚本、参数脚本
				 * 
				 * 最好 在 button.xml 配置文件中 所有的operatortype值都唯一。
				 * */
				if(btnType==btn.getType()||btn.getType()==3){
					mv.addObject("curbtn", btn);
				}
			}
		}
		JSONArray array = JSONArray.fromObject(list);
	    String buttonStr = array.toString();
	    
		return mv.addObject("bpmNodeButton",bpmNodeButton)
				.addObject("defId",defId)
				.addObject("nodeId",nodeId)
				.addObject("buttonFlag",buttonFlag)
				.addObject("returnUrl",returnUrl)
				.addObject("buttonStr",buttonStr);
	}

	/**
	 * 取得自定义工具条明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看自定义工具条明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		NodeButton bpmNodeButton = bpmNodeButtonService.getById(id);		
		return getAutoView().addObject("bpmNodeButton", bpmNodeButton);
	}
	
	
	/**
	 * 取得自定义工具条明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("sort")
	@Action(description="查看自定义工具条明细")
	public void sort(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		ResultMessage resultMessage = null;
		String ids = RequestUtil.getString(request, "ids");
		if(StringUtil.isEmpty(ids)){
			resultMessage = new ResultMessage(ResultMessage.Fail, getText("controller.bpmNodeButton.notSet"));
			response.getWriter().print(resultMessage);
			return;
		}
		try{
			bpmNodeButtonService.sort(ids);
			resultMessage = new ResultMessage(ResultMessage.Success,getText("controller.save.success"));
			response.getWriter().print(resultMessage);
		}
		catch(Exception ex){
			String str = MessageUtil.getMessage();
			if(StringUtil.isNotEmpty(str)){
			    resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.save.fail")+":" + str);
			    response.getWriter().print(resultMessage);
			}
			else{
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
	
	@RequestMapping("init")
	@Action(description="初始化操作按钮",
			detail="初始化流程定义 【${SysAuditLinkService.getDefinitionLink(Long.valueOf(defId))}】节点" +
					"<#if !StringUtil.isEmpty(nodeId)>" +
						"【${SysAuditLinkService.getNodeName(Long.valueOf(defId),nodeId)}】" +
					"</#if>" +
					"的操作按钮"
	)
	public void init(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String returnUrl=RequestUtil.getPrePage(request);
		Long defId=RequestUtil.getLong(request, "defId",0);
		String nodeId=RequestUtil.getString(request, "nodeId");
		ResultMessage resultMessage=null;
		List<NodeButton> buttons=null;
		try{
			
			bpmNodeButtonService.init(defId, nodeId);
			 resultMessage=new ResultMessage(ResultMessage.Success,getText("controller.bpmNodeButton.init.success"));
		}
		catch(Exception ex){
		     resultMessage = new ResultMessage(ResultMessage.Fail, (getText("controller.bpmNodeButton.init.fail")+":" + ex.getMessage()));
		}
		addMessage(resultMessage, request);
		response.sendRedirect(returnUrl);
		
	}
	
	@RequestMapping("initAll")
	@Action(description = "初始化操作按钮",
			detail="初始化流程定义 【${SysAuditLinkService.getDefinitionLink(Long.valueOf(defId))}】的全部操作按钮"
	)
	public void initAll(HttpServletRequest request, HttpServletResponse response)throws Exception 
	{
		String returnUrl = RequestUtil.getPrePage(request);
		Long defId = RequestUtil.getLong(request, "defId", 0);
		String actDefId=RequestUtil.getString(request, "actDefId");
		ResultMessage resultMessage = null;
		try {
		    bpmNodeButtonService.initAll(defId);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.bpmNodeButton.init.success"));
		} catch (Exception ex) {
			resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.bpmNodeButton.init.fail")+":" + ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(returnUrl);

	}
	
	
	@RequestMapping("delByDefId")
	@Action(description="清除按钮配置",
	
			detail="清除流程定义 【${SysAuditLinkService.getDefinitionLink(Long.valueOf(defId))}】的表单按钮"
	)
	public void delByDefId(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String returnUrl=RequestUtil.getPrePage(request);
		Long defId=RequestUtil.getLong(request, "defId",0); 
		ResultMessage resultMessage=null;
		try{
			
			bpmNodeButtonService.delByDefId(defId);
			resultMessage=new ResultMessage(ResultMessage.Success,getText("controller.bpmNodeButton.del.success"));
		}
		catch(Exception ex){
			resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.bpmNodeButton.del.fail")+":" + ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(returnUrl);
		
	}
	
	/**
	 * 根据流程定义Id和节点ID删除按钮操作。
	 * @param defId		流程定义id
	 * @param nodeId	流程节点ID
	 * @throws IOException 
	 */
	@RequestMapping("deByDefNodeId")
	@Action(description="清除按钮配置",
			detail="删除流程定义 【${SysAuditLinkService.getDefinitionLink(Long.valueOf(defId))}】的节点的按钮操作"
	)
	public void deByDefNodeId(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String returnUrl=RequestUtil.getPrePage(request);
		Long defId=RequestUtil.getLong(request, "defId",0);
		String nodeId=RequestUtil.getString(request, "nodeId");
	
		ResultMessage resultMessage=null;
		try{
			 this.delButInternational(defId,nodeId);
			 bpmNodeButtonService.delByDefNodeId(defId,nodeId);
			 resultMessage=new ResultMessage(ResultMessage.Success,getText("controller.del.success"));
		}
		catch(Exception ex){
			 resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(returnUrl);
	}
	
	/**
	 * 根据defId和nodeId查找出操作按钮的主键，在删除国际化资源
	 * @param defId
	 * @param nodeId
	 */
	public void delButInternational(Long defId, String nodeId) {
		List<NodeButton> list=null;
		if (StringUtil.isEmpty(nodeId)) {
			list=bpmNodeButtonService.getByStartForm(defId);
		}else {
			list=bpmNodeButtonService.getByDefNodeId(defId, nodeId);
		}
		if (BeanUtils.isEmpty(list)) {
			return;
		} 
		
	}


}
