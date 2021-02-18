package com.cssrc.ibms.system.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.log.util.LogThreadLocalHolder;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.annotion.ActionExecOrder;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.file.FileUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.msg.MsgUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.system.model.SerialNumber;
import com.cssrc.ibms.system.service.SerialNumberService;
/**
 * 流水号管理
 * <p>Title:SerialNumberController</p>
 * @author Yangbo 
 * @date 2016-8-31下午03:30:18
 */
@Controller
@RequestMapping({"/oa/system/serialNumber/"})
@Action(ownermodel=SysAuditModelType.PROCESS_AUXILIARY)
public class SerialNumberController extends BaseController
{

	@Resource
	private SerialNumberService serialNumberService;

	@RequestMapping({"list"})
	@Action(description="查看流水号生成分页列表")
	public ModelAndView list(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List list = this.serialNumberService.getAll(new QueryFilter(request, "serialNumberItem"));
		ModelAndView mv = getAutoView().addObject("serialNumberList", list);
		return mv;
	}

	@RequestMapping({"showlist"})
	@Action(description="查看流水号生成分页列表", detail="查看流水号生成分页列表")
	public ModelAndView showlist(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		List list = this.serialNumberService.getAll(new QueryFilter(request, "serialNumberItem"));
		ModelAndView mv = getAutoView().addObject("serialNumberList", list);
		return mv;
	}

