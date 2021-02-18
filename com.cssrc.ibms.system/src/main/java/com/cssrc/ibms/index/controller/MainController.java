package com.cssrc.ibms.index.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.intf.ISysLogService;
import com.cssrc.ibms.api.system.model.ISysLog;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.index.service.SysIndexMyLayoutService;
import com.cssrc.ibms.layout.service.SysLayoutService;
import com.cssrc.ibms.system.model.CurrentSystem;
import com.cssrc.ibms.system.model.Resources;
import com.cssrc.ibms.system.service.ResourcesService;
import com.cssrc.ibms.system.service.SysParameterService;
/**
 * 
 * @author YangBo 
 * main页面控制层
 *
 */
@Controller
@RequestMapping("/oa/console")
public class MainController extends BaseController {
	@Resource
	private IPositionService positionService;
	@Resource
	private SysIndexMyLayoutService sysIndexMylayoutService;
	@Resource
	private ISysOrgService sysOrgService;
	@Resource
	private ResourcesService resourcesService;
	@Resource
	SysParameterService sysParameterService;
	@Resource
	private IBpmService bpmService;
	@Resource
	private IMessageSendService messageSendService;
	@Resource
	private ISysLogService sysLogService;
	@Resource
	private SysLayoutService sysLayoutService;
	/**
	 * 主页面
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author yangBo 2016-7-18
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping( { "main" })
	public ModelAndView main(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String flag=RequestUtil.getString(request, "flag");
		//获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		int isWarning = 0;
		String warningMsg = "";
		//日志空间预警功能只展示给三员用户
		if(curUser.getUserId().equals(ISysUser.CHECK_USER)||curUser.getUserId().equals(ISysUser.RIGHT_USER)
				||curUser.getUserId().equals(ISysUser.SYSTEM_USER)){
			List<?extends ISysLog> logList = sysLogService.getAll();
			if(logList.size()>900000&&logList.size()<1000000){
				isWarning = 1;
				warningMsg = "日志存储空间即将存满，请及时处理！";
			}else if(logList.size()>=1000000){
				isWarning = 2;
				warningMsg = "日志存储空间已存满，请及时处理！";
			}
		}
		Long curUserId = curUser.getUserId();
		//获取用户角色
		String roleNames = curUser.getRoles();
		//关联表 CWM_SYS_ORG 和 CWM_SYS_USER_POSITION，获取用户组织
		List<?extends ISysOrg> sysOrgs = this.sysOrgService.getOrgsByUserId(curUserId);
		ISysOrg curSysOrg = (ISysOrg)UserContextUtil.getCurrentOrg();
		List<? extends IPosition> positions = this.positionService.getByUserId(curUserId);
		IPosition curPosition = (IPosition)UserContextUtil.getCurrentPos();
		String skinStyle = UserContextUtil.getCurrentUserSkin(request);
		CurrentSystem currentSystem=sysParameterService.getCurrentSystem();
		
		//未读消息
		Integer messCount = this.messageSendService.getCountReceiverByUser(UserContextUtil.getCurrentUserId());
		
		//菜单信息
		List<Resources> resourcesList = this.resourcesService.getSysMenu(curUser);
		List<Resources> menuList = this.resourcesService.getMenuTree(resourcesList);
		JSONArray menuArr = JSONArray.fromObject(menuList);
			
		//布局
		String layout = sysLayoutService.getLayoutType(curUserId,curSysOrg);
		layout=layout.equals("main")?"newMain":layout;
		
		ModelAndView mv = null;
		int type = SysConfConstant.SHOW_TYPE;
		if(flag.equals("")){
			if(type==1){
				//三员账户 或者implement 走此
				if(curUser.getUserId().equals(ISysUser.CHECK_USER)||curUser.getUserId().equals(ISysUser.RIGHT_USER)
						||curUser.getUserId().equals(ISysUser.SYSTEM_USER)||curUser.getUserId().equals(ISysUser.IMPLEMENT_USER)){
					mv = getView("console", "newMain").addObject("menuList", menuArr).addObject("msgCount", messCount)
							.addObject("curSysOrg",curSysOrg).addObject("curUser",curUser);
				}else{
					mv = new ModelAndView("/oa/console/selectinterface.jsp").addObject("menuList", menuArr).addObject("msgCount", messCount)
							.addObject("curSysOrg",curSysOrg).addObject("curUser",curUser).addObject("roleNames",roleNames);
//					mv = getView("console", "newMain").addObject("menuList", menuArr.toString()).addObject("msgCount", messCount)
//							.addObject("curSysOrg",curSysOrg).addObject("curUser",curUser);
				}
				
			}else {
				if(skinStyle.equals("default")||skinStyle.equals("green")||skinStyle.equals("gray")){
					skinStyle = "oldDefault";
				}
				mv = getView("console", "main").addObject("skinStyle", skinStyle)
						.addObject("positions", positions).addObject("sysOrgs", sysOrgs)
						.addObject("curSysOrg",curSysOrg).addObject("curPosition", curPosition)
						.addObject("curUser",curUser);
						
			}
		}else{
			// 8部定制
//			mv = new ModelAndView("/oa/console/dp_main.jsp").addObject("menuList", menuArr.toString())
//					.addObject("msgCount", messCount).addObject("curSysOrg",curSysOrg).addObject("curUser",curUser)
//					.addObject("flag",flag);
			// 系统默认
			mv = getView("console", layout).addObject("menuList", menuArr).addObject("msgCount", messCount)
					.addObject("curSysOrg",curSysOrg).addObject("curUser",curUser);
		}
		
		return mv.addObject("userId", curUserId).addObject("currentSystem", currentSystem)
				 .addObject("isWarning",isWarning).addObject("warningMsg",warningMsg).addObject("UserName",curUser.getFullname());

	}
	
	/**
	 * 用户菜单
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getUserResMenu"})
	@ResponseBody
	public List<Resources> getUserResMenu(HttpServletRequest request, HttpServletResponse response)	throws Exception
	{
		//获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		List<Resources> resourcesList = this.resourcesService.getSysMenu(curUser);
		return resourcesList;
	}

	/**
	 * center页面,包含echarts显示
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @author yangBo 2016-7-18
	 */
	@SuppressWarnings("deprecation")
	@RequestMapping( { "home" })
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		ModelAndView mv = null;
		int type = SysConfConstant.SHOW_TYPE;
		if(type==1){
			mv = getView("console", "newhome");
		}else {
			String html = this.sysIndexMylayoutService.obtainMyIndexData(userId);
			mv = getView("console", "home").addObject("html", html);
		}
		return mv;
	}

	/**
	 * 验证expression中的脚本
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("getValidateResult")
	@ResponseBody
	public Object getValidateResult(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String script = RequestUtil.getString(request, "script");
		GroovyScriptEngine engine = (GroovyScriptEngine) AppUtil
				.getBean(GroovyScriptEngine.class);

		Map<String, Object> reObj = new HashMap<String, Object>();
		try {
			Object result = engine.executeObject(script, null);
			reObj.put("hasError", false);
			reObj.put("errorMsg", "");

			if (result != null) {
				reObj.put("result", result);
				reObj.put("resultType", result.getClass().getName());
			}
		} catch (Exception ex) {
			reObj.put("hasError", true);
			reObj.put("errorMsg", ex.getMessage());
		}
		return reObj;
	}

	/**
	 * 提供给菜单树节点数据
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getSysRolResTreeData" })
	@ResponseBody
	public List<Resources> getSysRolResTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		List<Resources> resourcesList = this.resourcesService.getSysMenu(curUser);
		return resourcesList;
	}
	/**
	 * 获取父id下的树
	 * @param request
	 * @author YangBo 
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getChildsByResId" })
	@ResponseBody
	public List<Resources> getChildsByResId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long resId=RequestUtil.getLong(request, "resId");
		Resources root = this.resourcesService.getById(resId);
		List<Resources> resourcesList = this.resourcesService.getByParentId(resId);
		resourcesList.add(root);
		ResourcesService.addIconCtxPath(resourcesList, request.getContextPath());
		return resourcesList;
	}
	
	/**
	 * 获获取别名为alias下的树
	 * @author YangBo
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "getTreeByAlias" })
	@ResponseBody
	public List<Resources> getTreeByAlias(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String alias=RequestUtil.getString(request, "alias");
		Resources root = this.resourcesService.getByAlias(alias);
		List<Resources> resourcesList = this.resourcesService.getByParentId(root.getResId());
		resourcesList.add(root);
		ResourcesService.addIconCtxPath(resourcesList, request.getContextPath());
		return resourcesList;
	}
	
	/**
	 * 由url获得节点数据
	 * @author YangBo 2016-7-20
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping( { "getResourceNode" })
	@ResponseBody
	public Resources getResourceNode(HttpServletRequest request,
			HttpServletResponse response) {
		Resources resource = null;
		try {
			String columnUrl = RequestUtil.getString(request, "columnUrl");
			resource = this.resourcesService.getByUrl(columnUrl);
		} catch (Exception e) {
			return null;
		}
		return resource;
	}
	/**
	 * 转换当前岗位
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping( { "switchCurrentOrg" })
	public void switchCurrentPos(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		Long posId = Long.valueOf(RequestUtil.getLong(request, "posId"));
		UserContextUtil.setCurrentPos(posId);
		response.sendRedirect(request.getContextPath()
				+ "/oa/console/main.do");
	}
	/**
	 * 语言更换
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping( { "switchSysLanguage" })
	public void switchSysLanguage(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String language = RequestUtil.getString(request, "language");
		String[] lan = language.split("_");
		Locale local = new Locale(lan[0], lan[1]);
		UserContextUtil.setLocale(local);
		SessionLocaleResolver sessionLocaleResolver = (SessionLocaleResolver) AppUtil
				.getBean(SessionLocaleResolver.class);
		sessionLocaleResolver.setLocale(request, response, local);
		response.sendRedirect(request.getContextPath()
				+ "/oa/console/main.do");
	}
	
	/**
	 * extJs路径跳转
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("seaJSShow")
	public ModelAndView seaJSShow(HttpServletRequest request,
			HttpServletResponse response) {
		// ext路径跳转临时处理。
		ModelAndView mv = new ModelAndView(
				"../../js/system/SeaJSShow_Panel.jsp");
		return mv;
	}
	
	/**
	 * 登录页下载插件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("downloadPlugin")
	public void downloadPlugin(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		String fileName =  RequestUtil.getString(request, "fileName");
		try {
			response.reset();
			response.setContentType("application/x-msdownload");
			response.addHeader("Content-Disposition", "attachment;filename="+ fileName);
			//绝对路径
			String path = request.getSession().getServletContext().getRealPath("/"+"attachFile"+ "/"+fileName);
			FileInputStream in = new FileInputStream(path);
			OutputStream out = response.getOutputStream();
			byte[] content = new byte[in.available()];
			int len = -1;
			while ((len = in.read(content)) != -1) {
				out.write(content, 0, len);
			}
			out.flush();
			out.close();
			in.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
