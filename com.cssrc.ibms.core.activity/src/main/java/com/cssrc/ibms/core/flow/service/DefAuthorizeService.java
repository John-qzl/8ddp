package com.cssrc.ibms.core.flow.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IDefAuthorizeService;
import com.cssrc.ibms.api.activity.model.IDefAuthorize;
import com.cssrc.ibms.api.core.intf.BaseService;
import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.db.mybatis.dao.IEntityDao;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.flow.dao.DefActDao;
import com.cssrc.ibms.core.flow.dao.DefAuthorizeDao;
import com.cssrc.ibms.core.flow.dao.DefUserDao;
import com.cssrc.ibms.core.flow.dao.DefinitionDao;
import com.cssrc.ibms.core.flow.model.AuthorizeRight;
import com.cssrc.ibms.core.flow.model.DefAct;
import com.cssrc.ibms.core.flow.model.DefAuthorize;
import com.cssrc.ibms.core.flow.model.DefUser;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;

@Service
public class DefAuthorizeService extends BaseService<DefAuthorize> implements IDefAuthorizeService
{

  @Resource
  private DefUserDao bpmDefUserDao;

  @Resource
  private DefActDao bpmDefActDao;

  @Resource
  private DefAuthorizeDao bpmDefAuthorizeDao;

  @Resource
  private ISysRoleService sysRoleService;

  @Resource
  private ISysOrgService sysOrgService;

  @Resource
  private IUserPositionService userPositionService;

  @Resource
  private DefinitionDao bpmDefinitionDao;
  
  @Resource
  private ISysUserService sysUserService;

  protected IEntityDao<DefAuthorize, Long> getEntityDao()
  {
    return this.bpmDefAuthorizeDao;
  }

  public List<DefAuthorize> getAuthorizeListByFilter(QueryFilter queryFilter)
  {
    List bpmDefAuthorizeList = new ArrayList();
    List<DefAuthorize> list = this.bpmDefAuthorizeDao.getAll(queryFilter);
    for (DefAuthorize me : list) {
      Long id = me.getId();
      DefAuthorize bpmDefAuthorize = getAuthorizeByMe(id, me, false);
      bpmDefAuthorizeList.add(bpmDefAuthorize);
    }
    return bpmDefAuthorizeList;
  }

  public DefAuthorize getAuthorizeById(Long id)
  {
    DefAuthorize bpmDefAuthorize = getAuthorizeByMe(id, null, true);
    return bpmDefAuthorize;
  }

  public DefAuthorize getAuthorizeByMe(Long id, DefAuthorize me, boolean isNeedjson)
  {
    DefAuthorize bpmDefAuthorize = null;
    if (id.longValue() > 0L) {
      Map params = new HashMap();
      params.put("authorizeId", id);

      if ((me != null) && (me.getId() == id))
        bpmDefAuthorize = me;
      else {
        bpmDefAuthorize = (DefAuthorize)this.bpmDefAuthorizeDao.getById(id);
      }

      List bpmDefUserList = this.bpmDefUserDao.getUserByMap(params);
      bpmDefAuthorize.setDefUserList(bpmDefUserList);

      List bpmDefActList = this.bpmDefActDao.getActByMap(params);
      bpmDefAuthorize.setDefActList(bpmDefActList);

      if (isNeedjson) {
        String ownerNameJson = toOwnerNameJson(bpmDefUserList);
        bpmDefAuthorize.setOwnerNameJson(ownerNameJson);
        String defNameJson = toDefNameJson(bpmDefActList);
        bpmDefAuthorize.setDefNameJson(defNameJson);
      }
    }
    return bpmDefAuthorize;
  }

  public Long deleteAuthorizeByIds(Long[] lAryId)
  {
    Long delNum = Long.valueOf(-1L);
    for (Long id : lAryId)
    {
      delNum = Long.valueOf(this.bpmDefAuthorizeDao.delById(id));

      this.bpmDefUserDao.delByAuthorizeId(id);
      this.bpmDefActDao.delByAuthorizeId(id);
    }
    return delNum;
  }

