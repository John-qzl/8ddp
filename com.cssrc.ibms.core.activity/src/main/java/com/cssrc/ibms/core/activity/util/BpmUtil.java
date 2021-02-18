package com.cssrc.ibms.core.activity.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.activiti.engine.RepositoryService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.impl.persistence.entity.ExecutionEntity;
import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.core.activity.graph.DivShape;
import com.cssrc.ibms.core.activity.graph.Point;
import com.cssrc.ibms.core.activity.graph.ShapeMeta;
import com.cssrc.ibms.core.activity.graph.activiti.BPMNEdge;
import com.cssrc.ibms.core.activity.graph.activiti.BPMNShap;
import com.cssrc.ibms.core.activity.graph.activiti.ProcessDiagramGenerator;
import com.cssrc.ibms.core.activity.model.ForkNode;
import com.cssrc.ibms.core.activity.model.NodeCondition;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.service.BpmService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.util.xml.Dom4jUtil;

/**
 * BPM 函数。
 * 
 * @author zhulongchao
 * 
 */
public class BpmUtil {

	private static final Log logger = LogFactory.getLog(BpmUtil.class);

	/**
	 * 流程变量的前缀名,所有需要放入流程中运行的变量，在请求中的命名方式如：v_name_S或v_createtime_D
	 * 变量命名规范:v_变量名_变量类型缩写, 变量类型缩写有如下类型 String:S Date:D Long:L Float:F Double:DB
	 * BigDecimal:BD Integer:I Short:SH
	 */
	private final static String VAR_PRE_NAME = "v_";

	/**
	 * BPM的XML的命名空间
	 */
	private final static String BPM_XML_NS = "xmlns=\"http://www.omg.org/spec/BPMN/20100524/MODEL\"";


	/**
	 * 从Request对象构造ProcessCmd对象
	 * 
	 * @param request
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public static ProcessCmd getProcessCmd(HttpServletRequest request)
			throws Exception {

		// taskId
		ProcessCmd cmd = new ProcessCmd();

		String temp = request.getParameter("taskId");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setTaskId(temp);
		}


		// 添加表单数据
		temp = request.getParameter("formData");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setFormData(temp);
		}

		Map paraMap = RequestUtil.getParameterValueMap(request, false, false);
		cmd.setFormDataMap(paraMap);
		
		DefinitionService bpmDefinitionService = (DefinitionService)AppUtil.getBean(DefinitionService.class);
		Definition bpmDefinition=null;

		temp = request.getParameter("actDefId");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setActDefId(temp);
			bpmDefinition = bpmDefinitionService.getByActDefId(temp);
		} else {
			temp = request.getParameter("flowKey");
			if (StringUtil.isNotEmpty(temp)) {
				cmd.setFlowKey(temp);
				bpmDefinition = bpmDefinitionService.getMainDefByActDefKey(temp);
			}
		}
		
		
		
		
		if(BeanUtils.isNotEmpty(bpmDefinition)){
			String informType = "";
			informType = bpmDefinition.getInformType();
			cmd.setInformType(informType);
		}

		temp = request.getParameter("destTask");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setDestTask(temp);
		}
		// 可以根据主键启动流程。
		temp = request.getParameter("businessKey");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setBusinessKey(temp);
		}
	    // 是否驳回到子流程
        temp = request.getParameter("isBackToExtSub");
        if (StringUtil.isNotEmpty(temp)) {
            Integer rtn = Integer.parseInt(temp);
            cmd.setBackToExtSub(rtn.intValue()==1?true:false);
        }
        // 驳回子流程的PK
        temp = request.getParameter("subbusinessKey");
        if (StringUtil.isNotEmpty(temp)) {
            cmd.setSubbusinessKey(temp);
        }
        
		String[] destTaskIds = request.getParameterValues("lastDestTaskId");
		if (destTaskIds != null) {
			cmd.setLastDestTaskIds(destTaskIds);
			String[] destTaskUserIds = new String[destTaskIds.length];
			for (int i = 0; i < destTaskIds.length; i++) {
				String[] userIds = request.getParameterValues(destTaskIds[i]
						+ "_userId");
				if (userIds != null) {
					destTaskUserIds[i] = StringUtil.getArrayAsString(userIds);
				}
			}
			cmd.setLastDestTaskUids(destTaskUserIds);
		}

		
		// 驳回
		temp = request.getParameter("back");
		if (StringUtil.isNotEmpty(temp)) {
			Integer rtn = Integer.parseInt(temp);
			cmd.setBack(rtn);
		}

		cmd.setVoteContent(request.getParameter("voteContent"));
		cmd.setVoteCode(request.getParameter("voteContent_code"));

		temp = request.getParameter("stackId");
		if (StringUtils.isNotEmpty(temp)) {
			cmd.setStackId(new Long(temp));
		}
		temp = request.getParameter("voteAgree");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setVoteAgree(new Short(temp));
		}

		// 变量处理
		Enumeration<String> paramEnums = request.getParameterNames();
		while (paramEnums.hasMoreElements()) {
			String paramName = paramEnums.nextElement();
			if (!paramName.startsWith(VAR_PRE_NAME))
				continue;
			String[] vnames = paramName.split("[_]");
			if (vnames == null || vnames.length != 3)
				continue;
			String varName = vnames[1];
			String val = (String) request.getParameter(paramName);
			if (val.isEmpty())
				continue;

			Object valObj = getValue(vnames[2], val);
			cmd.getVariables().put(varName, valObj);
		}
	
		
		temp = request.getParameter("isManage");
		if (StringUtil.isNotEmpty(temp)) {
			cmd.setIsManage(new Short(temp));
		}
		
		temp=request.getParameter("startNode");
		if (StringUtil.isNotEmpty(temp)) {
	        cmd.setStartNode(temp);
	    }
		
		//流程启动设置下一步执行人。
		temp= request.getParameter("_executors_");
		
		if (StringUtil.isNotEmpty(temp)) {
			List<TaskExecutor> executorList=BpmUtil.getTaskExecutors(temp);
			cmd.setTaskExecutors(executorList);
		}

		return cmd;
	}
	/**
	 * 根据任务执行人字符串返回执行人列表。
	 * 
	 * @param executors
	 *            执行人为 user^id^名称，group^id^名称
	 * @return 执行人列表。
	 */
	public static List<TaskExecutor> getTaskExecutors(String executors) {
		String[] aryExecutor = executors.split("#");
		List<TaskExecutor> list = new ArrayList<TaskExecutor>();
		for (String tmp : aryExecutor) {
			String[] aryTmp = tmp.split("\\^");
			if (aryTmp.length == 3){
				list.add(new TaskExecutor(aryTmp[0], aryTmp[1], aryTmp[2]));
			}
			else if(aryTmp.length == 1){
				list.add(new TaskExecutor(aryTmp[0]));
			}
		}
		return list;
	}

