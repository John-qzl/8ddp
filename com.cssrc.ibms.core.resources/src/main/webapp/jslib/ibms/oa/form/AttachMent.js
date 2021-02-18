/**
 * 附件管理。
 * @returns {AttachMent}
 */
if (typeof AttachMent == 'undefined') {
	AttachMent = {};
}
AttachMent.addFileMerge = function(obj,callback){
	var attachementsObj = $(obj).parents('div[name=div_attachment_container]').find('div.attachement');
	var dataObj = $(obj).parents('div[name=div_attachment_container]').find('textarea');
	var oldFileData = $.parseJSON(dataObj.val());
	if($.isEmpty(oldFileData)) oldFileData= [];
	var operateRight = {upload: true, download: true, del: true};//新上传的文件有所有操作权限
	var newFileJson =[];
	UploadMergeDialog({isSingle:false,callback:function (result){
		var attachs = result.files;
		if(result.type=="local"){
			if(attachs==undefined || attachs==[]) return ;
			for(var i=0;i<attachs.length;i++){
				var fileId=attachs[i].fileId;
				var name=attachs[i].fileName;
				AttachMent.addJson(fileId,name,newFileJson);
			}	
		}else{
			var fileIds = attachs.fileIds;
			var fileNames = attachs.fileNames;
			if(fileIds==undefined || fileIds=="") return ;
			var aryFileId=fileIds.split(",");
			var aryName=fileNames.split(",");
		
			for(var i=0;i<aryFileId.length;i++){
				var name=aryName[i];
				if(!AttachMent.isFileExist(oldFileData,aryFileId[i])){
					AttachMent.addJson(aryFileId[i],name,newFileJson);
				}
			}
		}
		//dataObj.show();
		for(var i=0;i<newFileJson.length;i++){
				oldFileData.push(newFileJson[i]);
		}
		dataObj.val(JSON2.stringify(oldFileData));
		
		if(callback){//是否有回调
			var container = $(obj).parents('div[name=div_attachment_container]');
			callback(container);
		}else{
			var html=AttachMent.getHtml2(newFileJson,operateRight);
			attachementsObj.append($(html));
		}
		CustomForm.validate();
	}})
}
/**
 * 添加附件数据。
 * @param obj 按钮。
 * @param fieldName 字段名称
 */
AttachMent.addFile=function(obj,callback){
	var attachementsObj = $(obj).parents('div[name=div_attachment_container]').find('div.attachement');
	var dataObj = $(obj).parents('div[name=div_attachment_container]').find('textarea');
	var oldFileData = $.parseJSON(dataObj.val());
	if($.isEmpty(oldFileData)) oldFileData= [];
	var operateRight = {upload: true, download: true, del: true};//新上传的文件有所有操作权限
	var newFileJson =[];
	//文件选择器
	FlexUploadDialog({isSingle:false,callback:function (rtn){
		var attachs = rtn;
		var fileIds = attachs.fileIds;
		var fileNames = attachs.fileNames;
		if(fileIds==undefined || fileIds=="") return ;
		var aryFileId=fileIds.split(",");
		var aryName=fileNames.split(",");
	
		for(var i=0;i<aryFileId.length;i++){
			var name=aryName[i];
			if(!AttachMent.isFileExist(oldFileData,aryFileId[i])){
				AttachMent.addJson(aryFileId[i],name,newFileJson);
			}
		}
		//dataObj.show();
		for(var i=0;i<newFileJson.length;i++){
				oldFileData.push(newFileJson[i]);
		}
		dataObj.val(JSON2.stringify(oldFileData));
		
		if(callback){//是否有回调
			var container = $(obj).parents('div[name=div_attachment_container]');
			callback(container);
		}else{
			var html=AttachMent.getHtml2(newFileJson,operateRight);
			attachementsObj.append($(html));
		}
		CustomForm.validate();
	}});
};
//直接上传文件
AttachMent.directUpLoadFile=function(obj,callback){
	var attachementsObj = $(obj).parents('div[name=div_attachment_container]').find('div.attachement');
	var dataObj = $(obj).parents('div[name=div_attachment_container]').find('textarea');
	var oldFileData = $.parseJSON(dataObj.val());
	if($.isEmpty(oldFileData)) oldFileData= [];
	var operateRight = {upload: true, download: true, del: true};//新上传的文件有所有操作权限
	var newFileJson =[];
	AjaxUploadDialog({callback:function (curobj, attachs){		
		if(attachs==undefined || attachs==[]) return ;
		for(var i=0;i<attachs.length;i++){
			var fileId=attachs[i].fileId;
			var name=attachs[i].fileName;
			AttachMent.addJson(fileId,name,newFileJson);
		}	
		//dataObj.show();
		for(var i=0;i<newFileJson.length;i++){
			if(!AttachMent.isFileExist(oldFileData,newFileJson[i].id)){
				oldFileData.push(newFileJson[i]);
			}
		}
		dataObj.val(JSON2.stringify(oldFileData));
		if(callback){//是否有回调
			var container = $(obj).parents('div[name=div_attachment_container]');
			callback(container);
		}else{
			var html=AttachMent.getHtml2(newFileJson,operateRight);
			attachementsObj.append($(html));
			
			//附带文件名称
			var file = $("input[name='m:syfxsj:wjmc']");
			if (file.length > 0 ) {
				var fileName = $("a.attachment").text();
				if (fileName.length > 50) {
					fileName = fileName.substring(0, 20);
				}
				$(file).val(fileName.substring(0, fileName.lastIndexOf(".")));
			}
		}
		CustomForm.validate();
	}});
};

