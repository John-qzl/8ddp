<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@include file="/commons/include/html_doctype.html" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<head>
    <title>${currentSystem.companyName}${currentSystem.systemName}</title>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8"/>
    <c:set var="ctx" value="${pageContext.request.contextPath}"/>
    <%@include file="/commons/include/form.jsp" %>
    <f:link href="iconfont.css"></f:link>
	<f:link href="iconImg.css"></f:link>
    <link rel="stylesheet" href="${ctx}/styles/framework/basic.css">
    <style type="text/css">
        .table-container table td {
            padding: 0px;
        }
        .gwt-Image{
            width: 100%; height: 878px;
        }
        .fontsetting{
            font-family : 微软雅黑,宋体;
            font-size : 3rem;
            color : #fff!important;;
            font-weight: bold;
        }
        .fontposition{
            position: absolute; top: 40%; left: 30%;
        }

    </style>
    <script src="${ctx}/js/portal/share.js" type="text/javascript"></script>
    <script type="text/javascript">
        if (top != this) {//当这个窗口出现在iframe里，表示其目前已经timeout，需要把外面的框架窗口也重定向登录页面
            top.location = '<%=request.getContextPath()%>/oa/console/main.do';
        }
        var ctxPath = "<%=request.getContextPath()%>";
        window.location.href = "${ctx}/oa/console/main.do?flag=carry";
        //运载
        function carry() {
            debugger;
            //运载火箭只读角色
        	if('${roleNames}'.indexOf("yzhjzd") != -1){
        		 window.location.href = "${ctx}/oa/console/main.do?flag=carry";
        	}else{
        		$.ligerDialog.warn("您没有访问权限！请联系管理员分配权限！");
        	}
        }
        //空间
        function space() {
        	//空间科学只读角色
        	if('${roleNames}'.indexOf("kjkxzd") != -1){
                window.location.href = "${ctx}/oa/console/main.do?flag=space";
        	}else{
        		$.ligerDialog.warn("您没有访问权限！请联系管理员分配权限！");
        	}
        }
        //单机 结构机构
        function strutturale() {
        	//空间科学只读角色
        	if('${roleNames}'.indexOf("jgjgzd") != -1){
        		window.location.href = "${ctx}/oa/console/main.do?flag=strutturale";
        	}else{
        		$.ligerDialog.warn("您没有访问权限！请联系管理员分配权限！");
        	}
           
        }
      	//单机 结构机构
        function other() {
        	//空间科学只读角色
        	if('${roleNames}'.indexOf("qtzd") != -1){
        		window.location.href = "${ctx}/oa/console/main.do?flag=strutturale";
        	}else{
        		$.ligerDialog.warn("您没有访问权限！请联系管理员分配权限！");
        	}
        }
    </script>
</head>
<body>
<%@include file="main_top.jspf" %>
<div  style="margin: 0px; height: 878px;">
    <table >
        <tbody>
        <tr>
            <td align="center" style="vertical-align: middle;">
                <table cellspacing="20" style="border-width: 0px;" class='table-container'>
                    <tbody>
                    <tr>
                        <td>
                            <table>
                                <tbody>
                                <tr>
                                    <td align="center" style="vertical-align: middle;">
                                        <a class="fontsetting" href="javascript:carry();"title="全部">
                                            <div style="position: relative;">
                                                <img src="${ctx}/dpImg/carry.png" class="gwt-Image" alt=""/>
                                                <span class="fontposition " >全部</span>
                                            </div>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table >
                                <tbody>
                                <tr>
                                    <td align="center" style="vertical-align: middle;">
                                        <a class="fontsetting" href="javascript:space();" title="空间科学">
                                            <div style="position: relative;">
                                                <img src="${ctx}/dpImg/space.png" class="gwt-Image" alt=""/>
                                                <span class="fontposition">空间科学</span>
                                            </div>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table>
                                <tbody>
                                <tr>
                                    <td align="center" style="vertical-align: middle;">
                                        <a class="fontsetting" href="javascript:strutturale();"title="结构机构">
                                            <div style="position: relative;">
                                                <img src="${ctx}/dpImg/structure.png" class="gwt-Image" alt=""/>
                                                <span class="fontposition">结构机构</span>
                                            </div>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                        <td>
                            <table >
                                <tbody>
                                <tr>
                                    <td align="center" style="vertical-align: middle;">
                                        <a class="fontsetting" href="javascript:other();"title="其他">
                                            <div style="position: relative;">
                                                <img src="${ctx}/dpImg/product.png" class="gwt-Image" alt=""/>
                                                <span class="fontposition">其他</span>
                                            </div>
                                        </a>
                                    </td>
                                </tr>
                                </tbody>
                            </table>
                        </td>
                    </tr>
                    </tbody>
                </table>
            </td>
        </tr>
        </tbody>
    </table>
</div>

</body>
</html>
