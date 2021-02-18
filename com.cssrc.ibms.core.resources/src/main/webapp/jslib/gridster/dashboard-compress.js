(function(e, c, a, g) {
	var d = "dashboard", f = {
		version : "1.0"
	};
	function b(l, k) {
		this.element = e(l);
		this.pageInfo = k && k.cfg ? k.cfg : {};
		this.editorEnable = k && k.editorEnable ? k.editorEnable : "1";
		this._defaults = f;
		this._name = d;
		this.gridster = this.init();
		this.initComps();
		this.update = false;
		this.tmp = null;
		c.dashboard = this
	}
	b.prototype = {
		init : function() {
			var m = this;
			e("#savePortal").click(function() {
				m.savePage();
			});
			e("#saveGlobal").click(function() {
				m.saveGlobal();
			});
			e("#addPortal").click(function() {
				m.crtWidget();
			});
			e("#refreshPortal").click(function() {
				location.reload();
			});
			var l = e(c).width();
			var k = this.element
					.gridster(
							{
								widget_selector : "div.dashboard-box",
								widget_margins : [ 4, 5 ],
								widget_base_dimensions : [ "auto", 60 ],
								autogenerate_stylesheet : true,
								max_cols : 10,
								resize : {
									enabled : m.editorEnable == "1" ? true
											: false,
									stop : function(r, u, p) {
										m.update = true;
										var n = e(p);
										var q = n.height() - 35;
										var v = n.width();
										var x = e(p).attr("id");
										var t = m.pageInfo[x];
										if (t.type == "chart") {
											t.chartJson.height = q;
											t.chartJson.width = v;
											var n = a.getElementById("C" + x);
											if (n) {
												var s = echarts
														.getInstanceByDom(n);
												e("#C" + x).height(q).width(v);
												s.resize("auto", "auto")
											}
										} else {
											if (t.type == "box") {
												t.height = q;
												n.find(".boxcls")
														.css("line-height",
																q + "px")
											} else {
												if (t.type == "table") {
													t.height = q - 26;
													n
															.find(
																	".lock-dg-body")
															.animate(
																	{
																		height : t.height
																				+ "px"
																	})
												} else {
													if (t.type == "grid") {
														t.height = (q - 25 - (t.isnotfy == "true" ? 0
																: 35));
														n
																.find(
																		".lock-dg-body")
																.animate(
																		{
																			height : t.height
																					+ "px"
																		})
													} else {
														if (t.type == "text") {
															t.height = q
														}
													}
												}
											}
										}
									}
								},
								draggable : {
									handle : "header",
									stop : function() {
										m.update = true
									}
								},
								serialize_params : function(o, n) {
									return {
										id : e(o).attr("id"),
										col : n.col,
										row : n.row,
										size_x : n.size_x,
										size_y : n.size_y
									}
								}
							}).data("gridster");
			if (m.editorEnable == "0") {
				k.disable()
			}
			return k
		},
		initComps : function() {
			var m = this;
			if (m.pageInfo.layout && m.pageInfo.layout.length > 0) {
				e(".gridster .helptxt").remove();
				for (i = 0; i < m.pageInfo.layout.length; i++) {
					var n = m.pageInfo.layout[i];
					var k = m.pageInfo[n.id];
					if (!k) {
						continue
					}
					m.addWidget(k, false, n.size_x, n.size_y, n.col, n.row)
				}
			}
			//删除
			e("body div.gridster").on("click", "a.rmwidget", function() {
				var o = e(this).parents(".dashboard-box").attr("id");
				m.removeWidget(o);
			});
			//设置
			e("body div.gridster").on("click", "a.setwidget", function() {
				var o = e(this).parents(".dashboard-box").attr("id");
				m.setwidget(this, o);
			});
			//刷新
			e("body div.gridster").on("click", "a.refreshwidget", function() {
				var o = e(this).parents(".dashboard-box").attr("id");
				m.refreshWidget(o);
			});
			//更多
			e("body div.gridster").on("click", "a.morewidget", function() {
				var o = e(this).parents(".dashboard-box").attr("id");
				m.moreWidget(this, o);
			});
		},
		setwidget : function(e, n) {
			var d = document.getElementById(n);
			var key = $(e).attr('key');
	    	if(key=="listingPreview"){
	    		var url = __ctx+"/oa/system/listConfs/dialog.do";
	        		DialogUtil.open({
						height:500,
						width: 1000,
						url: url,
						showMax: false,                             //是否显示最大化按钮 
					    showToggle: false,                         //窗口收缩折叠
					    title: "数据预览设置",
					    showMin: false,
						sucCall:function(rtn){
							var f = $(".box-portal-frame", d);
							if(f && f.length>0){
								f[0].src = f[0].src;
							}		
						}
					});	
	    	}else if(key=="linkListPreview"){
	    		var url=__ctx+"/oa/system/CustLinkList/dialog.do?";
	 		    DialogUtil.open({
					height:500,
					width: 1000,
					url: url,
					showMax: false,                             //是否显示最大化按钮 
				    showToggle: false,                         //窗口收缩折叠
				    title: "看板链接管理",
				    showMin: false,
					sucCall:function(rtn){
						var f = $(".box-portal-frame", d);
						if(f && f.length>0){
							f[0].src = f[0].src;
						}		
					}
				});	 	
	    	}else if(key=="dailyWork"){
	    		var url=__ctx+"/oa/console/columnBoxEdit.do?";
	 		    DialogUtil.open({
					height:600,
					width: 500,
						url: url,
					e: e,
					showMax: false,                             //是否显示最大化按钮 
				    showToggle: false,                         //窗口收缩折叠
				    title: "个人日常办公配置",
				    showMin: false,
					sucCall:function(rtn){
						var f = $(".box-portal-frame", d);
						if(f && f.length>0){
							f[0].src = f[0].src;
						}		
					}
				});	 	
	    	}
		},
		refreshWidget : function(n) {
			var d = a.getElementById(n);
			var f = $(".box-portal-frame", d);
			if(f && f.length>0){
				f[0].src = f[0].src;
			}			
		},
		moreWidget : function(e, n) {
			var key = $(e).attr('key');
			if(key=="taskList"){
        		//task_url task_name task_id 三个值均已初始化 默认未待办的数据 
        		//task_id用于标识任务列表的多个tab： 每个标签页的ID唯一
        		var moreUrl = __ctx+$("#task_url").val();
            	var name = $("#task_name").val();
            	var colId = $(e).attr('pid')+$("#task_id").val();
            	mgrNewsRow(colId,name,moreUrl);
            	
        	}else if(key=="taskWarning"){
        		//不作处理
        	}else{
        		var moreUrl = $(e).attr('url');
            	var name = $(e).attr('pname');
            	var colId = $(e).attr('pid');
            	mgrNewsRow(colId,name,moreUrl);
        	}	
		},
		editorSave : function(l, m) {
			e("#compEditer").hide();
			var k = this.tmp;
			if (m && k == "dashboard") {
				this.pageInfo[l.id] = l;
				var o = l.name;
				e("#" + l.id + " .ibox-title").contents().filter(
						function(n, p) {
							if (p.nodeType == 3) {
								p.nodeValue = o
							}
							return p.nodeType == 3
						});
				this.compView(l)
			} else {
				if (m && k == "comp") {
					e.post("share/update.action", {
						title : l.name,
						cfg : JSON.stringify(l),
						id : l.id
					}, function(n) {
					})
				}
			}
			this.tmp = null
		},
		crtWidgetHtml : function(k) {
			var tools = "";
			var n = k.name;
			var l = k.id;
			var closeAllow = l.closeAllow;
			var more = '<a href="javascript:;" class="morewidget" title="更多" url="'+k.moreUrl+'" key="'+k.key+'" pname="'+k.name+'" pid="'+k.id+'"><i class="fa fa-ellipsis-h"></i></a>';
			var settings = '<a href="javascript:;" class="setwidget" title="设置" key="'+k.key+'"><i class="fa fa-cog"></i></a>';
			var refresh = '<a href="javascript:;" class="refreshwidget" title="刷新"><i class="fa fa-refresh"></i></a>';
			var del = '<a title="删除" href="javascript:;" class="rmwidget"><i class="fa fa-times"></i></a>';
			if(k.key=="taskWarning"){
				tools = refresh;
	    	}else if(k.key=="listingPreview"||k.key=="linkListPreview"){
				tools = settings + " " + refresh;
	    	}else if(k.key=="dailyWork"){
				tools = settings + " " + more + " " + refresh;
	     	}else{
				tools = more + " " + refresh;
	    	}
			if(this.editorEnable != "0" || closeAllow){
				tools += " " + del;
			}
			var html= '<div id="'
					+ (l ? l : this.newGuid())
					+ '" class="ibox dashboard-box" style="margin-bottom:0px;"><header class="ibox-title">';
				if(k.iconCls && k.iconCls.length>0){
					html+= '<i class="'+k.iconCls+'"></i>';
				}else{
					html+= '<i class="icon"></i>';
				}
				html+= (n ? n : "_未命名")
					+ '<div class="ibox-tools">'+tools+'</div></header><div class="ibox-content"> </div></div>';
			return html;
		},
		removeWidget : function(k) {
			if (confirm("是否确认删除？")) {
				this.gridster.remove_widget(a.getElementById(k));
				this.update = true;
				
				//删除栏目
				$.ajax({
					url : __ctx + '/oa/portal/insPortal/delCol.do',
	    			method : 'POST',
	    			data : {
	    				colId:k,
	    				portId:$('#portId').val()
	    			},
	    			success : function(result) {
	    				location.reload();
	    				return;
	    			}
				});
			}
			delete this.pageInfo[k]
		},
		crtWidget : function() {
			var k = this;
			top['index'].$.ligerDialog.open({
		        height:500,
		        width: 750,
	   			url: __ctx+"/oa/portal/insPortal/addColumn.do?portId="+$('#portId').val(),
	           	title: "增加本门户栏目",
		        isResize: true,
		        sucCall:function(rtn){
		        	/*c.setTimeout(function() {
						var n = h = 3;
						if (m.type == "table" || m.type == "grid") {
							n = 6
						}
						k.addWidget(m, true, n, h)
					}, 100);*/
		        	
		        	refreshPage();
		        }
		    });
		},
		addWidget : function(m, k, l, p, n, q) {
			if (k && this.pageInfo[m.id]) {
				alert("模块已经存在");
				return
			}
			e(".gridster .helptxt").remove();
			this.pageInfo[m.id] = m;
			var o = this.crtWidgetHtml(m);
			this.gridster.add_widget(o, l, p, n, q);
			this.update = true;
			var p = e("#" + m.id).height() - 35;
			if (m.type == "table") {
				p = p - 25
			} else {
				if (m.type == "grid") {
					p = p - 25 - 30
				}
			}
			if (m.type == "chart") {
				m.chartJson.height = p
			} else {
				m.height = p
			}
			this.compView(m)
		},
		compView : function(k) {
			var l = k.url;
			if (k.type == "TEMPLATE") {
				l = __ctx+'/oa/portal/insPortal/getPortalHtml.do?colId='+k.id;
			}
			var m = '<iframe src="' + l + '" class="box-portal-frame" width="100%" height="100%" frameborder="no" border="0"></iframe>'; //支持传入html
			e("#" + k.id + " div.ibox-content").html(m);            
		},
		seriesClick : function(o, n, r, q, p, s, l, m, k) {
			this.addParam(m, o, k, n, s)
		},
		tableRowClick : function(n, m, l, k, o) {
			this.addParam(l, o, k, o, m)
		},
		addParam : function(y, x, k, q, z) {
			if (!this.pageInfo.params) {
				this.pageInfo.params = []
			}
			var v = this.pageInfo[z];
			var u = null;
			if (v.type == "chart") {
				u = v.chartJson.xcol
			} else {
				if (v.type == "table") {
					u = v.rows[v.rows.length - 1]
				}
			}
			var m = this.newGuid();
			var l = {
				key : y,
				colname : u.colname,
				value : x,
				id : m,
				tid : v.tid,
				from : v.id,
				keyDesc : k,
				valueDesc : q,
				valType : u.valType
			};
			if (this.findParamById(m) != null) {
				return
			}
			var r = this.pageInfo.params;
			var t = -1;
			for (i = 0; r && i < r.length; i++) {
				if (r[i].from == v.id && r[i].key == y) {
					e("#" + r[i].id).remove();
					t = i
				}
			}
			if (t != -1) {
				r.splice(t, 1)
			}
			this.pageInfo.params.push(l);
			var A = ' <span class="label label-success dashboradparam" id="'
					+ m
					+ '">'
					+ k
					+ "("
					+ q
					+ ')<button class="btn btn-link btn-xs paramdelbtn"><i class="fa fa-remove"></i></button></span>';
			e("#paramDiv").append(A).show();
			for ( var n in this.pageInfo) {
				var w = this.pageInfo[n];
				if (w.id && w.id == n) {
					if (w.id == z) {
						continue
					}
					if (w.tid != v.tid) {
						continue
					}
					var v = this.pageInfo[w.id];
					v.dashboardParam = this.pageInfo.params;
					this.compView(v)
				}
			}
		},
		findParamById : function(l) {
			var k = null;
			for (i = 0; this.pageInfo.params && i < this.pageInfo.params.length; i++) {
				if (this.pageInfo.params[i].id == l) {
					k = this.pageInfo.params[i]
				}
			}
			return k
		},
		savePage : function() {
			var k = this.gridster.serialize();
			var n = function(q, o) {
				var t = q.col;
				var r = q.row;
				var s = o.col;
				var p = o.row;
				if (r == p) {
					return t - s
				} else {
					return r - p
				}
			};
			k = k.sort(n);
			this.pageInfo.layout = k;
			var m = this;
			if (!m.pageInfo.id) {
				alert("未设置门户");
			} else {
				e.ajax({
					type : "POST",
					url : __ctx + '/oa/portal/insPortal/savePortal.do' ,
					dataType : "JSON",
					data : {
						data : JSON.stringify(m.pageInfo),
						portId : m.pageInfo.id
					},
					success : function(o) {
						m.update = false;
						if (!m.pageInfo.id) {
							m.pageInfo.id = o.rows
						}
						alert("保存成功！", "success")
					}
				})
			}
		},
		saveGlobal : function() {
			var k = this.gridster.serialize();
			var n = function(q, o) {
				var t = q.col;
				var r = q.row;
				var s = o.col;
				var p = o.row;
				if (r == p) {
					return t - s
				} else {
					return r - p
				}
			};
			k = k.sort(n);
			this.pageInfo.layout = k;
			var m = this;			
			var name = $("input[name='name']").val();
			var desc = $("input[name='desc']").val();
			if (!m.pageInfo.id) {
				alert("未设置门户");
			} else {
				e.ajax({
					type : "POST",
					url : __ctx + '/oa/portal/insPortal/saveGlobal.do' ,
					dataType : "JSON",
					data : {
						data : JSON.stringify(m.pageInfo),
						portId : m.pageInfo.id,
						name: name,
						desc: desc
					},
					success : function(o) {
						m.update = false;
						if (!m.pageInfo.id) {
							m.pageInfo.id = o.rows
						}
						alert("保存成功！", "success")
					}
				})
			}
		},
		newGuid : function() {
			var k = "";
			for (var l = 1; l <= 32; l++) {
				var m = Math.floor(Math.random() * 16).toString(16);
				k += m
			}
			return k
		}
	};
	e.fn[d] = function(k) {
		this.each(function() {
			var l = e(this);
			if (l.data(d)) {
				l.data(d).remove()
			}
			l.data(d, new b(this, k))
		});
		return this
	}
})(jQuery, window, document);