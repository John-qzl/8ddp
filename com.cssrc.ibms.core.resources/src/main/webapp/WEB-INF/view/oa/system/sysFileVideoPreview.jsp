<!DOCTYPE html>
<html>
<head>
<%@include file="/commons/include/get.jsp"%>
<title>视频播放</title>
<link href="${ctx}/css/system/css/video-js.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="${ctx}/jslib/ibms/video.js"></script>
<script type="text/javascript">
	videojs.options.flash.swf="${ctx}/jslib/ibms/video-js.swf";
</script>
</head>
<body>
<script type="text/javascript">
  $(function() {
	 var width=$(window).width();
	 var height=$(window).height();
	 $("#example_video_2").css("width",width);
	 $("#example_video_2").css("height",height);
	 /*  var myPlayer1=videojs('example_video_2',{},{
		  	//var myPlayer1=this;
			//myPlayer1.play();
	  });  */
	videojs("example_video_1").ready(function(){
			var myPlayer1=this;
			myPlayer1.play();
	}); 
  });	
</script>

	<video id="example_video_2" class="video-js vjs-default-skin" controls="controls" preload="none"> 
		${videoSource}
	</video>
	<script type="text/javascript">
  </script>
</body>
</html>
