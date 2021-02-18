package com.cssrc.ibms.system.controller;
 
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.web.controller.BaseController;
 
/**
 * SysDataSourceMonitorController数据源监控
 * @author liubo
 * @date 2017年8月30日下午2:23:56
 */
@Controller
@RequestMapping({"/oa/system/sysDataSourceMonitor/"})
@Action(ownermodel = SysAuditModelType.DATASOURCE_ASSIST)
public class SysDataSourceMonitorController extends BaseController {
 
	@RequestMapping({"show"})
	@Action(description="查看数据源监控信息",detail="查看数据源监控信息",exectype=SysAuditExecType.SELECT_TYPE)
	public ModelAndView show(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		ModelAndView mv = getAutoView();
		return mv;
	}
 
}
