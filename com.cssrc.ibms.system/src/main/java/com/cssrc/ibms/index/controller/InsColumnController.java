package com.cssrc.ibms.index.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.json.JsonPageResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.service.InsColTypeService;
import com.cssrc.ibms.index.service.InsColumnService;

/**
 * 布局栏目管理Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insColumn/" })
public class InsColumnController extends BaseController {

	@Resource
	private InsColumnService insColumnService;
	@Resource
	private InsColTypeService insColTypeService;


	@RequestMapping({ "del" })
	public void del(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids) {
				this.insColumnService.delById(Long.valueOf(id));
			}
		}
		/*return new JsonResult(true, "成功删除！");*/
	}

	@RequestMapping({ "get" })
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pkId = request.getParameter("pkId");
		InsColumn insColumn = null;
		if (StringUtils.isNotBlank(pkId))
			insColumn = (InsColumn) this.insColumnService.getById(Long.valueOf(pkId));
		else {
			insColumn = new InsColumn();
		}
		return getAutoView().addObject("insColumn", insColumn);
	}
	
	/**
	 * 获取组织下的栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "getColumns" })
	@ResponseBody
	public List<InsColumn> getColumns(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<InsColumn> insColumns = this.insColumnService.getAllByOrgId(UserContextUtil.getCurrentOrgId().toString());
		return insColumns;
	}
	
	@RequestMapping({ "getAll" })
	@ResponseBody
	public List<InsColumn> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<InsColumn> insColumns = this.insColumnService.getAll();
		return insColumns;
	}
	/**
	 * 获取新闻栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "getNewsColumns" })
	@ResponseBody
	public List<InsColumn> getNewsColumns(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		//获取新闻模板
		InsColType insColType = insColTypeService.getByKey("news", orgId);
		List<InsColumn> insColumns = this.insColumnService.getByColType(insColType.getTypeId(), orgId);
		return insColumns;
	}

	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String pkId = request.getParameter("pkId");

		String forCopy = request.getParameter("forCopy");
		ModelAndView mv = getAutoView();
		String typeName = "";
		Long typeId = 0L;
		InsColumn insColumn = null;
		if (StringUtils.isNotEmpty(pkId)) {
			insColumn = (InsColumn) this.insColumnService.getById(Long.valueOf(pkId));
			typeId = insColumn.getTypeId();
			InsColType insColType = insColTypeService.getById(Long.valueOf(typeId));
			typeName = insColType.getName();
			if ("true".equals(forCopy))
				insColumn.setColId(null);
		} else {
			insColumn = new InsColumn();
		}
		mv.addObject("typeName", typeName);
		mv.addObject("typeId", typeId);
		return mv.addObject("insColumn", insColumn);
	}

	/*@RequestMapping({ "searchByName" })
	@ResponseBody
	public JsonPageResult<InsColumn> searchByName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String colName = request.getParameter("key");
		PagingBean page = QueryFilterBuilder.createPage(request);
		List list = this.insColumnService.getByName(colName, page);
		return new JsonPageResult(list, page.getTotalItems());
	}*/

	@RequestMapping({ "newCol" })
	public ModelAndView newCol(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String portId = request.getParameter("portId");
		ModelAndView mv = getAutoView();
		String typeName = "";
		String typeId = "";
		InsColumn insColumn = new InsColumn();
		mv.addObject("portId", portId);
		mv.addObject("typeName", typeName);
		mv.addObject("typeId", typeId);
		return mv.addObject("insColumn", insColumn);
	}

	@RequestMapping({ "getByIsClose" })
	@ResponseBody
	public JsonPageResult<InsColumn> getByIsClose(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request);
		List<InsColumn> list = new ArrayList<InsColumn>();

		list = this.insColumnService.getAll();

		return new JsonPageResult<InsColumn>(list, queryFilter.getPagingBean().getTotalCount());
	}
	
	 @RequestMapping({"listData"})
	 public void listData(HttpServletRequest request, HttpServletResponse response) throws Exception
	 {		 
		 response.setContentType("application/json");
		 QueryFilter queryFilter = new QueryFilter(request);
		 List<InsColumn> list = insColumnService.getAll(queryFilter);
		 JsonPageResult result = new JsonPageResult(list, queryFilter.getPagingBean().getTotalCount());
		 ObjectMapper mapper=new ObjectMapper();
		 String jsonResult = mapper.writeValueAsString(result);
		 PrintWriter pw = response.getWriter();
		 pw.println(jsonResult);
		 pw.close();

	 }
}
