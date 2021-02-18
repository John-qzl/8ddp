/**
 * IframeSwitch：用于切换当前的window对象，document对象
 * 
 * 使用帮助： 
 * 1.src ： url中能够唯一确定url的部分;
 * 	   eg : url = /ibms/dataPackage/tree/dataPackage/main.do?&id=10000000580066,那么 src可以是 tree/dataPackage/main.do
 * 2.getWindowBySrc(): 可以获取src对应的window，可以取里面已经存在的变量
 * 	 var swi = new IframeSwitch('/dataPackage/tree/projectTree/manage.do');
 * 	 var windowss = swi.getWindowBySrc();
 * 	 var tree = windowss.sTree.glTypeTree;
 * 3.getDocBySrc(): 可以获取src对应的document，可以获取页面上任何元素信息
 * 	 var swi = new IframeSwitch('/dataPackage/tree/projectTree/manage.do');
 * 	 var doc = swi.getDocBySrc();
 * 	 var button = $('.add',doc);
 */
var IframeSwitch = function(src){
	this.src = src;
}

IframeSwitch.prototype.isContain = function(frameEle){
	var srcUrl = frameEle.src;
	if(srcUrl.indexOf(this.src)>-1){
		return true;
	}else{
		return false;
	}		
}
/**
 * 可以获取src对应的window，可以取里面已经存在的变量
 */
IframeSwitch.prototype.getWindowBySrc = function(src){
	var windowxx=null;var me = this;
	var frames = top.frames;
	for(var s=0;s<frames.length;s++){
		var cur_window = frames[s];
		var frameEle = cur_window.frameElement;
		if(me.isContain(frameEle)){
			windowxx = cur_window;
			return false;
		}
		var frames2 = cur_window.frames;
		for(var i=0;i<frames2.length;i++){
			var cur_window2 = frames2[i];
			var frameEle2 = cur_window2.frameElement;
			if(me.isContain(frameEle2)){
				windowxx = cur_window2;
				break;
			}
		}
		if(windowxx!=null){
			return false;
		}
	}
	/*$.each(top.frames,function(index){
		var cur_window = $(this).get(0);
		var frameEle = cur_window.frameElement;
		if(me.isContain(frameEle)){
			windowxx = cur_window;
			return false;
		}
		var frames2 = cur_window.frames;
		for(var i=0;i<frames2.length;i++){
			var cur_window2 = frames2[i];
			var frameEle2 = cur_window2.frameElement;
			if(me.isContain(frameEle2)){
				windowxx = cur_window2;
				break;
			}
		}
		if(windowxx!=null){
			return false;
		}
	})*/
	if(windowxx==null){
		console.log("没有找到"+src+"对应的窗口！")
	}
	return windowxx;
}
/**
 * 可以获取src对应的document，可以获取页面上任何元素信息
 */
IframeSwitch.prototype.getDocBySrc = function(src){
	var doc=null;var me = this;
	var cur_window = me.getWindowBySrc(src);
	if(cur_window){
		var frameEle = cur_window.frameElement;
		doc = $(frameEle).contents();
	}
	return doc;
}