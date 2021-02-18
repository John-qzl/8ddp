
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>选择自定义表</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerLayout.js" ></script>
<script type="text/javascript">
	$(function(){
		//var height=$(".panel-top").height();
		$("#centerLayout").height($(window).height()-50);
		//$("#defLayout").ligerLayout({height:'100%', bottomHeight:40,allowTopResize:false,allowBottomResize:false});
		
		$("tr.odd,tr.even").unbind("hover");
		$("tr.odd,tr.even").click(function(){
			$(this).siblings().removeClass("over").end().addClass("over");
		});
	})

	function selectTable(){
		var obj=$("#bpmFormTableItem tr.over");
	
		if(obj.length>0){
			var objInput=$("input",obj);
			var aryTb=objInput.val().split(",");
			parent.getTable(aryTb[0],aryTb[1]);
		}
	}
	function closee(){
		window.top.$('.l-window-mask').hide();
		window.parent.parent.$('.l-dialog').remove();
	}
	 
</script>
<f:link href="from-jsp.css"></f:link>
</head>
<body>
	<div id="defLayout">
		<div id="centerLayout" position="center" class="panel">
			<div class="panel-top" usesclflw="true">
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					</div>	
				</div>
				<div class="panel-search">
					<form id="searchForm" method="post" action="dialog.do">
						<ul class="row plat-row">
							<li><span class="label">表名:</span><input type="text" name="Q_tableName_SL"  class="inputText" size="15" value="${param['Q_tableName_SL']}"/></li>
							<li><span class="label">描述:</span><input type="text" name="Q_tableDesc_SL"  class="inputText" size="15" value="${param['Q_tableDesc_SL']}"/></li>
						</ul>		
					</form>
				</div>
			</div>
			<div class="panel-body">
		    	<display:table name="bpmFormTableList" id="bpmFormTableItem" requestURI="list.do" sort="external"  export="false"  class="table-grid">
					<display:column  title="表名" style="text-align:left;width:200px;cursor:pointer;">
						<input  type="hidden" value="${bpmFormTableItem.tableId },${bpmFormTableItem.tableName }">
						${bpmFormTableItem.tableName }
					</display:column>
					<display:column property="tableDesc" title="描述" style="text-align:left;cursor:pointer;"></display:column>
				</display:table>
				<ibms:paging tableId="bpmFormTableItem"/>
			</div>
		</div>
		<div position="bottom"  class="bottom new-bottom">
			<a href='####' class='button'  onclick="selectTable()" ><span class="icon ok"></span><span>选择</span></a>
			<a href='####' class='button' style='margin-left:10px;' onclick="closee()"><span class="icon cancel"></span><span >取消</span></a>
		</div>
	</div> 
</body>
</html>


