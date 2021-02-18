package com.cssrc.ibms.core.flow.model; 

import java.util.ArrayList;
import java.util.List;

import com.cssrc.ibms.api.activity.model.IDefAuthorize;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class DefAuthorize extends BaseModel implements IDefAuthorize
{
  protected Long id;
  protected String authorizeType;
  protected String authorizeDesc;
  protected List<DefUser> bpmDefUserList = new ArrayList();

  protected List<DefAct> bpmDefActList = new ArrayList();
  protected String ownerName;
  protected String defName;
  protected String ownerNameJson;
  protected String defNameJson;

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public String getAuthorizeType()
  {
    return this.authorizeType;
  }

  public void setAuthorizeType(String authorizeType)
  {
    this.authorizeType = authorizeType;
  }

  public String getAuthorizeDesc()
  {
    return this.authorizeDesc;
  }

  public void setAuthorizeDesc(String authorizeDesc)
  {
    this.authorizeDesc = authorizeDesc;
  }

  public List<DefUser> getDefUserList()
  {
    return this.bpmDefUserList;
  }

  public void setDefUserList(List<DefUser> bpmDefUserList)
  {
    this.bpmDefUserList = bpmDefUserList;
  }

  public List<DefAct> getDefActList()
  {
    return this.bpmDefActList;
  }

  public void setDefActList(List<DefAct> bpmDefActList)
  {
    this.bpmDefActList = bpmDefActList;
  }

  public String getOwnerName()
  {
    return this.ownerName;
  }

  public void setOwnerName(String ownerName)
  {
    this.ownerName = ownerName;
  }

  public String getDefName()
  {
    return this.defName;
  }

  public void setDefName(String defName)
  {
    this.defName = defName;
  }

  public String getOwnerNameJson()
  {
    return this.ownerNameJson;
  }

  public void setOwnerNameJson(String ownerNameJson)
  {
    this.ownerNameJson = ownerNameJson;
  }

  public String getDefNameJson()
  {
    return this.defNameJson;
  }

  public void setDefNameJson(String defNameJson)
  {
    this.defNameJson = defNameJson;
  }

  public static final class BPMDEFAUTHORIZE_RIGHT_TYPE
  {
    public static final String START = "start";
    public static final String MANAGEMENT = "management";
    public static final String TASK = "task";
    public static final String INSTANCE = "instance";
  }
}
