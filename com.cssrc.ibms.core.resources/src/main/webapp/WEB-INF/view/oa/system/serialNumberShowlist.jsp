<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerLayout.js" ></script>
<title>流水号列表</title>
<script type="text/javascript">
var win = window.parent.frameElement.dialog;
	$(function(){
		//$("#defLayout").ligerLayout({ topHeight: 65,bottomHeight:40,allowTopResize:false,allowBottomResize:false});
		
		$("tr.odd,tr.even").unbind("hover");
		$("tr.odd,tr.even").click(function(){
			$(this).siblings().removeClass("over").end().addClass("over");
			$(this).find(':radio').attr('checked', 'checked');
		});
	})
	
	function selectTable(){
		var obj=$("#serialNumberItem tr.over");
	
		if(obj.length>0){
			var objInput=$("input.pk",obj);
			var aryTb=objInput.val().split(",");
			parent.getTable(aryTb[0],aryTb[1]);
		}else{
			alert("请选择记录");
		}
	}
	 
</script>
</head>



<body>
	 
	<div id="defLayout" class="panel">
		<%--<div position="top" title="选择流水号" >--%>
			<div class="panel-top">
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					<div class="group"><a class="link add" href="edit.do?islist=1">添加</a></div>
				</div>	
			</div>
			<div class="panel-search" moreHeight="50">
				<form id="searchForm" method="post" action="showlist.do">
					<ul class="row plat-row">
							<li><span class="label">名称:</span><input type="text" name="Q_name_SL"  class="inputText" size="15" value="${param['Q_name_SL']}"/></li>
					</ul>		
				</form>
			</div>
		</div>
		<%--<div position="center"  style="overflow: auto;">--%>
		<div class="panel-body">
	    	  	<display:table name="serialNumberList" id="serialNumberItem" requestURI="showlist.do" sort="external"  export="false"  class="table-grid">
				<display:column  media="html" style="width:30px;">
					  	<input type="radio" name="tableId" value="${serialNumberItem.id}">
						<input  type="hidden" class="pk"  value="${serialNumberItem.alias },${serialNumberItem.name}">
				</display:column>
				<display:column property="name" title="名称" sortable="true" sortName="name"></display:column>
				<display:column property="alias" title="别名" sortable="true" sortName="alias"></display:column>
				<display:column property="rule" title="规则" sortable="true" sortName="rule"></display:column>
			</display:table>
			<ibms:paging tableId="serialNumberItem"/>
		</div>
		<div position="bottom"  class="bottom new-bottom">
			<a href='####' class='button'  onclick="selectTable()" ><span class="icon ok"></span><span>选择</span></a>
			<a href="####" class='button' style='margin-left:10px;' onclick="win.close();" ><span class="icon cancel"></span><span >取消</span></a>
		</div>
	</div> 
</body>

</html>