package com.cssrc.ibms.system.model;



import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.map.annotate.JsonSerialize;

import com.cssrc.ibms.api.system.model.ISysFileFolder;
import com.cssrc.ibms.api.system.util.CustomDateSerializer;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.dbom.ITreeNode;


/**
 * SysFileFolder entity. @author MyEclipse Persistence Tools
 */

public class SysFileFolder  extends BaseModel implements ITreeNode,ISysFileFolder {
	
	 private static final long serialVersionUID = 1L;
	 private Long id;
     private Long pid;
     private String name;
     private String path;
     private Long depth;
     private String notes;
     private Long creatorId;
     private Date createtime;
     private Long updateId;
     private Date updatetime;
     private Boolean delflag;
     private Boolean sharedNode;//共享节点类型
     private Boolean systemNode;//系统节点不可删除 


    // Constructors

    /** default constructor */
    public SysFileFolder() {
    }

    
    /** full constructor */
	public SysFileFolder(Long id, Long pid, String name, String path,
			Long depth, String notes, Long creatorId, Date createtime,
			Long updateId, Date updatetime, Boolean delflag,
			Boolean sharedNode, Boolean systemNode) {
		super();
		this.id = id;
		this.pid = pid;
		this.name = name;
		this.path = path;
		this.depth = depth;
		this.notes = notes;
		this.creatorId = creatorId;
		this.createtime = createtime;
		this.updateId = updateId;
		this.updatetime = updatetime;
		this.delflag = delflag;
		this.sharedNode = sharedNode;
		this.systemNode = systemNode;
	}


   
    // Property accessors

    public Long getId() {
        return this.id;
    }

	public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return this.pid;
    }
    
    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return this.name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return this.path;
    }
    
    public void setPath(String path) {
        this.path = path;
    }

    public Long getDepth() {
        return this.depth;
    }
    
    public void setDepth(Long depth) {
        this.depth = depth;
    }

    public String getNotes() {
        return this.notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Long getCreatorId() {
        return this.creatorId;
    }
    
    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }
	@JsonSerialize(using = CustomDateSerializer.class) 
	public Date getCreatetime() {
		return createtime;
	}

	public void setCreatetime(Date createtime) {
		this.createtime = createtime;
	}


	public Long getUpdateId() {
        return this.updateId;
    }
    
    public void setUpdateId(Long updateId) {
        this.updateId = updateId;
    }

	public Date getUpdatetime() {
		return updatetime;
	}

	public void setUpdatetime(Date updatetime) {
		this.updatetime = updatetime;
	}


	public Boolean getDelflag() {
        return this.delflag;
    }
    
    public void setDelflag(Boolean delflag) {
        this.delflag = delflag;
    }
    
	public Boolean getSharedNode() {
		return sharedNode;
	}

	public void setSharedNode(Boolean sharedNode) {
		this.sharedNode = sharedNode;
	}

	public Boolean getSystemNode() {
		return systemNode;
	}
	
	public void setSystemNode(Boolean systemNode) {
		this.systemNode = systemNode;
	}


   
	@Override
	public Map<String, String> getRelation() {
		Map<String, String> propMap = new HashMap<String, String>();
		propMap.put("id", "id");
		propMap.put("text", "name");	 
		propMap.put("parentId", "pid");
		propMap.put("systemNode", "systemNode");
		return propMap;
	}








}