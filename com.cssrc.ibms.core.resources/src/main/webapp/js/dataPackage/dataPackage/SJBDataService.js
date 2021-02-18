  //数据包处理保存服务类（数据包详细信息-批量新增表单定制）
  var SJBDataService = function(tableName,fieldKeys,fieldSaveKeys,sucCall,savekeyType,fieldDesc){
	this.tableName = tableName;//'W_DATAPACKAGEINFO';
    this.fieldDesc = fieldDesc;//['保密期限','岗位','密级','上传人','上传人ID','上传时间','审批进度','所属数据包','完成时间','执行状态','版本','数据类型','数据名称','说明','文件'];//字段说明，仅作为说明
    this.fieldKeys = fieldKeys;//['bmqx','gw','mj','scr','scrID','scsj','spjd','sssjb','wcsj','zxzt','bb','sjlx','sjmc','sm','wjmc'];//用于采集数据的字段key
	this.fieldSaveKeys = fieldSaveKeys;//['F_bmqx','F_gw','F_mj','F_scr','F_scrID','F_scsj','F_spjd','F_sssjb','F_wcsj','F_zxzt','F_bb','F_sjlx','F_sjmc','F_sm','F_sjz'];//用于保存的字段key','与fieldKeys一一对应
    this.savekeyType = savekeyType;//{scrID:"number",F_scsj:"yyyy-MM-dd",F_wcsj:"yyyy-MM-dd"}
	this.records = [];
	this.sucCall = sucCall;
  };
  SJBDataService.prototype.getData = function(){
    var _self = this;
    var data = CustomForm.getData();
	var dataObj = JSON2.parse(data);
	var arr = [];
	var mainData_0 = dataObj.main.fields;
	var relDatas_0 = dataObj.refTab[0].fields;
	var mainData_1 = {};
	var relDatas_1 = [];
    if(relDatas_0==null){
      relDatas_0=[];
    }
	for(var i=0;i<_self.fieldKeys.length;i++){
		var key = _self.fieldKeys[i];
		var savekey = _self.fieldSaveKeys[i];
		if(mainData_0[key]){
			mainData_1[savekey]=mainData_0[key];
		}
	}
	for(var j=0;j<relDatas_0.length;j++){
		var relData_0 = relDatas_0[j];
		var relData_1 = {};
		for(var k=0;k<_self.fieldKeys.length;k++){
			var key = _self.fieldKeys[k];
			var savekey = _self.fieldSaveKeys[k];
			if(relData_0[key]){
				relData_1[savekey]=relData_0[key];
			}
		}
		relDatas_1.push(relData_1);
	}
	if(relDatas_1.length==0){
		 var record = new IbmsRecord(_self.tableName,"",mainData_1,_self.savekeyType);
		_self.records = [record];
	}else{
		var records = [];
		for(var l=0;l<relDatas_1.length;l++){
			var relData_1 = relDatas_1[l];
			var d = $.extend({},mainData_1,relData_1);
			var record = new IbmsRecord(_self.tableName,"",d,_self.savekeyType);
			records.push(record);
		}
		_self.records = records;
	}
    return this;
  }
  SJBDataService.prototype.save =function(){
	//console.log(this.records);
    var recordService = new RecordService(this.records,this.sucCall);
	recordService.add();
  } 
  
  