<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>包络分析</title>
<%@include file="/commons/include/form.jsp" %>

<style type="text/css">
 	.tree-title{overflow:hidden;width:100%;} 
	html,body{ padding:0px; margin:0; width:100%;height:100%;overflow: hidden;}	
	
	.p-body {display: none;width: 100%;}
	.p-detail {width: 100%; height: 100%;float: left;overflow: auto;}
	
	.check {margin-bottom: 10px; height: 30px;}
	.check .group {
		width: 65px;
		height: 20px; 
		border: 1px solid #48A1E4;
		text-align: center;
		margin-left: 0;
		color: #2787d4;
		border-radius: 3px;
		cursor: pointer;
	}
	.check .group:Hover {background: #3eaaf5;color: #fff!important}
	.required {color: red;}
	
	table {width: 100%;border: 1px solid #dddddd;}
	table tr {height: 42px; line-height: 42px;}
	table td,table th {text-align: center;}
	table td {
		border-right:1px dashed rgb(206, 206, 206);
		border-bottom:1px dashed rgb(206, 206, 206);
	}
	table th {
		border-right: 1px dotted #fff; 
		border-bottom: none; 
		color: #fff;
		font-weight: bold;
		background: #3eaaf5;
		font-size: 16px;
		white-space: nowrap;
	}
	
	.chart {display: none; width: 50%; height: 100%; float: right; overflow: auto;}
	#iecharts {width: 100%; height: 100%;max-width: 100%; max-height: 500px;}
	.xecharts {display: none; width: 100%; height: 100%;}
</style>

</head>
<body>
	<div class="panel">
		<div class="panel-toolbar">
			<div class="toolBar">
		      <div class="group"><a class="link search" href="####" onclick="javascript:query();">查询</a></div>
		      <div class="group"><a class="link reset" href="####" onclick="javascript:reset();">重置</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markSuccess();">标记为成功</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markFailure();">标记为失败</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markAnalyze();">标记为待分析</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:IControlChart();">单值控制图</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:XControlChart();">均值控制图</a></div>
		    </div>
	  	</div>
	  	
	  	<div class="panel-search xh">
			<div class="title">型号<span class="required"> *</span></div>
			<div class="drop">
				<a class="activi">收起</a>
			</div>
			
			 <form id="searchForm3" name="searchForm1" class="searchForm">
			 	<div class="check">
					 <div class="group" onclick="javascript:check(this);">全选</div>
					 <div class="group" onclick="javascript:unCheck(this);">取消全选</div>
					 <div class="group" onclick="javascript:getProject();">对应发次</div>
				</div>
			
			 	<ul class="row">	
			 		<c:forEach var="productInfo" items="${productInfo}">
			 			<li>
							<span>
								<input type="checkbox" name="checkbox" value="${productInfo.ID}" />
							</span>
							<lable>${productInfo.F_XHMC}</lable>
						</li>
			 		</c:forEach>
				</ul>
			</form> 
	  	</div>
	  	
	  	<div class="panel-search fc">
			<div class="title">发次<span class="required"> *</span></div>
			<div class="drop">
				<a class="activi">收起</a>
			</div>
			 <form id="searchForm1" name="searchForm1" class="searchForm">
			 	<div class="check">
					 <div class="group" onclick="javascript:check(this);">全选</div>
					 <div class="group" onclick="javascript:unCheck(this);">取消全选</div>
				</div>
				<ul class="row"></ul>
			</form> 
	  	</div>
	  	
	  	<div class="p-body">
	  		<div class="p-detail">
  				<table>
	  				<thead class="fixedThead">
						<tr class="th">
							<th><input type="checkbox" name="td-checkbox"/></td>
							<th>序号</th>
							<th>型号</th>
							<th>发次</th>
							<th>发次状态</th>
							<th>名称</th>
							<th>简称</th>
							<th>描述</th>
							<th>实测值</th>
							<th>包络分类</th>
						</tr>
					</thead>
					<tbody class="tbody"></tbody>
				</table>
			</div>
			<div class="chart">
				<div id="iecharts"></div>
				<div id="xecharts-r" class="xecharts"></div>
				<div id="xecharts-a" class="xecharts"></div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="${ctx}/jslib/echarts/echarts.min.js"></script>
	<script type="text/javascript">	

	$(function() {
		//查询条件展开收起 
		foldBox();
		getHeight();
	})	
	
	window.onresize = function() {
		getHeight();
	}
	
	//表格高度 
	function getHeight() {
		var height = $(".panel").height()-$(".panel-toolbar").height()-$(".panel-search.fc").height()
			-$(".panel-search.xh").height()-100;
		$(".p-body").height(height);
	}
	//重置查询条件 
	function reset() {
		$("#searchForm1")[0].reset();
		$("#searchForm2")[0].reset();
		$("#searchForm3")[0].reset();
		$('input[name="checkbox"]').removeAttr("checked");
	}
	//全选 
	function check(obj) {
		$(obj).parents(".panel-search").find('input[name="checkbox"]').attr("checked", "true");
	}
	//取消全选 
	function unCheck(obj) {
		$(obj).parents(".panel-search").find('input[name="checkbox"]').removeAttr("checked");
	}
	
	//标记为成功 
	function markSuccess() {
		$("table tr.record").find('input[name="td-checkbox"]:checked').each(function() {
			$(this).parents("tr").find(".deaType").text("成功");
		})
	}
	//标记为失败 
	function markFailure() {
		$("table tr.record").find('input[name="td-checkbox"]:checked').each(function() {
			$(this).parents("tr").find(".deaType").text("失败");
		})
	}
	//标记为待分析  
	function markAnalyze() {
		$("table tr.record").find('input[name="td-checkbox"]:checked').each(function() {
			$(this).parents("tr").find(".deaType").text("待分析");
		})
	}
	//查询条件展开收起 
	function foldBox() {
		$(".drop").click(function() {
			if ($(this).find("a").hasClass("activi")) {
				$(this).find("a").text('展开');
				$(this).find('a').removeClass('activi')
				$(this).parent().find(".searchForm").hide();
			} else {
				$(this).find("a").text('收起');
				$(this).find('a').addClass('activi')
				$(this).parent().find(".searchForm").show();
			}
			getHeight();
		})
	}
	//表格复选框
	function tdCheckEvent() {
		$(".th").find('input[name="td-checkbox"]').click(function() {
			if (this.checked) {
				$("table").find('input[name="td-checkbox"]').attr("checked", "true");
			} else {
				$("table").find('input[name="td-checkbox"]').removeAttr("checked");
			}
		})
	}
	//所选型号 
	function getProductId() {
		var productIds = [];
		$(".panel-search.xh").find('input[name="checkbox"]:checked').each(function() {
			productIds.push(this.value);
		})
		if (productIds.length == 0) {
			alert("请选择型号！");
		} else if (productIds.length > 0){
			return productIds;
		}
	}
	
	//所选发次 
	function getProjectId() {
		//所选发次 
		var projectId = [];
		$(".panel-search.fc").find('input[name="checkbox"]:checked').each(function() {
			projectId.push(this.value);
		})
		if (projectId.length == 0) {
			alert("请选择发次！");
		} else if (projectId.length > 0){
			return projectId;
		}
	}
	
	function getProject() {
		$.ajax({
			async: 'false',
			method: 'post',
			dataType: 'json',
			data: {
				productId: getProductId().toString()
			},
			url: "${ctx}/dataPackage/dea/getProject.do",
			success: function(data) {
				$(".panel-search.fc").find("ul.row").empty();
				for (var i = 0; i < data.length; i++) {
					var html = "<li><span>";
					html += '<input type="checkbox" name="checkbox" value="' + data[i].ID + '" />';
					if (getProductId().length > 1) {
						html += '</span><lable>' + data[i].F_FCMC + "(" + data[i].F_XHMC + ')</lable>';
					} else {
						html += '</span><lable>' + data[i].F_FCMC + '</lable>';
					}
					html += '</li>';
					$(".panel-search.fc").find("ul.row").append(html);
				}
			},
			error: function() {
				alert("发次加载出错！");
			}
		})
	}
	
	function query() {
		//收起查询条件
		$(".drop").find("a").text('展开');
		$(".drop").find('a').removeClass('activi')
		$(".drop").parent().find(".searchForm").hide();
		getHeight();
		
		$("table tr.record").remove();
		//查询结果 
		$.ajax({
			async: 'false',
			method: 'post',
			dataType: 'json',
			data: {
				productId: getProductId().toString(),
				projectId: getProjectId().toString(),
				ckResultId: "${ckResultId}",
				slid: "${slid}"
			},
			url: "${ctx}/dataPackage/dea/queryByFrom.do",
			success: function(data) {
				$("table tr.record").remove();
				for(var i=0; i< data.length; i++) {
					var html = "<tr class='record'>";
					html += "<td><input type='checkbox' name='td-checkbox'/></td>";
					html += "<td>" + (i+1) + "</td>";
					var deaType = "";
					for (var key in data[i]) {
						if (key == "F_VALUE") {
							html += "<td class='value'>" + (data[i][key]==null?"":data[i][key]) + "</td>";
						} else if (key == "F_DESCRIPTION") {
							html += "<td class='description'>" + (data[i][key]==null?"":data[i][key]) + "</td>";
						} else {
							html += "<td>" + (data[i][key]==null?"":data[i][key]) + "</td>";
						}
						
						if (key == "F_FCZT" && data[i][key] == "已飞") {
							deaType = "成功";
						} else if (key == "F_FCZT" && data[i][key] == "未飞") { 
							deaType = "待分析";
						}
					}
					html += "<td class='deaType'>" + deaType + "</td>";
					html += "</tr>";
					$("table .tbody").append(html);
				}

				$(".p-detail").css("width","100%");
				$(".chart").hide();
				$(".p-body").show();
				tdCheckEvent();
			},
			error: function() {
				alert("查询出错！");
			}
		})
	}
	
	//单值控制图
	function IControlChart() {
		var succData = [], analyData = [];
		var record = $("table tr.record");
		for (var i=0; i<record.length; i++) {
			var deaType = $(record[i]).find('.deaType').text();
			var value = $(record[i]).find('.value').text();
			if (deaType == "成功") {
				succData.push(value);
			} else if (deaType == "待分析") {
				analyData.push(value);
			}
		}
		
		if ((succData.length < 8) && (succData.length > 0)) {
			if (!confirm("成功数据样本量<8，不适合单值控制图分析，是否继续？")) {
				return false;
			}	
		} else if (succData.length <= 0){
			alert("没有成功数据，不能进行包络分析！");
			return false;
		}
			
		$.ajax({
			async: 'false',
			method: 'post',
			dataType: 'json',
			data: {
				succData: succData.toString(),
			},
			url: "${ctx}/dataPackage/dea/getIControlChart.do",
			success: function(data) {
				getIControlChart(data, succData, analyData);
			},
			error: function() {
				alert("单值控制图加载失败！");
			}
		})
		$(".p-detail").css("width","50%");
		$("#iecharts").show();
		$(".xecharts").hide();
		$(".chart").show();
	}
	//单值控制图 
	function getIControlChart(result, succData, analyData) {
		var avg = [], upperLimit = [], lowerLimit = [];
		avg.push(result.avg);
		upperLimit.push(result.upperLimit);
		lowerLimit.push(result.lowerLimit);
		
		//待分析值超出范围值-标红 
		for (var j=0; j<analyData.length; j++) {
			if (Number(analyData[j]) > Number(result.upperLimit) || Number(analyData[j]) < Number(result.lowerLimit)) {
				var record = $("table tr.record");
				for (var i=0; i<record.length; i++) {
					if (($(record[i]).find('.value').text() == analyData[j]) && $(record[i]).find('.deaType').text() == "待分析") {
						$(record[i]).find('.value').css("color", "red");
					}
				}
			}
		}
		
		//x轴坐标 
		var length = succData.length > analyData.length ? succData.length : analyData.length;
		var xAxis = [];
		for (var i=0; i<length; i++) {
			xAxis.push(i+1);
		}
		
		$("#iecharts").empty();
		var myChart = echarts.init(document.getElementById("iecharts"));
		var option = {
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: ['待分析数据', '历史成功数据', '成功均值', '最大包络UB', '最小包络LB'],
					orient: 'vertical',
					right: '2%',
					top: '10%'
				},
				grid: {
					right: '22%',
					top: '10%',
					left: '10%',
					bottom: '10%',
					containLabel: true
				},
				toolbox: {
					show: 'true',
					feature: {
						saveAsImage: {}
					},
					right: '2%'
				}, 
				xAxis: {
					type: 'category',
					data: xAxis,
					name: '测试点',
					nameLocation: 'middle',
					nameGap: '35'
				},
				yAxis: {
					type: 'value',
					name: '实测值',
					nameLocation: 'middle',
					nameGap: '35'
				},
				series: [
				    {
						name: '待分析数据',
						type: 'line',
						data: analyData
					},
					{
						name: '历史成功数据',
						type: 'line',
						data: succData
					},
					{
						name: '成功均值',
						type: 'line',
						data: avg,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: '成功均值'
							    },
							]
						}
					},
					{
						name: '最大包络UB',
						type: 'line',
						data: upperLimit,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: '最大包络UB'
							    },
							]
						}
					},
					{
						name: '最小包络LB',
						type: 'line',
						data: lowerLimit,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: '最小包络LB'
							    },
							]
						}
					}
				]
		}
		myChart.setOption(option);
	}
	//均值控制图
	function XControlChart() {
		var groupsNameSuc = [], groupsValueSuc = {}, groupsNameAnaly = [], groupsValueAnaly = {};
		var record = $("table tr.record");
		
		for (var i=0; i<record.length; i++) {
			var deaType = $(record[i]).find('.deaType').text();
			var value = $(record[i]).find('.value').text();
			var description = $(record[i]).find('.description').text();
			
			if (deaType == "成功") {
				if (jQuery.inArray(description, groupsNameSuc) < 0) {
					groupsNameSuc.push(description);
				}
			} else if (deaType == "待分析") {
				if (jQuery.inArray(description, groupsNameAnaly) < 0) {
					groupsNameAnaly.push(description);
				}
			}
		}
		
		//成功数据-分组值-按描述分组 
		for (var i=0; i<groupsNameSuc.length; i++) {
			var groupValueSuc = [];
			for (var j=0; j<record.length; j++) {
				var description = $(record[j]).find('.description').text();
				var deaType = $(record[j]).find('.deaType').text();
				
				if ((deaType == "成功") && (description == groupsNameSuc[i])) {
					groupValueSuc.push($(record[j]).find('.value').text());
				}
			}
			groupsValueSuc[i] = groupValueSuc;
		}
		
		//待分析数据-分组值-按描述分组  
		for (var i=0; i<groupsNameAnaly.length; i++) {
			var groupValueAnaly = [];
			for (var j=0; j<record.length; j++) {
				var description = $(record[j]).find('.description').text();
				var deaType = $(record[j]).find('.deaType').text();
				
				if ((deaType == "待分析") && (description == groupsNameAnaly[i])) {
					groupValueAnaly.push($(record[j]).find('.value').text());
				}
			}
			groupsValueAnaly[i] = groupValueAnaly;
		}
		
		if (JSON.stringify(groupsValueSuc) == "{}"){
			alert("没有成功数据，不能进行包络分析！");
			return false;
		}

		$.ajax({
			async: 'false',
			method: 'post',
			dataType: 'json',
			data: {
				sucData: JSON.stringify(groupsValueSuc),
				analyData: JSON.stringify(groupsValueAnaly)
			},
			url: "${ctx}/dataPackage/dea/getXControlChart.do",
			success: function(data) {
				getXControlChart(data);
			},
			error: function() {
				alert("均值控制图加载失败！");
			}
		})
		$(".p-detail").css("width","55%");
		$("#iecharts").hide();
		$(".xecharts").show();
		$(".chart").show();
	}
	
	//均值-极差包络范围图
	function getRangeChart(result) {
		var upperRBArr = [50], lowerRLArr = [10], avgRArr = [30];
		
		var rByGroupSuc = result.rByGroupSuc;
		var rByGroupSucA = rByGroupSuc.split(",");
		var rByGroupAnaly = result.rByGroupAnaly;
		var rByGroupAnalyA;
		if (rByGroupAnaly == null) {
			rByGroupAnalyA = []
		} else {
			rByGroupAnalyA = rByGroupAnaly.split(",");
		}
		
		/* var upperRB = result.upperRB;
		var lowerRL = result.lowerRL;
		var avgR = result.avgR;

		upperRBArr.push(upperRB);
		lowerRLArr.push(lowerRL);
		avgRArr.push(avgR); */
		
		//x轴坐标 
		var length = rByGroupSucA.length > rByGroupAnalyA.length ? rByGroupSucA.length : rByGroupAnalyA.length;
		var xAxis = [];
		for (var i=0; i<length; i++) {
			xAxis.push(i+1);
		}
		
		$("#xecharts-r").empty();
		var myChart = echarts.init(document.getElementById("xecharts-r"));
		var option = {
				tooltip: {
					trigger: 'axis'
				},
				legend: {
					data: ['R均值', 'RB', 'RL', '成功数据极差', '待分析数据极差'],
					orient: 'vertical',
					right: '0',
					top: '10%'
				},
				grid: {
					right: '22%',
					top: '10%',
					left: '10%',
					bottom: '10%',
					containLabel: true
				},
				xAxis: {
					type: 'category',
					data: xAxis,
					name: '子组号',
					nameLocation: 'middle',
					nameGap: '35'
				},
				yAxis: {
					type: 'value',
					name: '子组极差值及待分析极差',
					nameLocation: 'middle',
					nameGap: '55'
				},
				series: [
				    {
						name: 'R均值',
						type: 'line',
						data: avgRArr,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: 'R均值'
							    },
							]
						}
					},
					{
						name: 'RB',
						type: 'line',
						data: upperRBArr,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: 'RB'
							    },
							]
						}
					},
					{
						name: 'RL',
						type: 'line',
						data: lowerRLArr,
						symbol: 'none',
						markLine: {
							data: [
							    {
							    	type: 'average', 
							    	name: 'RL'
							    },
							]
						}
					},
					{
						name: '成功数据极差',
						type: 'line',
						data: rByGroupSucA
					},
					{
						name: '待分析数据极差',
						type: 'line',
						data: rByGroupAnalyA
					}
				]
		}
		myChart.setOption(option);
	}
	//均值-均值包络范围图 
	function getAvgChart(result) {
		
	}
	//均值控制图 
	function getXControlChart(result) {
		getRangeChart(result);
	}
</script> 
</body>
</html>