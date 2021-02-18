package com.cssrc.ibms.core.flow.service;


import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 脚本任务用于执行groovy脚本
 * @author zhulongchao
 *
 */
@Service
public class ScriptTask implements JavaDelegate {
	private Log logger = LogFactory.getLog(GroovyScriptEngine.class);
	@Override
	public void execute(DelegateExecution execution) throws Exception {
		ExecutionEntity ent=(ExecutionEntity)execution;
		String nodeId=ent.getActivityId();
		String actDefId=ent.getProcessDefinitionId();
		
		NodeScriptService bpmNodeScriptService=(NodeScriptService)AppUtil.getBean("nodeScriptService");
		//获取脚本节点的脚本对象。
		NodeScript model=bpmNodeScriptService.getScriptByType(nodeId, actDefId, BpmConst.ScriptNodeScript);
		if(model==null) return;
		
		
		String script=model.getScript();
		if(StringUtil.isEmpty(script)) return;
		
		GroovyScriptEngine scriptEngine=(GroovyScriptEngine)AppUtil.getBean("scriptEngine");
		Map<String, Object> vars=execution.getVariables();
		vars.put("execution", execution);
		scriptEngine.execute(script, vars);
		
		logger.debug("execution script :" + script);
	}

}
