/**
 人员、角色、组织、岗位选择器 html初始化
 * */
$(function(){
	//页面加载时,初始化
	handSelector();
});

/**
 * 初始化选择器，根据class构建选择器HTML
 */
function init(){
	$('[ctlType="selector"]').each(function(){
		var type = $(this).attr('class');
		//组建选择器html
		buildSelector($(this), type);
	});
}
var selelctTag = $('<span class="selectTag"></span>');
var selectField = $('<a href="javascript:;" class="link ryxzq-a" name="" ></a>');
var resetField = $('<a href="javascript:;" class="link reset ryxzq-a" name="" ></a>');
var hiddenField = $('<input name="" type="hidden" lablename="" class="hidden" value="" >');
var selectorFiledListBox = '<div class="input-content-list showDis"><div class="search-box">'
			     + '<input class="search-box-input selectorInput" type="text" value="" placeholder="请输入要查询的姓名" /></div>'
			     + '<ul class="list"></ul></div>';

var valArr = [];
/**
 * 组建选择器html
 * obj：     1.将在此对象之前添加"隐藏域"，
 *          2.在其之后添加"选择"、"重置"按钮
 * className：选择器的class属性值
 */
function buildSelector(obj, className){
	var self = $(obj);
	
	//判断是否设置为只读提交（只读提交时选择器会隐藏输入到页面，class中会多一个hidden）,此时给选择器添加隐藏的外层div
	if(className!=undefined&&className!=null&&className.indexOf("hidden")!=-1){
		self.wrap('<div class="input-content hidden"></div>');
	}else{
		self.wrap('<div class="input-content"></div>');
	}
	
	var name = self.attr('name');
	var lablename = self.attr('lablename');
	var initvalue = self.attr('initvalue');
	var selectObj = selectField.clone(true);
	var hiddenObj = hiddenField.clone(true);
	var resetObj  = resetField.clone(true);
	var tag = selelctTag.clone(true);
	
	hiddenObj.attr('name',name+'ID');
	hiddenObj.attr('lablename',lablename+'ID');
	if(initvalue && initvalue!=''){
		hiddenObj.attr('value',initvalue);
	}
	//将在此对象之前添加"隐藏域"
	self.before(hiddenObj);

	//将在此对象之后添加列表
	self.after(selectorFiledListBox)
	
	var inputText = $("input[name='"+name+"']");
	var right = inputText.attr("right");
	
	if(right=="r"||right=="rp"){//只读和只读是不增加按钮
		return false;
	}else{
		//在其之后添加"下拉"标识
		self.after(tag);
		
		resetObj.attr('name',name);
		//在其之后添加"重置"按钮
		self.after(resetObj);
		
		selectObj.attr('name',name);
		selectObj.addClass(className);
		//在其之后添加"选择"按钮
		self.after(selectObj);
	}
}

/**
 * 显示选择器对话框。
 * obj 按钮控件
 * fieldName 字段名称
 * type :选择器类型。
 * 1.人员选择器（单选）
 * 2.人员选择器（多选）
 * 3.角色选择器（单选）
 * 4.角色选择器（多选）
 * 5.组织选择器（单选）
 * 6.组织选择器（多选）
 * 7.岗位选择器（单选） 
 * 8.岗位选择器（单选）
 */
function handSelector(){
	// 初始化选择器，根据class构建选择器HTML
	init();
	
	//人员列表
	$("body").delegate("input[ctlType='selector']","click",function(event){
		isShowList($(this),event);
		getListDetail($(this),event);
	})
	//下拉标识点击
	$("body").delegate("span.selectTag","click",function(event){
		isShowList($(this),event);
		getListDetail($(this),event);
	})
	//点击其他区域隐藏列表
	$("body").delegate(document,"click",function(){
		hideAllList($(this))
	})
	//ajax获取列表详情
	$("body").delegate('input.search-box-input.selectorInput',"keyup",function(event){
		getListDetail($(this),event)
	})
	//input焦点事件
	$("body").delegate('input.search-box-input.selectorInput',"click",function(event){
		inputSearchClick($(this),event)
	})
	//1.人员选择器（单选）
	$("body").delegate("a.link.user", "click",function(){
		selector($(this),1);
	});
	//2.人员选择器（多选）
	$("body").delegate("a.link.users", "click",function(){
		selector($(this),2);
	});
	//3.角色选择器（单选）
	$("body").delegate("a.link.role", "click",function(){
		selector($(this),3);
	});
	//4.角色选择器（多选）
	$("body").delegate("a.link.roles", "click",function(){
		selector($(this),4);
	});
	//5.组织选择器（单选）
	$("body").delegate("a.link.org", "click",function(){
		selector($(this),5);
	});
	// 6.组织选择器（多选）
	$("body").delegate("a.link.orgs", "click",function(){
		selector($(this),6);
	});
	//7.岗位选择器（单选） 
	$("body").delegate("a.link.position", "click",function(){
		selector($(this),7);
	});
	//8.岗位选择器（单选）
	$("body").delegate("a.link.positions", "click",function(){
		selector($(this),8);
	});
	//9.流程实例选择器（多选）
	$("body").delegate("a.link.actInsts", "click",function(){
		selector($(this),9);
	});
	//10.职务（多选）
	$("body").delegate("a.link.jobs", "click",function(){
		selector($(this),10);
	});
	//重置选择器的值
	$("body").delegate("a.link.reset", "click",function(){
		var obj = $(this);
		var fieldName=obj.attr("name");
		var parent=obj.parent();
		var idFilter="input[name='"+fieldName+"ID']";
		var nameFilter="input[name='"+fieldName+"']";
		var inputId=$(idFilter,parent);
		var inputName=$(nameFilter,parent);
		inputId.val("");
		inputName.val("");
		inputName.removeAttr("refid");
		
	});
};

