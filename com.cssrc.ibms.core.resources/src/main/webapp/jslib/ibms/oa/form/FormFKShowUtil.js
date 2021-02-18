if (typeof FormFKShowUtil == 'undefined') {
	FormFKShowUtil = {};
}


$(function(){
	//页面加载时,初始化
	//handFormFKColumnShow();
});
/**
 *类似结构:
(1)：<![CDATA[{name:'ctcsbdhk',fields:[{src:'ID',target:'kf_test_id'},{src:'F_NAME',target:'kf_test_idXXXXXFKColumnShow'}],query:[],rpcrefname='interfacesImplConsumerCommonService'}]]>
(2): <![CDATA[{name:'自定义对话框别名',fields:[{src:'自定义对话框的返回ID列',target:'外键列名'},{src:'自定义对话框的返回主键显示值列',target:'外键显示列，列名为“外键列名XXXXXFKColumnShow”'}],query:[],rpcrefname='rpc远程接口'}]]> 

 * 1.根据"自定义查询"的配置，读取数据库的外键信息。
 * 2.根据外键的权限配置，获取外键显示值的权限配置。
 * 3.自定义查询按钮绑定事件在FormUtil.js中。
 * 4.子表和rel表情况的外键列显示也在这里
 */
//初始化外键显示值。设置在 /jslib/ibms/oa/form/FormInit.js
FormFKShowUtil.initFKColumnShow=function(){
	// 历遍每个外键字段div（查询id以XXXXXFKColumnShowDIV结尾的div）. 
	$("div[id$='XXXXXFKColumnShowDIV']").each(function(){
		//该DIV包含 外键id标签，外键显示值标签，选择和重置按钮。
		var fkShowNameDivEle=$(this);
		//对每一个关联关系下的空间 重置按钮binding click 事件
		FormFKShowUtil.setResetButton(fkShowNameDivEle);
		//获取DIV的ID, 结构为(m:主表名：外键字段名XXXXXFKColumnShowDIV)
		var divId = fkShowNameDivEle.attr('id');	
		//获取外键id, 结构为(m:主表名：外键字段名)
		var fkIdName = divId.replace('XXXXXFKColumnShowDIV','');
		//获取外键id的值
		var fkId;
		var fkIdNameEls = $("input:hidden[name='"+fkIdName+"']",fkShowNameDivEle);
		if(fkIdNameEls!=null){
			fkIdNameEl = fkIdNameEls[0];
			if(fkIdNameEl!=undefined&&fkIdNameEl!=""&&fkIdNameEl!=null)
				fkId = fkIdNameEl.value;
		}
		//
		//if(fkId!=null && fkId.trim()!='' && fkId>=0){}    
		//获取  fk外键选择按钮
		$("a.extend",fkShowNameDivEle).each(function(){
			//获取fk外键选择"按钮"
			var extend = $(this);
			//只有外键的选择按钮 有fkname属性,kfname结构为(m:主表名：外键字段名)
			var fkName = extend.attr('kfname');
			if(fkName !=null && 'undefined' != fkName.toLowerCase() && fkName == fkIdName){
			//dialog="{name:'ctcsbdhk',fields:[{src:'F_MC',target:'kf_test_idXXXXXFKColumnShow'},{src:'ID',target:'kf_test_id'}],query:[],rpcrefname='interfacesImplConsumerCommonService'}"
			//获取fk外键选择"按钮"的 “自定义查询”配置信息
			var jsonStr = extend.attr('dialog');
			if(jsonStr != null && 'undefined' != jsonStr.toLowerCase() && jsonStr.length>2 ){
				//去掉字符串中的"<![CDATA[]]"
			    jsonStr = jsonStr.stripCData();
				var jsonObj = eval('(' + jsonStr + ')');
				//获取映射列。
				var fileds = jsonObj.fields;
				//根据自定义查询配置，从数据库查询出外键显示值。
				if(fkId!=null && fkId.trim()!='' && fkId>=0){
					FormFKShowUtil.getFKColumnShow(jsonObj,fkId, fkIdName,fkShowNameDivEle);
				}
				//对“自定义查询”按钮进行权限控制 
				FormFKShowUtil.controlDialogButtonRight(fkShowNameDivEle,fileds,extend);
			} 
		}
	  });
		
		
	
	});
};


