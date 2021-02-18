//导入xml模板文件
function importModel(){
	var selectedTaskId = gantt.getSelectedId();
	var taskidstr="";
	if(selectedTaskId!=null && selectedTaskId.substring(0,1)!="W"){
		taskidstr="&id="+selectedTaskId;
	}
	var paramValueString = "";
	CommonDialog("xzmb",function(data){
		var url="/ibms/S0104/S010402/S01040202Project/importSave.do?" +
				"projID=" + projectKey + "&mbId="+data.ID+taskidstr+"&versionNumber="+versionNumber;
		DialogUtil.waitting("正在导入，请稍后...");
		$.ajax({
			url:url,					
			async:false,
			success:function(){	
				DialogUtil.close();
				DialogUtil.success("导入成功",function(rtn){
					if(rtn){
					location.href=location.href;
					}
				});
			},
			error:function(){
				DialogUtil.close();
				DialogUtil.success("导入失败");
			}
			
		})
		//data返回 Object { F_MBNR = "参数值"}，多个则返回 Object 数组
	},paramValueString);
}

//导出xml模板文件
function exportModel(){
	var selectedTaskId = gantt.getSelectedId();
	var taskidstr="";
	if(selectedTaskId!=null && selectedTaskId.substring(0,1)!="W"){
		taskidstr="&nodeID="+selectedTaskId;
	}
	var paramValueString = "";
	var height=500;
	var width=900;
	var url="/ibms/oa/form/dataTemplate/editData.do?__displayId__=2210000000220011&" +
			"projID="+ projectKey +taskidstr + "&versionNumber="+versionNumber;
	DialogUtil.open({
		title:'保存WBS模板',
		height:height,
		width: width,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				location.href=location.href;
			}
		}
	});
	//var url="/ibms/S0104/S010402/S01040202Project/Export2XML.do?projID=${projectKey}&nodeID="+selectedTaskId;
	//location.href=url;	
}

//导出Excel
function exportExcel(){
	window.location.href = __ctx+'/S0104/S010402/S01040202Project/expData.do?&dataId=' + projectKey + '&bbid=' + versionNumber;
}
//导入Excel
function importExcel(){
	var url = __ctx+'/S0104/S010402/S01040202Project/importExcelDialog.do?&dataId=' + projectKey + '&bbid=' + versionNumber;
	    url += "&templateName=项目计划模板&templateFile=项目计划模板";
	DialogUtil.open({
		title:'导入Excel',
		height:240,
		width: 500,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				location.href=location.href;
			}
		}
	});
}


//下发任务
function assignTask(){
	//获取勾选的任务
	var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
	if($aryId.length == 0){
		DialogUtil.warn("请选择需要下发任务记录！");
		return;
	}
	var delId="";
	$aryId.each(function(i){
		var obj=$(this);
		if(i < $aryId.length-1){
			delId+=obj.val() +",";
		}
		else{
			delId+=obj.val();
		}
	});
	DialogUtil.waitting("正在下发任务，请稍后...");
	$.ajax({
		url: __ctx + '/project/startWBSJob/executeJob.do',
		data:{
			delId: delId,
			/* __pk__:'${projectKey}', */
			dataTemplateId:dataTemplateId,
			typeAlias:typeAlias
		},
		async:false,
		success:function(){	
			DialogUtil.close();
			DialogUtil.success_callback("任务下发成功","提示信息",function(rtn){
				if(rtn){
					location.href=location.href;
				}
			});
		}
	})
}

//计划批量调整
function alterPlan(){
	var checkedTask=$("input[class='pk']:checked");
	if(checkedTask.size()<1){
		DialogUtil.warn("请先选择节点");
		return;
	}		
	var firstID=$(checkedTask.get(0)).val();//第一个节点ID
	var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
	var ids="";//所有ID集合					
	//判断是否选中相同层级的节点
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		ids+=tempID+",";
		var tempTask=gantt.getTask(tempID);
		if(tempTask.$level!=firstLevel){
			DialogUtil.warn("请选择相同层级节点");
			return;
		}				
	}
	ids=ids.substring(0,ids.length-1);
	DialogUtil.open({
		title:'批量调整计划时间',
		height:$(window).height()*0.7,
		width: $(window).width()*0.5,
		url: __ctx+'/S0104/S010402/S01040202Project/alterPlan.do?ids='+ids,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				location.href=location.href;
			}
		}
	});
}

