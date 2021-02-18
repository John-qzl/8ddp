<div id="${toolbarId}" class="mini-toolbar" style="padding:2px;" >
    <table style="width:100%;">
        <tr>
            <td style="width:100%;" id="toolbarBody">
            	<#if save=='true'>
               	 <a class="mini-button" iconCls="icon-save" plain="true" onclick="onOk">保存</a>
                </#if>
                <#if extToolbars??>
                ${extToolbars}
                </#if>
                <#if pkId??>
                	 <#if remove=='true'>
                	 	<a class="mini-button" iconCls="icon-remove" plain="true" onclick="onDelete()">删除</a>
                	</#if>
	                <#if hideRecordNav=="false" || hideRecordNav==''>
		                <#if prevRecord=="true">
		                <a class="mini-button" iconCls="icon-prev" plain="true" onclick="preRecord()">上一条</a>
		                </#if>
		                <#if nextRecord=="true">
		                <a class="mini-button" iconCls="icon-next" plain="true" onclick="nextRecord()">下一条</a>
		                </#if>
	                </#if>
                </#if>
                <a class="mini-button" iconCls="icon-close" plain="true" onclick="onCancel">关闭</a>
                
            </td>
        </tr>
    </table>
</div>