/**
 * 删除附件
 * @param obj 删除按钮。
 */
AttachMent.delFile=function(obj){
	var inputObj=$(obj);
	var parent=inputObj.parent();
	var divObj=parent.parent();
	var spanObj=$("span[name='attach']",parent);
	var divContainer=divObj.parent();
	var fileId=spanObj.attr("fileId");
	var aryJson=AttachMent.getFileJsonArray(divObj);
	AttachMent.delJson(fileId,aryJson);
	var json=JSON2.stringify(aryJson);
	var inputJsonObj=$("textarea",divContainer);
	if(aryJson.length == 0)
		json = "";		
	//设置json
	inputJsonObj.val(json);
	//删除span
	parent.remove();
	CustomForm.validate();
	
	//by weilei：重新加载文件数据
	AttachMent.reloadFileData(inputJsonObj[0].name, json);
	//客户端上传
	$('a[field="m:datafile:syyssj"]').show();
};
/**
 *  附件插入显示
 * @param {} div
 * @param {} jsonStr 
 * @param {} rights 权限 如果不传，默认是r
 */
AttachMent.insertHtml= function(div,jsonStr,rights){
	if($.isEmpty(jsonStr)) {
		div.empty();
		return ;
	}
	if($.isEmpty(rights)) rights ='r';
	var jsonObj=[];
	try {
		jsonStr = jsonStr.replaceAll("￥@@￥","\"");
		jsonObj =	jQuery.parseJSON(jsonStr);
	} catch (e) {
	}
	var html=AttachMent.getHtml(jsonObj,rights);
	div.empty();
	div.append($(html));
};

/**
 * 2017年8月29
 * 根据按钮操作权限控制
 * 附件插入显示
 * @param {} div
 * @param {} jsonStr 
 * @param {} rights Object {upload: true, download: true, del: false}
 */
AttachMent.insertHtml2= function(div,jsonStr,operateRight){
	if($.isEmpty(operateRight)) operateRight={upload: false, download: false, del: false};
	var jsonObj=[];
	try {
		jsonStr = jsonStr.replaceAll("￥@@￥","\"");
		jsonObj =	jQuery.parseJSON(jsonStr);
	} catch (e) {
	}
	var html=AttachMent.getHtml2(jsonObj,operateRight);
	div.empty();
	div.append($(html));
	//上传控制
	var canupload = operateRight.upload;
	if(!canupload){
		div.parent().find('a[operateright]').remove();
	}
};

