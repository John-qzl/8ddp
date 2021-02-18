package com.cssrc.ibms.system.controller;

import java.io.File;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.Seal;
import com.cssrc.ibms.system.model.SealRight;
import com.cssrc.ibms.system.model.SysFile;
import com.cssrc.ibms.system.service.SealRightService;
import com.cssrc.ibms.system.service.SealService;
import com.cssrc.ibms.system.service.SysFileService;

/**
 * 对象功能:印章管理 控制器类.
 * 
 * <p>
 * detailed comment
 * </p>
 * 
 * @author [创建人] WeiLei <br/>
 *         [创建时间] 2016-8-22 上午10:35:10 <br/>
 *         [修改人] WeiLei <br/>
 *         [修改时间] 2016-8-22 上午10:35:10
 * @see
 */
@Controller
@RequestMapping("/oa/system/seal/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class SealController extends BaseController {

	@Resource
	private SealService sealService;
	@Resource
	private SealRightService sealRightService;
	@Resource
	private SysFileService sysFileService;

	private String attachPath = AppUtil.getAttachPath();

	private String saveType = PropertyUtil.getSaveType();

	/**
	 * 查看电子印章分页列表.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:57:19 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:57:19
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "list" })
	@Action(description = "查看电子印章分页列表", detail = "查看电子印章分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		List<Seal>list = this.sealService.getAll(new QueryFilter(request, "sealItem"));
		return this.getAutoView().addObject("sealList", list);
	}

	/**
	 * 选择一个签章，进行盖章.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:58:03 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:58:03
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "dialog" })
	@Action(description = "选择一个签章，进行盖章", detail = "选择一个签章，进行盖章")
	public ModelAndView dialog(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ISysUser currentUser = (ISysUser) UserContextUtil.getCurrentUser();
		return this.getAutoView().addObject("user", currentUser);
	}

	/**
	 * 签章选择器.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午12:59:35 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午12:59:35
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "selector" })
	@Action(description = "签章选择器", detail = "签章选择器")
	public ModelAndView selector(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Long userId = UserContextUtil.getCurrentUserId();
		String sealName = RequestUtil.getString(request, "sealName");
		List<Seal>list = this.sealService.getSealByUserId(userId, sealName);
		return this.getAutoView().addObject("sealList", list)
								 .addObject("sealName", sealName);
	}

	/**
	 * 删除电子印章.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午01:01:03 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午01:01:03
	 * @param request
	 * @param response
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "del" })
	@Action(description = "删除电子印章", execOrder = ActionExecOrder.BEFORE, detail = "删除电子印章:<#list StringUtils.split(sealId,\",\") as item><#assign entity=sealService.getById(Long.valueOf(item))/>【${entity.sealName},及其对应的印章权限】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "sealId");
			if (!this.saveType.contains("database")) {
				for (Long id : lAryId) {
					Seal seal = (Seal) this.sealService.getById(id);
					SysFile sysFile = (SysFile) this.sysFileService.getById(seal.getAttachmentId());
					String filePath = sysFile.getFilepath();
					if (StringUtil.isEmpty(this.attachPath)) {
						filePath = AppUtil.getRealPath(filePath);
					}
					FileUtil.deleteFile(this.attachPath + File.separator + filePath);
					Long showImageId = seal.getShowImageId();
					if (BeanUtils.isNotIncZeroEmpty(showImageId)) {
						SysFile sysFileIma = (SysFile) this.sysFileService.getById(showImageId);
						String filePathIma = sysFileIma.getFilepath();
						if (StringUtil.isEmpty(this.attachPath)) {
							filePathIma = AppUtil.getRealPath(filePathIma);
						}
						FileUtil.deleteFile(this.attachPath + File.separator + filePathIma);
					}
				}
			}
			this.sealService.delByIds(lAryId);
			for (Long id : lAryId) {
				this.sealRightService.delBySealId(id, SealRight.CONTROL_TYPE_SEAL);
			}
			message = new ResultMessage(1, "删除电子印章成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	/**
	 * 添加或编辑电子印章.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午01:03:05 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午01:03:05
	 * @param request
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "edit" })
	@Action(description = "添加或编辑电子印章", detail = "<#if isAdd>添加电子印章<#else>编辑电子印章<#assign entity=sealService.getById(Long.valueOf(sealId))/>【${entity.sealName}】</#if>")
	public ModelAndView edit(HttpServletRequest request) throws Exception {
		
		//获取印章信息
		Long sealId = Long.valueOf(RequestUtil.getLong(request, "sealId"));
		String returnUrl = RequestUtil.getPrePage(request);
		Seal seal = null;
		if (sealId.longValue() != 0L){
			seal = (Seal) this.sealService.getById(sealId);
		}else {
			seal = new Seal();
		}
		//获取印章授权类型
		List<Map<String, Object>> typeList = this.sealRightService.getRightType();
		//获取印章授权信息
		Map<String, Object> sealRightMap = this.sealRightService.getSealRight(sealId, SealRight.CONTROL_TYPE_SEAL);
		return this.getAutoView().addObject("seal", seal)
								 .addObject("returnUrl",returnUrl)
								 .addObject("typeList", typeList)
								 .addObject("sealRightMap", sealRightMap);
	}

	/**
	 * 查看电子印章明细.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-8-22 下午01:03:05 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-22 下午01:03:05
	 * @param request
	 * @return
	 * @throws Exception
	 * @see
	 */
	@RequestMapping( { "get" })
	@Action(description = "查看电子印章明细", detail = "查看电子印章明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		long id = RequestUtil.getLong(request, "sealId");
		String returnUrl = RequestUtil.getPrePage(request);
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		Seal seal = (Seal) this.sealService.getById(Long.valueOf(id));
		return this.getAutoView().addObject("seal", seal)
				                 .addObject("returnUrl", returnUrl)
								 .addObject("canReturn", Long.valueOf(canReturn));
	}
	
}
