package com.cssrc.ibms.core.user.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.xml.parsers.DocumentBuilder;

import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.core.user.dao.PositionDao;
import com.cssrc.ibms.core.user.dao.SysOrgDao;
import com.cssrc.ibms.core.user.dao.SysUserDao;
import com.cssrc.ibms.core.user.dao.UserPositionDao;
import com.cssrc.ibms.core.user.model.Position;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.model.UserPosition;
import com.cssrc.ibms.core.util.encrypt.PasswordUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.sun.tools.internal.xjc.reader.xmlschema.bindinfo.BIConversion.User;

import org.w3c.dom.NodeList;

@Service
public class DataSynchronizationService {
	@Resource
	SysUserDao sysUserDao;
	@Resource
	SysOrgDao sysOrgDao;
	@Resource
	PositionDao positionDao;
	@Resource
	UserPositionDao userPositionDao;
	
	public void syncOrg(String url) {
		String str = "";
		try {
			URL connect = new URL(url);
			URLConnection connection = connect.openConnection();
			connection.connect();
			System.out.println("ok:网络连接成功");
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			org.w3c.dom.Document document = builder.parse(connection.getInputStream());
			NodeList nl = document.getElementsByTagName("CDepartment");
	        for(int i =0 ;i<nl.getLength();i++){
             SysOrg sysOrg=new SysOrg();
             String strOrgName = document.getElementsByTagName("strName").item(i).getFirstChild().getNodeValue();
             String nOrderNum = document.getElementsByTagName("nOrderNum").item(i).getFirstChild().getNodeValue();
             String strOrgID = document.getElementsByTagName("strOrgID").item(i).getFirstChild().getNodeValue();
             long sortno = Long.valueOf(strOrgID);
             sysOrg.setCode(strOrgID);
             sysOrg.setOrgName(strOrgName);
             sysOrg.setDemId(Long.valueOf(1001));
             sysOrg.setOrgId(sortno);
             sysOrg.setOrgSupId(Long.valueOf("10000025340019"));
             sysOrg.setOrgPathname("/单位/八部/"+strOrgName);
             sysOrg.setPath(".1001.10000025340019."+sortno+".");
             if(sysOrgDao.isOrgExist(strOrgID)) {
            	 sysOrgDao.update(sysOrg);
            	 System.out.println("----------更新了【"+sysOrg.getOrgName()+"】的信息----------");
             }else {
            	 sysOrgDao.add(sysOrg);
            	 System.out.println("----------插入了【"+sysOrg.getOrgName()+"】的信息----------");
            	 System.out.println("----------组织code【"+sysOrg.getCode()+"】----------");
                 Position position=new Position();
                 position.setJobId(Long.valueOf("10000000620003"));
                 position.setPosId(UniqueIdUtil.genId());
                 position.setPosCode(String.valueOf(UniqueIdUtil.genId()));
                 position.setPosName(strOrgName+"_成员");
                 position.setOrgId(sortno);
                 position.setIsDelete(Long.valueOf(0));
                 positionDao.add(position);
                 System.out.println("----------插入了【"+position.getPosName()+"----"+position.getOrgName()+"】的信息----------");
             }

             str = "部门信息同步完成！共更新（"+nl.getLength()+"）条记录";
             System.out.println(str);
            }
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			 str = "更新失败!!错误信息"+e.getMessage();
             System.out.println(str);
		}
	}
	public void syncUser(String url){
		String filter = "?strOrderField=&strFilterField=isWork&strFilterContent=1&strGetStaffID=&strGetDomain=&strCheckUserID=&strCheckPassword=&strCheckDomain=";
		URL connect;
		try {
			connect = new URL(url+filter);
			URLConnection connection = connect.openConnection();
	        connection.connect();
	        System.out.println("ok:网络连接成功");
	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(connection.getInputStream());
            NodeList nl = document.getElementsByTagName("CStaff");
            for(int i =0 ;i<nl.getLength();i++){
            	SysUser user = new SysUser();
            	
            	 String userName = document.getElementsByTagName("strUserID").item(i).getFirstChild()!=null?document.getElementsByTagName("strUserID").item(i).getFirstChild().getNodeValue():"";
                 // 姓名
                 String allName = document.getElementsByTagName("strName").item(i).getFirstChild()!=null?document.getElementsByTagName("strName").item(i).getFirstChild().getNodeValue():"";
                 String userId = document.getElementsByTagName("strStaffID").item(i).getFirstChild()!=null?document.getElementsByTagName("strStaffID").item(i).getFirstChild().getNodeValue():"0";
                 
                 if (StringUtil.isEmpty(userName)) {
                     System.out.println("用户"+allName+"无登录名");
                     continue;
                 }

              // 默认密码
                 String password = PasswordUtil.generatePassword("abcd123456");
                 // 密级
                 String grade = document.getElementsByTagName("strSecret").item(i).getFirstChild()!=null?document.getElementsByTagName("strSecret").item(i).getFirstChild().getNodeValue():"-1";
                 switch (grade){
                     case "核心":
                         grade = "12";
                         break;
                     case "重要":
                         grade = "9";
                         break;
                     case "一般":
                         grade = "6";
                         break;
                     default:
//                         grade = "一般"; 
                         // 除了上述三种密级外的其他人员不同步
                         continue;
                 }
              // 性别
                 String sex = document.getElementsByTagName("nCodeGender").item(i).getFirstChild()!=null?document.getElementsByTagName("nCodeGender").item(i).getFirstChild().getNodeValue():"";
                 switch (sex){
                     case "0":
                         sex = "0";
                         break;
                     case "1":
                         sex = "1";
                         break;
                     case "2":
                         sex = "0";
                         break;
                     default:
                         sex = "1";
                         break;
                 }
                 // 所在部门名称
                 String deptName = document.getElementsByTagName("strOrg4").item(i).getFirstChild()!=null?document.getElementsByTagName("strOrg4").item(i).getFirstChild().getNodeValue():""; 
                 String orgId = document.getElementsByTagName("strOrgID").item(i).getFirstChild()!=null?document.getElementsByTagName("strOrgID").item(i).getFirstChild().getNodeValue():""; 
                 System.out.println("----------当前部门编码为【"+orgId+"】----------");
//               // 将部门名称转化为部门ID
//               String deptId = getDeptIdByDeptName(deptName);
                 String email = document.getElementsByTagName("strEmail").item(i).getFirstChild()!=null?document.getElementsByTagName("strEmail").item(i).getFirstChild().getNodeValue():"";
                 user.setUserId(Long.valueOf(userId));
                 user.setPassword(password);
                 user.setUsername(userName);
                 user.setFullname(allName);
                 user.setSecurity(grade); 
                 user.setStatus(Short.valueOf("1"));
                 user.setDelFlag(Short.valueOf("0"));
                 user.setSex(Short.valueOf(sex));
                 user.setEmail(email);
                 if(sysUserDao.isUsernameExist(userName)) {
                	 sysUserDao.update(user);
                	 List<UserPosition> userPositionList=userPositionDao.getByUserId(user.getUserId());
                	 if(userPositionList.size()!=0&&orgId!=null) {
                		 UserPosition userPosition=userPositionList.get(0);
                		 userPosition.setOrgId(Long.valueOf(orgId));
                		 userPositionDao.update(userPosition);
                		 
                	 }
                	 System.out.println("----------更新了【"+user.getFullname()+"】的信息----------");
                 }
                 else {
                	 sysUserDao.add(user); 
                	 if(orgId!=null) {
                		 SysOrg sysOrg=sysOrgDao.getById(Long.valueOf(orgId));
                		 Position position=positionDao.getByOrgId(Long.valueOf(orgId),sysOrg.getOrgName()+"_成员");
                		 System.out.println("----------更新了【"+position.getPosId()+"】的信息----------");
                		 UserPosition userPosition=new UserPosition();
                		 userPosition.setUserPosId(UniqueIdUtil.genId());
                		 userPosition.setPosId(position.getPosId());
                		 userPosition.setUserId(user.getUserId());
                		 userPosition.setIsPrimary(Short.valueOf("1"));
                		 userPosition.setOrgId(Long.valueOf(orgId));
                		 userPosition.setIsCharge(Short.valueOf("0"));
                		 userPosition.setJobId(Long.valueOf("10000000620003"));
                		 userPositionDao.add(userPosition); 
                	 }
                	 
                	 
                	 System.out.println("----------插入了【"+user.getFullname()+"】的信息----------");
                 }

            }
            String str = "人员信息同步完成！";
            System.out.println(str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
