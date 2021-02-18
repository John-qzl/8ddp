	<script type="text/javascript">
		$(function() {
			if($('.basic-tabCont-wrap div.task-list').length>0){			
				$('.basic-tabCont-wrap div.task-list').eq(0).show();
			}
			//任务列表tab滑动
			$('.task-tabMenu-ul li').click(function(){
				var liindex = $('.task-tabMenu-ul li').index(this);
				$(this).addClass('basic-tabMenu-selected').siblings().removeClass("basic-tabMenu-selected");
				$('.basic-tabCont-wrap div.task-list').eq(liindex).fadeIn(150).siblings('div.task-list').hide();
			});
			$('table tr[pk]').click(openDetailDialog);
		})
		
		function show(url,Id,subject){
			window.open(url + Id)
		}
		function openDetailDialog(obj){
		 	debugger;
		 	var pk = $(obj.target).parents('tr').attr('pk');
		 	var displayId = $(obj.target).parents('div.panel-table').find('#displayId').val();
		 	var url = __ctx+"/oa/form/dataTemplate/detailData.do?";
		 	url+="__displayId__="+displayId;
		 	url+="&__pk__="+pk;
		 	url = url.getNewUrl();
		 	DialogUtil.open({
				height:800,
				width: 1000,
				url: url,
				showMax: true,                             //是否显示最大化按钮 
			    showToggle: false,                         //窗口收缩折叠
			    title: "门户-明细查看",
			    showMin: false,
				sucCall:function(rtn){}
			});
 }	
	</script>
	<div  class="block-III clear">
		<!--加载tab-->
		<div  class="basic-tabMenu-wrap clearFix">
			<ul class="task-tabMenu-ul fl clearFix">
				<#if confList?exists>
					<#assign count=0 />
					<#list confList as conf>
						<#if count==0>
							<li class="basic-tabMenu basic-tabMenu-selected">
								<a href="javascript:void(0);" type="alreadyMatters">${conf.name}</a>
								<span class="base-tool-item-split"></span>
							</li>
						<#elseif count !=0>
							<li class="basic-tabMenu">
								<a href="javascript:void(0);" type="alreadyMatters">${conf.name}</a>
								<span class="base-tool-item-split"></span>
							</li>
						</#if>			
						<#assign count=count + 1>
					</#list>
				</#if>
			</ul>
		</div>
		<!--加载列表-->
		<div  style="height:100%"  class="basic-tabCont-wrap clearFix">	
			<#if confList?exists>
				<#list confList as conf>
						<div style="height:100%" class="task-list" style="display:none;">		
							${conf.html}
						</div>
				</#list>
			</#if>
		</div>
	</div>
