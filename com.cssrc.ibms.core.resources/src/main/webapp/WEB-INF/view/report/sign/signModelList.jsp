<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>报表模板管理</title>
<%@include file="/commons/include/get.jsp"%>
<f:link href="signal/imaList.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/syssign/userSign.js"></script>
<f:sysparam paramname="fdfsserver" alias="fdfsserver"></f:sysparam>

<script type="text/javascript">
	function checkOk(obj) {
		var ck = $(obj).find("input[type='checkbox']");
		if (!ck) {
			var check = ck[0];
			if ($(check).attr("checked")) {
				$(":checkbox[id!=" + $(obj).attr("id") + "]").attr("checked",
						false);
			}
		}
	}

	var dialog;
	$(function() {
		dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
	});
	function ok() {
		var chk_value = [];
		var chk_id = [];

		$("input[type='checkbox']:checked").each(function() {
			chk_value.push($(this).val());
			chk_id.push($(this).attr("id"));
		})
		if (chk_value.length != 1) {
			$.ligerDialog.warn("请选择一条印章");
		} else {
			var rtn = new Object();
			rtn.img = chk_value[0];
			rtn.signId = chk_id[0];
			dialog.get("succcall")(rtn);
			dialog.close();
		}
	}
	function close() {
		dialog.close();
	}
</script>
</head>
<body>

	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">签章列表管理</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link" id="btnUpd" onclick="ok()">
								
								确定
							</a>
						</div>
						
						<div class="group">
							<a class="link" onclick="close()">
								
								取消
							</a>
						</div>

					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">

			<div class="w oh clearFix">
				<div class="enterprise-wrap clearFix">
					<c:forEach items="${signList}" var="item">
						<a class="enterprise fl" href="javascript:checkOk(this);">
							<div class="enterprise-img fl">
								<label for="${item.id}">
									<c:if test="${item.isDefault==1}">
										<input id="${item.id}" name="defaultSign" type="checkbox" value="${item.imageSrc}${item.imgPath}" checked="checked" />
									</c:if>
									<c:if test="${item.isDefault!=1}">
										<input id="${item.id}" name="defaultSign" type="checkbox" value="${item.imageSrc}${item.imgPath}"/>
									</c:if>
								</label>
								<img src="${item.imageSrc}${item.imgPath}" />
							</div>
							<span class="fuwu fl">印章名称：${item.name}</span>
							<span class="fuwu fl">印章描述：${item.desc}</span>
						</a>
					</c:forEach>
					<a class="enterprise fl" href="javascript:addSign('${userId}');">
						<li class="zc_upLoadSpase zc_right01">
							<div class="upImg zc_auto">
								<p class="pr">上传印章图片</p>
							</div>
						</li>
					</a>
				</div>
			</div>

		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->

</body>
</html>


