<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>维度管理</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=globalType"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
<script type="text/javascript">
var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
var curDimensionKey = '${curDimensionKey}';
$(function(){
	if(curDimensionKey!="undefined"){
		$('#dimension [value='+curDimensionKey+']')
			.attr("selected","selected");
	}
});
//选择任务节点
 function selectDimension(){
	if('${hasSon}'=='true'){
		var dimensons = ",";
		var dimensonNames = [];
		$('select :checked').each(function(i,e){
			dimensons += $(e).val()+",";
			dimensonNames.push($(e).text().trim());
		});
		var rtn={dimensonNames:dimensonNames,dimensons:dimensons};
		dialog.get("sucCall")(rtn);
	}else{
		var key=$('#dimension :checked').val();
		var rtn={dimensionKey:key};
		dialog.get("sucCall")(rtn);
	}
	dialog.close();
}
</script>
</head>
<body>
				<div id="onlyDimension" <c:if test="${hasSon==true}">style="display:none"</c:if>>
					<div class="panel-top">
								<div class="tbar-title">
									<span class="tbar-label">选择维度</span>
								</div>
								<div class="panel-toolbar">
									<div class="toolBar">
										<div class="group"><a class="link save" onclick="javascript:selectDimension();">选择</a></div>
										<div class="group"><a class="link close" onclick="dialog.close();">关闭</a></div>
									</div>
								</div>
					</div>
					<form id="frmDefInfo">
						<table>
						    <tbody>
						        <tr class ="style_tr">
						            <th width="30%">
						               	 维度:
						            </th>
						            <td>
						                <select id="dimension" class="select" style="width:95%">
						                <c:forEach items="${dimensionList}" var="dimension">
						                 	<option value="${dimension.nodeKey}">
						                        ${dimension.typeName}
						                    </option>
						                </c:forEach>				             
						                </select>
						            </td>
						        </tr>
						    </tbody>
					 </table>
					</form>
				</div>
				<div id="hasSon" <c:if test="${hasSon==false}">style="display:none"</c:if>>
					<div class="panel-top">
								<div class="tbar-title">
									<span class="tbar-label">选择维度</span>
								</div>
								<div class="panel-toolbar">
									<div class="toolBar">
										<div class="group"><a class="link save" onclick="javascript:selectDimension();">选择</a></div>
										<div class="group"><a class="link close" onclick="dialog.close();">关闭</a></div>
									</div>
								</div>
					</div>
					<form id="frmDefInfo">
						<table>
						    <tbody>
						    	<c:forEach items="${dimensionMap}" var="map">
							        <tr>
							            <th width="100px;">
							               	${map.dimension.typeName}:
							            </th>
							            <td>
							                <select id="son" class="select">
							                 <c:forEach items="${map.son}" var="son">
							                 	<option value="${son.typeId}">
							                        ${son.typeName}
							                    </option>	
							                 </c:forEach>					            				             
							                </select>
							            </td>
							        </tr>
						        </c:forEach>
						    </tbody>
					 </table
					</form>
				</div>
</body>
</html>