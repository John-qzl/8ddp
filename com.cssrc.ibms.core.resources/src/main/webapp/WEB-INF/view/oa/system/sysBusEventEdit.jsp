<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%@ taglib prefix="ht" tagdir="/WEB-INF/tags/wf"%>
<html>
<head>
	<title>业务数据保存设置</title>
	<%@include file="/commons/include/form.jsp" %>

	<f:link href="tree/zTreeStyle.css"></f:link>
	<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/javacode/codemirror.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/javacode/InitMirror.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Permission.js"></script>
	<script type="text/javascript">
		var returnUrl="${returnUrl}";	
		
		var dialog = frameElement.dialog; 
		
		var metaTree=null;
		
		var setting = {
			data: {
				key:{
					name:"comment",
					title:"name"
				},
				simpleData: {
					enable: true
				}
			},
			callback: {
				onDblClick: zTreeOnDblClick,
				onClick: zTreeOnClick
			}
		};
		
		function zTreeOnClick(event, treeId, node){
			
			var name=node.name;
			var isTable=node.isTable;
			var isExternal=node.isExternal;
			var isMain=node.isMain;
			
			$(".chkBox").hide();
			//子表字段
			if(isMain==0 && isTable==0){
				$("#subTableField").show();
			}
			else if(isMain==1 && isTable==0){
				$("#mainFieldChk").show();
			}
		}
		
		function zTreeOnDblClick(event, treeId, node){
			var name=node.name;
			var isTable=node.isTable;
			var isExternal=node.isExternal;
			var isMain=node.isMain;
			var tableName=node.tableName;
			var code="";
			if(blnJsTab){
				
			}
			else{
				//data.getMainField
				//为表
				if(isTable==1){
					if(isMain==1){
						code=(isExternal==1)?name:"${tablePre}" +name;
					}
					else{
						code="SubTable subTable= data.getSubTableByName(\""+name+"\");";
						name=(isExternal==1)?name:"${fieldPre}" +name;
						
					}
				}
				//字段
				else{
					//主表
					if(isMain==1){
						name=(isExternal==1)?name:"${fieldPre}" +name;
						
						var v=$('input[name="rdoMain"]:checked').val();
						if(v==2){
							code="String "+name+"=data.getMainField(\""+name+"\");";
						}
						else{
							code=name;
						}
					}
					else{
						name=(isExternal==1)?name:"${fieldPre}" +name;
						var v=$('input[name="rdoSubField"]:checked').val();
						if(v==2){
							code="List<Map<String, Object>> rows= subTable.getDataList();"+
								" for(Map<String, Object> row:rows){\r\n" +
								"Object obj=row.get(\""+name+"\");\r\n" +
								"}"
						}
						else{
							code=name;
						}
					}
				}
			}
			InitMirror.editor.insertCode(code);
		}
		
		var blnJsTab=true;
		
		$(function() {
				$("a.save").click(function() {
					submitForm();
				});
				
				$("#defLayout").ligerLayout({ leftWidth:210,height:'100%',allowLeftResize:false});
				
				$("#tabSubEvent").ligerTab({onAfterSelectTabItem:function(tabId){
					blnJsTab=("javaTab"!=tabId);
				}});
				
				initTree();
		});
		
		
		function initTree(){
			var url=__ctx +"/oa/form/formTable/getJsonByTableId.do";
			url = url.getNewUrl();
			var params={tableId : "${param.tableId}"};
			$.post(url, params, function(data){
					var obj=eval("("+data+")");
				
					setIcon(obj);
					
					metaTree=$.fn.zTree.init($("#tableMetaTree"), setting, obj);
			});
		}
		
		var iconFolder = __ctx + '/styles/tree/';
		function setIcon(json){
			json.icon = iconFolder + 'table.png';
			var aryChild=json.children;
			for(var i=0;i<aryChild.length;i++){
				var obj=aryChild[i];
				if(obj.isTable==1){
					setIcon(obj);
				}
				else{
					obj.icon= iconFolder + obj.dataType + '.png';
				}
			}
		}
		
		
		//提交表单
		function submitForm(){//
			var options={};
			if(showResponse){
				options.success=showResponse;
			}
			var frm=$('#frmSubmit').form();
			frm.ajaxForm(options);
			if(frm.valid()){
				InitMirror.save();
				frm.submit();
			}
		}
		
		function showResponse(responseText) {
			var obj = new com.ibms.form.ResultMessage(responseText);
			if (obj.isSuccess()) {
				$.ligerDialog.success(obj.getMessage(),"提示信息", function() {
					//dialog.close();
					dialog.close();
				});
			} else {
				$.ligerDialog.error(obj.getMessage(),"提示信息");
			}
		}
		
		//展开收起
		function treeExpandAll(type){
			metaTree = $.fn.zTree.getZTreeObj("tableMetaTree");
			metaTree.expandAll(type);
		};
	</script>