  public Long saveOrUpdateAuthorize(DefAuthorize bpmDefAuthorize)
  {
    Long authorizeId = bpmDefAuthorize.getId();
    if (authorizeId.longValue() > 0L) {
      this.bpmDefAuthorizeDao.update(bpmDefAuthorize);

      this.bpmDefUserDao.delByAuthorizeId(authorizeId);
      this.bpmDefActDao.delByAuthorizeId(authorizeId);
    } else {
      authorizeId = Long.valueOf(UniqueIdUtil.genId());
      bpmDefAuthorize.setId(authorizeId);
      this.bpmDefAuthorizeDao.add(bpmDefAuthorize);
    }

    String myOwnerNameJson = bpmDefAuthorize.getOwnerNameJson();
    List<DefUser> bpmDefUserList = toDefUserList(myOwnerNameJson, authorizeId);
    for (DefUser bpmDefUser : bpmDefUserList) {
      this.bpmDefUserDao.add(bpmDefUser);
    }

    String myDefNameJson = bpmDefAuthorize.getDefNameJson();
    List<DefAct> bpmDefActList = toDefActList(myDefNameJson, authorizeId);
    for (DefAct bpmDefAct : bpmDefActList) {
      this.bpmDefActDao.add(bpmDefAct);
    }

    return authorizeId;
  }

  public String toOwnerNameJson(List<DefUser> myDefUserList)
  {
    Map mapObj = new HashMap();
    Map map = new HashMap();

    if ((myDefUserList == null) || (myDefUserList.size() < 1)) {
      map.put("allJson", "N");
      map.put("userJson", "[]");
      map.put("roleJson", "[]");
      map.put("orgJson", "[]");
      map.put("grantJson", "[]");
      map.put("positionJson", "[]");
    }
    else {
      List allList = new ArrayList();
      List userList = new ArrayList();
      List orgList = new ArrayList();
      List roleList = new ArrayList();
      List grantList = new ArrayList();
      List positionList = new ArrayList();
      for (DefUser bpmDefUser : myDefUserList) {
        String rightType = bpmDefUser.getRightType();
        if ("all".equals(rightType)) {
          allList.add(bpmDefUser);
          break;
        }if ("user".equals(rightType))
          userList.add(bpmDefUser);
        else if ("role".equals(rightType))
          roleList.add(bpmDefUser);
        else if ("org".equals(rightType))
          orgList.add(bpmDefUser);
        else if ("grant".equals(rightType))
          grantList.add(bpmDefUser);
        else if ("position".equals(rightType)) {
          positionList.add(bpmDefUser);
        }

      }

      if (allList.size() > 0) {
        map.put("allJson", "Y");
        map.put("userJson", "[]");
        map.put("roleJson", "[]");
        map.put("orgJson", "[]");
        map.put("grantJson", "[]");
        map.put("positionJson", "[]");
      } else {
        map.put("allJson", "N");
        if (userList.size() > 0) {
          JSONArray jsonarray = JSONArray.fromObject(userList);
          map.put("userJson", jsonarray);
        } else {
          map.put("userJson", "[]");
        }

        if (orgList.size() > 0) {
          JSONArray jsonarray = JSONArray.fromObject(orgList);
          map.put("orgJson", jsonarray);
        } else {
          map.put("orgJson", "[]");
        }

        if (roleList.size() > 0) {
          JSONArray jsonarray = JSONArray.fromObject(roleList);
          map.put("roleJson", jsonarray);
        } else {
          map.put("roleJson", "[]");
        }

        if (grantList.size() > 0) {
          JSONArray jsonarray = JSONArray.fromObject(grantList);
          map.put("grantJson", jsonarray);
        } else {
          map.put("grantJson", "[]");
        }

        if (positionList.size() > 0) {
          JSONArray jsonarray = JSONArray.fromObject(positionList);
          map.put("positionJson", jsonarray);
        } else {
          map.put("positionJson", "[]");
        }
      }
    }

    mapObj.put("objJson", map);
    JSONObject obj = JSONObject.fromObject(mapObj);
    System.out.println(obj.toString());
    return obj.toString();
  }

  public List<DefUser> toDefUserList(String myOwnerNameJson, Long authorizeId)
  {
    List myDefUserList = new ArrayList();
    if (StringUtil.isEmpty(myOwnerNameJson)) {
      return myDefUserList;
    }

    if (BeanUtils.isEmpty(authorizeId)) {
      authorizeId = Long.valueOf(0L);
    }

    JSONObject obj = JSONObject.fromObject(myOwnerNameJson);
    if (obj.containsKey("objJson")) {
      JSONObject objJson = (JSONObject)obj.get("objJson");
      if (objJson.containsKey("allJson")) {
        String allJson = objJson.getString("allJson");

        if ("Y".equals(allJson)) {
          DefUser objUser = new DefUser();
          objUser.setId(Long.valueOf(UniqueIdUtil.genId()));
          objUser.setAuthorizeId(authorizeId);
          objUser.setOwnerId(Long.valueOf(0L));
          objUser.setOwnerName("all");
          objUser.setRightType("all");
          myDefUserList.add(objUser);
        }
        else {
          String[] strArray = { "userJson", "roleJson", "orgJson", "grantJson", "positionJson" };
          for (String jsonName : strArray) {
            getJsontoList(objJson, myDefUserList, jsonName, authorizeId);
          }
        }
      }
    }
    return myDefUserList;
  }

