<%--
	desc:edit the 子系统功能点
--%>
<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
	<title>编辑子系统功能点</title>
	<%@include file="/commons/include/form.jsp" %>
	<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerComboBox.js"></script>
	<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerWindow.js" ></script>
	<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/IconDialog.js"></script>
	<script type="text/javascript" src="${ctx }/jslib/ibms/oa/system/record/recFunEdit.js"></script>
	<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=recFun"></script>
	<script type="text/javascript">
		var i=100;
		var ctx="${ctx}";
		$(function() {
			__RecFun__.init();
			function showRequest(formData, jqForm, options) {
				return true;
			}
			if(${recFun.alias==null||recFun.alias==""}){
				valid(showRequest,showResponse,1);
				$("#funName").blur(function(){
					var obj=$(this);
					autoPingin(obj);
				});
			}else{
				valid(showRequest,showResponse);
			}
			$("a.save").click(function() {
				var form=$('#recFunForm');
				saveChange();
				var options ={};
				options.success=showResponse;
				if(!form.valid())
					return;
				if(buttonValid()){
					customFormSubmit(options);
				}
			});
		});

		function showResponse(responseText){
			var json=eval("("+responseText+")");
			if(json.result==1){
				var funName=$("#funName").val();
				var isFolder=$("#isFolder").val();
				var icon=$("#icon").val();
				if(json.operate=='add'){
					parent.addResource(json.funId,funName,icon,isFolder);
					$.ligerDialog.confirm('添加节点成功,继续添加吗?','提示信息',function(rtn){
						if(rtn){
							$("#funName,#alias,#defaultUrl").val("");
						}
						else{
							location.href=ctx +"/oa/system/recFun/get.do?funId="+ json.funId;
						}
					});
				}
				else{
					parent.editResource(funName,icon,isFolder);
					$.ligerDialog.success('编辑节点成功!','提示信息');
				}
			}
			else{
				$.ligerDialog.error('出错信息',"编辑节点失败",json.message);
			}
		}



		function selectIcon(){
			 IconDialog({callback:function(iconVal){
				$("#icon").val(iconVal);
				$("#iconImg").attr("value",iconVal);
				$("#iconImg").removeClass();
				$("#iconImg").attr("class",iconVal+" fl");
			}});
		};
		function autoPingin(obj){
			var value=obj.val();
			if(value!=""&&value!=undefined){
				Share.getPingyin({
					input:value,
					postCallback:function(data){
						$("#alias").val(data.output);
					}
				});
			}
		}
		//按钮字段信息保存
		function saveChange(){
			var buttonArr = JSON2.stringify(getButtonArr());
			$('#buttonArr').val(buttonArr);
			return true;
		}
		function getButtonArr(){
			var json = [];
			$("#buttonItem tr[var='buttonTr']").each(function(){
				var me = $(this),obj={};
				obj.unique =$("[var='unique']",me).val();
				obj.desc =$("[var='desc']",me).val();
				obj.name =$("[var='name']",me).val();
				json.push(obj);
			});
			$("#buttonItem tr[var='buttonTr_def']").each(function(){
				var me = $(this),obj={};
				obj.unique =$("[var='unique']",me).val();
				obj.desc =$("[var='desc']",me).val();
				obj.name =$("[var='name']",me).val();
				json.push(obj);
			});
			return json;
		}
		function buttonValid(){
			var res = true;
			var bInfo = $('#buttonArr').val();
		   //按钮信息必填（按钮ID）
			var buttonArr = eval(bInfo);
		  	for(var i=1;i<buttonArr.length+1;i++){
		  		var button = buttonArr[i-1];
		  		var unique = button.unique;
		  		var desc =  button.desc;
		  		var name =  button.name;
		  		if($.isEmpty(unique.trim())){
		  			$.ligerDialog.warn("第"+i+"行的按钮ID不能为空！",'信息');
		  			return false;
		  		}else if($.isEmpty(desc.trim())){
		  			$.ligerDialog.warn("第"+i+"行的按钮名称不能为空！",'信息');
		  			return false;
		  		}else if($.isEmpty(name.trim())){
		  			$.ligerDialog.warn("第"+i+"行的按钮别名不能为空！",'信息');
		  			return false;
		  		}

		  	}
			return res;
		}
		function getIcon(){
			var icon = $('#icon').val()
			var defaultIcon_leaf = "form";
			var defaultIcon_floder = "folder";
			var isFolder = $('#isFolder :checked').val();
			if(icon==""){
				icon = isFolder==1?defaultIcon_floder:defaultIcon_leaf;
			}
			return icon;
		}
		//自定义提交
		function customFormSubmit(options){
			var json={
				funName:$('#funName').val(),
				alias:$('#alias').val(),
				icon:getIcon(),
				defaultUrl:$('#defaultUrl').val(),
				isFolder:$('#isFolder :checked').val(),
				isDisplayInMenu:$('#isDisplayInMenu :checked').val(),
				isOpen:$('#isOpen :checked').val(),
				isNewOpen:$('#isNewOpen :checked').val(),
				sn:$('#sn').val(),
				parentId:$('#parentId').val(),
				funId:$('#funId').val(),
				typeId:$('#typeId').val(),
				buttonArr:$('#buttonArr').val()
			};
			var form = $('<form method="post" action="save.do"></form>');
			var input = $("<input type='hidden' name='json'/>");
			var jsonStr=JSON2.stringify(json);
			input.val(jsonStr);
			form.append(input);
			form.ajaxForm(options);
			form.submit();
		}
		function updateUrlParam(obj){
			var url = $('input[name=defaultUrl]').val();
			var U = url.toURL();
			var type = $(obj).attr('var');
			if($(obj).is(':checked')){
				U.add(type,"["+type+"]");
			}else{
				U.remove(type);
			}
			$('input[name=defaultUrl]').val(U.toString());
		}
	</script>
