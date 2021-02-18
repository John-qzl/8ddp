国际化说明
	<!-- 通用国际化-->
	<f:js pre="jslib/lang/common" ></f:js>
	<!-- 引用的js国际化-->
	<f:js pre="jslib/lang/js" ></f:js>	
	<!-- 模块国际化-->
	<f:js pre="jslib/lang/view/oa/bpm" ></f:js>

|—common    通用  以$lang定义    使用$lang.tipMsg
|—js		js  以$lang_js定义     使用 $lang_js.模块key.文件名key.方法名.具体内容key
|-view
	|-oa
		|-bpm 页面模块 以$lang_bpm定义  使用 $lang_模块key.小模块key.方法名.具体内容key