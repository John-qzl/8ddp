package com.cssrc.ibms.system.controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
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
import com.cssrc.ibms.system.model.ResourceFolder;
import com.cssrc.ibms.system.model.Resources;
import com.cssrc.ibms.system.model.ResourcesUrl;
import com.cssrc.ibms.system.service.ResourcesService;
import com.cssrc.ibms.system.service.ResourcesUrlService;
/**
 * 对象功能:子系统资源 控制器类  
 * 开发人员:zhulongchao  
 */
@Controller
@RequestMapping("/oa/system/resources/")
@Action(ownermodel=SysAuditModelType.RESOURCES_MANAGEMENT)
public class ResourcesController extends BaseController {
	@Resource
	private ResourcesUrlService resourcesUrlService;

	@Resource
	private ResourcesService resourcesService;
	/**
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/icons")
	public ModelAndView icons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconfont.css");
		List<Map<String, Object>> iconList = resourcesService.getIcons(iconPath, "iconfont");
		
		ModelAndView mv = getAutoView();
		mv.addObject("iconList",iconList);
		return mv;
	}
	
	@RequestMapping("/icomoon")
	public ModelAndView icomoon(HttpServletRequest request, HttpServletResponse response) 
			throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconfont.css");
		List<Map<String, Object>> iconList = resourcesService.getIcons(iconPath, "icomoon");
		
		ModelAndView mv = getAutoView();
		mv.addObject("iconList",iconList);
		return mv;
	}
	
	@RequestMapping("/imgIcons")
	public ModelAndView imgIcons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconImg.css");
		List<Map<String, Object>> iconImgList = resourcesService.getIconImgClassNameAndContent(iconPath);
		
		ModelAndView mv = getAutoView();
		mv.addObject("iconImgList",iconImgList);
		return mv;
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
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("controller.resources.file.create.success")));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.resources.file.create.fail")+":" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail, message);
				response.getWriter().print(resultMessage);
			}
		}
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
				if (Resources.ICON_TYPE.contains(ext)) {
					return true;
				} else {
					return false;
				}
			}
		});
		return fileNameList;
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
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("record.deleted",getText("controller.resources.folder"))));
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Fail,getText("record.delete.fail",getText("controller.resources.folder"))+":" + str);
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
	@Action(description = "从样式文件中获取扩展功能图标",detail = "从样式文件中获取扩展功能图标", exectype = SysAuditExecType.SELECT_TYPE)
	public void getExtendIcons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ResultMessage resultObj = null;
		PrintWriter out = response.getWriter();
		try {
			String fileName = FileUtil.getRootPath() + Resources.EXTEND_CSS_PATH;
			String cssStr = FileUtil.readFile(fileName);
			int start = cssStr.indexOf("/*--extend start--*/");
			int end = cssStr.indexOf("/*--extend end--*/");
			String subStr = cssStr.substring(start, end);
			String icons = "";
			Pattern regex = Pattern.compile("a\\.extend\\.([\\w]+)\\s*\\:before\\{", 66);
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
		String iconPath = FileUtil.getRootPath() + Resources.ICON_EXTEND.replace("/", File.separator);
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
	@Action(description = "刷新扩展功能样式文件",detail = "刷新扩展功能样式文件", exectype = SysAuditExecType.SELECT_TYPE)
	public void refreshExtendCss(HttpServletRequest request, HttpServletResponse response) throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = null;
		try {
			String fileName = FileUtil.getRootPath() + Resources.EXTEND_CSS_PATH.replace("/", File.separator);;
			String cssStr = FileUtil.readFile(fileName);
			String newSubStr = getNewIconsStr();
			int length = cssStr.length();
			int start = cssStr.indexOf("/*--extend start--*/");
			int end = cssStr.indexOf("/*--extend end--*/");
			String bottomStr = cssStr.substring(end, length);
			String headStr = cssStr.substring(0, start);
			String newStr = headStr + newSubStr + bottomStr;
			FileUtil.writeFile(fileName, newStr);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.resources.refresh.success"));
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.resources.refresh.fail")+":" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(ResultMessage.Fail, message);
			}
		}
		out.print(resultMessage);
	}



	@RequestMapping({"list"})
	@Action(description="查看子系统资源分页列表", detail="查看子系统资源分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List<Resources> list = this.resourcesService.getAll(new QueryFilter(request, "resourcesItem"));
		if ((list != null) && (list.size() > 0)) {
			for (Resources res : list) {
				res.setIcon(request.getContextPath() + res.getIcon());
			}
		}
		ModelAndView mv = getAutoView().addObject("resourcesList", list);
		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除子系统资源", execOrder=ActionExecOrder.BEFORE, detail="删除子系统资源<#list resId?split(\",\") as item><#assign entity=resourcesService.getById(Long.valueOf(item))/> ${entity.resName}【${entity.alias}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		ResultMessage resultMessage = new ResultMessage(1, "删除资源成功!");
		Long resId = Long.valueOf(RequestUtil.getLong(request, "resId"));
		Resources res = (Resources)this.resourcesService.getById(resId);
		try {
			this.resourcesService.delById(resId);
		}
		catch (Exception ex)
		{
			resultMessage = new ResultMessage(0, "删除资源失败!");
		}
		response.getWriter().print(resultMessage);
	}

	@RequestMapping({"edit"})
	@Action(description="添加或编辑子系统资源", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加子系统资源<#else>编辑子系统资源<#assign entity=resourcesService.getById(Long.valueOf(resId))/> ${entity.resName}【${entity.alias}】</#if>", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		ModelAndView mv = getAutoView();
		long parentId = RequestUtil.getLong(request, "parentId", 0L);
		long resId = RequestUtil.getLong(request, "resId", 0L);

		Resources resources = null;
		boolean isadd = true;
		if (resId != 0L) {
			resources = (Resources)this.resourcesService.getById(Long.valueOf(resId));
			if ((resources != null) && (resources.getIsFolder() != null) && (resources.getIsFolder().equals(Resources.IS_FOLDER_N))) {
				List<ResourcesUrl> resourcesUrlList = this.resourcesUrlService.getByResId(resources.getResId().longValue());
				mv.addObject("resourcesUrlList", resourcesUrlList);
			}
			isadd = false;
		}else {
			Resources parent = this.resourcesService.getParentResourcesByParentId( parentId);
			resources = new Resources();

			resources.setIsFolder(Resources.IS_FOLDER_N);
			resources.setIsDisplayInMenu(Resources.IS_DISPLAY_IN_MENU_Y);
			resources.setIsOpen(Resources.IS_OPEN_Y);
			resources.setIsNewOpen(Resources.IS_NEWOPEN_N);

			resources.setParentId(parent.getResId());
			resources.setSn(Integer.valueOf(1));
		}
		resources.setIcon(resources.getIcon());

		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		return mv.addObject("resources", resources);
	}

	@RequestMapping({"get"})
	@Action(description="查看系统资源明细", detail="查看系统资源明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long id = RequestUtil.getLong(request, "resId");
		Resources resources = (Resources)this.resourcesService.getById(Long.valueOf(id));
		if (resources != null) {
			resources.setIcon(resources.getIcon());
		}
		List<ResourcesUrl> resourcesUrlList = this.resourcesUrlService.getByResId(id);
		return getAutoView().addObject("resources", resources).addObject("resourcesUrlList", resourcesUrlList);
	}
	/**
	 * 获得功能点树
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getSystemTreeData"})
	@ResponseBody
	public List<Resources> getSystemTreeData(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List<Resources> list = this.resourcesService.getAll();
		Resources parent = this.resourcesService.getParentResourcesByParentId(0L);
		list.add(parent);

		//ResourcesService.addIconCtxPath(list, request.getContextPath());
		return list;
	}
	
	/**
	 *  查找该角色选中的功能点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getSysRolResTreeChecked"})
	@ResponseBody
	public List<Resources> getSysRolResTreeChecked(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long roleId = RequestUtil.getLong(request, "roleId", 0L);

		List resourcesList = this.resourcesService.getBySysRolResChecked(Long.valueOf(roleId));

		Resources parent = this.resourcesService.getParentResourcesByParentId(0L);
		resourcesList.add(parent);

		ResourcesService.addIconCtxPath(resourcesList, request.getContextPath());
		return resourcesList;
	}
	
	@RequestMapping({"move"})
	@Action(description="转移子系统资源", detail="<#assign sourceEntity=globalTypeService.getById(Long.valueOf(sourceId))/><#assign targetEntity=globalTypeService.getById(Long.valueOf(targetId))/>分类【${targetEntity.typeName}】转移到分类【${sourceEntity.typeName}】之内", exectype = SysAuditExecType.UPDATE_TYPE)
	public void move(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		ResultMessage resultObj = null;
		PrintWriter out = response.getWriter();
		long targetId = RequestUtil.getLong(request, "targetId", 0L);
		long sourceId = RequestUtil.getLong(request, "sourceId");
		this.resourcesService.move(Long.valueOf(sourceId), Long.valueOf(targetId));
		resultObj = new ResultMessage(1, "转移子系统资源完成");
		out.print(resultObj);
	} 
	
	/**
	 * 自定义图标文件夹
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getFolderData"})
	@ResponseBody
	public List<ResourceFolder> getFolderData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List list = new ArrayList();
		String rootPath = "/styles/default/images/resicon";
		ResourceFolder folder = new ResourceFolder();
		folder.setFolderId(Long.valueOf(0L));
		folder.setFolderName("全部");
		folder.setFolderPath(rootPath);
		list.add(folder);
		getFolderList(request, folder, list);
		return list;
	}
	
	
	public void getFolderList(HttpServletRequest request, ResourceFolder resFolder, List<ResourceFolder> list)
	{
		String path = resFolder.getFolderPath();
		String realPath = request.getSession().getServletContext().getRealPath(path);
		File file = new File(realPath);
		File[] files = file.listFiles();
		if ((files != null) && (files.length != 0))
			for (File f : files)
				if (f.isDirectory()) {
					ResourceFolder folder = new ResourceFolder();
					folder.setFolderId(Long.valueOf(UniqueIdUtil.genId()));
					folder.setFolderName(f.getName());
					folder.setFolderPath(path + "/" + f.getName());
					folder.setParentId(resFolder.getFolderId());
					list.add(folder);
					getFolderList(request, folder, list);
				}
	}
	
	/**
	 * 上传图标资源
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"uploadIcon"})
	public void uploadIcon(MultipartHttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String iconPath = RequestUtil.getString(request, "path");
		Map files = request.getFileMap();
		Iterator it = files.values().iterator();
		if (it.hasNext()) {
			MultipartFile f = (MultipartFile)it.next();
			String fileName = f.getOriginalFilename();
			String extName = FileUtil.getFileExt(fileName);
			if ((!extName.equals("gif")) && (!extName.equals("png")) && (!extName.equals("jpg"))) {
				writeResultMessage(response.getWriter(), new ResultMessage(0, "上传的文件格式必须为图片类型"));
				return;
			}
			String filePath = request.getSession().getServletContext().getRealPath(iconPath + "/" + fileName);
			File file = new File(filePath);
			file.createNewFile();
			FileUtil.writeByte(filePath, f.getBytes());
			writeResultMessage(response.getWriter(), new ResultMessage(1, "上传的文件成功！"));
		}
	}
	
	@RequestMapping({"exportXml"})
	@Action(description="导出资源", execOrder=ActionExecOrder.AFTER, detail="导出资源 ${SysAuditLinkService.getResourcesLink(Long.valueOf(resId))}", exectype = SysAuditExecType.EXPORT_TYPE)
	public void exportXml(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		long resId = RequestUtil.getLong(request, "resId");
		//设置操作结果，默认为操作失败
		Short result = 0;
		if (resId != 0L) {
			Map map = new HashMap();
			map.put("resources", Boolean.valueOf(true));
			try {
				Resources resources = (Resources)this.resourcesService.getById(Long.valueOf(resId));
				String fileName = resources.getAlias();
				String strXml = this.resourcesService.exportXml(resId, map);
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, "UTF-8") + ".xml");
				response.getWriter().write(strXml);
				response.getWriter().flush();
				response.getWriter().close();
				result = 1;
				LogThreadLocalHolder.putParamerter("resources", resources);
			} catch (Exception e) {
				e.printStackTrace();
				this.logger.error(e.getMessage());
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping({"importXml"})
	@Action(description="导入资源", execOrder=ActionExecOrder.AFTER, detail="导入资源  ${SysAuditLinkService.getResourcesLink(Long.valueOf(resId))}", exectype = SysAuditExecType.IMPORT_TYPE)
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	throws IOException
	{
		long resId = RequestUtil.getLong(request, "resId");
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage resultMessage = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		try
		{
			this.resourcesService.importXml(fileLoad.getInputStream(), resId);
			result = 1;
			resultMessage = new ResultMessage(1, "导入成功!");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "导入失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
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
		Long resId = Long.valueOf(RequestUtil.getLong(request, "resId", -1L));
		List<Resources> list = this.resourcesService.getByParentId(resId);
		return getAutoView().addObject("ResourcesList", list);
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
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "resIds");
		if (BeanUtils.isNotEmpty(lAryId)) {
			for (int i = 0; i < lAryId.length; i++) {
				Long resId = lAryId[i];
				long sn = i + 1;
				this.resourcesService.updSn(resId, sn);
			}
		}

		resultObj = new ResultMessage(1, "排序分类完成");
		out.print(resultObj);
	}

	@RequestMapping({"tab_{alias}"})
	public ModelAndView tab(HttpServletRequest request, HttpServletResponse response, @PathVariable("alias") String alias)
	throws Exception
	{
		Resources res = this.resourcesService.getByAlias(alias);
		ISysUser user = (ISysUser)UserContextUtil.getCurrentUser();
		String rolealias = null;
		if (!UserContextUtil.isSuperAdmin()) {
			rolealias = user.getRoles();
		}

		List<Resources> resList = this.resourcesService.getNormMenuByAllRoleParentId(res.getResId(), rolealias);

		Map resrouceMap = new HashMap();
		for (Resources resource : resList) {
			List<Resources> tmpList = this.resourcesService.getNormMenuByAllRoleParentId(resource.getResId(), rolealias);
			for (Resources tmp : tmpList) {
				String url = tmp.getDefaultUrl();
				if ((StringUtil.isNotEmpty(url)) && (url.indexOf("http") == -1)) {
					tmp.setDefaultUrl(request.getContextPath() + url);
				}
			}
			resrouceMap.put(resource.getAlias(), tmpList);
		}

		ModelAndView mv = new ModelAndView();
		mv.setViewName("/oa/system/resourcesTab.jsp");
		mv.addObject("resList", resList);
		mv.addObject("resrouceMap", resrouceMap);
		return mv;
	}
	//保存添加的节点或者编辑的节点
	@RequestMapping({"save"})
	@Action(description="添加或更新子系统资源", execOrder=ActionExecOrder.AFTER, detail="<#if isAdd>添加<#else>更新</#if>子系统资源 ${SysAuditLinkService.getResourcesLink(alias)} ", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { Resources.class }, pkName = "resId")
	public void save(HttpServletRequest request, HttpServletResponse response, Resources resources, BindingResult bindResult)
	throws Exception
 {
		PrintWriter out = response.getWriter();
		ResultMessage resultMessage = validForm("resources", resources,
				bindResult, request);
		//设置操作结果，默认为操作失败
		Short resultStatus = 0;
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}

		boolean isadd = resources.getResId() == null;
		
		String icon = resources.getIcon();
		//去除图片路径项目头文字
		icon = icon.replace(request.getContextPath(), "");
		resources.setIcon(icon);

		String defaultUrl = resources.getDefaultUrl();
 		if (defaultUrl != null) {
			defaultUrl = defaultUrl.trim();
			if (defaultUrl.equals("")) {
				defaultUrl = null;
			}
			resources.setDefaultUrl(defaultUrl);
		}
		//获取自定义resourcesUrl
		String[] aryName = request.getParameterValues("name");
		String[] aryUrl = request.getParameterValues("url");

		if (resources.getResId() == null) {
			Integer rtn = this.resourcesService.isAliasExists(resources);
			if (rtn.intValue() > 0) {
				writeResultMessage(response.getWriter(), "别名在系统中已存在!", 0);
			}else{
				try {
					long resId = this.resourcesService.addRes(resources, aryName,
							aryUrl).longValue();
					resultStatus = 1;
					//注意返回的json格式，resId用于定位该新节点
					String result = "{result:1,resId:" + resId + ",operate:'add'}";
					out.print(result);
				} catch (Exception e) {
					writeResultMessage(response.getWriter(), "添加资源失败!", 0);
				}
			}
		} else {
			Integer rtn = this.resourcesService.isAliasExistsForUpd(resources);
			if (rtn.intValue() > 0) {
				writeResultMessage(response.getWriter(), "别名在系统中已存在!", 0);
			}else{
				try {
					this.resourcesService.updRes(resources, aryName, aryUrl);
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
			LogThreadLocalHolder.putParamerter("resultMsg", "别名在系统中已存在!");
			LogThreadLocalHolder.putParamerter("isAdd",Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("alias", resources.getAlias());
			LogThreadLocalHolder.setResult(resultStatus);
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}
	
	
	@RequestMapping("addResource")
	public ModelAndView addResource(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return mv;
	}
	
	@RequestMapping("tree")
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		return getAutoView();
	}
	
	
	public String getIconClassName(HttpServletRequest request) throws IOException{
		String str = new String();
		//获取主题
		String theme = PropertyUtil.getValueByName("SYS_THEME");
		String iconPath = request.getSession().getServletContext().getRealPath("/styles/"+theme+"/css/iconfont.css");
		try {
			BufferedReader br =new BufferedReader(new InputStreamReader(new FileInputStream(new File(iconPath)),"UTF-8"));
			
			String lineTxt = null;
			while((lineTxt = br.readLine()) != null){
				if(lineTxt.indexOf("before") > 0){
					str += lineTxt.substring(lineTxt.indexOf("-")+1, lineTxt.indexOf(":"));
					str +=",";
				}
			}
			br.close();
		} catch (UnsupportedEncodingException | FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return str;
	}
 
	 
}
