<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="rx" uri="http://www.ibms.cn/formFun"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>门户编辑</title>
<%@include file="/commons/portalCustom.jsp"%>
<script type="text/javascript">
	if("${insPortal.key}"=="GLOBAL-ORG"||"${insPortal.key}"=="ORG"){
		$(function(){
			$(".abc").find("#saveButton").hide();
			$(".abc").find("#editButton").hide();
		});
	}
</script>
<style>
    span.mini-tools-more
    {
        background: url("${ctx}/styles/images/home/contenticon.png") no-repeat 0px -95px; width: 40px; height: 15px; display: block; float: left; margin-right: 10px;
    }
    span.mini-tools-refresh {
    	background: url("${ctx}/styles/images/home/contenticon.png") no-repeat -82px -95px; width: 15px; height: 15px; display: block; float: left;margin-right: 10px;
    }
    
    span.mini-tools-collapse{
    	background: url("${ctx}/styles/images/home/contenticon.png") no-repeat -56px -95px; width: 15px; height: 15px; display: block; float: left;margin-right: 10px;
    }	
</style>

</head>
<body>
	<c:if test="${curUserId < 0}">
	<div id="toolbar1" class="mini-toolbar" style="padding: 2px;">
		<table class="abc" style="width: 100%;">
			<tr>
				<td style="width: 100%;" align="right">
				
				<a class="mini-button" id="saveButton" iconCls="icon-save" plain="true" onclick="savePortal()"><b>保存</b></a>
				<a class="mini-button" id="editButton" iconCls="icon-edit" plain="true" onclick="setPort()">设置门户</a>
					<span class="separator"></span>
				<a class="mini-button" iconCls="icon-reload" plain="true" onclick="reload()">刷新</a></td> 
			</tr>
		</table>
	</div>
	</c:if>
	
	<div>
		<input id="portId" name="portId" class="mini-hidden" value="${insPortal.portId}" />
	</div>
	
	<script type="text/javascript">    	
	mini.parse();
    var portId = '${insPortal.portId}';
    var portal = new mini.ux.Portal();
    var colWidths = [${colWidths}];
  //设置门户的栏目默认宽高
    portal.set({
        style: "width: 100%;height:400px",
        columns: colWidths
        });
  //将后台的数据分割   
    portal.render(document.body);
  	var portalCols = '${portalCols}';
  	var portColsArr=jQuery.parseJSON(portalCols);
  	//
    //生成每一个栏目
    for(var i=0;i<portColsArr.length;i++){
    	portColsArr[i].column=portColsArr[i].colNum;
    	portColsArr[i].id=portColsArr[i].colId;
    	portColsArr[i].title=portColsArr[i].colName;
   		if(portColsArr[i].loadType=='TEMPLATE'){
    		portColsArr[i].url=__ctx+'/oa/portal/insPortal/getPortalHtml.do?colId='+portColsArr[i].colId;
    	}else{

    		portColsArr[i].url=portColsArr[i].url;
    	} 
    	if(portColsArr[i].iconCls==null){
    		portColsArr[i].iconCls='icon-detail';
    	}
    	portColsArr[i].buttons='more refresh collapse';
    	portColsArr[i].showCollapseButton= true;
    	portColsArr[i].onbuttonclick='onbuttonclick';
    	portColsArr[i].refreshOnExpand=true;
    	portColsArr[i].moreUrl=portColsArr[i].moreUrl;
    	
    	//判断是前台html片段还是后台片段
    	portal.addPanel(portColsArr[i]);
    	
    }
  	
    var pans =portal.getPanels();
  	//刷新
	function reload() {
			location.reload();
	}
	//点击more按钮触发的函数
	function onbuttonclick(e) {
        if(e.name=="more"){
        	var moreUrl = e.source.moreUrl;
        	var name = e.source.title;
        	var colId = e.source.el.id;
        	mgrNewsRow(colId,name,moreUrl);
        }
        if(e.name=="refresh"){
        	this.reload();
        }
	}
	//打开一个新的页面显示这个栏目的more
	function mgrNewsRow(colId,name,moreUrl){
		top['index'].showTabFromPage({
			title:name,
			tabId:'colNewsMgr_'+colId,
			url:moreUrl
		});
	}
	//打开设置门户页面
	function setPort(){
		top['index'].showTabFromPage({
			title:'编辑门户',
			tabId:'portId_'+portId,
			iconCls:'icon-window',
			url:__ctx+'/oa/portal/insPortal/personShowEdit.do'
		});
	}
	//保存
	function savePortal(){
		var panels = [];
		//将每个栏目遍历存起来
		for(var i=0;i<pans.length;i++){ 
			panels.push({
				id:pans[i].id,
				title:pans[i].title,
				column:pans[i].column,
				sn:i,
				height:pans[i].height
			});
		}
		var data = mini.encode(panels);
		_SubmitJson({
			url : __ctx + '/oa/portal/insPortal/savePortal.do?portId='+ portId ,
			method : 'POST',
			data : {data:data}
		});
	}
</script>
</body>
</html>