<#setting number_format="#">
<#--定义数据 -->
<#assign displayFields=displayField>
<#assign conditionFields=conditionField>

<#noparse>
<#setting number_format="#">
<#assign displayFields=displayField>
<#assign tableLength = 1>
	
</#noparse>
<#--日期选择器 -->
<#macro genQueryDate field>
    <#switch field.qt>
      <#case "D">
        <input type="text" name="Q_${field.na}_D" readonly="readonly" datefmt="${field.ftm}"  class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_D'<#noparse>]}"</#noparse>  />
      <#break>
      <#case "DL">
        <input type="text" name="Q_${field.na}_DL" readonly="readonly" datefmt="${field.ftm}"  class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_DL'<#noparse>]}"</#noparse> />
      <#break>
      <#case "DG">
        <input type="text" name="Q_${field.na}_DG"  readonly="readonly" datefmt="${field.ftm}" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_DG'<#noparse>]}"</#noparse>  />
      <#break>
      <#case "DR">
        <input type="text" name="Q_begin${field.na}_DL" readonly="readonly" datefmt="${field.ftm}" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_begin${field.na}_DL'<#noparse>]}"</#noparse>  />
        </li><li><span class="label">到:</span>
        <input type="text" name="Q_end${field.na}_DG" readonly="readonly" datefmt="${field.ftm}" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_end${field.na}_DG'<#noparse>]}"</#noparse> />
      <#break>
      <#default>
        <input type="text" name="Q_${field.na}_${field.qt}" datefmt="${field.ftm}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
      <#break>
    </#switch>
</#macro>
<#--数字选择器 -->
<#macro genQueryNumber field>
		<#switch field.qt>
			<#case "D">
				<input type="text" name="Q_${field.na}_D" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_D'<#noparse>]}"</#noparse>  />
			<#break>
			<#case "NL">
				<input type="text" name="Q_${field.na}_NL" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_NL'<#noparse>]}"</#noparse> />
			<#break>
			<#case "NG">
				<input type="text" name="Q_${field.na}_NG"  class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_NG'<#noparse>]}"</#noparse>  />
			<#break>
			<#case "NR">
				<input type="text" name="Q_begin${field.na}_NL" class="inputText" value="<#noparse>${param[</#noparse>'Q_begin${field.na}_NL'<#noparse>]}"</#noparse>  />
				</li><li><span class="label">到:</span>
				<input type="text" name="Q_end${field.na}_NG" class="inputText" value="<#noparse>${param[</#noparse>'Q_end${field.na}_NG'<#noparse>]}"</#noparse> />
			<#break>
			<#default>
				<input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
			<#break>
		</#switch>
</#macro>
<#--用户选择器 -->
<#macro genQuerySelector field>
		<#switch field.ct>
			<#case "4"><#--用户单选选择器 -->
				<input type="hidden" name="Q_${field.na}ID_L" class="inputText" id="${field.na}ID"  value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "5"><#--角色多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectRole({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "6"><#--组织多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectOrg({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "7"><#--岗位多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}"  onclick="__Selector__.selectPos({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "8"><#--人员多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "17"><#--角色单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectRole({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "18"><#--组织单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectOrg({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#case "19"><#--岗位单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectPos({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
			<#default>
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>/>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>/>
			<#break>
		</#switch>
</#macro>
<#-- 选择器 -->
<#macro genQuerySelect field formatData>
  	<select name="Q_${field.na}_${field.qt}" <#noparse><#if </#noparse>'${field.ct}'<#noparse> == '28'> multiple="multiple" class="multiple_select" </#if></#noparse>>
	  	<#noparse><#if </#noparse>'${field.ct}'<#noparse> != '28'> <option value="">全部</option></#if></#noparse>
	  	<#if formatData?if_exists >
			<#list formatData?keys as key>
				<option value="${key}"  <#noparse><#if param['</#noparse>Q_${field.na}_${field.qt}'] == '${key}'>selected<#noparse></#if></#noparse>>${formatData[key]}</option>
			</#list>
	 	</#if>
  	</select>
