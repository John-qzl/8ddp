package com.cssrc.ibms.core.resources.impl;

import java.util.Map;

import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.custom.intf.ICustomService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.db.mybatis.help.JdbcHelper;
import com.cssrc.ibms.core.user.model.SysUser;
import com.cssrc.ibms.core.user.service.CurrentUserService;
import com.cssrc.ibms.core.util.common.CommonTools;
@Service
public class CustomServiceIntf implements ICustomService{

	@Override
	public String getCustomDataTemplateSql(JdbcHelper jdbcHelper, IDataTemplate bpmDataTemplate,
			IFormTable bpmFormTable, Map<String, Object> params, String sql) {
		String alias = CommonTools.Obj2String(params.get("__displayId__"));
		if(alias.equals("10000021170075")) {
			ISysUser curUser=(ISysUser) UserContextUtil.getCurrentUser();
			if(curUser.getRoles().contains("sjgly")) {
				return null;
			}
			else {
				String newsql="SELECT  DISTINCT * FROM ( SELECT xhjbsxb. ID, xhjbsxb.F_xhdh, to_char(xhjbsxb.F_sm),";
				newsql+="xhjbsxb.F_jssj,xhjbsxb.F_zgbmld,xhjbsxb.F_cjbm,xhjbsxb.F_xhfzs,xhjbsxb.F_xzxhgl,";
				newsql+="xhjbsxb.F_xhzzh,xhjbsxb.F_xhzzhID,xhjbsxb.F_rwly,xhjbsxb.F_xhzs,xhjbsxb.F_xhmc,";
				newsql+="xhjbsxb.F_xmb,xhjbsxb.F_zgbmldID,xhjbsxb.F_zxzt,xhjbsxb.F_htjfhzgbm,xhjbsxb.F_xhzsID,xhjbsxb.F_kssj,";
				newsql+="xhjbsxb.F_htjfhzgbmID,xhjbsxb.F_xmbID,xhjbsxb.F_xzxhdw,xhjbsxb.F_mj,xhglryb.F_SSXHID,xhglryb.F_RYID ";
				newsql+="FROM ";
				newsql+="W_XHJBSXB xhjbsxb ";
				newsql+="INNER JOIN W_XHGLRYB xhglryb ON xhglryb.F_SSXHID = xhjbsxb. ID ";
				newsql+="WHERE ";
				newsql+="xhglryb.F_RYID = '"+curUser.getUserId()+"'";
				newsql+=" ) T ORDER BY ID ASC";
				return newsql;
			}
		}
		else if(alias.equals("10000029830022")) {
			ISysUser curUser=(ISysUser) UserContextUtil.getCurrentUser();
			if(curUser.getRoles().contains("sjgly")) {
				return null;
			}
			else {
				String newsql="SELECT  DISTINCT * FROM ( SELECT xhjbsxb. ID, xhjbsxb.F_xhdh, to_char(xhjbsxb.F_sm),";
				newsql+="xhjbsxb.F_jssj,xhjbsxb.F_zgbmld,xhjbsxb.F_cjbm,xhjbsxb.F_xhfzs,xhjbsxb.F_xzxhgl,";
				newsql+="xhjbsxb.F_xhzzh,xhjbsxb.F_xhzzhID,xhjbsxb.F_rwly,xhjbsxb.F_xhzs,xhjbsxb.F_xhmc,";
				newsql+="xhjbsxb.F_xmb,xhjbsxb.F_zgbmldID,xhjbsxb.F_zxzt,xhjbsxb.F_htjfhzgbm,xhjbsxb.F_xhzsID,xhjbsxb.F_kssj,";
				newsql+="xhjbsxb.F_htjfhzgbmID,xhjbsxb.F_xmbID,xhjbsxb.F_xzxhdw,xhjbsxb.F_mj,xhglryb.F_SSXHID,xhglryb.F_RYID ";
				newsql+="FROM ";
				newsql+="W_XHJBSXB xhjbsxb ";
				newsql+="INNER JOIN W_XHGLRYB xhglryb ON xhglryb.F_SSXHID = xhjbsxb. ID ";
				newsql+="WHERE ";
				newsql+="xhglryb.F_RYID = '"+curUser.getUserId()+"'";
				newsql+=" ) T ORDER BY ID ASC";
				return newsql;
			}
		}
		
		/*else if(alias.equals("10000027710053")) {
			ISysUser curUser=(ISysUser) UserContextUtil.getCurrentUser();
			String newsql="SELECT * FROM ( SELECT gzxkb. ID,gzxkb.F_xyb,gzxkb.F_zzID,gzxkb.F_zz,gzxkb.F_zjID,";
			newsql+="gzxkb.F_dqzt,gzxkb.F_gzm,gzxkb.F_lx FROM W_GZXKB gzxkb ";
			newsql+="INNER JOIN W_CPYSCHBGB cpyschgb ON gzxkb.F_ZJID = cpyschgb. ID";
			newsql+=" WHERE cpyschgb.F_JSFZRID='"+curUser.getUserId()+"'";
			newsql+=" or cpyschgb.F_YSZZID='"+curUser.getUserId()+"'";
			newsql+=" ) T ORDER BY ID ASC";
			return newsql;
		}*/
		return null;
	}

	@Override
	public Map getCustomDataTemplateMap(IDataTemplate bpmDataTemplate, IFormTable bpmFormTable,
			Map<String, Object> params, Map<String, Object> map) {
		return null;
	}

	@Override
	public String getCustomFormDialogSql(JdbcHelper jdbcHelper, IFormDialog bpmFormDialog, IFormTable formtable,
			Map<String, Object> params, Map<String, Object> map, String sql) {
		 String dialogAlias = CommonTools.Obj2String(params.get("dialog_alias_"));
		 if(dialogAlias.equals("gwxz")) {
			 Long userId = UserContextUtil.getCurrentUserId();
			 sql = sql.replace("1=1", "1=1 and F_CYID like '%"+userId+"%' ");
		 }
		return sql;
	}

}
