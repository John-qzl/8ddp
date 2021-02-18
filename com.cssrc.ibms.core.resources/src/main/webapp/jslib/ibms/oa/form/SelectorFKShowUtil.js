
if (typeof SelectorFKShowUtil == 'undefined') {
	SelectorFKShowUtil = {};
}


$(function(){
	//页面加载时,初始化
	handFormFKColumnShow();
});
/**
 * 根据列表页面，"自定义查询"的配置，读取数据库的外键信息。
 */
SelectorFKShowUtil.initFKColumnShow=function(){
	$("body").delegate("[dialog]", "click", function(){
		var obj=$(this);
		var dialogJson=obj.attr("dialog");
		//去掉字符串中的"<![CDATA[]]"
		dialogJson = dialogJson.stripCData();
		var json=eval("("+dialogJson+")" );
		var name=json.name;
		var rpcrefname=json.rpcrefname;//需要调用的rpc远程接口名称
		//
		var fields=json.fields;
		var parentObj=obj.closest("[formtype]");
		var isGlobal=parentObj.length==0;
		var paramsValueString = "" ;
		var queryArr = json.query;
		var isMain,preSelector,isReturn=false ;
		//加入rpc远程接口参数
		if(rpcrefname!=undefined && rpcrefname.length>0){
			paramsValueString += "rpcrefname=" +rpcrefname +"&" ;
		}
		//绑定自定义查询框
		CommonDialog(name,function(data){
			try{
			//
			var len=data.length;
			//定义数组，用于判断是否要将多个返回值赋值给一个对象。
			var targetArrays = [];
			var targetFieldArrays = [];
			for(var i=0;i<fields.length;i++){
				if(fields[i] == undefined){continue;}
				var json=fields[i];
				var src=json.src;
				var targets=json.target.split(','),target;
				
				
				while(target=targets.pop()){
					var src=json.src;
					if(!target)return;
					/*
					 * 以下通过js对赋值进行重构，代码片段很重要，不要修改，物理字段通过以下方式找找对应的target回填值
					 * */
					if(target.indexOf('XXXXXFKColumnShow')<0&&target!=obj.attr('kfname')&&json.listShow){
						//物理字段。查询的话可以直接使用 虚拟方式查询
						target=obj.attr('kfname')+'XXXXXFKColumnShow';
						targetFieldArrays.push(json.target);
					}else if($.inArray(json.target,targetFieldArrays)>-1){
						target=obj.attr('kfname')+'XXXXXFKColumnShow';
					}else if(target!=obj.attr('kfname')&&target.indexOf('XXXXXFKColumnShow')<0){
						target=null;
					}
					//
					var filter="input[name='"+target+"']";
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
						//
						//外键显示值赋值
						var parent=targetObj.parentElement;
						var queryFilter="[name^='Q_"+target+"_']";
						var fkColumnShowEle=$(queryFilter,parent);
						fkColumnShowEle.val(data[src]);
					}else{
						//多选
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
				//定制：应用于：不在当前表建冗余字段的情况，将关联表中的字段数据在当前页面显示。通过otherRelShowColumn['F_字段名'];方式获取值。
				/*本处用于列表页面的查询框的回填方法，故      "不需要"  执行如下代码。
				  var otherRelShowColumn = data;
				  FormFKShowUtil.fillOtherRelShowColumn(otherRelShowColumn);
				*/
			}
		
		},paramsValueString);
	});
};
//外键显示值"重置"按钮
function handFormFKColumnShow(){

$("body").delegate("a.link.resetFKShow", "click",function(){
	var obj = $(this);
	var fieldName=obj.attr("name");
	var parent=obj.parent();
	var idFilter="input[name='"+fieldName+"']";
	var queryFilter="[name^='Q_"+fieldName+"_']";
	var nameFilter="input[name='"+fieldName+"XXXXXFKColumnShow']";
	var inputId=$(idFilter,parent);
	var query=$(queryFilter,parent);
	var inputName=$(nameFilter,parent);
	inputId.val("");//清空hidden外键id值
	query.val("");//清空hidden查询外键id值
	inputName.val("");//清空外键显示值
  });
};
