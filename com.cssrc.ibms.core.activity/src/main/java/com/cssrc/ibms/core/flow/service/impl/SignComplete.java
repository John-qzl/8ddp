package com.cssrc.ibms.core.flow.service.impl;

import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.activiti.engine.impl.pvm.delegate.ActivityExecution;
import org.activiti.engine.repository.ProcessDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cssrc.ibms.api.activity.intf.IBpmService;
import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.activity.model.ProcessCmd;
import com.cssrc.ibms.core.constant.activity.BpmConst;
import com.cssrc.ibms.core.engine.ISignComplete;
import com.cssrc.ibms.core.flow.dao.ProStatusDao;
import com.cssrc.ibms.core.flow.model.NodeSign;
import com.cssrc.ibms.core.flow.model.TaskOpinion;
import com.cssrc.ibms.core.flow.service.NodeSignService;
import com.cssrc.ibms.core.flow.service.TaskOpinionService;
import com.cssrc.ibms.core.flow.service.TaskSignDataService;
import com.cssrc.ibms.core.flow.service.thread.TaskThreadService;

public class SignComplete implements ISignComplete {
	private Logger logger = LoggerFactory.getLogger(SignComplete.class);

	@Resource
	private IBpmService bpmService;

	@Resource
	private TaskSignDataService taskSignDataService;

	@Resource
	private NodeSignService nodeSignService;

	@Resource
	private ProStatusDao proStatusDao;

	@Resource
	private TaskOpinionService taskOpinionService;

	// private CalendarAssignService calendarAssignService;

	public boolean isComplete(ActivityExecution execution) {
		this.logger.debug("entert the SignComplete isComplete method...");

		String nodeId = execution.getActivity().getId();
		String actInstId = execution.getProcessInstanceId();

		boolean isCompleted = false;
		String signResult = "";
		ProcessDefinition processDefinition = this.bpmService
				.getProcessDefinitionByProcessInanceId(actInstId);

		NodeSign bpmNodeSign = this.nodeSignService.getByDefIdAndNodeId(
				processDefinition.getId(), nodeId);

		Integer completeCounter = (Integer) execution
				.getVariable("nrOfCompletedInstances");

		Integer instanceOfNumbers = (Integer) execution
				.getVariable("nrOfInstances");

		Short approvalStatus = Short.valueOf(TaskThreadService.getProcessCmd()
				.getVoteAgree().shortValue());

		Long orgId = Long.valueOf(0L);

		ISysOrg curOrg = (ISysOrg)UserContextUtil.getCurrentOrg();

		if (curOrg != null) {
			orgId = curOrg.getOrgId();
		}

		ProcessCmd processCmd = TaskThreadService.getProcessCmd();

		if ((BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()))
				|| (BpmConst.TASK_BACK.equals(processCmd.isBack()))) {
			isCompleted = true;
		} else if ((approvalStatus.shortValue() == 5)
				|| (approvalStatus.shortValue() == 6)) {
			isCompleted = true;
			if (approvalStatus.shortValue() == 5)
				signResult = "pass";
			else {
				signResult = "refuse";
			}
		} else {
			boolean isOneVote = this.nodeSignService.checkNodeSignPrivilege(
					processDefinition.getId(), nodeId,
					NodeSignService.NodePrivilegeType.ALLOW_ONE_VOTE,
					UserContextUtil.getCurrentUserId(), orgId);
			if ((isOneVote)
					&& (!execution.hasVariable("resultOfSign_" + nodeId))) {
				execution.setVariable("resultOfSign_" + nodeId, approvalStatus);
			}

			Short oneVoteResult = null;
			if (execution.hasVariable("resultOfSign_" + nodeId)) {
				oneVoteResult = (Short) execution.getVariable("resultOfSign_"
						+ nodeId);
			}

			VoteResult voteResult = calcResult(bpmNodeSign, actInstId, nodeId,
					completeCounter, instanceOfNumbers, oneVoteResult);

			signResult = voteResult.getSignResult();
			isCompleted = voteResult.getIsComplete();
		}

