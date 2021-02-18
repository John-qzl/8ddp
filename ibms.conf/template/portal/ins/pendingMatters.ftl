<div class="bd-left fr">
	<div class="block">
		<div class="basic-tabCont-wrap clearFix">
			<#if result?exists&&(result?size> 0)> 
				<ol class="history-list">
				
					<#list result as item>
					
						<li>
							<a href="###" class="msg fl" onclick="javascript:DialogUtil.open({width:1000,height:800,title:'${item.subject}',url:'${ctxPath}/oa/flow/task/toStart.do?taskId=${item.id}',sucCall:function(rtc){}})">${item.subject}</a>
							<span class="name fl">${item.creator}</span>
							<span class="date fr">${item.createTime?string('yyyy-MM-dd')}</span>
						</li>
						
					</#list>
				
				</ol>
			<#else>
				<div>当前没有记录。</div>
		</#if>	
		</div>
	</div>
</div>
