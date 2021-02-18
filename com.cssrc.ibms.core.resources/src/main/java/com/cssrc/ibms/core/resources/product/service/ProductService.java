package com.cssrc.ibms.core.resources.product.service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.core.flow.service.impl.ScriptImpl;
import com.cssrc.ibms.core.resources.product.dao.ProductDao;

@Service
public class ProductService {
	
	@Resource
	private ProductDao pDao;	
	@Resource
	private ScriptImpl scriptImpl;
	@Resource
	private ProductTypeDao productTypeDao;
	/**
	 * 根据目录树节点ID，查询其所有子节点
	 * @param typeId 当前目录树节点Id
	 * @return 所有子节点
	 */
//	public String queryChildrenNodeById(String typeId) {
//		List<String> idList = new ArrayList<String>();	
//		idList.add(typeId);		
//		
//		String idAll = ptreeDao.queryChildrenNodeById(typeId,idList);
//		String idString = idAll.substring(1, idAll.length()-1);
//		return idString;
//	}
	
	/** 根据类型查询查询目录树所有信息  */
	public List<Map<String, Object>> queryTreeAllByType(String flag){
		return pDao.queryTreeAllByType(flag);
	}
	/** 查询目录树所有信息  */
	public List<Map<String, Object>> queryTreeAll(){
		return pDao.queryTreeAll();
	}
	/**
	 * @param isAdd true:添加节点 false：编辑节点
	 * @param typeId
	 * @param parentId
	 * @param typeName
	 * @param username
	 * @param currentUserID
	 */
//	public void operateNode(Boolean isAdd,Long typeId,Long parentId,String typeName,String username,String currentUserID) {
//		if (isAdd) {
//			//添加节点
//			ptreeDao.addNode(typeId,parentId,typeName,username,currentUserID);
//		} else {
//			//修改节点
//			ptreeDao.modifyNodeById(parentId,typeName);
//		}
//	}	
	
	/**
	 * 获取当前用户角色
	 */
	public String getCurrentRole() {
		List<ISysRole> currentUserRole = scriptImpl.getCurrentUserRoles();
		String role = "";
		for (ISysRole iSysRole: currentUserRole) {
			role = iSysRole.getRoleName();
		}
		return role;
	}
	
	/** 根据F_TCPX查询ID  */
	public String selectIdByTcpx(String tcpx,String prId){
		return pDao.selectIdByTcpx(tcpx,prId);
	}
	
	/** 根据ID查询F_TCPX  */
	public String selectTcpxById(String id){
		return pDao.selectTcpxById(id);
	}
	
	/** 根据ID更新F_TCPX  */
	public void updateTcpxById(String id,String tcpx){
		 pDao.updateTcpxById(id,tcpx);
	}
	
	/** 根据F_TCPX查询ID(型号层)  */
	public String selectIdByTcpxFromProduct(String tcpx){
		return pDao.selectIdByTcpxFromProduct(tcpx);
	}
	
	/** 根据fcId查询发次名  */
	public String selectNameByfcId(String fcId){
		return pDao.selectNameByfcId(fcId);
	}
	
	/** 根据ID查询父节点ID(发次层)  */
	public String selectPridByIdFromProject(String Id){
		return pDao.selectPridByIdFromProject(Id);
	}
	
	
	
	/** 根据ID查询F_TCPX(型号层)  */
	public String selectTcpxByIdFromProduct(String id){
		return pDao.selectTcpxByIdFromProduct(id);
	}
	
	/** 根据ID更新F_TCPX(型号层)  */
	public void updateTcpxByIdFromProduct(String id,String tcpx){
		 pDao.updateTcpxByIdFromProduct(id,tcpx);
	}
	
	/** 根据F_TCPX更新 ID */
	public void updateIdByTcpx(String id,String tcpx){
		 pDao.updateIdByTcpx(id,tcpx);
	}
	/** 查询tcpx为空（型号层） */
	public List selectNullTcpxproduct () {
    	List list = new ArrayList();
        list = pDao.selectNullTcpxproduct();
        return list;
    }
	
	/** 查询tcpx为空（发次层） */
	public List selectNullTcpxproject () {
    	List list = new ArrayList();
        list = pDao.selectNullTcpxproject();
        return list;
    }
	
