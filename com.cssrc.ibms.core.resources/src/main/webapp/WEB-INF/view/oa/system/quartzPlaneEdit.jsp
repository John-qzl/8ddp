<%@page language="java" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<title>编辑 执行计划</title>
<%@include file="/commons/include/form.jsp"%>
<script type="text/javascript" src="${ctx}/jslib/ibms/CustomValid.js"></script>
<script type="text/javascript" src="${ctx}/jslib/util/dateUtil.js"></script>
<script type="text/javascript">
	$(function() {
		var options = {};
		if (showResponse) {
			options.success = showResponse;
		}
		var frm = $('#TriggerObject').form();
		$("a.save").click(function() {
			frm.ajaxForm(options);
			if (frm.valid()&&getCron()) {
				$('#TriggerObject').submit();
			}
		});
		
		initPage();
	});
    /**
     * 保存按钮点击后回调函数
     * */
	function showResponse(responseText) {
		var obj = new com.ibms.form.ResultMessage(responseText);
		if (obj.isSuccess()) {
			$.ligerDialog.confirm(obj.getMessage() + ",是否继续操作","提示信息",
							function(rtn) {
								if (rtn) {
									this.close();
									$("#TriggerObject").resetForm();
									initPage();
								} else {
									window.location.href = "${ctx}/oa/system/quartz/planeList.do?name=${TriggerObject.jobName}";

								}
							});
		} else {
			$.ligerDialog.error(obj.getMessage(),'提示信息');
		}
	}
	/**
	 * 初始化新增页面
	 * */
	function initPage(){
	   var name = document.getElementById('name').value;
	   if(name==null||name==''){
	      document.getElementById('oneTime').checked = true;
	      document.getElementById('oneTimeText').setAttribute('validate','{required:true,maxlength:50}');
	   }
	}
	
	/**
	 * 根据所选类别生成相应的cron表达式
	 * */
   function getCron(){
   	var result = false;
	var processMethod = '';
	var radioButtons =  document.getElementsByName('processMethod');
	var cron = document.getElementById('cron');
	for(i=0;i<radioButtons.length;i++){
		if(radioButtons[i].checked){
			processMethod=radioButtons[i].id;
			break;
		}
	}
	if(processMethod=='oneTime'){//一次
		var oneTimeText = document.getElementById('oneTimeText').value;
	    if(oneTimeText!=null&&oneTimeText!=""){
	    	 var myArray = DateUtil.toChinaArray(DateUtil.strToDate(oneTimeText));
	    	 var cronStr =  myArray[5] + ' ' + myArray[4] 
	    	               + ' ' + myArray[3] + ' ' + myArray[2]
	    	               + ' ' + myArray[1] + ' ? ' + myArray[0];
	    	 cron.value = cronStr;
	    	 result = true;
	    }
	}else if(processMethod=='manyTimesAday'){//每隔多少分钟
		var manyTimesAdayText = document.getElementById('manyTimesAdayText').value;
		if(manyTimesAdayText!=null&&manyTimesAdayText!=''){
		    var myArray = manyTimesAdayText.split(':');
		    var cronStr = '0' + ' ' +'0'
		                  +'/' + (myArray[0]*1) + ' '
		                  +'*' + ' ' + '*' + ' ' + '*' + ' ' + '?' + ' ' + '*'; 
		                  cron.value = cronStr;
		                  result = true;
		}
	}else if(processMethod=='oneTimeAday'){//每天
		var oneTimeAdayText = document.getElementById('oneTimeAdayText').value;
		if(oneTimeAdayText!=null&&oneTimeAdayText!=''){
		    var myArray = oneTimeAdayText.split(':');
		    var cronStr = '0' + ' ' +(myArray[1]*1)
		                  +' ' + (myArray[0]*1) + ' '
		                  +'*' + ' ' + '*' + ' ' + '?' + ' ' + '*'; 
		                  cron.value = cronStr;
		                  result = true;
		}
		
	}else if(processMethod=='manyTimesAWeek'){//每周
		var manyTimesAWeekText = document.getElementById('manyTimesAWeekText').value;
		var weeks = document.getElementsByName('week');
		var weekElement = '';
		var myArray ;
		if(manyTimesAWeekText!=null&&manyTimesAWeekText!=''){
		   myArray = manyTimesAWeekText.split(':');
		}
		for(i=0;i<weeks.length;i++){
			if(weeks[i].checked){
			    weekElement = weekElement + ',' + weeks[i].value;
			}
		}
		weekElement = weekElement.trim();
		if(weekElement!=''&&myArray.length>0){
			weekElement = weekElement.substring(1,weekElement.length).trim();
			var cronStr = '0' + ' ' +(myArray[1]*1)
		                  +' ' + (myArray[0]*1) + ' '
		                  +'?' + ' ' + '*' +' '+  weekElement + ' ' + '*';
		     cron.value = cronStr;
			 result = true;
		}
		
	}else if(processMethod=='manyTimesAMonth'){//每月
		var manyTimesAMonthText = document.getElementById('manyTimesAMonthText').value;
		var months = document.getElementsByName('month');
		var monthElement = '';
		var myArray ;
		if(manyTimesAMonthText!=null&&manyTimesAMonthText!=''){
		   myArray = manyTimesAMonthText.split(':');
		}
		for(i=0;i<months.length;i++){
			if(months[i].checked){
			    monthElement = monthElement + ',' + months[i].value;
			}
		}
		monthElement = monthElement.trim();
		if(monthElement!=''&&myArray.length>0){
			monthElement = monthElement.substring(1,monthElement.length).trim();
			var cronStr = '0' + ' ' +(myArray[1]*1)
		                  +' ' + (myArray[0]*1) + ' '
		                  +  monthElement + ' ' + '*' + ' ' + '?' + ' ' + '*';
		     cron.value = cronStr;
			 result = true;
		}
	}else if(processMethod=='cronRadio'){
	     cron.value =  document.getElementById('cronRadioText').value;
	     result = true;
	}
	return  result ;
}
	    /**
     * 重新设置验证,重置form表单
     * */
	function randioChange(element){
	  var name = document.getElementById('name').value;
	  var jobName = document.getElementById('jobName').value;
	  var cron = document.getElementById('cron').value;
	      $("#TriggerObject").resetForm();
	      document.getElementById('name').value = name;
	      document.getElementById('jobName').value = jobName;
	  var radioButtons =  document.getElementsByName('processMethod');
		for(i=0;i<radioButtons.length;i++){
		   var textId = radioButtons[i].id + 'Text';
		   var obj = document.getElementById(textId);
		   if(element.id==radioButtons[i].id){
		      obj.setAttribute('validate','{required:true,maxlength:50}');
		      element.setAttribute('checked','checked');
		   }else{
		   	 radioButtons[i].removeAttribute('checked');
		     obj.removeAttribute('validate');
		     $(obj).parent().find('label.error').remove();	
		   }
		}
	}
