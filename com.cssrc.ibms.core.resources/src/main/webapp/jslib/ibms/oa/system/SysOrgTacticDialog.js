/**
 * 组织策略选择器(待开发)
 * @author YangBo 2016-7-23
 *
 */
function SysOrgTacticDialog(conf){
	var orgTactic = conf.orgTactic;
	if(orgTactic == 0){
		var json =null;
		OrgDialog({isSingle:true,arguments:json,callback:function(orgId,orgName){
			conf.callback.call(this,orgId,orgName);
		}});
	}else{
		//策略出来的组织
		DialogUtil.open({
	        height:600,
	        width: 1000,
	        title : '组织选择器',
	        url: __ctx+"/oa/system/sysOrgTactic/dialog.do", 
	        isResize: true,
	        //回调函数
	        sucCall:function(rtn){
	        	conf.callback.call(this,rtn.orgId,rtn.orgName);
	        }
	    });
	}
}