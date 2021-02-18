package com.cssrc.ibms.bus.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ezmorph.object.DateMorpher;
import net.sf.json.JSONObject;
import net.sf.json.util.JSONUtils;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.sysuser.util.CommonVar;
import com.cssrc.ibms.bus.model.BusQueryRule;
import com.cssrc.ibms.bus.service.BusQueryRuleService;
import com.cssrc.ibms.core.db.mybatis.query.QueryFilter;
import com.cssrc.ibms.core.db.mybatis.query.scan.SearchCache;
import com.cssrc.ibms.core.db.mybatis.query.scan.TableEntity;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.core.web.controller.BaseController;
/**
 * 高级查询规则
 * <p>Title:BusQueryRuleController</p>
 * @author Yangbo 
 * @date 2016-8-8下午04:23:33
 */
@Controller
@RequestMapping( { "/oa/bus/busQueryRule/" })
public class BusQueryRuleController extends BaseController {

	@Resource
	private BusQueryRuleService busQueryRuleService;

	@RequestMapping( { "save" })
	@Action(description = "添加或更新高级查询规则")
	public void save(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String resultMsg = null;
		BusQueryRule busQueryRule = getFormObject(request);
		try {
			if ((busQueryRule.getId() == null)
					|| (busQueryRule.getId().longValue() == 0L)) {
				busQueryRule.setId(Long.valueOf(UniqueIdUtil.genId()));
				this.busQueryRuleService.add(busQueryRule);
				resultMsg = getText("添加", new Object[] { "高级查询规则" });
			} else {
				this.busQueryRuleService.update(busQueryRule);
				resultMsg = getText("更新", new Object[] { "高级查询规则" });
			}
			writeResultMessage(response.getWriter(), resultMsg, 1);
		} catch (Exception e) {
			writeResultMessage(response.getWriter(), resultMsg + ","
					+ e.getMessage(), 0);
		}
	}

	protected BusQueryRule getFormObject(HttpServletRequest request)
			throws Exception {
		JSONUtils.getMorpherRegistry().registerMorpher(
				new DateMorpher(new String[] { "yyyy-MM-dd HH:mm:ss" }));

		String json = RequestUtil.getString(request, "json", false);
		if (StringUtil.isEmpty(json))
			return null;
		JSONObject obj = JSONObject.fromObject(json);
		String displayField = obj.getString("displayField");
		String sortField = obj.getString("sortField");
		String filterField = obj.getString("filterField");
		String exportField = obj.getString("exportField");

		obj.remove("displayField");
		obj.remove("conditionField");
		obj.remove("sortField");
		obj.remove("filterField");
		obj.remove("manageField");

		BusQueryRule busQueryRule = (BusQueryRule) JSONObject.toBean(obj,
				BusQueryRule.class);

		busQueryRule.setDisplayField(displayField);
		busQueryRule.setSortField(sortField);
		busQueryRule.setFilterField(filterField);
		busQueryRule.setExportField(exportField);

		return busQueryRule;
	}

	@RequestMapping( { "list" })
	@Action(description = "查看高级查询规则分页列表")
	public ModelAndView list(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		List<TableEntity> list = TableEntity.getAll(new QueryFilter(request,
				"tableEntityItem"));

		Map<String,Object> queryRuleCounts = new HashMap<String,Object>();

		for (TableEntity tableEntity : list) {
			String tableName = tableEntity.getTableName();
			queryRuleCounts.put(tableName, this.busQueryRuleService
					.getCountByTableName(tableName));
		}

		return getAutoView().addObject("tableEntityList", list).addObject(
				"queryRuleCounts", queryRuleCounts);
	}

	@RequestMapping( { "del" })
	@Action(description = "删除高级查询规则")
	public void del(HttpServletRequest request, HttpServletResponse response)
			throws Exception {
		String preUrl = RequestUtil.getPrePage(request);
		ResultMessage message = null;
		try {
			Long[] lAryId = RequestUtil.getLongAryByStr(request, "id");
			this.busQueryRuleService.delByIds(lAryId);
			message = new ResultMessage(1, "删除高级查询规则成功!");
		} catch (Exception ex) {
			message = new ResultMessage(0, "删除失败" + ex.getMessage());
		}
		addMessage(message, request);
		response.sendRedirect(preUrl);
	}

	@RequestMapping( { "edit_{tableName}" })
	@Action(description = "编辑高级查询规则")
	public ModelAndView editTable(HttpServletRequest request,
			HttpServletResponse response,
			@PathVariable("tableName") String tableName) throws Exception {
		return new ModelAndView("redirect:edit.do?tableName=" + tableName);
	}

	@RequestMapping( { "edit" })
	@Action(description = "编辑高级查询规则")
	public ModelAndView edit(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableName = RequestUtil.getString(request, "tableName");
		TableEntity tableEntity = (TableEntity) SearchCache.getTableEntityMap()
				.get(tableName);
		BusQueryRule busQueryRule = this.busQueryRuleService
				.getByTableEntity(tableEntity);

		return getAutoView().addObject("busQueryRule", busQueryRule).addObject(
				"tableEntity", tableEntity).addObject("bpmFormTableJSON",
				JSONObject.fromObject(tableEntity)).addObject("DataRightsJson",
				JSONObject.fromObject(busQueryRule));
	}

	@RequestMapping( { "get" })
	@Action(description = "查看高级查询规则明细")
	public ModelAndView get(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		Long id = Long.valueOf(RequestUtil.getLong(request, "id"));
		BusQueryRule busQueryRule = (BusQueryRule) this.busQueryRuleService
				.getById(id);
		return getAutoView().addObject("busQueryRule", busQueryRule);
	}

	@RequestMapping( { "filterDialog" })
	public ModelAndView filterDialog(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableName = RequestUtil.getString(request, "tableName");
		TableEntity tableEntity = (TableEntity) SearchCache.getTableEntityMap()
				.get(tableName);
		List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
		return getAutoView().addObject("commonVars", commonVars).addObject(
				"tableEntity", tableEntity).addObject("tableName", tableName);
	}

	@RequestMapping( { "script" })
	public ModelAndView script(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String tableName = RequestUtil.getString(request, "tableName");
		TableEntity tableEntity = (TableEntity) SearchCache.getTableEntityMap()
				.get(tableName.toLowerCase());
		List<CommonVar> commonVars = CommonVar.getCurrentVars(false);
		return getAutoView().addObject("commonVars", commonVars).addObject(
				"tableEntity", tableEntity).addObject("tableName", tableName);
	}
}
