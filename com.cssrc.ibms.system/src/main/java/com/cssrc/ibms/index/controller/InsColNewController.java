package com.cssrc.ibms.index.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColNew;
import com.cssrc.ibms.index.service.InsColNewService;

/**
 * 栏目新闻关联信息可编辑操作Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insColNew/" })
public class InsColNewController extends BaseController{

	@Resource
	InsColNewService insColNewService;
	
	
	/**
	 * 得到一条查询条件
	 * @param request
	 * @return
	 */
	protected QueryFilter getQueryFilter(HttpServletRequest request){	
		QueryFilter queryFilter = new QueryFilter(request);
		return queryFilter;
	}
	
	/**
	 * 删除栏目有关数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"del"})
	@ResponseBody
	public JsonResult del(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids) {
				this.insColNewService.delById(Long.valueOf(id));
			}
		}
		return new JsonResult(true, "成功删除！");
	}
	
	
	/**
	 * 删除新闻信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"delNew"})
	@ResponseBody
	public JsonResult delNew(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		
		Long uId = RequestUtil.getLong(request,"ids");
		Long colId = RequestUtil.getLong(request,"colId");
		if (uId !=0L) {
			this.insColNewService.delByColIdNewId(colId, uId);
		}
		return new JsonResult(true, "成功删除！");
	}
	
	/**
	 * 获取一条新闻纪录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"get"})
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String pkId = request.getParameter("pkId");
		InsColNew insColNew = null;
		if (StringUtils.isNotBlank(pkId))
			insColNew = (InsColNew)this.insColNewService.getById(Long.valueOf(pkId));
		else {
			insColNew = new InsColNew();
		}
		return getAutoView().addObject("insColNew", insColNew);
	}
	
	/**
	 * 编辑一条新闻纪录
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long newId =  RequestUtil.getLong(request,"pkId");
		Long colId =  RequestUtil.getLong(request,"colId");

		InsColNew insColNew = null;
		if (newId !=0L) {
			insColNew = this.insColNewService.getByColIdNewId(colId, newId);
		}
		return getAutoView().addObject("insColNew", insColNew).addObject("newId", newId).
				addObject("colId", colId);
	}

}