// 外键显示值"重置"按钮
FormFKShowUtil.setResetButton = function(fkShowNameDivEle) {
	$(fkShowNameDivEle).delegate("a.link.resetFKShow", "click", function() {
		var dialog=$(fkShowNameDivEle).find("a.extend.fkselect").attr("dialog");
		dialog = dialog.stripCData();
		var jsonObj = eval('(' + dialog + ')');
		$(jsonObj.fields).each(function(i,f){
			$("input[name$=':"+f.target+"']").val("");
		})
		/*$("input", fkShowNameDivEle).each(function(i, input) {
			$(input).val("");
		});*/
	});
};

//获取 对话框数据
FormFKShowUtil.getDialogData = function(jsonObj, fkId, fkIdName,
		fkShowNameDivEle){
	// 获取自定义对话框名称
	var commonDialogName = jsonObj.name;
	// 获取映射列 ,结构为(字段名),不包含（m:主表名：）
	var fileds = jsonObj.fields;
	var rpcrefname = jsonObj.rpcrefname;// 需要调用的rpc远程接口名称
	// 循环将页面上的字段值，赋值给“自定义查询”中的映射字段,主要是对外键id进行赋值.
	var param = FormFKShowUtil.getQueryParam(fileds, fkShowNameDivEle, fkId,
			fkIdName);
	// 加入rpc远程接口参数
	if (rpcrefname) {
		param += '&rpcrefname=' + rpcrefname;
	}
	var url = __ctx + "/oa/form/formDialog/getFKColumnShow.do?dialog_alias_="
	+ commonDialogName + param;
	url = url.getNewUrl();
	var result;
	$.ajax({
		type:"post",
		url:url,
		async:false,
		success:function(data){
			result= data.resultMap;
		}
	})
	return result;
}

//dialog="{name:'ctcsbdhk',fields:[{src:'F_MC',target:'kf_test_idXXXXXFKColumnShow'},{src:'ID',target:'kf_test_id'}],query:[],rpcrefname='interfacesImplConsumerCommonService'}"
FormFKShowUtil.getFKColumnShow = function(jsonObj, fkId, fkIdName,
		fkShowNameDivEle) {
	// 获取映射列 ,结构为(字段名),不包含（m:主表名：）
	var fileds = jsonObj.fields;
	var resultBean;
	var targetArrays = [];
	try{
		for (var i = 0; i < fileds.length; i++) {
			var field=fileds[i];
			if (!field) {
				continue;
			}
			var target = field.target;
			var src = field.src;
			if ($.isEmpty(target)) {
				continue;
			}
			if(target.lastIndexOf("XXXXXFKColumnShow")>-1){
				//表示虚拟字段
				if(!resultBean){
					//对话框数据只需要取一次就可以了
					resultBean=FormFKShowUtil.getDialogData(jsonObj,fkId, fkIdName,fkShowNameDivEle);
				}
				var targetArr = target.split(',');
				for (var m = 0; m < targetArr.length; m++) {
					// 找到对应的 结构为(m:主表名：字段名)的页面元素
					var fkColumnShowName = "input[name$=':"+ targetArr[m] + "']";
					var ds = $(fkColumnShowName,fkShowNameDivEle);
					if (ds && ds.length > 0) {
						var fieldName = ds[0].name;
						var value = '无法获得显示值';
						// 获取返回值
						if (resultBean) {
							value = resultBean[src];
						}
						var right = '';// 获取字段权限，分三种情况，main主表字段，sub子表字段，rel从表字段
						if (fieldName.indexOf("s:") == 0) {
							// 获取子表字段权限
							right = FormFKShowUtil
									.getSubFileRight(fieldName);
						} else if (fieldName.indexOf("r:") == 0) {
							// 获取rel表字段权限
							right = FormFKShowUtil
									.getRelFileRight(fieldName);
						} else {
							// 主表权限
							right = ds[0].getAttribute('right');
						}

						// 判断target是否在数组中,即判断是否要将多个返回值赋值给一个对象。
						var inArray = $.inArray(targetArr[m],
								targetArrays);

						var targetIdEle = ds[0];
						if ((right == "r" || right == "rp")) {
							// 将可编辑的外键显示值改成不可编辑。 通过F12调试得到
							var parent = targetIdEle.parentElement;
							var fkColumnShowName = "input[name='"
									+ target + "']";
							var fkColumnShowEle = $(
									fkColumnShowName, parent);
							// 删除input标签
							$(targetIdEle).remove();
							// 显示为红色
							if (value != null
									&& value.indexOf('无法获得显示值') > -1) {
								value = '<font color=red>无法获得显示值</font>';
							}
							// 替换为显示值。
							parent.innerHTML = value;
						} else {
							//对input标签进行赋值。
							//将多个返回值赋值给一个对象。
							if (inArray > -1) {
								var splitPart = ds[0].value == '' ? ''
										: '-';
								ds[0].value = ds[0].value
										+ splitPart + value;
							} else {
								ds[0].value = value;
							}
						}
						//把target加入到数组中。用于判断是否要将多个返回值赋值给一个对象。
						targetArrays.push(targetArr[m]);

					}
				}
				
			}else{
				//物理字段--不需要从对话框赋值。
			}
		}
	}finally{
		//定制：应用于：不在当前表建冗余字段的情况，将关联表中的字段数据在当前页面显示。通过otherRelShowColumn['F_字段名'];方式获取值。
		var otherRelShowColumn = resultBean;
		FormFKShowUtil.fillOtherRelShowColumn(otherRelShowColumn);
	}
}


