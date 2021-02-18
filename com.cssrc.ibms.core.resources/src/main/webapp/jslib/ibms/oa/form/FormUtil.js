if (typeof FormUtil == 'undefined') {
	FormUtil = {};
}
//tab前置回调事件
//前置可以通过返回 return false组织tab切换。
var onBeforeSelectTabItemCallBack=function(id){
	$(".l-tab-content>div[tabid='"+id+"'] .l-text.l-text-combobox").hide();
};
//tab后置回调事件
var onAfterSelectTabItemCallBack=function(id){
	$(".l-tab-content>div[tabid='"+id+"'] .l-text.l-text-combobox").show();
};

//拼接下拉单选框
var clearIcon = $('<span id="selectClear" class="selectClear iconfont icon-close"></span>');
var dialogTag = $('<span class="dialogTag"></span>');
var dialogFiledListBox = '<div class="input-content-list showdialog showDis"><div class="search-box">'
     + '<input class="search-dialog-input dialogInput" type="text" value="" placeholder="请输入要查询的字段内容" /></div>'
     + '<ul class="list"></ul></div>';

/**
 * 初始化表单tab。
 */
FormUtil.initTab=function(){
	var amount=$("#formTab").length;
	if(amount>0){
		$("#formTab").ligerTab({
			onBeforeSelectTabItem:function(tabid){						
				if(onBeforeSelectTabItemCallBack){
					return onBeforeSelectTabItemCallBack(tabid);
				}
			},
			onAfterSelectTabItem:function(tabid){
				FormUtil.changeTabStyle(tabid);	 //要放到TAB产生后，才执行这个样式的改变，方可可以生效，否则效果不一定有效
				if(onAfterSelectTabItemCallBack){
					onAfterSelectTabItemCallBack(tabid);
				}
	        }
		});
			
	}
};


/**
 * 初始化TAB中不能隐藏的对象。
 */
FormUtil.changeTabStyle=function(tabid){
	var tabidObj = $("div[tabid='"+tabid+"']");   //在产生的 DIV tab对象（由ligerTab产生的）		
	//处理OFFICE控件中 工具栏目 在个别浏览器上有出现不能隐藏的问题
	FormUtil.changeTabOfficeStyle(tabidObj);
	//处理图片控件容器会出现 不能隐藏的问题      要结合PictureShowPlugin处理
	FormUtil.changeTabZoomStyle(tabidObj);
};


/**
 * 处理OFFICE控件中 工具栏目 在个别浏览器上有出现不能隐藏的问题
 */
FormUtil.changeTabOfficeStyle=function(tabidObj){	
	var menuBars = $("div[name='menuBar']");
	var offices =$("input[controltype='office']");
	if( menuBars.length>0 && offices.length>0 ){
		menuBars.each(function(index){         //把所有的OFFICE控件中 工具栏目隐藏掉
			$(this).css("display","none");
		});
		
		$("div[name='menuBar']",tabidObj).each(function(index){        //把 DIV tab对象（由ligerTab产生的）的OFFICE控件中 工具栏目显示出来
			var menuBarRight = $(this).attr("menuBarRight");   //读取菜单的权限
			if(menuBarRight!="r"){
				$(this).css("display","block");
			}
		});
		
		//把在TAB里面的所有的OFFICE控件有影响的l-tab-content-item样式：overflow去掉
		tabidObj.attr("style","");
		
		 
		/*offices.each(function(index){         //把所有的OFFICE控件容器隐藏掉
			var name = $(this).attr("name");
			var divId="div_" + name.replaceAll(":","_");
			$("#"+divId).css("display","none");
		});
		
		$("input[controltype='office']",tabidObj).each(function(index){        
    		var name = $(this).attr("name");
			var divId="div_" + name.replaceAll(":","_");
			$("#"+divId).css("display","block");
		});*/

		OfficePlugin.isTabItemOffice = false;              //初始化图片控件样式的完成标志， 防止定时器不停止检查
	}
	
};

/**
 * 处理图片控件容器会在个别浏览器上有出现不能隐藏的问题      要结合PictureShowPlugin处理
 */
