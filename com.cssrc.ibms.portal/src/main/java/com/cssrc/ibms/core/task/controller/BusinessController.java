package com.cssrc.ibms.core.task.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.rpc.intf.CommonService;
import com.cssrc.ibms.api.system.model.BaseInsPortalParams;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.model.InsColType;
import com.cssrc.ibms.index.model.InsColumn;
import com.cssrc.ibms.index.model.InsPortCol;
import com.cssrc.ibms.index.model.InsPortal;
import com.cssrc.ibms.index.model.InsPortalParams;
import com.cssrc.ibms.index.model.PortalColumn;
import com.cssrc.ibms.index.model.PortalConfig;
import com.cssrc.ibms.index.service.InsColTypeService;
import com.cssrc.ibms.index.service.InsColumnService;
import com.cssrc.ibms.index.service.InsPortColService;
import com.cssrc.ibms.index.service.InsPortalService;
import com.cssrc.ibms.index.service.PortalConfigService;
/**
 * BusinessController
 * 事务处理
 * @author liubo
 * @date 2017年4月20日
 */
@Controller
@RequestMapping("/oa/task/business/")
public class BusinessController extends BaseController {
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
	/**
	 * 进入任务列表展示页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "show" })
	public ModelAndView show(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
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
		insPortal = this.insPortalService.getPortalByRights();
		 //初始化个人无布局则读默认布局设置
		if (insPortal == null) {
			insPortal = this.insPortalService.getByKey("GLOBAL-PERSONAL", "0");
			/*portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());*/
		}/*else {
			portcols = this.insPortColService.getPersonalPort(insPortal.getPortId(), userId);
		}*/
		portcols = this.insPortColService.getGlobalPortal(insPortal.getPortId());
		ModelAndView mv = getAutoView();
		try{
		List<PortalColumn> portalCols = new ArrayList<PortalColumn>();
		for (InsPortCol portcol : portcols) {
			InsColumn insColumn = null;
			if (portcol.getColId() != null) {
				insColumn = (InsColumn)this.insColumnService.getById(Long.valueOf(portcol.getColId()));
			}
			if (insColumn != null && !"DISABLED".equals(insColumn.getEnabled())) {

				InsColType insColType = insColTypeService.getById(insColumn.getTypeId());
				PortalColumn portalCol = new PortalColumn(insColumn.getName(), portcol.getColNum().intValue(), portcol.getSn().intValue(), portcol.getHeight().intValue(), insColType.getUrl(), insColType.getMoreUrl(), portcol.getColId());

				String url = insColType.getUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
				url = url.replace("{colId}", portcol.getColId());
				if (!url.toUpperCase().startsWith("HTTP")) {
					url = request.getContextPath() + url;
				}

				String moreUrl = insColType.getMoreUrl().replace("{pageSize}", insColumn.getNumsOfPage().toString());
				moreUrl = moreUrl.replace("{colId}", portcol.getColId());
				if (!moreUrl.toUpperCase().startsWith("HTTP")) {
					moreUrl = request.getContextPath() + moreUrl;
				}

				portalCol.setMoreUrl(moreUrl);

				portalCol.setLoadType(insColType.getLoadType());

				portalCol.setUrl(url);
				portalCol.setIconCls(insColType.getIconCls());
				portalCols.add(portalCol);
			}
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
			/*StringWriter wr=new StringWriter();
			JsonGenerator gen=new JsonFactory().createJsonGenerator(wr);
			mapper.writeValue(wr, portalCols);*/
			//String portalStr = wr.toString();
			String portalStr = mapper.writeValueAsString(portalCols);


			mv.addObject("portalCols",portalStr);
		}catch(Exception e){
			e.printStackTrace();
		}

		return mv.addObject("insPortal", insPortal).addObject("curUserId",userId);

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
		param.setOrgId("123");
		param.setUserId("3622");
		//param.setOrgId(orgId);
		//param.setUserId(userId);
		String html = null;
		String rpcUrl = "/ibms";
		try {
			String rpcrefname = null;
			String templateId = insColType.getTempId();
			String memo = insColType.getMemo();
			rpcUrl = insColType.getUrl();
			if(StringUtil.isNotEmpty(memo)&&memo.contains("rpc")){
				String[] rpc = memo.split("=");
				rpcrefname = rpc[1];
			}
			Object result = null;
			//判断是否有RPC远程过程调用服务
			//String rpcrefname = null;
			//String rpcrefname = "htglConsumerProjectService";
			
			if("taskListData".equals(templateId)&&StringUtil.isNotEmpty(rpcrefname)){
				//采用IOC方式，根据RPC远程过程调用服务调用数据
				CommonService commonService = (CommonService)  AppUtil.getContext().getBean(rpcrefname);
				result = commonService.getTaskMap(param);
			}else{
				PortalConfig portalConfig = (PortalConfig)this.portalConfigService.getPortalConfigMap().get(templateId);
				String[] services = portalConfig.getService().split("[.]");
				Object serviceBean = AppUtil.getBean(services[0]);
				Method method = serviceBean.getClass().getDeclaredMethod(services[1], new Class[] { BaseInsPortalParams.class });

				result = method.invoke(serviceBean, new Object[] { param });
			}
			
			Map<String,Object> model = new HashMap<String,Object>();
			model.put("ctxPath", request.getContextPath());
			model.put("rpcUrl", rpcUrl);
			if ((result instanceof Map))
				model.putAll((Map)result);
			else {
				model.put("result", result);
			}
			html = this.freemarkEngine.mergeTemplateIntoString("portal/ins/" + templateId + ".ftl", model);
		} catch (Exception ex) {
			ex.printStackTrace();
			this.logger.error(ex.getMessage());
			html = "模板加载出错！请联系管理员！";
		}
		return getAutoView().addObject("html", html);
	}

	
}
