/**
 * 关闭窗口
 * @returns {unresolved}
 */
function CloseWindow(action,preFun,afterFun) {
	if(preFun){
		preFun.call(this);
	}
	if (frameElement) {
		dialog = frameElement.dialog;
		dialog.get("sucCall")('ok');
		dialog.close();
	}
    else window.close();
    if(afterFun){
		afterFun.call(this);
	}
}
/**
 * 打开窗口
 * @param config
 *         url 为打开窗口的内部地址
 *         title:窗口标题
 *         height:高
 *         width:宽
 *         onload：加载事件
 *         ondestory:关闭的事件
 */
function _OpenWindow(config){
	top.$.ligerDialog.open({
        height:config.height,
        width: config.width,
        title : config.title,
        url: config.url, 
        isResize: true,
        sucCall:function(rtn){
        	if(config.callback){
        		config.callback.call();
        	}else if(rtn){
        		location.href=location.href;
        	}
        	
        }
    });
	

}

/**
 * 获得Grid中的选择中的所有主键ID
 * @param gridId
 * @returns {Array}
 */
function _GetGridIds(gridId){
	var grid=mini.get('#'+gridId);
	 var rows = grid.getSelecteds();
	 var ids = [];
	 if (rows.length > 0) {
	     for (var i = 0, l = rows.length; i < l; i++) {
	         var r = rows[i];
	         ids.push(r.pkId);
	     }
	 }
	 return ids;
}

/**
 * 提交JSON信息
 * @param config
 *         url：必需，handle the url
 *         method:'POST' or 'GET' ,default is 'GET'
 *         data: data of the json ,such as {'field1':1,'feld2':2}
 *         success:成功返回时调用的函数，格式如：function(result){alert(result);}    
 */
function _SubmitJson(config){
	
	if(!config) return;
	if(!config.url) return;
	if(!config.method)config.method='GET';
	if(!config.data) config.data={};
	
	if(!config.submitTips){
		config.submitTips='正在处理...';
	}

	var msgId=null;
	//显示提交数据的进度条
	if(typeof(config.showProcessTips)=='undefined'){
		config.showProcessTips=true;
	}
	
	if(config.showProcessTips){
		msgId=mini.loading(config.submitTips, "操作信息");
	}
	var showMsg=(config.showMsg==false)?false:true;
	$.ajax({
        url: config.url,
        type: config.method,
        data: config.data,
        cache: false,
        success: function(result,status, xhr) {
        	var valid=xhr.getResponseHeader("valid");
        	if(config.showProcessTips  && msgId!=null){
        		mini.get(msgId).hide();
        	}
        	if(valid!='true'){
        		mini.Cookie.set('enabled',false);
        	}
        	if(result.success){
        		if(config.success){
            		config.success.call(this,result);
            	}
        		if(showMsg){
	        		var msg=(result.message!=null)?result.message:'成功执行!';
	        		//显示操作信息
	        		/*top['index']._ShowTips({
	            		msg:msg
	            	});*/
	        		
	        		top.$.ligerDialog.success(msg);
        		}
        	}else{
        		if(showMsg){
	        		//显示出错信息
        			
        			top.$.ligerDialog.error( result.message,"出错了");
	        		/*top['index']._ShowErr({
	            		content:result.message
	            	});	*/
        		}
        	}
        },
        error: function(jqXHR) {
        	if(config.showProcessTips && msgId!=null){
        		mini.get(msgId).hide();
        	}
        	if(jqXHR.responseText!='' && jqXHR.responseText!=null){
        		top.$.ligerDialog.error( jqXHR.responseText,"出错了");
	        	/*top['index']._ShowErr({
	        		content:jqXHR.responseText
	        	});*/
        	}
        }
    });
}

/**
 * 操作完成后，展示操作提示
 * @param config
 */
