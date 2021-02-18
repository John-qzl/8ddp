package com.cssrc.ibms.core.log.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.api.sysuser.model.IUserRole;
import com.cssrc.ibms.api.sysuser.model.IUserUnder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.DataNote;
import com.cssrc.ibms.core.util.common.CommonTools;

/**
 * 用户相关表特殊处理
 * @author liubo
 * @date 2017年8月24日上午8:24:21
 */
@Service
public class SysLogCustomService {
	public static final String USERPOSITION_CLASS = "com.cssrc.ibms.core.user.model.UserPosition";
	public static final String USERROLE_CLASS = "com.cssrc.ibms.core.user.model.UserRole";
	public static final String USERUNDER_CLASS = "com.cssrc.ibms.core.user.model.UserUnder";
	
	public static List<Map<String,Object>> userPositionOldList = new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> userRoleOldList = new ArrayList<Map<String,Object>>();
	public static List<Map<String,Object>> userUnderOldList = new ArrayList<Map<String,Object>>();
	
	@Resource
	private SysLogService sysLogService;
	@Resource
	private ISysUserService sysUserService;

	/**
	 * 获取用户表相关old数据
	 *@author liubo
	 *@date 2017-8-26
	 *@param methodDataNote
	 *@param userId 主键值
	 *@return
	 */
	public void getUserDataOldList(DataNote methodDataNote,Long userId){
		for(int c=0;c<methodDataNote.beanName().length;c++){
			Class<?> beanName = methodDataNote.beanName()[c];
			 if(beanName.getName().equals(USERPOSITION_CLASS)){
				 userPositionOldList = sysLogService.getUserDataListByBean(beanName,userId);
			 }else if(beanName.getName().equals(USERROLE_CLASS)){
				 userRoleOldList = sysLogService.getUserDataListByBean(beanName,userId);
			 }else if(beanName.getName().equals(USERUNDER_CLASS)){
				 userUnderOldList = sysLogService.getUserDataListByBean(beanName,userId);
			 }
		}
	}
	
