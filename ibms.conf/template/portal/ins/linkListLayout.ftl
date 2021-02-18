
<div class="main">
	<ul class="news-list" style="display:block;">
		<#list linkList as item>
		<li>
			<a href="${item.url}" target="_blank">${item.name}</a>
		</li>
		</#list>
	</ul>
</div>