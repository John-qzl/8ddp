
/**
 *明细多Tab构造方法
 */
var DataTemplateMultiTabEdit = function(){}
// 属性及函数
DataTemplateMultiTabEdit.prototype = {
	/**
	 * 初始化
	 */
	init : function() {
		var _self = this;
		this.initRecRightField();	
	},
	// =====================表单权限设置（初始化 关联表单类型、关联字段、绑定事件）==============================================================================
	initRecRightField : function() {
		$('#recTypeTd').append(this.addRecTypeSelect('recTypes'));
		$('#relationColumnTd').append(this.addColSelect('fieldList'));
		var recRightFieldVal = $('#recRightField').val();
		if ($.isEmpty(recRightFieldVal))
			return;		
		var recRightField = $.parseJSON(recRightFieldVal);
		$("input[name=openType][value="+recRightField.openType+"]").attr('checked','checked');
		$("input[name=relType][value="+recRightField.type+"]").attr('checked','checked');
		$("#recTypeTd [var=recTypes] [value="+recRightField.recType+"]").attr('selected','selected');
		$("#relationColumnTd [var=relationColumn] [value="+recRightField.ckey+"]").attr('selected','selected');
		
		$('#relationColumnSettingTable tbody').children().remove();
		var sb = new StringBuffer();
		var relation = recRightField.relation;
		if($.isEmpty(relation))
			return;
		for(var i = 0;i<relation.length;i++){
			var r =  relation[i];
			sb.append('<tr>');
			sb.append('<td>'+(i+1)+'</td>');
			sb.append('<td><input id="'+r.colKey+'" value="'+r.colVal+'" /></td>');
			sb.append('<td>'+this.addRecTypeSelect('recTypes',r.recType)+'</td>');
			sb.append('</tr>');
		}
		$('#relationColumnSettingTable tbody').append(sb.toString());
		
		if(recRightField.type=='table'){
			this.setTabRelation();
		}else{
			this.setColRelation();
		}
	},
	addRecTypeSelect : function(source,val){
		var recTypesVal = $('#'+source).val();
		if ($.isEmpty(recTypesVal))
			return;
		var recTypes = $.parseJSON(recTypesVal);
		var sb = new StringBuffer();
		sb.append('<select var="recTypes" >');
		for(var i = 0;i<recTypes.length;i++){
			var type = recTypes[i];
			var alias = type.alias;
			var typeName = type.typeName;
			if(val==alias){
				sb.append('<option value="'+alias+'" selected="selected">'+typeName+'</option>');
			}else{
				sb.append('<option value="'+alias+'">'+typeName+'</option>');
			}
		}
		if(recTypes.length<1){
			sb.append('<option value="">没有数据</option>');
		}
		sb.append('</select>');
		return sb.toString();
	},
	addColSelect : function(source){
		var  fieldListVal =  $('#'+source).val();
		if ($.isEmpty(fieldListVal))
			return;
		var fieldList = $.parseJSON(fieldListVal);
		var sb = new StringBuffer();
		sb.append('<select var="relationColumn" onchange="__MultiTab__.initRCSet(this)">');
		var j = 0;
		for(var i = 0;i<fieldList.length;i++){
			var field = fieldList[i];
			var desc = field.fieldDesc;
			var options = field.options;
			var name = field.fieldName;
			if(options!=''){
				if(j==0){
					this.initOptions(options);
				}
				j++;
				sb.append('<option value="'+name+'" ops="'+options.replaceAll("\"","\'")+'">'+desc+'</option>');
			}
		}
		sb.append('</select>');
		return sb.toString();
	},
	initRCSet : function(obj){
		$('#relationColumnSettingTable tbody').children().remove();
		var sb = new StringBuffer();
		var ops = $(obj.selectedOptions).attr('ops');
		var opsObj = $.parseJSON(ops.replaceAll("\'","\""));
		for(var i = 0;i<opsObj.length;i++){
			var opObj =  opsObj[i];
			sb.append('<tr>');
			sb.append('<td>'+(i+1)+'</td>');
			sb.append('<td><input id="'+opObj.key+'" value="'+opObj.value+'" /></td>');
			sb.append('<td>'+this.addRecTypeSelect('recTypes')+'</td>');
			sb.append('</tr>');
		}
		$('#relationColumnSettingTable tbody').append(sb.toString());
	},
	initOptions : function(options){
		var opsObj = $.parseJSON(options);
		var sb = new StringBuffer();
		for(var i = 0;i<opsObj.length;i++){
			var opObj =  opsObj[i];
			sb.append('<tr>');
			sb.append('<td>'+(i+1)+'</td>');
			sb.append('<td><input id="'+opObj.key+'" value="'+opObj.value+'" /></td>');
			sb.append('<td>'+this.addRecTypeSelect('recTypes')+'</td>');
			sb.append('</tr>');
		}
		$('#relationColumnSettingTable tbody').append(sb.toString());
	},
	setTabRelation : function(){
		$('#recTypeTr').show();
		$('#relationColumnTr').hide();
		$('#relationColumnSettingTable').hide();
	},
	setColRelation : function(){
		$('#recTypeTr').hide();
		$('#relationColumnTr').show();
		$('#relationColumnSettingTable').show();
	},
	//获取表单权限字段
	getRecRightField : function(){
		var type =  $("input[name='relType']:checked").val();
		var openType =  $("input[name='openType']:checked").val();
		var field={};
		field.type = type;
		field.openType = openType;
		if(type=='table'){
			var tkey = $('#relationTableTd').text().trim();
			var recType = $('#recTypeTd [var=recTypes] :checked').val();
			field.tkey = tkey;
			field.recType = recType;
		}else{
			var relation = [];
			var tkey = $('#relationTableTd').text().trim();
			var ckey = $('#relationColumnTd [var=relationColumn] :checked').val();
			$('table#relationColumnSettingTable tbody tr').each(function(i,e){
				var obj ={};
				var colKey =  $(e).find('td input').attr('id');
				var colVal =  $(e).find('td input').val();
				var recType = $(e).find('td select :checked').val();
				obj.colKey = colKey;
				obj.colVal = colVal;
				obj.recType = recType;
				relation.push(obj);
			});
			field.tkey = tkey;
			field.ckey = ckey;
			field.relation = relation;
		}
		return field;
	}
};

var __MultiTab__ = new DataTemplateMultiTabEdit();// 默认生成一个对象

