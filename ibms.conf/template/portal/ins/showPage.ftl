<script type="text/javascript">
	function show(newId,subject){
		top['index']._OpenWindow({
				url : __ctx+"/oaortal/insNews/get.do?permit=no&pkId=" + newId,
				title : subject,
				width : 850,
				height : 800,
			//max:true,
		});		
	}
</script>

div class="content mCustomScrollbar clearFix" id="content">
			<div class="bd-left fl">
					<div class="callbacks_container">
					  <ul class="rslides callbacks" id="slider">
					  
						<li>
							<a href="javascript:show('${item.newId}','${item.subject}')">
								<img src="${ctx}/oa/system/sysFile/preview.do?fileId=${item.imgFileId}" alt="${item.subject}">
								<p class="caption">${item.subject}</p>
							</a>
						</li>
						
			  	</ul>
		</div>
	</div>
</div>	