<#setting number_format="#">
<#--定义数据 -->
<#assign displayFields=displayField>

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
				<input type="hidden" name="Q_${field.na}ID_L" class="inputText" id="${field.na}ID"  value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "5"><#--角色多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectRole({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "6"><#--组织多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectOrg({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "7"><#--岗位多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}"  onclick="__Selector__.selectPos({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "8"><#--人员多选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "17"><#--角色单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectRole({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "18"><#--组织单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectOrg({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#case "19"><#--岗位单选选择器 -->
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectPos({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
			<#break>
			<#default>
				<input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
				<input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectUser({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
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

<#macro genInput field formatData>
	<#switch field.controltype>
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

<#list displayFields as field>
<#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
	<div class="tips-detail">
		<span class="tips-title">${field.desc}:</span>
		<span class="tips-content"><@genInput field=field formatData=formatData/></span>
	</div>
<#noparse></#if></#noparse>
</#list>



