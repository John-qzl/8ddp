<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<%
	String filenum = request.getParameter("filenum");
	String type = request.getParameter("type");
%>

<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>附件上传</title>
<f:link href="from-jsp.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/fileupload/ajaxUpload/ajaxupload.js"></script>
<script type="text/javascript">
	//文件上传组件
	var fileUploader;
	//文件上传集合
	var uploadArray = new Array();
	//文件上传结果集合
	var returnArray = new Array();
	//文件上传个数（累计值）
	var completeNum = 0;
	//获取父窗体传入参数
	var pArgs;
	//文件组件初始化
	$(initUploader);
	
	/**
	 * 初始化文件组件
	 */
	function initUploader(){
		//获取父窗体传入参数
		pArgs = window.dialogArguments;
		fileUploader = new AjaxUpload('addFile', {
			action : '${ctx}/oa/system/sysFile/fileUpload.do', 
			autoSubmit : false, //是否自动提交
			data : {
				fileIndex : ''
			},
			onChange : function(file, extension){
				completeNum = 0;
				if(file.length > 50){
					$.ligerDialog.warn("文件名过长，请重新选择！", "提示信息");
					return;
				}
				uploadArray[uploadArray.length] = fileUploader.getUploadResult();
				addFile(file);
			},
			onComplete : function(file, response){
				$.ligerDialog.closeWaitting(); 
				callback(file,response);
			}
		});
	};
	
	/**
	 * 添加上传文件记录
	 */
	function addFile(file){
		var fileTable = document.getElementById("fileTable");
		var files=fileTable.rows.length;
		if(files><%=filenum%>){
			$.ligerDialog.warn("您只能选择"+<%=filenum%>+"个文件上传","提示信息");
			return;
		}
		var fileTable = document.getElementById("fileTable");
		
		var currRowNum = fileTable.rows.length
		var fileTr,fileTd;
		
		fileTr = fileTable.insertRow(currRowNum);
		fileTr.setAttribute("align", "center");
		fileTr.setAttribute("align", "center");
		
		fileTd = fileTr.insertCell(0); //序号
		fileTd.innerHTML=currRowNum;
		
		fileTd = fileTr.insertCell(1); //上传状态
		fileTd.innerHTML='等待';
		
		fileTd = fileTr.insertCell(2); //文件名
		fileTd.innerHTML=file;
		
		fileTd = fileTr.insertCell(3); //删除
		fileTd.innerHTML='<a href="javascript:;" onclick="javascript:deleteFile(this);" id="deleteImg_'+currRowNum+'"><img src="${ctx}/styles/images/cancel.png" border="0"></a>';
	
		//检查当前插入文件名是否存在
		isExistFile(fileTr);
	};
	
	/**
	 * 检查当前添加文件是否存在同名
	 */
	function isExistFile(fileTr){
		
		var fileId = fileTr.childNodes[0].innerHTML;
		var fileName = fileTr.childNodes[2].innerHTML;
		
		var fileTable = document.getElementById("fileTable");
		var fileTrs = fileTable.getElementsByTagName("tr");
		
		for(var i=1;i<fileTrs.length;i++){
			var id = fileTrs[i].childNodes[0].innerHTML;
			var name = fileTrs[i].childNodes[2].innerHTML;
			if(name==fileName && id!=fileId){
				$.ligerDialog.warn("列表中存在同名文件！", "消息提示");
				fileTable.deleteRow(fileTr.rowIndex);
				//删除文件集合
				uploadArray.splice(-1, 1, null);
				//去掉空值
				var copyArray = new Array();
				copyArray = uploadArray.slice(0);
				//重新更新文件集合顺序
				uploadArray.splice(0, uploadArray.length);
				for(var index=0; index<copyArray.length; index++){
					var array = copyArray[index];
					if(array != null){
						uploadArray.push(array);
					}
				}
				return;
			}
		}
	};
	
	/**
	 * 删除文件上传列表
	 */
	function deleteFile(obj){
		
		var delIndex = obj.id.split('_')[1];
		var fileTable = document.getElementById("fileTable");
		//如果当前文件已上传，不可以删除
		var status = fileTable.rows[delIndex].childNodes[1].innerHTML;
		if(status == '完成'){
			$.ligerDialog.warn("文件已上传成功，不可删除！", "提示信息");
			return;
		}
		fileTable.deleteRow(delIndex);
		//删除文件集合
		uploadArray.splice(delIndex-1, 1, null);
		
		var copyArray = new Array();
		copyArray = uploadArray.slice(0);
		var nextId = 0;
		//重新排列序号，如果没有序号，这一步省略
	    for(var index=delIndex; index<fileTable.rows.length; index++){
	        //序号列
	        fileTable.rows[index].cells[0].innerHTML = index;
	        nextId = parseInt(index)+1;
	        //删除列
	        var deleteImg = document.getElementById("deleteImg_"+nextId);
	        deleteImg.id = "deleteImg_"+index;
	    }
		
		//重新更新文件集合顺序
		uploadArray.splice(0, uploadArray.length);
		for(var index=0; index<copyArray.length; index++){
			var array = copyArray[index];
			if(array != null){
				uploadArray.push(array);
			}
		}
	};
	
	/**
	 * 文件上传
	 */
	function uploadFile(){
		
		if(uploadArray.length > 0){
			//获取需要上传的文件列表
			$.ligerDialog.waitting("正在上传中，请稍后...", "提示信息");
			var fileDatas = getFileDatas();
			for(var index=0; index<uploadArray.length; index++){
				var fileData = fileDatas[index];
				fileData.childNodes[1].innerHTML="上传中"
				var attributes = new Array();
				var fileIndex = fileData.childNodes[0].innerHTML;
				attributes[0] = fileIndex;
				fileUploader.submitUploadForm(uploadArray[index][0], uploadArray[index][1], attributes);
			}
		} else {
			$.ligerDialog.warn("请先添加文件！", "提示信息");
		}
	};
	
	/**
	 * 获取待上传的文件数据
	 */
	function getFileDatas(){
		
		var fileData = new Array();
		var fileTable = document.getElementById("fileTable");
		var fileTrs = fileTable.getElementsByTagName("tr");
		var rows = 0;
		for(var index=1; index<fileTrs.length; index++){
			if(fileTrs[index].childNodes[1].innerHTML == "等待"){
				fileData[rows] = fileTrs[index];
				rows++;
			}
		}
		return fileData;
	};
		
	/**
	 * 文件上传完成后，回调函数
	 */
	function callback(fileItem,response){
		
		var fileTable = document.getElementById("fileTable");
		var fileTrs = fileTable.getElementsByTagName("tr");
		var responseText = $.parseJSON(response);
		var fileIndex = responseText.fileIndex;
		var fileName = responseText.fileName;
		var fileId = responseText.fileId;
		if(responseText.success){
			fileTrs[fileIndex].childNodes[1].innerHTML = "完成";
			//存储返回结果
			returnArray.push(responseText);
			completeNum++;
			if(completeNum == uploadArray.length){
				$.ligerDialog.success("文件上传成功！");
				//清空当前上传数据
				uploadArray.splice(0, uploadArray.length);
			}
		} else if(!response.flag){
			$.ligerDialog.warn("["+fileName+"]上传文件失败，请联系系统管理员！", "提示信息");
			fileTrs[fileIndex].childNodes[1].innerHTML = '<span style="color: red;">错误</span>';
		}
	};

	/**
	 * 关闭窗体返回值
	 */
	function closeWindow(){
		if(pArgs != null){ //ModelDialog
			window.returnValue = returnArray;
		}else{             //Dialog
			window.opener.returnValue.call(returnArray);
		}
		window.close();
	};
	
</script>
</head>


<body>
<!-- 文件上传：操作按钮 -->
<div class="panel-toolbar" style="height:60px">
	<div class="group"><a class="link add" id="addFile" href="javascript:;" >添加</a></div>
	
	<div class="group"><a class="link upload" href="javascript:;" onclick="javascript:uploadFile()">上传</a></div>
	
	<div class="group"><a class="link ok" href="javascript:;" onclick="javascript:closeWindow()">确定</a></div>
	
	<div class="group"><a class="link close" href="javascript:;" onclick="javascript:window.close();">取消</a></div>
</div>

<!-- 文件上传：文件列表 -->
<div class="panel-body">
	<table id="fileTable" width="100%" cellpadding="1" cellspacing="1" class="table-grid">
		<tr align="center" class="table-grid-th">
			<td nowrap style="width:30px;height:10px;padding:2px 0px 4px 5px;">序号</td>
			<td nowrap style="width:50px;height:10px;padding:2px 0px 4px 5px;">上传状态</td>
			<td style="height:10px;padding:2px 0px 4px 5px;"></td>
			<td nowrap style="width:30px;height:10px;padding:2px 0px 4px 5px;">删除</td>
		</tr>
	</table>
</div>
		
		
</body>

</html>