<script>
function goPendingTask(ctx,dataId) {
	var url=ctx+'/oa/flow/task/toStart.do?taskId='+dataId;
	DialogUtil.open({
		height : screen.availHeight - 35,
		width : screen.availWidth - 5,
		url : url,
		sucCall : function(rtn) {
			location.href = location.href.getNewUrl();
		}
	})
}

</script>

<div class="widget-box border " >
	<div class="widget-header">
		<h4 class="widget-title"><i class="ht-icon fa fa-tasks"></i>${model.title}</h4>
		<div class="widget-toolbar">
			<a href="javascript:void(0);" data-action="more" data-url="${model.url}" data-title="${model.title}"  data-placement="bottom" title="更多">
				<i class="ht-icon fa fa-align-justify"></i>
			</a>
			<a href="javascript:void(0);" data-action="reload" data-placement="bottom" title="刷新">
				<i class="ht-icon fa fa-refresh"></i>
			</a>
			<a href="javascript:void(0);" data-action="collapse"  data-placement="bottom" title="折叠">
				<i class="ht-icon fa fa-chevron-up"></i>
			</a>
		</div>
	</div>
	<div class="widget-body">
		<div  class="widget-scroller" data-height="${model.height}px">
			<#if data?exists> 
				<ul class="widget-list list-unstyled"  >
					<#list data as data>
							<!--<li class="clearfix" onclick="javascript:jQuery.openFullWindow('${ctx}/oa/flow/task/toStart.do?taskId=${data.id}')">-->
							<li class="clearfix" onclick="goPendingTask('${ctx}','${data.id}')">
								<div class="pull-left">
									<p></p><h5><strong> ${data.subject}</strong></h5>
                                  	<p><i class="fa fa-clock-o"></i> <abbr class="timeago" title="">${data.createTime?string("yyyy-MM-dd HH:mm:ss")}</abbr></p>
								</div>
								<div class="text-right pull-right">
									<h4 class="cost"> ${data.creator}</h4>
									<p>
										<span class="label label-success arrow-in-right"><i class="fa fa-check"></i> 待办</span>
									</p>
								</div>
							</li>
					</#list>
				</ul>
			<#else>
				<div class="alert alert-info">当前没有记录。</div>
			</#if>
		</div>
	</div>
</div>
<#--
代办任务模板配置
代办事项显示内容说明：
流程标题：${data.subject}
上一步发起人账号：${data.preUserName}
上一步发起人名称：${data.preUserFullName}
上一步任务提交时间：${data.preSubmitTime}
当前节点名称：${data.curNodeName}
当前节点到期时间：${data.dateDue}
-->