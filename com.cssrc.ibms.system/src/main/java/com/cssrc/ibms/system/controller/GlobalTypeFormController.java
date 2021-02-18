package com.cssrc.ibms.system.controller;


import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.cssrc.ibms.core.web.controller.BaseFormController;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.system.model.GlobalType;
import com.cssrc.ibms.system.model.SysFileType;
import com.cssrc.ibms.system.service.GlobalTypeService;
import com.cssrc.ibms.system.service.SerialNumberService;
import com.cssrc.ibms.system.service.SysFileTypeService;
/**
 * 系统分类Form提交和编辑
 * <p>Title:GlobalTypeFormController</p>
 * @author Yangbo 
 * @date 2016年9月13日上午10:32:45
 */
@Controller
@RequestMapping({ "/oa/system/globalType/" })
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class GlobalTypeFormController extends BaseFormController {

	@Resource
	private GlobalTypeService globalTypeService;

	@Resource
	private SerialNumberService serialNumberService;
	
	@Resource
	private SysFileTypeService sysFileTypeService;

	@RequestMapping({ "save" })
	@Action(description = "添加或更新系统分类")
	public void save(HttpServletRequest request, HttpServletResponse response,
			GlobalType globalType, BindingResult bindResult) throws Exception {
		ResultMessage resultMessage = validForm("globalType", globalType, bindResult, request);
		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		boolean isadd = globalType.getTypeId().longValue() == 0L;

		long parentId = RequestUtil.getLong(request, "parentId", 0L);
		
		Long dataId = RequestUtil.getLong(request, "dataId", 0L);

		int isRoot = RequestUtil.getInt(request, "isRoot");

		int isPrivate = RequestUtil.getInt(request, "isPrivate", 0);

		Long userId = UserContextUtil.getCurrentUserId();

		String nodeKey = globalType.getNodeKey();
		String resultMsg = null;
		if (globalType.getTypeId().longValue() == 0L) {
			if (parentId != 0L) {
				if(dataId>0L){//关联记录
					GlobalType parentGlobal = (GlobalType) this.globalTypeService.getById(Long.valueOf(parentId));
					if (parentGlobal != null) {
						parentGlobal.setIsLeaf(Integer.valueOf(1));
						this.globalTypeService.update(parentGlobal);
					}
				}else {//不关联记录
					GlobalType parentGlobal = (GlobalType) this.globalTypeService.getById(Long.valueOf(parentId));
					if (parentGlobal != null) {
						parentGlobal.setIsLeaf(Integer.valueOf(1));
						this.globalTypeService.update(parentGlobal);
					}
				}
			}
			GlobalType tmpGlobalType;
			String catKey = "";
			if(dataId >0L){
				SysFileType sysFileType = this.sysFileTypeService.getInitSysFileType(parentId, dataId);
				tmpGlobalType = new GlobalType(sysFileType);
				catKey = tmpGlobalType.getCatKey();
				if (StringUtil.isNotEmpty(nodeKey)) {
					boolean isExist = this.sysFileTypeService.isNodeKeyExists(catKey, nodeKey, dataId);
					if (isExist) {
						resultMsg = "节点KEY已存在!";
						writeResultMessage(response.getWriter(), resultMsg, 0);
						return;
					}
				}
			}else {
				tmpGlobalType = this.globalTypeService.getInitGlobalType(isRoot, parentId);
				catKey = tmpGlobalType.getCatKey();
				if (StringUtil.isNotEmpty(nodeKey)) {
					boolean isExist = this.globalTypeService.isNodeKeyExists(catKey, nodeKey);
					if (isExist) {
						resultMsg = "节点KEY已存在!";
						writeResultMessage(response.getWriter(), resultMsg, 0);
						return;
					}
				}
			}
	

			if (!catKey.equals(GlobalType.CAT_DIC)) {
				globalType.setType(tmpGlobalType.getType());
			}
			//系统用户编辑的节点UserId为0表示共享
			if (isPrivate == 1) {
				globalType.setUserId(userId);
			}
			
			globalType.setCatKey(catKey);
			globalType.setNodePath(tmpGlobalType.getNodePath());
			globalType.setTypeId(tmpGlobalType.getTypeId());
			globalType.setDepth(Integer.valueOf(1));
			globalType.setSn(Long.valueOf(0L));
			globalType.setIsLeaf(Integer.valueOf(0));
			if (globalType.getNodeCodeType().equals("1")) {
				globalType.setNodeCode(this.serialNumberService.nextId(globalType.getNodeCode()));
			}
			if(dataId >0L){
				globalType.setUserId(0L);
				//初始化成SysFileType
				SysFileType fileType = new SysFileType(globalType);
				fileType.setDataId(dataId.toString());
				sysFileTypeService.add(fileType);
			}else {
				this.globalTypeService.add(globalType);
			}
			resultMsg = "添加系统分类成功";
		} else {
			Long typeId = globalType.getTypeId();
			String catKey = globalType.getCatKey();
			boolean isExist = true;
			if(dataId >0L){//关联记录的节点查重
				isExist = sysFileTypeService.isNodeKeyExistsForUpdate(typeId, catKey, nodeKey, dataId);
			}else {
				isExist = this.globalTypeService.isNodeKeyExistsForUpdate(typeId, catKey, nodeKey);
			}
			
			if (isExist) {
				resultMsg = "节点KEY已存在!";
				writeResultMessage(response.getWriter(), resultMsg, 0);
				return;
			}
			if(dataId >0L){ //关联记录节点更新
				globalType.setUserId(0L);
				//初始化成SysFileType
				SysFileType fileType = new SysFileType(globalType);
				fileType.setDataId(dataId.toString());
				sysFileTypeService.update(fileType);
			}else {
				this.globalTypeService.update(globalType);
			}
			
			resultMsg = "更新系统分类成功";
		}
		writeResultMessage(response.getWriter(), resultMsg, 1);
		try {
			LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
			LogThreadLocalHolder.putParamerter("typeId", globalType.getTypeId().toString());
		} catch (Exception e) {
			e.printStackTrace();
			this.logger.error(e.getMessage());
		}
	}

	@ModelAttribute
	protected GlobalType getFormObject(@RequestParam("typeId") Long typeId,
			@RequestParam("dataId") Long dataId,Model model) throws Exception {
		GlobalType globalType = null;
		if (typeId.longValue() != 0L){
			if(dataId != null && dataId.longValue() > 0L){
				SysFileType sysFileType = sysFileTypeService.getFileType(typeId, dataId);
				globalType = new GlobalType(sysFileType);
			}else {
				globalType = (GlobalType) this.globalTypeService.getById(typeId);
			}
		}
		else {
			globalType = new GlobalType();
		}
		return globalType;
	}
}
