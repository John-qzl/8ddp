<script type="text/javascript">
	function doTask(taskId,taskName){
		top['index']._OpenWindow({
	        		title:'任务办理-'+taskName,
	        		height:600,
	        		width:860,
	        		max:true,
	        		url:__ctx+'/bpm/core/bpmTask/toStart.do?fromMgr=true&taskId='+taskId,
	        		ondestroy:function(action){
	        			if(action!='ok') return;
	        			location.reload();
	        		}
	       });
	}
</script>
<div class="panel-body">
	<#if result?exists&&(result?size> 0)> 
		<ul>
			<#list result as item>
			    <li class="task">
			        <p><a href="javascript:doTask('${item.id}','${item.subject}')">
			        <#if item.subject ?length gt 20>
			        ${item.subject?substring(0,20)}...
			        <#else>
			        ${item.subject}
			        </#if>
			        </p>
			        </a>
			        <span>${item.receiveTime?string('yyyy-MM-dd')}</span>
			    </li>
			</#list>
		</ul>
	<#else>
		<div>当前没有记录。</div>
	</#if>
	
</div>
