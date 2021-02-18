package com.cssrc.ibms.core.log.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.log.model.SysErrorLog;
import com.cssrc.ibms.core.log.service.SysErrorLogService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditExecType;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * 操作错误日志Controller层
 * @author Yangbo 2016-7-26
 *
 */
@Controller
@RequestMapping( { "/oa/system/sysErrorLog/" })
@Action(ownermodel = SysAuditModelType.LOG_ERROR_MANAGEMENT)
public class SysErrorLogController extends BaseController {

	@Resource
	private SysErrorLogService sysErrorLogService;

	@RequestMapping( { "list" })
	@Action(description = "查看系统错误日志分页列表",detail="查看系统错误日志分页列表", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List list = this.sysErrorLogService.getAll(new QueryFilter(request,
				"sysErrorLogItem"));
		ModelAndView mv = getAutoView().addObject("sysErrorLogList", list);

		return mv;
	}

	@RequestMapping( { "del" })
	@Action(description = "删除系统错误日志", execOrder = ActionExecOrder.BEFORE, detail = "删除系统附件<#list StringUtils.split(id,\",\") as item><#assign entity=sysErrorLogService.getById(Long.valueOf(item))/>【${entity.hashcode}】</#list>", exectype = SysAuditExecType.DELETE_TYPE)
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.sysErrorLogService.delByIds(lAryId);
			message = new ResultMessage(1, "删除系统错误日志成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "get" })
	@Action(description = "查看系统错误日志明细", detail="查看系统错误日志明细", exectype = SysAuditExecType.SELECT_TYPE)
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysErrorLog sysErrorLog = (SysErrorLog) this.sysErrorLogService
				.getById(Long.valueOf(id));
		return getAutoView().addObject("sysErrorLog", sysErrorLog);
	}

	@RequestMapping( { "geterror" })
	@ResponseBody
	public String geterror(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		long id = RequestUtil.getLong(request, "id");
		SysErrorLog sysErrorLog = (SysErrorLog) this.sysErrorLogService
				.getById(Long.valueOf(id));
		JSONObject jObject = JSONObject.fromObject(sysErrorLog);
		return jObject.toString();
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
    @Action(description = "导出备份系统所有错误日志记录", execOrder = ActionExecOrder.AFTER, detail = "导出(备份)系统所有错误日志记录，文件名为\"${fileName}\"", exectype = SysAuditExecType.EXPORT_TYPE)
    public void exportData(HttpServletRequest request, HttpServletResponse response) throws Exception {	
    	//设置操作结果，默认为操作失败
		Short result = 0;
		try {
			QueryFilter filter = new QueryFilter(request,"sysErrorLogItem");
	    	filter.setPagingBean(null);
			List<SysErrorLog> list = this.sysErrorLogService.getAll(filter);
			
			// 以下开始写excel
			// 创建新的Excel 工作簿
			HSSFWorkbook workbook = new HSSFWorkbook();
			HSSFSheet sheet = workbook.createSheet("系统错误日志");
			//设置宽度
			sheet.setColumnWidth(0, 3500);
			sheet.setColumnWidth(1, 3500);
			sheet.setColumnWidth(2, 4000);
			sheet.setColumnWidth(3, 12000);
			sheet.setColumnWidth(4, 5000);
			sheet.setColumnWidth(5, 20000);
			sheet.setColumnWidth(6, 3500);
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
			
			dataRow.createCell(0).setCellValue("用户名");
			dataRow.createCell(1).setCellValue("用户账户");
			dataRow.createCell(2).setCellValue("IP地址");
			dataRow.createCell(3).setCellValue("错误URL");
			dataRow.createCell(4).setCellValue("错误日期");
			dataRow.createCell(5).setCellValue("错误信息");
			dataRow.createCell(6).setCellValue("操作结果");
			//给表头设置样式
			for(int i=0;i<7;i++){
				dataRow.getCell(i).setCellStyle(style);
			}
			
			//插入数据
			for(int index=1; index<list.size(); index++){
				dataRow = sheet.createRow(index);
				dataRow.createCell(0).setCellValue(list.get(index).getName());
				dataRow.createCell(1).setCellValue(list.get(index).getAccount());
				dataRow.createCell(2).setCellValue(list.get(index).getIp());
				dataRow.createCell(3).setCellValue(list.get(index).getErrorurl());
				dataRow.createCell(4).setCellValue(list.get(index).getErrordate().toLocaleString());
				dataRow.createCell(5).setCellValue(list.get(index).getError());
				dataRow.createCell(6).setCellValue("操作失败");
			}
			
			String fileName = "系统错误日志备份_" + DateFormatUtil.getNowByString("yyyyMMddHHmmdd");
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
