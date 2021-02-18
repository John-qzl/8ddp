<script>
function handRowEvent(ev,table){
		$("td.tdNo",table).each(function(i){
			$(this).text(i+1);
		});
	}
</script>
<#setting number_format="0">
<#function getFieldList fieldList teamNameKey>
 	<#assign rtn>
		<#list fieldList as field>
			<#if  field.isHidden == 0>
				<tr teamNameKey="team:${table.tableId}:${teamNameKey}">
					<td align="right" nowrap="nowarp" style="width:20%;" class="formTitle"><span>${field.fieldDesc}</span>:</td>
					<td  class="formInput" style="width:80%;">
						<@input field=field/>
					</td>
				</tr>
			</#if>
		</#list>
 	</#assign>
	<#return rtn>
</#function>
<#function setTeamField teams>
 	<#assign rtn>
		 <#list teams as team>
				<tr teamNameKey="team:${table.tableId}:${team.teamNameKey}">
					<td colspan="2" class="teamHead">${team.teamName}</td>
				</tr>
				${getFieldList(team.teamFields,team.teamNameKey)}
		</#list>
	</#assign>
	<#return rtn>
</#function>

<table cellpadding="2" cellspacing="0" border="1" class="formTable">
	<tr>
		<td colspan="2"  class="formHead" >${table.tableDesc }</td>
	</tr>
	<#if teamFields??>
		<#if isShow>
			<#if showPosition == 1>
				${setTeamField(teamFields)}
				${getFieldList(fields,'null')}
			<#else>
				${getFieldList(fields,'null')}
				${setTeamField(teamFields)}
			</#if>
		<#else>
			${setTeamField(teamFields)}
		</#if>
	<#else>
		${getFieldList(fields,'null')}
	</#if>
</table>
<br />
