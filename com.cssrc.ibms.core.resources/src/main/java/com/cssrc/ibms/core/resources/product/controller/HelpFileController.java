package com.cssrc.ibms.core.resources.product.controller;

import java.io.File;
import java.util.ArrayList;
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
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.resources.product.service.HelpFileService;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SysFileService;

import groovy.json.StringEscapeUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
/**
 * @description 电子词典文件树
 * @author fuyong
 * @date 2020年9月9日 上午9:25:46
 * @version V1.0
 */
@Controller
@RequestMapping("/helpfile/manage/tree/")
public class HelpFileController extends BaseController{
	@Resource
	HelpFileService helpFileService;
	@Resource
	SysFileService sysFileService;
	
	@RequestMapping({ "getTreeData" })
	@ResponseBody
	@Action(description = "获取文件树")
	public void getTreeData(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String serviceUrl="\"/dp/temp/使用手册_服务器端.pdf\"";
		String padUrl="\"/dp/temp/使用手册_pad端.pdf\"";
		String middleUrl="\"/dp/temp/使用手册_中转机.pdf\"";
		List<String> productTree = new ArrayList<String>();
		String allFileMangeUrl="\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000028380212\"";
		String productUrl = "\"/dp/oa/form/dataTemplate/preview.do?__displayId__=10000030190153\"";
		// 构建静态根节点
		String rootName = "全部文件";
		String rootNode = "{typeId:0" + ",dbomSql : \"F_TYPE='全部'\", parentId:-1"
				+ ", typeName:\"" + rootName + "\" , tempUrl:" + allFileMangeUrl + ", target : \"listFrame\",open:true}";
		productTree.add(rootNode);
		/*String node = "{typeId:1" + ",dbomSql : \"F_TYPE='pdf'\", parentId:0"
				+ ", typeName:\"" + "服务器使用手册v1.0" + "\" , tempUrl:" + serviceUrl + ", target : \"listFrame\",open:true}";
		productTree.add(node);
		node = "{typeId:2" + ",dbomSql : \"F_TYPE='pdf'\", parentId:0"
				+ ", typeName:\"" + "摆渡机使用手册v1.0" + "\" , tempUrl:" + middleUrl + ", target : \"listFrame\",open:true}";
		productTree.add(node);
		node = "{typeId:3" + ",dbomSql : \"F_TYPE='pdf'\", parentId:0"
				+ ", typeName:\"" + "移动端使用手册v1.0" + "\" , tempUrl:" + padUrl + ", target : \"listFrame\",open:true}";
		productTree.add(node);*/
		 rootName = "版本明细";
		 rootNode = "{typeId:4" + ",dbomSql : \"F_TYPE='全部'\", parentId:-1"
				+ ", typeName:\"" + rootName + "\" , tempUrl:" + productUrl + ", target : \"listFrame\",open:true}";
		productTree.add(rootNode);
		List<Map<String, Object>> fileList=helpFileService.getAllFile();
		int i=10;
		for (Map<String, Object> map : fileList) {
			String str=map.get("F_XZ").toString();
			JSONArray jsonArray=JSONArray.fromObject(str);
			JSONObject jsonObject=jsonArray.getJSONObject(0);
			String fileId=jsonObject.getString("id");
			SysFile sysFile=sysFileService.getById(Long.valueOf(fileId));
			String path="";
			if(sysFile!=null) {
				String str1=sysFile.getFilepath().replace("\\", "\\\\");
				 path= "\""+"/helpfile"+"\\\\"+str1+"\"";
			}
			String node = "{typeId:"+i + ",dbomSql : \"F_TYPE='pdf'\", parentId:0"
					+ ", typeName:\"" + map.get("F_WJM") + "\" , tempUrl:" + path + ", target : \"listFrame\",open:true}";
			productTree.add(node);
			i++;
		}
		response.getWriter().print(JSONArray.fromObject(productTree).toString());
	}
}
