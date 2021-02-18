/**
 * Pad操作js
 * 靶场和武器所检都用的同一个模板,  是另一个js
 * 此js已经没有在使用了
 *
 */
if(!PadOperater){
	var PadOperater = {};
}
//下载到PAD(执行状态为未开始\进行中的，改为待下载)
PadOperater.downLoad =function(){
	var records = [];
	var ids = getSelectIds(true,false);
	if(!ids) return;
	var result = PadOperater.rightCheck(ids,"未开始,进行中,已终止",true,true);
	if(result.isCan){
		var idArr = ids.split(",");
		$.each(idArr,function(i){
			var record = new IbmsRecord("W_DATAPACKAGEINFO",$(this).get(0).toString(),{"F_ZXZT":"已下载"});
			records.push(record);
		});
		var updateService =  new RecordService(records,function(result){
			$.ligerDialog.success("状态更新成功","",function(){
				location.href = window.location.href.getNewUrl();
			});
		});
		updateService.update();		
	}else{
		$.ligerDialog.warn(result.message);
	}
}
//取消下载到pad(执行状态为待下载的，改为已终止)
PadOperater.cancelDownLoad =function(){
	var records = [];
	var ids = getSelectIds(true,false);
	if(!ids) return;
	var result = PadOperater.rightCheck(ids,"待下载",true,true);
	if(result.isCan){
		var idArr = ids.split(",");
		$.each(idArr,function(i){
			var obj=$(this);
			var record = new IbmsRecord("W_DATAPACKAGEINFO",$(this).get(0).toString(),{"F_ZXZT":"已终止"});
			records.push(record);
		});
		var updateService =  new RecordService(records,function(result){
			$.ligerDialog.success("状态更新成功","",function(){
				location.href = window.location.href.getNewUrl();
			});
		});
		updateService.update();
	}else{
		$.ligerDialog.warn(result.message);
	}
}
//同步到其他PAD(执行状态为已完成的，改为已完成待下载)
PadOperater.asyn =function(){
	var records = [];
	var ids = getSelectIds(true,false);
	if(!ids) return;
	var result = PadOperater.rightCheck(ids,"已完成",true,true);
	if(result.isCan){
		var idArr = ids.split(",");
		$.each(idArr,function(i){
			var record = new IbmsRecord("W_DATAPACKAGEINFO",$(this).get(0).toString(),{"F_ZXZT":"已完成待下载"});
			records.push(record);
		});
		var updateService =  new RecordService(records,function(result){
			$.ligerDialog.success("状态更新成功","",function(){
				location.href = window.location.href.getNewUrl();
			});
		});
		updateService.update();
	}else{
		$.ligerDialog.warn(result.message);
	}
}
//取消同步到其他pad(执行状态为已完成待下载的，改为已完成)
PadOperater.cancelAsyn =function(){
	var records = [];
	var ids = getSelectIds(true,false);
	if(!ids) return;
	var result = PadOperater.rightCheck(ids,"已完成待下载");
	if(result.isCan){
		var idArr = ids.split(",");
		$.each(idArr,function(i){
			var record = new IbmsRecord("W_DATAPACKAGEINFO",$(this).get(0).toString(),{"F_ZXZT":"已完成"});
			records.push(record);
		});
		var updateService =  new RecordService(records,function(result){
			$.ligerDialog.success("状态更新成功","",function(){
				location.href = window.location.href.getNewUrl();
			});
		});
		updateService.update();
	}else{
		$.ligerDialog.warn(result.message);
	}
}
/**
 * ids:需要校验的数据
 * canOperaterZt ：canOperaterZt集合中的数据状态可以操作数据
 * needgw：是否需要岗位检查
 * needFzr：是否需要负责任检查
 */
PadOperater.rightCheck =function(ids,canOperaterZt){
	var sssjb = $.getParameter("F_SSSJB");
	var rtn={isCan:false};
	var url = __ctx+"/dataPackage/tree/ptree/getButtonRight.do";
	$.ajax({
		  type: "POST",
	      url:url,
		  data:{
			  	   buttonName :'pad',
			  	   sssjb: sssjb,
				   ids : ids,
				   canOperaterZt:canOperaterZt
			   },
		  dataType : "text",
	      async:false,
	      success:function(result){
	    	  rtn = JSON2.parse(result);
	      }   		  
	});
	return rtn;
}

//初始化menu列表
PadOperater.init =function(menu){
	var menuJSON = [
		{text: '下载到PAD',click:function(){ PadOperater.downLoad();}},
		{text: '取消下载到pad',click:function(){ PadOperater.cancelDownLoad();}},
		{text: '同步到其他PAD',click:function(){ PadOperater.asyn();}},
		{text: '取消同步到其他pad',click:function(){ PadOperater.cancelAsyn();}},
	];
	$(menu).ligerMenuBar({
		items: [
	 		{ 
	 			text: '<a href="####" class="link run"><span></span>Pad操作集合&nbsp;&nbsp;</a>', menu: {width: 160,items: menuJSON} 
	 		}
        ]
	});
	$(menu).removeClass('l-menubar');
}
function getSelectIds(isAlert,isSingle){
	var idArr = [];
	$('input.pk:checked').each(function(){
		idArr.push($(this).val());
	})
	if(isAlert){
		if(idArr.length==0){
			$.ligerDialog.warn("请选择数据！");
			return false;
		}
		if(idArr.length!=1&&isSingle){
			$.ligerDialog.warn("请选择一条数据！");
			return false;
		}
		return idArr.toString();
	}else{
		return idArr.toString();
	}
}
$(function(){
	$('div.PadOperaterMenu').each(function(){
		PadOperater.init(this);
	})
})