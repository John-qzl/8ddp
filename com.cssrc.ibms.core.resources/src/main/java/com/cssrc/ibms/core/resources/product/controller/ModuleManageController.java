package com.cssrc.ibms.core.resources.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.resources.product.service.ModuleBatchService;
import com.cssrc.ibms.core.resources.product.service.ModuleManageService;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.web.controller.BaseController;

/**
 * @description 型号管理人员控制
 * @author fu yong
 * @date 2020年8月22日 下午11:53:27
 * @version V1.0
 */
@Controller
@RequestMapping("/module/manage/")
public class ModuleManageController extends BaseController {
	@Resource
	ModuleManageService moduleManageService;

	@RequestMapping("setManager")
    @ResponseBody
    public void setManager(HttpServletRequest request, HttpServletResponse response) {
		String type=request.getParameter("type");
		String id=request.getParameter("id");
		String fullNames=request.getParameter("fullNames");
		String userIds=request.getParameter("userIds");
		List<String> userList=moduleManageService.getModuleManger(id,type);  //找到库里面所有这个型号的管理员
		if(userIds==null&&userIds.equals("")) {  //判断是否选择了用户
			return;
		}
		String[] str=userIds.split(",");
		String[] names=fullNames.split(",");
		int i=0;
		for (String string : str) {
			if(!userList.contains(string)) {  //如果选择的用户和库中用户重复则不需要再次添加
				moduleManageService.insert(id, string, names[i], type);
			}
			i++;
		}
    }
	@RequestMapping("deleteManage")
    @ResponseBody
    public Map<String, Object> deleteManage(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map=new HashMap<String, Object>();
		map.put("useRole", "0");
		String moduleId=request.getParameter("moduleId");
		String ids=request.getParameter("ids");
		String idArray[]=ids.split(",");
		List<String> userIdList=moduleManageService.getByModuleId(moduleId,"manage");
		ISysUser curUser = (ISysUser) UserContextUtil.getCurrentUser();
		if(curUser.getRoles().indexOf("sjgly")>=0) {
			for (String id : idArray) {
				moduleManageService.deleteById(id);
			}
			map.put("useRole", "1");
			return map;
		}
		else if(userIdList.contains(CommonTools.Obj2String(curUser.getUserId()))){
			for (String id : idArray) {	
				Map<String, Object> moduleMap=moduleManageService.getManageById(id);
				if(moduleMap.get("F_QX").equals("manage")) {
					map.put("useRole", "0");
					return map;
				}
			}
			for (String id : idArray) {
				moduleManageService.deleteById(id);
			}
			map.put("useRole", "1");
			return map;
		}
		return map;
	}
}
