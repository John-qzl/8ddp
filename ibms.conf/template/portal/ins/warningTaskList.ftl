<#setting number_format="0">
	
	<script type="text/javascript">
	$(function() {
		//任务列表tab滑动
		$('.task-tabMenu-ul li').click(function(){
			var liindex = $('.task-tabMenu-ul li').index(this);
			$(this).addClass('basic-tabMenu-selected').siblings().removeClass("basic-tabMenu-selected");
			$('.basic-tabCont-wrap div.task-list').eq(liindex).fadeIn(150).siblings('div.task-list').hide();
		});
		//代办任务列表鼠标滑动显示提示
		$('ol').off('mouseover','a.amsg');
		$('ol').off('mouseout','a.amsg');
		$('ol').on('mouseover','a.amsg',function(){
			var top = $(this).offset().top;
			var left = $(this).offset().left;
			var height = $(document).height();
			var taskId = $(this).attr('paramid')
			var offS = (top+270<height)?(top+20):(top-200);
			
			$(this).after(showTips(offS,left,taskId));
			//$(this).after(getInfo(offS,left,taskId));
		})
		
		$('ol').on('mouseout','a.amsg',function(){
			//$(this).remove();
			$(this).next('div.portal-tips').remove()
		})
	})
	
	function show(url,Id,subject){
		top['index']._OpenWindow({
			url : url + Id,
			title : subject,
			width : screen.availWidth-5,
			height : screen.availHeight-35
		});		
	}
	
	function showTips(top,left,taskId){
		var html = $('<div class="portal-tips" style="top:'+top+'px;left:'+left+'px"></div>');
		getInfo(taskId);
		return html;
	}
	
	function getInfo(taskId){
		$.ajax({
			type:"get",
			url:__ctx+"/oa/flow/userTask/mattersView.do",
			//url:'http://localhost:8088/ibms/jslib/ibms/oa/form/test.json',
			data:{
				paramid:taskId
			},
			async:true,
			beforeSend:function(){
				$('.portal-tips').html("数据加载中")
			},
			success:function(res){
				$('.portal-tips').html(res)
			},
			error:function(err){
				$('.portal-tips').html("加载错误")
			}
		});
	}
	
	</script>
	
	<div class="block-III clear">
		<!--加载tab-->
		<div class="basic-tabMenu-wrap clearFix">
			<ul class="task-tabMenu-ul fl clearFix">
				<#if tab?exists>
					<#list tab as warn>
						<#if warn_index==0>
							<li class="basic-tabMenu basic-tabMenu-selected" >
						<#else>
							<li class="basic-tabMenu">
						</#if>	
							<a href="javascript:void(0);" type="${warn.key}">${warn.name}</a>
							<span class="base-tool-item-split"></span>
						</li>
					</#list>
				</#if>
			</ul>
		</div>
		<!--加载列表-->
		<div class="basic-tabCont-wrap clearFix">
			<#list tab as warn>
				<div class="task-list show" style="display:block;">
					<ol >
						<#list taskMap[warn.key] as task>
							<li class="nth-of-typeEeven">
								<a paramId='taskId:${task.id}' href="javascript:show('${ctxPath}/oa/flow/task/toStart.do?taskId=','${task.id}','${task.subject}')" style="color:${task.color}" class="fl amsg">${task.subject}</a>
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
		</div>		
			
		
		
	</div>