FormUtil.changeTabZoomStyle=function(tabidObj){	
//	var zoomContainers = $('div.zoomContainer');
	var pictureShows =$("input[controltype='pictureShow']");
	if( pictureShows.length>0 ){
        // ZOOM容器序号初始化情况        
		/*var num = null;
		if(PictureShowPlugin.isZoomNum){  //ZOOM容器isZoomNum为true证明还没有把序号初始化
        	PictureShowPlugin.initZoomContainer();         //ZOOM容器都没有序号时就重新按顺序赋值排序（图片控件对象的序号和ZOOM容器都序号是一致的，都是按每一次进入页面的顺序决定）
        	PictureShowPlugin.isZoomNum = false;
        }else{  //有序列化的情况
        	num = zoomContainers.get(zoomContainers.length-1).getAttribute("zoomContainerNum");   //ZOOM容器有序列化了，但是最后一个没有没有序号时要把最近修改的图片控件序号加上
        	if(typeof(num)==undefined||num==null||num==""||num=="null"){  //每一个ZOOM容器都没有序号证明还没有把序号初始化
            	PictureShowPlugin.setZoomContainer();         //ZOOM容器都没有序号时就重新按顺序赋值排序（图片控件对象的序号和ZOOM容器都序号是一致的，都是按每一次进入页面的顺序决定）
            }				
        }		
		
		zoomContainers.each(function(index){         //把所有的图片控件产生的zoomContainer容器隐藏掉
			$(this).css("display","none");
		});
		
		var myNum = pictureShows.get(0).getAttribute("myNum");
		if(typeof(myNum)==undefined||myNum==null||myNum==""||myNum=="null"){  //如果图片控件都没序号化时
			pictureShows.each(function(index){        //图片控件都没有序号时就重新按顺序赋值排序（图片控件对象的序号和ZOOM容器都序号是一致的，都是按每一次进入页面的顺序决定）
				$(this).attr("myNum",index);
			});         
        } */
		
		//图片控件显示处理(原因：个别浏览器有问题需要处理)
		pictureShows.each(function(index){         //把所有的图片控件容器隐藏掉
			var name = $(this).attr("name");
			var divId="div_" + name.replaceAll(":","_");
			$("#"+divId).css("display","none");
		});
		
		//把 DIV tab对象（由ligerTab产生的）的图片控件 显示出来
		$("input[controltype='pictureShow']",tabidObj).each(function(index){        
    		var name = $(this).attr("name");
			var divId="div_" + name.replaceAll(":","_");
			$("#"+divId).css("display","block");
		});
		
		//把 DIV tab对象（由ligerTab产生的）的图片控件在IE下面（IE6、9）大图有可能会发生偏移区域，只有做居左补上
		if(PictureShowPlugin.browserName=="MSIE"){
			$("div.wrap_div",tabidObj).each(function(index){        
	    		$(this).css("text-align","left");
			});
		}
		
		PictureShowPlugin.isTabItemZoom = false;                   //初始化图片控件样式的完成标志，防止定时器不停止检查
	}
};

/**
 * 默认是进入每一个TAB的特别处理
 */
FormUtil.initTabStyle=function(){
	var tabid = null;
	var amount=$("#formTab").length;
	if(amount>0){
		//默认是进入每一个TAB
		var tabid = null;
		$('div.l-tab-content-item').each(function(index){
			tabid = $(this).attr("tabid");            //选中每一个TAB
			if(typeof(tabid)!=undefined||tabid!=null||tabid!=""||tabid!="null"){ 
				return false;
			}
		});
		var tabidObj = $("div[tabid='"+tabid+"']");   //在产生的 DIV tab对象（由ligerTab产生的）	
		FormUtil.recursiveTabItemOffice(tabidObj);
		FormUtil.recursiveTabItemZoom(tabidObj);
	}	
};

/**
 * 对于当前页面展示自定义对话框的情况添加列表显示div
 */
FormUtil.initDialogList=function(){
	//初始化下拉单选框
	$('[dialogtype="1"]').each(function(){
		
		var me =$(this);
		//对于已经添加了样式的就不再进行处理了
		var isExit=me.parent().find(".dialogTag");
		if(isExit.length == 0){
			var clearIconObj = clearIcon.clone(true);
			var dialogTagObj  = dialogTag.clone(true);
			
			$(this).parent().attr("class","input-content");
			//将在此对象之后添加列表
			$(this).after(dialogFiledListBox)
			//将在此对象之后添加删除按钮
			$(this).after(clearIconObj);
			//将在此对象之后添加标识符
			$(this).after(dialogTagObj);
		}
		
	});
};

/**
 * 递归函数每隔1.5秒，直到Office工具栏目的样式修改好才，停止！
 */
FormUtil.recursiveTabItemOffice=function(tabidObj){	
	window.setTimeout(function(){ 
		if(OfficePlugin.isTabItemOffice){
			FormUtil.changeTabOfficeStyle(tabidObj);
			if(OfficePlugin.isTabItemOffice){
				FormUtil.recursiveTabItemOffice(tabidObj);
			}
		}
	},1500); 
};


/**
 * 递归函数每隔1.5秒，直到图片控件的容器的样式修改好才，停止！
 */
FormUtil.recursiveTabItemZoom=function(tabidObj){	
	window.setTimeout(function(){ 
		if(PictureShowPlugin.isTabItemZoom){
			FormUtil.changeTabZoomStyle(tabidObj);
			if(PictureShowPlugin.isTabItemZoom){
				FormUtil.recursiveTabItemZoom(tabidObj);
			}
		}
	},1500); 
};



/**
 * 初始化日历控件。
 */
FormUtil.initCalendar=function(){
	$("body").delegate("input.Wdate", "click",function(){
		var fmt=$(this).attr("dateFmt");
		WdatePicker({el:this,dateFmt:fmt});
	});
};


/**
 * 绑定"自定义对话框"。 按钮或者文本框定义如下：
 * dialog="{name:'globalType',
 *        fields:[{src:'TYPENAME',target:'m:mainTable:name'},{src:'TYPEID',target:'m:mainTable:address'}],
 *         query:[{'id':'bm','name':'ORGID','isMain':'true'}]}"
 * 
 * name:对话框的别名 fields：为字段映射，可以有多个映射。 src：对话框返回的字段。 target：需要映射的控件名称。dialogQuery：向自定义对话框传递的参数字段
 */