</#macro>


<#-- 数据字典 -->
<#macro dicQuerySelect field formatData>
  <#if formatData?if_exists >
      <input lablename="${field.na}" class="dicComboTree" nodeKey="${formatData}" validate="{empty:false,required:true}" name="${field.na}" height="200" width="170"/>
  </#if>
</#macro>
<#-- 外键控件宏 -->
<#macro genFKColumnShow field>
	<#--关系控件-->
		<input type="hidden" name="Q_${field.na}_${field.qt}" class="inputText"  
				value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse>/>
		<input type="hidden" name="${field.na}"  class="hidden" lablename="${field.cm}" 
				value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse>/>
		<input type="text"   name="${field.na}XXXXXFKColumnShow" class="inputText" lablename="${field.cm}"  readonly="readonly"  
				value="<#noparse>${param[</#noparse>'${field.na}XXXXXFKColumnShow'<#noparse>]}"</#noparse>/>
		<span name="editable-input" style="display:inline-block;"><#--span这行不能少  下一行不能是dialog="${field.relFormDialogStripCData}"-->
		    <a kfname="${field.na}" dialog="${field.relFormDialog}" href="javascript:;" class="extend fkselect">
			  选择</a>
		</span>
		<a href="javascript:;" class="link resetFKShow" atype="reset" name="${field.na}">重置</a>

</#macro>

<#--生成查询条件宏 -->
<#macro genCondition field formatData>
	<#if field.vf=="1" >
		<span class="label" title="${field.cm}">${field.cm}:</span>		
		<#switch field.ct>
			<#case "1">
				<#if field.ty == 'number'>
				<input type="text" name="Q_${field.na}_${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'${field.na}'<#noparse>]}"</#noparse>  validate="{number:true}" />
				<#else>
				<input type="text" name="Q_${field.na}_${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'${field.na}'<#noparse>]}"</#noparse>  />
				</#if>
			<#break>
			<#-- 选择器 -->
			<#case "4">
			<#case "5">
			<#case "6">
			<#case "7">
			<#case "8">
			<#case "17">
			<#case "18">
			<#case "19">
				<@genQuerySelector field=field/>
			<#break>
			<#case "15"><#--日期选择器 -->
				<@genQueryDate field=field/>
			<#break>
			<#case "25"><#--数字选择器 -->
				<@genQueryNumber field=field/>
			<#break>
			<#case "11"><#--下拉单选项-->
			<#case "28"><#--下拉多选项-->
				<#if formatData[field.na]?if_exists >
					<@genQuerySelect field=field formatData=formatData[field.na]/>
				<#else>
					<input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'${field.na}'<#noparse>]}"</#noparse> />
				</#if>
			<#break>
		    <#case "3"><#--数据字典-->
		        <#if formatData[field.na]?if_exists >
		          <@dicQuerySelect field=field formatData=formatData[field.na]/>
		        <#else>
		          <input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'${field.na}'<#noparse>]}"</#noparse> />
		        </#if>
		    <#break>
			<#case "23"><#--外键控件宏-->
				<@genFKColumnShow field=field />
			<#break>
			<#default>
				<input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'${field.na}'<#noparse>]}"</#noparse> />
			<#break>
		</#switch>
	</#if>
</#macro>

<#macro genInput field formatData>
	<#switch field.controltype>
		<#case "PK">
			<a name="subject" href="javascript:;" onclick="javascript:executeTask('<#noparse>${data.</#noparse>${pkField}<#noparse>}</#noparse>',this)" title="<#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>" class="message open-message"><#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse></a>
		<#break>
		<#case "11">
			<#if formatData[field.name]?if_exists >
				<#list formatData[field.name]?keys as key>
					<#noparse><#if data['</#noparse>${field.name}<#noparse>']</#noparse> == '${key}' >
						${formatData[field.name][key]}
					<#noparse></#if></#noparse>
				</#list>
			</#if>
			<#break>
		<#case "23">
			<#noparse>${service.getFieldRelData('${tableId}', </#noparse>'${field.name}'<#noparse>,'${data.</#noparse>${field.name}<#noparse>}')}</#noparse>
			<#break>
		<#default>
			<#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>
			<#break>
	</#switch>