/**
 * 获取文件的html。
 * @param aryJson
 * @returns {String}
 */
AttachMent.getHtml=function(aryJson,rights){	
	var str="";
	var template="";
	var templateW="<span class='attachement-span'><span fileId='#fileId#' name='attach' file='#file#' ><a class='attachment' target='_blank' path='#path#' onclick='AttachMent.handleClickItem(this)' title='#title#'>#name#</a></span><a href='javascript:;' onclick='AttachMent.download(this);' title='下载' class='link download'></a>&nbsp;<a href='javascript:;' onclick='AttachMent.delFile(this);' title='删除' class='link del'></a></span>";
	var templateR="<span class='attachement-span'><span fileId='#fileId#' name='attach' file='#file#' ><a class='attachment' target='_blank' path='#path#' onclick='AttachMent.handleClickItem(this)' title='#title#'>#name#</a></span><a href='javascript:;' onclick='AttachMent.download(this);' title='下载' class='link download'></a></span>";
	if(rights=="w"){
		template=templateW;
	}
	else{
		template=templateR;
	}
	for(var i=0;i<aryJson.length;i++){
		var obj=aryJson[i];
		var id=obj.id;
		var name=obj.name;
		var path =__ctx +"/oa/system/sysFile/file_" +obj.id+ ".do";
			
		var file=id +"," + name ;
		var tmp=template.replace("#file#",file).replace("#path#",path).replace("#name#", AttachMent.parseName(name)).replace("#title#",name).replace("#fileId#", id);
		//附件如果是图片就显示到后面
		str+=tmp;
	}
	return str;
};

/**
 *附件名显示 
 */
AttachMent.parseName = function(name){
	if(name.length >40)
		return name.substr(0,35)+"...";
	return name;
};

/**
 * 添加json。
 * @param fileId
 * @param name
 * @param path
 * @param aryJson
 */
AttachMent.addJson=function(fileId,name,aryJson){
	var rtn=AttachMent.isFileExist(aryJson,fileId);
	if(!rtn){
		var obj={id:fileId,name:name};
		aryJson.push(obj);
	}
};

/**
 * 删除json。
 * @param fileId 文件ID。
 * @param aryJson 文件的JSON。
 */
AttachMent.delJson=function(fileId,aryJson){
	for(var i=aryJson.length-1;i>=0;i--){
		var obj=aryJson[i];
		if(obj.id==fileId){
			aryJson.splice(i,1);
		}
	}
};

/**
 * 判断文件是否存在。
 * @param aryJson
 * @param fileId
 * @returns {Boolean}
 */
AttachMent.isFileExist=function(aryJson,fileId){
	for(var i=0;i<aryJson.length;i++){
		var obj=aryJson[i];
		if(obj.id==fileId){
			return true;
		}
	}
	return false;
};

/**
 * 取得文件json数组。
 * @param divObj
 * @returns {Array}
 */
AttachMent.getFileJsonArray=function(divObj){
	var aryJson=[];
	var arySpan=$("span[name='attach']",divObj);
	arySpan.each(function(i){
		var obj=$(this);
		var file=obj.attr("file");
		var aryFile=file.split(",");
		var obj={id:aryFile[0],name:aryFile[1]};
		aryJson.push(obj);
	});
	return aryJson;
};

/**
 * 点击附件事件处理
 * @param divObj
 * @returns {Array}
 */