//附件管理
function manageFile(){
	var height=800;
	var width=1200;
	var lx=encodeURIComponent(encodeURIComponent("附件"));
	var url = "/ibms/project/querySetting/preview.do?__displayId__=30000000030055&xm_dataid=" + projectKey +"&rw_dataid=&typeAlias=${typeAlias}&dataTemplateId=" + dataTemplateId+"&versionNumber="+versionNumber;
	DialogUtil.open({
		title:'附件管理',
		height:height,
		width: width,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				return;
			}
		}
	});
}

//任务上移
function taskUp(){
	var checkedTask=$("input[class='pk']:checked");
	if(checkedTask.size()<1){
		DialogUtil.warn("请先选择节点");
		return;
	}		
	var firstID=$(checkedTask.get(0)).val();//第一个节点ID
	var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
	
	//判断是否选中相同层级的节点
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);
		if(tempTask.$level!=firstLevel){
			DialogUtil.warn("请选择相同层级节点");
			return;
		}				
	}
	
	//判断选中的节点是否是当前层级的最后一个节点，如果是，就不交换
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);		
		var prevTaskId = gantt.getPrevSibling(tempID);
		if (prevTaskId == null ){
			return;
		}			
	}
	
	//交换的规则是所有选中的节点按照从前往后的顺序，依次和next节点进行交换			
	for(var i=0;i<=checkedTask.size()-1;i++){
		var tempID=$(checkedTask.get(i)).val();	
		var tempTask=gantt.getTask(tempID);	
		var prevTaskId = gantt.getPrevSibling(tempID);
		if (prevTaskId == null){
			return;
		}
		var prevTask = gantt.getTask(prevTaskId);
		var selectedOrder = tempTask.order;
		var selectedRwbh = tempTask.number;
		tempTask.order = prevTask.order;
		tempTask.number = prevTask.number;
		prevTask.order = selectedOrder;
		prevTask.number = selectedRwbh;
		//修改子节点信息
		editSubNum(tempTask, tempTask.order);
		editSubNum(prevTask, prevTask.order);
		sortByOrder();
	}		
	var dataList = gantt._get_tasks_data();
	var params = [];
	for(var i = 0;i<dataList.length;i++){
		params.push({"index":i,"id":dataList[i]["id"],"order":dataList[i]["order"],"parent":dataList[i]["parent"]});
	}
	var json = JSON.stringify(params);
	$.ajax({
        url : '${ctx}/S0104/S010402/S01040202Project/exchangeTaskOrder.do',
        data : {mydata:json},
        async : false,
        success:function(data){
        }
    })
}
/**修改子节点信息*/
function editSubNum(startTask, rwbh){
	var sub_id = startTask.subId;
	if(sub_id != null && sub_id != ""){
		var sub_ids = sub_id.split(",");
		for(var num = 0; num < sub_ids.length; num++){
			var curTask = gantt.getTask(sub_ids[num]);
			var order = curTask.order;
			var number = curTask.number;
			curTask.order = rwbh + order.substr(order.lastIndexOf("."));
			curTask.number = curTask.order;
			editSubNum(curTask, curTask.order);
		}
	}
}

//任务下移
function taskDown(){
	var checkedTask=$("input[class='pk']:checked");
	if(checkedTask.size()<1){
		DialogUtil.warn("请先选择节点");
		return;
	}		
	var firstID=$(checkedTask.get(0)).val();//第一个节点ID
	var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
	
	//判断是否选中相同层级的节点
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);
		if(tempTask.$level!=firstLevel){
			DialogUtil.warn("请选择相同层级节点");
			return;
		}				
	}
	
	//判断选中的节点是否是当前层级的最后一个节点，如果是，就不交换
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);				
		var nextTaskId = gantt.getNextSibling(tempTask.id);
		if (nextTaskId == null ){
			return;
		}			
	}
	
	//交换的规则是所有选中的节点按照从后往前的顺序，依次和next节点进行交换			
	for(var i=checkedTask.size()-1;i>=0;i--){
		var tempID=$(checkedTask.get(i)).val();	
		var tempTask=gantt.getTask(tempID);	
		var nextTaskId = gantt.getNextSibling(tempID);
		if (nextTaskId == null){
			return;
		}
		var nextTask = gantt.getTask(nextTaskId);
		var selectedOrder = tempTask.order;
		var selectedRwbh = tempTask.number;
		tempTask.order = nextTask.order;
		tempTask.number = nextTask.number;
		nextTask.order = selectedOrder;
		nextTask.number = selectedRwbh;
		//修改子节点信息
		editSubNum(tempTask, tempTask.order);
		editSubNum(nextTask, nextTask.order);
		sortByOrder();
	}
	var dataList = gantt._get_tasks_data();
	var params = [];
	for(var i = 0;i<dataList.length;i++){
		params.push({"index":i,"id":dataList[i]["id"],"order":dataList[i]["order"],"parent":dataList[i]["parent"]});
	}
	var json = JSON.stringify(params);
	$.ajax({
        url : '${ctx}/S0104/S010402/S01040202Project/exchangeTaskOrder.do',
        data : {mydata:json},
        async : false,
        success:function(data){
        }
    })
}

