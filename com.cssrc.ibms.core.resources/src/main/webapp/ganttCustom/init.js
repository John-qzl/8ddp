/**
 * wbs的初始化 服务页面ganttView.jsp
 */
$(function(){
	
	//初始化查询条件(给搜索框添加参数)
	/*$("input[name='projectKey']").val(projectKey);
	$("input[name='tableName']").val(tableName);*/
	
    
    //****************查询
    $("#tiezitypes li a").click(function(){
		$("#tiezitypes li a").each(function(){				
			$(this).removeAttr("class");					
		})
		$(this).attr("class","tiezitypeselect");		
	})
	//查询条件收缩		
	$('.tiezidrop').click(function(){
		var ishide=$('#tiezisearchForm').css("display");			
		 if( ishide=="none"){			
			 $('#tiezisearchForm').show();	
			 $('.tiezidrop').find('a').text('收起');
			 $('.tiezidrop').find('a').addClass('activi');				
		 }else{		
			 $('#tiezisearchForm').hide();	
			 $('.tiezidrop').find('a').text('展开');
			 $('.tiezidrop').find('a').removeClass('activi');
		 }
	});	
	$("#searchField").bind("keydown",function(e) {
		if(	e.keyCode == 13){//回车
			$("#searchByNames").click();
		}	
    })	   
    $(".searchInputSpan a").click(function(e) {
			updateSearch();			
    })
    //初始化日期控件
    $("body").delegate("input.Wdate", "click",function(){
		var fmt=$(this).attr("dateFmt");
		WdatePicker({el:this,dateFmt:fmt});
	});
})

 //导入project文件
function importProject(){
	/*var url = ctx + "/project/wbs/uploadFile.do?projectKey=" + projectKey;
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
	});*/
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
/**
 * 甘特图插件有一个clearAll的方法，用来清除上次页面上的所有甘特图数据，然后可以通过load方法重新加载数据
 * 这个方法用在当用户需要重新加载数据时使用，主要是版本的控制使用
 */
/*function loadWBSByVersion(versionNumber){
	var tasks = $.ajax({
        url : ctx +'/project/wbs/getWBSDataByVersion.do',
        data : {
        	tableName: "W_XMB",
  			projectKey: projectKey,
  			versionNumber : versionNumber
        },
        async : false
    }).responseText;
    tasks = jQuery.parseJSON(tasks);
    gantt.clearAll();
	gantt.parse(tasks);
}
*/
/**
 * 查询数据通过查询条件
 */
function loadWBSBySearchArea(){
	var tasks = $.ajax({
        url : '/ibms/project/wbs/getWBSData.do',
        data : $('#querysearchForm').serialize(),
        async : false
    }).responseText;
    tasks = jQuery.parseJSON(tasks);
    gantt.clearAll();
    gantt.parse(tasks);
    //排序
//    sortByOrder();
    $("div[name='advancedSearchDiv']").animate({height:"0px"},50,null,function(){});
	$("div[name='advancedSearchDiv']").attr("class","advancedSearchDiv"); 
}


