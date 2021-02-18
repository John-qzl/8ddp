package com.cssrc.ibms.record.controller;


import java.io.PrintWriter;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MessageUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.record.model.RecFun;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.record.service.RecFunService;
import com.cssrc.ibms.record.service.RecTypeService;

@Controller
@RequestMapping("/oa/system/recType/")
@Action(ownermodel=SysAuditModelType.RECTYPE_PROJECT_MANAGEMENT)
public class RecTypeController extends BaseController {
	@Resource
	private RecFunService recFunService;
	@Resource
	private RecTypeService recTypeService;
	
	public ModelAndView getAutoView() throws Exception {
		ModelAndView mv = super.getAutoView();
		String viewName = mv.getViewName();
		if(!viewName.contains("/oa/system/record/"))
			viewName = viewName.replace("/oa/system/", "/oa/system/record/type/");
		mv.setViewName(viewName);
		return mv;
	}
	/**
	 * 表单类别列表
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "list" })
	@Action(description = "查看表单类别分页列表", execOrder = ActionExecOrder.AFTER, detail = "查看表单类别分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<RecType> list = this.recTypeService.getAll(new QueryFilter(request, "recTypeItem"));
		ModelAndView mv = getAutoView().addObject("recTypeList", list);
		return mv;
	}
	/**
	 * 编辑-表单类别
	 * 
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "edit" })
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long typeId = Long.valueOf(RequestUtil.getLong(request, "typeId"));
		String returnUrl = RequestUtil.getPrePage(request);
		RecType recType = null;
		if (typeId.longValue() != 0L) {
			recType = (RecType) this.recTypeService.getById(typeId);
		} else {
			recType = new RecType();
		}
		return getAutoView().addObject("recType", recType).addObject("returnUrl", returnUrl);
	}
	
	/**
	 * 删除-表单类别
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({ "del" })
	@Action(description = "删除表单类别表", execOrder = ActionExecOrder.BEFORE, detail = "删除表单类别:<#list typeId?split(\",\") as item><#assign entity=recTypeService.getById(Long.valueOf(item))/>${entity.typeName}【${entity.alias}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "typeId");
			delByIds(lAryId);
			message = new ResultMessage(1, "删除表单类别成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除表单类别失败:" + e.getMessage());
			e.printStackTrace();
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	/**
	 * 相当于级联删除，删除有关表单类别的所有表信息
	 * 
	 * @param lAryId
	 */
	private void delByIds(Long[] lAryId) {
		if (BeanUtils.isEmpty(lAryId))
			return;
		for (Long typeId : lAryId) {
			//删除此类别下的所有表单
			List<RecFun> list =  this.recFunService.getByTypeId(typeId);
			for(RecFun rf : list){
				Long funId = rf.getFunId();
				this.recFunService.delById(funId);
			}
			//删除此类别
			this.recTypeService.delById(typeId);
			
		}
	}
	/**
	 * 明细-表单类别
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({ "get" })
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		ModelAndView mv = getAutoView();
		return getView(request, response, mv, 0);
	}
	@Action(description = "查看表单类别明细", execOrder = ActionExecOrder.AFTER, detail = "查看表单类别明细<#assign entity=recTypeService.getById(typeId)/>【${entity.typeId}】", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView getView(HttpServletRequest request,
			HttpServletResponse response, ModelAndView mv, int isOtherLink)
			throws Exception {
		long typeId = RequestUtil.getLong(request, "typeId");
		RecType recType = (RecType) this.recTypeService.getById(typeId);
		return mv.addObject("recType", recType)
				.addObject("isOtherLink", Integer.valueOf(isOtherLink));
	}
	/**
	 * @param request
	 * @param response
	 * @param type
	 * @param bindResult
	 * @throws Exception
	 */
	@RequestMapping( { "save" })
	@Action(description = "添加或更新表单类别", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>表单类别：${SysAuditLinkService.getRecTypeLink(Long.valueOf(id))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { RecType.class }, pkName = "typeId")
	public void save(HttpServletRequest request, HttpServletResponse response, RecType type, BindingResult bindResult) 
			throws Exception {
				PrintWriter out = response.getWriter();
				//设置操作结果，默认为操作失败
				Short resultStatus = 0;
				String id = null;
				ResultMessage resultMessage = validForm("type", type,
						bindResult, request);
				if (resultMessage.getResult() == 0) {
					writeResultMessage(response.getWriter(), resultMessage);
				}
				boolean isadd = type.getTypeId() == null;

				String resultMsg = null;
				if (type.getTypeId() == null) {
					Integer rtn = this.recTypeService.isAliasExists(type);
					if (rtn.intValue() > 0) {
						resultMsg = "别名在系统中已存在!";
						id = recTypeService.getByAlias(type.getAlias()).getTypeId().toString();
						writeResultMessage(response.getWriter(), resultMsg, 0);
						LogThreadLocalHolder.putParamerter("resultMsg", "别名在系统中已存在!");
						LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
						LogThreadLocalHolder.putParamerter("id", recTypeService.getByAlias(type.getAlias()).getTypeId().toString());
						LogThreadLocalHolder.setResult(resultStatus);
						return;
					}else{
						try {
							Long typeId = Long.valueOf(UniqueIdUtil.genId());
							type.setTypeId(typeId);
							this.recTypeService.add(type);
							resultStatus = 1;
							String result = "{result:1,operate:'add'}";
							out.print(result);
						} catch (Exception e) {
							e.printStackTrace();
							writeResultMessage(response.getWriter(), "添加表单类别失败!", 0);
						}
						id = type.getTypeId().toString();
					}
				} else {
					try {
						this.recTypeService.update(type);
						resultStatus = 1;
						String result = "{result:1,operate:'edit'}";
						out.print(result);
					} catch (Exception e) {
						String str = MessageUtil.getMessage();
						if (StringUtil.isNotEmpty(str)) {
							resultMessage = new ResultMessage(0, "更新表单类别失败:" + str);
							response.getWriter().print(resultMessage);
						} else {
							String message = ExceptionUtil.getExceptionMessage(e);
							resultMessage = new ResultMessage(0, message);
							response.getWriter().print(resultMessage);
						}
					}
					id = type.getTypeId().toString();
				}
				try {
					LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
					LogThreadLocalHolder.putParamerter("type", type);
					LogThreadLocalHolder.putParamerter("isAdd",Boolean.valueOf(isadd));
					LogThreadLocalHolder.putParamerter("id", id);
					LogThreadLocalHolder.setResult(resultStatus);
				} catch (Exception e) {
					e.printStackTrace();
					this.logger.error(e.getMessage());
				}
		}
	/**
	 * 获取所有的表单类别信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping({"getSystemTreeData"})
	@ResponseBody
	public List<RecType> getSystemTreeData(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List<RecType> list = this.recTypeService.getAll();
		RecType parent = this.recTypeService.getParentTypeById(0L);
		list.add(parent);	
		return list;
	}
}