</head>
<body>
<form id="recFunForm" method="post" action="save.do" style="height:100%">
<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">
				<c:if test="${recFun.funId==null }">添加子系统功能点</c:if>
				<c:if test="${recFun.funId!=null }">编辑子系统功能点</c:if> 
				</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
					
				</div>
			</div>
		</div>
		<div class="panel-body">
				<table id="recFunTable" class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<th width="20%">功能点名称:  <span class="required">*</span></th>
							<td><input type="text"    id="funName" name="funName" value="${recFun.funName}"  class="inputText longInputText"/></td>
						</tr>
						<tr>
							<th width="20%">功能点别名: </th>
							<td><input type="text"   id="alias" name="alias" value="${recFun.alias}"  class="inputText longInputText"/></td>
						</tr>
						
						<tr>
							<th width="20%">功能点图标: </th>
							<td>
								<input type="hidden" id="icon" name="icon" value="${recFun.icon}"  class="inputText"/>
								<%-- <img id="iconImg" alt="" src="${recFun.icon}" <c:if test="${recFun.icon==null}">style="display:none;"</c:if>> --%>
								<div id="iconImg" class="${recFun.icon} fl" value="${recFun.icon}"></div>
								<a class="link detail" href="javascript:selectIcon();">选择</a>
							</td>
						</tr>
						<tr>
							<th width="20%">默认地址: </th>
							<td><input type="text"  id="defaultUrl" name="defaultUrl" style="width:400px" value="${recFun.defaultUrl}"  class="inputText"/>
						</tr>
						<tr>
							<th width="20%">是否有子节点: </th>
							<td>
								<select id="isFolder" name="isFolder">
									<option value="0" <c:if test="${recFun.isFolder==0}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${recFun.isFolder==1}">selected="selected"</c:if>>是</option>
								</select>
							</td>
						</tr>
						<tr style="display:none">
							<th width="20%">显示到菜单: </th>
							<td>
								<select id="isDisplayInMenu" name="isDisplayInMenu">
									<option value="0" <c:if test="${recFun.isDisplayInMenu==0}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${recFun.isDisplayInMenu==1}">selected="selected"</c:if>>是</option>
								</select>
							</td>
						</tr>
						<tr>
							<th width="20%">默认打开: </th>
							<td>
								<select id="isOpen" name="isOpen">
									<option value="0" <c:if test="${recFun.isOpen==0}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${recFun.isOpen==1}">selected="selected"</c:if>>是</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th width="20%">是否打开新窗口: </th>
							<td>
								<select id="isNewOpen" name="isNewOpen">
									<option value="0" <c:if test="${recFun.isNewOpen==0}">selected="selected"</c:if>>否</option>
									<option value="1" <c:if test="${recFun.isNewOpen==1}">selected="selected"</c:if>>是</option>
								</select>
							</td>
						</tr>
						
						<tr>
							<th width="20%">同级排序: </th>
							<td><input type="text" id="sn" name="sn" value="${recFun.sn}"  class="inputText"/></td>
						</tr>
						
						
						<tr style="display: none;">
							<th width="20%">父ID: </th>
							<td><input type="text" id="parentId" name="parentId" value="${recFun.parentId}"  class="inputText"/></td>
						</tr>
							
				</table>
					<input type="hidden" id="funId" name="funId" value="${recFun.funId}" />
					<textarea style="display: none;" id="buttonArr" name="buttonArr" >${fn:escapeXml(recFun.buttonArr)}</textarea>
					<input type="hidden" id="typeId" name="typeId" value="${typeId}" />
					<input type="hidden" id="returnUrl" value="${returnUrl}" />		
				<div class="panel-page">
					<div class="tbar-title">
						<span class="tbar-label">按钮管理</span>
					</div>
					<div class="panel-toolbar">
						<div class="toolBar">
							<div class="group"><a onclick="__RecFun__.setDataTemplateButtonInfo();" class="link add"> 列表按钮初始化 </a></div>
							<div class="group"><a onclick="__RecFun__.addButton();" class="link add">新增定制按钮</a></div>
						</div>	
					</div>
												
					<table id="buttonItem" class="table-grid table-list" id="0" cellpadding="1" cellspacing="1">
						   		<thead>
							   		<tr>
							   			<th width="10%">序号</th>
							   			<th width="10%">按钮ID</th>
							   			<th width="30%">名称</th>
							    		<th width="50%">别名</th>				    	
							    		<th width="10%" style="text-align: center;">管理</th>
							    	</tr>
						    	</thead>
						    	<tbody>
						    	</tbody>
					</table>
				   	 </br>
				   	 </br>
				   	 </br>
				   	 <c:if test="${recFun.buttonArr==[]||recFun.buttonArr==''}">
				   	 	<div id="notSetButton"  width="90%">
					    	当前没有按钮信息
					    </div>
					 </c:if>					
				</div>
		</div>					
			
