		<ul id="popupAddMenu" class="mini-menu" style="display:none;">
		 	<#if add=="true">
            <li iconCls="icon-create" onclick="add()">新建</li>
            </#if>
            <#if copyAdd=="true">
            <li iconCls="icon-copyAdd" onclick="copyAdd()">复制新建</li>
            </#if>
        </ul>
        <ul id="popupSearchMenu" class="mini-menu" style="display:none;">
         	<#if newSearch=="true">
            <li iconCls="icon-addSearch" onclick="newSearch('${entityName}')">新建查询方案</li>
            </#if>
            <#if searchList=="true">
            <li iconCls="icon-loadSearch" onclick="searchList('${entityName}')">查询方案列表</li>
            </#if>
        </ul>
        <ul id="popupAttachMenu" class="mini-menu" style="display:none;">
         	<#if newAttach=="true">
            	<li iconCls="icon-attachAdd" onclick="newAttach('${entityName}')">新建附件</li>
            </#if>
             <#if previewAttachs=="true">
            	<li iconCls="icon-preview" onclick="previewAttachs('${entityName}')">预览所有附件</li>
            </#if>
            <#if downloadAttachs=="true">
            	<li iconCls="icon-download" onclick="downloadAttachs()">下载附件列表</li>
            </#if>
        </ul>
        <ul id="popupSettingMenu" class="mini-menu" style="display:none;">
         	<#if saveCurGridView=="true">
            <li iconCls="icon-save" onclick="saveCurGridView()">保存当前方案</li>
            </#if>
             <#if saveAsNewGridView=="true">
            <li iconCls="icon-save" onclick="saveAsNewGridView('${entityName}')">保存为新方案</li>
            </#if>
            
            <li>
                <span iconCls="icon-download">导出</span>
                <ul id="popupExportMenu" class="mini-menu">
                	<#if exportCurPage=="true">
                    <li iconCls="icon-excel" onclick="exportCurPage()">导出当前页</li>
                    </#if>
                    <#if exportAllPage=="true">
                    <li iconCls="icon-excel" onclick="exportAllPage()">导出所有页</li>
                    </#if>
                    
                </ul>
            </li>
        </ul>
        
        <div class="mini-toolbar" style="border-bottom:0;margin:0;padding:0;">
            <table style="width:100%;">
                <tr>
                    <td style="width:100%;">
                    	<#if detail=="true">
                        <a class="mini-button" iconCls="icon-detail" plain="true" onclick="detail()">明细</a>
                        </#if>
                        <#if popupAddMenu=="true">
                        <a class="mini-menubutton" iconCls="icon-create" plain="true" menu="#popupAddMenu">增加</a>
                        </#if>
                        <#if edit=="true">
                        <a class="mini-button" iconCls="icon-edit" plain="true" onclick="edit()">编辑</a>
                        </#if>
                        <#if remove=="true">
                        <a class="mini-button" iconCls="icon-remove" plain="true" onclick="remove()">删除</a>
                        </#if>                        
                       <#if popupSearchMenu=="true">
                        <a class="mini-menubutton" iconCls="icon-search" plain="true" menu="#popupSearchMenu">高级查询方案</a>
                        </#if>
                        <#if popupAttachMenu=="true">
                        <a class="mini-menubutton" iconCls="icon-attach" plain="true" menu="#popupAttachMenu">附件</a>
                        </#if>
                        <#if extToolbars??>
                        ${extToolbars}
                        </#if>
                    </td>
                    <td style="white-space:nowrap;text-align:right">
                     <#if popupSettingMenu=="true">
                        <a class="mini-menubutton" iconCls="icon-setting" plain="true" menu="#popupSettingMenu">工具</a>
                     </#if>   
                    </td>
                </tr>
                
                <!--<#if fieldSearch=="true">
	                <tr>
	                    <td style="white-space:nowrap;border-top:1px solid #909aa6;padding:5px;" colspan="2">
	                        请选择查询字段：
	                        <input id="fieldName" class="mini-combobox" textField="title" valueField="attrName" 
	                               parentField="parentId"  onvaluechanged="onSearchFieldChanged" 
	                               url="${rootPath}/ui/module/getModuleFields.do?entityName=${entityName}"/>
	                        <input id="fieldCompare" class="mini-combobox" textField="fieldOpLabel" valueField="fieldOp"/>
	                        <input id="fieldVal" class="mini-textbox" emptyText="请输入查询条件值" style="width:150px;" onenter="onKeyEnter"/>
	                        <a class="mini-button" iconCls="icon-search" onclick="search()">查询</a>
	                        <a class="mini-button" iconCls="icon-cancel" onclick="clearSearch()">清空查询</a>
	                    </td>
	                </tr>
                 </#if>-->
            </table>
             <#if selfSearch??>
             	${selfSearch}
             </#if>
        </div>