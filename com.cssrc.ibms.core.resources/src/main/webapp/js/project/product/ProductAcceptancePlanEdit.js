/**
 * 产品验收策划新增/编辑JS
 */
$(function() {
	// 操作类型
	var handleType = $.getParameter("handleType");
	
	// 产品批次id
	var batchId = $.getParameter("batchId");
	// 原为先新增后启动，现改为直接发起流程审批
	if (handleType == "add" || handleType == "start") {	
		if (handleType == "add") {
			$("input[name='m:cpyschbgb:spzt']").attr("value", "未审批");
		}else if(handleType == "start"){
			debugger;
			$("input[name='m:cpyschbgb:gdlczt']").attr("value", "未归档");
			$("input[name='m:cpyschbgb:spzt']").attr("value", "审批中");
			
		}
		// 需要主管领导审批--外地产品（默认否）
		var wdcp = $("input:radio[name='m:cpyschbgb:sfwdcp']:checked").val();
		if (wdcp == undefined || wdcp == null) {
			$("input:radio[name='m:cpyschbgb:sfwdcp']").each(function() {
				if ($(this).val() == '否') {
					$(this).attr("checked", "true");
				}
			})
		}
		
		// 不显示"主管部门领导"
		$("#managerTr").attr("style","display:none");
		$("#acceptanceGroup").attr("style","display:none");
		// 页面初始化
		$.ajax({
			url : __ctx
					+ "/product/category/batch/getBatchAndModule.do?batchId="
					+ batchId,
			async : false,
			success : function(data) {
				if (data.success) {
					debugger;
					var date = new Date();
					var year = date.getFullYear();
					
					var moudle=data.MODULEKEY+"-YSCH-"+year+"-"+data.Number; 
					$("input[name='m:cpyschbgb:chbgbbh']").attr("value",moudle);
					// 1.型号信息
					$("input[name='m:cpyschbgb:ssxh']").attr("value",
							data.MODULENAME);
					$("input[name='m:cpyschbgb:ssxhID']").attr("value",
							data.MODULEID);
					// 2.产品类别基本信息
					$("input[name='m:cpyschbgb:cpmc']").attr("value",
							data.F_CPMC);
					$("input[name='m:cpyschbgb:cpdh']").attr("value",
							data.F_CPDH);
					$("input[name='m:cpyschbgb:yzdw']").attr("value",
							data.F_YZDW);
					$("input[name='m:cpyschbgb:yzdwID']").attr("value",
							data.F_YZDWID);
					// 产品批次id
					$("input[name='m:cpyschbgb:sscppc']")
							.attr("value", data.ID);
					$("input[name='m:cpyschbgb:cppc']").attr("value",
							data.F_PCH);
					$("input[name='m:cpyschbgb:jsfzr']").attr("value",
							curUserName);
					$("input[name='m:cpyschbgb:jsfzrId']").attr("value",
							curUserId);
					
				}
			}
		})
	}


	// 流程其他节点
	var managerTrCheck = $("input:radio[name='m:cpyschbgb:sfwdcp']:checked").val();
	if (managerTrCheck == '否') {
		// 不显示"主管部门领导"
		$("#managerTr").attr("style","display:none");
	}
	
	var curPageUrl = window.location.href;
	if(curPageUrl.indexOf('editData.do?__displayId__')>0||curPageUrl.indexOf('startFlowForm.do?__displayId__')>0){
		/*$("#acceptanceGroup").hide();*/
	}
	
//	$(document).on("input propertychange","#acceptanceGroup .input-userPicker", function (e) {
//	    debugger;
//    });
	 $(document).on('change','#childTable .hidden',function(e){
		 debugger;
         //获取input输入的值
		 var userId = $(this).val();
		 var parentElements=this.parentNode.parentNode;
		 var brotherElements=parentElements.nextElementSibling;
		 var url= __ctx+"/dataPackage/tree/ptree/getOrgByUserId.do";
		 $.ajax({
			url:url,
			data:{
				'userId':userId
			}, 
			type:'post',
			async : false,
			success : function(data) {
				if(data!=null&&data!=""){
					var s1=brotherElements.firstChild;
					var s2=s1.firstChild;
					s2.value=data.orgId;
					var s3=s2.nextElementSibling;
					s3.value=data.orgName;
				}
			}
			 
		 });
     });
	 	
/*	$('input.user').on('input',function(e){
	     alert('Changed!')
	});*/
})

// 是否外地产品
function isOutProduct(){
	var wdcp = $("input:radio[name='m:cpyschbgb:sfwdcp']:checked").val();
	if (wdcp == '是') {
		// 显示"主管部门领导"
		$("#managerTr").removeAttr("style");
		// 主管部门领导必填
		$("input[name='m:cpyschbgb:zgbmld']").attr("validate","{empty:false,required:true}");
	}else{
		// 不显示"主管部门领导"
		$("#managerTr").attr("style","display:none");
		// 去除必填--后续优化隐藏
		$("input[name='m:cpyschbgb:zgbmld']").attr("validate","{empty:false}");
	}
}