//列表显示与否
function isShowList(obj,e){
	var list = $('.input-content-list');
	var curList = obj.nextAll('.input-content-list');
	//隐藏所有的list
	list.hide();
	//当前的list状态
	curList.show();
	curList.find('input').focus();
	oCanelBubble(e);
}

//点击其他区域，隐藏列表
function hideAllList(){
	$('.input-content-list').hide();
	$('input.search-box-input.selectorInput').val('')
	//$('ul.list').html('')
}

//此参数用于判断是那种选择器
var selectorType;

//获取列表详情
function getListDetail(obj,e){
	
	//防止输入框有值时查询值被过滤
	var fuzzyName = "";
	if(obj.is(".search-box-input")){
		fuzzyName = obj.val();
	}
	
	if(obj.is(".selectTag")){
		if(obj.prevAll("input.user").length==1){
			selectorType = obj.prevAll("input.user").attr("class");
		}else if(obj.prevAll("input.org").length==1){
			selectorType = obj.prevAll("input.org").attr("class");
		}else if(obj.prevAll("input.position").length==1){
			selectorType = obj.prevAll("input.position").attr("class");
		}else if(obj.prevAll("input.role").length==1){
			selectorType = obj.prevAll("input.role").attr("class");
		}
	}else if(obj.attr("ctltype")=="selector"){
		selectorType = obj.attr("class");
	}
	
	//人员与组织存在级联查询或者数据过滤的情况
	var type,typeVal,variety,relname,relvalue;
	var scopeValue = obj.attr("scope");
	
	if(obj.is(".selectTag")){
		scopeValue = obj.prevAll("input.user").attr("scope");
	}
	if(scopeValue!=undefined&&scopeValue!=null&&scopeValue!=""){
		var scopeValues =eval("("+scopeValue+")");
		if(scopeValues){
			//通过typeVal进行数据过滤
			type = scopeValues.type;
			typeVal = scopeValues.value; 
			
			variety = scopeValues.variety;
			relname = scopeValues.relname;
			
			//存在级联关系时对三种情况进行处理
			//关联关系为主表之中或者主表与子表、主表与关联表
			if(variety == "1" || variety == "2"){
				relvalue = $("input[name='"+relname+"']").val();
			}
			//关联表之中或者子表之中
			if(variety == "3"){
				if(relname.indexOf("s:")!=-1){//子表
					relvalue = $(obj).closest("tr").find("input[name='"+relname+"']").val();
				}
				if(relname.indexOf("r:")!=-1){//关联表
					relvalue = $(obj).closest("table").find("input[name='"+relname+"']").val();
				}
			}
		}
	}
	
	var userUrl = __ctx + "/oa/system/sysUser/getFuzzySysUser.do";
	var orgUrl = __ctx + "/oa/system/sysOrg/getFuzzySysOrg.do";
	var positionUrl = __ctx + "/oa/system/position/getFuzzyPosition.do";
	var roleUrl = __ctx + "/oa/system/sysRole/getFuzzySysRole.do";
	
	var dataUrl = "";
	//添加过滤条件
	if(selectorType!=undefined&&selectorType!=null&&selectorType!=""){
		if(selectorType.indexOf("acceptanceFile")!=-1){
			typeVal=$("#xhID").val();
			type="acceptanceFile";
		}
	}
	
	if(selectorType!=undefined&&selectorType!=null&&selectorType!=""){
		if(selectorType.indexOf("userFileter")!=-1){
			type="currentOrgFilter";
			typeVal="self";
		}else if(selectorType.indexOf("orgFileter")!=-1){
			type="currentOrgFilter";
			typeVal="self";
		}
	}
	
	//给不同类型的选择框设置不同的访问路径
	if(selectorType!=undefined&&selectorType!=null&&selectorType!=""){
		if(selectorType.indexOf("user")!=-1){
			dataUrl = userUrl;
		}else if(selectorType.indexOf("org")!=-1){
			dataUrl = orgUrl;
		}else if(selectorType.indexOf("position")!=-1){
			dataUrl = positionUrl;
		}else if(selectorType.indexOf("role")!=-1){
			dataUrl = roleUrl;
		}
	}
	
	$.ajax({
		type:"get",
		url:dataUrl,
		async:true,
		data:{
			fuzzyName:fuzzyName,
			relvalue:relvalue,
			type:type,
			typeVal:typeVal
		},
		dataType:"json",
		success:function(res){
			var dataObj = JSON.parse(res.message);
			
			var html ="";
			if(res.type == "user"){
				$(dataObj).each(function(index,item){
					html += "<li data-id='"+item.userId+"'>"+item.fullname + "</li>"
				})
			}else if(res.type == "org"){
				$(dataObj).each(function(index,item){
					html += "<li data-id='"+item.orgId+"'>"+item.orgName + "</li>"
				})
			}else if(res.type == "position"){
				$(dataObj).each(function(index,item){
					html += "<li data-id='"+item.posId+"'>"+item.posName + "</li>"
				})
			}else if(res.type == "role"){
				$(dataObj).each(function(index,item){
					html += "<li data-id='"+item.roleId+"'>"+item.roleName + "</li>"
				})
			}
			
			if(obj.is('.search-box-input')){
				obj.parent().next('ul.list').html(html);
				selectList(obj.parent().next('ul.list'),e)
			}else{
				obj.nextAll(".showDis.input-content-list").find('ul.list').html(html);
				selectList(obj.nextAll(".showDis.input-content-list").find('ul.list'),e)
			}
		},
		error:function(err){
			console.log(err.message)
		}
	});
	oCanelBubble(e);
}

