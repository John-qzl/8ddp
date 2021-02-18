<#setting number_format="0">
<div class="panel-page l-bar-text">		
	<div class="l-panel-bbar-inner">
		<#if (showPageSize)>
			<label class="fl">
				<div class="fl l-bar-text">每页记录&nbsp;</div>
				<div class="l-bar-group l-bar-selectpagesize">
					<select id="pageSize" name="pageSize" onchange="changePageSize(this,'${tableIdCode}');" class="select_short">
						<option value="5" <#if pageBean.pageSize==5> selected="selected" </#if>>5</option>
						<option value="10" <#if pageBean.pageSize==10> selected="selected" </#if>>10</option>
						<option value="15" <#if pageBean.pageSize==15> selected="selected" </#if>>15</option>
						<option value="20" <#if pageBean.pageSize==20> selected="selected" </#if>>20</option>
						<option value="25" <#if pageBean.pageSize==25> selected="selected" </#if>>25</option>
						<option value="50" <#if pageBean.pageSize==50> selected="selected" </#if>>50</option>
						<option value="100" <#if pageBean.pageSize==100> selected="selected" </#if>>100</option>
					</select>
				</div>
			</label>
		</#if>
		
		<div class="fr">
			<#if (showExplain) >
				<div class="fl" >
					<span class="l-bar-text">共 ${pageBean.totalCount} 条</span>
				</div>
			</#if>
			
			<!--*刷新*-->
			<!--<div class="l-bar-group">
				<div class="l-bar-button l-bar-btnload">
					<a href="javascript:window.location.reload()">
						<span class=""></span>
					</a>
				</div>
			</div>-->
			
			<div class="l-bar-group-right">
				<div class="fl">
					<div class="l-bar-button l-bar-btnfirst">
						<#if pageBean.currentPage == 1>
							<button href="javascript:;" onclick="first('${tableIdCode}')" disabled="disabled" title="首页">
								<span>首页</span>
							</button> 
						<#else>
							<button href="javascript:;" onclick="first('${tableIdCode}')" title="首页">
								<span>首页</span>
							</button> 
						</#if>
					</div>
				</div>	
				<div class="fl">	
					<div class="l-bar-button l-bar-btnprev">
						<#if pageBean.currentPage == 1>
							<button href="javascript:;" onclick="previous('${tableIdCode}');" disabled="disabled" title="上一页">
								<span>上一页</span>
							</button>
						<#else>
							<button href="javascript:;" onclick="previous('${tableIdCode}');" title="上一页">
								<span>上一页</span>
							</button>
						</#if>
					</div>
				</div>
				
				<div class="fl">
					<div class="l-bar-button l-bar-btnnext">
						<#if pageBean.currentPage == pageBean.totalPage>
							<button href="javascript:;" onclick="next('${tableIdCode}')" disabled="disabled" title="下一页">
								<span>下一页</span>
							</button>
						<#else>
							<button href="javascript:;" onclick="next('${tableIdCode}')" title="下一页">
								<span>下一页</span>
							</button>
						</#if>
					</div> 
				</div>
				<div class="fl">
					<div class="l-bar-button l-bar-btnlast">
						<#if pageBean.currentPage == pageBean.totalPage>
							<button href="javascript:;" onclick="last('${tableIdCode}')" disabled="disabled" title="尾页">
								<span>尾页</span>
							</button>
						<#else>
							<button href="javascript:;" onclick="last('${tableIdCode}')"  title="尾页">
								<span>尾页</span>
							</button>
						</#if>
					</div> 	
				</div>
				<div class="fl">
					<span class="pcontrol"> 
						<input size="4" value="${pageBean.currentPage}" maxlength="3" class="inputText pageInput" type="text" tableidcode="${tableIdCode}" id="navNum${tableIdCode}" name="navNum"/><span class="page-line">/</span><span class="page-total-num">${pageBean.totalPage}</span>
					</span>
				</div>
				<div class="fl">
					<input type="button" id="btnGo" class="btn-go" value="确定" onclick="jumpTo('${tableIdCode}');"/>
				</div>
			</div>
		</div>
		<div class="l-clear"></div>
			<input type="hidden" id="currentPage${tableIdCode}" name="currentPage" value="${pageBean.currentPage}"/>
			<input type="hidden" id="totalPage${tableIdCode}" name="totalPage" value="${pageBean.totalPage}"/>
			<input type="hidden" id="oldPageSize${tableIdCode}" name="oldPageSize" value="${pageBean.pageSize}"/>
			<a id="_nav${tableIdCode}" href='${baseHref}' style="display:none" ></a>
	</div>
</div>
