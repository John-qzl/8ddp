var conf ={};
// 字符串-数据字典
var charDicField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=varchar] [title=varchar-3]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	this.dictType = lvfObj.dictType;	
	this.dictOptions = lvfObj.dictOptions;
		
	this.createHtml = function(){
		var html = "";
		var dic = this.dictOptions;
		if("#"+obj.lvform+" div[title=varchar]"){
			html +='<div title="varchar" ></div>';
		}
		html +='<ul class="row" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_multi">';
		for(var i=0;i<dic.length;i++){
			var length = dic[i].key.length;
			if(length>10){
				html +='  <li style="display: list-item;width:100%">';	
			}else{
				html +='  <li style="display: list-item;width:50%">';	
			}
			html+='<input type="checkbox"';
			html+='var="'+dic[i].key+'"';			
			//初始时全部选中
			html+=' checked="checked"';
			html+='>';
			html+=dic[i].value;
			html +='  </li>';
		}
		html +='</ul>';
		return html;
	};
}
// 字符串-下拉选项、密级控件、 
var charEnumField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=varchar] [title=varchar-enum]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	
	this.options = lvfObj.options;		
	this.selected = lvfObj.selected;
	this.createHtml = function(){
		var html = "";
		var opt = this.options;
		html +='<ul class="row" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_multi">';
		for(var i=0;i<opt.length;i++){
			var length = opt[i].value.length;
			if(length>10){
				html +='  <li style="display: list-item;width:100%">';
			}else{
				html +='  <li style="display: list-item;width:50%">';
			}
			html+='<input type="checkbox"';
			html+='var="'+opt[i].key+'"';
			//初始时全部选中
			html+='checked="checked"';
			html+='>';
			html+=opt[i].value;
			html +='  </li>';
		}
		html +='</ul>';
		return html;
	};
}
// 字符串-其他
var charOtherField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=varchar] [title=varchar-other]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	
	this.searchValue = $.isEmpty(lvfObj.searchValue)?"":lvfObj.searchValue;
	this.createHtml = function(){
		var html = "";
		html +='<ul class="row" style="display: inline-block;margin:10px auto" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_lr">';
		html +='  <li style="display: list-item; width:80%">';
		html+=this.fieldDesc+':';
		html+='<input type="text" class="inputText" value="'+this.searchValue+'">';
		html +='  </li>';
		html +='</ul>';
		return html;
	};
}
// 数字
var numberField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=number]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	
	this.startNum = $.isEmpty(lvfObj.startNum)?"":lvfObj.startNum;
	this.endNum =   $.isEmpty(lvfObj.endNum)?"":lvfObj.endNum;
	
	this.createHtml = function(){
		var html = "";
		html +='<ul class="row" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_numberRange">';
		html +='  <li style="display: list-item;width:80%;">';
		html+='从:';
		html+='<input style="margin-bottom:10px" type="text" name="start" class="inputText" value="'+this.startNum+'">';
		html+='<br/>到:';
		html+='<input type="text" name="end" class="inputText" value="'+this.endNum+'">';
		html +='  </li>';
		html +='</ul>';
		return html;
	};
}
// 日期
var dateField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=date]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	this.format = lvfObj.format;
	
	this.startTime =  $.isEmpty(lvfObj.startTime)?"":lvfObj.startTime;
	this.endTime =  $.isEmpty(lvfObj.endTime)?"":lvfObj.endTime;
	this.createHtml = function(){
		var html = "";
		html +='<ul class="row" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_dateRange">';
		html +='  <li style="display: list-item;width:80%;">';
		html +='从:';
		html +='    <input type="text" name="start" readonly="readonly" datefmt="'+this.format+'" class="wdateTime inputText" value="'+this.startTime+'"> ';
		html +='  </li>';
		html +='  <li style="display: list-item;width:80%">';
		html +='到:';
		html +='    <input type="text" name="end" readonly="readonly" datefmt="'+this.format+'" class="wdateTime inputText" value="'+this.endTime+'"> ';
		html +='  </li>';
		html +='</ul>';
		return html;
	};
}
// 大文本
var clobField = function(lvfObj){
	this.selector = "#"+lvfObj.lvform+" div[title=clob]";
	this.fieldName = lvfObj.fieldName;
	this.fieldDesc = lvfObj.fieldDesc;
	
	this.createHtml = function(){
		var html = "";
		html +='<ul class="row" name="'+this.fieldName+'" var="LVF_'+this.fieldName+'_lr">';
		html +='  <li style="display: list-item; width:80%">';
		html +=this.fieldDesc+':';
		html+='<input type="text"  class="inputText" value="">';
		html +='  </li>';
		html +='</ul>';
		return html;
	};
}


