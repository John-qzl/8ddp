
$(function(){
	
	if($("#slider").length>0){
		$("#slider").responsiveSlides({
			pager: false,
			nav: true,
			speed: 500,
			namespace: "callbacks"
		});
	};
	
	$(window).resize(function(){
		$(".content").height(document.documentElement.clientHeight - 10);
	});
		
	if($(".content").length>0){
		$(".content").height(document.documentElement.clientHeight - 10)
		 .rollbar({
		 		scroll:'vertical', 
		 		pathPadding : 12,
		 		zIndex:100
		 });
	};
	
	if($("#rcbg").length>0){
		$("#rcbg").height(328)
		   .rollbar({
		   		sliderSize: '45%',
	 			scroll:'vertical', 
	 			pathPadding : 12,
	 			zIndex:120
			});
	}
	
	if($(".task-list").length>0){
		$(".task-list").height(document.documentElement.clientHeight - 100)
		 .rollbar({
		 		scroll:'vertical', 
		 		pathPadding : 12,
		 		zIndex:100
		 });
	};


	$(".history-list li:odd").addClass("odd");
	
	$(".history-list li:even").addClass("even");
});
	
