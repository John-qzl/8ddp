package com.cssrc.ibms.core.web.tag;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.servlet.tags.RequestContextAwareTag;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.cssrc.ibms.api.system.util.PropertyUtil;
import com.cssrc.ibms.api.sysuser.util.UserContextUtil;
import com.cssrc.ibms.core.constant.system.SysConfConstant;
import com.cssrc.ibms.core.util.appconf.AppConfigUtil;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.file.FastDFSFileOperator;
import com.cssrc.ibms.core.util.string.StringUtil;

public class SysParamTag extends RequestContextAwareTag {
	private static final long serialVersionUID = -2247520825166062410L;
	private String paramname;
	private String alias;

	@Override
	protected int doStartTagInternal() throws Exception {
		if("CUR_JSON_USER".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, UserContextUtil.getCurUserInfo());
			}else{
				pageContext.getOut().print(UserContextUtil.getCurUserInfo());
			}
		}else if("CALL_FILE_PATH".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, AppUtil.getCallFilePath());
			}else{
				pageContext.getOut().print(AppUtil.getCallFilePath());
			}
		}else if("CALL_FILE_TEMP_PATH".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, AppUtil.getCallFileTempPath());
			}else{
				pageContext.getOut().print(AppUtil.getCallFileTempPath());
			}
		}else if("CALL_FILE_SYS_TEMP_PATH".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, AppUtil.getCallFileSystemPath());
			}else{
				pageContext.getOut().print(AppUtil.getCallFileSystemPath());
			}
		}else if("maxUploadSize".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				this.pageContext.setAttribute(alias, AppConfigUtil.get(paramname));
			}else{
				pageContext.getOut().print(AppConfigUtil.get(paramname));
			}
		}else if ("fdfsserver".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
                this.pageContext.setAttribute(alias, FastDFSFileOperator.getInterviewServer());
			}else{
				pageContext.getOut().print(AppConfigUtil.get(paramname));
			}
		}else if ("SYS_UITYPE".equals(paramname)){
			//SYS_UITYPE: 系统UI界面：0为旧风格，1为新风格，新平台不支持旧风格。
			if(!StringUtil.isEmpty(alias)){
				String paramnameValue = PropertyUtil.getValueByName(paramname);
                this.pageContext.setAttribute(alias, paramnameValue);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(paramname));
			}
		}else if ("SYS_LOGIN_PNG".equals(paramname)||"SYS_LOGIN_LOG".equals(paramname)){
			//SYS_LOGIN_PNG:登录页面背景图片。   SYS_LOGIN_LOG:登录页面标语，警示语图片。
			if(!StringUtil.isEmpty(alias)){
				String paramnameValue = PropertyUtil.getValueByName(paramname);
				
				//start 解决由于老版本存放各项目登录页面的图片位置问题，将目录切换到图片迁移后的目录位置
				String theme = PropertyUtil.getValueByName("SYS_THEME");//获取主题
				int type = SysConfConstant.SHOW_TYPE;
				if(StringUtil.isNotEmpty(paramnameValue) && paramnameValue.contains("oa/images/login") && type==1){
					paramnameValue = paramnameValue.replace("oa", "styles/"+theme);
				}else{
					paramnameValue = "/styles/"+theme+paramnameValue;
				}
				//end
                this.pageContext.setAttribute(alias, paramnameValue);
			}else{
				pageContext.getOut().print(AppConfigUtil.get(paramname));
			}
		}else if("THEME_COLOR".equals(paramname)){
			if(!StringUtil.isEmpty(alias)){
				String paramnameValue = PropertyUtil.getValueByName(paramname);
				if(StringUtil.isNotEmpty(paramnameValue)){
					Map<String,String> params=null;
					try {
						List<Map<String,String>> list=JSONObject.parseObject(paramnameValue, new TypeReference<List<Map<String,String>>>(){});
						params=new HashMap<String, String>();
						for(Map<String,String> value:list){
							params.put(value.get("name"), value.get("value"));
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					if(params!=null){
						this.pageContext.setAttribute(alias, params);
					}
					
				}
				
                
			}
		}
		else{
			if(StringUtil.isEmpty(alias)){
				pageContext.getOut().print(PropertyUtil.getByAlias(paramname));
			}else{
				this.pageContext.setAttribute(alias, PropertyUtil.getByAlias(paramname));
			}
		}

		return SKIP_BODY;
	}

	public String getParamname() {
		return paramname;
	}

	public void setParamname(String paramname) {
		this.paramname = paramname;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;

	}	
}
