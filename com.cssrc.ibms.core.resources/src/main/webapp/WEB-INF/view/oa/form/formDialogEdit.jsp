<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑 通用表单对话框</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/Share.js"></script>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmFormDialog"></script>
	<script type="text/javascript">
		var id=${bpmFormDialog.id==0};
		var returnUrl=("${returnUrl}"==null||"${returnUrl}"=="")?("${preUrl}"):("${returnUrl}");
		$(function() {
			function showRequest(formData, jqForm, options) { 
				return true;
			} 
			if(${bpmFormDialog.id==0}){
				valid(showRequest,showResponse,1);
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				var alias=$("#alias").val(),
					objname=$("#objname").val(),
					settingobj=$("#settingobj").val(),
					style=$("input[name='style']:checked").val(),
					styletemp=$("#styletemp").val();
				if(alias==""){
					$.ligerDialog.warn("别名不能为空");
				}else if(/.*[\u4e00-\u9fa5]+.*$/.test(alias)){
					$.ligerDialog.warn("别名不能为中文");
				}else if(objname!=settingobj){
					$.ligerDialog.warn("当前选择的表(视图)未设置显示的列!");
				}else if(id && style!=styletemp){
					$.ligerDialog.warn("你配置的表(视图)的列类型与选择显示样式的类型不一致，请重设置列！");
				}
				else{
					$('#bpmFormDialogForm').submit();
				}
			});
			
			$("#btnSearch").click(searchObjectList);
			
			var needpage = ${bpmFormDialog.style==0} ;
			if(!needpage) {
				//树，隐藏分页TD以及合并TD
				$('#needpage').next('td').hide() ;
				$('#needpage').hide() ;
				$('#needpage').prev('td').attr('colspan',3) ;
			}
			$('#treeRadio').click(function(){
				$('#needpage').next('td').hide() ;
				$('#needpage').hide() ;
				$('#needpage').prev('td').attr('colspan',3) ;
			});
			$('#listRadio').click(function(){
				$('#needpage').show() ;
				$('#needpage').next('td').show() ;
				$('#needpage').prev('td').removeAttr('colspan') ;
			});
			
			$('#ligerDialogRadio').click(function(){
				$("#treeRadio").removeAttr("disabled");
				
				$("#isMultipleSelect").removeAttr("disabled");
				
				$('#paging').removeAttr("disabled");
			});
			$('#selectDialogRadio').click(function(){
				//设置展示为列表并不可编辑
				$('#listRadio').click();
				$("#treeRadio").attr("disabled","disabled");
				//设置为单选并不可编辑
				$("#isSingleSelect").click();
				$('#isMultipleSelect').attr("disabled","disabled");
				//设置为不分页并不可编辑
				$("#nonsort").click();
				selsize();
				$('#paging').attr("disabled","disabled");
				
			});
			
			//编辑时为下拉框时，单选框和是否分页不可编辑
			if("${bpmFormDialog.dialogType}"==1){
				$('#isMultipleSelect').attr("disabled","disabled");
				$('#paging').attr("disabled","disabled");
			}
			
		});
		
		function searchObjectList(){
			
			var selList=$("#objname");
			var dsName=$("#dataSource").val();
			
			var objectname=$("#objectname").val();
			var istable=$("#istable").val();
			var url=__ctx +"/oa/form/formDialog/getByDsObjectName.do";
			if(dsName==null || dsName==""){
				$.ligerDialog.error("请选择数据源!");
				return ;
			}
			
			$.ligerDialog.waitting('正在查询，请等待...');
			$.post(url, { dsName:dsName, objectName: objectname,istable:istable },function(data) {
				$.ligerDialog.closeWaitting();
				selList.empty();
				var success=data.success;
				if(success=='false'){
					$.ligerDialog.error("出错了!");
					return;
				}
				//表的处理
				if(istable=="1"){
					var tables=data.tables;
					//创建临时数组进行排序
					var sortArr = [];
					for(key in tables){
						sortArr.push(key);
					}
					sortArr.sort();
					for (var i = 0; i < sortArr.length; i++) {
						var element = sortArr[i];
						selList.append("<option title='"+tables[element]+"' value='"+ element+"'>"+ element +"("+tables[element] +")" +"</option>" );
					}
					// for(key in tables ){
					// 	selList.append("<option title='"+tables[key]+"' value='"+ key+"'>"+ key +"("+tables[key] +")" +"</option>" );
					// }
				}
				//视图的处理
				else{
					var aryView=data.views;
					for(var i=0;i<aryView.length;i++){
						var v=aryView[i];
						selList.append("<option value='"+ v+"'>"+v+"</option>" );
					}
				}
		    });
		}
		
		function selsize(){
			var isneedPage=$("input:radio[name='needpage']:checked").val();
			if(isneedPage>0){
				$("#spanPageSize").show();
			}
			else{
				$("#spanPageSize").hide();
			}
		}
		
		
		function dialog(){
			$("#selInfo").text("");
			var id=$("#id").val();
			var istable=$("#istable").val();
			var objname=$("#objname").val();
			var dataSource=$("#dataSource").val();
			var style=$("input[name='style']:checked").val();
			
			if(id==0){
				if(objname==null){
					$("#selInfo").text("请先选择数据库表");
					return ;
				}
			}
			showSettingDialog(dataSource,objname,istable,style,id);
		}
		
		function showSettingDialog(dsName,objectname,istable,style,id){
			var settingobj=$("#settingobj").val(),
				  fields={};
			
			if(settingobj==objectname){
				var displayField=$("#displayfield").val(),
					conditionField=$("#conditionfield").val(),
					sortField=$("#sortfield").val(),
					resultField=$("#resultfield").val();
				
				if(displayField)
					fields.displayField=displayField;
				if(conditionField)
					fields.conditionField=conditionField;
				if(resultField)
					fields.resultField=resultField;
				if(sortField)
					fields.sortField=sortField;
			}
			var url=__ctx +"/oa/form/formDialog/setting.do?dsName=" +dsName +"&objectName=" + objectname +"&istable=" + istable +"&style=" +style +"&id=" + id;
			url=url.getNewUrl();
			DialogUtil.open({
					height:600,
					width: 1000,
					title : '设置列',
					url: url,
					isResize: true,
					//自定义参数
					fields: fields,
					sucCall:function(rtn){
						if(rtn==undefined) return;
						if(rtn.length>0){
							$("#settingobj").val(objectname);
							$("#displayfield").val(rtn[0]);
							$("#conditionfield").val(rtn[1]);
							$("#resultfield").val(rtn[2]);
							$("#sortfield").val(rtn[3]);
							$("#styletemp").val(rtn[4]);
						}		
					}
			});
		}
		function getKeyName(obj){
		    var value=$(obj).val();
		    if(!value)return false;
			Share.getPingyin({
				input:value,
				postCallback:function(data){
					var inputObj=	$("input[name='alias']");
					if(inputObj.val().length<1 || /.*[\u4e00-\u9fa5]+.*$/.test(inputObj.val())){
						inputObj.val(data.output);
					}			
				}
			});
		}
		function goBack(){
		location.href=returnUrl;
		}
	</script>