</div>
<!-- 按钮模板 -->
  <div  id="buttonTemplate"  style="display: none;">
		<table cellpadding="1" cellspacing="1"  class="table-detail">
			<!-- 初始化生成的按钮 -->
			<tr var="buttonTr">
					<td style="text-align: center;" var="no">
	    				${cnt.count+1}
	    			</td>
	    			<td><input readonly="readonly" type="text" var="unique" value=""></td>
		    		<td style="text-align: center;">
	    				<input class="inputText" readonly="readonly" type="text" style="width: 95%;"  var="desc" value="">
	    			</td>
	    			<td style="text-align: center;">
	    				<input class="inputText" readonly="readonly" type="text" style="width: 95%;"  var="name" value="">
	    			</td>
					<td style="text-align: center;">
	    				<a href="javascript:;" class="link del" onclick="__RecFun__.singleDelete(this);">删除</a>
	    			</td>
			</tr>
			<!-- 新增定制按钮 -->
			<tr var="buttonTr_def">
					<td style="text-align: center;" var="no">
	    				${cnt.count+1}
	    			</td>
	    			<td><input readonly="readonly" type="text" var="unique" value=""></td>
		    		<td style="text-align: center;">
	    				<input class="inputText"  type="text" style="width: 95%;"  var="desc" value="">
	    			</td>
	    			<td style="text-align: center;">
	    				<input class="inputText"  type="text" style="width: 95%;"  var="name" value="">
	    			</td>
					<td style="text-align: center;">
	    				<a href="javascript:;" class="link del" onclick="__RecFun__.singleDelete(this);">删除</a>
	    			</td>
			</tr>
		</table>
	</div>
</form>
</body>
</html>
