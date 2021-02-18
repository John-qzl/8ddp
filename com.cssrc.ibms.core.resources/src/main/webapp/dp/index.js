$(function(){


	layui.use(['element','form','laypage','tree'],function(){
	    var element = layui.element,
	        form = layui.form,
	    	laypage = layui.laypage;
	    	
	    	
	 
	    $('.layui-tab-title li[tableName]').click(function(){
			var elemName = $(this).attr('tableName');
			setTimeout(function(){
				layui.use(['table','form'],function(){
					var table = layui.table;
					//table1实例
					table.render({
						elem:'#'+elemName,
						height: 'full-100',
						page: true,
						url: './api.json',
						cols:[[//表头
							{type:'checkbox'},
							{field:'I0',title:'checkbox1',width:120,sort:true},
							{field:'I1',title:'检查项描述',width:120,sort:true},
							{field:'I2',title:'I类单点',width:120,sort:true},
							{field:'I3',title:'II类单点',width:120,sort:true},
							{field:'I4',title:'易错难',width:120,sort:true},
							{field:'I5',title:'拧紧力矩要求',width:120,sort:true},
							{field:'I6',title:'最后一次动作',width:120,sort:true},
							{field:'I7',title:'是否多媒体项',width:120,sort:true}			
						]]
					})
				})
			},20)
		})
	  










	})
})

	
		
	
	
	
	
	
//	layui.use('table',function(){
//		var table = layui.table;
//		//table1实例
//		table.render({
//			elem:'#table1',
//			height: 600,
//			page: true,
//			url: './api.json',
//			cols:[[//表头
//			    {type:'checkbox'},
//				{field:'I0',title:'checkbox',width:120,sort:true},
//				{field:'I1',title:'检查项描述',width:120,sort:true},
//				{field:'I2',title:'I类单点',width:120,sort:true},
//				{field:'I3',title:'II类单点',width:120,sort:true},
//				{field:'I4',title:'易错难',width:120,sort:true},
//				{field:'I5',title:'拧紧力矩要求',width:120,sort:true},
//				{field:'I6',title:'最后一次动作',width:120,sort:true},
//				{field:'I7',title:'是否多媒体项',width:120,sort:true}			
//			]]
//		})
//	})
	

	
	
	
	
	
	
	
	
	

