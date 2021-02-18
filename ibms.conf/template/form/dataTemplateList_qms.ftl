<#-- 
Name: 数据列表模板
Desc:数据列表模板

本模板需要通过2次解析才能得到最终的Html
第一次解析：
*************************************************************
*************************************************************
*数据模型:****************************************************
*************************************************************
*************************************************************

tbarTitle：Tool Bar 的标题

********************************************
conditionFields:条件字段
--joinType：  条件联合类型
--name：  列名
--name：完全指定名
--operate：条件类型: =|>=|<=|….
--comment：注释
--type：  类型
--value：值
--valueFrom：值来源

************************************************************
displayFields：显示字段
--name：列名
--name：完全指定名
--label：别名
--index：索引
--comment：注释
--type：类型

******************************************************
tableIdCode:Table ID Code

**************************************************
displayId: 自定义显示的ID

**************************************************
pageHtml：分页的Html 详见pageAjax.xml

*************************************************
pageURL：当前页面的URL

searchFormURL：搜索表单的Action


sortField：当前排序字段

orderSeq：当前的排序类型

***********************************************
pkcols:主键列

deleteBaseURL：删除一行数据的BaseURL
editBaseURL：编辑一行数据的BaseURL
 -->


<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>
<#assign conditionFields=bpmDataTemplate.conditionField?eval>
<#assign filterFields=bpmDataTemplate.filterField?eval>
<#assign manageFields=bpmDataTemplate.manageField?eval>

<#noparse>
<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>
<#assign conditionFields=bpmDataTemplate.conditionField?eval>
<#assign filterFields=bpmDataTemplate.filterField?eval>
<#assign manageFields=bpmDataTemplate.manageField?eval>

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
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectUser({self:this});" value="...">
      <#break>
      <#case "5"><#--角色多选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectRole({self:this});" value="...">
      <#break>
      <#case "6"><#--组织多选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectOrg({self:this});" value="...">
      <#break>
      <#case "7"><#--岗位多选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectPos({self:this});" value="...">
      <#break>
      <#case "8"><#--人员多选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectUser({self:this});" value="...">
      <#break>
      <#case "17"><#--角色单选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectRole({self:this});" value="...">
      <#break>
      <#case "18"><#--组织单选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectOrg({self:this});" value="...">
      <#break>
      <#case "19"><#--岗位单选选择器 -->
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectPos({self:this});" value="...">
      <#break>
      <#default>
        <input  type="hidden" name="Q_${field.na}ID_L" id="${field.na}ID" value="<#noparse>${param[</#noparse>'Q_${field.na}ID_L'<#noparse>]}"</#noparse>>
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
        <input type="button" onclick="__Selector__.selectUser({self:this});" value="...">
      <#break>
    </#switch>
</#macro>
<#-- 选择器 -->
<#macro genQuerySelect field formatData>
  <select  name="Q_${field.na}_${field.qt}">
    <option value="">全部</option>
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
        <input type="text" name="Q_${field.na}_${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse>  validate="{number:true}" />
        <#else>
        <input type="text" name="Q_${field.na}_${field.qt}" class="inputText"  value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse>  />
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
      <#case "11"><#--下拉选项-->
        <#if formatData[field.na]?if_exists >
          <@genQuerySelect field=field formatData=formatData[field.na]/>
        <#else>
          <input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
        </#if>
      <#break>
       <#case "24"><#--流程状态--> 
         <#if formatData[field.na]?if_exists >
          <@genQuerySelect field=field formatData=formatData[field.na]/>
        <#else>
          <input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
        </#if>
      <#break>
        <#case "3"><#--数据字典-->
            <#if formatData[field.na]?if_exists >
              <@dicQuerySelect field=field formatData=formatData[field.na]/>
            <#else>
              <input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
            </#if>
        <#break>
      <#case "23"><#--外键控件宏-->
        <@genFKColumnShow field=field />
      <#break>
      <#default>    
        <input type="text" name="Q_${field.na}_${field.qt}" class="inputText" value="<#noparse>${param[</#noparse>'Q_${field.na}_${field.qt}'<#noparse>]}"</#noparse> />
      <#break>
    </#switch>
  </#if>