	/**
	 * 通过变量类型及字符串值返回变量实际类型值。
	 * 
	 * @param type
	 *            变量类型缩写有如下类型 String:S Date:D Long:L Float:F Double:DB
	 *            BigDecimal:BD Integer:I Short:SH
	 * @param paramValue
	 * @return
	 */
	public static Object getValue(String type, String paramValue) {
		Object value = null;
		// 大部的查询都是该类型，所以放至在头部
		if ("S".equals(type)) {
			value = paramValue;
		} else if ("L".equals(type)) {
			value = new Long(paramValue);
		} else if ("I".equals(type)) {
			value = new Integer(paramValue);
		} else if ("DB".equals(type)) {
			value = new Double(paramValue);
		} else if ("BD".equals(type)) {
			value = new BigDecimal(paramValue);
		} else if ("F".equals(type)) {
			value = new Float(paramValue);
		} else if ("SH".equals(type)) {
			value = new Short(paramValue);
		} else if ("D".equals(type)) {
			try {
				value = DateUtils.parseDate(paramValue, new String[] {
						"yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss" });
			} catch (Exception ex) {
			}
		} else {
			value = paramValue;
		}
		return value;
	}

	/**
	 * 从Activity流程定义文件中获取流程任务id及名称。<br>
	 * 返回MAP对象。
	 * 
	 * @param defXml
	 * @return Map对象，键名为任务id，键值为任务名称。
	 */
	public static Map<String, Map<String, String>> getTaskActivitys(
			String defXml) {
				return getTaskActivitys(defXml, true);
	}
    /**
     * 从Activity流程定义文件中获取流程任务id及名称。<br>
     * 返回MAP对象。
     * 
     * @param defXml
     * @param flag
     * @return Map对象，键名为任务id，键值为任务名称。
     */
    @SuppressWarnings("unchecked")
    public static Map<String, String> getUserTaskActivitys(String defXml) {
        defXml = defXml.replace(BPM_XML_NS, "");
        Document doc = Dom4jUtil.loadXml(defXml);
        Element root = doc.getRootElement();
        List<Node> list = root.selectNodes("./process//userTask | ./process//callActivity");
        LinkedHashMap<String, String> taskMap = new LinkedHashMap<String, String>();
        addToMap(list, taskMap);
        return taskMap;
    }
    
