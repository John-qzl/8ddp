$(function() {
	CustomForm = {
		subFileJsonMap:null,                                  //子表权限对象
		subFileJsonType:true,                                 //子表权限类型
		relFileJsonMap:null,                                  //rel表权限对象
		relFileJsonType:true,                                 //rel表权限类型
		onEditCallBack:null,
		tables: {},
		init : function() {
			var self=this;
			self.removeBorder('.input-content');			
			self.isMainTableRequest();    //主表的权限验证补充
			if(typeof subFilePermissionMap != 'undefined' && subFilePermissionMap!= null ){
				//获得子表权限
				//self.subFileJsonMap = self.loadSubTablePermission();//Ajax方式获取
				self.subFileJsonMap = subFilePermissionMap;//转换为json对象（通过后台生成HTML时直接获取）
				var subFileJsonList =self.subFileJsonMap["subFileJsonList"];                    //子表新版本字段权限
				var oldSubFileJsonList =self.subFileJsonMap["oldSubFileJsonList"];                   //子表旧版本字段权限
				var subTableShowList =self.subFileJsonMap["subTableShowList"];               //子表显示与否权限（新功能）
				//子表权限标记: subFileJsonType
				if(subFileJsonList!=undefined && subFileJsonList!=null && subFileJsonList.length>0){
					self.subFileJsonType=true;  
					self.newSubTablePermission(self,subFileJsonList,subTableShowList);       //新版本
				}else{
					self.subFileJsonType=false; 
					self.oldSubTablePermission(self,oldSubFileJsonList);      //旧版本
				}	
				
				self.initUI();//初始化表单界面。
				self.initComdify();//处理块模式和表内编译模式下千分位
				self.validate();//获取验证器
			}	
			//关系表
			if(typeof relFilePermissionMap != 'undefined' && relFilePermissionMap!= null ){
				//获得关系表权限
				self.relFileJsonMap = relFilePermissionMap;//转换为json对象（通过后台生成HTML时直接获取）
				var relFileJsonList =self.relFileJsonMap["relFileJsonList"];                    //关系表新版本字段权限
				var oldRelFileJsonList =self.relFileJsonMap["oldRelFileJsonList"];                   //关系表旧版本字段权限
				var relTableShowList =self.relFileJsonMap["relTableShowList"];               //关系表显示与否权限（新功能）
				//关系表权限标记: relFileJsonType
				if(relFileJsonList!=undefined && relFileJsonList!=null && relFileJsonList.length>0){
					self.relFileJsonType=true;  
					self.newRelTablePermission(self,relFileJsonList,relTableShowList);       //新版本
				}else{
					self.relFileJsonType=false; 
					self.oldRelTablePermission(self,oldRelFileJsonList);      //旧版本
				}
				self.initUI();//初始化表单界面。
				self.initComdify();//处理块模式和表内编译模式下千分位
				self.validate();//获取验证器
			}	
			AttachMent.rightEffect("all");//附件字段权限初始化
		},
		//处理边框
		removeBorder:function (ele) {
			$(ele).each(function () {
				var inputC = $(this).find('input[type="text"]').length //判断是否有input输入框
				var selectC = $(this).find('select').length //判断是否有select选择框
				var textArea = $(this).find('textarea').length //判断是否有textarea文本框
				
				if (!inputC && !selectC && !textArea) {
					$(this).css({ "border": "none", "width": "auto", "line-height": $(this).outerHeight() + "px" })
					if ($(this).find('.selectTag').html()) {
						$(this).find('.selectTag').css('display', 'none')
					}
					if ($(this).find('.dialogTag').length) {
						$(this).find('.dialogTag').css('display', 'none')
					}
				}
			})		
		},
		//处理新版本的子表权限
		newSubTablePermission:function(obj,subFileJsonList,subTableShowList){
			//var subFileJsonList =obj.subFileJsonMap["subFileJsonList"];
			$('div[type=subtable]').each(function() {
				//子表
				var table = $(this);
				var tableName = table.attr('tableName');//.toLowerCase()
				table.attr('tableName',tableName);
				//是否 只读  编辑  隐藏   				决定子表的权限
				for ( var cn = 0; cn < subTableShowList.length; cn++) {
					var tableShow=subTableShowList[cn];
					if(tableName==tableShow.tableName){
						if(tableShow.show=="y"){    //子表编辑功能：子表表单不可见（y）
							table.attr('show','false');
							table.attr('right','w');  //子表编辑功能
						}else{
							table.attr('show','true');  //子表编辑功能：子表表单可见（w或r）
							if(tableShow.show=="r"){      
								$("a.add,a.link",table).remove();    //只读时去除子表的添加功能
								table.attr('right','r');  //子表编辑功能
							}else{
								table.attr('right',tableShow.show);  //子表编辑功能或者必填
							}
							
						}
						break;
					}
				}
				var show = table.attr('show');          //兼容旧版本的子表是否显示
				if(typeof show!='undefined'&&show!='true'){
					table.attr('style', 'display:none;');
				}
				var subList =[];
				for ( var i = 0; i < subFileJsonList.length; i++) {
					var objPermission=subFileJsonList[i];
					if(tableName==objPermission.tableName){
					    var right = objPermission.power;
					    var name = "s:"+tableName+":"+objPermission.title;      //字段在HTML的真实NAME
						if(right!="w" && right!="b"){      						  //非写权限(只读)
							//处理超链接
							$("a.link",table).each(function(){      //只读时，历遍超链接
								var link=$(this);
								if(name==link.attr('name')||name==link.attr('field')){
									link.remove();                 //只读时，删除(可能会有多个一样的，要删除所以不能用return false break)
								}                            
							});
							
							//处理自定义按钮对话框的权限问题（这里只是处理字表上的）
							$("a.extend",table).each(function(){      //只读时，历遍超链接中自定义对话框的按钮
								var extend=$(this);
								var jsonStr = extend.attr('dialog');
								var fkName = extend.attr('kfname');//判断是否为外键列.如果为外键列，则继续循环.外键自定义查询按钮的隐藏和显示在FormFKShowUtil.js文件中处理
								if(jsonStr != null && 'undefined' != jsonStr.toLowerCase() && jsonStr.length>2 
										&& fkName == undefined){
									//去掉字符串中的"<![CDATA[]]"     
										jsonStr = jsonStr.stripCData();
									 var jsonObj = eval('(' + jsonStr + ')');
									var fileds = jsonObj.fields;
									for ( var i = 0; i < fileds.length; i++) {
										var filedName = "s:"+tableName+":"+fileds[i].target;
										if(filedName==name){
											extend.remove();                 //只读时，删除(可能会有多个一样的)
										    break;
										}
									}
								}                           
							});
							
							//处理其它
							$("input:visible,textarea:visible,select:visible",table).each(function(){					
								var me=$(this);									
								if(name==me.attr('name')){
									me.attr("right",right);
									var isSelect=me.is("select");
									var isTextarea=me.is("textarea");
									var isCheckboxOrRadio = false;   //单选或者复选
									var type = me.attr("type");  //标签种类
									if(type=="checkbox"||type=="radio"){
										isCheckboxOrRadio = true;
									}
									var clt = me.attr("ctltype");  //控件类型
									var isSelector = false;//是否为选择器
									if(clt=="selector"){
										isSelector = true;
									}
									var val ='';
									var validRule = me.attr("validate");
									if ( validRule != null && validRule != '' && 'undefined' != validRule.toLowerCase() ){
										var json = eval('(' + validRule + ')');		
										if(json.isWebSign){
											var lablename = me.attr("lablename");
											if(isSelect){
												// var str = me.find("option[value='"+me.attr("val")+"']").text();    //和值相对应的显示  select中有一个val保存值的
												val = '<input name="'+name+'"  lablename="'+lablename+'" type="hidden" validate="{\'isWebSign\':true}" value="'+me.attr("val")+'" />';
											}else if(isTextarea){
												val = '<textarea name="'+name+'" lablename="'+lablename+'" class="hidden"  validate="{\'isWebSign\':true}" >'+me.val()+'</textarea>'
											}else{
												val = '<input name="'+name+'"  lablename="'+lablename+'"  type="hidden" validate="{\'isWebSign\':true}" value="'+me.val()+'" />';
											}
										}				
									}	
									if(isSelect){
										val += obj.selectValue(me);
										me.before(val);
										me.remove();
									}else if(isCheckboxOrRadio){
										me.attr("disabled","disabled");
									}else if(isSelector){
										val += me.val();
										var selectorObj = me.parents('div[class=input-content]');
										if(selectorObj.size()>0){
											selectorObj.attr("class","");
										}
										obj.removeTdAndTh(table,me,name);
										selectorObj.html(val);
									}else{
										val += me.val();
										var refid=me.attr("refid");
										if(typeof(refid)!=undefined && refid!=null&&refid!=''){
											var refids=me.prev("input[name='"+refid+"']").val();
											var linkType=me.attr("linktype");
											var tempval="<div>";
											var nametemp = new Array();
											var str = me.val();
											
											if(typeof(str)!=undefined && str!=null&&str!=''){
												nametemp = str.split(",");
											}
											
											if(refids){
												var id=refids.split(",");											
												for(var i=0;i<id.length; i++){
													tempval+="<span class='backgrounddiv'><a refid='"+id[i]+"' href='####' linktype='"+linkType+"' style='text-decoration:none'>"+nametemp[i]+"</a> </span>";
												}
											}else{
												for(var i=0;i<nametemp.length; i++){
													tempval+="<span class='backgrounddiv'>"+nametemp[i]+"</span>";
												}
											}
											tempval+="</div>";
											val = tempval;
										}
										me.parents('.input-content').css({'width':'auto',"border":"none"});
										me.before(val);
										me.remove();
									}															
								} 
							});
							
					    }else{
					        if(right=="b"){
					        	$("input:visible,textarea:visible,select:visible,a:visible",table).each(function(){
					        		var me=$(this);	
					        		me.attr("right",right);
									var isSelect=me.is("a");         //这个一般情况是子表的附件上传功能的 链接必填问题
									var objName = "";
									if(isSelect){
										objName = me.attr('field');
									}else{
										objName = me.attr('name');
									}
									if(name==objName){
										var validRule = me.attr("validate");
										if ( validRule != null && 'undefined' != validRule.toLowerCase() && validRule.length>2 ){ 
											var json = eval('(' + validRule + ')');		
											if(json.required){
												//不做操作
											}else{
												var jsonStr = validRule.substring(0, validRule.lastIndexOf('}'));
												jsonStr +=",\'required\':true}";    //加上必填
												me.attr("validate",jsonStr);    
											}				
										}else{
											me.attr("validate","{\'required\':true}");     //必填
										}
									//	return false;    //找相就的就跳出each 相当于break  可能会有多个相同名称的
									} 
								});
					        }
					    }
					}
				}
							
				//没有form类型的属于表内编辑模式
				var row = table.find('[formType=form]');
				//表单
				var form;
				//窗口编辑模式
				if(row.length > 0) {
					form = table.find('[formType=window]');
				}
				//页内编辑模式
				else {
					row = table.find('[formType=edit]');
				}
				table.data('row', $('<div></div>').append(row.clone()).html());				
				row.css('display', 'none').html('');
				//窗口编辑模式
				if(form) {
					//修复窗口模式附件展示
					$("div[name='div_attachment_container']",form).each(function(){
						var atta  =$("textarea[controltype='attachment']",$(this));
						var attaName =  atta.attr("name");
						if($.isEmpty(attaName)) return true;
						$("td[fieldname='"+attaName+"']",table).each(function() {
								AttachMent.insertHtml($(this),$(this).text());
						});
					});
					
					table.data('form', '<form>' + $('<div></div>').append(form.clone()).html() + '</form>');
					table.data("formProperty",{width:form.width()*0.8 ,height: form.height()*0.8});
					form.remove();
				}
				obj.tables[tableName] = table;
				//权限
				var right = table.attr('right') ;
				
				//子表的权限(可以编辑或必填)
				var canWrite = (right == 'w'|| right=='b' );
				//子表只读
				var canRead = canWrite || (right == 'r');
				$(this).find('[formType="newrow"]').each(function() {
					if(canWrite) {
						obj.addBind($(this), table);
					}
				});
				//子表是否可以编辑
				if(canWrite) {
					var menu = $.ligerMenu({top : 100,left : 100,width : 130,
						items : [{
									text : '添加',
									click : function() {
										//closest（）方法获得匹配选择器的第一个祖先元素，从当前元素开始沿dom树向上。
										obj.add($(menu.target).closest('div[type=subtable]'));
									}
								}]
					});
					table.find('[formType]:first').prev().bind("contextmenu", function(e) {
						menu.target = e.target;
						menu.show({top : e.pageY,left : e.pageX});
						return false;
					});
				}
				//没有读的属性这个表格为隐藏
				if(!canRead) {
					$(this).hide();
				}					
				obj.handButton(table,canWrite);
				
				//要在完全初始化后再做必填的样式处理
				if(right=='b' && table.find('[formtype]:visible').length<1){     //是必填时且子表没有数据行时
					if(!(table.hasClass('validError'))){		//去掉必填样式							
						table.addClass('validError');
					}
				}

			});	
		},
		
		
		//处理旧版本的子表权限
		oldSubTablePermission:function(obj,oldSubFileJsonList){
			//		var oldSubFileJsonList =obj.subFileJsonMap["oldSubFileJsonList"];
			//对子表进行遍历(版本)
			$('div[type=subtable]').each(function() {
				//子表
				var table = $(this);
				//是否显示
				var show = table.attr('show');
				if(typeof show!='undefined'&&show!='true'){
					table.attr('style', 'display:none;');
				}
				//获取表名
				var tableName = table.attr('tableName');
				table.attr('right','w');       //默认为可编辑
				//决定子表的权限
				for ( var i = 0; i < oldSubFileJsonList.length; i++) {
					var objPermission=oldSubFileJsonList[i];
					if(tableName==objPermission.title){
						table.attr('right',objPermission.power);       //权限  w  r  b   只读提交暂时没有（rp）
					    break;
					}
				}				
				//没有form类型的属于表内编辑模式
				var row = table.find('[formType=form]');
				//表单
				var form;
				//窗口编辑模式
				if(row.length > 0) {
					form = table.find('[formType=window]');
				}
				//页内编辑模式
				else {
					row = table.find('[formType=edit]');
				}
				
				table.data('row', $('<div></div>').append(row.clone()).html());
				
				row.css('display', 'none').html('');
				
				//窗口编辑模式
				if(form) {
					//修复窗口模式附件展示
					$("div[name='div_attachment_container']",form).each(function(){
						var atta  =$("textarea[controltype='attachment']",$(this));
						var attaName =  atta.attr("name");
						if($.isEmpty(attaName)) return true;
						$("td[fieldname='"+attaName+"']",table).each(function() {
								AttachMent.insertHtml($(this),$(this).text());
						});
					});
					
					table.data('form', '<form>' + $('<div></div>').append(form.clone()).html() + '</form>');
					table.data("formProperty",{width:form.width()*0.8 ,height: form.height()*0.8});
					form.remove();
				}
				obj.tables[tableName] = table;
				//权限
				var right = table.attr('right') ;
				
				//子表的权限(可以编辑或必填)
				var canWrite = (right == 'w'|| right=='b' );
				//子表只读
				var canRead = canWrite || (right == 'r');
				$(this).find('[formType="newrow"]').each(function() {
					if(canWrite) {
						obj.addBind($(this), table);
					}
				});
				//子表是否可以编辑
				if(canWrite) {
					var menu = $.ligerMenu({top : 100,left : 100,width : 130,
						items : [{
									text : '添加',
									click : function() {
										obj.add($(menu.target).closest('div[type=subtable]'));
									}
								}]
					});
					table.find('[formType]:first').prev().bind("contextmenu", function(e) {
						menu.target = e.target;
						menu.show({top : e.pageY,left : e.pageX});
						return false;
					});
				}
				//没有读的属性这个表格为隐藏
				if(!canRead) {
					$(this).hide();
				}					
				obj.handButton(table,canWrite);				
			});
		},
		
		removeTdAndTh:function(table,me,name){
			var right= me.attr("right");
			if(right=='r'){
				me.attr("readonly","readonly");
				return;
			}
			var thead=$(table).find('tr.headRow').find('th[fname="'+name+'"]');
			me.parents("td[tname='r:"+name+"']").remove();
			$(thead).remove();
		},
		//处理新版本的rel关系表权限
		newRelTablePermission:function(obj,relFileJsonList,relTableShowList){
			var self=this;
			$('div[type=reltable]').each(function() {
				//rel关系表
				var table = $(this);
				var tableName = table.attr('tableName');//.toLowerCase()
				table.attr('tableName',tableName);
				/**
				 * 关系表权限控制
				 * 权限信息： "[{"title":"dc","memo":"订车","tableId":10000001940275,"mainTableId":10000001940007,"tableName":"dc","mainTableName":"cc","show":"w"}]"
				 * show：{y:不可见,r:只读,w:编辑,b:必填}
				 * 控制div上的show、right属性
				 */
				for ( var cn = 0; cn < relTableShowList.length; cn++) {
					var tableShow=relTableShowList[cn];
					if(tableName==tableShow.tableName){
						if(tableShow.show=="y"){    //rel关系表编辑功能：rel关系表表单不可见（y）
							table.attr('show','false');
							table.attr('right','w');  //rel关系表编辑功能
						}else{
							table.attr('show','true');  //rel关系表编辑功能：rel关系表表单可见（w或r）
							if(tableShow.show=="r"){
								$("a.add,a.link",table).remove();    //只读时去除rel关系表的添加功能
								table.attr('right','r');  //rel关系表编辑功能
							}else{
								table.attr('right',tableShow.show);  //rel关系表编辑功能或者必填
							}
							
						}
						break;
					}
				}
				var show = table.attr('show');          //兼容旧版本的rel关系表是否显示
				if(typeof show!='undefined'&&show!='true'){
					table.attr('style', 'display:none;');
				}
				/**
				 * 字段权限控制
				 * 单个字段的权限信息："{"title":"sqr","memo":"申请人","tableId":"10000001940275","mainTableId":"10000001940007","tableName":"dc","mainTableName":"cc",
				 * 					"read":{"type":"everyone","id":"","fullname":""},
				 * 					"write":{"type":"everyone","id":"","fullname":""},
				 * 					"required":{"type":"none","id":"","fullname":""},
				 * 					"rpost":false,"operate":{},"power":"w"}"
				 * 只读权限时、隐藏时：超链接字段、关联关系列、其他文本，通过removeTdAndTh方法区分隐藏和只读
				 */
				var relList =[];
				for ( var i = 0; i < relFileJsonList.length; i++) {
					var objPermission=relFileJsonList[i];
					if(tableName==objPermission.tableName){
					    var right = objPermission.power;
					    var name = "r:"+tableName+":"+objPermission.title; //字段在HTML的真实NAME eg: r:dc:sqr
						if(right!="w" && right!="b"){      					//非写权限(只读)
							//处理超链接
							$("a.link",table).each(function(){      //只读时，历遍超链接
								var link=$(this);
								if(name==link.attr('name')||name==link.attr('field')){
									link.attr("right",right);
									self.removeTdAndTh(table,link,name);
									link.remove();//只读时，删除(可能会有多个一样的，要删除所以不能用return false break)
								}                            
							});
							
							$("a[dialog]",table).each(function(){      //只读时，处理关联关系列
								var d=$(this);
								if(name==d.attr('kfname')){
									var input_=$("input[name='"+name+"']")
									input_.attr("right",right);
									self.removeTdAndTh(table,input_,name);
								}                            
							});
							
							//处理自定义按钮对话框的权限问题（这里只是处理字表上的）
							$("a.extend",table).each(function(){      //只读时，历遍超链接中自定义对话框的按钮
								var extend=$(this);
								var jsonStr = extend.attr('dialog');	
								var fkName = extend.attr('kfname');//判断是否为外键列.如果为外键列，则继续循环.外键自定义查询按钮的隐藏和显示在FormFKShowUtil.js文件中处理
								if(jsonStr != null && 'undefined' != jsonStr.toLowerCase() && jsonStr.length>2 
										&& fkName == undefined){
									//去掉字符串中的"<![CDATA[]]"    
									jsonStr = jsonStr.stripCData();
									var jsonObj = eval('(' + jsonStr + ')');
									var fileds = jsonObj.fields;
									for ( var i = 0; i < fileds.length; i++) {
										var filedName = "r:"+tableName+":"+fileds[i].target;
										if(filedName==name){
											self.removeTdAndTh(table,extend,name);
											extend.remove();                 //只读时，删除(可能会有多个一样的)
										    break;
										}
									}
								}                           
							});
							//处理其它
							$("input:visible,textarea:visible,select:visible,a[dialog]",table).each(function(){
								var me=$(this);									
								if(name==me.attr('name')){
									me.attr("right",right);
									var isSelect=me.is("select");
									var isTextarea=me.is("textarea");
									var isCheckboxOrRadio = false;//单选或者复选
									var isSelector = false;//是否为选择器
									var type = me.attr("type");  //标签种类
									var clt = me.attr("ctltype");  //控件类型
									if(clt=="selector"){
										isSelector = true;
									}
									if(type=="checkbox"||type=="radio"){
										isCheckboxOrRadio = true;
									}
									var val ='';
									var validRule = me.attr("validate");
									if ( validRule != null && validRule != '' && 'undefined' != validRule.toLowerCase() ){
										var json = eval('(' + validRule + ')');		
										if(json.isWebSign){
											var lablename = me.attr("lablename");
											if(isSelect){
												// var str = me.find("option[value='"+me.attr("val")+"']").text();    //和值相对应的显示  select中有一个val保存值的
												val = '<input name="'+name+'"  lablename="'+lablename+'" type="hidden" validate="{\'isWebSign\':true}" value="'+me.attr("val")+'" />';
											}else if(isTextarea){
												val = '<textarea name="'+name+'" lablename="'+lablename+'" class="hidden"  validate="{\'isWebSign\':true}" >'+me.val()+'</textarea>'
											}else{
												val = '<input name="'+name+'"  lablename="'+lablename+'"  type="hidden" validate="{\'isWebSign\':true}" value="'+me.val()+'" />';
											}
										}				
									}	
									if(isSelect){
										//需要将表头对应的th也remove
										self.removeTdAndTh(table,me,name);
										val += obj.selectValue(me);
										me.parents('.input-content').css({'width':'auto',"border":"none"});
										me.before(val);
										me.remove();
									}else if(isCheckboxOrRadio){
										me.attr("disabled","disabled");
									}else if(isSelector){
										val += me.val();
										var selectorObj = me.parents('div[class=input-content]');
										if(selectorObj.size()>0){
											selectorObj.attr("class","");
										}
										self.removeTdAndTh(table,me,name);
										selectorObj.html(val);
									}else{
										val += me.val();
										var refid=me.attr("refid");
										if(typeof(refid)!=undefined && refid!=null&&refid!=''){
											var refids=me.prev("input[name='"+refid+"']").val();
											var linkType=me.attr("linktype");
											var tempval="<div>";
											var nametemp = new Array();
											var str = me.val();
											
											if(typeof(str)!=undefined && str!=null&&str!=''){
												nametemp = str.split(",");
											}
											
											if(refids){
												var id=refids.split(",");											
												for(var i=0;i<id.length; i++){
													tempval+="<span class='backgrounddiv'><a refid='"+id[i]+"' href='####' linktype='"+linkType+"' style='text-decoration:none'>"+nametemp[i]+"</a> </span>";
												}
											}else{
												for(var i=0;i<nametemp.length; i++){
													tempval+="<span class='backgrounddiv'>"+nametemp[i]+"</span>";
												}
											}
											tempval+="</div>";
											val = tempval;
										}
										//需要将表头对应的th也remove
										self.removeTdAndTh(table,me,name);
										//me.parent(".input-content").html(val);
										me.parents('.input-content').css({'width':'auto',"border":"none"});
										me.before(val);
										me.remove();
									}															
								} 
							});
							
					    }else{
					    	
				        	$("input:visible,textarea:visible,select:visible,a:visible",table).each(function(){
				        		var me=$(this);	
				        		me.attr("right",right);
								var isSelect=me.is("a");//这个一般情况是rel关系表的附件上传功能的 链接必填问题
								var objName = "";
								if(isSelect){
									objName = me.attr('field');
								}else{
									objName = me.attr('name');
									//如果是可编辑则需要加上高亮显示
									me.addClass("tableHighLight");
								}
								
								if(name==objName){
									var validRule = me.attr("validate");
									if ( validRule != null && 'undefined' != validRule.toLowerCase() && validRule.length>2 ){ 
										var json = eval('(' + validRule + ')');		
										if(right=="b"&&!json.required){
											var jsonStr = validRule.substring(0, validRule.lastIndexOf('}'));
											jsonStr +=",\'required\':true}";    //加上必填
											me.attr("validate",jsonStr);    
										}				
									}else{
										if(right=="b"){
											me.attr("validate","{\'required\':true}");     //必填
										}
									}
								} 
							});
					    }
					}
				}
							
				//没有form类型的属于表内编辑模式
				var row = table.find('[formType=form]');
				//表单
				var form;
				//窗口编辑模式
				if(row.length > 0) {
					form = table.find('[formType=window]');
				}
				//页内编辑模式
				else {
					row = table.find('[formType=edit]');
				}
				table.data('row', $('<div></div>').append(row.clone()).html());				
				row.css('display', 'none').html('');
				//窗口编辑模式
				if(form) {
					//修复窗口模式附件展示
					$("div[name='div_attachment_container']",form).each(function(){
						var atta  =$("textarea[controltype='attachment']",$(this));
						var attaName =  atta.attr("name");
						if($.isEmpty(attaName)) return true;
						$("td[fieldname='"+attaName+"']",table).each(function() {
								AttachMent.insertHtml($(this),$(this).text());
						});
					});
					
					table.data('form', '<form>' + $('<div></div>').append(form.clone()).html() + '</form>');
					table.data("formProperty",{width:form.width()*0.8 ,height: form.height()*0.8});
					form.remove();
				}
				obj.tables[tableName] = table;
				//权限
				var right = table.attr('right') ;
				
				//rel关系表的权限(可以编辑或必填)
				var canWrite = (right == 'w'|| right=='b' );
				//rel关系表只读
				var canRead = canWrite || (right == 'r');
				$(this).find('[formType="newrow"]').each(function() {
					if(canWrite) {
						obj.addRelBind($(this), table);
					}
				});
				//rel关系表是否可以编辑
				if(canWrite) {
					var menu = $.ligerMenu({top : 100,left : 100,width : 130,
						items : [{
									text : '添加',
									click : function() {
										obj.add($(menu.target).closest('div[type=reltable]'));
									}
								}]
					});
					table.find('[formType]:first').prev().bind("contextmenu", function(e) {
						menu.target = e.target;
						menu.show({top : e.pageY,left : e.pageX});
						return false;
					});
				}
				//没有读的属性这个表格为隐藏
				if(!canRead) {
					$(this).hide();
				}	
				obj.handButton(table,canWrite);
				
				//要在完全初始化后再做必填的样式处理
				if(right=='b' && table.find('[formtype]:visible').length<1){     //是必填时且rel关系表没有数据行时
					if(!(table.hasClass('validError'))){		//去掉必填样式							
						table.addClass('validError');
					}
				}

			});	
		},
		
		
		//处理旧版本的rel关系表权限
		oldRelTablePermission:function(obj,oldRelFileJsonList){
			//对rel关系表进行遍历(版本)
			$('div[type=reltable]').each(function() {
				//rel关系表
				var table = $(this);
				//是否显示
				var show = table.attr('show');
				if(typeof show!='undefined'&&show!='true'){
					table.attr('style', 'display:none;');
				}
				//获取表名
				var tableName = table.attr('tableName');
				table.attr('right','w');       //默认为可编辑
				//决定rel关系表的权限
				for ( var i = 0; i < oldRelFileJsonList.length; i++) {
					var objPermission=oldRelFileJsonList[i];
					if(tableName==objPermission.title){
						table.attr('right',objPermission.power);       //权限  w  r  b   只读提交暂时没有（rp）
					    break;
					}
				}				
				//没有form类型的属于表内编辑模式
				var row = table.find('[formType=form]');
				//表单
				var form;
				//窗口编辑模式
				if(row.length > 0) {
					form = table.find('[formType=window]');
				}
				//页内编辑模式
				else {
					row = table.find('[formType=edit]');
				}
				
				table.data('row', $('<div></div>').append(row.clone()).html());
				
				row.css('display', 'none').html('');
				
				//窗口编辑模式
				if(form) {
					//修复窗口模式附件展示
					$("div[name='div_attachment_container']",form).each(function(){
						var atta  =$("textarea[controltype='attachment']",$(this));
						var attaName =  atta.attr("name");
						if($.isEmpty(attaName)) return true;
						$("td[fieldname='"+attaName+"']",table).each(function() {
								AttachMent.insertHtml($(this),$(this).text());
						});
					});
					
					table.data('form', '<form>' + $('<div></div>').append(form.clone()).html() + '</form>');
					table.data("formProperty",{width:form.width()*0.8 ,height: form.height()*0.8});
					form.remove();
				}
				
				
				obj.tables[tableName] = table;
				//权限
				var right = table.attr('right') ;
				
				//rel关系表的权限(可以编辑或必填)
				var canWrite = (right == 'w'|| right=='b' );
				//rel关系表只读
				var canRead = canWrite || (right == 'r');
				$(this).find('[formType="newrow"]').each(function() {
					if(canWrite) {
						obj.addRelBind($(this), table);
					}
				});
				//rel关系表是否可以编辑
				if(canWrite) {
					var menu = $.ligerMenu({top : 100,left : 100,width : 130,
						items : [{
									text : '添加',
									click : function() {
										obj.add($(menu.target).closest('div[type=reltable]'));
									}
								}]
					});
					table.find('[formType]:first').prev().bind("contextmenu", function(e) {
						menu.target = e.target;
						menu.show({top : e.pageY,left : e.pageX});
						return false;
					});
				}
				//没有读的属性这个表格为隐藏
				if(!canRead) {
					$(this).hide();
				}					
				obj.handButton(table,canWrite);				
			});
		},
		


		//表单设计页面，处理添加和删除按钮。//表单设计上的“添加”按钮 事件
		handButton:function(table,canWrite){
			var self=this;
			if(!canWrite) {
				$(".add",table).addClass("disabled");
				$(".del",table).addClass("disabled");
				$(".selectTemplate",table).addClass("disabled");
				//移除管理列
				$('.headRow th',table).each(function(i,e){
					var thObj = $(e);
					if(!$.isEmpty(thObj.text())&&thObj.text().trim()=="管理"){
						thObj.remove();
						return false;
					}
				});
				$('.listRow .trManage',table).remove();			
				return;
			};
			
			//为每一行添加上移，下移，删除 按钮
			var trManage = $("td.trManage",table);
			if(trManage.length>0){
				this.addTrManageButton(trManage,table);
			}
			$(".add",table).click(function(){
				
				//表单设计上的“添加”按钮 事件
				self.add(table);
				var tableType =  table.attr('type') ;// (子表，关系表)
				//alert(tableType);
			 	//初始化子表级联
				self.initSubQuery(tableType);
				//初始化子表选择器
				self.subSelectorInit();
				//初始化子表自定义对话框的下拉情况
				self.subDialogShowInit();
				
			});
			//定制
			$(".selectTemplate",table).click(function(){
				selectTemplate(table,self);
			})
			table.delegate(".del", "click", function(){
				var t = $(this).closest('[formType]'),
					brother = t.next('[formType]').length?t.next('[formType]'):t.prev('[formType]');
				t.remove();
				if(brother)
					FormUtil.InitMathfunction(brother);
			});
		},
		
		
		//处理块模式和表内编译模式下千分位
		initComdify:function(){
			$("div[type='custform']").delegate('[showtype]', 'blur',function (){
					CustomForm.delaComdify($(this));
				});
		},
		
		//处理弹出框模式下千分位
		initComdifyForWindow:function(){
			$("[formtype='window']").delegate('[showtype]', 'blur',function(){
					CustomForm.delaComdify($(this));
				});
		},
		
		//处理千分位
		delaComdify:function(varObj){
			var me=$(varObj);
			var value = me.val();
			var json=null;
			try{
				var jsonStr=me.attr("showtype");
				json=eval('('+jsonStr+')');
				//json=jQuery.parseJSON();
			}
			catch(err){}
			if(json!=null){
				var coinvalue=json.coinValue;
				var iscomdify=json.isShowComdify;
				var decimallen=json.decimalValue;
				//去除货币标签
				if (coinvalue!=null&&coinvalue!='') {
					if (value.split(coinvalue) != -1) {
						var ary = value.split(coinvalue);
						value = ary.join("");
					}
				}
				
				if (value.indexOf(',') != -1) {
					value = $.toNumber(value);
				}
				if(iscomdify){
					var value = $.comdify(value);
				}
				
					// 小数处理
				if (decimallen > 0 && value != '') {
						if (value.indexOf('.') != -1) {
							var ary = value.split('.');
							var temp = ary[ary.length - 1];
							if (temp.length > 0 && temp.length < decimallen) {
								var zeroLen = '';
								for (var i = 0; i < decimallen- temp.length; i++) {
									zeroLen = zeroLen + '0';
								}
								value = value + zeroLen;
							}
						} else {
							var zeroLen = '';
							for (var i = 0; i < decimallen; i++) {
								zeroLen = zeroLen + '0';
							}
							value = value + '.' + zeroLen;
						}
				}
				//添加货币标签
				if (coinvalue!=null && coinvalue!='' && value != '') {
					value = coinvalue + value;
				}
			}
			me.val(value);
		},
		
		
		/**
		 * 初始化表单界面。
		 * 
		 * @param parent
		 */
		initUI : function(parent) {
			//初始化复选框，让其选中
			$('input[type="checkbox"]').each(function() {
				var value=$(this).val();
				var data=$(this).attr('data');
				if(!data||data==''||data=='null')return;
				var ary=data.split(",");
				for(var i=0;i<ary.length;i++){
					if(value==ary[i]){
						$(this).attr("checked","checked");
					}
				}
			});
			
			
			//给参数parent赋值
			if (parent==undefined) {
				parent = $('body div[type=custform]');
				if(!parent||parent.length==0)parent = $("body");
			}else{
				//根据权限信息，控制附件展示形式
				//AttachMent.init('w', $("div[name='div_attachment_container']",parent));
			}
			//初始化下拉框默认选中,在下拉框定义一个val属性,使用脚本选中。
			$("select[name][val]",parent).each(function(){
				var obj=$(this),val= obj.attr("val");
				if($.isEmpty(val))
					val= obj.val();		
				obj.val(val);
			});
			
			//$.metadata.setType("attr", "validate");
			var filter='input,textarea,.dicComboTree,.dicComboBox,.dicCombo';
			parent.find(filter).each(function() {
				if ($(this).is('.dicComboTree,.dicComboBox,.dicCombo')) {
					$(this).dicCombo();//初始化数据字典分类下拉框，方法在dicCombo.js中.
				}
				//处理默认日期
				if ($(this).is('.Wdate[displayDate=1]')){
					var me = $(this);
					if($.isEmpty(me.val())){			
						var datefmt = me.attr("datefmt");
						var nowDate=new Date().Format(datefmt);
						me.val(nowDate);
					}
				}	
			});
			//对form获取校验规则
			if (parent.is('form')) {
			
				parent.data('validate', this.getValidate(parent));
			} else {
				var v = parent.closest('form');
				this.valid = this.getValidate(v);
			}
			//处理sub表的行。可在表单设计页面,重写handRowEvent方法实现定制,默认实现是赋行号.
			$('div[type=subtable]').each(function() {
				var subTable=$(this);
				CustomForm.handRow('edit',subTable);
			});
			//处理关系表的行。可在表单设计页面,重写handRowEvent方法实现定制,默认实现是赋行号.
			$('div[type=reltable]').each(function() {
				var relTable=$(this);
				CustomForm.handRow('edit',relTable);
			});
		},
		//添加一行数据
		//参数，子表区域，在那个地方插入。
		add : function(table, beforeElement) {
			var self=this;
			var right=table.attr('right');
			if(right!="w" && right!="b"){
				return;
			}
			//判断是否是使用窗口方式编辑数据。
			var frm=table.data('form');
			if (frm) {
				self.openWin('添加', $(frm), table, beforeElement, function(form, table, beforeElement) {
					//校验数据
					var v = form.data('validate');
					if (!v.valid()) {
						return false;
					} else {
						var row = $(table.data('row'));
						//添加行数据
						self.addRow(row, form);
					
						self._add(table, row, beforeElement);
						self.handRow('add',table);
						return true;
					}
				});
				//弹窗模式下初始化千分位货币处理函数
				self.initComdifyForWindow();
			} 
			//使用的行内编辑。
			else {
				var row = $(table.data('row'));
				
				this._add(table, row, beforeElement);
			}
			self.handRow('add',table);
		},
		//
		handRow:function(type,table){
			if(typeof(handRowEvent)=="function"){
				/*function handRowEvent(ev,table) 具体实现在每个自定义表单的顶部,默认实现是赋行号.
				function handRowEvent(ev,table){
					$("td.tdNo",table).each(function(i){
					$(this).text(i+1);});}*/
				handRowEvent(type,table);
			}
		},
		//添加一行数据往行数据中添加隐藏域，同时设置显示的数据。
		addRow:function(row,form){
			form.find('input:text[name],input:hidden[name], textarea[name],select[name]').each(function() {
				var name=$(this).attr('name');
				var val=$(this).val();
				//添加隐藏表单。
				row.append($('<input type="hidden"/>').attr('name', name).val(val));
				//修改表格的文本显示。
				var filter="[fieldName='"+name+"']";
				var objTd=$(filter,row);
				if(objTd.length>0){
					var controltype = $(this).attr('controltype');
					if(!$.isEmpty(controltype) && controltype=='attachment'){
						AttachMent.insertHtml(objTd,val);
					}else if(!$.isEmpty(controltype) && controltype=='select'){
						var text = $(this).find("option:selected").text();
						objTd.text(text);
					}else{
						objTd.text(val);
					}	
				}
				$(this).val('');
			});
			
			
			//回填checkbox的值。
			form.find('input:checkbox,input:radio').each(function() {
				var name=$(this).attr('name');
				var value=$(this).val();
				
				var filterHidden="input[name='"+ name +"']";
				var isChecked=($(this).attr("checked")!=undefined);
				var obj=$(filterHidden,row);
				
				var filter="[fieldName='"+name+"']";
				var objTd=$(filter,row);
				
			
				
				var val=(isChecked)?value:"";
				if(obj.length==0){
					row.append($('<input type="hidden"/>').attr('name', name).val(val));
					if(objTd.length>0){
						objTd.text(val);
					}
				}
				else{
					var existVal=obj.val();
					if(existVal==""){
						if(val!=""){
							existVal=val;
						}
					}
					else{
						if(val!=""){
							existVal+="," +val;
						}
					}
					obj.val(existVal);
					if(objTd.length>0){
						objTd.text(existVal);
					}
				}
			});
		},
		//表单设计上的“添加”按钮 事件
		_add : function(table, newRow, beforeElement) {
			//处理newRow的字段的必填
			$("input,textarea,select",newRow).each(function(){
        		var me=$(this);	
        		//处理默认日期
				if (me.is('.Wdate[displayDate=1]')){
					if($.isEmpty(me.val())){			
						var datefmt = me.attr("datefmt");
						var nowDate=new Date().Format(datefmt);
						me.val(nowDate);
					}
				}
        		var validRule = me.attr("validate");
				if ( validRule != null && 'undefined' != validRule.toLowerCase() && validRule.length>2 ){ 
					var json = eval('(' + validRule + ')');		
					if(json.required){
						me.addClass("validError");
					}				
				}
			});
			//为每一行添加上移，下移，删除 按钮
			var trManage = $("td.trManage",newRow);
			if(trManage.length>0){
				this.addTrManageButton(trManage,table);
			}
			if (beforeElement) {
				$(beforeElement).before(newRow);
			}
			//在最后面加入
			else {
				table.find('[formType]:last').after(newRow);
			}
			//初始化界面
			this.initUI(newRow);
			//添加右键绑定
			this.addBind(newRow, table);
			FormUtil.triggerChoice(newRow);
			//删除必填样式
			if(table.hasClass('validError')){
				table.removeClass('validError');
			}
			
		},
		addTrManageButton : function(tr,table){
			var needEdit = (this.tables[table.attr('tableName')].data('form') != null);
			var tableType =  table.attr('type') ;
			var buttonTd = '';
			buttonTd+='<a class="link moveup"   onclick="CustomForm.moveTr(this,true,\''+tableType+'\')"  href="####">上移</a>&nbsp;';
			buttonTd+='<a class="link movedown" onclick="CustomForm.moveTr(this,false,\''+tableType+'\')" href="####">下移</a>&nbsp;';
			if(needEdit){
				buttonTd+='<a class="link edit" onclick="CustomForm.editTr(this,\''+tableType+'\')" href="####"></a>&nbsp;';
			}
			buttonTd+='<a class="link del"  href="####">删除</a>&nbsp;';
			tr.prepend(buttonTd);
		},
		editTr : function(obj,tableType){
			var row = $(obj).closest('[formType]');
			var table = row.closest('div[type='+tableType+']');
			this.edit(table, row);
		},
		/**
		 * 上下移动
		 * @param {}
		 *            obj
		 * @param {}
		 *            isUp 是否上移
		 */
		moveTr : function(obj, isUp,tableType) {
			$(obj).parents('tbody').find('tr').css('background','white');
			var t = $(obj).parents('tr');
			if(isUp){
				var prev = t.prev('[formType]:visible');
				if (prev.length > 0) {
					prev.before(t);
					$('table.listTable tr.listRow').hover(function () {
						$('table.listTable tr').css('background','white');
						t.css('background','#EEEBC2');
						$(this).css('background','#E6E6E6');
					})
				}
			}else{
				var next = t.next('[formType]:visible');
				if (next.length > 0) {
					next.after(t);
					$('table.listTable tr.listRow').hover(function () {
						$('table.listTable tr').css('background','white');
						t.css('background','#EEEBC2');
						$(this).css('background','#E6E6E6');
					})
				}
			}
			/*$('table.listTable tr.listRow').hover(function () {
				$('table.listTable tr').css('background','white');
				$(this).css('background','#E6E6E6');
			})*/
			var table = t.closest('div[type='+tableType+']');
			this.handRow('add',table);
		},
		/**
		 * 添加sub表 行数据的右键事件绑定。
		 * @param row
		 * @param table
		 */
		addBind: function(row, table) {
			//是否需要编辑菜单项目
			//如果该表弹出窗口定义，那么就不需要编辑菜单项目。
			var needEdit = (this.tables[table.attr('tableName')].data('form') != null);
			var tableType =  table.attr('type') ;
			//alert(tableType);
			//2017年8月19日 右键菜单去掉
			/*var menu = this.getMenu(needEdit,table);
			row.bind("contextmenu", function(e) {
				menu.target = e.target;
				menu.show({top : e.pageY,left : e.pageX});
				return false;
			});*/
		},
		/**
		 * 添加rel表行数据的右键事件绑定//数据行按钮
		 * @param row
		 * @param table
		 */
		addRelBind: function(row, table) {
			//是否需要编辑菜单项目
			//如果该表弹出窗口定义，那么就不需要编辑菜单项目。
			var needEdit = (this.tables[table.attr('tableName')].data('form') != null);
			var tableType =  table.attr('type') ;
		
			//2017年8月19日 右键菜单去掉
			/*var menu = this.getMenu(needEdit,table);
			row.bind("contextmenu", function(e) {
				menu.target = e.target;
				menu.show({top : e.pageY,left : e.pageX});
				return false;
			});*/
		},
		/**
		 * 编辑行数据。
		 * @param table
		 * @param row
		 */
		edit : function(table, row) {
			var self=this;
			var tableName = table.attr('tableName');
			//获取表单
			var form = $(this.tables[tableName].data('form'));
			
			//对表单数据进行初始化
			self.initFormData(form,row);
			
			form.data('row', row);
			this.openWin('编辑', form, table, null, function(form, table) {
				var v = form.data('validate');
				var rtn=v.valid();
				if(!rtn) return false;
				
				var row = form.data('row');
				//对表单进行遍历
				self.setRowData(form,row);
				return true;
				
			});
		},
		initFormData:function(form,row){
			form.find('input:text,textarea,select,input[type="hidden"]').each(function() {
				var name= $(this).attr('name');
				var value = row.find('[name="' + name+ '"]').val();
				$(this).val(value);
			});
			form.find('input:checkbox,input:radio').each(function(){
				var name=$(this).attr("name");
				var chkValue=$(this).val();
				var value=row.find('[name="' + name+ '"]').val();
				
				if(value.indexOf(chkValue)!=-1){
					$(this).attr("checked","checked");
				}
			});
		},
		setRowData:function(form,row){
			var self=this;
			//对表单进行遍历
			form.find('input:text, textarea,input[type="hidden"],select').each(function() {
				var name=$(this).attr('name');
				var val=$(this).val();
				//修改隐藏域的数据值
				var objHidden=$("input[name='"+name+"']",row);
				objHidden.val(val);
				//修改表格的文本显示。
				var filter="[fieldName='"+name+"']";
				var objTd=$(filter,row);
				if(objTd.length>0){
					//处理附件
					var controltype = $(this).attr('controltype');
					if(!$.isEmpty(controltype) && controltype=='attachment'){
						AttachMent.insertHtml(objTd,val);
					}else{
						objTd.text(val);
					}	
				}
			});
			
			form.find('input:checkbox,input:radio').each(function(){
				var name=$(this).attr('name');
				var objHidden=$("input[name='"+name+"']",row);
				objHidden.val("");
				//修改表格的文本显示。
				var filter="[fieldName='"+name+"']";
				var objTd=$(filter,row);
				if(objTd.length>0){
					objTd.text("");
				}
			});
			
			form.find('input:checkbox:checked,input:radio:checked').each(function(){
				var name=$(this).attr('name');
				var value=$(this).val();
				var objHidden=$("input[name='"+name+"']",row);
				var filter="[fieldName='"+name+"']";
				var objTd=$(filter,row);
				
				var hidValue=objHidden.val();
				if(self.isNull(hidValue)){
					objHidden.val(value);
					objTd.text(value);
				}
				else{
					objHidden.val(hidValue +"," +value);
					objTd.text(hidValue +"," +value);
				}
				
			});
			
		},
		isNull:function(obj){
			if(obj==undefined || obj==null || obj==""){
				return true;
			}
			return false;
		},
		
		/**
		 * 获取验证器。
		 * @param target 为一个表单。
		 * @returns
		 */
		getValidate : function(target) {
			return target.form({
				/**
				 * 错误消息处理
				 */
				errorPlacement : function(el, msg) {
					var element=$(el),corners =['right center','left center'],flipIt= element.parents('span.right').length > 0;
					element.addClass('validError');
					//付勇
					element.removeClass('tableHighLight');
					//添加必填样式
					var parentTd = element.closest("td");
					if(parentTd){
						var formTitle = parentTd.prev("td.formTitle");
						if(formTitle){
							var span = $("span.red", formTitle);
							if(!span || span.length==0){
								formTitle.prepend($("<span class='red'>*</span>"));
							}
						}
					}
					
					if(element.hasClass("ckeditor")){
						setTimeout(function(){
							element = element.next();
							element.css("border","1px solid red");
						},1000);
					}else if(element.hasClass("Wdate")||element.is('textarea')){
						//element.css("border","1px solid red");
						element.css("border","1px solid #ccc");
					}else if(element.is("select")||element.attr('type')&&(element.attr('type')=='checkbox'||element.attr('type')=='radio')){
						var name = element.attr('name');
						if(!name)return;
						var priElement = $("*[name='"+name+"']",$("span.select_contain_span"));
						if(priElement.length>0)return;
						element.removeClass('validError');
						var errorSpan = $('<span></span>').css({"border":"1px solid red","padding":"1px"}).addClass("select_contain_span");
						element.before(errorSpan);
						errorSpan.append(element);
					}
					
					if(!$(msg).is(':empty')){
						element.qtip({
							overwrite:false,
							content : msg,
							position:{
								my:corners[flipIt?0:1],
								at:corners[flipIt?1:0],
								viewport:$(window)
							},
					        show:{
			   			     	effect: function(offset) {
			   						$(this).slideDown(100);
			   					}
		   			        },
		   			        hide:{
		   			        	event:'click mouseleave',
		   			        	leave: false,
		   			        	fixed:true,
		   			        	delay:200
		   			        },
		   			        style:{
								classes:'ui-tooltip-red'
							}
						});
					}else{
						element.qtip("destroy");
					}
				},
				/**
				 * 成错误消息
				 */
				success : function(el) {
					var element=$(el);
					if(element.hasClass("ckeditor")){
						element = element.next();
						element.css("border","");
					}else if(element.hasClass("Wdate")||element.is('textarea')){
						//取消 border -zxg 20170726
						//element.css("border","1px solid #999");
					}else if(element.is("select")||element.attr('type')&&(element.attr('type')=='checkbox'||element.attr('type')=='radio')){
						var selectSpan = element.parents("span.select_contain_span");
						if(!selectSpan||selectSpan.length==0){
							var name = element.attr('name');
							if(!name)return;
							var priElement = $("*[name='"+name+"']",$("span.select_contain_span"));
							if(!priElement||priElement.length==0)return;
							var tipSpan = priElement.parents("span.select_contain_span");
							var formtype = priElement.parents("[formtype]");
							if(formtype&&formtype.length>0){
								$("[name='"+name+"']",formtype).each(function(){
									$(this).removeClass('validError');
									$(this).qtip("destroy");
									$(this).unbind('mouseover');
								});
							}
							else{
								$("[name='"+name+"']").each(function(){
									$(this).removeClass('validError');
									$(this).qtip("destroy");
									$(this).unbind('mouseover');
								});
							}
							tipSpan.before(priElement);
							tipSpan.remove();
						}						
						else{
							selectSpan.before(element);
							selectSpan.remove();
						}
					}
					element.removeClass('validError');
					//by weilei:由于表单中关联业务模板，导致Html元素无.qtip()方法；解决的方法：暂时去掉。
					try{element.qtip("destroy");}catch(e){}
					element.unbind('mouseover');
				}
				,rules:com.ibms.form.rule.CustomRules
			});
		},
		validate:function(conf){
			return this.valid.valid(conf);//TODO 初始化表单校验 如有必填，则input的框线为红色。
		},
		handleValueTran : function(obj,value){
			var tagName = $(obj).prop("tagName");
			if(tagName=="SELECT"&&value=="请选择"){
				value = "";
			}
			return value;
		},
		//处理有数据格式定义的数据。
		handNumberData:function(obj){
			var value=$(obj).val();		
			value = this.handleValueTran(obj,value);
			var showType=$(obj).attr("showtype");
			if(showType){
				try{
					showType=showType.replaceAll("'","\"");
					var json=jQuery.parseJSON(showType);
					var coinvalue = json.coinValue;
					var isShowComdify = json.isShowComdify;
					if (coinvalue != null && coinvalue != '') {
						if (value.split(coinvalue) != -1) {
							var ary = value.split(coinvalue);
							value = ary.join("");
						}
					}
					if (isShowComdify) {
						if (value.split(",") != -1) {
							var temp = value.split(",");
							value = temp.join("");
						}
					}
				}
				catch(err){}
	
			}
			return value;
		},
		/**
		 * 获取数据。
		 * 返回json数据。
		 */
		getData: function() {
			var self=this;
			// 主表数据
			var main = {
				fields:{}
			};
			//取主表的字段。
			$("input:text[name^='m:'],input:hidden[name^='m:'],textarea[name^='m:'],select[name^='m:']").each(function() {
				var name = $(this).attr('name');
				var value=self.handNumberData(this);
				main.fields[name.replace(/.*:/, '')] = value;
			});
			
			$("textarea[name^='m:'].ckeditor").each(function() {
				var name = $(this).attr('name');
				var data=CKEDITOR.instances[name].getData();
				main.fields[name.replace(/.*:/, '')] =data;
				$(this).val(data);
			});
			//设置单选按钮。
			self.setMainRadioData(main.fields);
			//设置复选框。
			self.setMainCheckBoxData(main.fields);
			
			//子表数据
			var sub = [];
			$('div[type=subtable][right=w || right=b][show=true||undefined]').each(function() {
				var table = {
					tableName: $(this).attr('tableName'),
					fields: []
				};
				$(this).find('[formtype]').not(":first").each(function() {
					var row = {};
					var objRow=$(this);
					$("input:text[name^='s:'],input[type='hidden'][name^='s:'],textarea[name^='s:'],select[name^='s:']",objRow).each(function() {
						var name = $(this).attr('name').replace(/.*:/, '');
						var value=self.handNumberData(this);
						row[name] = value;
					});
					//设置复选框按钮的数据。
					self.setSubCheckBoxData(objRow,row);
					
					//设置单选按钮的数据
					self.setSubRadioData(objRow, row);
					
					table.fields.push(row);
				});
				sub.push(table);
				
				JSON2.stringify(sub);
			});
			//关系表数据
			var rel = [];
			$('div[type=reltable][right=w || right=b][show=true||undefined]').each(function() {
				var table = {
					tableName: $(this).attr('tableName'),
					fields: []
				};
				$(this).find('[formtype]').not(":first").each(function() {//第一行不显示
					var row = {};
					var objRow=$(this);
					$("input:text[name^='r:'],input[type='hidden'][name^='r:'],textarea[name^='r:'],select[name^='r:']",objRow).each(function() {
						var name = $(this).attr('name').replace(/.*:/, '');
						var value=self.handNumberData(this);
						row[name] = value;
					});
					//设置复选框按钮的数据。
					self.setRelCheckBoxData(objRow,row);
					
					//设置单选按钮的数据
					self.setRelRadioData(objRow, row);
					
					table.fields.push(row);
				});
				rel.push(table);
				
				JSON2.stringify(rel);
			});
			
			//意见
			var opinion = [];
			$('textarea[name^=opinion]').each(function() {
				var _name=$(this).attr('name').split(':')[1];
				var _value=$(this).val();
				opinion.push({
					name: _name,
					value: _value
				});
			});
			
			var data = {main: main, sub: sub,refTab:rel, opinion: opinion};
			
			return JSON2.stringify(data);
		},
		/**
		 * 设置子表复选框的数据
		 * @param objParent
		 * @param dataObj
		 */
		setSubCheckBoxData:function(objParent,dataObj){
			$('input:checkbox',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				dataObj[name]="";
			});
			
			//复选框取值。
			$('input:checkbox:checked',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				if(dataObj[name]==""){
					dataObj[name]=value;
				}
				else{
					dataObj[name]+="," + value;
				}
			});
		},
		/**
		 * 设置子表的radio单选按钮字段。
		 * @param dataObj
		 */
		setSubRadioData:function(objParent,dataObj){
			//单选按钮
			$('input:radio',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				
				if($(this).attr("checked")!=undefined){
					dataObj[name]=value;
				}
			});
		},
		/**
		 * 设置关系表复选框的数据
		 * @param objParent
		 * @param dataObj
		 */
		setRelCheckBoxData:function(objParent,dataObj){
			$('input[name^=r]:checkbox',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				//关联表内联模板 带 checkbox，如果name是id 不能将value值置空 
				if(name!='id'&&name!='ID'){
					dataObj[name]="";
				}
			});
			
			//复选框取值。
			$('input[name^=r]:checkbox:checked',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				if(dataObj[name]==""){
					dataObj[name]=value;
				}
				else{
					dataObj[name]+="," + value;
				}
			});
		},
		/**
		 * 设置关系表的radio单选按钮字段。
		 * @param dataObj
		 */
		setRelRadioData:function(objParent,dataObj){
			//单选按钮
			$('input[name^=r]:radio',objParent).each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				
				if($(this).attr("checked")!=undefined){
					dataObj[name]=value;
				}
			});
		},
		/**
		 * 设置主表的radio单选按钮字段。
		 * @param dataObj
		 */
		setMainRadioData:function(dataObj){
			//单选按钮
			$('input[name^=m]:radio').each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				
				if($(this).attr("checked")!=undefined){
					dataObj[name]=value;
				}
			});
		},
		setMainCheckBoxData:function(dataObj){
			//将所有复选框选址清空。
			$('input[name^=m]:checkbox').each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				dataObj[name]="";
			});
			
			//复选框取值。
			$('input[name^=m]:checkbox:checked').each(function() {
				var name = $(this).attr('name').replace(/.*:/, '');
				var value= $(this).val();
				
				if(dataObj[name]==""){
					dataObj[name]=value;
				}
				else{
					dataObj[name]+="," + value;
				}
			});
		},
		
		/**
		 * 打开操作数据窗口。
		 * @param title 窗口标题
		 * @param form	窗口对象。
		 * @param table 表
		 * @param beforeElement
		 * @param callback 回调函数
		 */
		openWin : function(title, form, table, beforeElement, callback) {
			var formProperty=table.data("formProperty");
			var width=formProperty.width+20;
			var height=formProperty.height+100;
			var self=this;
			
	
			form.data('beforeElement', beforeElement);
			var win = $.ligerDialog({target:form,
//					left : ($(window).width() - 400) / 2,
//					top : ($(window).height() - 300) / 2,
					width:width,
					height:height,
					title : title,
					showMax : true,
					showToggle : true,
					onClose : true,
					showButton : true,
					buttons : [ {
						text : "确定",
						onclick: function (item, dialog) {
							var result = callback(form, table, form.data('beforeElement'));
							if(result) {
							    if(self.onEditCallBack!=null){
									self.onEditCallBack(title);
								}
								dialog.close();
							}
						}
					} ]
				});
			//初始化表单界面
			win.max();//最大化
			this.initUI(form);
			win.show();
		
		},
		/**
		 * 获取右键菜单。
		 * @param needEdit
		 * @returns
		 */
		getMenu : function(needEdit,table) {
			var self=this;
			//获取表的类型 (子表，关系表)
			var tableType =  table.attr('type') ;
			//判断菜单是否存在，不存在则新建菜单。
			if ((needEdit && this.menuWithEdit) || (!needEdit && this.menu)) {
				return needEdit ? this.menuWithEdit : this.menu;
			} else {
				var menu;
				var items = [ {
					text : '在前面插入记录',
					click : function() {
						var row = $(menu.target).closest('[formType]');
						var table = row.closest('div[type='+tableType+']');
						self.add(table, row);
						//修正，当同一表单既有子表又含关系表时，取不到table的问题（this.menu只有一个）
						if(table.length==0){
							if(tableType=='subtable'){
								table = t.closest('div[type=reltable]');
							}else if(tableType=='reltable'){
								table = t.closest('div[type=subtable]');
							}
						}
						//初始化子表选择器
						self.subSelectorInit();
						//初始化子表自定义对话框的下拉情况
						self.subDialogShowInit();
						 //初始化子表级联
						self.initSubQuery(tableType);
						self.handRow('add',table);
					}
				}, {
					text : '在后面插入记录',
					click : function() {
						var row = $(menu.target).closest('[formType]');
						var table = row.closest('div[type='+tableType+']');
						row = row.next('[formType]:visible');
						if (row.length == 0) {
							row = null;
						}
						self.add(table, row);
						//修正，当同一表单既有子表又含关系表时，取不到table的问题（this.menu只有一个）
						if(table.length==0){
							if(tableType=='subtable'){
								table = t.closest('div[type=reltable]');
							}else if(tableType=='reltable'){
								table = t.closest('div[type=subtable]');
							}
						}
						//初始化子表选择器
						self.subSelectorInit();
						//初始化子表自定义对话框的下拉情况
						self.subDialogShowInit();
						 //初始化子表级联
						self.initSubQuery(tableType);
						self.handRow('add',table);
					}
				}, {
					line : true
				}, {
					text : '编辑',
					click : function() {
						var row = $(menu.target).closest('[formType]');
						var table = row.closest('div[type='+tableType+']');
						self.edit(table, row);
					}
				}, {
					text : '删除此记录',
					click : function() {
						var t = $(menu.target).closest('[formType]'),
							table = t.closest('div[type='+tableType+']'),
							brother = t.next('[formType]').length?t.next('[formType]'):t.prev('[formType]');
						//修正，当同一表单既有子表又含关系表时，取不到table的问题（this.menu只有一个）
						if(table.length==0){
							if(tableType=='subtable'){
								table = t.closest('div[type=reltable]');
							}else if(tableType=='reltable'){
								table = t.closest('div[type=subtable]');
							}
						}
							
						t.remove();
						
						self.handRow('del',table);
						if(brother)
							FormUtil.InitMathfunction(brother);
					}
				}, {
					line : true
				}, {
					text : '向上移动',
					click : function() {
						var t = $(menu.target).closest('[formType]');
						var prev = t.prev('[formType]:visible');
						if (prev.length > 0) {
							prev.before(t);
						}
						var table = t.closest('div[type='+tableType+']');
						self.handRow('add',table);
					}
				}, {
					text : '向下移动',
					click : function() {
						var t = $(menu.target).closest('[formType]');
						var next = t.next('[formType]:visible');
						if (next.length > 0) {
							next.after(t);
						}
						var table = t.closest('div[type='+tableType+']');
						self.handRow('add',table);
					}
				} ];
				//如果不需要编辑，删除编辑菜单。
				if (!needEdit) {
					items.splice(3, 1);
				}
				menu = $.ligerMenu({top : 100,left : 100,width : 130,items : items});
				if (needEdit) {
					this.menuWithEdit = menu;
				}
				else{
					this.menu = menu;
				}
				return menu;
			}
		},
		
		
		/**
		 * 从数据库加载子表权限。
		 */
	  loadSubTablePermission:function(){
			var defId = $(':hidden[name=defId]').val();
			var actDefId = $(':hidden[name=actDefId]').val();
			var formKey = $(':hidden[name=formKey]').val();
			var nodeId = $(':hidden[name=nodeId]').val();
		//	alert("defId="+defId+"  actDefId="+actDefId+"  formKey="+formKey+" nodeId="+nodeId);
			var params={defId:defId,actDefId:actDefId,formKey:formKey,nodeId:nodeId};
			var url = "getSubTablePermission.do";
			var map = null;
			$.ajaxSetup({async:false});  //修改成同步
			$.post(url, params,function(data){
				map = data;
	        });
			$.ajaxSetup({async:true}); //改为原来的异步
			return map;
	  },
	  
	  selectValue:function(select){
		    var html='';
			var query=select.attr("selectquery")
			var queryJson;
			if(query!=undefined && query!=null ){
				query=query.replaceAll("'","\"");
				queryJson = JSON2.parse(query);
			}else{
				queryJson = "";
				query = "";
			}
			var sValue=select.attr("val");    //select中有一个属性是保存选中的值的（初始化时用这个）
			if(sValue==undefined || sValue==null && sValue.length<=0){
				sValue = "";
			}
			if(query){
				html="<span selectvalue="+sValue+" selectquery="+query+"><lable></lable></span>"
			}else{
				if(sValue==""){
					html=select.find("option:selected").text();    //获取Select选择的Text   选中（不可编辑时或者没有值时，是默认的）
				}else{
					html += select.find("option[value='"+sValue+"']").text();    //和值相对应的显示
				}
			}
			return html;
	  },
	  
	 /**
	  * 主表的权限补充（必埴）
	  */
	  isMainTableRequest:function(){
		  //主表的附件必埴验证问题
		 $("div[name=div_attachment_container]").each(function() {
			 var div_me=$(this);
			 if(div_me.attr("right")=='b'){
				 $("a.link",div_me).each(function(){
		        	var me=$(this);	
	        		var validRule = me.attr("validate");
					if ( validRule != null && 'undefined' != validRule.toLowerCase() && validRule.length>2 ){ 
						var json = eval('(' + validRule + ')');		
						if(json.required){
							//不做操作
						}else{
							var jsonStr = validRule.substring(0, validRule.lastIndexOf('}'));
							jsonStr +=",\'required\':true}";    //加上必填
							me.attr("validate",jsonStr);    
						}				
					}else{
						me.attr("validate","{\'required\':true}");     //必填
					}
		         });
			 }
		 });
	  },
	  
	   //初始化子表级联
	  initSubQuery:function(tableType){
		
	  	$("select[selectquery]",$("[type="+tableType+"],[formtype='window']")).each(function(){
		  	var me = $(this);
		  	var selected=me.attr("selectqueryed");
		  	//selectqueryed标识这个子表是否已经执行过了change绑定，存在就是已经绑定了，不存在就是没用被绑定过
		  	if(selected)return true;
			var selectquery = me.attr("selectquery");
			if (!selectquery)
				return true;
			selectquery = selectquery.replaceAll("'", "\"");
			var queryJson = JSON2.parse(selectquery);
			var query = queryJson.query;
			for (var i = 0; i < query.length; i++) {
				var field;
				// isMain是true 就是绑定主表的字段
				if (query[i].isMain=="true") {
					field = $(".formTable [name$=':" + query[i].trigger + "']");
				} else {
					field = me.parents('table.listTable,[formtype="window"]').find("[name='" + query[i].trigger + "']");
					//添加新一个子表时，执行了多次绑定change事件，把之前的change事件都解绑
					field.unbind("change");
				}
				if (field != "")
					QueryUI.change(field, me, queryJson);
			}
			//添加一个属性，标识已经被绑定过，
			me.attr("selectqueryed","selectqueryed");
			QueryUI.getvalue(me, queryJson);
	  	});
	  },
	  //初始化子表/关联表的选择器
	 subSelectorInit:function(){
  		$('[ctlType="selector"]','.listTable,[formtype="window"]').each(function(){
  			var me =$(this);
  			var nameID=me.attr('name')+'ID';
  			var isHidden=me.siblings("input[name='"+nameID+"']");
  			//如果存在隐藏域，则是已经添加了
  			if(isHidden.length!=0) return;
				var type = me.attr('class');
				//方法在SelectorInit.js
				buildSelector(me, type);
			});
		},
	  	//初始化子表/关联表的自定义对话框
		 subDialogShowInit:function(){
	  		$('[dialogtype="1"]','.listTable,[formtype="window"]').each(function(){
	  			var me =$(this);
	  			//避免重复添加样式
	  			var isExit=me.parent().find(".dialogTag");
	  			if(isExit.length == 0){
	  				FormUtil.initDialogList();
		  			FormUtil.initCommonDialog();
	  			}
			});
		}
	};
	//对表单初始化。
	CustomForm.init();
});