/**------------------------------初始化-------------------------------------**/

function initLineValueFilterHtml(lvform){
	if($("#"+lvform).length==0){
		return;
	}
	$("#"+lvform).html('<div id="formCon" style="max-height:200px;overflow:hidden;overflow-y:auto;"></div>');
	
	
	
	var charDiv = $("#"+obj.lvform+" div[title=varchar]");
	if(charDiv.length==0){
		var vahrHtml ='';
		vahrHtml += '<div title="varchar">';
		vahrHtml += '<div title="varchar-3"></div>';
		vahrHtml += '<div title="varchar-enum"></div>';
		vahrHtml += '<div title="varchar-other"></div>';
		vahrHtml += '</div>';
		$("#formCon").append(vahrHtml);
	}
	var numberDiv = $("#"+obj.lvform+" div[title=number]");
	if(charDiv.length==0){
		var numberHtml ='';
		numberHtml += '<div title="number"></div>';
		$("#formCon").append(numberHtml);
	}
	var dateDiv = $("#"+obj.lvform+" div[title=date]");
	if(dateDiv.length==0){
		var dateHtml ='';
		dateHtml += '<div title="date"></div>';
		$("#formCon").append(dateHtml);
	}
	var clobDiv = $("#"+obj.lvform+" div[title=clob]");
	if(clobDiv.length==0){
		var clobHtml ='';
		clobHtml += '<div title="clob"></div>';
		$("#formCon").append(clobHtml);
	}
	var buttonHtml = "";
	buttonHtml+='<div class="panel-toolbar table-btn"><div class="toolBar">' 
			  + '<div class="group">'
			  +	'  <a class="link save table-btn-save">过滤</a></div>'
			  + '<div class="group">'
			  + '  <a class="link del table-btn-del">取消</a></div></div></div>';
	$("#"+lvform).append(buttonHtml);
}
function appendFieldHtml(lvfObj){
	var obj;
	var fieldType = lvfObj.fieldType;
	var controlType = lvfObj.controlType;
	switch(fieldType){
	case "varchar":
		if(controlType==3){
			obj = new charDicField(lvfObj);
		}else if(controlType=="enum"){
			obj = new charEnumField(lvfObj);
		}else{
			obj = new charOtherField(lvfObj);
		}
		break;
	case "number":
		obj = new numberField(lvfObj);
		break;
	case "date":
		obj = new dateField(lvfObj);
		break;
	case "clob":
		obj = new clobField(lvfObj);
		break;
	}
	$(obj.selector).append(obj.createHtml());
}
/**------------------------------初始化-------------------------------------**/
function getLvfParams(lvfForm){
	var params = {};
	lvfForm.find("ul").each(function(){
		var key = $(this).attr("var");
		if(key.indexOf('LVF_')>-1){
			var type = key.substring(key.lastIndexOf('_')+1);
			switch(type){
			case "multi":
				var value=",";
				$(this).find('input:checked').each(function(i,e){
					value+=$(e).attr("var")+",";
				});
				params[key] = value;
				break;
			case "lr":
				var value=$(this).find('input').val();
				params[key] = value;
				break;
			case "numberRange":
				var sValue=$(this).find('input[name=start]').val();
				var eValue=$(this).find('input[name=end]').val();
				sValue = sValue==""?"-999999999":sValue;
				eValue = eValue==""?"999999999999":eValue;
				params[key] = sValue+","+eValue;
				break;
			case "dateRange":
				var sValue=$(this).find('input[name=start]').val();
				var eValue=$(this).find('input[name=end]').val();
				var datefmt = $(this).find('input[name=start]').attr("datefmt");
				params[key] = sValue+","+eValue+","+datefmt;
				break;
			}
		}
	});
	return params;
}
