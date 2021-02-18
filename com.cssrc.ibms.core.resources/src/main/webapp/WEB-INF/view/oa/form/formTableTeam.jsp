<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>分组设置</title>
<f:link href="tree/zTreeStyle.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/SelectOption.js"></script>
<script type="text/javascript">
var tableId = '${bpmFormTable.tableId}',valid, team = '${bpmFormTable.team}';

$(function() {
	$("#teamLayout").ligerLayout({
		leftWidth : 150,
		height : '100%',
		allowLeftResize : false
	});
	loadTree();
	valid =  $("#teamItem").form();
	var height = $("body").height();
	$("#fieldtree").height(height-95);
	
	parseTeam(team);	
	
	var teamItem = 	$("#teamItem");
	$("a.add").click(function(){
		add(true);
	});
	$("a.save").click(function(){
		saveData();
	});
	
	teamItem.delegate("select[name='teamField']", "focus click", function() {		
		selectTeam (this);
	});
	
	$(".moveFieldTop").click(function(){
		var td = $(this).parent().parent().parent();
		var select  = $("select[name='teamField']",td).get(0);
		__SelectOption__.moveTop(select);
	});
	$(".moveFieldUp").click(function(){
		var td = 	$(this).parent().parent().parent();
		var select  = $("select[name='teamField']",td).get(0);
		__SelectOption__.moveUp(select,'1');
	});
	$(".moveFieldDown").click( function(){
		var td = $(this).parent().parent().parent();
		var select  = $("select[name='teamField']",td).get(0);;
		__SelectOption__.moveDown(select,'1');
	});
	$(".moveFieldBottom").click( function(){
		var td = $(this).parent().parent().parent();
		var select  = $("select[name='teamField']",td).get(0);
		__SelectOption__.moveBottom(select);
	});
	
	$('#isShow').click(function(){
		if($(this).is(":checked"))
			$('#showPosition').show();
		else
			$('#showPosition').hide();	
	});
	
	$("a.moveup,a.movedown").click(move);

});

//绑定上下移动	
function move(){
 	var obj=$(this);
 	var direct=obj.hasClass("moveup");
 	var objFieldset=obj.closest('[zone="team"]');
	if(direct){
		var prevObj=objFieldset.prev();
		if(prevObj.length>0){
			objFieldset.insertBefore(prevObj);	
		}
	}
	else{
		var nextObj=objFieldset.next();
		if(nextObj.length>0){
			objFieldset.insertAfter(nextObj);
		}
	}
};

//加载树
function loadTree(){
	BpmFormTableTeam.getFieldsByTableId(tableId);
}

//根据注释生成表名
function autoGetTableKey(inputObj){
	var url=__ctx + '/oa/form/formTable/getTableKey.do' ;
	var subject=$(inputObj).val();
	if($.trim(subject).length<1) return;
	$.post(url,{'subject':subject,'tableId':'${tableId}'},function(response){
		var json=eval('('+response+')'); 
		if(json.result==1 ){
			var teamKey=$(inputObj).parent().parent().parent().find("[name='teamNameKey']");
			if($.trim(	$(teamKey).val()).length<1){
				$(teamKey).val(json.message);
			};
		}else{
			$.ligerDialog.error(json.message,"提示");
		}
	 });
}

</script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/BpmFormTableTeam.js"></script>

<body>	
	<div class="panel">
			<div id="teamLayout">
				<div position="left" title="表字段" style="overflow: auto; float: left; width: 100%; height: 100%;">
					<div class="panel-toolbar tree-title">
						<span class="toolBar">
							<div class="group">
								<a class="link reload" onclick="loadTree()">刷新</a>
							</div>
						</span>
					</div>
					<ul id="colstree" class="ztree"></ul>
				</div>
				<div position="center" title="分组设置" style="overflow: auto;">
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group">
								<a class="link save"  href="####">保存</a>
							</div>
							
							<div class="group">
								<a class="link add"  href="####">添加</a>
							</div>
							
							<div class="group">
								<a class="link back" href="${returnUrl}">返回</a>
							</div>
						</div>
					</div>
					<div>
						<table cellpadding="1" cellspacing="1"  class="table-grid">
							<tr>
								<th style="text-align: left;" width="120px">未分组字段是否显示：</th>
								<th style="text-align: left;" width="70%" >
									<input type="checkbox" id="isShow"  checked="checked">
									&nbsp;&nbsp;&nbsp;&nbsp;
									<select id="showPosition"  >
										<option value="1">在分组后显示</option>
										<option value="2">在分组前显示</option>
									</select>
								<th>
							</tr>
						</table>
					</div>
					<div>
						<form  id ="teamItem" >
						
						</form>
					</div>
					<div  id="cloneTemplate"  style="display: none;">
							<fieldset style="margin: 5px 0px 5px 0px;" zone="team" >
								<legend>
									<span>分组设置</span>
									
								</legend>
								<table cellpadding="1" cellspacing="1"  class="table-detail">
									<tr>
										<th width="10%">分组名称</th>
										<td>
											<div style="float: left;">
												<input type="text" width="70%" name="teamName" class='inputText valid' validate="{required:true}" onblur="autoGetTableKey(this)" />
											</div>
										</td>
										<th width="10%">分组KEY</th>
										<td>
											<div style="float: left;">
												<input type="text" width="70%" name="teamNameKey" class='inputText valid' validate="{required:true}"/>
											</div>
											<div class="group" style="float: left;">
												<a class="link del" var="del" title="删除"></a>
												<a class='link moveup' title="上移"></a>
												<a class='link movedown' title="下移"></a>
											</div>
										</td>
		
									</tr>
									<tr>
										<th>分组字段</th>
										<td colspan="3">
												<div style="float: left;">
													<select size="10" style="width:200px;height:200px;display:inline-block;" id="teamField1" name="teamField" ondblclick="removeOpt(this)"  ></select>						
												</div>
												<div style="float: left;">
													</br>
													<p>
														<input type="button" value="置顶" class="moveFieldTop">
													</p>
													</br>
													<p>
														<input type="button" value="上移" class="moveFieldUp" >
													</p>
													</br>
													<p>
														<input type="button" value="下移" class="moveFieldDown" >
													</p>
													</br>
													<p>
														<input type="button" value="置底" class="moveFieldBottom">
													</p>
												</div>
			
											</table>
										</td>
									</tr>
								</table>
							</fieldset>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>		