	/**
	 * 从Activity流程定义文件中获取流程任务id及名称。<br>
	 * 返回MAP对象。
	 * 
	 * @param defXml
	 * @param flag
	 * @return Map对象，键名为任务id，键值为任务名称。
	 */
	@SuppressWarnings("unchecked")
	public static Map<String, Map<String, String>> getTaskActivitys(
			String defXml, Boolean flag) {
		Map<String, Map<String, String>> rtnMap = new HashMap<String, Map<String, String>>();

		defXml = defXml.replace(BPM_XML_NS, "");

		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();

		List<Node> list = root.selectNodes("./process//userTask");
		Map<String, String> taskMap = new HashMap<String, String>();
		addToMap(list, taskMap);
		// rtnMap.put("任务节点", taskMap);
		// 约定，将CallActivity当作任务节点看待
		List<Node> callActivityList = root
				.selectNodes("./process//callActivity");
		if (callActivityList.size() > 0) {
			addToMap(callActivityList, taskMap);
		}
		rtnMap.put("任务节点", taskMap);

		Map<String, String> gateWayMap = new HashMap<String, String>();
		// 并行网关
		List<Node> parallelGatewayList = root
				.selectNodes("./process//parallelGateway");
		if (parallelGatewayList.size() > 0) {
			addToMap(parallelGatewayList, gateWayMap);
		}

		// 条件并行网关
		List<Node> inclusiveGatewayList = root
				.selectNodes("./process//inclusiveGateway");
		if (inclusiveGatewayList.size() > 0) {
			addToMap(inclusiveGatewayList, gateWayMap);
		}
		// 分支网关
		List<Node> exclusiveGatewayGatewayList = root
				.selectNodes("./process//exclusiveGateway");
		if (exclusiveGatewayGatewayList.size() > 0) {
			addToMap(exclusiveGatewayGatewayList, gateWayMap);
		}

		if (gateWayMap.size() > 0) {
			rtnMap.put("网关节点", gateWayMap);
		}

		if (flag) {
			// 开始节点
			List<Node> startList = root.selectNodes("./process//startEvent");
			Map<String, String> startMap = new HashMap<String, String>();
			addToMap(startList, startMap);
			rtnMap.put("开始节点", startMap);
		}
		// 结束节点
		List<Node> endList = root.selectNodes("./process//endEvent");
		Map<String, String> endMap = new HashMap<String, String>();
		addToMap(endList, endMap);
		rtnMap.put("结束节点", endMap);
		// 自动节点
		List<Node> serviceTask = root.selectNodes("./process//serviceTask");
		if (serviceTask.size() > 0) {
			Map<String, String> serviceMap = new HashMap<String, String>();
			addToMap(serviceTask, serviceMap);
			rtnMap.put("自动任务", serviceMap);
		}

		return rtnMap;
	}

	private static void addToMap(List<Node> list, Map<String, String> map) {
		for (Node node : list) {
			Element el = (Element) node;
			String id = el.attributeValue("id");
			String name = el.attributeValue("name");
			map.put(id, name);
		}
	}

	/**
	 * 根据流程定义的xml取得第一个任务节点ID。
	 * 
	 * @param defXml
	 *            任务定义XML。
	 * @return 返回任务id。
	 */
	public static String getFirstTaskNode(String defXml) {
		defXml = defXml.replace(BPM_XML_NS, "");
		Document doc = Dom4jUtil.loadXml(defXml);
		Element root = doc.getRootElement();
		Element startNode = (Element) root
				.selectSingleNode("/definitions/process/startEvent");
		if (startNode == null)
			return "";
		String startId = startNode.attributeValue("id");
		Element sequenceFlow = (Element) root
				.selectSingleNode("/definitions/process/sequenceFlow[@sourceRef='"
						+ startId + "']");
		if (sequenceFlow == null)
			return "";
		String taskId = sequenceFlow.attributeValue("targetRef");
		return taskId;
	}

	/**
	 * 取得流程可以跳转的任务节点。
	 * 
	 * @param defXml
	 *            流程定义的xml。
	 * @param nodes
	 *            任务节点。
	 * 
	 * @param flag 标记是去掉开始节点
	 * @return
	 */
	public static Map<String, Map<String, String>> getTranstoActivitys(
			String defXml, List<String> nodes, Boolean flag) {
		Map<String, Map<String, String>> actMap = getTaskActivitys(defXml, flag);
		Collection<Map<String, String>> values = actMap.values();
		for (String node : nodes) {
			for (Map<String, String> map : values) {
				map.remove(node);
			}
		}
		return actMap;
	}

	/** 
	* @Title: getTranstoTaskActivitys 
	* @Description: TODO(取得流程可以配置表单的任务节点) 
	* @param @param defXml
	* @param @param nodes
	* @param @return     
	* @return Map<String,String>    返回类型 
	* @throws 
	*/
	public static Map<String, String> getTranstoTaskActivitys(String defXml) {
		Map<String, String> actMap = getUserTaskActivitys(defXml);
		return actMap;
	}

	
    public static Map<String, Map<String, String>> getTranstoActivitys(String defXml, List<String> nodes)
    {
        Map<String, Map<String, String>> actMap = getTaskActivitys(defXml);
        Collection<Map<String, String>> values = actMap.values();
        for (String node : nodes)
        {
            for (Map<String, String> map : values)
            {
                map.remove(node);
            }
        }
        return actMap;
    }
	   
	/**
	 * 判断输入类是否继承了TaskListener接口。
	 * 
	 * @param className
	 *            类名 如
	 *            com.ibms.oa.service.flow.listener.StartEventListener
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static boolean isTaskListener(String className) {
		try {

			Class cls = Class.forName(className);
			return BeanUtils.isInherit(cls, TaskListener.class);

		} catch (ClassNotFoundException e) {
			return false;
		}
	}

	/**
	 * 验证handler输入是否是否有效。
	 * 
	 * <pre>
	 * 	handler 输入规则。
	 *  spring的 serviceId +“." + 方法名称。
	 * </pre>
	 * 
	 * @param handler
	 *            spring 的serviceId + "." + 方法名
	 * @return 0 有效，-1，格式不对，-2 没有找到service类，-3没有找到对应的方法，-4，未知的错误。
	 */
	public static int isHandlerValid(String handler) {

		if (handler.indexOf(".") == -1)
			return -1;
		String[] aryHandler = handler.split("[.]");
		if (aryHandler.length != 2)
			return -1;
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		Object serviceBean = null;
		try {
			serviceBean = AppUtil.getBean(beanId);
		} catch (Exception ex) {
			return -2;
		}
		if (serviceBean == null)
			return -2;

		try {
			Method invokeMethod = serviceBean.getClass().getDeclaredMethod(
					method, new Class[] { ProcessCmd.class });
			return 0;
		} catch (NoSuchMethodException e) {
			return -3;
		} catch (Exception e) {
			return -4;
		}

	}

