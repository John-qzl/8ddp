package com.cssrc.ibms.system.controller;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.common.PinyinUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.dao.DictionaryDao;
import com.cssrc.ibms.system.model.Dictionary;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.service.DictionaryService;
import com.cssrc.ibms.system.service.GlobalTypeService;

import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
/**
 * 数据字典
 * <p>Title:DictionaryController</p>
 * @author Yangbo 
 * @date 2016-8-5上午10:45:08
 */
@Controller
@RequestMapping( { "/oa/system/dictionary/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DictionaryController extends BaseController {
	@Resource
	private DictionaryDao dictionaryDao;
	@Resource
	private DictionaryService dictionaryService;
	@Resource
	private GlobalTypeService globalTypeService;

	@RequestMapping( { "tree" })
	public ModelAndView tree(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv=new ModelAndView("oa/system/dictionaryTree.jsp");
		return mv;
	}
	
	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑数据字典", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd==1>添加数据字典<#else>编辑数据字典<#assign entity=dictionaryService.getById(Long.valueOf(dicId))/>【${entity.itemName}】</#if>")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		int isAdd = RequestUtil.getInt(request, "isAdd", 0);
		int isRoot = RequestUtil.getInt(request, "isRoot", 0);
		Long dicId = Long.valueOf(RequestUtil.getLong(request, "dicId", 0L));
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		ModelAndView mv = getAutoView();
		Dictionary dictionary = null;
		if (isAdd == 1) {
			dictionary = new Dictionary();

			if (isRoot == 1) {
				GlobalType globalType = (GlobalType) this.globalTypeService.getById(dicId);
				dictionary.setTypeId(dicId);
				dictionary.setParentId(dicId);
				dictionary.setType(globalType.getType());
			} else {
				Dictionary parentDic = (Dictionary) this.dictionaryService
						.getById(dicId);
				dictionary.setParentId(dicId);
				dictionary.setTypeId(parentDic.getTypeId());
				dictionary.setType(parentDic.getType());
			}
		} else {
			dictionary = (Dictionary) this.dictionaryService.getById(dicId);
		}
		mv.addObject("dictionary", dictionary).addObject("isAdd",
				Integer.valueOf(isAdd)).addObject("canReturn",
				Long.valueOf(canReturn));

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除数据字典", execOrder = ActionExecOrder.BEFORE, detail = "删除数据字典<#list StringUtils.split(dicId,\",\") as item><#assign entity=dictionaryService.getById(Long.valueOf(item))/>【${entity.itemName}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		ResultMessage message = null;
		try {
			Long dicId = Long.valueOf(RequestUtil.getLong(request, "dicId"));
			this.dictionaryService.delByDicId(dicId);
			message = new ResultMessage(1, "删除数据字典成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除数据字典失败");
		}
		writeResultMessage(out, message);
	}
	
	@RequestMapping( { "delLogic" })
	@Action(description = "逻辑删除数据字典", execOrder = ActionExecOrder.BEFORE, detail = "逻辑删除数据字典<#list StringUtils.split(dicId,\",\") as item><#assign entity=dictionaryService.getById(Long.valueOf(item))/>【${entity.itemName}】</#list>")
	public void delLogic(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		PrintWriter out = response.getWriter();
		Long currentUserId = UserContextUtil.getCurrentUser().getUserId();
		ResultMessage message = null;
		try {
			Long dicId = Long.valueOf(RequestUtil.getLong(request, "dicId"));
			dictionaryService.updateStatus(dicId,currentUserId,(short) 1);
			message = new ResultMessage(1, "逻辑删除数据字典成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "逻辑删除数据字典失败");
		}
		writeResultMessage(out, message);
	}

	@RequestMapping( { "sortList" })
	@Action(description = "排序数据字典列表", detail = "排序数据字典列表")
	public ModelAndView sortList(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long parentId = Long.valueOf(RequestUtil.getLong(request, "parentId",
				-1L));
		List<Dictionary> list = this.dictionaryService.getByParentId(parentId.longValue());
		return getAutoView().addObject("dictionaryList", list);
	}

	@RequestMapping( { "sort" })
	@Action(description = "数据字典排序", detail = "数据字典排序")
	public void sort(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		Long[] lAryId = RequestUtil.getLongAryByStr(request, "dicIds");
		try {
			this.dictionaryService.updSn(lAryId);
			resultMessage = new ResultMessage(1, "字典排序成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "字典排序失败:" + str);
				response.getWriter().print(resultMessage);
			} else {
				String message = ExceptionUtil.getExceptionMessage(e);
				resultMessage = new ResultMessage(0, message);
				response.getWriter().print(resultMessage);
			}
		}
	}

	@RequestMapping( { "getByTypeId" })
	@ResponseBody
	public List<Dictionary> getByTypeId(HttpServletRequest request) {
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		List<Dictionary> list = this.dictionaryService.getByTypeId(typeId.longValue(), true);
		return list;
	}

	@RequestMapping( { "getByNodeKey" })
	@ResponseBody
	public List<Dictionary> getByNodeKey(HttpServletRequest request) {
		String nodeKey = RequestUtil.getString(request, "nodeKey");
		List<Dictionary> list = this.dictionaryService.getByNodeKey(nodeKey);
		return list;
	}

	@RequestMapping( { "getMapByNodeKey" })
	@ResponseBody
	public Map<String, Object> getMapByNodeKey(HttpServletRequest request) {
		String nodeKey = RequestUtil.getString(request, "nodeKey");
		boolean isSearchForm = RequestUtil.getBoolean(request, "isSearchForm");
		Map<String, Object> map = new HashMap<String, Object>();
		GlobalType globalType = this.globalTypeService
				.getByDictNodeKey(nodeKey);
		List<Dictionary> list = this.dictionaryService.getByTypeId(globalType.getTypeId()
				.longValue(), false);
		//去除已逻辑删除的数据
		for(int i = 0;i<list.size();i++){
			if(list.get(i).getDic_delFlag()!=null && list.get(i).getDic_delFlag() == 1){
				list.remove(i);
			}
		}
		//在查询区域中的数据字典添加全部选项
		if(isSearchForm){
			Dictionary dictionaryAll = new Dictionary();
			dictionaryAll.setItemName("全部");
			list.add(0,dictionaryAll);
		}
		
		map.put("globalType", globalType);
		map.put("dicList", list);
		return map;
	}

	@RequestMapping( { "move" })
	@Action(description = "移动字典", detail = "<#assign dragEntity=dictionaryService.getById(Long.valueOf(dragId))/><#assign targetEntity=dictionaryService.getById(Long.valueOf(targetId))/>字典【${dragEntity.itemName}】转移到字典【${targetEntity.itemName}】<#if moveType=='prev'>之前<#elseif moveType=='next'>之后<#else>之下</#if>")
	public void move(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage resultMessage = null;
		PrintWriter out = response.getWriter();
		long targetId = RequestUtil.getLong(request, "targetId", 0L);
		long dragId = RequestUtil.getLong(request, "dragId", 0L);
		String moveType = RequestUtil.getString(request, "moveType");
		try {
			this.dictionaryService.move(Long.valueOf(targetId), Long
					.valueOf(dragId), moveType);
			resultMessage = new ResultMessage(1, "移动字典成功");
		} catch (Exception ex) {
			String str = MessageUtil.getMessage();
			if (StringUtil.isNotEmpty(str)) {
				resultMessage = new ResultMessage(0, "移动字典失败:" + str);
			} else {
				String message = ExceptionUtil.getExceptionMessage(ex);
				resultMessage = new ResultMessage(0, message);
			}
		}
		out.print(resultMessage);
	}

	@RequestMapping( { "getDictionaryKey" })
	public void getDictionaryKey(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String dicName = RequestUtil.getString(request, "subject");
		String pingyin = PinyinUtil.getPinYinHeadCharFilter(dicName);
		writeResultMessage(response.getWriter(), pingyin, 1);
	}
	
	/**
	 * 还原被逻辑删除的数据字典
	 * @author liubo
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping( { "restore" })
	@Action(description = "还原被逻辑删除的数据字典", execOrder = ActionExecOrder.AFTER, detail = "还原被逻辑删除的数据字典")
	public void restore(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		Long currentUserId = UserContextUtil.getCurrentUser().getUserId();
		try {
			Long dicId = Long.valueOf(RequestUtil.getLong(request, "dicId"));
			
			dictionaryService.updateStatus(dicId,currentUserId,(short)0);
			message = new ResultMessage(1, "数据字典还原成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "数据字典还原失败" + e.getMessage());
		}
		writeResultMessage(response.getWriter(), message);
	}
}
