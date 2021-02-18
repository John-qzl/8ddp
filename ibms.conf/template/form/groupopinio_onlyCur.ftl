<table cellpadding="1" cellspacing="1" class="table-grid table-list">
	<tbody>
		<tr>
			<th class="formTitle" style="text-align:left" nowrap="nowarp" colspan="6">
				<a href="javascript:;" onclick='opinionHistory("${instanceId}","${flowNodeId}")'>${title}</a>
			</th>
		</tr>
		<tr>
			<td style="width: 15%;" class="formTitle" align="right" nowrap="nowarp">处理人:</td>
			<td class="formInput" style="width: 35%;">${taskOpinion.exeFullname}</td>
			<td style="width: 15%;" class="formTitle" align="right" nowrap="nowarp">处理时间:</td>
			<td class="formInput" style="width: 35%;">${taskOpinion.endTime?string("yyyy-MM-dd HH:mm:ss")}</td>

		</tr>
		<tr>
			<td style="width: 15%;" class="formTitle" align="right" nowrap="nowarp">处理意见:</td>
			<td class="formInput" style="width: 35%;" colspan="3">${taskOpinion.opinion}</td>
		</tr>
	</tbody>
</table>