  public List<DefUser> getJsontoList(JSONObject objJson, List<DefUser> myDefUserList, String jsonName, Long authorizeId)
  {
    if (objJson.containsKey(jsonName)) {
      JSONArray myJsonArray = objJson.getJSONArray(jsonName);
      String rightType = "";

      if ("orgJson".equals(jsonName))
        rightType = "org";
      else if ("roleJson".equals(jsonName))
        rightType = "role";
      else if ("grantJson".equals(jsonName))
        rightType = "grant";
      else if ("positionJson".equals(jsonName))
        rightType = "position";
      else if ("userJson".equals(jsonName)) {
        rightType = "user";
      }

      if ("".equals(rightType)) {
        return myDefUserList;
      }

      for (int i = 0; i < myJsonArray.size(); i++) {
        JSONObject jsonObject = myJsonArray.getJSONObject(i);
        DefUser bpmDefUser = new DefUser();
        if (jsonObject.containsKey("ownerId")) {
          Long ownerId = Long.valueOf(jsonObject.getLong("ownerId"));
          bpmDefUser.setOwnerId(ownerId);
        }
        if (jsonObject.containsKey("ownerName")) {
          String ownerName = jsonObject.getString("ownerName");
          bpmDefUser.setOwnerName(ownerName);
        }

        bpmDefUser.setId(Long.valueOf(UniqueIdUtil.genId()));
        bpmDefUser.setAuthorizeId(authorizeId);
        bpmDefUser.setRightType(rightType);
        myDefUserList.add(bpmDefUser);
      }
    }
    return myDefUserList;
  }

  public String toDefNameJson(List<DefAct> myDefActList)
  {
    if ((myDefActList == null) || (myDefActList.size() < 1)) {
      return "{\"defArry\":[]}";
    }

    Collections.reverse(myDefActList);
    Map map = new HashMap();
    JSONArray jsonArray = JSONArray.fromObject(myDefActList);
    map.put("defArry", jsonArray);
    JSONObject obj = JSONObject.fromObject(map);
    System.out.println("toDefNameJson === " + obj.toString());
    return obj.toString();
  }

  public List<DefAct> toDefActList(String myDefNameJson, Long authorizeId)
  {
    List myDefActList = new ArrayList();
    if (StringUtil.isEmpty(myDefNameJson)) {
      return myDefActList;
    }
    if (BeanUtils.isEmpty(authorizeId)) {
      authorizeId = Long.valueOf(0L);
    }
    JSONObject obj = JSONObject.fromObject(myDefNameJson);
    if (obj.containsKey("defArry")) {
      JSONArray myJsonArray = obj.getJSONArray("defArry");

      for (int i = 0; i < myJsonArray.size(); i++) {
        JSONObject jsonObject = myJsonArray.getJSONObject(i);
        DefAct bpmDefAct = new DefAct();
        if (jsonObject.containsKey("defKey")) {
          String defKey = jsonObject.getString("defKey");
          bpmDefAct.setDefKey(defKey);
        }
        if (jsonObject.containsKey("defName")) {
          String defName = jsonObject.getString("defName");
          bpmDefAct.setDefName(defName);
        }
        if (jsonObject.containsKey("rightContent")) {
          String rightContent = jsonObject.getString("rightContent");
          bpmDefAct.setRightContent(rightContent);
        }

        bpmDefAct.setId(Long.valueOf(UniqueIdUtil.genId()));
        bpmDefAct.setAuthorizeId(authorizeId);
        myDefActList.add(bpmDefAct);
      }
    }
    return myDefActList;
  }

