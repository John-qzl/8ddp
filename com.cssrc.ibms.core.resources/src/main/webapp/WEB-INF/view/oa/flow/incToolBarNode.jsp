<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="f" uri="http://www.cssrc.com.cn/functions"%>
<script type="text/javascript">
	function beforeClick(operatorType){
		<c:if test="${not empty mapButton.button}">
		  switch(operatorType){
		    <c:forEach items="${mapButton.button }" var="btn">
		      <c:if test="${not empty btn.prevscript}">
				case ${btn.operatortype}:
				  ${btn.prevscript}
				break;
			  </c:if>
			</c:forEach>
			}
		</c:if>
	}
	
	function afterClick(operatorType){
		<c:if test="${not empty mapButton.button}">
		  switch(operatorType){
		   <c:forEach items="${mapButton.button }" var="btn" >
		    <c:if test="${not empty btn.afterscript}">
				case ${btn.operatortype}:
					${btn.afterscript}
					break;
			</c:if>
		   </c:forEach>
		}
		</c:if>
	}
	function getParam(operatorType){
		<c:if test="${not empty mapButton.button}">
		 switch(operatorType){
			<c:forEach items="${mapButton.button }" var="btn" >
		 		case ${btn.operatortype}:
			   	<c:if test="${not empty btn.paramscript}">
					${btn.paramscript}
					break;
		       	</c:if>
		       	<c:if test="${not empty btn.paramscript}">
					return null;
					break;
	       		</c:if>
		    </c:forEach>
		}
		</c:if>
	}	
