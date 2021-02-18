package com.cssrc.ibms.system.controller;
import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.activity.intf.IDefAuthorizeService;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.IProTransToService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.intf.ITaskExeService;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IProTransTo;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.activity.model.ITaskAmount;
import com.cssrc.ibms.api.activity.model.ITaskExe;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.FileDimension;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SysFileType;
import com.cssrc.ibms.system.model.SysTypeKey;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.SysFileTypeService;
import com.cssrc.ibms.system.service.SysTypeKeyService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping( { "/oa/system/globalType/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class GlobalTypeController extends BaseController {

	@Resource
	private GlobalTypeService globalTypeService;

	@Resource
	private SysTypeKeyService sysTypeKeyService;

	@Resource
	private ISysRoleService sysRoleService;

	@Resource
	private ISysOrgService sysOrgService;

	@Resource
	private IProcessRunService processRunService;

	@Resource
	private IBpmService bpmService;

	@Resource
	private ITaskExeService taskExeService;

	@Resource
	private IDefAuthorizeService defAuthorizeService;

	@Resource
	private IDefinitionService definitionService;

	@Resource
	private IProTransToService proTransToService;
	
	@Resource
	private SysFileTypeService sysFileTypeService;
	


	@RequestMapping( { "getPingyin" })
	@ResponseBody
	@Action(description = "获取中文字全部拼音", detail = "获取中文字全部拼音")
/*	public String getPingyin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String typeName = RequestUtil.getString(request, "typeName");
		if(!StringUtil.isEmpty(typeName)){
			String nodeKey = PinyinUtil.getPinyinToLowerCase(typeName);
			return nodeKey;
		}else{
			return "";
		}
		
	}*/
	//yangBo 通过Share.js调用getPingyin的方法都适用
	public Map<String, Object> getPingyin(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Map<String, Object> map = new HashMap<String, Object>();
		String input = RequestUtil.getString(request, "input");
		String output = PinyinUtil.getPinYinHeadCharFilter(input);
		map.put("output", output);
		return map;
	}

	@RequestMapping( { "getByParentId" })
	@ResponseBody
	public List<GlobalType> getByParentId(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long catId = Long.valueOf(RequestUtil.getLong(request, "catId", 0L));
//		SysTypeKey sysTypeKey =sysTypeKeyService.getById(catId);
		Long parentId = Long.valueOf(RequestUtil.getLong(request, "parentId",-1L));
//		if(sysTypeKey!=null){
//			List<GlobalType> list = this.globalTypeService.getByCatKey(sysTypeKey.getTypeKey(), true);
//			return list;
//		}else{
//			return null;
//		}
		List<GlobalType> list = this.globalTypeService.getByParentId(parentId.longValue() == -1L ? (parentId = catId): parentId);
		//将已逻辑删除的数据用*标识
		List<GlobalType> typeLists = list;
		for(GlobalType globalType:typeLists){
			String typeName = globalType.getTypeName();
			if(globalType.getGltype_delFlag()!=null&&globalType.getGltype_delFlag()==1){
				globalType.setTypeName(typeName+"*");
			}
		}
		if (catId.equals(parentId)) {
			SysTypeKey sysTypeKey = (SysTypeKey) this.sysTypeKeyService
					.getById(catId);
			GlobalType globalType = new GlobalType();
			globalType.setTypeName(sysTypeKey.getTypeName());
			globalType.setCatKey(sysTypeKey.getTypeKey());

			globalType.setTypeId(sysTypeKey.getTypeKeyId());
			globalType.setParentId(Long.valueOf(0L));
			globalType.setType(sysTypeKey.getType());
			if (list.size() == 0) {
				globalType.setIsParent("false");
			} else {
				globalType.setIsParent("true");
				globalType.setOpen("true");
			}
			globalType.setNodePath(sysTypeKey.getTypeKeyId() + ".");
			list.add(0, globalType);
		}
		
		return list;
	}

	@RequestMapping( { "tree" })
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<SysTypeKey> list = this.sysTypeKeyService.getAll();
		SysTypeKey sysTypeKey = (SysTypeKey) list.get(0);
		ModelAndView mv = getAutoView().addObject("typeList", list).addObject(
				"sysTypeKey", sysTypeKey);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "物理删除系统分类", execOrder = ActionExecOrder.BEFORE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResultMessage resultMessage = null;
		try {
			Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
			Long dataId = RequestUtil.getLong(request, "dataId",0L);
			if(dataId.longValue() > 0){
				sysFileTypeService.delFileType(typeId,dataId);
			}else {
				this.globalTypeService.delByTypeId(typeId);
			}
			resultMessage = new ResultMessage(1, "删除系统分类成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "删除系统分类失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}
	
	@RequestMapping( { "delLogic" })
	@Action(description = "逻辑删除系统分类", execOrder = ActionExecOrder.BEFORE)
	public void delLogic(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		ResultMessage resultMessage = null;
		Long currentUserId = UserContextUtil.getCurrentUser().getUserId();
		try {
			Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
			Long dataId = RequestUtil.getLong(request, "dataId",0L);
			if(dataId.longValue() > 0){
				sysFileTypeService.delFileType(typeId,dataId);
			}else {
				this.globalTypeService.updateStatus(typeId,currentUserId, (short) 1);
			}
			resultMessage = new ResultMessage(1, "逻辑删除系统分类成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "逻辑删除系统分类失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	/**
	 * 还原被逻辑删除的分类
	 * @author liubo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "restore" })
	@Action(description = "还原被逻辑删除的分类", execOrder = ActionExecOrder.AFTER, detail = "还原被逻辑删除的分类")
	public void restore(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		Long currentUserId = UserContextUtil.getCurrentUser().getUserId();
		try {
			Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
			
			this.globalTypeService.updateStatus(typeId,currentUserId, (short)0);
			message = new ResultMessage(1, "分类还原成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "分类还原失败" + e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
	
	@RequestMapping( { "exportXml" })
	@Action(description = "导出系统分类", execOrder = ActionExecOrder.AFTER, detail = "导出系统分类【${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(typeId))}】")
	public void exportXml(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		long typeId = RequestUtil.getLong(request, "typeId");
		String filename = "";
		if (typeId != 0L) {
			String strXml = this.globalTypeService.exportXml(typeId);
			GlobalType globalType = (GlobalType) this.globalTypeService
					.getById(Long.valueOf(typeId));
			if (globalType != null)
				filename = globalType.getTypeName();
			else {
				filename = ((SysTypeKey) this.sysTypeKeyService.getById(Long
						.valueOf(typeId))).getTypeName();
			}
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition", "attachment;filename="
					+ StringUtil.encodingString(filename, "GBK", "ISO-8859-1")
					+ ".xml");
			response.getWriter().write(strXml);
			response.getWriter().flush();
			response.getWriter().close();
		}
	}

	@RequestMapping( { "importXml" })
	@Action(description = "导入系统分类", execOrder = ActionExecOrder.AFTER, detail = "导入系统分类【${SysAuditLinkService.getGlobalTypeLink(Long.valueOf(typeId))}】<#else>失败</#if>")
	public void importXml(MultipartHttpServletRequest request,
			HttpServletResponse response) throws IOException {
		long typeId = RequestUtil.getLong(request, "typeId");
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage resultMessage = null;
		try {
			this.globalTypeService.importXml(fileLoad.getInputStream(), typeId);
			resultMessage = new ResultMessage(1, "导入成功!");
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(true));
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception ex) {
			ex.printStackTrace();
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(false));
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "导入系统分类失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@RequestMapping( { "get" })
	@Action(description = "查看系统分类表明细", detail = "查看系统分类表明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "typeId");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		GlobalType po = (GlobalType) this.globalTypeService.getById(Long
				.valueOf(id));
		return getAutoView().addObject("globalType", po).addObject("canReturn",
				Long.valueOf(canReturn));
	}

	@RequestMapping( { "sortList" })
	@Action(description = "排序分类列表", detail = "排序分类列表")
	public ModelAndView sortList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long parentId = Long.valueOf(RequestUtil.getLong(request, "parentId",-1L));
		List<GlobalType> list = this.globalTypeService.getByParentId(parentId);
		return getAutoView().addObject("globalTypeList", list);
	}

	@RequestMapping( { "sort" })
	@Action(description = "系统分类排序", detail = "查看系统分类表明细")
	public void sort(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultObj = null;
		PrintWriter out = response.getWriter();
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "typeIds");
		if (BeanUtils.isNotEmpty(lAryId)) {
			for (int i = 0; i < lAryId.length; i++) {
				Long typeId = lAryId[i];
				long sn = i + 1;
				this.globalTypeService.updSn(typeId, Long.valueOf(sn));
			}
		}
		resultObj = new ResultMessage(1, "排序分类完成");
		out.print(resultObj);
	}

	@RequestMapping( { "move" })
	@Action(description = "转移分类", detail = "<#assign dragEntity=globalTypeService.getById(Long.valueOf(dragId))/><#assign targetEntity=globalTypeService.getById(Long.valueOf(targetId))/>分类【${dragEntity.typeName}】转移到分类【${targetEntity.typeName}】<#if moveType=='prev'>之前<#elseif moveType=='next'>之后<#else>之内</#if>")
	public void move(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		PrintWriter out = response.getWriter();
		long targetId = RequestUtil.getLong(request, "targetId", 0L);
		long dragId = RequestUtil.getLong(request, "dragId", 0L);
		String moveType = RequestUtil.getString(request, "moveType");
		try {
			this.globalTypeService.move(Long.valueOf(targetId), Long
					.valueOf(dragId), moveType);
			resultMessage = new ResultMessage(1, "转移分类完成");
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "转移分类失败:" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(0, message);
			}
		}
		out.print(resultMessage);
	}

	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑系统分类", execOrder = ActionExecOrder.AFTER, detail = "<#if typeId>编辑系统分类<#assign entity=globalTypeService.getById(Long.valueOf(typeId))/>【${entity.typeName}】<#else>添加系统分类</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		return getEditView(request);
	}

	@RequestMapping( { "dialog" })
	@Action(description = "添加或编辑系统分类", execOrder = ActionExecOrder.AFTER)
	public ModelAndView dialog(HttpServletRequest request) throws Exception {
		return getEditView(request);
	}

	private ModelAndView getEditView(HttpServletRequest request)
			throws Exception {
		int isRoot = RequestUtil.getInt(request, "isRoot", 0);
		Long parentId = Long.valueOf(RequestUtil.getLong(request, "parentId"));
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		int isPrivate = RequestUtil.getInt(request, "isPrivate", 0);
		Long dataId = RequestUtil.getLong(request, "dataId", 0L);

		String parentName = "";
		GlobalType globalType = null;
		boolean isDict = false;
		boolean isAdd = false;
		if (typeId.longValue() > 0L) {
			if(dataId > 0L){ //关联记录从CWM_File_Type表取
				SysFileType sysFileType = sysFileTypeService.getFileType(typeId, dataId);
				globalType = new GlobalType(sysFileType);
			}else {
				globalType = (GlobalType) this.globalTypeService.getById(typeId);
				
			}
			parentId = globalType.getParentId();
			isDict = globalType.getCatKey().equals(GlobalType.CAT_DIC);
			
		} else {
			//初始化子节点
			GlobalType tmpGlobalType = null;
			if(dataId > 0L){
				SysFileType sysFileType = sysFileTypeService.getInitSysFileType(parentId, dataId);
				tmpGlobalType = new GlobalType(sysFileType);
			}else {
				tmpGlobalType = this.globalTypeService.getInitGlobalType(isRoot, parentId.longValue());
			}
			parentName = tmpGlobalType.getTypeName();
			
			isDict = tmpGlobalType.getCatKey().equals(GlobalType.CAT_DIC);
			globalType = new GlobalType();
			globalType.setType(tmpGlobalType.getType());
			globalType.setDataId(dataId.toString());
			isAdd = true;
		}
		return getAutoView().addObject("globalType", globalType)
				.addObject("parentId", parentId)
				.addObject("isRoot",Integer.valueOf(isRoot))
				.addObject("isAdd",Boolean.valueOf(isAdd))
				.addObject("parentName", parentName)
				.addObject("isDict", Boolean.valueOf(isDict))
				.addObject("isPrivate", Integer.valueOf(isPrivate));
	}

	@RequestMapping( { "getByCatKey" })
	@ResponseBody
	public List<? extends GlobalType> getByCatKey(HttpServletRequest request) {
		String catKey = RequestUtil.getString(request, "catKey");
		Long dataId = RequestUtil.getLong(request, "dataId", 0L);
		this.logger.debug("[catKey]:" + catKey);
		boolean hasRoot = RequestUtil.getInt(request, "hasRoot", 1) == 1;
		if( dataId>0L){//关联记录查询
			List<SysFileType> fileTypelist = sysFileTypeService.getByCatKey(catKey, dataId);
			return fileTypelist;
		}else {
			List<GlobalType> list = this.globalTypeService.getByCatKey(catKey, hasRoot);
			//将已逻辑删除的数据用*标识
			List<GlobalType> typeLists = list;
			for(GlobalType globalType:typeLists){
				String typeName = globalType.getTypeName();
				if(globalType.getGltype_delFlag()!=null&&globalType.getGltype_delFlag()==1){
					globalType.setTypeName(typeName+"*");
				}
				
			}
			return typeLists;
		}
		
	}

	@RequestMapping( { "getByCatKeyForBpm" })
	@ResponseBody
	public Set<GlobalType> getByCatKeyForBpm(HttpServletRequest request) {
		//ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		//String roleIds = this.sysRoleService.getRoleIdsByUserId(curUser.getUserId());
		//String orgIds = this.sysOrgService.getOrgIdsByUserId(curUser.getUserId());
		
		Set<GlobalType> globalTypeSet = null;
		globalTypeSet = new HashSet<GlobalType>();
		globalTypeSet.addAll(this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true));

		return globalTypeSet;
	}


	@RequestMapping( { "getByCatKeyForForm" })
	@ResponseBody
	public Set<GlobalType> getByCatKeyForForm(HttpServletRequest request) {
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();

		String roleIds = this.sysRoleService.getRoleIdsByUserId(curUser
				.getUserId());

		String orgIds = this.sysOrgService.getOrgIdsByUserId(curUser
				.getUserId());
		Set<GlobalType> globalTypeSet = null;

		if (!UserContextUtil.isSuperAdmin()) {
			globalTypeSet = this.globalTypeService.getByFormRightCat(curUser
					.getUserId(), roleIds, orgIds, true);
		} else {
			globalTypeSet = new HashSet<GlobalType>();
			globalTypeSet.addAll(this.globalTypeService.getByCatKey(
					GlobalType.CAT_FORM, true));
		}
		return globalTypeSet;
	}
	
	/**
	 * 个人文件类型
	 *@author YangBo @date 2016年10月11日下午4:00:57
	 *@param request
	 *@return
	 */
	@RequestMapping( { "getPersonType" })
	@ResponseBody
	public List<? extends GlobalType> getPersonType(HttpServletRequest request) {
		String catKey = RequestUtil.getString(request, "catKey");
		String dimensionKey = RequestUtil.getString(request, "dimensionKey");
		Long dataId = RequestUtil.getLong(request, "dataId", 0L);
		boolean hasRoot = RequestUtil.getInt(request, "hasRoot", 1) == 1;
		boolean isDimension = RequestUtil.getInt(request, "dimension", 0) == 1;
		Long userId = UserContextUtil.getCurrentUserId();
		//初始文件树模板--分类管理中添加指定catKey
		List<SysFileType> list = new ArrayList<SysFileType>();

		if(dataId>0L){//关联记录的文件分类
			List<SysFileType> fileTypeList = sysFileTypeService.getPersonType(catKey, userId, dataId.toString());
			if(fileTypeList.size()==0){
				List<GlobalType> typeList = this.globalTypeService.getPersonType(catKey, userId, hasRoot);
				for(GlobalType gltype:typeList){
					//初始化插入Sys_File_Type表
					SysFileType sysFileType = new SysFileType(gltype);
					sysFileType.setDataId(dataId.toString());
					sysFileTypeService.add(sysFileType);
					/*//输出到前台节点对象
					gltype.setDataId(dataId.toString());*/
					list.add(sysFileType);					
				}
			}else {
				if(isDimension){
					FileDimension fileD = new FileDimension();
					fileD.init(fileTypeList, dimensionKey);
					return fileD.getFileTypeList();
				}else{
					return fileTypeList;
				}
			}
		}else {
			//无记录关联
			List<GlobalType> typeList = this.globalTypeService.getPersonType(catKey, userId, hasRoot);
			for(GlobalType gltype:typeList){
				//初始化插入Sys_File_Type表
				SysFileType sysFileType = new SysFileType(gltype);
				list.add(sysFileType);					
			}
		}
		//将已逻辑删除的类型进行标记
		List<SysFileType> typeLists = list;
		for(SysFileType globalType:typeLists){
			String typeName = globalType.getTypeName();
			if(globalType.getGltype_delFlag()!=null&&globalType.getGltype_delFlag()==1){
				globalType.setTypeName(typeName+"*");
			}
		}
		if(isDimension){
			FileDimension fileD = new FileDimension();
			fileD.init(list, dimensionKey);
			return fileD.getFileTypeList();
		}
		return list;
	}
	@RequestMapping( { "getDimensionInfo" })
	@ResponseBody
	public ModelAndView getDimensionInfo(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String catKey = RequestUtil.getString(request, "catKey",SysFileType.CAT_FILE);
		String curDimensionKey = RequestUtil.getString(request, "curDimensionKey");
		Long dataId = RequestUtil.getLong(request, "dataId", 0L);
		Long userId = UserContextUtil.getCurrentUserId();
		boolean hasSon = RequestUtil.getBoolean(request, "hasSon",false);		
		List<SysFileType> dimensionList = new ArrayList<SysFileType>();
		List<Map> dimensionMap = new ArrayList<Map>();
		if(dataId>0L){//关联记录的文件分类
			List<SysFileType> fileTypeList = sysFileTypeService.getPersonType(catKey, userId, dataId.toString());
			FileDimension fileD = new FileDimension();
			if(hasSon){
				dimensionMap = fileD.getDimensionMap(fileTypeList);
			}else{
				dimensionList = fileD.getDimensionInfo(fileTypeList);
			}
		}
		return getAutoView().addObject("dimensionList", dimensionList)
				.addObject("dimensionMap", dimensionMap).addObject("hasSon", hasSon)
				.addObject("curDimensionKey", curDimensionKey);
	}
	
	@RequestMapping( { "getByCatKeyForMyRequest" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmMyRequest(HttpServletRequest request) {
		List<GlobalType> globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());

		List<?extends IProcessRun> list = this.processRunService.getMyRequestList(filter);

		Map<Long,Integer> typeMap = new HashMap<Long,Integer>();

		int emptyTypeCount = 0;
		for (IProcessRun processRun : list) {
			Long typeId = processRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}
		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}
     /***
      * 我的办结
      * @param request
      * @return
      */
	@RequestMapping( { "getByCatKeyForMyCompleted" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmMyCompleted(
			HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());
		List<?extends IProcessRun> list = this.processRunService
				.getMyCompletedList(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProcessRun IProcessRun : list) {
			Long typeId = IProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	@RequestMapping( { "getByCatKeyForMyRequestCompleted" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmMyRequestCompleted(
			HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());

		List<?extends IProcessRun> list = this.processRunService
				.getMyRequestCompletedList(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProcessRun processRun : list) {
			Long typeId = processRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}
		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	@RequestMapping( { "getByCatKeyForAlreadyCompleted" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmAlreadyCompleted(
			HttpServletRequest request) {
		List<GlobalType> globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId());

		List<?extends IProcessRun> list = this.processRunService
				.getMyRequestCompletedList(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProcessRun processRun : list) {
			Long typeId = processRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}
		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	private void mapCount(Map<Long, Integer> map, Long key, Integer num) {
		Integer count = (Integer) map.get(key);
		if (count != null){
	        map.put(key, Integer.valueOf(count.intValue() + num.intValue()));
		}else{
		    map.put(key, num);
		}
			
	}

	private List<GlobalType> setGlobalTypeSet(List<GlobalType> globalTypeList,
			Map<Long, Integer> typeMap, Integer emptyTypeCount) {
		SysTypeKey sysTypeKey = this.sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);

		Map typeCountMap = new HashMap();
		Long typeId;
		for (GlobalType globalType : globalTypeList) {
			typeId = globalType.getTypeId();
			Integer count = (Integer) typeMap.get(typeId);
			if (count == null) {
				continue;
			}
			mapCount(typeCountMap, typeId, count);

			Long[] globalTypeIds = removeElementToLong(globalType.getNodePath().split("\\."), typeId);
			for (GlobalType type : globalTypeList) {
				for (Long globalTypeId : globalTypeIds) {
					if (globalTypeId==null||type.getTypeId().longValue() != globalTypeId.longValue())
						continue;
					mapCount(typeCountMap, globalTypeId, count);
				}
			}
		}
		List<GlobalType> globalTypeSet = new ArrayList<GlobalType>();

		for (GlobalType globalType : globalTypeList) {
			typeId = globalType.getTypeId();
			Integer count = (Integer) typeCountMap.get(typeId);
			if (count == null)
				continue;
			String typeName = globalType.getTypeName();

			globalType.setTypeName(typeName + "(" + count + ")");
			globalTypeSet.add(globalType);
		}

		setEmptyGlobalTypeSet(globalTypeSet, GlobalType.CAT_FLOW, "");
		return globalTypeSet;
	}

	private Long[] removeElementToLong(String[] array, Long element) {
		if (ArrayUtils.isEmpty(array))
			return null;
		Long[] l = new Long[array.length];
		for (int i = 0; i < array.length; i++) {
		    if(StringUtil.isNotEmpty(array[i])){
		        l[i] = Long.valueOf(Long.parseLong(array[i]));
		    }
		}
		return (Long[]) ArrayUtils.removeElement(l, element);
	}

	private void setEmptyGlobalTypeSet(List<GlobalType> globalTypeSet,
			String catKey, String name) {
		if (BeanUtils.isNotEmpty(globalTypeSet))
			return;
		GlobalType globalType = this.globalTypeService.getRootByCatKey(catKey);
		if (StringUtils.isEmpty(name))
			name = globalType.getTypeName() + "(0)";
		else
			name = globalType.getTypeName() + name;
		globalType.setTypeName(name);
		globalTypeSet.add(globalType);
	}

	@RequestMapping( { "forPending" })
	@ResponseBody
	public List<GlobalType> forPending(HttpServletRequest request) {
		String catKey = GlobalType.CAT_FLOW;
		List<GlobalType> globalTypeSet = new ArrayList<GlobalType>();
		List<GlobalType> globalTypeList = this.globalTypeService.getFlowTreeByCatKey();
		//SysTypeKey sysTypeKey = this.sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);
		List<?extends ITaskAmount> countlist = this.bpmService
				.getMyTasksCount(UserContextUtil.getCurrentUserId());
		//合并子流程到主流程
		for(ITaskAmount taskAmount:countlist){
		   taskAmount.setDefId(getParent(taskAmount.getDefId()));
		}
		
		Map<Long,Integer> typeMap = new HashMap<Long,Integer>();
		Map<Long,Integer> typeNotReadMap = new HashMap<Long,Integer>();
		//构建 已读 未读 待办任务map
		for (ITaskAmount taskAmount : countlist) {
			//Long typeId = taskAmount.getTypeId();
	        Long defId = taskAmount.getDefId();
			if (BeanUtils.isEmpty(defId)) {
				continue;
			}
            //mapCount(typeMap, typeId, taskAmount.getTotal());
            mapCount(typeMap, defId, taskAmount.getTotal());
            
            //mapCount(typeNotReadMap, typeId, taskAmount.getNotRead());
            mapCount(typeNotReadMap, defId, taskAmount.getNotRead());

		}

		Map<Long,Integer> typePendingMap = new HashMap<Long,Integer>();
		Map<Long,Integer> typePendingReadMap = new HashMap<Long,Integer>();

		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = (Integer) typeMap.get(typeId);
			Integer readCount = (Integer) typeNotReadMap.get(typeId);
			if (count == null) {
				continue;
			}
			readCount = Integer.valueOf(readCount == null ? 0 : readCount
					.intValue());
			mapCount(typePendingMap, typeId, count);
			mapCount(typePendingReadMap, typeId, readCount);

			Long[] globalTypeIds = removeElementToLong(globalType.getNodePath()
					.split("[.]"), typeId);
			for (GlobalType type : globalTypeList) {
				for (Long globalTypeId : globalTypeIds) {
					if (type.getTypeId().longValue() != globalTypeId
							.longValue()){
					    continue;
					}
					mapCount(typePendingMap, globalTypeId, count);
					mapCount(typePendingReadMap, globalTypeId, readCount);
				}
			}

		}

		for (GlobalType globalType : globalTypeList) {
			Long typeId = globalType.getTypeId();
			Integer count = (Integer) typePendingMap.get(typeId);
			Integer readCount = (Integer) (typePendingReadMap)
					.get(typeId);
			if (count == null){
			    continue;
			}
			readCount = Integer.valueOf(readCount == null ? 0 : readCount
					.intValue());

			String typeName = globalType.getTypeName();
			globalType.setTypeName(typeName + "(" + readCount + "/" + count
					+ ")");
			globalTypeSet.add(globalType);
		}

		setEmptyGlobalTypeSet(globalTypeSet, catKey, "(0/0)");
		return globalTypeSet;
	}
	private Long getParent(Long defId)
    {
        IDefinition definition=definitionService.getById(defId);
        if(StringUtil.isNotEmpty(definition.getKeyPath())){
            definition=definitionService.getByDefKeyIsMain(definition.getKeyPath());
            if(definition!=null){
                getParent(definition.getDefId());
            }
        }
        return definition.getDefId();
    }

    /***
	 * 我的已办任务
	 * @param request
	 * @return
	 */
	@RequestMapping( { "getByCatKeyForAlready" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForAlready(HttpServletRequest request) {
	    List<GlobalType> globalTypeList = this.globalTypeService.getFlowTreeByCatKey();
		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("assignee", UserContextUtil.getCurrentUserId()
				.toString());

		List<?extends IProcessRun> list = this.processRunService
				.getAlreadyMattersList(filter);

		Map<Long,Integer> typeMap = new HashMap<Long,Integer>();

		int emptyTypeCount = 0;
		for (IProcessRun IProcessRun : list) {
			Long typeId = IProcessRun.getTypeId();
	        Long defId = IProcessRun.getDefId();
	        //如果是子流程，获取最顶层的父流程
	        defId=getParent(defId);
	        if(BeanUtils.isNotEmpty(typeId)&&BeanUtils.isNotEmpty(defId)){
                mapCount(typeMap, defId, Integer.valueOf(1));
            }else{
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	@RequestMapping( { "getByCatKeyForSelectPro" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForSelectPro(HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("currentUserId", UserContextUtil.getCurrentUserId()
				.toString());

		List<?extends IProcessRun> list = this.processRunService.selectPro(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProcessRun IProcessRun : list) {
			Long typeId = IProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}
	/***
	 * 加签(流转)事宜
	 * @param request
	 * @return
	 */
	@RequestMapping( { "getByCatKeyForAccording" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmAccording(HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("ownerId", UserContextUtil.getCurrentUserId());

		List<?extends ITaskExe> list = this.taskExeService.accordingMattersList(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (ITaskExe taskExe : list) {
			Long typeId = taskExe.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	@RequestMapping( { "getByCatKeyForToMatter" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForToMatter(HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("createUserId", UserContextUtil.getCurrentUserId());

		List<?extends IProTransTo> list = this.proTransToService.mattersList(filter);
		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProTransTo proTransTo : list) {
			Long typeId = proTransTo.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}
	/***
	 * 办结事宜
	 * @param request
	 * @return
	 */
	@RequestMapping( { "getByCatKeyForCompleted" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForCompleted(HttpServletRequest request) {
		List globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		filter.addFilterForIB("creatorId", UserContextUtil.getCurrentUserId()
				.toString());
		List<?extends IProcessRun> list = this.processRunService
				.getCompletedMattersList(filter);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IProcessRun IProcessRun : list) {
			Long typeId = IProcessRun.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}

		return setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));
	}

	@RequestMapping( { "getByCatKeyForNewPro" })
	@ResponseBody
	public List<GlobalType> getByCatKeyForBpmNewPro(HttpServletRequest request) {
	    List<GlobalType> globalTypeList = this.globalTypeService.getByCatKey(GlobalType.CAT_FLOW,
				true);

		QueryFilter filter = new QueryFilter(request, false);
		Long glTypeId = Long
				.valueOf(RequestUtil.getLong(request, "typeId", 0L));

		Long start = Long.valueOf(System.currentTimeMillis());

		Long userId = UserContextUtil.getCurrentUserId();
		String isNeedRight = "";
		Map authorizeRightMap = null;
		if (!UserContextUtil.isSuperAdmin()) {
			isNeedRight = "yes";

			Map actRightMap = this.defAuthorizeService.getActRightByUserMap(
					userId, "start", false, false);

			String actRights = (String) actRightMap.get("authorizeIds");
			filter.addFilterForIB("actRights", actRights);
		}
		filter.addFilterForIB("isNeedRight", isNeedRight);

		List<?extends IDefinition> list = this.definitionService.getMyDefList(filter,
				glTypeId);

		Map typeMap = new HashMap();

		int emptyTypeCount = 0;
		for (IDefinition definition : list) {
			Long typeId = definition.getTypeId();
			if (BeanUtils.isNotEmpty(typeId))
				mapCount(typeMap, typeId, Integer.valueOf(1));
			else {
				emptyTypeCount++;
			}
		}
		List<GlobalType> globalTypeSet = setGlobalTypeSet(globalTypeList, typeMap, Integer
				.valueOf(emptyTypeCount));

		this.logger.debug("getByCatKeyForBpmNewPro:--------------"
				+ (System.currentTimeMillis() - start.longValue())
				+ "-------------------");

		SysTypeKey sysTypeKey = this.sysTypeKeyService.getByKey(GlobalType.CAT_FLOW);

		if ("1".equals(AppConfigUtil.get("isNewProcessIncludeOA"))) {
			GlobalType oaType = new GlobalType();
			oaType.setTypeName("原OA流程");
			oaType.setCatKey(sysTypeKey.getTypeKey());
			oaType.setParentId(sysTypeKey.getTypeKeyId());
			oaType.setIsParent("false");
			oaType.setTypeId(Long.valueOf(-1000L));
			oaType.setType(sysTypeKey.getType());
			oaType.setNodePath(sysTypeKey.getTypeKeyId() + ".");
			globalTypeSet.add(oaType);
		}

		return globalTypeSet;
	}

	@RequestMapping( { "flowTypeSelector" })
	public ModelAndView flowTypeSelector(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		QueryFilter queryFilter = new QueryFilter(request, "globalTypeItem");
		queryFilter.addFilterForIB("catkey", GlobalType.CAT_FLOW);
		List globalTypeList = this.globalTypeService.getAll(queryFilter);
		return getAutoView().addObject("globalTypeList", globalTypeList);
	}

	/***
	 * 空页面
	 * @author liubo
	 * @param request
	 * @return
	 */
	@RequestMapping( { "getEmpty" })
	public ModelAndView getEmpty(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long isDelete = RequestUtil.getLong(request, "isDelete");
		boolean isShow = false;
		if(isDelete==1){
			isShow = true;
		}
		return getAutoView().addObject("isShow", isShow);
	}

}