</#macro>
  
<#--显示列start -->
<#macro genDisplayS field>
  <#switch field.displayType>
    <#case "hyperlink">
        <a class="" action="<#noparse>${formControlService.getCustomUrl(</#noparse>'${field.action}'<#noparse>,data)}</#noparse>" href="####" onclick="${field.onclick};">
    <#break>
    <#case "processWatch">
          <a class="" action="<#noparse>${formControlService.getCustomUrl(</#noparse>'${field.action}'<#noparse>,data)}</#noparse>" href="####" onclick="${field.onclick};">
      <#break>
    <#case "detail">
          <a class="" action="<#noparse>${formControlService.getCustomUrl(</#noparse>'${field.action}'<#noparse>,data)}</#noparse>" href="####" onclick="${field.onclick};">
    <#break>
    <#default>        
    <#break>
  </#switch>
</#macro>
<#--显示列end -->
<#macro genDisplayE field>
    <#switch field.displayType>
      <#case "hyperlink">
         </a>
      <#break>
      <#case "processWatch">
            </a>
        <#break>
      <#case "detail">
           </a>
      <#break>
      <#default>
    </#switch>
</#macro>
<#noparse>
<#--管理列-->
<#macro genManage mindex manage managePermission actionUrl pk>
  <#--编辑-->
  <#if manage.name == 'edit' >
    <#if managePermission['${mindex}_edit']>
      <a class="link edit" action="${actionUrl.edit}&__pk__=${pk}&__dbomFKName__=${dbomFKName}&__dbomFKValue__=${dbomFKValue}" onclick="openLinkDialog({keyName:'${mindex}_${manage.name}',scope:this,width:1000,height:800,isFull:false})" href="####">${manage.desc}</a>
    </#if>
  <#--删除-->
  <#elseif manage.name == 'del' >
    <#if managePermission['${mindex}_del']>
      <a class="link del"  href="${actionUrl.del}&__pk__=${pk}">${manage.desc}</a>
    </#if>
  <#--明细-->
  <#elseif manage.name == 'detail' >
    <#if managePermission['${mindex}_detail']>
      <a class="link detail" action="${actionUrl.detail}&__pk__=${pk}" onclick="openLinkDialog({'${mindex}_${manage.name}',scope:this,width:1000,height:800,isFull:false})" href="####">${manage.desc}</a>
    </#if>
  <#--复杂表单明细-->
    <#elseif manage.name == 'complexDetail' >
      <#if managePermission['${mindex}_complexDetail']>
        <a class="link complexDetail" action="${actionUrl.complexDetail}&__pk__=${pk}" onclick="multiTabView({'${mindex}_${manage.name}',scope:this,isFull:false,openType:'frist',formKey:'${bpmDataTemplate.formKey}'})" href="####">${manage.desc}</a> 
      </#if>
   <#--附件-->
    <#elseif manage.name == 'attach' >
        <#if managePermission.attach>
          <a class="link attach" action="${actionUrl.attach}&__pk__=${pk}" onclick="fileAttachDialog('${mindex}_${manage.name}','${actionUrl.attach}&__pk__=${pk}')" href="####">${manage.desc}</a>
       </#if>  

  <#--打印-->
  <#elseif manage.name == 'print' >
    <#if managePermission['${mindex}_print']>
      <a class="link print" action="${actionUrl.print}&__pk__=${pk}" onclick="printReport('${mindex}_${manage.name}','${pk}','${bpmDataTemplate.id}')" href="javascript:;">${manage.desc}</a>
    </#if>
    
  <#--流程监控-->
    <#elseif manage.name == 'process' >
        <#if managePermission['${mindex}_process']>
          <a class="link detail" action="${actionUrl.process}&__pk__=${pk} " onclick="processDialog('${mindex}_${manage.name}','${actionUrl.process}&__pk__=${pk}')" href="####">${manage.desc}</a>
       </#if> 
    
    <#--启动1-->
    <#elseif manage.name == 'start1' >
        <#if managePermission.start1>
          <a class="link detail" action="${actionUrl.start1}&__pk__=${pk} " onclick="processStartDialog('${mindex}_${manage.name}','${actionUrl.start1}&__pk__=${pk}')" href="####">${manage.desc}</a>
       </#if> 
       
       <#--启动2-->
    <#elseif manage.name == 'start2' >
        <#if managePermission.start2>
          <a class="link detail" action="${actionUrl.start2}&__pk__=${pk} " onclick="processStartDialog('${mindex}_${manage.name}','${actionUrl.start2}&__pk__=${pk}')" href="####">${manage.desc}</a>
       </#if> 
       
       <#--启动3-->
    <#elseif manage.name == 'start3' >
        <#if managePermission.start3>
          <a class="link detail" action="${actionUrl.start3}&__pk__=${pk} " onclick="processStartDialog('${mindex}_${manage.name}','${actionUrl.start3}&__pk__=${pk}')" href="####">${manage.desc}</a>
       </#if> 
    
  <#--启动-->
  <#elseif manage.name == 'start' >
    <#if managePermission['${mindex}_start']>
      <#if actionUrl.start?if_exists>
               <#assign isStart= "false" >
            <#else> 
              <#assign isStart= "true" >
            </#if>
      <a class="link run" action="${actionUrl.start}&businessKey=${pk}"  onclick="openLinkDialog({'${mindex}_${manage.name}',scope:this,width:1000,height:800,isFull:false,isStart:${isStart}})" href="####">${manage.desc}</a>
    </#if>
  </#if>