function _ShowTips(config){

	var x,y,width,height;
	if(config==null){
		config={};
	}
	//title=config.title?config.title:'操作提示';
	msg=(config.msg!='' && config.msg!=undefined)?config.msg:'成功操作！';
	x=config.x?config.x:'center';
	y=config.y?config.y:'top';
	width=config.width?config.width:450;
	height=config.height?config.height:100;

	var en=getCookie('enabled');
    if(en=='false'){
		config.msg=config.msg+'<br/>'+__status_tips;
    }
	top.mini.showTips({
		width:width,
		height:height,
        content: "<div style='width:100%;height:"+(height-40)+"px;color:red;overflow:auto'>"+config.msg+"</div>",
        state: "success",
        x: x,
        y: y,
        timeout: 5000
    });
}

/**
 * 展示出错信息
 * @param config
 */
function _ShowErr(config){
	
	var x,y,width,height;
	x=config.x?config.x:'center';
	y=config.y?config.y:'top';
	width=config.width?config.width:450;
	height=config.height?config.height:100;
	
	 mini.showTips({
		 width:width,
		 height:height,
         content: "<div style='width:100%;height:"+(height-40)+"px;color:red;overflow:auto'>"+config.content+"</div>",
         state: "warning",
         x: x,
         y: y,
         timeout: 5000
     });
	
}

/**
 * config的参数有：
 * types:Document,Icon,Image,Zip,Vedio
 * from:APP,
 * callback:function(files){
 * 	fileId,path
 * }
 * 
 */
function _FileUploadDlg(config) {
    var url = __ctx+"/oa/system/sysFile/dialog.do";
    
    _OpenWindow({
        url: url,
        title: "上传文件", width: 600, height: 420,
        onload: function() {
        },
        ondestroy: function(action) {
            if (action == 'ok') {
                var iframe = this.getIFrameEl();
                var files = iframe.contentWindow.getFiles();
                if (config.callback) {
                    config.callback.call(this, files);
                }
            }
        }
    });
}



/**
 * 显示图片对框架
 * 
 * @param config
 * {
 * 	  from:来自个人的上传图片（值为：SELF），来自应用程序(值为：APPLICATION)
 *    single:单选择(true),复选(false)
 *    callback:回调函数，当选择ok，则可以通过回调函数的参数获得选择的图片
 * }
 */
function _ImageDlg(config){
	if(!config.width) config.width=620;
	
	if(!config.height) config.height=450;
	
	mini.open({
        allowResize: true, //允许尺寸调节
        allowDrag: true, //允许拖拽位置
        showCloseButton: true, //显示关闭按钮
        showMaxButton: true, //显示最大化按钮
        showModal: true,
        //from=SELF代表来自个人的图片，single代表只允许上传一张
        url: __ctx+"/oa/system/sysFile/appImages.do?from="+config.from+"&single="+config.single,
        title: "选择图片", 
        width: config.width, 
        height: config.height,
        onload: function() {
			
        },
        ondestroy: function(action) {
            if (action != 'ok') return;
            var iframe = this.getIFrameEl();
            var imgs = iframe.contentWindow.getImages();
            if(config && config.callback){
                config.callback.call(this,imgs);
            }
        }
    });	
}

/**
 * 预览原图
 * @param fileId
 */
function _ImageViewDlg(fileId){
	var win=mini.open({
        allowResize: true, //允许尺寸调节
        allowDrag: true, //允许拖拽位置
        showCloseButton: true, //显示关闭按钮
        showMaxButton: true, //显示最大化按钮
        showModal: true,
        //只允许上传图片，具体的图片格式配置在config/fileTypeConfig.xml
        url: __ctx+"/oa/system/sysFile/imgPreview.do?fileId="+fileId,
        title: "图片预览", width: 600, height: 420,
        onload: function() {
        },
        ondestroy: function(action) {
        }
    });
	win.max();
}

/**
 * 显示个人的对话框
 * @param single 是否为单选图片
 * @param callback 回调函数
 */
function _UserImageDlg(single,callback){
	var config={
		single:single,
		callback:callback,
		from:'SELF'
	};
	_ImageDlg(config);
}