</script>
</head>
<body>
	<div class="panel">
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">添加执行计划</span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="####">保存</a>
					</div>
					
					<div class="group">
						<a class="link back" href="planeList.do?name=${TriggerObject.jobName}">返回</a>
					</div>
				</div>
			</div>
		</div>
		<div class="panel-body">
			<form id="TriggerObject" method="post" action="planeSave.do">
				<table class="table-detail" cellpadding="0" cellspacing="0"
					border="0" type="main">
					<tr>
						<th width="20%">计划名称:</th>
						<td><input type="text" id="name" name="name" value="${TriggerObject.name}" class="inputText" style="width: 40%" validate="{required:true,maxlength:50}" /></td>
					</tr>
					<tr>
						<th width="100%" colspan="2" style="text-align: left">执行计划的方式</th>
					</tr>

					<tr>
						<th width="20%"><input type="radio" name="processMethod" id="oneTime" onclick="randioChange(this)">一次:</th>
						<td>
						  开始：
						 <input type="text" id="oneTimeText" class="inputText" style="width: 20%" onFocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" readonly="readonly">
						</td>
					</tr>
					<tr>
						<th width="20%"><input type="radio" name="processMethod" id="manyTimesAday" onclick="randioChange(this)">每天的每个小时:</th>
						<td>
						 <input type="text" id="manyTimesAdayText" class="inputText" style="width: 20%" onFocus="WdatePicker({dateFmt:'mm'})" readonly="readonly">
						 </td>
					</tr>
					<tr>
						<th width="20%"><input type="radio" name="processMethod" id="oneTimeAday" onclick="randioChange(this)">每天:</th>
						<td>
						 <input type="text" id="oneTimeAdayText" class="inputText" style="width: 20%" onFocus="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly">
						</td>
					</tr>
					<tr>
						<th width="20%"><input type="radio" name="processMethod" id="manyTimesAWeek" onclick="randioChange(this)">每周:</th>
						<td>
						  <input type="checkbox" id="MON" name="week" value="2">星期一
						  <input type="checkbox" id="TUE" name="week" value="3">星期二
						  <input type="checkbox" id="WED" name="week" value="4">星期三
						  <input type="checkbox" id="THU" name="week" value="5">星期四
						  <input type="checkbox" id="FRI" name="week" value="6">星期五
						  <input type="checkbox" id="SAT" name="week" value="7">星期六
						  <input type="checkbox" id="SUN" name="week" value="1">星期日
						  </br>
						  <input type="text" id="manyTimesAWeekText" class="inputText" style="width: 20%" onFocus="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly">
						</td>
					</tr>
					<tr>
						<th width="20%"><input type="radio" name="processMethod" id="manyTimesAMonth" onclick="randioChange(this)">每月:</th>
						<td>
						<input type="checkbox" id="MON1" name="month" value="1">1
						<input type="checkbox" id="MON2" name="month" value="2">2
						<input type="checkbox" id="MON3" name="month" value="3">3
						<input type="checkbox" id="MON4" name="month" value="4">4
						<input type="checkbox" id="MON5" name="month" value="5">5
						<input type="checkbox" id="MON6" name="month" value="6">6
						<input type="checkbox" id="MON7" name="month" value="7">7
						<input type="checkbox" id="MON8" name="month" value="8">8
						<input type="checkbox" id="MON9" name="month" value="9">9
						<input type="checkbox" id="MON10" name="month" value="10">10
						<input type="checkbox" id="MON11" name="month" value="11">11
						<input type="checkbox" id="MON12" name="month" value="12">12
						<input type="checkbox" id="MON13" name="month" value="13">13
						<input type="checkbox" id="MON14" name="month" value="14">14
						<input type="checkbox" id="MON15" name="month" value="15">15
						<input type="checkbox" id="MON16" name="month" value="16">16
						<input type="checkbox" id="MON17" name="month" value="17">17
						<input type="checkbox" id="MON18" name="month" value="18">18
						<input type="checkbox" id="MON19" name="month" value="19">19
						<input type="checkbox" id="MON20" name="month" value="20">20
						<input type="checkbox" id="MON21" name="month" value="21">21
						<input type="checkbox" id="MON22" name="month" value="22">22
						<input type="checkbox" id="MON23" name="month" value="23">23
						<input type="checkbox" id="MON24" name="month" value="24">24
						<input type="checkbox" id="MON25" name="month" value="251">25
						<input type="checkbox" id="MON26" name="month" value="26">26
						<input type="checkbox" id="MON27" name="month" value="27">27
						<input type="checkbox" id="MON28" name="month" value="28">28
						<input type="checkbox" id="MON29" name="month" value="29">29
						<input type="checkbox" id="MON30" name="month" value="30">30
						<input type="checkbox" id="MON31" name="month" value="31">31
						<input type="checkbox" id="MONL" name="month"  value="L">最后一天
						</br>
						   <input type="text" id="manyTimesAMonthText" class="inputText" style="width: 20%" onFocus="WdatePicker({dateFmt:'HH:mm'})" readonly="readonly">
						</td>
					</tr>
					
					<%-- <tr>
						<th width="20%"><input type="radio" name="processMethod" id="cronRadio" onclick="randioChange(this)">cron表达式:</th>
						<td><input type="text" id="cronRadioText" name="cronRadioText" value="${TriggerObject.cron}" onfocus="getCron()" class="inputText" style="width: 70%" /></td>
					</tr> --%>

				</table>
				<input type="hidden" name="cron" id="cron" value="${TriggerObject.cron}" />
				<input type="hidden" name="jobName" id="jobName" value="${TriggerObject.jobName}" />
			</form>

		</div>
	</div>
</body>
</html>