	/**
	 * 验证handler输入是否是否有效。
	 * 
	 * <pre>
	 * 	handler 输入规则。
	 *  spring的 serviceId +“." + 方法名称。
	 * </pre>
	 * 
	 * @param handler
	 *            spring 的serviceId + "." + 方法名
	 * @return 0 有效，-1，格式不对，-2 没有找到service类，-3没有找到对应的方法，-4，未知的错误。
	 */
	public static int isHandlerValidNoCmd(String handler) {

		if (handler.indexOf(".") == -1)
			return -1;
		String[] aryHandler = handler.split("[.]");
		String beanId = aryHandler[0];
		String method = aryHandler[1];
		Object serviceBean = null;
		try {
			serviceBean = AppUtil.getBean(beanId);
		} catch (Exception ex) {
			return -2;
		}
		if (serviceBean == null)
			return -2;

		try {
			Method invokeMethod = serviceBean.getClass().getDeclaredMethod(
					method, new Class[0]);
			if(invokeMethod!=null){
				return 0;
			}else{
				return -3;
			}
		} catch (NoSuchMethodException e) {
			return -3;
		} catch (Exception e) {
			return -4;
		}

	}

	/**
	 * 根据流程的key，流程名称和流程设计器的xml通过转换，转换成流程activiti的XML。
	 * 
	 * @param id
	 *            流程key。
	 * @param name
	 *            流程名称。
	 * @param xml
	 *            设计器输出的流程定义xml。
	 * @return 返回转换后的xml。
	 * @throws TransformerFactoryConfigurationError
	 * @throws Exception
	 */
	public static String transform(String id, String name, String xml)
			throws TransformerFactoryConfigurationError, Exception {
		Map<String, String> map = new HashMap<String, String>();
		map.put("id", id);
		map.put("name", name);
		String xlstPath = SysConfConstant.CONF_ROOT+File.separator+"template"+File.separator+"activiti"+File.separator+"transform.xsl";
		xml = xml.trim();
		String str = Dom4jUtil.transXmlByXslt(xml, xlstPath, map);
		str = str.replace("&lt;", "<").replace("&gt;", ">")
				.replace("xmlns=\"\"", "").replace("&amp;", "&");

		Pattern regex = Pattern.compile("name=\".*?\"");
		Matcher regexMatcher = regex.matcher(str);
		while (regexMatcher.find()) {
			String strReplace = regexMatcher.group(0);
			String strReplaceWith = strReplace.replace("&", "&amp;")
					.replace("<", "&lt;").replace(">", "&gt;");
			str = str.replace(strReplace, strReplaceWith);
		}

		return str;
	}