//绑定外部链接link TODO待开发
/*FormFKShowUtil.initExtLink = function(){
	$("body").delegate("a[linktype]","click",function(){
		var linktype = $(this).attr("linktype");
		if(!linktype){
			return;
		}
		var type = parseInt(linktype);
		switch (type) {
		case 4://用户单选
		case 8://用户多选
			var userid = $(this).attr("refid");
			url = __ctx+"/oa/system/sysUser/getByUserId.do?canReturn=2&userId="+userid;
			var args={};
			DialogUtil.open({
				height:600,
				width: 800,
				title : '用户选择',
				url: url, 
				isResize: true,
				args : args
			});
			break;
		case 5://角色
		case 17://角色
			var roleId = $(this).attr("refid");
			ShowExeInfo.showRole(roleId);
			break;
		case 6://组织
		case 18://组织
			var orgId = $(this).attr("refid");
			ShowExeInfo.showOrg(orgId);
			break;
		case 7://岗位
		case 19://岗位
			var posId = $(this).attr("refid");
			ShowExeInfo.showPos(posId);
			break;
		case 20:
			var runId = $(this).attr("refid");
			var url=__ctx+"/oa/flow/processRun/info.do?isOpenDialog=1&link=1&runId="+runId;
			var args={};
			DialogUtil.open({
				height:700,
				width: 900,
				title : '用户选择',
				url: url, 
				isResize: true,
				args : args
			})	
			break;
		}
	});
};*/

//获取子表字段权限   
FormFKShowUtil.getSubFileRight = function (fieldName){
	//替换fieldName，因为tttttttttXXXXXFKColumnShowDIV的权限和ttttttttt的权限一致
	var subfieldName = fieldName.replace('XXXXXFKColumnShow','');
	var right = '';
	if(typeof subFilePermissionMap != 'undefined' && subFilePermissionMap!= null ){
		  //子表新版本字段权限
		var subFileJsonList = subFilePermissionMap["subFileJsonList"];
		//子表权限标记: 
		if(subFileJsonList!=undefined && subFileJsonList!=null && subFileJsonList.length>0){
			for ( var j = 0; j < subFileJsonList.length; j++) {
				var objPermission=subFileJsonList[j];
				//获取表名
				var tableName = subfieldName.split(':')[1];;
				
				if(tableName==objPermission.tableName){
				    var subright = objPermission.power;
				    var name = "s:"+tableName+":"+objPermission.title;      //字段在HTML的真实NAME
					if(subright!="w" && subright!="b"){
						if(subfieldName == name){
							right = subright;
							break;
						  }
						}
					}
		        }
			}
		  }
	return right;
}
//获取rel表字段权限
FormFKShowUtil.getRelFileRight = function (fieldName){
	//替换fieldName，因为tttttttttXXXXXFKColumnShowDIV的权限和ttttttttt的权限一致
	var relfieldName = fieldName.replace('XXXXXFKColumnShow','');
	var right = '';
	//关系表
	if(typeof relFilePermissionMap != 'undefined' && relFilePermissionMap!= null ){
		//获得关系表权限
		var relFileJsonList =relFilePermissionMap["relFileJsonList"];//关系表新版本字段权限
		//关系表权限标记: relFileJsonType
		if(relFileJsonList!=undefined && relFileJsonList!=null && relFileJsonList.length>0){
			for ( var t = 0; t < relFileJsonList.length; t++) {
				var objPermission=relFileJsonList[t];
				var tableName = relfieldName.split(':')[1];
				if(tableName==objPermission.tableName){
				    var relright = objPermission.power;
				    var name = "r:"+tableName+":"+objPermission.title;      //字段在HTML的真实NAME
					if(relright!="w" && relright!="b"){ 
						if(relfieldName == name){
							right = relright;
							break;
						  }
					   }    
				}
			}
		}
	}	
	return right;
}

