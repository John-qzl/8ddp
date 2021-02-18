package com.cssrc.ibms.core.flow.controller;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.NodePrivilege;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.service.NodeSignService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;

/**
 * 对象功能:会签任务投票规则 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/nodeSign/")
public class NodeSignController extends BaseController {
	@Resource
	private NodeSignService nodeSignService;

	/**
	 * 取得会签任务投票规则分页列表
	 * 
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description = "查看会签任务投票规则分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<NodeSign> list = nodeSignService.getAll(new QueryFilter(
				request, "bpmNodeSignItem"));
		ModelAndView mv = this.getAutoView().addObject("bpmNodeSignList", list);
		return mv;
	}

	/**
	 * 删除会签任务投票规则
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description = "删除会签任务投票规则")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "signId");
		nodeSignService.delByIds(lAryId);
		response.sendRedirect(preUrl);
	}

	@RequestMapping("edit")
	@Action(description = "编辑会签任务投票规则")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		// Long signId=RequestUtil.getLong(request,"signId");
		String actDefId = RequestUtil.getString(request, "actDefId");
		String nodeId = RequestUtil.getString(request, "nodeId");

		NodeSign bpmNodeSign = nodeSignService.getByDefIdAndNodeId(
				actDefId, nodeId);
		if (bpmNodeSign == null) {
			bpmNodeSign = new NodeSign();
			bpmNodeSign.setActDefId(actDefId);
			bpmNodeSign.setNodeId(nodeId);
		}
		// 0=拥有所有特权
		// 1=允许直接处理
		// 2=允许一票制
		// 3=允许补签
		// 这里的顺序不能乱.
		String modelPrivilege = getText("controller.bpmNodeSign.modeList.all")
				+","+
				 getText("controller.bpmNodeSign.modeList.directlyDeal")
				+","+
				 getText("controller.bpmNodeSign.modeList.allowVote")
				+","+
				 getText("controller.bpmNodeSign.modeList.retroactive");
		List<String> modeList = Arrays.asList(modelPrivilege.split(","));
		List<NodePrivilege> bpmNodePrivileges = nodeSignService
				.getPrivilegesByDefIdAndNodeId(actDefId, nodeId);
		NodePrivilege[] bpmNodePrivilegeList = new NodePrivilege[modeList.size()];
		if (bpmNodePrivileges != null) {
			for (int i = 0; i < bpmNodePrivileges.size(); i++) {
				NodePrivilege vo = bpmNodePrivileges.get(i);
				if (vo.getPrivilegemode() == null) {
					continue;
				}
				bpmNodePrivilegeList[vo.getPrivilegemode().intValue()] = vo;
			}
		}

		return getAutoView().addObject("bpmNodeSign", bpmNodeSign)
				.addObject("modeList", modeList)
				.addObject("bpmNodePrivilegeList", bpmNodePrivilegeList);
	}

	/**
	 * 取得会签任务投票规则明细
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description = "查看会签任务投票规则明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "signId");
		NodeSign bpmNodeSign = nodeSignService.getById(id);
		return getAutoView().addObject("bpmNodeSign", bpmNodeSign);
	}
}
