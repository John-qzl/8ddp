<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>待办任务</title>
<%@include file="/commons/include/get.jsp"%>
<f:link href="tree/zTreeStyle.css"></f:link>
<link rel="stylesheet" href="${ctx}/jslib/jquery/multiple-select.css" />
<!--初始化数据字典的样式-->
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerComboBox.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/dicCombo.js"></script>

<script type="text/javascript" src="${ctx}/jslib/tree/jquery.ztree.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/ajaxgrid.js"></script>
<!--所有列表和按钮操作的js在 /jslib/ibms/ajaxgrid.js中-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/ReportUtil.js"></script>
<!-- 报表打印浏览js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<!-- 自定义表单验证插件 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/Export.js"></script>
<!-- 初始化导出按钮事件 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/system/SysDialog.js"></script>
<!-- 组织选择器 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorUtil.js"></script>
<!-- *选择器帮助类 -->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/SelectorFKShowUtil.js"></script>
<!-- *查询区域有外键字段的情况-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<!-- *查询区域有外键字段的情况----弹出自定义查询框-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/QueryAndListUtil.js"></script>
<!-- *查询区域事件、列表的脚本事件-->
<script type="text/javascript" src="${ctx}/jslib/util/fileUtil.js"></script>
<!-- *文件下载js-->
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ComplexDetail.js"></script>
<!-- *复杂表单初始化-->
<script type="text/javascript" src="${ctx}/jslib/jquery/multiple-select.js"></script>
<!-- 下拉多选框 -->

<script type="text/javascript">
	$(function() {
		handleAjaxSearchKeyPress();//初始化查询 处理回车查询  方法在ajaxgrid.js中
	});
	function showDetail(obj) {
		var title = obj.getAttribute('title');
		var url = "${ctx}/oa/flow/processRun/" + $(obj).attr("action");
		DialogUtil.open({
			height : screen.availHeight * 0.7,//2017-05-15：设置弹出窗口的高度(rzq)
			width : screen.availWidth * 0.7,//2017-05-15：设置弹出窗口的高度(rzq)
			title : title,
			url : url,
			isResize : true,
		});
	};
	function executeTask(taskId, obj) {
		var url = "${ctx}/oa/flow/task/toStart.do?taskId=" + taskId;
		DialogUtil.open({
			height : screen.availHeight - 35,
			width : screen.availWidth - 5,
			url : url,
			sucCall : function(rtn) {
				location.href = location.href.getNewUrl();
			}
		});
		var sub = $(obj);
		var class1 = sub.attr("class");
		if (class1 == 'message close-message') {
			sub.attr("class", "message open-message");
		}
	}
	//数据列表页面“查询”按钮事件
	function handlerSearchAjax(obj) {
		var form = $(obj).closest("div.panel-top").find(
				"form[name='searchForm']");
		$(form).submit();
	}
	//导出
	function exportExcel(){
		var aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
		if(aryId.length==0){
			var form = $("#searchForm");
			form=form.clone();
			form.attr("id","downloadform");
			form.attr("style","display:none");
			var url="${ctx}/oa/flow/userTask/downloadAllPendingMatters.do";
			form.attr("action",url);
			form.submit();
		}else{
			var headers=new Array();
			$("input[id='chkall']").each(function(){
				var i=0;
				$(this).parents("tr").find("th").each(function(){
					if(i!=0){
						headers.push($(this).text().trim());
					}
					i++;
				});
			});
			var headersvalue=headers.join(",");
			
			var tasks=new Array();
			$("input[name='f_taskId']:checked").each(function(){
				var task=new Array();
				var i=0;
				$(this).parents("tr").find("td").each(function(){
					if(i!=0){
						task.push($(this).text().trim());
					}
					i++;
				});
				tasks.push(task.join("##"));
			});
			var val=tasks.join(",,");
	
			var fileName="已选待办任务详情";
			
			var form=$("<form>");
			
			var taskInput=$("<input>");
			taskInput.attr("name","tasksValue");
			taskInput.attr("value",val);
			
			var headerInput=$("<input>");
			headerInput.attr("name","headersValue");
			headerInput.attr("value",headersvalue);
			
			var fileNameInput=$("<input>");
			fileNameInput.attr("name","fileName");
			fileNameInput.attr("value",fileName);
			
			form.append(taskInput);
			form.append(headerInput);
			form.append(fileNameInput);
			form.attr("action",__ctx+"/oa/flow/task/exportSelectData.do");
			form.attr("method","post");
			$('body').append(form);			
			form.attr("style","display:none");
			form.submit();
		}
	}
	//追回
	function redo(runId) {
		FlowUtil.recover({
			runId : runId,
			backToStart : 0,
			callback : function() {
				window.location.href = "alreadyMattersList.do";
			}
		});
	}
</script>
</head>
<body style="overflow: hidden;">
	<div id="content" style="height: 100%;">${html}</div>
</body>
</html>