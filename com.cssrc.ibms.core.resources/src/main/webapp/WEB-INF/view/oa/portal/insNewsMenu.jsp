<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>[HrDutyRegister]列表管理</title>
<%@include file="/commons/portalCustom.jsp"%>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
<style>
html,body {
	width: 100%;
	height: 230px;
	border: 0;
	margin: 0;
	padding: 0;
	overflow: visible;
}

.bgbtn div {
	margin-left: 20px;
	padding-left: 20px;
}

.bgbtn span {
	margin-left: 20px;
	padding-left: 20px;
	color: #aaa;
}

.btn {
	display: inline-block;
	margin-top: 25px;
	margin-left: 25px;
	padding: 10px 24px;
	border-radius: 5px;
	background-color: #63b7ff;
	color: #36cc82;
	cursor: pointer;
	text-decoration: none;
	background: url("${ctx}/styles/images/portal/icons/user.png") 10% center
		no-repeat;
	border: 1px solid #c3c3c3;
	padding: 10px 24px
}

.btn:hover {
	background-color: #99c6ff;
}

.btn {
	font-style: normal;
}
</style>
</head>

<body>
	<div>
		<a href="" class="bgbtn btn">
			<div>用户管理</div> <span>user Manager</span>
		</a>
		 <a href="" class="bgbtn btn">
			<div>用户管理</div> <span>user Manager</span>
		</a>
	</div>
	<div>
		<a href="" class="bgbtn btn">
			<div>用户管理</div> <span>user Manager</span>
		</a> <a href="" class="bgbtn btn">
			<div>用户管理</div> <span>user Manager</span>
		</a>
	</div>
	<script type="text/javascript">
		mini.parse();
	</script>
</body>
</html>