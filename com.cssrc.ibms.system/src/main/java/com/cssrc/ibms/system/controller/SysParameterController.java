package com.cssrc.ibms.system.controller;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.common.ExceptionUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysParameter;
import com.cssrc.ibms.system.service.SysFileService;
import com.cssrc.ibms.system.service.SysParameterService;
import com.cssrc.ibms.system.service.SysPaurService;
/**
 * 
 * <p>Title:SysParameterController</p>
 * @author Yangbo 
 * @date 2016-8-10下午04:53:52
 */
@Controller
@RequestMapping( { "/oa/system/sysParameter/" })
@Action(ownermodel = SysAuditModelType.PARAMETER_MANAGEMENT)
public class SysParameterController extends BaseController {

	@Resource
	private SysParameterService sysParameterService;
	@Resource
	private SysPaurService sysPaurService;
	@Resource
	private SysFileService sysFileService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新参数表", execOrder = ActionExecOrder.AFTER, detail = "<#if isAdd>添加<#else>更新</#if>参数 ${SysAuditLinkService.getSysParameterLink(Long.valueOf(id))}", exectype = SysAuditExecType.UPDATE_TYPE)
	@DataNote(beanName = { SysParameter.class }, pkName = "id")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SysParameter sysParameter) throws Exception {
		String resultMsg = null;
		//设置操作结果，默认为操作失败
		Short result = 0;
		boolean isadd = true;
		String id = null;
		try {
			boolean isExistParamName = false;
			if ((sysParameter.getId() == null) || (sysParameter.getId().longValue() == 0L)) {
				sysParameter.setId(Long.valueOf(UniqueIdUtil.genId()));
				isExistParamName = this.sysParameterService.isExistParamName(sysParameter.getParamname());
				if (!isExistParamName) {
					this.sysParameterService.add(sysParameter);
					result = 1;
					id = sysParameter.getId().toString();
					resultMsg = "添加新参数成功";
				} else {
					resultMsg = "该参数名已经存在！";
					id = this.sysParameterService.getByParamName(sysParameter.getParamname()).get(0).getId().toString();
				}
			}else{
				isExistParamName=this.sysParameterService.isExistParam(sysParameter.getParamname(), sysParameter.getId());
				if(isExistParamName){
					resultMsg="该参数名已用过";
					id = this.sysParameterService.getByParamName(sysParameter.getParamname()).get(0).getId().toString();
				}else{
					if(sysParameter.getParamname().equals("SYS_THEME")){//当对主题更改时还要对主题表进行更改
						sysPaurService.updSysTheme(sysParameter.getParamvalue());
					}
					this.sysParameterService.update(sysParameter);
					this.sysParameterService.updateCache(sysParameter);
					//如果是文件密级就更新map
					if(ISysFile.FILE_SECURITY_MAP.equals(sysParameter.getParamname())){
						sysFileService.addFileSecurity();
					}
					result = 1;
					isadd = false;
					id = sysParameter.getId().toString();
					resultMsg="更新参数成功";
				}
			}
			if (isExistParamName)
				writeResultMessage(response.getWriter(), resultMsg, 0);
			else{
				//更新后再读取新的分类
				List<Map<String, Object>> typeList = this.sysParameterService.getType();
				ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,resultMsg);
				resultMessage.addData("types", JSONArray.fromObject(typeList).toString());
				writeResultMessage(response.getWriter(), resultMessage);
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
			e.printStackTrace();
		}
		try {
			LogThreadLocalHolder.putParamerter("resultMsg", resultMsg);
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("id", id);
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
	}

	@RequestMapping( { "list" })
	@Action(description = "查看参数表分页列表", detail = "查看参数表分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		@SuppressWarnings("rawtypes")
		String type = request.getParameter("type");
		QueryFilter queryFilter = new QueryFilter(request,"sysParameterItem");
		
		List<Map<String, Object>> typeList = this.sysParameterService.getType();
		if(type!=null){
			queryFilter.addFilterForH("type", type);
		}
		List list = this.sysParameterService.getAll(queryFilter);
		ModelAndView mv = getAutoView()
				.addObject("sysParameterList", list)
				.addObject("type", type)
				.addObject("typeMap", typeList);
		return mv;
	}
	
	@RequestMapping( { "manage" })
	@Action(description = "参数分类显示", detail = "参数分类显示", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView manage(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<Map<String, Object>> typeList = this.sysParameterService.getType();
		ModelAndView mv = getAutoView().addObject("types", JSONArray.fromObject(typeList).toString());
		return mv;
	}
	
	@RequestMapping({"del"})
	@Action(description="删除参数表", execOrder = ActionExecOrder.BEFORE, detail = "删除参数<#list id?split(\",\") as item><#assign entity=sysParameterService.getById(Long.valueOf(item))/> ${entity.paramname}【${entity.id}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try
		{
			Long[] ids=RequestUtil.getLongAryByStr(request, "id");
			this.sysParameterService.delByIds(ids);
			message=new ResultMessage(1,"删除成功");
		}
		catch (Exception ex)
		{
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑参数表", detail = "编辑参数表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id", 0L));
		String type = RequestUtil.getString(request, "type");
		String returnUrl = RequestUtil.getPrePage(request);
		SysParameter sysParameter = (SysParameter) this.sysParameterService
				.getById(id);

		return getAutoView().addObject("sysParameter", sysParameter)
				.addObject("returnUrl", returnUrl)
				.addObject("type", type);
	}

	@RequestMapping( { "setCategory" })
	@Action(description = "设置系统参数分类", execOrder = ActionExecOrder.AFTER, detail = "设置参数<#list parameterIds?split(\",\") as item><#assign entity=sysParameterService.getById(Long.valueOf(item))/>${entity.paramname}【${entity.id}】 </#list> 分类为:${type} ", exectype = SysAuditExecType.UPDATE_TYPE)
	public void setCategory(HttpServletRequest request,HttpServletResponse response) throws Exception {
		PrintWriter writer = response.getWriter();
		String parameterIds = request.getParameter("parameterIds");
		String type = RequestUtil.getString(request, "type");
		String[] aryParameterId = parameterIds.split(",");

		if (type == null) {
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,"没有选择分类"));
			return;
		}
		
		List<Long> list = new ArrayList<Long>();
		
		for (String parameterId : aryParameterId) {
			list.add(Long.parseLong(parameterId));
		}
		try {
			sysParameterService.updCategory(type, list);
			//更新后再读取新的分类
			List<Map<String, Object>> typeList = this.sysParameterService.getType();
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success,"设置分类成功");
			resultMessage.addData("types", JSONArray.fromObject(typeList).toString());
			writeResultMessage(writer, resultMessage);
			
			LogThreadLocalHolder.putParamerter("parameterIds", parameterIds);
			LogThreadLocalHolder.putParamerter("type", type);
		} catch (Exception ex) {
			String msg = ExceptionUtil.getExceptionMessage(ex);
			writeResultMessage(writer, new ResultMessage(ResultMessage.Fail,msg));
		}
	}
	
	@RequestMapping( { "get" })
	@Action(description = "查看参数表明细",detail="查看参数表明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysParameter sysParameter = (SysParameter) this.sysParameterService
				.getById(id);
		return getAutoView().addObject("sysParameter", sysParameter);
	}
}