  public Map<String, Object> getActRightByUserMap(Long userId, String authorizeType, boolean isRight, boolean isMyDef)
  {
    Map mapRight = new HashMap();
    Map map = new HashMap();
    map.put("authorizeType", authorizeType);
    String authorizeIds = "";

    map.put("all", "all");
    map.put("user", "user");
    map.put("role", "role");
    map.put("org", "org");
    map.put("position", "position");
    map.put("grant", "grant");

    map.put("userId", userId);
    ISysUser sysUser = sysUserService.getById(userId);
   /* Set<ISysRole> roles = sysUser.getRoles();*/
    List<?extends ISysRole> roles=sysRoleService.getByUserId(userId);
    if ((BeanUtils.isNotEmpty(roles)) && (roles.size() > 0)) {
      String roleIds = "";
      for (ISysRole sysRole : roles) {
        roleIds = roleIds + sysRole.getRoleId() + ",";
      }
      roleIds = roleIds.substring(0, roleIds.length() - 1);
      map.put("roleIds", roleIds);
    }
   
    /*Set<ISysOrg> orgs =  sysUser.getOrgs();*/
    List<?extends ISysOrg> orgs =  sysOrgService.getByUserId(userId);//包含isprimary
    											
    Map mapPath = new HashMap(); 
    if ((BeanUtils.isNotEmpty(orgs)) && (orgs.size() > 0)) {
      String orgIds = "";
      String grantIds = "";
      for (ISysOrg sysOrg : orgs) {
        orgIds = orgIds + sysOrg.getOrgId() + ",";
        String paths = sysOrg.getPath();
        if (StringUtil.isNotEmpty(paths)) {
          String[] pathArray = paths.split("\\.");
          for (String path : pathArray) {
            if (StringUtil.isNotEmpty(path)) {
              String pt = (String)mapPath.get(path);
              if (StringUtil.isEmpty(pt)) {
                mapPath.put(path, path);
                grantIds = grantIds + path + ",";
              }
            }
          }
        }
      }
      orgIds = orgIds.substring(0, orgIds.length() - 1);
      map.put("orgIds", orgIds);
      if (grantIds.length() > 0) {
        grantIds = grantIds.substring(0, grantIds.length() - 1);
        map.put("grantIds", grantIds);
      }

    }

    List<?extends IUserPosition> userPositions = this.userPositionService.getByUserId(userId);
    if ((BeanUtils.isNotEmpty(userPositions)) && (userPositions.size() > 0)) {
      String positionIds = "";
      for (IUserPosition userPosition : userPositions) {
        positionIds = positionIds + userPosition.getPosId() + ",";
      }
      positionIds = positionIds.substring(0, positionIds.length() - 1);
      map.put("positionIds", positionIds);
    }

    Map authorizeRightMap = new HashMap();
    Map myRightMap = new HashMap(); 
    if (isMyDef) {
      List<String> myList = this.bpmDefinitionDao.getByCreateBy(userId);
      if (myList.size() > 0)
      {
        if (isRight) {
          AuthorizeRight authorizeRight = new AuthorizeRight();
          authorizeRight.setRightByAuthorizeType("Y", "management");
          for (String myDefKey:myList) {  
            authorizeRightMap.put(myDefKey, authorizeRight);
            myRightMap.put(myDefKey, authorizeRight);
            authorizeIds = authorizeIds + "'" + myDefKey + "',"; }
        }
        else
        {
          for (String myDefKey : myList) {
            authorizeIds = authorizeIds + "'" + myDefKey + "',";
          }

        }

      }

    }

    List<DefAct> list = this.bpmDefActDao.getActRightByUserMap(map);
    if (list.size() > 0) {
      if (isRight)
        for (DefAct bpmDefAct : list) {
          String defKey = bpmDefAct.getDefKey();

          if (myRightMap.get(defKey) == null)
          {
            authorizeIds = authorizeIds + "'" + defKey + "',";
            String rightContent = bpmDefAct.getRightContent();
            AuthorizeRight authorizeRight = (AuthorizeRight)authorizeRightMap.get(defKey);
            if (authorizeRight != null) {
              authorizeRight.setRightByNeed("Y", rightContent, authorizeType);
            } else {
              authorizeRight = new AuthorizeRight();
              authorizeRight.setAuthorizeType(authorizeType);
              authorizeRight.setDefKey(defKey);
              authorizeRight.setRightContent(rightContent);
            }
            authorizeRightMap.put(defKey, authorizeRight);
          }
        }
      else for (DefAct bpmDefAct : list) {
          authorizeIds = authorizeIds + "'" + bpmDefAct.getDefKey() + "',";
        }


    }

    if (StringUtil.isNotEmpty(authorizeIds)) {
      authorizeIds = authorizeIds.substring(0, authorizeIds.length() - 1);
    }

    mapRight.put("authorizeIds", authorizeIds);
    mapRight.put("authorizeRightMap", authorizeRightMap);
    return mapRight;
  }


}