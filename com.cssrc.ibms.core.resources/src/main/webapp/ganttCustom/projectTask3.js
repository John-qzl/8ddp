//科研项目任务定制
var customHandler = {};

//表单Id
customHandler.getDisplayId = function(){
	return '10000000510040';
}

//定制甘特图
//系统方法
customHandler.beforeGanttInitHandler = function(gantt, operatePermissions, tempWidth){
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
	gantt.config.columns.push({
    	name:"text", label:"任务名称", tree:true, width:tempWidth*0.2, resize: true,
    	template: function(item) {
    		var retVal = item.text;
    		if(item.taskSwitch == '0'){
    			if (item.taskType != '阶段'){
    				retVal = "<div style='color:red;'>"+item.text+"</div>";
    			}
    		}
    		return retVal;
    	}
    });
	gantt.config.columns.push({
    	name:"start_date", label:"计划开始时间", align: "center", width:tempWidth*0.2,
    	template: function(item) {
    		return item.start_date;
    	}
    });
	gantt.config.columns.push({
    	name:"end_date", label:"计划结束时间", align: "center", width:tempWidth*0.2,
    	template: function(item) {
    		return item.end_date;
    	}
    });
	gantt.config.columns.push({
    	name:"duration", label:"计划工期", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		return item.duration + "天";
    	}
    });
	gantt.config.columns.push({
    	name:"department", label:"责任部门", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		if (item.department == undefined) return "";
    		return item.department;
    	}
    });
	gantt.config.columns.push({
    	name:"assigned", label:"负责人", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		if (item.assigned == undefined) return "";
    		return item.assigned;
    	}
    });
	
	gantt.config.columns.push({
		name:"manage", label:"管理", align: "center", width:tempWidth*0.05,
		template: function(item) {
			return "<a name='detail' href='javascript:void(0)' onclick='showDetail(\""+item.id+"\")'>详细</a>";
		}
	});
		
		gantt.config.columns.push({name:"add", width:tempWidth*0.05});
	
	
	window.showDetail = function(id){
		
		if (id.split('&').length != 1){
    		return;
    	}
    	
    	var ganttTask = gantt.getTask(id);
    	
    	var parentNodeId = ganttTask.parent;
    	var parts = parentNodeId.split('&');
    	
    	var parentTask = '';
    	if (parts.length == 1){
    		parentTask = parentNodeId;
    	}
    	
    	var displayId = customHandler.getDisplayId();
    	
		var width = 900;
		var height = 600;
		var url = "/ibms/oa/form/dataTemplate/editData.do?__displayId__="+displayId+"&__pk__="+id+"&__dbomFKName__=&__dbomFKValue__=&__projectKey__=${projectKey}&__tableName__=${tableName}&dataTemplateId=${dataTemplateId}&readonly=true&__parentTask__="+parentTask;
		
    	DialogUtil.open({
    		title:'任务详细',
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
		}else if (taskType == "阶段"){
			customCss.push("<div class='gantt_tree_icon gantt_jd'></div>");
		}else if (taskType == "合同节点"){
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