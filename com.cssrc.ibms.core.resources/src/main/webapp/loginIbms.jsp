<%@include file="/commons/include/html_doctype.html"%><!-- 定义了浏览器的文本模式（防止IE8进入杂项） -->
<%@page import="org.springframework.security.authentication.AuthenticationServiceException"%>
<%@page import="org.springframework.security.authentication.AccountExpiredException"%>
<%@page import="org.springframework.security.authentication.DisabledException"%>
<%@page import="org.springframework.security.authentication.LockedException"%>
<%@page import="org.springframework.security.authentication.BadCredentialsException"%>
<%@ taglib uri="http://www.cssrc.com.cn/functions" prefix="ibms"%>
<%@page import="java.util.Enumeration"%>
<%@ page pageEncoding="UTF-8" %>
<%@page import="org.springframework.security.web.WebAttributes"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title><ibms:sysparam paramname="COMPANY_NAME"/>－－<ibms:sysparam paramname="SYSTEM_TITLE"/></title>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <meta http-equiv="X-UA-Compatible" content="IE=edge" />
    <f:js pre="jslib/lang/common" ></f:js>
    <script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerDialog.js" ></script><!-- ligerDialog弹窗 -->
	<f:link href="login/login.css" ></f:link>
	<f:link href="Aqua/css/ligerui-dialog.css"></f:link>
	
	<script type="text/javascript">
		if(top!=this){//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
			//跳转到老样式
			  top.location='${ctx}/loginIbms.jsp';
		}
		
		function reload(){
			var url="${ctx}/servlet/ValidCode?rand=" +new Date().getTime();
			document.getElementById("validImg").src=url;
		}
		
		function changePassword(userId){
			var title = "密码更新";
			var url = "${ctx}/oa/system/sysUser/modifyPwdView.do?userId="+userId;
			$.ligerDialog.open({
				title:title, 
    			url:url, 
    			height:400,
    			width:600,
    			isDrag:false,
    			showToggle:false,
    			isResize:false,
    			top:70,
    			sucCall:function(rtn){
    				//rtn作为回调对象，可进行定制和扩展
    				if(!(rtn == undefined || rtn == null || rtn == '')){
    					window.location.reload(true);
    				}
    			}
			});
		}
		
		$(function(){
			$('.main_login').find('input').keydown(function(event){
				if(event.keyCode==13){
					$('#form-login').submit();
				}
			});
		})
	</script>
</head>
<body>
<div class="form_background">
<form id="form-login" action="${ctx}/login.do" method="post">

	<span id="officeSpan" style="color:red" width="100%";height="50px"></span>
	<center>
		<div class="main_login">
			<span class="bpmlogo_login"></span>
			<div class="content_login">
			<div class="column" id="showSystemLog">
			<span style="font-size:20px;text-align:center">
			<ibms:sysparam paramname="COMPANY_NAME"/> <ibms:sysparam paramname="SYSTEM_TITLE"/>
			</span>
			<img id="logoImg" src="${ctx}<ibms:sysparam paramname='SYSTEM_TITLE_LOGO'/>" width="280px" 
			onerror="document.getElementById('showSystemLog').innerHTML='Logo图片无法显示，请修改参数管理中的参数值：SYSTEM_TITLE_LOGO，默认为/images/logo.png';"/>
			</div>	
				<div class="column">
					<span style="font-size:15px;">用户名&nbsp;:</span>
			        <input class="username" type="text" name="username" class="login" /><br>	
				</div>	
				<div class="column">
					<span style="font-size:15px;">密&nbsp;&nbsp;&nbsp;&nbsp;码&nbsp;:</span>
					<input class="password" type="password" name="password"/>
				</div>	
				<c:if test="${sessionScope.validCodeEnabled=='true'}">
					<div class="vcode column">
					 	<div>
							<span>验证码:</span><br>
						
							<input type="text" name="validCode"  />
						</div>
						<div class="vcode_img">
							<img id="validImg" src="${ctx}/servlet/ValidCode" /><br>
							<input type="hidden" name="validCodeEnabled" value="true"/>
							<a onclick="reload()">看不清，换一张</a>
						</div>
					</div>
				</c:if>
				<div class="confirm">
					<input id="checkbox" type="checkbox" name="_spring_security_remember_me" value="1"/>
					<span><img src="styles/login/images/5c5d5f.png" alt="" /></span>
					<a class="login_btn btn right" onclick="document.getElementById('form-login').submit();">登录</a>
					<a class="reset_btn btn right" onclick="document.getElementById('form-login').reset();">重置</a>
					<div>
						<!-- <a href='${ctx}/media/office/NtkoOfficeControlSetup.msi'>下载Office控件</a> -->
					</div>
				</div>
				<%
				Object loginError=(Object)request.getSession().getAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
				
				if(loginError!=null ){
					String msg="";
					if(loginError instanceof BadCredentialsException){
						msg="密码输入错误";
					}
					else if(loginError instanceof AuthenticationServiceException){
						AuthenticationServiceException ex=(AuthenticationServiceException)loginError;
						msg=ex.getMessage();
					}
					else{
						msg=loginError.toString();
					}
				%>
				<div class="confirm"><span style="color:#ff0000;"><%=msg %></span></div>
				<%
				request.getSession().removeAttribute("validCodeEnabled");//删除需要验证码的SESSION
				request.getSession().removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);//删除登录失败信息
				}
				%>
			</div>
		</div>
	</center>
</form>
</div>
<!--版权-->
<div class="login-copyright">
	 <%--<div class="copy-en">Copyright &copy; Wuxi Orient Software Tech Co.,Ltd</div>--%>
	 <%--<div class="copy-zh">中船重工奥蓝托无锡软件技术有限公司</div>--%>
</div>
</body>
</html>

 
