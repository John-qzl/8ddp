package com.cssrc.ibms.core.form.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.cssrc.ibms.api.core.util.ContextUtil;
import com.cssrc.ibms.api.form.model.IFieldPool;
import com.cssrc.ibms.api.form.model.IFormField;
import com.cssrc.ibms.api.form.model.ITableModel;
import com.cssrc.ibms.api.migration.model.IOutField;
import com.cssrc.ibms.api.system.intf.IDictionaryService;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.core.db.mybatis.model.BaseModel;
import com.cssrc.ibms.core.util.annotion.SysFieldDescription;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.common.CommonTools;
import com.cssrc.ibms.core.util.string.StringPool;
import com.cssrc.ibms.core.util.string.StringUtil;
/**
 * 对象功能:ibms_form_field Model对象 开发人员:zhulongchao
 */
@XmlRootElement(name = "field")
@XmlAccessorType(XmlAccessType.NONE)
public class FormField extends BaseModel implements Cloneable, IFormField {
	@XmlAttribute
	@SysFieldDescription(detail="fieldId")
	protected Long fieldId = 0L;
	// tableId
	@XmlAttribute
	@SysFieldDescription(detail="tableId")
	protected Long tableId = 0L;
	@SysFieldDescription(detail="--formTable")
    protected FormTable formTable;
	// 字段名称
	@XmlAttribute
	@SysFieldDescription(detail="字段名称")
	protected String fieldName = "";
	// 字段类型
	@XmlAttribute
	@SysFieldDescription(detail="字段类型")
	protected String fieldType = "";
	// 是否必填
	@XmlAttribute
	@SysFieldDescription(detail="是否必填",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isRequired = 0;

	// 是否列表显示
	@XmlAttribute
	@SysFieldDescription(detail="是否列表显示",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isList = 1;
	// 是否查询显示
	@XmlAttribute
	@SysFieldDescription(detail="是否查询显示",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isQuery = 1;
	// 说明
	@XmlAttribute
	@SysFieldDescription(detail="说明")
	protected String fieldDesc = "";
	// 字符长度，仅针对字符类型
	@XmlAttribute
	@SysFieldDescription(detail="字符长度")
	protected Integer charLen = 0;
	// 整数长度，仅针对数字类型
	@XmlAttribute
	@SysFieldDescription(detail="整数长度")
	protected Integer intLen = 0;
	// 小数长度，仅针对数字类型
	@XmlAttribute
	@SysFieldDescription(detail="小数长度")
	protected Integer decimalLen = 0;
	// 数据字典的类别，仅针对数据字典类型
	@XmlAttribute
	@SysFieldDescription(detail="数据字典的类别")
	protected String dictType = "";
	// 是否删除
	@XmlAttribute
	@SysFieldDescription(detail="是否删除",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isDeleted = 0;
	// 验证规则
	@XmlAttribute
	@SysFieldDescription(detail="验证规则")
	protected String validRule = "";
	// 字段原名
	@XmlAttribute
	@SysFieldDescription(detail="字段原名")
	protected String originalName = "";
	// 排列顺序
	@XmlAttribute
	@SysFieldDescription(detail="排列顺序")
	protected Integer sn = 0;
	// 值来源 0-表单 1-脚本
	@XmlAttribute
	@SysFieldDescription(detail="值来源",maps="{\"0\":\"表单\",\"1\":\"脚本\"}")
	protected Short valueFrom = 0;
	// 脚本
	@XmlAttribute
	@SysFieldDescription(detail="script")
	protected String script = "";
	// 控件类型
	@XmlAttribute
	@SysFieldDescription(detail="控件类型")
	protected Short controlType = 0;
	// 是否隐藏域
	@XmlAttribute
	@SysFieldDescription(detail="是否隐藏域",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isHidden = 0;
	// 是否流程变量
	@XmlAttribute
	@SysFieldDescription(detail="是否流程变量",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isFlowVar = 0;
	// 流水号别名
	@XmlAttribute
	@SysFieldDescription(detail="流水号别名")
	protected String serialNumber = "";
	// 下拉框
	@XmlAttribute
	@SysFieldDescription(detail="下拉框")
	protected String options = "";
	// 控件属性
	@XmlAttribute
	@SysFieldDescription(detail="控件属性")
	protected String ctlProperty = "";
	// 新添加的列
	@XmlAttribute
	@SysFieldDescription(detail="新添加的列",maps="{\"false\":\"否\",\"true\":\"是\"}")
	protected boolean isAdded = false;

	// 支持手机客户端显示
	@XmlAttribute
	@SysFieldDescription(detail="是否支持手机客户端显示",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isAllowMobile = 0;
	// 文字为日期格式
	@XmlAttribute
	@SysFieldDescription(detail="文字为日期格式")
	protected Short isDateString = 0;
	// 文字取当前日期
	@XmlAttribute
	@SysFieldDescription(detail="文字取当前日期")
	protected Short isCurrentDateStr = 0;
	// 控件样式
	@XmlAttribute
	@SysFieldDescription(detail="控件样式")
	protected String style;
	// 流程引用
	@XmlAttribute
	@SysFieldDescription(detail="流程引用")
	protected Short isReference;
    // 加密算法
    @XmlAttribute
    @SysFieldDescription(detail="加密算法")
    protected String encrypt;
    
	// 非数据库字段 日期格式
	@XmlAttribute
	@SysFieldDescription(detail="非数据库字段 日期格式")
	protected String datefmt;
	
	

	/**
	 * 选择器ID脚本。
	 */
	@XmlAttribute
	@SysFieldDescription(detail="选择器ID脚本")
	protected String scriptID = "";

	// 该字段是否主键
	// 1为主键
	// protected Integer isPk = 0;
	@XmlAttribute
	@SysFieldDescription(detail="该字段是否主键",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected short type;

	// web印章验证 1为支持，0为不用
	@XmlAttribute
	@SysFieldDescription(detail="是否支持web印章验证",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isWebSign = 0;
	// 唯一约束
	@XmlAttribute
	@SysFieldDescription(detail="唯一约束",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isUnique = 0;
	// 是否为主数据
	@XmlAttribute
	@SysFieldDescription(detail="是否为主数据",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isMainData = 0;
	// 数字类型是否显示千分位
	@XmlAttribute
	@SysFieldDescription(detail="数字类型是否显示千分位",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isShowComdify;
	// 数字类型显示货币符号
	@XmlAttribute
	@SysFieldDescription(detail="数字类型显示货币符号")
	protected String coinValue;
	// 关联关系表ID
	@XmlAttribute
	@SysFieldDescription(detail="关联关系表ID")
	protected Long relTableId;
	// 关联关系表Name
	@XmlAttribute
	@SysFieldDescription(detail="关联关系表Name")
    protected String relTableName;
	// 关联关系表类型 0-一对一、2-多对一
	@XmlAttribute
	@SysFieldDescription(detail="关联关系表类型",maps="{\"0\":\"一对一\",\"2\":\"多对一\"}")
	protected Short relTableType;
	// 是否主键显示值 0-否，1-是
	@XmlAttribute
	@SysFieldDescription(detail="是否主键显示值",maps="{\"0\":\"否\",\"1\":\"是\"}")
	protected Short isPkShow;
	// rel关联表记录删除类型，1-直接删除，0-取消关联。
	@XmlAttribute
	@SysFieldDescription(detail="rel关联表记录删除类型",maps="{\"0\":\"取消关联\",\"1\":\"直接删除\"}")
	protected Short relDelType;
	// 主表记录删除，rel关联表记录删除类型，1-直接删除，0-取消关联。
	@XmlAttribute
	@SysFieldDescription(detail="主表记录删除，rel关联表记录删除类型",maps="{\"0\":\"取消关联\",\"1\":\"直接删除\"}")
	protected Short relDelLMType;
	// 关联的自定义对话框信息
	// e.g.{name:'ctcsbdhk',fields:[{src:'ID',target:'kf_test_id'},{src:'F_NAME',target:'kf_test_idXXXXXFKColumnShow'}],query:[]}
	@XmlAttribute
	@SysFieldDescription(detail="关联的自定义对话框信息")
	protected String relFormDialog;
	// relFormDialog去掉"<![CDATA[]]>";
	@SysFieldDescription(detail="关联的自定义对话框StripCData")
	protected String relFormDialogStripCData;
	public static String SYNC_FIELD = "[{'charLen':200,'intLen':0,"
			+ "'decimalLen':0,'dictType':'','identity':'','validRule':'','isDeleted':0,"
			+ "'valueFrom':'0','script':'','controlType':'1','fieldName':'id_from',"
			+ "'fieldDesc':'数据源ID','fieldType':'varchar','isRequired':0,"
			+ "'isList':0,'isQuery':0,'isFlowVar':0,'isWebSign':0,'isUnique':0,'relTableId':'','isPkShow':0,'relTableType':'','relDelType':'','relDelLMType':'','relFormDialog':''},"
			+ "{'charLen':200,'intLen':0,"
			+ "'decimalLen':0,'dictType':'','identity':'','validRule':'',"
			+ "'isDeleted':0,'valueFrom':'0','script':'','controlType':'1','fieldName':'place_from',"
			+ "'fieldDesc':'数据源','fieldType':'varchar','isRequired':0,"
			+ "'isList':0,'isQuery':0,'isFlowVar':0,'isWebSign':0,'isUnique':0,'relTableId':'','isPkShow':0,'relTableType':'','relDelType':'','relDelLMType':'','relFormDialog':''},"
			+ "{'charLen':200,'intLen':0,"
			+ "'decimalLen':0,'dictType':'','identity':'','validRule':'',"
			+ "'isDeleted':0,'valueFrom':'0','script':'','controlType':'1','fieldName':'place_to',"
			+ "'fieldDesc':'数据目标','fieldType':'varchar','isRequired':0,"
			+ "'isList':0,'isQuery':0,'isFlowVar':0,'isWebSign':0,'isUnique':0,'relTableId':'','isPkShow':0,'relTableType':'','relDelType':'','relDelLMType':'','relFormDialog':''},"
			+ "{'charLen':200,'intLen':0,"
			+ "'decimalLen':0,'dictType':'','identity':'','validRule':'','isDeleted':0,"
			+ "'valueFrom':'0','script':'','controlType':'1','fieldName':'id_to',"
			+ "'fieldDesc':'数据目标ID','fieldType':'varchar','isRequired':0,'isList':0,"
			+ "'isQuery':0,'isFlowVar':0,'isWebSign':0,'isUnique':0,'relTableId':'','isPkShow':0,'relTableType':'','relDelType':'','relDelLMType':'','relFormDialog':''},"
			+ "{'charLen':200,'intLen':0,"
			+ "'decimalLen':0,'dictType':'','identity':'','validRule':'','isDeleted':0,"
			+ "'valueFrom':'0','script':'','controlType':'1','fieldName':'msg_no',"
			+ "'fieldDesc':'同步批次','fieldType':'varchar','isRequired':0,'isList':0,"
			+ "'isQuery':0,'isFlowVar':0,'isWebSign':0,'isUnique':0,'relTableId':'','isPkShow':0,'relTableType':'','relDelType':'','relDelLMType':'','relFormDialog':''}]";

	public void setFieldId(Long fieldId) {
		this.fieldId = fieldId;
	}

	/**
	 * 返回 fieldId
	 * 
	 * @return
	 */
	public Long getFieldId() {
		return fieldId;
	}

	public void setTableId(Long tableId) {
		this.tableId = tableId;
	}

	public Short getIsAllowMobile() {
		return isAllowMobile;
	}

	public Short getIsMainData() {
		return isMainData;
	}

	public void setIsMainData(Short isMainData) {
		this.isMainData = isMainData;
	}

	public void setIsAllowMobile(Short isAllowMobile) {
		this.isAllowMobile = isAllowMobile;
	}

	public Short getIsWebSign() {
		return isWebSign;
	}

	public void setIsWebSign(Short isWebSign) {
		this.isWebSign = isWebSign;
	}

	public Short getIsUnique() {
		return isUnique;
	}

	public void setIsUnique(Short isUnique) {
		this.isUnique = isUnique;
	}

	/**
	 * 返回 tableId
	 * 
	 * @return
	 */
	public Long getTableId() {
		return tableId;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	/**
	 * 返回 字段名称
	 * 
	 * @return
	 */
	public String getFieldName() {
		return fieldName;
	}

	public void setFieldType(String fieldType) {
		this.fieldType = fieldType;
	}

	/**
	 * 返回 字段类型
	 * 
	 * @return
	 */
	public String getFieldType() {
		return fieldType;
	}

	public void setIsRequired(Short isRequired) {
		this.isRequired = isRequired;
	}

	/**
	 * 返回 是否必填
	 * 
	 * @return
	 */
	public Short getIsRequired() {
		return isRequired;
	}

	public void setIsList(Short isList) {
		this.isList = isList;
	}

	/**
	 * 返回 是否列表显示
	 * 
	 * @return
	 */
	public Short getIsList() {
		return isList;
	}

	public void setIsQuery(Short isQuery) {
		this.isQuery = isQuery;
	}

	/**
	 * 返回 是否查询显示
	 * 
	 * @return
	 */
	public Short getIsQuery() {
		return isQuery;
	}

	public void setFieldDesc(String fieldDesc) {
		this.fieldDesc = fieldDesc;
	}

	/**
	 * 返回 说明
	 * 
	 * @return
	 */
	public String getFieldDesc() {
		return fieldDesc;
	}

	public void setCharLen(Integer charLen) {
		this.charLen = charLen;
	}

	/**
	 * 返回 字符长度，仅针对字符类型
	 * 
	 * @return
	 */
	public Integer getCharLen() {
		return charLen;
	}

	public void setIntLen(Integer intLen) {
		this.intLen = intLen;
	}

	/**
	 * 返回 整数长度，仅针对数字类型
	 * 
	 * @return
	 */
	public Integer getIntLen() {
		return intLen;
	}

	public void setDecimalLen(Integer decimalLen) {
		this.decimalLen = decimalLen;
	}

	/**
	 * 返回 小数长度，仅针对数字类型
	 * 
	 * @return
	 */
	public Integer getDecimalLen() {
		return decimalLen;
	}

	public void setDictType(String dictType) {
		this.dictType = dictType;
	}

	/**
	 * 返回 数据字典的类别，仅针对数据字典类型
	 * 
	 * @return
	 */
	public String getDictType() {
		return dictType;
	}

	public void setIsDeleted(Short isDeleted) {
		this.isDeleted = isDeleted;
	}

	/**
	 * 返回 是否删除
	 * 
	 * @return
	 */
	public Short getIsDeleted() {
		return isDeleted;
	}

	public void setValidRule(String validRule) {
		this.validRule = validRule;
	}

	/**
	 * 返回 验证规则
	 * 
	 * @return
	 */
	public String getValidRule() {
		return validRule;
	}

	public void setOriginalName(String originalName) {
		this.originalName = originalName;
	}

	/**
	 * 返回 字段原名
	 * 
	 * @return
	 */
	public String getOriginalName() {
		return originalName;
	}

	public void setSn(Integer sn) {
		this.sn = sn;
	}

	/**
	 * 返回 排列顺序
	 * 
	 * @return
	 */
	public Integer getSn() {
		return sn;
	}

	public void setValueFrom(Short valueFrom) {
		this.valueFrom = valueFrom;
	}

	/**
	 * 返回 值来源 0-表单 1-脚本 3-流水号
	 * 
	 * @return
	 */
	public Short getValueFrom() {
		return valueFrom;
	}

	public void setScript(String script) {
		this.script = script;
	}

	/**
	 * 返回 脚本
	 * 
	 * @return
	 */
	public String getScript() {
		return script;
	}

	public void setControlType(Short controlType) {
		this.controlType = controlType;
	}

	/**
	 * 返回 控件类型
	 * 
	 * @return
	 */
	public Short getControlType() {
		return controlType;
	}

	public Short getIsHidden() {
		return isHidden;
	}

	public void setIsHidden(Short isHidden) {
		this.isHidden = isHidden;
	}

	public Short getIsFlowVar() {
		return isFlowVar;
	}

	public void setIsFlowVar(Short isFlowVar) {
		this.isFlowVar = isFlowVar;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String identity) {
		this.serialNumber = identity;
	}

	public String getOptions() {
		return this.options;
	}

	public void setOptions(String options) {
		this.options = options;
	}
	private String getDicJson(){
		IDictionaryService dicService = (IDictionaryService)AppUtil.getBean(IDictionaryService.class);
		JSONArray jarray = new JSONArray();
		try{
			List<?extends IDictionary> list = dicService.getByNodeKey(this.dictType);
			for(IDictionary dic : list){
				 JSONObject jObject = new JSONObject();
				 jObject.put("value", dic.getItemName());
				 jObject.put("key", dic.getItemValue());
				 jarray.add(jObject);
			}
		}catch(Exception e){
			return jarray.toString();
		}
		String reStr = jarray.toString();
		reStr = reStr.replaceAll("\"", "'");
		return  reStr;
	}
	/**
	 * 获取json对象。
	 * 
	 * @return
	 */
	public String getJsonOptions() {
		//如果是数据字典，需要将
		if(this.controlType==IFieldPool.DICTIONARY){
			return getDicJson();
		}		
		if (StringUtil.isEmpty(this.options))
			return "";
		String lanType = ContextUtil.getLocale().toString();
		JSONArray jarray = new JSONArray();
		if (StringUtil.isJson(this.options)) {
			JSONArray jArray = JSONArray.fromObject(this.options);
			for (Object obj : jArray) {
				JSONObject jObject = (JSONObject) obj;
				String key = jObject.getString("key");
				String jsonValue = jObject.getString("value");
				if (StringUtil.isJSONArray(jsonValue)) {
					String value = "";
					String defVal = "";
					JSONArray valAry = jObject.getJSONArray("value");
					for (Object valObj : valAry) {
						JSONObject valJObject = (JSONObject) valObj;
						String type = valJObject.getString("lantype");
						if (lanType.equals(type)) {
							value = valJObject.getString("lanres");
						}
					}
					// 未获取到对应语言版本的资源时 获取简体中文的资源
					if (StringUtil.isEmpty(value)) {
						value = defVal;
					}
					jarray.add(new JSONObject().accumulate("key", key)
							.accumulate("value", value));
				} else {// 兼容3.2
					jarray.add(new JSONObject().accumulate("key", key)
							.accumulate("value", jsonValue));
				}

			}
		} else {
			String[] ary = this.options.split("\n");
			for (String opt : ary) {
				jarray.add(new JSONObject().accumulate("key", opt).accumulate(
						"value", opt));
			}
		}
		String reStr = jarray.toString();
		reStr = reStr.replaceAll("\"", "'");
		return reStr;
	}

	/**
	 * 选项构建成hashmap对象。
	 * 
	 * @return
	 */
	public Map<String, Object> getAryOptions() {
		Map<String, Object> map = new LinkedHashMap<String, Object>();
		if (StringUtil.isEmpty(this.options))
			return map;
		String lanType = ContextUtil.getLocale().toString();
		if (StringUtil.isJson(this.options)) {
			JSONArray jArray = JSONArray.fromObject(this.options);
			for (Object obj : jArray) {
				JSONObject jObject = (JSONObject) obj;
				String key = jObject.getString("key");
				String jsonValue = jObject.getString("value");
				if (StringUtil.isJSONArray(jsonValue)) {
					String value = "";
					String defVal = "";
					JSONArray valAry = jObject.getJSONArray("value");
					for (Object valObj : valAry) {
						JSONObject valJObject = (JSONObject) valObj;
						String type = valJObject.getString("lantype");
						if (lanType.equals(type)) {
							value = valJObject.getString("lanres");
						}
					}
					// 未获取到对应语言版本的资源时 获取简体中文的资源
					if (StringUtil.isEmpty(value)) {
						value = defVal;
					}
					map.put(key, value);
				} else {// 兼容3.2
					map.put(key, jsonValue);
				}
			}
		} else {
			String[] ary = this.options.split("\n");
			for (String opt : ary) {
				map.put(opt, opt);
			}
		}
		return map;
	}

	public boolean isAdded() {
		return isAdded;
	}

	public void setAdded(boolean isAdded) {
		this.isAdded = isAdded;
	}

	public Short getIsDateString() {
		return isDateString;
	}

	public void setIsDateString(Short isDateString) {
		this.isDateString = isDateString;
	}

	public Short getIsCurrentDateStr() {
		return isCurrentDateStr;
	}

	public void setIsCurrentDateStr(Short isCurrentDateStr) {
		this.isCurrentDateStr = isCurrentDateStr;
	}

	public String getStyle() {
		return style;
	}

	public void setStyle(String style) {
		this.style = style;
	}

	/**
	 * 获取控件属性
	 * 
	 * @return
	 */
	public String getCtlProperty() {
		return ctlProperty;
	}

	public void setCtlProperty(String ctlProperty) {
		this.ctlProperty = ctlProperty;
	}

	/**
	 * 将控件属性转换成map输出。
	 * 
	 * @return
	 */
	public Map<String, String> getPropertyMap() {
		Map<String, String> map = new HashMap<String, String>();
		if (StringUtil.isEmpty(this.ctlProperty)) {
			return map;
		}
		try {
			JSONObject jsonObject = JSONObject.fromObject(this.ctlProperty);
			Iterator it = jsonObject.keys();
			while (it.hasNext()) {
				String key = it.next().toString();
				String value = jsonObject.getString(key);
				map.put(key, value);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return map;
	}

	public String getFieldTypeDisplay() {
		if ("varchar".equals(this.fieldType)) {
			return "字符串,varchar(" + this.charLen + ")";
		} else if ("number".equals(this.fieldType)) {
			if (BeanUtils.isEmpty(this.decimalLen)) {
				this.decimalLen = 0;
			}
			if (this.decimalLen == 0) {
				return "数字,number(" + this.intLen + ")";
			}
			return "数字,number(" + this.intLen + "," + this.decimalLen + ")";
		} else if ("date".equals(this.fieldType)) {
			return "日期,date";
		} else if ("clob".equals(this.fieldType)) {
			return "大文本";
		}
		return "字符串";
	}

	public Short getIsReference() {
		return isReference;
	}

	public void setIsReference(Short isReference) {
		this.isReference = isReference;
	}

	public String getDatefmt() {
		datefmt = StringPool.DATE_FORMAT_DATE;
		if (StringUtils.isNotEmpty(ctlProperty)) {
			try {
				JSONObject json = JSONObject.fromObject(ctlProperty);
				Object format = json.get("format");
				if (BeanUtils.isNotEmpty(format))
					datefmt = (String) format;
			} catch (Exception e) {
			}
		}
		return datefmt;
	}

	public void setDatefmt(String datefmt) {
		this.datefmt = datefmt;
	}

	public short getType() {
		return type;
	}

	public void setType(short type) {
		this.type = type;
	}

	public String getScriptID() {
		return scriptID;
	}

	public void setScriptID(String scriptID) {
		this.scriptID = scriptID;
	}

	public Short getIsShowComdify() {
		return isShowComdify;
	}

	public void setIsShowComdify(Short isShowComdify) {
		this.isShowComdify = isShowComdify;
	}

	public String getCoinValue() {
		return coinValue;
	}

	public void setCoinValue(String coinValue) {
		this.coinValue = coinValue;
	}

	public Long getRelTableId() {
		return relTableId;
	}

	public void setRelTableId(Long relTableId) {
		this.relTableId = relTableId;
	}
	
	public String getRelTableName()
    {
        return relTableName;
    }

    public void setRelTableName(String relTableName)
    {
        this.relTableName = relTableName;
    }

    public Short getRelTableType() {
		return relTableType;
	}

	public void setRelTableType(Short relTableType) {
		this.relTableType = relTableType;
	}

	public Short getRelDelType() {
		return relDelType;
	}

	public void setRelDelType(Short relDelType) {
		this.relDelType = relDelType;
	}

	public Short getRelDelLMType() {
		return relDelLMType;
	}

	public void setRelDelLMType(Short relDelLMType) {
		this.relDelLMType = relDelLMType;
	}

	public Short getIsPkShow() {
		return isPkShow;
	}

	public void setIsPkShow(Short isPkShow) {
		this.isPkShow = isPkShow;
	}

	/**
	 * @see java.lang.Object#equals(Object) 
	 */
	public boolean equals(Object object) {
		if (!(object instanceof FormField)) {
			return false;
		}
		FormField rhs = (FormField) object;
		return this.fieldName.equals(rhs.getFieldName());

	}
	
	public Object clone() {
		FormField obj = null;
		try {
			obj = (FormField) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}
		return obj;
	}

	/**
	 * @see java.lang.Object#hashCode()
	 */
	public int hashCode() {
		return new HashCodeBuilder(-82280557, -700257973)
				.append(this.fieldName).toHashCode();
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return new ToStringBuilder(this).append("fieldName", this.fieldName)
				.toString();
	}

	public String getEncrypt()
    {
        return encrypt;
    }

    public void setEncrypt(String encrypt)
    {
        this.encrypt = encrypt;
    }

    public String getRelFormDialog() {
		return relFormDialog;
	}

	public void setRelFormDialog(String relFormDialog) {
		this.relFormDialog = relFormDialog;
	}
	
	public FormTable getFormTable()
    {
        return formTable;
    }

    public void setFormTable(FormTable formTable)
    {
        this.formTable = formTable;
    }

    // 解析relFormDialog，构造为"自定义对话框"信息的Map
	// dialog="{name:'ctcsbdhk',fields:[{src:'F_MC',target:'kf_test_idXXXXXFKColumnShow,xxxx'},{src:'ID',target:'kf_test_id'}],query:[],rpcrefname='interfacesImplConsumerCommonService'}"
	public Map<String, Object> getFKColumnShowMapByRelFormDialog(
			String relFormDialog) {
		Map<String,Object> returnMap = new HashMap<String,Object>();
		Object formDialogAlias = "";// 自定义对话框别名alias
		Map<String,String> targetSrcFieldMap = new HashMap<String,String>();// 循环json中的fields，将src，target作为键值对放入map中。
		Object queryStr = "";// 获取查询条件
		String fkColumnShow = "";// 外键显示列。如F_MC,F_MT,可以是多个，用“，”分开
		String fkColumn = "";// 外键显示列。如kf_test_id
		String fkIdName = "";// 外键对应表的主键名称,如ID
		Object rpcrefname = "";// rpc远程接口

		if (StringUtils.isNotEmpty(relFormDialog)) {
			String relFormDialogJsonObj = CommonTools.stripCData(relFormDialog);
			JSONObject relFormDialogJson = JSONObject
					.fromObject(relFormDialogJsonObj);
			formDialogAlias = relFormDialogJson.get("name");
			String fieldsJson = relFormDialogJson.getString("fields");
			queryStr = relFormDialogJson.get("query");
			rpcrefname = relFormDialogJson.get(IFieldPool.rpcrefname);
			if (StringUtils.isNotEmpty(fieldsJson)) {
				JSONArray fieldJsonArray = JSONArray.fromObject(fieldsJson);
				if (fieldJsonArray != null) {
					// 循环json中的fields，将src，target作为键值对放入map中。
					for (int i = 0; i < fieldJsonArray.size(); i++) {
						Object jsonObj = fieldJsonArray.get(i);
						if (jsonObj != null) {
							JSONObject fieldJson = JSONObject
									.fromObject(jsonObj);
	                        String target = fieldJson.getString(IFieldPool.FK_TABLEFIELD);
                            boolean listShow = Boolean.valueOf(fieldJson.getString(IFieldPool.FK_LISTSHOW));
							String src = fieldJson.getString(IFieldPool.FK_DIALOGFIELD);
							targetSrcFieldMap.put(src, target);

							if (listShow) {
							    //-----------需要判断字段是否为 物理表字段，如果是不需要关联查询，直接显示字段值就可以了。
								// 获取外键显示列。
								fkColumnShow += "," + src;
								// 查找外键对应表的主键名称
								String fkName = target.replace(
										IFieldPool.FK_SHOWAppCode, "");
								fkColumn = fkName;

								for (int j = 0; j < fieldJsonArray.size(); j++) {
									Object findFKObj = fieldJsonArray.get(j);
									if (findFKObj != null) {
										if (("," + target + ",")
												.indexOf("," + fkName + ",") > -1) {
											fkIdName = src;
											break;
										}
									}
								}
							}
						}
					}
				}
			}
		}

		returnMap.put("name", formDialogAlias);
		returnMap.put("fields", targetSrcFieldMap);
		returnMap.put("query", queryStr);
		returnMap.put(IFieldPool.fkColumnShow,
				fkColumnShow.indexOf(",") == 0 ? fkColumnShow.substring(1)
						: fkColumnShow);
		returnMap.put(IFieldPool.fkColumn, fkColumn);
		returnMap.put(IFieldPool.fkIdName, fkIdName);
		returnMap.put(IFieldPool.rpcrefname, rpcrefname == null ? ""
				: rpcrefname.toString());
		return returnMap;
	}

	/**
     * 是否选择器的隐藏字段
     * 
     * @return
     */
    public boolean isExecutorSelector() {
        if (BeanUtils.isEmpty(controlType)){
            return false;
        }
        if (controlType.shortValue() == IFieldPool.SELECTOR_USER_SINGLE
                || controlType.shortValue() == IFieldPool.SELECTOR_USER_MULTI
                || controlType.shortValue() == IFieldPool.SELECTOR_ORG_SINGLE
                || controlType.shortValue() == IFieldPool.SELECTOR_ORG_MULTI
                || controlType.shortValue() == IFieldPool.SELECTOR_ROLE_SINGLE
                || controlType.shortValue() == IFieldPool.SELECTOR_ROLE_MULTI
                || controlType.shortValue() == IFieldPool.SELECTOR_POSITION_SINGLE
                || controlType.shortValue() == IFieldPool.SELECTOR_POSITION_MULTI) {
            return true;
        }
        return false;
    }
    
	/**
	 * 是否选择器的隐藏字段
	 * 
	 * @return
	 */
	public boolean isExecutorSelectorHidden() {
	    boolean result=this.isExecutorSelector();
		if (result&&isHidden.shortValue() == FormField.HIDDEN) {
		    return true;
		}
		return false;
	}
	
    /**
     * 数据库真正的字段名
     * 
     * @return
     */
    public String getFactFiledName() {
    	if(ITableModel.PK_COLUMN_NAME.equals(fieldName)||ITableModel.PK_COLUMN_NAME.toLowerCase().equals(fieldName)){
    		return fieldName;
    	}else{
            return ITableModel.CUSTOMER_COLUMN_PREFIX+this.fieldName;

    	}
    }

	public String getRelFormDialogStripCData() {
		relFormDialogStripCData = CommonTools.stripCData(this
				.getRelFormDialog());
		return relFormDialogStripCData;
	}

	public void setRelFormDialogStripCData(String relFormDialogStripCData) {
		if (StringUtil.isNotEmpty(relFormDialogStripCData)) {
			relFormDialogStripCData = "<![CDATA[" + relFormDialogStripCData
					+ "]]>";
		}
		this.relFormDialogStripCData = relFormDialogStripCData;
	}
	
	
    public Object[] getRelField()
    {
        String relation = this.getRelFormDialogStripCData();
        if (relation != null)
        {
            JSONObject object = JSONObject.fromObject(relation);
            JSONArray fields = object.getJSONArray("fields");
            JSONArray result = new JSONArray();
            for (Object f : fields)
            {
                JSONObject _f = (JSONObject)f;
                if (_f.getString("target").indexOf(IFieldPool.FK_SHOWAppCode) > -1)
                {
                    _f.put("isTableField", false);
                }
                else
                {
                    _f.put("isTableField", true);
                    if(formTable==null){
                        return null;
                    }
                    String extStr = (formTable.isMain.shortValue() == FormTable.IS_MAIN
                        .shortValue() )? "m:" : "s:";
                    _f.put("fieldName", extStr+formTable.getTableName()+":"+_f.getString("target"));

                }
                try{
                	 boolean isShow = _f.getBoolean(IFieldPool.FK_LISTSHOW);
                     if (isShow)
                     {
                         result.add(_f);
                         break;
                     }
                }catch(Exception e){
                	
                }
               
            }
            if (result != null)
            {
                return result.toArray();
            }
        }
        return null;
    }
    public static FormField transField(IOutField outfield){
    	FormField field = new FormField();  
    	field.setCharLen(outfield.getCharLen());
    	field.setControlType(outfield.getControlType());
    	field.setCtlProperty(outfield.getCtlProperty());
    	field.setDecimalLen(outfield.getDecimalLen());
    	field.setDictType(outfield.getDictType());
    	field.setEncrypt(outfield.getEncrypt());
    	field.setFieldDesc(outfield.getFieldDesc());
    	field.setFieldId(outfield.getFieldId());
    	field.setFieldName(outfield.getFieldName());
    	field.setFieldType(outfield.getFieldType());
    	field.setIntLen(outfield.getIntLen());
    	field.setIsDeleted(outfield.getIsDeleted());
    	field.setIsFlowVar(outfield.getIsFlowVar());
    	field.setIsHidden(outfield.getIsHidden());
    	field.setIsMainData(outfield.getIsMainData());
    	field.setIsPkShow(outfield.getIsPkShow());
    	field.setIsReference(outfield.getIsReference());
    	field.setIsRequired(outfield.getIsRequired());
    	field.setIsShowComdify(outfield.getIsShowComdify());
    	field.setIsUnique(outfield.getIsUnique());
    	field.setIsWebSign(outfield.getIsWebSign());
    	field.setOptions(outfield.getOptions());
    	field.setOriginalName(outfield.getOriginalName());
    	field.setRelDelLMType(outfield.getRelDelLMType());
    	field.setRelDelType(outfield.getRelDelType());
    	field.setRelFormDialog(outfield.getRelFormDialog());
    	field.setRelFormDialogStripCData(outfield.getRelFormDialogStripCData());
    	field.setRelTableId(outfield.getRelTableId());
    	field.setRelTableName(outfield.getRelTableName());
    	field.setRelTableType(outfield.getRelTableType());
    	field.setScript(outfield.getScript());
    	field.setScriptID(outfield.getScriptID());
    	field.setSerialNumber(outfield.getSerialNumber());
    	field.setSn(outfield.getSn());
    	field.setStyle(outfield.getStyle());
    	field.setTableId(outfield.getTableId());
    	field.setValidRule(outfield.getValidRule());
    	field.setValueFrom(outfield.getValueFrom());
    	return field;
    }
}