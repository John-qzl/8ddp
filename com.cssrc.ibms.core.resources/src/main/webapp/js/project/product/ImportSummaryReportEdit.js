/**
 * 验收报告/编辑页/js
 */

$(function(){
	debugger;
	var type=$.getParameter("type");
	if(type=="add"){
		$("input[name='m:cpysbgb:spzt']").attr("value", "未审批");
	}
	// 如果当前是新增页面或者流程启动页面就去 找对应策划的数据回填
	if(type=="add"||type=="startAdd"){
		var acceptancePlanId=$.getParameter("acceptancePlanId");
		var html="";
		$.ajax({
			url: __ctx+"/product/acceptance/plan/getAcceptancePlanGroup.do?acceptancePlanId="+acceptancePlanId,
			async:false,	
			  success:function(data){
				  for(var i=0;i<data.length;i++){
					  var detailedData=data[i];
					  html+='<tr class="listRow" formtype="edit"> ';
					  html+='<td tname="r:r:cpyszb:zw"><span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<input name="r:cpyszb:zw"  lablename="职务" controltype="select" validate="{empty:false}" readonly=""   value='+'"'+detailedData.F_ZW+'"'+'  class="inputText">';
					  html+='</span></td>';
					  
					  html+='<td tname="r:r:cpyszb:xm">';
					  html+='<input name="r:cpyszb:xmID" type="hidden" lablename="姓名ID" class="hidden" value='+'"'+detailedData.F_XMID+'"'+'>';
					  html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<input name="r:cpyszb:xm"  lablename="姓名" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_XM+'"'+'  class="inputText  ">';
					  html+='</span></td>';
					  
					  html+='<td tname="r:r:cpyszb:dw">';
					  html+='<input name="r:cpyszb:dwID" type="hidden" lablename="单位ID" class="hidden" value='+'"'+detailedData.F_DWID+'"'+'>';
					  html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<input name="r:cpyszb:dw"  lablename="单位" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_DW+'"'+'  class="inputText">';
					  html+='</span></td>';
					  
					  html+='<td tname="r:r:cpyszb:fzxm">';
					  html+='<span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<input name="r:cpyszb:fzxm"  lablename="负责项目" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.F_FZXM+'"'+'  class="inputText">';
					  html+='</span></td>';

					  html+='<input type="hidden" name="r:cpyszb:id" value="">';
					  html+='</tr>';
				  }
				  
				  debugger;
				 
				  $("#context").append(html);
			  }
		});
		$.ajax({
		    url: __ctx+"/product/acceptance/plan/getAcceptancePlanInfoById.do?acceptancePlanId="+acceptancePlanId,
		    async:false,
		    success:function(data){
		    	if(data!=null&&data!=""){
		    		debugger;
		    		$("input[name='m:cpysbgb:bgbbh']").attr("value", data.F_CHBGBBH);
		    		$("input[name='m:cpysbgb:ssxh']").attr("value", data.F_SSXH);
		    		$("input[name='m:cpysbgb:cpmc']").attr("value", data.F_CPMC);
		    		$("input[name='m:cpysbgb:cpdh']").attr("value", data.F_CPDH);
		    		$("input[name='m:cpysbgb:yzdw']").attr("value", data.F_YZDW);
		    		$("input[name='m:cpysbgb:yzdwId']").attr("value", data.F_YZDWID);
		    		$("input[name='m:cpysbgb:yzjd']").attr("value", data.F_YZJD);
		    		$("input[name='m:cpysbgb:ysrq']").attr("value", data.F_YSRQ);
		    		$("input[name='m:cpysbgb:ysdd']").attr("value", data.F_YSDD);
		    		$("input[name='m:cpysbgb:yscpsl']").attr("value", data.F_CPSL);
		    		$("input[name='m:cpysbgb:yscpbh']").attr("value", data.F_CPBH);
		    		$("input[name='m:cpysbgb:ysyj']").attr("value", data.F_YSYJWJ);

		    		if(data.F_YSYJWJ!=null){
		    			var file=JSON.parse(data.F_YSYJWJ);
		    			if(file.length>0){
		    				$("textarea[name='m:cpysbgb:ysyj']").attr("value",data.F_YSYJWJ);
		    				var fileId=file[0].id;
		    				
			    			$.ajax({
			    			    url: __ctx+"/oa/system/sysFile/getFile.do?fileId="+fileId,
			    			    async:false,
			    			    success:function(fileData){
			    			    	// 处理文件
			    			    	var html='<span class="attachement-span">';
			    			    	html+='<span fileid="10000023120044" name="attach" ';
			    			    	html+='file="'+fileData.fileId+',ng-inspector.safariextz">';
			    			    	html+='<a class="attachment" target="_blank"';
			    			    	html+='path="'+fileData.filepath+'" onclick="AttachMent.handleClickItem(this)"';
			    			    	html+='title="'+fileData.filename+'">';
			    			    	html+=fileData.filename+'</a>';
			    			    	html+='</span><a href="javascript:;" onclick="AttachMent.download(this);" title="下载" class="link download"></a>';
			    			    	html+='&nbsp;<a href="javascript:;" onclick="AttachMent.delFile(this);" title="删除" class="link del">';
			    			    	html+='</a></span>';
			    			    	$("#acceptanceFile").prepend(html);
			    			    	$(".del").hide();
			    			    }
			    			})
		    			}
		    		}
		    	}	 
		    }
		 })
	}
})

