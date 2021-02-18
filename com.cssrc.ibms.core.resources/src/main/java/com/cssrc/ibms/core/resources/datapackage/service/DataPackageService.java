package com.cssrc.ibms.core.resources.datapackage.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.CheckDataPackageInfoDao;
import com.cssrc.ibms.core.resources.datapackage.dao.CheckInstantDao;
import com.cssrc.ibms.core.resources.datapackage.dao.CheckPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.CheckWorkTeamDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.TeamDao;
import com.cssrc.ibms.core.resources.datapackage.model.CheckDataPackageInfo;
import com.cssrc.ibms.core.resources.datapackage.model.CheckInstant;
import com.cssrc.ibms.core.resources.datapackage.model.CheckPackage;
import com.cssrc.ibms.core.resources.datapackage.model.CheckWorkTeam;
import com.cssrc.ibms.core.resources.io.bean.pack.SimplePackage;
import com.cssrc.ibms.core.resources.io.util.IOConstans;
import com.cssrc.ibms.core.resources.project.dao.ProjectDao;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.common.ListUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.dp.form.dao.CheckResultCarryDao;
import com.cssrc.ibms.dp.form.dao.CheckResultDao;
import com.cssrc.ibms.dp.form.dao.CheckResultJgjgDao;
import com.cssrc.ibms.dp.form.dao.CkConditionResultDao;
import com.cssrc.ibms.dp.form.dao.FormFolderDao;
import com.cssrc.ibms.dp.form.dao.ProductTypeDao;
import com.cssrc.ibms.dp.form.dao.SignResultDao;
import com.cssrc.ibms.dp.form.model.CheckResult;
import com.cssrc.ibms.dp.form.model.CheckResultCarry;
import com.cssrc.ibms.dp.form.model.CheckResultJgjg;
import com.cssrc.ibms.dp.form.model.CkConditionResult;
import com.cssrc.ibms.dp.form.model.SignResult;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@Service
public class DataPackageService {
	
	@Resource
	private DataPackageDao dataPackageDao;
	@Resource
	private PackageDao packageDao;
	@Resource
	private CheckPackageDao checkPackageDao;
	@Resource
	private CheckDataPackageInfoDao checkDataPackageInfoDao;
	@Resource
	private CkConditionResultDao ckConditionResultDao;
	@Resource
	private SignResultDao signResultDao;
	@Resource
	private CheckInstantDao checkInstantDao;
	@Resource
	private CheckWorkTeamDao checkWorkTeamDao;
	@Resource
	private ProjectDao projectDao;
	@Resource
	private TeamDao teamDao;
	@Resource
	private JdbcDao jdbcDao;
	@Resource
	ProductTypeDao productTypeDao;
	@Resource
    CheckResultDao checkresultDao;
	@Resource
    CheckResultCarryDao checkresultCarryDao;
    @Resource
    CheckResultJgjgDao checkresultJgjgDao;
    @Resource
    private FormFolderDao formFolderDao;
	
	/**
	 * 清空805数据包所有业务表
	 */
	public void delAllData() {
		//型号表
		dataPackageDao.delAllData("W_PRODUCT");
		//发次表
		dataPackageDao.delAllData("W_PROJECT");
		//数据包节点表
		dataPackageDao.delAllData("W_PACKAGE");
		//工作队
		dataPackageDao.delAllData("W_WORKTEAM");
		//数据包详细信息表
		dataPackageDao.delAllData("W_DATAPACKAGEINFO");
		//检查表模板
		dataPackageDao.delAllData("W_TABLE_TEMP");
		//模板文件夹
		dataPackageDao.delAllData("W_TEMP_FILE");
		//表格实例
		dataPackageDao.delAllData("W_TB_INSTANT");
		//签署结果
		dataPackageDao.delAllData("W_SIGNRESULT");
		//检查条件结果
		dataPackageDao.delAllData("W_CONDI_RES");
		//检查结果
		dataPackageDao.delAllData("W_CK_RESULT");
		dataPackageDao.delAllData("W_CK_RESULT_CARRY");
		dataPackageDao.delAllData("W_CK_RESULT_JGJG");
		//检查条件
		dataPackageDao.delAllData("W_CK_CONDITION");
		//检查项定义
		dataPackageDao.delAllData("W_ITEMDEF");
		//签署定义
		dataPackageDao.delAllData("W_SIGNDEF");
		//数据包结构树导出表
		dataPackageDao.delAllData("W_SJBJGSDCB");
		//工作规划
		dataPackageDao.delAllData("W_TASK");
	}
	
