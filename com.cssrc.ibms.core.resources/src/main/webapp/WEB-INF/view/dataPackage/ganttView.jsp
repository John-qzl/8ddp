<%@ page language="java" contentType="text/html; charset=UTF-8"	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<c:set var="projectKey" value="${requestScope.projectKey}" />
<c:set var="tableName" value="${requestScope.tableName}" />
<c:set var="ganttType" value="${requestScope.ganttType}" />
<c:set var="ganttSiteId" value="${requestScope.ganttSiteId}" />
<c:set var="dataTemplateId" value="${requestScope.dataTemplateId}" />
<c:set var="typeAlias" value="${requestScope.typeAlias}" />
<c:set var="ButtonPermission" value="${requestScope.ButtonPermission}" />
<c:set var="showRealTimeLine" value="${requestScope.showRealTimeLine}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html>
<head>
	<title>甘特图</title>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<%@include file="/commons/include/color.jsp"%>
	<f:js pre="jslib/lang/common" ></f:js>
	<f:js pre="jslib/lang/js" ></f:js>
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-common.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-layout.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-dialog.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-tab.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-menu.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-grid.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-form.css">
	<link rel="stylesheet" href="${ctx}/styles/default/css/Aqua/css/ligerui-msg.css">
	<link rel="stylesheet" href="${ctx}/styles/custom_css/ganttView.css">
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/util.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/util/dateUtil.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.min.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerResizable.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/lg/util/DialogUtil.js" ></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/CommonDialog.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/ibms/panelBodyHeight.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/dhtmlxgantt/dhtmlxgantt.js"></script>
	<script type="text/javascript" src="${ctx}/jslib/dhtmlxgantt/ext/dhtmlxgantt_quick_info.js" charset="utf-8"></script>
	<script type="text/javascript" src="${ctx}/jslib/dhtmlxgantt/locale_cn.js" charset="utf-8"></script>
	<link rel="stylesheet" href="${ctx}/jslib/dhtmlxgantt/skins/dhtmlxgantt_terrace.css" type="text/css" media="screen" title="no title" charset="utf-8"/>
	
	<link title="no title" name="styleTag" rel="stylesheet" type="text/css" href="${ctx}/styles/default/css/Aqua/css/ligerui-all.css"></link>
	<link title="no title" name="styleTag" rel="stylesheet" type="text/css" href="${ctx}/styles/default/css/web.css"></link>
	<link title="no title" name="styleTag" rel="stylesheet" type="text/css" href="${ctx}/styles/default/css/jquery/plugins/rowOps.css"></link>
	
	<!-- 添加定制js -->
	
	<script type="text/javascript" src="${ctx}/ganttCustom/projectTask.js"></script>
	<script type="text/javascript" src="${ctx}/ganttCustom/modelType.js"></script>
	<script type="text/javascript" src="${ctx}/ganttCustom/init.js"></script>
	<!-- 添加定制css -->
	<link rel="stylesheet" href="${ctx}/ganttCustom/projectTask.css"/>
	<!-- 查询按钮的js START-->
	<script src="${ctx}/jslib/jquery/jquery.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.form.js"></script>
	<script src="${ctx}/jslib/util/util.js" type="text/javascript"></script>
	<script src="${ctx}/jslib/util/json2.js" type="text/javascript"></script>
	<script src="${ctx}/jslib/lg/util/DialogUtil.js" type="text/javascript"></script>
	<script src="${ctx}/ganttCustom/bk/WdatePicker.js" type="text/javascript"></script>
	<script src="${ctx}/jslib/ibms/oa/system/SysDialog.js" type="text/javascript"></script>  
	<!-- 查询按钮的js END-->
	<script type="text/javascript" src="${ctx}/jslib/lg/ligerui.all.js"></script>
	
	<style type="text/css">
		html, body{ height:100%; padding:0px; margin:0px; overflow: hidden;}
		.gantt_task_line.gantt_dependent_task {
			background-color: #65c16f;
			border: 1px solid #3c9445;
		}
		.gantt_task_line.gantt_dependent_task .gantt_task_progress {
			background-color: #46ad51;
		}
		.highlighted-column {
			background-color:#fff3a1;
		}
		.gantt_task_scale .gantt_scale_cell{
			cursor: default;
		}
		.gantt_task_scale .gantt_scale_cell .highlighted-column {
			color: #454545;
			font-weight: bold;
		}
		#WBS{
		  border: 0;
		}
    	.gantt_task_progress {
  			background: #547dab;
		}
		.gantt_left_container{
		  width: 20px;
		  height: 95%;
		  float: left;
		  border: 1px solid #dddddd;
		  
		}
		.l-layout-header-toggle:before{
		    font-family: "iconfont";
		    /*content: "\e605";*/
		    content: "\e612";
		}
		.l-layout-header-toggle {
		    cursor: pointer;
		    position: fixed;
		    font-size:17px;
		    z-index: 9999;
		}
		.baseline {
			position: absolute;
			border-radius: 2px;
			opacity: 0.6;
			margin-top: -7px;
			height: 12px;
			background: #ffd180;
			border: 1px solid rgb(255, 153, 0);
		}
		.panel-search{
		    background-color: rgb(255, 255, 255);
		}
		.tiezidrop{
			background: rgb(249, 249, 249);
		    padding-left: 10px;
		    padding-right: 5px;
		    position: absolute;
		    right: 0px;
		    top: -12px;
		    font-size: 13px;
		    cursor: pointer;
		   /*  color: rgb(241, 166, 116); */
		}
		TABLE.LayoutTable{
			WIDTH: 100%;
			border:0;margin:0;
			border-collapse:collapse;
		}
		
		TABLE.LayoutTable TR{
			height:40px;
		}
		TABLE.LayoutTable TR TD{
			text-align: center;
		}
		.advancedSearchDiv_show{
			  width: 100%;
			  height:0px;
			  position: fixed;
			  overflow: hidden;
			  z-index: 999;
			  background-color: #ffffff;
			  padding: 10px;
		}
		.advancedSearchDiv{
			  width: 100%;
			  height:0px;
			  position: fixed;
			  overflow: hidden;
			  z-index: 999;
			  background-color: #ffffff;
		}
		.label{
		    text-indent: 2em;
		}
		.row{
		   border: 1px solid  #dddddd;
		
		}
		.user{
		 cursor: pointer;
		}
	</style>
	<!-- 判断是否需要显示实际时间线 -->
	<c:choose>
		<c:when test="${showRealTimeLine eq true}">
			<style type="text/css">
				.gantt_task_line, .gantt_line_wrapper {
					margin-top: -9px;
				}
				.gantt_side_content {
					margin-bottom: -7px;
				}
				.gantt_task_link .gantt_link_arrow {
					margin-top: -12px;
				}
				.gantt_side_content .gantt_right {
					bottom: 0;
				}
			</style>
		</c:when>
		<c:otherwise>
		</c:otherwise>
	</c:choose>
	
	<script type="text/javascript">
		$(function(){
			var obj = $("#${ganttSiteId}");
			var pObj = obj.parent();
			var pObjHeight = pObj.height();
			obj.height(pObjHeight-$(".group_buttons").height());
			gantt.render();
		});
	</script>
	
