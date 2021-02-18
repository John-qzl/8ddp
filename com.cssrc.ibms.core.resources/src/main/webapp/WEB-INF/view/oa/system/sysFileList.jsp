<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ include file="/commons/include/html_doctype.html"%>
<html>

	<head>
		<%@include file="/commons/include/get.jsp"%>
		<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
		<script type="text/javascript" src="${ctx}/jslib/jquery/plugins/jquery.qtip.js"></script>
		<f:sysFile name="GONGKAI" alias="GONGKAI"></f:sysFile>
		<title>文件列表</title>
		<script type="text/javascript">
			/**
			 * 上传文件 
			 */
			function uploadFile(fileId, upStatus) {
				var url = __ctx + "/oa/system/sysFile/upload.do";
				if(fileId != null && fileId != undefined) {
					url += '?fileId=' + fileId;
				}
				url = url.getNewUrl();

				DialogUtil.open({
					height: 300,
					width: 500,
					title: '系统附件上传',
					url: url,
					isResize: true,
					//自定义参数
					sucCall: function(attachs) {
						location.href = location.href.getNewUrl();
					}
				});

			}

			/**
			 * 下载文件(多个)
			 */
			function downloadFiles() {

				var $arrId = $("input[type='checkbox'][class='pk']:checked");
				var len = $arrId.length;
				if(len == 0) {
					$.ligerDialog.warn("请选择记录！");
				} else {
					//获取需要下载文件Id集合
					var fileId = '';
					$arrId.each(function(index) {
						var obj = $(this);
						if(index < len - 1) {
							fileId += obj.val() + ",";
						} else {
							fileId += obj.val();
						}
					});
					window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId=' + fileId;
				}
			};

			/**
			 * 下载文件(单个)
			 */
			function downloadFile() {
				var $arrId = $("input[type='checkbox'][class='pk']:checked");
				var len = $arrId.length;
				if(len != 1) {
					$.ligerDialog.warn("请选择一条记录下载！");
				} else {
					var fileId = $arrId[0].value;
					AttachMent.downloadFiles(fileId);
				}
			};
		</script>
	</head>

	<body>
		<div class="panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">文件列表</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group" id="search">
							<a class="link search">查询</a>
						</div>
						
						<div class="group">
							<a class="link upload" href="####" onclick="javascript:uploadFile()">上传</a>
						</div>
						
						<div class="group">
							<a class="link reset" onclick="$.clearQueryForm()">重置</a>
						</div>
						
						<div class="group">
							<a class="link download" href="####" onclick="javascript:downloadFile()">下载</a>
						</div>
						
						<div class="group" id="del">
							<a class="link del" action="del.do">删除</a>
						</div>
						
						<div class="group">
							<a class="link download" href="####" onclick="javascript:downloadFiles()">打包下载所有附件</a>
						</div>
					</div>
				</div>
				<div class="panel-search">
					<form id="searchForm" method="get" action="list.do">
						<ul class="row plat-row">
							<li><span class="label">文件名:</span><input type="text" name="Q_fileName_SL" class="inputText" value="${param['Q_fileName_SL']}" /></li>
							<li><span class="label">上传者:</span><input type="text" name="Q_creator_SL" class="inputText" value="${param['Q_creator_SL']}" /></li>
							<li><span class="label">扩展名:</span><input type="text" name="Q_ext_SL" class="inputText" value="${param['Q_ext_SL']}" /></li>
							<div class="row_date">
								<li><span class="label">创建时间 从:</span><input type="text" name="Q_begincreatetime_DL" class="inputText date" value="${param['Q_begincreatetime_DL']}" /></li>
								<li><span class="label">至:</span><input type="text" name="Q_endcreatetime_DG" class="inputText date" value="${param['Q_endcreatetime_DG']}" /></li>
							</div>
						</ul>
					</form>
				</div>
			</div>
			<div class="panel-body">
				<c:set var="checkAll">
					<input type="checkbox" id="chkall" style="text-align: center" />
				</c:set>
				<display:table name="sysFileList" id="sysFileItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1" class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;text-align:center">
						<input type="checkbox" class="pk" name="fileId" value="${sysFileItem.fileId}">
					</display:column>
					<display:column title="序号" media="html" style="width:40px;text-align:center">${sysFileItem_rowNum}</display:column>
					<display:column property="filename" title="文件名称" style="text-align:center"></display:column>
					<c:if test="${isShowSecurity }">
						<display:column title="密级" style="width:150px;text-align:center">
							<c:if test="${(sysFileItem.security == null)||(sysFileItem.security eq '')}">
								<span class="green">${GONGKAI}</span>
							</c:if>
							<c:forEach var="securityChineseMap" items="${securityChineseMap}">
								<c:if test="${sysFileItem.security eq securityChineseMap.key}">
									<span class="green">${securityChineseMap.value}</span>
								</c:if>
							</c:forEach>
						</display:column>
					</c:if>
					<display:column property="creator" title="上传人" style="width:150px;text-align:center"></display:column>
					<display:column title="上传时间" style="width:150px;text-align:center">
						<fmt:formatDate value="${sysFileItem.createtime}" pattern="yyyy-MM-dd HH:mm:ss" />
					</display:column>
					<display:column property="ext" title="文件类型" style="width:80px;text-align:center"></display:column>
					<display:column title="管理" media="html" style="width:10%;text-align:center" class="rowOps">
						<f:a alias="delFile" href="${ctx}/oa/system/sysFile/del.do?fileId=${sysFileItem.fileId}" css="link del">删除</f:a>
						<a href="####" onclick="javascript:AttachMent.previewFile(${sysFileItem.fileId})" class="link preview">预览</a>
						<a href="####" onclick="javascript:uploadFile(${sysFileItem.fileId},1)" class="link update">更新附件版本</a>
						<c:choose>
							<c:when test="${sysFile.delFlag eq 1}">
								<font color="red">已删除</font>
							</c:when>
							<c:otherwise>
								<a href="${ctx}/oa/system/sysFile/download.do?fileId=${sysFileItem.fileId }" class="link download">下载</a>
							</c:otherwise>
						</c:choose>
					</display:column>
				</display:table>
				<ibms:paging tableId="sysFileItem" />
			</div>
		</div>

	</body>

</html>