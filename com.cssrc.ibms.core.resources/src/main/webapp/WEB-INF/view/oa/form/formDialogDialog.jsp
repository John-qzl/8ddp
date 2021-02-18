<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions" %>
<c:set var="ctx" value="${pageContext.request.contextPath}" />
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
<title>对话框选择</title>
<f:link href="Aqua/css/ligerui-all.css"></f:link>
<script type="text/javascript" src="${ctx}/jslib/jquery/jquery.js"></script>
<script src="${ctx}/jslib/util/util.js"></script>
<script src="${ctx}/jslib/lg/base.js" type="text/javascript"></script>
<script src="${ctx}/jslib/lg/ligerui.all.js"></script>


<script src="${ctx}/jslib/lg/plugins/ligerResizable.js" type="text/javascript"></script>
<script src="${ctx}/jslib/lg/plugins/ligerCheckBox.js" type="text/javascript"></script>
<script src="${ctx}/jslib/lg/plugins/ligerComboBox.js" type="text/javascript"></script>
<script src="${ctx}/jslib/lg/plugins/ligerGrid.js" type="text/javascript"></script>
<script src="${ctx}/jslib/lg/plugins/ligerFilter.js" type="text/javascript"></script>


<script type="text/javascript">
	var dialog_;
	var manager;
	var TableFiledList;
	var DialogFiledList;
	var DialogCombox;
	var rpcbeaNameComBox;
	var conf;
	if (frameElement) {
		//
		dialog_ = frameElement.dialog; //调用页面的dialog对象(ligerui对象)
		conf = dialog_.get('conf');
		var tableFields = conf.allFileds;
		tableFields.addField(conf.curentFiled);
		TableFiledList = tableFields.Fields;
	}
	var dialogText;
	var dialogAlias;
	function getGridOptions(checkbox) {
		var options = {
			columns : [ {
				hide : true,
				display : 'id',
				name : 'id',
				align : 'left',
				width : 100,
				minWidth : 60
			}, {
				display : '名称',
				name : 'name',
				align : 'left',
				width : 100,
				minWidth : 60
			}, {
				display : '别名',
				name : 'alias',
				minWidth : 120,
				width : 100
			}, {
				display : '显示样式',
				name : 'style',
				minWidth : 140,
				width : 100,
				render : function(record, rowindex, value, column) {
					if (value == 0) {
						return "列表";
					} else {
						return "属性";
					}
				}
			}, {
				display : '是否单选',
				name : 'issingle',
				width : 100,
				render : function(record, rowindex, value, column) {
					if (value == 1) {
						return "单选";
					} else {
						return "多选";
					}
				}
			}, {
				display : '宽度',
				name : 'width',
				width : 100
			}, {
				display : '高度',
				name : 'height',
				width : 100
			}, {
				display : '是否为表',
				name : 'istable',
				width : 100,
				render : function(record, rowindex, value, column) {
					if (value == 0) {
						return "视图";
					} else {
						return "数据库表";
					}
				}
			}, {
				display : '对象名称',
				name : 'objname',
				width : 100
			} ],
			onSuccess : function(data) {
				//
				//初始化字段映射器
	 			if (conf&&conf.relation) {
	 				var strRel=conf.relation;
					var relation = strRel.replace("<![CDATA[", "").replace("]]>", "");
					relation=relation.replace("\n", "");
					var _relation =eval("("+relation+")");

					//var _relation =$.parseJSON("[{name:'ssbj',fields:[{'target':'ssbjID','src':'','listShow':false}],query:[],rpcrefname:''}]");

					//var _relation =JSON.stringify(relation);
					//DialogCombox.selectValue(row.alias);
					for (var i = 0; i < data.Rows.length; i++) {
						var row = data.Rows[i];
						if (row.alias == _relation.name) {
							dialogText = row.name;
							dialogAlias = _relation.name;
							//DialogCombox.setValue(row.alias);
							//DialogCombox.setText(row.name);
							break;
						}
					} 
				}

			},
			async : false,
			dataAction : "local",
			switchPageSizeApplyComboBox : false,
			url : '${ctx}/oa/form/formDialog/dialogData.do',
			pageSize : 10,
			checkbox : checkbox
		};
		return options;
	}
	var condition = {
		fields : [ {
			name : 'name',
			label : '名称',
			width : 90,
			type : 'text'
		} ]
	};

	$(function() {
		
		$(".link.var.help").mouseover(function() {
			$(this).ligerTip({
				content : $("#reldialogHelp").html(),
				width:230
			});
			$(this).mouseout(function() {
				$(this).ligerHideTip();
			})

		})
		rpcbeaNameComBox=$("#rpcbeaName").ligerComboBox({
			url:'${ctx}//oa/form/formTable/getAllDubboService.do',
			valueField : 'intf',
			textField : 'intf',
			selectBoxHeight : 300,
			width : 200,
			onSelected : function(rpcbeaName) {
				grid =DialogCombox.grid;
				var param = {};
				param['rpcbeaName'] = rpcbeaName;
				grid.set('parms', param);
				grid.reload();
			},
			autocomlate:true,
			keySupport:true
		})
		
		
		DialogCombox = $("#formDialog").ligerComboBox({
			height: 25,
			width : 250,
			slide : false,
			selectBoxWidth : 600,
			selectBoxHeight : 400,
			condition : condition,
			valueField : 'alias',
			textField : 'name',
			delayLoadGrid : false,
			grid : getGridOptions(false),
			onSelected : function(alias) {
				if (alias) {
					f_initGrid(alias, null);
				}

			},
			onAfterSetData : function() {
			},
			conditionSearchClick : function(e) {
				grid = e.grid;
				var self = this;
				//alert(this.grid.pageSize)
				//alert(e.rules[0]['value']);
				var param = {};
				param['Q_name_SL'] = e.rules && e.rules[0] ? e.rules[0]['value']
						: '';
				param['rpcbeaName'] = rpcbeaNameComBox.getValue();
				param['page'] = grid.options.page;
				param['pageSize'] = grid.options.pageSize;
				grid.set('parms', param);
				grid.reload();
			}
		});
		//
		
		
		var relation = conf.relation.replace("<![CDATA[", "")
				.replace("]]>", "");
		var _relation = eval('(' + relation + ')');
		
		if(_relation.rpcrefname&&_relation.rpcrefname!=""){
			rpcbeaNameComBox.setValue(_relation.rpcrefname);
			rpcbeaNameComBox.setText(_relation.rpcrefname);
		}
		f_initGrid(dialogAlias, _relation.fields);
		DialogCombox.setValue(dialogAlias);
		DialogCombox.setText(dialogText);
	});

	function f_initGrid(alias, fields) {
		var rpcbeaName=rpcbeaNameComBox.getValue();
		var url = '${ctx}/oa/form/formDialog/dialogResultFiled.do?alias='
				+ alias+"&rpcbeaName="+rpcbeaName;
		$.ajax({
			url : url,
			async : false,
			success : function(data) {
				DialogFiledList = data.fileds;
				manager = $("#maingrid").ligerGrid({
					columns : [{
						display : '表字段',
						name : 'tableField',
						width : 200,
						type : 'text',
						editor : {
							valueField : 'fieldName',
							textField : 'fieldDesc',
							data : TableFiledList,
							keySupport : true,
							type : 'select'
						},
						render : function(item) {
							if (!item) {
								return "";
							}
							for (var i = 0; i < TableFiledList.length; i++) {
								if (TableFiledList[i]['fieldName'] == item.tableField) {
									return TableFiledList[i]['fieldDesc'];
								}
							}
							return item.fieldDesc;
						}
					},
					{
						display : '对话框返回列',
						width : 200,
						name : 'dialogField',
						type : 'text',
						editor : {
							valueField : 'fieldName',
							textField : 'comment',
							data : DialogFiledList,
							type : 'select'
						},
						render : function(item) {
							if (!item) {
								return "";
							}
							for (var i = 0; i < DialogFiledList.length; i++) {
								if (DialogFiledList[i]['fieldName'] == item.dialogField)
									return DialogFiledList[i]['comment']
							}
							return item.comment;
						}
					},
					{
						display : '是否为显示列',
						name : 'listShow',
						align : 'center',
						type : 'checkbox',
						width : 100,
						editor : {
							align : 'center',
							type : 'checkbox'
						},
						render : function(item) {
							if (!item) {
								return "否";
							}
							if (item.listShow) {
								return "是";
							} else {
								return "否";
							}
						}
					}],
					onSelectRow : function(rowdata,
							rowindex) {
						$("#txtrowindex").val(rowindex);
					},
					enabledEdit : true,
					isScroll : false,
					checkbox : true,
					usePager : false,
					rownumbers : true,
					width : 560
				});
				$(fields).each(function(i, f) {
					var filed = new Object();
					if (f.target.indexOf('XXXXXFKColumnShow') < 0) {
						filed.tableField = f.target;
					}
					filed.dialogField = f.src;
					filed.listShow = f.listShow;
					var row = manager.getSelectedRow();
					manager.addRow(filed, false);
				})
			}
		})

	}

	function deleteRow() {
		manager.deleteSelectedRow();
	}
	function addNewRow() {
		var dialogId = DialogCombox.getValue();
		var rpcrefname = $("#rpcbeaName").val();
		if (!dialogId) {
			alert("请先确定对话框信息！");
		}
		var row = manager.getSelectedRow();
		manager.addRow({
			tableName : '',
			dialogField : '',
			listShow : false
		}, false);
	}
	function ok() {
		//<![CDATA[{name:'ssbj',fields:[{src:'ID',target:'ssbj'},{src:'F_bjmc',target:'ssbjXXXXXFKColumnShow'}],query:[],rpcrefname:''}]]>
		//{name:'ssbj',fields:[{src:'ID',target:'ssbj'},{src:'F_bjmc',target:'ssbjXXXXXFKColumnShow'}],query:[],rpcrefname:''}
		var fileds = new Array();

		var rpcrefname = $("#rpcbeaName").val();
		var dialogAlias = DialogCombox.getValue();
		if(manager){
			$(manager.rows).each(function(i, row) {
				//
				var filed = new Object();
				filed.target = row.tableField ? row.tableField: conf.curentFiled.fieldName + "XXXXXFKColumnShow";
				filed.src = row.dialogField ? row.dialogField : "";
				filed.listShow = row.listShow;
				fileds.push(filed);
			})
		}
		if(fileds.length<1){
			alert("请配置列映射关心！");
			return;
		}
		var _fields = JSON.stringify(fileds);
		_fields = _fields.replaceAll("\"", "'");
		var returnVal = "<![CDATA[{'name':'" + dialogAlias + "'";
		returnVal += ",'fields':" + _fields;
		returnVal += ",'query':[]";
		returnVal += ",'rpcrefname':'" + rpcrefname + "'";
		returnVal += "}]]>";
		dialog_.get("sucCall")(returnVal)
		dialog_.close();
	}
