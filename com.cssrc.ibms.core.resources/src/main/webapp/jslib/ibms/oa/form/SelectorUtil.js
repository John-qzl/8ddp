/**
*选择器帮助类
*/
var SelectorUtil = function() {

};
// 属性及函数
SelectorUtil.prototype = {
	/**
	 * 选择用户
	 * @param {} conf 
	 * 				{self:this //这个是必须的
	 * 				  isSingle：true或者false 
	 * 				}
	 */
	selectUser:function(conf){
		var me= $(conf.self),
			isSingle = typeof conf.isSingle != 'undefined'?conf.isSingle:true;
			if(me.attr("type")=="button"){
				objNames = me.prev();
			}else{
				objNames = me;
			}
			objIds= me.siblings("[type='hidden']");
		
		UserDialog({
			selectUserIds:objIds.val(),
			selectUserNames:objNames.val(),
			isSingle:isSingle,
			callback:function(ids,names){
				objIds.val(ids);
				objNames.val(names);
			}});	
	},
	/**
	 * 选择组织
	 * @param {} conf 
	 * 				{self:this //这个是必须的
	 * 				  isSingle：true或者false 
	 * 				}
	 */
	selectOrg:function(conf){
		var me= $(conf.self),
			isSingle = typeof conf.isSingle != 'undefined'?conf.isSingle:true;
		if(me.attr("type")=="button"){
			objNames = me.prev();
		}else{
			objNames = me;
		}
		objIds= me.siblings("[type='hidden']");
		
		OrgDialog({
			isSingle:isSingle,
			callback:function(ids,names){
				objIds.val(ids);
				objNames.val(names);
		}});	
	},
	/**
	 * 选择角色
	 * @param {} conf 
	 * 				{self:this //这个是必须的
	 * 				  isSingle：true或者false 
	 * 				}
	 */
	selectRole:function(conf){
		var me= $(conf.self),
			isSingle = typeof conf.isSingle != 'undefined' ?conf.isSingle:true;
		if(me.attr("type")=="button"){
			objNames = me.prev();
		}else{
			objNames = me;
		}
		objIds= me.siblings("[type='hidden']");
		
		RoleDialog({
			isSingle:isSingle,
			callback:function(ids,names){
				objIds.val(ids);
				objNames.val(names);
		}});	
	},
	/**
	 * 选择岗位 实现在 SysDialog.js
	 * @param {} conf 
	 * 				{self:this //这个是必须的
	 * 				  isSingle：true或者false 
	 * 				}
	 */
	selectPos:function(conf){
		var me= $(conf.self),
			isSingle = typeof conf.isSingle != 'undefined'?conf.isSingle:true;
		if(me.attr("type")=="button"){
			objNames = me.prev();
		}else{
			objNames = me;
		}
		objIds= me.siblings("[type='hidden']");
		
		PosDialog({
			isSingle:isSingle,
			callback:function(ids,names){
				objIds.val(ids);
				objNames.val(names);
		}});	
	}
};


var __Selector__ = new SelectorUtil();// 默认生成一个对象