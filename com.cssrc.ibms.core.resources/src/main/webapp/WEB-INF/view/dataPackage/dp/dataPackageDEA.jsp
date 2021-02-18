<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>包络分析</title>
<%@include file="/commons/include/form.jsp"%>

<style type="text/css">
.tree-title {
	overflow: hidden;
	width: 100%;
}

html, body {
	padding: 0px;
	margin: 0;
	width: 100%;
	height: 100%;
	overflow: hidden;
}

.check {
	margin-bottom: 10px;
	height: 30px;
}

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

.pagging {
	width: 65px;
	height: 20px;
	border: 1px solid #48A1E4;
	text-align: center;
	margin-left: 0;
	color: #2787d4;
	border-radius: 3px;
	cursor: pointer;
}

.check .group:Hover {
	background: #3eaaf5;
	color: #fff !important
}

.page-button {
	width: 65px;
	height: 20px;
	border: 1px solid #48A1E4;
	text-align: center;
	margin-left: 0;
	color: #2787d4;
	border-radius: 3px;
	cursor: pointer;
}
.p-detail .page-button:Hover{
    background: #3eaaf5;
    color: #fff !important
}

.required {
	color: red;
}

.p-body {
	display: none;
	width: 100%;
}

.p-detail {
	width: 100%;
	float: left;
	overflow: auto;
}

table {
	width: 100%;
	/* border: 1px solid #dddddd; */
}

table tr {
	/* height: 42px; */
	line-height: 42px;
}

table td, table th {
	text-align: center;
}

table td {
	border-right: 1px dashed rgb(206, 206, 206);
	border-bottom: 1px dashed rgb(206, 206, 206);
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

/* .chart {display: none; width: 100%; height: 400px;} 
	#iecharts{width: 100%; height: 100%;}*/
#iecharts {
	display: inline-block;
	width: 98%;
	height: 400px;
}
</style>
<script type="text/javascript"
	src="${ctx}/jslib/echarts/chart/echarts.min.js"></script>