//任务左移
function taskLeft(){
	var checkedTask=$("input[class='pk']:checked");
	if(checkedTask.size()<1){
		DialogUtil.warn("请先选择节点");
		return;
	}		
	var firstID=$(checkedTask.get(0)).val();//第一个节点ID
	var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
	
	//判断是否选中相同层级的节点
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);
		if(tempTask.$level!=firstLevel){
			DialogUtil.warn("请选择相同层级节点");
			return;
		}	
		if(tempTask.$level==1){
			DialogUtil.warn("请选择下级节点");
			return;
		}	
	}
	//交换的规则是所有选中的节点按照从后往前的顺序，依次和next节点进行交换			
	for(var i=checkedTask.size()-1;i>=0;i--){
		var tempID=$(checkedTask.get(i)).val();	
		var tempTask=gantt.getTask(tempID);	
		var tempParentID = gantt.getParent(tempID);
		//修改本级节点
		//0. 修改同级兄弟节点的信息 和 此节点的子节点信息
		var tempNextID = tempID;
		while(gantt.getNextSibling(tempNextID)!=null){
			var nextTask = gantt.getTask(gantt.getNextSibling(tempNextID));
			nextTask.order = changeNumber(nextTask.order,0,"reduce");
			nextTask.number = changeNumber(nextTask.number,0,"reduce");
			setChildrenNumber(tempNextID,"brother");
			tempNextID = gantt.getNextSibling(tempNextID);
		}
		//1. 设置本级节点的父级是该节点父级的节点
		gantt.setParent(tempTask,gantt.getParent(tempParentID));
		//1.1 将本级节点父节点中的子节点将本节点移除，并将此节点作为父节点的父节点的子节点
		gantt._branches[tempParentID] = removeArr(gantt._branches[tempParentID],tempID);
		gantt._branches[gantt.getParent(tempParentID)].push(tempID);
		//2. 修改本级节点的编号（找到父级节点的编号，在父级节点的编号下+.1）
		var tempParentBh = gantt.getTask(tempParentID).number;
		gantt.getTask(tempID).number = changeNumber(tempParentBh,0,"add");
		gantt.getTask(tempID).order = changeNumber(tempParentBh,0,"add");
		gantt.getTask(tempID).$level = gantt.getTask(tempParentID).$level;
		setChildrenNumber(tempID,"self");
		//3. 修改父级节点的兄弟节点的编号，+.1
		while(gantt.getNextSibling(tempParentID)!=null&&gantt.getNextSibling(tempParentID)!=tempID){
			var nextTask = gantt.getTask(gantt.getNextSibling(tempParentID));
			for(var j = 0;j<gantt.getChildren(nextTask.id).length;j++){
				gantt.getTask(gantt.getChildren(nextTask.id)[j]).order = 
					changeNumber(nextTask.order,gantt.getTask(gantt.getChildren(nextTask.id)[j]).order,"childrenAdd");
				gantt.getTask(gantt.getChildren(nextTask.id)[j]).number = 
					changeNumber(nextTask.number,gantt.getTask(gantt.getChildren(nextTask.id)[j]).number,"childrenAdd");
			}
			nextTask.number = changeNumber(nextTask.number,0,"add");
			nextTask.order = changeNumber(nextTask.order,0,"add");
			tempParentID = gantt.getNextSibling(tempParentID);
		}
		sortByOrder();
	}
	var dataList = gantt._get_tasks_data();
	var params = [];
	for(var i = 0;i<dataList.length;i++){
		params.push({"index":i,"id":dataList[i]["id"],"order":dataList[i]["order"],"parent":dataList[i]["parent"]});
	}
	var json = JSON.stringify(params);
	$.ajax({
        url : '${ctx}/S0104/S010402/S01040202Project/exchangeTaskOrder.do',
        data : {mydata:json},
        async : false,
        success:function(data){
        }
    })
}

