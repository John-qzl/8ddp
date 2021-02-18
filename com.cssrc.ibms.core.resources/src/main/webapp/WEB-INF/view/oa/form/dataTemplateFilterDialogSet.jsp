<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp"%>
<title>发起人或上个任务执行人</title>
<script type="text/javascript">
	var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)

	//获取组织类型
	var sysOrgTypeList=[
	 <c:forEach items="${sysOrgTypelist}"  var="t" >
				['${t.demId}' , '${t.id }' , '${t.name }'] ,
	</c:forEach>
	];

	//维度改变时，对数据进行处理
	function changeDemId(obj){
		var currentDem=$(obj).val();
		var childrens=$(obj).children();
		var orgType = $('select:[name=orgType]');
		orgType.children().remove();
		for(var i=0;i<(sysOrgTypeList.length-1);i++){
				if (sysOrgTypeList[i][0]==currentDem){
					orgType.html(orgType.html()+'<option value="'+sysOrgTypeList[i][1]+'">'+sysOrgTypeList[i][2]+'</option>');
				}	
		}	
	}
	

	function save() {
		if(${judgeConditionVal==17}){
			select();
			return;
		}
		var obj=new Object();
		var json=new Object();
		obj.json=json;
		
		//是否递归查询
		var recurssionType = $("[name='recurssion']");
		if (recurssionType.length > 0) {
			recurssionType = $("[name='recurssion']:checked");
			if (recurssionType.length == 0) {
				$.ligerDialog.warn('请确定是否递归查询!', '提示');
				return;
			}
			obj.json.recurssion = recurssionType.val();
		}
		
		//领导类型
		var leader;
		var leadertype=$("[name='leadertype']")
		if (leadertype.length> 0) {
			leadertype = $("[name='leadertype']:checked");
			if(leadertype.length==0){
				$.ligerDialog.warn('请确定领导类型!', '提示');
				return;
			}
			obj.json.leadertype = leadertype.val();

		}
		//维度关系
		var demensiontext;
		if(${judgeConditionVal==7}){
			demensiontext=$("#demensionId").find("option:selected").text();
			if(demensionId==null){
				$.ligerDialog.warn('请选择行政维度!', '提示');
				return;
			}
			obj.json.demensionId = $("#demensionId").val();
		}
		
		//指定职务
		var jobtext;
		var jobtype=$("[name='jobId']")
		if (jobtype.length> 0) {
			jobtype=$("#jobId").find("option:selected");
			if(jobtype.length==1){
				obj.json.jobId = jobtype.val();
				jobtext=$("#jobId").attr("memo")+":"+jobtype.text();
			}
		}

		obj.description="";
		if(leadertype.length==1){
			obj.description +=leadertype.attr("memo");
		}
		if(demensiontext&&demensiontext!=null){
			obj.description +=demensiontext;
		}
		if(recurssionType.length==1){
			obj.description +=recurssionType.attr("memo");
		}
		if(jobtext&&jobtext!=null){
			obj.description +=jobtext;
		}
		dialog.get("sucCall")(obj);
		dialog.close();
	}
	
	//点击选择时的数据处理
	function select(){
		var obj=new Object();
		var json=new Object();
		obj.json=json;
		
		var demId=$('select:[name=demensionId]').val();
		var orgType=$('select:[name=orgType]').val();
		var orgTypeTxt=$('select:[name=orgType]').find("option:selected").text();
		var stategy=$('input:radio[name=stategy]:checked').val();
		var demTxt=$('select:[name=demensionId]').find("option:selected").text();
		var orgSourcetxt=$('input:radio[name=OrgSource]:checked').next().html();
		var stategyTxt=$('input:radio[name=stategy]:checked').next().html();
		
		//拼接的json串，隐藏输入
		if(orgType!=null){
			obj.json.demId=demId;
			obj.json.stategy=stategy;
			obj.json.orgType=orgType;
		}else{
			obj.json.demId=demId;
			obj.json.stategy=stategy;
			obj.json.orgType="";
		}
		obj.description="";
		var showTxt="维度:"+demTxt+";查找策略:"+stategyTxt+";组织类型:"+orgTypeTxt;
		obj.description +=showTxt;
		dialog.get("sucCall")(obj);
		dialog.close();
	};
</script>
</head>