AttachMent.handleClickItem = function(obj){

	var _this = $(obj);
	var span = _this.closest("span");
	var fileId = span.attr("fileId");
	
	var url =__ctx+"/oa/system/sysFile/getJson.do";
	var sysFile;
//	$.ajax({
//		url:url,
//		data:{
//			fileId:fileId
//		},
//		success:function(data){
//			
//			if(typeof(data)=="string"){
//				$.ligerDialog.error('系统超时请重新登录!','提示');
//				return ;
//			}
//			
//			if(data.status!=1){
//				$.ligerDialog.error(data.msg,'提示');
//			}else{
//				sysFile = data.sysFile;
//			
//				var path = _this.attr("path")+"?download=true";
				//var path =__ctx+"/oa/system/sysFile/file_"+fileId+".do?download=true";
				
				
				/*if(/(doc)|(docx)|(xls)|(xlsx)|(ppt)|(pptx)/ig.test(sysFile.ext)){
					if($.browser.msie){
						window.open(path,'_blank');	
					}else{
						var h=screen.availHeight-35;
						var w=screen.availWidth-5;
						var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
						var showUrl = __ctx+"/oa/form/office/get.do?fileId=" + fileId;
						window.open(showUrl,"myWindow",vars);
						alert(123);
					}*/
				//}else{
					
				var path = _this.attr("path")+"?download=true";
				var h=screen.availHeight-35;
					var w=screen.availWidth-5;
				    var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
					window.open(path,'_blank',vars);	
				//}
			}
//		}
//		
//		
//	});
//};

/**
 * 下载
 */		
AttachMent.download	= function(obj){
	
	var me = $(obj);
	var	span = me.siblings("span");
	if(span.length >0)
	var	fileId = span.attr("fileId");
	var h=screen.availHeight-35;
	var w=screen.availWidth-5;
	var vars="top=0,left=0,height="+h+",width="+w+",status=no,toolbar=no,menubar=no,location=no,resizable=1,scrollbars=1";
	var path =__ctx+"/oa/system/sysFile/file_"+fileId+".do?download=true";
	window.open(path,'_blank',vars);	
}

