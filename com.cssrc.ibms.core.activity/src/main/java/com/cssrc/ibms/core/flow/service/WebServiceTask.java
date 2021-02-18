package com.cssrc.ibms.core.flow.service;

import java.util.Map;

import net.sf.json.JSONArray;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;

import com.cssrc.ibms.core.flow.model.NodeWebService;
import com.cssrc.ibms.core.soap.SoapUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;


/**
 * Webservice任务
 * 
 * @author zhulongchao
 * 
 */
public class WebServiceTask implements JavaDelegate {

	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity ent = (ExecutionEntity) execution;
		String nodeId = ent.getActivityId();
		String actDefId = ent.getProcessDefinitionId();
		
		Map<String, Object> map = execution.getVariables();
		NodeWebServiceService nodeWebServiceService = (NodeWebServiceService) AppUtil.getBean("nodeWebServiceService");
		// 获取脚本节点的脚本对象。
		NodeWebService nodeWebService = nodeWebServiceService.getByNodeIdActDefId(nodeId, actDefId);

		if(BeanUtils.isEmpty(nodeWebService))return;
		String document = nodeWebService.getDocument();
		
		JSONArray jArray = (JSONArray)JSONArray.fromObject(document);
		if(jArray.size()==0)return;

		SoapUtil.invoke(map, jArray);
		execution.setVariables(map);

	}

}
