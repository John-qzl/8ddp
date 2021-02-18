<div class="bd-left fl clear">
		<div class="block-II">
			<div class="basic-tabCont-wrap clearFix">
				<div id="rcbg">
					<#if result?exists> 
						<ul class="cubeBox">
							<#list result as item>
							
								<li class="fl box-col">
									<a href="###" class="bgef7373"> <!--class内为颜色内容，可以参考home.css767行，也可自行加上background -->
										<div class="cubeBox-icon cubeBox-jp"></div>
										<div class="cubeBox-text">${item.name}</div>
									</a>
								</li>
						
							
							</#list>
						</ul>
					</#if>
				</div>
			</div>
		</div>
</div>