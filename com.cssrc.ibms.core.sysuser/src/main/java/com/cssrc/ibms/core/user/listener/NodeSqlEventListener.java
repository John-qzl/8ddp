package com.cssrc.ibms.core.user.listener;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.springframework.context.ApplicationListener;
import org.springframework.core.Ordered;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.INodeSqlService;
import com.cssrc.ibms.api.activity.model.INodeSql;
import com.cssrc.ibms.api.form.intf.IFormHandlerService;
import com.cssrc.ibms.api.form.model.IFormData;
import com.cssrc.ibms.core.db.datasource.JdbcTemplateUtil;
import com.cssrc.ibms.core.user.event.NodeSqlContext;
import com.cssrc.ibms.core.user.event.NodeSqlEvent;
import com.cssrc.ibms.core.util.common.MapUtil;
import com.cssrc.ibms.core.util.string.StringUtil;
     
/**
 * NodeSqlEventListener
 * @author liubo
 * @date 2017年2月17日
 */
@Service
public class NodeSqlEventListener implements ApplicationListener<NodeSqlEvent>, Ordered {
 
   @Resource
   INodeSqlService bpmNodeSqlService;
 
   @Resource
   IFormHandlerService bpmFormHandlerService;
     
   @SuppressWarnings({ "unchecked", "rawtypes" })
public void onApplicationEvent(NodeSqlEvent event){
	   NodeSqlContext source = (NodeSqlContext)event.getSource();
	   Map dataMap = source.getDataMap();
	   String actdefId = source.getActdefId();
	   String nodeId = source.getNodeId();
	   String action = source.getAction();
	   if (StringUtil.isEmpty(actdefId)) {
		   return;
	   }
	   List<? extends INodeSql> nodeSqls = this.bpmNodeSqlService.getByNodeIdAndActdefIdAndAction(nodeId, actdefId, action);
	   if (nodeSqls.isEmpty())
		   return;
	   try {
		   try {
			   IFormData data = this.bpmFormHandlerService.getBpmFormData(dataMap.get("businessKey").toString(), nodeId);
			   dataMap.putAll(data.getMainFields());
		   } catch (Exception e) {
			   e.printStackTrace();
		   }
		   for (INodeSql nodeSql : nodeSqls) {
			   String sql = nodeSql.getSql();
			   if ((dataMap != null) && (!dataMap.isEmpty())) {
				   Pattern pattern = Pattern.compile("<#(.*?)#>");
				   Matcher matcher = pattern.matcher(sql);
				   while (matcher.find()) {
					   String str = matcher.group();
					   String key = matcher.group(1);
					   String val = MapUtil.getString(dataMap, key);
					   sql = sql.replace(str, val);
				   }
			   }
     
			   JdbcTemplate jdbcTemplate = JdbcTemplateUtil.getNewJdbcTemplate(nodeSql.getDsAlias());
			   jdbcTemplate.execute(sql);
           	}
	   } catch (Exception e) {
		   throw new RuntimeException(e.getMessage());
	   }
	}
     
	public int getOrder() {
		return 2;
	}
}