	/**
	 * 拼接显示人员相关表数据
	 *@author liubo
	 *@date 2017-8-23
	 *@param detail 描述
	 *@param methodDataNote
	 *@param userId
	 *@return
	 */
	public String getUserDataListByBean(String detail,DataNote methodDataNote,Long userId){
		//用户组织岗位表
		String userPositionDetail = "<br/>所在组织岗位变化如下：<br/>";
		//用户角色映射
		String userRoleDetail = "<br/>所在用户角色映射变化如下：<br/>";
		//下属管理表
		String userUnderDetail = "<br/>相关下属变化如下：<br/>";
		
		for(int i=0;i<methodDataNote.beanName().length;i++){
			
			Class<?> beanName = methodDataNote.beanName()[i];
			String className = beanName.getName();
			//Field[] fieldsObject = beanName.getDeclaredFields();
			
			if(className.equals(USERPOSITION_CLASS)){
				List<Map<String,Object>> list = sysLogService.getUserDataListByBean(beanName,userId);
				
				//未做任何变化
				if(userPositionOldList.size()==0&&list.size()==0){
					userPositionDetail = "";
				}else if(userPositionOldList.size()==0){//全是新增
					//临时明细
					String temDetail = "该用户新增组织岗位：";
					for(int n=0;n<list.size();n++){
						//初始化表数据map--new
						IUserPosition newMap=(IUserPosition) list.get(n);
						
						//组织名
						String orgName = newMap.getOrgName();
						//岗位名
						String posName = newMap.getPosName();
						
						temDetail += orgName+"【"+posName+"】 ";
					}
					userPositionDetail += temDetail;
				}else if(list.size()==0){//全部删除
					//临时明细
					String temDetail = "该用户移除组织岗位：";
					for(int n=0;n<userPositionOldList.size();n++){
						//初始化表数据map--old
						IUserPosition oldMap=(IUserPosition) userPositionOldList.get(n);
						
						//组织名
						String orgName = oldMap.getOrgName();
						//岗位名
						String posName = oldMap.getPosName();
						
						temDetail += orgName+"【"+posName+"】 ";
					}
					userPositionDetail += temDetail;
				}else{//添加明细信息记录功能模块数据的更新和修改
					//临时明细
					String temDetail = "";
					
					for(int d=0;d<userPositionOldList.size();d++){
						//初始化表数据map--old
						IUserPosition oldMap=(IUserPosition) userPositionOldList.get(d);
						
						//主键--old
						String oldPk = oldMap.getOrgId().toString() + oldMap.getPosId().toString();
						//组织名
						String orgName = oldMap.getOrgName();
						//岗位名
						String posName = oldMap.getPosName();
						//是否主岗位
						Short isPrimary = oldMap.getIsPrimary();
						//是否负责人
						Short isCharge = oldMap.getIsCharge();
						
						//新数据中是否存在该对应数据
						Boolean isExit = false;
						
						for(int n=0;n<list.size();n++){
							//初始化表数据map--new
							IUserPosition newMap=(IUserPosition) list.get(n);
							
							//主键--new
							String newPk = newMap.getOrgId().toString() + newMap.getPosId().toString();
							
							if(newPk.equals(oldPk)){
								isExit = true;
								
								//比较数据
								//是否主岗位
								Short isPrimaryNew = newMap.getIsPrimary();
								//是否负责人
								Short isChargeNew = newMap.getIsCharge();
								//判断是否改变
								boolean isPrimaryChange=CommonTools.isNoEqual(isPrimary,isPrimaryNew);
								if(isPrimaryChange){
									if(isPrimaryNew.equals(1))
										temDetail += "将该用户的主岗位设置为"+orgName+"【"+posName+"】；";
									else
										temDetail += "将"+orgName+"【"+posName+"】设置为该用户的非主岗位；";
								}
									
								boolean isChargeChange=CommonTools.isNoEqual(isCharge,isChargeNew);
								if(isChargeChange){
									if(isChargeNew.equals(1))
										temDetail += "设置该用户为"+orgName+"【"+posName+"】的主要负责人；";
									else
										temDetail += "设置该用户为"+orgName+"【"+posName+"】的非主要负责人；";
								}
							}
							
						}
						
						if(!isExit)
							temDetail += "该用户移除组织岗位 "+orgName+"【"+posName+"】；";
						
					}
					//判断是否有新增数据
					for(int n=0;n<list.size();n++){
						Boolean isExitNew = false;
						//初始化表数据map--new
						IUserPosition newMap=(IUserPosition) list.get(n);
						//主键--new
						String newPk = newMap.getOrgId().toString() + newMap.getPosId().toString();
						//组织名
						String orgName = newMap.getOrgName();
						//岗位名
						String posName = newMap.getPosName();
						for(int d=0;d<userPositionOldList.size();d++){
							//初始化表数据map--old
							IUserPosition oldMap=(IUserPosition) userPositionOldList.get(d);
							
							//主键--old
							String oldPk = oldMap.getOrgId().toString() + oldMap.getPosId().toString();
							
							if(oldPk.equals(newPk))
								isExitNew = true;
						}
						if(!isExitNew)
							temDetail += "该用户新增组织岗位 "+orgName+"【"+posName+"】；";
					}
					if(temDetail.isEmpty())
						userPositionDetail = "";
					else
						userPositionDetail += temDetail;
				}
				
				detail = getDetail(detail,userPositionDetail,"UserPosition");
			}else if(className.equals(USERROLE_CLASS)){
				List<Map<String,Object>> list = sysLogService.getUserDataListByBean(beanName,userId);
					
				//未做任何变化
				if(userRoleOldList.size()==0&&list.size()==0){
					userRoleDetail = "";
				}else if(list.size()==0){//全是新增
					//临时明细
					String temDetail = "该用户新增用户角色：";
					for(int n=0;n<list.size();n++){
						//初始化表数据map--new
						IUserRole newMap=(IUserRole) list.get(n);
						
						//角色名
						String roleName = newMap.getRoleName();
						
						temDetail += "【"+roleName+"】 ";
					}
					userRoleDetail += temDetail;
				}else if(list.size()==0){//全部删除
					//临时明细
					String temDetail = "该用户移除用户角色：";
					for(int n=0;n<userRoleOldList.size();n++){
						//初始化表数据map--old
						IUserRole oldMap=(IUserRole) userRoleOldList.get(n);
						
						//角色名
						String roleName = oldMap.getRoleName();
						
						temDetail += "【"+roleName+"】 ";
					}
					userRoleDetail += temDetail;
				}else{//添加明细信息记录功能模块数据的更新和修改
					//临时明细
					String temDetail = "";
					for(int d=0;d<userRoleOldList.size();d++){
						//初始化表数据map--old
						IUserRole oldMap=(IUserRole) userRoleOldList.get(d);
						
						//主键--old
						Long oldPk = oldMap.getRoleId();
						//角色名
						String roleName = oldMap.getRoleName();
						
						//新数据中是否存在该对应数据
						Boolean isExit = false;
						
						for(int n=0;n<list.size();n++){
							//初始化表数据map--new
							IUserRole newMap=(IUserRole) list.get(n);
							
							//主键--new
							Long newPk = newMap.getRoleId();
							
							if(newPk.equals(oldPk))
								isExit = true;
							
						}
						
						if(!isExit)
							temDetail += "该用户移除用户角色 "+"【"+roleName+"】；";
						
					}
					//判断是否有新增数据
					for(int n=0;n<list.size();n++){
						Boolean isExitNew = false;
						//初始化表数据map--new
						IUserRole newMap=(IUserRole) list.get(n);
						//主键--new
						Long newPk = newMap.getRoleId();
						//角色名
						String roleName = newMap.getRoleName();
						for(int d=0;d<userRoleOldList.size();d++){
							//初始化表数据map--old
							IUserRole oldMap=(IUserRole) userRoleOldList.get(d);
							
							//主键--old
							Long oldPk = oldMap.getRoleId();
							
							if(oldPk.equals(newPk))
								isExitNew = true;
						}
						if(!isExitNew)
							temDetail += "该用户新增用户角色 "+"【"+roleName+"】；";
					}
					if(temDetail.isEmpty())
						userRoleDetail = "";
					else
						userRoleDetail += temDetail;
					
				}
				detail = getDetail(detail,userRoleDetail,"UserRole");
			}else if(className.equals(USERUNDER_CLASS)){
				List<Map<String,Object>> list = sysLogService.getUserDataListByBean(beanName,userId);
				
				//未做任何变化
				if(userUnderOldList.size()==0&&list.size()==0){
					userUnderDetail = "";
				}else if(list.size()==0){//全是新增
					//临时明细
					String temDetail = "该用户新增上级组织：";
					for(int n=0;n<list.size();n++){
						//初始化表数据map--new
						IUserUnder newMap=(IUserUnder) list.get(n);
						
						//上级领导
						String underusername = newMap.getUnderusername();
						
						temDetail += "【"+underusername+"】 ";
					}
					userUnderDetail += temDetail;
				}else if(list.size()==0){//全部删除
					//临时明细
					String temDetail = "该用户移除上级组织：";
					for(int n=0;n<userUnderOldList.size();n++){
						//初始化表数据map--old
						IUserUnder oldMap=(IUserUnder) userUnderOldList.get(n);
						
						//上级领导
						String underusername = oldMap.getUnderusername();
						
						temDetail += "【"+underusername+"】 ";
					}
					userUnderDetail += temDetail;
				}else{//添加明细信息记录功能模块数据的更新和修改
					//临时明细
					String temDetail = "";
					for(int d=0;d<userUnderOldList.size();d++){
						//初始化表数据map--old
						IUserUnder oldMap=(IUserUnder) userUnderOldList.get(d);
						
						//主键--old
						Long oldPk = oldMap.getUnderuserid();
						//角色名
						String underusername = oldMap.getUnderusername();
						
						//新数据中是否存在该对应数据
						Boolean isExit = false;
						
						for(int n=0;n<list.size();n++){
							//初始化表数据map--new
							IUserUnder newMap=(IUserUnder) list.get(n);
							
							//主键--new
							Long newPk = newMap.getUnderuserid();
							
							if(newPk.equals(oldPk))
								isExit = true;
							
						}
						
						if(!isExit)
							temDetail += "该用户移除上级组织 "+"【"+underusername+"】；";
						
					}
					//判断是否有新增数据
					for(int n=0;n<list.size();n++){
						Boolean isExitNew = false;
						//初始化表数据map--new
						IUserUnder newMap=(IUserUnder) list.get(n);
						//主键--new
						Long newPk = newMap.getUnderuserid();
						//角色名
						String underusername = newMap.getUnderusername();
						for(int d=0;d<userUnderOldList.size();d++){
							//初始化表数据map--old
							IUserUnder oldMap=(IUserUnder) userUnderOldList.get(d);
							
							//主键--old
							Long oldPk = oldMap.getUnderuserid();
							
							if(oldPk.equals(newPk))
								isExitNew = true;
						}
						if(!isExitNew)
							temDetail += "该用户新增上级组织 "+"【"+underusername+"】；";
						userUnderDetail += temDetail;
					}
					if(temDetail.isEmpty())
						userUnderDetail = "";
					else
						userUnderDetail += temDetail;
				}
				detail = getDetail(detail,userUnderDetail,"UserUnder");
			}
			
		}
		return detail;
	}
	
	private String getDetail(String detail,String temDetail,String className){
		//获取操作用户权限
		Long currentUserId =  UserContextUtil.getCurrentUserId();
		if(className.equals("UserPosition")&&!currentUserId.equals(ISysUser.RIGHT_USER)){//权限管理员不能保存组织相关信息
			detail += temDetail;
		}

		if(className.equals("UserRole")&&(currentUserId.equals(ISysUser.RIGHT_USER)||currentUserId.equals(ISysUser.IMPLEMENT_USER))){//系统管理员不能保存人员角色信息
			detail += temDetail;
		}

		if(className.equals("UserUnder")&&currentUserId.equals(ISysUser.IMPLEMENT_USER)){//权限管理员和系统管理员不能保存上级领导信息
			detail += temDetail;
		}
		
		return detail;
	}
}
