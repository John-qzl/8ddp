var FlowTask=function(){
	var _conf = {
		taskId : '',
		taskDefinitionKey : '',
		isExtForm : '',
		isEmptyForm : '',
		isSignTask : '',
		isHidePath : '',
		isManage : '',
		isNeedSubmitConfirm : '',
		isHandChoolse : '',
		creatorId : '',
		bpmGangedSets : [],
		curUserId : '',
		curUserName : '',
		curDate : '',
		curDateTime : '',
		processRunJson : {},
		processRunMap : {},
		variablesJson : {},
		variablesMap : {},
		operatorType : {{"type":1,"id":"btnPrint","des":"执行任务成功"},{}}
	}
	
	
	
	this.initBtnEvent=function(){
		// 0，弃权，1，同意，2反对。
		var objVoteAgree=$("#voteAgree");
		var objBack=$("#back");
		// 同意
		if($("#btnAgree").length>0){
				$("#btnAgree").click(function(){
					$.ligerDialog.confirm("确认同意？","提示",function(rtn){
						if(rtn){
							if(preAgree()){
								setExtFormData();
								var isDirectComlete = getIsDirectComplete();
								operatorType=1;
								// 同意:5，一票通过。
								var tmp =isDirectComlete?"5":"1";
								objVoteAgree.val(tmp);
								objBack.val("0");
								completeTask();
							}
						}
					});
				});
			
		}
		// 反对
		if($("#btnNotAgree").length>0){
			$("#btnNotAgree").click(function(){
				$.ligerDialog.confirm("确认反对？","提示",function(rtn){
					if(rtn){
						setExtFormData();
						var isDirectComlete = getIsDirectComplete();
						operatorType=2;
						// //直接一票通过
						var tmp =isDirectComlete?'6':'2';
						objVoteAgree.val(tmp);
						objBack.val("0");
						completeTask();
					}
				});
			});
		}
		// 弃权
		if($("#btnAbandon").length>0){
			$("#btnAbandon").click(function(){
				$.ligerDialog.confirm("确认弃权？","提示",function(rtn){
					if(rtn){
						setExtFormData();
						operatorType=3;
						objVoteAgree.val(0);
						objBack.val("");
						completeTask();
					}
				});
			})
		}
		// 驳回
		if($("#btnReject").length>0){
			$("#btnReject").click(function(){
				var voteContent = $('#voteContent'),
				content = voteContent.val();
				if(voteContent.length==0||(content && content.trim()!='')){
					setExtFormData();
					operatorType=4;
					$.ligerDialog.confirm("确认驳回？","提示",function(rtn){
						if(rtn){
							objVoteAgree.val(3);
							objBack.val(1);
							completeTask();
						}
					});
				}else{
					$.ligerDialog.warn("请填写驳回意见","提示信息");
				}
			})
		}
		// 驳回到发起人
		if($("#btnRejectToStart").length>0){
			$("#btnRejectToStart").click(function(){
			var voteContent = $('#voteContent'),
			content = voteContent.val();
			if(voteContent.length==0||(content && content.trim()!='')){
				setExtFormData();
				
				operatorType=5;
				// 驳回到发起人
				$.ligerDialog.confirm("确认驳回到发起人？","提示",function(rtn){
					if(rtn){
						objVoteAgree.val(3);
						objBack.val(2);
						completeTask();
					}
				});
			}else{
				$.ligerDialog.warn("请填写驳回意见","提示信息");
			}
			})
		}
		// 保存表单
		if($("#btnSave").length>0){
			$("#btnSave").click(function(){
				// 保存表单前的预处理
				preSaveHandler();
				// 提交第三方表单时检查该表单的参数
				setExtFormData();
				operatorType=8;
				saveData();
				//postSaveHandler();
			})
		}
		
		// 终止流程
		$("#btnEnd").click(function(){
			operatorType=18;
			isTaskEnd(endTask);
		});		
		
	}
}