//循环将页面上的字段值，赋值给“自定义查询”中的映射字段,主要是对外键id进行赋值.
FormFKShowUtil.getQueryParam = function (fileds,fkShowNameDivEle,fkId,fkIdName){
	var param = '';
	for(var i = 0; i < fileds.length; i++) {
		if(fileds[i] == undefined){continue;}
		  //页面上的字段,结构为(字段名),不包含（m:主表名：）
		  var target = fileds[i].target;
		  //自定义查询中的字段,结构为(字段名),不包含（m:主表名：）
		  var src = fileds[i].src; 
		   if($.isEmpty(target)){continue;}
		   var targetArr = fileds[i].target.split(',');
		   for(var m=0;m<targetArr.length;m++){
			   //找到对应的 结构为(m:主表名：字段名)的页面元素
			   var fkColumnShowName="input[name$=':"+targetArr[m]+"']";
			   var ds = $(fkColumnShowName);
			   //默认取第一个元素 作为参数。
			   if(ds!=null && ds!="" && undefined != ds && ds.length > 0){
				  var fieldName = ds[0].name;
				   if(fieldName == fkIdName){
					   //只为ID赋值，所以如果找到fkId ，直接break就可以了
					   param += '&'+src+'='+fkId;
					   break;
				   }
			   }   
		   }
		   
	}
	return param;
	
}
//对“自定义查询”按钮进行权限控制 
FormFKShowUtil.controlDialogButtonRight = function (fkShowNameDivEle,fileds,extend){
	var tableName=$("#tableName").val();
	for (var i = 0; i < fileds.length; i++) {
		  if(fileds[i] == undefined){continue;}
		   if($.isEmpty(fileds[i].target)){
			   continue;
			}
			var targetArr = fileds[i].target.split(',');
			for(var m=0;m<targetArr.length;m++){
				var target = targetArr[m];
				
				var fkColumnShowName="input[name$=':"+target+"']";
				//如果是物理存在的字段，不能通过这种方式查找,只要是表单存在的，都可以进行赋值
				//var ds = $(fkColumnShowName,fkShowNameDivEle);
				var ds=$(fkColumnShowName);
				if(ds.length<1){
					//主表字段在只读时，其内容直接为值，所以当对话框读取对应主表字段时没有，证明字段是只读的，
					//所以要删除“自定义查询”按钮
					extend.remove(); 
					break;
				}else{
					var mark = false;
					ds.each(function(index){   //主表字段
						var targetIdEle = $(this);
						var right = $(this).attr("right");
						if(right=="r"||right=="rp"){
							extend.remove();  //只读和只读提交时,删除“自定义查询”按钮
							mark = true;
							return false;
						}else{
							//编辑权限时需要添加高亮样式 --zxg
							mark = true;
							targetIdEle.addClass('tableHighLight');
						}
					});
					if(mark){
						break;
					}
				}	
		 }				
	}
 }


//定制：应用于：不在当前表建冗余字段的情况，将关联表中的字段数据在当前页面显示。通过otherRelShowColumn['F_字段名'];方式获取值。
//定制时，在“自定义表单”处重写该方法。
FormFKShowUtil.fillOtherRelShowColumn = function (otherRelShowColumn){
}
