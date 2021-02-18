package com.cssrc.ibms.core.flow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.NodeSql;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.NodeSqlService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * NodeSqlController
 * @author liubo
 * @date 2017年2月16日
 */
@Controller
@RequestMapping({"/oa/flow/NodeSql/"})
public class NodeSqlController extends BaseController{

	@Resource
	private NodeSqlService bpmNodeSqlService;
 
	@Resource
	private DefinitionService bpmDefinitionService;
 
	@Resource
	private NodeSetService bpmNodeSetService;
 
	@RequestMapping({"save"})
	@Action(description="添加或更新bpm_node_sql")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		String json = FileUtil.inputStream2String(request.getInputStream());
		NodeSql bpmNodeSql = (NodeSql)JSONObjectUtil.toBean(json, NodeSql.class);
		try {
			if ((bpmNodeSql.getId() == null) || (bpmNodeSql.getId().longValue() == 0L)) {
				bpmNodeSql.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.bpmNodeSqlService.add(bpmNodeSql);
				resultMsg = getText("添加", new Object[] { "bpm_node_sql" });
			} else {
				this.bpmNodeSqlService.update(bpmNodeSql);
				resultMsg = getText("更新", new Object[] { "bpm_node_sql" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + "," + e.getMessage(), 0);
		}
	}
  
	@RequestMapping({"list"})
	@Action(description="查看bpm_node_sql分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "bpmNodeSqlItem");
		Long defId = Long.valueOf(RequestUtil.getLong(request, "defId"));
		Definition bpmDefinition = (Definition)this.bpmDefinitionService.getById(defId);
		queryFilter.addFilterForIB("actdefId", bpmDefinition.getActDefId());
		List<NodeSql> list = this.bpmNodeSqlService.getAll(queryFilter);
		List mapList = new ArrayList();
		for (NodeSql bnq : list) {
			Map map = MapUtil.transBean2Map(bnq);
			NodeSet bpmNodeSet = this.bpmNodeSetService.getByMyActDefIdNodeId(bpmDefinition.getActDefId(), bnq.getNodeId());
			if (bpmNodeSet != null) {
				map.put("nodeName", bpmNodeSet.getNodeName());
			}
			if (bnq.getNodeId().equals("StartEvent1")) {
				map.put("nodeName", "开始节点");
			}
			if (bnq.getNodeId().equals("EndEvent1")) {
				map.put("nodeName", "结束节点");
			}
			mapList.add(map);
		}
  
		ModelAndView mv = getAutoView().addObject("bpmNodeSqlList", mapList);
		return mv;
	}
 
	@RequestMapping({"del"})
	@Action(description="删除bpm_node_sql")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.bpmNodeSqlService.delByIds(lAryId);
			message = new ResultMessage(1, "删除bpm_node_sql成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
 
	@RequestMapping({"edit"})
	@Action(description="编辑bpm_node_sql")
	public ModelAndView edit(HttpServletRequest request)
			throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		return getAutoView().addObject("returnUrl", returnUrl);
	}
 
	@RequestMapping({"get"})
	@Action(description="查看bpm_node_sql明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		NodeSql bpmNodeSql = (NodeSql)this.bpmNodeSqlService.getById(id);
		return getAutoView().addObject("bpmNodeSql", bpmNodeSql); 
	} 
	
	@RequestMapping({"getObject"})
	@Action(description="按各种参数查询bpmNodeSql")
	@ResponseBody
	public NodeSql getObject(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		Long id = RequestUtil.getLong(request, "id", null);
		if (id != null) {
			return (NodeSql)this.bpmNodeSqlService.getById(id);
		}
		String nodeId = RequestUtil.getString(request, "nodeId", "");
		String actdefId = RequestUtil.getString(request, "actdefId", "");
		if ((StringUtil.isNotEmpty(actdefId)) && (StringUtil.isNotEmpty(nodeId))) {
			return this.bpmNodeSqlService.getByNodeIdAndActdefId(nodeId, actdefId);
		}
 
		return null; 
	}
 
	@RequestMapping({"getTable"})
	@ResponseBody
	public IFormTable getTable(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		String actdefId = RequestUtil.getString(request, "actdefId", "");
		Long id = RequestUtil.getLong(request, "id", null);
 
		if (StringUtil.isEmpty(actdefId)) {
			actdefId = ((NodeSql)this.bpmNodeSqlService.getById(id)).getActdefId();
		}
		Definition bpmDefinition = this.bpmDefinitionService.getByActDefId(actdefId);
		IFormTable bpmFormTable = ((IFormTableService)AppUtil.getBean(IFormTableService.class)).getByDefId(bpmDefinition.getDefId());
		return bpmFormTable;
	}
	
	@RequestMapping({"getNodeType"})
	@Action(description="获取节点类型")
	@ResponseBody
	public Object getNodeType(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		String nodeId = RequestUtil.getString(request, "nodeId", "");
		String actdefId = RequestUtil.getString(request, "actdefId", "");
		FlowNode flowNode = (FlowNode)NodeCache.getByActDefId(actdefId).get(nodeId);
		Map map = new HashMap();
		map.put("nodeType", flowNode.getNodeType());
		return map;
	}
}
