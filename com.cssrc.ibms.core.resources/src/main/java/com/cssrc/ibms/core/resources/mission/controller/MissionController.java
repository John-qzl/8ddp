package com.cssrc.ibms.core.resources.mission.controller;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.resources.mission.service.MissionService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * @description 任务控制器类
 * @author zmz
 *
 */
@Controller
@RequestMapping("/mission/")
public class MissionController extends BaseController{
	@Resource
	private MissionService service;
	/**
	 * excel导入任务
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping("importView")
	public ModelAndView ImportView(HttpServletRequest request, HttpServletResponse response){
		return new ModelAndView("/mission/missionImport.jsp").addObject("moduleId", request.getParameter("moduleId"));
	}
	
	/**
	 * 解析导入的excel文件
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("importMission")
	@Action(description = "导入任务", detail = "导入任务")
	public void importMission(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartFile file = request.getFile("file");
        String moduleId = request.getParameter("moduleId");
        ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "导入成功");
        try {
            resultMessage = service.importMissions(file,moduleId);
        } catch (Exception e) {
            resultMessage = new ResultMessage(ResultMessage.Fail, "导入失败：\r\n"+e.getMessage());
            System.err.println("--------------------by xiechen-----------------导入失败的错误信息为:"
                    +e.getCause().getMessage()+ "," + "当前输出的所在类为:ProductCategoryBatchController.importCategories()");
            writeResultMessage(response.getWriter(), resultMessage);
        }
        writeResultMessage(response.getWriter(), resultMessage);
        //测试idea的git功能
	}
}