	/**
	 * 根据流程定义xml获取流程节点的div数据。 用于显示流程定义页面和页面进行交互，比如设置流程节点的相关信息。
	 * 
	 * @param xml
	 * @return
	 * @throws Exception
	 */
	public static ShapeMeta transGraph(String xml) throws Exception {

		List<BPMNShap> shaps = ProcessDiagramGenerator.extractBPMNShap(xml);
		List<BPMNEdge> edges = ProcessDiagramGenerator.extractBPMNEdge(xml);
		Point2D.Double[] points = ProcessDiagramGenerator.caculateCanvasSize(
				shaps, edges);
		double shiftX = points[0].getX() < 0 ? points[0].getX() : 0;
		double shiftY = points[0].getY() < 0 ? points[0].getY() : 0;
		int width = (int) Math.round((points[1].getX() + 10 - shiftX));
		int height = (int) Math.round((points[1].getY() + 10 - shiftY));
		int minX = (int) Math.round((points[0].getX() - shiftX)); 
		int minY = (int) Math.round((points[0].getY() - shiftY));
		minX = (minX <= 5 ? 5 : minX);
		minY = (minY <= 5 ? 5 : minY);

		xml = xml.replace(BPM_XML_NS, "");
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		List sequenceFlows = root.selectNodes("//sequenceFlow");
		Map<String, String> seqIdandName = new HashMap<String, String>();
		StringBuffer sb = new StringBuffer();
		for (Object node : sequenceFlows) {
			String id = ((Element) node).attributeValue("id");
			String name = ((Element) node).attributeValue("name");
			seqIdandName.put(id, name);
		}
		List list = root.selectNodes("//bpmndi:BPMNShape");
		int subProcessNum=1;//内嵌子流程的层数
		Map<String,Integer> parentZIndexes=new HashMap<String, Integer>();//存放父节点的Zindex ，key值为父节点的ID
		for (int i = 0; i < list.size(); i++) {
			Element el = (Element) list.get(i);

			// Exclude Pool and Lane components
			String id = el.attributeValue("bpmnElement");
			Element component = (Element) root.selectSingleNode("//*[@id='"
					+ id + "']");

			if (component == null
					|| component.getName().equalsIgnoreCase("participant")
					|| component.getName().equalsIgnoreCase("lane")) {
				continue;
			}

			Element tmp = (Element) el.selectSingleNode("omgdc:Bounds");
			int x = (int) Float.parseFloat(tmp.attributeValue("x"));
			int y = (int) Float.parseFloat(tmp.attributeValue("y"));

			int w = (int) Float.parseFloat(tmp.attributeValue("width"));
			int h = (int) Float.parseFloat(tmp.attributeValue("height"));
			x = (int) (x - minX + 5 - shiftX);
			y = (int) (y - minY + 5 - shiftY);

			Element procEl = (Element) root
					.selectSingleNode("//process/descendant::*[@id='" + id
							+ "']");
			if (procEl != null) {
				String type = procEl.getName();
				if (type.equals("serviceTask")) {
		            Element ext = procEl.element("extensionElements");
		            Element service = ext.element("serviceType");
		            type = service.attributeValue("value");
		        }
				if (!"subProcess".equals(type) && !"callActivity".equals(type)) {
					Element multiObj = procEl
							.element("multiInstanceLoopCharacteristics");
					if (multiObj != null && !"subProcess".equals(type))
						type = "multiUserTask";
				}

				Element parent = procEl.getParent();

				String name = procEl.attributeValue("name");

				int zIndex = 10;
				String parentName = parent.getName();
				// 父节点为子流程的情况，zindex 设为父节点ZIndex+1，开始事件类型修改为subStartEvent。
				if (parentName.equals("subProcess")) {
					if (parent.getParent().getName().equals("subProcess")) {
						subProcessNum++;
					}
					if(type.equalsIgnoreCase("subProcess")){
						zIndex=parentZIndexes.get(parent.attributeValue("id"))+1;
						parentZIndexes.put(id, zIndex);
					}else if (type.equalsIgnoreCase("startEvent")) {
						type = "subStartEvent";
					}else if (type.equalsIgnoreCase("endEvent")) {
						type = "subEndEvent";
					}else{
						zIndex = 10+subProcessNum;
					}
				}else{
					if(type.equalsIgnoreCase("subProcess")){
						parentZIndexes.put(id, zIndex);
					}
				}
				DivShape shape = new DivShape(name, (float) x, (float) y, w, h,
						zIndex, id, type);
				sb.append(shape);
			}
		}

		ShapeMeta shapeMeta = new ShapeMeta(width, height, sb.toString());
		return shapeMeta;
	}

	/**
	 * 计算直角曲线的中点，
	 * 
	 * @param waypoints
	 *            用(x1,y1,x2,y2,x3,y3.....)方式表示的直角曲线。
	 * @return 用Map<String,Integer>表示的计算出的中点位置，
	 * 
	 *         <pre>
	 * key="x"表示中点 的x坐标;
	 * key="y"表示中点 的y坐标;
	 * key="flag"表示中点所在的直线的方向标志:
	 *  	flag=1,由上到下.
	 *  	flag=2,由下到上 .			
	 *  	flag=3,由左到右.
	 * flag=4,由右到左.
	 * 
	 *         <pre>
	 */
	private static Map<String, Integer> caculateCenterPosition(
			List<Integer> waypoints) {
		// 连线的中点傡置
		int x = 0, y = 0, flag = 0;
		Map<String, Integer> point = new HashMap<String, Integer>();
		List<Integer> lens = new ArrayList<Integer>();
		for (int i = 2; i < waypoints.size(); i += 2) {
			lens.add(Math.abs(waypoints.get(i - 2) - waypoints.get(i))
					+ Math.abs(waypoints.get(i - 1) - waypoints.get(i + 1)));
		}

		int halfLen = 0;
		for (int len : lens) {
			halfLen += len;
		}
		halfLen = halfLen / 2;

		int accumulativeLen = 0;

		int i;
		for (i = 0; i < lens.size(); i++) {
			accumulativeLen += lens.get(i);
			if (accumulativeLen > halfLen) {
				break;
			}
		}
		i += 1;

		// x坐标相等，
		if (waypoints.get(2 * i).intValue() == waypoints.get(2 * (i - 1))
				.intValue()) {
			// 前一个点在后一个点的上边
			if (waypoints.get(2 * i + 1) > waypoints.get(2 * (i - 1) + 1)) {
				// halfLen=accumulativeLen-lens.get(i)+(x-waypoints.get(2*(i-1)+1))
				y = halfLen - (accumulativeLen - lens.get(i - 1))
						+ waypoints.get(2 * (i - 1) + 1);
				flag = 1;
			} else {
				// halfLen=accumulativeLen-lens.get(i)+(waypoints.get(2*(i-1)+1)-x)
				y = accumulativeLen - lens.get(i - 1)
						+ waypoints.get(2 * (i - 1) + 1) - halfLen;
				flag = 2;
			}
			x = waypoints.get(2 * i);
		} else {// y坐标相等，
				// 前一个点在后一个点的左边
			if (waypoints.get(2 * i) > waypoints.get(2 * (i - 1))) {
				// halfLen=accumulativeLen-lens.get(i)+(x-waypoints.get(2*(i-1)))
				x = halfLen - (accumulativeLen - lens.get(i - 1))
						+ waypoints.get(2 * (i - 1));
				flag = 3;
			} else {
				// halfLen=accumulativeLen-lens.get(i)+(waypoints.get(2*(i-1)-1)-x)
				x = accumulativeLen - lens.get(i - 1)
						+ waypoints.get(2 * (i - 1)) - halfLen;
				flag = 4;
			}
			y = waypoints.get(2 * i + 1);
		}

		point.put("x", x);
		point.put("y", y);
		point.put("flag", flag);
		return point;

	}

