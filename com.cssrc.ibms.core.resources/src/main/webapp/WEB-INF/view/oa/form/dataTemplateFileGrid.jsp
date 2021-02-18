<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>自定义表单列表</title>
<%@include file="/commons/include/get.jsp" %>
<f:link href="jquery.qtip.css" ></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerGrid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/file/AttachFileDisplay.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/AttachMent.js"></script>
<script type="text/javascript">
	var pool = {};
	var fieldName = '${fileGrid.fieldName}';
	$(function(){
		var data = $.parseJSON($('#data').val());
		var columns = $.parseJSON($('#columns').val());		
		var operaterRight = $.parseJSON($('#operaterRight').val());
		pool = {data:data,columns:columns,operaterRight:operaterRight};
		var g = $("#fileGrid").ligerGrid({
			columns	 : columns,
			isScroll : true,
			checkbox : true,
			data     : data,
			width :'auto',
			height :"auto",
			usePager:"false",
			showTableToggleBtn :true,
			pageSize : "50",
			checkbox:true,
			rownumbers:true
    	});
		pool.grid = g;
		if(!operaterRight){
			operaterRight = {download:true,del:true};
		}
		if(!operaterRight.download){
			$('a.link.downLoad').remove();
		}
		if(!operaterRight.del){
			$('a.link.delFile').remove();
		}
		$('div.l-panel-bar').remove();
	});
	function getSelcetData(key){
		var rows = pool.grid.getSelectedRows();
		var vals = [];
		for(var i=0;i<rows.length;i++){
			vals[i] = rows[i][key];
		}
		return vals;
	}
	
	function delAttach(){
		if(getSelcetData("fileId").length==0){
			$.ligerDialog.warn("请选择记录！");
			return;
		}
	    var fileIds = getSelcetData("fileId").toString(); 
	    $.ligerDialog.confirm("确认对附件进行删除吗？",'提示信息',function(rtn) {
	      if(rtn){
	        $.ajax({
	          url : __ctx + '/oa/system/sysFile/del.do?fileId='+fileIds+'&isDialog=1',
	          type : "post",
	          async : false,
	          success : function(data){//更新pool.data，并重新加载grid
	        	  var new_rows = [];
	         	  var old_rows = pool.data.Rows;
	         	  for(var i=0;i<old_rows.length;i++){
	         		  var id = old_rows[i].fileId;
	         		  if((","+fileIds+",").indexOf(","+id+",")>-1){
	         			  continue;
	         		  }
	         		 new_rows.push(old_rows[i]);
	         	  }
	         	  pool.data.Rows = new_rows;
	          	  pool.grid.reload(pool.data);
	          	  //刷新父窗口的
	          	  var files = [];
	          	  for(var j=0;j<new_rows.length;j++){
	          		files.push({id:new_rows[j].fileId,name:new_rows[j].filename});
	          	  }
	          	  var p_doc = window.parent.document;
	          	  var container = $(p_doc).find("textarea[name='"+fieldName+"']").parents('div[name=div_attachment_container]');
	          	  $(p_doc).find("textarea[name='"+fieldName+"']").val(JSON2.stringify(files));
	          	  AttachList.init(container);	          	  
	          }
	          ,error : function(){
	            $.ligerDialog.warn("删除文件失败！","提示信息");
	          } 
	        })
	      }
	    });
	     
	 } 
	/**
	* 下载文件(多个)
	*/
	function downloadFiles(){
		var fileIds = getSelcetData("fileId"); 
		var len = fileIds.length;
		if(len == 0){
		  $.ligerDialog.warn("请选择记录！");
		}else{
		  window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId='+fileIds.toString();
		}
	};
	 /**
	   * 下载文件(单个)
	   */
	  function downloadFile(){
		var fileId = getSelcetData("fileId"); 
	    if(fileId.length>1){
	      $.ligerDialog.warn("请选择一条记录下载！");
	    }else if(fileId.length==0){
	    	$.ligerDialog.warn("请选择一条记录下载！");
	    }else{
	    	 AttachMent.downloadFiles(fileId[0]);
	    }
	  };
</script>
</head>
	<body>
	<div class="panel">
		<div class="panel-top">
			<div class="panel-toolbar">
			   <div class="toolBar">
					<div class="group"><a class="link delFile" onclick="delAttach()">删除</a></div>
					<div class="group"><a class="link downLoad"  onclick="javascript:downloadFile()">下载</a></div>	
					<div class="group"><a class="link downloads" href="####" onclick="javascript:downloadFiles()">打包下载所有附件</a></div>		
				</div>	
			</div>
		</div>
		<div class="panel-body" style="">
				<div class="1-clear"></div>
				<div id="fileGrid" style="margin-top:0px"></div>
		</div><!-- end of panel-body -->
	</div> <!-- end of panel -->
		<textarea style="display: none;" id="operaterRight" name=operaterRight >${fn:escapeXml(fileGrid.operaterRight)}</textarea>
		<textarea style="display: none;" id="data" name="data" >${fn:escapeXml(fileGrid.data)}</textarea>
		<textarea style="display: none;" id="columns" name="columns" >${fn:escapeXml(fileGrid.columns)}</textarea>
	</body>
</html>