FormUtil.initCommonDialog=function(){
	
	//1.主表中"自定义对话框"的包含字段中有只读的权限时，只能把对话框功能删除
	//2.（子表和rel表情况的放在CustomForm.js中处理了）
	//3. 外键的"自定义对话框"在FormFKShowUtil.js的相应的方法中处理。
	$("a.extend",$(".formTable")).each(function(){      //只读时，历遍超链接中自定义对话框的按钮
		var extend=$(this);
		var jsonStr = extend.attr('dialog');
		var fkName = extend.attr('kfname');//判断是否为外键列.如果为外键列，则继续循环.
		if(jsonStr != null && 'undefined' != jsonStr.toLowerCase() && jsonStr.length>2
				&& fkName == undefined){
			//去掉字符串中的"<![CDATA[]]"
			jsonStr = jsonStr.stripCData();
			var jsonObj = eval('(' + jsonStr + ')');
			var fileds = jsonObj.fields;
			for ( var i = 0; i < fileds.length; i++) {
				   if($.isEmpty(fileds[i].target)){continue;}
				   //对"自定义查询"按钮进行显示、隐藏判断
					var targetArr = fileds[i].target.split(',');
					for(var m=0;m<targetArr.length;m++){
					    var ds = $(".formTable [name$=':"+targetArr[m]+"']").not("[name^='s:'],[name^='r:']");
						if(ds.length<1){
							//如果是详细页面(只读)，则页面没有name=targetArr[m] 的标签。则需要删除“自定义查询”按钮。
							//主表字段在只读时，其内容直接为值，所以当对话框读取对应主表字段时没有，证明字段是只读的，所以要删除“自定义查询”按钮。
							extend.remove();                 
							break;
						}else{
							var mark = false;
							ds.each(function(index){   //主表字段
								var right = $(this).attr("right");
								if(right=="r"||right=="rp"){
									//只读和只读提交时,删除“自定义查询”按钮。
									extend.remove();                
									mark = true;
									return false;
								}
							});
							if(mark){
								break;
							}
						}	
					}
									
			}
		}                           
	});
	
	//对于当前页面展示自定义对话框的情况再点击后显示隐藏的div
	$("body").delegate("input[dialogtype='1']","click",function(event){
		isShowList($(this),event);
		getDialogList($(this),event);
	})
	//下拉标识点击
	$("body").delegate("span.dialogTag","click",function(event){
		isShowList($(this),event);
		getDialogList($(this),event);
	})
	//点击其他区域隐藏列表
	$("body").delegate(document,"click",function(){
		hideDialogList($(this));
	})
	//ajax获取列表详情
	$("body").delegate('input.search-dialog-input.dialogInput',"keyup",function(event){
		getDialogList($(this),event)
	})
	//input焦点事件
	$("body").delegate('input.search-dialog-input.dialogInput',"click",function(event){
		inputDialogClick($(this),event)
	})
	//选择框后清除按键事件
	$(".selectClear").delegate(document,"click",function(){
		clearInput($(this));
	})
	//inputFocus
	function inputDialogClick(obj,e){
		oCanelBubble(e);;
	}
	//清空输入框事件
	function clearInput(obj){
		obj.prevAll("input[dialogtype='1']").val("");
		obj.hide();
	}
	//点击其他区域，隐藏列表
	function hideDialogList(){
		$('.input-content-list').hide();
		$('input.search-dialog-input.dialogInput').val('')
		//$('ul.list').html('')
	}
	
	var dialogFields;
	//获取列表详情
	function getDialogList(obj,e){
		var fuzzyName = "";
		if(obj.is(".search-dialog-input")){
			fuzzyName = obj.val();
		}
		
		//判断是否为新表单
		var dialog = "";
		var inputName = "";
		var tableNew_con = obj.closest("div.tableNew_con").length;
		//获取当前点击位置所在的列的数据
		if(tableNew_con == 1){//新表单
			dialog = obj.closest('div.tableNew_con').find(".inputText").attr("dialog");
			inputName = obj.closest('div.tableNew_con').find(".inputText").attr("name");
		}else{//平台标准表单
			dialog = obj.closest('td').find(".inputText").attr("dialog");
			inputName = obj.closest('td').find(".inputText").attr("name");
		}
		
		var names = [];
		if(inputName!=undefined&&inputName!=null&&inputName!=""){
			names= inputName.split(":");
		}
		
		//需要显示的字段名
		var showField = "";
		
		var dialogJson=eval("("+dialog+")" );
		//自定义对话框的别名
		var dialogName = dialogJson.name;
		
		var fields=dialogJson.fields;
		dialogFields = fields;
		
		for(var i=0;i<fields.length;i++){
			var json=fields[i];
			var src=json.src;
			var targets=json.target.split(',');
			for(var j=0;j<targets.length;j++){
				if(names[2]==targets[j]){
					showField = src;
				}
			}
		}
		
		var dataUrl = __ctx + "/oa/form/formDialog/getDialogData.do";
		$.ajax({
			type:"get",
			url:dataUrl,
			async:true,
			data:{
				dialogName:dialogName,
				showField:showField,
				fuzzyName:fuzzyName
			},
			dataType:"text",
			success:function(res){
				
				var data = $.parseJSON(res);
				var html ="";
				$(data).each(function(index,item){
					html += "<li dialogData='"+JSON.stringify(item)+"'>"+item[showField] + "</li>"
				})
				if(obj.is('.search-dialog-input')){
					obj.parent().next('ul.list').html(html);
					selectList(obj.parent().next('ul.list'),e)
				}else{
					obj.nextAll(".showDis.showdialog").find('ul.list').html(html);
					selectList(obj.nextAll(".showDis.showdialog").find('ul.list'),e)
				}
			},
			error:function(err){
				console.log(err)
			}
		});
		oCanelBubble(e);;
	}
	
	//列表选择事件
	function selectList(obj,e){
		
		obj.off('click',"li");
		obj.on('click','li',function(){
			
			var dialogData = $(this).attr("dialogData");
			
			var data = eval("(" + dialogData +")");
			
			var parentObj=obj.closest("[formtype]");
			var isGlobal=parentObj.length==0;
			
			//定义数组，用于判断是否要将多个返回值赋值给一个对象。
			var targetArrays = [];
			for(var i=0;i<dialogFields.length;i++){
				if(dialogFields[i] == undefined){
					continue;
				}
				var json=dialogFields[i];
				var src=json.src;
				var targets=json.target.split(','),target;
				while(target=targets.pop()){
					if(!target)return;
					var filter="[name$=':"+target+"']";
					//在子表中选择
					var targetObj=isGlobal?$(filter):$(filter,parentObj);
					//单选
					//判断target是否在数组中,即判断是否要将多个返回值赋值给一个对象。
					var inArray = $.inArray(target,targetArrays); 
					if(inArray>-1){
						var splitPart = targetObj.val()==''?'':'-';
						targetObj.val(targetObj.val() + splitPart + data[src]);
					}else{
						targetObj.val(data[src]);
					}
					targetObj.trigger("change");
					//把target加入到数组中。用于判断是否要将多个返回值赋值给一个对象。
					targetArrays.push(target);
				}
			}
			
			//显示清空按钮
			$(this).closest("td").find("#selectClear").show();
		});
	}
	
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
	
	//阻止事件冒泡
function oCanelBubble(e){
	if(e.stopPropagation){
		e.stopPropagation()
	}else{
		e.cancelBubble = true
	}
}
			     
	$("body").delegate("[dialog]", "click", function(){
		
		var obj=$(this);
		
		var dialogtype = obj.attr("dialogtype");
		
		var dialogJson=obj.attr("dialog");
		var fkName = obj.attr('kfname');//判断是否为外键列.如果为外键列，则继续循环.
		var json = '';
		if(fkName != undefined){
			dialogJson = dialogJson.stripCData();
		}
		
		json=eval("("+dialogJson+")" );
		
		var name=json.name;
		var rpcrefname=json.rpcrefname;//需要调用的rpc远程接口名称
		var fields=json.fields;
		var parentObj=obj.closest("[formtype]");
		var isGlobal=parentObj.length==0;
		//var paramsValueString = "" ;
		
		//获取自定义对话中的能够唯一确认数据的回填值集合
		var dialogValue = $("#"+name).val();
		//回填值对应的自定义对话框key
		var dialogKeyName ;
		if(dialogValue!=undefined&&dialogValue!=null){
			var keyName = $("#"+name)[0].name;
			var keyTarget = keyName.split(":")[2];
			for(var i=0;i<fields.length;i++){
				if(fields[i] == undefined){
					continue;
				}
				var json=fields[i];
				var src=json.src;
				var targets=json.target.split(',');
				for(var j=0;j<targets.length;j++){
					if(keyTarget==targets[j]){
						dialogKeyName = src;
					}
				}
			}
		}
		
		var dialogValue = $("#"+name).val();
		//var paramsValueString = "";
		var paramsValueString = name+"="+dialogValue+"&"+"dialogKeyName="+dialogKeyName+ "&" ;
		
		var queryArr = json.query;
		var isMain,preSelector,isReturn=false ;
		if(!queryArr==false && queryArr.length>0){
			for(var i=0;i<queryArr.length;i++){
				isMain = queryArr[i].isMain ;
				if(isMain=="true"){
					preSelector = ".formTable" ;
				}else{
					preSelector = "div[type='subtable']" ;
				}
				
				//解析获取对应的数据
				$(preSelector+" :hidden[name$=':"+queryArr[i].id+"ID']").each(function(){
					if($(this).val()!=""){
						paramsValueString += queryArr[i].name + "=" + $(this).val() +"&" ;
						return false ;
					}
				});
				if(paramsValueString.indexOf(queryArr[i].name)<0){
					$(preSelector+" [name$=':"+queryArr[i].id+"']").each(function(){
						var self = $(this) ;
						if(self.val()!=""){
							paramsValueString += queryArr[i].name + "=" + $(this).val() +"&" ;
							return false ;
						}else{
							var selfClass = self.attr("class") ;
							if(!selfClass==false){
								if(selfClass.indexOf("validError")>=0){
									isReturn = true ;
									$.ligerDialog.warn('请填写好--'+self.attr("lablename"),'提示');
									return false ;
								}
							}
						}
					});
				}
				if(isReturn) return false ;
			}
		}
		//加入rpc远程接口参数
		if(rpcrefname!=undefined && rpcrefname.length>0){
			paramsValueString += "rpcrefname=" +rpcrefname +"&" ;
		}
		
		if(dialogtype==undefined||dialogtype!='1'){//判断是否为弹出框的形式
			//弹出自定义对话框
			CommonDialog(name,function(data){
				try{
					var len=data.length;
					//定义数组，用于判断是否要将多个返回值赋值给一个对象。
					var targetArrays = [];
					for(var i=0;i<fields.length;i++){
						if(fields[i] == undefined){
							continue;
						}
						var json=fields[i];
						var src=json.src;
						var targets=json.target.split(','),target;
						while(target=targets.pop()){
							if(!target)return;
							var filter="[name$=':"+target+"']";
							//在子表中选择
							var targetObj=isGlobal?$(filter):$(filter,parentObj);
							//单选
							if(len==undefined){
								//判断target是否在数组中,即判断是否要将多个返回值赋值给一个对象。
								var inArray = $.inArray(target,targetArrays); 
								if(inArray>-1){
									var splitPart = targetObj.val()==''?'':'-';
									targetObj.val(targetObj.val() + splitPart + data[src]);
								}else{
									targetObj.val(data[src]);
								}
								targetObj.trigger("change");
							}else{//多选
								for(var k=0;k<len;k++){
									var dataJson=data[k];
									if(json.data){
										json.data.push(dataJson[src]);
									}
									else{
										var tmp=[];
										tmp.push(dataJson[src]);
										json.data=tmp;
									}
								}
								targetObj.val(json.data.join(","));
								targetObj.trigger("change");
							}
							//把target加入到数组中。用于判断是否要将多个返回值赋值给一个对象。
							targetArrays.push(target);
						}
					}
				}finally{
					//定制：应用于：不在当前表建冗余字段的情况，将关联表中的字段数据在当前页面显示。通过resultBean['F_字段名'];方式获取值。
					var otherRelShowColumn = data;
					FormFKShowUtil.fillOtherRelShowColumn(otherRelShowColumn);
				}

			},paramsValueString);
		}
	});
};