	/** 根据数据包id和pid查询符合条件的数据包结构树  */
	public List<Map<String, Object>> queryDataPackageInfo(String id, String pid){
		return packageDao.queryDataPackageInfo(id, pid);
	}
	
	/** 查询数据包结构树普通分类节点  (PARENTID=0)*/
	public List<Map<String, Object>> queryPackageNormalNode(String pId){
		return packageDao.queryPackageNormalNode(pId);
	}
	
	/** 查询数据包结构树非普通分类节点 (PARENTID=0) */
	public List<Map<String, Object>> queryPackageSpecNode(String pId){
		return packageDao.queryPackageSpecNode(pId);
	}
	
	/** 根据发次id查询发次名称  */
	public String getProjectNameById(String projectId) {
		Map<String,Object> projectName = packageDao.getProjectNameById(projectId);
		if (projectName == null) {
			return "false";
		} else {
			return (String) projectName.get("F_FCMC");
		}
	}
	
	/** 根据数据包id删除数据包信息*/
	public int deleteData(String id) throws Exception{		
		int count = 1;
		try {
			Map<String,Object> map = packageDao.getById(Long.valueOf(id));
			SimplePackage pack= new SimplePackage(map);
			if(pack.getJdlx().equals(IOConstans.SIMPLE_PACKAGE_NODE)) {
				boolean isEmpty = packageDao.isEmptyPackgaeNode(id);
				if(isEmpty) {
					count = packageDao.deleteData(id);
				}else {
					count = -2;
				}
			}else if(pack.getJdlx().equals(IOConstans.TEST_PACKAGE_NODE)||pack.getJdlx().equals(IOConstans.PART_PACKAGE_NODE)||
					pack.getJdlx().equals(IOConstans.DESI_PACKAGE_NODE)||pack.getJdlx().equals(IOConstans.SOFT_PACKAGE_NODE)){
				boolean isEmpty = packageDao.isEmptyPackgaeNode(id);
				if(isEmpty) {
					count = packageDao.deleteData(id);
				}else {
					count = -2;
				}
			}else {
				packageDao.deleteById(id,true);
			}
		}catch(Exception e) {
			e.printStackTrace();
			count =-1;
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly(); 
		}
		return count;
	}
	/** 根据型号id删除关联数据包信息*/
	public void deleteDataByXHId(String id) {
		List<Map<String, Object>> idList = projectDao.queryProjectById(id);
		if (idList.size() > 0) {
			for (Map<String, Object> map : idList) {
				String nodeId = CommonTools.Obj2String(map.get("ID"));
				projectDao.deleteById(nodeId);
				deleteDataByFCId(nodeId);
			}
		}
	}
	/** 根据发次id删除关联数据包信息*/
	public void deleteDataByFCId(String id) {
		List<Map<String, Object>> idList = packageDao.queryDataPackageById(id);
		if (idList.size() > 0) {
			for (Map<String, Object> map : idList) {
				String nodeId = CommonTools.Obj2String(map.get("ID"));
				packageDao.deleteById(nodeId, true);
			}
		}
	}
	/** 根据数据包详细信息id删除表单实例，录入数据*/
	public void deleteResult(String id) {
		String[] ids = id.split(",");
		for (int i = 0; i < ids.length; i++) {
			dataPackageDao.deleteById(ids[i], true);
		}
	}
	
	/**
	 * @param buttonName :要校验的按钮名称
	 * @param sssjb ： 所属数据包
	 * @param ids ： 要校验的记录主键（123,2342,...）
	 * @param canOperaterZt ：可以操作的执行状态 
	 * @return
	 */
	public JSONObject getButtonRight(String buttonName,Long sssjb,String ids,String[] canOperaterZt) {
		JSONObject rtn = new JSONObject();
		rtn.put("isCan", true);
		switch(buttonName) {
		case "add":
		case "pladd":
			getAddRight(rtn,sssjb);
			break;
		case "del":
			getDeleteRight(rtn,sssjb,ids,canOperaterZt);
			break;
		case "qzdel":
			getQzdeleteRight(rtn,sssjb,ids);
			break;
		case "edit":
			getEditRight(rtn,sssjb,ids);
			break;
		case "pad":
			getPadRight(rtn,sssjb,ids,canOperaterZt);
			break;
		case "formEdit":
			// 废弃，暂时不需要
//			getFormEditRight(rtn, sssjb, ids, canOperaterZt);
				break;
		}

		return rtn;
	}

