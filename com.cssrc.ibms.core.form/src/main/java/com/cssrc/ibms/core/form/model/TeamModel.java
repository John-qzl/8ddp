package com.cssrc.ibms.core.form.model;

import java.util.ArrayList;
import java.util.List;

public class TeamModel {
	/**
	 * 分组名称
	 */
	protected String teamName = "";
    /**
     * 分组名称key
     */	
	protected String teamNameKey = "";

	/**
	 * 分组对应的字段
	 */
	protected List<FormField> teamFields = new ArrayList<FormField>();

	public String getTeamName() {
		return teamName;
	}

	public void setTeamName(String teamName) {
		this.teamName = teamName;
	}

	public List<FormField> getTeamFields() {
		return teamFields;
	}

	public void setTeamFields(List<FormField> teamFields) {
		this.teamFields = teamFields;
	}

    public String getTeamNameKey()
    {
        return teamNameKey;
    }

    public void setTeamNameKey(String teamNameKey)
    {
        this.teamNameKey = teamNameKey;
    }
	
	

}
