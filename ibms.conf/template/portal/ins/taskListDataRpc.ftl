	<script type="text/javascript">
	$(function() {
	//任务列表tab滑动
	$('.task-tabMenu-ul li').click(function(){
		var liindex = $('.task-tabMenu-ul li').index(this);
		$(this).addClass('basic-tabMenu-selected').siblings().removeClass("basic-tabMenu-selected");
		$('.basic-tabCont-wrap div.task-list').eq(liindex).fadeIn(150).siblings('div.task-list').hide();
	});
	})
	
	function show(url,Id,subject){
		window.open(url + Id)
	}
	
	</script>
	
	<div class="block-III clear">
		<!--加载tab-->
		<div class="basic-tabMenu-wrap clearFix">
			<ul class="task-tabMenu-ul fl clearFix">
				<#if taskTab?exists>
					<#list taskTab as key>
						<#if key.tabName=='待办'>
							<li class="basic-tabMenu basic-tabMenu-selected" >
						<#else>
							<li class="basic-tabMenu">
						</#if>	
							<a href="javascript:void(0);" type="alreadyMatters">${key.tabName}（${key.dataNum}）</a>
							<span class="base-tool-item-split"></span>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
		<!--加载列表-->
		<div class="basic-tabCont-wrap clearFix">
		
				<#if pendingMatters?exists&&(pendingMatters?size> 0)>
				<div class="task-list show" style="display:block;">
					<ol >
						<#list pendingMatters as data>
							<li class="nth-of-typeEeven">
								<a href="javascript:show('${rpcUrl}/oa/flow/task/toStart.do?taskId=','${data.id}','${data.subject}')" class="fl msg">${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createTime?string("yyyy-MM-dd HH:mm:ss")}</span>
								<#if data.hasRead==0>
									<span class="fr unread">未读</span>
								<#else>
									<span class="fr readed">已读</span>
								</#if>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:block;">待办任务当前没有记录</div>
				</#if>
				
				
				<#if myDraft?exists&&(myDraft?size> 0)>
				<div class="task-list show" style="display:none;">
					<ol >
						<#list myDraft as data>
							<li class="nth-of-typeEeven">
								<a href="###" class="fl msg"> ${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">草稿当前没有记录</div>
				</#if>
				
				
				<#if alreadyMatters?exists&&(alreadyMatters?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list alreadyMatters as data>
							<li class="nth-of-typeEeven">
								<a href="javascript:show('${rpcUrl}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')"  class="fl msg" >${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">已办任务当前没有记录</div>
				</#if>
				
				
				<#if completedMatters?exists&&(completedMatters?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list completedMatters as data>
							<li class="nth-of-typeEeven">
								<a href="javascript:show('${rpcUrl}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')" class="fl msg">${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">办结任务当前没有记录</div>
				</#if>
				

				<#if myAccordingMatter?exists&&(myAccordingMatter?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list myAccordingMatter as data>
							<li class="nth-of-typeEeven">
								<a class="fl msg" href="javascript:show('${rpcUrl}/oa/flow/taskExe/get.do?id=','${data.id?c}','${data.subject}')">${data.subject}</a>
								<a href="###" class="fl uname">${data.ownerName}</a>
								<span class="fl date">${data.cratetime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">转办代理当前没有记录</div>
				</#if>
				
				
				<#if MyProCopyto?exists&&(MyProCopyto?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list MyProCopyto as data>
							<li class="nth-of-typeEeven">
								<a  class="fl msg" href="javascript:show('${rpcUrl}/oa/flow/proCopyto/get.do?copyId=','${data.copyId?c}','${data.subject}')">${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
								<#if data.isReaded==0>
									<span class="fr unread">未读</span>
								<#else>
									<span class="fr readed">已读</span>
								</#if>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">抄送转发当前没有记录</div>
				</#if>
				
				<#if myRequest?exists&&(myRequest?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list myRequest as data>
							<li class="nth-of-typeEeven">
								<a class="fl msg" href="javascript:show('${rpcUrl}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')">${data.subject}</a>
								<a href="###" class="fl uname">${data.creator}</a>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">我的请求当前没有记录</div>
				</#if>

		</div>		
			
		
		
	</div>
