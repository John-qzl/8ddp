<div class="widget-box border " >
	<div class="widget-header">
		<h4 class="widget-title"><i class="ht-icon fa fa-envelope-o"></i>${model.title}</h4>
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
			<ul class="widget-list list-unstyled">
					<#list data as data>
               <li class="clearfix" onclick="javascript:window.top.showReadMsgDlg(${data.id})">
								${data.subject}
						</li>

				</#list>
			</ul>
		</div>
	</div>
</div>