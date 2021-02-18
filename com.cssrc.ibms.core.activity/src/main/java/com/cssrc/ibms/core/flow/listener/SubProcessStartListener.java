package com.cssrc.ibms.core.flow.listener;

import java.util.Map;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.ExecutionListener;

import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.flow.model.NodeScript;
import com.cssrc.ibms.core.flow.service.NodeScriptService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;

/**
 * 内部子流程启动事件监听器。
 * <pre>
 * 	子流程启动时执行事件脚本。
 * </pre>
 * @author zhulongchao
 *
 */
public class SubProcessStartListener  implements ExecutionListener{

	@Override
	public void notify(DelegateExecution execution) throws Exception {
		Integer loopCounter=(Integer)execution.getVariable("loopCounter");
		//子流程第一次执行
		if(loopCounter==null || loopCounter==0){
			String actDefId=execution.getProcessDefinitionId();
			String nodeId=execution.getCurrentActivityId();
			exeEventScript(execution,BpmConst.StartScript,actDefId,nodeId);
		}
	}

	
	private void exeEventScript(DelegateExecution execution,int scriptType,String actDefId,String nodeId ){
		if(nodeId==null) return;
		NodeScriptService bpmNodeScriptService=(NodeScriptService)AppUtil.getBean("nodeScriptService");
		NodeScript model=bpmNodeScriptService.getScriptByType(nodeId, actDefId, scriptType);
		if(model==null) return;

		String script=model.getScript();
		if(StringUtil.isEmpty(script)) return;
		
		GroovyScriptEngine scriptEngine=(GroovyScriptEngine)AppUtil.getBean("scriptEngine");
		Map<String, Object> vars=execution.getVariables();
		vars.put("execution", execution);
		scriptEngine.execute(script, vars);
	}
	

}
