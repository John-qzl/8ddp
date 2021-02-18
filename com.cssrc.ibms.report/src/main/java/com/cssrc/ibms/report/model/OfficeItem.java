package com.cssrc.ibms.report.model;

import java.util.Date;

import com.cssrc.ibms.api.report.model.IOfficeItem;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class OfficeItem extends BaseModel implements IOfficeItem{
	private static final long serialVersionUID = 4560050009368161987L;

	private String id;

    private String officeId;

    //private String tableId;
    
    private String tableName;

    private String columnId;

    private String columnName;

    private String relations;

    private String type;
    
	protected Long items_creatorId;// 创建人ID
	
	protected Date items_createTime;// 创建时间
	
	protected Long items_updateId;// 更改人ID
	
	protected Date items_updateTime;// 更改时间

    public Long getItems_creatorId() {
		return items_creatorId;
	}

	public void setItems_creatorId(Long items_creatorId) {
		this.items_creatorId = items_creatorId;
	}

	public Date getItems_createTime() {
		return items_createTime;
	}

	public void setItems_createTime(Date items_createTime) {
		this.items_createTime = items_createTime;
	}

	public Long getItems_updateId() {
		return items_updateId;
	}

	public void setItems_updateId(Long items_updateId) {
		this.items_updateId = items_updateId;
	}

	public Date getItems_updateTime() {
		return items_updateTime;
	}

	public void setItems_updateTime(Date items_updateTime) {
		this.items_updateTime = items_updateTime;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id == null ? null : id.trim();
    }

    public String getOfficeId() {
        return officeId;
    }

    public void setOfficeId(String officeId) {
        this.officeId = officeId == null ? null : officeId.trim();
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName == null ? null : columnName.trim();
    }

    public String getRelations() {
        return relations;
    }

    public void setRelations(String relations) {
        this.relations = relations == null ? null : relations.trim();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

	public String getColumnId() {
		return columnId;
	}

	public void setColumnId(String columnId) {
		this.columnId = columnId;
	}
    
}