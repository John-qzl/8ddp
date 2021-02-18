package com.cssrc.ibms.core.resources.datapackage.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.cssrc.ibms.api.core.util.UniqueIdUtil;
import com.cssrc.ibms.api.rec.intf.IRecRoleSonService;
import com.cssrc.ibms.api.rec.model.IRecRoleSon;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;
import com.cssrc.ibms.core.resources.datapackage.dao.DataPackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.PackageDao;
import com.cssrc.ibms.core.resources.datapackage.dao.WBSDao;
import com.cssrc.ibms.core.resources.datapackage.model.NodeFilter;
import com.cssrc.ibms.core.resources.datapackage.model.WorkPlan;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.date.DateFormatUtil;
import com.cssrc.ibms.core.util.http.RequestUtil;
import com.cssrc.ibms.core.util.string.StringUtil;















import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.net.aso.i;

@Service
public class WBSService 
{
	@Resource
	private WBSDao wbsDao;
	@Resource
	private PackageDao packageDao;
	/**
	 * 获取甘特图数据
	 * @param request
	 * @return
	 */
	public Map<String, Object> getWBSData(HttpServletRequest request) {
		Map<String,Object> result=new HashMap<String,Object>();
		
		//所属的数据包ID
		String sjbId=RequestUtil.getString(request, "sjbId");
		Map<String,Object> parms=new HashMap<String,Object>();
		parms.put("F_SSSJBJD", sjbId);
		
		//是否展开树
		boolean open =true;
		List<Map<String, Object>> plans=new ArrayList<Map<String,Object>>();
		//查询根节点
		Map<String,Object> rootWorkPlan=packageDao.getById(Long.valueOf(sjbId));
		//处理根节点
		Map<String, Object> root = new HashMap<String, Object>();
		root.put("id", sjbId);
		root.put("open", open);
		root.put("order", 0);
		root.put("text", RequestUtil.getString(request, "sjbName"));
		String type=CommonTools.Obj2String(rootWorkPlan.get("F_JDLX"));
		if(type.equals("设计节点")){
			root.put("start_date",CommonTools.Obj2String(rootWorkPlan.get("F_DESI_JHKSSJ")));
			root.put("end_date",CommonTools.Obj2String(rootWorkPlan.get("F_DESI_JHJSSJ")));
		}else if(type.equals("试验节点")){
			root.put("start_date",CommonTools.Obj2String(rootWorkPlan.get("F_TEST_JHKSSJ")));
			root.put("end_date",CommonTools.Obj2String(rootWorkPlan.get("F_TEST_JHJSSJ")));
		}
		
		root.put("type", "project");
		root.put("readonly", true);
		plans.add(root);
		//查询其他节点
		List<WorkPlan>workPlans=wbsDao.getDataInSjbID(parms);
		
		//处理其他节点
		Map<String, Object> other;
		for(WorkPlan workPlan:workPlans){
			other=new HashMap<String,Object>();
			other.put("id", workPlan.getId());
			other.put("text", workPlan.getName());
			other.put("open", open);
			other.put("taskType", workPlan.getType());
			String parentId=workPlan.getParentPlanId();
			other.put("parent",parentId);
			other.put("number", workPlan.getIndex());
			other.put("order", workPlan.getIndex());
			other.put("end_date", workPlan.getPlanEndTime());
			other.put("start_date", workPlan.getPlanStartTime());
			other.put("real_start_date", workPlan.getRealStartTime());
			other.put("real_end_date", workPlan.getRealEndTime());
			plans.add(other);
		}
		//设置plans中每个plan的subId
		//首先按照Id进行分类
		Map<String,Map<String,Object>> mapPlans=listToMap(plans);
		for(Map<String,Object> plan:plans){
			if(plan.containsKey("parent")){
				String parentId=plan.get("parent").toString();
				if(mapPlans.containsKey(parentId)){
					//获取parent
					Map<String,Object> parent=mapPlans.get(parentId);
					//如果parent中已经含有subId,追加
					if(parent.get("subId")!=null){
						String subId=parent.get("subId").toString();
						parent.put("subId", subId+","+plan.get("id").toString());
					}else{//新增
						parent.put("subId", plan.get("id").toString());
					}
				}
			}
		}
		result.put("data", plans);
		return result;
	}
	private Map<String, Map<String, Object>> listToMap(List<Map<String, Object>> plans) {
		Map<String,Map<String,Object>> mapPlans=new HashMap<String,Map<String,Object>>();
		for(Map<String,Object> plan:plans){
			mapPlans.put(plan.get("id").toString(), plan);
		}
		return mapPlans;
	}
	/**
	 * 获取当前任务编号
	 * @param curNodeOrder 
	 */
	public String getSubTaskBh(String sjbId, String curNodeId, String curNodeOrder) {
		
		String order= wbsDao.getSubTaskBh(sjbId,curNodeId);
		if(order.equals("")){
			if(curNodeOrder.equals("0")){
				return "1";
			}
			return curNodeOrder+".1";
		}
		if(order.contains(".")){
			int lastd=order.lastIndexOf(".");
			String pre=order.substring(0,lastd);
			String last=order.substring(lastd+1,order.length());
			return pre+"."+(Integer.valueOf(last)+1);
		}
		return (Integer.valueOf(order)+1)+"";
	}
	public void exchangeTaskOrder(String jsonData) {
		List<String> insertList = new ArrayList<String>();
		JSONArray jsonArray = JSONArray.fromObject(jsonData);
		for (int i = 1;i<jsonArray.size();i++) {
			StringBuffer stringBuffer = new StringBuffer();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			stringBuffer.append("UPDATE W_TASK SET ").append("F_XH = '" + jsonObject.get("order") + "' ");
			if(jsonObject.get("parent")!=null&&!jsonObject.get("parent").toString().equals("0")){
				stringBuffer.append(",F_SSFSYRW = '" + jsonObject.get("parent") + "' ");
			}else{
//				stringBuffer.append(",");
//				stringBuffer.append("F_SJRW = '' ");
			}
			stringBuffer.append(" where ID='" + jsonObject.get("id") + "'");
			insertList.add(stringBuffer.toString());
		}
		wbsDao.updateTaskOrder(insertList);
	}
}