/**
 * 初始化统计函数事件绑定
 */
FormUtil.InitMathfunction = function(t){
	if(t){
		$("input",t).trigger("change");
	}
	else{
		$("input").live('change',FormMath.doMath);
	}
};

/**
 * 获取目标控件的值是否为指定值
 */
FormUtil.getTargetVal = function(t,obj){
	//
	var type = obj.attr("type");
	var nodekey = obj.attr("nodekey");
	 if(obj.is("select")){ //下拉框
		var val = obj.find('option:selected').val();
		if(val==t.value)
			return true;
		return false;
	 }else if(type == "radio" || type =="checkbox"){//单选框，多选框
		 var temp = false;
		 obj.each(function(){
			 var me = $(this),
			 	 state = me.attr("checked"),
			 	 val = me.val();
			 
			 if(state&&t.value.indexOf(val)>-1)
				 temp = true;
		 });
		 return temp;
	 }else if(nodekey!=undefined&&nodekey!=null&&nodekey!=""){//数据字典
		 var result = false;
		 var url = __ctx + '/oa/flow/task/getDicValue.do';
		 $.ajax({
		    type:"get",
		    async:false,
		    url:url,
		    data:{
		    	nodeKey:nodekey,
		    	dicName:obj.val()
		    },
		    success:function(data){
		    	if(data==t.value)
		    		result=true;
		    }
		 });
		 return result;
	 }else{
		 if($(obj).val()==t.value){
			 return true;
		 }
		return false;
	 }
};

