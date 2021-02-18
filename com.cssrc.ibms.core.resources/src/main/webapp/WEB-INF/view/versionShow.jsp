<%--
	time:
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@include file="/commons/include/html_doctype.html"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>版本说明</title>
	<%@include file="/commons/include/form.jsp" %>
	<f:link href="from-jsp.css"></f:link>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=sysUser"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery-1.7.2.min.js"></script>
    <script type="text/javascript">
            var myclick = function(v) {
                var llis = document.getElementsByTagName("li");
                for(var i = 0; i < llis.length; i++) {
                    var lli = llis[i];
					var tabId = "tab" +v ;
                    if(lli == document.getElementById("tab" + v)) {
                        lli.style.backgroundColor = "#fff";
						$("#"+tabId).addClass('mainColor');
                    } else {
                        lli.style.backgroundColor = "#dcdcdc";
						$("#"+tabId).addClass('mainColor');
                    }
                }
				
                var divs = document.getElementsByClassName("tab_Content");
                for(var i = 0; i < divs.length; i++) {
                    var divv = divs[i];
                    if(divv == document.getElementById("version" + v)) {
                        divv.style.display = "block";
                    } else {
                        divv.style.display = "none";
                    }
                }
            }
    </script>   
</head>
<body>

<div id="content">
	<div id="tab_bar">
		<ul>
        	<li id="tab1" onclick="myclick(1)" style="background-color: #fff">
            	v ${version1}
            </li>
            <li id="tab2" onclick="myclick(2)">
                v ${version2}
            </li>
			<li id="tab3" onclick="myclick(3)">
				v ${version3}
			</li>
			<li id="tab4" onclick="myclick(4)">
				更多历史版本
			</li>
        </ul>
	</div>

	<div id="version1" class="tab_Content" style="display:block">
		<div>
			<!-- 搜索version1所对应版本 -->
			<span style="font-size: 14px;font-weight: bold;color:#666;" >更新说明:</span>
			
			<!-- <c:forEach var="description" items="${versions[version1]}">
				<span class="green">${description}</span>
			</c:forEach>-->
			<c:forEach var="version" items="${versions}" >
				<c:if test="${version1 eq version.key}">
					 <c:forEach var="description" items="${version.value}" varStatus="status">
						<span class="green">&nbsp;&nbsp;&nbsp;&nbsp;${status.index+1}. ${description}</span>
					</c:forEach>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<div id="version2" class="tab_Content" >
		<div>
			<!-- 搜索version2所对应版本 -->
			<span style="font-size: 14px;font-weight: bold;color:#666;" >更新说明:</span>
			
			<c:forEach var="version" items="${versions}">
				<c:if test="${version2 eq version.key}">
					 <c:forEach var="description" items="${version.value}" varStatus="status">
						<span class="green">&nbsp;&nbsp;&nbsp;&nbsp;${status.index+1}. ${description}</span>
					</c:forEach>
				</c:if>
			</c:forEach>
			
		</div>
	</div>
	<div id="version3" class="tab_Content" >
		<div>
			<!-- 搜索version3所对应版本 -->
			<span style="font-size: 14px;font-weight: bold;color:#666;" >更新说明:</span>
			
			<c:forEach var="version" items="${versions}">
				<c:if test="${version3 eq version.key}">
					 <c:forEach var="description" items="${version.value}" varStatus="status">
						<span class="green">&nbsp;&nbsp;&nbsp;&nbsp;${status.index+1}. ${description}</span>
					</c:forEach>
				</c:if>
			</c:forEach>
		</div>
	</div>
	<div id="version4" class="tab_Content" >
		<div>
			<!-- 循环历史版本 -->
			<c:forEach var="version" items="${versions}">
				<c:if test="${version1 ne version.key && version2 ne version.key && version3 ne version.key}">
					<span style="font-size: 14px;font-weight: bold;color:#666;" >v ${version.key}</span>
					
					<c:forEach var="description" items="${version.value}" varStatus="status">
						<span class="green">&nbsp;&nbsp;&nbsp;&nbsp;${status.index+1}. ${description}</span>
					</c:forEach>
					
				</c:if>
			</c:forEach>
		</div>
	</div>
</div>


</body>
</html>