	/**
	 * 计算连线上的标签的左上角坐标点与右下角坐标点。
	 * 
	 * @param name
	 *            连线上的标签名。
	 * @param waypoints
	 *            用(x1,y1,x2,y2,x3,y3.....)方式表示的直角曲线。
	 * @return 用一维数组表示的连线上的标签的左上角坐标点与右下角坐标点。
	 */
	private static Point[] caculateLabelRectangle(String name,
			List<Integer> waypoints) {
		if (name == null) {
			return new Point[] {};
		}
		BufferedImage processDiagram = new BufferedImage(2, 2, 2);
		Graphics2D g = processDiagram.createGraphics();
		g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		g.setPaint(Color.black);
		Font font = new Font("宋体", 1, 12);
		g.setFont(font);
		FontMetrics fontMetrics = g.getFontMetrics();

		Map<String, Integer> pos = caculateCenterPosition(waypoints);
		int x = pos.get("x");
		int y = pos.get("y");
		int flag = pos.get("flag");

		int drawX = x, drawY = y;
		switch (flag) {
		case 1:
			drawX = x + fontMetrics.getHeight() / 2;
			drawY = y;
			break;
		case 2:
			drawX = x - fontMetrics.stringWidth(name) - fontMetrics.getHeight()
					/ 2;
			drawY = y + fontMetrics.getHeight();
			break;
		case 3:
			drawX = x - fontMetrics.stringWidth(name) / 2;
			drawY = y - fontMetrics.getHeight() / 2 - fontMetrics.getHeight();
			break;
		case 4:
			drawX = x - fontMetrics.stringWidth(name) / 2;
			drawY = y + fontMetrics.getHeight() - fontMetrics.getHeight();
			break;
		}

		Point points[] = {
				new Point(drawX, drawY),
				new Point(drawX + fontMetrics.stringWidth(name), drawY
						+ fontMetrics.getHeight()) };

		return points;
	}