/**
 * 显示应用级别的图片对话框
 * @param single
 * @param callback
 */
function _AppImageDlg(single,callback){
	var config={
		single:single,
		callback:callback,
		from:'APPLICATION'
	};
	_ImageDlg(config);
}
/**
 * 获得业务表单的JSON值
 * @param String
 */
function _GetFormJson(formId){
	var modelJson={};
	
	var formData=$("#"+formId).serializeArray();
	for(var i=0;i<formData.length;i++){
		modelJson[formData[i].name]=formData[i].value;
	}
	
	var extJson=_GetExtJsons(formId);
	
	$.extend(modelJson,extJson);
	
	return modelJson;
}

function _GetExtJsons(formId){
	var form=$("#"+formId);
	var modelJson={};
	//获得自定义表格的数值
	form.find('.grid-table').each(function(){
		var name=$(this).attr('name');
		var grid=mini.get(name);
		modelJson[name]=grid.getData();
	});
	
	form.find('.mini-textboxlist').each(function(){
		var cname=$(this).find('tbody>tr>td').children('input[type="hidden"]').attr('name');
		var ctext=[];
		var list=$(this).find('tbody>tr>td>ul').children('.mini-textboxlist-item');
		list.each(function(){
			ctext.add($(this).text());
		});
		modelJson[cname+'_name']=ctext.join(',');
	});
	
	form.find('.mini-radiobuttonlist').each(function(){
		var cname=$(this).find('tbody>tr>td').children('input[type="hidden"]').attr('name');
		var ctext=$(this).find('tbody>tr>td').find('.mini-radiobuttonlist-item-selected').find("label").text();
		modelJson[cname+'_name']=ctext;
	});
	
	form.find('.mini-checkboxlist').each(function(){
		var cname=$(this).find('tbody>tr>td').children('input[type="hidden"]').attr('name');
		var ctext=[];
		var list=$(this).find('tbody>tr>td>div').find('.mini-checkboxlist-item-selected');
		list.each(function(){
			ctext.add($(this).find("label").text());
		});
		modelJson[cname+'_name']=ctext.join(',');
	});
	
	form.find('.mini-combobox').each(function(){
		var cname=$(this).children('input[type="hidden"]').attr('name');
		if(cname!=''){
			var cbbObj=mini.getbyName(cname);
			if(cbbObj && cbbObj.getText){
				var ctext=cbbObj.getText();
				modelJson[cname+'_name']=ctext;
			}
		}
	});
	
	form.find('.mini-treeselect').each(function(){
		var cname=$(this).children('input[type="hidden"]').attr('name');
		if(cname!=''){
			var tsObj=mini.getbyName(cname);
			if(tsObj && tsObj.getText){
				var ctext=tsObj.getText();
				modelJson[cname+'_name']=ctext;
			}
		}
	});
	
	form.find('.mini-buttonedit').each(function(){
		var cname=$(this).children('input[type="hidden"]').attr('name');
		if(cname!=''){
			var tsObj=mini.getbyName(cname);
			if(tsObj && tsObj.getText){
				var ctext=tsObj.getText();
				modelJson[cname+'_name']=ctext;
			}
		}
	});
	
	return modelJson;
}

/**
 * 获得表单的所有控件值串，格式为[{name:'a',value:'a'},{name:'b',value:'b'}],并且对value值的特殊符号包括中文进行编码
 * @param formId 表单ID
 * @returns
 */
function _GetFormParams(formId){
	return $("#"+formId).serializeArray();
}

/**
 * 加载用户信息，使用的时候，是需要在页面中带有以下标签，
 * <a class="mini-user" userId="11111"></a>
 * 其则会根据userId来加载处理成fullname
 * @param editable 是否可编辑
 */