	/** 根据父节点ID查询该父节点下最大F_TCPX的后一位  */
	public String selectTcpxByParentId(String id){
		String max="";
		List<Map<String,Object>> s = pDao.selectByParentId(id);
		List<String> result = new ArrayList<String>();
		if(s.size()>0){      //如果大于0说明该父节点下存在子节点
			for(int i=0;i<s.size();i++){
				for(Map<String, Object> resMap : s){   //取出F_TCPX
					String tcpx = CommonTools.Obj2String(resMap.get("F_TCPX"));
					if("".equals(tcpx)){   //如果F_TCPX是空值时则不取出
						
					}else{
						result.add(tcpx);
					}
				}	
			}
			if(result.size()>0){
				int Max = Integer.parseInt(result.get(0));   //将最大值的初始值设为第一个F_TCPX
				for(int j=0;j<result.size();j++){
					int tcpx= Integer.parseInt(result.get(j));
					if(Max<tcpx){
						Max = tcpx;
					}
				}
				Max = Max+1;           //将得到的最大值加一，作为新增节点的F_TCPX，则默认新增节点处于最后一个
				max = String.valueOf(Max);
			}else{
			//	String tcpx=pDao.selectTcpxByIdFromProduct(id);
			//	int Tcpx=Integer.parseInt(tcpx);
				int Max = 1;          //将父节点的F_TCPX乘10作为新增节点的F_TCPX
				max = String.valueOf(Max);
			}
			
			
		}else{      //如果该父节点下没有子节点，即新增节点是该父节点的第一个子节点
			
			
		}
		
		return max;
		
		
	}
	
	
	/** 根据父节点ID查询该父节点下最大F_TCPX的后一位 (型号层) */
	public String selectTcpxByParentIdFromProduct(){
		String max="";
		List<Map<String,Object>> s = pDao.selectByParentIdFromProduct();
		List<String> result = new ArrayList<String>();
		if(s.size()>0){
			for(int i=0;i<s.size();i++){
				for(Map<String, Object> resMap : s){
			//		result.add(CommonTools.Obj2String(resMap.get("F_TCPX")));
					String tcpx = CommonTools.Obj2String(resMap.get("F_TCPX"));
					if("".equals(tcpx)){
						
					}else{
						result.add(tcpx);
					}
				}	
			}
			int Max = Integer.parseInt(result.get(0));
			for(int j=0;j<result.size();j++){
				int tcpx= Integer.parseInt(result.get(j));
				if(Max<tcpx){
					Max = tcpx;
				}
			}
			Max = Max+1;
			max = String.valueOf(Max);
			
		}else{
			
			int Max = 1;
			max = String.valueOf(Max);
			
		}
		
		return max;
		
		
	}
	