/**
 * 获取是否所有的选择框字段都是指定值
 */
FormUtil.validHasChange = function(chooseField){
	//
	var result = true;
	for(var i=0,c;c=chooseField[i++];){
		var obj = FormUtil.getTargetObj(c.key);
		if(!FormUtil.getTargetVal(c,obj))
			result = false;
	}
	return result;
};

/**
 * 进行变更字段和表的变更
 * @param changes
 */
FormUtil.doChange = function(choiceKey,changes){
	for(var i=0,c;c=changes[i++];){
		//对表进行变更
		if(!$.isEmpty(c.fieldtype) && c.fieldtype == '1')	
			FormUtil.doChangeTable(choiceKey,c);
		else//对字段进行变更
			FormUtil.doChangeField(choiceKey,c);
	}
};

/**
 * 变换预处理
 */
FormUtil.preChange = function(choice,changes,t){
	//
	var choiceKey = [],
		trace = $changeObj.data("formtrace");
	for(var i=0,c;c=choice[i++];){
		choiceKey.push(c.key);
		if(trace){
			for(var j=0,n;n=changes[j++];){
				var traceCall = trace[n.key+'_'+n.type];
				if(traceCall){
					var me = null;
					if(!$.isEmpty(n.fieldtype) && n.fieldtype == '1')
						me = $("div[type='subtable'][tablename='"+n.key+"']");
					else{
						me = FormUtil.getTargetObj(n.key);
					}
					traceCall.call(me,n);
					me.trigger("blur");
					delete trace[n.key+'_'+n.type];
				}
			}
		}
	}
	if(t){
		$changeFunc.push({choiceKey:choiceKey,changes:changes});	
	}
};

