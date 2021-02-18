<!-- 说明：
	初始化函数：权限信息初始化、按钮事件绑定、提示信息初始化
	自定义函数： 批量设置权限、保存权限数据。
-->
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>表单授权</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Permission.js"></script>

<script type="text/javascript">
	var dialog;
	if(frameElement){
		frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	}
	var nodeId="${nodeId}";
	var actDefId="${actDefId}";
	var formKey="${formKey}";
	var tableId="${tableId}";
	var isNodeRights=${isNodeRights};
	var parentActDefId = "${parentActDefId}";
	var platform = 0;

	var __Permission__;
	
	$(function() {
		platform = $("[name='platform']").val();
		//权限处理
		__Permission__=new Permission();
		   //根据节点ID获取表单权限。
		if(isNodeRights){
			__Permission__.loadByNode(actDefId,nodeId,formKey,parentActDefId,platform);
			$("#nodeId").change(changeNode);
		}else if(actDefId){
			//根据流程定义ID获取表单权限。
			__Permission__.loadByActDefId(actDefId,formKey,parentActDefId,platform);
		}else{
			//根据表单key获取表单权限。
			__Permission__.loadPermission(tableId,formKey,platform);
		}
		$("#dataFormSave").click(savePermission);
		$("input:radio").click(setPermision);
		//重置权限设置
		$("#initRights").click(function(){
			$.ligerDialog.confirm("是否确定重置权限设置?",function (r){
					if(r){
						var url = __ctx+"/oa/form/formDef/initRights.do";
						$.post(url,{formKey:formKey,actDefId:actDefId,nodeId:nodeId,parentActDefId:parentActDefId,platform:platform},function(d){
							if(d.result)
								$.ligerDialog.success(d.message,function(){
									frameElement.dialog.close();
								});
							else
								$.ligerDialog.warn(d.message);
						});
					}
				});
		});
		//提示信息初始化
		$("a.tipinfo").each(function(){
			var helpId = $(this).attr('name');
			$(this).ligerTip({content : $("#"+helpId).html(),width:500,auto:true});
		});
	});
	
	//重置权限设置后 返回默认值 
	function setDefaultPermision(){
		//fieldEdit tableEdit  opinionEdit
		var arry = new Array();  //新建:
		if($("#fieldPermission").html()!=''){
			arry.push('fieldEdit');  //增加  默认触发事件的对象ID
		}
		if($("#filesPermission").html()!=''){
			arry.push('filesEdit');   //增加  默认触发事件的对象ID
		}
		if($("#tablePermission").html()!=''){
			arry.push('tableEdit');  //增加   默认触发事件的对象ID
		}
		if($("#tableRelPermission").html()!=''){
			arry.push('tableRelEdit');  //增加   默认触发事件的对象ID
		}
		if($("#opinionPermission").html()!=''){
			arry.push('opinionEdit');  //增加   默认触发事件的对象ID
		}
		
		//alert($("#opinionPermission").html());
		for(var i=0; i<arry.length; i++){
			var name = arry[i];
			var editObj=document.getElementById ("fieldEdit");
			editObj.click(); 
			editObj.checked="";
		}
	}
	
	
	//批量设置权限。
	function setPermision(){
		var val=$(this).val();
		var obj=$(this).closest("[name=tableContainer]");
		$("span[name='r_span'],span[name='w_span'],span[name='b_span']",obj).hide();
		switch(val){
			//hidden隐藏 
			case "1":
				$(".r_select,.w_select,.b_select",obj).val("none");
				$("[name=rpost]",obj).removeAttr("checked");
				break;
			//readonly只读
			case "2":
				$(".r_select",obj).val("everyone");
				$(".w_select,.b_select",obj).val("none");
				break;
			//edit 编辑
			case "3":
				$(".r_select,.w_select",obj).val("everyone");
				$(".b_select",obj).val("none");
				$("[name=rpost]",obj).removeAttr("checked");
				break;
		   //必填	
			case "4":
				$(".r_select,.w_select,.b_select",obj).val("everyone");
				$("[name=rpost]",obj).removeAttr("checked");
				break;
		}
	}
	
	//重新加载任务节点的权限
	function changeNode(){
		var obj=$("#nodeId");
		nodeId=obj.val();
		__Permission__.loadByNode(actDefId,nodeId,formKey,parentActDefId,platform);
	};
	
	//保存权限数据。
	function savePermission(){
		//设置所有的权限。
		__Permission__.setAllPermission();
		//
		var json=__Permission__.getPermissionJson();
		var params={};
		params.formKey=formKey;
		params.permission=json;
		params.actDefId=actDefId;
		if(parentActDefId!=""){
			params.parentActDefId = parentActDefId;
		}
		params.platform = platform;
		//流程节点权限。
		if(isNodeRights){
			//params.actDefId=actDefId;
			params.nodeId=nodeId;
		}
		
		$.post("savePermission.do",params,showResponse);
	}
		
	function showResponse(data){
		var obj=new com.ibms.form.ResultMessage(data);
		if(obj.isSuccess()){//成功
			$.ligerDialog.confirm('操作成功,继续操作吗?','提示信息',function(rtn){
				if(!rtn){
					frameElement.dialog.close();
				}
			});
	    }else{//失败
	    	$.ligerDialog.err('出错信息',"表单授权失败",obj.getMessage());
	    }
	};