</head>
<body>
	<div class="panel" style="overflow: auto;">
		<div class="panel-toolbar">
			<div class="toolBar">
				<!-- <div class="group"><a class="link search" href="####" onclick="javascript:query();">查询</a></div>
		      <div class="group"><a class="link reset" href="####" onclick="javascript:reset();">重置</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markSuccess();">标记为成功</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markFailure();">标记为失败</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:markAnalyze();">标记为待分析</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:IControlChart();">单值控制图</a></div>
		      <div class="group"><a class="link" href="####" onclick="javascript:XControlChart();">均值控制图</a></div> -->
			</div>
		</div>

		<div class="panel-search xh">
			<div class="title">
				型号 <a style="color: red">*</a>
			</div>


			<form id="moduleList" name="moduleList" class="searchForm">
				<!-- <div class="check">
					 <div class="group" onclick="javascript:moduleCheck(this);">全选</div>
					 <div class="group" onclick="javascript:moduleUnCheck(this);">取消全选</div>
					 <div class="group" onclick="javascript:getProject();">对应发次</div>
				</div> -->

				<ul class="row" id="moduleUl">
					<c:forEach var="productInfo" items="${productInfo}">
						<li><span> <input type="radio" name="checkbox"
								onchange="modelChange(this)" value="${productInfo.ID}" />
						</span> <lable>${productInfo.F_XHDH}</lable></li>
					</c:forEach>
				</ul>
			</form>
		</div>
		<div class="panel-search fc">
			<div class="title">
				产品 <a style="color: red">*</a>
			</div>


			<form id="categoryList" name="categoryList" class="searchForm">
				<!-- <div class="drop">
					<a class="activi">收起</a>
				</div>
				<div class="check">
					<div class="group" onclick="javascript:categoryCheck(this);">全选</div>
					<div class="group" onclick="javascript:categoryUnCheck(this);">取消全选</div>
				</div> -->

				<ul class="row" id="categoryUl">

				</ul>
			</form>
		</div>

		<div class="panel-search fc">
			<div class="title">
				批次 <a style="color: red">*</a>
			</div>
			<form id="batchList" name="batchList" class="searchForm">
				<div class="drop">
					<a class="activi">收起</a>
				</div>
				<div class="check">
					<div class="group" onclick="javascript:batchCheck(this);">全选</div>
					<div class="group" onclick="javascript:batchUnCheck(this);">取消全选</div>
				</div>

				<ul class="row" id="batchUl">

				</ul>
			</form>
		</div>
		<div class="panel-search fc">
			<div class="title">
				产品编号<a style="color: red">*</a>
			</div>
			<form id="productList" name="productList" class="searchForm">
				<div class="drop">
					<a class="activi">收起</a>
				</div>
				<div class="check">

					<div class="group" onclick="javascript:productCheck(this);">全选</div>
					<div class="group" onclick="javascript:productUnCheck(this);">取消全选</div>
					<div class="group" onclick="searchTable(1)"
						style="margin-left: 30px;">查询</div>
				</div>

				<ul class="row" id="productListUl">

				</ul>
			</form>
		</div>
		<div class="p-body">
			<div class="p-detail">
				<table id="proudctTable">
					<thead class="fixedThead">
						<tr class="th">
							<th><input type="checkbox" name="td-checkbox" />
							</td>
							<th>序号</th>
							<th>产品编号</th>
							<th>验收项目</th>
							<th>操作要求</th>
							<th>规定值</th>
							<th>单位</th>
							<th>接收实际值</th>
							<th>结论</th>
						</tr>
					</thead>
					<tbody class="tbody"></tbody>
				</table>
			</div>
			<!-- <div class="chart"> -->
			<div id="iecharts"></div>
			<!-- </div> -->
		</div>
	</div>

	<script type="text/javascript">
		//表格高度 
		function getHeight() {
			var height = $(".panel").height() - $(".panel-toolbar").height()
					- $(".panel-search.args").height()
					- $(".panel-search.fc").height() - 95;
			if ($(".panel-search.xh").css("display") != "none") {
				height = height - $(".panel-search.xh").height() - 45;
			}
			$(".p-body").height(height);
		}
		//查询条件展开收起 
		function foldBox() {
			$(".drop").click(function() {
				if ($(this).find("a").hasClass("activi")) {
					$(this).find("a").text('展开');
					$(this).find('a').removeClass('activi')
					$(this).parent().find(".row").hide();
				} else {
					$(this).find("a").text('收起');
					$(this).find('a').addClass('activi')
					$(this).parent().find(".row").show();
				}
				getHeight();
			})
		}
		function modelChange(obj) {
			moduleId = obj.value;
			if (obj.checked) {
				$
						.ajax({
							async : 'false',
							url : "${ctx}/product/category/batch/getBathByModuleId.do",
							method : 'post',
							data : {
								moduleId : moduleId
							},
							success : function(data) {
								$("#categoryList").find("ul.row").empty();
								$("#batchList").find("ul.row").empty();
								$("#productList").find("ul.row").empty();

								for (var i = 0; i < data.length; i++) {
									var html = "<li name="+moduleId+"><span>";
									html += '<input type="checkbox" name="categoryCheckbox" onchange="categoryChange(this)" value="'
											+ data[i].ID + '" />';
									html += '</span><lable>' + data[i].F_CPMC
											+ "(" + data[i].F_CPDH
											+ ')</lable>';
									html += '</li>';
									$("#categoryList").find("ul.row").append(
											html);
								}
							}
						})
			} else {
				if (!cateagoryIsCheck()) {
					obj.checked = true;
					$.ligerDialog.warn("产品处于选中状态请先清除！", '提示信息');
				} else {
					$("li:[name=" + moduleId + "]").remove();
				}

			}
		}
		//全选 
		function moduleCheck(obj) {
			$(obj).parents(".panel-search").find('input[name="checkbox"]')
					.attr("checked", "true");
			var moduleIdArray = "";
			$("#moduleUl").find("li").each(function() {
				var moduleId = $(this).children().children().first()[0].value;
				moduleIdArray += moduleId + ",";
			});
			moduleId = moduleIdArray;
			$
					.ajax({
						async : 'false',
						url : "${ctx}/product/category/batch/getBathByModuleIds.do",
						method : 'post',
						data : {
							moduleId : moduleId
						},
						success : function(data) {
							$("#categoryList").find("ul.row").empty();
							for (var i = 0; i < data.length; i++) {
								var html = "<li name="+data[i].F_SSXH+"><span>";
								html += '<input type="checkbox" name="categoryCheckbox" onchange="categoryChange(this)" value="'
										+ data[i].ID + '" />';
								html += '</span><lable>' + data[i].F_CPMC + "("
										+ data[i].F_CPDH + ')</lable>';
								html += '</li>';
								$("li:[name=" + data[i].ID + "]").remove();
								$("#categoryList").find("ul.row").append(html);
							}
						}
					})
		}
		//取消全选 
		function moduleUnCheck(obj) {
			$(obj).parents(".panel-search").find('input[name="checkbox"]')
					.removeAttr("checked");
			$("#categoryList").find("ul.row").empty();
			$("#batchList").find("ul.row").empty();
			$("#productList").find("ul.row").empty();
		}

		//全选 
		function categoryCheck(obj) {
			debugger;
			$(obj).parents(".panel-search").find(
					'input[name="categoryCheckbox"]').attr("checked", "true");
			var categoryIdArray = "";
			var categoryName = "";
			var check = true;
			$("#categoryUl")
					.find("li")
					.each(
							function() {
								var categoryId = $(this).children().children()
										.first()[0].value;
								if (categoryName != "") {
									if (categoryName != $(this).children()
											.last()[0].innerText) {
										$.ligerDialog.warn("当前选择产品不同无法比对！",
												'提示信息');
										$(obj)
												.parents(".panel-search")
												.find(
														'input[name="categoryCheckbox"]')
												.removeAttr("checked");
										check = false;
										return;
									}
								}
								categoryName = $(this).children().last()[0].innerText;
								categoryIdArray += categoryId + ",";
							});
			if (!check) {
				return;
			}
			$
					.ajax({
						async : 'false',
						url : "${ctx}/product/category/batch/getBathBycategoryIds.do",
						method : 'post',
						data : {
							categoryIds : categoryIdArray
						},
						success : function(data) {
							$("#batchList").find("ul.row").empty();
							for (var i = 0; i < data.length; i++) {
								var html = "<li name="+data[i].F_SSCPLB+"><span>";
								html += '<input type="checkbox" name="batchCheckbox" onchange="batchChange(this)" value="'
										+ data[i].ID + '" />';
								html += '</span><lable>' + data[i].F_PCH
										+ '</lable>';
								html += '</li>';
								$("#batchList").find("ul.row").append(html);
							}
						}
					})
		}
		function categoryChange(obj) {
			var categoryId = obj.value;
			var check = true;
			var categoryName = "";
			$("#categoryUl")
					.find("li")
					.each(
							function() {
								var c = $(this).children().children().first()[0].checked;
								if (c) {
									if (categoryName != ""
											&& categoryName != $(this)
													.children().last()[0].innerText) {
										check = false;
										return;
									}
									categoryName = $(this).children().last()[0].innerText;
								}
							});
			if (!check) {
				$.ligerDialog.warn("当前选择产品不同无法比对！", '提示信息');
				obj.checked = false;
				return;
			}
			if (obj.checked) {
				$
						.ajax({
							async : 'false',
							url : "${ctx}/product/category/batch/getBathBycategoryId.do",
							method : 'post',
							data : {
								categoryId : categoryId
							},
							success : function(data) {
								/* $("#categoryList").find("ul.row").empty(); */
								for (var i = 0; i < data.length; i++) {
									var html = "<li name="+categoryId+"><span>";
									html += '<input type="checkbox" name="batchCheckbox" onchange="batchChange(this)" value="'
											+ data[i].ID + '" />';
									html += '</span><lable>' + data[i].F_PCH
											+ '</lable>';
									html += '</li>';
									$("#batchList").find("ul.row").append(html);
								}
							}
						})
			} else {
				if (!batchIsCheck()) {
					obj.checked = true;
					$.ligerDialog.warn("批次处于选中状态请先清除！", '提示信息');
				} else {
					$("li:[name=" + categoryId + "]").remove();
				}

			}
		}
		function cateagoryIsCheck() {
			var checkbox = $("input:[name='categoryCheckbox']");
			for (var i = 0; i < checkbox.length; i++) {
				if (checkbox[i].checked) {
					return false;
				}
			}
			return true;
		}
		function batchIsCheck() {
			var checkbox = $("input:[name='batchCheckbox']");
			for (var i = 0; i < checkbox.length; i++) {
				if (checkbox[i].checked) {
					return false;
				}
			}
			return true;
		}
		function productIsCheck() {
			var checkbox = $("input:[name='productCheckbox']");
			for (var i = 0; i < checkbox.length; i++) {
				if (checkbox[i].checked) {
					return false;
				}
			}
			return true;
		}

		//全选 
		function batchCheck(obj) {
			$(obj).parents(".panel-search").find('input[name="batchCheckbox"]')
					.attr("checked", "true");
			var batchIdArray = "";
			$("#batchUl").find("li").each(function() {
				var batchId = $(this).children().children().first()[0].value;
				batchIdArray += batchId + ",";
			});
			$
					.ajax({
						async : 'false',
						url : "${ctx}/dataPackage/dea/getProductByBaths.do",
						method : 'post',
						data : {
							batchIdArray : batchIdArray
						},
						success : function(data) {
							$("#productList").find("ul.row").empty();
							for (var i = 0; i < data.length; i++) {
								var html = "<li name="+data[i].ssxhfc+"><span>";
								html += '<input type="checkbox" name="productCheckbox" onchange="batchChange(this)" value="'
										+ data[i].id + '" />';
								html += '</span><lable>' + data[i].cpmc + "("
										+ data[i].pcchbh + ")" + '</lable>';
								html += '</li>';
								$("#productList").find("ul.row").append(html);
							}
						}
					})
		}

		function batchChange(obj) {
			var batchId = obj.value;
			if (obj.checked) {
				$
						.ajax({
							async : 'false',
							url : "${ctx}/dataPackage/dea/getProductByBath.do",
							method : 'post',
							data : {
								batchId : batchId
							},
							success : function(data) {
								/* $("#categoryList").find("ul.row").empty(); */
								for (var i = 0; i < data.length; i++) {
									var html = "<li name="+batchId+"><span>";
									html += '<input type="checkbox" name="productCheckbox" onchange="batchChange(this)" value="'
											+ data[i].id + '" />';
									html += '</span><lable>' + data[i].cpmc
											+ "(" + data[i].pcchbh + ")"
											+ '</lable>';
									html += '</li>';
									$("#productList").find("ul.row").append(
											html);
								}
							}
						})
			} else {
				$("li:[name=" + batchId + "]").remove();
			}
		}
		//取消全选 
		function categoryUnCheck(obj) {
			$(obj).parents(".panel-search").find(
					'input[name="categoryCheckbox"]').removeAttr("checked");
			$("#batchList").find("ul.row").empty();
			$("#productList").find("ul.row").empty();
		}
		function batchUnCheck(obj) {
			$(obj).parents(".panel-search").find('input[name="batchCheckbox"]')
					.removeAttr("checked");
			$("#productList").find("ul.row").empty();
		}
		//全选 
		function productCheck(obj) {
			$(obj).parents(".panel-search").find(
					'input[name="productCheckbox"]').attr("checked", "true");
		}
		//取消全选 
		function productUnCheck(obj) {
			$(obj).parents(".panel-search").find(
					'input[name="productCheckbox"]').removeAttr("checked");
		}
		//全选 
		function check(obj) {
			$(obj).parents(".panel-search").find(
					'input[name="productCheckbox"]').attr("checked", "true");
		}
		//取消全选 
		function unCheck(obj) {
			$(obj).parents(".panel-search").find(
					'input[name="productCheckbox"]').removeAttr("checked");
		}

		function searchTable(index) {
			if (productIsCheck()) {
				$.ligerDialog.warn("请选择查询的产品编号！", '提示信息');
				return;
			}
			var productIdArray = "";
			$("#productListUl").find("li").each(
					function() {
						if ($(this).children().children().first()[0].checked) {
							var productId = $(this).children().children()
									.first()[0].value;
							productIdArray += productId + ",";
						}
					});
			$
					.ajax({
						async : 'false',
						url : "${ctx}/dataPackage/dea/getActualData.do",
						method : 'post',
						data : {
							productIds : productIdArray,
							index : index
						},
						success : function(data) {
							if (!data.check) {
								return;
							}
							$(".p-detail").html("");
							var html = "<table id='proudctTable'><thead class='fixedThead'>";
							html += "<tr class='th'>";
							html += "<th style='width:50px'>序号</th>";
							html += "<th>验收项目</th>";
							html += "<th style='width:100px'>单位</th>";
							html += "<th style='width:100px'>规定值</th>";
							/* html += "<th>操作要求</th>"; */
							if (data.productNameList.length != 0) {
								for (var i = 0; i < data.productNameList.length; i++) {
									html += "<th>" + data.productNameList[i]
											+ "</th>";
								}
								html += "<th style='width:160px'>管理</th>";
								html += "</tr></thead><tbody class='tbody'>"
								for (var i = 0; i < data.data.length; i++) {
									html += "<td>" + (i + 1) + "</td>";
									for ( var key in data.data[i]) {
										if (key == "F_YSXM") {
											html += "<td class='value'>"
													+ (data.data[i][key] == null ? ""
															: data.data[i][key])
													+ "</td>";
										} 
										/* else if (key == "F_CZYQ") {
											html += "<td class='value'>"
													+ (data.data[i][key] == null ? ""
															: data.data[i][key])
													+ "</td>";
										}  */
										else if (key == "F_YQZ") {
											html += "<td class='value'>"
													+ (data.data[i][key] == null ? ""
															: data.data[i][key])
													+ "</td>";
										} else if (key == "F_DW") {
											html += "<td class='value'>"
													+ (data.data[i][key] == null ? ""
															: data.data[i][key])
													+ "</td>";
										}
									}
									debugger;
									var check = true;
									var pattern = new RegExp("[0-9]+");
									for (var j = 0; j < data.productNameList.length; j++) {
										html += "<td class='value'>"
												+ (data.data[i][data.productNameList[j]] == null ? ""
														: data.data[i][data.productNameList[j]])
												+ "</td>";
										if (!pattern
												.test(data.data[i][data.productNameList[j]])) {
											check = false;
										}
									}
									if (check) {
										html += "<td class='value' style='width:100px'><input style='width: 70px;float:left' type='button' value='数据分析'";
										var titleList = JSON
												.stringify(data.productNameList);
										var dataList = JSON
												.stringify(data.data[i]);
										html += "onclick='javascript:buildEchart("
												+ titleList
												+ ","
												+ dataList
												+ ");'/>";
										html += " <input style='width: 70px;float:right' type='button' value='包络分析'";
										html += "onclick='javascript:buildEchartByValue("
												+ titleList
												+ ","
												+ dataList
												+ ");'/>";
									} else {
										html += "<td class='value' style='width:100px'>";
									}

									html += "</td></tr>";
								}
								html += "</tbody>";
								var toolHtml = '<div  style=" text-align: center;margin-bottom: 5px;"><div    class="page-button" style="display: inline-block;" onclick="buttonUp()" type="Button">上一页</div><input  type="text" id="page"  value="'+index+'"'+' readonly="" style="margin-left: 50px;width:40px;margin-right: 50px; text-align: center;display: inline-block;"><div  onclick="buttonDown()" class="page-button" style="display: inline-block;" type="Button">下一页</div></div>';
								$(".p-detail").css("width", "100%");
								$(".p-detail").append(toolHtml);
								$(".p-detail").append(html);

								$(".p-body").show();
							}
						}
					});

		}
		function buttonDown() {
			var page = $("#page").val();
			page = page * 1 + 1;
			searchTable(page);
		}
		function buttonUp() {
			var page = $("#page").val();
			page = page * 1 - 1;
			if (page <= 0) {
				return;
			}
			searchTable(page);
		}
		function buildEchartByValue(titleList, data) {
			$.ligerDialog
					.open({
						width : 280,
						type : 'ok',
						showMax: false,                             //是否显示最大化按钮 
				        showToggle: false, 
						title : '输入',
						content : '<div float="left"><a>上限:</a><input type="input"   id="on"/></div><div float="left"><a>下限:</a><input id="down" type="input" /></div>',
						buttons : [
								{
									text : '确定',
									onclick : function(item, dialog) {
										debugger;
										var down = $("#down").val();
										var on = $("#on").val();
										dialog.close();
										var pattern3 = new RegExp("[0-9]+");
										var checkDown = false;
										var checkOn = false;
										if (!down == "") {
											if (!pattern3.test(down)) {
												$.ligerDialog.warn("下限请输入数字!",
														'提示信息');
												return;
											}
											checkDown = true;
										}
										if (!on == "") {
											if (!pattern3.test(on)) {
												$.ligerDialog.warn("上限请输入数字!",
														'提示信息');
												return;
											}
											checkOn = true;
										}
										if (checkDown && checkOn) {
											if (down > on) {
												$.ligerDialog.warn("上限不能小于下限!",
														'提示信息');
												return;
											}
										}
										$("#iecharts").empty();
										var xAxis = [];
										var seriesData = [];

										for (var i = 0; i < titleList.length; i++) {
											xAxis.push(titleList[i]);

										}
										var pattern = new RegExp("[0-9]+");
										var check = true;
										for (var i = 0; i < titleList.length; i++) {
											for ( var key in data) {
												if (key == titleList[i]) {
													var c = data[titleList[i]];
													if (!pattern.test(c)) {
														check = false;
													}
													seriesData.push(c);
												}
											}
										}
										if (!check) {
											$.ligerDialog.warn("当前数据不为数字无法显示！",
													'提示信息');
											return;
										}
										var Min = Math.min
												.apply({}, seriesData);
										var Max = Math.max
												.apply({}, seriesData);
										debugger;
										var myChart = echarts.init(document
												.getElementById("iecharts"));
										var option = {
											tooltip : {
												trigger : 'axis'
											},
											grid : {
												right : '22%',
												top : '10%',
												left : '6%',
												bottom : '10%',
												containLabel : true
											},
											legend : {
												data : [ '产品实测值' ],
												orient : 'vertical',
												right : '2%',
												top : '10%'
											},
											toolbox : {
												show : 'true',
												feature : {
													saveAsImage : {}
												},
												right : '2%'
											},
											xAxis : {
												type : 'category',
												data : xAxis,
												name : '产品编号',
												nameLocation : 'middle',
												nameGap : '35',
												nameTextStyle : {
													fontSize : 15
												},
											},

											yAxis : {
												type : 'value',
												name : data.F_YSXM + '实测值',
												nameLocation : 'middle',
												nameGap : '55',
												nameTextStyle : {
													fontSize : 15
												},
												axisLabel : {
													formatter : function(value,
															index) {
														return value.toFixed(2)
													}
												}
											},
											series : [ {
												name : '实测值',
												type : 'line',
												data : seriesData
											}, ]
										}

										if (down != "") {
											var markData = [];
											for (var i = 0; i < titleList.length; i++) {
												markData.push(down);
											}
											var markString = {};

											markString.name = "下限值";
											option.legend.data.push("下限值");
											markString.type = "line";
											markString.data = markData;
											markString.symbol = "none";
											option.series.push(markString);
										}
										if (on != "") {
											var markDatas = [];
											for (var i = 0; i < titleList.length; i++) {
												markDatas.push(on);
											}
											var markStrings = {};

											markStrings.name = "上限值";
											option.legend.data.push("上限值");
											markStrings.type = "line";
											markStrings.data = markDatas;
											markStrings.symbol = "none";
											option.series.push(markStrings);
										}
										myChart.setOption(option);
									}
								}, {
									text : '取消',
									onclick : function(item, dialog) {
										dialog.close();
									}
								} ]
					})
			/* $.ligerDialog.confirm("<div float='left'><a>上限:</a><input type='input' id='on'/></div><div float='left'><a>下限:</a><input id='down' type='input'/></div>",function (r){
				if(r){
					down=$("#down").val();
					on=$("#on").val();
				}
			});  */
		}
		function buildEchart(titleList, data) {
			debugger;
			$("#iecharts").empty();

			var xAxis = [];
			var seriesData = [];

			for (var i = 0; i < titleList.length; i++) {
				xAxis.push(titleList[i]);

			}
			var check = true;
			var pattern = new RegExp("[0-9]+");
			for (var i = 0; i < titleList.length; i++) {
				for ( var key in data) {
					if (key == titleList[i]) {
						var c = data[titleList[i]];
						if (!pattern.test(c)) {
							check = false;
						}
						seriesData.push(c);
					}
				}
			}
			if (!check) {
				$.ligerDialog.warn("当前数据不为数字无法显示！", '提示信息');
				$("#iecharts").empty();
				return;
			}

			var Min = Math.min.apply({}, seriesData);
			var Max = Math.max.apply({}, seriesData);
			debugger;
			var myChart = echarts.init(document.getElementById("iecharts"));
			var option = {
				tooltip : {
					trigger : 'axis'
				},
				toolbox : {
					feature : {
						saveAsImage : {}
					}
				},
				grid : {
					right : '22%',
					top : '10%',
					left : '6%',
					bottom : '10%',
					containLabel : true
				},
				legend : {
					data : [ '产品实测值' ],
					width : 10,
					height : 10
				},
				xAxis : {
					type : 'category',
					data : xAxis,
					name : '产品编号',
					nameLocation : 'middle',
					nameGap : '35',
					nameTextStyle : {
						fontSize : 15
					},
				},
				yAxis : {
					type : 'value',
					name : data.F_YSXM + '实测值',
					nameLocation : 'middle',
					nameGap : '55',
					nameTextStyle : {
						fontSize : 15
					},
					/* min: (Min),
					max: (Max), */
					axisLabel : {
						formatter : function(value, index) {
							return value.toFixed(2)
						}
					}
				},

				series : [ {
					name : '实测值',
					type : 'line',
					data : seriesData
				}, ]
			}
			var value = data.F_YQZ;
			//检查结果是否含有中文
			var reg = new RegExp("[\\u4E00-\\u9FFF]+", "g");
			if (!reg.test(value)) {
				//如果字符串不包含中日韩文
				if (value != "/") {
					var pattern = new RegExp("[]~≥≤[＞＜±]");
					var pattern = new RegExp(
							"^(\\≥|\\＞|\\≤|\\＜)(\\-|\\+)?([\\d]+)(\\.[\\d]+)?$");
					var pattern2 = new RegExp(
							"^([\\d]+)(\\.[\\d]+)?(\\(|\\[){1}(\\+){1}([\\d]+)(\\.[\\d]+)?(\\,)?(\\-){1}([\\d]+)(\\.[\\d]+)?(\\)|\\]){1}$");

					var pattern3 = new RegExp(
							"^(\\-|\\+)?([\\d]+)(\\.[\\d]+)?$");
					var pattern4 = new RegExp(
							"^^(0|[1-9]\\d*)$|^(0|[1-9]\\d*)\\.(\\d+)$");
					var pattern1 = new RegExp("[\\u4e00-\\u9fa5]");
					var m = pattern.test(value);
					var m1 = pattern1.test(value);
					var m2 = pattern2.test(value);
					var m3 = pattern3.test(value);
					var m4 = pattern4.test(value);
					if (m) {
						if (m4) {
							return;
						}
						var symbol = value.substring(0, 1);
						var requirevalNum = value.substring(1, value.length);
						if (symbol != "") {
							switch (symbol) {
							case "≥":
								var markData = [];
								for (var i = 0; i < titleList.length; i++) {
									markData.push(requirevalNum);
								}
								var markString = {};
								/* 	if(requirevalNum<Min){
										option.yAxis.min=requirevalNum;
									} */
								markString.name = "下限值";
								option.legend.data.push("下限值");
								markString.type = "line";
								markString.data = markData;
								markString.symbol = "none";
								option.series.push(markString);
								break;
							case "＞":
								var markData = [];
								for (var i = 0; i < titleList.length; i++) {
									markData.push(requirevalNum);
								}
								var markString = {};
								/* if(requirevalNum<Min){
									option.yAxis.min=requirevalNum;
								} */
								markString.name = "下限值";
								option.legend.data.push("下限值");
								markString.type = "line";
								markString.data = markData;
								markString.symbol = "none";
								option.series.push(markString);
								break;
							case "≤":
								var markData = [];
								for (var i = 0; i < titleList.length; i++) {
									markData.push(requirevalNum);
								}
								var markString = {};
								if (requirevalNum > Max) {
									option.yAxis.max = requirevalNum;
								}
								markString.name = "上限值";
								option.legend.data.push("上限值");
								markString.type = "line";
								markString.data = markData;
								markString.symbol = "none";
								option.series.push(markString);
								break;
							case "＜":
								var markData = [];
								for (var i = 0; i < titleList.length; i++) {
									markData.push(requirevalNum);
								}
								var markString = {};
								if (requirevalNum > Max) {
									option.yAxis.max = requirevalNum;
								}
								markString.name = "上限值";
								option.legend.data.push("上限值");
								markString.type = "line";
								markString.data = markData;
								markString.symbol = "none";
								option.series.push(markString);
								break;
							}
						}
					} else if (value.indexOf("(") != -1
							|| value.indexOf(")") != -1
							|| value.indexOf("[") != -1
							|| value.indexOf("]") != -1) {
						if (m4) {
							return;
						}
						if (value != "") {
							var leftS = value.indexOf("(") >= 0 ? "1" : "0";// 判断有没有左小括号
							var rightS = value.indexOf(")") >= 0 ? "1" : "0";// 判断有没有右小括号
							var leftM = value.indexOf("[") >= 0 ? "1" : "0";// 判断有没有左中括号
							var rightM = value.indexOf("]") >= 0 ? "1" : "0";// 判断有没有右中括号
							var condition = leftS + rightS + leftM + rightM;
							switch (condition) {
							case "1100":
								var mid = value
										.substring(0, value.indexOf("("));
								if (mid == "") {
									mid = 0;
								}
								var strWithoutPreNum = value.substring(value
										.indexOf("("), value.indexOf(")") + 1);
								var supIndexWithoutPreNum = strWithoutPreNum
										.indexOf("+") == -1 ? strWithoutPreNum
										.indexOf("(") + 1 : strWithoutPreNum
										.indexOf("+");
								var subIndexWithoutPreNum = strWithoutPreNum
										.indexOf("-") == -1 ? strWithoutPreNum
										.indexOf(",") + 1 : strWithoutPreNum
										.indexOf("-");
								var supIndex = supIndexWithoutPreNum
										+ value.indexOf("(");
								var subIndex = subIndexWithoutPreNum
										+ value.indexOf("(");
								var sup = value.substring(supIndex, value
										.indexOf(","));
								var sub = value.substring(subIndex, value
										.lastIndexOf(")"));
								var min = mid * 1 + sub * 1;
								var max = mid * 1 + sup * 1;
								var minMarkData = [];
								var maxMarkData = [];
								for (var i = 0; i < titleList.length; i++) {
									minMarkData.push(min);
									maxMarkData.push(max);
								}
								var minMarkString = {};
								var maxMarkString = {};
								/* if(min<Min){
									option.yAxis.min=min;
								}
								if(max>Max){
									option.yAxis.max=max;
								} */
								minMarkString.name = "下限值";
								option.legend.data.push("下限值");
								minMarkString.type = "line";
								minMarkString.data = minMarkData;
								minMarkString.symbol = "none";
								option.series.push(minMarkString);
								maxMarkString.name = "上限值";
								option.legend.data.push("上限值");
								maxMarkString.type = "line";
								maxMarkString.data = maxMarkData;
								maxMarkString.symbol = "none";
								option.series.push(maxMarkString);
								break;
							case "1001":
								var mid = value
										.substring(0, value.indexOf("("));
								if (mid == "") {
									mid = 0;
								}
								var strWithoutPreNum = value.substring(value
										.indexOf("("), value.indexOf(")") + 1);
								var supIndexWithoutPreNum = strWithoutPreNum
										.indexOf("+") == -1 ? strWithoutPreNum
										.indexOf("(") + 1 : strWithoutPreNum
										.indexOf("+");
								var subIndexWithoutPreNum = strWithoutPreNum
										.indexOf("-") == -1 ? strWithoutPreNum
										.indexOf(",") + 1 : strWithoutPreNum
										.indexOf("-");
								var supIndex = supIndexWithoutPreNum
										+ value.indexOf("(");
								var subIndex = subIndexWithoutPreNum
										+ value.indexOf("(");
								var sup = value.substring(supIndex, value
										.indexOf(","));
								var sub = value.substring(subIndex, value
										.lastIndexOf("]"));
								var min = mid * 1 + sub * 1;
								var max = mid * 1 + sup * 1;

								var minMarkData = [];
								var maxMarkData = [];
								for (var i = 0; i < titleList.length; i++) {
									minMarkData.push(min);
									maxMarkData.push(max);
								}
								var minMarkString = {};
								var maxMarkString = {};
								/*   if(min<Min){
								  	option.yAxis.min=min;
								  }
								  if(max>Max){
								  	option.yAxis.max=max;
								  } */
								minMarkString.name = "下限值";
								option.legend.data.push("下限值");
								minMarkString.type = "line";
								minMarkString.data = minMarkData;
								minMarkString.symbol = "none";
								option.series.push(minMarkString);
								maxMarkString.name = "上限值";
								option.legend.data.push("上限值");
								maxMarkString.type = "line";
								maxMarkString.data = maxMarkData;
								maxMarkString.symbol = "none";
								option.series.push(maxMarkString);

								break;
							case "0110":
								var mid = value
										.substring(0, value.indexOf("["));
								if (mid == "") {
									mid = 0;
								}
								var strWithoutPreNum = value.substring(value
										.indexOf("["), value.indexOf(")") + 1);
								var supIndexWithoutPreNum = strWithoutPreNum
										.indexOf("+") == -1 ? strWithoutPreNum
										.indexOf("[") + 1 : strWithoutPreNum
										.indexOf("+");
								var subIndexWithoutPreNum = strWithoutPreNum
										.indexOf("-") == -1 ? strWithoutPreNum
										.indexOf(",") + 1 : strWithoutPreNum
										.indexOf("-");
								var supIndex = supIndexWithoutPreNum
										+ value.indexOf("[");
								var subIndex = subIndexWithoutPreNum
										+ value.indexOf("[");
								var sup = value.substring(supIndex, value
										.indexOf(","));
								var sub = value.substring(subIndex, value
										.lastIndexOf(")"));
								var min = mid * 1 + sub * 1;
								var max = mid * 1 + sup * 1;
								var mid = value
										.substring(0, value.indexOf("["));
								var sup = value.substring(
										value.indexOf("+") + 1, value
												.indexOf(","));
								var sub = value.substring(
										value.indexOf("-") + 1, value
												.lastIndexOf(")"));
								var min = mid * 1 - sub * 1;
								var max = mid * 1 + sup * 1;
								var minMarkData = [];
								var maxMarkData = [];
								for (var i = 0; i < titleList.length; i++) {
									minMarkData.push(min);
									maxMarkData.push(max);
								}
								var minMarkString = {};
								var maxMarkString = {};
								/* if(min<Min){
									option.yAxis.min=min;
								} */
								/* if(max>Max){
									option.yAxis.max=max;
								} */
								minMarkString.name = "下限值";
								option.legend.data.push("下限值");
								minMarkString.type = "line";
								minMarkString.data = minMarkData;
								minMarkString.symbol = "none";
								option.series.push(minMarkString);
								maxMarkString.name = "上限值";
								option.legend.data.push("上限值");
								maxMarkString.type = "line";
								maxMarkString.data = maxMarkData;
								maxMarkString.symbol = "none";
								option.series.push(maxMarkString);

								break;
							case "0011":

								var mid = value
										.substring(0, value.indexOf("["));
								if (mid == "") {
									mid = 0;
								}
								var strWithoutPreNum = value.substring(value
										.indexOf("["), value.indexOf("]") + 1);
								var supIndexWithoutPreNum = strWithoutPreNum
										.indexOf("+") == -1 ? strWithoutPreNum
										.indexOf("[") + 1 : strWithoutPreNum
										.indexOf("+");
								var subIndexWithoutPreNum = strWithoutPreNum
										.indexOf("-") == -1 ? strWithoutPreNum
										.indexOf(",") + 1 : strWithoutPreNum
										.indexOf("-");
								var supIndex = supIndexWithoutPreNum
										+ value.indexOf("[");
								var subIndex = subIndexWithoutPreNum
										+ value.indexOf("[");
								var sup = value.substring(supIndex, value
										.indexOf(","));
								var sub = value.substring(subIndex, value
										.lastIndexOf("]"));
								var min = mid * 1 + sub * 1;
								var max = mid * 1 + sup * 1;
								var minMarkData = [];
								var maxMarkData = [];
								for (var i = 0; i < titleList.length; i++) {
									minMarkData.push(min);
									maxMarkData.push(max);
								}
								var minMarkString = {};
								var maxMarkString = {};
								/*   if(min<Min){
								  	option.yAxis.min=min;
								  }
								  if(max>Max){
								  	option.yAxis.max=max;
								  } */
								minMarkString.name = "下限值";
								option.legend.data.push("下限值");
								minMarkString.type = "line";
								minMarkString.data = minMarkData;
								minMarkString.symbol = "none";
								option.series.push(minMarkString);
								maxMarkString.name = "上限值";
								option.legend.data.push("上限值");
								maxMarkString.type = "line";
								maxMarkString.data = maxMarkData;
								maxMarkString.symbol = "none";
								option.series.push(maxMarkString);
								break;
							}
						}
					} else if (value.indexOf("~") >= 0) {
						var min = value.substring(0, value.indexOf("~"));
						var max = value.substring(value.indexOf("~") + 1,
								value.length);
						var minMarkData = [];
						var maxMarkData = [];
						for (var i = 0; i < titleList.length; i++) {
							minMarkData.push(min);
							maxMarkData.push(max);
						}
						var minMarkString = {};
						var maxMarkString = {};
						/* if(min<Min){
							option.yAxis.min=min;
						}
						if(max>Max){
							option.yAxis.max=max;
						} */
						minMarkString.name = "下限值";
						option.legend.data.push("下限值");
						minMarkString.type = "line";
						minMarkString.data = minMarkData;
						minMarkString.symbol = "none";
						option.series.push(minMarkString);
						maxMarkString.name = "上限值";
						option.legend.data.push("上限值");
						maxMarkString.type = "line";
						maxMarkString.data = maxMarkData;
						maxMarkString.symbol = "none";
						option.series.push(maxMarkString);
					} else if (value.indexOf("±") >= 0) {
						if (m4) {
							return;
						}
						var mid = value.substring(0, value.indexOf("±"));
						var mm = value.substring(value.indexOf("±") + 1,
								value.length);
						var min = mid - mm;
						var max = mid + mm;
						var minMarkData = [];
						var maxMarkData = [];
						for (var i = 0; i < titleList.length; i++) {
							minMarkData.push(min);
							maxMarkData.push(max);
						}
						var minMarkString = {};
						var maxMarkString = {};
						/* if(min<Min){
							option.yAxis.min=min;
						}
						if(max>Max){
							option.yAxis.max=max;
						} */
						minMarkString.name = "下限值";
						option.legend.data.push("下限值");
						minMarkString.type = "line";
						minMarkString.data = minMarkData;
						minMarkString.symbol = "none";
						option.series.push(minMarkString);
						maxMarkString.name = "上限值";
						option.legend.data.push("上限值");
						maxMarkString.type = "line";
						maxMarkString.data = maxMarkData;
						maxMarkString.symbol = "none";
						option.series.push(maxMarkString);
					}
				}
			}

			myChart.setOption(option);
			//$(".chart").css('display','inline-block');
		}

		//表格复选框
		function tdCheckEvent() {
			$(".th").find('input[name="td-checkbox"]').click(
					function() {
						if (this.checked) {
							$("table").find('input[name="td-checkbox"]').attr(
									"checked", "true");
						} else {
							$("table").find('input[name="td-checkbox"]')
									.removeAttr("checked");
						}
					})
		}
		$(function() {
			//查询条件展开收起 
			foldBox();
			getHeight();
		});
	</script>
</body>
</html>