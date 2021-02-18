//新增更新
var RecordService = function(records,sucCall){
	this.records = records;
	this.addData = function(record){
		if($.isEmpty(this.records)){
			this.records = [];
		}
		this.records.push(record);
	}
	this.sucCall = sucCall;
}
/**
 * tableName:表名
 * pk：记录主键
 * keyValue ：{filedName：newValue,...}
 */
var IbmsRecord = function(tableName,pk,keyValue,keyType){
	this.tableName = tableName;
	this.pk = pk;
	this.keyValue = keyValue;
	this.keyType = keyType;
}

/**
 * 更新数据
 */
RecordService.prototype.update=function(){
	var me = this;
	var url = __ctx + "/oa/form/formHandler/updateCustomRecord.do"
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{
			  ibmsRecords : JSON2.stringify(me.records)
		  },
		  dataType : "text",
	      async:false,
	      success:function(responseText){
		  		var obj = new com.ibms.form.ResultMessage(responseText);
				if (obj.isSuccess()) {
					if(me.sucCall){
			    		  me.sucCall.apply(obj.getMessage())
			    	}					
				} else {
					$.ligerDialog.err("提示信息","",obj.getMessage());
				}
	      }   		  
	});
}
RecordService.prototype.add =function(){
	var me = this;
	var url = __ctx + "/oa/form/formHandler/addCustomRecord.do"
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{
			  ibmsRecords : JSON2.stringify(me.records)
		  },
		  dataType : "text",
	      async:false,
	      success:function(responseText){
		  		var obj = new com.ibms.form.ResultMessage(responseText);
				if (obj.isSuccess()) {
					if(me.sucCall){
			    		  me.sucCall.apply(obj.getMessage())
			    	}					
				} else {
					$.ligerDialog.err("提示信息","",obj.getMessage());
				}
	      }   		  
	});
}