	/**
	 * @Author  shenguoliang
	 * @Description: 获取能否修改表单录入的权限
	 * @Params [rtn, sssjb, ids, canOperaterZt]
	 * @Date 2018/6/4 11:04
	 * @Return void
	 */
	private void getFormEditRight(JSONObject rtn, Long sssjb, String ids, String[] canOperaterZt) {
		String sjbId = Long.toString(sssjb);
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		String UserId = Long.toString(userId);
		String FzrId = dataPackageDao.selectFzrIdById(sjbId);
		//isCanOperater 为false 代表 该表单实例仍处于 未开始或者 进行中的状态
		boolean isCanOperater = dataPackageDao.canOperaterZt(ids, canOperaterZt);
		if(!UserId.equals(FzrId)){
			if(isCanOperater) {
				rtn.put("isCan", false);
				String ztStr = StringUtil.getStringFromArray(canOperaterZt, ",");
				rtn.put("message", "所选信息中的执行状态必须不在【"+ztStr+"】中，请重新选择！");
			}
		}
		

	}


	/**
	 * 添加、批量添加按钮权限
	 *
	 * @param rtn
	 */
	private void getAddRight(JSONObject rtn, Long sssjb) {

	}
	/**
	 * 删除按钮权限
	 * @param rtn
	 */
	private void getDeleteRight(JSONObject rtn,Long sssjb,String ids,String[]canOperaterZt) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId, sssjb);
		//if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isBelongToTeam(userId, sssjb, ids);
		if(!isTeamPerson&&!isNodeMan) {
			rtn.put("isCan", false);
			rtn.put("message", "所选信息中有不属于本人工作队的，请重新选择！");
		}
		boolean isCanOperater = dataPackageDao.canOperaterZt(ids, canOperaterZt);
		if(!isCanOperater) {
			rtn.put("isCan", false);
			String ztStr = StringUtil.getStringFromArray(canOperaterZt, ",");
			rtn.put("message", "所选信息中的执行状态必须在【"+ztStr+"】中，请重新选择！");
		}
	}
	/**
	 * 强制删除按钮权限
	 * @param rtn
	 */
	private void getQzdeleteRight(JSONObject rtn,Long sssjb,String ids) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId, sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isBelongToTeam(userId, sssjb, ids);
		if(!isTeamPerson) {
			rtn.put("isCan", false);
			rtn.put("message", "所选信息中有不属于本人工作队的，请重新选择！");
		}
	}
	/**
	 * 编辑按钮权限
	 * @param rtn
	 */
	private void getEditRight(JSONObject rtn,Long sssjb,String ids) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId, sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isBelongToTeam(userId, sssjb, ids);
		if(!isTeamPerson&&!isNodeMan) {
			rtn.put("isCan", false);
			rtn.put("message", "您不是本工作队人员，无法编辑！");
		}
	}
	/**
	 * pad操作按钮权限
	 * @param rtn
	 */
	private void getPadRight(JSONObject rtn,Long sssjb,String ids,String[] canOperaterZt) {
		Long userId = UserContextUtil.getCurrentUser().getUserId();
		boolean isNodeMan = packageDao.isNodeMan(userId, sssjb);
		if(isNodeMan) {return;}
		boolean isTeamPerson = teamDao.isBelongToTeam(userId, sssjb, ids);
		if(!isTeamPerson) {
			rtn.put("isCan", false);
			rtn.put("message", "所选信息中有不属于本人工作队的，请重新选择！");
		}
		boolean isCanOperater = dataPackageDao.canOperaterZt(ids, canOperaterZt);
		if(!isCanOperater) {
			rtn.put("isCan", false);
			String ztStr = StringUtil.getStringFromArray(canOperaterZt, ",");
			rtn.put("message", "所选信息中的执行状态必须在【"+ztStr+"】中，请重新选择！");
		}
	}
	@SuppressWarnings({"unchecked", "rawtypes" })
	public void importTreeInfo(Long sourceId,Long sourceFcId,Long targetId,Long targetFcId,Long targetXhId,String targetFcName) {
		List<Map<String,Object>> sourceList;
		sourceList = packageDao.getTreeBysID(sourceId);
		if(sourceId.equals(0L)) {
			//获取被复制的 发次模板 (及其节点)
			Map<String,Object> keyValueMap = new HashMap();
			keyValueMap.put("F_SSFC", sourceFcId);
			keyValueMap.put("F_PARENTID", 0);
			sourceList = packageDao.query(keyValueMap);
			for(Map<String,Object> map : sourceList) {
				importTreeInfo(CommonTools.Obj2Long(map.get("ID")),sourceFcId,targetId,targetFcId,targetXhId ,targetFcName);
			}
			return;
		}
		Map<String,Map<String,Object>> sourceMap = ListUtil.list2Map(sourceList, "ID");
		List<String> sqls = new ArrayList();
		List<Map<String,Object>> list = new ArrayList();
		getInsertTreeSql(sqls,list,sourceMap,sourceId,targetId,targetFcId,targetXhId,targetFcName);
		for(int i=0;i<sqls.size();i++) {
			jdbcDao.exesql(sqls.get(i), list.get(i));
		}
	}
	/**
	 * @param sqls
	 * @param sourceList
	 * @param sourceId
	 * @param targetId
	 */
	@SuppressWarnings({ "unlikely-arg-type"})
	private void getInsertTreeSql(List<String> sqls,List<Map<String,Object>> list,Map<String,Map<String,Object>> sourceMap,
			Long sourceId,Long targetId,Long targetFcId,Long targetXhId ,String targetFcName) {
		Long newId = UniqueIdUtil.genId();
		StringBuffer sql= new StringBuffer();//F_SM
		sql.append(" insert into W_PACKAGE \r\n");
		sql.append("(ID, F_JDMC, F_JDLX, F_FZR, F_FZRID,F_SM, F_PARENTID, F_CJSJ, F_CHSPZT, F_TZZT, F_SSXH, F_SSFC, F_PARENTNAME, F_TEST_SYDD, F_TEST_CSDW, F_TEST_JHKSSJ, F_TEST_JHJSSJ, F_SOFT_RJDH, F_SOFT_RJMC, F_SOFT_ZT, F_SOFT_ZRDW, F_SOFT_BBH, F_PART_CPDH, F_PART_CPMC, F_PART_ZT, F_PART_ZRDW, F_DESI_JHKSSJ, F_DESI_JHJSSJ)\r\n");
		sql.append(" values \r\n");
		sql.append(" ("+newId+",:F_JDMC,:F_JDLX,:F_FZR,:F_FZRID,:F_SM,:F_PARENTID,:F_CJSJ,:F_CHSPZT,:F_TZZT,:F_SSXH,:F_SSFC,:F_PARENTNAME,:F_TEST_SYDD,:F_TEST_CSDW,:F_TEST_JHKSSJ,:F_TEST_JHJSSJ,:F_SOFT_RJDH,:F_SOFT_RJMC,:F_SOFT_ZT,:F_SOFT_ZRDW,:F_SOFT_BBH,:F_PART_CPDH,:F_PART_CPMC,:F_PART_ZT,:F_PART_ZRDW,:F_DESI_JHKSSJ,:F_DESI_JHJSSJ)\r\n");
		
		Map<String,Object> map = sourceMap.get(sourceId.toString());
		if(map.get("F_PARENTID").equals("0")){
			map.put("F_PARENTID", targetId);
			map.put("F_SSXH", targetXhId);//型号
			map.put("F_SSFC", targetFcId);//发次
			map.put("F_PARENTNAME", targetFcName); //父节点名称
			map.put("F_CJSJ", new Date());//取当前时间

		}else {
			map.put("F_PARENTID", targetId);
			map.put("F_SSXH", targetXhId);//型号
			map.put("F_SSFC", targetFcId);//发次

			map.put("F_CJSJ", new Date());//取当前时间
		}
		//map.put("F_FZR", "");//负责人清空
		//map.put("F_FZRID", "");//负责人清空
		
		sqls.add(sql.toString());
		list.add(map);
		List<Map<String,Object>> sonList = packageDao.getSon(sourceId);
		for(Map<String,Object> sMap : sonList) {
			Long sourceId1 = CommonTools.Obj2Long(sMap.get("ID"));
			getInsertTreeSql(sqls,list,sourceMap,sourceId1,newId,targetFcId,targetXhId,targetFcName);
		}
	}
	/**
	 * @param projectId : 发次Id
	 * @return
	 */
	public String getPackageTree(String projectId) {
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_SSCPLB", projectId);
	/*	keyValueMap.put("F_PARENTID", 0);*/
		List<Map<String,Object>> sourceList = packageDao.query(keyValueMap);
		/*List<Map<String,Object>> treeList = new ArrayList();
		for(Map<String,Object> map : sourceList) {
			treeList.addAll(packageDao.getTreeBysID(CommonTools.Obj2Long(map.get("ID"))));
		}*/
		return JSONArray.fromObject(sourceList).toString();
	}
	
	/**
	 * 合并普通节点-节点名称相同
	 * @param normalNode 普通节点
	 * @param normalNodeModel 合并后集合
	 * @return
	 */
	public List<String> unionNormalNode(List<Map<String, Object>> normalNode, List<String> dataPackageTree
			, String dataPackageUrl, String productId) {
		
		List<Map<String, Object>> normalNodeModel = new ArrayList<Map<String,Object>>();
		
		for (int i = 0; i < normalNode.size(); i++) {
			String curNodeId = CommonTools.Obj2String(normalNode.get(i).get("ID"));
			String curNodeame = CommonTools.Obj2String(normalNode.get(i).get("F_JDMC"));
			if (normalNodeModel.size() > 0) {
				boolean flag = false;
				for (int j = 0; j < normalNodeModel.size(); j++) {
					String exsitNodeame = CommonTools.Obj2String(normalNodeModel.get(j).get("F_JDMC"));
					String unionNodeId = CommonTools.Obj2String(normalNodeModel.get(j).get("UNIONID"));
					if (curNodeame.equals(exsitNodeame)) {
						unionNodeId += "," + curNodeId;
						normalNodeModel.get(j).put("UNIONID", unionNodeId);
						flag = true;
					}
				}
				if (!flag) {
					normalNode.get(i).put("UNIONID", curNodeId);
					normalNodeModel.add(normalNode.get(i));
				}
			} else {
				normalNode.get(i).put("UNIONID", curNodeId);
				normalNodeModel.add(normalNode.get(i));
			}
		}
		
		if (normalNodeModel.size() > 0) {
			for (int i = 0; i < normalNodeModel.size(); i++) {
				String nodeId = CommonTools.Obj2String(normalNodeModel.get(i).get("ID"));
				String unionNodeId = CommonTools.Obj2String(normalNodeModel.get(i).get("UNIONID"));
				String parentId = CommonTools.Obj2String(normalNodeModel.get(i).get("F_PARENTID"));
				String nodeName = CommonTools.Obj2String(normalNodeModel.get(i).get("F_JDMC"));
				String nodeType = CommonTools.Obj2String(normalNodeModel.get(i).get("F_JDLX"));
				String node = "{id:\"" + nodeId + "\", parentId:" + parentId + ", unionId:\"" + unionNodeId + "\", Name:\"" + nodeName
						+ "\" , Type: \"" + nodeType + "\" , tempUrl:" + dataPackageUrl
						+ ", target : \"dataPackageFrame\",open:true}";
				dataPackageTree.add(node);
				
				//普通分类节点的所有子节点
				List<Map<String, Object>> dataPackageInfo = packageDao.queryDataPackageInfoByPId(unionNodeId, productId);
				List<Map<String, Object>> newNormalNode = new ArrayList<Map<String,Object>>();
				if (dataPackageInfo.size() > 0) {
					for (int j = 0; j < dataPackageInfo.size(); j++) {
						String dpNodePid = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_PARENTID"));
						if (unionNodeId.indexOf(dpNodePid) >= 0) {
							dpNodePid = nodeId;
						}
						String dpNodeType = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_JDLX"));
						//普通分类节点递归合并
						if (dpNodeType.equals(IOConstans.SIMPLE_PACKAGE_NODE)) {
							dataPackageInfo.get(j).put("F_PARENTID", dpNodePid);
							newNormalNode.add(dataPackageInfo.get(j));
						} else {
							String dpNodeId = CommonTools.Obj2String(dataPackageInfo.get(j).get("ID"));
							String dpNodeName = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_JDMC"));
							String nodeProductId = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_SSXH"));
							String nodeProjectId = CommonTools.Obj2String(dataPackageInfo.get(j).get("F_SSFC"));
							//追加发次信息（非普通节点）
							String nodeProjectName = getProjectNameById(nodeProjectId);
							if (nodeProjectName == "false") {
								continue;
							}
							dpNodeName += "," + nodeProjectName;
							
							String currentNode = "{id:" + dpNodeId + ", parentId:\"" + dpNodePid + "\", Name:\"" + dpNodeName
									+ "\" , Type: \"" + dpNodeType + "\" , ProductId :\"" + nodeProductId + "\",ProjectId : \""
									+ nodeProjectId + "\", tempUrl:" + dataPackageUrl
									+ ", target : \"dataPackageFrame\",open:true}";
							dataPackageTree.add(currentNode);
						}
					}
				}
				if (newNormalNode != null) {
					unionNormalNode(newNormalNode, dataPackageTree, dataPackageUrl, productId);
				}
			}
		}
		return dataPackageTree;
	}
	
	/**
	 * 添加数据包树节点
	 * @param dataPackageTree
	 * @param dataPackageInfo
	 * @param dataPackageUrl
	 * @return
	 */
	public List<String> addTreeNode(List<String> dataPackageTree, List<Map<String, Object>> dataPackageInfo, String dataPackageUrl) {
		if (dataPackageInfo.size() > 0) {
			for (int i = 0; i < dataPackageInfo.size(); i++) {
				String nodeId = CommonTools.Obj2String(dataPackageInfo.get(i).get("ID"));
				String nodePid = CommonTools.Obj2String(dataPackageInfo.get(i).get("F_PARENTID"));
				String nodeName = CommonTools.Obj2String(dataPackageInfo.get(i).get("F_JDMC"));
				String nodeType = CommonTools.Obj2String(dataPackageInfo.get(i).get("F_JDLX"));
				String nodeProductId = CommonTools.Obj2String(dataPackageInfo.get(i).get("F_SSXH"));
				String nodeProjectId = CommonTools.Obj2String(dataPackageInfo.get(i).get("F_SSFC"));
				
				//追加发次信息（非普通节点）
				if (!nodeType.equals(IOConstans.SIMPLE_PACKAGE_NODE)) {
					String nodeProjectName = getProjectNameById(nodeProjectId);
					nodeName += "," + nodeProjectName;
				}
				
				String currentNode = "{id:" + nodeId + ", parentId:" + nodePid + ", Name:\"" + nodeName
						+ "\" , Type: \"" + nodeType + "\" , ProductId :\"" + nodeProductId + "\",ProjectId : \""
						+ nodeProjectId + "\", tempUrl:" + dataPackageUrl
						+ ", target : \"dataPackageFrame\",open:true}";
				dataPackageTree.add(currentNode);
			}
		}
		return dataPackageTree;
	}
	
	/** 根据F_TCPX查询ID  */
	public List<Map<String,Object>> selectIdByTcpx(String tcpx,String prId,String fcId){
		return packageDao.selectIdByTcpx(tcpx,prId,fcId);
	}
	
	/** 根据ID查询F_TCPX  */
	public String selectTcpxById(String id){
		return packageDao.selectTcpxById(id);
	}
	
	/** 查询tcpx为空 */
	public List selectNullTcpx (String prId) {
    	List list = new ArrayList();
        list = packageDao.selectNullTcpx(prId);
        return list;
    }
	
	
	/** 根据ID查询父节点名  */
	public String selectParentNameById(String id){
		return packageDao.selectParentNameById(id);
	}
	
	/** 根据ID查询父节点ID  */
	public String selectParentIdById(String id){
		return packageDao.selectParentIdById(id);
	}
	
	/** 根据ID查询W_PACKAGE  */
	public CheckPackage selectById(Long id){
		return checkPackageDao.getById(id);
	}
	
	/** 根据F_SSSJB查询W_DataPACKAGEInfo  */
	public List<CheckDataPackageInfo> selectDataPackageInfoById(String id){
		 return checkDataPackageInfoDao.getByModelId(id);
	}
	
	/** 根据F_TB_INSTANT_ID查询W_CONDI_RES  */
	public List<CkConditionResult> selectConditionResultById(String id){
		 return ckConditionResultDao.getByModelId(id);
	}
	
	/** 根据F_TB_INSTANT_ID查询W_SIGNRESULT  */
	public List<SignResult> selectSignResultById(String id){
		 return signResultDao.getByModelId(id);
	}
	
	/** 根据F_SSSJB查询W_WORKTEAM  */
	public List<CheckWorkTeam> selectWorkTeamById(String id){
		 return checkWorkTeamDao.getByModelId(id);
	}
	
	/** 根据F_SSSJB查询W_DataPACKAGEInfo  */
	public CheckInstant selectInstantById(Long id){
		 return checkInstantDao.getById(id);
	}
	
	/**
	 * Description : 通过所属发次查找数据包信息
	 * Author : XYF
	 * Date : 2018年9月8日下午4:30:52
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectPackageByFcId(String Id){
		return dataPackageDao.selectPackageByFcId(Id);
	}
	
	/**
	 * Description : 通过所属型号查询发次信息
	 * Author : XYF
	 * Date : 2018年9月10日下午2:24:32
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectprojectByXhId(String Id){
		return dataPackageDao.selectprojectByXhId(Id);
	}
	
	/**
	 * Description : 通过Id查找发次信息
	 * Author : XYF
	 * Date : 2018年9月8日下午4:40:49
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProjectById(String Id){
		return dataPackageDao.selectProjectById(Id);
	}
	
	/**
	 * Description : 通过Id查找型号信息
	 * Author : XYF
	 * Date : 2018年9月10日下午2:26:56
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectProductById(String Id){
		return dataPackageDao.selectProductById(Id);
	}
	
	/**
	 * Description : 通过用户ID查询用户信息
	 * Author : XYF
	 * Date : 2018年9月14日下午2:00:15
	 * Return : List<Map<String,Object>>
	 */
	public List<Map<String,Object>> selectUserById(Long Id){
		return dataPackageDao.selectUserById(Id);
	}
	/**
     * 根据表单实例ID更新表格实例content
     *
     * @param param
     * @return
     */
    public void updateTableIns(Map<String, Object> param) {
        formFolderDao.updateTableIns(param);
    }
	
	/**
     * 生成检查结果、且替换掉表格实例内的content中input标签的ID
     *
     * @param checkitems
     * @param insId
     */
    public void createcheckresult(List<Map<String, Object>> checkitems, Long insId, String content,Long oldId) {
        for (int i = 0; i < checkitems.size(); i++) {
            Map<String, Object> checkitem = checkitems.get(i);
            Long ID = UniqueIdUtil.genId();
            String intemdefId = checkitem.get("ID").toString();
            String OldId = Long.toString(oldId);
            String resultId = packageDao.selectIdByIntemdefId(intemdefId,OldId);
            //根据表单实例ID区分检查结果表的类型
            // 检查结果(W_CK_RESULT 空间检查结果/W_CK_RESULT_CARRY 运载检查结果 /W_CK_RESULT_JGJG 结构机构检查结果 )
            Map typeMap = productTypeDao.getProductType(insId);

            if ("空间".equals(typeMap.get("TYPE"))) {
                CheckResult checkresult = checkresultDao.getById(resultId);
                checkresult.setID(ID);
          //      checkresult.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                checkresult.setInstantID(insId);
                checkresultDao.add(checkresult);
                content = content.replaceAll(resultId.toString(), ID.toString());
            } else if ("运载".equals(typeMap.get("TYPE"))) {
            	
                CheckResultCarry checkresultCarry = checkresultCarryDao.getById(resultId);
                checkresultCarry.setID(ID);
             //   checkresultCarry.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                checkresultCarry.setInstantID(insId);
                checkresultCarryDao.add(checkresultCarry);
                content = content.replaceAll(resultId.toString(), ID.toString());
            } else {
                CheckResultJgjg checkresultJgjg = checkresultJgjgDao.getById(resultId);
                checkresultJgjg.setID(ID);
          //      checkresultJgjg.setItemID(Long.parseLong(checkitem.get("ID").toString()));
                checkresultJgjg.setInstantID(insId);
                checkresultJgjgDao.add(checkresultJgjg);
                content = content.replaceAll(resultId.toString(), ID.toString());
            }

        }
        System.out.println(content);
        Map<String, Object> param = new HashMap<String, Object>();
        param.put("ID", insId);
        param.put("F_CONTENT", content);
        updateTableIns(param);
    }
	
	/**
	 * Description : 查找数据包结构树中一个节点的所有上级节点
	 * Author : XYF
	 * Date : 2018年8月15日下午4:37:06
	 * Return : String
	 */
	public String selectParentId(String id){
		String parentIds = "";
		String parentId = "";
		while(!"0".equals(parentId)){
			parentId = packageDao.selectParentIdById(id);
			parentIds += parentId +",";
			id = parentId;
		}
		
		return parentIds;
	}
    
	/** 根据父节点ID查询该父节点下最大F_TCPX的后一位  */
	public String selectTcpxByParentId(String id){
		List<Map<String,Object>> s = packageDao.selectByParentId(id);
		List<String> result = new ArrayList<String>();
		String max="";
		if(s.size()>0){
			for(int i=0;i<s.size();i++){
				for(Map<String, Object> resMap : s){
				//	result.add(CommonTools.Obj2String(resMap.get("F_TCPX")));
					String tcpx = CommonTools.Obj2String(resMap.get("F_TCPX"));
					if("".equals(tcpx)){
						
					}else{
						result.add(tcpx);
					}
				}	
			}
			if(result.size()>0){
				int Max = Integer.parseInt(result.get(0));
				for(int j=0;j<result.size();j++){
					int tcpx= Integer.parseInt(result.get(j));
					if(Max<tcpx){
						Max = tcpx;
					}
				}
				Max = Max+1;
				max = String.valueOf(Max);
				if("".equals(max)){
			//		String tcpx=packageDao.selectTcpxById(id);
			//		int Tcpx=Integer.parseInt(tcpx);
					Max=1;
					max = String.valueOf(Max);
				}
			}else{
			//	String tcpx=packageDao.selectTcpxById(id);
			//	int Tcpx=Integer.parseInt(tcpx);
				int Max = 1;
				max = String.valueOf(Max);
			}
			
		}else{
		//	String tcpx=packageDao.selectTcpxById(id);
		//	int Tcpx=Integer.parseInt(tcpx);
			int Max = 1;
			max = String.valueOf(Max);
		}
		
		
		
		return max;
	}
	
	/** 根据tcpx找出上一个tcpx */
	public String selectUptcpxBytcpx(String tcpx,String prId,String fcId){
		String upTcpx="";
		int j=0;
		
			List<Map<String,Object>> list = packageDao.selectAllFrompackage(prId,fcId);
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
		
		return upTcpx;
	}
	/** 根据tcpx找出下一个tcpx */
	public String selectDowntcpxBytcpx(String tcpx,String prId,String fcId){
		String downTcpx="";
		int j=0;
		
			List<Map<String,Object>> list = packageDao.selectAllFrompackage(prId,fcId);
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
		
		return downTcpx;
	}
	/** 根据ID查询节点名  */
	public String selectNameById(String id){
		return packageDao.selectNameById(id);
	}
	
	/** 根据ID更新F_TCPX  */
	public void updateTcpxById(String id,String tcpx){
		packageDao.updateTcpxById(id,tcpx);
	}
	
	/** 根据ID更新父节点名  */
	public void updateParentNameById(String id,String prname){
		packageDao.updateParentNameById(id,prname);
	}
	

	public void addPackage (CheckPackage checkPackage){
		checkPackageDao.add(checkPackage);
	}
	

	public void addDataPackageInfo (CheckDataPackageInfo checkDataPackageInfo){
		checkDataPackageInfoDao.add(checkDataPackageInfo);
	}
	
	public void addWorkTeam (CheckWorkTeam checkWorkTeam){
		checkWorkTeamDao.add(checkWorkTeam);
	}
	
	
	public void addInstant (CheckInstant checkInstant){
		checkInstantDao.add(checkInstant);
	}
	
	public void addConditionResult (CkConditionResult ckConditionResult){
		ckConditionResultDao.add(ckConditionResult);
	}
	public void addSignResult (SignResult signResult){
		signResultDao.add(signResult);
	}
	
	
	/** 根据父节点ID更新父节点ID  */
	public void updateParentIdByParentId(String id,String newid){
		packageDao.updateParentIdByParentId(id,newid);
	}
	
	/** 根据父节点ID更新父节点名  */
	public void updateParentNameByParentId(String id,String newname){
		packageDao.updateParentNameByParentId(id,newname);
	}
	
	/** 根据ID更新父节点ID  */
	public void updateParentIdById(String id,String prid){
		packageDao.updateParentIdById(id,prid);
	}
	
	/**根据ID删除模版信息*/
	public void deleteTemplate(String Id){
		packageDao.deleteTemplate(Id);
	}
	
	
		/**
		 * @Author  shenguoliang
		 * @Description: 父表发次名称的刷新
		 * @Params [typeId, typeName]
		 * @Date 2018/5/21 20:02
		 * @Return void
		 */
	public void doChangeParentTypeName(String typeId,String typeName) {
		Map<String,Object> keyValueMap = new HashMap();
		keyValueMap.put("F_SSFC", typeId);
		keyValueMap.put("F_PARENTID", 0);
		List<Map<String,Object>> sourceList = packageDao.query(keyValueMap);
		for(Map<String,Object> map : sourceList) {
			long dataId =  CommonTools.Obj2Long(map.get("ID"));
			packageDao.uodateParentTypeName(dataId,typeName);
		}
	}
}