</#macro>
<#--顶部按钮-->
<#macro genToolBar gendex manage managePermission actionUrl>
  <#--新增-->
  <#if manage.name == 'add' >
    <#if managePermission.add>
      <div class="group"><a class="link add" action="${actionUrl.add}&__dbomFKName__=${dbomFKName}&__dbomFKValue__=${dbomFKValue}"  onclick="openLinkDialog({keyName:'${gendex}_${manage.name}',scope:this,width:1000,height:800,isFull:false})"  href="####">${manage.desc}</a></div>
    </#if>
  <#--删除-->
  <#elseif manage.name == 'del' >
    <#if managePermission.del>
      <div class="group"><a class="link del" action="${actionUrl.del}" href="####">${manage.desc}</a></div>
    </#if>
  <#--导出-->
  <#elseif manage.name == 'export' >
    <#if managePermission.export>
      <div class="group"> <div class="exportMenu"></div> </div>
    </#if>
  <#--导出选中数据-->
  <#elseif manage.name == 'exportSelect' >
    <#if managePermission.exportSelect>
      <div class="group"><a class="link export" action="${actionUrl.export}" onclick="handlerExpSelect('${gendex}_${manage.name}')" href="####">${manage.desc}</a></div>
    </#if>
  <#--导出全部数据-->
  <#elseif manage.name == 'exportAll' >
    <#if managePermission.exportAll>
      <div class="group"><a class="link export" action="${actionUrl.export}" onclick="handlerExpAll('${gendex}_${manage.name}')" href="####">${manage.desc}</a></div>
    </#if>
  <#--导出当页数据-->
  <#elseif manage.name == 'exportPage' >
    <#if managePermission.exportSelect>
      <div class="group"><a class="link export" action="${actionUrl.export}" onclick="handlerExpPage('${gendex}_${manage.name}')" href="####">${manage.desc}</a></div>
    </#if>
