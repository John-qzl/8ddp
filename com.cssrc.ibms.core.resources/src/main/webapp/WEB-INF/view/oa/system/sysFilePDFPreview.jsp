<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="com.zhuozhengsoft.pageoffice.PDFCtrl"%>
<%@ page import="java.awt.*" %>
<%@ taglib uri="http://java.pageoffice.cn" prefix="po" %>
<%
    String contextPath = request.getContextPath();
	String filePath = (String)request.getAttribute("filePath");
    System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>打开文档路径："+filePath);
    
	PDFCtrl pdfCtrl = new PDFCtrl(request);
	pdfCtrl.setServerPage(contextPath+"/poserver.do"); //设置服务器页面
	pdfCtrl.setTitlebar(false);
	pdfCtrl.setMenubar(false);
	
	//添加自定义按钮
	pdfCtrl.addCustomToolButton("打印", "Print()", 6);
	pdfCtrl.addCustomToolButton("-", "", 0);
	pdfCtrl.addCustomToolButton("实际大小", "SetPageReal()", 16);
	pdfCtrl.addCustomToolButton("适合页面", "SetPageFit()", 17);
	pdfCtrl.addCustomToolButton("适合宽度", "SetPageWidth()", 18);
	pdfCtrl.addCustomToolButton("-", "", 0);
	pdfCtrl.addCustomToolButton("首页", "FirstPage()", 8);
	pdfCtrl.addCustomToolButton("上一页", "PreviousPage()", 9);
	pdfCtrl.addCustomToolButton("下一页", "NextPage()", 10);
	pdfCtrl.addCustomToolButton("尾页", "LastPage()", 11);
	pdfCtrl.addCustomToolButton("-", "", 0);
	pdfCtrl.addCustomToolButton("左转", "RotateLeft()", 12);
	pdfCtrl.addCustomToolButton("右转", "RotateRight()", 13);
	pdfCtrl.addCustomToolButton("-", "", 0);
	pdfCtrl.addCustomToolButton("放大", "ZoomIn()", 14);
	pdfCtrl.addCustomToolButton("缩小", "ZoomOut()", 15);
	pdfCtrl.addCustomToolButton("-", "", 0);
	pdfCtrl.addCustomToolButton("全屏/还原", "SwitchFullScreen()", 4);
	//设置禁止拷贝
	pdfCtrl.setAllowCopy(false);
	pdfCtrl.webOpen(filePath);
	pdfCtrl.setTagId("PDFCtrl1");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=GBK">
<title>Insert title here</title>
<script type="text/javascript">

	function Print() {
		document.getElementById("PDFCtrl1").ShowDialog(4);
	}
	function SwitchFullScreen() {
		document.getElementById("PDFCtrl1").FullScreen = !document
				.getElementById("PDFCtrl1").FullScreen;
	}
	function SetPageReal() {
		document.getElementById("PDFCtrl1").SetPageFit(1);
	}
	function SetPageFit() {
		document.getElementById("PDFCtrl1").SetPageFit(2);
	}
	function SetPageWidth() {
		document.getElementById("PDFCtrl1").SetPageFit(3);
	}
	function ZoomIn() {
		document.getElementById("PDFCtrl1").ZoomIn();
	}
	function ZoomOut() {
		document.getElementById("PDFCtrl1").ZoomOut();
	}
	function FirstPage() {
		document.getElementById("PDFCtrl1").GoToFirstPage();
	}
	function PreviousPage() {
		document.getElementById("PDFCtrl1").GoToPreviousPage();
	}
	function NextPage() {
		document.getElementById("PDFCtrl1").GoToNextPage();
	}
	function LastPage() {
		document.getElementById("PDFCtrl1").GoToLastPage();
	}
	function RotateRight() {
		document.getElementById("PDFCtrl1").RotateRight();
	}
	function RotateLeft() {
		document.getElementById("PDFCtrl1").RotateLeft();
	}
</script>
</head>
<body>
<po:PDFCtrl id="PDFCtrl1"/>
</body>
</html>