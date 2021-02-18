package com.cssrc.ibms.index.controller;


import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.activity.intf.ITaskReminderService;
import com.cssrc.ibms.api.activity.model.IProcessTask;
import com.cssrc.ibms.api.activity.model.ITaskReminder;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.custom.intf.ICustLinkListService;
import com.cssrc.ibms.api.custom.intf.IListConfsService;
import com.cssrc.ibms.api.system.intf.worktime.ICalendarAssignService;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.sysuser.intf.ISysObjRightsService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTacticService;
import com.cssrc.ibms.api.sysuser.model.ISysObjRights;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysOrgTactic;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.TimeUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JsonResult;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.model.InsPortal;
import com.cssrc.ibms.index.model.InsPortalParams;
import com.cssrc.ibms.index.model.PortalColumn;
import com.cssrc.ibms.index.model.PortalConfig;
import com.cssrc.ibms.index.model.PortalItem;
import com.cssrc.ibms.index.service.InsColTypeService;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsPortColService;
import com.cssrc.ibms.index.service.InsPortalService;
import com.cssrc.ibms.index.service.PortalConfigService;

import net.sf.json.JSONArray;

/**
 * 首页布局Controller
 * @author YangBo
 *
 */
@Controller
@RequestMapping({ "/oa/portal/insPortal/" })
public class InsPortalController extends BaseController {

	@Resource
	private InsPortalService insPortalService;

	@Resource
	private InsPortColService insPortColService;

	@Resource
	private InsColumnService insColumnService;

	@Resource
	private InsColTypeService insColTypeService;

	@Resource
	private FreemarkEngine freemarkEngine;

	@Resource
	private PortalConfigService portalConfigService;
	
	@Resource
	private ISysOrgService sysOrgService;
	
	@Resource
	private ISysOrgTacticService sysOrgTacticService;
	
	@Resource
	private ISysObjRightsService sysObjRightsService;
	
	@Resource
	private ICustLinkListService custLinkListService;


	 /**
	  * 删除布局
	  * @param request
	  * @param response
	  * @throws Exception
	  */
	@RequestMapping({"del"})
	@ResponseBody
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		String uId = request.getParameter("portId");
		try {
			if (StringUtils.isNotEmpty(uId)) {
				String[] ids = uId.split(",");
				for (String id : ids) {
					InsPortal curInsPortal =  this.insPortalService.getById(Long.valueOf(id));
					if(curInsPortal.getIsDefault().equals("NO")){
						this.insPortalService.delById(Long.valueOf(id));
						insPortColService.delByPortal(Long.valueOf(id));
						//删除绑定的权限
						sysObjRightsService.deleteByObjTypeAndObjectId(ISysObjRights.RIGHT_TYPE_INS_PORTAL,id);
						
					}
				}
			}
			message = new ResultMessage(1, "删除布局成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	/**
	 * 删除该布局下的栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"delCol"})
	@ResponseBody
	public JsonResult delCol(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long colId = RequestUtil.getLong(request,"colId");
		Long portId = RequestUtil.getLong(request,"portId");
		if (colId != 0L) {
			InsPortal insPortal = (InsPortal)this.insPortalService.getById(Long.valueOf(portId));
			//InsPortCol insPortCol = this.insPortColService.getByPortCol(portId, colId);
			/*insPortal.getInsPortCols().remove(insPortCol);*/
			
			//更新布局
			if(insPortal.getLayoutInfo()!=null) {
				JSONArray jarray = JSONArray.fromObject(insPortal.getLayoutInfo());
				List<Map<String,Object>> layoutInfo = (List)jarray;
				List<Map<String,Object>> new_layoutInfo = new ArrayList<Map<String,Object>>();
				for(Map<String,Object> col : layoutInfo) {
					String colId_ = (String) col.get("id");
					if(Long.valueOf(colId_)!=colId) {
						new_layoutInfo.add(col);
					}
				}
				insPortal.setLayoutInfo(new_layoutInfo.toString());
				this.insPortalService.update(insPortal);				
			}
			
			this.insPortColService.delByPortCol(portId, colId);
			return new JsonResult(true, "成功删除！");
		}
		return new JsonResult(true, "成功失败！");
	}
	
