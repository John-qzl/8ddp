
<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2020/11/17
  Time: 16:39
  To change this template use File | Settings | File Templates.
--%>


<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>工作统计结果</title>
</head>
<script type="text/javascript" src="/dp/jslib/jquery/jquery.js"></script>
<script type="text/javascript" src="/dp/jslib/echarts/chart/echarts.min.js"></script>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/Aqua/css/ligerui-all.css"></link>
<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/web.css"></link>

<link title="index" name="styleTag" rel="stylesheet" type="text/css" href="/dp/styles/default/css/table/table-basic.css"></link>


<script>
    $(function () {
        drawByNumberOfModule();
    })
    function draw() {
        var drawCondition=$("#drawConditionSelecter").val();
        switch (drawCondition) {
            case "numberOfModule":drawByNumberOfModule();break;
            case "numberOfPlan":drawByNumberOfPlan();break;
            case "numberOfTeamMember":drawByNumberOfTeamMember();break;
            case "numberOfDays":drawByNumberOfDays();break;
            default :alert("未匹配到您的选择");
        }
    }
    function drawByNumberOfModule() {
        debugger;
        var myChart = echarts.init(document.getElementById('main'));
        option={
            title: {
                text: '各部门所检型号数量柱状图',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                name:'部门',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    '${modelMap.departmentName}',
                    </c:forEach>
                ]
            },
            yAxis: {
                name:'型号数量',
                type: 'value'
            },
            tooltip: {
                trigger: 'axis'
            },
            series: [{
                name:'型号数量',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    ${modelMap.countNumberOfModule},
                    </c:forEach>
                ],
                type: 'bar'
            }]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    function drawByNumberOfPlan() {
        debugger;
        var myChart = echarts.init(document.getElementById('main'));
        option={
            title: {
                text: '各部门所检次数柱状图',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                name:'部门',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    '${modelMap.departmentName}',
                    </c:forEach>
                ]
            },
            yAxis: {
                name:'所检次数',
                type: 'value'
            },
            tooltip: {
                trigger: 'axis'
            },
            series: [{
                name:'所检次数',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    ${modelMap.countNumberOfPlan},
                    </c:forEach>
                ],
                type: 'bar'
            }]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    function drawByNumberOfTeamMember() {
        debugger;
        var myChart = echarts.init(document.getElementById('main'));
        option={
            title: {
                text: '各部门所检人数柱状图',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                name:'部门',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    '${modelMap.departmentName}',
                    </c:forEach>
                ]
            },
            yAxis: {
                name:'所检人数',
                type: 'value'
            },
            tooltip: {
                trigger: 'axis'
            },
            series: [{
                name:'所检人数',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    ${modelMap.countNumberOfTeamMember},
                    </c:forEach>
                ],
                type: 'bar'
            }]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    function drawByNumberOfProduct() {
        debugger;
        var myChart = echarts.init(document.getElementById('main'));
        option={
            title: {
                text: '各部门产品数量柱状图',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                name:'部门',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    '${modelMap.departmentName}',
                    </c:forEach>
                ]
            },
            yAxis: {
                name:'产品数量',
                type: 'value'
            },
            tooltip: {
                trigger: 'axis'
            },
            series: [{
                name:'产品数量',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    ${modelMap.countNumberOfProduct},
                    </c:forEach>
                ],
                type: 'bar'
            }]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }
    function drawByNumberOfDays() {
        debugger;
        var myChart = echarts.init(document.getElementById('main'));
        option={
            title: {
                text: '各部门持续天数柱状图',
                left: 'center'
            },
            xAxis: {
                type: 'category',
                name:'部门',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    '${modelMap.departmentName}',
                    </c:forEach>
                ]
            },
            yAxis: {
                name:'持续天数',
                type: 'value'
            },
            tooltip: {
                trigger: 'axis'
            },
            series: [{
                name:'持续天数',
                data: [
                    <c:forEach items="${modelMapList}" var="modelMap">
                    ${modelMap.countNumberOfDays},
                    </c:forEach>
                ],
                type: 'bar'
            }]
        }
        // 使用刚指定的配置项和数据显示图表。
        myChart.setOption(option);
    }

</script>
<style type="text/css">

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

</style>
<body>
<div style="height:240px;padding-left: 10px" >
    <div style="padding-bottom: 5px">
        <label>柱状图依据：</label>&nbsp;
        <select id="drawConditionSelecter" onchange="draw()">
            <option value="numberOfModule" selected>型号数量</option>
            <option value="numberOfPlan">所检次数</option>
            <option value="numberOfTeamMember">所检人数</option>
            <option value="numberOfDays">持续天数</option>
        </select>
    </div>

    <table class="table-grid table-list">
        <tr>
            <th style="width:100px">部门名称</th>
            <c:forEach items="${modelMapList}" var="modelMap">
                <td>${modelMap.departmentName}</td>
            </c:forEach>
        </tr>
        <tr>
            <th style="width:100px">型号数量</th>
            <c:forEach items="${modelMapList}" var="modelMap">
                <td>${modelMap.countNumberOfModule}</td>
            </c:forEach>
        </tr>
        <tr>
            <th style="width:100px">所检次数</th>
            <c:forEach items="${modelMapList}" var="modelMap">
                <td>${modelMap.countNumberOfPlan}</td>
            </c:forEach>
        </tr>
        <tr>
            <th style="width:100px">所检人数</th>
            <c:forEach items="${modelMapList}" var="modelMap">
                <td>${modelMap.countNumberOfTeamMember}</td>
            </c:forEach>
        </tr>
        <tr>
            <th style="width:100px">持续天数</th>
            <c:forEach items="${modelMapList}" var="modelMap">
                <td>${modelMap.countNumberOfDays}</td>
            </c:forEach>
        </tr>

        <%-- 20201225 需求变更,要求表头在竖向方向
        <tr>
            <th>部门名称</th>
            <th>型号数量</th>
            <th>所检次数</th>
            <th>所检人数</th>
            <th>持续天数</th>
        </tr>
        <c:forEach items="${modelMapList}" var="modelMap">
            <tr style="text-align: center">
                <td>${modelMap.departmentName}</td>
                <td>${modelMap.countNumberOfModule}</td>
                <td>${modelMap.countNumberOfPlan}</td>
                <td>${modelMap.countNumberOfTeamMember}</td>
                <td>${modelMap.countNumberOfDays}</td>
            </tr>
        </c:forEach>
         --%>
    </table>
</div>
<%--echarts容器--%>
<div id="main" style="height:480px;padding: 20px"></div>
</body>
</html>
