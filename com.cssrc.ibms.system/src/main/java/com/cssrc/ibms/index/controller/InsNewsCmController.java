package com.cssrc.ibms.index.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
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

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonPageResult;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsNewsCm;
import com.cssrc.ibms.index.model.SortReply;
import com.cssrc.ibms.index.service.InsNewsCmService;
import com.cssrc.ibms.index.service.InsNewsService;


/**
 * 新闻评论内容管理Controller层
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insNewsCm/" })
@Action(ownermodel = SysAuditModelType.NEWS_COMMENT_MANAGEMENT)
public class InsNewsCmController extends BaseController{


	@Resource
	InsNewsCmService insNewsCmService;

	@Resource
	InsNewsService insNewsService;

	
	
	protected QueryFilter getQueryFilter(HttpServletRequest request)
	{	
		QueryFilter queryFilter = new QueryFilter(request);
		return queryFilter;
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping({"listData"})
	@Action(description = "新闻评论内容列表",detail="新闻评论内容列表", exectype = SysAuditExecType.SELECT_TYPE)
	public void listData(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
/*		String export = request.getParameter("_export");

		if (StringUtils.isNotEmpty(export)) {
			String exportAll = request.getParameter("_all");
			if (StringUtils.isNotEmpty(exportAll))
				exportAllPages(request, response);
			else
				exportCurPage(request, response);
		}
		else {*/
			response.setContentType("application/json");
			QueryFilter queryFilter = getQueryFilter(request);
			List<InsNewsCm> list = insNewsCmService.getAll(queryFilter);
			List<InsNewsCm> list2 = new ArrayList<InsNewsCm>();
			for (int i = 0; i < list.size(); i++) {
				InsNewsCm cm = (InsNewsCm)list.get(i);
				cm.setNewsTitle(this.insNewsService.getById(Long.valueOf(cm.getNewId())).getSubject());
				list2.add(cm);
			}
			JsonPageResult result = new JsonPageResult(list2, queryFilter.getPagingBean().getTotalCount());
			
			ObjectMapper mapper=new ObjectMapper();
			String jsonResult = mapper.writeValueAsString(result);
			PrintWriter pw = response.getWriter();
			pw.println(jsonResult);
			pw.close();
/*		}*/
	}

	
	@SuppressWarnings("unchecked")
	public List<InsNewsCm> findAllReply(Long rplId)
	{
		List<InsNewsCm> rplCms = new ArrayList<InsNewsCm>();
		List<InsNewsCm> list = insNewsCmService.getByReplyId(rplId);
		if (list.size() != 0) {
			for (InsNewsCm rplcm : list) {
				rplCms.add(rplcm);
				rplCms.addAll(findAllReply(rplcm.getCommId()));
			}
		}
		SortReply sort = new SortReply();
		Collections.sort(rplCms, sort);
		return rplCms;
	}

	
	@RequestMapping({"del"})
	@ResponseBody
	@Action(description = "删除新闻评论内容", execOrder = ActionExecOrder.BEFORE, detail = "删除新闻评论内容<#list ids?split(\",\") as item><#assign entity=insNewsCmService.getById(Long.valueOf(item))/> ${entity.fullName}【${entity.content}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public JsonResult del(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids)
			{
				if ("yes".equals(this.insNewsCmService.getById(Long.valueOf(id)).getIsReply())) {
					this.insNewsCmService.delById(Long.valueOf(id));
				} else {
					List<InsNewsCm> replyCms = findAllReply(Long.valueOf(id));
					for (InsNewsCm replyCm : replyCms) {
						this.insNewsCmService.delById(Long.valueOf(replyCm.getCommId()));
					}
					this.insNewsCmService.delById(Long.valueOf(id));
				}
			}
		}
		return new JsonResult(true, "成功删除！");
	}
	
	
	@RequestMapping({"listByNewId"})
	@ResponseBody
	public JsonPageResult<InsNewsCm> listByNewId(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		QueryFilter queryFilter = getQueryFilter(request);
		Long newId = RequestUtil.getLong(request,"newId");
		List<InsNewsCm> insNewsCms = this.insNewsCmService.getByNewId(newId);
		return new JsonPageResult<InsNewsCm>(insNewsCms, queryFilter.getPagingBean().getTotalCount());
	}

	
	@RequestMapping({"get"})
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String pkId = request.getParameter("pkId");
		InsNewsCm insNewsCm = null;
		if (StringUtils.isNotBlank(pkId))
			insNewsCm = this.insNewsCmService.getById(Long.valueOf(pkId));
		else {
			insNewsCm = new InsNewsCm();
		}
		return getAutoView().addObject("insNewsCm", insNewsCm);
	}

	
	@RequestMapping({"edit"})
	@Action(description = "编辑新闻评论内容",detail="编辑新闻评论内容", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String pkId = request.getParameter("pkId");
		String forCopy = request.getParameter("forCopy");
		InsNewsCm insNewsCm = null;
		if (StringUtils.isNotEmpty(pkId)) {
			insNewsCm = this.insNewsCmService.getById(Long.valueOf(pkId));
			if ("true".equals(forCopy))
				insNewsCm.setCommId(null);
		}
		else {
			insNewsCm = new InsNewsCm();
		}
		return getAutoView().addObject("insNewsCm", insNewsCm);
	}

}