</head>
<body>
	<div class="panel">
	<div id="defLayout">
         <div position="left" title="表单字段" style="float:left;width:100%;overflow: scroll;">
         	<div class="tree-toolbar">
				<span class="toolBar">
					<div class="group"><a class="link expand" id="treeExpandAll" href="javascript:treeExpandAll(true)" >展开</a></div>
					
					<div class="group"><a class="link collapse" id="treeCollapseAll" href="javascript:treeExpandAll(false)" >收起</a></div>
				</span>
			</div>
			<ul id="tableMetaTree" class="ztree" style="overflow:auto;height:480px"></ul>
         </div>
         <div position="center">
        	<form id="frmSubmit" style="height:100%" method="post" action="save.do">
				<div class="panel">
					<div class="panel-top">
						<div class="tbar-title">
						    <c:choose>
							    <c:when test="${sysBusEvent.id !=null}">
							        <span class="tbar-label">编辑sys_bus_event</span>
							    </c:when>
							    <c:otherwise>
							        <span class="tbar-label">添加sys_bus_event</span>
							    </c:otherwise>			   
						    </c:choose>
						</div>
						<div class="panel-toolbar">
							<div class="toolBar">
								<div class="group"><a class="link save" id="dataFormSave" href="####">保存</a></div>
								<div class="group"><a class="link close"  href="####" onclick="dialog.close();">关闭</a></div>
							</div>
						</div>
					</div>
					<div class="panel-body">
						<div id="tabSubEvent" >
						
							<div tabid="jsTab" title="Javascript前台脚本">
								<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
									
										<tr>
											<th width="20%">JS前置代码: </th>
											<td>
												<textarea id="jsPreScript" name="jsPreScript" codemirror="true" mirrorheight="200px" rows="10" cols="80" >${sysBusEvent.jsPreScript}</textarea>
											</td>
										</tr>
										<tr>
											<th width="20%">JS后置脚本: </th>
											<td>
												<textarea id="jsAfterScript" name="jsAfterScript" codemirror="true" mirrorheight="200px" rows="10" cols="80" >${sysBusEvent.jsAfterScript}</textarea>
											</td>
										</tr>
										
								</table>
							</div>
							<div tabid="javaTab" title="Java后台脚本">
								<ul>
									<li class="chkBox" id="subTableField" style="display: none;"><input name="rdoSubField" type="radio" value="1" checked="checked"/>字段<input name="rdoSubField" type="radio" value="2"/>遍历字段</li>
									<li class="chkBox" id="mainFieldChk" style="display: none;"><input name="rdoMain" type="radio" value="1" checked="checked"/>字段<input name="rdoMain" type="radio" value="2"/>代码</li>
								</ul>
								<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
									
										
										<tr>
											<th width="20%">JAVA前置代码: </th>
											<td>
												<textarea id="preScript" name="preScript" codemirror="true" mirrorheight="200px" rows="10" cols="80" >${sysBusEvent.preScript}</textarea>
											</td>
										</tr>
										<tr>
											<th width="20%">JAVA后置代码: </th>
											<td>
												<textarea id="afterScript" name="afterScript" codemirror="true" mirrorheight="200px" rows="10" cols="80" >${sysBusEvent.afterScript}</textarea>
											</td>
										</tr>
								</table>
							</div>
							<div tabid="helpTab" title="帮助">
								<table class="table-detail" cellpadding="0" cellspacing="0" border="0" type="main">
									
										
										<tr>
											<th width="20%">Javascript代码: </th>
											<td>
												前置Javascript需要返回boolean值。
												后置脚本在数据提交后进行处理。
											</td>
										</tr>
										<tr>
											<th width="20%">JAVA代码: </th>
											<td>
<pre>
在java代码中可以使用data对象，data为BpmFormData的对象实例。
BpmFormData结构如下：<br>
import com.ibms.oa.model.form.SubTable;
import com.ibms.oa.model.form.BpmFormData;
import java.util.*;
SubTable subTable= data.getSubTableByName(String tableName);
//子表遍历
List&lt;Map&lt;String, Object>> rows= subTable.getDataList();
for(Map&lt;String, Object> row:rows){
	//设置值
	row.put("key",123);
	
}

//判断数据是否存在
scriptImpl.validDataExist(data,"字段名称");
</pre>				
				
											</td>
										</tr>
								</table>
							</div>
						</div>
							<input type="hidden" name="id" value="${sysBusEvent.id}" />		
							<input type="hidden" id="formkey" name="formkey" value="${sysBusEvent.formkey}" />			
					</div>
				</div>
			</form>
         </div>
	</div>
</div>
</body>
</html>
