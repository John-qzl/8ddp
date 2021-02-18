<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/formFun"%>
<%@taglib prefix="ib" uri="http://www.ibms.cn/formUI"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>栏目编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
</head>
<body>
	<ibms:toolbar toolbarId="toolbar1" pkId="${insColumn.colId}" />
	<div id="p1" class="form-outer">
		<form id="form1" method="post">
			<div style="padding: 5px;">
				<input id="pkId" name="colId" class="mini-hidden" value="${insColumn.colId}" />
				<table class="table-detail" cellspacing="1" cellpadding="0">
					<caption>栏目基本信息</caption>
					<tr>
						<th>栏目名称 <span class="star">*</span> ：
						</th>
						<td><input name="name" value="${insColumn.name}" class="mini-textbox" 
						vtype="maxLength:80" required="true" emptyText="请输入栏目名称" style="width: 80%" />
						</td>
						<th>栏目Key <span class="star">*</span> ：
						</th>
						<td><input name="key" value="${insColumn.key}" class="mini-textbox" 
						vtype="maxLength:50" required="true" emptyText="请输入栏目Key" style="width: 80%" />
						</td>
					</tr>
		
					<tr>
						<th>是否启用 <span class="star">*</span> ：</th>
						<td>
						<ib:radioStatus name="enabled" value="${insColumn.enabled}" 
						emptyText="请输入是否启用" required="true" />
						</td>
						
						<th>是否允许关闭 ：</th>
						<td><ib:radioBoolean name="allowClose" 
						value="${insColumn.allowClose}" required="true" />
						</td>
					</tr>
					<tr>
						<th>信息栏目类型 ：</th>
						<td>
						<input name="colType" class="mini-combobox" style="width: 150px;" 
						value="${typeId}" text="${typeName}" textField="name" valueField="typeId" 
						emptyText="请选择..." url="${ctx}/oa/portal/insColType/getAll.do" 
						required="true" allowInput="true" showNullItem="true" nullItemText="请选择..." />
						<a class="mini-button" iconCls="icon-add" onclick="addColType">增加类型</a>
						</td>
						<th>每页记录数 ：</th>
						<td>
						<input name="numsOfPage" value="${insColumn.numsOfPage}" 
						class="mini-spinner" minValue="1" maxValue="1000" vtype="maxLength:10" />
						</td>
					</tr>
					<tr>
						<th>高度(默认为300) ：</th>
						<td>
						<input name="height" value="${insColumn.height}" 
						class="mini-textbox"  vtype="maxLength:50" style="width: 60%"/>
						</td>
					</tr>
				</table>
			</div>
		</form>
	</div>
	<script type="text/javascript">
		function addColType(){
			_OpenWindow({
				url : "${ctx}/oa/portal/insColType/edit.do?",
				title : "新增栏目类型",
				width : 800,
				height : 400,
				ondestroy: function(action) {
		               if (action == 'ok') {
		            	   location.reload();
		               }
		          }
			});
		}
	</script>
	<ibms:formScript formId="form1" baseUrl="oa/portal/insColumn" entityName="com.ibms.oa.info.entity.InsColumn" />
</body>
</html>