package com.cssrc.ibms.core.activity.servlet;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.activiti.engine.impl.persistence.entity.TaskEntity;
import org.apache.commons.lang.StringUtils;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.core.activity.graph.activiti.ProcessDiagramGenerator;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.status.FlowStatusService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;


/**
 * 产生流程图的servlet。<br>
 * 
 * <pre>
 * 要求传入名称为deployId参数。
 * 在web.xml中配置：
 * &lt;servlet>
 *       &lt;servlet-name>bpmImageServlet&lt;/servlet-name>
 *       &lt;servlet-class>com.ibms.core.bpm.servlet.BpmImageServlet&lt;/servlet-class>
 *    &lt;/servlet>
 * &lt;servlet-mapping>
 *   	&lt;servlet-name>bpmImageServlet&lt;/servlet-name>
 *   	&lt;url-pattern>/bpmImage&lt;/url-pattern>
 *   &lt;/servlet-mapping>
 *   
 *   页面使用方法如下：
 *   &lt;img src="${ctx}/bpmImage?deployId=**" />
 *   &lt;img src="${ctx}/bpmImage?taskId=**" />
 *   &lt;img src="${ctx}/bpmImage?processInstanceId=**" />
 *   &lt;img src="${ctx}/bpmImage?definitionId=**" />
 * </pre>
 * 
 * @author zhulongchao.
 * 
 */
@SuppressWarnings("serial")
@WebServlet(name="imageServlet",urlPatterns="/bpmImage")
public class ImageServlet extends HttpServlet {
	private IBpmService bpmService = AppUtil.getBean(IBpmService.class);
	private FlowStatusService flowStatusService = AppUtil.getBean(FlowStatusService.class);

	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
		String deployId = RequestUtil.getString(request, "deployId");
		String taskId = RequestUtil.getString(request, "taskId");
		String processInstanceId = RequestUtil.getString(request,"processInstanceId");
		String definitionId = RequestUtil.getString(request, "definitionId");
		String runId = request.getParameter("runId");
		// 生成图片
		InputStream is = null;
		//根据流程deployId产生图片
		if (StringUtil.isNotEmpty(deployId)) {
			String bpmnXml = bpmService.getDefXmlByDeployId(deployId);
			is = ProcessDiagramGenerator.generatePngDiagram(bpmnXml);
		}
		//流程定义id
		else if (StringUtils.isNotEmpty(definitionId)) {
			String bpmnXml = bpmService.getDefXmlByProcessDefinitionId(definitionId);
			is = ProcessDiagramGenerator.generatePngDiagram(bpmnXml);

		}
		//流程任务id
		else if (StringUtil.isNotEmpty(taskId)) {
			String bpmnXml = bpmService.getDefXmlByProcessTaskId(taskId);
			TaskEntity taskEntity = bpmService.getTask(taskId);
			Map<String,String> highLightList = flowStatusService.getStatusByInstanceId(new Long( taskEntity.getProcessInstanceId()));
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml,highLightList, "png");
		}
		//流程实例ID
		else if (StringUtils.isNotEmpty(processInstanceId)) {
			String bpmnXml = bpmService.getDefXmlByProcessProcessInanceId(processInstanceId);
			if (bpmnXml == null) {
				ProcessRunService processRunService = (ProcessRunService) AppUtil.getBean(ProcessRunService.class);
				ProcessRun processRun = processRunService.getByActInstanceId(new Long(processInstanceId));
				bpmnXml = bpmService.getDefXmlByDeployId(processRun.getActDefId());
			}
			Map<String,String>  highLightMap = flowStatusService.getStatusByInstanceId(Long.parseLong(processInstanceId));
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml,highLightMap, "png");
		}
		//流程运行id
		else if (StringUtils.isNotEmpty(runId)) {
			ProcessRunService processRunService = (ProcessRunService) AppUtil.getBean(ProcessRunService.class);
			ProcessRun processRun = processRunService.getById(new Long(runId));
			processInstanceId=processRun.getActInstId();
			String bpmnXml = bpmService.getDefXmlByProcessProcessInanceId(processRun.getActInstId());
			if (bpmnXml == null) {
				bpmnXml = bpmService.getDefXmlByDeployId(processRun.getActDefId());
			}
			Map<String,String>  highLightMap = flowStatusService.getStatusByInstanceId(Long.parseLong(processInstanceId));
			is = ProcessDiagramGenerator.generateDiagram(bpmnXml,highLightMap, "png");
		}

		if (is != null) {
			response.setContentType("image/png");
			OutputStream out = response.getOutputStream();

			try {
				byte[] bs = new byte[1024];
				int n = 0;
				while ((n = is.read(bs)) != -1) {
					out.write(bs, 0, n);
				}
				out.flush();
			} catch (Exception ex) {
			} finally {
				is.close();
				out.close();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}
}
