<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html" %>
<html>
<head>
<title>上级类型部门负责人选择</title>
<%@include file="/commons/include/form.jsp" %>
<script type="text/javascript"	src="${ctx }/jslib/ibms/displaytag.js"></script>
<script type="text/javascript" src="${ctx}/jslib/lg/plugins/ligerLayout.js"></script>

<script type="text/javascript" src="${ctx}/servlet/ValidJs?form=bpmNodeUserUplow"></script>
<f:link="from-jsp.css"></f:link>

<script type="text/javascript">

var dialog = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
var params;
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
	var level = $('select:[name=level]');
	level.children().remove();
	for(var i=0;i<(sysOrgTypeList.length-1);i++){
			if (sysOrgTypeList[i][0]==currentDem){
				level.html(level.html()+'<option value="'+sysOrgTypeList[i][1]+'">'+sysOrgTypeList[i][2]+'</option>');
			}	
	}	
}


$(function(){
	changeDemId($('select:[name=demensionId]'));	
	params=dialog.get("args");
	var jsonStr=params.cmpIds;	
	var json;
	if(jsonStr && jsonStr.length>0)
		json=eval('('+jsonStr+')');
	if(json)
		eidtInit(json);
});

function eidtInit(json){
		$('select:[name=demensionId]').children().each(function(){
				if($(this).val()==json.demId){
					$(this).attr('selected','true');
				}
		});
		changeDemId($('select:[name=demensionId]'));
		$('select:[name=level]').children().each(function(){
			if($(this).val()==json.level){
				 $(this).attr('selected','true');
			}
		});
		$('input:radio[name=OrgSource]:[value='+json.orgSource+']').attr('checked','true');
		$('input:radio[name=stategy]:[value='+json.stategy+']').attr('checked','true');
}

//点击选择时的数据处理
function select(){
	//var currentLevel=$(' select:[name=level]').val();
	//if(!currentLevel || currentLevel.length<1){
	//	$.ligerDialog.warn("请选择部门类型，如没可选请先通知 管理员添加部门类型!","提示信息");
	//	return;
	//}
	var demId=$('select:[name=demensionId]').val();
	var level=$('select:[name=level]').val();
	var orgSource=$('input:radio[name=OrgSource]:checked').val();
	var currentLevelTxt=$('select:[name=level]').find("option:selected").text();
	var stategy=$('input:radio[name=stategy]:checked').val();
	var demTxt=$('select:[name=demensionId]').find("option:selected").text();
	var orgSourcetxt=$('input:radio[name=OrgSource]:checked').next().html();
	var stategyTxt=$('input:radio[name=stategy]:checked').next().html();
	//var hiddenJson = {};
	//hiddenJson.demId = demId;
	//hiddenJson.orgSource = orgSource;
	//hiddenJson.stategy = stategy;
	//hiddenJson.level = level;
	//将对象转换为字符串
	//这样的写法能避免当对象中某属性值为空时显示为null值，让它显示为""。
	//hiddenJson = JSON2.stringify(hiddenJson);
	//拼接的json串，隐藏输入
	if(level!=null){
		var hiddenJson ="{\"demId\":\""+demId+"\",\"orgSource\":\""+orgSource+"\",\"stategy\":\""+stategy+"\",\"level\":\""+level+"\"}";
	}else{
		var hiddenJson ="{\"demId\":\""+demId+"\",\"orgSource\":\""+orgSource+"\",\"stategy\":\""+stategy+"\",\"level\":\"\"}";
	}
	var showTxt="维度:"+demTxt+";类型:"+orgSourcetxt+";查找策略:"+stategyTxt+";组织类型:"+currentLevelTxt;
	var rtn={json:hiddenJson,show:showTxt};
	dialog.get("sucCall")(rtn);
	dialog.close();
};
</script>
</head>
<body>
<div  style="height:280px">  				
							<input type="hidden" name="userId" value="${userId }">							
							<table id="demensionItem" cellpadding="1" cellspacing="1"  class="table-grid">
								<head>								
									<th style="text-align: center;">分类</th>
									<th style="text-align: center;">选项</th>
								
								</head>
								<tbody>									
										<tr>
											<td >维度</td>
											<td>
														<select name="demensionId" style="width: 70%;" onchange="changeDemId(this)">
															<c:forEach items="${demensionList}" var="d" >
																	<option value="${d.demId}" >${d.demName}</option>
															</c:forEach>
														</select>
											</td>
										</tr>
										<tr>
											<td>类型</td>
											<td>
														<input type="radio"  name="OrgSource"    value="0"  checked="checked"  /> <span>发起人 </span>
														<input type="radio"  name="OrgSource"'  value="1" /> <span>上一步执行者</span>
											</td>
										</tr>
										<tr>
											<td>查找策略</td>
											<td>
														<input type="radio"  name="stategy"    value="0"  checked="checked"/><span>查找指定组织类型，为空时继续向上一级查询 </span>
														<br/><br/>
														<input type="radio"  name="stategy"'  value="1" /><span>只查指定组织类型，为空时不继续往上找</span>
												
											</td>										
										</tr>
										<tr>
											<td >组织类型</td>
											<td>
												<select name="level" style="width: 70%;">
												</select>
											</td>
										</tr>
								</tbody>
							</table>				
<div position="bottom"  class="bottom" style='margin-top:10px'>
		<a href='####' class='button'  onclick="select()" ><span class="icon ok"></span><span >选择</span></a>
		<a href='####' class='button'  style='margin-left:10px;' onclick="dialog.close()"><span class="icon cancel"></span><span >取消</span></a>
</div>
</div>



</body>
</html>