function _LoadUserInfo(editable){
	var uIds=[];
	$('.mini-user').each(function(){
		var uId=$(this).attr('userId');
		if(uId){
			uIds.push(uId);
		}
	});
	if(uIds.length>0){
		$.ajax({
            url:__ctx+ '/pub/org/user/getUserJsons.do',
            data:{
            	userIds:uIds.join(',')
            },
            success: function (jsons) {
                for(var i=0;i<jsons.length;i++){
                	if(editable){
                		$("a.mini-user[userId='"+jsons[i].userId+"']").attr('href','javascript:void(0)').attr('onclick','_ShowUserEditor(\''+jsons[i].userId+'\')').html(jsons[i].fullname);
                	}else{
                		var sex=jsons[i].sex;
                		var html='';
/*                		if(sex=='Male'){
		            		html='<img src="'+__ctx+'/styles/images/male.png" alt="男性">&nbsp;';
		            	}else{
		            		html='<img src="'+__ctx+'/styles/images/female.png" alt="女性">&nbsp;';
		            	}*/
                		$("a.mini-user[userId='"+jsons[i].userId+"']").html(html+jsons[i].fullname);
                	}
                	
                }
            }
        });
	}
}

function _ShowUserEditor(userId){
	_OpenWindow({
		title:'编辑用户信息',
		url:__ctx+'/sys/org/osUser/edit.do?pkId='+userId,
		height:450,
		width:780,
		onload:function(){
			
		}
	});
}

/**
 * 显示我的文件上传对话框
 * @param config
 * 		  showMgr:true 是否显示管理界面
 *        from：SELF,APPLICATION
 *        types： Image,Document,Zip,Vedio
 *        single： true,false单选或多选
 *        callback： 回调函数
 */
function _UploadFileDlg(config){
	if(config.showMgr){
		_OpenWindow({
			title:'我的附件管理器',
			height:500,
			width:820,
			url:__ctx+'/oa/system/sysFile/myMgr.do?dialog=true&single='+config.single,
			ondestroy:function(action){
				if (action != 'ok') return;
	            var iframe = this.getIFrameEl();
	            var files = iframe.contentWindow.getFiles();
	            if(config.callback){
	                config.callback.call(this,files);
	            }
			}
		});
	}else{
		_UploadDialogShowFile({
			onlyOne:config.onlyOne,
			from:config.from,
			types:config.types,
			callback:config.callback,
			single:config.single
		});
	}
}

/**
 * 流程解决方案对话框
 * @param single
 * @param callback
 */
function _BpmSolutionDialog(single,callback){
	_OpenWindow({
		url:__ctx+'/bpm/core/bpmSolution/dialog.do?single='+single,
		title:'流程解决方案选择',
		height:600,
		width:800,
		iconCls:'icon-flow',
		ondestroy:function(action){
			if(action!='ok')return;
			var iframe = this.getIFrameEl();
            var sols = iframe.contentWindow.getSolutions();
            if(callback){
            	callback.call(this,sols);
            }
		}
	});
}

/**
 * 上传对话框
 * @param config
 *       from:SELF,APPLICATION
 *       types:Image,Document,Zip,Vedio
 *       callback
 */
function _UploadDialog(config){
 	_OpenWindow({
        //只允许上传图片，具体的图片格式配置在config/fileTypeConfig.xml
        url: __ctx+"/oa/system/sysFile/dialog.do?from="+config.from+"&types="+config.types,
        title: "文件上传", 
        width: 600,
        height: 420,
        onload: function() {
			
        },
        ondestroy: function(action) {
            if (action != 'ok') return;
            var iframe = this.getIFrameEl();
            var files = iframe.contentWindow.getFiles();
            if(config.callback){
                config.callback.call(this,files);
            }
        }
    });
}

/**
 * 上传对话框
 * @param config
 *       from:SELF,APPLICATION
 *       types:Image,Document,Zip,Vedio
 *       callback
 */
function _UploadDialogShowFile(config){
 	_OpenWindow({
        //只允许上传图片，具体的图片格式配置在config/fileTypeConfig.xml
        url: __ctx+"/oa/system/sysFile/dialog.do?from="+config.from+"&types="+config.types+"&onlyOne="+config.onlyOne,
        title: "附件上传", 
        width: 600,
        height: 420,
        onload: function() {
			
        },
        ondestroy: function(action) {
            if (action != 'ok') return;
            var iframe = this.getIFrameEl();
            var files = iframe.contentWindow.getFiles();
            if(config.callback){
                config.callback.call(this,files);
            }
        }
    });
}

