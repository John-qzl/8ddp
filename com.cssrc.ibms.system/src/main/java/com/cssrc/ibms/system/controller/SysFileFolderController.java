/**
 * 
 */
package com.cssrc.ibms.system.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.ParamUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.dbom.ITreeNode;
import com.cssrc.ibms.core.util.dbom.JsonTree;
import com.cssrc.ibms.core.util.dbom.TreeNode;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.model.SysFileFolder;
import com.cssrc.ibms.system.service.SysFileFolderService;
import com.cssrc.ibms.system.service.SysFileService;

/**
 * 系统管理-文件夹管理控制器类
 */
@Controller
@RequestMapping("/oa/system/sysFileFolder")
public class SysFileFolderController extends BaseController {
   @Resource
   private SysFileFolderService sysFileFolderService;
   @Resource
   private  SysFileService sysFileService;
   
   /**
    * 获取文件夹树结构
    * **/
	@RequestMapping("tree")
	@ResponseBody
	public List<TreeNode> tree(HttpServletRequest request) throws Exception {
		Long userId = UserContextUtil.getCurrentUserId();
		List list = sysFileFolderService.getFolderByUserId(userId);
		if(null==list||list.size()<=0){
			list = sysFileFolderService.saveFolder(userId);//每个用户默认添加的
		}
		TreeNode node = JsonTree.beansToTree((List<ITreeNode>) list,true);
		List<TreeNode> nodes = new ArrayList<TreeNode>();
		nodes.add(node);
		return nodes;
	}
	/**
	 * 获取文件夹树节点下的附件记录
	 * */
	@RequestMapping("treeNodeFile")
	@ResponseBody
	public Map<String,Object> treeNodeFile(HttpServletRequest request) throws ParseException{
		Map<String,Object> map = new HashMap<String,Object>();
		QueryFilter filter = new QueryFilter(request);
		filter.addSorted("shared", "asc");
		filter.addSorted("createtime", "desc");
		SysFileFolder sysFileFolder = null;
		List<SysFile> lis = new ArrayList<SysFile>();
		Long folderId = RequestUtil.getLong(request, "folderId");
		String searchPanel = RequestUtil.getString(request, "searchPanel");
		if(null!=searchPanel&&!searchPanel.equals("")){
			filter.addFilterForIB("delFlag", SysFile.FILE_NOT_DEL);
			filter.addFilterForIB("fileatt", SysFile.FILEATT_TRUE);
			//添加查询字段
			Enumeration paramEnu = request.getParameterNames();
			while (paramEnu.hasMoreElements()) {
				String paramName = (String) paramEnu.nextElement();
				if (paramName.startsWith("Q_")) {
					String paramValue = request.getParameter(paramName);
					String[] fieldInfo = paramName.split("[_]");
					Object value = null;
					if ((fieldInfo != null) && (fieldInfo.length == 4)) {
						value = ParamUtil.convertObject(fieldInfo[2], paramValue);
						if (value != null) {
							filter.addFilterForIB(fieldInfo[1], value);
						}
					}
				}
			}
			//重置当前页码
			int currentPage = filter.getPagingBean().getCurrentPage();
			int pageSize = filter.getPagingBean().getPageSize();
			currentPage = currentPage/pageSize + 1;
			filter.getPagingBean().setCurrentPage(currentPage);
			lis = sysFileService.getAll(filter);
		}else{
			if(null==folderId||folderId.equals(0L)){
				Long userId = RequestUtil.getLong(request, "userId");
				sysFileFolder = sysFileFolderService.getRootFolderByUserId(userId);
			}else{
				sysFileFolder = sysFileFolderService.getById(folderId);
			}
			Boolean isRootFolder =false;
			if(sysFileFolder !=null){
				isRootFolder = sysFileFolder.getDepth().equals(1L)?true : false;
				Boolean isSharedFolder = null!=sysFileFolder.getSharedNode()?sysFileFolder.getSharedNode():false;
				String folderPath = sysFileFolder.getPath();
				lis = sysFileService.getSysFileByFolder(sysFileFolder.getCreatorId(), isRootFolder, isSharedFolder, folderPath, filter);
			}
		}
		map.put("success", "true");
		map.put("totalCounts", filter.getPagingBean().getTotalCount());
		map.put("result",lis);
		return map;
	}
	
	/**
	 * 文件夹详情
	 * */
	@RequestMapping("get")
	@ResponseBody
	public Map<String,Object> get(HttpServletRequest request,HttpServletResponse response){
		Map<String,Object> map = new HashMap<String,Object>();
		Long id = RequestUtil.getLong(request, "id");
		SysFileFolder folder = sysFileFolderService.getById(id);
		map.put("success", "true");
		map.put("data", folder);
		return map;
	}
	
	/**
	 * 保存文件夹
	 * */
	@RequestMapping("save")
	public void save(HttpServletRequest request,HttpServletResponse response,SysFileFolder folder) throws IOException{
		Long userId = UserContextUtil.getCurrentUserId();
		if(null==folder.getId()){
			SysFileFolder parent = sysFileFolderService.getById(folder.getPid());
			folder.setSharedNode(parent.getSharedNode());
			folder.setSystemNode(SysFileFolder.systemNode_FALSE);
			sysFileFolderService.saveNewFolder(folder,userId);
		}else{
			folder.setUpdateId(userId==null? UserContextUtil.getCurrentUser().getUserId() : userId);
			folder.setUpdatetime(new java.util.Date());
			sysFileFolderService.update(folder);
		}
		ResultMessage resultMessage =new ResultMessage(ResultMessage.Success, "分享成功");
		writeResultMessage(response.getWriter(), resultMessage);
	}
	
	/**
	 * 删除文件夹
	 * */
	@RequestMapping("delete")
	public void delete(HttpServletRequest request,HttpServletResponse response) throws IOException{
		Long id = RequestUtil.getLong(request, "id");
		sysFileFolderService.deleteFolder(id);
		ResultMessage resultMessage =new ResultMessage(ResultMessage.Success, "删除成功");
		writeResultMessage(response.getWriter(), resultMessage);
	}
}