	@RequestMapping({"del"})
	@Action(description="删除流水号", execOrder=ActionExecOrder.AFTER, detail="删除流水号:<#list StringUtils.split(id,\",\") as item><#assign entity=serialNumberService.getById(Long.valueOf(item))/>【${entity.name}】</#list>")
	public void del(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.serialNumberService.delByIds(lAryId);
			message = new ResultMessage(1, "删除流水号生成成功!");
		}
		catch (Exception ex) {
			message = new ResultMessage(0, "删除失败:" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping({"edit"})
	@Action(description="添加或编辑流水号生成", detail="<#if isAdd>添加新的流水号生成<#else>编辑流水号:【${serialNumber.name}】</#if>")
	public ModelAndView edit(HttpServletRequest request)
	throws Exception
	{
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		//传入来源页面url（带get方法传入的参数）用于返回
		String returnUrl = RequestUtil.getPrePage(request);
		String islist = RequestUtil.getString(request, "islist");
		SerialNumber serialNumber = null;
		boolean isadd = true;
		if (id.longValue() != 0L) {
			serialNumber = (SerialNumber)this.serialNumberService.getById(id);
			isadd = false;
		} else {
			serialNumber = new SerialNumber();
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		LogThreadLocalHolder.putParamerter("serialNumber", serialNumber);
		return getAutoView().addObject("serialNumber", serialNumber).addObject("returnUrl", returnUrl).addObject("islist", islist);
	}

	@RequestMapping({"getAllSerialNumber"})
	@ResponseBody
	public List<SerialNumber> getAllSerialNumber(HttpServletRequest request)
	throws Exception
	{
		return this.serialNumberService.getAll();
	}

	@RequestMapping({"get"})
	@Action(description="查看流水号生成明细", detail="查看流水号生成明细")
	public ModelAndView get(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{   //传入来源页面url（带get方法传入的参数）用于返回
		String returnUrl = RequestUtil.getPrePage(request);
		long id = RequestUtil.getLong(request, "id");
		long canReturn = RequestUtil.getLong(request, "canReturn", 0L);
		SerialNumber serialNumber = (SerialNumber)this.serialNumberService.getById(Long.valueOf(id));
		return getAutoView().addObject("serialNumber", serialNumber).addObject("returnUrl", returnUrl).addObject("canReturn", Long.valueOf(canReturn));
	}
	/**
	 * 停用，后续删掉
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"getById"})
	public void getById(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String alias = RequestUtil.getString(request, "alias");
		SerialNumber serialNumber = this.serialNumberService.getByAlias(alias);
		writeResultMessage(response.getWriter(), serialNumber.getName(), 1);
	}
	/**
	 * 根据别名获得流水号
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping({"getByAlias"})
	public void getByAlias(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String alias = RequestUtil.getString(request, "alias");
		SerialNumber serialNumber = this.serialNumberService.getByAlias(alias);
		writeResultMessage(response.getWriter(), serialNumber.getName(), 1);
	}

	@RequestMapping({"export"})
	@Action(description="导出选择导出xml")
	public ModelAndView export(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String tableIds = RequestUtil.getString(request, "tableIds");
		ModelAndView mv = getAutoView();
		mv.addObject("tableIds", tableIds);
		return mv;
	}

	@RequestMapping({"exportXml"})
	@Action(description="导出流水号", detail="导出流水号:<#list StringUtils.split(tableIds,\",\") as item><#assign entity=FormTableService.getById(Long.valueOf(item))/>【${entity.tableDesc}(${entity.tableName})】</#list>")
	public void exportXml(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String strXml = null;
		String fileName = null;
		Long[] tableIds = RequestUtil.getLongAryByStr(request, "tableIds");
		List list = this.serialNumberService.getAll();
		try {
			if (BeanUtils.isEmpty(tableIds)) {
				if (BeanUtils.isNotEmpty(list)) {
					strXml = this.serialNumberService.exportXml(list);
					fileName = "全部流水号记录_" + 
					DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + 
					".xml";
				}
			} else {
				strXml = this.serialNumberService.exportXml(tableIds);
				fileName = DateFormatUtil.getNowByString("yyyyMMddHHmmdd") + 
				".xml";
				if (tableIds.length == 1) {
					SerialNumber serialNumber = (SerialNumber)this.serialNumberService.getById(tableIds[0]);
					fileName = serialNumber.getName() + "_" + fileName;
				} else if (tableIds.length == list.size()) {
					fileName = "全部流水号记录_" + fileName;
				} else {
					fileName = "多条流水号记录_" + fileName;
				}
			}
			FileUtil.downLoad(request, response, strXml, fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping({"importXml"})
	@Action(description="导入流水号")
	public void importXml(MultipartHttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		MultipartFile fileLoad = request.getFile("xmlFile");
		ResultMessage message = null;
		try {
			this.serialNumberService.importXml(fileLoad.getInputStream());
			message = new ResultMessage(1, 
					MsgUtil.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
			message = new ResultMessage(0, "导入文件异常，请检查文件格式！");
		}
		writeResultMessage(response.getWriter(), message);
	}

	@RequestMapping({"getnextId"})
	@ResponseBody
	public Map<String, Object> getnextId(HttpServletRequest request, HttpServletResponse response)
	throws Exception
	{
		String alias = RequestUtil.getString(request, "alias");
		Map map = new HashMap();
		String rtn = this.serialNumberService.nextId(alias);
		if (rtn != null) {
			map.put("rtn", rtn);
			map.put("success", Integer.valueOf(1));
		} else {
			map.put("success", Integer.valueOf(0));
		}
		return map;
	}
	
	@RequestMapping({ "save" })
	@Action(description = "添加或更新流水号生成", detail = "<#if isAdd>添加<#else>更新</#if>流水号生成：【${SysAuditLinkService.getSerialNumberLink(Long.valueOf(id))}】")
	public void save(HttpServletRequest request, HttpServletResponse response,
			SerialNumber serialNumber, BindingResult bindResult)
			throws Exception {
		ResultMessage resultMessage = validForm("serialNumber", serialNumber,
				bindResult, request);

		if (resultMessage.getResult() == 0) {
			writeResultMessage(response.getWriter(), resultMessage);
			return;
		}
		boolean isadd = true;
		String resultMsg = null;
		if (serialNumber.getId().longValue() == 0L) {
			boolean rtn = this.serialNumberService.isAliasExisted(serialNumber
					.getAlias());
			if (rtn) {
				writeResultMessage(response.getWriter(), "别名已经存在!", 0);
				return;
			}

			serialNumber.setId(Long.valueOf(UniqueIdUtil.genId()));
			serialNumber.setCurDate(serialNumberService.getCurDate(serialNumber.getGenType().shortValue()));
			this.serialNumberService.add(serialNumber);
			resultMsg = "添加流水号生成成功";
		} else {
			boolean rtn = this.serialNumberService
					.isAliasExistedByUpdate(serialNumber);
			if (rtn) {
				writeResultMessage(response.getWriter(), "别名已经存在!", 0);
				return;
			}
			this.serialNumberService.update(serialNumber);
			resultMsg = "更新流水号生成成功";
			isadd = false;
		}
		LogThreadLocalHolder.putParamerter("isAdd", Boolean.valueOf(isadd));
		LogThreadLocalHolder.putParamerter("id", serialNumber.getId()
				.toString());
		writeResultMessage(response.getWriter(), resultMsg, 1);
	}
	
}


