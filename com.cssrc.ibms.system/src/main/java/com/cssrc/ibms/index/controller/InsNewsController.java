package com.cssrc.ibms.index.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
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

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonPageResult;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.Comment;
import com.cssrc.ibms.index.model.InsColNew;
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.model.InsNews;
import com.cssrc.ibms.index.model.InsNewsCm;
import com.cssrc.ibms.index.model.ReplyComment;
import com.cssrc.ibms.index.model.SortReply;
import com.cssrc.ibms.index.service.InsColNewService;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsNewsCmService;
import com.cssrc.ibms.index.service.InsNewsService;

/**
 * 新闻公告功能Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insNews/" })
@Action(ownermodel = SysAuditModelType.NEWS_ANNOUNCE_MANAGEMENT)
public class InsNewsController extends BaseController{


	@Resource
	InsNewsService insNewsService;

	@Resource
	InsColNewService insColNewService;

	@Resource
	InsColumnService insColumnService;

	@Resource
	InsNewsCmService insNewsCmService;


	@RequestMapping({"publish"})
	@Action(description = "新闻公告发布",detail="新闻公告发布", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView publish(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String newIds = request.getParameter("pkId");
		String[] pkIds = newIds.split(",");
		StringBuffer newId = new StringBuffer();
		StringBuffer newTitle = new StringBuffer();
		for (int i = 0; i < pkIds.length; i++) {
			InsNews insNews = (InsNews)this.insNewsService.getById(Long.valueOf(pkIds[i]));
			if ("Draft".equals(insNews.getStatus())) {
				newId.append(insNews.getNewId()).append(",");
				newTitle.append(insNews.getSubject()).append(",");
			}
		}
		if (newId.length() > 0) {
			newId.deleteCharAt(newId.length() - 1);
			newTitle.deleteCharAt(newTitle.length() - 1);
		}
		return getAutoView().addObject("newId", newId.toString()).addObject("newTitle", newTitle.toString());
	}

	@RequestMapping({"byColId"})
	public ModelAndView byColId(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String colId = request.getParameter("colId");
		InsColumn column = this.insColumnService.getById(Long.valueOf(colId));
		return getAutoView().addObject("insColumn", column);
	}

	/*@RequestMapping({"inMail"})
	public ModelAndView inMail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String userId = ContextUtil.getCurrentUserId();
		MailFolder mailFolder = this.mailFolderService.getInnerMailFolderByUserIdAndType(userId, "RECEIVE-FOLDER");

		ModelAndView view = getAutoView();
		if (mailFolder != null) {
			view.addObject("folderId", mailFolder.getFolderId());
		}

		return view;
	}*/


	@RequestMapping({"listByColId"})
	@ResponseBody
	@Action(description = "新闻公告列表",detail="新闻公告列表", exectype = SysAuditExecType.SELECT_TYPE)
	public JsonPageResult<InsNews> listByColId(HttpServletRequest request, HttpServletResponse response)
			throws Exception
			{
		String colId = request.getParameter("colId");
		QueryFilter queryFilter = new QueryFilter(request);
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		queryFilter.addFilterForIB("orgId", orgId);
		queryFilter.addSorted("createTime", "DESC");
/*		queryFilter.getFieldLogic().getCommands().add(new QueryParam("insColumn.colId", "EQ", colId));
		queryFilter.getFieldLogic().getCommands().add(new QueryParam("startTime", "LT", new Date()));
		queryFilter.getFieldLogic().getCommands().add(new QueryParam("endTime", "GT", new Date()));*/

		List dataList = this.insColNewService.getAll(queryFilter);
		return new JsonPageResult(dataList, queryFilter.getPagingBean().getTotalCount());
			}
	
	/**
	 *将新闻添加进栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"joinColumn"})
	@ResponseBody
	@Action(description = "将新闻添加进栏目", execOrder = ActionExecOrder.AFTER, detail = "将新闻 <#list newsIds?split(\",\") as item><#assign entity=insNewsService.getById(Long.valueOf(item))/> ${entity.subject}【${entity.author}】</#list>添加进栏目 【${columnName}】", exectype = SysAuditExecType.ADD_TYPE)
	public JsonResult joinColumn(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long colId = RequestUtil.getLong(request,"colId");
		InsColumn column = (InsColumn)this.insColumnService.getById(colId);
		String newsIds = request.getParameter("newsIds");
		String[] nIds = newsIds.split("[,]");
		int i = 1;
		for (String nId : nIds)
		{
			InsColNew insColNew = this.insColNewService.getByColIdNewId(colId, Long.valueOf(nId));
			if (insColNew != null)
			{
				continue;
			}
			InsNews news = (InsNews)this.insNewsService.getById(Long.valueOf(nId));
			InsColNew cn = new InsColNew();
			cn.setStartTime(new Date());
			Calendar cal = Calendar.getInstance();

			cal.set(1, cal.get(1) + 2);
			cn.setEndTime(cal.getTime());
			cn.setNewId(Long.valueOf(nId));
		/*	cn.setInsNews(news);
			cn.setInsColumn(column);*/
			cn.setColId(colId);
			cn.setSn(Integer.valueOf(i++));
			this.insColNewService.add(cn);
		}
		LogThreadLocalHolder.putParamerter("columnName", column.getName());
		return new JsonResult(true, "成功加入!");
	}

	
	@RequestMapping({"getValids"})
	@ResponseBody
	public JsonPageResult<InsNews> getValids(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		QueryFilter queryFilter = new QueryFilter(request);
		queryFilter.addFilterForIB("status", "Issued");
		List<InsNews> newsList = this.insNewsService.getAll(queryFilter);
		return new JsonPageResult<InsNews>(newsList, queryFilter.getPagingBean().getTotalCount());
	}
	
	
	@RequestMapping({"del"})
	@ResponseBody
	@Action(description = "删除新闻", execOrder = ActionExecOrder.BEFORE , detail = "删除新闻 <#list ids?split(\",\") as item><#assign entity=insNewsService.getById(Long.valueOf(item))/> ${entity.subject}【${entity.author}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public JsonResult del(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		String uId = request.getParameter("ids");
		if (StringUtils.isNotEmpty(uId)) {
			String[] ids = uId.split(",");
			for (String id : ids) {
				this.insNewsService.delById(Long.valueOf(id));
			}
		}
		return new JsonResult(true, "成功删除！");
	}

	
	@SuppressWarnings("unchecked")
	public List<InsNewsCm> findAllReply(Long rplId){
		List<InsNewsCm> rplCms = new ArrayList<InsNewsCm>();
		if (this.insNewsCmService.getByReplyId(Long.valueOf(rplId)).size() != 0) {
			for (InsNewsCm rplcm : this.insNewsCmService.getByReplyId(Long.valueOf(rplId))) {
				rplCms.add(rplcm);
				rplCms.addAll(findAllReply(rplcm.getCommId()));
			}
		}
		SortReply sort = new SortReply();
		Collections.sort(rplCms, sort);
		return rplCms;
	}

	
	@RequestMapping({"get"})
	@Action(description = "新闻公告明细",detail="新闻公告明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception{
		String permit = request.getParameter("permit");
		Long pkId = RequestUtil.getLong(request,"pkId");
		
		Long curUserId = UserContextUtil.getCurrentUserId();
		if(curUserId < 0L){ //只有系统管理员账户可对所有删除
			permit = "yes";
		}
		String user = UserContextUtil.getCurrentUser().getFullname();

		InsNews insNew = (InsNews)this.insNewsService.getById(pkId);
		int readTime = insNew.getReadTimes().intValue();
		readTime++;
		insNew.setReadTimes(Integer.valueOf(readTime));
		this.insNewsService.update(insNew);

		List<InsNewsCm> replyCms = null;
		List<InsNewsCm> insNewsCms = this.insNewsCmService.getByNewId(pkId);
		List<Comment> comments = new ArrayList<Comment>();
		for (InsNewsCm insNewsCm : insNewsCms)
		{
			if ("no".equals(insNewsCm.getIsReply())) {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				String dateString = formatter.format(insNewsCm.getCreateTime());
				String content = insNewsCm.getContent().replace("\r\n", "<br>");
				replyCms = findAllReply(insNewsCm.getCommId());
				List<ReplyComment> replyComments = new ArrayList<ReplyComment>();
				for (InsNewsCm replyCm : replyCms) {
					String replyData = formatter.format(replyCm.getCreateTime());
					String replycontent = replyCm.getContent().replace("\r\n", "<br>");

					ReplyComment rplcom = new ReplyComment(replyCm.getCommId(), replyCm.getFullName(), 
							replyData, replycontent, ((InsNewsCm)this.insNewsCmService.getById(Long.valueOf(replyCm.getRepId()))).getFullName(), replyCm.getRepId());
					replyComments.add(rplcom);
				}

				Comment comment = new Comment(insNewsCm.getCommId(), insNewsCm.getFullName(), dateString, content, replyComments,Long.valueOf(insNewsCm.getCreateBy()));
				comments.add(comment);
			}
		}
		ModelAndView mv = getAutoView();
		InsNews insNews = null;
		if (pkId !=null)
			insNews = (InsNews)this.insNewsService.getById(pkId);
		else {
			insNews = new InsNews();
		}
		mv.addObject("permit", permit);
		mv.addObject("user", user);

		ObjectMapper mapper=new ObjectMapper();
		String commentStr = mapper.writeValueAsString(comments);

		mv.addObject("comments", commentStr);
		
		mv.addObject("curUserId", curUserId);
		return mv.addObject("insNews", insNews);
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		String pkId = request.getParameter("pkId");

		String forCopy = request.getParameter("forCopy");
		InsNews insNews = null;
		if (StringUtils.isNotEmpty(pkId)) {
			insNews = (InsNews)this.insNewsService.getById(Long.valueOf(pkId));
			if ("true".equals(forCopy))
				insNews.setNewId(null);
		}
		else {
			insNews = new InsNews();
		}

		return getAutoView().addObject("insNews", insNews);
	}

	
	@RequestMapping({"getAll"})
	@ResponseBody
	public List<InsNews> getAll(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		List<InsNews> insNews = this.insNewsService.getAll();
		return insNews;
	}

}