/**
 * 加载用户组信息，使用的时候，是需要在页面中带有以下标签，
 * <a class="mini-group" groupId="11111"></a>
 * 其则会根据groupId来加载处理成name
 */
function _LoadGroupInfo(){
	var uIds=[];
	$('.mini-group').each(function(){
		var uId=$(this).attr('groupId');
		if(uId){
			uIds.push(uId);
		}
	});
	if(uIds.length>0){
		$.ajax({
            url:__ctx+ '/pub/org/group/getGroupJsons.do',
            data:{
            	groupIds:uIds.join(',')
            },
            success: function (jsons) {
                for(var i=0;i<jsons.length;i++){
                	$("a.mini-group[groupId='"+jsons[i].groupId+"']").html(jsons[i].name);
                }
            }
        });
	}
}

/**
 * 获得表格的行的主键Id列表，并且用',’分割
 * @param rows
 * @returns
 */
function _GetIds(rows){
	var ids=[];
	for(var i=0;i<rows.length;i++){
		ids.push(rows[i].pkId);
	}
	return ids.join(',');
}
/*
_ModuleFlowWin({
		title:'供应商入库申请',
		modleKey:'CRM_PROVIDER',
		failCall:add,
		success:function(action){
			
		}
	});
*/
/**
 * 用于用户自已经的模块中使用，点击弹出添加流程审批业务功能
 */
function _ModuleFlowWin(conf){
	var width=conf.width?conf.width:780;
	var height=conf.height?conf.height:480;
	$.ajax({
        url:__ctx+ '/bpm/integrate/bpmModuleBind/getSolByModuleKey.do?moduleKey='+conf.moduleKey,
        success: function (json) {
            if(!json.success){
            	if(conf.failCall){
            		conf.failCall.call(this);
            	}else{
            		alert('流程业务模块没有绑定的流程解决方案：'+conf.moduleKey);
            	}
            }
            _OpenWindow({
            	url:__ctx+'/bpm/core/bpmInst/start.do?solId='+json.data.solId,
            	title:conf.title,
            	width:width,
            	height:height,
            	ondestroy:function(action){
            		if(action=='ok' && conf.success){
            			conf.success.call(this);
            		}
            	}
            });
        }
    });
}

String.prototype.trim=function(){
    return this.replace(/(^\s*)|(\s*$)/g, "");
};
 
String.prototype.ltrim=function(){
    return this.replace(/(^\s*)/g,"");
};

String.prototype.rtrim=function(){
    return this.replace(/(\s*$)/g,"");
};

String.prototype.endWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substring(this.length-str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

String.prototype.startWith=function(str){
	if(str==null||str==""||this.length==0||str.length>this.length)
	  return false;
	if(this.substr(0,str.length)==str)
	  return true;
	else
	  return false;
	return true;
};

//是否存在指定函数 
function isExitsFunction(funcName) {
    try {
        if (typeof(eval(funcName)) == "function") {
            return true;
        }
    } catch(e) {}
    return false;
}
//是否存在指定变量 
function isExitsVariable(variableName) {
    try {
        if (typeof(variableName) == "undefined") {
            //alert("value is undefined"); 
            return false;
        } else {
            //alert("value is true"); 
            return true;
        }
    } catch(e) {}
    return false;
}

//表单数据转成json对象
$.fn.funSerializeObject= function() {

	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {

			if (!o[this.name].push) {
				o[this.name] = [ o[this.name] ];
			}
			o[this.name].push(this.value || '');
		} else {
			//alert( this.value);
			o[this.name] = this.value || '';
		}
	});
	return o;
};

//表单数据转成json数据
$.fn.funEncodeJson = function() {
	return JSON.stringify(this.funSerializeObject());
};
