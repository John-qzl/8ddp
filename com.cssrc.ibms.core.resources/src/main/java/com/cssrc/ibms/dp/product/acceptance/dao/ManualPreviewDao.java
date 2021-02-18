package com.cssrc.ibms.dp.product.acceptance.dao;

import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cssrc.ibms.core.db.mybatis.dao.JdbcDao;

@Repository
public class ManualPreviewDao {

	@Resource
	JdbcDao jdbcDao;
	
	public String getFileId(String id) {
		String sql="select * from W_SYSCB where ID='"+id+"'";
		Map<String,Object> map=jdbcDao.queryForMap(sql, null);
		String fileInfo=map.get("F_XZ").toString();
		JSONArray jsonArray=JSONArray.parseArray(fileInfo);
		JSONObject jsonObject =jsonArray.getJSONObject(0);
		return jsonObject.getString("id").toString();
	}
}