FormUtil.isAttachment = function(obj){
	var type = obj.attr("controltype");
	if(!$.isEmpty(type) && type== 'attachment')
		return true;
	else
		return false;
}

//字段变换方法
var $fieldChange = {
        1:function(c){//隐藏
			//var self = $(this).val('');
        	var self = $(this);
			if(FormUtil.isAttachment(self)){	
				self.parent("div").hide();
			}else{

				self.hide();
				self.parents("label").hide();
				//隐藏自定义对话框按钮
				self.closest("span").siblings().hide();
				self.parents("td").hide();
				self.parents("td").hide();
				self.parents("td").prev("td").hide();
				
				//div 模板隐藏
				self.parents("div.tableNew_con").hide();

				
			}
		}
        ,
        2:function(c){//非隐藏
			var self = $(this);		
			if(FormUtil.isAttachment(self)){	
				self.parent("div").show();
			}else{
				self.show();
				self.parents("label").show();
				//隐藏自定义对话框按钮
				self.closest("span").siblings().show();
				self.parents("td").show();
				self.parents("td").show();
				self.parents("td").prev("td").show();
				
				//div 模板隐藏
				self.parents("div.tableNew_con").show();

			}
		}
        ,
        3:function(c){//只读
			var self = $(this);
			if(self.is("a"))//对选择器的特殊处理
				 return;
			if(FormUtil.isAttachment(self)){	
				self.siblings("a[field='"+c.key+"']").hide();
				self.siblings(".attachement").find("a.cancel").each(function(){
					$(this).hide();
				});
			}else{	
				self.attr("disabled",true);
			}
		}
        ,
        4:function(c){//非只读
			var self = $(this);
			if(self.is("a"))//对选择器的特殊处理
				 return;
			if(FormUtil.isAttachment(self)){	
				self.siblings("a[field='"+c.key+"']").show();
				self.siblings(".attachement").find("a.cancel").each(function(){
					$(this).show();
				});
			}else{	
				self.removeAttr("disabled");
			}
		}
        ,
        5:function(c){//必填					
			var self = $(this),
				validate = self.attr("validate");
			if(self.is("a"))//对选择器的特殊处理
				 return;
			if(FormUtil.isAttachment(self)){		
				self.siblings("a.selectFile").addClass("validError");
				self.parent().attr('right','b');
			}else{
				self.addClass("validError");
				if(!validate){
					self.attr("validate","{required:true}");						
				}
				else{
					validate = eval("("+validate+")");
					if(!validate.required){
						validate.required = true;
						self.attr("validate",JSON2.stringify(validate));
					}
				}
				if(!$.isEmpty(self.val()))
					self.removeClass("validError");
				else{
					if(self.hasClass("Wdate")){
						self.css("border","1px solid red");
					}
				}
			}
		}
        ,
        6:function(c){//非必填
			var self = $(this),
				validate = self.attr("validate");
			if(self.is("a"))//对选择器的特殊处理
				 return;
			if(FormUtil.isAttachment(self)){
				self.siblings("a.selectFile").removeClass("validError")
				self.parent().attr('right','w');
			}else{
				if(validate){
					validate = eval("("+validate+")");
					if(validate.required){
						delete validate.required;
						self.attr("validate",JSON2.stringify(validate));
					}
					//删除样式
					self.removeClass("validError");
					if(self.hasClass("Wdate")){
						self.css("border","1px solid #ccc");
					}
				}
			}
		}
        ,
        7:function(c){//置空
			var self = $(this);
			if(self.is("a"))//对选择器的特殊处理
				 return;
			if(FormUtil.isAttachment(self))
				self.siblings(".attachement").html('');
			if(self.attr("type")=="checkbox"||self.attr("type")=="radio")
				self.removeAttr("checked");
			$(this).val('');
		}
	};

/**
 * 获取关联的需要变动的字段
 */
FormUtil.getTargetObj = function(filter){
	if(!$changeObj){
		return $("[name='"+filter+"']");
	}
	var formtype = $changeObj.parents("[formtype]"),
		me = null;
	if(formtype&&formtype.length>0){
		me = $("[name='"+filter+"']",formtype);
	}
	else{
		me = $("[name='"+filter+"']");
	}
	return me;
};

/**
 *  字段变换
 * @param {} changeField
 */
