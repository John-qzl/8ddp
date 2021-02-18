package com.cssrc.ibms.core.log.controller;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.model.SysLog;
import com.cssrc.ibms.core.log.service.SysLogService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * 系统日志 aop日志切面
 * <p>Title:SysLogController</p>
 * @author Yangbo 
 * @date 2016年9月6日下午3:42:23
 */
@Controller
@RequestMapping({"/oa/system/sysLog/"})
@Action(ownermodel=SysAuditModelType.LOG_SYS_MANAGEMENT)
public class SysLogController extends BaseController
{

	@Resource
	private SysLogService sysLogService;
	@Resource
	private ISysUserService sysUserService;

	@RequestMapping({"list"})
	@Action(description = "查看系统日志分页列表",detail="查看系统日志分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{		
		Long curUserId=UserContextUtil.getCurrentUserId();
		//三元用户的日志区分
		QueryFilter filter=new QueryFilter(request, "sysLogItem");
		List<SysLog> list=this.sysLogService.getLogListByRight(filter,curUserId);
		ModelAndView mv = getAutoView().addObject("sysLogList", list);

		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除系统日志", execOrder=ActionExecOrder.BEFORE, detail="删除系统附件<#list StringUtils.split(auditId,\",\") as item><#assign entity=sysLogService.getById(Long.valueOf(item))/>【${entity.opName}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		ResultMessage message = null;
		String preUrl = RequestUtil.getPrePage(request);
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "auditId");
			this.sysLogService.delByIds(lAryId);
			message = new ResultMessage(1, "删除系统日志成功");
		} catch (Exception e) {
			message = new ResultMessage(0, "删除系统日志失败");
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	public void add(SysLog sysLog)
			throws Exception
	{
		sysLog.setAuditId(Long.valueOf(UniqueIdUtil.genId()));
		this.sysLogService.add(sysLog);
	}

	public void upd(SysLog sysLog)
			throws Exception
	{
		this.sysLogService.update(sysLog);
	}

	@RequestMapping({"edit"})
	public ModelAndView edit(HttpServletRequest request, @RequestParam("auditId") Long auditId) throws Exception
	{
		String returnUrl = RequestUtil.getPrePage(request);
		SysLog po = null;
		if (auditId != null)
			po = (SysLog)this.sysLogService.getById(auditId);
		else {
			po = new SysLog();
		}
		return getAutoView().addObject("sysLog", po).addObject("returnUrl", returnUrl);
	}

	@RequestMapping({"get"})
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
			throws Exception
	{
		long id = RequestUtil.getLong(request, "auditId");
		SysLog po = (SysLog)this.sysLogService.getById(Long.valueOf(id));
		ISysUser sysUser = sysUserService.getById(po.getExecutorId());
		String executorFullname = null;
		if(sysUser!=null)
			executorFullname = sysUser.getFullname();
		return getAutoView().addObject("sysLog", po).addObject("executorFullname",executorFullname);
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
    @Action(description = "导出系统日志记录", execOrder = ActionExecOrder.AFTER, detail = "导出(备份)系统所有日志记录，文件名为\"${fileName}\"", exectype = SysAuditExecType.EXPORT_TYPE)
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {	
    	Long curUserId=UserContextUtil.getCurrentUserId();
    	//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			//三元用户的日志区分
			QueryFilter filter=new QueryFilter(request, "sysLogItem");
			filter.setPagingBean(null);;
			List<SysLog> list=this.sysLogService.getLogListByRight(filter,curUserId);
			
			// 以下开始写excel
			// 创建新的Excel 工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("系统日志");
			//设置宽度
			sheet.setColumnWidth(0, 10000);
			sheet.setColumnWidth(1, 3000);
			sheet.setColumnWidth(2, 3000);
			sheet.setColumnWidth(3, 4000);
			sheet.setColumnWidth(4, 6000);
			sheet.setColumnWidth(5, 3500);
			sheet.setColumnWidth(6, 3500);
			sheet.setColumnWidth(7, 20000);
			sheet.setColumnWidth(8, 3500);
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
			
			dataRow.createCell(0).setCellValue("操作名称");
			dataRow.createCell(2).setCellValue("执行人姓名");
			dataRow.createCell(3).setCellValue("执行人账户");
			dataRow.createCell(4).setCellValue("执行人IP");
			dataRow.createCell(1).setCellValue("执行时间");
			dataRow.createCell(5).setCellValue("归属模块");
			dataRow.createCell(6).setCellValue("日志类型");
			dataRow.createCell(7).setCellValue("明细");
			dataRow.createCell(8).setCellValue("操作结果");
			//给表头设置样式
			for(int i=0;i<9;i++){
				dataRow.getCell(i).setCellStyle(style);
			}
			
			//插入数据
			for(int index=1; index<list.size(); index++){
				dataRow = sheet.createRow(index);
				dataRow.createCell(0).setCellValue(list.get(index).getOpName());
				dataRow.createCell(1).setCellValue(list.get(index).getExecutorName());
				dataRow.createCell(2).setCellValue(list.get(index).getExecutor());
				dataRow.createCell(3).setCellValue(list.get(index).getFromIp());
				dataRow.createCell(4).setCellValue(list.get(index).getOpTime().toLocaleString());
				dataRow.createCell(5).setCellValue(list.get(index).getOwnermodel());
				dataRow.createCell(6).setCellValue(list.get(index).getExectype());
				dataRow.createCell(7).setCellValue(list.get(index).getDetail());
				
				String optResult = "操作成功";
				if(list.get(index).getResult() == 0)
					optResult = "操作失败";
				
				dataRow.createCell(8).setCellValue(optResult);
			}
			
			String fileName = "系统日志备份_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
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