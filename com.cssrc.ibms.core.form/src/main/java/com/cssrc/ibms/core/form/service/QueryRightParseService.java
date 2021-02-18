package com.cssrc.ibms.core.form.service;
 
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.form.intf.IQueryRightParseService;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.core.engine.GroovyScriptEngine;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.json.JSONUtil;
import com.cssrc.ibms.core.util.string.StringUtil;


@Service
public class QueryRightParseService implements IQueryRightParseService{

  @Resource
  private ISysUserService sysUserService;
  
  @Resource
  private ISysRoleService sysRoleService;

  @Resource
  private IPositionService positionService;

  @Resource
  private ISysOrgService sysOrgService;

  @Resource
  private GroovyScriptEngine groovyScriptEngine;
  

  public Map<String, Object> getRightMap(Long userId, Long curOrgId)
  {
    Map map = new HashMap();
    ISysUser user = sysUserService.getById(userId);
    List<?extends ISysRole> roles = sysRoleService.getByUserId(userId);
    List<?extends IPosition> positions = positionService.getByUserId(userId);
    List<?extends ISysOrg> orgs = sysOrgService.getOrgsByUserId(userId);
    List<?extends ISysOrg> ownOrgs = sysOrgService.getChargeOrgByUserId(userId);
    map.put("userId", userId);
    map.put("curOrgId", curOrgId);
    map.put("roles", roles);
    map.put("positions", positions);
    map.put("orgs", orgs);

    return map;
  }

  public boolean hasRight(JSONObject permission, Map<String, Object> rightMap)
  {
    String type = permission.get("type").toString();
    String id = permission.get("id").toString();
    Object script = permission.get("script");
    if ("none".equals(type))
      return false;
    if ("everyone".equals(type))
      return true;
    Long userId = (Long)rightMap.get("userId");
    Long curOrgId = (Long)rightMap.get("curOrgId");
    List<ISysRole> roles = (List<ISysRole>)rightMap.get("roles");
    List<?extends IPosition> positions = (List<?extends IPosition>)rightMap.get("positions");
    List<ISysOrg> orgs = (List<ISysOrg>) rightMap.get("orgs");
    List<ISysOrg> ownOrgs = (List<ISysOrg>)rightMap.get("ownOrgs");

    if ("user".equals(type)) {
      return StringUtil.contain(id, userId.toString());
    }

    if ("role".equals(type)) {
      if (roles == null)
        return false;
      for (ISysRole role : roles) {
        if (StringUtil.contain(id, role.getRoleId().toString())) {
          return true;
        }
      }

    }
    else if ("org".equals(type)) {
      if (orgs == null)
        return false;
      for (ISysOrg org : orgs) {
        if (StringUtil.contain(id, org.getOrgId().toString())) {
          return true;
        }
      }

    }
    else if ("orgMgr".equals(type)) {
      if (ownOrgs == null)
        return false;
      for (ISysOrg ownOrg : ownOrgs) {
        if (StringUtil.contain(id, ownOrg.getOrgId().toString())) {
          return true;
        }
      }

    }
    else if ("pos".equals(type)) {
      if (positions == null)
        return false;
      for (IPosition position : positions) {
        if (StringUtil.contain(id, position.getPosId().toString()))
          return true;
      }
    }
    else if ("script".equals(type)) {
      if (BeanUtils.isEmpty(script))
        return false;
      Map map = new HashMap();
      CommonVar.setCurrentVars(map);
      return this.groovyScriptEngine.executeBoolean(script.toString(), map);
    }
    return false;
  }

  public String getDefaultRight(Integer rightType)
  {
    JSONArray jsonAry = new JSONArray();
    JSONObject json = new JSONObject();
    json.accumulate("s", rightType);
    json.accumulate("type", "none");
    json.accumulate("id", "");
    json.accumulate("name", "");
    json.accumulate("script", "");
    jsonAry.add(json);
    return jsonAry.toString();
  }

  public Map<String, Boolean> getPermission(int type, String field, Map<String, Object> rightMap)
  {
    JSONArray jsonAry = JSONArray.fromObject(field);
    return getPermissionMap(type, jsonAry, rightMap);
  }

  private Map<String, Boolean> getPermissionMap(int type, JSONArray jsonAry, Map<String, Object> rightMap)
  {
    Map map = new HashMap();
    if (JSONUtil.isEmpty(jsonAry))
      return map;
    Iterator localIterator2;
    for (Iterator localIterator1 = jsonAry.iterator(); localIterator1.hasNext(); )
    {
      Object obj = localIterator1.next();
      JSONObject json = JSONObject.fromObject(obj);
      String name = (String)json.get("name");
      JSONArray rights = (JSONArray)json.get("right");
      
      for(localIterator2 = rights.iterator();localIterator2.hasNext();){
    	  Object right = localIterator2.next();
          JSONObject rightJson = JSONObject.fromObject(right);
          Integer s = (Integer)rightJson.get("s");
          if (s.intValue() == type) {
            map.put(name, Boolean.valueOf(hasRight(rightJson, 
              rightMap)));
          }
      } 
    }
    return map;
  }
}
 