FormUtil.doChangeField = function(choiceKey,c){
	//
	var me = FormUtil.getTargetObj(c.key);
	if(!me||me.length==0)return;
	var changeType = c.type,
		traceCall = null;
	
	if(changeType!='8'){
		var changeFun = $fieldChange[changeType];
		if(changeType%2){
			traceCall = $fieldChange[++changeType];
		}
		else{
			traceCall = $fieldChange[--changeType];
		}
		me.each(function(){
			changeFun.call(this,c);
		});
	}
	else{
		var	reduceSet = c.cascade.reduce,
			oldHtml = '';
			
		me.each(function(){
			var self = $(this),
				myType = self.attr('type');
			if($(this).is("a"))//对选择器的特殊处理
				 return;
			if(myType=='radio'||myType=='checkbox'){
				var spanPar = self.parents("span");
				for(var i=0,c;c=reduceSet[i++];){
					if(self.val()==c.id){
						spanPar.remove();
					}
				}
			}
			else{//下拉框
				isSelect = true;
				oldHtml = self.html();
				var options = self.find("option");			
				for(var i=0,c;c=reduceSet[i++];){
					for(var j=0,n;n=options[j++];){
						var t = $(n);
						if(!t.val())continue;
						if(t.val()==c.id){
							t.remove();
						}
					}
				}
			}
		});
		FormUtil.putInFormTrace(choiceKey,"tracedata",c.key,oldHtml);
		traceCall = function(c){
			var oldHtmlObj = $changeObj.data("tracedata");
			if(oldHtmlObj){
				oldHtml = oldHtmlObj[c.key];
				$(this).each(function(){
					var curValue = $(this).val();
					$(this).html(oldHtml).val(curValue).trigger("change");
				});
				delete oldHtmlObj[c.key];
			}
		};
	}
	FormUtil.putInFormTrace(choiceKey,"formtrace",c.key+'_'+c.type,traceCall);
	me.trigger("blur");
};

/**
 * 将变动轨迹放入控件的缓存数据中
 */
FormUtil.putInFormTrace = function(choiceKey,datakey,key,value){
	for(var j=0,n;n=choiceKey[j++];){
		var changeObj = FormUtil.getTargetObj(n);
		if(!changeObj)continue;
		var formTrace = changeObj.data(datakey);
		if(!formTrace){
			formTrace = {};
			formTrace[key] = value;
			changeObj.data(datakey,formTrace);
		}
		else{
			formTrace[key] = value;
		}
	}
};

//表表换方法集合
var $tableChange = {
		1:function(){//隐藏
			var self = $(this);
			self.hide();
		},
		2:function(){//非隐藏
			var self = $(this);
			self.show();
		},
		3:function(){//只读(不可以添加)
			var self = $(this);
			$('a.add',self).hide();
			self.find('[formType]').prev().unbind('contextmenu');	
			$('td',self).find('input,select,textarea').attr("disabled","disabled");
			$('td',self).find('a').hide();
		},
		4:function(){//非只读(可以添加)
			var self = $(this);
			$('a.add',self).show();	
			$('td',self).find('input,select,textarea').removeAttr("disabled","disabled");
			$('td',self).find('a').show();
		},
		5:function(){//必填
			var self = $(this);
			var y =self.attr('right');
			self.attr('right','b');
			self.attr("yright",y);
		},
		6:function(){//非必填
			var self = $(this);
			var y =	self.attr("yright");
			self.attr('right',y?'w':y);
		},
		7:function(){//置空
			var self = $(this);
			$(":text,select",self).each(function(){
				 $(this).val('');
			});
			$("textarea",self).each(function(){
				var s= 	$(this);
				//处理附件
				if(FormUtil.isAttachment(s))
					s.siblings(".attachement").html('');
				s.val('');
			});
			$("input[type='radio']",self).each(function(){
				 $(this).removeAttr("checked");
			});
			$("input[type='checkbox']",self).each(function(){
				 $(this).removeAttr("checked");
			});
		}
};

/**
 * 表变换
 * @param {} c
 */
FormUtil.doChangeTable = function(choiceKey,c){	
	var me = $("div[type='subtable'][tablename='"+c.key+"']"),
		changeType = c.type;
	
	var changeFun = $tableChange[changeType];
	if(changeType%2){
		traceCall = $tableChange[++changeType];
	}
	else{
		traceCall = $tableChange[--changeType];
	}
	me.each(function(){
		changeFun.call(this,c);
	});
	FormUtil.putInFormTrace(choiceKey,"formtrace",c.key+'_'+c.type,traceCall);
};

var $changeObj = null,
	$changeFunc = [],
	$changeNoChoise = [];

FormUtil.ChangeNoChoise = function(){
	FormUtil.doChange([],$changeNoChoise);
};

/**
 * 联动设置的初始化
 */
FormUtil.InitGangedSet = function(d){
	//
	$changeNoChoise = [];
	for(var i=0,c;c=d[i++];){
		var choisefield = eval("("+c.choisefield+")"),
			changefield = eval("("+c.changefield+")");
		
		if(choisefield&&choisefield.length==0){
			$changeNoChoise = $changeNoChoise.concat(changefield);
		}
		
		for(var j=0,m;m=choisefield[j++];){
			var changeObj = $("*[name='"+m.key+"']");
			changeObj.die("change").live("change",function(e){
				$changeFunc = [];
				$changeObj = $(e.target);
				var	key = $changeObj.attr("name");
				//当一个select控件绑定了多次事件时，需要多次验证这个控件的所选值
				//比如 性别这个字段，在联动中设置了选择'男'时隐藏一个字段，选择'女'时 显示一个字段，此时需要2次验证
				if(!key)return;
				for(var x=0,n;n=d[x++];){
					if(n.choisefield.indexOf(key)>-1){
						var curChoise = eval("("+n.choisefield+")"),
							r = FormUtil.validHasChange(curChoise);
						FormUtil.preChange(curChoise,eval("("+n.changefield+")"),r);
					}
				}
				//延迟处理：等待该变换字段的所有还原方法调用完以后再进行变换
				if($changeFunc.length>0){
					for(var y=0,o;o=$changeFunc[y++];){
						FormUtil.doChange(o.choiceKey,o.changes);
					}
				}
			});
			changeObj.trigger("change");
		}
	}
	FormUtil.ChangeNoChoise();
};

