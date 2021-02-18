package com.cssrc.ibms.reportclient;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.cssrc.ibms.reportutil.FileUtil;
import com.fr.base.FRContext;
import com.fr.dav.LocalEnv;
import com.fr.io.TemplateWorkBookIO;
import com.fr.io.exporter.PDFExporter;
import com.fr.main.impl.WorkBook;
import com.fr.stable.WriteActor;

@WebServlet(name = "getReportServlet", urlPatterns = "/getReport")
public class ReadFileController extends HttpServlet {
	private static final long serialVersionUID = -1288845041440597199L;

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream outp = response.getOutputStream();
		FileInputStream in = null;
		File tempfile=null;
		try {
			tempfile = this.export(request, response);
			// 读取临时文件
			byte[] b = FileUtil.readByte(tempfile);
			outp.write(b);
			outp.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			response.flushBuffer();
			if (in != null) {
				in.close();
				in = null;
			}
			if (outp != null) {
				outp.close();
				outp = null;
			}
			if(tempfile!=null){
				tempfile.delete();
			}
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		doGet(req, resp);
	}

	/**
	 * 根据报表模板生成临时报表文件
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	public File export(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		// 报表模板相对路径
		String reportlet = request.getParameter("reportlet");
		// 报表模板参数
		Enumeration<String> parameterNames = request.getParameterNames();
		Map<String,String> params=this.formatParam(parameterNames, request);
		
		//获取文件名
		String reportletName = FileUtil.getFileName(reportlet);

		// 定义报表运行环境
		String envpath = request.getServletContext().getRealPath("/") + "WEB-INF";
		FRContext.setCurrentEnv(new LocalEnv(envpath));

		// 生成临时文件
		File tempF = FileUtil.generateTempFile(envpath,reportletName);
		FileOutputStream outputStream = new FileOutputStream(tempF);
		try {
			// 读取报表模板文件
			WorkBook workbook = (WorkBook) TemplateWorkBookIO
					.readTemplateWorkBook(FRContext.getCurrentEnv(),reportlet);
			// 定义生成的报表文件格式
			PDFExporter pdfExport = new PDFExporter();
			// 将生成的报表文件写入临时文件中
			try{
				pdfExport.export(outputStream,
						workbook.execute(params, new WriteActor()));
				return tempF;
			}catch(Exception e){
				tempF.deleteOnExit();
				e.printStackTrace();
				return tempF;
			}
			
		} catch (Exception e) {
			tempF.deleteOnExit();
			e.printStackTrace();
			return tempF;
		} finally {
			if (outputStream != null) {
				outputStream.close();
			}
		}

	}
	
	private Map<String,String> formatParam(Enumeration<String> parameterNames,HttpServletRequest request){
		 Map<String,String> params=new HashMap<String,String>();
		while(parameterNames.hasMoreElements()){
			String paramName=(String)parameterNames.nextElement();
			if(!"reportlet".equals(paramName)){
				params.put(paramName, request.getParameter(paramName));
			}
		}
		return params;
	}
}
