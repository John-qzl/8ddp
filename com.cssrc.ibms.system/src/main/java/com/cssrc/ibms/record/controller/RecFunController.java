package com.cssrc.ibms.record.controller;

import java.io.File;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.record.model.RecFun;
import com.cssrc.ibms.record.service.RecFunService;

@Controller
@RequestMapping("/oa/system/recFun/")
@Action(ownermodel=SysAuditModelType.RECTYPE_PROJECT_MANAGEMENT)
public class RecFunController extends BaseController {
	@Resource
	private RecFunService recFunService;
	@Resource
	private IDataTemplateService dataTemplateService;
	
	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/record/"))
			viewName = viewName.replace("/oa/system/", "/oa/system/record/fun/");
		mv.setViewName(viewName);
		return mv;
	}
	@RequestMapping("tree")
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long typeId = RequestUtil.getLong(request, "typeId");
		return getAutoView().addObject("typeId",typeId);
	}
	/**
	 * 提供给菜单树节点数据
	 */
	@RequestMapping( { "getRecRolResTreeData" })
	@ResponseBody
	public List<RecFun> getRecRolResTreeData(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String typeAlias = RequestUtil.getString(request, "typeAlias", "");
		Long pk = RequestUtil.getLong(request, "__pk__");
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		
		List<RecFun> funList = recFunService.getRecMenu(curUser, typeAlias,pk);
		RecFun parent = new RecFun();
		parent.setFunId(Long.valueOf(0L));
		parent.setTypeId(-1L);
		parent.setParentId(Long.valueOf(-1L));
		parent.setSn(Integer.valueOf(0));
		parent.setFunName("表单详细");

		parent.setIsDisplayInMenu(RecFun.IS_DISPLAY_IN_MENU_Y);
		parent.setIsFolder(RecFun.IS_FOLDER_Y);
		parent.setIsOpen(RecFun.IS_OPEN_Y);
		funList.add(parent);
		recFunService.addIconCtxPath(funList, request.getContextPath());
		return funList;
	}
	/**
	 *  查找该角色选中的功能点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getRecRolFunTreeChecked"})
	@ResponseBody
	public List<RecFun> getRecRolFunTreeChecked(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long roleId = RequestUtil.getLong(request, "roleId", 0L);
		Long typeId = RequestUtil.getLong(request, "typeId", 0L);
		List funList = this.recFunService.getByRecRolResChecked(typeId,roleId);

		RecFun parent = this.recFunService.getParentFunctionsByParentId(0L, typeId);
		funList.add(parent);

		recFunService.addIconCtxPath(funList, request.getContextPath());
		return funList;
	}
	/**
	 * 查找该功能点对应的按钮信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getButtonTreeChecked"})
	@ResponseBody
	public String getButtonTreeChecked(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long roleId = RequestUtil.getLong(request, "roleId", 0L);
		Long funId = RequestUtil.getLong(request, "funId", 0L);
		String buttons = this.recFunService.getButtonTreeChecked(roleId,funId);		
		return buttons;
	}
	/**
	 *  查找该角色选中的功能点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getRecRolSonFunTreeChecked"})
	@ResponseBody
	public List<RecFun> getRecRolSonFunTreeChecked(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long roleSonId = RequestUtil.getLong(request, "roleSonId", 0L);
		Long typeId = RequestUtil.getLong(request, "typeId", 0L);
		List funList = this.recFunService.getByRecRolSonResChecked(typeId,roleSonId);

		RecFun parent = this.recFunService.getParentFunctionsByParentId(0L, typeId);
		funList.add(parent);

		recFunService.addIconCtxPath(funList, request.getContextPath());
		return funList;
	}
	/**
	 * 查找该功能点对应的按钮信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getButtonSonTreeChecked"})
	@ResponseBody
	public String getButtonSonTreeChecked(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long roleSonId = RequestUtil.getLong(request, "roleSonId", 0L);
		Long funId = RequestUtil.getLong(request, "funId", 0L);
		String buttons = this.recFunService.getButtonSonTreeChecked(roleSonId,funId);		
		return buttons;
	}
	/**
	 * 根据功能点类别获得功能点树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getSystemTreeData"})
	@ResponseBody
	public List<RecFun> getSystemTreeData(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		Long typeId = RequestUtil.getLong(request, "typeId");
		List<RecFun> list = this.recFunService.getByTypeId(typeId);
		RecFun parent = this.recFunService.getParentFunctionsByParentId(0L,typeId);
		list.add(parent);

		RecFunService.addIconCtxPath(list, request.getContextPath());
		return list;
	}
	
	@RequestMapping({"get"})
	@Action(description="查看表单功能点明细", detail="查看表单功能点明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long id = RequestUtil.getLong(request, "funId");
		RecFun function = (RecFun)this.recFunService.getById(Long.valueOf(id));
		if (function != null) {
			function.setIcon(request.getContextPath() + function.getIcon());
		}
		return getAutoView().addObject("recFun", function);
	}
	
	@RequestMapping({"del"})
	@Action(description="删除子系统资源", execOrder=ActionExecOrder.BEFORE, detail="删除子系统资源：<#list funId?split(\",\") as item><#assign entity=recFunService.getById(Long.valueOf(item))/>${entity.resName}【${entity.alias}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		//设置操作结果，默认为操作失败
		Short result = 0;
		ResultMessage resultMessage = new ResultMessage(1, "删除资源成功!");
		Long funId = Long.valueOf(RequestUtil.getLong(request, "funId"));
		RecFun res = (RecFun)this.recFunService.getById(funId);
		try {
			this.recFunService.delById(funId);
			result = 1;
		}
		catch (Exception ex)
		{
			resultMessage = new ResultMessage(0, "删除资源失败!");
		}
		response.getWriter().print(resultMessage);
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		ModelAndView mv = getAutoView();
		long parentId = RequestUtil.getLong(request, "parentId", 0L);
		long funId = RequestUtil.getLong(request, "funId", 0L);
		long typeId = RequestUtil.getLong(request, "typeId", 0L);

		RecFun function = null;
		boolean isadd = true;
		if (funId != 0L) {
			function = (RecFun)this.recFunService.getById(Long.valueOf(funId));
			isadd = false;
		}
		else {
			RecFun parent = this.recFunService.getParentFunctionsByParentId(parentId,typeId);
			function = new RecFun();

			function.setIsFolder(RecFun.IS_FOLDER_N);
			function.setIsDisplayInMenu(RecFun.IS_DISPLAY_IN_MENU_Y);
			function.setIsOpen(RecFun.IS_OPEN_Y);
			function.setIsNewOpen(RecFun.IS_NEWOPEN_N);

			function.setParentId(parent.getFunId());
			function.setSn(Integer.valueOf(1));
		}
		function.setIcon(request.getContextPath() + function.getIcon());
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
		return mv.addObject("recFun", function).addObject("typeId", typeId);
	}
	//保存添加的节点或者编辑的节点
	@RequestMapping({"save"})
	@Action(description="添加或编辑子系统资源", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>修改</#if>子系统资源：${SysAuditLinkService.getRecFunLink(Long.valueOf(funId))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { RecFun.class }, pkName = "funId")
	public void save(HttpServletRequest request, HttpServletResponse response,RecFun rf, BindingResult bindResult)
		throws Exception {
			RecFun function  = getFormObject(request);
			PrintWriter out = response.getWriter();
			//设置操作结果，默认为操作失败
			Short resultStatus = 0;
			String resultMsg = null;
			ResultMessage resultMessage = validForm("function", function,
					bindResult, request);
			if (resultMessage.getResult() == 0) {
				writeResultMessage(response.getWriter(), resultMessage);
			}
			boolean isadd = function.getFunId() == null;
			String id = null;
			String icon = function.getIcon();
			//去除图片路径项目头文字
			icon = icon.replace(request.getContextPath(), "");
			function.setIcon(icon);

			String defaultUrl = function.getDefaultUrl();
	 		if (defaultUrl != null) {
				defaultUrl = defaultUrl.trim();
				if (defaultUrl.equals("")) {
					defaultUrl = null;
				}
				function.setDefaultUrl(defaultUrl);
			}

			if (function.getFunId() == null) {
				Integer rtn = this.recFunService.isAliasExists(function);
				if (rtn.intValue() > 0) {
					resultMsg = "别名在系统中已存在!";
					id = recFunService.getByAlias(function.getAlias()).getFunId().toString();
					writeResultMessage(response.getWriter(), resultMsg, 0);
				}else{
					try {
						long funId = this.recFunService.addFunction(function).longValue();
						resultStatus = 1;
						//注意返回的json格式，funId用于定位该新节点
						String result = "{result:1,funId:" + funId + ",operate:'add'}";
						out.print(result);
					} catch (Exception e) {
						e.printStackTrace();
						writeResultMessage(response.getWriter(), "添加资源失败!", 0);
					}
				}
			} else {
				Integer rtn = this.recFunService.isAliasExistsForUpd(function);
				if (rtn.intValue() > 0) {
					resultMsg = "别名在系统中已存在!";
					id = recFunService.getByAlias(function.getAlias()).getFunId().toString();
					writeResultMessage(response.getWriter(), resultMsg, 0);
				}else{
					try {
						this.recFunService.updFunction(function);
						resultStatus = 1;
						String result = "{result:1,operate:'edit'}";
						out.print(result);
					} catch (Exception e) {
						String str = MessageUtil.getMessage();
						if (StringUtil.isNotEmpty(str)) {
							resultMessage = new ResultMessage(0, "更新资源失败:" + str);
							response.getWriter().print(resultMessage);
						} else {
							String message = ExceptionUtil.getExceptionMessage(e);
							resultMessage = new ResultMessage(0, message);
							response.getWriter().print(resultMessage);
						}
					}
				}
			}
			try {
				LogThreadLocalHolder.putParamerter("resultMsg","别名在系统中已存在!");
				LogThreadLocalHolder.putParamerter("isAdd",Boolean.valueOf(isadd));
				LogThreadLocalHolder.putParamerter("funId", id);
				LogThreadLocalHolder.setResult(resultStatus);
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error(e.getMessage());
			}
		}
		/**
		 * 功能点顺序列表
		 * @param request
		 * @param response
		 * @return
		 * @throws Exception
		 */
		@RequestMapping({"sortList"})
		@Action(description="资源树排序列表", detail="资源树排序列表", exectype = SysAuditExecType.SELECT_TYPE)
		public ModelAndView sortList(HttpServletRequest request, HttpServletResponse response)
		throws Exception
		{
			Long funId = Long.valueOf(RequestUtil.getLong(request, "funId", -1L));
			List<RecFun> list = this.recFunService.getByParentId(funId);
			return getAutoView().addObject("RecFunList", list);
		}
		/**
		 * 更新序号
		 * @param request
		 * @param response
		 * @throws Exception
		 */
		@RequestMapping({"sort"})
		@Action(description="资源树排序", detail="资源树排序", exectype = SysAuditExecType.UPDATE_TYPE)
		public void sort(HttpServletRequest request, HttpServletResponse response)
		throws Exception
		{
			ResultMessage resultObj = null;
			PrintWriter out = response.getWriter();
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "funIds");
			if (BeanUtils.isNotEmpty(lAryId)) {
				for (int i = 0; i < lAryId.length; i++) {
					Long funId = lAryId[i];
					long sn = i + 1;
					this.recFunService.updSn(funId, sn);
				}
			}

			resultObj = new ResultMessage(1, "排序分类完成");
			out.print(resultObj);
		}
	/**
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/icons")
	public ModelAndView icons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		int iconType = RequestUtil.getInt(request, "iconType");
		String path=RequestUtil.getString(request, "path");
		String iconTypeStr="";
		if(StringUtil.isNotEmpty(path)){
			iconTypeStr=path;
		}else{
			iconTypeStr=RecFun.ICON_PATH;
			if (iconType == 1)
				iconTypeStr = RecFun.LOGO_PATH;
		}
		String iconPath = request.getSession().getServletContext().getRealPath(iconTypeStr);
		String[] iconList= getIconList(iconPath);
		mv.addObject("iconList", iconList);
		mv.addObject("iconPath",iconTypeStr);
		return mv;
	}
	/**
	 * 图标文件列表。
	 * @return
	 */
	private String[] getIconList(String iconPath) {
		File iconFolder = new File(iconPath);
		String[] fileNameList = iconFolder.list(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				String ext = name.substring(name.lastIndexOf(".") + 1).toUpperCase();
				if (RecFun.ICON_TYPE.contains(ext)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return fileNameList;
	}
	/**
	 * 创建资源图标文件夹
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("createFolder")
	public void createFolder(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String iconPath=RequestUtil.getString(request, "path");
		String folderName=RequestUtil.getString(request, "folderName");
		String path = request.getSession().getServletContext().getRealPath(iconPath);
		try {
			FileUtil.createFolder(path, folderName);
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("controller.function.file.create.success")));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.function.file.create.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	/**
	 * 删除图标文件夹
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("delFile")
	public void delFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String folderPath=RequestUtil.getString(request, "path");
		String path = request.getSession().getServletContext().getRealPath(folderPath);
		try {
			FileUtil.deleteDir(new File(path));
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.function.folder"))));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("record.delete.fail",getText("controller.function.folder"))+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	/**
	 * 从样式文件中获取扩展功能图标
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("getExtendIcons")
	public void getExtendIcons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage resultObj = null;
		PrintWriter out = response.getWriter();
		try {
			String fileName = FileUtil.getRootPath() + RecFun.EXTEND_CSS_PATH;
			String cssStr = FileUtil.readFile(fileName);
			int start = cssStr.indexOf("/*--extend start--*/");
			int end = cssStr.indexOf("/*--extend end--*/");
			String subStr = cssStr.substring(start, end);
			String icons = "";
			Pattern regex = Pattern.compile("a\\.extend\\.([\\w]+)\\s*\\{", 66);
			Matcher regexMatcher = regex.matcher(subStr);
			while (regexMatcher.find()) {
				icons += regexMatcher.group(1) + ",";
			}
			resultObj = new ResultMessage(ResultMessage.Success, icons);
		} catch (Exception ex) {
			resultObj = new ResultMessage(ResultMessage.Fail, ex.getMessage());
		}
		out.print(resultObj);
	}
	private String getNewIconsStr() {
		String iconPath = FileUtil.getRootPath() + RecFun.ICON_EXTEND.replace("/", File.separator);
		String[] icons = getIconList(iconPath);
		String returnStr = "/*--extend start--*/";
		for (String str : icons) {
			returnStr += "\n";
			returnStr += "a.extend." + str.substring(0, str.lastIndexOf("."));
			returnStr += "{\n";
			returnStr += "    background:url(../images/extendIcon/" + str + ") 0px -1px no-repeat;\n}\n";
		}
		return returnStr;
	}
	/**
	 * 刷新扩展功能样式文件
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("refreshExtendCss")
	public void refreshExtendCss(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		try {
			String fileName = FileUtil.getRootPath() + RecFun.EXTEND_CSS_PATH.replace("/", File.separator);;
			String cssStr = FileUtil.readFile(fileName);
			String newSubStr = getNewIconsStr();
			int length = cssStr.length();
			int start = cssStr.indexOf("/*--extend start--*/");
			int end = cssStr.indexOf("/*--extend end--*/");
			String bottomStr = cssStr.substring(end, length);
			String headStr = cssStr.substring(0, start);
			String newStr = headStr + newSubStr + bottomStr;
			FileUtil.writeFile(fileName, newStr);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.function.refresh.success"));
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.function.refresh.fail")+":" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}
	@RequestMapping("getDataTemplateButton")
	public void getDataTemplateButton(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		try{
			Long displayId = RequestUtil.getLong(request, "displayId");
			IDataTemplate dataTemplate = dataTemplateService.getById(displayId);
			if(recFunService.hasManage(dataTemplate.getManageField())){
				resultMessage = new ResultMessage(ResultMessage.Success,"");
				String buttonJson = recFunService.analyseManageField(dataTemplate,resultMessage);
			}else{
				resultMessage = new ResultMessage(ResultMessage.Fail,"业务数据模板没有定义按钮信息！");
			}
		}catch (Exception e){
			resultMessage = new ResultMessage(ResultMessage.Fail,"从业务数据模板，获取按钮信息失败！");
		}
		out.print(resultMessage);
	}
	private RecFun getFormObject(HttpServletRequest request){
        String json = RequestUtil.getString(request, "json",false);
        if (StringUtil.isEmpty(json))
            return null;
        JSONObject obj = JSONObject.fromObject(json);
        RecFun function = (RecFun) JSONObject.toBean(obj, RecFun.class);
        return function;
	}
}