<#--导出主表关联表-->
	<#elseif manage.name == 'exportMainRel' >
		<#if managePermission.exportMainRel>
			<div class="group"><a class="link export" action="${actionUrl.export}" onclick="openTableSelectDialog({keyName:'${gendex}_${manage.name}',scope:this,width:450,height:500})" href="####">${manage.desc}</a></div>
		</#if>
  <#--附件-->
    <#elseif manage.name == 'attach' >
        <#if managePermission.attach>
    <div class="group">
          <a class="link attach" action="${actionUrl.attach}}" onclick="fileAttachDialog('${gendex}_${manage.name}','${actionUrl.attach}')" href="####">${manage.desc}</a></div>
       </#if>    
  <#--导入-->
  <#elseif manage.name == 'import' >
    <#if managePermission.import>
      <div class="group"> <div class="group"><a class="link import" action="${actionUrl.import}"  onclick="openLinkDialog({keyName:'${gendex}_${manage.name}',scope:this,width:450,height:200})"   href="####">${manage.desc}</a></div></div>
    </#if>
  <#--报表导出-->
  <#elseif manage.name == 'expprint' >
    <#if managePermission.expprint>
      <div class="group"> <div class="group"><a class="link exportMenu" action="${actionUrl.exportMenu}"  onclick="expprint('${gendex}_${manage.name}','${bpmDataTemplate.id}')"   href="####"><span></span>${manage.desc}</a></div></div>
    </#if>    
  <#--启动-->  
  <#elseif manage.name == 'start' >
    <#if managePermission.start>
          <#if actionUrl.start?if_exists>
               <#assign isStart= "false" >
            <#else> 
              <#assign isStart= "true" >
            </#if>
      <div class="group"><a class="link run" action="${actionUrl.start}"  onclick="openLinkDialog({keyName:'${gendex}_${manage.name}',scope:this,width:1000,height:800,isFull:false,isStart:${isStart}})"  href="####">${manage.desc}</a></div>
    </#if>
    <#--同步-->  
    <#elseif manage.name == 'reportData' >
      <#if managePermission.reportData>
        <div class="group"><a class="link reportData" action="${actionUrl.print}"  onclick="reportData()"  href="####">${manage.desc}</a></div>
      </#if>
    <#--接收-->  
    <#elseif manage.name == 'accept' >
      <#if managePermission.accept>
        <div class="group"><a class="link accept" action="${actionUrl.print}"  onclick="accept()"  href="####">${manage.desc}</a></div>
      </#if>
      <#-- 拒收-->  
    <#elseif manage.name == 'decline' >
      <#if managePermission.decline>
        <div class="group"><a class="link decline" action="${actionUrl.print}"  onclick="decline()"  href="####">${manage.desc}</a></div>
      </#if>
      <#--反馈-->  
    <#elseif manage.name == 'feedBack' >
      <#if managePermission.feedBack>
        <div class="group"><a class="link feedBack" action="${actionUrl.print}"  onclick="feedBack()"  href="####"></span>${manage.desc}</a></div>
      </#if>
  </#if>
</#macro>
</#noparse>

 <!--列值过滤-->
<div class="line-value-filter-table" style="position:fixed;z-index:1000;width: 320px;display: none;">         
  <form id="lineValueForm" name="lineValueForm" method="post" action="<#noparse>${searchFormURL}</#noparse>"> </form> 
</div> 
<#--过滤条件-->
<#noparse><#if filterFields?if_exists>
<div class="panel" ajax="ajax"  displayId="${bpmDataTemplate.id}" filterKey="${filterKey}" dbomSql="${dbomSql}" dbomFKName="${dbomFKName}" dbomFKValue="${dbomFKValue}">
<#if filterFields?size gt 1>
<div class='panel-nav'>
  <div class="l-tab-links">
    <ul style="left: 0px; ">
      <#list filterFields as field>
      <li tabid="${field.key}" <#if field.key ==filterKey> class="l-selected"</#if>>
        <a href="${field.url}" title="${field.name}">${field.desc}</a>
      </li>
      </#list>
    </ul>
  </div>
