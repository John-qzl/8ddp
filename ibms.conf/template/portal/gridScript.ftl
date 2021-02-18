<script type="text/javascript">
	var orgId='${orgId}';
	//进行转化
	mini.parse();
	
	//用于弹出的子页面获得父窗口
	top['${entityName}']=window;
	
    var grid = mini.get("${gridId}");
    //存储其原URL，为后续的高级查询条件的组合
    var gdBaseUrl=grid.getUrl();
  
	grid.load();

	function loadGrid(){
		grid.load();
	}
    function detailRow(pkId) {
        _OpenWindow({
        	url: "${rootPath}/${baseUrl}/get.do?pkId=" + pkId,
            title: "${entityTitle}明细", width: ${winWidth}, height: ${winHeight}
        });
    }

	//复制添加数据
    function add(rowData) {

    	if(isExitsFunction('_add')){
    		_add(rowData);
    		return;
    	}
    	var urlAppend="";
    	if(rowData){
    		urlAppend="&forCopy=true&pkId="+rowData.pkId;
    	}
    	
    	_OpenWindow({
    		url: "${rootPath}/${baseUrl}/edit.do?orgId=${orgId}"+urlAppend,
            title: "新增${entityTitle}", width: ${winWidth}, height: ${winHeight},
            ondestroy: function(action) {
            	if(action == 'ok' && typeof(addCallback)!='undefined'){
            		var iframe = this.getIFrameEl().contentWindow;
            		addCallback.call(this,iframe);
            	}else if (action == 'ok') {
                    grid.reload();
                }
            }
    	});
    }
	//编辑行数据
    function editRow(pkId) {
    	if(isExitsFunction('_editRow')){
    		_editRow(pkId);
    		return;
    	}
    	    
        _OpenWindow({
    		 url: "${rootPath}/${baseUrl}/edit.do?orgId="+orgId+"&pkId="+pkId,
            title: "编辑${entityTitle}",
            width: ${winWidth}, height: ${winHeight},
            ondestroy: function(action) {
                if (action == 'ok') {
                    grid.reload();
                }
            }
    	});
    }
    
	//编辑
    function edit() {
    
        var row = grid.getSelected();
                //行允许删除
        if(rowEditAllow && !rowEditAllow(row)){
        	return;
        }
        
        if (row) {
            editRow(row.pkId);
        } else {
            alert("请选中一条记录");
        }
        
    }
    //删除行
    function delRow(pkId) {
    
    	if(isExitsFunction('_delRow')){
    		_delRow(pkId);
    		return;
    	}
    	
        if (!confirm("确定删除选中记录？")) return;
       
        _SubmitJson({
        	url:"${rootPath}/${baseUrl}/del.do",
        	method:'POST',
        	data:{ids: pkId},
        	success: function(text) {
                grid.load();
            }
         });
    }
    //删除多条记录
    function remove() {
    
    	if(isExitsFunction('_remove')){
    		_remove();
    		return;
    	}
    	
        var rows = grid.getSelecteds();
        if (rows.length <= 0) {
        	alert("请选中一条记录");
        	return;
        }
        //行允许删除
        if(rowRemoveAllow && !rowRemoveAllow()){
        	return;
        }
        
        if (!confirm("确定删除选中记录？")) return;
        
        var ids = [];
        for (var i = 0, l = rows.length; i < l; i++) {
            var r = rows[i];
            ids.push(r.pkId);
        }
        //grid.loading("操作中，请稍后......");

        _SubmitJson({
        	url:"${rootPath}/${baseUrl}/del.do",
        	method:'POST',
        	data:{ids: ids.join(',')},
        	 success: function(text) {
                grid.load();
            }
        });
       
    }
    
    //导出当前页
    function exportCurPage() {
        //var gridViewId=mini.get('gridViewCombo').getValue();
		var url=constructUrlParams(grid.getUrl(),{_export:true});
        var columns = grid.getColumns();
        var valCols = [];
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            if (col.type == 'checkcolumn' || col.name == 'action') {
                continue;
            }
            valCols.push(col);
        }
        jQuery.download(url, {
            pageIndex: grid.getPageIndex(),
            pageSize: grid.getPageSize(),
            columns: mini.encode(valCols)}, 'post');
    }
    //导出所有页
    function exportAllPage() {
        //var url = __ctx + "/${baseUrl}/downAllRecord.do";
        var url=constructUrlParams(grid.getUrl(),{_export:true,_all:true});
        var columns = grid.getColumns();
        var valCols = [];
        for (var i = 0; i < columns.length; i++) {
            var col = columns[i];
            if (col.type == 'checkcolumn' || col.name == 'action') {
                continue;
            }
            valCols.push(col);
        }
        jQuery.download(url, {
            pageIndex: grid.getPageIndex(),
            pageSize: grid.getPageSize(),
            columns: mini.encode(valCols)}, 'post');
    }
    
    
</script>