package com.cssrc.ibms.migration.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.migration.model.MigrationConfigure;
import com.cssrc.ibms.migration.service.DataMigrationService;

/**
 * Description:
 * <p>DataMigrationController.java</p>
 * @author dengwenjie 
 * @date 2017年7月18日
 */
@Controller
@RequestMapping("/oa/migration/")
public class DataMigrationController  extends BaseController {
	@Resource
	private DataMigrationService dataMigrationService;
	
	
	/**
	 * 业务表结构视图
	 */
	@RequestMapping({"/config"})
	public ModelAndView view(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/oa/migration/tabStructConfig.jsp");
		mv.addObject("typeList",MigrationConfigure.getTypes())
		.addObject("versionList",MigrationConfigure.getVersions(""));
		return mv;
	}
	/**
	 * 业务表结构处理
	 */
	@RequestMapping({"/deal"})
	public void deal(MigrationConfigure config,HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		String log = dataMigrationService.createBusinessTable(config);
		FileUtil.downLoad(request, response,log,"导入日志.txt");
		//response.getWriter().write(log);
	}
	
	/**
	 * 根据导入的xml文件、生成表单模板、表单设计xml；
	 */
	@RequestMapping({"/xml"})
	public ModelAndView xml(HttpServletRequest request,HttpServletResponse response) throws Exception {
		ModelAndView mv = new ModelAndView("/oa/migration/xmlConfig.jsp"); 
		mv.addObject("typeList",MigrationConfigure.getTypes())
		.addObject("versionList",MigrationConfigure.getVersions(""))
		.addObject("xmlTypeList",MigrationConfigure.getXmlTypes());
		return mv;
	}
	/**
	 * 业务表结构处理
	 */
	@RequestMapping({"/creatXml"})
	public void creatXml(MigrationConfigure config,HttpServletRequest request,
			HttpServletResponse response)throws Exception{
		String xml = dataMigrationService.createXml(config);
		if(!xml.contains("ibms")){
			FileUtil.downLoad(request, response,xml,"生成日志.txt");		
		}else{
			FileUtil.downLoad(request, response,xml,config.getXmlName());	
		}
	}
	
}