//列表选择事件
function selectList(obj,e){
	obj.off('click',"li");
	obj.on('click','li',function(){
		var name = $(this).html();
		var userId = $(this).attr("data-id");
		$(this).parents('div.input-content').children('input[ctltype="selector"]').val(name).change();
		$(this).parents('div.input-content').children('input[type=hidden]').val(userId).change();
		$(this).parent().prev().children('input').val('');
	})
}

//阻止事件冒泡
function oCanelBubble(e){
	if(e.stopPropagation){
		e.stopPropagation()
	}else{
		e.cancelBubble = true
	}
}

//inputFocus
function inputSearchClick(obj,e){
	oCanelBubble(e);//阻止事件冒泡
}

//发起人或上一任务执行人
function  orgFilterSetDialog(conf){
	var inputId=conf.inputId;
	var inputName=conf.inputName;
	url=__ctx + "/oa/form/dataTemplate/filterDialogSet.do?judgeConditionVal="+conf.judgeConditionVal+"&jugeVal="+inputId.val();
	url=url.getNewUrl();
	$.ligerDialog.open({
		height:300,
		width: 500,
		title : '过滤条件详细配置',
		url: url, 
		//自定义参数
		sucCall:function(rtn){
			if (inputId.length > 0) {
				inputId.val(JSON2.stringify(rtn.json));
			};
			inputName.val(rtn.description);
			
		}
	});
}


/**
 * 绑定选择器事件
 * 触发选择器 obj 按钮控件对象 type :选择器类型。
 */