/**
 * 根据附件字段名获取附件Ids
 */
 AttachMent.getFileIdsByFieldName = function(fieldName){
	
    var fileIds = '';
	var fieldObj = $("textarea[name='"+fieldName+"']");
	if(fieldObj != '' && fieldObj != undefined && fieldObj != null && fieldObj != 'undefined'){
	    var fileDatas = fieldObj.val();
	    if(fileDatas != '' && fileDatas != undefined){
		    fileDatas = fileDatas.replaceAll('￥@@￥', '\"');
		    var fileArray = $.parseJSON(fileDatas);
		    for(var index=0; index<fileArray.length; index++){
		    	fileIds += fileArray[index].id + ',';
		    }
	    }
	}
	fileIds = fileIds==''?'-1':fileIds.substring(0, fileIds.length-1);
	return fileIds;
 };

 /**
  * 重新加载文件列表数据
  */
 AttachMent.reloadFileData = function(fieldName, json){
	 
	 var fileIds = '';
	 if(json != ''){
		 var fileArray = $.parseJSON(json);
		 for(var index=0; index<fileArray.length; index++){
	    	fileIds += fileArray[index].id + ',';
	     }
	 }
	 fileIds = fileIds==''?'-1':fileIds.substring(0, fileIds.length-1);
	 var fieldNameArray = fieldName.split(':');
	 var divFile = $('#divFile_'+fieldNameArray[fieldNameArray.length-1]);
	 //重新加载文件数据
	 if(divFile != null && divFile != undefined){
		 divFile.load(__ctx+"/oa/system/sysFile/list.do?fieldName="+fieldName+"&fileIds="+fileIds);
	 }
 };
 
 
 /**
  * 附件下载
  * by YangBo
  */
 AttachMent.downloadFiles=function(id){
	 var fileId=id;
		var callback = function(data){
			var dataObj = new com.ibms.form.ResultMessage(data);
			var filePath = dataObj.data.filePath;
			var fileType = dataObj.data.fileType;
			if(filePath == 'none'){
				$.ligerDialog.warn("文件不存在！","提示信息");
			}else{
				window.location.href = __ctx + '/oa/system/sysFile/download.do?fileId='+fileId;
			}
		};
		this.isExistFile(fileId, callback);
 };
 
 AttachMent.previewFile=function(fileId){
	 var callback = function(data){
			var dataObj = new com.ibms.form.ResultMessage(data);
			var filePath = dataObj.data.filePath;
			var fileType = dataObj.data.fileType;
			if(filePath == 'none'){
				$.ligerDialog.warn("文件不存在！","提示信息");
			}else{
				var title = '在线预览';
				var width = $(document).width()*.9;
				var height = $(document).height()*.9;
				var url = __ctx + "/oa/system/sysFile/preview.do?fileId="+fileId;
				if('doc,docx,ppt,pptx,xls,xlsx'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFileOfficePreview.jsp";
					$.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
				}else if('pdf'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFilePDFPreview.jsp";
					$.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
				}else if('jpg,png,gif,bmp'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFileImagePreview.jsp";
					$.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});	
				}else if('txt'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFileTextPreview.jsp";
					$.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
				}else if('mp4,webm,ogv'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFileVideoPreview.jsp";
					$.ligerDialog.open({title:title, url:url, height:height, width:width, isResize:true});
				}else if('mp3'.indexOf(fileType) != '-1'){
					url += "&url=oa/system/sysFileMusicPreview.jsp";
					$.ligerDialog.open({title:"音频播放", url:url, height:286, width:600, isResize:true});
				}else{
					$.ligerDialog.warn("暂不支持此类型的文件在线预览！","提示信息");
				}
			}
		};
		this.isExistFile(fileId, callback);
 };
 
 /**
  * 核实附件存不存在
  * by YangBo
  */
 AttachMent.isExistFile=function(fileId, callback){
	 $.ajax({
			url : __ctx + '/oa/system/sysFile/isExist.do?fileId='+fileId,
			type : "post",
			success : function(data){
				callback.call(this, data);
			}
			,error : function(){
				$.ligerDialog.warn("连接超时，请联系系统管理员！","提示信息");
			} 
		})
 };
 /**
  * 获取附件版本
  */
 AttachMent.getAttachHistory = function(fileId){
	 var url=__ctx + "/oa/form/dataTemplate/versionHistory.do?fileId="+fileId;
		url=url.getNewUrl();
		DialogUtil.open({
			height:300,
			width: 800,
			title : '附件版本',
			url: url, 
			isResize: true,
			sucCall:function(rtn){
				//写回调
			}
		});
 };
 /**
  * type ： main / rel / sub
  * attachmentContainer : 附件字段容器 权限信息以属性存储
  * right：字段数据权限
  * operateright：操作权限
  * 新增的数据不进行权限控制
  */
 AttachMent.rightEffect = function(type){
	 var selector = {main:"table.formTable [name=div_attachment_container],div.tableNew_con [name=div_attachment_container]",
			 sub:"div[type=subtable] [name=div_attachment_container]",
			 rel:"div[type=reltable] [name=div_attachment_container]"};
	 if(type=="all"){
		 $(selector["main"]).each(function(i,container){
			 AttachMent.containerEffect(container);
		 });
		 $(selector["sub"]).each(function(i,container){
			 AttachMent.containerEffect(container);
		 });
		 $(selector["rel"]).each(function(i,container){
			 AttachMent.containerEffect(container);
		 });
	 }else{
		 $(selector[type]).each(function(i,container){
			 AttachMent.containerEffect(container);
		 })
	 }
 }
 /**
  * container需满足以下结构
  */
