<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@include file="/commons/include/html_doctype.html"%>
<html>
<head>
<%@include file="/commons/include/form.jsp" %>
<title>添加表</title>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/ColumnDialog.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/EditTable.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FieldsManage.js"></script>
<script type="text/javascript" src="${ctx}/jslib/ibms/oa/form/FormTableDialog.js"></script>

<script type="text/javascript">
	var tableId=${tableId};
	var validator;
	var fieldObj;
	var canEditTbColName=${canEditTbColName};
	var mainTableIsPublished='${mainTableIsPublished}';	
	var table=null;
	var returnUrl=("${returnUrl}"==null||"${returnUrl}"=="")?("list.do"):("${returnUrl}");
	//var retUrl="${returnUrl}";
	$(function(){
		var isEdit=tableId>0;//[编辑:{isEdit：true},新增:{isEdit：false}]
		fieldObj=new FieldsManage();//字段管理对象
		mainTableIsPublished = ~~mainTableIsPublished;
		TableRow.setFieldManage(fieldObj);
		TableRow.setAllowEditColName(canEditTbColName);
		TableRow.setIsExternal(0);
		TableRow.setIsEdit(isEdit);
		if(isEdit){
			//根据表的Id返回表和字段对象。
			$.get("getByTableId.do",{tableId:tableId,mainTableIsPublished:mainTableIsPublished},function(data){
				bindTable(data,canEditTbColName,mainTableIsPublished);//方法在EditTable.js中
				table=data.table;
			});
		}
		
		
		//行高亮
		$("#tableColumnItem").delegate("tbody>tr", "click", function() {
			$("#tableColumnItem>tbody>tr").removeClass("over");
			$(this).addClass("over");
		});
		//编辑列的详细信息
		$("#tableColumnItem").delegate("tbody>tr a[name='editColumn']", "click", function() {
			//
			var trObj=$(this).parent().parent();
			var idx=$("#tableColumnItem>tbody>tr").index(trObj);
			var field=fieldObj.getFieldByIndex(idx);
			TableRow.editField(field.fieldName,$("input[name='isMain']:checked").val());
		});
		//处理选项选择。
		$("#tableColumnItem").delegate("tbody>tr input:checkbox", "click", function() {
			var chkObj=$(this);
			TableRow.editFieldOption(chkObj);
		});
		//处理点击列名和列注释。
		$("#tableColumnItem").delegate("tbody>tr>td[class='editField']", "click", function() {
			var tdObj=$(this);
			TableRow.editNameComment(tdObj);
		});
		
		//字段上移动
		$("#moveupField").click(function(){
			TableRow.move(true);
		});
		//字段下移
		$("#movedownField").click(function(){
			TableRow.move(false);
		});
		
		$("#delField").click(function(){
			TableRow.del();
		});
		
		validator=validTable();
		//处理字段保存
		
		$("#dataFormSave").click(function(){
			handSave();
		});
		//处理点击主表子表的radio事件。
		handIsMain();
		
		// 点击返回时判断字段是否修改
		$("a.back").click(function(){		
			if(isEdited){
				$.ligerDialog.confirm("自定义表已修改，是否进行保存?","提示",function(rtn){
					if(!rtn){
						location.href=returnUrl;
					}
					else{
						handSave();
					}
				});
			}else{
				location.href=returnUrl;
			}
		});
		$("a.moreinfo").click(function(){
			var me = $(this),
				hrefStr = me.attr('hrefstr');
			if(!hrefStr)return;
			openDetailWin({url:hrefStr});
		});
		$("a.table").click(generatorSubToMain);
		$("input[name='publishedAsMain']").click(switchMainType).each(function(){
			var val = $(this).val();

			if(mainTableIsPublished){
				if(~~val){
					$(this).attr("checked",true).trigger("click");
				}
				else{
					$(this).attr("checked",false).trigger("click");
				}
			}
		});
	});

	//为已生成的主表  生成子表
	function generatorSubToMain(){
		$.ligerDialog.confirm("确定要生成子表吗?","提示",function(rtn){
			if(!rtn) return;
			handSave(1);
		});
	}

	function choosePublishMain(){
		var callBack=function(tableId,tableName){
			$("#mainTableId").val(tableId);
			$("#tableName").val(tableName);
		}
		
		FormTableDialog({callBack:callBack,isExternal:0});
	}

	function switchMainType(){
		var me = $(this),
			state = me.val();
		if(~~state){
			$("#mainTable").hide();
			$("#choose-div").show();
			$("div.generate").show();
			$("#mainTableId").val("");
			$("#mainTable").val(0);
		}
		else{
			$("#mainTable").show();
			$("#choose-div").hide();
			$("div.generate").hide();
			$("#mainTableId").val("");
			$("#tableName").val("");
		}
	}

	function openDetailWin(conf){
		var dialogWidth=1000;
		var dialogHeight=700;
		
		conf=$.extend({},{dialogWidth:dialogWidth ,dialogHeight:dialogHeight ,help:0,status:0,scroll:0,center:1},conf);
		
		DialogUtil.open({
			height:conf.dialogHeight,
			width: conf.dialogWidth,
			title : '新窗口',
			url: conf.url, 
			isResize: true
		});		
	}
	
	/**
	 * 处理保存事件。
	 */
	function handSave(generate){
			var rtn=validator.form();
			if(!rtn) return;
			
			var isMain=$("input[name='isMain']:checked").val();
		
			var name=$("#name").val();
			var comment=$("#comment").val();
			//处理主表字段。
			var mainTableId=$("#mainTableId").val();
			
			var objMainTable=$("#mainTable");
			var isPublished = table? ((table.isPublished == 1)?false:true):true;
			//所属主表
			var isVisible=$("#spanMainTable").is(":visible");
			
			if(isVisible){
				//如果修改了主表的值，则重新选取主表。
				var publishedAsMain=$("input[name='publishedAsMain']:checked").val();
				if(publishedAsMain==0){
					mainTableId=$("#mainTable").val();
				}
			}
			
			if(mainTableId=="" || mainTableId=="0"){
				if(isMain !=1 && canEditTbColName && isPublished){
					$.ligerDialog.error("请选择所属主表!","提示");
					 return;
				}
			}
			
			var tableJson={tableName:name,tableDesc:comment,isMain:isMain,mainTableId:mainTableId};
			table= $.extend({}, table, tableJson);
			var tableJson=JSON2.stringify(table);
			
			if(!fieldObj.Fields||fieldObj.Fields.length==0){
				$.ligerDialog.warn("没有添加字段信息","提示");
				return;
			}
			var fieldJson=JSON2.stringify(fieldObj.Fields);
			if(generate==undefined){
				generate=0;
			}
			//
			//实现类在FormTableFormController.java
			$.post("saveTable.do",{table:tableJson,fields:fieldJson,generator:generate},showResponse);
	}
	
	/**
	 * 显示返回结果。
	 */
	function showResponse(data){
		var obj=new com.ibms.form.ResultMessage(data);
		if(obj.isSuccess()){//成功
			$.ligerDialog.confirm("操作成功,继续操作吗?","提示",function(rtn){
				if(!rtn){
					location.href=returnUrl;
				}
				else{
					//
					//window.location.href=window.location.href;
					window.location.href =location.href.getNewUrl() ;
					//window.location.reload();
				}
			});
	    }else{//失败
	    	$.ligerDialog.err("提示","添加自定义表失败",obj.getMessage());
	    };
	}
	
	function clickAddRow(){
		//FieldsManage.js中搜索TableRow.addColumn=function(isMain)查找js实现
		TableRow.addColumn($("input[name='isMain']:checked").val());
	}
	
	//根据注释生成表名
	function autoGetTableKey(inputObj){
		var url=__ctx + '/oa/form/formTable/getTableKey.do' ;
		var subject=$(inputObj).val();
		if($.trim(subject).length<1) return;
		$.post(url,{'subject':subject,'tableId':'${tableId}'},function(response){
			var json=eval('('+response+')'); 
			if(json.result==1 ){
				if($.trim(	$('#name').val()).length<1){
					$('#name').val(json.message);
				};
			}else{
				$.ligerDialog.error(json.message,"提示");
			}
		 });
	}