function selector(obj, type) {
	var judgeConditionVal=$("select[name='judgeCondition']",$(obj).parent().parent()).val();
	var fieldName = obj.attr("name"), 
		parent = obj.parent(), 
		idFilter = "input[name='"+ fieldName + "ID']", 
		nameFilter = "input[name='" + fieldName+ "']", 
		linkFielter = "a[name='" + fieldName + "ID']", 
		inputId = $(idFilter, parent), 
		inputName = $(nameFilter, parent), 
		link = $(linkFielter, parent), 
		oldIdVal = inputId.val(), 
		idStr = inputId.val(), 
		nameStr = inputName.val(), 
		scopeValue = [],
		arguments = [];
	var className=inputName.context.className;
	var variety,relname,relvalue;
	debugger;
	if(className.indexOf("acceptanceFile")!=-1){
		var dataId=$("#xhID").val();
		var dataType="acceptanceFile";
	}
	//获取选择器中input的scope属性
	$.each(inputName,function(i,value){
		var attributes = value.attributes;
		for(var n = 0;n<attributes.length;n++){
			if(attributes[n].name == "scope"){
				scopeValue = attributes[n].value;
				var scopeValues =eval("("+scopeValue+")");
				//存在级联关系时对三种情况进行处理
				if(scopeValues){
					
					variety = scopeValues.variety;
					relname = scopeValues.relname;
					//关联关系为主表之中或者主表与子表、主表与关联表
					if(variety == "1" || variety == "2"){
						relvalue = $("input[name='"+relname+"']").val();
					}
					//关联表之中或者子表之中
					if(variety == "3"){
						if(relname.indexOf("s:")!=-1){//子表
							relvalue = $(obj).closest("tr").find("input[name='"+relname+"']").val();
						}
						if(relname.indexOf("r:")!=-1){//关联表
							relvalue = $(obj).closest("table").find("input[name='"+relname+"']").val();
						}
					}
				}
			}
		}
	});
	
	if (idStr) {
		var ids = idStr.split(','), names = nameStr.split(','), size = ids.length;
		
		for (var i = 0; i < size; i++) {
			arguments.push({
						id : ids[i],
						name : names[i]
					});
		}
	}

	switch (type) {
		// 人员选择器(单选)
		case 1 :
		// 人员选择器(多选)
		case 2 :
			if(judgeConditionVal==5){
				//所属组织负责人为当前用户
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});
			}else if(judgeConditionVal==6){
				//所属组织的分管领导为当前用户
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==7){
				//所属组织为当前用户组织的子组织
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==8){
				//所属角色和当前用户角色相同
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==10){
				//上级领导为当前用户
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==11){
				//所属组织为当前用户所属组织(可指定职务)
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==17){
				//上级组织负责人为当前用户
				orgFilterSetDialog({"judgeConditionVal":judgeConditionVal,"inputId":inputId,"inputName":inputName});

			}else if(judgeConditionVal==9){
				//所属角色包含
				RoleDialog({
					isSingle : true,
					scopeValue:scopeValue,
					callback : function(ids, names) {
						if (inputId.length > 0) {
							inputId.val(ids);
						};
						inputName.val(names);
					}
				});
			}else{
				UserDialog({
					isSingle : (type==1),
					scopeValue:scopeValue,
					relvalue:relvalue,
					dataId:dataId,
					dataType:dataType,
					selectUsers:arguments,
					callback : function(ids, names) {
						if (inputId.length > 0) {
							inputId.val(ids);
						};
						inputName.val(names);
						
						inputName.trigger("blur");
						if (inputId.val() != oldIdVal) {
							inputId.trigger("change");
							inputName.trigger("change");
						}
					}
				});
			}
			break;
		// 3.角色选择器(单选)
		case 3 :
			RoleDialog({
						isSingle : true,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;
		// 4.角色选择器（多选）
		case 4 :
			RoleDialog({
						arguments:arguments,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;
		// 5.组织选择器(单选)
		case 5 :
			OrgDialog({
						isSingle : true,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;

		// 6.组织选择器（多选）
		case 6 :
			OrgDialog({
						arguments:arguments,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;

		// 岗位选择器(单选)
		case 7 :
			PosDialog({
						isSingle : true,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;
		// 岗位选择器（多选）
		case 8 :
			PosDialog({
						arguments:arguments,
						scopeValue:scopeValue,
						callback : function(ids, names) {
							if (inputId.length > 0) {
								inputId.val(ids);
							};
							inputName.val(names);
						}
					});
			break;
		// 流程引用选择器（多选）
		case 9 :
			var defId = $("[name='defId']").val();
			if (!defId) {
				defId = 0;
			}
			ActInstDialog({
						defId : defId,
						isSingle : 2,
						scopeValue:scopeValue,
						arguments : arguments,
						callback : function(data) {
							if (!data) {
								return;
							}
							if (inputId.length > 0) {
								inputId.val(data.ids);
							};
							inputName.val(data.names);
							inputName.attr("refid", data.ids);
						}
					});
		case 10 ://职务选择器
			JobDialog({
				arguments:arguments,
				scopeValue:scopeValue,
				callback : function(ids, names) {
					if (inputId.length > 0) {
						inputId.val(ids);
					};
					inputName.val(names);
				}
			});
			break;
	}
	
	inputName.trigger("blur");
	if (inputId.val() != oldIdVal) {
		inputId.trigger("change");
		inputName.trigger("change");
	}
};