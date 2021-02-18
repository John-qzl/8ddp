package com.cssrc.ibms.core.flow.model;

import com.cssrc.ibms.api.activity.model.IDefUser;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;

public class DefUser extends BaseModel 
  implements Cloneable,IDefUser
{
  protected Long id;
  protected Long authorizeId;
  protected Long ownerId;
  protected String ownerName;
  protected String rightType;

  public Long getId()
  {
    return this.id;
  }

  public void setId(Long id)
  {
    this.id = id;
  }

  public Long getAuthorizeId()
  {
    return this.authorizeId;
  }

  public void setAuthorizeId(Long authorizeId)
  {
    this.authorizeId = authorizeId;
  }

  public Long getOwnerId()
  {
    return this.ownerId;
  }

  public void setOwnerId(Long ownerId)
  {
    this.ownerId = ownerId;
  }

  public String getOwnerName()
  {
    return this.ownerName;
  }

  public void setOwnerName(String ownerName)
  {
    this.ownerName = ownerName;
  }

  public String getRightType()
  {
    return this.rightType;
  }

  public void setRightType(String rightType)
  {
    this.rightType = rightType;
  }

  public static final class BPMDEFUSER_RIGHT_TYPE
  {
    public static final String ALL = "all";
    public static final String USER = "user";
    public static final String ROLE = "role";
    public static final String ORG = "org";
    public static final String POSITION = "position";
    public static final String GRANT = "grant";
  }
}