		if (isCompleted) {
			this.taskSignDataService.batchUpdateCompleted(actInstId, nodeId);

			Short status = null;
			if ((BpmConst.TASK_BACK_TOSTART.equals(processCmd.isBack()))
					|| (BpmConst.TASK_BACK.equals(processCmd.isBack()))) {
				status = processCmd.getVoteAgree();
				if ((TaskOpinion.STATUS_RECOVER_TOSTART.equals(status))
						|| (TaskOpinion.STATUS_RECOVER.equals(status)))
					signResult = "recover";
				else if (TaskOpinion.STATUS_REJECT_TOSTART.equals(status))
					signResult = "rejectToStart";
				else if (TaskOpinion.STATUS_REJECT.equals(status))
					signResult = "reject";
				else
					signResult = "UNKNOW_ACTION";
			} else {
				status = TaskOpinion.STATUS_PASSED;
				if (signResult.equals("refuse")) {
					status = TaskOpinion.STATUS_NOT_PASSED;
				}
			}
			this.logger.debug("set the sign result + " + signResult);

			execution.setVariable("signResult_" + nodeId, signResult);
			String resultSign = "resultOfSign_" + nodeId;
			if (execution.hasVariable(resultSign)) {
				execution.removeVariable(resultSign);
			}

			this.proStatusDao.updStatus(new Long(actInstId), nodeId, status);

			updOption(execution, status);

			String multiInstance = (String) execution.getActivity()
					.getProperty("multiInstance");

			if ("sequential".equals(multiInstance)) {
				String varName = nodeId + "_" + "signUsers";
				execution.removeVariable(varName);
			}

		}

