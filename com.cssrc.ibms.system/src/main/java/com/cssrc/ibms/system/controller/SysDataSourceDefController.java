package com.cssrc.ibms.system.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.db.datasource.DataSourceUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.json.JSONObjectUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysDataSourceDef;
import com.cssrc.ibms.system.service.SysDataSourceDefService;

/**
 * SysDataSourceDefController
 * @author liubo
 * @date 2017年4月14日
 */
@Controller
@RequestMapping({"/oa/system/sysDataSourceDef/"})
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SysDataSourceDefController extends BaseController{

	@Resource
	private SysDataSourceDefService sysDataSourceDefService;
	@Resource
	private IProcessRunService processRunService;
	
	@RequestMapping({"save"})
	@Action(description="添加或更新SYS_DATA_SOURCE_DEF")
	public void save(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String json = FileUtil.inputStream2String(request.getInputStream());
		SysDataSourceDef sysDataSourceDef = 
				(SysDataSourceDef)JSONObjectUtil.toBean(json, SysDataSourceDef.class);
		try {
			if(sysDataSourceDef.getId() == null){
				sysDataSourceDef.setId(UniqueIdUtil.genId());
				sysDataSourceDefService.add(sysDataSourceDef);
				writeResultMessage(response.getWriter(), "添加成功", 1);
			}else{
				sysDataSourceDefService.update(sysDataSourceDef);
				writeResultMessage(response.getWriter(), "更新成功", 1);
			}
			
			if(sysDataSourceDef.getIsEnabled()==1){
				try {
					DataSourceUtil.addDataSource(sysDataSourceDef.getAlias(), 
							this.sysDataSourceDefService.getDsFromSysSource(sysDataSourceDef));
				} catch (Exception e) {
					// TODO: handle exception
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			writeResultMessage(response.getWriter(), e.getMessage(), 0);
		}
	}
	
	@RequestMapping({"list"})
	@Action(description="查看SYS_DATA_SOURCE_DEF分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mv = getAutoView();
		List list = this.sysDataSourceDefService.getAll(new QueryFilter(request, "sysDataSourceDefItem"));
		mv.addObject("sysDataSourceDefList", list);
		return mv;
	}
	
	@RequestMapping({"del"})
	@Action(description="删除SYS_DATA_SOURCE_DEF")
	public void del(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			if (lAryId.length > 1)
				message = new ResultMessage(1, "删除SYS_DATA_SOURCE成功!");
			else
				message = new ResultMessage(1, "删除" + ((SysDataSourceDef)this.sysDataSourceDefService.getById(lAryId[0])).getName() + "成功!");
			this.sysDataSourceDefService.delByIds(lAryId);
		} catch (Exception e) {
			message = new ResultMessage(0, "删除失败" + e.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}
	
	@RequestMapping({"edit"})
	@Action(description="编辑SYS_DATA_SOURCE_DEF")
	public ModelAndView edit(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mv = getAutoView();
		String returnUrl = RequestUtil.getPrePage(request);
		mv.addObject("returnUrl",returnUrl);
		return mv;
	}
	
	@RequestMapping({"get"})
	@Action(description="查看SYS_DATA_SOURCE_DEF明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception{
		ModelAndView mv = getAutoView();
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		Long runId = Long.valueOf(0L);
		SysDataSourceDef sysDataSourceDef = (SysDataSourceDef)this.sysDataSourceDefService.getById(id);
		IProcessRun processRun = this.processRunService.getByBusinessKey(null,id.toString());
		if (BeanUtils.isNotEmpty(processRun)) {
			runId = processRun.getRunId();
		}
		mv.addObject("sysDataSourceDef",sysDataSourceDef)
		  .addObject("runId", runId);
		return mv;
	}
	
	@RequestMapping({"checkConnection"})
	public void checkConnection(HttpServletRequest request, HttpServletResponse response) throws IOException{
		String json = FileUtil.inputStream2String(request.getInputStream());
		SysDataSourceDef sysDataSourceDef = 
				(SysDataSourceDef)JSONObjectUtil.toBean(json, SysDataSourceDef.class);
		boolean b = false;
		try {
			b = this.sysDataSourceDefService.checkConnection(sysDataSourceDef);
		} catch (Exception e) {
			b = false;
		}
		String resultMsg = "";
		if(b){
			resultMsg = sysDataSourceDef.getName() + ":连接成功";
			writeResultMessage(response.getWriter(), resultMsg, 1);
		}else{
			resultMsg = sysDataSourceDef.getName() + ":连接失败";
			writeResultMessage(response.getWriter(), resultMsg, 0);
		}
	}
	
	@RequestMapping({"getAll"})
	@Action(description="获取所有的数据源")
	@ResponseBody
	public List<SysDataSourceDef> getAll(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List dsList = this.sysDataSourceDefService.getAllAndDefault();
		return dsList;
	}
	
	@RequestMapping({"getById"})
	@Action(description="查看SYS_DATA_SOURCE_DEF明细")
	@ResponseBody
	public SysDataSourceDef getById(HttpServletRequest request, HttpServletResponse response) throws Exception { 
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		SysDataSourceDef sysDataSourceDef = (SysDataSourceDef)this.sysDataSourceDefService.getById(id);
		return sysDataSourceDef;
	}
	
	@RequestMapping({"exportXml"})
	@Action(description="导出系统数据源", detail="导出系统数据源:<#list StringUtils.split(tableIds,\",\") as item><#assign entity=bpmFormTableService.getById(Long.valueOf(item))/>【${entity.tableDesc}(${entity.tableName})】</#list>")
	public void exportXml(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String strXml = "";
		String fileName = "";
		Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");

		if (BeanUtils.isEmpty(tableIds)) {
			List list = this.sysDataSourceDefService.getAll();
			if (BeanUtils.isNotEmpty(list)) {
				strXml = this.sysDataSourceDefService.exportXml(list);
				fileName = "全部数据源_" + DateFormatUtil.getNowByString("yyyyMMddHHmm") + ".xml";
			}
		} else {
			strXml = this.sysDataSourceDefService.exportXml(tableIds);
			fileName = DateFormatUtil.getNowByString("yyyyMMddHHmm") + ".xml";
			if (tableIds.length == 1) {
				SysDataSourceDef sysDataSourceDef = (SysDataSourceDef)this.sysDataSourceDefService.getById(tableIds[0]);
				fileName = sysDataSourceDef.getName() + "_" + fileName;
			} else {
				fileName = "数据源_" + fileName;
			}
		}
		FileUtil.downLoad(request, response, strXml, fileName);
	}
	
	@RequestMapping({"importXml"})
	@Action(description="导入系统数据源")
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			this.sysDataSourceDefService.importXml(fileLoad.getInputStream());
			message = new ResultMessage(1, MsgUtil.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(0, "导入文件异常，请检查文件格式！");
		}
		writeResultMessage(response.getWriter(), message);
	}
}
