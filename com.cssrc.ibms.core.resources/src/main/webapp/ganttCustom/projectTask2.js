//科研项目任务定制
var customHandler = {};

//表单Id
customHandler.getDisplayId = function(){
	return '10000000510040';
}

//定制甘特图
//系统方法
customHandler.beforeGanttInitHandler = function(gantt, operatePermissions,tempWidth){

	//tempWidth =  tempWidth * 2 * 0.6;
	
	var spaceDaysValue = 3; //默认三天提醒
	
	gantt.config.columns = [];
	gantt.config.columns.push({name:"text", label:"任务名称", tree:true, width:tempWidth*0.2});
	gantt.config.columns.push({
    	name:"progress", label:"进度", width:tempWidth*0.1, align: "center",
    	template: function(item) {
    		if (item.type == "project") return "";
    		if (item.progress >= 1)
    			return "<div style='color:green;'>"+"已完成"+"</div>";
    		if (item.progress == 0)
    			return "<div style='color:red;'>"+"未开始"+"</div>";
    		if (item.progress <= 0.1)
    			return "<div style='color:blue;'>"+"刚开始"+"</div>";
			if (item.progress >= 0.9)
    			return "<div style='color:blue;'>"+"即将完成"+"</div>";
    		return "<div style='color:blue;'>"+Math.round(item.progress*100) + "%"+"</div>";
    	}
    });
	gantt.config.columns.push({
    	name:"duration", label:"计划工期", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		return item.duration + "天";
    	}
    });
	gantt.config.columns.push({
    	name:"end_date", label:"计划结束时间", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		
    		var value = "";
    		var endDate = item.end_date;
    		if(endDate == null) return value;
    		var strEndDate = gantt.templates.date_grid(endDate, item);
    		value = strEndDate;
    		
    		//var realEndTime = item.realEndDate; //实际结束时间
    		var realEndTime = item.real_end_date; //实际结束时间
    		var realEndDate = null;
    		if (realEndTime != null && realEndTime != ""){
    			realEndTime = realEndTime.format("yyyy-MM-dd hh:mm:ss");
    			realEndDate = getDate(realEndTime);
    		}
			var taskProcess = item.progress;  //任务进度
			var type = item.taskType; //任务类型
			
			//判断value和当前日期的时间间隔
			var currDate = new Date();
			var spaceTime = endDate.getTime() - currDate.getTime() + 24*3600*1000;
			
			if (type != "阶段" && type != "") {
				if (taskProcess < 1) {
					if (spaceTime > 0) {
						var days = Math.floor(spaceTime/ (24 * 3600 * 1000)); // 相差天数
						if (days < spaceDaysValue) {
							value = "<div style='background:#FFFF00'>"+strEndDate+"</div>";
						} else {
						}
					} else {
						value = "<div style='background:#FF6347'>"+strEndDate+"</div>";
					}
				} else {
					var actualTime = realEndDate.getTime() - endDate.getTime();
					var actualDay = Math.floor(actualTime/ (24 * 3600 * 1000));
					if (actualDay <= 0) {
					} else {
						value = "<div style='background:#FF6347'>"+strEndDate+"</div>";
					}
				}
			}
    		return value;
    	}
    });
	gantt.config.columns.push({
    	name:"realStartDate", label:"实际开始时间", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		var realStartDate = "";
    		if (item.real_start_date != null && item.real_start_date != ""){
    			var realStartTime = item.real_start_date.format("yyyy-MM-dd hh:mm:ss");
    			realStartDate = realStartTime.split(" ")[0];
    		}
    		return realStartDate;
    	}
    });
	gantt.config.columns.push({
    	name:"realEndDate", label:"实际结束时间", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		var realEndDate = "";
    		if (item.real_end_date != null && item.real_end_date != ""){
    			var realEndTime = item.real_end_date.format("yyyy-MM-dd hh:mm:ss");
    			realEndDate = realEndTime.split(" ")[0];
    		}
    		return realEndDate;
    	}
    });
	gantt.config.columns.push({
    	name:"realDuration", label:"实际工期", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		if (item.realDuration == "") return "";
    		return item.realDuration + "天";
    	}
    });
	gantt.config.columns.push({
    	name:"assigned", label:"负责人", align: "center", width:tempWidth*0.1,
    	template: function(item) {
    		if (item.assigned == undefined) return "";
    		return item.assigned;
    	}
    });
	//if (operatePermissions['detailManage']){
		gantt.config.columns.push({
			name:"manage", label:"管理", align: "center", width:tempWidth*0.1,
			template: function(item) {
				return "<a name='detail' href='javascript:void(0)' onclick='testaa()'>详细</a>";
			}
		});
	//}
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