<div class="abc clearfix item3">
	<a href="javascript:;" onclick='opinionHistory("${instanceId}","${flowNodeId}")'>${title}</a>
	<div class="clearfix">
		<div class="tableNew_con clearfix ">
			<div class="tableNew_con_1">
				<p>处理人:</p>
			</div>
			<div class="tableNew_con_2">
				${taskOpinion.exeFullname}
			</div>
		</div>
		<div class="tableNew_con clearfix ">
			<div class="tableNew_con_1">
				<p>处理时间:</p>
			</div>
			<div class="tableNew_con_2">
				${taskOpinion.endTime?string("yyyy-MM-dd HH:mm:ss")}
			</div>
		</div>
		<div class="tableNew_con clearfix ">
			<div class="tableNew_con_1">
				<p>处理意见:</p>
			</div>
			<div class="tableNew_con_2">
				${taskOpinion.opinion}
			</div>
		</div>
	</div>
</div>
      