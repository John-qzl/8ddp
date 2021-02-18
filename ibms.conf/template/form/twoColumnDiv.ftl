<#setting number_format="0">
<#function setTeamField teams>
 	<#assign rtn>
		 <#list teams as team>
		 	<div class="tableNew clearfix item2" teamNameKey="team:${table.tableId}:${team.teamNameKey}">
		 		<div class="tableNew_title">
					<p>${team.teamName}</p>
				</div>
			    <div class="clearfix">
					${getFieldList(team.teamFields,team.teamNameKey)}
				</div>
			</div>
		</#list>
	</#assign>
	<#return rtn>
</#function>

<#function getFieldList fieldList teamNameKey>
 	<#assign rtn>
		<#list fieldList as field>
			<#if field.isHidden == 0>
				<div class="tableNew_con clearfix ">
					<div class="tableNew_con_1">
						<p>${field.fieldDesc}</p>
					</div>
					<div class="tableNew_con_2">
						<@input field=field/>
					</div>
				</div>
			</#if>
		</#list>
 	</#assign>
	<#return rtn>
</#function>

<#function getHeadFieldList fieldList>
 	<#assign rtn>
		<#list fieldList as field>
			<#if field.isHidden == 0>
				<div class="tableNew_con con2 clearfix">
	               <div class="tableNew_con_1">
						<p>${field.fieldDesc}</p>
					</div>
					<div class="tableNew_con_2">
						<@input field=field/>
					</div>
	            </div>
			</#if>
		</#list>
 	</#assign>
	<#return rtn>
</#function>

<!--新增-->
<div class="tableNewHeader tableNew item2 clearfix">
	<div class="tableNewHeader_con">
		<h3>${table.tableDesc}</h3>
	</div>
	<div class="tableNew_con con1" style="">
	</div>
	${getHeadFieldList(fields)}
</div>
<#if teamFields??>
	${setTeamField(teamFields)}
</#if>