	/** 根据tcpx找出上一个tcpx */
	public String selectUptcpxBytcpx(String tcpx,String prId){
		String upTcpx="";
		int j=0;
		if(prId.equals("0")){
			List<Map<String,Object>> list = pDao.selectAllFromproduct();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap=list.get(i);
					String Tcpx = resMap.get("F_TCPX").toString();
					if(Tcpx.equals(tcpx)){
						j=i;
						break;
					}
					
					}	
				if(j>0){
				upTcpx=list.get(j-1).get("F_TCPX").toString();
				}
				}
		}else{
			List<Map<String,Object>> list = pDao.selectAllFromproject(prId);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap=list.get(i);
					String Tcpx = resMap.get("F_TCPX").toString();
					if(Tcpx.equals(tcpx)){
						j=i;
						break;
					}
					
					}	
				if(j>0){
				upTcpx=list.get(j-1).get("F_TCPX").toString();
				}
				}
		}
		
		return upTcpx;
	}
	
	/** 根据tcpx找出下一个tcpx */
	public String selectDowntcpxBytcpx(String tcpx,String prId){
		String downTcpx="";
		int j=0;
		if(prId.equals("0")){
			List<Map<String,Object>> list = pDao.selectAllFromproduct();
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap=list.get(i);
					String Tcpx = resMap.get("F_TCPX").toString();
					if(Tcpx.equals(tcpx)){
						j=i;
						break;
					}
					
					}	
				if(j<list.size()-1){
				downTcpx=list.get(j+1).get("F_TCPX").toString();
				}
				}
		}else{
			List<Map<String,Object>> list = pDao.selectAllFromproject(prId);
			if(list.size()>0){
				for(int i=0;i<list.size();i++){
					Map<String, Object> resMap=list.get(i);
					String Tcpx = resMap.get("F_TCPX").toString();
					if(Tcpx.equals(tcpx)){
						j=i;
						break;
					}
					
					}	
				if(j<list.size()-1){
				downTcpx=list.get(j+1).get("F_TCPX").toString();
				}
				}
		}
		return downTcpx;
	}
	
	
	/**
	 * @Author  shenguoliang
	 * @Description:获取数据包的发次下面的节点对应的负责人
	 * @Params [fcId, userId]
	 * @Date 2018/5/19 9:56
	 * @Return boolean
	 */
	public boolean getNodeCharger(String fcId, Long userId) {

		boolean flag = false ;
		List<Map<String, Object>> dataList = pDao.getNodeChargerByProjectId(fcId,userId) ;
		for(Map map : dataList){
			long fzrId = CommonTools.Obj2Long(map.get("F_FZRID"));
			if(fzrId == userId){
				flag = true  ;
				break ;
			}
		}
		return flag;
	}
	
	/**
	 * Description : 判断当前用户是否在当前节点的工作队或负责人中
	 * Author : XYF
	 * Date : 2018年10月12日上午11:21:20
	 * Return : boolean
	 */
	public boolean getNodeChargerAndWorkTeam(String fcId, Long userId) {

		boolean flag = false ;
		List<Map<String, Object>> dataList = pDao.getNodeChargerByProjectId(fcId,userId) ;
		String person = getWorkTeamPerson(fcId);
		String[] list = person.split(",");
		for(Map map : dataList){
			long fzrId = CommonTools.Obj2Long(map.get("F_FZRID"));
			if(fzrId == userId){
				flag = true  ;
				break ;
			}
		}
		for(int i=0;i<list.length;i++){
			String Person = list[i];
			String UserId = userId.toString();
			if(Person.equals(UserId)){
				flag = true  ;
				break ;
			}
		}
		return flag;
	}
	
	/**
	 * Description : 通过发次ID查询该发次下所有的工作队人员
	 * Author : XYF
	 * Date : 2018年10月12日下午1:36:30
	 * Return : List<Map<String,Object>>
	 */
	public String getWorkTeamPerson(String fcId){
		List<Map<String, Object>> nodeId = pDao.getNodeIdByProjectId(fcId);
		String NodeId="";
		List<Map<String, Object>> person = new ArrayList<Map<String,Object>>();
		if(nodeId.size()==1){
			NodeId = "('"+nodeId.get(0).get("ID")+"')";
		}else{
			for(int i=0;i<nodeId.size();i++){
				if(i==0){
					NodeId = "('"+nodeId.get(i).get("ID")+"'";
				}else if(i==nodeId.size()-1){
					NodeId = NodeId + ",'"+nodeId.get(i).get("ID")+"')";
				}else{
					NodeId = NodeId + ",'"+nodeId.get(i).get("ID")+"'";
				}
			}
		}
		if(!"".equals(NodeId)){
			person =  pDao.getWorkTeamTeamPersonByProjectId(NodeId);
		}
		
		String Person="";
		for(int j=0;j<person.size();j++){
			if(j==0){
				Person = person.get(j).get("F_CYID").toString();
			}else{
				Person = Person +","+ person.get(j).get("F_CYID").toString();
			}
		}
		return Person;
	}

	public String getCondResultNameByInsId(Long slId) {
		Map<String, Object> typeMap = new HashMap<String, Object>() ;
		typeMap = productTypeDao.getProductType(slId) ;
//		String flag = CommonTools.Obj2String(typeMap.get("TYPE"));
		String ck_resultName = "W_CK_RESULT_CARRY" ;
//		if("空间".equals(typeMap.get("TYPE"))){
//			ck_resultName = "W_CK_RESULT" ;
//		}else if("运载".equals(typeMap.get("TYPE"))){
//			ck_resultName = "W_CK_RESULT_CARRY" ;
//		}else{
//			ck_resultName = "W_CK_RESULT_JGJG" ;
//		}
		return ck_resultName;
	}
	
	/**
	 * Description : 获取所有型号
	 * Author : XYF
	 * Date : 2018年10月18日下午6:15:58
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getAllProduct (String product) {
		List<Map<String,Object>> list = new ArrayList();
		if("".equals(product)){
			list = pDao.selectAllFromproduct();
		}else{
			list = pDao.getAllProduct(product);
		}
        
        return list;
    }
	/**
	 * Description : 获取项目办人员管理表中所有数据
	 * Author : XYF
	 * Date : 2018年10月19日上午9:44:18
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getProjectOffice () {
		List<Map<String,Object>> list = new ArrayList();
        list = pDao.getProjectOffice();
        return list;
    }
	/**
	 * Description : 根据型号ID查询型号信息
	 * Author : XYF
	 * Date : 2018年10月18日下午6:19:09
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProductById (String Id) {
		List<Map<String,Object>> list = new ArrayList();
        list = pDao.selectProductById(Id);
        return list;
    }
	/**
	 * Description : 根据发次ID查询发次信息
	 * Author : XYF
	 * Date : 2018年10月19日下午3:29:22
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProjectById (String Id) {
		List<Map<String,Object>> list = new ArrayList();
        list = pDao.selectProjectById(Id);
        return list;
    }
	/**
	 * Description : 根据型号ID查询项目办人员管理信息
	 * Author : XYF
	 * Date : 2018年10月19日下午2:04:17
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> getProjectOffById (String Id) {
		List<Map<String,Object>> list = new ArrayList();
        list = pDao.getProjectOffById(Id);
        return list;
    }
	
	/**
	 * Description : 将型号名称保存至项目办人员管理
	 * Author : XYF
	 * Date : 2018年10月19日上午8:23:07
	 * Return : void
	 */
	public void saveProductName(String Id,String productName,String type){
		pDao.saveProductName(Id,productName,type);
	}

}
