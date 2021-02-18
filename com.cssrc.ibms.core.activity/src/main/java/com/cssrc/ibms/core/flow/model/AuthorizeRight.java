package com.cssrc.ibms.core.flow.model;

import net.sf.json.JSONObject;

import com.cssrc.ibms.core.util.string.StringUtil;

public class AuthorizeRight
{
  protected String authorizeType = "management";
  protected String defKey;
  protected String managementEdit = "N";

  protected String managementDel = "N";

  protected String managementStart = "N";

  protected String managementSet = "N";

  protected String managementInternational = "N";

  protected String managementClean = "N";

  protected String instanceDel = "N";

  protected String instanceLog = "N";

  protected String rightContent = "";

  protected JSONObject rightJsonObj = new JSONObject();

  public String getAuthorizeType()
  {
    return this.authorizeType;
  }

  public void setAuthorizeType(String authorizeType)
  {
    this.authorizeType = authorizeType;
  }

  public String getDefKey()
  {
    return this.defKey;
  }

  public void setDefKey(String defKey)
  {
    this.defKey = defKey;
  }

  public String getManagementEdit()
  {
    return this.managementEdit;
  }

  public void setManagementEdit(String managementEdit)
  {
    this.managementEdit = managementEdit;
  }

  public String getManagementDel()
  {
    return this.managementDel;
  }

  public void setManagementDel(String managementDel)
  {
    this.managementDel = managementDel;
  }

  public String getManagementStart()
  {
    return this.managementStart;
  }

  public void setManagementStart(String managementStart)
  {
    this.managementStart = managementStart;
  }

  public String getManagementSet()
  {
    return this.managementSet;
  }

  public void setManagementSet(String managementSet)
  {
    this.managementSet = managementSet;
  }

  public String getManagementInternational()
  {
    return this.managementInternational;
  }

  public void setManagementInternational(String managementInternational)
  {
    this.managementInternational = managementInternational;
  }

  public String getManagementClean()
  {
    return this.managementClean;
  }

  public void setManagementClean(String managementClean)
  {
    this.managementClean = managementClean;
  }

  public String getInstanceDel()
  {
    return this.instanceDel;
  }

  public void setInstanceDel(String instanceDel)
  {
    this.instanceDel = instanceDel;
  }

  public String getInstanceLog()
  {
    return this.instanceLog;
  }

  public void setInstanceLog(String instanceLog)
  {
    this.instanceLog = instanceLog;
  }

  public String getRightContent()
  {
    return this.rightContent;
  }

  public void setRightContent(String rightContent)
  {
    this.rightContent = rightContent;
    if (StringUtil.isNotEmpty(rightContent)) {
      JSONObject obj = JSONObject.fromObject(rightContent);

      if ("management".equals(this.authorizeType)) {
        if (obj.containsKey("m_edit")) {
          this.managementEdit = obj.getString("m_edit");
        }
        if (obj.containsKey("m_del")) {
          this.managementDel = obj.getString("m_del");
        }
        if (obj.containsKey("m_start")) {
          this.managementStart = obj.getString("m_start");
        }
        if (obj.containsKey("m_set")) {
          this.managementSet = obj.getString("m_set");
        }
        if (obj.containsKey("m_international")) {
          this.managementInternational = obj.getString("m_international");
        }
        if (obj.containsKey("m_clean")) {
          this.managementClean = obj.getString("m_clean");
        }
      }
      else if ("instance".equals(this.authorizeType)) {
        if (obj.containsKey("i_del")) {
          this.instanceDel = obj.getString("i_del");
        }
        if (obj.containsKey("i_log")) {
          this.instanceLog = obj.getString("i_log");
        }
      }
      this.rightJsonObj = obj;
    }
  }

  public void setRightByNeed(String needRight, String rightContent, String authorizeType)
  {
    if ((StringUtil.isNotEmpty(needRight)) && (StringUtil.isNotEmpty(rightContent))) {
      JSONObject obj = JSONObject.fromObject(rightContent);

      if ("management".equals(authorizeType)) {
        if (obj.containsKey("m_edit")) {
          String m_edit = obj.getString("m_edit");
          if (needRight.equals(m_edit)) {
            this.managementEdit = m_edit;
            this.rightJsonObj.put("m_edit", m_edit);
          }
        }
        if (obj.containsKey("m_del")) {
          String m_del = obj.getString("m_del");
          if (needRight.equals(m_del)) {
            this.managementDel = m_del;
            this.rightJsonObj.put("m_del", m_del);
          }
        }
        if (obj.containsKey("m_start")) {
          String m_start = obj.getString("m_start");
          if (needRight.equals(m_start)) {
            this.managementStart = m_start;
            this.rightJsonObj.put("m_start", m_start);
          }
        }
        if (obj.containsKey("m_set")) {
          String m_set = obj.getString("m_set");
          if (needRight.equals(m_set)) {
            this.managementSet = m_set;
            this.rightJsonObj.put("m_set", m_set);
          }
        }
        if (obj.containsKey("m_international")) {
          String m_international = obj.getString("m_international");
          if (needRight.equals(m_international)) {
            this.managementInternational = m_international;
            this.rightJsonObj.put("m_international", m_international);
          }
        }
        if (obj.containsKey("m_clean")) {
          String m_clean = obj.getString("m_clean");
          if (needRight.equals(m_clean)) {
            this.managementClean = m_clean;
            this.rightJsonObj.put("m_clean", m_clean);
          }
        }
      }
      else if ("instance".equals(authorizeType)) {
        if (obj.containsKey("i_del")) {
          String i_del = obj.getString("i_del");
          if (needRight.equals(i_del)) {
            this.instanceDel = i_del;
            this.rightJsonObj.put("i_del", i_del);
          }
        }
        if (obj.containsKey("m_log")) {
          String i_log = obj.getString("i_log");
          if (needRight.equals(i_log)) {
            this.instanceLog = i_log;
            this.rightJsonObj.put("i_log", i_log);
          }
        }
      }
      this.rightContent = this.rightJsonObj.toString();
    }
  }

  public void setRightByAuthorizeType(String right, String authorizeType)
  {
    this.authorizeType = authorizeType;

    if ("management".equals(authorizeType)) {
      this.managementEdit = right;
      this.rightJsonObj.put("m_edit", right);
      this.managementDel = right;
      this.rightJsonObj.put("m_del", right);
      this.managementStart = right;
      this.rightJsonObj.put("m_start", right);
      this.managementSet = right;
      this.rightJsonObj.put("m_set", right);
      this.managementInternational = right;
      this.rightJsonObj.put("m_international", right);
      this.managementClean = right;
      this.rightJsonObj.put("m_clean", right);
    }
    else if ("instance".equals(authorizeType)) {
      this.instanceDel = right;
      this.instanceLog = right;
    }
    this.rightContent = this.rightJsonObj.toString();
  }

  public JSONObject getRightJsonObj()
  {
    return this.rightJsonObj;
  }

  public void setRightJsonObj(JSONObject rightJsonObj)
  {
    this.rightJsonObj = rightJsonObj;
  }
}