function displayType(conf){
	if($.isEmpty(conf&&conf.type&&conf.scope)){
		return;
	}
	var me = $(conf.scope||this);
	var url = me.attr("action");
	var params = url.getArgs();
	var _height=window.top.document.documentElement.clientHeight;
	var displayId = params.__displayId__;
	var permissionUrl = __ctx+"/oa/form/dataTemplate/getManagePermission.do";
	permissionUrl+="?displayId="+displayId;
	$.getJSON(permissionUrl,function(result){
		if(result.success){
			if(true){
				openLinkDialog({scope:conf.scope,width:500,height:400,isFull:false});
			}else{
				$.ligerDialog.warn("没有明细访问权限！",'提示信息');
			}
		}else{
			$.ligerDialog.error("getManagePermission请求出错！",'提示信息');
		}
	});
	function openLinkDialog(conf){
		conf = conf || {};
		var keyName = conf.keyName;
		if(keyName){
			try{
				var rtn = getPreScript(keyName);
				if(rtn!=null&&rtn==false){
					return;  
				}
			}catch(e){
				console.error(e);
				alert("前置脚本执行出错！");
			}
		}	
		var	contentWidth = window.top.document.documentElement.clientWidth,
			contentHeight = window.top.document.documentElement.clientHeight;
		conf.width>contentWidth?conf.width = contentwidth:null;
		conf.height>contentHeight?conf.height = contentHeight:null;
		var obj =conf.scope||this; 
			width= conf.width||1000,
			height=conf.height||760,
			isFull =conf.isFull||false,
			isStart = conf.isStart||false,
			title = conf.title||'';
		if(isFull){
			height=contentHeight;
			width=contentWidth;
		}
		if(title == ''){
			title = '提示';
		}
		if(isStart){
			$.ligerDialog.warn("请先绑定流程！");
			return;
		}
		var url=$(obj).attr("action");
		var optClass=$(obj).attr("class");
		url=url.getNewUrl();
		DialogUtil.open({
			height:height,
			width: width,
			url: url,
			showMax: false,                             //是否显示最大化按钮 
		    showToggle: false,                         //窗口收缩折叠
		    title: title,
		    showMin: false,
			sucCall:function(rtn){
				if(keyName){
					try{
						getAfterScript(keyName);
					}catch(e){
						console.error(e);
						alert("后置脚本执行出错");
					}
				}
				//rtn作为回调对象，可进行定制和扩展
				if(!(rtn == undefined || rtn == null || rtn == '')){
					//自动刷新有树的页面情况
					//setTimeout(function(){
					//	parent.isRefresh=true;
					//	parent.reFresh();
					//},0);
					if(window.parent.document.getElementById("treeFresh")) {
						window.parent.document.getElementById("treeFresh").click();
					}
					//刷新原来的当前的页面信息
					locationPrarentPage();
					//如果存在ifrem父页面 存在 dbomtree 存在 reloadSelectNode方法，需要重新load 选中的当前树节点数据
					location.href=location.href.getNewUrl();
				}
			}
		});
	}
}
window.onload = function(){
	debugger;
	var problem=$("#problem").val();
	if(problem!=""&&problem!=undefined){
		if(problem=="无"){
			$(".table-grid").hide();
			$("#legacy").hide();
			$("#problemSigned").hide(); 
			$("#situationConfirm").hide();
			$("#confirmSigned").hide();	
			$(".table-grid").hide();
		}
	}

}
