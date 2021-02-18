<#setting number_format="0">	
	<script type="text/javascript">
	$(function() {
		//任务列表tab滑动
		$('.task-tabMenu-ul li').click(function(){
			window.parent.$("#task_url").val($(this).attr("task_url"));
			window.parent.$("#task_name").val($(this).attr("task_name"));
			window.parent.$("#task_id").val($(this).attr("task_id"));
			var liindex = $('.task-tabMenu-ul li').index(this);
			$(this).addClass('basic-tabMenu-selected').siblings().removeClass("basic-tabMenu-selected");
			$('.basic-tabCont-wrap div.task-list').eq(liindex).fadeIn(150).siblings('div.task-list').hide();
		});
	
		//代办任务列表鼠标滑动显示提示

		var msg = $('a.amsg','ol');
		
		for (var i = 0; i < msg.length; i++) {
			var element = msg[i];
			var top = $(element).offset().top+20;
			var left = $(element).offset().left;
			var taskId = $(element).attr('paramid');
			var height = $(document).height();
			var offS = (top+270<height)?(top+20):(top-200);
			getInfo($(element),top,left,taskId)
		}
		var boxHeight = msg.parents('div.task-list').height();
		msg.hover(function () {
			var scrollTop = $(this).parents('.rollbar-content').css('top');
			scrollTop = parseInt(scrollTop.substring(0,scrollTop.length-2));
			var top = $(this).offset().top-50;
			var curTop = boxHeight-top;
			if (curTop<200) {
				$(this).find('.portal-tips').css('top',top-200-scrollTop+'px');
			}
		})
	})

	


	
	function show(url,Id,subject){
		debugger;
		/*
		top['index']._OpenWindow({
			url : url + Id,
			title : subject,
			height : window.top.document.documentElement.clientHeight,
			width : window.top.document.documentElement.clientWidth
		});
		*/
		DialogUtil.open({
    		height: window.top.document.documentElement.clientHeight*0.9,
    		width: window.top.document.documentElement.clientWidth*0.9,
    		title: subject,
    		url: url + Id, 
    		isResize: true,
    		//自定义参数
    		sucCall:function(rtn){
    			if(rtn){
    				//window.location.reload(true);
    			}
    		}
    	});
    			
	}
	
	
	function checkFormChange(runId,defId,subject){
		var height=screen.availHeight*0.7;
		var width=screen.availWidth*0.7;
<!-- 	var url=__ctx+"/oa/flow/processRun/checkForm.do?runId="+runId; -->
		var url= __ctx+'/oa/flow/task/startFlowForm.do?isNewVersion=1'+'&runId='+runId;
		show(url,"",subject);
		
	}
	
	function showTips(ele,top,left,data){
		var html = $('<div class="portal-tips">'+data+'</div>');
		ele.append(setColor(html));
	}

	function closeTips(ele){
		$(ele).parents('.portal-tips').remove()
	}
	//获取数据
	function getInfo(ele,top,left,taskId){
		$.ajax({
			type:"get",
			url:__ctx+"/oa/flow/userTask/mattersView.do",
			data:{
				paramid:taskId
			},
			async:true,
			success:function(res){
				if(res.result){
					showTips(ele,top,left,res.data);
				}else{
					return false
				}
			},
			error:function(err){
				return false
			}
		});
	}
	//设置不同的颜色
	function setColor(html){ 
		$('div.tips-detail:odd').css('background','#f1f1f1')
		return html
	}
	
	</script>
	
	<div class="block-III clear">
		<!--加载tab-->
		<div class="basic-tabMenu-wrap clearFix">
			<ul class="task-tabMenu-ul fl clearFix">
				<#if taskTab?exists>
					<#list taskTab as key>
						<#if key.tabName=='待办' >
							<li class="basic-tabMenu basic-tabMenu-selected" task_url="${key.url}" task_name="${key.tabName}" task_id="${key_index}">
						<#else>
							<li class="basic-tabMenu" task_url="${key.url}" task_name="${key.tabName}" task_id="${key_index}">
						</#if>	
							<a href="javascript:void(0);" type="alreadyMatters">${key.tabName}（${key.dataNum}）</a>
							<span class="base-tool-item-split"></span>
					</#list>
				</#if>
				
				<#if myWarning.tab?exists>
					<#list myWarning.tab as warn>
						<li class="basic-tabMenu" task_id="${taskTab?size+warn_index}">
							<a href="javascript:void(0);" type="${warn.key}">${warn.name}</a>
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
									<!--流程列表显示格式自定义 -->
								<!-- <a paramId='taskId:${data.id}' href="javascript:show('${ctxPath}/oa/flow/task/toStart.do?taskId=','${data.id}','${data.subject}')" style="color:${data.color}" class="fl amsg">${data.processName}-${data.name}</a> -->
								<a paramId='taskId:${data.id}' href="javascript:show('${ctxPath}/oa/flow/task/toStart.do?taskId=','${data.id}','${data.subject}')" style="color:${data.color}" class="fl amsg">${data.subject}</a>
								<span class="fl uname">${data.creator}</span>
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
								<a href="javascript:;" onclick="checkFormChange('${data.runId}','${data.defId}','${data.subject}')" class="fl amsg"> ${data.processName}</a>
								<span class="fl uname">${data.creator}</span>
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
								<a paramId='runId:${data.runId}' href="javascript:show('${ctxPath}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')"  class="fl amsg" >${data.processName}</a>
								<span class="fl uname">${data.creator}</span>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">已办任务当前没有记录</div>
				</#if>
				
				
				
			<!-- 	<#if completedMatters?exists&&(completedMatters?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list completedMatters as data>
							<li class="nth-of-typeEeven">
								<a href="javascript:show('${ctxPath}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')" class="fl amsg">${data.processName}</a>
								<span class="fl uname">${data.creator}</span>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">办结任务当前没有记录</div>
				</#if> -->

				<!-- <#if myAccordingMatter?exists&&(myAccordingMatter?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list myAccordingMatter as data>
							<li class="nth-of-typeEeven">
								<a class="fl amsg" href="javascript:show('${ctxPath}/oa/flow/processRun/info.do?link=1&runId=','${data.runId}','${data.subject}')">${data.processName}</a>
								<span class="fl uname">${data.ownerName}</span>
								<span class="fl date">${data.cratetime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">转办代理当前没有记录</div>
				</#if> -->
				
				
			<!-- 	<#if MyProCopyto?exists&&(MyProCopyto?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list MyProCopyto as data>
							<li class="nth-of-typeEeven">
								<a  class="fl amsg" href="javascript:show('${ctxPath}/oa/flow/proCopyto/get.do?copyId=','${data.copyId?c}','${data.subject}')">${data.subject}</a>
								<span class="fl uname">${data.creator}</span>
								<span class="fl date"></span>
								<#if data.isReaded==0>
									<span class="fr unread">未读</span>
								<#else>
									<span class="fr readed">已读/span>
								</#if>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">抄送转发当前没有记录</div>
				</#if> -->
				
				<#if myRequest?exists&&(myRequest?size> 0)>
				<div class="task-list" style="display:none;">
					<ol >
						<#list myRequest as data>
							<li class="nth-of-typeEeven">
								<a class="fl amsg" href="javascript:show('${ctxPath}/oa/flow/processRun/info.do?runId=','${data.runId?c}','${data.subject}')">${data.processName}</a>
								<span class="fl uname">${data.creator}</span>
								<span class="fl date">${data.createtime?string("yyyy-MM-dd HH:mm:ss")}</span>
							</li>
						</#list>	
					</ol>
				</div>
				<#else>
					<div class="task-list alert-info" style="display:none;">我的请求当前没有记录</div>
				</#if>
				
				<#if myWarning?exists&&(myWarning?size> 0)>
					<#list myWarning.tab as warn>
						<div class="task-list" style="display:none;">
							<ol >
								<#list myWarning.taskMap[warn.key] as task>
									<li class="nth-of-typeEeven">
										<a paramId='taskId:${task.id}' href="javascript:show('${ctxPath}/oa/flow/task/toStart.do?taskId=','${task.id}','${task.subject}')" style="color:${task.color}" class="fl amsg">${task.processName}</a>
										<a href="###" class="fl uname">${task.creator}</a>
										<span class="fl date">${task.createTime?string("yyyy-MM-dd HH:mm:ss")}</span>
										<#if task.hasRead==0>
											<span class="fr unread">未读</span>
										<#else>
											<span class="fr readed">已读</span>
										</#if>
									</li>
								</#list>	
							</ol>
						</div>
					</#list>
				</#if>
		</div>		
	</div>
