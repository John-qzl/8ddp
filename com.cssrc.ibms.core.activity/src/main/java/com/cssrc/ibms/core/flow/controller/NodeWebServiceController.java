package com.cssrc.ibms.core.flow.controller;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.flow.model.NodeWebService;
import com.cssrc.ibms.core.flow.service.NodeWebServiceService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.wsdl.OperationInfo;
import com.cssrc.ibms.core.util.wsdl.ParameterInfo;
import com.cssrc.ibms.core.util.wsdl.ServiceInfo;
import com.cssrc.ibms.core.util.wsdl.WSDLParser;

/**
 * 对象功能: WebService节点的控制器类 
 * 开发人员:zhulongchao  
 */
@Controller
@RequestMapping("/oa/flow/nodeWebService/")
public class NodeWebServiceController extends BaseController {
	@Resource
	private NodeWebServiceService nodeWebServiceService;

	/**
	 * 获取WSDL树
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getTreeData")
	@ResponseBody
	public String getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String wsdlUrl = RequestUtil.getString(request, "wsdlUrl");

		if (StringUtils.isEmpty(wsdlUrl))
			return null;
		WSDLParser parser = new WSDLParser(wsdlUrl);
		request.getSession().setAttribute("parser", parser);//设置到session中供下次使用.
		Map<String, ServiceInfo> services = parser.getServices();

		JSONArray treeList = new JSONArray();
		for (String key : services.keySet()) {
			ServiceInfo serviceInfo = services.get(key);
			JSONObject oneService = new JSONObject();
			oneService.accumulate("name", serviceInfo.getName()).accumulate("wsdlUrl", serviceInfo.getWsdlUrl())
					.accumulate("namespace", serviceInfo.getTargetNamespace()).accumulate("open", true);
			JSONArray children = new JSONArray();
			for (String operationKey : serviceInfo.getOperations().keySet()) {
				OperationInfo operation = serviceInfo.getOperations().get(operationKey);
				JSONObject oneOperation = new JSONObject();
				oneOperation.accumulate("name", operation.getOperationName())
						.accumulate("wsdlUrl", serviceInfo.getWsdlUrl())
						.accumulate("namespace", serviceInfo.getTargetNamespace());
				children.add(oneOperation);
			}
			oneService.accumulate("children", children);
			treeList.add(oneService);
		}
		return treeList.toString();
	}

	/**
	 * 获取函数信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("get")
	@ResponseBody
	public String get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String nodeName = RequestUtil.getString(request, "nodeName");
		String serviceName = RequestUtil.getString(request, "serviceName");
		String wsdlUrl = RequestUtil.getString(request, "wsdlUrl");
		
		WSDLParser parser;
		try{
			parser = new WSDLParser(wsdlUrl);
		}
		catch(Exception ex){
			if (!wsdlUrl.matches(".*\\?(?i)wsdl$"))
				wsdlUrl = wsdlUrl+"?wsdl";
			parser = new WSDLParser(wsdlUrl);
		}
		request.getSession().setAttribute("parser", parser);//设置到session中供下次使用.
		Map<String, ServiceInfo> services = parser.getServices();
		ServiceInfo serviceInfo = services.get(serviceName);
		JSONObject jsonObject = new JSONObject();

		if (serviceInfo == null) {
			jsonObject.accumulate("success", false).accumulate("msg", getText("controller.WEbService.method.notExist",nodeName));
		} else {
			OperationInfo operationInfo = serviceInfo.getOperations().get(nodeName);
			if (operationInfo == null) {
				jsonObject.accumulate("success", false).accumulate("msg", getText("controller.WEbService.method.notExist",nodeName));
			} else {
				jsonObject.accumulate("success", true).accumulate("msg", getText("controller.WEbService.getSuccess"));
				jsonObject.accumulate("wsdl", serviceInfo.getWsdlUrl());
				jsonObject.accumulate("namespace", serviceInfo.getTargetNamespace());
				jsonObject.accumulate("invokeUrl",serviceInfo.getHttpAddress());
				
				jsonObject.accumulate("method", operationInfo.getOperationName());
				jsonObject.accumulate("soapaction", operationInfo.getInputAction());

				// 处理入参树
				JSONArray inputTree = new JSONArray();
				
				List<?> inputParams = operationInfo.getInputParams();
				
				if(BeanUtils.isNotEmpty(inputParams)){
					for(Object inputParamObj : inputParams){
						ParameterInfo inputParameterInfo = (ParameterInfo)inputParamObj;
						//非复杂结构
						if(ParameterInfo.COMPLEX_NO.equals(inputParameterInfo.getIsComplext())){
							JSONObject paramJson = new JSONObject();
							setParamInfoIntoJson(inputParameterInfo.getName(), inputParameterInfo, paramJson);
							inputTree.add(paramJson);
						}
						//复杂结构
						else{
							Map<String, ParameterInfo> complextParams = inputParameterInfo.getComplextParams();
							if(complextParams.size()>0){
								ParameterInfo inputRoot = complextParams.values().iterator().next();
								for (String key : inputRoot.getComplextParams().keySet()) {
									ParameterInfo parameterInfo = inputRoot.getComplextParams().get(key);
									JSONObject paramJson = new JSONObject();
									setParamInfoIntoJson(key, parameterInfo, paramJson);
									inputTree.add(paramJson);
								}
							}
						}
					}
				}
				
				JSONObject inputTreeRoot = new JSONObject();
				inputTreeRoot.accumulate("name", getText("controller.WEbService.param.input")).accumulate("children", inputTree);
				jsonObject.accumulate("inputParams", inputTreeRoot);

				// 处理出参树
				JSONArray outputTree = new JSONArray();
				List<?> outputParams = operationInfo.getOutputParams();
				if(BeanUtils.isNotEmpty(outputParams)){
					Map<String, ParameterInfo> complextParams = ((ParameterInfo)outputParams.get(0)).getComplextParams();
					if(complextParams.size()>0){
						ParameterInfo outputRoot = complextParams.values().iterator().next();
						for (String key : outputRoot.getComplextParams().keySet()) {
							ParameterInfo parameterInfo = outputRoot.getComplextParams().get(key);
							JSONObject paramJson = new JSONObject();
							setParamInfoIntoJson(key, parameterInfo, paramJson);
							outputTree.add(paramJson);
						}
					}
				}
				JSONObject outputTreeRoot = new JSONObject();
				outputTreeRoot.accumulate("name", getText("controller.WEbService.param.output")).accumulate("children", outputTree);
				jsonObject.accumulate("outputParams", outputTreeRoot);
			}
		}
		return jsonObject.toString();
	}

	/**
	 * 递归设置参数json值
	 * 
	 * @param parameterInfo
	 * @param paramJson
	 */
	private void setParamInfoIntoJson(String name, ParameterInfo parameterInfo, JSONObject paramJson) {
		if (parameterInfo.getIsComplext() == ParameterInfo.COMPLEX_YES) {// 复杂类型
			String type = "bean";
			if(parameterInfo.isList()){
				type = "List{bean}";
			}
			paramJson.accumulate("name", name)
					 .accumulate("type", type)
					 .accumulate("nocheck", true);
			JSONArray children = new JSONArray();
			Map<String, ParameterInfo> complextParams = parameterInfo.getComplextParams();
			for (String key : complextParams.keySet()) {
				JSONObject childJson = new JSONObject();
				setParamInfoIntoJson(key, complextParams.get(key), childJson);
				children.add(childJson);
			}
			paramJson.accumulate("children", children);
		} else {
			paramJson.accumulate("name", parameterInfo.getName())
					 .accumulate("type", parameterInfo.getType())
					 .accumulate("nocheck", false);
		}
	}

