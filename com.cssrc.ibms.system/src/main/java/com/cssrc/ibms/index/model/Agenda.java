package com.cssrc.ibms.index.model;

import java.util.Date;

/**
 * 日程信息Model
 * @author YangBo
 *
 */
public class Agenda extends AgendaExecut{
	
    private Long agendaId; //日程主键ID

    private String type; //日程类型

    private String subject; //标题

    private String content; //内容

    private Long creatorId; //创建人ID

    private String creator; //创建人名
    
    private Date startTime; //开始时间
    
    private Date endTime; //结束时间

    private String grade; //紧急程度 一般 重要 紧急

    private Long warnWay = Long.valueOf(0L); //提醒方式 0 不提醒 1 站内信 2 外部邮件 

    private Long fileId; //附件ID

    private Long runId; //任务ID
    
    public Long status = Long.valueOf(0L); //状态 0 未完成 1 完成
    
    public StringBuffer executorIds;//执行人Id组合
    
    public StringBuffer executors; //执行人组合

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject == null ? null : subject.trim();
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator == null ? null : creator.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade == null ? null : grade.trim();
    }

    public Long getWarnWay() {
        return warnWay;
    }

    public void setWarnWay(Long warnWay) {
        this.warnWay = warnWay;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Long getRunId() {
        return runId;
    }

    public void setRunId(Long runId) {
        this.runId = runId;
    }

	public Long getStatus() {
		return status;
	}

	public void setStatus(Long status) {
		this.status = status;
	}

	public StringBuffer getExecutorIds() {
		return executorIds;
	}

	public void setExecutorIds(StringBuffer executorIds) {
		this.executorIds = executorIds;
	}

	public StringBuffer getExecutors() {
		return executors;
	}

	public void setExecutors(StringBuffer executors) {
		this.executors = executors;
	}
    
}