</head>
<body>
	<div class="group_buttons">
	  <c:if test="${ButtonPermission['107']==true}">
	      <div class="module">
	          <span>模板操作</span>
	          <div class="custom_div">
	              <a class="custom_link custom_padding_3 import" onclick="importModel()">导入模板</a>
	              <a class="custom_link custom_padding_3 export" onclick="exportModel()">导出模板</a>
	          </div>
	          <div class="custom_div">
	             <a class="custom_link custom_padding_2 import" onclick="importExcel()">导入Excel</a>
	             <a class="custom_link custom_padding_2 export" onclick="exportExcel()">导出Excel</a>
	          </div>
	     </div>
	  </c:if>
	    <%--  <c:if test="${ButtonPermission['106']==true}"> --%>
		     <div class="module">
		          <span>批量操作</span>
		          <%-- <div class="custom_div">
	 	           <a class="custom_link custom_padding_3 icon-task" onclick="assignTask()">下发任务</a>
		            <a class="custom_link custom_padding_3 icon-task" onclick="alterPlan()">计划批量修改</a>
		          </div>
		          <c:if test="${zlchspzt=='已批复' }">
		          	<div class="custom_div">
		           		<a class="custom_link custom_padding_3 icon-task">任务变更</a>
		          	</div>
		          </c:if> --%>
		          <div class="custom_div">
	     		          <a class="custom_link moveup custom_padding_2" onclick="taskUp()">上移</a>
			              <a class="custom_link movedown custom_padding_2" onclick="taskDown()">下移</a>
		          </div>
		          <div class="custom_div">
		             <a class="custom_link moveleft custom_padding_2" onclick="taskLeft()">左移</a>
		             <a class="custom_link moveright custom_padding_2" onclick="taskRight()">右移</a>
		          </div>
		     </div>
	     <%-- </c:if> --%>
	     <div class="module">
	          <span>甘特图查看</span>
	          <div>
	             <div class="custom_div">
	                <a class="custom_link custom_padding_1 icon-search" id="queryId" onclick="openQueryDialog();">查询</a>
	                <ul>
	                   <li><a class="custom_link icon-iconfonthaofang26-copy-copy" onclick="initGantt('up')"></a></li>
	                   <li><a class="custom_link icon-circle1" onclick="initGantt('')"></a></li>
	                   <li><a class="custom_link icon-zhankai"onclick="initGantt('down')"></a></li>
	                </ul>
	             </div>

	             <div class="custom_div">
	                <a class="custom_link icon-move custom_padding_2" id="onChangeClick" onclick="toggleMode(this)">自适应</a>
		            <select name="selectScale" onchange="setScale(this.value)" title="设置显示标尺" class="szbc" lablename="选择显示标尺" controltype="select">
						<option value="year">年度</option>
						<option value="season">季度</option>
						<option value="month" selected="true">月度</option>
						<option value="week">周</option>
						<option value="day" >天</option>
			        </select>
	             </div>
	          </div>
	     </div>

	</div>
	<div class="advancedSearchDiv" name="advancedSearchDiv" id="advancedSearchDiv">
	 <form id="querysearchForm" name="querysearchForm" method="post"
			action="${ctx}/project/wbs/getWBSData.do">
		<input type="hidden" name="bbid" value="${bbid }">
		<input type="hidden" name="tableName" value="${tableName}">
		<input type="hidden" name="projectKey" val="${projectKey }">
		<input type="hidden" name="ident_task" val="1">
	    <table class="LayoutTable">
			<colgroup>
				<col width="15%">
				<col width="35%">
				<col width="15%">
				<col width="35%">
			</colgroup>
			<tbody>
				<tr class="row">
					<td><span class="label">任务名称:</span></td>
					<td style="text-align: left;"><input type="text" name="rwmc" class="inputText" id="searchContent"></td>
					<td><span class="label"><!-- 负责人: --></span></td>
					<td style="text-align: left;">
						<!-- <input type="text" name="assignedID" readonly="readonly" class="inputText user" onclick="selectAllUser({self:this});"> -->
						<!-- <input type="text" name="rwfzr" class="inputText" id="searchContent"> -->
					</td>
				</tr>
				<tr class="row">
					<td><span class="label">计划开始时间:</span></td>
					<td style="text-align: left;"><input type="text" name="startTime" dateFmt="yyyy-MM-dd" class="Wdate" ></td>
					<td><span class="label">计划结束时间:</span></td>
					<td style="text-align: left;"><input type="text" name="endTime" dateFmt="yyyy-MM-dd" class="Wdate"></td>
				</tr>
				<tr class="row">
					<td colspan="4">
						<div style="text-align: center;">
							<a href="####" class="button" onclick="searchRecord()"><!-- onclick="loadWBSBySearchArea()" -->
								<span class="icon ok"></span><span>搜索</span>
							</a>
							<a href="####" class="button" style="margin-left:10px;" onclick="clearQueryForm()"><span class="icon cancel"></span><span>清空</span></a>
						</div>
					</td>
				</tr>
	        </tbody>
	  </table>
	</form>
	</div>
	<div id="${ganttSiteId}" style='width:100%'></div>
	<script type="text/javascript">
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
        		url: __ctx+'/project/task/alterPlan.do?ids='+ids,
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
	    
		//导出Excel
		function exportExcel(){
			window.location.href = __ctx+'/project/data/expData.do?&dataId=${projectKey}&bbid=${bbid}';
		}
		//导入Excel
		function importExcel(){
			var url = __ctx+'/project/data/importDialog.do?&dataId=${projectKey}&bbid=${bbid}';
			url += "&templateName=项目计划模板&templateFile=项目计划模板";
			DialogUtil.open({
        		title:'导入Excel',
        		//height:$(window).height()*0.5,
        		//width: $(window).width()*0.5,
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
		
		
	   //鼠标绑定事件
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
	   //绑定双击事件
	   $("#${ganttSiteId}").on('click','.gantt_row .odd',function(){
		   
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
	   
	   //日期比较
	   function dateComparision(time1,time2){
		   time1 = time1.split("-");
		   time2 = time2.split("-");
		   time1 =  new Date(time1[0],time1[1],time1[2]);
		   time2 =  new Date(time2[0],time2[1],time2[2]);
		   return (time1<=time2);
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
				url:__ctx + '/project/splitWBSJob/splitJob.do',
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
	
		//是否需要显示实际时间线
		var showRealTimeLine = ${showRealTimeLine};
		var ctx = "${ctx}";
		var projectKey = "${projectKey}";
		var tableName = "${tableName}";
		var versionNumber = "";
		function getButtonPermission(){
			var operatePermissions = {};
			var bpStr = '${ButtonPermission}';
			if (bpStr == '') return operatePermissions;
			
			bpStr = bpStr.substring(1, bpStr.length-1);
			var bpStrs = bpStr.split(',');
			for (var i = 0; i < bpStrs.length; ++i){
				var bps = bpStrs[i].split('=');
				var bpCode = bps[0]+'';
				bpCode = bpCode.replace(/(^\s*)|(\s*$)/g, "");
				var bpValue = bps[1]==="true";
				operatePermissions[bpCode]=bpValue;
			}
			return operatePermissions;
		}
		
		var __ctx='<%=request.getContextPath()%>';
		var _operatePermissions = getButtonPermission();
		
		//导入xml模板文件
		function importModel(){
        	var selectedTaskId = gantt.getSelectedId();
			var taskidstr="";
			if(selectedTaskId!=null && selectedTaskId.substring(0,1)!="W"){
				taskidstr="&id="+selectedTaskId;
			}
			var paramValueString = "";
			CommonDialog("xzmb",function(data){
				var url="/ibms/project/wbs/importSave.do?projID=${projectKey}&mbId="+data.ID+taskidstr+"&versionNumber="+versionNumber;
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
			/* var selectedTaskId = gantt.getSelectedId();
			var taskidstr="";
			if(selectedTaskId!=null && selectedTaskId.substring(0,1)!="W"){
				taskidstr="&nodeID="+selectedTaskId;
			}
			var paramValueString = "";
			var height=500;
			var width=900;
			var url="/ibms/oa/form/dataTemplate/editData.do?__displayId__=2210000000220011&projID="+${projectKey}+taskidstr+"&versionNumber="+versionNumber;
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
        	}); */
			//var url="/ibms/project/wbs/Export2XML.do?projID=${projectKey}&nodeID="+selectedTaskId;
			//location.href=url;	
		}
		//导入excel文件
		function importEXL(){
			
		}
		//导出excel文件
		function exportEXL(){
			
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
					dataTemplateId:'${dataTemplateId}',
					typeAlias:'${typeAlias}'
				},
				async:false,
				success:function(){	
					DialogUtil.close();
					DialogUtil.success("任务下发成功");
				}
			})
		}
	    //质量审批
	   
	   
	   
	    //质量审批
	    var dataTemplateId = "${dataTemplateId}"
	    //附件管理
	    function manageFile(){
	    	/* var height=800;
			var width=1200;
			var lx=encodeURIComponent(encodeURIComponent("附件"));
			var url = "/ibms/project/querySetting/preview.do?__displayId__=30000000030055&xm_dataid="+${projectKey}+"&rw_dataid=&typeAlias=${typeAlias}&dataTemplateId=" + dataTemplateId+"&versionNumber="+versionNumber;
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
        	}); */
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
	            url : '${ctx}/project/wbs/exchangeTaskOrder.do',
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
		
		//2017-11-21 alter by chenliang
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
	            url : '${ctx}/project/wbs/exchangeTaskOrder.do',
	            data : {mydata:json},
	            async : false,
	            success:function(data){
	            }
	        })
		}
		
		//2017-11-21 alter by chenliang
		//任务左移
		function taskLeft(){
			var checkedTask=$("input[class='pk']:checked");
			if(checkedTask.size()<1){
				DialogUtil.warn("请先选择节点");
				return;
			}
			var firstID=$(checkedTask.get(0)).val();//第一个节点ID
			var firstLevel=gantt.getTask(firstID).$level;//第一个节点层级
			if(firstLevel==0){
				//DialogUtil.warn("跟节点无法左移");
				return;
			}
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
	            url : '${ctx}/project/wbs/exchangeTaskOrder.do',
	            data : {mydata:json},
	            async : false,
	            success:function(data){
	            }
	        })
		}
		
		
		//2017-11-21 alter by chenliang
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
	            url : '${ctx}/project/wbs/exchangeTaskOrder.do',
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
		
		var tasks;
		//加载数据
		function loadWBSData(){
			tasks = $.ajax({
	            url : '${ctx}/project/wbs/getWBSData.do',
	            data : {
	            	sjbName: "${sjbName}",
	            	sjbId: "${sjbId}",
	            	fcName: "${fcName}",
	            	fcId: "${fcId}"
	            },
	            async : false
	        }).responseText;
	        tasks = jQuery.parseJSON(tasks);
			gantt.parse(tasks);
		}
		
		//切换显示模式
		function toggleMode(toggle) {
			toggle.enabled = !toggle.enabled;
			if (toggle.enabled) {
				toggle.innerHTML = "取消自适应";
				saveConfig();
				zoomToFit();
			} else {
				toggle.innerHTML = "自适应";
				restoreConfig();
				gantt.render();
			}
		}
		
		function restoreConfig() {
			applyConfig(cachedSettings);
		}
		
		var cachedSettings = {};
		function saveConfig() {
			var config = gantt.config;
			cachedSettings = {};
			cachedSettings.scale_unit = config.scale_unit;
			cachedSettings.date_scale = config.date_scale;
			cachedSettings.step = config.step;
			cachedSettings.subscales = config.subscales;
			cachedSettings.template = gantt.templates.date_scale;
			cachedSettings.start_date = config.start_date;
			cachedSettings.end_date = config.end_date;
		}
		
		//自适应显示
		function zoomToFit() {
			var project = gantt.getSubtaskDates(),
				areaWidth = gantt.$task.offsetWidth;
			
			for (var i = 0; i < scaleConfigs.length; i++) {
				var columnCount = getUnitsBetween(project.start_date, project.end_date, scaleConfigs[i].unit, scaleConfigs[i].step);
				if ((columnCount + 2) * gantt.config.min_column_width <= areaWidth) {
					break;
				}
			}

			if (i == scaleConfigs.length) {
				i--;
			}
			
			applyConfig(scaleConfigs[i], project);
			gantt.render();
		}
		
		//设置比例尺
		function applyConfig(config, dates) {
			gantt.config.scale_unit = config.scale_unit;
			if (config.date_scale) {
				gantt.config.date_scale = config.date_scale;
				gantt.templates.date_scale = null;
			}
			else {
				gantt.templates.date_scale = config.template;
			}

			gantt.config.step = config.step;
			gantt.config.subscales = config.subscales;

			if (dates) {
				gantt.config.start_date = gantt.date.add(dates.start_date, -1, config.unit);
				gantt.config.end_date = gantt.date.add(gantt.date[config.unit + "_start"](dates.end_date), 2, config.unit);
			} else {
				gantt.config.start_date = gantt.config.end_date = null;
			}
		}
		
		// 获取时间间隔
		function getUnitsBetween(from, to, unit, step) {
			var start = new Date(from),
				end = new Date(to);
			var units = 0;
			while (start.valueOf() < end.valueOf()) {
				units++;
				start = gantt.date.add(start, step, unit);
			}
			return units;
		}
		
		//Setting available scales比例尺配置
		var scaleConfigs = [
			//minutes分钟级的比例尺
			{
				unit: "minute", step: 1, scale_unit: "hour", date_scale: "%H", 
				subscales: [
					{unit: "minute", step: 1, date: "%H:%i"}
				]
			},
			// hours小时级的比例尺
			{
				unit: "hour", step: 1, scale_unit: "day", date_scale: "%j %M",
				subscales: [
					{unit: "hour", step: 1, date: "%H:%i"}
				]
			},
			// days 精确到天的比例尺
			{
				unit: "day", step: 1, scale_unit: "month", date_scale: "%F",//%M
				subscales: [
					{unit: "day", step: 1, date: "%j %l"}
				]
			},
			// weeks 精确到周的比例尺
			{
				unit: "week", step: 1, scale_unit: "month", date_scale: "%F",
				subscales: [
					{
						unit: "week", step: 1, 
						template: function (date) {
							var dateToStr = gantt.date.date_to_str("%M %d");
							var endDate = gantt.date.add(gantt.date.add(date, 1, "week"), -1, "day");
							return dateToStr(date) + " - " + dateToStr(endDate);
						}
					}
				]
			},
			// months月级的比例尺
			{
				unit: "month", step: 1, scale_unit: "year", date_scale: "%Y",
				subscales: [
					{unit: "month", step: 1, date: "%M"}
				]
			},
			// quarters季级的比例尺
			{
				unit: "month", step: 3, scale_unit: "year", date_scale: "%Y",
				subscales: [
					{
						unit: "month", step: 3, 
						template: function (date) {
							var dateToStr = gantt.date.date_to_str("%M");
							var endDate = gantt.date.add(gantt.date.add(date, 3, "month"), -1, "day");
							return dateToStr(date) + " - " + dateToStr(endDate);
						}
					}
				]
			},
			// years年级的比例尺
			{
				unit: "year", step: 1, scale_unit: "year", date_scale: "%Y",
				subscales: [
					{
						unit: "year", step: 5, 
						template: function (date) {
							var dateToStr = gantt.date.date_to_str("%Y");
							var endDate = gantt.date.add(gantt.date.add(date, 5, "year"), -1, "day");
							return dateToStr(date) + " - " + dateToStr(endDate);
						}
					}
				]},
			// decades十年级的比例尺
			{
				unit: "year", step: 10, scale_unit: "year", template: function (date) {
					var dateToStr = gantt.date.date_to_str("%Y");
					var endDate = gantt.date.add(gantt.date.add(date, 10, "year"), -1, "day");
					return dateToStr(date) + " - " + dateToStr(endDate);
				},
				subscales: [
					{
						unit: "year", step: 100, template: function (date) {
							var dateToStr = gantt.date.date_to_str("%Y");
							var endDate = gantt.date.add(gantt.date.add(date, 100, "year"), -1, "day");
							return dateToStr(date) + " - " + dateToStr(endDate);
						}
					}
				]
			}
		];
		
		function setScale(scaleValue){
			/* var scaleValue = $("select[name='selectScale']").val(); */
			var scaleIndex = 0;
			if (scaleValue == 'year'){
				scaleIndex = 6;
			} else if (scaleValue == 'season'){
				scaleIndex = 5;
			} else if (scaleValue == 'month'){
				scaleIndex = 4;
			} else if (scaleValue == 'week'){
				scaleIndex = 3;
			} else if (scaleValue == 'day'){
				scaleIndex = 2;
			}
			applyConfig(scaleConfigs[scaleIndex]);
			gantt.render();
		}
		gantt.locale.date.day_full = ["周日", "周一", "周二", "周三", "周四", "周五", "周六"];
		applyConfig(scaleConfigs[4]);//初始化：显示到日
		
		
		//配置5：给连接线添加单击响应
		gantt.attachEvent("onLinkClick", function(id){
			var link = this.getLink(id),
				src = this.getTask(link.source),
				trg = this.getTask(link.target),
				types = this.config.links;
			
			var first = "", second = "";
			switch (link.type){
				case types.start_to_finish:
					first = "开始";
					second = "完成";
					break;
				case types.finish_to_start:
					first = "完成";
					second = "开始";
					break;
				case types.start_to_start:
					first = "开始";
					second = "开始";
					break;
				case types.finish_to_finish:
					first = "完成";
					second = "完成";
					break;
			}
			//gantt.message("<b>" + src.text + "</b> " + first + " <b>-></b> " + " <b>" + trg.text + "</b> " + second);
		});
		//配置6：
		gantt.config.lightbox.sections = [
			{name: "description", height: 70, map_to: "text", type: "textarea", focus: true},
			{name: "time", type: "duration", map_to: "auto"}
		];
		if(showRealTimeLine){
			gantt.config.lightbox.sections = [
			    {name: "description", height: 70, map_to: "text", type: "textarea", focus: true},
			    {name: "time", map_to: "auto", type: "duration"},
			    {name: "baseline", map_to: {start_date: "real_start_date", end_date: "real_end_date"}, button: true, type: "duration_optional"}
			];
		}
		
		gantt.attachEvent("onTaskLoading", function (task) {
			task.real_start_date = gantt.date.parseDate(task.real_start_date, "xml_date");
			task.real_end_date = gantt.date.parseDate(task.real_end_date, "xml_date");
			return true;
		});
		
		gantt.config.xml_date = "%Y-%m-%d %H:%i"; //任务日期的格式
        
		//配置：自定义列表
		gantt.config.columns = [
			{name:"text", label:"任务名称", tree:true, width:200},
			{
				name:"progress", label:"进度", width:60, align: "center",
    			template: function(item) {
        			if (item.progress >= 1)
            			return "已完成";
        			if (item.progress == 0)
            			return "未开始";
        			if (item.progress <= 0.1)
            			return "刚开始";
        			if (item.progress >= 0.9)
            			return "即将完成";
        			return Math.round(item.progress*100) + "%";
    			}
			},
			{
				name:"start_date", label:"计划开始时间", align: "center", width:90,
				template: function(item) {
        			return item.start_date;
    			}
			},
			{
				name:"end_date", label:"计划结束时间", align: "center", width:90,
				template: function(item) {
        			return item.end_date;
    			}
			},//resize:true
			/*{
				name:"duration", label:"计划工期", align: "center", width:60,
				template: function(item) {
        			return item.duration + "天";
    			}
			},
			 {
				name:"department", label:"责任部门", align: "center", width:100,
    			template: function(item) {
        			if (item.department == undefined) return "";
        			return item.department;
    			}
			},
			{
				name:"assigned", label:"负责人", align: "center", width:100,
    			template: function(item) {
        			if (item.assigned == undefined) return "";
        			return item.assigned;
    			}
			}, */
			{name:"add", width:40}
		];
		
		function getTaskFitValue(task){
			var taskStartPos = gantt.posFromDate(task.start_date),
				taskEndPos = gantt.posFromDate(task.end_date);
			
			var width = taskEndPos - taskStartPos;
			var textWidth = (task.text || "").length * gantt.config.font_width_ratio;
			
			if(width < textWidth){
				var ganttLastDate = gantt.getState().max_date;
				var ganttEndPos = gantt.posFromDate(ganttLastDate);
				if(ganttEndPos - taskEndPos < textWidth){
					return "left"
				}
				else {
					return "right"
				}
			}
			else {
				return "center";
			}
		}
		
		//动态调整任务名显示位置
		function autoAdjustTaskTextSite(){
			gantt.config.font_width_ratio = 7;
			gantt.templates.leftside_text = function leftSideTextTemplate(start, end, task) {
				if (getTaskFitValue(task) === "left") {
					return task.text;
				}
				return "";
			};
			gantt.templates.rightside_text = function rightSideTextTemplate(start, end, task) {
				if (getTaskFitValue(task) === "right") {
					return task.text;
				}
				return "";
			};
			gantt.templates.task_text = function taskTextTemplate(start, end, task){
				if (getTaskFitValue(task) === "center") {
					return task.text;
				}
				return "";
			};
		}
		autoAdjustTaskTextSite();
		
		gantt.config.quickinfo_buttons = [];
		//gantt.config.quick_info_detached = false;
		if (_operatePermissions['103']){
			gantt.config.quickinfo_buttons.push("icon_delete");
		}
		
		if (_operatePermissions['102']){
			gantt.config.quickinfo_buttons.push("icon_edit");
		}
		//合同节点、控制节点添加能够设置完成的功能
		gantt.locale.labels.icon_reset="还原";
		gantt.config.quickinfo_buttons.push("icon_reset");
		gantt.attachEvent("onBeforeShowOperPanel", function(id, showButtons){
			var ganttTask = gantt.getTask(id);
			var sjzt = ganttTask.sjzt;
			if(sjzt == "0"){
				//废弃状态if(_operatePermissions['114'])
				showButtons.push("icon_reset");
			}else{
				showButtons.push("icon_edit");
				showButtons.push("icon_delete");
			}
			return true;
		}); 
		
		gantt.config.quickinfo_buttons = ["icon_delete", "icon_edit"];
		gantt.templates.quick_info_title = function(start, end, ev){ return "<font size='4px'><b>操作</b></font>"; };
		gantt.templates.quick_info_content = function(start, end, ev){
			return "任务名称：<font>"+(ev.details || ev.text);+"</font>" 
			
		};
		gantt.templates.quick_info_date = function(start, end, ev){return "";};

		gantt.locale.labels.message_cancel = "取消";
		gantt.locale.labels.message_ok = "确定";
		
		//显示实际时间线
		if(showRealTimeLine){
			gantt.config.task_height = 16;
			gantt.config.row_height = 40;
			
			gantt.addTaskLayer(function draw_planned(task) {
	  			if (task.real_start_date) {
	  				var endDate = task.real_end_date;
	  				if (!endDate){
	  					endDate = new Date();
	  				}
	  				var sizes = gantt.getTaskPosition(task, task.real_start_date, endDate);
	  				var el = document.createElement('div');
	  				el.className = 'baseline';
	  				el.style.left = sizes.left + 'px';
	  				el.style.width = sizes.width + 'px';
	  				el.style.top = sizes.top + gantt.config.task_height + 13 + 'px';
	  				return el;
	  			}
	  			return false;
	  		});
			gantt.templates.task_class = function (start, end, task) {
				if (task.real_end_date) {
					var classes = ['has-baseline'];
					if (end.getTime() > task.real_end_date.getTime()) {
						classes.push('overdue');
					}
					return classes.join(' ');
				}
			};
		}
		
		 //页面初始化
        $(function(){
            loadWBSData();
        	sortByOrder();
        	ndkyBgColorInit();
        });
		function ndkyBgColorInit(){
			var isndky = false;
			$(".gantt_row").each(function(){
				   $(this).find(".gantt_cell").each(function(){
						var ndky = $(this).find(".gantt_tree_content").find('div').attr("ndky");
						if(ndky=="是"){
							isndky = true;
						}
				   });
				   if(isndky){
						$(this).css("background-color","lightgrey");//灰色
					   isndky = false;
				   }
			});
		}
        //任务排序
        function sortByOrder(){
        	gantt.sort(function(task1, task2){
            	var order1 = task1.order;
            	var order2 = task2.order;
            	if(order1.indexOf(".") != -1) order1 = order1.substr(order1.lastIndexOf(".") + 1);
            	if(order2.indexOf(".") != -1) order2 = order2.substr(order2.lastIndexOf(".") + 1);
            	return parseInt(order1) > parseInt(order2) ? 1 : (parseInt(order1) < parseInt(order2) ? -1 : 0);
            });
        }     

		//甘特图初始化前的定制代码
		customHandler.beforeGanttInitHandler(gantt, _operatePermissions,$(document).width()*0.6,'${projectKey}','${chspzt}',"${ctx}",'${fcId}',"${sjbId}");
		//初始化
		gantt.init("${ganttSiteId}");
		//甘特图初始化后的定制代码
        customHandler.afterGanttInitHandler(gantt, _operatePermissions);
		
        function initGantt(op){
        	var tempWidth;
        	if(op == 'up'){
        		tempWidth = 0;
        	}else if(op == 'down'){
        		tempWidth = $(document).width();
        	}else{
        		tempWidth = $(document).width()*0.8;
        	}
        	//甘特图初始化前的定制代码
    		customHandler.beforeGanttInitHandler(gantt, _operatePermissions,tempWidth);
    		//初始化
    		gantt.init("${ganttSiteId}");
    		//甘特图初始化后的定制代码
            customHandler.afterGanttInitHandler(gantt, _operatePermissions);
            sortByOrder();
        }
        
        //新增任务
        gantt.attachEvent("onTaskCreated", function(item){
			//判断当前任务状态
			/* var sjzt = gantt.getTask(item.parent).sjzt;
			if(sjzt == "0"){
				//废弃状态
				DialogUtil.warn("当前任务已废弃,请先还原!");
				return;
			} */
        	
        	var parentNodeId = item.parent;
        	var parentNode=gantt.getTask(parentNodeId);
        	var xh="";
        	 $.ajax({
 	            url : '${ctx}/project/wbs/getSubTaskBh.do',
 	         	async : false,
 	            data : {
 	            	curNodeId:parentNode.id,
 	            	sjbId: "${sjbId}",
 	            	curNodeOrder:parentNode.order
 	            },
 	            success:function(data){
 	            	//console.log(data);
 	            	xh=data;
 	            }
 	        });
        	
        	/* var parts = parentNodeId.split('&');
        	
        	var parentTask = '';
        	if (parts.length == 1){
        		parentTask = parentNodeId;
        	} */
        	
       // 	var displayId = customHandler.getDisplayId();
			//获取当前任务最大时间和最小时间
			/* var startDate = DateUtil.dateToStr("yyyy-MM-dd",gantt.getTask(item.parent).start_date);
			var endDate = DateUtil.dateToStr("yyyy-MM-dd",gantt.getTask(item.parent).end_date); */
			//sjbId: "${sjbId}",
	        //fcName: "${fcName}",
	        //fcId: "${fcId}"
			var url = "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000006660037"+
					"&sjbId=${sjbId}&fcId=${fcId}&parentNodeId="+parentNodeId+"&xh="+xh;
								
        	DialogUtil.open({
        		title:'新增工作计划',
        		height: $(window).height(),
        		width: $(window).width(),
        		url: url,
        		showMax: false,                             //是否显示最大化按钮 
        	    showToggle: false,                         //窗口收缩折叠
        	    showMin: false,
        		sucCall:function(rtn){
        			location.href=location.href.getNewUrl();
        			//loadWBSByVersion(versionNumber);
        		}
        	});
        });
        
        function getDate(strDate){ //YYYY-MM-DD HH:mm:ss
        	var st = strDate;
        	var a = st.split(" ");
        	var b = a[0].split("-");
        	var c = a[1].split(":");
        	var date = new Date(b[0], b[1]-1, b[2], c[0], c[1], c[2]);
        	return date;
        }
        
      
        //编辑任务
        function editTask(id){
			var url = "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000006660037"+"&__pk__="+id;	
        	openWindow(url,"任务编辑");
        }
        
        //修改任务
        var taskIds = "";
        function deleteTask(id, prompt, editState){
        	var title = "状态修改";        
        	parent.$.ligerDialog.confirm(prompt, title, function(rtn){
            	if (rtn) { 
         		if(!gantt.isTaskExists(id)){
 					gantt.hideLightbox();
 					return;
 				}
         		deleteTaskAndChildren(id);
 				editTaskState(taskIds, editState);
 			    location.href=location.href.getNewUrl();
            	}
            	
            }); 
        }
        //递归获取所有任务节点
        function deleteTaskAndChildren(taskId){
        	var children = gantt.getChildren(taskId);
        	if (children.length > 0){
        		for (var i=0; i < children.length; ++i){
        			var childTaskId = children[i];
        			deleteTaskAndChildren(childTaskId);
        		}
        	}
			if(taskId.indexOf("&") == -1){
				taskIds += taskId + ",";
			}
			return taskIds;
        }
        /**修改节点任务状态*/
        function editTaskState(taskIds, editState){
        	$.ajax({
    			url : '${ctx}/project/task/editTaskState.do',
    			data : {
    				taskIds : taskIds,
    				editState : editState
    			},
    			async : false
			});
        }
        
        
        gantt.$click.buttons["edit"] = function(id){
        	editTask(id);
        }
        
    	//任务变更过滤
		function operationFilter(id,operation){
    		var chspzt = '${chspzt}';
    		var F_XMID = '${projectKey}';
    		var F_RWID = id;
    		if(chspzt != "已审批"){
    			return true;
    		}
    		var ifZGXM = jQuery.getParameter("ifZGXM");//是否是主管设计师或项目负责人
    		var ganttTask = gantt.getTask(id);
    		var startDate = DateUtil.dateToStr("yyyy-MM-dd",ganttTask.start_date);
			var endDate = DateUtil.dateToStr("yyyy-MM-dd",ganttTask.end_date);
    		var taskType = ganttTask.taskType;
    		var rwfzr = ganttTask.assigned;
    		var rwfzrId = ganttTask.assignedId;
    		var ndkyscjh = ganttTask.ndkyscjh;
    		if(ifZGXM == "true"){
				if(taskType == "阶段" || ndkyscjh == "是"){
					//走流程
					var url = "${ctx}/oa/flow/task/startFlowForm.do?__displayId__=10000014160191"
							+ "&__dbomFKValue__="+F_XMID+"&bglx=阶段任务变更"+"&F_RWFZR="+rwfzr
							+ "&F_JHKSSJ="+startDate+"&F_JHJSSJ="+endDate+"&F_RWID="+F_RWID+"&defId=10000014440144";
					openWindow(url,"任务变更");
				}else{
					//不走流程                        
					var url = "${ctx}/oa/form/dataTemplate/editData.do?__displayId__=10000014160191"
							+ "&__dbomFKValue__="+F_XMID+"&bglx=普通任务变更"+"&F_RWFZR="+rwfzr
							+ "&F_JHKSSJ="+startDate+"&F_JHJSSJ="+endDate+"&F_RWID="+F_RWID+"&falg=true";
					openWindow(url,"任务变更");
				}
			}else{
				if(taskType == "阶段" || ndkyscjh == "是"){
					$.ligerDialog.warn("阶段任务/年度科研生产计划无权限"+operation);
				}else{
					//走流程
					var url = "${ctx}/oa/flow/task/startFlowForm.do?__displayId__=10000014160191"
						+ "&__dbomFKValue__="+F_XMID+"&bglx=普通任务变更"+"&F_RWFZR="+rwfzr
						+ "&F_JHKSSJ="+startDate+"&F_JHJSSJ="+endDate+"&F_RWID="+F_RWID+"&defId=10000014440144";
					openWindow(url,"任务变更");
				}
			}
			return false;
		}
    	
    	function openWindow(url,title){
    		DialogUtil.open({
        		title: title,
        		height: $(window).height(),
        		width: $(window).width(),
        		url: url,
        		showMax: false,                             //是否显示最大化按钮 
        	    showToggle: false,                         //窗口收缩折叠
        	    showMin: false,
        		sucCall:function(rtn){
        			location.href=location.href.getNewUrl();
        			//loadWBSByVersion(versionNumber);
        		}
        	});
    	}
    	
        gantt.$click.buttons["delete"] = function(id){
        	/* if(!operationFilter(id,"删除")){
        		return;
        	} */
        /* 	var lock = false;
        	var num = 0;
        	var taskIdList =  "";
        	var level;
        	var currentBH;
        	$(".gantt_row").each(function(){
        		if(lock){
        			if(level<=$(this).attr("aria-level")){
        				taskIdList += $(this).attr("task_id")+",";
        			}else{
        				lock = false;
        			}
        		}
        		if($(this).attr("task_id")==id){
        			level = $(this).attr("aria-level");
        			currentBH = $(this).find(".gantt_tree_bh").text();
        			lock = true;
        		}
        	}); */
        	var question = "任务将被永久删除，是否确认？";
            var title = "删除确认";
            $.ligerDialog.confirm(question,title,function (rtn){
				
				if(rtn){
					var result = $.ajax({
            			//deleteData
        	            url : '${ctx}/oa/form/dataTemplate/deleteData.do?',
        	            data : {
        	            	__displayId__:'10000006660037',
        	            	__pk__:id
        	            },
        	            async : false
        	        }).responseText;     
                	location.href=location.href;
				}
			});
            /* DialogUtil.confirm_Title(title,question, function(rtn){
            	if (rtn) {
            		var result = $.ajax({
            			//deleteData
        	            url : '${ctx}/oa/form/dataTemplate/deleteData.do?',
        	            data : {
        	            	__displayId__:'10000006660037',
        	            	__pk__:id
        	            },
        	            async : false
        	        }).responseText;     
                	location.href=location.href;
            	}
            }); */
        }
        
      //任务详情
      function dblClickTask(id){
    	
     	var url = "";			
			DialogUtil.open({
				title:'任务详情',
				height: $(window).height(),
				width: $(window).width(),
				url: url,
				showMax: true,                             //是否显示最大化按钮 
	    		showToggle: false,                         //窗口收缩折叠
	    		showMin: false,
				sucCall:function(rtn){
					location.href=location.href.getNewUrl();
				}
			});
      }
        //设置合同节点或控制节点完成
        function finishTask(taskId){
        	
        	var question = "是否确定更改该节点的完成状态？";
            var title = "确认";
            //gantt._dhtmlx_confirm(question, title, function(){
            parent.$.ligerDialog.confirm(question, title, function(rtn){
            	if (rtn) {
        			//修改 进度，状态，实际开始时间和实际结束时间
            		$.ajax({
        	     		url : __ctx + '/project/task/finishTask.do',
        	     		data : {
        	    	 		taskId: taskId
        	     		},
        	     		async : true,
        	     		success:function(sjjssj){	
        	     			DialogUtil.success("设置节点完成状态成功！", "提示", function(rtn){
    							if(rtn){
    								var ganttTask = gantt.getTask(taskId);
    	        	     			
    	        	     			ganttTask.real_start_date = getDate(sjjssj+" 00:00:00");
    	            				ganttTask.real_end_date = getDate(sjjssj+" 23:59:59");
    	            				ganttTask.progress = 1;
    	            				ganttTask.realDuration = "1";
    	        	     			
    	        	     			gantt.updateTask(taskId, ganttTask);
    	            				gantt.refreshData();
    							}
    						});
        	     		},
    					error:function(){
    						//parent.$.ligerDialog.closeWaitting();
    						//parent.$.ligerDialog.success("导入失败");
    					}
        			});
            	}
            });
        }
        
        gantt.config.drag_progress = false; //禁止拖拉进度
        if (!_operatePermissions['editTask']){
        	gantt.config.drag_resize = false; //禁止移动开始或结束时间
        	gantt.config.drag_move = false; //禁止整体平移
        }
        //拖拉改变时间
        gantt.attachEvent("onAfterTaskDrag", function(id, mode){
			var task = gantt.getTask(id);
			
			if(mode == gantt.config.drag_mode.progress){ //改变进度（已禁用）
				/* var pr = Math.floor(task.progress * 100 * 10)/10;
				gantt.message(task.text + " is now " + pr + "% completed!"); */
				
			}else if(mode == gantt.config.drag_mode.resize || //移动开始或结束时间
					mode == gantt.config.drag_mode.move){ //整体平移
				
				var convert = gantt.date.date_to_str("%Y-%m-%d");
				var startDate = convert(task.start_date);
				var endDate = convert(task.end_date);
				
				var result = {jhkssj:startDate, jhjssj:endDate};
				var saveData = JSON.stringify(result);
				
				$.ajax({
        			url : '${ctx}/project/task/editTask.do',
        			data : {
        				taskId : id,
        				saveData : saveData
        			},
        			async : false
    			});
			}
		});
        
     
        gantt.config.drag_links = true;
        //新增连接线
        gantt.attachEvent("onBeforeLinkAdd", function(linkId, link){
        	
        	var newTaskLinkId = $.ajax({
    			url : '${ctx}/project/wbs/addTaskLink.do',
    			data : {
    				tableName : '${tableName}',
    				projectKey : '${projectKey}',
    				sourceId : link.source,
    				targetId : link.target,
    				type : link.type
    			},
    			async : false
			}).responseText;
        	
        	link.id = newTaskLinkId;
        	return true;
        });
		//删除连接线
        gantt.attachEvent("onBeforeLinkDelete", function(linkId, link){
        	if(linkId.indexOf("LINK&")<0){
        		linkId = "LINK&" + linkId.substring(1,linkId.length-1);
        	}
        	$.ajax({
    			url : '${ctx}/project/wbs/deleteTaskLink.do',
    			data : {
    				taskLinkId : linkId
    			},
    			async : false
			});
        	return true;
        });
        
		//点击日期标尺，纵向高亮
        var selected_column = null;
        gantt.attachEvent("onScaleClick", function (e, date) {
    		selected_column = date;
    		var pos = gantt.getScrollState();
    		gantt.render();
    		gantt.scrollTo(pos.x, pos.y);
    	});
        
        function is_selected_column (column_date){
        	if(selected_column && column_date.valueOf() == selected_column.valueOf()){
        		return true;
    		}
    		return false;
    	}
        var ndkyKG = true;
        var ndkyKG2 = true;
    	gantt.templates.scale_cell_class = function (date) {
    		ndkyKG = true;
    		if(is_selected_column(date))
    			return "highlighted-column";
    	};
    	gantt.templates.task_cell_class = function (item, date) {
    		if(ndkyKG){
    			if(item.taskType==undefined){
    				ndkyBgColorInit();
    			}else if(item.taskType!=undefined && ndkyKG2){
    				ndkyKG2 = false;
    			}else{
    				ndkyBgColorInit();
    			}
    			ndkyKG = false;
    		}
    		if(is_selected_column(date))
    			return "highlighted-column";
    	}
	</script>
</body>
</html>