//任务右移
function taskRight(){
	var checkedTask=$("input[class='pk']:checked");
	if(checkedTask.size()<1){
		DialogUtil.warn("请先选择节点");
		return;
	}		
	var firstID=$(checkedTask.get(0)).val();//第一个节点ID
	var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
	
	//判断是否选中相同层级的节点
	for(var i=0;i<checkedTask.size();i++){
		var tempID=$(checkedTask.get(i)).val();
		var tempTask=gantt.getTask(tempID);
		if(tempTask.$level!=firstLevel){
			DialogUtil.warn("请选择相同层级节点");
			return;
		}	
		if(gantt.getPrevSibling(tempID)==null){
			DialogUtil.warn("请选择有上级兄弟节点的数据");
			return;
		}
	}
	//交换的规则是所有选中的节点按照从后往前的顺序，依次和next节点进行交换			
	for(var i=checkedTask.size()-1;i>=0;i--){
		var tempID = $(checkedTask.get(i)).val();	
		var tempTask = gantt.getTask(tempID);	
		var tempParentID = gantt.getParent(tempID);
		var tempBrotherTask = gantt.getTask(gantt.getPrevSibling(tempID));
		var maxChildrenOrder = gantt.getTask(gantt.getPrevSibling(tempID)).order+".0";
		if(gantt.getChildren(tempBrotherTask.id).length>0){
			maxChildrenOrder = gantt.getTask(gantt.getChildren(tempBrotherTask.id)[gantt.getChildren(tempBrotherTask.id).length-1]).order;
		}
		
		
		//设置此节点变换之前的后续节点及子节点的编号
		var tempNextID = tempID;
		while(gantt.getNextSibling(tempNextID)!=null){
			var nextTask = gantt.getTask(gantt.getNextSibling(tempNextID));
			nextTask.number = changeNumber(nextTask.number,0,"reduce");
			nextTask.order = changeNumber(nextTask.order,0,"reduce");
			for(var j = 0;j<gantt.getChildren(nextTask.id).length;j++){
				gantt.getTask(gantt.getChildren(nextTask.id)[j]).order = 
					changeNumber(nextTask.order,gantt.getTask(gantt.getChildren(nextTask.id)[j]).order,"childrenSelf");
				gantt.getTask(gantt.getChildren(nextTask.id)[j]).number = 
					changeNumber(nextTask.number,gantt.getTask(gantt.getChildren(nextTask.id)[j]).number,"childrenSelf");
			}
			tempNextID = gantt.getNextSibling(tempNextID);
		}
		
		gantt._branches[tempParentID] = removeArr(gantt._branches[tempParentID],tempID);
		if(typeof(gantt._branches[tempBrotherTask.id])=="undefined"){
			gantt._branches[tempBrotherTask.id] = [];
		}
		gantt._branches[tempBrotherTask.id].push(tempID);
		//修改本级节点
		gantt.setParent(tempTask,tempBrotherTask.id);
		gantt.getTask(tempID).number = changeNumber(maxChildrenOrder,0,"add");
		gantt.getTask(tempID).order = changeNumber(maxChildrenOrder,0,"add");
		gantt.getTask(tempID).$level = tempBrotherTask.$level + 1;
		setChildrenNumber(tempID,"self");
		sortByOrder();
	}
	var dataList = gantt._get_tasks_data();
	var params = [];
	for(var i = 0;i<dataList.length;i++){
		params.push({"index":i,"id":dataList[i]["id"],"order":dataList[i]["order"],"parent":dataList[i]["parent"]});
	}
	var json = JSON.stringify(params);
	$.ajax({
        url : '${ctx}/S0104/S010402/S01040202Project/exchangeTaskOrder.do',
        data : {mydata:json},
        async : false
    })
}