</script>
</head>
<body>
	<div style="margin-left: 10px;">
		<input type="button" value="保存" onclick="ok()" />
		<a href="javascript:;" class="link var help" style="margin-left: 10px;display: inline-block;">帮助</a>
		<table class="table-detail" border="0" cellpadding="0" cellspacing="0" style="margin-left: 10px;">
			<tbody>
				<tr style="height:35px">
					<th  width="150" style="align:right">rpc模式beanName：</th>
					<td>
						<input type="text" id="rpcbeaName" style="height:25px" name="rpcbeaName">
					</td>
				</tr>
				<tr style="height:35px">
					<th width="150" style="align:right">选择自定义对话框：</th>
					<td>
						<input type="text" id="formDialog"  name="formDialog" >
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<br />
	<div style="margin-left: 10px;">
		<a class="l-button" style="width: 100px; display: inline-block;" onclick="addNewRow()">添加一行</a>
		<a class="l-button" style="width: 120px; display: inline-block;" onclick="deleteRow()">删除选中行</a>
		<div id="maingrid" style="margin-top: 10px; hight: 100"></div>
	</div>
	
	<div id="reldialogHelp" style="display:none">
	<br/>
	(1)请先定义:左侧选中的"关联关系表"为主表的自定义对话框,"关联关系表"需要设定主键显示值，否则显示ID列。
	<br/>(2)<font color=red>如果是rpc服务，请先选择rpc模式beanName。</font>
	<br/><font color=red>其他配置相同，只是对话框信息是通过rpc来获取的。</font>
	<br/>(3)对话话配置分为三列，表字段，对话框返回数据字段，是否为显示列
	<br/>(4)<font color=red>表字段可以不选择，表示虚拟字段，系统会按照格式生成虚拟字段</font>
	<br/>(5)显示列只能勾选一个。系统会在对应的显示列后面追加 ‘选择’以及‘重置’按钮。
	<br/>(6)不同的对话框字段可以对应同一个表字段，系统会按照配置的顺序追加到表字段中。
	<br/>(7)此字段作为业务数据模板中的“查询条件字段”，如修改了“关联的自定义对话框信息”，需要在表单设计中重新保存业务数据模板。
	<br/>(8)模板文件修改了，需要最新的模板文件。
</div>
</body>
</html>



