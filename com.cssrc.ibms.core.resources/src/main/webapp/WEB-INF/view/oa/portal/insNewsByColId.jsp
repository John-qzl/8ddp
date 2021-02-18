<%@page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="ibms" uri="http://www.ibms.cn/gridFun"%>
<html>
<head>
<%@include file="/commons/portalCustom.jsp"%>
<script src="${ctx}/js/common/list.js" type="text/javascript"></script>
<link href="${ctx}/styles/portal/list.css" rel="stylesheet" type="text/css" />
<title>${insColumn.name}-公告信息列表</title>
</head>
<body>

	<div class="mini-toolbar" style="border-bottom: 0; padding: 0px;">
		<table style="width: 100%;">
			<tr>
				<td style="white-space: nowrap; border-top: 1px solid #909aa6; padding: 5px;">
					<form id="searchForm">
						标题 <input class="mini-textbox" id="subject" name="Q_subject_S_LK" emptyText="请输入标题" /> 关键字 <input class="mini-textbox" id="keywords" name="Q_keywords_S_LK" emptyText="请输入关键字" /> 作者 <input class="mini-textbox" id="author" name="Q_author_S_LK" emptyText="请输入作者" /> <a class="mini-button" iconCls="icon-search" onclick="onSearch">查询</a> <a class="mini-button" iconCls="icon-cancel" onclick="onClear">清空</a>
					</form>
				</td>
			</tr>
		</table>
	</div>
	<div class="mini-fit" style="height: 100px;">
		<div id="newsgrid" class="mini-datagrid" style="width: 100%; height: 100%;" allowResize="false" url="${ctx}/oa/portal/insNews/listByColId.do?joinAttName=insNews&colId=${insColumn.colId}" idField="newId" multiSelect="true" showColumnsMenu="true" sizeList="[5,10,20,50,100,200,500]" pageSize="20" onrowdblclick="onRowDblClick" ondrawcell="onDrawCell" allowAlternating="true">
			<div property="columns">
				<div type="checkcolumn" width="20"></div>
				<div field="action" name="action" cellCls="actionIcons" width="50" headerAlign="center" align="center" renderer="onActionRenderer" cellStyle="padding:0;">#</div>
				<div field="subject" width="150" headerAlign="center" allowSort="true">标题</div>
				<div field="keywords" width="100" headerAlign="center" allowSort="true">关键字</div>
				<div field="readTimes" width="80" headerAlign="center" allowSort="true">阅读次数</div>
				<div field="author" width="80" headerAlign="center" allowSort="true">作者</div>
			</div>
		</div>
	</div>

	<script type="text/javascript">
			mini.parse();
			//用于弹出的子页面获得父窗口
			top['main']=window;
			//console.log(${insColumn.colId});
			
			var colId='${insColumn.colId}';
			var grid=mini.get('newsgrid');
			grid.load();
			var portal = "${param['portal']}";
			function onDrawCell(e) {
				var record = e.record;
				var uid = record.pkId;
				//功能键
				if (e.field == "action") {
					if (portal == "YES") {
						e.cellHtml = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + uid + '\')"></span>';
					}
				}
				//超链接
				if (e.field == "subject") {
					var sub = record.subject;
					e.cellStyle = "text-align:left";
					e.cellHtml = '<a href="javascript:detailRow(\'' + uid + '\')">' + sub + '</a>';

				}
			}
			function onRowDblClick(e) {
				var record = e.record;
				var pkId = record.pkId;
				var row = grid.getSelected();
				var title = row.subject;
				_OpenWindow({
					title:title,
					url:__ctx+'/oa/portal/insNews/get.do?permit=no&pkId='+pkId,
					width:800,
					height:800,
					//max:true
				});
			}
			//功能键
	        function onActionRenderer(e) {
	            var record = e.record;
	            var uid = record.pkId;
	            var s = '<span class="icon-detail" title="明细" onclick="detailRow(\'' + uid + '\')"></span>'
	            + '<span class="icon-edit" title="编辑" onclick="editNew(\'' + uid + '\')"></span>'
	            + '<span class="icon-remove" title="删除" onclick="delNew(\'' + uid + '\')"></span>';
	            return s;
	        }
			
			function editNew(pkId){
				var colId = ${insColumn.colId};
				_OpenWindow({
					title:"编辑",
					url:__ctx+'/oa/portal/insColNew/edit.do?pkId='+pkId+'&colId='+colId,
					width:800,
					height:450,
				});
			}
			
			//查看明细
			function detailRow(pkId){
				var row = grid.getSelected();
				var title = row.subject;
				_OpenWindow({
					title:title,
					url:__ctx+'/oa/portal/insNews/get.do?permit=no&pkId='+pkId,
					width:800,
					height:800,
					//max:true
				});
			}
			//清空查询
			function onClear(){
				$("#searchForm")[0].reset();		
			}
			//查询
			function onSearch(){
				var formData=$("#searchForm").serializeArray();
				var filter=mini.encode(formData);
				grid.setUrl(__ctx+'/oa/portal/insNews/listByColId.do?colId='+colId+'&joinAttName=insNews&filter='+filter);
				grid.load();
			}
			
			//弹出选择新闻加入此栏目的页面
			function showInfoDialog(){
				_OpenWindow({
					title:'加入信息至栏目-${insColumn.name}',
					url:__ctx+'/oa/portal/insNews/dialog.do',
					width:800,
					height:500,
					ondestroy:function(action){
						if(action!='ok')return;
						var iframe = this.getIFrameEl();
			            var newsIds = iframe.contentWindow.getNewsIds();
			            if(newsIds=='')return;
			            _SubmitJson({
			            	url:__ctx+'/oa/portal/insNews/joinColumn.do',
			            	data:{
			            		colId:'${insColumn.colId}',
			            		newsIds:newsIds
			            	},
			            	method:'POST',
			            	success:function(e){
			            		grid.load();
			            	}
			            });
			            
					}
				});
			}
			
			//删除此栏目与此新闻的关系
			function delNew(pkId){
				var colId = ${insColumn.colId};
				if (!confirm("确定删除选中记录？")) return;
			    console.log(pkId);
		        _SubmitJson({
		        	url:__ctx + '/oa/portal/insColNew/delNew.do',
		        	method:'POST',
		        	data:{ids: pkId,
		        		  colId:colId},
		        	 success: function(text) {
		                grid.load();
		            }
		         });
			}
	</script>
</body>
</html>