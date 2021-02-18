<table cellpadding="1" cellspacing="1" class="table-grid table-list">
	<tr>
		<th colspan="5" class="teamHead">${title}</th>
	</tr>
	<tr>
		<th>序号</th>
		<th>
			处理人
		</th>
		<th>
			处理意见
		</th>
		<th>
			处理时间
		</th>
		<th width="200px">管理</th>
	</tr>
	<#list opinions as data>
		<tr class="<#if data_index % 2 == 0>odd</#if><#if data_index % 2 == 1>even</#if>">
			<td style="width: 30px;">${data_index+1}</td>
			<td>${data.exeFullname}</td>
			<td>${data.opinion}</td>
			<td>${data.endTime?string("yyyy-MM-dd HH:mm:ss")}</td>
			<td class="rowOps">
			</td>
		</tr>
	</#list>
</table>