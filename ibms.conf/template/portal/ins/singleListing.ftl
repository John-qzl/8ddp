<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>

<#noparse>
<#setting number_format="#">
<#assign displayFields=bpmDataTemplate.displayField?eval>
<#assign tableLength = 1>
  
</#noparse>
<#--表头-->  
<div class="panel">
<div class="panel-body">
  <div class="panel-table">
    <table cellpadding="1" cellspacing="1" class="table-grid table-list">
      <thead>
      <tr>
        <#noparse>
          <#assign tableLength = tableLength+1>
          <th>序号</th>
        </#noparse>
              <#-- 显示字段-->
        <#list displayFields as field>
             <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
               <th>${field.desc}</th>
            <#noparse></#if></#noparse>
        </#list>
      </tr>
      </thead>
      <tbody>
      <#--表体-->        
      <#noparse>
        <#list bpmDataTemplate.list as data>
        <tr pk="${data.ID}" class="<#if data_index % 2 == 0>odd</#if><#if data_index % 2 == 1>even</#if>" align="center">
            </#noparse>
      <#--序号-->
            <#noparse><td style="width:30px;">${data_index+1}</td> </#noparse>
        <#list displayFields as field>
          <#noparse><#if permission.</#noparse>${field.name}<#noparse>></#noparse>
            <td title="<#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>">
              <#noparse>${data.</#noparse>${field.name}<#noparse>}</#noparse>
            </td>  
          <#noparse></#if></#noparse>
        </#list>
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
     <input type="hidden" name="displayId" id="displayId" value="${bpmDataTemplate.id}">
    </table>
  </div>
</div>
</div>