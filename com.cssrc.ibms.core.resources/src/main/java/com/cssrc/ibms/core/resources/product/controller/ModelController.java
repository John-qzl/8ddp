package com.cssrc.ibms.core.resources.product.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.xalan.xsltc.compiler.sym;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.product.bean.ModuleManage;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.resources.product.service.ModuleService;
import com.cssrc.ibms.core.resources.product.service.ProductService;
import com.cssrc.ibms.core.resources.project.service.ProjectService;
import com.cssrc.ibms.core.user.model.SysOrg;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.SysOrgService;
import com.cssrc.ibms.core.user.service.SysUserService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.web.controller.BaseController;

import edu.emory.mathcs.backport.java.util.Arrays;
import net.sf.json.JSONArray;

/**
 * @description 型号管理结构树控制器
 * @author xie chen
 * @date 2019年11月21日 上午11:25:46
 * @version V1.0
 */
@Controller
@RequestMapping("/model/manage/tree/")
public class ModelController extends BaseController {
	@Resource
	private ProductService productService;
	@Resource
	private ProjectService projectService;
	@Resource
	private ModuleService moduleService;
	@Resource
	private SysUserService sysUserService;
	@Resource
	private ModuleManageService moduleManageService;
	@Resource
	private SysOrgService sysOrgService;


	@RequestMapping({ "getTreeData" })
	@ResponseBody
	@Action(description = "获取型号管理结构树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		List<String> productTree = new ArrayList<String>();
		String productUrl = "\"/oa/form/dataTemplate/preview.do?__displayId__=10000021170075\"";
		String projectUrl = "\"/oa/form/dataTemplate/detailData.do?__displayId__=10000021170075\"";
		
		// 构建静态根节点
		String rootName = "全部型号";
		String rootNode = "{typeId:0" + ",dbomSql : \"F_TYPE='全部'\", parentId:-1"
				+ ", typeName:\"" + rootName + "\" , tempUrl:" + productUrl + ", target : \"listFrame\",open:true}";
		productTree.add(rootNode);
		//权限
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		String userId=String.valueOf(curUser.getUserId());
		List<Map<String, Object>> moduleList = moduleService.getAllModules();
		for (Map<String, Object> moduleMap : moduleList) {
			/*String[] group=moduleMap.get("f_xzxhglID").toString().split(",");*/
			List<String> userIdList=moduleManageService.getByModuleId(moduleMap.get("ID").toString());
			if(curUser.getRoles().indexOf("sjgly")<0) {
				if(!userIdList.contains(userId)) {
					if(!userIdList.contains(userId)) {
						continue;
					}
				}
			}
			String typeId = CommonTools.Obj2String(moduleMap.get("ID"));
			String parentId = "0";
			String typeName = CommonTools.Obj2String(moduleMap.get("F_XHMC"));
			String typeKey = CommonTools.Obj2String(moduleMap.get("F_XHDH"));
//			String typeOfPart = CommonTools.Obj2String(moduleMap.get("F_SSMK"));
//			String node = "{typeId:" + typeId +",typeOfPart:"+"\""+typeOfPart+"\""+ ",dbomSql:'F_SSXH=" + typeId + "',parentId:" + parentId
			String node = "{typeId:" + typeId + ",dbomSql:'F_SSXH=" + typeId + "',parentId:" + parentId
					+ ", typeName:\"" + typeKey + "\" , tempUrl:" + projectUrl
					+ ", target : \"listFrame\",open:true}";
			productTree.add(node);
		}
		response.getWriter().print(JSONArray.fromObject(productTree).toString());
	}
	
	@RequestMapping("getCurUserInfo")
	@ResponseBody
	public Map<String, Object> getCurUserInfo(HttpServletRequest request, HttpServletResponse response) {
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("curUser", curUser);
		return map;
	}

	@RequestMapping("getNodeCharger")
	@ResponseBody
	public Boolean getNodeCharger(HttpServletRequest request, HttpServletResponse response) {
		String fcId = request.getParameter("fcId");
		// 获取当前用户
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		return productService.getNodeChargerAndWorkTeam(fcId, curUser.getUserId());
	}

