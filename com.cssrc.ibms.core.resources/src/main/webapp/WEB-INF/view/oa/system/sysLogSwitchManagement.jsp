<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<%@include file="/commons/include/get.jsp" %>
<title>系统日志开关管理列表</title>
	<script type="text/javascript">
		
		var dialog=null;
		function setStatus(){
			
			var logPks=getLogPk();
			if(logPks==""){
				$.ligerDialog.warn('还没有选择,请选择一项记录!','提示');
				return;
			}
			
			dialog=$.ligerDialog.open({title:'设置状态',target:$("#dialogStatus"),width:360,buttons:
				[ {text : '确定',onclick: setStatusOk},
				{text : '取消',onclick: function (item, dialog) {
						dialog.hide();
					}
				}]});	
			
			//先移除dialog绑定的关闭事件
			$(".l-dialog-close").unbind("click");
			//手动添加dialog关闭时处理事件
			$(".l-dialog-close").click(function(event,dialog){
				dialog.hide();
			});
			dialog.show();
		};
		
		function getLogPk(){
			var aryChk=$("input:checkbox[name='pk']:checked");
			if(aryChk.length==0) return "";
			var aryLogPk=[];
			aryChk.each(function(){
				aryLogPk.push($(this).val());
			});
			return aryLogPk.join(",");
		}
		
		function setStatusOk(item, dialog){
			var logStatus = $("#logStatus").val();
			var logPks=getLogPk();
			var execTypes=getCheckedTypes();
			
			var params={logPks:logPks,logStatus:logStatus,execTypes:execTypes};
			var url="${ctx}/oa/system/sysLogSwitch/setLogStatus.do";
			$.post(url,params,function(responseText){
				var obj=new com.ibms.form.ResultMessage(responseText);
				if(obj.isSuccess()){
					$.ligerDialog.success('保存成功!','提示',function(){
						dialog.hide();
						var url=location.href.getNewUrl();
						location.href=url;
					});
				}
				else{
					$.ligerDialog.err('提示','保存失败!',obj.getMessage());
				}
			}); 
		}
		
		//获取选中的日志类型
		function getCheckedTypes(){
			var aryChk=$("input:checkbox[name='execType']:checked");
			if(aryChk.length==0)
				return "";
			var execTypes=[];
			aryChk.each(function(){
				execTypes.push($(this).val());
			});
			return execTypes.join(",");
		}
	</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">系统日志开关管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<f:a alias="addUser" css="link edit" href="####" onclick="setStatus();">设置状态</f:a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table export="false" name="sysLogSwitchList" id="sysLogSwitchItem" requestURI="management.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;text-align:center;">
				  	<input type="checkbox" class="pk" name="pk" value="${sysLogSwitchItem.model}">
				</display:column>
				<display:column property="model" title="模块名" sortable="true" sortName="model" style="text-align:left"></display:column>
				<display:column property="execTypes" title="日志类型" style="text-align:left"></display:column>
				<display:column property="memo" title="备注" sortable="true" sortName="memo" style="text-align:left"></display:column>
               	<display:column title="状态" sortable="true" sortName="status">
					<c:choose>
						<c:when test="${sysLogSwitchItem.status eq 1 }"> 开启</c:when>
						<c:otherwise><sapn style="color:red;">关闭</sapn></c:otherwise>
					</c:choose>
				</display:column>
				<display:column title="管理" media="html" style="text-align:center;width:13%;" class="rowOps">
					<a href="edit.do?id=${sysLogSwitchItem.id}&model=${sysLogSwitchItem.model}" class="link edit">编辑</a>
				</display:column>
			</display:table>
			<%-- <ibms:paging tableId="sysLogSwitchItem"/> --%>
		</div>
	</div>
</body>
<div id="dialogStatus" style="display: none;">
	<div id="logSwitchEditDiv" style="margin-left: -3px;">
		<table id="logSwitchEditTable" style="width:100%;">
			<tr id="logSwitchEditTr" class="logSwitchEditTr">
				<td colspan="4">
					<div style="background:#EEE;padding:0 0 18px 0;">
						<div style="padding: 5px">
							<span class="edit-label">状&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;态:</span>
							<span>
								<select id="logStatus" class="select" style="width:230px !important">
									<option value="1" selected="selected">开启</option>
									<option value="0" >关闭</option>
								</select>	
							</span>
						</div>
					</div>
					<div style="background:#EEE;padding:0 0 18px 0;">
						<div style="padding: 5px">
							<span class="edit-label">日志类型:</span>
							<span>
								<ul style="overflow:hidden;">
									<c:forEach items="${execTypeList}" var="item">
										<li style="width:33.3%;float:left;">
										   <input type="checkbox" checked name="execType" value="${item}" />
								            ${item}
								        <li>
									</c:forEach>
								</ul>	
							</span>
						</div>
					</div>
				</td>
			</tr>
		</table>
	</div>
</div>
</html>