</div>
</#if>
</#noparse> 
<#--查询头-->
  <div class="panel-top">
    <div class="tbar-title">
      <span class="tbar-label">${bpmDataTemplate.name}</span>
    </div>
      <div class="panel-toolbar">
        <div class="toolBar">
         
          <#if hasManage>
                    <#noparse><#list manageFields as manage>
                      <@genToolBar gendex='${manage_index}' manage=manage managePermission=managePermission actionUrl=actionUrl/>
                      </#list></#noparse>
                </#if>
        </div>
      </div>

   
    <#if hasCondition >
      <div class="panel-search">
         <#--搜索切换-->
        <div class="searchShift">
          <span class="searchTitle"></span>
          <span class="ss_btn">普通搜索</span>
          <label class="fr">展开</label>
        </div>

        <div class="search_box">
        <form id="searchForm" name="searchForm" method="post" action="<#noparse>${searchFormURL}</#noparse>">
        <#--查询条件-->
          <#if conditionFields?if_exists>
            <ul class="row">
            <#list conditionFields as field>
              <li var="${field.na}">
                <@genCondition field=field formatData=formatData/>
              </li>
            </#list>
            </ul>
             <#if hasCondition>
              <div class="group fr"><a class="link clearSearch" href="####" onclick="clearQueryForm()"><span></span>清空</a></div>             
              <div class="group fr"><a class="link queryFieldSet" href="####" onclick="openQuerySetDialog()"><span></span>查询条件设置</a></div>
              <div class="group fr"><a class="link ajaxSearch" href="####" onclick="handlerSearchAjax(this)">查询</a></div>
            </#if>
            
          </#if>
        </form>
        <div class="panel-complex-search">
          <div class="compSearchBox">
            <div class="csb_btnbox">
              <div class="fl label-angle">
                	搜索项
              </div>
            </div>
            <div>
              <div class="addBtn"><span>+</span></div>
            </div>
          </div>
        </div>
        </div>




      </div>

    </#if>
  </div>
  <#--表头-->  
  <div class="panel-body">
    <div class="panel-table">
      <!--表格model-->
      <div class="table-pop"></div>
      <!--表格控制-->
      <div class="table-manage"></div>
      <!--自定义配置项-->
      <div class="user-defined-table dis"></div> 
       <!--列值过滤-->
       <ul class="col-filter">
             <li style="color:#e0edff" <#noparse> onclick="sort(this,'ASC','${tableIdCode}')" action="${pageURL}"</#noparse>>
               <span class="iconfont icon-home"></span>
               <span>正序↑</span>
             </li>
             <li style="color:#e0edff" <#noparse> onclick="sort(this,'DESC','${tableIdCode}')" action="${pageURL}"</#noparse>>
               <span class="iconfont icon-home"></span>
               <span>倒序↓</span>
             </li>
             <li onmouseover="showLvfp(this)">
               列值过滤
             </li>
        </ul>
  
      <table cellpadding="1" cellspacing="1" class="table-grid table-list">
        <thead>
        <tr>
          <#noparse>
          <#if checkbox>
            <#assign tableLength = tableLength+1>
              <th> <input id="chkall" type="checkbox"></th>
                  </#if>
                  </#noparse>
          <#noparse>
            <#assign tableLength = tableLength+1>
            <th>序号</th>
          </#noparse>
                  <#-- 显示字段-->
          <#list displayFields as field>
               <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
                 <th name='${field.name}'>
              <#if field.type!="clob">
                <#noparse>
                <a href="####" onclick="linkAjax(this)" action="${pageURL}&${tableIdCode}__ns__=</#noparse>${field.name}<#noparse>"></#noparse>
                  ${field.desc}<#noparse><#if (sortField?? && sortField=="</#noparse>${field.name}<#noparse>")><#if (orderSeq=="ASC")>↑<#else>↓</#if></#if>
                </a>
                </#noparse>
              <#else>
                  ${field.desc}  
              </#if>
                <div class="group"><a class="link shouqi2" onclick="showUL(this)" href="####"></a></div>
                </th>
                <#noparse></#if></#noparse>
          </#list>
          <#if hasManage>
            <#assign tableLength = tableLength+1>
            <th width="120px">管理</th>
          </#if>
        </tr>
        </thead>
        <tbody>
        <#--表体-->        
        <#noparse>
          <#list bpmDataTemplate.list as data>
          <tr class="<#if data_index % 2 == 0>odd</#if><#if data_index % 2 == 1>even</#if>" align="center">
          <#if checkbox>
        </#noparse>
              <td style="width:30px;text-align: center;">
                          <input class="pk" type="checkbox" value="<#noparse>${data.</#noparse>${pkField}<#noparse>}</#noparse>" name="${pkField}">
                        </td>
                 <#noparse>
                    </#if>
                </#noparse>
        <#--序号-->
                <#noparse><td style="width:30px;">${data_index+1}</td> </#noparse>
          <#list displayFields as field>
            <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
              <td title="<#noparse>${data.</#noparse>show_${field.name}<#noparse>}</#noparse>">
                <@genDisplayS field=field /><#noparse>${data.</#noparse> ${field.name} <#noparse>}</#noparse><@genDisplayE field=field />
              </td>  
            <#noparse></#if></#noparse>
          </#list>
          <#if hasManage>
            <td  class="rowOps">
              <#noparse><#list manageFields as manage>
                <@genManage mindex='${manage_index}' manage=manage managePermission=managePermission actionUrl=actionUrl pk=data.</#noparse>${pkField}<#noparse>/>
              </#list></#noparse>
            </td>
          </#if>
          </tr>
        <#noparse></#list></#noparse>
        <#noparse>
          <#if bpmDataTemplate.list?size==0>
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
    <#noparse>
        <!--
        <#if bpmDataTemplate.list?size==0>
          <div class="panel-norecord">
            <div class="l-panel-bbar-inner">
              <div style="padding:6px 0px 12px 0px;">当前没有记录。</div>
            </div>
          </div>
        </#if>-->
      ${pageHtml}</#noparse>  
  </div>
