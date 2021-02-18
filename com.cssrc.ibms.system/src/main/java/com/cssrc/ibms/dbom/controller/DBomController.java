package com.cssrc.ibms.dbom.controller;

import java.util.Date;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.util.annotion.Action;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.logmodel.SysAuditModelType;
import com.cssrc.ibms.core.util.result.ResultMessage;
import com.cssrc.ibms.core.web.controller.BaseController;
import com.cssrc.ibms.dbom.model.DBom;
import com.cssrc.ibms.dbom.model.DBomNode;
import com.cssrc.ibms.dbom.service.DBomService;
import com.cssrc.ibms.dbom.service.DbomMetaDataService;

/**
 * 对象功能:DBom分类管理 控制器类.
 *
 * <p>detailed comment</p>
 * @author [创建人] WeiLei <br/> 
 * 		   [创建时间] 2016-7-13 上午08:05:04 <br/> 
 * 		   [修改人] WeiLei <br/>
 * 		   [修改时间] 2016-7-13 上午08:05:04
 * @see
 */
@Controller
@RequestMapping("/oa/system/dbom/")
@Action(ownermodel = SysAuditModelType.SYSTEM_SETTING)
public class DBomController extends BaseController{

	@Resource
	private DBomService dBomService;
	
	@Resource
	private DbomMetaDataService dbomMetaDataService;
	
	/**
	 * 获取DBom分类下拉框选项数据.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午10:15:21 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:42:04
	 * @param request
	 * @param response
	 * @see
	 */
	@RequestMapping("combo")
	@ResponseBody
	@Action(description="获取DBom分类下拉框选项数据")
	public void combo(HttpServletRequest request, HttpServletResponse response){
		try {
			List<DBom> dbomDatas = dBomService.getAll();
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(JSONArray.fromObject(dbomDatas).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取DBom分类数据明细.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午10:35:01 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:41:57
	 * @param request
	 * @param response
	 * @see
	 */
	@RequestMapping("get")
	@ResponseBody
	@Action(description="获取DBom分类数据明细")
	public void get(HttpServletRequest request, HttpServletResponse response){
		
		try {
			Long id = RequestUtil.getLong(request, "id");
			JSONObject object = new JSONObject();
			object.put("data", dBomService.getById(id));
			object.put("success", "true");
			response.setCharacterEncoding("utf-8");
			response.getWriter().write(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 检查DBom分类代号是否已存在.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午11:34:23 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:41:49
	 * @param request
	 * @param response
	 * @return
	 * @see
	 */
	@RequestMapping("check")
	@ResponseBody
	@Action(description="检查DBom分类代号是否已存在")
	public boolean check(HttpServletRequest request, HttpServletResponse response){
		
		boolean result = false;
		try {
			Long id = RequestUtil.getLong(request, "id");
			String code = RequestUtil.getString(request, "code");
			result = dBomService.check(id, code);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	/**
	 * 保存DBom分类信息.
	 *
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 上午10:35:01 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-8-20 上午09:23:34
	 * @param request
	 * @param response
	 * @param dbom
	 * @see
	 */
	@RequestMapping("save")
	@ResponseBody
	@Action(description="保存DBom分类信息")
	public void save(HttpServletRequest request, HttpServletResponse response, DBom dbom){
		
		try {
			dbom.setUsername(UserContextUtil.getCurrentUser().getUsername());
			dbom.setModifiedTime(new Date());
			dBomService.save(dbom);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 删除DBom分类信息.
	 * <p>detailed comment</p>
	 * @author [创建人] WeiLei <br/> 
	 * 		   [创建时间] 2016-7-13 下午05:10:13 <br/> 
	 * 		   [修改人] WeiLei <br/>
	 * 		   [修改时间] 2016-7-13 下午05:10:13
	 * @param request
	 * @param response
	 * @see
	 */
	@RequestMapping("delete")
	@ResponseBody
	@Action(description="删除DBom分类信息及其DBom子节点信息")
	public void delete(HttpServletRequest request, HttpServletResponse response){
		try {
			Long id = RequestUtil.getLong(request, "ids");
			DBom dbom = dBomService.getById(id); 
			List<DBomNode> dbomNodeList = dbomMetaDataService.getByDbomCode(dbom.getCode());
			for(DBomNode dbomNode : dbomNodeList){
			    dbomMetaDataService.delById(dbomNode.getId());
			}
			dBomService.delById(id);
			ResultMessage resultMessage = new ResultMessage(ResultMessage.Success, "删除成功");
			writeResultMessage(response.getWriter(), resultMessage);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