	/**
	 * 编辑流程WebService节点
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("edit")
	@Action(description = "编辑流程WebService节点")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long defId = RequestUtil.getLong(request, "defId");

		NodeWebService bpmNodeWebService = nodeWebServiceService.getByNodeIdActDefId(nodeId, actDefId);
		
		Long setId = 0L;
		String document = "";
		if(bpmNodeWebService!=null){
			setId = bpmNodeWebService.getId();
			document = bpmNodeWebService.getDocument();
		}
		
		return getAutoView().addObject("setId", setId)
							.addObject("document",document)
							.addObject("actDefId", actDefId)
							.addObject("nodeId", nodeId)
							.addObject("defId", defId);
	}


	/**
	 * 添加或更新流程WebService节点。
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("save")
	@ResponseBody
	public String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String json = RequestUtil.getString(request, "json");
		String nodeId = RequestUtil.getString(request, "nodeId");
		Long setId = RequestUtil.getLong(request, "setId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		JSONObject jsonObject = new JSONObject();
		try {
			if(setId>0){
				NodeWebService bpmNodeWebService = nodeWebServiceService.getById(setId);
				if(bpmNodeWebService!=null){
					bpmNodeWebService.setDocument(json);
					nodeWebServiceService.update(bpmNodeWebService);
				}
				else{
					nodeWebServiceService.save(nodeId, actDefId, json);
				}
			}
			else
				nodeWebServiceService.save(nodeId, actDefId, json);
			jsonObject.accumulate("success", true).accumulate("msg", getText("controller.save.success"));
		} catch (Exception e) {
			jsonObject.accumulate("success", false).accumulate("msg", getText("controller.save.fail")+e.getMessage());
		}
		return jsonObject.toString();
	}
}