	/**
	 * 根据网关节点取得该节点的流入节点和外出的节点。
	 * 
	 * @param forkNode
	 *            网关节点id。
	 * @param xml
	 *            流程定义的XML。
	 * @return
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static ForkNode getForkNode(String forkNode, String xml)
			throws IOException {
		xml = xml.replace(BPM_XML_NS, "xmlns:bpm='ibms'");
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();
		List<Element> preNodes = root.selectNodes("//sequenceFlow[@targetRef='"
				+ forkNode + "']");
		ForkNode model = new ForkNode();
		model.setForkNodeId(forkNode);
		// 分支前只有一个节点
		if (preNodes.size() == 1) {
			Element preLine = (Element) preNodes.get(0);
			String sourceId = preLine.attributeValue("sourceRef");
			// 前面的节点是任务节点并且是多实例节点
			Element soureNode = (Element) root
					.selectSingleNode("//userTask[@id='" + sourceId + "']");
			if (soureNode != null) {
				model.setPreNodeId(sourceId);
				Element multiNode = soureNode
						.element("multiInstanceLoopCharacteristics");
				if (multiNode != null) {
					model.setMulti(true);
				}
			}
		}
		// 添加分支流向
		List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='"
				+ forkNode + "']");
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = "";
			Element conditionNode = el.element("conditionExpression");
			if (conditionNode != null) {
				condition = conditionNode.getText().trim();
				condition = StringUtil.trimPrefix(condition, "${");
				condition = StringUtil.trimSufffix(condition, "}");
			}

			Element targetNode = (Element) root.selectSingleNode("//*[@id='"
					+ id + "']");
			String nodeName = targetNode.attributeValue("name");

			NodeCondition nodeCondition = new NodeCondition(nodeName, id,
					condition);
			model.addNode(nodeCondition);
		}
		return model;
	}

	/**
	 * 取得流程定义中的分支节点的设置条件 ，返回为该节点的跳转节点<ID，条件>的映射
	 * 
	 * @param processXml
	 *            流程定义
	 * @param decisionNodeId
	 *            分支节点ID
	 * @return
	 */
	public static Map<String, String> getDecisionConditions(String processXml,
			String decisionNodeId) {
		Map<String, String> map = new HashMap<String, String>();
		processXml = processXml.replace(BPM_XML_NS, "xmlns:bpm='ibms'");
		Document doc = Dom4jUtil.loadXml(processXml);
		Element root = doc.getRootElement();

		// 添加分支流向
		List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='"
				+ decisionNodeId + "']");
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = "";
			Element conditionNode = el.element("conditionExpression");
			if (conditionNode != null) {
				condition = conditionNode.getText().trim();
				condition = StringUtil.trimPrefix(condition, "${");
				condition = StringUtil.trimSufffix(condition, "}");
			}
			map.put(id, condition);
		}
		return map;
	}

	/**
	 * 根据源节点设置后续节点的跳转条件。
	 * 
	 * @param sourceNode
	 *            源节点
	 * @param map
	 *            条件Map
	 * @param xml
	 *            流程定义xml
	 * @return 返回设置条件后的流程定义xml。
	 * @throws IOException
	 */
	@SuppressWarnings("unchecked")
	public static String setCondition(String sourceNode,
			Map<String, String> map, String xml) throws Exception {
		xml = xml.replace(BPM_XML_NS, "xmlns:bpm=\"ibms\"");
		Document doc = Dom4jUtil.loadXml(xml, "utf-8");
		Element root = doc.getRootElement();
		List<Element> nodes = root.selectNodes("//sequenceFlow[@sourceRef='"
				+ sourceNode + "']");
		for (Element el : nodes) {
			String id = el.attributeValue("targetRef");
			String condition = map.get(id);

			Element conditionEl = el.element("conditionExpression");
			if (conditionEl != null)
				el.remove(conditionEl);
			if (StringUtil.isNotEmpty(condition)) {
				// condition=StringUtil.stringToHtmlEntity(condition);
				Element elAdd = el.addElement("conditionExpression");
				elAdd.addAttribute("xsi:type", "tFormalExpression");
				elAdd.addCDATA("${" + condition + "}");
			}
		}
		String outXml = doc.asXML();
		outXml = outXml.replace("xmlns:bpm=\"ibms\"", BPM_XML_NS);
		return outXml;
	}

	/**
	 * 修改流程设计器的xml代码。
	 * 
	 * @param sourceNode
	 *            网关节点ID
	 * @param map
	 *            条件map 节点id，节点条件
	 * @param xml
	 *            设计器的xml代码。
	 * @return 返回流程设计器的XML代码。
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static String setGraphXml(String sourceNode,
			Map<String, String> map, String xml) throws IOException {
		Document doc = Dom4jUtil.loadXml(xml);
		Element root = doc.getRootElement();

		Element node = (Element) root.selectSingleNode("//bg:Gateway[@id='"
				+ sourceNode + "']");
		Element portsEl = node.element("ports");
		List portList = portsEl.elements();

		for (int i = 0; i < portList.size(); i++) {
			Element portEl = (Element) portList.get(i);
			if (portEl.attribute("x") == null && portEl.attribute("y") == null)
				continue;
			String id = portEl.attributeValue("id");
			Element outNode = (Element) root
					.selectSingleNode("//bg:SequenceFlow[@startPort='" + id
							+ "']");
			if (outNode != null) {
				String outPort = outNode.attributeValue("endPort");
				Element tmpNode = (Element) root
						.selectSingleNode("//ciied:Port[@id='" + outPort + "']");
				Element taskNode = tmpNode.getParent().getParent();
				String taskId = taskNode.attributeValue("id");

				Element conditionEl = outNode.element("Condition");
				if (conditionEl != null)
					outNode.remove(conditionEl);
				// 有条件的情况，处理条件
				if (map.containsKey(taskId)) {
					String condition = map.get(taskId);
					Element elAdd = outNode.addElement("Condition");
					elAdd.addText(condition);
				}
			}

		}
		return doc.asXML();

	}

	/**
	 * 根据deployId生成流程图。
	 * 
	 * @param deployId
	 *            流程deployid
	 * @param fileName
	 *            文件路径
	 * @param repositoryService
	 * @throws IOException
	 */
	public static ResultMessage genImageByDepolyId(String deployId,
			String fileName, RepositoryService repositoryService)
			throws IOException {
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		ProcessDefinition def = query.deploymentId(deployId).singleResult();
		if (def == null) {
			ResultMessage result = new ResultMessage();
			result.setMessage("没有找到流程定义!");
			result.setResult(ResultMessage.Fail);
			return result;
		}
		String defId = def.getId();
		return genImageByDefId(defId, fileName, repositoryService);
	}

	/**
	 * 根据deployId生成生成节点可以高亮的流程图。
	 * 
	 * @param deployId
	 *            流程deployid
	 * @param fileName
	 *            图片文件路径
	 * @param repositoryService
	 * @param activitys
	 *            高亮节点名称
	 * @return
	 * @throws IOException
	 */
	public static ResultMessage genImageByDepolyId(String deployId,
			String fileName, RepositoryService repositoryService,
			String... activitys) throws IOException {
		ProcessDefinitionQuery query = repositoryService
				.createProcessDefinitionQuery();
		ProcessDefinition def = query.deploymentId(deployId).singleResult();
		if (def == null) {
			ResultMessage result = new ResultMessage();
			result.setMessage("没有找到流程定义!");
			result.setResult(ResultMessage.Fail);
			return result;
		}
		String defId = def.getId();
		return genImageByDefId(defId, fileName, repositoryService, activitys);
	}

	/**
	 * 根据流程定义ID生成图片。
	 * 
	 * @param actDefId
	 *            流程定义ID。
	 * @param fileName
	 *            生成图片文件的名称。
	 * @param repositoryService
	 * @throws IOException
	 */
	public static ResultMessage genImageByDefId(String actDefId,
			String fileName, RepositoryService repositoryService)
			throws IOException {
		ResultMessage result = new ResultMessage();
		IBpmService bpmService = (IBpmService) AppUtil
				.getBean(BpmService.class);
		String bpmXml = bpmService.getDefXmlByProcessDefinitionId(actDefId);
		if (bpmXml == null) {
			result.setMessage("没有找到对应的流程定义!");
			result.setResult(ResultMessage.Fail);
			return result;
		}
		InputStream is = ProcessDiagramGenerator.generatePngDiagram(bpmXml);
		FileOperator.writeFile(fileName, is);

		result.setMessage("成功生成流程定义!");
		result.setResult(ResultMessage.Success);

		return result;
	}

	/**
	 * 根据流程定义ID生成节点可以高亮的流程图
	 * 
	 * @param defId
	 *            流程定义id
	 * @param fileName
	 *            生成的路径
	 * @param repositoryService
	 * @param activitys
	 *            指定节点需要高亮节点名称
	 * @return
	 * @throws IOException
	 */
	public static ResultMessage genImageByDefId(String defId, String fileName,
			RepositoryService repositoryService, String... activitys)
			throws IOException {

		ResultMessage result = new ResultMessage();
		IBpmService bpmService = (IBpmService) AppUtil
				.getBean(BpmService.class);
		String bpmXml = bpmService.getDefXmlByProcessDefinitionId(defId);
		if (bpmXml == null) {
			result.setMessage("没有找到对应的流程定义!");
			result.setResult(ResultMessage.Fail);
			return result;
		}

		List<String> list = new ArrayList<String>();
		for (String node : activitys) {
			list.add(node);
		}

		InputStream is = ProcessDiagramGenerator.generateDiagram(bpmXml, "png",
				list);
		FileOperator.writeFile(fileName, is);

		result.setMessage("成功生成流程定义!");
		result.setResult(ResultMessage.Success);

		return result;
	}

	/**
	 * 规则使用大括号括起来，使用map对其进行替换。
	 * 
	 * <pre>
	 * 例如：
	 * 1.规则如下：
	 * name={name}&userId={userId}
	 * 2.map数据如下：
	 * Map map=new HashMap();
	 * map.put("name","ray");
	 * map.put("userId","123");
	 * 解析结果如下：
	 * name=ray&userId=123
	 * </pre>
	 * 
	 * @param rule
	 *            规则
	 * @param map
	 *            规则对应的数据。
	 * @return
	 */
	public static String getStrByRule(String rule, Map<String, Object> map) {
		Pattern regex = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL
				| Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = regex.matcher(rule);
		while (matcher.find()) {
			String tag = matcher.group(0);
			String name = matcher.group(1);

			Object value = (Object) map.get(name);
			if (BeanUtils.isEmpty(value)) {
				rule = rule.replace(tag, "");
			} else {
				rule = rule.replace(tag, value.toString());
			}
		}
		return rule;
	}

	/**
	 * 根据标题规则获取任务标题。
	 * 
	 * <pre>
	 * 1.标题规则如下：
	 * 		{流程标题:title}-{发起人:startUser}-{发起日期:startDate}
	 * 		也可以简写为：
	 * 		{title}-{startUser}-{startDate}
	 * 2.map数据如下：
	 * 		Map map=new HashMap();
	 * 		map.put("title","发文流程");
	 * 		map.put("startUser","ray");
	 * 		map.put("startDate","2012-05-29");
	 * 
	 * 3.解析结果如下:
	 * 		发文流程-ray-2012-05-29
	 * </pre>
	 * 
	 * @param titleRule
	 *            标题规则 ，标题规则写法 {流程标题:title}-{发起人:startUser}-{发起日期:startDate}
	 * @param map
	 *            任务变量map
	 * @return
	 */
	public static String getTitleByRule(String titleRule,
			Map<String, Object> map) {
		if(StringUtil.isEmpty(titleRule))return "";
		Pattern regex = Pattern.compile("\\{(.*?)\\}", Pattern.DOTALL
				| Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE);
		Matcher matcher = regex.matcher(titleRule);
		while (matcher.find()) {
			String tag = matcher.group(0);
			String rule = matcher.group(1);
			String[] aryRule = rule.split(":");
			String name = "";
			if (aryRule.length == 1) {
				name = rule;
			} else {
				name = aryRule[1];
			}
			if (map.containsKey(name)) {
				Object obj = map.get(name);
				if(BeanUtils.isNotEmpty(obj)){
					try {
						titleRule = titleRule.replace(tag, obj.toString());
					} catch (Exception e) {
						titleRule = titleRule.replace(tag, "");
					}
				}else{
					titleRule = titleRule.replace(tag, "");
				}
			} else {
				titleRule = titleRule.replace(tag, "");
			}
		}
		return titleRule;
	}

	/**
	 * 是否多实例任务。
	 * 
	 * @param delegateTask
	 * @return
	 */
	public static boolean isMultiTask(DelegateTask delegateTask) {
		ActivityExecution execution = (ActivityExecution) delegateTask
				.getExecution();
		String multiInstance = (String) execution.getActivity().getProperty(
				"multiInstance");
		if (multiInstance != null) {
			return true;
		}
		return false;
	}

	/**
	 * 判断当前节点是否是多实例节点。
	 * 
	 * @param execution
	 * @return
	 */
	public static boolean isMuiltiExcetion(ExecutionEntity execution) {
		String multiInstance = (String) execution.getActivity().getProperty(
				"multiInstance");
		if (multiInstance != null) {
			return true;
		}
		return false;
	}
	
	


}
