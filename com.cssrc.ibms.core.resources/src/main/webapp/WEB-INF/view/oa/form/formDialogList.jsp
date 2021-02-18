
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>通用表单对话框管理</title>
<%@include file="/commons/include/get.jsp" %>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/bpm/ImportExportXmlUtil.js" charset="UTF-8"></script>
<script type="text/javascript">
	var win;
	function preview(alias){
		//向对话框传入参数
		var paramValueString = "" ;
		if(alias==null || alias==undefined){
			$.ligerDialog.warn("别名为空！",'提示信息');
			return;
		}
		var isPreviewCallCode = false ;
		if(alias.indexOf("callCode-")==0){
			isPreviewCallCode = true;
			alias = alias.replace("callCode-","");
		}
		
		var url=__ctx + "/oa/form/formDialog/dialogObj.do?alias=" +alias;
		url=url.getNewUrl();
		var isParamsNeeded = false ;
		$.ajax({  
			type : "post",  
			url  : url,  
			data : {"alias":alias},   
			async : false,  
			success : function(data){
			if(data.success==0){
				$.ligerDialog.warn("输入别名不正确！",'提示信息');
				isParamsNeeded = true ;
				return;
			}
			var obj=data.bpmFormDialog;
			var fieldObj=eval("("+obj.conditionfield.trim()+")") ;
			var paramArr = [] ;
			var urlForParams = __ctx + "/oa/form/formDialog/params.do?alias="+alias;
			for(var i=0,c;c=fieldObj[i++];){
				//4：动态传入参数，5：java脚本参数
				if(c.defaultType=="4"){
					paramArr.push(c.field) ;
					isParamsNeeded = true ;
				}else if(c.defaultType=="5" && c.dbType=='isAfferent'){
					paramArr.push(c.field) ;
					isParamsNeeded = true ;
				}
			}
			//需要传入参数
			urlForParams += "&params="+JSON2.stringify(paramArr)+"&isPreviewCallCode="+isPreviewCallCode;
			if(isParamsNeeded){
				if(isPreviewCallCode){
					urlForParams += "&resultfield="+obj.resultfield.trim() ;
					$.ligerDialog.open({ url:urlForParams, height: obj.height,width: obj.width,title:'对话框调用说明',name:'paramDialog_'});
				}else{
				//若取消，返回null
				$.ligerDialog.open({ url:urlForParams, height: obj.height,width: obj.width,title:'对话框参数传入',name:'paramDialog_',
					buttons: [ { text: '确定', onclick: function (item, dialog) { 
						paramValueString = $('#paramDialog_')[0].contentWindow.selectForm() ;						
						CommonDialog(alias,function(data){
							var json=JSON2.stringify(data);
							$("#txtJsonData").val(json);
							if(!win){
								var obj=$("#divJsonData");
								win= $.ligerDialog.open({ target:obj , height: 300,width:500, modal :true}); 
							}
							win.show();
						},paramValueString);
						dialog.close();
					} }, 
					{ text: '取消', onclick: function (item, dialog) { dialog.close(); } } ] });
				
				}
			}else if(isPreviewCallCode){
				urlForParams += "&resultfield="+obj.resultfield.trim() ;
				$.ligerDialog.open({ url:urlForParams, height: obj.height,width: obj.width,title:'对话框调用代码预览',name:'paramDialog_'});
			}
			}
		});
		if(isParamsNeeded || isPreviewCallCode) return ;
		CommonDialog(alias,function(data){			
			var json=JSON2.stringify(data);
			var obj = null;
			//第二级弹窗出现在第一级之上
			if(!win){
				$("#txtJsonData").val(json);
				obj=$("#divJsonData");
			}else{
				window.top.$("#txtJsonData").val(json);
				obj= window.top.$("#divJsonData");
			}
			win= window.top.$.ligerDialog.open({ target:obj , height: 300,width:500, modal :true});
			//点击关闭后，再打开出错的问题
			dialog = window.top.$('#divJsonData').parents('.l-dialog');
		 	$(".l-dialog-close",dialog).unbind("click");
	 		$(".l-dialog-close",dialog).click(function (){
	 			$('.l-window-mask').hide();
			 	dialog.hide();
	 		}); 
		},paramValueString,'false');
	}
	function exportXml(){	
	
		var tableIds = ImportExportXml.getChkValue('pk');
		var url=__ctx + "/oa/form/formDialog/exportXml.do?tableIds="+tableIds;
		if (tableIds  ==''){
			
			$.ligerDialog.confirm('还没有选择，是否导出全部？','提示信息',function(rtn) {
				if(rtn) {
					var form=new com.ibms.form.Form();
					form.creatForm("form", url);
					form.submit();
				}
			});
		}else{

		$.ligerDialog.confirm('确认导出吗？','提示信息',function(rtn) {
			if(rtn) {
				var form=new com.ibms.form.Form();
				form.creatForm("form", url);
				form.submit();
			}
		});
		}
	}
	//导入自定义对话框
	function importXml(){
		var url=__ctx + "/oa/form/formDialog/import.do";
		ImportExportXml.showModalDialog({url:url,title:'导入自定义对话框'});
	}
	
	function edit(id,returnUrl){
		href="edit.do?id="+id+"&returnUrl="+returnUrl;
		location.href=href;
	}
	
	
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">通用表单对话框管理列表</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group"><a class="link search" id="btnSearch">查询</a></div>
					
					<div class="group"><a class="link add" href="edit.do">添加</a></div>
					
					<div class="group"><a class="link update" id="btnUpd" action="edit.do">编辑</a></div>
					
					<div class="group"><a class="link del"  action="del.do">删除</a></div>
					<div class="group">
					<a onclick="exportXml()"  class="link export">导出</a>
					</div>
	   				
					<div class="group">
					<a onclick="importXml()"  class="link import">导入</a>
					</div>
					
				</div>
			</div>
			<div class="panel-search">
				<form id="searchForm" class="plat-form" method="get" action="list.do">
						<ul class="row plat-row">
								<li><span class="label">名称:</span><input type="text" name="Q_name_SL"  class="inputText" value="${param['Q_name_SL']}"/></li>
								<li><span class="label">别名:</span><input type="text" name="Q_alias_SL"  class="inputText" value="${param['Q_alias_SL']}"/></li>
						</ul>
				</form>
			</div>
		</div>
		<div class="panel-body">
	    	<c:set var="checkAll">
				<input type="checkbox" id="chkall"/>
			</c:set>
		    <display:table name="bpmFormDialogList" id="bpmFormDialogItem" requestURI="list.do" sort="external" cellpadding="1" cellspacing="1"   class="table-grid">
				<display:column title="${checkAll}" media="html" style="width:30px;">
					  	<input type="checkbox" class="pk" name="id" value="${bpmFormDialogItem.id}">
				</display:column>
				<display:column property="name" title="名称" sortable="true" sortName="name"></display:column>
				<display:column property="alias" title="别名" sortable="true" sortName="alias"></display:column>
				<display:column  title="显示样式" sortable="true" sortName="style">
				
					<c:choose>
						<c:when test="${bpmFormDialogItem.style==0}">
							<span class="green">列表</span>
						</c:when>
						<c:otherwise>
							<span class="red">树形</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column  title="是否单选" sortable="true" sortName="issingle">
					<c:choose>
						<c:when test="${bpmFormDialogItem.issingle==0}">
							<span class="red">多选</span>
						</c:when>
						<c:otherwise>
							<span class="green">单选</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column property="width" title="宽度" sortable="true" sortName="width"></display:column>
				<display:column property="height" title="高度" sortable="true" sortName="height"></display:column>
				<display:column  title="是否为表" sortable="true" sortName="istable">
					<c:choose>
						<c:when test="${bpmFormDialogItem.istable==0}">
							<span class="red">视图</span>
						</c:when>
						<c:otherwise>
							<span class="green">数据库表</span>
						</c:otherwise>
					</c:choose>
				</display:column>
				<display:column property="objname" title="对象名称" sortable="true" sortName="objname"></display:column>
				<display:column property="dsalias" title="数据源名称" sortable="true" sortName="dsalias"></display:column>
				<display:column title="管理" media="html" style="width:50px;text-align:center" class="rowOps">
					<f:a alias="delFormDialog" href="del.do?id=${bpmFormDialogItem.id}" css="link del">删除</f:a>
					<a href="javascript:void(0);" onclick="edit('${bpmFormDialogItem.id}','${returnUrl}');" class="link edit">编辑</a>
					<a href="javascript:preview('${bpmFormDialogItem.alias}')"  class="link detail">预览</a>
					<a href="javascript:preview('callCode-${bpmFormDialogItem.alias}')"  class="link copyTo">开发帮助</a>
				</display:column>
			</display:table>
			<ibms:paging tableId="bpmFormDialogItem"/>
			
		</div><!-- end of panel-body -->				
	</div> <!-- end of panel -->
	
	<div id="divJsonData" style="display: none;">
		<div>对话框返回的JSON格式数据。</div>
		<ul>
			<li >1.单选为JSON对象数据,字段为return字段。</li>
			<li>2.多选为JSON数组,字段为return字段。</li>
		</ul>
		<textarea id="txtJsonData" rows="10" cols="80" style="height: 180px;width:480px"></textarea>
	</div>
</body>
</html>