	@RequestMapping({"get"})
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long pkId = RequestUtil.getLong(request,"pkId");
		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(pkId);
		InsPortal insPortal = null;
		StringBuffer colName = new StringBuffer();
		ModelAndView mv = getAutoView();
		for (InsPortCol portcol : portcols) {
			if (portcol.getColId() != null) {
				InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
				colName.append(insColumn.getName()).append(",");
			}
		}
		if (colName.length() > 0) {
			colName.deleteCharAt(colName.length() - 1);
		}
		mv.addObject("colName", colName.toString());
		if (pkId != 0L) {
			insPortal = (InsPortal)this.insPortalService.getById(pkId);
		}
		return mv.addObject("insPortal", insPortal);
	}
	
	/**
	 * 首页展示
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"show"})
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String key = request.getParameter("key");
		String userId = UserContextUtil.getCurrentUserId().toString();
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		InsPortal insPortal = null;
		
		List<InsPortCol> portcols = new ArrayList<InsPortCol>();
		if ("ORG".equals(key)) {
			insPortal = this.insPortalService.getByKey("ORG", orgId);
			if (insPortal == null)
				insPortal = this.insPortalService.getByKey("GLOBAL-ORG", "0");
		}
		else if ("personal".equals(key)) {
			insPortal = this.insPortalService.getByIdKey("PERSONAL", orgId, userId);
			
		}
		//个人无设置走权限
		if(insPortal == null)
			insPortal = this.insPortalService.getPortalByRights();
		 //初始化个人无布局则读默认布局设置
		if (insPortal == null) {
			insPortal = this.insPortalService.getByKey("GLOBAL-PERSONAL", "0");
			/*portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());*/
		}/*else {
			portcols = this.insPortColService.getPersonalPort(insPortal.getPortId(), userId);
		}*/
		portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());
		
		Map<String,Object> portalInfo = new HashMap<String,Object>();
		portalInfo.put("id", insPortal.getPortId());
		
		List<Map<String,Object>> layoutInfo = new ArrayList<Map<String,Object>>();
		if(insPortal.getLayoutInfo()!=null) {
			JSONArray jarray = JSONArray.fromObject(insPortal.getLayoutInfo());
			layoutInfo = (List)jarray;
		}
		portalInfo.put("layout", layoutInfo);
		
		ModelAndView mv = getAutoView();
		try{
			List<PortalColumn> portalCols = new ArrayList<PortalColumn>();
			for (InsPortCol portcol : portcols) {
				String id = portcol.getColId();
				InsColumn insColumn = this.insColumnService.getById(Long.valueOf(id));
				if (!"DISABLED".equals(insColumn.getEnabled())) {

					InsColType insColType = insColTypeService.getById(insColumn.getTypeId());
					PortalItem portalCol = new PortalItem(id, insColumn.getName(), insColType.getLoadType());
					
					String url="";
					String moreUrl="";
					if(insColType.getLoadType().equals("URL")){
						url = insColType.getUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
						url = url.replace("{colId}", id.toString());
						if (!url.toUpperCase().startsWith("HTTP")) {
							url = request.getContextPath() + url;
						}
					}
					moreUrl = insColType.getMoreUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
					moreUrl = moreUrl.replace("{colId}", id.toString());
					if (!moreUrl.toUpperCase().startsWith("HTTP")) {
						moreUrl = request.getContextPath() + moreUrl;
					}

					portalCol.setMoreUrl(moreUrl);
					portalCol.setUrl(url);
					portalCol.setKey(insColType.getKey());
					portalCol.setIconCls(insColType.getIconCls());					
					portalInfo.put(id, portalCol);
				}
			}

			ObjectMapper mapper = new ObjectMapper();
			String portalStr = mapper.writeValueAsString(portalInfo);

			mv.addObject("portalCols", portalStr);
			
		}catch(Exception e){
			e.printStackTrace();
		}

		return mv.addObject("insPortal", insPortal).addObject("curUserId",userId);
	}
	
	/**
	 * 获取模板栏目类型
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getPortalConfigs"})
	@ResponseBody
	public Collection<PortalConfig> getPortalConfigs(HttpServletRequest request, HttpServletResponse response) throws Exception {

		return this.portalConfigService.getPortalConfigMap().values(); 
	}

	/**
	 * 加载模板返回页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getPortalHtml"})
	public ModelAndView getPortalHtml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String colId = request.getParameter("colId");
		InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(colId));
		InsColType insColType = insColTypeService.getById(insColumn.getTypeId());

		InsPortalParams param = new InsPortalParams();
		param.setPageSize(insColumn.getNumsOfPage());
		String userId = UserContextUtil.getCurrentUserId().toString();
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		param.setOrgId(orgId);
		param.setUserId(userId);
		String html = null;
		try {
			String templateId = insColType.getTempId();
			PortalConfig portalConfig = (PortalConfig)this.portalConfigService.getPortalConfigMap().get(templateId);
			String[] services = portalConfig.getService().split("[.]");
			Object serviceBean = AppUtil.getBean(services[0]);
			Method method = serviceBean.getClass().getDeclaredMethod(services[1], new Class[] { BaseInsPortalParams.class});

			//对已进行催办的任务标识
			ITaskReminderService taskReminderService = (ITaskReminderService)AppUtil.getBean(ITaskReminderService.class);
			ICalendarAssignService calendarAssignService = (ICalendarAssignService)AppUtil.getBean(ICalendarAssignService.class);
			Object result = method.invoke(serviceBean, new Object[] { param});
			if("taskListData".equals(templateId)){
				Map resultMap = (Map)result;
				List<IProcessTask> list = (ArrayList<IProcessTask>) resultMap.get("pendingMatters");
				for(IProcessTask processTask:list){
					List<?extends ITaskReminder> taskReminders = taskReminderService.getByActDefAndNodeId(processTask.getProcessDefinitionId(), processTask.getTaskDefinitionKey());
					for (ITaskReminder taskReminder : taskReminders) {
						int isReminder = 0;
						Date relativeStartTime = calendarAssignService.getRelativeStartTime(processTask.getProcessInstanceId(), taskReminder.getRelativeNodeId(), taskReminder.getRelativeNodeType());
						
						if (relativeStartTime == null)
							break;
						Date dueDate = new Date(TimeUtil.getNextTime(1, taskReminder.getCompleteTime().intValue(), relativeStartTime.getTime()));
						Date curDate = new Date();
						if (dueDate.compareTo(curDate) < 0)
							isReminder = 1;
						processTask.setExpireDate(dueDate);
						processTask.setIsReminder(isReminder);
					}
				}
				resultMap.put("pendingMatters", list);
				result = resultMap;
			}
			Map<String,Object> model = new HashMap<String,Object>();			
			model.put("ctxPath", request.getContextPath());
			if ((result instanceof Map))
				model.putAll((Map)result);
			else {
				model.put("result", result);
			}
			html = this.freemarkEngine.mergeTemplateIntoString("portal/ins/" + templateId + ".ftl", model);
			//logger.info(html);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.logger.error(ex.getMessage());
			html = "模板加载出错！请联系管理员！";
		}
		return getAutoView().addObject("html", html);
	}

	/**
	 * 编辑布局
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"showEdit"})
	public ModelAndView showEdit(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		InsPortal insPortal = this.insPortalService.getByKey("ORG", orgId);
		if (insPortal == null) {
			InsPortal insPortal1 = this.insPortalService.getByKey("GLOBAL-ORG", "0");
			insPortal = new InsPortal();
			insPortal.setPortId(UniqueIdUtil.genId());
			insPortal.setKey("ORG");
			insPortal.setName("公司");
			insPortal.setIsDefault("NO");
			insPortal.setOrgId(orgId);
			insPortal.setColNums(insPortal1.getColNums());
			insPortal.setColWidths(insPortal1.getColWidths());
			this.insPortalService.add(insPortal);
		}
		
		Map<String,Object> portalInfo = new HashMap<String,Object>();
		portalInfo.put("id", insPortal.getPortId());
		
		List<Map<String,Object>> layoutInfo = new ArrayList<Map<String,Object>>();
		if(insPortal.getLayoutInfo()!=null) {
			JSONArray jarray = JSONArray.fromObject(insPortal.getLayoutInfo());
			layoutInfo = (List)jarray;
		}
		portalInfo.put("layout", layoutInfo);
		
		ModelAndView mv = getAutoView();
		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());
		for (InsPortCol portcol : portcols) {
			String id = portcol.getColId();
			InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
			String closeAllow = insColumn.getAllowClose();
			InsColType insColType = insColTypeService.getById(insColumn.getTypeId());
			String url = insColType.getUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
			url = url.replace("{colId}", portcol.getColId().toString());
			if (!url.toUpperCase().startsWith("HTTP")) {
				url = request.getContextPath() + url;
			}

			String moreUrl = insColType.getMoreUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
			moreUrl = moreUrl.replace("{colId}", portcol.getColId().toString());
			if (!moreUrl.toUpperCase().startsWith("HTTP")) {
				moreUrl = request.getContextPath() + moreUrl;
			}
			
			PortalItem portalCol = new PortalItem(id, insColumn.getName(), insColType.getLoadType());
			portalCol.setMoreUrl(moreUrl);
			portalCol.setUrl(url);
			portalCol.setKey(insColType.getKey());
			portalCol.setIconCls(insColType.getIconCls());
			portalCol.setCloseAllow(closeAllow);			
			portalInfo.put(id, portalCol);
		}

		String[] widths = insPortal.getColWidths().split(",");
		StringBuffer colWidths = new StringBuffer();
		for (int i = 0; i < widths.length; i++) {
			if (widths[i].contains("%")) {
				widths[i] = ("'" + widths[i] + "'");
			}
			colWidths.append(widths[i]).append(",");
		}
		if (colWidths.length() > 0) {
			colWidths.deleteCharAt(colWidths.length() - 1);
		}
		mv.addObject("colWidths", colWidths.toString());
		ObjectMapper mapper=new ObjectMapper();
		String portalStr = mapper.writeValueAsString(portalInfo);
		mv.addObject("portalCols",portalStr);
		return mv.addObject("insPortal", insPortal);
	}

	/**
	 * 个人布局编辑页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"personShowEdit"})
	public ModelAndView personShowEdit(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		String userId = UserContextUtil.getCurrentUserId().toString();
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Date currdate = new Date();
		InsPortal insPortal = this.insPortalService.getByIdKey("PERSONAL", orgId, userId);
		if (insPortal == null) {
			InsPortal insPortal1 = this.insPortalService.getByKey("GLOBAL-PERSONAL", "0");
			insPortal = new InsPortal();
			insPortal.setPortId(UniqueIdUtil.genId());
			insPortal.setKey("PERSONAL");
			insPortal.setName("个人");
			insPortal.setIsDefault("NO");
			insPortal.setUserId(userId);
			insPortal.setOrgId(orgId);
			insPortal.setCreateBy(userId);
			insPortal.setCreateTime(currdate);
			insPortal.setUpdateTime(currdate);
			insPortal.setUpdateBy(userId);
			insPortal.setColNums(insPortal1.getColNums());
			insPortal.setColWidths(insPortal1.getColWidths());
			this.insPortalService.add(insPortal);
		}
		
		Map<String,Object> portalInfo = new HashMap<String,Object>();
		portalInfo.put("id", insPortal.getPortId());
		
		List<Map<String,Object>> layoutInfo = new ArrayList<Map<String,Object>>();
		if(insPortal.getLayoutInfo()!=null) {
			JSONArray jarray = JSONArray.fromObject(insPortal.getLayoutInfo());
			layoutInfo = (List)jarray;
		}
		portalInfo.put("layout", layoutInfo);
		
		ModelAndView mv = getAutoView();
		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());
		for (InsPortCol portcol : portcols) {
			String id = portcol.getColId();
			InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
			String closeAllow = insColumn.getAllowClose();
			InsColType insColType = insColTypeService.getById(insColumn.getTypeId());
			
			String url = insColType.getUrl();
			if(url != null) {
				url = url.replace("{pageSize}", CommonTools.Obj2String(insColumn.getNumsOfPage()));
				url = url.replace("{colId}", portcol.getColId().toString());
				if (!url.toUpperCase().startsWith("HTTP")) {
					url = request.getContextPath() + url;
				}
			}
			
			String moreUrl = insColType.getMoreUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
			moreUrl = moreUrl.replace("{colId}", portcol.getColId().toString());
			if (!moreUrl.toUpperCase().startsWith("HTTP")) {
				moreUrl = request.getContextPath() + moreUrl;
			}
			
			PortalItem portalCol = new PortalItem(id, insColumn.getName(), insColType.getLoadType());
			portalCol.setMoreUrl(moreUrl);
			portalCol.setUrl(url);
			portalCol.setKey(insColType.getKey());
			portalCol.setIconCls(insColType.getIconCls());
			portalCol.setCloseAllow(closeAllow);			
			portalInfo.put(id, portalCol);
		}

		String[] widths = insPortal.getColWidths().split(",");
		StringBuffer colWidths = new StringBuffer();
		for (int i = 0; i < widths.length; i++) {
			if (widths[i].contains("%")) {
				widths[i] = ("'" + widths[i] + "'");
			}
			colWidths.append(widths[i]).append(",");
		}
		if (colWidths.length() > 0) {
			colWidths.deleteCharAt(colWidths.length() - 1);
		}
		mv.addObject("colWidths", colWidths.toString());

		ObjectMapper mapper=new ObjectMapper();
		String portalStr = mapper.writeValueAsString(portalInfo);
		mv.addObject("portalCols",portalStr);

		return mv.addObject("insPortal", insPortal);
	}

	/**
	 * 全局编辑页面
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"global"})
	public ModelAndView global(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Long portId = Long.valueOf(RequestUtil.getLong(request, "portId"));
		InsPortal insPortal = insPortalService.getById(portId);
		
		String userId = UserContextUtil.getCurrentUserId().toString();
		String orgId = UserContextUtil.getCurrentOrgId().toString();
		Date currdate = new Date();
		if (insPortal == null) {
			InsPortal insPortal1 = this.insPortalService.getByKey("GLOBAL-PERSONAL", "0");
			insPortal = new InsPortal();
			insPortal.setPortId(UniqueIdUtil.genId());
			insPortal.setKey("COMMON");
			insPortal.setName("未命名");
			insPortal.setIsDefault("NO");
			insPortal.setOrgId(orgId);
			insPortal.setCreateBy(userId);
			insPortal.setCreateTime(currdate);
			insPortal.setUpdateTime(currdate);
			insPortal.setUpdateBy(userId);
			insPortal.setColNums(insPortal1.getColNums());
			insPortal.setColWidths(insPortal1.getColWidths());
			this.insPortalService.add(insPortal);
		}
		
		Map<String,Object> portalInfo = new HashMap<String,Object>();
		portalInfo.put("id", insPortal.getPortId());
		
		List<Map<String,Object>> layoutInfo = new ArrayList<Map<String,Object>>();
		if(insPortal.getLayoutInfo()!=null && !insPortal.getLayoutInfo().equals("")) {
			JSONArray jarray = JSONArray.fromObject(insPortal.getLayoutInfo());
			layoutInfo = (List)jarray;
		}
		portalInfo.put("layout", layoutInfo);
		
		ModelAndView mv = getAutoView();
		
		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());
		for (InsPortCol portcol : portcols) {
			String id = portcol.getColId();
			InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
			if (insColumn == null) {
				continue;
			}
			InsColType insColType = insColTypeService.getById(Long.valueOf(insColumn.getTypeId()));
			String closeAllow = insColumn.getAllowClose();
			
			String url = insColType.getUrl();
			if(url != null) {
				url = url.replace("{pageSize}", CommonTools.Obj2String(insColumn.getNumsOfPage()));
				url = url.replace("{colId}", portcol.getColId().toString());
				if (!url.toUpperCase().startsWith("HTTP")) {
					url = request.getContextPath() + url;
				}
			}
			
			String moreUrl = insColType.getMoreUrl().replace("{pageSize}", CommonTools.Obj2String(insColumn.getNumsOfPage()));
			moreUrl = moreUrl.replace("{colId}", portcol.getColId().toString());
			if (!moreUrl.toUpperCase().startsWith("HTTP")) {
				moreUrl = request.getContextPath() + moreUrl;
			}
			
			PortalItem portalCol = new PortalItem(id, insColumn.getName(), insColType.getLoadType());
			portalCol.setMoreUrl(moreUrl);
			portalCol.setUrl(url);
			portalCol.setKey(insColType.getKey());
			portalCol.setIconCls(insColType.getIconCls());
			portalCol.setCloseAllow(closeAllow);			
			portalInfo.put(id, portalCol);
		}

		String[] widths = insPortal.getColWidths().split(",");
		StringBuffer colWidths = new StringBuffer();
		for (int i = 0; i < widths.length; i++) {
			if (widths[i].contains("%")) {
				widths[i] = ("'" + widths[i] + "'");
			}
			colWidths.append(widths[i]).append(",");
		}
		if (colWidths.length() > 0) {
			colWidths.deleteCharAt(colWidths.length() - 1);
		}
		mv.addObject("colWidths", colWidths.toString());

		ObjectMapper mapper=new ObjectMapper();
		String portalStr = mapper.writeValueAsString(portalInfo);
		mv.addObject("portalCols",portalStr);

		return mv.addObject("insPortal", insPortal);
	}

	@RequestMapping({"editPort"})
	public ModelAndView editPort(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long pkId = RequestUtil.getLong(request,"portId");

		ModelAndView mv = getAutoView();
		InsPortal insPortal = null;

		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(pkId);
		StringBuffer colName = new StringBuffer();
		StringBuffer colId = new StringBuffer();
		for (InsPortCol portcol : portcols) {
			if (StringUtil.isNotEmpty(portcol.getColId())) {
				InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
				colName.append(insColumn.getName()).append(",");
				colId.append(insColumn.getColId()).append(",");
			}
		}
		if (colName.length() > 0) {
			colName.deleteCharAt(colName.length() - 1);
			colId.deleteCharAt(colId.length() - 1);
		}
		mv.addObject("colName", colName.toString());
		mv.addObject("colId", colId.toString());
		if (pkId != 0L) {
			insPortal = (InsPortal)this.insPortalService.getById(pkId);
		}
		return mv.addObject("insPortal", insPortal);
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long pkId = RequestUtil.getLong(request,"pkId");

		String forCopy = request.getParameter("forCopy");
		ModelAndView mv = getAutoView();
		InsPortal insPortal = null;

		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(pkId);
		StringBuffer colName = new StringBuffer();
		StringBuffer colId = new StringBuffer();
		for (InsPortCol portcol : portcols) {
			if (portcol.getColId() != null) {
				InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
				colName.append(insColumn.getName()).append(",");
				colId.append(insColumn.getColId()).append(",");
			}
		}
		if (colName.length() > 0) {
			colName.deleteCharAt(colName.length() - 1);
			colId.deleteCharAt(colId.length() - 1);
		}
		mv.addObject("colName", colName.toString());
		mv.addObject("colId", colId.toString());
		if (pkId != 0L) {
			insPortal = (InsPortal)this.insPortalService.getById(Long.valueOf(pkId));
			if ("true".equals(forCopy))
				insPortal.setPortId(null);
		}
		else {
			insPortal = new InsPortal();
		}
		return mv.addObject("insPortal", insPortal);
	}

	/**
	 * 新增栏目
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"addColumn"})
	public ModelAndView addColumn(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		Long pkId = RequestUtil.getLong(request,"portId");

		ModelAndView mv = getAutoView();
		InsPortal insPortal = null;

		List<InsPortCol> portcols = this.insPortColService.getGlobalPortal(pkId);
		StringBuffer colName = new StringBuffer();
		StringBuffer colId = new StringBuffer();
		for (InsPortCol portcol : portcols) {
			if (portcol.getColId() != null) {
				InsColumn insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
				if (insColumn != null) {
					colName.append(insColumn.getName()).append(",");
					colId.append(insColumn.getColId()).append(",");
				}
			}
		}
		if (colName.length() > 0) {
			colName.deleteCharAt(colName.length() - 1);
			colId.deleteCharAt(colId.length() - 1);
		}
		mv.addObject("colName", colName.toString());
		mv.addObject("colId", colId.toString());
		if (pkId != null) {
			insPortal = (InsPortal)this.insPortalService.getById(pkId);
		}
		return mv.addObject("insPortal", insPortal);
	}

	/**
	 * 布局管理列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "list" })
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) 
			throws Exception {
		QueryFilter filter = new QueryFilter(request,"insPortalItem");
		boolean isSuperAdmin = UserContextUtil.isSuperAdmin();
		if (!isSuperAdmin) {
			List orgIds = sysOrgService.getOrgsByUserId(UserContextUtil.getCurrentUserId());
			filter.addFilterForIB("orgIds", StringUtils.join(orgIds, ","));
		}
		List<InsPortal> list = this.insPortalService.getAll(filter);
		for (InsPortal insPortal : list) {
			if (BeanUtils.isNotIncZeroEmpty(insPortal.getOrgId())) {
				ISysOrg Org = (ISysOrg) this.sysOrgService.getById(Long.valueOf(insPortal.getOrgId()));
				if(Org != null)
					insPortal.setOrgName(Org.getOrgName());
			}
		}
		ISysOrgTactic sysOrgTactic = this.sysOrgTacticService.getOrgTactic();
		return getAutoView().addObject("objType",ISysObjRights.RIGHT_TYPE_INS_PORTAL)
				.addObject("isSuperAdmin", Boolean.valueOf(isSuperAdmin))
				.addObject("insPortalList", list)
				.addObject("orgTactic",sysOrgTactic.getOrgTactic());
	}
	
	
	@RequestMapping( { "getDataListHtml" })
	public ModelAndView getDataListHtml(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mv = new ModelAndView("/oa/portal/listConfsPreview.jsp");
		IListConfsService serice = (IListConfsService)AppUtil.getBean(IListConfsService.class);
		Map<String,Object> map = new HashMap();
		map.put("__ctx", request.getContextPath());
		map.put("__tic", "bpmDataTemplate");
		String html = serice.getDataListHtml(UserContextUtil.getCurrentUserId(),map);
		mv.addObject("html",html);
		return mv;
	}   	    
    @RequestMapping("getLinkListHtml")
    public ModelAndView getLinkListHtml(HttpServletRequest request,HttpServletResponse response)
        throws Exception
    {
		ModelAndView mv = new ModelAndView("/oa/portal/custLinkListPreview.jsp");
		String html = custLinkListService.getDataListHtml();
		String attachHtml = custLinkListService.getAttachListHtml();
		mv.addObject("html",html).addObject("attachHtml",attachHtml);
		return mv;
    }}
