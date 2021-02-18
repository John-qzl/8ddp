__TableFactory = function(){
	/**
	 * 维护一个当前表的缓存
	 * 用于存放table信息
	 * 格式 {tableId={table},...}
	 */
	this.TablePool = {},
	
	this.getTable = function(tableId){
		try{
			var tables = this.TablePool;
			var isExist = tables[tableId]?true:false;
			if(isExist){
				return tables[tableId];
			}else{
				$.ajax({
					url : __ctx + '/oa/form/formTable/getTableById.do',
					data : {
						tableId : tableId
					},
					async : false, //{false：同步加载，true：异步加载} 
					type : 'post',
					success : function(data) {
						tables[tableId] = data.table;
					}
				});
				return tables[tableId];
			}
		}catch(err){
			console.log("TableFactory.getTable("+tableId+") 执行出错！");
			return null;
		}
	},
	this.getField = function(tableId,fieldName){
		var defaultField = {tableId:tableId,fieldName:fieldName};
		var field,table;
		var table = this.getTable(tableId);
		if($.isEmpty(table)){
			defaultField.error = tableId+"没有取到对应的table";
			console.log(defaultField.error);
			return defaultField;
		}
		$(table.fieldList).each(function(i,e){
			var name = e.fieldName;
			if(name==fieldName){
				field = e;
				return false;
			}
		})
		if(!field){
			defaultField.error = tableId+"对应的table中没有字段"+fieldName;
			console.log(defaultField.error);
			return defaultField;
		}else{
			return field;
		}
		
	}
}
if(!TableFactory){
	var TableFactory = new __TableFactory();
}