</script>
<div class="noprint">
	<div class="panel-toolbar">
		<div class="toolBar">
			<c:choose>
				<c:when test="${ isManage==0 }">
					<c:choose>
						<c:when test="${empty mapButton }">
							<c:if test="${isSignTask && isAllowDirectExecute}">
								<div class="group">
									特权：
									<input type="checkbox" value="1" id="chkDirectComplete">
									<label for="chkDirectComplete">
										
										直接结束
									</label>
								</div>
							</c:if>
							<div class="group">
								<a id="btnAgree" class="link agree">
									
									同意
								</a>
							</div>
							
							<c:if test="${isSignTask==true}">
								<div class="group">
									<a id="btnNotAgree" class="link notAgree">
										
										反对
									</a>
								</div>
								
								<div class="group">
									<a id="btnAbandon" class="link abandon">
										
										弃权
									</a>
								</div>
								
								<c:if test="${isAllowRetoactive==true}">
									<div class="group">
										<a class="link flowDesign" onclick="showAddSignWindow()">
											
											补签
										</a>
									</div>
									
								</c:if>
							</c:if>
							<div class="group">
								<a id="btnSave" class="link save">
									
									保存表单
								</a>
							</div>
							
							<c:if test="${isCanAssignee}">
								<div class="group">
									<a id="btnForward" class="link goForward " onclick="changeAssignee()">
										
										交办
									</a>
								</div>
								
							</c:if>
							<c:if test="${isCanBack}">
								<div class="group">
									<a id="btnReject" class="link reject">
										
										驳回
									</a>
								</div>
								
								<div class="group">
									<a id="btnRejectToStart" class="link rejectToStart">
										
										驳回到发起人
									</a>
								</div>
								
							</c:if>
							<div class="group">
								<a class="link setting" onclick="showTaskUserDlg('${processRun.actInstId}')">
									
									流程图
								</a>
							</div>
							
							<div class="group">
								<a class="link search" onclick="showTaskOpinions()">
									
									审批历史
								</a>
							</div>
							
							<div class="group">
								<a class="link sendMessage" onclick="showTaskCommunication('沟通')">
									
									沟通
								</a>
							</div>
							
							<c:if test="${isSignTask==false && task.description!='39'}">
								<div class="group">
									<a class="link switchuser" onclick="showTaskTransTo()">
										
										加签
									</a>
								</div>
								
							</c:if>
							<div class="group">
								<a class="link print" id="btnPrint" onclick="printReportFlow();">
									
									打印
								</a>
							</div>

							<c:if test="${isExtForm}">
								<c:choose>
									<c:when test="${!empty detailUrl && !empty form}">
										
										<div class="group">
											<a class="link edit" onclick="openForm('${form}')">
												
												编辑表单
											</a>
										</div>
									</c:when>
								</c:choose>
							</c:if>
						</c:when>
						<c:otherwise>
							<c:if test="${not empty mapButton.button}">
								<c:if test="${isSignTask && isAllowDirectExecute}">
									<div class="group">
										特权：
										<input type="checkbox" value="1" id="chkDirectComplete">
										<label for="chkDirectComplete">直接结束</label>
									</div>
								</c:if>
								<c:forEach items="${mapButton.button}" var="btn" varStatus="status">
									<c:choose>
										<c:when test="${btn.operatortype==1 }">
											<!--  同意-->
											<div class="group">
												<a id="btnAgree" class="link agree">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==2 }">
											<!-- 反对-->
											<div class="group">
												<a id="btnNotAgree" class="link notAgree">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==3 }">
											<!--弃权-->
											<c:if test="${isSignTask==true}">
												<div class="group">
													<a id="btnAbandon" class="link abandon">
														${btn.btnname }</a>
												</div>
												
											</c:if>
										</c:when>

										<c:when test="${btn.operatortype==4 }">
											<!--驳回-->
											<c:if test="${isCanBack}">
												<div class="group">
													<a id="btnReject" class="link reject">
														${btn.btnname }</a>
												</div>
												
											</c:if>
										</c:when>
										<c:when test="${btn.operatortype==5 }">
											<!--驳回到发起人-->
											<c:if test="${isCanBack && toBackNodeId!=task.taskDefinitionKey}">
												<div class="group">
													<a id="btnRejectToStart" class="link rejectToStart">
														${btn.btnname }</a>
												</div>
												
											</c:if>
										</c:when>
										<c:when test="${btn.operatortype==6 && isCanAssignee}">
											<!--交办-->
											<div class="group">
												<a id="btnForward" class="link goForward" onclick="changeAssignee()">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==7 }">
											<c:if test="${isSignTask==true}">
												<!--补签-->
												<c:if test="${isAllowRetoactive==true}">
													<div class="group">
														<a class="link flowDesign" onclick="showAddSignWindow()">
															${btn.btnname }</a>
													</div>
													
												</c:if>
											</c:if>
										</c:when>
										<c:when test="${btn.operatortype==8 }">
											<!--保存表单-->
											<div class="group">
												<a id="btnSave" class="link save">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==9 }">
											<!--流程图-->
											<div class="group">
												<a class="link setting" onclick="showTaskUserDlg('${processRun.actInstId}')">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==10 }">
											<!--打印-->
											<div class="group">
												<a class="link print" onclick="printReportFlow('${btn.operatortype}');">
													${btn.btnname}</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==11 }">
											<!--审批历史-->
											<div class="group">
												<a class="link history" onclick="showTaskOpinions()">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==14 }">
											<!--Web签章-->
											<div class="group">
												<a class="link addWebSigns" onclick="addWebSigns()">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==15 }">
											<!--手写签章-->
											<div class="group">
												<a class="link addHangSigns" onclick="addHangSigns()">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==20 }">
											<!--ibms签章-->
											<div class="group">
												<a class="link addWebSigns ibmsSign">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==16 }">
											<!--沟通-->
											<div class="group">
												<a class="link sendMessage" onclick="showTaskCommunication('${btn.btnname }')">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==17 && task.description!='39'}">
											<!--加签-->
											<div class="group">
												<a class="link switchuser" onclick="showTaskTransTo()">
													${btn.btnname }</a>
											</div>
											
										</c:when>
										<c:when test="${btn.operatortype==18 }">
											<!--终止-->
											<div class="group">
												<f:a alias="endProcess" css="link abandon" id="btnEnd" href="####">
													${btn.btnname }</f:a>
											</div>
											
										</c:when>
									</c:choose>
								</c:forEach>
							</c:if>
							<c:if test="${isExtForm}">
								<c:choose>
									<c:when test="${!empty detailUrl && !empty form}">
										
										<div class="group">
											<a class="link edit" onclick="openForm('${form}')">
												
												编辑表单
											</a>
										</div>
									</c:when>
								</c:choose>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:when>
				<c:otherwise>
					<c:if test="${isSignTask && isAllowDirectExecute}">
						<div class="group">
							特权：
							<input type="checkbox" value="1" id="chkDirectComplete">
							<label for="chkDirectComplete">直接结束</label>
						</div>
					</c:if>
					<div class="group">
						<a id="btnAgree" class="link agree">
							
							同意
						</a>
					</div>
					
					<c:if test="${isSignTask==true}">
						<div class="group">
							<a id="btnNotAgree" class="link notAgree">
								
								反对
							</a>
						</div>
						
						<div class="group">
							<a id="btnAbandon" class="link abandon">
								
								弃权
							</a>
						</div>
						
						<c:if test="${isAllowRetoactive==true}">
							<div class="group">
								<a class="link flowDesign" onclick="showAddSignWindow()">
									
									补签
								</a>
							</div>
							
						</c:if>
					</c:if>
					<div class="group">
						<a id="btnSave" class="link save">
							
							保存表单
						</a>
					</div>
					
					<c:if test="${isCanBack}">
						<div class="group">
							<a id="btnReject" class="link reject">
								
								驳回
							</a>
						</div>
						
						<div class="group">
							<a id="btnRejectToStart" class="link rejectToStart">
								
								驳回到发起人
							</a>
						</div>
						
					</c:if>
					<div class="group">
						<f:a alias="endProcess" css="link abandon" id="btnEnd" href="####">
							终止</f:a>
					</div>
					

					<div class="group">
						<a class="link setting" onclick="showTaskUserDlg('${processRun.actInstId}')">
							
							流程图
						</a>
					</div>
					
					<div class="group">
						<a class="link search" onclick="showTaskOpinions()">
							
							审批历史
						</a>
					</div>
					

					<div class="group">
						<a class="link print" onclick="printReportFlow();">
							
							打印
						</a>
					</div>
					

					<div class="group">
						<a class="link sendMessage" onclick="showTaskCommunication('沟通');">
							
							沟通
						</a>
					</div>
					
					<c:if test="${isSignTask==false && task.description!='39'}">
						<div class="group">
							<a class="link switchuser" onclick="showTaskTransTo()">
								
								加签
							</a>
						</div>
						
					</c:if>
					<!--Web签章-->
					<div class="group">
						<a class="link addWebSigns" onclick="addWebSigns()">
							
							Web签章
						</a>
					</div>
					
					<!--手写签章-->
					<div class="group">
						<a class="link addHangSigns" onclick="addHangSigns()">
							
							手写签章
						</a>
					</div>
					

					<c:if test="${isExtForm}">
						<c:choose>
							<c:when test="${!empty detailUrl && !empty form}">
								
								<div class="group">
									<a class="link edit" onclick="openForm('${form}')">编辑表单</a>
								</div>
							</c:when>
						</c:choose>
					</c:if>
				</c:otherwise>
			</c:choose>
			<c:choose>
				<c:when test="${ isManage==0 }">
					<c:if test="${fn:indexOf(bpmNodeSet.jumpType,'1')!=-1}">
						<span style="height: 30px; line-height: 30px;">
							<input type="radio" <c:if test="${!isHandChoolse}"> checked='checked' </c:if> name="jumpType" onclick="chooseJumpType(1)" value="1" />
							&nbsp;正常跳转
						</span>
					</c:if>
					<c:if test="${fn:indexOf(bpmNodeSet.jumpType,'2')!=-1}">
						<span style="height: 30px; line-height: 30px;">
							<input type="radio" <c:if test="${isHandChoolse}"> checked='checked' </c:if> name="jumpType" onclick="chooseJumpType(2)" value="2" />
							&nbsp;选择路径跳转
						</span>
					</c:if>
					<c:if test="${fn:indexOf(bpmNodeSet.jumpType,'3')!=-1}">
						<span style="height: 30px; line-height: 30px;">
							<input type="radio" name="jumpType" onclick="chooseJumpType(3)" value="3" />
							&nbsp;自由跳转
						</span>
					</c:if>
				</c:when>
				<c:otherwise>
					<span style="height: 30px; line-height: 30px;">
						<input type="radio" <c:if test="${!isHandChoolse}"> checked='checked' </c:if> name="jumpType" onclick="chooseJumpType(1)" value="1" />
						&nbsp;正常跳转
					</span>
					<span style="height: 30px; line-height: 30px;">
						<input type="radio" <c:if test="${isHandChoolse}"> checked='checked' </c:if> name="jumpType" onclick="chooseJumpType(2)" value="2" />
						&nbsp;选择路径跳转
					</span>
					<span style="height: 30px; line-height: 30px;">
						<input type="radio" name="jumpType" onclick="chooseJumpType(3)" value="3" />
						&nbsp;自由跳转
					</span>
				</c:otherwise>
			</c:choose>
			<c:if test="${bpmDefinition.allowRefer==1}">
				<!-- 流程参考 -->
				<div class="group">
					<a id="btnReference" class="link reference" onclick="reference()">
						
						流程参考
					</a>
				</div>
			</c:if>

			<c:if test="${bpmNodeSet.jumpType=='4'}">
				<%--自定义跳转节点路径选择器 --%>
				<div class="group row" id="customDestTaskdiv">
					<select id="customDestTask" name="destTask" class="selectSearch" onchange="changeCustomDestTask(this)">
						<option value="">请选择...</option>
						<c:forEach items="${customJumpNodeMap}" var="item">
							<option value="${item.id}.${item.callActivity}" prentFlowKey="${item.prentFlowKey}" opinion="${item.opinion}">${item.jumpName}</option>
						</c:forEach>
					</select>
				</div>
				<%--自定义跳转节点人员选择器 --%>
				<div class="group" id="jumpUserLink">
					<input type="hidden" id="lastDestTaskId" name="lastDestTaskId" value="">
					<span id="jumpUserDiv"></span>
					<a href="javascript:;"  class="link grant" onclick="selectExeUsers(this)">选择..</a>
				</div>
			</c:if>
			<c:if test="${superInstanceId!=null}">
				<div class="group">
					<a href="javascript:;"  class="link superform" onclick="superform('${superInstanceId}')">查看主表单</a>
				</div>
			</c:if>
			<c:if test="${bpmDefinition.attachment!=''}">
				<%@include file="incHelp.jsp"%>
			</c:if>
		</div>
	</div>

</div>