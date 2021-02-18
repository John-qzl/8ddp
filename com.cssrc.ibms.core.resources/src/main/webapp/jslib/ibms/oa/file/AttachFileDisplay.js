//附件列表展示
if (typeof AttachList == 'undefined') {
	AttachList = {};
}
/**
 * div_attachment_container元素调整
 */
AttachList.init = function(container){
	var right = AttachList.getData('right',container);
	var operateright = AttachList.getData('operateright',container);
	var files = AttachList.getData('file',container);
	var fileNum = files==null?0:files.length;
	var hasUploadButton = $("a.link.selectFile",container).length>0
	switch(fileNum){
	case 0:
		$('div.attachement',container).empty().append("<span class='attachement-num'>（0）</span>");
		break;
	case 1:
		var span = AttachList.getHtml(files,operateright);
		$('div.attachement',container).empty().append($(span));
		break;
	default :
		var span = AttachList.getHtml([files[0]],operateright);
		var moreButton = '<span><a class="link morefile" onclick="{AttachList.showMore(this);}" href="javascript:void(0);">更多...</a></span>';
		$('div.attachement',container).empty()
		.append($(span))
		.append('<span class="attachement-num">（'+fileNum+'）</span>')
		.append(moreButton);
		break;
	}
	//为上传按钮传入回调函数
	if(hasUploadButton){
		var old = $('a.link.selectFile',container).attr('onclick');
		if(old.indexOf('AttachMent')>0&&old.indexOf('afterUpload')<0){
			var newClick = "";
			newClick = old.replace('this)','this,AttachList.afterUpload)');
			$('a.link.selectFile',container).attr('onclick',newClick);
		}
		//选择文件和文件操作移到一行
		$('div.attachement',container).attr("style","display:inline-block");
		$('a.selectFile',container).attr("style","display:inline-block");
	}
}


/**
 * 更多，文件列表的入口
 */
AttachList.showMore = function(obj){
	var containnerDiv = $(obj).parents('div[name=div_attachment_container]');
	var fieldName = containnerDiv.find('textarea[controltype=attachment]').attr("name");
	var obj = {
			operaterRight : AttachList.getData('operateright',containnerDiv),
			fileIds		  :AttachList.getData('fileIds',containnerDiv),
			fieldName:fieldName
	}
	var url = __ctx+"/oa/form/dataTemplate/fileGrid.do?fileContainer="+JSON2.stringify(obj);	
	var height = (AttachList.getData('fileNum',containnerDiv)+1)*30 +150;
	$.ligerDialog.open({
		width: "800",
		height : height,
		url: url,
	    title: "文件列表展示"
	});
}
/**
 * 上传文件-后置事件
 */
AttachList.afterUpload = function(obj){
	AttachList.init(obj);
}
/**
 * type :{right:容器权限,operateright：按钮操作权限,file:文件数据}
 */
AttachList.getData = function(type,container){
	if(!type) return null;
	var data;
	switch(type){
	case 'right':
		data = container.attr('right');
		break;
	case 'operateright':
		data = $.parseJSON(container.attr('operateright').replaceAll("￥@@￥","\""));
		break;
	case 'file':
		var file = $(container).find('textarea[controltype=attachment]').val().replaceAll("￥@@￥","\"");
		data = $.parseJSON(file);
		break;
	case 'fileIds':
		var file = $(container).find('textarea[controltype=attachment]').val().replaceAll("￥@@￥","\"");
		var fileObj = $.parseJSON(file);
		data = "";
		$(fileObj).each(function(){
			data += $(this).attr("id")+",";
		})
		data.endWith(",")?data=data.substring(0,data.length-1):"";
		break;
	case 'fileNum':
		var file = $(container).find('textarea[controltype=attachment]').val().replaceAll("￥@@￥","\"");
		var fileObj = $.parseJSON(file);
		fileObj = $.isEmpty(fileObj)?[]:fileObj;
		data = fileObj.length;
		break;
	}
	return data;
};
/**
 * 获取文件的html。
 * @param aryJson
 * @returns {String}
 */
AttachList.getHtml=function(aryJson,operateRight){	
	var str="";
	var html="";
	var canDel = operateRight.del;
	var canDownLoad = operateRight.download;

	html += "<span class='attachement-span'>";
	html += canDownLoad?"<span fileId='#fileId#' name='attach' file='#file#' ><a class='attachment' target='_blank' path='#path#' onclick='AttachMent.handleClickItem(this)' title='#title#'>#name#</a></span>"
			:"<span  name='attach'>#name#</a></span>";
	html += canDownLoad?"<a href='javascript:void(0);' onclick='AttachMent.download(this);' title='下载' class='link download'></a>&nbsp;":"";
	html += canDel?"<a href='javascript:;' onclick='AttachList.delFile(this);' title='删除' class='link del'></a>":"";
	html += 	"</span>";
	
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
AttachList.delFile = function(obj){
	var containnerDiv = $(obj).parents('div[name=div_attachment_container]');
	var textareaObj = $(containnerDiv).find('textarea[controltype=attachment]');
	var textareaVal = $(containnerDiv).find('textarea[controltype=attachment]').val().replaceAll("￥@@￥","\"");
	var textareaJson = $.parseJSON(textareaVal);
	var fileid = $(obj).parent().find('span[fileid]').attr('fileid');
	//移除隐藏域的id
	for(var i=0;i<textareaJson.length;i++){
		if(textareaJson[i].id==fileid){
			textareaJson.splice(i,1);
			break;
		}
	}
	textareaObj.val(JSON2.stringify(textareaJson));
	//移除显示值
	$(obj).parent().remove();
	//容器重新加载
	AttachList.init(containnerDiv);
};