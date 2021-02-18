package com.cssrc.ibms.core.flow.model;

import com.cssrc.ibms.api.activity.model.ITaskWarningSet;
     
/**
 * TaskWarningSet
 * @author liubo
 * @date 2017年5月10日
 */
public class TaskWarningSet implements ITaskWarningSet{
	private String name;
	private String key;
	private Integer relativeDueTime;
	private int reminderDueDay;
	private int reminderDueHour;
	private int reminderDueMinute;
	private String relativeType;
	private int level;
	public String getName() {
		return this.name;
	}
     
	public void setName(String name) {
		this.name = name;
	}
     
	public Integer getRelativeDueTime() {
		if (this.relativeDueTime != null) {
			return this.relativeDueTime;
		}
		return Integer.valueOf(this.reminderDueMinute + 
				this.reminderDueHour * 60 + 
				this.reminderDueDay * 1440);
	}
     
	public int getReminderDueDay() {
		return this.reminderDueDay;
	}
     
	public void setReminderDueDay(int reminderDueDay) {
		this.reminderDueDay = reminderDueDay;
	}
     
	public int getReminderDueHour() {
		return this.reminderDueHour;
	}
     
	public void setReminderDueHour(int reminderDueHour) {
		this.reminderDueHour = reminderDueHour;
	}
     
	public int getReminderDueMinute() {
		return this.reminderDueMinute;
	}
     
	public void setReminderDueMinute(Integer reminderDueMinute) {
		this.reminderDueMinute = reminderDueMinute.intValue();
	}
     
	public String getRelativeType() {
		return this.relativeType;
	}
     
	public void setRelativeType(String relativeType) {
		this.relativeType = relativeType;
	}
     
	public int getLevel() {
		return this.level;
	}
     
	public void setLevel(int level) {
		this.level = level;
	}
     
	public void setReminderDueMinute(int reminderDueMinute) {
		this.reminderDueMinute = reminderDueMinute;
	}

    public String getKey()
    {
        return "L_"+this.level;
    }

	
}
 