<body>
	<div class="panel">
		<div class="hide-panel">
			<div class="panel-top">
				<div class="tbar-title">
					<span class="tbar-label">过滤方式设置</span>
				</div>
				<div class="panel-toolbar">
					<div class="toolBar">
						<div class="group">
							<a class="link save" id="dataFormSave" onclick="save();" href="####">
								
								保存
							</a>
						</div>
						
						<div class="group">
							<a class="link close" onclick="dialog.close()" href="####">
								
								关闭
							</a>
						</div>
					</div>
				</div>
			</div>
		</div>
		<div style="text-align: center; padding-top: 10px;">
			<table class="table-grid" width="90%">

				<c:if test="${judgeConditionVal==10}">
					<tr>
						<td>是否递归查询</td>
						<td align="left">
							<input type="radio" name="recurssion" title="递归查询所有下属成员" value="1" memo="递归查询所有下属成员" <c:if test="${recurssion eq '1'}">checked="checked"</c:if> />
							递归查询所有下属成员
							<input type="radio" name="recurssion" title="查询当前下属成员" value="0" memo="查询当前下属成员" <c:if test="${recurssion eq '0'}">checked="checked"</c:if> />
							查询当前下属成员
						</td>
					</tr>
				</c:if>
				<c:if test="${judgeConditionVal==7}">
					<tr>
						<td>行政维度</td>
						<td align="left">
							<select id="demensionId" name="demensionId">
								<c:forEach var="dem" items="${demensionList}">
									<option value="${dem.demId}" <c:if test="${dem.demId==demensionId}">selected="selected"</c:if>>${dem.demName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
				</c:if>


				<c:if test="${judgeConditionVal==6}">
					<tr>
						<td>领导类型</td>
						<td align="left">
							<input name="leadertype" value="all" memo="分管领导" type="radio" <c:if test="${leadertype eq 'all'}">checked="checked"</c:if>>
							分管领导
							<input name="leadertype" value="leader" memo="分管主领导" type="radio" <c:if test="${leadertype eq 'leader'}">checked="checked"</c:if>>
							分管主领导
							<input name="leadertype" value="viceLeader" memo="分管副领导" type="radio" <c:if test="${leadertype eq 'viceLeader'}">checked="checked"</c:if>>
							分管副领导
						</td>
					</tr>
				</c:if>

				<!--所属组织负责人为当前用户  -->
				<c:if test="${judgeConditionVal==5||judgeConditionVal==6||judgeConditionVal==7}">
					<tr>
						<td>是否递归查询</td>
						<td align="left">
							<input type="radio" name="recurssion" title="递归查询所有子组织" value="1" memo="递归查询所有子组织" <c:if test="${recurssion eq '1'}">checked="checked"</c:if> />
							递归查询
							<input type="radio" name="recurssion" title="不递归查询所有子组织" value="0" memo="所属组织" <c:if test="${recurssion eq '0'}">checked="checked"</c:if> />
							不递归查询
						</td>
					</tr>
				</c:if>

				<c:if test="${judgeConditionVal==11}">
					<tr>
						<td>指定职务</td>
						<td align="left">
							<select id="jobId" name="jobId" memo="指定职务">
								<c:forEach items="${jobs}" var="job">
									<option value="${job.jobid}" <c:if test="${jobId==job.jobid}">selected</c:if>>${job.jobname}</option>
								</c:forEach>
							</select>
						</td>
					</tr>

				</c:if>


				<c:if test="${judgeConditionVal==17}">
					<tr>
						<td>维度</td>
						<td>
							<select name="demensionId" style="width: 70%;" onchange="changeDemId(this)">
								<c:forEach items="${demensionList}" var="d">
									<option value="${d.demId}">${d.demName}</option>
								</c:forEach>
							</select>
						</td>
					</tr>
					<tr>
						<td>查找策略</td>
						<td style="text-align: left;">
							<div style="margin-left: 55px;">
								<input type="radio" name="stategy" value="0" checked="checked" />
								<span>查找指定组织类型，为空时继续向上一级查询 </span>
								<br>
								<input type="radio" name="stategy" '  value="1" />
								<span>只查指定组织类型，为空时不继续往上找</span>
							</div>
						</td>
					</tr>
					<tr>
						<td>组织类型</td>
						<td>
							<select name="orgType" style="width: 70%;">
							</select>
						</td>
					</tr>
				</c:if>

			</table>

		</div>

	</div>

</body>
</html>