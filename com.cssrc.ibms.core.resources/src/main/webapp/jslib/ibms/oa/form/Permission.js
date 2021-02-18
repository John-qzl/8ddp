/**
 * 初始化函数：loadByNode、loadPermission、loadByActDefId
 * 获取页面设置后的权限：getPermissionJson
 * 表单权限。
 * @returns {Permission}
 */
Permission=function(){
	{
		this.TableGroupPermission=[];//主表分组权限
		this.FieldsPermission=[];//主表字段权限。
		
		this.FilesPermission=[];//文件附件权限
		
		this.SubTablePermission=[];//sub表权限
		this.SubTableFilePermission=[];//sub表字段权限
		this.subTableShows=[];//sub子表是否显示
		
		this.RelTablePermission=[];//rel表权限
		this.RelTableFilePermission=[];//rel表字段权限
		this.relTableShows=[];//rel表是否显示
		this.Opinion=[];
		
		this.FieldOperate = {
				file :[
				       {key:'download',value:'下载',checked:true},
				       {key:'del',value:'删除',checked:true},
				       {key:'upload',value:'上传',checked:true}
				      ]
		};
	};
	/**
	 * 获取默认权限对象。
	 */
	this.getDefaultPermission=function(name,memo){
		var permission={"title":name,"memo":memo,"read": {"type":"everyone","id":"", "fullname":""},"write":{"type":"everyone","id":"", "fullname":""},"required":{"type":"everyone","id":"", "fullname":""}};
		return permission;
	};
	
	
	/**
	 * 从数据库加载权限，并初始化html表格状态。
	 * 需要传入tableId，formDefId。
	 * 如果是新建表单，权限根据表获取。
	 * 如果是更新表单，权限从表单权限获取。
	 */
	this.loadPermission=function(tableId,formKey,platform){
		var params={tableId:tableId,formKey:formKey,platform:platform};
		this.load("getPermissionByTableFormKey.do", params);
	};
		
	/**
	 * 从数据库加载权限，并初始化html表格状态。
	 */
	this.loadByNode=function(actDefId, nodeId,formKey,parentActDefId,platform){
		var params={actDefId:actDefId,nodeId:nodeId,formKey:formKey,parentActDefId:parentActDefId,platform:platform};
		this.load("getPermissionByFormNode.do", params);
	};
	
	/**
	 * 从数据库加载权限，并初始化html表格状态。
	 */
	this.loadByActDefId=function(actDefId,formKey,parentActDefId,platform){
		var params={actDefId:actDefId,formKey:formKey,parentActDefId:parentActDefId,platform:platform};
		this.load("getPermissionByActDefId.do", params);
	};
	
	this.load=function(url,params){
		var _self=this;
		$.ligerDialog.waitting("正在加载表单权限,请稍后...");
		$.post(url, params,function(data){
			$.ligerDialog.closeWaitting();			
			var tables =data["table"];//主表权限json串
			var fields =data["field"];//主表字段权限json串
			var fileAttachs =data["fileAttach"];//文件夹附件json串
			var opinions =data["opinion"];//意见权限json串
			var tableShows =data["tableShow"];////子表显示与否
			var tableGroupShows =data["tableGroupShows"];////子表显示与否
			var reltables =data["reltable"];//rel表权限json串
			var tableRelShows =data["tableRelShow"];//rel表权限json串
			//表分组权限
			if(tableGroupShows){
				_self.TableGroupPermission=tableGroupShows;
				var fieldHtml=_self.getPermission(_self.TableGroupPermission,"tablegroup");
				$("#tableGroupPermission").empty();
				$("#tableGroupPermission").append(fieldHtml);
				_self.initStatus("tableGroupPermission");
			}
			//字段权限。
			if(fields!=undefined && fields!=''){
				_self.FieldsPermission=fields;
				var fieldHtml=_self.getPermission(_self.FieldsPermission,"field");
				$("#fieldPermission").empty();
				$("#fieldPermission").append(fieldHtml);
				_self.initStatus("fieldPermission");
			}
			//文件附件权限 by YangBo
			if(fileAttachs!=undefined && fileAttachs!=''){
				_self.FilesPermission=fileAttachs;
				var fieldHtml=_self.getPermission(_self.FilesPermission,"fileAttach");
				$("#filesPermission").empty();
				$("#filesPermission").append(fieldHtml);
				_self.initStatus("filesPermission");
			}
			//子表权限
			if(tables!=undefined && tables!=''){
				_self.SubTablePermission=tables;
				_self.setSubTableFilePermission(tables);//由子表权限解析并获取子表字段的权限并放入subTableFilePermission。
				var tableHtml=_self.getPermission(_self.SubTablePermission,"subtable");
				$("#tablePermission").empty();
				$("#tablePermission").append(tableHtml);
				for ( var i = 0; i < _self.SubTablePermission.length; i++) {    //按子表table的ID区域去初始化控件状态
					var tablePermission=_self.SubTablePermission[i];
					_self.initStatus("tableId_"+tablePermission.tableId);
				}
			}else{
				$("#tablePermission").closest( 'table' ).hide();
			}
			//rel表权限
			if(reltables!=undefined && reltables!=''){
				_self.RelTablePermission=reltables;
				_self.setRelTableFilePermission(reltables);//由rel表权限解析并获取rel表字段的权限并放入relTableFilePermission。
				var tableHtml=_self.getPermission(_self.RelTablePermission,"reltable");
				$("#tableRelPermission").empty();
				$("#tableRelPermission").append(tableHtml);
				for ( var i = 0; i < _self.RelTablePermission.length; i++) {    //按rel表table的ID区域去初始化控件状态
					var tableRelPermission=_self.RelTablePermission[i];
					_self.initStatus("tableId_"+tableRelPermission.tableId);
				}
			}else{
				$("#tableRelPermission").closest( 'table' ).hide();
			}
			
			//意见权限。
			if(opinions!=undefined && opinions!=''){
				_self.Opinion=opinions;
				var opinionHtml=_self.getPermission(_self.Opinion,"opinion");
				$("#opinionPermission").empty();
				$("#opinionPermission").append(opinionHtml);
				_self.initStatus("opinionPermission");
			}else{
				$("#opinionPermission").closest( 'table' ).hide();
			}
			
			//子表是否显示。
			if(tableShows!=undefined && tableShows!=''){
				_self.subTableShows=tableShows;
				_self.initSubTableRadio(_self.subTableShows);
			}
			//rel表是否显示。
			if(tableRelShows!=undefined && tableRelShows!=''){
				_self.relTableShows=tableRelShows;
				_self.initRelTableRadio(_self.relTableShows);
			}
		});
		_self.handChange();//处理下拉框change事件。
		_self.handClick();//处理选择人员，岗位，组织，角色点击事件。
	};
	
	
	/**
	 * 加载完权限后，修改子表是否显示的单项选择。
	 */
	this.initSubTableRadio=function(tableShows){
		for(var i=0;i<tableShows.length;i++){
			var objPermission=tableShows[i];
			var scope = "#tableId_"+objPermission.tableId;
			var objScope=$(scope);
			var radio_name = "radio_"+objPermission.tableId;
			$('input:radio[name="'+radio_name+'"]',objScope).each(function(){      //设置objPermission.showr的值为当前选中项
				var value=$(this).val();
				if(value==objPermission.show){
					$(this).attr("checked","checked");   //勾上
				}else{
					$(this).attr("checked",false);//不打勾
				}
			}); 
		}
	};
	
	/**
	 * 加载完权限后，修改rel表是否显示的单项选择。
	 */
	this.initRelTableRadio=function(relTableShows){
		for(var i=0;i<relTableShows.length;i++){
			var objPermission=relTableShows[i];
			var scope = "#tableId_"+objPermission.tableId;
			var objScope=$(scope);
			var radio_name = "radio_"+objPermission.tableId;
			$('input:radio[name="'+radio_name+'"]',objScope).each(function(){      //设置objPermission.showr的值为当前选中项
				var value=$(this).val();
				if(value==objPermission.show){
					$(this).attr("checked","checked");   //勾上
				}else{
					$(this).attr("checked",false);//不打勾
				}
			}); 
		}
	};
	
	/**
	 * 加载完权限表格后，修改控件的状态。
	 */
	this.initStatus=function(id){
		var _self=this;
		$("tr","#"+id).each(function(){
			var trObj=$(this);
			//取得下拉框
			var selReadObj=$("select.r_select",trObj);
			var selWriteObj=$("select.w_select",trObj);
			var selRequiredObj=$("select.b_select",trObj);
			//值为user,everyone,none,role,orgMgr,pos等。
			//查看下拉框
			var rPermissonType=selReadObj.attr("permissonType");
			var wPermissonType=selWriteObj.attr("permissonType");
			var bPermissonType=selRequiredObj.attr("permissonType");
			
			//初始化下拉框选中。
			selReadObj.val(rPermissonType);
			selWriteObj.val(wPermissonType);
			selRequiredObj.val(bPermissonType);
			
			//是否显示选中的人员或岗位等信息。
			var spanReadObj=$("span[name='r_span']",trObj);
			var spanWriteObj=$("span[name='w_span']",trObj);
			var spanRequiredObj=$("span[name='b_span']",trObj);
			//初始化是否显示选择人员
			_self.showSpan(rPermissonType,spanReadObj);
			_self.showSpan(wPermissonType,spanWriteObj);
			_self.showSpan(bPermissonType,spanRequiredObj);
			
		});
	};
	
	/**
	 * 处理下拉框change事件。
	 */
	this.handChange=function(){
		var _self=this;
		$("#fieldPermission,#filesPermission,#tablePermission,#tableRelPermission,#opinionPermission,#tableGroupPermission").delegate("select.r_select,select.w_select,select.b_select","change",function(){
			var obj=$(this);
			var spanObj=obj.next();
			//当用户权限类型修改时，同时修改span的显示。
			_self.showSpan(obj.val(), spanObj);
			
			var trObj=obj.parents("tr");
			var tbodyObj=obj.parents("tbody");
			//read,write
			//read,write,required
			var mode="read";
			if(obj.attr("class")=="r_select")
				mode ="read";
			else if (obj.attr("class")=="w_select")
				mode = "write";
			else if (obj.attr("class")=="b_select")
				mode = "required";
			//var mode=(obj.attr("class")=="r_select")?"read":"write";
			//获取行在表格中的索引
			var idx=tbodyObj.children().index(trObj);
			//权限类型（field,subtable,opinion)
			var permissionType=trObj.attr("type");
			var selType=obj.val();
			_self.changePermission(permissionType,idx,mode,selType,"","");
			var txtObj=$("input:text",spanObj);
			var idObj=$("input:hidden",spanObj);
			txtObj.val("");
			idObj.val("");
		});
	};
	
	/**
	 * 修改对应行的权限数据。
	 * permissionType:权限类型
	 * field:字段类型
	 * subtable:子表
	 * opinion:意见
	 * 
	 * idx:行数
	 * mode:read,write
	 * type:
	 * everyone,none,user,role,org,orgMgr,pos
	 */
	this.changePermission=function(permissionType,idx,mode,type,ids,names){
		if(idx==-1) return;
		var aryPermission=[];
		switch(permissionType){
			case "field":
				aryPermission=this.FieldsPermission;
				break;
			case "fileAttach":
				aryPermission=this.FilesPermission;
				break;
			case "subtable":
				aryPermission=this.SubTableFilePermission;
				if(idx>0){
					idx=idx-1;   //子表的显示区域中中多了一行表名解析，所以相对的第行对应SubTableFilePermission数组的第一个元素
				}
				break;
			case "reltable":// rel表
				aryPermission=this.RelTableFilePermission;
				if(idx>0){
					idx=idx-1;   //rel表的显示区域中中多了一行表名解析，所以相对的第行对应RelTableFilePermission数组的第一个元素
				}
				break;
			case "opinion":
				aryPermission=this.Opinion;
				break;
		}
		var objPermssion=aryPermission[idx];
		
		
		//alert(permissionType +"," + idx + "," + mode + "," + type +","+ ids);
		
		objPermssion[mode]["type"]=type;
		objPermssion[mode]["id"]=ids;
		objPermssion[mode]["fullname"]=names;
	};
	
	/**
	 * 处理选择人员，岗位，组织，角色点击事件。
	 */
	this.handClick=function(){
		var _self=this;
		$("#fieldPermission,#filesPermission,#tablePermission,#tableRelPermission,#opinionPermission").delegate("a.link-get","click",function(){
			var obj=$(this);
			
			var txtObj=obj.prev();   //select 对象的前一个对象
			var idObj=txtObj.prev();
			var selObj=obj.parent().prev();
			var selType=selObj.val();
			
			var callback = function(ids, names) {
				var trObj=obj.parents("tr");
				var tbodyObj=obj.parents("tbody");
				//read,write
				var mode=obj.attr("mode");
				var idx=tbodyObj.children().index(trObj);
				var permissionType=trObj.attr("type");
				
				_self.changePermission(permissionType,idx,mode,selType,ids,names);
				txtObj.val(names);
				idObj.val(ids);
			};
			
			var ids = idObj.val();
			var names = txtObj.val();
			
			switch(selType){
				case "user":
					UserDialog({
						selectUserIds:ids,
	    	        	selectUserNames:names,
						callback : callback
					});
					break;
				case "role":
					RoleDialog({
						ids:ids,
	    	        	names:names,
						callback : callback
					});
					break;
				case "org":
				case "orgMgr":
					OrgDialog({
						ids:ids,
	    	        	names:names,
						callback : callback
					});
					break;
				case "pos":
					PosDialog({
						ids:ids,
	    	        	names:names,
						callback : callback
					});
					break;
			}
		});
	};
	
	/**
	 * 是否显示选择框
	 */
	this.showSpan=function(permissionType,spanObj){
		switch(permissionType){
			case "user":
			case "role":
			case "org":
			case "orgMgr":
			case "pos":
				spanObj.show();
				break;
			//case "hiddenfield":
			case "everyone":
			case "none":
				spanObj.hide();
				break;
		}
	};
	
	/**
	 * 根据权限集合和权限类型获取权限的html，代码。
	 */
	this.getPermission=function(aryPermission,type){
		var sb=new StringBuffer();
		if(type=='subtable'){
			for(var i=0;i<aryPermission.length;i++){
				var tablePermission=aryPermission[i];	
				var subTableHtml = '<tr><td colspan="5">';
				subTableHtml += '<table id="tableId_'+tablePermission.tableId+'" mainTableId="tableId_'+tablePermission.mainTableId+'" cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;" name="subTablePermission" value="'+tablePermission.title+'">';
				subTableHtml += '<tr><th colspan="5">'+tablePermission.memo+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="w" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'" checked="checked" ><label for="SubtableShow">编辑</label>';
				subTableHtml += '&nbsp;&nbsp;<input type="radio" value="b" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="SubtableShow">必填</label>';
				subTableHtml += '&nbsp;&nbsp;<input type="radio" value="r" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="SubtableShow">只读</label>';
				subTableHtml += '&nbsp;&nbsp;<input type="radio" value="y" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="SubtableShow">隐藏</label></th></tr>';
			//	subTableHtml += '<tr><td colspan="4">'+tablePermission.memo+'</td></tr>';
				var fieldPermission=tablePermission.subField;
				for(var j=0;j<fieldPermission.length;j++){
					var objPermission=fieldPermission[j];
					var str=this.getHtml(objPermission, type);
					subTableHtml += str;
				}
				subTableHtml += '</table></td></tr>';
				sb.append(subTableHtml);
			}
		}else if(type=='reltable'){
			for(var i=0;i<aryPermission.length;i++){
				var tablePermission=aryPermission[i];	
				var relTableHtml = '<tr><td colspan="5">';
				relTableHtml += '<table id="tableId_'+tablePermission.tableId+'" mainTableId="tableId_'+tablePermission.mainTableId+'" cellpadding="1" cellspacing="1" class="table-grid" style="margin-top: 5px;" name="relTablePermission" value="'+tablePermission.title+'">';
				relTableHtml += '<tr><th colspan="5">'+tablePermission.memo+'&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="radio" value="w" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'" checked="checked" ><label for="ReltableShow">编辑</label>';
				relTableHtml += '&nbsp;&nbsp;<input type="radio" value="b" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="ReltableShow">必填</label>';
				relTableHtml += '&nbsp;&nbsp;<input type="radio" value="r" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="ReltableShow">只读</label>';
				relTableHtml += '&nbsp;&nbsp;<input type="radio" value="y" name="radio_'+tablePermission.tableId+'" id="radio_'+tablePermission.tableId+'"><label for="ReltableShow">隐藏</label></th></tr>';
			//	relTableHtml += '<tr><td colspan="4">'+tablePermission.memo+'</td></tr>';
				var fieldPermission=tablePermission.relField;
				for(var j=0;j<fieldPermission.length;j++){
					var objPermission=fieldPermission[j];
					var str=this.getHtml(objPermission, type);
					relTableHtml += str;
				}
				relTableHtml += '</table></td></tr>';
				sb.append(relTableHtml);
			}
		}else if(type=='fileAttach'){
			for(var i=0;i<aryPermission.length;i++){
				var objPermission=aryPermission[i];
				var isTreeButton=objPermission.title.indexOf("file")>-1;
				var isTopButton=objPermission.title.indexOf("attach")>-1;
				var isManageButton=objPermission.title.indexOf("manage")>-1;
				//分类
				if(isTreeButton&&sb.toString().indexOf("--分类树按钮权限--")<=0){
					sb.append('<tr><td colspan="3" style="color:#0a5290;font-size: 14px;font-weight: bold;">--分类树按钮权限--</td></tr>');
				}else if(isTopButton&&sb.toString().indexOf("--顶部按钮权限--")<=0){
					sb.append('<tr><td colspan="3" style="color:#0a5290;font-size: 14px;font-weight: bold;">--顶部按钮权限--</td></tr>');
				}else if(isManageButton&&sb.toString().indexOf("--管理列按钮权限--")<=0){
					sb.append('<tr><td colspan="3" style="color:#0a5290;font-size: 14px;font-weight: bold;">--管理列按钮权限--</td></tr>');
				}
				var str=this.getHtml(objPermission, type);
				sb.append(str);
			}
		}else{
			for(var i=0;i<aryPermission.length;i++){
				var objPermission=aryPermission[i];
				var str=this.getHtml(objPermission, type);
				sb.append(str);
			}
		}
		return sb.toString();
	};
	
	
	/**
	 * 由子表权限解析并获取子表字段的权限并放入subTableFilePermission。
	 */
	this.setSubTableFilePermission=function(tables){
		//将字表的字段权限入到_self.SubTableFilePermission数组
		for(var i=0;i<tables.length;i++){
			var tablePermission=tables[i];
			var fieldPermission=tablePermission.subField;
			for(var j=0;j<fieldPermission.length;j++){
				var objPermission=fieldPermission[j];
				this.SubTableFilePermission.push(objPermission);
			}
		}
	};
	
	/**
	 * 由rel表权限解析并获取rel表字段的权限并放入relTableFilePermission。
	 */
	this.setRelTableFilePermission=function(tables){
		//将rel表的字段权限入到_self.RelTableFilePermission数组
		for(var i=0;i<tables.length;i++){
			var tablePermission=tables[i];
			var fieldPermission=tablePermission.relField;
			for(var j=0;j<fieldPermission.length;j++){
				var objPermission=fieldPermission[j];
				this.RelTableFilePermission.push(objPermission);
			}
		}
	};
	
	/**
	 * 根据权限对象和权限类型（字段，子表，意见）获取一行的显示。
	 */
	this.getHtml=function(permission,type){
		var rpostTd ="";
		if(type=='field'){
			var f =permission.rpost?' checked="checked" ':''; 
			rpostTd =  '<td><input name="rpost" type="checkbox" '+f+'/></td>';	
		}
		if(type=='fileAttach'){
			var aryTr = ['<tr type="#permissionType" tableId="'+permission.tableId+'" mainTableId="'+permission.mainTableId+'" tableName="'+permission.tableName+'" mainTableName="'+permission.mainTableName+'">'
			 			, '<td>#desc</td>'
			 			//只读
			 			//,this.getHtmlTd('r','read',type)
			 			//编辑
			 			,this.getHtmlTd('w','write',type)
			 			, '</tr>'];
		}else if(type=='tablegroup'){
			//
			var ck =permission.hightLight?' checked="checked" ':''; 
			var ckHtml='<td><input name="hightLight" type="checkbox" '+ck+'/></td>';
			var aryTr = ['<tr type="#permissionType" tableId="'+permission.tableId+'" mainTableId="'+permission.mainTableId+'" tableName="'+permission.tableName+'" mainTableName="'+permission.mainTableName+'">'
				 			, '<td>#desc</td>'
				 			//编辑
				 			,this.getHtmlTd('w','write',type)
				 			,ckHtml
				 			, '</tr>'];
		}else{
			var aryTr = ['<tr type="#permissionType" tableId="'+permission.tableId+'" mainTableId="'+permission.mainTableId+'" tableName="'+permission.tableName+'" mainTableName="'+permission.mainTableName+'">'
			 			, '<td>#desc</td>'
			 			//只读
			 			,this.getHtmlTd('r','read',type)
			 			//编辑
			 			,this.getHtmlTd('w','write',type)
			 			//必填
			 			,this.getHtmlTd('b','required',type)
			 			//只读提交
			 			, rpostTd
			 			//控件操作权限
			 			,this.getOperateHtmlTd(permission,type)
			 			, '</tr>'];
		}
		
		permissionTr=aryTr.join("");
		//兼容之前版本
		var required =  permission.required, requiredType = 'none', requiredId='', requiredFullname='';
		if(required!=undefined && required!='')
		{
			requiredType = required.type;
			requiredId = required.id;
			requiredFullname = required.fullname;
		}	
		
		var tmp=permissionTr.replaceAll('#name', permission.title)
		.replaceAll('#desc', permission.memo)
		.replaceAll('#r_type', permission.read.type)
		.replaceAll('#w_type', permission.write.type)
		.replaceAll('#b_type', requiredType)
					
		.replaceAll('#R_ID', permission.read.id)
		.replaceAll('#W_ID', permission.write.id)
		.replaceAll('#B_ID', requiredId)
		
		.replaceAll('#R_FullName', permission.read.fullname)
		.replaceAll('#W_FullName', permission.write.fullname)
		.replaceAll('#B_FullName', requiredFullname)
		
		.replaceAll('#permissionType', type);
		return tmp;
	};
	
	/**
	 *设置每行的显示
	 *@param v 权限简称
	 *@param full 权限全称
	 */
	this.getHtmlTd=function(v,full,type){
		var  uv = v.toUpperCase();
		var tdstr = '';
		if(type=="subtable" || type=="reltable" || type=="fileAttach"||type=="tablegroup"){
			tdstr = ' width="25%" ';
		}
		var aryTd = ['<td'+tdstr+'>'
			, '<select class="',v,'_select plat-select-user"  permissonType="#',v,'_type" name="#name"  >'
			, '<option value="user">用户</option>'
			, '<option value="role">角色</option>'
			, '<option value="org">组织</option>'
			, '<option value="orgMgr">组织负责人</option>'
			, '<option value="pos">岗位</option>'
			//, '<option value="hiddenfield">隐藏</option>'
			, '<option value="everyone">所有人</option>'
			, '<option value="none">无</option>'
			, '</select>'
			, '<span name="',v,'_span">'
			, '<input  type="hidden"  value="#',uv,'_ID"/>'
			, '<input  type="text" class="plat-input" style="margin-left:5px"  readonly value="#',uv,'_FullName"/>'
			, '<a href="####" class="link-get" mode="',full,'" ><span class="link-btn">选择</span></a>'
			, '</span>'
			, '</td>'];
		return aryTd.join('');
	};
	this.getOperateHtmlTd = function(permission,type){
		var tableId = permission.tableId,//表id
			fieldName = permission.title,//字段名称
			clt = 'none',//控件类型
			tdHtml = "";
		var field = TableFactory.getField(tableId,fieldName);
		clt = field.controlType?field.controlType:clt;
		clt = clt+'';
		switch(clt){
		case '9': //文件上传
			var ops =  this.FieldOperate.file;
			tdHtml +='<td clt="'+clt+'" >';
			for(var i=0;i<ops.length;i++){
				var hasNoData = $.isEmpty(permission.operate&&permission.operate.clt);
				var hasCheck = hasNoData?ops[i].checked:permission.operate[ops[i].key];
				if(hasCheck){
					tdHtml +='<input name="'+ops[i].key+'" checked type="checkbox">'+ops[i].value;
				}else{
					tdHtml +='<input name="'+ops[i].key+'"  type="checkbox">'+ops[i].value;
				}
			}
			tdHtml +='</td>';
			break;
		default :
			tdHtml = '<td clt="'+clt+'" ></td>';
			break;
		}
		return tdHtml;
	}
	/**
	 * 获取权限的json字符串。
	 */
	this.getPermissionJson = function() {
		var fieldJson = {
			field : this.FieldsPermission,
			fileAttach : this.FilesPermission,
			subtable : this.SubTableFilePermission,
			reltable : this.RelTableFilePermission,
			opinion : this.Opinion,
			subTableShows : this.subTableShows,
			tableGroup : this.TableGroupPermission,
			relTableShows : this.relTableShows
		};
		var jsonStr = JSON2.stringify(fieldJson);
		return jsonStr;
	};
	
	/**
	 * 添加权限。
	 */
	this.addPermission=function(name,memo,aryPermission){
		var rtn=this.isPermissionExist(name, aryPermission);
		if(!rtn)
		{
			var obj=this.getDefaultPermission(name, memo);
			aryPermission.push(obj);
			return true;
		}
		return false;
	};
	
	/**
	 * 判断权限在集合中已经存在。
	 */
	this.isPermissionExist=function(name,aryPermission){
		for(var i=0;i<aryPermission.length;i++){
			var obj=aryPermission[i];
			var tmp=obj.title.toLocaleLowerCase();
			name=name.toLocaleLowerCase();
			if(tmp==name){
				return true;
			}
		}
		return false;
	};
	
	/**
	 * 根据名称获取权限。
	 */
	this.getPermissionByName=function(name,aryPermission){
		for(var i=0;i<aryPermission.length;i++){
			var obj=aryPermission[i];
			var tmp=obj.title.toLocaleLowerCase();
			name=name.toLocaleLowerCase();
			if(tmp==name){
				return obj;
			}
		}
		return null;
	};
	
	/**
	 * 添加意见权限。
	 */
	this.addOpinion=function(formName,name){
		formName=formName.replace(/opinion:/g,'');
		var rtn=this.addPermission(formName,name, this.Opinion);
		//意见权限。
		var opinionHtml=this.getPermission(this.Opinion,"opinion");
		$("#opinionPermission").empty().append(opinionHtml);
		this.initStatus("opinionPermission");
		return rtn;
	};
	
	/**
	 * 替换意见权限。
	 * title:"",memo:""
	 */
	this.replaceOpinion=function(originName,curName,curMemo){
		var obj=this.getPermissionByName(originName,this.Opinion);
		obj["title"]=curName;
		obj["memo"]=curMemo;
		var opinionHtml=this.getPermission(this.Opinion,"opinion");
		$("#opinionPermission").empty().append(opinionHtml);
		this.initStatus("opinionPermission");
	};
	
	/**
	 * 同步意见列表
	 * arry意见列表
	 */
	this.syncOpinion=function(arry){
		if(arry.length==0){
			this.Opinion=[];
		}
		else{
			for(var i=0;i<this.Opinion.length;i++){
				var tmp=0,title=this.Opinion[i].title.toLocaleLowerCase();	
				for(var j=0;j<arry.length;j++){
					var name=$(arry[j]).attr("name").toLocaleLowerCase().replace(/opinion:/g,'');
					if(title!=name){					
						this.addOpinion(name,$(arry[j]).attr("opinionname"));
					}else{
						tmp=1;
						arry.splice(j, 1);
					}
				}
				if(tmp==0){
					this.Opinion.splice(i,1);
				}
			}
		}
		if(this.Opinion.length!=0){
			var opinionHtml=this.getPermission(this.Opinion,"opinion");
			$("#opinionPermission").empty().append(opinionHtml);
			$("#opinionPermission").closest('table').show();
			this.initStatus("opinionPermission");
		}
	};	
	
	/**
	 * 重新设置权限。
	 */
	this.setAllPermission=function(){
		this.readPermission(this.FieldsPermission,"field");
		this.readPermission(this.FilesPermission,"fileAttach");//by YangBo
		this.readPermission(this.SubTablePermission,"subtable");
		this.readPermission(this.RelTablePermission,"reltable");
		this.readPermission(this.Opinion,"opinion");
		this.readPermission(this.TableGroupPermission,"tablegroup");
	};
	
	//用区域分段读取权限
	this.readPermission=function(aryPermission,type){
		if(type=='tablegroup'){
	    	this.TableGroupPermission = this.readPermissionByObj("#tableGroupPermission",type);
		}else if(type=='field'){
	    	this.FieldsPermission = this.readPermissionByObj("#fieldPermission",type);
		}else if(type=='fileAttach'){
			this.FilesPermission = this.readPermissionByObj("#filesPermission",type); //by YangBo
		}else if(type=='subtable'){
			var tables = aryPermission;        //多个子表
			//每个子表是否显示数组：
			this.subTableShows = this.readTablePermission(tables,type);	
			for(var i=0;i<tables.length;i++){
					var tablePermission=tables[i];				
					//子表每子段的数据内容：
					var id = "#tableId_"+tablePermission.tableId
					var arry = this.readPermissionByObj(id,type);  
					if(i==0){
						this.SubTableFilePermission = arry; //添加第一个子表时，将覆盖原来的数据
					}else{
						for(var j=0;j<arry.length;j++){
							this.SubTableFilePermission.push(arry[j])  //其它的子表的数据则直接在的数组追加 (或者合并)
						}
					}
			}
		}else if(type=='reltable'){
			var tables = aryPermission;        //多个rel表
			//每个rel表是否显示数组：
			this.relTableShows = this.readTablePermission(tables,type);	
			for(var i=0;i<tables.length;i++){
					var tablePermission=tables[i];				
					//rel表每字段的数据内容：
					var id = "#tableId_"+tablePermission.tableId
					var arry = this.readPermissionByObj(id,type);  
					if(i==0){
						this.RelTableFilePermission = arry; //添加第一个rel表时，将覆盖原来的数据
					}else{
						for(var j=0;j<arry.length;j++){
							this.RelTableFilePermission.push(arry[j])  //其它的rel表的数据则直接在的数组追加 (或者合并)
						}
					}
			}
		}else{
			this.Opinion = this.readPermissionByObj("#opinionPermission",type);
		}
		
	};
	
	//读取每一个字段的权限，并放入对应的数组里面  
	this.readPermissionByObj=function(scope,type){
		var objScope=$(scope);
		var aryPermission = []; // 赋值为一个空数组 
		$("tr",objScope).each(function(index){
			if(type=="subtable"&&index==0){   //子表的第一行o是子表的表名（子表的相关信息）
				return true;             // each 方法中 return false时相当于break, 如果return true 就相当于continure。
			}
			if(type=="reltable"&&index==0){   //rel表的第一行是rel表的表名（rel表的相关信息）
				return true;             // each 方法中 return false时相当于break, 如果return true 就相当于continure。
			}
			var trObj=$(this);
			var tableId=trObj.attr("tableId");
			var mainTableId=trObj.attr("mainTableId");
			var tableName=trObj.attr("tableName");
			var mainTableName=trObj.attr("mainTableName");
			
			var memo=trObj.children().first().text();				
			var rSelectObj=$(".r_select",trObj);
			var rSpanObj=$("[name=r_span]",trObj);
			var rId=$("input:hidden",rSpanObj).val();
			var rFullName=$("input:text",rSpanObj).val();
			
			var wSelectObj=$(".w_select",trObj);
			var wSpanObj=$("[name=w_span]",trObj);
			var wId=$("input:hidden",wSpanObj).val();
			var wFullName=$("input:text",wSpanObj).val();
			
			var bSelectObj=$(".b_select",trObj);
			var bSpanObj=$("[name=b_span]",trObj);
			var bId=$("input:hidden",bSpanObj).val();
			var bFullName=$("input:text",bSpanObj).val();
			
			var rpostInput = $("[name=rpost]",trObj);
			var rpost = false;
			if(rpostInput.length>0){
				rpost = rpostInput.is(":checked");	
			}
			var hightLight = false;
			var hightLight = $("[name=hightLight]",trObj);
			if(hightLight.length>0){
				hightLight = hightLight.is(":checked");	
			}
			var fieldName=rSelectObj.attr("name");
			
			var wFieldName=wSelectObj.attr("name");
			//获取特殊控件的操作权限
			var hasOperate = trObj.find('td[clt]').children('input').length>0;
			if(hasOperate){
				var operateTd = trObj.find('td[clt]');
				var operate = {clt:operateTd.attr('clt')};
				$(operateTd).children('input').each(function(){
					 var name = $(this).attr('name');
					 var value = $(this).attr('checked')?true:false
					operate[name] = value;				
				});
			}
			//文件附件条件判断
			if(type=="fileAttach"){
				if(wFieldName==null){
					return;
				}
				var permission={
						"title":wFieldName,"memo":memo,"tableId":tableId,"mainTableId":mainTableId,
						"tableName":tableName,"mainTableName":mainTableName,
						"read": {"type": rSelectObj.val() ,"id":rId, "fullname":rFullName},
						"write":{"type":wSelectObj.val(),"id":wId, "fullname":wFullName}
						};
			}else if(type=="tablegroup"){
				if(wFieldName==null){
					return;
				}
				var permission={
						"title":wFieldName,"memo":memo,"tableId":tableId,"mainTableId":mainTableId,
						"tableName":tableName,"mainTableName":mainTableName,"hightLight":hightLight,
						"read": {"type": rSelectObj.val() ,"id":rId, "fullname":rFullName},
						"write":{"type":wSelectObj.val(),"id":wId, "fullname":wFullName}
						};
			}else{
				var permission={
						"title":fieldName,"memo":memo,"tableId":tableId,"mainTableId":mainTableId,
						"tableName":tableName,"mainTableName":mainTableName,
						"read": {"type": rSelectObj.val() ,"id":rId, "fullname":rFullName},
						"write":{"type":wSelectObj.val(),"id":wId, "fullname":wFullName},
						"required":{"type":bSelectObj.val(),"id":bId, "fullname":bFullName},
						"rpost":rpost,
						"operate":$.isEmpty(operate)?{}:operate
						};
			}
			aryPermission.push(permission);			
		});
		return aryPermission;
	};
	
	//读取每个子表的（radio）是否显示的选择，并放入对应的数组里面；  
	this.readTablePermission=function(tables,type){
		var aryPermission = []; // 赋值为一个空数组 
		for ( var cn = 0; cn < tables.length; cn++) {
        	var table=tables[cn];
        	var scope = "#tableId_"+table.tableId;
    		var objScope=$(scope);
    		var radio_name = "radio_"+table.tableId;
    		var value = $('input:radio[name="'+radio_name+'"]:checked',objScope).val();
    		var permission={
    				"title":table.title,"memo":table.memo,"tableId":table.tableId,"mainTableId":table.mainTableId,
    				"tableName":table.tableName,"mainTableName":table.mainTableName,
    				"show":value
    				};
    		
    		aryPermission.push(permission);
		}		
		return aryPermission;
	}
	
};