AttachMent.containerEffect = function(container){
	 var me=$(container),
	 div = $("div.attachement",me),//用于存放数据的
	 right=me.attr("right"),//数据权限
	 operateRightStr = me.attr("operateright"),
	 operateRight;//操作权限（上传、删除、下载）
	 if(AttachMent.isSpecial()){
		 return;
	 }	
	 if($.isEmpty(operateRightStr)){	
		 //兼容老版本： 没有operateRightStr属性的，需进行特殊处理
		 //没有进行权限设置的
		// operateRight={upload: true, download: true, del: true};
		 var dataStr =$("textarea[controltype='attachment']",me).val();	
		 if(!$.isEmpty(dataStr)){
			dataStr = dataStr.replaceAll("￥@@￥","\"");
			$("textarea[controltype='attachment']",me).val(dataStr);
		 }
		 AttachMent.insertHtml(div,dataStr,right);
		 return;
	 }else{
		 operateRightStr = operateRightStr.replaceAll("￥@@￥","\"");
		 operateRight = $.parseJSON(operateRightStr);
	 }	
	 if(right!="w" && right!="r" && right!="b"){
		 me.remove();
		 return;
	 }	
	 //上传按钮的控制
	if(right=="r"||!operateRight.upload){
		$("a.link.selectFile",me).remove(); 
	}
	
	//获取数据
	var dataStr =$("textarea[controltype='attachment']",me).val();	
	if(!$.isEmpty(dataStr)){
		dataStr = dataStr.replaceAll("￥@@￥","\"");
		$("textarea[controltype='attachment']",me).val(dataStr);
	}
	try{
		var dataJson =	jQuery.parseJSON(dataStr);
		var displayType = me.attr("displaytype");
		//
		if(displayType=="list"){//展示方式-列表展示
			AttachList.init(me);
		}else{//展示方式-表单展示
			var html=AttachMent.getHtml2(dataJson,operateRight);
			div.empty();
			div.append($(html));
		}
	}catch(e){
		AttachMent.insertHtml(div,dataStr,right);
	}	
 };
 /**
  * 例外的情况，不进行处理
  */
 AttachMent.isSpecial = function(me){
	//表内编辑、弹框模式不进行处理
	 var isNewRowTemplate = $(me).parents('tr').attr('formtype')=="edit";
	 var isNewRowTemplate_window = $(me).parents('table').attr('formtype')=="window";	 
	 if(isNewRowTemplate||isNewRowTemplate_window){
		 return true;
	 }
 }
 /**
  * 获取文件的html。
  * @param aryJson
  * @returns {String}
  */
 AttachMent.getHtml2=function(aryJson,operateRight){	
 	var str="";
 	var html="";
 	var canDel = operateRight.del;
 	var canDownLoad = operateRight.download;
 
 	html += "<span class='attachement-span'>";
 	html += canDownLoad?"<span fileId='#fileId#' name='attach' file='#file#' ><a class='attachment' target='_blank' path='#path#' onclick='AttachMent.handleClickItem(this)' title='#title#'>#name#</a></span>"
 			:"<span  name='attach'>#name#</a></span>";
 	html += canDownLoad?"<a href='javascript:;' onclick='AttachMent.download(this);' title='下载' class='link download'></a>&nbsp;":"";
 	html += canDel?"<a href='javascript:;' onclick='AttachMent.delFile(this);' title='删除' class='link del'></a>":"";
 	html += 	"</span><br/>";
 	
 	for(var i=0;i<aryJson.length;i++){
 		var obj=aryJson[i];
 		var id=obj.id;
 		var name=obj.name;
 		var path =__ctx +"/oa/system/sysFile/file_" +obj.id+ ".do";
 			
 		var file=id +"," + name ;
 		var tmp= html.replace("#file#",file).replace("#path#",path).replace("#name#", AttachMent.parseName(name)).replace("#title#",name).replace("#fileId#", id);
 		//附件如果是图片就显示到后面
 		str+=tmp;
 	}
 	
 	return str;
 };
 
 //客户端文件上传
 AttachMent.clientUpLoadFile = function() {
	 $.ajax({
		async : true,
        cache : false,
        dataType : "json",
        url: __ctx + "/test/data/clientUploadFile.do",
        success:function(data){
        	var exePath = data.exePath;
        	//alert(exePath);
        	try {
        		var o = new ActiveXObject("wscript.shell");
            	o.run(exePath);
            	o = null;
        	} catch (e) {
        		alert("找不到指定路径："+exePath);
        	}
        	
        },
        error:function(){
            $.ligerDialog.error('客户端启动失败！');
        }
	 })
 };