</script>
</head>
<body>
	
	<div class="panel">
	<!--12017-05-12:去掉了hide-panel,为了保证table布局一致性(rzq)-->
		<div class="panel-top">
			<div class="tbar-title">
				<span class="tbar-label">
				<c:choose>
					<c:when test="${tableId==0}">
						添加自定义表
					</c:when>
					<c:otherwise>
						编辑自定义表
					</c:otherwise>
				</c:choose> </span>
			</div>
			<div class="panel-toolbar">
				<div class="toolBar">
					<div class="group">
						<a class="link save" id="dataFormSave" href="javascript:void(0);">保存</a>
					</div>
					
					<div class="group"><a class="link back">返回</a></div>
					
					<div class="group"><a onclick="clickAddRow()" class="link add">添加列</a></div>
					
					<div class="group"><a id="moveupField" class="link moveup">上移</a></div>
					
					<div class="group"><a id="movedownField" class="link movedown">下移</a></div>
					
					<div class="group"><a id="delField" class="link del ">删除</a></div>
					<div class="group generate hidden"><a class="link table" href="####">生成表</a></div>
				</div>
			</div>
		</div>
		<!--2017-05-12：panel-body放在form表单外-->
	<div class="panel-body"><!-- 原来是panel-detail -->
		<form id="bpmTableForm" method="post" action="saveTable.do">
				<!--2017-05-12：增加panel-table（rzq）-->
			<div class="panel-table">
				<table class="table-detail" cellpadding="0" cellspacing="0" border="0">
					<tr>
						<th width="5%">注释: </th>
						<td width="15%">
							<input  type="text" id="comment" name="comment"  class="inputText longInputText"   onblur="autoGetTableKey(this)"/>
						</td>
						
						<th width="5%">表名: </th>
						<td width="15%">
							<input type="text" id="name" name="name" maxlength='25' class="inputText longInputText"/>							
						</td>
					</tr>
						
					<tr>
					
						<th width="5%">是否主表: </th>
						<td width="15%">
							<label><input  type="radio"  name="isMain"  value="1" checked="checked"/>主表</label>
							<label><input  type="radio"  name="isMain"  value="0" />从表</label>
						</td>
						<th width="5%">所属主表:</th>
						<td width="15%">
							<div>
								<span id="showMainTable">
									<a href="####" class="moreinfo"></a>
								</span>
								<div id="spanMainTable" style="display:none;">
									<label><input type="radio" value="0" checked="checked" name="publishedAsMain"/>未生成的表</label>
									<label><input type="radio" value="1" name="publishedAsMain"/>已生成的表</label>
									<input type="hidden" value="" name="mainTableId" id="mainTableId">
									<select id="mainTable" name="mainTable">
										<option value="0">---请选择---</option>
										<c:forEach items="${mainTableList}" var="table">
											<c:if test="${tableId==0||table.tableId != tableId}">
												<option value="${table.tableId}">${table.tableDesc}</option>
											</c:if>
										</c:forEach>
									</select>
									<span class="hidden" id="choose-div">
										<input type="button" value="选择已生成的主表" onclick="choosePublishMain()"/>
										<input type="text" id="tableName" name="tableName" value="" readonly="readonly">
									</span>
								</div>
							</div>
						</td>
					</tr>
				</table>
			
				
				
				<table id="tableColumnItem" class="table-grid table-list" id="0" cellpadding="1" cellspacing="1" style="margin-top: 3px;">
			   		<thead>
			   			<th width="10%">列名</th>
			   			<th width="15%">注释</th>
			    		<th width="10%">类型</th>
			    		<th width="5%">必填</th>
			    		<th width="10%">控件类型</th>
			    		<th width="5%" name="Listth">显示到列表</th>
			    		<th width="4%" >是否流程变量</th>
			    		<th width="5%" >是否主键显示值</th>
			    		
						<c:if test="${!canEditTbColName}">
			    		<th width="6%">是否删除</th>
			    		</c:if>
			    		<th width="4%" >唯一约束</th>
			    		<th width="4%">是否主数据</th>
			    		<th width="4%" >是否外键</th>
			    		<th width="10%" >关联关系列</th>
			    		<th width="5%">管理</th>
			    	</thead>
			    	<tbody>
			    		
			    	</tbody>
			   	 </table>
		   	 </div>
		</form>
	</div>
	
</div>
</body>
</html>
