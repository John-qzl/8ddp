package com.cssrc.ibms.core.log.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.model.SysLogSwitch;
import com.cssrc.ibms.core.log.service.SysLogSwitchService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 日志开关管理
 * <p>Title:SysLogSwitchController</p>
 * @author Yangbo 
 * @date 2016年9月7日上午9:26:44
 */
@Controller
@RequestMapping({ "/oa/system/sysLogSwitch/" })
@Action(ownermodel = SysAuditModelType.LOG_SWITCH_MANAGEMENT)
public class SysLogSwitchController extends BaseController {

	@Resource
	private SysLogSwitchService sysLogSwitchService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新日志开关", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>修改</#if>系统日志开关信息：${SysAuditLinkService.getSysLogSwitchLink(Long.valueOf(id))}",exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysLogSwitch.class }, pkName = "id")
	public void save(HttpServletRequest request, HttpServletResponse response,SysLogSwitch sysLogSwitch)
			throws Exception {
		String resultMsg = null;
		Timestamp time=new Timestamp(System.currentTimeMillis());
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		String id = null;
		try {
			if ((sysLogSwitch.getId() == null)
					|| (sysLogSwitch.getId().longValue() == 0L)) {
				sysLogSwitch.setId(Long.valueOf(UniqueIdUtil.genId()));
				sysLogSwitch.setLastuptime(time);
				this.sysLogSwitchService.add(sysLogSwitch);
				result = 1;
				id = sysLogSwitch.getId().toString();
				resultMsg = "添加日志开关成功";
			} else {
				SysLogSwitch iSysLogSwitch = (SysLogSwitch) this.sysLogSwitchService
						.getById(sysLogSwitch.getId());
				iSysLogSwitch.setStatus(sysLogSwitch.getStatus());
				iSysLogSwitch.setMemo(sysLogSwitch.getMemo());
				iSysLogSwitch.setExecTypes(sysLogSwitch.getExecTypes());
				this.sysLogSwitchService.update(iSysLogSwitch);
				result = 1;
				isadd = false;
				id = sysLogSwitch.getId().toString();
				resultMsg = "更新日志开关成功";
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(),
					resultMsg + "," + e.getMessage(), 0);
		}
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", id);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	protected SysLogSwitch getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd" }));

		String json = RequestUtil.getString(request, "json");
		JSONObject obj = JSONObject.fromObject(json);

		SysLogSwitch sysLogSwitch = (SysLogSwitch) JSONObject.toBean(obj,
				SysLogSwitch.class);

		return sysLogSwitch;
	}

	@RequestMapping({ "list" })
	@Action(description = "查看日志开关分页列表", detail = "查看日志开关分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		@SuppressWarnings("rawtypes")
		List list = this.sysLogSwitchService.getAll(new QueryFilter(request,
				"sysLogSwitchItem"));
		ModelAndView mv = getAutoView().addObject("sysLogSwitchList", list);

		return mv;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@RequestMapping({ "management" })
	@Action(description = "系统日志开关管理", detail = "系统日志开关管理",exectype = SysAuditExecType.SELECT_TYPE )
	public ModelAndView management(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
 		List list = this.sysLogSwitchService.getAll();
 		List execTypeList = new ArrayList();
 		//获取所有的日志类型选项
 		SysAuditExecType[] execTypes = SysAuditExecType.values();
		for (SysAuditExecType type : execTypes) {
			execTypeList.add(type.toString());
		}
		ModelAndView mv = getAutoView().addObject("sysLogSwitchList", list)
									   .addObject("execTypeList", execTypeList);
		return mv;
	}

	@RequestMapping({ "del" })
	@Action(description = "删除日志开关", execOrder = ActionExecOrder.BEFORE, detail = "删除日志开关<#assign entity=sysLogSwitchService.getById(Long.valueOf(id))/>【${entity.model}】",exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysLogSwitchService.delByIds(lAryId);
			message = new ResultMessage(1, "删除日志开关成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({ "edit" })
	@Action(description = "编辑日志开关", detail = "编辑日志开关<#assign entity=sysLogSwitchService.getById(Long.valueOf(id))/>${entity.model}【${entity.id}】",
			exectype = SysAuditExecType.SELECT_TYPE
	)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		ModelAndView mv = getAutoView();
		
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		String model = RequestUtil.getString(request, "model");
		String returnUrl = RequestUtil.getPrePage(request);
		SysLogSwitch sysLogSwitch = (SysLogSwitch) this.sysLogSwitchService.getById(id);
		JSONArray execTypeArr = new JSONArray();
		
		//获取所有的日志类型选项
 		SysAuditExecType[] execTypes = SysAuditExecType.values();
		for (SysAuditExecType type : execTypes) {
			JSONObject execTypeObj = new JSONObject();
			String name = type.toString();
			execTypeObj.put("name", name);
			execTypeObj.put("status", "0");
			if(sysLogSwitch!=null){
				//获取当前开关选择的日志类型
				String logSwitchExecTypes = sysLogSwitch.getExecTypes();
				if(logSwitchExecTypes!=null){//若已有选中的日志类型，则将状态设置为1
					if(logSwitchExecTypes.contains(name)){
						execTypeObj.put("status", "1");
					}
				}
			}
			
			execTypeArr.add(execTypeObj);
		}
		
		return mv.addObject("sysLogSwitch", sysLogSwitch)
				 .addObject("returnUrl", returnUrl)
				 .addObject("modelxx", model)
				 .addObject("execTypeArr", execTypeArr);
	}

	@RequestMapping({ "get" })
	@Action(description = "查看日志开关明细", detail = "查看日志开关明细",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysLogSwitch sysLogSwitch = (SysLogSwitch) this.sysLogSwitchService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("sysLogSwitch", sysLogSwitch);
	}
	
	@RequestMapping( { "setLogStatus" })
	@Action(description = "设置日志开关状态", execOrder = ActionExecOrder.AFTER, detail = "设置日志开关<#list logPks?split(\",\") as item><#assign entity=sysLogSwitchService.getByModel(item)/>【${entity.model}】 </#list> 状态为:${switchStatus} ", exectype = SysAuditExecType.UPDATE_TYPE)
	public void setLogStatus(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		String logPks = request.getParameter("logPks");
		String execTypes = request.getParameter("execTypes");
		Short logStatus = RequestUtil.getShort(request, "logStatus");
		
		Timestamp time=new Timestamp(System.currentTimeMillis());
		String[] aryLogPk = logPks.split(",");
		//设置操作结果，默认为操作失败
		Short result = 0;
		String switchStatus = "开启";
		try {
			if(logStatus == 0)
				switchStatus = "关闭";
			//获取当前用户
			ISysUser sysUser = UserContextUtil.getCurrentUser();
			
			for(int i=0;i<aryLogPk.length;i++){
				String ownermodel = aryLogPk[i];
				//获取每一个日志开关数据
				SysLogSwitch sysLogSwitch = this.sysLogSwitchService.getByModel(ownermodel);
				
				if (sysLogSwitch == null) {
					SysLogSwitch isysLogSwitch = new SysLogSwitch();
					isysLogSwitch.setModel(ownermodel);
					isysLogSwitch.setId(Long.valueOf(UniqueIdUtil.genId()));
					isysLogSwitch.setCreator(sysUser.getUsername());
					isysLogSwitch.setCreatorid(sysUser.getUserId());
					isysLogSwitch.setLastuptime(time);
					isysLogSwitch.setStatus(logStatus);
					isysLogSwitch.setExecTypes(execTypes);
					this.sysLogSwitchService.add(isysLogSwitch);
				} else {
					sysLogSwitch.setUpdby(sysUser.getUsername());
					sysLogSwitch.setUpdbyid(sysUser.getUserId());
					sysLogSwitch.setMemo(sysLogSwitch.getMemo());
					sysLogSwitch.setStatus(logStatus);
					sysLogSwitch.setExecTypes(execTypes);
					this.sysLogSwitchService.update(sysLogSwitch);
				}
			}
			result = 1;
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,"设置状态成功");
			writeResultMessage(writer, resultMessage);
			
		} catch (Exception e) {
			// TODO: handle exception
			String msg = ExceptionUtil.getExceptionMessage(e);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,msg));
		}
		try {
			LogThreadLocalHolder.putParamerter("logPks", logPks);
			LogThreadLocalHolder.putParamerter("switchStatus", switchStatus);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}
}
