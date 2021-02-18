<%--
	用户明细
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>用户表明细</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/formdata.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/subform.js"></script>
<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerTab.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/FlexUploadDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/HtmlUploadDialog.js" ></script>
<script type="text/javascript">
	var dialog =null;
	try{
		dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	}catch(e){
	}
	$(function() {
		var h = $('body').height();
		$("#tabMyInfo").ligerTab({
		});
	});
	
	$(function() {
		$("a.save").click(function() {
			submitForm();
		});
	});
	//提交表单
	function submitForm() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#sysUserForm').form();
		frm.ajaxForm(options);
		if (frm.valid()) {
			frm.submit();
		}
	}

	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog
					.confirm(
							obj.getMessage() + ",是否继续操作",
							"提示信息",
							function(rtn) {
								if (rtn) {
									window.location.reload();
								} else {
									window.location.href = "${returnUrl}";
								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(), "提示信息");
		}
	}
	
	//添加个人照片
	function picCallBack(array){
		if(!array && array!="") return;
		var fileId=array[0].fileId,
			fileName=array[0].fileName;
		var path= __ctx + "/oa/system/sysFile/getFileById.do?fileId=" + fileId;
		if(/\w+.(png|gif|jpg)/gi.test(fileName)){
			$("#photo").val("/oa/system/sysFile/getFileById.do?fileId=" + fileId);
			$("#personPic").attr("src",path);
		}
			
		else
			$.ligerDialog.warn("请选择*png,*gif,*jpg类型图片!");
				
	};
	//上传照片
	function addPic(){
		var uploadType ;
		/* $.ajax({
			type : "get",
			async : false,
			dataType : "json",
			url : __ctx + "/oa/system/sysFile/uploadType.do",
			success : function(data) {
				uploadType =data.uploadType;
			},
			error : function(msg) {
				//alert(msg);
			}
		}); */
		if(uploadType=="flash"){
			DirectUploadDialog({isSingle:true,callback:picCallBack});
		}else{
			HtmlUploadDialog({max:1,size:10,callback:picCallBack});
		}
	};
	//删除照片
	function delPic(){
		$("#photo").val("");
		$("#personPic").attr("src","${ctx}/styles/images/default_image_male.jpg");
	};
	
	
</script>
</head>
<body>
	<div class="panel-toolbar">
		<div class="toolBar">
			<div class="group"><a class="link save" id="dataFormSave" href="javascript:;">保存</a></div>
			
			<div class="group"><a class="link back" href="${returnUrl}">返回</a></div>
		</div>
	</div>
	<form id="sysUserForm" method="post" action="updateCommon.do">
		<div id="tabMyInfo" style="overflow: hidden; position: relative;">
			<div title="基本信息" tabid="userdetail">
				<div class="panel-detail">
					<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
						<tr>
							<td rowspan="11" align="center" width="26%">
							<div class="person_pic_div">
								<p><img id="personPic" src="${ctx}/${pictureLoad}" style="height: 480px; width:100%;" alt="个人相片" /></p>
							</div>
							</td>
							<th width="18%">帐   号:<%-- <span class="required red">*</span></th> 禁止用户修改 20200819 by zmz--%>
							<td >
								${sysUser.username}
							</td>
						</tr>						
						<tr>
						    <th>用户姓名:<%-- <span class="required red">*</span></th> 禁止用户修改 20200819 by zmz --%>
							<td >${sysUser.fullname}</td>
						</tr>
						<tr>
							<th>密   码:<%-- <span class="required red">*</span></th> 密码为空则默认为不改密码 20200819 by zmz--%>
							<td><input type="word" id="password" placeholder="为空则不修改" name="password"  style="width:240px !important" class="inputText"/></td>
						</tr>
						<tr>
							<th>用户性别: </th>
							<td>
							<select name="sex" class="select" style="width:245px !important">
								<option value="1" <c:if test="${sysUser.sex==1}">selected</c:if> >男</option>
								<option value="0" <c:if test="${sysUser.sex==0}">selected</c:if> >女</option>
							</select>						
							</td>
						</tr>						
						<tr>
							<th>入职时间: </th>
							<td>
								<fmt:formatDate value='${sysUser.accessionTime}' pattern='yyyy-MM-dd'/>
							</td>
						</tr>												
						<tr>
							<th>是否锁定: </th>
							<td >								
								<c:if test="${sysUser.delFlag==0}">未锁定</c:if>
								<c:if test="${sysUser.delFlag==1}">已锁定</c:if>
							</td>				  
						</tr>
						
						<tr>
						   <th>当前状态: </th>
							<td>
								<c:if test="${sysUser.status==1}">激活</c:if>
								<c:if test="${sysUser.status==0}">禁用</c:if>
								<c:if test="${sysUser.status==-1}">删除</c:if>
							</td>								
						</tr>						
						<tr>
						   <th>邮箱地址: </th>
						   <td ><input type="text" id="email" name="email" value="${sysUser.email}" style="width:240px !important" class="inputText"/></td>
						</tr>
						
						<tr>
							<th>手   机: </th>
							<td ><input type="text" id="mobile" name="mobile" value="${sysUser.mobile}" style="width:240px !important" class="inputText"/></td>						   
						</tr>
						
						<tr>
						    <th>电   话: </th>
							<td ><input type="text" id="phone" name="phone" value="${sysUser.phone}"  style="width:240px !important" class="inputText"/></td>
						</tr>
												
					</table>
					<input type="hidden" name="userId" value="${sysUser.userId}" />
					<input type="hidden" id="photo" name="photo" value="${sysUser.photo}" />
				</div>
			</div>
		</div>
	</form>	
</body>
</html>