</script>
</head>
<body >
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">表单权限</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link close" href="javascript:frameElement.dialog.close();">关闭</a>
					</div>
					
					<div class="group">
						<a class="link initRights" id="initRights" href="javascript:;">重置权限设置</a>
					</div>
					<a href="javascript:void(0)" class="tipinfo"  name="operateHelp">
						控件操作权限						
					</a>
			<!-- 	<c:if test="${nodeId==null}">
						
						<div class="group">
							<a class="link initRights" id="initRights" href="javascript:;">重置权限设置</a>
						</div>
					</c:if>	 -->				
				</div>
			</div>
		</div>
		<div  class="panel-body">
			<c:if test="${isNodeRights}">
				<form id="bpmFormDefForm">
					<table cellpadding="0" cellspacing="0" border="0" style=" margin: 4px 0px;"  class="table-detail">
						<tr style="height:25px;">
							<td>流程节点:
								<select id="nodeId" >
									<c:forEach items="${nodeMap}" var="node">
										<option value="${node.key}" <c:if test="${node.key== nodeId}"> selected="selected" </c:if> >${node.value}</option>
									</c:forEach>
								</select>
							</td>
						</tr>
					</table>
					
				</form>
				
			</c:if>
			<div class="panel-table">
			
				<table cellpadding="1" cellspacing="1" class="table-grid" name="tableContainer">
					<tr>
						<th width="20%">group</th>
						<th width="40%">可见权限</th>
						<th width="40%">高亮显示</th>
					</tr>
					<tr>
						<td colspan="3">
							<input type="radio" value="1" name="rdoPermission" id="tableGroupHidden" ><label for="tableGroupHidden">隐藏</label>
							<input type="radio" value="3" name="rdoPermission" id="tableGroupReadonly"><label for="tableGroupReadonly">可见</label>
						</td>
					</tr>
					<tbody id="tableGroupPermission"></tbody>
				</table>
				
				<!-- 主表权限 设置-->
				<table cellpadding="1" cellspacing="1" class="table-grid" name="tableContainer">
					<tr>
						<th width="20%">字段</th>
						<th width="20%">只读权限</th>
						<th width="20%">编辑权限</th>
						<th width="20%">必填权限</th>
						<th width="5%">只读提交</th>
						<th var="operate" width="15%">控件操作权限</th>
					</tr>
					<tr>
						<td colspan="6">
							<input type="radio" value="1" name="rdoPermission" id="fieldHidden" ><label for="fieldHidden">隐藏</label>
							<input type="radio" value="2" name="rdoPermission" id="fieldReadonly"><label for="fieldReadonly">只读</label>
							<input type="radio" value="3" name="rdoPermission" id="fieldEdit" ><label for="fieldEdit">编辑</label>
							<input type="radio" value="4" name="rdoPermission" id="fieldRequired" ><label for="fieldRequired">必填</label>			
						</td>
					</tr>
					<tbody id="fieldPermission"></tbody>
				</table>
				<!-- sub表权限 设置-->
				<table  cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;"  name="tableContainer">
					<tr>
						<th width="20%">sub子表字段</th>
						<th width="20%">只读权限</th>
						<th width="20%">编辑权限</th>
						<th width="20%">必填权限</th>
						<th var="operate" width="20%">控件操作权限</th>
					</tr>
					<tr>
						<td colspan="5">
							<input type="radio" value="1" name="rdoTbPermission" id="tableHidden" ><label for="tableHidden">隐藏</label>
							<input type="radio" value="2" name="rdoTbPermission" id="tableReadonly"><label for="tableReadonly">只读</label>
							<input type="radio" value="3" name="rdoTbPermission" id="tableEdit" ><label for="tableEdit">编辑</label>
							<input type="radio" value="4" name="rdoTbPermission" id="tableRequired" ><label for="tableRequired">必填</label>					
						</td>
					</tr>
					<tbody id="tablePermission"></tbody>
				</table>
				<!-- rel表权限 设置-->
				<table  cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;"  name="tableContainer">
					<tr>
						<th width="20%">rel关联表字段</th>
						<th width="20%">只读权限</th>
						<th width="20%">编辑权限</th>
						<th width="20%">必填权限</th>
						<th var="operate" width="20%">控件操作权限</th>
					</tr>
					<tr>
						<td colspan="5">
							<input type="radio" value="1" name="rdoRelTbPermission" id="tableRelHidden" ><label for="tableRelHidden">隐藏</label>
							<input type="radio" value="2" name="rdoRelTbPermission" id="tableRelReadonly"><label for="tableRelReadonly">只读</label>
							<input type="radio" value="3" name="rdoRelTbPermission" id="tableRelEdit" ><label for="tableRelEdit">编辑</label>
							<input type="radio" value="4" name="rdoRelTbPermission" id="tableRelRequired" ><label for="tableRelRequired">必填</label>					
						</td>
					</tr>
					<tbody id="tableRelPermission"></tbody>
				</table>
				<!-- 文件夹附件权限设置 -->
				<table cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;" name="tableContainer">
					<tr>
						<th width="30%">文件/附件按钮</th>
						<th >可见权限</th>
						<!-- <th width="35%">编辑权限</th> -->
					</tr>
					<tr>
						<td colspan="3">
							<input type="radio" value="1" name="rdoFlPermission" id="filesHidden" ><label for="filesHidden">隐藏</label>
							<input type="radio" value="3" name="rdoFlPermission" id="filesReadonly"><label for="filesReadonly">可见</label>
							<!-- <input type="radio" value="3" name="rdoFlPermission" id="filesEdit" ><label for="filesEdit">编辑</label> -->	
						</td>
					</tr>
					<tbody id="filesPermission"></tbody>
				</table>
				</br>
				<!-- 意见表权限 设置-->
				<table  cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;" name="tableContainer">
				<tr>
					<th width="20%">意见</th>
					<th width="25%">只读权限</th>
					<th width="25%">编辑权限</th>
					<th width="30%">必填权限</th>
				</tr>
				<tr>
					<td colspan="4">
						<input type="radio" value="1" name="rdoOpPermission" id="opinionHidden" ><label for="opinionHidden">隐藏</label>
						<input type="radio" value="2" name="rdoOpPermission" id="opinionReadonly"><label for="opinionReadonly">只读</label>
						<input type="radio" value="3" name="rdoOpPermission" id="opinionEdit" ><label for="opinionEdit">编辑</label>
						<input type="radio" value="4" name="rdoOpPermission" id="opinionRequired" ><label for="opinionRequired">必填</label>
					</td>
				</tr>
				<tbody id="opinionPermission"></tbody>
			</table>
		</div>
	 </div>
		<input type="hidden" value="${platform}" name="platform">
  </div>	
 <div id="operateHelp" style="display:none">
	<br/>
	<b>控件操作权限</b>
	<br/><font color=red>（1）为什么添加此权限？</font>
	<br/>在业务表单中，某些控件字段不仅有数据、还拥有操作按钮（操作行为）；
	<br/>控件操作权限便是对操作行为的控制；
	<br/><font color=red>（2）字段数据权限 与 控件操作权限的关系（以附件控件为例）？</font>
	<br/>字段为<font color=blue>隐藏权限</font>时：操作按钮权限信息不生效；
	<br/>字段为<font color=blue>只读权限</font>时：操作按钮权限信息只有 关于<font color=blue>下载</font>的生效；
	<br/>字段为<font color=blue>编辑、必填权限</font>时：操作按钮权限信息全部生效；
	<br/><font color=blue>使用此权限，需要更新系统模板、并将表单设计重新生成！！！</font>；
</div>
</body>
</html>

