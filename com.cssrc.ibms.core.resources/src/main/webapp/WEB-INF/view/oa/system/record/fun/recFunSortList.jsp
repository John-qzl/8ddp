<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>功能点排序</title>
	
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/util/SelectOption.js"></script>
	<script type="text/javascript">
	/*KILLDIALOG*/
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	
	$(function(){
		//格子
		//for(var i=1;i<=10;i++)$("#sel_cell").append("<option value='"+i+"'>"+i+"</option>");
		//顶部、向上、向下、底部
		var obj=document.getElementById('funId');
		$("#btn_top").click(function(){
			
			__SelectOption__.moveTop(obj);
		});
		$("#btn_up").click(function(){
			__SelectOption__.moveUp(obj, 1);
		});
		$("#btn_down").click(function(){
			__SelectOption__.moveDown(obj, 1);
		});
		$("#btn_bottom").click(function(){
			__SelectOption__.moveBottom(obj);
		});
		
		$("a.save").click(function() {
			var obj=document.getElementById('funId');
			var funIds = "";
			for(var i=0;i<obj.length;i++){
				funIds+=obj[i].value+",";
			}
			if(funIds.length>1){
				funIds=funIds.substring(0,funIds.length-1);
				var url="${ctx}/oa/system/recFun/sort.do";
				var params={"funIds":funIds};
	 			$.post(url,params,function(result){
	 				var obj=new com.ibms.form.ResultMessage(result);
	 				if(obj.isSuccess()){//成功
	 					$.ligerDialog.success("功能点排序完成!",'提示信息',function(){
	 						dialog.close();});
	 					dialog.get("sucCall")();
	 				}
	 				else{
	 					$.ligerDialog.err('出错信息',"功能点排序失败",obj.getMessage());
	 				}
	 			});
			}
		});
	});
	
	
	</script>
</head>
<body>
<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">功能点排序</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
						
						<div class="group"><a class="link close"  href="javascript:;" onclick="dialog.close();">关闭</a></div>
					</div>
				</div>
		</div>
	<form id="dataForm" method="post" action="sort.do">
		<div class="panel-data">
			<table class="table-detail"  border="0" cellspacing="0" cellpadding="0" >
			
				<tr>
					<td class="form_title" align="center" >
						<select id="funId" name="funId" size="12" style="width:100%;" multiple="multiple">
							<c:forEach items="${RecFunList}" var="d">
								<option value="${d.funId }">${d.funName }</option>
							</c:forEach>
						</select>
					</td>
					<td class="form_title" style="text-align:left;width:80px">
						<input type="button" id="btn_top" value="顶部" /><br/>
						<input type="button" id="btn_up" value="向上" /><br/>
						<input type="button" id="btn_down" value="向下" /><br/>
						<input type="button" id="btn_bottom" value="底部" /><br/>
					</td>
				</tr>
			</table>
		</div>
	</form>
</div>
</body>
</html>