</#macro>

	

<div class="panel" ajax="ajax">
	<#--查询头-->
	<div class="panel-top">
		<div class="tbar-title">
			<span class="tbar-label"></span>
		</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<#if hasCondition>
						<div class="group"><a class="link ajaxSearch" href="javascript:;" onclick="handlerSearchAjax(this)">查询</a></div>
					</#if>
					<div class="group"><a onclick="exportExcel()"  class="link export">导出</a></div>
				</div>
			</div>
		<#if hasCondition >
			<div class="panel-search">
				<form id="searchForm" name="searchForm" method="post" action="<#noparse>${searchFormURL}</#noparse>">
				<input type="hidden" name="defId" value="${defId}"/>
				<#--查询条件-->
					<#if conditionFields?if_exists>
						<ul class="row">
						<#list conditionFields as field>
							<li>
								<@genCondition field=field formatData=formatData/>
							</li>
						</#list>
						</ul>
					</#if>
				</form>
			</div>
		</#if>
	</div>
	<#--表头-->	
	<div class="panel-body">
		<div class="panel-table">
			<table cellpadding="1" cellspacing="1" class="table-grid table-list">
				<thead>
				<tr>
					<#noparse>
						<#--显示checkbox-->	
						<#assign tableLength = tableLength+1>
					    <th> <input id="chkall" type="checkbox"/></th>
              		</#noparse>
					<#noparse>
						<#assign tableLength = tableLength+1>
						<th>序号</th>
					</#noparse>
              		<#-- 显示字段-->
					<#list displayFields as field>
				 			<#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
				 				<th>
							<#if field.type!="clob">
								<#noparse>
								<a href="javascript:;" onclick="linkAjax(this)" action="${pageURL}&${tableIdCode}__ns__=</#noparse>${field.name}<#noparse>"></#noparse>
									${field.desc}<#noparse><#if (sortField?? && sortField=="</#noparse>${field.name}<#noparse>")><#if (orderSeq=="ASC")>↑<#else>↓</#if></#if>
								</a>
								</#noparse>
							<#else>
									${field.desc}	
							</#if>
								</th>
								<#noparse></#if></#noparse>
					</#list>
					<#if hasManage>
						<#assign tableLength = tableLength+1>
						<th width="80px">管理</th>
					</#if>
				</tr>
				</thead>
				<tbody>
				<#--表体-->				
				<#noparse>
					<#list list as data>
					<tr class="<#if data_index % 2 == 0>odd</#if><#if data_index % 2 == 1>even</#if>" align="center">
					
				</#noparse>
				    <td style="width:30px;text-align: center;">
                  	  <input class="pk" type="checkbox" value="<#noparse>${data.</#noparse>${pkField}<#noparse>}</#noparse>" name="${pkField}"/>
                    </td>
               	
				<#--序号-->
                <#noparse><td style="width:30px;">${data_index+1}</td> </#noparse>
					<#list displayFields as field>
						<#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
							<td title="<#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>">
								<@genInput field=field formatData=formatData/>
							</td>	
						<#noparse></#if></#noparse>
					</#list>
					</tr>
				<#noparse></#list></#noparse>
				<#noparse>
					<#if list?size==0>
						<tr>
							<td style="background: #fff;text-align: center;" colspan="${displayFields?size + tableLength}">
								当前没有记录。
							</td>
						</tr>
					</#if>	
				</#noparse>
				</tbody>
			</table>
		</div>
		<#noparse>${pageHtml}</#noparse>	
	</div>

</div>

