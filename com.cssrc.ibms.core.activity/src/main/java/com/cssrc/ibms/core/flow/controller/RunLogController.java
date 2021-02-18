package com.cssrc.ibms.core.flow.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.model.RunLog;
import com.cssrc.ibms.core.flow.service.RunLogService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
/**
 * 对象功能:流程运行日志 控制器类 
 * 开发人员:zhulongchao 
 */
@Controller
@RequestMapping("/oa/flow/runLog/")
@Action(ownermodel=SysAuditModelType.FLOWLOG_MANAGEMENT)
public class RunLogController extends BaseController
{
	@Resource
	private RunLogService runLogService;
	
	/**
	 * 取得流程运行日志分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("list")
	@Action(description="查看流程运行日志分页列表",detail="查看流程运行日志分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Long runId=RequestUtil.getLong(request, "runId");
		QueryFilter filter=new QueryFilter(request,"bpmRunLogItem");
		if(runId>0)filter.addFilterForIB("runid", runId);
		List<RunLog> list=runLogService.getAll(filter);
		ModelAndView mv=this.getAutoView().addObject("bpmRunLogList",list).addObject("runId",runId);		
		return mv;
	}
	
	/**
	 * 取得当前用户的流程运行日志分页列表
	 * @param request
	 * @param response
	 * @param page
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("mylist")
	@Action(description="查看流程运行日志分页列表",detail="查看流程运行日志分页列表",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView mylist(HttpServletRequest request,HttpServletResponse response) throws Exception
	{
		Long userId=UserContextUtil.getCurrentUserId();
		QueryFilter filter=new QueryFilter(request,"bpmRunLogItem");
		filter.addFilterForIB("userid", userId);
		List<RunLog> list=runLogService.getAll(filter);
		ModelAndView mv=this.getAutoView().addObject("bpmRunLogList",list);
		return mv;
	}
	
	/**
	 * 删除流程运行日志
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("del")
	@Action(description="删除流程运行日志",
			execOrder=ActionExecOrder.BEFORE,
	        detail="删除流程<#list StringUtils.split(id,\",\") as item>" +
					  "<#assign entity=bpmRunLogService.getById(Long.valueOf(item))/>" +
					  "【${entity.processSubject}】" +
				   "</#list>的运行日志",
			exectype = SysAuditExecType.DELETE_TYPE
    )
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		String preUrl= RequestUtil.getPrePage(request);
		ResultMessage resultMessage=null;
		try{
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			runLogService.delByIds(lAryId);
			resultMessage = new ResultMessage(ResultMessage.Success, getText("controller.del.success"));
		}
		catch(Exception ex){
			resultMessage = new ResultMessage(ResultMessage.Fail,getText("controller.del.fail")+":" + ex.getMessage());
		}
		addMessage(resultMessage, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 取得流程运行日志明细
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("get")
	@Action(description="查看流程运行日志明细",detail="查看流程运行日志明细",exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception
	{
		long id=RequestUtil.getLong(request,"id");
		long canReturn=RequestUtil.getLong(request,"canReturn",0);
		RunLog bpmRunLog = runLogService.getById(id);		
		return getAutoView().addObject("bpmRunLog", bpmRunLog).addObject("canReturn", canReturn);
	}

    /**
     * 导出数据
     * 
     * @author liubo
     * @param request
     * @param response
     * @throws Exception
     * @throws Exception
     */
    @RequestMapping("exportData")
    @Action(description = "导出流程日志记录", execOrder = ActionExecOrder.AFTER, detail = "导出(备份)系统所有流程日志记录，文件名为\"${fileName}\"", exectype = SysAuditExecType.EXPORT_TYPE)
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {	
		QueryFilter filter=new QueryFilter(request,"bpmRunLogItem");
		//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			filter.setPagingBean(null);
			List<RunLog> list=runLogService.getAll(filter);
			
			// 以下开始写excel
			// 创建新的Excel 工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("系统日志");
			//设置宽度
			sheet.setColumnWidth(0, 13000);
			sheet.setColumnWidth(1, 3500);
			sheet.setColumnWidth(2, 6000);
			sheet.setColumnWidth(3, 3500);
			sheet.setColumnWidth(4, 18000);
			//设置数据行高度
			sheet.setDefaultRowHeight((short) 320);
			
			//设置excel表头
			HSSFRow dataRow = sheet.createRow(0);
			//设置表头样式
			dataRow.setHeight((short) 450);
			//生成一个样式
			HSSFCellStyle style = workbook.createCellStyle();
			style.setFillForegroundColor(HSSFColor.PALE_BLUE.index);//给单元格着色
			style.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			style.setBorderLeft(HSSFCellStyle.BORDER_THIN);
			//生成一个字体
			HSSFFont font = workbook.createFont();
			font.setFontName("黑体");//设置字体
			font.setFontHeightInPoints((short) 14);//设置字号
			font.setBoldweight(HSSFFont.BOLDWEIGHT_NORMAL);//设置字体样式，正常显示
			style.setFont(font);
			
			dataRow.createCell(0).setCellValue("流程标题");
			dataRow.createCell(1).setCellValue("用户名称");
			dataRow.createCell(2).setCellValue("操作时间");
			dataRow.createCell(3).setCellValue("操作类型");
			dataRow.createCell(4).setCellValue("备注");
			//给表头设置样式
			for(int i=0;i<5;i++){
				dataRow.getCell(i).setCellStyle(style);
			}
			
			//插入数据
			for(int index=1; index<list.size(); index++){
				dataRow = sheet.createRow(index);
				dataRow.createCell(0).setCellValue(list.get(index).getProcessSubject());
				dataRow.createCell(1).setCellValue(list.get(index).getUsername());
				dataRow.createCell(2).setCellValue(list.get(index).getCreatetime().toLocaleString());
				dataRow.createCell(3).setCellValue(list.get(index).getOperatortype());
				dataRow.createCell(4).setCellValue(list.get(index).getMemo());
			}
			
			String fileName = "流程日志备份_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
	        downloadExcel(workbook, fileName, response, request);
	        result = 1;
	        LogThreadLocalHolder.putParamerter("fileName", fileName);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		try {
			LogThreadLocalHolder.setResult(result);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			logger.error(e.getMessage());
		}
    }
    
	/**
	 * 下载文件
	 * @author liubo
	 * @param workBook
	 * @param fileName
	 * @param response
	 * @param request
	 * @throws IOException
	 */
	public static void downloadExcel(HSSFWorkbook workBook, String fileName,
			HttpServletResponse response,HttpServletRequest request) throws IOException {
		String userAgent = request.getHeader("USER-AGENT");
		String finalFileName = fileName;
		response.setContentType("application/vnd.ms-excel");
		//判断浏览器类型
		if(userAgent.contains("MSIE")){//IE浏览器
			finalFileName = URLEncoder.encode(fileName, "UTF-8");
		}else if(userAgent.contains("Mozilla")){//google，火狐浏览器
			finalFileName = new String(fileName.getBytes(),"ISO8859-1");
		}else{
			finalFileName = URLEncoder.encode(fileName, "UTF-8");//其他
		}
		response.setHeader("Content-Disposition", "attachment;filename=" + finalFileName +".xls");
		OutputStream os = null;
		try {
			os = response.getOutputStream();
			workBook.write(os);
			os.flush();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (os != null)
				os.close();
		}
	}
}
