/*package com.cssrc.ibms.dbom.controller;

import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.dbom.TreeNode;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dbom.model.DBom;
import com.cssrc.ibms.dbom.service.DBomNodeService;
import com.cssrc.ibms.dbom.service.DBomService;

*//**
 * 对象功能:DBom分类管理 控制器类.
 *
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] WeiLei <br/>
 *         [创建时间] 2016-7-13 上午08:05:04 <br/>
 *         [修改人] WeiLei <br/>
 *         [修改时间] 2016-7-13 上午08:05:04
 * @see
 *//*
@Controller
@RequestMapping("/oa/system/dbom/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DBomControllerNoExt extends BaseController {
	@Resource
	private DBomService dBomService;
	@Resource
	private DBomNodeService dbomNodeService;
	
	*//**
	 * dbom 管理页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("manage")
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("oa/dbom/dbomManageList.jsp");
		List<DBom> dbomDatas = dBomService.getAll();
		return mv.addObject("dbomDatas", dbomDatas);
	}

	*//**
	 * 获取dbom tree
	 * 
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("getDobomByCode")
	@ResponseBody
	@Action(description = "获取DBom树节点数据")
	public List<TreeNode> tree(HttpServletRequest request,
			HttpServletResponse response) {
		String pCode = RequestUtil.getString(request, "pCode");
		String nodeType = RequestUtil.getString(request, "nodeType");
		return dbomNodeService.getTreeNode(pCode, nodeType);
	}

	*//**
	 * 编辑 dbom 分类
	 * 
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("edit")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) {
		String dbomCode = RequestUtil.getString(request, "dbomCode");
		ModelAndView mv = new ModelAndView("oa/dbom/dbomTypeEdit.jsp");
		if (StringUtil.isEmpty(dbomCode)) {
			mv.addObject("dbom", new DBom());
		} else {
			mv.addObject("dbom", this.dBomService.getUniqueByCode(dbomCode));
		}
		return mv;
	}

	*//**
	 * 保存之前先检查dbom code是否存在
	 * 
	 * @param request
	 * @param response
	 * @return
	 *//*
	@RequestMapping("checkDbom")
	@ResponseBody
	@Action(description = "检查DBom分类代号是否已存在")
	public boolean checkDbom(HttpServletRequest request,
			HttpServletResponse response) {
		boolean result = false;
		try {
			Long id = RequestUtil.getLong(request, "id");
			String code = RequestUtil.getString(request, "code");
			result = dBomService.check(id, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	*//**
	 * 保存dbom
	 * 
	 * @param request
	 * @param response
	 * @param dbom
	 * @throws IOException
	 *//*
	@RequestMapping("save")
	@Action(description = "保存DBom分类信息")
	public void save(HttpServletRequest request, HttpServletResponse response,
			DBom dbom) throws IOException {
		ResultMessage message = new ResultMessage(ResultMessage.Fail,
				"系统bug，请联系管理员");
		PrintWriter writer = response.getWriter();
		try {
			dbom.setUsername(UserContextUtil.getCurrentUser().getUsername());
			dbom.setModifiedTime(new Date());
			message = dBomService.save(dbom);
			writer.print(message.toString());

		} catch (Exception e) {
			e.printStackTrace();
			writer.print(message.toString());
		}
	}

	*//**
	 * 删除dbom
	 * 
	 * @param request
	 * @param response
	 *//*
	@RequestMapping("delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) {
		try {
			String dbomCode = RequestUtil.getString(request, "dbomCode");
			DBom dbom = dBomService.getUniqueByCode(dbomCode);
			List<DBomNode> dbomNodeList = dbomNodeService.getByPCode(dbom
					.getCode());
			for (DBomNode dbomNode : dbomNodeList) {
				dbomNodeService.deleteById(dbomNode.getId());
			}
			dBomService.delById(dbom.getId());
			ResultMessage resultMessage = new ResultMessage(
					ResultMessage.Success, "删除成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
*/