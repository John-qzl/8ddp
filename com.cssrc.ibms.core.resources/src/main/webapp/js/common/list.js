	/**
	 * 允许行编辑，在页面中可重写
	 * @param row
	 * @returns {Boolean}
	 */
	function rowEditAllow(row){
		return true;
	}
	/**
	 * 允许行删除，在页面中可重写
	 * @returns {Boolean}
	 */
	function rowRemoveAllow(){
		return true;
	}
	
	/**
	 * 允许行明细，在页面中可重写
	 * @param row
	 * @returns {Boolean}
	 */
	function rowDetailAllow(row){
		return true;
	}

	/**
	 * 构造URL参数
	 * @param url
	 * @param params
	 * @returns
	 */
	function constructUrlParams(url,params){
		if(url.indexOf('?')==-1){
			url+="?";
		}
		url+="&"+jQuery.param(params);
		return url;
	}

	//搜索字段条件变化时
	function onSearchFieldChanged(e) {
        var combo = e.sender;
        var comboData = combo.getData();
        var val = combo.getValue();
        var record = null;
        for (var i = 0; i < comboData.length; i++) {
            if (val == comboData[i].attrName) {
                /*row.fieldId=comboData[i].fieldId;
                 row.label=comboData[i].title;                        
                 row.ctlType=comboData[i].compType;
                 */
                record = comboData[i];
                break;
            }
        }
        if (record == null) return;
        var fieldType = record.fieldType;
        var opEditor = mini.get('fieldCompare');
        //设置比较编辑器
        if (fieldType == 'java.lang.String') {
            opEditor.setUrl(__ctx+'oa/dics/compares_of_string.txt');
        } else {
            opEditor.setUrl(__ctx+'oa/dics/compares_of_num_date.txt');
        }
		//TODO
        if (fieldType == 'java.lang.String') {
        	var fieldVal=mini.get('fieldVal');
        	var el=fieldVal.getEl();
        	var pl=new mini.TextBox();
        	pl.setId('fieldVal');
        	$(el).html('');
        	pl.render(el);
        	
        } else if (fieldType == 'java.lang.Integer' || fieldType == 'java.lang.Short' || fieldType == 'java.lang.Float' ||
                fieldType == 'java.lang.Double' || fieldType == 'java.math.BigDecimal') {
        	var fieldVal=mini.get('fieldVal');
        	var el=fieldVal.getEl();
        	var pl=new mini.TextBox();
        	pl.setId('fieldVal');
        	$(el).html('');
        	pl.render(el);

        } else if (fieldType == 'java.util.Date') {
        	var fieldVal=mini.get('fieldVal');
        	var el=fieldVal.getEl();
        	var pl=new mini.DatePicker();
        	pl.setId('fieldVal');
        	pl.setFormat('yyyy-MM-dd');
        	$(el).html('');
        	pl.render(el);
        }
        
        opEditor.setValue(opEditor.getData()[0].fieldOp);
    }
	/**
	 * 自定义的查询的默认处理
	 * @param e
	 */
	function selfSearch(e){
		 var button = e.sender;
		 var el=button.getEl();
		 var form=$(el).parents('form');
		 
		 if(form!=null){
		 	var formData=form.serializeArray();
		 	var data={};
		 	//加到查询过滤器中
			data.filter=mini.encode(formData);
			/*
		 	for(var i=0;i<formData.length;i++){
				data[formData[i].name]=formData[i].value;
			}*/
			data.pageIndex=grid.getPageIndex();
			data.pageSize=grid.getPageSize();
		    data.sortField=grid.getSortField();
		    data.sortOrder=grid.getSortOrder();
			grid.load(data);
		 }
	}
	
	function selfClearSearch(e){
		 var button = e.sender;
		 var el=button.getEl();
		 var form=$(el).parents('form');
		 if(form!=null){
			 form[0].reset();
		 }
	}

    function ShowUploadDialog(config) {
        var url = __ctx+"/oa/system/sysFile/dialog.do?1=1";
        if (config.recordId) {
            url += '&recordId=' + config.recordId;
        }
        if (config.entityName) {
            url += '&entityName=' + config.entityName;
        }
        mini.open({
            allowResize: true, //允许尺寸调节
            allowDrag: true, //允许拖拽位置
            showCloseButton: true, //显示关闭按钮
            showMaxButton: true, //显示最大化按钮
            showModal: true,
            url: url,
            title: "上传文件", width: 600, height: 420,
            onload: function() {
            },
            ondestroy: function(action) {
                if (action == 'ok') {
                    var iframe = this.getIFrameEl();
                    var files = iframe.contentWindow.getFiles();
                    if (config.callback) {
                        config.callback.call(this, files);
                    }
                }
            }
        });
    }

    function newAttach(entityName) {
        var row = grid.getSelected();
        if (!row) {
            alert("请选中一条记录");
            return;
        }
        ShowUploadDialog({
            entityName: entityName,
            recordId: row.pkId
        });
    }

    function previewAttachs(entityName) {
        var row = grid.getSelected();
        if (!row) {
            alert("请选中一条记录");
            return;
        }
        var url = __ctx + "/oa/system/sysFile/recordList.do?recordId=" + row.pkId + "&entityName="+entityName;
        mini.open({
            allowResize: true, //允许尺寸调节
            allowDrag: true, //允许拖拽位置
            showCloseButton: true, //显示关闭按钮
            showMaxButton: true, //显示最大化按钮
            showModal: true,
            url: url,
            title: "附件列表",
            width: 600,
            height: 420
        });
    }
    
    function preRecord() {
        var row = grid.getSelected();
        var index = grid.indexOf(row);
        if (index == 0)
            return 0;
        grid.deselect(index);
        grid.select(index - 1);
        var preRow = grid.getSelected();
        return preRow.pkId;
    }

    function nextRecord() {
        var row = grid.getSelected();
        var index = grid.indexOf(row);

        if (index == grid.totalCount - 1)
            return 0;

        grid.deselect(index);
        grid.select(index + 1);
        var nextRow = grid.getSelected();
        return nextRow.pkId;
    }
    //复制一新记录进行清加
    function copyAdd() {
        var row = grid.getSelected();
        if (row) {
            add(row);
        } else {
            alert("请选中一条记录");
        }
    }

    function detail() {
        var row = grid.getSelected();
        //行允许查看明细
        if(rowDetailAllow && !rowDetailAllow(row)){
        	return;
        }
        if (row) {
            detailRow(row.pkId);
        } else {
            alert("请选中一条记录");
        }
    }

    //更新表单视图
    function changeGridView() {
        var gridViewId = mini.get('gridViewCombo').getValue();
        $.ajax({
            url: __ctx + "/ui/view/sysGridView/getColumns.do?isInclude=true",
            data: {
                gridViewId: gridViewId
            },
            success: function(text) {
                var columns = mini.decode(text);
                grid.set({columns: columns});
                grid.load();
                //grid.reload();
            },
            error: function() {
            }
        });
    }

    
    function loadGrid() {
        grid.reload();
    }
    

    /**
     * 返回数据类型
     * @param javaType
     * @returns {String}
     */
    function getDataField(javaType) {
        if (javaType == 'java.lang.String')
            return 'String';
        if (javaType == 'java.util.Date')
            return 'Date';
        if (javaType == 'java.lang.Short')
            return 'Number';
        if (javaType == 'java.lang.Integer')
            return 'Number';
        if (javaType == 'java.lang.Long')
            return 'Number';
        if (javaType == 'java.math.BigDecimal')
            return 'Number';
        if (javaType == 'java.lang.Double')
            return 'Number';
        if (javaType == 'java.lang.Float')
            return 'Number';
        return 'String';
    }

    function getDataQueryType(javaType) {
        if (javaType == 'java.lang.String')
            return 'S';
        if (javaType == 'java.util.Date')
            return 'D';
        if (javaType == 'java.lang.Short')
            return 'SN';
        if (javaType == 'java.lang.Integer')
            return 'I';
        if (javaType == 'java.lang.Long')
            return 'L';
        if (javaType == 'java.math.BigDecimal')
            return 'BD';
        if (javaType == 'java.lang.Double')
            return 'DB';
        if (javaType == 'java.lang.Float')
            return 'F';
        return 'S';
    }

    function getDataOpType(opType) {
        if (opType == 'lt')
            return 'LT';
        if (opType == 'gt')
            return 'GT';
        if (opType == 'lt_eq')
            return 'LE';
        if (opType == 'gt_eq')
            return 'GE';
        if (opType == '=')
            return 'EQ';
        if (opType == 'like')
            return 'LK';
        if (opType == 'left-like')
            return 'LEK';
        if (opType == 'right-like')
            return 'RIK';
        return 'EQ';
    }

    function search() {
        var combo = mini.get('fieldName');
        var fieldName = combo.getValue();
        var val = combo.getValue();
        var record = null;
        var comboData = combo.getData();
        for (var i = 0; i < comboData.length; i++) {
            if (val == comboData[i].attrName) {
                record = comboData[i];
                break;
            }
        }
        if (record == null)
            return;
        var fieldType = record.fieldType;
       
        var fieldCompare = mini.get('fieldCompare').getValue();     
        var fieldVal = mini.get('fieldVal').getValue();
        var queryField = 'Q_' + fieldName + '_' + getDataQueryType(fieldType) + '_' + getDataOpType(fieldCompare);
        
        var data={};
        data.filter=mini.encode([{name:queryField,value:fieldVal}]);
		data.pageIndex=grid.getPageIndex();
		data.pageSize=grid.getPageSize();
	    data.sortField=grid.getSortField();
	    data.sortOrder=grid.getSortOrder();
		grid.load(data);
        
    }
    
    //清空查询条件
    function clearSearch(){
    	grid.setUrl(gdBaseUrl);
    	grid.load();
    }
    
    function searchByFilterId(searchId){
    	grid.setUrl(constructUrlParams(gdBaseUrl,{_searchId:searchId}));
        grid.load();
    }

    //视图方案管理器
    function onGridViewMgr(entityName) {
    	_OpenWindow({
    		url: __ctx + "/ui/view/sysGridView/list.do?entityName="+entityName,
            title: "视图方案管理",
            width: 720,
            height: 380,
            ondestroy: function(action) {
                mini.get('gridViewCombo')
                        .load(__ctx + '/ui/view/sysGridView/listByEntityName.do?entityName='+entityName);
            }
    	});
    }
    function newSearch(entityName) {
    	
    	_OpenWindow({
    		 title: "高级查询管理",
    		 iconCls:'icon-addSearch',
             width: 700,
             height: 550,
             url: __ctx + "/ui/search/sysSearch/edit.do?entityName="+entityName,
             onload: function() {
                 var iframe = this.getIFrameEl();
                 var data = {action: "edit", pWin: window};
                 //取子窗口中数据
                 iframe.contentWindow.setData(data);
             },
             ondestroy: function(action) {
                 if (action == 'ok') {
                     grid.reload();
                 }
             }
    		
    	});
    }

    //显示查询方案列表
    function searchList(entityName) {
        _OpenWindow({
            url:__ctx + "/ui/search/sysSearch/list.do?entityName="+entityName,
            title: "高级查询列表",
            iconCls:'icon-loadSearch',
            width: 650,
            height: 420,
            onload: function() {
                 var iframe = this.getIFrameEl();
                 var data = {pWin:window};
                 iframe.contentWindow.setData(data);
            },
            ondestroy: function(action) {
                //if (action == "close") return false;
                if (action == "ok") {
                    var iframe = this.getIFrameEl();
                    var data = iframe.contentWindow.GetData();
                    data = mini.clone(data);    //必须
                    if (data) {
                        btnEdit.setValue(data.id);
                        btnEdit.setText(data.name);
                    }
                }
            }
        });
    }
    
    //添加GridView
    function addGridView(entityName) {
    	_OpenWindow({
    		title:'增加视图方案',
    		width: 680, 
            height: 480,
    		url: __ctx + "/oa/portal/insColumn/edit.do"
    		onload: function() {
                var iframe = this.getIFrameEl();
                iframe.contentWindow.initGrids(grid.columns);
            },
            ondestroy: function(action) {
            }
    	});
    	
    }
    
    //加载表格视图
    function loadGridViews(){
    	
    }

    function onKeyEnter(e) {
        search();
    }

    function printCurPage() {
        var LODOP = getLodop();
        LODOP.PRINT_INIT("打印控件功能演示_Lodop功能_全页");
        LODOP.ADD_PRINT_HTM(0, 0, "100%", "100%", $('#datagrid1').html());
        LODOP.PREVIEW();
    }

    //保存当前视图方案
    function saveCurGridView() {
        var gridViewId = mini.get('gridViewCombo').getValue();
        var columns = grid.getColumns();
        var valCols = [];
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            if (col.type == 'checkcolumn' || col.name == 'action') {
                continue;
            }
            valCols.push(col);
        }

        $.ajax({
            url: __ctx + "/ui/view/sysGridView/saveCurView.do",
            method: 'post',
            data: {
                gridViewId: gridViewId,
                columns: mini.encode(valCols)
            },
            success: function(text) {
                mini.showMessageBox({
                    showModal: false,
                    width: 250,
                    title: "提示",
                    iconCls: "mini-messagebox-warning",
                    message: "成功保存",
                    timeout: 3000,
                    x: 'right',
                    y: 'bottom'
                });
            },
            error: function() {
            }
        });
    }

    
    //另存当前视图方案
    function saveAsNewGridView(entityName) {
        var gridViewId = mini.get('gridViewCombo').getValue();
        _OpenWindow({
            url: __ctx + "/ui/view/sysGridView/edit.do?asNew=true&gdViewId=" + gridViewId + "&entityName="+entityName,
            title: "增加视图方案",
            width: 680,
            height: 420,
            onload: function() {
                var iframe = this.getIFrameEl();
                //取子窗口中数据
                iframe.contentWindow.initGrids(grid.columns);
            },
            ondestroy: function(action) {
            }
        });
    }