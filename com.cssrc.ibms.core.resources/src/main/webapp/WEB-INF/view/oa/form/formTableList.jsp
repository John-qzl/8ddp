<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>自定义表管理</title>
<script type="text/javascript" src="${ctx }/jslib/lg/plugins/ligerWindow.js"></script>
<script type="text/javascript" src="${ctx }/jslib/ibms/oa/bpm/SetRelationWindow.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js" charset="UTF-8"></script>
<script type="text/javascript">
	var win;
	$(function(){
		$("table.table-grid td a.link.del").unbind("click");
		delFormTable();
		$("a.reset").click(function(){
			var me = $(this),
				url = me.attr("prehref"),
				message ='重置操作将<font color=red>删除物理表</font>，并重置表的状态为未生成。已经绑定表单的自定义表不能重置。是否重置该表? ';
			$.ligerDialog.confirm(message,'提示',function(rtn) {
				if(rtn){
					if($.browser.msie){
						$.gotoDialogPage(url);
					}
					else{
						location.href=url;
					}
				}
			});
		});
	});
	
	
	function delFormTable(){	
		$("table.table-grid td a.link.del").click(function(){
			if($(this).hasClass('disabled')) return false;
			var ele=this,tableId = $(this).attr('action');
			$.post('isMain.do', {'tableId': tableId}, function(data) {
				var message = data.isMain?"是否删除该表":"是否删除该表";
					$.ligerDialog.confirm(message,'提示',function(rtn) {
						if(rtn){
							if($.browser.msie){
								$.gotoDialogPage(ele.href);
							}
							else{ 
								location.href=ele.href;
							}
						}
					});
					return false;
			});
			return false;
		});
	}
	
	
	function showRelation(tableId,dsName) {
		var conf={tableId:tableId,dsName:dsName};
		SetRelationWindow(conf);
	}
	
	function assignMainTable(subTableId){
		var url=__ctx + "/oa/form/formTable/assignMainTable.do?subTableId="+subTableId;
		url=url.getNewUrl();
		
		DialogUtil.open({
			height:200,
			width: 400,
			title : '',
			url: url, 
			isResize: true
		});
	}
	
	//复制表
	function copyTable(tableId){
		var url= __ctx + "/oa/form/formTable/copyTable.do?tableId="+tableId+"&addSync=false";
		ImportExportXml.showModalDialog({url:url,width:750,height:450,scroll:1});
	}
	
	function copySyncTable(tableId){
		var url= __ctx + "/oa/form/formTable/copyTable.do?tableId="+tableId+"&addSync=true";
		ImportExportXml.showModalDialog({url:url,width:750,height:450,scroll:1});
	}
	
	function newTableTemp(e){		
		var url=__ctx + '/oa/form/tableTemplate/edit.do?tableId='+e;
		win= $.ligerDialog.open({ url: url, height: 400,width:600 ,isResize: false });		
	}
	function closeWin(){
		if(win){
			win.close();
		}		
	}	
	function generator(tableId) {
		$.ligerDialog.confirm('将连同子表一起生成,是否继续?','提示信息',function(rtn){
			if(!rtn) return;

			$.post('generateTable.do', {'tableId': tableId}, function(data) {
				var obj=new com.ibms.form.ResultMessage(data);
				if(obj.isSuccess()){//成功
					$.ligerDialog.success("生成成功",'提示信息',function(){
						location.reload();
					});
				
			    }else{//失败
			    	$.ligerDialog.err('出错信息',"生成自定义表失败",obj.getMessage());
			    }
			});

		});
	}
	
	// 导出自定义表
	function ExportXml(){
		var tableId = "";
		$("input[type='checkbox'][class='pk']:checked").each(function(){ 
				var obj=$(this);
				if(obj.attr('id')==1)
					tableId+= obj.val()+",";
		
		});
		if(tableId.length==0){
			$.ligerDialog.warn("请选择主表进行导出!");
			return;
		}else{
			tableId=tableId.substring(0,tableId.length-1);
		}
		var url = __ctx + "/oa/form/formTable/export.do?tableIds="+tableId;
		ImportExportXml.showModalDialog({url:url});
		
	}
	//导入自定义xml
	function ImportXml(){
		var url= __ctx + "/oa/form/formTable/import.do";
		ImportExportXml.showModalDialog({url:url});
	}
	
	function edit(tableId,returnUrl){
		var href="edit.do?tableId="+tableId+"&returnUrl="+returnUrl;
		location.href=href;
	}
	
	//批量生成表 
	function GenerateTable(){
		var tableId = "";
		var arr = [];
		$("input[type='checkbox'][class='pk']:checked").each(function(i,e){ 
			var obj=$(e);
			tableId+= obj.val()+",";
			if(obj.parents('tr').find('td').eq(4).find('span').hasClass('green')){
				arr.push(i+1);
				//alert(i);
			}
	});
		if(tableId.length==0){
			$.ligerDialog.warn("请选择主表进行批量生成!");
			return;	
		}else if(arr.length>0){
			$.ligerDialog.warn("第"+arr.toString()+"行主表已经生成，请选择未生成的表！");	
		}else{
			
			tableId=tableId.substring(0,tableId.length-1);
			
			$.ligerDialog.confirm('将连同子表一起生成,是否继续?','提示信息',function(rtn){
				if(!rtn) return;
				$.post('produceTable.do', {'tableId': tableId}, function(data) {
					var obj=new com.ibms.form.ResultMessage(data);
					if(obj.isSuccess()){//成功
						$.ligerDialog.success("生成成功",'提示信息',function(){
							location.reload();
						});					
				    }else{//失败
				    	console.log(obj);
				    	$.ligerDialog.err('出错信息',"生成自定义表失败",obj.getMessage());
				    }
				});

			});
		}
	}
	function trans(){
		location.href='edit.do';
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">自定义表管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link search" id="btnSearch">查询</a>
					</div>
					<div class="group">
						<!-- <a class="link add" href="edit.do">添加</a> -->
						<a class="link add" href="####" onclick="trans()">添加</a>
					</div>
					<div class="group"><f:a alias="delForm" css="link del" action="delByTableId.do">删除</f:a></div>
					<%--<div class="group">
						<a class="link add" href="defExtTable1.do" >添加外部表</a>
					</div>
					
					
				    --%>
					<div class="group">
						<a class="link export" href="####" onclick="ExportXml()">导出</a>
					</div>
					
					
				    <div class="group">
					<a   class="link import"  href="####" onclick="ImportXml()">导入</a></div>
				    
				    
				     <div class="group">
						<a class="link generate" href="####" onclick="GenerateTable()"><span>
						</span>批量生成表</a>
					 </div>
					</div>
				</div>
				<div class="panel-search">
					<form id="searchForm" class="plat-form" method="get" action="list.do">
						<ul class="row plat-row">
							<li><span class="label">表名:</span>
								<input type="text" 
								name="Q_tableName_SL" class="inputText"
								value="${param['Q_tableName_SL']}" />
							</li>
							<li> <span class="label">注释:</span>
								<input type="text"
								 name="Q_tableDesc_SL" class="inputText"
								value="${param['Q_tableDesc_SL']}" /> 
							</li>
							<li>
								<span class="label">是否主表:</span>
								<select name="Q_isMain_SN" value="${param['Q_isMain_SN']}">
									<option value="" >全部</option>
									<option value="1"
										<c:if test="${param['Q_isMain_SN'] == '1'}">selected</c:if>>主表</option>
									<option value="0"
										<c:if test="${param['Q_isMain_SN'] == '0'}">selected</c:if>>子表</option>
								</select>
							</li>
							<li> <span class="label">生成方式:</span> 
								<select name="Q_genByForm_SN" value="${param['Q_genByForm_SN']}">
								<option value="">全部</option>
								<option value="0" 
									<c:if test="${param['Q_genByForm_SN'] == '0'}">selected</c:if>>自定义表</option>
								<option value="1" 
									<c:if test="${param['Q_genByForm_SN'] == '1'}">selected</c:if>>由表单生成</option>
								</select>
							</li>
							<li> <span class="label">是否生成:</span>
								 <select name="Q_isPublished_SN" value="${param['Q_isPublished_SN']}">
									<option value="">全部</option>
									<option value="0"
										<c:if test="${param['Q_isPublished_SN'] == '0'}">selected</c:if>>未生成</option>
									<option value="1"
										<c:if test="${param['Q_isPublished_SN'] == '1'}">selected</c:if>>已生成</option>
								</select>
							</li>
						</ul>
					</form>
				</div>
		</div>
		<div class="panel-body">
				<c:set var="checkAll">
					<input type="checkbox" id="chkall" />
				</c:set>
				<display:table name="bpmFormTableList" id="bpmFormTableItem"
					requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"
					export="false" class="table-grid">
					<display:column title="${checkAll}" media="html" style="width:30px;">
						<input type="checkbox" class="pk" name="tableId" id="${bpmFormTableItem.isMain}" value="${bpmFormTableItem.tableId}">
					</display:column>
					<display:column property="tableName" title="表名" sortable="true" sortName="tableName" style="text-align:left"></display:column>
					<display:column property="tableDesc" title="注释" sortable="true" sortName="tableDesc" style="text-align:left"></display:column>
					<display:column title="属性" style="text-align:left">
						<c:choose>
							<c:when test="${bpmFormTableItem.isMain == 1}">主表</c:when>
							<c:otherwise>子表</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="状态" style="text-align:center">
						<c:choose>
							<c:when test="${bpmFormTableItem.isPublished==0}">
								<span class="red">未生成</span>
							</c:when>
							<c:otherwise>
								<span class="green">已生成</span>
							</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="数据源">
							${bpmFormTableItem.dsAlias}
					</display:column>
					<display:column title="生成方式" style="text-align:left">
						<c:choose>
							<c:when test="${bpmFormTableItem.genByForm == 0}">
								<span class="red">自定义表</span>
							</c:when>
							<c:otherwise>
								<span class="green">由表单生成</span>
							</c:otherwise>
						</c:choose>
					</display:column>
					<display:column title="管理" media="html" style=" width:50px;text-align:center;" class="rowOps">
						<c:choose>
							<c:when test="${bpmFormTableItem.isExternal == 0 }">
								<c:if test="${bpmFormTableItem.genByForm==0}">
									<a href="javascript:void(0);" onclick="edit('${bpmFormTableItem.tableId}','${returnUrl }');" class="link edit">编辑</a>
									<f:a alias="delTable" href="delByTableId.do?tableId=${bpmFormTableItem.tableId}" action ="${bpmFormTableItem.tableId}" css="link del">删除</f:a>
									 
									<a href="####" prehref="reset.do?tableId=${bpmFormTableItem.tableId}" class="link reset" >重置</a>
									
									 
									<a href="team.do?tableId=${bpmFormTableItem.tableId}" class="link setting">分组</a>				
									<c:if test="${bpmFormTableItem.isMain == 1  && bpmFormTableItem.isPublished==0}">
										<a href="####" class="link table"
											onclick="generator('${bpmFormTableItem.tableId}')">生成表</a>
									</c:if> 
								</c:if>
							</c:when>
							<c:otherwise>
								<a href="javascript:void(0);" onclick="edit('${bpmFormTableItem.tableId}','${returnUrl }');" class="link edit">编辑</a>
								<f:a alias="delTable" href="delExtTableById.do?tableId=${bpmFormTableItem.tableId}" css="link del">删除</f:a>
								 
								<a href="team.do?tableId=${bpmFormTableItem.tableId}" class="link setting">分组</a> 
							</c:otherwise>
						</c:choose>
						<a href="get.do?tableId=${bpmFormTableItem.tableId}"
							class="link detail">明细</a>  
							<c:if test="${bpmFormTableItem.isMain == 1 && bpmFormTableItem.genByForm==0 }">
								<a href="####" onclick="copySyncTable('${bpmFormTableItem.tableId}')" class="link copy">生成同步表</a>
							</c:if> 
						  
						<c:if test="${bpmFormTableItem.isPublished!=0}">
							<a href="editIndex.do?tableId=${bpmFormTableItem.tableId}&tableName=${bpmFormTableItem.tableName}" class="link edit">索引管理</a>
						</c:if>
						
					</display:column>
				</display:table>
			<ibms:paging tableId="bpmFormTableItem" />
		</div>
		<!-- end of panel-body -->
	</div>
	<!-- end of panel -->
</body>
</html>


