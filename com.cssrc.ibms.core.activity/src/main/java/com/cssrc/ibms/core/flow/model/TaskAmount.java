package com.cssrc.ibms.core.flow.model;

import com.cssrc.ibms.api.activity.model.ITaskAmount;

public class TaskAmount implements ITaskAmount{
	
	private Long typeId=0L;
	
	private Long defId=0L;

	private int read=0;
	
	private int total=0;
	
	private int notRead=0;

	public Long getTypeId() {
		return typeId;
	}

	public void setTypeId(Long typeId) {
		this.typeId = typeId;
	}

	public int getRead() {
		return read;
	}

	public void setRead(int read) {
		this.read = read;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getNotRead() {
		return notRead;
	}

	public void setNotRead(int notRead) {
		this.notRead = notRead;
	}

    public Long getDefId()
    {
        return defId;
    }

    public void setDefId(Long defId)
    {
        this.defId = defId;
    }
	

}
