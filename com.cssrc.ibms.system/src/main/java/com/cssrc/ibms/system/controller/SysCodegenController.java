package com.cssrc.ibms.system.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.util.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormFieldService;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.core.engine.FreemarkEngine;
import com.cssrc.ibms.core.util.file.FileOperator;
import com.cssrc.ibms.core.util.file.ZipUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.SysCodeTemplate;
import com.cssrc.ibms.system.service.SysCodeTemplateService;
/**
 * 对象功能:基于自定义表的代码生成器 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/system/sysCodegen/")
public class SysCodegenController  extends BaseController  {
	@Resource
	private IFormDefService formDefService;
	@Resource
	private SysCodeTemplateService sysCodeTemplateService;
	@Resource
	private IFormFieldService formFieldService;
	@Resource
	private IFormTableService formTableService;
	@Resource 
	private IFormHandlerService formHandlerService;
	@Resource
	private FreemarkEngine freemarkEngine;
	
	private static final String BASE_PATH = FileOperator.getRootPath() + File.separator + "codegen";
	
	/**
	 * 获取所有自定义表数据
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@ResponseBody
	@RequestMapping("getTableData")
	public List<?extends IFormDef> getTableData(HttpServletRequest request, HttpServletResponse response)throws Exception{
		String subject=RequestUtil.getString(request, "subject","");
		List<?extends IFormDef> formDefList=formDefService.getAllPublished(subject);
		return formDefList;
	}
	
	/**
	 * 代码模板文件列表
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("detail")
	public ModelAndView genDetail(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<SysCodeTemplate> templateList=sysCodeTemplateService.getAll();
		return getAutoView().addObject("templateList",templateList);
	}
	
	/**
	 * 生成代码
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("codegen")
	public void codegen(HttpServletRequest request, HttpServletResponse response) throws Exception {
		//文件相关参数
		String[] templateIds=request.getParameterValues("templateId");
		int override=RequestUtil.getInt(request, "override");
		String flowKey=RequestUtil.getString(request, "defKey");
		int isZip=RequestUtil.getInt(request, "isZip");
		String folderPath=RequestUtil.getString(request, "folderPath");
		String[] formDefIds=RequestUtil.getString(request, "formDefIds").split(",");
		//自定义表相关
		String basePath=RequestUtil.getString(request, "baseDir");
		String system=RequestUtil.getString(request, "system");
		List<String> codeFiles=new ArrayList<String>(); 
		List<?extends IFormTable>list=getTableModels(request);
		try {
			for(String formDefId:formDefIds){
				IFormDef bpmFormDef=formDefService.getById(Long.parseLong(formDefId));
				List<IFormTable> tables=new ArrayList<IFormTable>();
				Long tableId=bpmFormDef.getTableId();
				for(IFormTable model:list){
					if(model.getTableId().equals(tableId)||model.getMainTableId().equals(tableId)){
						tables.add(model);
					}
				}
				List<String> fileList=genCode(basePath,system,tables,templateIds, override,flowKey,bpmFormDef);
				codeFiles.addAll(fileList);
			}
			//压缩生成文件到本地
			if(isZip==1){
				String toDir=folderPath+File.separator+"codegen";
				for(String filePath:codeFiles){
					FileOperator.createFolderFile(toDir+File.separator+filePath);
					FileOperator.copyFile(basePath+File.separator+filePath,toDir+File.separator+filePath);
				}
				ZipUtil.zip(toDir, true);
			}
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Success, getText("controller.sysCodegen.generate.success")));
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), new ResultMessage(ResultMessage.Fail, getText("controller.sysCodegen.generate.fail")+":"+e.getMessage()));
		}
	}
	
	@RequestMapping( { "codegenZip" })
	public void codegenZip(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String[] templateIds = request.getParameterValues("templateId");
		String flowKey = RequestUtil.getString(request, "defKey");
		String[] formDefIds = RequestUtil.getString(request, "formDefIds")
				.split(",");

		String basePath = BASE_PATH + File.separator + "codegen";
		String basePathZip = BASE_PATH + File.separator + "codegen.zip";
		String system = RequestUtil.getString(request, "system");
		List<?extends IFormTable> list = getTableModels(request);
		try {
			for (String formDefId : formDefIds) {
				IFormDef bpmFormDef = formDefService.getById(Long.valueOf(Long
						.parseLong(formDefId)));
				List<IFormTable> tables = new ArrayList<IFormTable>();
				Long tableId = bpmFormDef.getTableId();
				for (IFormTable model : list) {
					if ((model.getTableId().equals(tableId))
							|| (model.getMainTableId().equals(tableId))) {
						tables.add(model);
					}
				}

				genCode(basePath, system, tables, templateIds, 1, flowKey,
						bpmFormDef);
			}

			FileOperator.deleteFile(basePathZip);

			ZipUtil.zip(basePath, Boolean.valueOf(true));

			writeResultMessage(response.getWriter(), new ResultMessage(1,
					"自定义表生成代码成功"));
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), new ResultMessage(0,
					"自定义表生成代码失败:" + e.getMessage()));
		}
	}

	@RequestMapping( { "downLoadZip" })
	public void downLoadZip(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		try {
			String basePathZip = BASE_PATH + File.separator + "codegen.zip";
			File file = new File(basePathZip);
			InputStream in = new FileInputStream(file);
			String fileName = "codegen.zip";
			response.setContentType("application/x-download");
			if (System.getProperty("file.encoding").equals("GBK"))
				response.setHeader("Content-Disposition",
						"attachment;filename=\""
								+ new String(fileName.getBytes(), "ISO-8859-1")
								+ "\"");
			else {
				response.setHeader("Content-Disposition",
						"attachment;filename=\""
								+ URLEncoder.encode(fileName, "utf-8") + "\"");
			}
			ServletOutputStream out = response.getOutputStream();
			IOUtils.copy(in, out);
			if (in != null)
				in.close();
			if (out != null)
				out.close();
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), new ResultMessage(0,
					"文件不存在，请重新再生成一次!:" + e.getMessage()));
		}
	}
	/**
	 * 根据前端所配置 获得表信息列表
	 * @param request
	 * @return
	 */
	private List<?extends IFormTable> getTableModels(HttpServletRequest request){
		String[] tableIds=request.getParameterValues("tableId");
		String[] classNames=request.getParameterValues("className");
		String[] classVars=request.getParameterValues("classVar");
		String[] packageNames=request.getParameterValues("packageName");
		String system=request.getParameter("system");
		String[] formDefIds=RequestUtil.getString(request, "formDefIds").split(",");
		return formTableService.getFormtableList(tableIds,classNames,classVars,packageNames,system,formDefIds);
	}
	
	private List<String> genCode(String basePath,String system,List<?extends IFormTable> tables, String[] templateIds,int override,String flowKey,IFormDef bpmFormDef) throws Exception {
		List<String> fileList=new ArrayList<String>();
		for(int i=0;i<templateIds.length;i++){
			Long templateId=Long.parseLong(templateIds[i]);
			SysCodeTemplate template=sysCodeTemplateService.getById(templateId);
			for(IFormTable table:tables){
				Map<String,String> variables=table.getVariable();
				variables.put("system", system);
				String fileName=template.getFileName();
				String fileDir=template.getFileDir();
				String path=basePath+File.separator+template.getFileDir();
				int isSub=template.getIsSub();
				Map<String,Object> model=new HashMap<String, Object>();
				if(template.getFormEdit()==1){
					model.put("html", this.formTableService.getFormHtml(bpmFormDef,table,true));
				}
				if(template.getFormDetail()==1){
					model.put("html", formTableService.getFormHtml(bpmFormDef,table,false));
				}
				model.put("table", table);
				model.put("system", system);
				if(StringUtil.isNotEmpty(flowKey)){
					model.put("flowKey", flowKey);
				}
				if(table.getIsMain()!=1){
					if(isSub==0){
						continue;
					}
				}
				String templateStr=template.getHtml();
				FreemarkEngine freemarkEngine=new FreemarkEngine();
				String html=freemarkEngine.parseByStringTemplate(model,templateStr);
				String fileStr=path+File.separator+fileName;
				String filePath=StringUtil.replaceVariable(fileStr, variables);
				addFile(filePath,html,override);
				String relativePath=StringUtil.replaceVariable(fileDir+File.separator+fileName, variables);
				fileList.add(relativePath);
			}
		}
		return fileList;
	}
	private void addFile(String filePath, String html, int override) {
		File newFile=new File(filePath);
		if(newFile.exists()){
			if(override==1){
				FileOperator.deleteFile(filePath);
				FileOperator.writeFile(filePath, html);
			}
		}else{
			FileOperator.writeFile(filePath, html);
		}
	}

}
