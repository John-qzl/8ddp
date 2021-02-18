package com.cssrc.ibms.report.client.servlet;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
@WebServlet(name="reportClient",urlPatterns="/frreport")
public class FrReportClient extends HttpServlet{


	@Override
	protected void doGet(HttpServletRequest request,HttpServletResponse response) {
		String filename=request.getParameter("reportTitle");
		String path=this.getClass().getClassLoader().getResource("/").getFile(); 
		
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp){
		
		
	}

}
