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
<#assign conditionFields=bpmDataTemplate.conditionField?eval>

<#noparse>
<#setting number_format="#">
<#assign conditionFields=bpmDataTemplate.conditionField?eval>
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
        从:
        <input type="text" name="Q_begin${field.na}_DL" readonly="readonly" datefmt="${field.ftm}" class="wdateTime inputText" value="<#noparse>${param[</#noparse>'Q_begin${field.na}_DL'<#noparse>]}"</#noparse>  />
        </li><li>到:
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
        从:
        <input type="text" name="Q_begin${field.na}_NL" class="inputText" value="<#noparse>${param[</#noparse>'Q_begin${field.na}_NL'<#noparse>]}"</#noparse>  />
        </li><li>到:
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
        <input type="text" readonly="readonly" class="inputText" name="Q_${field.na}~_SL" id="${field.na}" onclick="__Selector__.selectPos({self:this});" value="<#noparse>${param[</#noparse>'Q_${field.na}~_SL'<#noparse>]}"</#noparse>>
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
  <select  name="Q_${field.na}_${field.qt}">
    <option value="">全部</option>
  <#if formatData?if_exists >
    <#list formatData?keys as key>
    <option value="${key}"  <#noparse><#if param['</#noparse>Q_${field.na}_${field.qt}'] == '${key}'>selected<#noparse></#if></#noparse>>${formatData[key]}</option>
    </#list>
  </#if>
  </select>
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
      <span class="label">${field.cm}:</span>    
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
      <#case "3"><#--数据字典-->
        <#if formatData[field.na]?if_exists >
          <@genQuerySelect field=field formatData=formatData[field.na]/>
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



<#--查询头-->
  <div class="panel-top">
    <div class="tbar-title">
      <span class="tbar-label">${bpmDataTemplate.name}</span>
    </div>
      <div class="panel-toolbar">
        <div class="toolBar">
          <#if hasCondition>
            <div class="group"><a class="link ajaxSearch" href="####" onclick="handlerSearchAjax(this)">查询</a></div>
            
          </#if>
          <#if hasManage>
                    <#noparse><#list manageFields as manage>
                      <@genToolBar gendex='${manage_index}' manage=manage managePermission=managePermission actionUrl=actionUrl/>
                      </#list></#noparse>
                </#if>
        </div>
      </div>
    <#if hasCondition >
      <div class="panel-search">
        <form id="searchForm" name="searchForm" method="post">
        <input type="hidden" name="filter" value="${filter}"/>
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


