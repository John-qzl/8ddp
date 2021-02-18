function projectDel(){
		var Ids = "";

		var result = "";
		var __displayId__ = $.getParameter("__displayId__");
		var i = 1;
		var __pk__ = "";
		$('input[type="checkbox"][name=ID][class=pk x-input x-input-checkbox x-input-checkbox-pk]:checked').each(function() {
			if(i==1){
				Ids = $(this).val()
			}else{
				Ids += "," + $(this).val();
			}
			i+=1;
		});
		__pk__ = Ids;
		$.ajax({
			  type: "POST",
		      url:__ctx+"/dataPackage/tree/ptree/projectDelCheck.do",
			  data:{Ids : Ids},
		      async:false,
		      success:function(data){
		    	   result = data;
		      }   		  
		});
		if(result!="0"&&result!=""){
			$.ligerDialog.warn(result+"发次下存在数据包信息，请先删除该发次下数据包！");
		}else{
//			var __dbomJHMSValue__ = decodeURI(location.href.getNewUrl().split("__dbomJHMSValue__=")[1].split("&")[0]);
			var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__='+__displayId__+'&__pk__='+__pk__;
		//	var url = __ctx+'/oa/form/dataTemplate/deleteData.do?__displayId__=10000000340019';
			$.ligerDialog.confirm('确认删除吗？','提示信息',function(rtn) {
				if(rtn) {
					$.ajax({
						  type: "POST",
					      url:url,
					      async:false,
					      success:function(data){

					      }   		  
					});
					window.location.reload();
				}
			});
	}
	}