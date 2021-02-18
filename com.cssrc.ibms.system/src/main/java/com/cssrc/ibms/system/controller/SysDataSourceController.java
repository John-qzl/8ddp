package com.cssrc.ibms.system.controller;
 
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.datasource.DataSourceUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysDataSource;
import com.cssrc.ibms.system.service.SysDataSourceService;
 
/**
 * SysDataSourceController
 * @author liubo
 * @date 2017年2月16日
 */
@Controller
@RequestMapping({"/oa/system/sysDataSource/"})
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysDataSourceController extends BaseController {
 
	@Resource
	private SysDataSourceService sysDataSourceService;
 
	@Resource
	private IProcessRunService processRunService;
 
	@RequestMapping({"save"})
	@Action(description="添加或更新SYS_DATA_SOURCE")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String json = FileUtil.inputStream2String(request.getInputStream());
		SysDataSource sysDataSource = (SysDataSource)JSONObjectUtil.toBean(json, SysDataSource.class);
		try {
			if (sysDataSource.getId() == null) {
				sysDataSource.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.sysDataSourceService.add(sysDataSource);
				writeResultMessage(response.getWriter(), "添加成功", 1);
			} else {
				this.sysDataSourceService.update(sysDataSource);
				writeResultMessage(response.getWriter(), "更新成功", 1);
			}
 
			if (sysDataSource.getEnabled().booleanValue())
				try {
					DataSourceUtil.addDataSource(sysDataSource.getAlias(), this.sysDataSourceService.getDsFromSysSource(sysDataSource));
				} catch (Exception localException1) {
				}
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), e.getMessage(), 0);
		}
	}
 
	@RequestMapping({"list"})
	@Action(description="查看SYS_DATA_SOURCE分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List list = this.sysDataSourceService.getAll(new QueryFilter(request, "sysDataSourceItem"));
		ModelAndView mv = getAutoView().addObject("sysDataSourceList", list);
		return mv;
	}
 
	@RequestMapping({"del"})
	@Action(description="删除SYS_DATA_SOURCE")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			if (lAryId.length > 1)
				message = new ResultMessage(1, "删除SYS_DATA_SOURCE成功!");
			else {
				message = new ResultMessage(1, "删除" + ((SysDataSource)this.sysDataSourceService.getById(lAryId[0])).getName() + "成功!");
			}
			this.sysDataSourceService.delByIds(lAryId);
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
 
	@RequestMapping({"edit"})
	@Action(description="编辑SYS_DATA_SOURCE")
	public ModelAndView edit(HttpServletRequest request)
			throws Exception {
		String returnUrl = RequestUtil.getPrePage(request);
		return getAutoView().addObject("returnUrl", returnUrl);
	}
 
	@RequestMapping({"get"})
	@Action(description="查看SYS_DATA_SOURCE明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysDataSource sysDataSource = (SysDataSource)this.sysDataSourceService.getById(id);
		Long runId = Long.valueOf(0L);
		IProcessRun processRun = this.processRunService.getByBusinessKey(null,id.toString());
		if (BeanUtils.isNotEmpty(processRun)) {
			runId = processRun.getRunId();
		}
		return getAutoView().addObject("sysDataSource", sysDataSource).addObject("runId", runId);
	} 
	
	@RequestMapping({"getById"})
	@Action(description="查看SYS_DATA_SOURCE明细")
	@ResponseBody
	public SysDataSource getById(HttpServletRequest request, HttpServletResponse response) 
			throws Exception { 
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysDataSource sysDataSource = (SysDataSource)this.sysDataSourceService.getById(id);
		return sysDataSource;
	}
  
	@RequestMapping({"checkConnection"})
	@Action(description="检验连接情况")
	public void checkConnection(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String json = FileUtil.inputStream2String(request.getInputStream());
		SysDataSource sysDataSource = (SysDataSource)JSONObjectUtil.toBean(json, SysDataSource.class);
		boolean b = false;
		try {
			b = this.sysDataSourceService.checkConnection(sysDataSource);
		} catch (Exception e) {
			b = false;
		}
		String resultMsg = "";
		if (b) {
			resultMsg = sysDataSource.getName() + ":连接成功";
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} else {
			resultMsg = sysDataSource.getName() + ":连接失败";
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}
	}
 
	@RequestMapping({"getAll"})
	@Action(description="获取系统所有的数据源（包括 本地数据源）")
	@ResponseBody
	public List<SysDataSource> getAll(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		List dsList = this.sysDataSourceService.getAllAndDefault();
		return dsList;
	}
}
