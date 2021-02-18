package com.cssrc.ibms.core.flow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormRunService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormRun;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.system.intf.IGlobalTypeService;
import com.cssrc.ibms.api.system.intf.ISysTemplateService;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.system.model.ISysDataSource;
import com.cssrc.ibms.api.system.model.ISysTemplate;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.graph.ShapeMeta;
import com.cssrc.ibms.core.activity.model.FlowNode;
import com.cssrc.ibms.core.activity.model.NodeCache;
import com.cssrc.ibms.core.activity.model.ProcessTask;
import com.cssrc.ibms.core.activity.model.TaskExecutor;
import com.cssrc.ibms.core.activity.util.BpmUtil;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.AuthorizeRight;
import com.cssrc.ibms.core.flow.model.Definition;
import com.cssrc.ibms.core.flow.model.NodeSet;
import com.cssrc.ibms.core.flow.model.ProCopyto;
import com.cssrc.ibms.core.flow.model.ProTransTo;
import com.cssrc.ibms.core.flow.model.ProTransToAssignee;
import com.cssrc.ibms.core.flow.model.ProcessRun;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.model.TaskApprovalItems;
import com.cssrc.ibms.core.flow.model.TaskExeStatus;
import com.cssrc.ibms.core.flow.model.TaskNodeStatus;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.BpmService;
import com.cssrc.ibms.core.flow.service.DefAuthorizeService;
import com.cssrc.ibms.core.flow.service.DefinitionService;
import com.cssrc.ibms.core.flow.service.NodeSetService;
import com.cssrc.ibms.core.flow.service.ProCopytoService;
import com.cssrc.ibms.core.flow.service.ProTransToService;
import com.cssrc.ibms.core.flow.service.ProcessRunService;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.core.flow.service.TaskApprovalItemsService;
import com.cssrc.ibms.core.flow.service.TaskMessageService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.TaskReadService;
import com.cssrc.ibms.core.flow.service.TaskUserService;
import com.cssrc.ibms.core.flow.service.impl.TaskExecutorService;
import com.cssrc.ibms.core.flow.util.FlowUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.encrypt.Base64;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;

@Controller
@RequestMapping( { "/oa/flow/taskApprovalItems/" })
@Action(ownermodel = SysAuditModelType.FLOW_MANAGEMENT)
public class TaskApprovalItemsController extends BaseController {

	@Resource
	private TaskApprovalItemsService taskApprovalItemsService;

	@Resource
	private DefinitionService bpmDefinitionService;

	@Resource
	private IGlobalTypeService globalTypeService;

	@RequestMapping( { "edit" })
	@Action(description = "编辑常用语管理")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		String isAdmin = RequestUtil.getString(request, "isAdmin");
		return getAutoView().addObject("isAdmin", isAdmin);
	}

	@RequestMapping( { "save" })
	@Action(description="添加或更新节点运行脚本",
			detail="添加或更新流程定义【${SysAuditLinkService.getDefinitionLink(actDefId)}】的节点" +
					"【${SysAuditLinkService.getNodeName(actDefId,nodeId)}】的节点运行脚本（常用语）")
	public void save(HttpServletRequest request, HttpServletResponse response,
			TaskApprovalItems taskApprovalItems, BindingResult bindResult)
			throws Exception {
		String approvalItem = RequestUtil.getString(request, "approvalItem");
		Short type = RequestUtil.getShort(request, "type", Short
				.valueOf((short) 1));
		String typeId = RequestUtil.getString(request, "flowTypeId");
		String defKey = RequestUtil.getString(request, "defKey");
		try {
			this.taskApprovalItemsService.addTaskApproval(approvalItem, type,
					typeId, defKey,taskApprovalItems, UserContextUtil.getCurrentUserId());
			writeResultMessage(response.getWriter(), "保存常用语成功!", 1);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(0, "保存常用语失败:"
						+ str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@SuppressWarnings("unchecked")
	@RequestMapping( { "list" })
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long currUserId = UserContextUtil.getCurrentUserId();
		Long isAdmin = Long.valueOf(RequestUtil.getLong(request, "isAdmin"));
		List<TaskApprovalItems> taskApprovalItemsList = null;
		QueryFilter queryFilter = new QueryFilter(request, "taskApprovalItems");

		if (isAdmin.longValue() == 1L) {
			taskApprovalItemsList = this.taskApprovalItemsService
					.getAll(queryFilter);
		} else {
			queryFilter.addFilterForIB("admin", Integer.valueOf(1));
			queryFilter.addFilterForIB("userId", currUserId);
			taskApprovalItemsList = this.taskApprovalItemsService
					.getAll(queryFilter);
		}

		Map defTypeMap = new HashMap();
		Map defTypeTempMap = new HashMap();

		Map defMap = new HashMap();
		Map defTempMap = new HashMap();

		QueryFilter queryFilter1 = new QueryFilter(request);
		queryFilter1.addFilterForIB("isMain", Integer.valueOf(1));
		List<Definition> bpmDefinitionlist = this.bpmDefinitionService
				.getAll(queryFilter1);
		for (Definition bpmDefinition : bpmDefinitionlist) {
			defTempMap.put(bpmDefinition.getDefKey(), bpmDefinition
					.getSubject());
		}

		List<?extends IGlobalType> globalTypeList = this.globalTypeService
				.getByCatKey(IGlobalType.CAT_FLOW, false);
		for (IGlobalType globalType : globalTypeList) {
			defTypeTempMap.put(globalType.getTypeId(), globalType
					.getTypeName());
		}

		for (TaskApprovalItems taskApprovalItems : taskApprovalItemsList) {
			if (taskApprovalItems.getType() == TaskApprovalItems.TYPE_FLOW)
				defMap.put(taskApprovalItems.getDefKey(), (String) defTempMap
						.get(taskApprovalItems.getDefKey()));
			else if (taskApprovalItems.getType() == TaskApprovalItems.TYPE_FLOWTYPE) {
				defTypeMap.put(taskApprovalItems.getTypeId(),
						(String) defTypeTempMap.get(taskApprovalItems
								.getTypeId()));
			}
		}

		return getAutoView().addObject("taskApprovalItemsList",taskApprovalItemsList)
		                    .addObject("defMap", defMap)
		                    .addObject("defTypeMap", defTypeMap)
		                    .addObject("isAdmin", isAdmin)
							.addObject("currUserId", currUserId)
							.addObject("globalTypeList", globalTypeList);
	}

	@RequestMapping( { "del" })
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "itemId");
			this.taskApprovalItemsService.delByIds(lAryId);
			message = new ResultMessage(1, "删除成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
}
