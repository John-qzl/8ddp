<script type="text/javascript">
	function show(newId,subject){
		top['index']._OpenWindow({
				url : __ctx+"/oaortal/insNews/get.do?permit=no&pkId=" + newId,
				title : subject,
				width : 850,
				height : 800
		});		
	}
</script>


<div class="bd-right fr">
	<div class="block">
		<div class="basic-tabCont-wrap clearFix">
			<#if result?exists> 
				<ul class="item-list-box">
					<#list result as item>
					
						<li class="item-li">
							<a  class="fl item-li-link" href="javascript:show('${item.newId?c}','${item.subject}')">${item.subject}</a>
							<span class="fr item-li-date">${item.createTime?string('yyyy-MM-dd')}</span>
						</li>
						
					</#list>
				</ul>
			</#if>
		</div>
	</div>
</div>