	// 型号树节点上移
	@RequestMapping("setUp")
	@ResponseBody
	public void setUp(HttpServletRequest request) {
		String fcId = request.getParameter("fcId");
		String prId = request.getParameter("prId");
		if (prId.equals("0")) {
			List<Map<String, Object>> list = productService.selectNullTcpxproduct();// 定义一个list寻找tcpx值为空的记录
			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> resMap = list.get(i);
					String ID = resMap.get("ID").toString();
					String tcpx = productService.selectTcpxByParentIdFromProduct();
					productService.updateTcpxByIdFromProduct(ID, tcpx);
				}
			}
			String tcpx = productService.selectTcpxByIdFromProduct(fcId); // 通过型号id查询同层排序
			String upTcpx = productService.selectUptcpxBytcpx(tcpx, prId);
			String upId = productService.selectIdByTcpxFromProduct(upTcpx); // 通过上层型号的同层排序得到上层型号的型号id
			String t = upTcpx;
			productService.updateTcpxByIdFromProduct(upId, tcpx);
			productService.updateTcpxByIdFromProduct(fcId, t);// 将同层排序与上层型号的同层排序互换
		} else {
			List<Map<String, Object>> list = productService.selectNullTcpxproject();
			;// 定义一个list寻找tcpx值为空的记录

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> resMap = list.get(i);
					String ID = resMap.get("ID").toString();
					String tcpx = productService.selectTcpxByParentId(prId);
					productService.updateTcpxById(ID, tcpx);
				}
			}

			String tcpx = productService.selectTcpxById(fcId); // 通过发次id查询同层排序

			String upTcpx = productService.selectUptcpxBytcpx(tcpx, prId);
			String upId = productService.selectIdByTcpx(upTcpx, prId); // 通过上层发次的同层排序得到上层发次的发次id
			String t = upTcpx;
			productService.updateTcpxById(upId, tcpx);
			productService.updateTcpxById(fcId, t);// 将同层排序与上层发次的同层排序互换

		}

	}

	// 型号数节点下移
	@RequestMapping("setDown")
	@ResponseBody
	public void setDown(HttpServletRequest request) {
		String fcId = request.getParameter("fcId");
		String prId = request.getParameter("prId");
		if (prId.equals("0")) {

			List<Map<String, Object>> list = productService.selectNullTcpxproduct();

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> resMap = list.get(i);
					String ID = resMap.get("ID").toString();
					String tcpx = productService.selectTcpxByParentIdFromProduct();
					productService.updateTcpxByIdFromProduct(ID, tcpx);
				}
			}
			String tcpx = productService.selectTcpxByIdFromProduct(fcId); // 通过型号id查询同层排序
			String downTcpx = productService.selectDowntcpxBytcpx(tcpx, prId);
			String downId = productService.selectIdByTcpxFromProduct(downTcpx); // 通过下层型号的同层排序得到下层型号的型号id
			String t = downTcpx;
			productService.updateTcpxByIdFromProduct(downId, tcpx);
			productService.updateTcpxByIdFromProduct(fcId, t);// 将同层排序与下层型号的同层排序互换
		} else {

			List<Map<String, Object>> list = productService.selectNullTcpxproject();

			if (list.size() > 0) {
				for (int i = 0; i < list.size(); i++) {
					Map<String, Object> resMap = list.get(i);
					String ID = resMap.get("ID").toString();
					String tcpx = productService.selectTcpxByParentId(prId);
					productService.updateTcpxById(ID, tcpx);
				}
			}
			String tcpx = productService.selectTcpxById(fcId); // 通过发次id查询同层排序
			String downTcpx = productService.selectDowntcpxBytcpx(tcpx, prId);
			String downId = productService.selectIdByTcpx(downTcpx, prId); // 通过下层发次的同层排序得到下层发次的发次id
			String t = downTcpx;
			productService.updateTcpxById(downId, tcpx);
			productService.updateTcpxById(fcId, t);// 将同层排序与下层发次的同层排序互换
		}

	}
	//型号新建后置事件，把相关人员加入到管理队伍中
	@RequestMapping("modelMangerSave")
	public void modelMangerSave(HttpServletRequest request, HttpServletResponse response) {
		String id = request.getParameter("id");
		Map<String, Object> map=moduleService.getByModelId(id);
		Set<String> userIds=new HashSet<>();
		userIds.add(map.get("F_xhzzhID").toString());
		userIds.add(map.get("F_zgbmldID").toString());
		String userArray=map.get("F_xhfzsID").toString();
		if(userArray!=null&&userArray.length()!=0) {
			String[] array=userArray.split(",");
			userIds.addAll(Arrays.asList(array));
		}
		
		for (String string : userIds) {
			List<String> userList=moduleManageService.getByModuleId(id);
			if(!userList.contains(string)) {
				SysUser sysUser=sysUserService.getById(Long.valueOf(string));
				ModuleManage moduleManage=new ModuleManage();
				moduleManage.setBm("");
				moduleManage.setBmId("");
			/*	SysOrg sysOrg=sysOrgService.getPrimaryOrgByUserId(sysUser.getUserId());*/
				List<SysOrg> sysOrg=(List<SysOrg>) sysOrgService.getByUserId(sysUser.getUserId());
				if(sysOrg!=null&&sysOrg.size()!=0) {
					moduleManage.setBm(sysOrg.get(0).getOrgName());
					moduleManage.setBmId(sysOrg.get(0).getOrgName());
				}
				moduleManage.setId(String.valueOf(UniqueIdUtil.genId()));
				moduleManage.setQx("manage");
				moduleManage.setRy(sysUser.getFullname());
				moduleManage.setRyId(sysUser.getUserId().toString());
				moduleManage.setSsxhID(id);
				moduleManageService.insert(moduleManage);
			}
		}
	}
}