		return isCompleted;
	}

	private void updOption(ActivityExecution execution, Short signStatus) {
		String multiInstance = (String) execution.getActivity().getProperty(
				"multiInstance");
		String nodeId = execution.getCurrentActivityId();
		String actInstId = execution.getProcessInstanceId();
		if (!"parallel".equals(multiInstance))
			return;

		Short status = getStatus(signStatus);

		List<TaskOpinion> list = this.taskOpinionService
				.getByActInstIdTaskKeyStatus(actInstId, nodeId,
						TaskOpinion.STATUS_CHECKING);
		for (TaskOpinion taskOpinion : list) {
			taskOpinion.setCheckStatus(status);
			taskOpinion.setEndTime(new Date());
			// Long duration =
			// this.calendarAssignService.getRealWorkTime(taskOpinion.getStartTime(),
			// taskOpinion.getEndTime(), taskOpinion.getExeUserId());
			Long duration = taskOpinion.getEndTime().getTime()
					- taskOpinion.getStartTime().getTime();
			taskOpinion.setDurTime(duration);
			this.taskOpinionService.update(taskOpinion);
		}
	}

	private Short getStatus(Short signResult) {
		ProcessCmd cmd = TaskThreadService.getProcessCmd();
		Short status = TaskOpinion.STATUS_PASS_CANCEL;

		int isBack = cmd.isBack().intValue();
		boolean isRevover = cmd.isRecover();
		switch (isBack) {
		case 0:
			if (TaskOpinion.STATUS_PASSED.equals(signResult)) {
				status = TaskOpinion.STATUS_PASS_CANCEL;
			} else {
				status = TaskOpinion.STATUS_REFUSE_CANCEL;
			}
			break;
		case 1:
		case 2:
			if (isRevover) {
				status = TaskOpinion.STATUS_REVOKED_CANCEL;
			} else {
				status = TaskOpinion.STATUS_BACK_CANCEL;
			}
		}

		return status;
	}

	private VoteResult calcResult(NodeSign nodeSign, String actInstId,
			String nodeId, Integer completeCounter, Integer instanceOfNumbers,
			Short oneVoteResult) {
		VoteResult voteResult = new VoteResult();

		Integer agreeAmount = this.taskSignDataService.getAgreeVoteCount(
				actInstId, nodeId);

		Integer refuseAmount = this.taskSignDataService.getRefuseVoteCount(
				actInstId, nodeId);

		if (nodeSign == null) {
			voteResult = getResultNoRule(oneVoteResult, refuseAmount,
					agreeAmount, instanceOfNumbers);
			return voteResult;
		}

		voteResult = getResultByRule(nodeSign, oneVoteResult, agreeAmount,
				refuseAmount, completeCounter, instanceOfNumbers);
		return voteResult;
	}

	private VoteResult getResultByRule(NodeSign nodeSign, Short oneVoteResult,
			Integer agreeAmount, Integer refuseAmount, Integer completeCounter,
			Integer instanceOfNumbers) {
		VoteResult voteResult = new VoteResult();

		boolean isDirect = NodeSign.FLOW_MODE_DIRECT.equals(nodeSign
				.getFlowMode());

		if (oneVoteResult != null) {
			String result = oneVoteResult.shortValue() == 1 ? "pass" : "refuse";

			if (isDirect) {
				voteResult = new VoteResult(result, true);
			} else if (completeCounter.equals(instanceOfNumbers)) {
				voteResult = new VoteResult(result, true);
			}
			return voteResult;
		}

		if (NodeSign.VOTE_TYPE_PERCENT.equals(nodeSign.getVoteType())) {
			voteResult = getResultByPercent(nodeSign, agreeAmount,
					refuseAmount, instanceOfNumbers, completeCounter);
		} else {
			voteResult = getResultByVotes(nodeSign, agreeAmount, refuseAmount,
					instanceOfNumbers, completeCounter);
		}

		return voteResult;
	}

	private VoteResult getResultNoRule(Short oneVoteResult,
			Integer refuseAmount, Integer agreeAmount, Integer instanceOfNumbers) {
		VoteResult voteResult = new VoteResult();

		if (oneVoteResult != null) {
			if (oneVoteResult.shortValue() == 1) {
				voteResult.setSignResult("pass");
			} else {
				voteResult.setSignResult("refuse");
			}
			voteResult.setIsComplete(true);
		} else if (refuseAmount.intValue() > 0) {
			voteResult.setSignResult("refuse");
			voteResult.setIsComplete(true);
		} else if (agreeAmount.equals(instanceOfNumbers)) {
			voteResult.setSignResult("pass");
			voteResult.setIsComplete(true);
		}

		return voteResult;
	}

	private VoteResult getResultByVotes(NodeSign nodeSign, Integer agree,
			Integer refuse, Integer instanceOfNumbers, Integer completeCounter) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isDirect = nodeSign.getFlowMode().shortValue() == 1;
		boolean isPass = false;

		if (NodeSign.DECIDE_TYPE_PASS.equals(nodeSign.getDecideType())) {
			if (agree.intValue() >= nodeSign.getVoteAmount().longValue()) {
				result = "pass";
				isPass = true;
			} else {
				result = "refuse";
			}

		} else if (refuse.intValue() >= nodeSign.getVoteAmount().longValue()) {
			result = "refuse";
			isPass = true;
		} else {
			result = "pass";
		}

		if ((isDirect) && (isPass)) {
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

	private VoteResult getResultByPercent(NodeSign nodeSign, Integer agree,
			Integer refuse, Integer instanceOfNumbers, Integer completeCounter) {
		boolean isComplete = instanceOfNumbers.equals(completeCounter);
		VoteResult voteResult = new VoteResult();
		String result = "";
		boolean isPass = false;
		boolean isDirect = nodeSign.getFlowMode().shortValue() == 1;
		float percents = 0.0F;

		if (NodeSign.DECIDE_TYPE_PASS.equals(nodeSign.getDecideType())) {
			percents = agree.intValue() / instanceOfNumbers.intValue();

			if (percents * 100.0F >= (float) nodeSign.getVoteAmount()
					.longValue()) {
				result = "pass";
				isPass = true;
			} else {
				result = "refuse";
			}
		} else {
			percents = refuse.intValue() / instanceOfNumbers.intValue();

			if (percents * 100.0F >= (float) nodeSign.getVoteAmount()
					.longValue()) {
				result = "refuse";
				isPass = true;
			} else {
				result = "pass";
			}
		}

		if ((isDirect) && (isPass)) {
			voteResult = new VoteResult(result, true);
		} else if (isComplete) {
			voteResult = new VoteResult(result, true);
		}
		return voteResult;
	}

	class VoteResult {
		private String signResult = "";

		private boolean isComplete = false;

		public VoteResult() {
		}

		public VoteResult(String signResult, boolean isComplate) {
			this.signResult = signResult;
			this.isComplete = isComplate;
		}

		public String getSignResult() {
			return this.signResult;
		}

		public void setSignResult(String signResult) {
			this.signResult = signResult;
		}

		public boolean getIsComplete() {
			return this.isComplete;
		}

		public void setIsComplete(boolean isComplete) {
			this.isComplete = isComplete;
		}
	}

}