</head>
<body>
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">编辑通用表单对话框</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
					
					<div class="group"><a class="link back" onclick="goBack();">返回</a></div>
				</div>
			</div>
		</div>
		<div class="panel-body">
				<form id="bpmFormDialogForm" method="post" action="save.do">
					
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">对话框名称: </th>
							<td><input type="text" id="name" name="name" value="${bpmFormDialog.name}"  class="inputText" onblur="getKeyName(this)"/></td>
							<th width="20%">对话框别名: </th>
							<td><input type="text" id="alias" name="alias" value="${bpmFormDialog.alias}"  class="inputText" /><span id="aliasInfo" style="color:red"></span></td>
						</tr>
						<tr>
							<th width="20%">对话框类型: </th>
							<td colspan="3">
								<c:choose>
									<c:when test="${bpmFormDialog.id==0}">
										<input type="radio" name="dialogType" value="0" id="ligerDialogRadio" checked="checked"/>弹出框
										<input type="radio" name="dialogType" value="1" id="selectDialogRadio"/>下拉框
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${bpmFormDialog.dialogType==0}">弹出框</c:when>
											<c:otherwise >下拉框</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th width="20%">显示样式： </th>
							<td colspan="3">
								<c:choose>
									<c:when test="${bpmFormDialog.id==0}">
										<input type="radio" name="style" value="0" id="listRadio" checked="checked"/>列表
										<input type="radio" name="style" value="1" id="treeRadio" />树形
									</c:when>
									<c:otherwise>
										<c:choose>
											<c:when test="${bpmFormDialog.style==0}">列表</c:when>
											<c:otherwise >树形</c:otherwise>
										</c:choose>
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
						<tr>
							<th width="20%">对话框宽度: </th>
							<td><input type="text" id="width" name="width" value="${bpmFormDialog.width}"  class="inputText"/></td>
							<th width="20%">高度: </th>
							<td><input type="text" id="height" name="height" value="${bpmFormDialog.height}"  class="inputText"/></td>
						</tr>
						<tr>
							<th width="20%">是否单选
							 </th>
							<td>
								<input type="radio" name="issingle" value="1" id="isSingleSelect" <c:if test="${bpmFormDialog.issingle==1}">checked="checked"</c:if>>单选
								<input type="radio" name="issingle" value="0" id="isMultipleSelect" <c:if test="${bpmFormDialog.issingle==0}">checked="checked"</c:if>>多选
								
							</td>
							<th width="20%" id="needpage">是否分页: </th>
							<td>
								<input type="radio" name="needpage" value="0" id="nonsort" onclick="selsize();" <c:if test="${bpmFormDialog.needpage==0}">checked="checked"</c:if> >不分页
								<input type="radio" name="needpage" value="1" id="paging" onclick="selsize();" <c:if test="${bpmFormDialog.needpage==1}">checked="checked"</c:if>>分页
								<span style="color:red;<c:if test="${bpmFormDialog.needpage==0}">display:none;</c:if>" id="spanPageSize" name="spanPageSize">
									分页大小：
									  <select id="pagesize" name="pagesize" >
									  		<option value="5" <c:if test="${bpmFormDialog.pagesize==5}">selected="selected"</c:if> >5</option>
											<option value="10" <c:if test="${bpmFormDialog.pagesize==10}">selected="selected"</c:if>>10</option>
											<option value="15" <c:if test="${bpmFormDialog.pagesize==15}">selected="selected"</c:if> >15</option>
											<option value="20" <c:if test="${bpmFormDialog.pagesize==20}">selected="selected"</c:if>>20</option>
											<option value="50" <c:if test="${bpmFormDialog.pagesize==50}">selected="selected"</c:if>>50</option>
											<option value="100" <c:if test="${bpmFormDialog.pagesize==100}">selected="selected"</c:if>>100</option>
											<option value="150" <c:if test="${bpmFormDialog.pagesize==150}">selected="selected"</c:if>>150</option>
											<option value="200" <c:if test="${bpmFormDialog.pagesize==200}">selected="selected"</c:if>>200</option>
											<option value="500" <c:if test="${bpmFormDialog.pagesize==500}">selected="selected"</c:if>>500</option>						  
									  </select>
								 </span>
								
							</td>
						</tr>
						<c:if test="${bpmFormDialog.id==0}">
							<tr>
								<th width="20%">数据源: </th>
								<td>
									<select id="dataSource" name="dsalias">
										<c:forEach items="${dsList}" var="ds">
											<option value="${ds.alias}">${ds.name} </option>
										</c:forEach>
									</select>
								</td>
								<th width="20%">查询表(视图): </th>
								<td>
									<select name="istable" id="istable">
										<option value="1">表</option>
										<option value="0">视图</option>
									</select>
									<input type="text" name="objectname" id="objectname">
									<a href="####" id="btnSearch" class="link search">查询</a>
									
								</td>
							</tr>
						</c:if>
						<tr>
							<th width="20%">对话框字段设置: </th>
							<td colspan="3">
								<a href="####" id="btnSetting" class="link setting" onclick="dialog()">设置列</a>
								<c:choose>
									<c:when test="${bpmFormDialog.id==0}">
										<br>
										<select id="objname" name="objname" size="10" style="width:350px;height:auto;">
										</select>
										<span id="selInfo" name="selInfo" style="color:red"></span>
									</c:when>
									<c:otherwise >
										<input type="hidden"  id="objname" name="objname" value="${bpmFormDialog.objname}" />
									</c:otherwise>
								</c:choose>
							</td>
						</tr>
					</table>
					
					<input type="hidden" id="id" name="id" value="${bpmFormDialog.id}" />
					<input type="hidden" id="styletemp" name="styletemp" value="${bpmFormDialog.style}" />
					<input type="hidden" id="settingobj" value="${bpmFormDialog.objname}" />
					<textarea id="displayfield" name="displayfield"  style="display: none;">
						${bpmFormDialog.displayfield}
					</textarea>
					<textarea  id="conditionfield"  name="conditionfield" style="display: none;">
						${bpmFormDialog.conditionfield}
					</textarea>
					<textarea  id="resultfield"  name="resultfield" style="display: none;">
						${bpmFormDialog.resultfield}
					</textarea>
					<textarea  id="sortfield"  name="sortfield" style="display: none;">
						${bpmFormDialog.sortfield}
					</textarea>
					
				</form>
				<input type="hidden" id="returnUrl"  value="${preUrl}" />
		</div>
</div>
</body>
</html>