function changeNumber(temp,tempNext,type){
	//add 父级增加(1.2.1 => 1.2.2)
	if(type=="add"){
		var pre = temp.substr(0,temp.lastIndexOf(".") + 1);
		var after = parseInt(temp.substr(temp.lastIndexOf(".") + 1)) + 1;
	}
	//reduce 兄弟节点递减 (1.2.2 => 1.2.1)父级1.2 
	if(type=="reduce"){
		var pre = temp.substr(0,temp.lastIndexOf(".") + 1);
		var after = parseInt(temp.substr(temp.lastIndexOf(".") + 1)) - 1;
	}
	//childrenAdd 父级兄弟子节点增加 (3.1 => 4.1) 父级由3变成4
	if(type=="childrenAdd"){
		var pre = temp.substr(0,temp.lastIndexOf(".") + 1) + parseInt(parseInt(temp.substr(temp.lastIndexOf(".") + 1)) + 1);
		var after = tempNext.substr(temp.length);
	}
	//兄弟节点的子节点跟着上级节点变化
	if(type=="childrenReduce"){
		var pre = temp;
		var after = tempNext.substr(temp.length);
	}
	//子节点跟着上级节点变化
	if(type=="childrenSelf"){
		var pre = temp;
		var after = tempNext.substr(tempNext.lastIndexOf("."));
	}
	return pre + "" + after;
}

function removeArr(arr,str){
	var arrNew = new Array();
	for(var i = 0;i<arr.length;i++){
		if(str != arr[i]){
			arrNew.push(arr[i]);
		}
	}
	return arrNew;
}

function setChildrenNumber(id,type){
	if(gantt.getChildren(id) != null){
		for(var i = 0;i<gantt.getChildren(id).length;i++){
			if(type=="self"){
				//1. 修改子节点级别
				gantt.getTask(gantt.getChildren(id)[i]).$level = gantt.getTask(id).$level + 1;
				//2. 修改子节点order 和 number
				gantt.getTask(gantt.getChildren(id)[i]).order = 
					changeNumber(gantt.getTask(id).order,gantt.getTask(gantt.getChildren(id)[i]).order,"childrenSelf");
				gantt.getTask(gantt.getChildren(id)[i]).number = 
					changeNumber(gantt.getTask(id).number,gantt.getTask(gantt.getChildren(id)[i]).number,"childrenSelf");
				setChildrenNumber(gantt.getChildren(id)[i],"self");
			}
			if(type=="brother"){
				gantt.getTask(gantt.getChildren(id)[i]).order = 
					changeNumber(gantt.getTask(id).order,gantt.getTask(gantt.getChildren(id)[i]).order,"childrenReduce");
				gantt.getTask(gantt.getChildren(id)[i]).number = 
					changeNumber(gantt.getTask(id).number,gantt.getTask(gantt.getChildren(id)[i]).number,"childrenReduce");
				setChildrenNumber(gantt.getChildren(id)[i],"brother");
			}
		}
	}
}


//显示网络图
function shownetworkgraph(){
	var height=800;
	var width=1400;
	var url="/ibms/project/networkgraph/show.do?prjID=${projectKey}"
	DialogUtil.open({
		title:'网络图',
		height:height,
		width: width,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//rtn作为回调对象，可进行定制和扩展
			if(!(rtn == undefined || rtn == null || rtn == '')){
				location.href=location.href;
			}
		}
	});
	
}

//任务再次分解
function splitTask(){
	//获取勾选的任务
	var $aryId = $("input[type='checkbox'][disabled!='disabled'][class='pk']:checked");
	if($aryId.length == 0){
		DialogUtil.warn("请选择需要通知到的部门！");
		return;
	}
	var delId="";
	$aryId.each(function(i){
		var obj=$(this);
		if(i < $aryId.length-1){
			delId+=obj.val() +",";
		}
		else{
			delId+=obj.val();
		}
	});
	
	
	$.ajax({
		url:__ctx + '/S0104/S010402/S01040202Project/splitJob.do',
		data:{
			__pk__:'${projectKey}',
			preurl:'${preurl}',
			delId:delId
		},
		async:false,
		success:function(){	
			DialogUtil.close();
			DialogUtil.success("任务已经通知到部门！");
		}
	})
}

//打开查询框
function openQueryDialog(){
   var ishide=$("div[name='advancedSearchDiv']").attr("class");
   //根据tr获取显示宽度
   var height = $(".LayoutTable").height()
   if(ishide == "advancedSearchDiv"){
	   $("div[name='advancedSearchDiv']").animate({height:height},50,null,function(){});
	   $("div[name='advancedSearchDiv']").attr("class","advancedSearchDiv_show");
   }else{
	   $("div[name='advancedSearchDiv']").animate({height:"0px"},50,null,function(){});
	   $("div[name='advancedSearchDiv']").attr("class","advancedSearchDiv"); 
   }
}

