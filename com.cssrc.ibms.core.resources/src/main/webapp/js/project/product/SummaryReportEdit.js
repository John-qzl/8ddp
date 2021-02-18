/**
\ * 验收报告/编辑页/js
 */

$(function(){

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
				  var userIds="";
				  var userNames="";
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

					  html+='<td tname="signPath"><span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<img style="width:100px;height:31px"  src='+'"'+"/dp/oa/system/sysFile/getFileById.do?fileId="+detailedData.signPath+'"'+'>';
					  html+='</span></td>';
					  
					  html+='<td style="display:none" tname="r:r:cpyszb:qsId"><span name="editable-input" style="width:150px" class="selectinput input-content select-pullOpt">';
					  html+='<input name="r:cpyszb:qsId"  lablename="签署Id" readonly="readonly" controltype="select" validate="{empty:false}" readonly="" value='+'"'+detailedData.signPath+'"'+'  class="inputText">';
					  html+='</span></td>';
					  
					  html+='<input type="hidden" name="r:cpyszb:id" value="">';
					  html+='</tr>';
					  if(detailedData.F_XMID!=""&&detailedData.F_XMID!=null&&detailedData.F_ZW!="组长"){
						  userIds+=detailedData.F_XMID+",";
						  userNames+=detailedData.F_XM+",";
					  }
				  }
		
				  if(userIds!=""){
					  $.ajax({
							url: __ctx+"/product/acceptance/plan/checkDepartment.do",
							async:false,	
							data:{userIds:userIds,userNames:userNames},
							  success:function(data){
								  $("input[name='m:cpysbgb:zyqr']").attr("value", data.userNames);
								  $("input[name='m:cpysbgb:zyqrID']").attr("value",data.userIds);
							  }
					  })
				  }
				  $("#context").append(html);
			  }
		});
		$.ajax({
		    url: __ctx+"/product/acceptance/plan/getAcceptancePlanById.do?acceptancePlanId="+acceptancePlanId,
		    async:false,
		    success:function(data){
		    	if(data!=null&&data!=""){
		    		debugger;
		    		// 验收报告填写策划回传的数据
		    		if(data.F_YSBGHCSJ!=null&&data	.F_YSBGHCSJ!=""){
		    			var json=JSON.parse(data.F_YSBGHCSJ);
		    			$("#problem").text(json.problem);
		    			$("#opinion").text(json.opinion); 
		    			$("#czdwyj").text(json.sellerOpinion);
		    			$("#qtsm").text(json.instructions);
		    			$("#serialNumber").val(json.serialNumber);
		    			
		    			if(json.signreses.length!=0){
		    				$("input[name='m:cpysbgb:bgbbh']").attr("value", data.F_SSXH);
		    			}
		    			if(data.signList!=null){
		    				for(var i=0;i<data.signList.length;i++){
	    						var dataSign=data.signList[i];
	    						var signresesId=dataSign.ID;
			    				var time=dataSign.F_SIGNTIME.substring(0,10);
		    					if(dataSign.F_NAME.indexOf("组长")>=0){
				    				$("input[name='m:cpysbgb:yszzqsrq']").attr("value", time);		
				    				setImagePath(signresesId,$("input[name='m:cpysbgb:yszz']"),$("#tdyszz"));
		    					}   
		    					else if(dataSign.F_NAME.indexOf("承制方")>=0){
		    	    				$("input[name='m:cpysbgb:czfqsrq']").attr("value", time);		    				
				    				setImagePath(signresesId,$("input[name='m:cpysbgb:czf']"),$("#czfqs"));
		    					}
		    				}
		    			}
		    		}
		    		$("input[name='m:cpysbgb:xzzz']").attr("value", data.F_YSZZ);
		    		$("input[name='m:cpysbgb:xzzzID']").attr("value", data.F_YSZZID);
		    		$("input[name='m:cpysbgb:bgbbh']").attr("value", data.F_CHBGBBH);
		    		$("input[name='m:cpysbgb:ssxh']").attr("value", data.F_SSXH);
		    		$("input[name='m:cpysbgb:ssxhID']").attr("value", data.F_SSXHID);
		    		$("input[name='m:cpysbgb:cpmc']").attr("value", data.F_CPMC);
		    		$("input[name='m:cpysbgb:cpdh']").attr("value", data.F_CPDH);
		    		$("input[name='m:cpysbgb:yzdw']").attr("value", data.F_YZDW);
		    		$("input[name='m:cpysbgb:yzdwId']").attr("value", data.F_YZDWID);
		    		$("input[name='m:cpysbgb:yzjd']").attr("value", data.F_YZJD);
		    		$("input[name='m:cpysbgb:ysrq']").attr("value", fomtDate(data.F_YSRQ));
		    		$("input[name='m:cpysbgb:ysdd']").attr("value", data.F_YSDD);
		    		$("input[name='m:cpysbgb:yscpsl']").attr("value", data.F_CPSL);
		    		$("input[name='m:cpysbgb:yscpbh']").attr("value", data.F_CPBH);
		    		$("input[name='m:cpysbgb:ysyj']").attr("value", data.F_YSYJWJ);
					//验收总结编号,不是验收策划编号
						//取尾号
				/*	var lastNextNumber="";*/
					/*$.ajax({
						url:__ctx+"/product/acceptance/report/getNextNumber.do",
						data:{'xhId':data.F_SSXHID},
						dataType:"json",
						async:false,
						success:function(result){
							console.log("请求成功");
							console.log(result);
							lastNextNumber=result;
						},
						error:function () {
							console.log("ajax请求失败");
						}
					})*/
					var summaryNumber="";
				/*	var date = new Date();
					var year = date.getFullYear();
					planNumber=$("input[name='m:cpysbgb:bgbbh']").val()+"";
					moduleKey=planNumber.substring(0,planNumber.indexOf("-"));*/
					summaryNumber=data.F_CHBGBBH.replace("YSCH","YSBG");
					$("input[name='m:cpysbgb:zjbh']").val(summaryNumber);
					//验收总结编号--------完
		    		if(data.F_YSYJWJ!=null){
		    			var file=JSON.parse(data.F_YSYJWJ);
		    			if(file.length>0){
		    				$("textarea[name='m:cpysbgb:ysyj']").attr("value",data.F_YSYJWJ);
		    				var html='<div class="attachement">';
		    				for(var i=0;i<file.length;i++){
		    					var fileId=file[i].id;
		    					$.ajax({
				    			    url: __ctx+"/oa/system/sysFile/getFile.do?fileId="+fileId,
				    			    async:false,
				    			    success:function(fileData){
				    			    	html+='<span class="attachement-span">';
				    			    	html+='<span fileid="'+fileData.fileId+'" name="attach" file="'+fileData.fileId+','+fileData.filename+'.'+fileData.ext+'">';
				    			    	html+='<a class="attachment" target="_blank" path="/dp/oa/system/sysFile/file_'+ fileData.fileId+'.do" onclick="AttachMent.handleClickItem(this)" title="'+fileData.filename+'">';
				    			    	html+=fileData.filename+'.'+fileData.ext+'</a></span><a href="javascript:;" onclick="AttachMent.download(this);" title="下载" class="link download"></a>&nbsp;</span>';
				    			    }
		    					});

		    				}
	    					html+="</div>";
	    					$("#acceptanceFile").prepend(html);
		    			}
		    		}
		    	}
		    }
		 })
	}
	else{
		var fileId=$("#td1").text();
		$("#tdyszz").attr("src","/dp/oa/system/sysFile/getFileById.do?fileId="+fileId);
		
		var fileId1=$("#td2").text();
		$("#czfqs").attr("src","/dp/oa/system/sysFile/getFileById.do?fileId="+fileId1);
	}
})
	function setImagePath(Id,controls,filepath){
		$.ajax({
			url: __ctx+"/oa/system/sysFile/getFileByDataId.do?dataId="+Id,
		    async:false,
		    success:function(imagefile){
		    	filepath.attr("src","/dp/oa/system/sysFile/getFileById.do?fileId="+imagefile.fileId);
		    	controls.attr("value", imagefile.fileId);
		    	
		    }
		});
	}

function fomtDate(date){  //格式化日期
	var time = new Date(date.replace("-","/"));
	var b = 24*60; //分钟数
	time.setMinutes(time.getMinutes() + b, time.getSeconds(), -1);
	return time.format("yyyy-MM-dd");
}
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
	var problem=$("#opinion").val();
	if(problem!=""&&problem!=undefined){
		
	}
	
	
}
