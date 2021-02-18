<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 报表参数</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript"
	src="${ctx}/servlet/ValidJs?form=signModel"></script>
<script type="text/javascript">
var dialog;
$(function() {
	dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	function showRequest(formData, jqForm, options) { 
		return true;
	} 
	if(${signModel.id ==null}){
		valid(showRequest,showResponse,1);
	}else{
		valid(showRequest,showResponse);
	}
	$("a.save").click(function() {
		var path = $("#imgPath").val();
		var passw=$("#passw").val();
		var passwok=$("#passwok").val();
		if(passwok!=passw){
			$.ligerDialog.warn("密码不一致");
			return;
		}
		if(path==''){
			$.ligerDialog.warn("请上传模板文件");
			return;
		}
		$('#signModelForm').submit(); 
		
	});
});
function returnBack(returnUrl){
	dialog.get("reload")();
	dialog.close();
}
function setFileName(name){
	$("#imgPath").val(name);
}

function drawSign(){
	var dialogWidth=900;
	var dialogHeight=700;
	var conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
	var winArgs="dialogWidth="+conf.dialogWidth+"px;dialogHeight="+conf.dialogHeight
		+"px;help=" + conf.help +";status=" + conf.status +";scroll=" + conf.scroll +";center=" +conf.center;
	if(!conf.isSingle)conf.isSingle=false;
	var url=__ctx + '/oa/system/sign/drawSign.do?isSingle='+ conf.isSingle;
	url=url.getNewUrl();
	var that =this;
	DialogUtil.open({
        height:conf.dialogHeight,
        width: conf.dialogWidth,
        title : '添加印章模型',
        url: url, 
        userId:conf.userId,
        isResize: true,
        succcall:function(rtn){
        	$("#imgPath").val(rtn.img);
        	$("#pathType").val(2);
        }
    });	
}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">添加印章模型</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="javascript:;">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="javascript:returnBack();">取消</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<input type="hidden" id="returnUrl" name="returnUrl"
				value="${ctx}/oa/system/sysUser/edit.do?userId=${signModel.userId}" />
			<form id="signModelForm" method="post" action="save.do"
				enctype="multipart/form-data">
				<input type="hidden" id="id" name="id" value="${signModel.id}"/>
				<input type="hidden" id="userId" name="userId" value="${signModel.userId}"/>
				<input type="hidden" id="code" name="code" value="${signModel.code}"/>
				<input type="hidden" id="imgPath" name="imgPath" value="${signModel.imgPath}"/>
				<input type="hidden" id="pathType" name="pathType" value="${signModel.pathType}"/>
				<input type="hidden" id="isDefault" name="isDefault" value="${signModel.isDefault}"/>
				<input type="hidden" id="orgId" name="orgId" value="${signModel.orgId}"/>
				<input type="hidden" id="type" name="type" value="${signModel.type}"/>
				<input type="hidden" id="startDate" name="startDate" value="${signModel.startDate}"/>
				<input type="hidden" id="endDate" name="endDate" value="${signModel.endDate}"/>
				<input type="hidden" id="status" name="status" value="${signModel.status}"/>
				<input type="hidden" id="version" name="version" value="${signModel.version}"/>
				
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0"
						border="0">
						<tr>
							<th width="20%">印章名称: <span class="required">*</span></th>
							<td><input type="text" id="name" name="name"
								value="${signModel.name}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">印章描述:</th>
							<td><input type="text" id="desc" name="desc"
								value="${signModel.desc}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">印章密码:</th>
							<td><input type="password" id="passw" name="passw"
								value="${signModel.passw}" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">密码确认:</th>
							<td><input type="password" id="passwok" name="passwok"
								value="" class="inputText" /></td>
						</tr>
						<tr>
							<th width="20%">印章图片:<span class="required">*</span></th>
							<td>
							<input type="file" id="signImg" name="signImg" onchange="setFileName(this.value)"  />
							<a href="javascript:drawSign();">手写签名</a>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>
