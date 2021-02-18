package com.cssrc.ibms.core.log.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.model.QuartzLog;
import com.cssrc.ibms.core.log.service.QuartzLogService;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 定时任务日志-控制器类 zx
 * */

@Controller
@RequestMapping("/oa/system/quartzLog")
public class QuartzLogController extends BaseController {

	@Resource
	private QuartzLogService quartzLogService;

	/**
	 * 定时任务日志列表
	 * 
	 * @throws Exception
	 * */
	@RequestMapping("list")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<QuartzLog> list = quartzLogService.getAll(new QueryFilter(request,
				"quartzLog"));
		ModelAndView mv = this.getAutoView().addObject("list", list);
		return mv;
	}

}
