//数据包项目定制代码
var customHandler = {};
//
////表单Id
//customHandler.getDisplayId = function(){
//	return '10000000510040';
//}

//定制甘特图
//系统方法
customHandler.beforeGanttInitHandler = function(gantt, operatePermissions, tempWidth, xmId, chspzt, _ctx, fcId, sjbId){
	/*function addColumnToggle(column_name) {
		var column = gantt.getGridColumn(column_name);
		var label = getColumnLabel(column);

		//add prefix to distinguish from the same item in 'show columns' menu
		var item_id = "toggle#" + column_name
		menu.addNewChild(null, -1, item_id, "Hide '" + label + "'", false);
		menu.addNewSeparator(item_id);
	}

	function addColumnsConfig() {
		menu.addNewChild(null, -1, "show_columns", "Show columns:", false);
		var columns = gantt.config.columns;

		for (var i = 0; i < columns.length; i++) {
			var checked = (!columns[i].hide),
					itemLabel = getColumnLabel(columns[i]);
			menu.addCheckbox("child", "show_columns", i, columns[i].name, itemLabel, checked);
		}
	}

	function getColumnLabel(column) {
		if (column == null)
			return '';

		var locale = gantt.locale.labels;
		var text = column.label !== undefined ? column.label : locale["column_" + column.name];

		text = text || column.name;
		return text;
	}*/
	gantt.config.columns = [];
	/*gantt.config.columns.push({
		name:"number", label:"计划编号", align: "left", width:10,
		template: function(item) {
    		return item.number;
    	}
    });*/
	gantt.config.columns.push({
    	name:"text", label:"工作任务", width:(tempWidth-400)*0.5, resize: true,tree:true,text:true,
    	template: function(item) {
    		var retVal = item.text;
    		if(item.taskSwitch == '0'){
    			if (item.taskType != '工作计划'){
    				retVal = "<div style='color:red;' ndky='"+ item.ndkyscjh +"'>"+item.text+"</div>";
    			}else{
    				retVal = "<div ndky='"+ item.ndkyscjh +"'>"+item.text+"</div>";
    			}    			
    		}
    		return retVal;
    	}
    });
	gantt.config.columns.push({
    	name:"start_date", label:"计划开始时间", align: "center", width:(tempWidth-400)*0.25,
    	template: function(item) {
    		return item.start_date;
    	}
    });
	gantt.config.columns.push({
    	name:"end_date", label:"计划结束时间", align: "center", width:(tempWidth-400)*0.25,
    	template: function(item) {
    		return item.end_date;
    	}
    });
	/*gantt.config.columns.push({
    	name:"duration", label:"计划工期", align: "center", width:"50",
    	template: function(item) {
    		if(item.taskType!=undefined && item.taskType=="阶段"){
    			return "";
    		}
    		return item.duration + "天";
    	}
    });
	gantt.config.columns.push({
    	name:"department", label:"责任部门", align: "center", width:"100",
    	template: function(item) {
    		if (item.department == undefined) return "";
    		return item.department;
    	}
    });
	gantt.config.columns.push({
    	name:"assigned", label:"负责人", align: "center", width:"100",
    	template: function(item) {
    		if (item.assigned == undefined) return "";
    		return item.assigned;
    	}
    });*/
	//if (operatePermissions['105']){
		gantt.config.columns.push({
			name:"manage", label:"管理", align: "center", width:"50",
			template: function(item) {
				if(item.$level>0){		
					//alert(JSON.stringify(item))
					return "<a name='detail' href='javascript:void(0)' onclick='editDataPackage("+item.id+","+fcId+","+sjbId+',"'+_ctx+'"'+")'>管理</a>";
				}else{
					return "";
				}
			
			}
		});
	//}
	
	//if (operatePermissions['101']){
		gantt.config.columns.push({name:"add", width:tempWidth*0.05});
	//}
		
	window.showDetail = function(id){
		var width = 900;
		var height = 600;
		//var url = "/ibms/oa/form/dataTemplate/editData.do?__displayId__="+displayId+"&__pk__="+id+"&__dbomFKName__=&__dbomFKValue__=&__projectKey__=${projectKey}&__tableName__=${tableName}&dataTemplateId=${dataTemplateId}&readonly=true&__parentTask__="+parentTask;
		var url = _ctx+"/oa/form/dataTemplate/detailData.do?__displayId__=10000006660037&__pk__="+id;
    	DialogUtil.open({
    		title:'任务详情',
    		height:height,
    		width: width,
    		url: url,
    		showMax: false,                             //是否显示最大化按钮 
    	    showToggle: false,                         //窗口收缩折叠
    	    showMin: false,
    		sucCall:function(rtn){
    		}
    	});
	}
	gantt.config.grid_resize = true;
	gantt.config.grid_width = 480;
}

function editDataPackage(id, projectId, sjbId, _ctx){
	//alert("在projectTask.js function:editDataPackage 该行的主键："+id);
	
	var url = _ctx + "/oa/form/dataTemplate/preview.do?__displayId__=10000006730031"
		+"&__dbomFKName__="+id
		//+"&__dbomFKName__="+sjbName
		+"&__dbomFKValue__="+sjbId
		+"&projectId="+projectId
		//+"&taskId="+id
		+"&__dbomSql__=F_SSRW="+id;

	DialogUtil.open({
		title:'数据项目管理',
		height: 650,
		width: 1200,
		url: url,
		showMax: false,                             //是否显示最大化按钮 
	    showToggle: false,                         //窗口收缩折叠
	    showMin: false,
		sucCall:function(rtn){
			//location.href=location.href.getNewUrl();
		}
	});
}
customHandler.afterGanttInitHandler = function(gantt, operatePermissions){
	//任务条左侧和右侧添加文本
    /*gantt.templates.leftside_text = function(start, end, task){
		return task.duration + " days";
	};
    gantt.templates.rightside_text = function(start, end, task){
		return "ID: #" + task.id;
	};*/
	//自定义任务图标
	gantt.attachEvent("onBeforeSetTaskIcon", function (item, customCss) {
		var taskType = item.taskType;
		if (taskType == undefined)
			return false;
		if (taskType == "普通任务"){
			customCss.push("<div class='gantt_tree_icon gantt_ptrw'></div>");
		}else if (taskType == "工作计划"){
			customCss.push("<div class='gantt_tree_icon gantt_jd'></div>");
		}else if (taskType == "岗位任务"){
			customCss.push("<div class='gantt_tree_icon gantt_htjd'></div>");
		}else if (taskType == "控制节点"){
			customCss.push("<div class='gantt_tree_icon gantt_kzjd'></div>");
		}else if (taskType == "部门协作任务"){
			customCss.push("<div class='gantt_tree_icon gantt_bmxzrw'></div>");
		}else if (taskType == "外协任务"){
			customCss.push("<div class='gantt_tree_icon gantt_wxrw'></div>");
		}else{
			customCss.push("<div class='gantt_tree_icon gantt_ptrw'></div>");
		}
		return true;
	});
}

//========定制方法===================