</div>
<div style="display: none;" id="exportField" >
    <table cellpadding="1" cellspacing="1" class="table-grid table-list">
      <tr>
            <th width="30px"><input id="checkFieldAll" type="checkbox" checked="checked">选择</th>
            <th>字段</th>
      </tr>  
      <#list displayFields as field>
           <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
           <tr>
             <td><input class="field" type="checkbox" value="${field.name}" name="field" checked="checked"></td>
             <td>${field.desc}</td>
          <tr>
          <#noparse></#if></#noparse>
      </#list>
    </table>    
</div>
<script>
<#--
在reportutil.js中调用，获取参数脚本function ，
通过name匹配不同按钮获取按钮设置的参数脚本
-->
function getParam(name){
  switch(name){
    <#list manageFields as manage>
    case '${manage_index}_${manage.name}':
      <#if manage.paramscript!=null&&manage.paramscript!=''>
        ${manage.paramscript};
      <#else>
        return true;
      </#if>
      break;
    </#list>
  }
}
<#--
在ajaxgrid.js中调用，获取参数脚本function ，
通过idStartValue匹配不同数据，获取状态值
-->
function getStartId(idStartValue){
 switch(idStartValue){
    <#noparse>
    <#list bpmDataTemplate.list as data>
    case '${data.</#noparse>${pkField}<#noparse>}':
      <#if data.zt=='未发起'>
        return true;
      <#else>
        return false;
      </#if>
      break;
    </#list>
   </#noparse>
  }
}
<#--
在reportutil.js中调用，获取前置脚本function ，
通过name匹配不同按钮获取按钮设置的参数脚本
-->
function getPreScript(name){
  switch(name){
    <#list manageFields as manage>
    case '${manage_index}_${manage.name}':
      <#if manage.prescript!=null&&manage.prescript!=''>
        ${manage.prescript};
      <#else>
        return true;
      </#if>
      break;
    </#list>
  }
}
<#--
在reportutil.js中调用，获取后置脚本function ，
通过name匹配不同按钮获取按钮设置的参数脚本
-->
function getAfterScript(name){
  switch(name){
    <#list manageFields as manage>
    case '${manage_index}_${manage.name}':
      <#if manage.afterscript!=null&&manage.afterscript!=''>
        ${manage.afterscript};
      <#else>
        return true;
      </#if>
      break;
    </#list>
  }
}
</script>
<form id="exportForm" name="exportForm" method="post" target="download" action="exportData.do" style="display: none;"></form>  
<iframe id="download" name="download" height="0px" width="0px" style="display: none;"></iframe>     
<input type="hidden" id="recRightField" name="recRightField" value='<#noparse> ${bpmDataTemplate.recRightField}</#noparse> '/>
<#noparse>
<#else>
   <div style="padding:6px 0px 12px 0px;">当前用户没有满足的过滤条件,请设置过滤条件。<div>
</#if>
</#noparse> 
