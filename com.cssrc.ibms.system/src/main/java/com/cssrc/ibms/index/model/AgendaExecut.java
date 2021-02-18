package com.cssrc.ibms.index.model;

/**
 * 日程人员关系Model
 * @author YangBo
 *
 */
public class AgendaExecut{
    private Long id;

    private Long agendaId; //日程ID

    private Long executorId; //执行人ID

    private String executor; //执行人名

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAgendaId() {
        return agendaId;
    }

    public void setAgendaId(Long agendaId) {
        this.agendaId = agendaId;
    }

    public Long getExecutorId() {
        return executorId;
    }

    public void setExecutorId(Long executorId) {
        this.executorId = executorId;
    }

    public String getExecutor() {
        return executor;
    }

    public void setExecutor(String executor) {
        this.executor = executor == null ? null : executor.trim();
    }
}