//前台查询
function searchRecord(){
   var first = true;
   var colnum = 0;
   var rwmc = $("input[name='rwmc']").val().trim();
   var rwfzr = $("input[name='rwfzr']").val().trim();
   var startTime = $("input[name='startTime']").val().trim();
   var endTime = $("input[name='endTime']").val().trim();
   var test = startTime.split("-");
   $(".gantt_row").each(function(){
	   $(this).find(".gantt_cell").each(function(){
			$(this).find(".gantt_tree_content").removeAttr("style");
	   })
   })
   $(".gantt_row").each(function(){
	   if(!first){
		   colnum = 0;
		   $(this).find(".gantt_cell").each(function(){
			   var content = $(this).attr("aria-label");
			   if(colnum==0 && rwmc!="" && content.indexOf(rwmc) >= 0){
				   $(this).find(".gantt_tree_content").attr("style","background-color:yellow");
			   }
			   if(colnum==1 && startTime!="" && dateComparision(startTime,content)){
				   $(this).find(".gantt_tree_content").attr("style","background-color:yellow");
			   }
			   if(colnum==2 && endTime!="" && dateComparision(content,endTime)){
				   $(this).find(".gantt_tree_content").attr("style","background-color:yellow");
			   }
			   if(colnum==5 && rwfzr!="" && content.indexOf(rwfzr) >= 0){
				   $(this).find(".gantt_tree_content").attr("style","background-color:yellow");
			   }
			   ++colnum;
		   })
	   }
	   first = false;
   })
   $("div[name='advancedSearchDiv']").animate({height:"0px"},50,null,function(){});
   $("div[name='advancedSearchDiv']").attr("class","advancedSearchDiv"); 
}


//日期比较
function dateComparision(time1,time2){
   time1 = time1.split("-");
   time2 = time2.split("-");
   time1 =  new Date(time1[0],time1[1],time1[2]);
   time2 =  new Date(time2[0],time2[1],time2[2]);
   return (time1<=time2);
}


//鼠标绑定事件(查询)
$(document).bind('click',function(e){
   var e = e || window.event;
   var elem = e.target || e.srcElement
   while(elem){
	  if((elem.id && elem.id == "advancedSearchDiv") || (elem.id && elem.id == "queryId")){
		return;
	  }
	  elem = elem.parentNode;
   }
   $("div[name='advancedSearchDiv']").animate({height:"0px"},50,null,function(){});
   $("div[name='advancedSearchDiv']").attr("class","advancedSearchDiv"); 
}); 

 //清空数据
function clearQueryForm(){
   $('#querysearchForm').find('input[type=text]').each(function() {
		$(this).val("");
	});
   $(".gantt_row").each(function(){
	   $(this).find(".gantt_cell").each(function(){
			$(this).find(".gantt_tree_content").removeAttr("style");
	   })
   })
}
 //导入project文件
function importProject(){
	var url = ctx + "/project/wbs/uploadFile.do?projectKey=" + projectKey;
	url=url.getNewUrl();
	DialogUtil.open({
		height:300,
		width: 800,
		title : '上传附件',
		url: url, 
		isResize: true,
		sucCall:function(rtn){
			location.href=location.href.getNewUrl();
		}
	});
}

/**人员选择器
 * @param obj
 */
function selectAllUser(obj){
	var params="{'width':'1024','height':'600','title':'人员选择','isCurUser':'false','addOrgsUser':'','isNewSys':'true'}";
	var inputId=$("input[name='createuserid']");
	var inputName=$("input[name='createuser']");
	UserDialog({
		isSingle :true,		
		callback : function(ids, names) {
			if (inputId.length > 0) {
				inputId.val(ids);
			};
			inputName.val(names);
		}
	},params);
}

/**
 * 保存当前的计划版本
 */
function saveVersion(){
	var url = ctx + "/project/wbs/openWBSVersion.do?projectKey=" + projectKey+"&currVersion="+versionNumber;
	url=url.getNewUrl();
	DialogUtil.open({
		height:400,
		width: 600,
		url: url, 
		isResize: true,
		sucCall:function(rtn){
			location.href=location.href.getNewUrl();
		}
	});
}

/**
 * 查看历史的版本
 */
function checkVersion(){
	var url = ctx + "/oa/form/querySetting/preview.do?__displayId__=10000005270016&__dbomSql__=F_XMXXID=" +projectKey;
	url=url.getNewUrl();
	DialogUtil.open({
		height:400,
		width: 900,
		title:"版本列表",
		url: url, 
		isResize: true,
		sucCall:function(rtn){
			versionNumber = rtn;
			$("input[name='versionNumber']").val(versionNumber);
			loadWBSByVersion(rtn);
		}
	});
}