/**
 * 新增子表记录时，触发联动字段change，使新增记录保持合适的联动状态
 */
FormUtil.triggerChoice = function(newRow){
	$("[name]",newRow).each(function(){
		var me = $(this),
			name = me.attr("name"),
			choiceName = FormUtil.findWithChange(name);
		
		if(choiceName){
			var choiceObj = $("[name='"+choiceName+"']"),
				val = choiceObj.val(); 
			choiceObj.trigger("change");
		}
	});
	
};

/**
 * 遍历联动设置记录，查找 应该触发哪个字段
 */
FormUtil.findWithChange = function(name){
	if(typeof bpmGangedSets == 'undefined')return null;
	for(var i=0,c;c=bpmGangedSets[i++];){
		var choisefield = eval("("+c.choisefield+")"),
			changefield = eval("("+c.changefield+")");
		
		if(choisefield&&choisefield.length==0){
			return null;
		}
		for(var j=0,m;m=changefield[j++];){
			if(m.key==name){
				return choisefield[0]["key"];
			}
		}
	}
};

/**
 * 绑定"自定义查询"。 按钮或者文本框定义如下：
 * query="{name:'globalType',evt:'click',fields:[{src:'TYPENAME',target:'m:mainTable:name'},{src:'TYPEID',target:'m:mainTable:address'}]}"
 * 
 * name:自定义查询的别名 fields：为字段映射，可以有多个映射。 src：对话框返回的字段。 target：需要映射的控件名称。
 */
FormUtil.initCommonQuery=function(){
	$("[query]").live("keyup change", function(event){
		var obj = $(this),
			queryJson=obj.attr("query"),
			json = eval("("+queryJson+")"),
			evt = json.evt,
			next = obj.next();
		if(evt.key!=event.type){
			return;
		}
		if("carriage_return" == evt.name && event.keyCode != evt.code){
			return;
		}
		var obj=$(this);
		var queryJson=obj.attr("query");
		var json = eval("("+queryJson+")");
		var name = json.name;
		var cond = json.cond;
		
		
		var value = obj.val();
		if(next&&next.length > 0){
			//如果是 选择器类型字段 则获取 选择器的ID字段值 作为查询条件
			if(next.hasClass("link")&&next[0].tagName == "A"){
				value = $("input[name='"+obj.attr("name")+"ID']").val();
			}
		}
		if(!value){
			return;
		}
		var fields=json.fields;
		var parentObj=obj.closest("[formtype]");
		var isGlobal=parentObj.length==0;
		var querydataStr = "{"+cond+":\""+value+"\"}";
	    var  queryCond = {alias:name,querydata:querydataStr,page:1,pagesize:10};
		DoQuery(queryCond,function(data){
			if(data.errors){
				return;
			}
			for(var i=0;i<fields.length;i++){
				var json=fields[i];
				var src=json.src;
				var targets=json.target;
				var target;
				while(target=targets.pop()){
					if(!target)return;
					var filter="[name$=':"+target+"']";
					//在子表中选择
					var targetObj=isGlobal?$(filter):$(filter,parentObj);
					if(data.list.length<1){
						targetObj.val("");
						continue;
					}
					var dataArr = [] ;
					for(var k=0;k<data.list.length;k++){
						var dataJson=data.list[k];
						if(dataArr){
							dataArr.push(dataJson[src.toLowerCase()]);
						}
						else{
							var tmp=[];
							tmp.push(dataJson[src.toLowerCase()]);
							dataArr=tmp;
						}
					}
					targetObj.val(dataArr.join(","));
					targetObj.trigger("change").trigger("blur");
				}
			}				
		});
	}).trigger("change");
};
//绑定外部链接link TODO没用过
FormUtil.initExtLink = function(){
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
			
			DialogUtil.open({
				height:700,
				width: 900,
				title : '',
				url: url, 
				isResize: true
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
		case 20:// 流程明细
			var runId = $(this).attr("refid");
			var url=__ctx+"/oa/flow/processRun/info.do?isOpenDialog=1&link=1&runId="+runId;
			DialogUtil.open({
				height:700,
				width: 900,
				title : '流程明细',
				url: url, 
				isResize: true
			});	
			break;
		}
	});
};
//显示审批历史
function opinionHistory(actInstId,nodeId){
	var url=__ctx+"/oa/flow/taskOpinion/getNodeList.do?actInstId="+actInstId+"&nodeId="+nodeId;
	url=url.getNewUrl();
	DialogUtil.open({
		url:url,
		title:'审批历史',
		height:'600',
		width:'800',
		arg:"taskOpinion"
	});
}