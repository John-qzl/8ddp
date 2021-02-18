<div id="${toolbarId}" class="mini-toolbar" style="padding:2px;">
    <table style="width:100%;">
        <tr>
	        <td style="width:100%;">
	            <a class="mini-button" iconCls="icon-close" plain="true" onclick="onClose()">关闭</a>
	            <#if extToolbars??>
                ${extToolbars}
                </#if>
	            <#if hideRecordNav=="false">
					<#if prevRecord=="true">
		            <a class="mini-button" iconCls="icon-prev" plain="true" onclick="preRecord()">上一条</a>
		            </#if>
		            <#if nextRecord=="true">
		            <a class="mini-button" iconCls="icon-next" plain="true" onclick="nextRecord()">下一条</a>
		            </#if>
	            </#if>
	            <#if remove=="true">
	            <a class="mini-button" iconCls="icon-remove" plain="true" onclick="onDelete()">删除</a>
	            </#if>
	        </td>
        </tr>
    </table>
</div>