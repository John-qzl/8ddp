package com.cssrc.ibms.system.service;

import java.util.List;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.cssrc.ibms.api.activity.intf.IAgentSettingService;
import com.cssrc.ibms.api.activity.intf.IDefinitionService;
import com.cssrc.ibms.api.activity.intf.INodeSetService;
import com.cssrc.ibms.api.activity.intf.IProcessRunService;
import com.cssrc.ibms.api.activity.model.IAgentSetting;
import com.cssrc.ibms.api.activity.model.IDefinition;
import com.cssrc.ibms.api.activity.model.IFlowNode;
import com.cssrc.ibms.api.activity.model.IProcessRun;
import com.cssrc.ibms.api.form.intf.IDataTemplateService;
import com.cssrc.ibms.api.form.intf.IFormDefService;
import com.cssrc.ibms.api.form.intf.IFormDialogService;
import com.cssrc.ibms.api.form.intf.IFormQueryService;
import com.cssrc.ibms.api.form.intf.IFormRuleService;
import com.cssrc.ibms.api.form.intf.IFormTableService;
import com.cssrc.ibms.api.form.intf.IFormTemplateService;
import com.cssrc.ibms.api.form.model.IDataTemplate;
import com.cssrc.ibms.api.form.model.IFormDef;
import com.cssrc.ibms.api.form.model.IFormDialog;
import com.cssrc.ibms.api.form.model.IFormQuery;
import com.cssrc.ibms.api.form.model.IFormRule;
import com.cssrc.ibms.api.form.model.IFormTable;
import com.cssrc.ibms.api.form.model.IFormTemplate;
import com.cssrc.ibms.api.jms.intf.IMessageReceiverService;
import com.cssrc.ibms.api.jms.intf.IOutMailUserSetingService;
import com.cssrc.ibms.api.jms.model.IMessageReceiver;
import com.cssrc.ibms.api.jms.model.IOutMailUserSeting;
import com.cssrc.ibms.api.job.model.IJob;
import com.cssrc.ibms.api.log.intf.ISysAuditLinkService;
import com.cssrc.ibms.api.report.inf.IOfficeTemplateService;
import com.cssrc.ibms.api.report.inf.IReportParamService;
import com.cssrc.ibms.api.report.inf.IReportTemplateService;
import com.cssrc.ibms.api.report.model.IReportParam;
import com.cssrc.ibms.api.report.model.IReportTemplate;
import com.cssrc.ibms.api.system.intf.IMessageSendService;
import com.cssrc.ibms.api.system.intf.ISysLogSwitchService;
import com.cssrc.ibms.api.system.intf.ISysParameterService;
import com.cssrc.ibms.api.system.model.IDemension;
import com.cssrc.ibms.api.system.model.IDictionary;
import com.cssrc.ibms.api.system.model.IGlobalType;
import com.cssrc.ibms.api.system.model.IMessageSend;
import com.cssrc.ibms.api.system.model.ISerialNumber;
import com.cssrc.ibms.api.system.model.ISysFile;
import com.cssrc.ibms.api.system.model.ISysLogSwitch;
import com.cssrc.ibms.api.system.model.ISysParam;
import com.cssrc.ibms.api.system.model.ISysParameter;
import com.cssrc.ibms.api.system.model.ISysScript;
import com.cssrc.ibms.api.sysuser.intf.IPositionService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgRoleManageService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgService;
import com.cssrc.ibms.api.sysuser.intf.ISysOrgTypeService;
import com.cssrc.ibms.api.sysuser.intf.ISysRoleService;
import com.cssrc.ibms.api.sysuser.intf.ISysUserService;
import com.cssrc.ibms.api.sysuser.intf.IUserPositionService;
import com.cssrc.ibms.api.sysuser.model.IPosition;
import com.cssrc.ibms.api.sysuser.model.IResources;
import com.cssrc.ibms.api.sysuser.model.ISysOrg;
import com.cssrc.ibms.api.sysuser.model.ISysOrgRole;
import com.cssrc.ibms.api.sysuser.model.ISysOrgRoleManage;
import com.cssrc.ibms.api.sysuser.model.ISysOrgType;
import com.cssrc.ibms.api.sysuser.model.ISysRole;
import com.cssrc.ibms.api.sysuser.model.ISysUser;
import com.cssrc.ibms.api.sysuser.model.IUserPosition;
import com.cssrc.ibms.core.util.appconf.AppUtil;
import com.cssrc.ibms.core.util.bean.BeanUtils;
import com.cssrc.ibms.core.util.string.StringUtil;
import com.cssrc.ibms.index.model.InsNews;
import com.cssrc.ibms.index.model.InsNewsCm;
import com.cssrc.ibms.index.service.InsNewsCmService;
import com.cssrc.ibms.index.service.InsNewsService;
import com.cssrc.ibms.record.model.RecFun;
import com.cssrc.ibms.record.model.RecRole;
import com.cssrc.ibms.record.model.RecType;
import com.cssrc.ibms.record.service.RecFunService;
import com.cssrc.ibms.record.service.RecRoleService;
import com.cssrc.ibms.record.service.RecTypeService;
import com.cssrc.ibms.system.model.ConditionScript;
import com.cssrc.ibms.system.model.Seal;
import com.cssrc.ibms.system.model.SysCodeTemplate;
import com.cssrc.ibms.system.model.SysPaur;
import com.cssrc.ibms.system.model.SysScript;
import com.cssrc.ibms.system.model.SysTemplate;
import com.cssrc.ibms.system.model.SysTypeKey;

@Service("sysAuditLinkService")
public class SysAuditLinkService implements  ISysAuditLinkService{
	private Log logger = LogFactory.getLog(SysAuditLinkService.class);
	@Resource
	SysFileService sysFileService;
	
	@Resource
	SerialNumberService serialNumberService;
	
	@Resource
	ResourcesService resourcesService;
	
	@Resource
    ISysUserService sysUserService;
	
	@Resource
	SysPaurService sysPaurService;
	
	@Resource
	JobService jobService;
	@Resource
	IUserPositionService userPositionService;
	@Resource
	IMessageSendService messageSendService;
	@Resource
	ISysOrgService sysOrgService;
	@Resource
	IReportTemplateService reportTemplateService;
	@Resource
	IReportParamService reportParamService;
	@Resource
	IProcessRunService processRunService;
	@Resource
	IDefinitionService definitionService;
	@Resource
	IFormDefService formDefService;
	@Resource
	GlobalTypeService globalTypeService;
	@Resource
	IFormDialogService formDialogService;
	@Resource
	IDataTemplateService dataTemplateService;
	@Resource
	IFormQueryService formQueryService;
	@Resource
	IFormTableService formTableService;
	@Resource
	IFormRuleService formRuleService;
	@Resource
	IFormTemplateService formTemplateService;
	@Resource
	IAgentSettingService agentSettingService;
	@Resource
	SysScriptService sysScriptService;
	@Resource
	SysTemplateService sysTemplateService;
	@Resource
	ConditionScriptService conditionScriptService;
	@Resource
	SysCodeTemplateService sysCodeTemplateService;
	@Resource
	SysTypeKeyService sysTypeKeyService;
	@Resource
	DictionaryService dictionaryService;
	@Resource
	SealService sealService;
	@Resource
	SysParamService sysParamService;
	@Resource
	IPositionService positionService;
	@Resource
	DemensionService demensionService;
	@Resource
	IMessageReceiverService messageReceiverService;
	@Resource
	IOutMailUserSetingService outMailUserSetingService;
	@Resource
	IOfficeTemplateService officeTemplateService;
	@Resource
	INodeSetService nodeSetService;
	@Resource
	ISysParameterService sysParameterService;
	@Resource
	ISysLogSwitchService sysLogSwitchService;
	@Resource
	RecTypeService recTypeService;
	@Resource
	RecFunService recFunService;
	@Resource
	RecRoleService recRoleService;
	@Resource
	ISysOrgTypeService sysOrgTypeService;
	@Resource
	InsNewsCmService insNewsCmService;
	@Resource
	InsNewsService insNewsService;
	@Resource
	ISysOrgRoleService sysOrgRoleService;
	@Resource
	ISysRoleService sysRoleService;
	@Resource
	ISysOrgRoleManageService sysOrgRoleManageService;
	//流程实例
	public String getProcessRunLink(Long runId, String actInstName) { 
		return "<a href='####'  hrefLink='../../flow/processRun/processImage.do?runId=" + runId + "' class='ProcessRunLink' ProcessRunId='" + runId + "'>" + actInstName + "</a>";
	 }
	public String getProcessRunLink(Long runId)
	{
		IProcessRun processRun = (IProcessRun)this.processRunService.getById(runId);
		return getProcessRunLink(processRun);
	}
	
	public String getProcessRunLink(IProcessRun processRun)
	{
		return getProcessRunLink(processRun.getRunId(), processRun.getSubject());
	}
	
	public String getProcessRunLink(String taskId)
	{
		IProcessRun processRun = this.processRunService.getByTaskId(taskId);
		return getProcessRunLink(processRun);
	}
	
	public String getProcessRunDetailLink(String taskId) {
		IProcessRun processRun = this.processRunService.getByTaskId(taskId);
		return "<a href='####'  hrefLink='../../flow/processRun/get.do?runId=" + processRun.getRunId() + "' class='ProcessRunLink' ProcessRunId='" + processRun.getRunId() + "'>" + processRun.getSubject() + "</a>";
	}
	
	//流程定义
	public String getDefinitionLink(Long defId)
	{
		IDefinition def = (IDefinition)this.definitionService.getById(defId);
		return def.getSubject();
	}
	
	public String getDefinitionLink(String actDefId)
	{
		IDefinition def = this.definitionService.getByActDefId(actDefId);
		return def.getSubject();
	}

	public String getDefinitionLinkByKey(String actDefKey)
	{
		IDefinition def = this.definitionService.getMainByDefKey(actDefKey);
		return def.getSubject();
	}

	public String getDefinitionLink(IDefinition definition)
	{
		return getDefinitionLink(definition.getDefId(), definition.getSubject());
	}
	
	public String getDefinitionLink(Long defId, String actDefName)
	{
		return "<a href='####'  hrefLink='../../flow/definition/design.do?defId=" + defId + "' class='DefinitionLink' DefinitionDefId='" + defId + "'>" + actDefName + "</a>";
	}
	
	
	//组织角色 
	public String getSysOrgLink(Long orgId)
	{
		ISysOrg sysOrg = (ISysOrg)this.sysOrgService.getById(orgId);
		return sysOrg.getOrgName()+"【"+sysOrg.getCode()+"】";
	}

	public String getSysRoleLink(ISysRole sysRole)
	{
		return sysRole.getRoleName()+"【"+sysRole.getAlias()+"】";
	}

	//自定义表单
	public String getFormDefLink(Long formDefId, String formDefName)
	{
		return "<a href='####'  hrefLink='../../form/formDef/get.do?formDefId=" + formDefId + "' class='FormDefLink' FormDefId='" + formDefId + "'>" + formDefName + "</a>";
	}
	
	public String getFormDefLink(IFormDef def)
	{
		return getFormDefLink(def.getFormDefId(), def.getSubject());
	}

	public String getFormDefLink(Long formDefId)
	{
		IFormDef def = (IFormDef)this.formDefService.getById(formDefId);
		return getFormDefLink(def);
	}
	public String getFormDefLink(String formKey) {
		IFormDef def = this.formDefService.getDefaultVersionByFormKey(Long.valueOf(formKey));
		return getFormDefLink(def);
	}
	
	//分类管理
	public String getGlobalTypeLink(Long typeId)
	{
		IGlobalType type = (IGlobalType)this.globalTypeService.getById(typeId);
		return getGlobalTypeLink(type);
	}

	public String getGlobalTypeLink(IGlobalType type) {
		return getGlobalTypeLink(type.getTypeId(), type.getTypeName());
	}

	public String getGlobalTypeLink(Long typeId, String typeName)
	{
		return "<a href='####'  hrefLink='../../system/globalType/get.do?typeId=" + typeId + "' class='GlobalTypeLink' GlobalTypeId='" + typeId + "'>" + typeName + "</a>";
	}
	
	//表单对话框
	public String getFormDialogLink(Long id, String name) {
		return "<a href='####'  hrefLink='../../form/formDialog/get.do?id=" + id + "' class='FormDialogLink' FormDialogId='" + id + "'>" + name + "</a>";
	}
	public String getFormDialogLink(Long dialogId) {
		IFormDialog dialog = (IFormDialog)this.formDialogService.getById(dialogId);
		return getFormDialogLink(dialog);
	}
	public String getFormDialogLink(IFormDialog dialog) {
		return getFormDialogLink(dialog.getId(), dialog.getName());
	}
	
	//业务数据模板
	public String getDataTemplateLink(Long tempId)
	{
		IDataTemplate dataTemplate = (IDataTemplate)this.dataTemplateService.getById(tempId);
		return "<a href='####' hrefLink='../../form/dataTemplate/detailData.do?__displayId__=" + tempId + "&__pk__=" + dataTemplate.getFormKey() + "' class='DataTemplateLink' DataTemplateId='" + tempId + 
				"'>【" + dataTemplate.getName() + "】" + "</a>";
	}
	
	//表单查询
	public String getFormQueryLink(Long queryId)
	{
		IFormQuery formQuery = (IFormQuery)this.formQueryService.getById(queryId);
		return "<a href='####' hrefLink='../../form/formQuery/edit.do?id=" + queryId + "' class='FormTableLink' FormTableId='" + queryId + 
				"'>" + formQuery.getName() + "</a>";
	}
	
	//自定义业务表 
	public String getFormTableLink(Long tableId)
	{
		IFormTable formTable = this.formTableService.getTableById(tableId);

		return "<a href='####' hrefLink='../../form/formTable/getOfLink.do?tableId=" + tableId + "' class='FormTableLink' FormTableId='" + tableId + 
				"'>" + formTable.getTableDesc() + "【" + formTable.getTableName() + "】" + "</a>";
	}
	//自定义业务表描述信息  scc
	public String getFormTableDesc(Long tableId,String id){
		IFormTable formTable = this.formTableService.getTableById(tableId);
		if(StringUtil.isNotEmpty(id)){
			return "【"+formTable.getTableDesc()+"-pk:"+id+"】 ";
		}
		return "【"+formTable.getTableDesc()+"】 ";
	}
	/**
	 * 自定义业务表-导出
	 * @param isFR 是否导出报表
	 * @param id 数据模板ID
	 * @param exportPK 导出的数据PK值
	 * @param exportType 1 选中   0全部  2当前页  3列表关联附件
	 * @return  scc
	 */
	public String getExportDesc(String isFR,Long id,String exportPK,int exportType){
		IDataTemplate dataTemplate = dataTemplateService.getById(id);
		IFormTable formTable = this.formTableService.getTableById(dataTemplate.getTableId());
		if (isFR != null && "1".equals(isFR)){
			return "导出报表【"+formTable.getTableDesc()+"】";
        }else{
        	if(exportType==0){
        		return "导出全部业务数据【"+formTable.getTableDesc()+"】";
        	}else if(exportType==1){
        		if(StringUtil.isNotEmpty(exportPK)){
        			return "导出选中的业务数据【"+formTable.getTableDesc()+"-pk:"+exportPK+"】";
        		}
        		return "导出选中的业务数据【"+formTable.getTableDesc()+"】";
        	}else if(exportType==2){
        		if(StringUtil.isNotEmpty(exportPK)){
        			return "导出当前页的业务数据【"+formTable.getTableDesc()+"-pk:"+exportPK+"】";
        		}
        		return "导出当前页的业务数据【"+formTable.getTableDesc()+"】";
        	}else if(exportType==3){
        		return "导出列表关联附件，业务数据【"+formTable.getTableDesc()+"】";
        	}
        }
		return "导出业务数据【"+formTable.getTableDesc()+"】";
	}
	//表单验证规则
	public String getFormRuleLink(long ruleId)
	{
		IFormRule formRule = (IFormRule)this.formRuleService.getById(Long.valueOf(ruleId));
		return "<a href='####' hrefLink='../../form/formRule/get.do?id=" + ruleId + "' class='FormRuleLink' FormRuleId='" + ruleId + 
				"'>" + formRule.getName() + "</a>";
	}

	//表单模板
	public String getFormTemplateLink(long tempId)
	{
		IFormTemplate formTemplate = (IFormTemplate)this.formTemplateService.getById(Long.valueOf(tempId));
		return "<a href='####' hrefLink='../../form/formTemplate/get.do?templateId=" + tempId + "' class='FormTemplateLink' FormTemplateId='" + tempId + 
				"'>" + formTemplate.getTemplateName() + "</a>";
	}
	
	
	
	
	/**
	 * 获取用户组织岗位详细列表
	 * @param userPosId
	 * @return
	 */
	public IUserPosition getByUserPosId(long userPosId)
	{
		IUserPosition userPosition = (IUserPosition)this.userPositionService.getById(Long.valueOf(userPosId));
		ISysOrg sysOrg = (ISysOrg)this.sysOrgService.getById(userPosition.getOrgId());
		ISysUser sysUser = (ISysUser)this.sysUserService.getById(userPosition.getUserId());
		userPosition.setOrgName(sysOrg.getOrgName());
		userPosition.setFullname(sysUser.getFullname());
		return userPosition;
	}
	
	/**
	 * 获取附件的超链接
	 * @param fileId
	 * @return
	 */
	public String getSysFileLink(String fileIds){
		String context = AppUtil.getContextPath();
		List<ISysFile> list = (List)sysFileService.getFileByIds(StringUtil.addSingleQuotes(fileIds));
		String html = "";
		for(ISysFile file : list){
			String single = "";
			single ="<a  href='"+context+"/oa/system/sysFile/download.do?fileId="+file.getFileId()+"' class='SysFileLink' SysFileId='"+file.getFileId()+"' >";
			single += file.getFilename()+"."+file.getExt();
			single += "</a>";
			single += "&nbsp&nbsp";
			html +=single;
		}
		return html;
	}
	public String getSysFileNameArr(String fileIds){
		List<ISysFile> list = (List)sysFileService.getFileByIds(StringUtil.addSingleQuotes(fileIds));
		String html = "";
		for(ISysFile file : list){
			String single = "";
			single += file.getFilename()+"."+file.getExt();
			single +="、";
			html +=single;
		}
		if(html.endsWith("、")){
			html = html.substring(0,html.length()-1);
		}
		return html;
	}
	/**
	 * 用户模块
	 *@author YangBo
	 *@param id
	 *@return
	 */
	public String getSysUserLink(long id){
		ISysUser sysUser = sysUserService.getById(id);
		return sysUser.getFullname()+"【"+sysUser.getUsername()+"】";
	}
	
	public String getSysUserLink(String account){
		ISysUser sysUser = sysUserService.getByUsername(account);
		if(sysUser==null)
			return "【"+account+"】";
		else
			return sysUser.getFullname()+"【"+sysUser.getUsername()+"】";
	}
	
	public String getSysUserLink(ISysUser sysUser){
		return  getSysUserLink(sysUser.getUserId(),sysUser.getFullname());
	}
	
	public String getSysUserLink(long id,String name){
		return "<a href='####' hrefLink='../../system/sysUser/getByUserId.do?userId="+ id +"' class='SysUserLink' SysUserId='"+id+"'>"+name+"</a>";
	}
	
	/**
	 * 流程代理设定
	 *@author YangBo 
	 *@param id
	 *@return
	 */
	public String getAgentSettingLink(Long id)
	{
		IAgentSetting agentSetting = (IAgentSetting)this.agentSettingService.getById(id);
		return getAgentSettingLink(agentSetting);
	}
	public String getAgentSettingLink(IAgentSetting agentSetting) {
		String agentType = "";
		if (IAgentSetting.AUTHTYPE_GENERAL == agentSetting.getAuthtype().intValue())
			agentType = "全权代理";
		else if (IAgentSetting.AUTHTYPE_PARTIAL == agentSetting.getAuthtype().intValue())
			agentType = "部分代理";
		else if (IAgentSetting.AUTHTYPE_CONDITION == agentSetting.getAuthtype().intValue()) {
			agentType = "条件代理";
		}
		String link = "【授权人：" + 
				getSysUserLink(agentSetting.getAuthid().longValue(), agentSetting.getAuthname()) + 
				" ; 代理类型：" + agentType + 
				" ; 代理人：" + getSysUserLink(agentSetting.getAgentid().longValue(), agentSetting.getAgent()) + 
				"】";
		return link;
	}
	
	//流程节点名
	public String getNodeName(String actDefId, String nodeId)
	{
		IFlowNode flowNode = nodeSetService.getNodeByActNodeIdFCache(actDefId, nodeId);
		return flowNode.getNodeName();
	}

	public String getNodeName(Long defId, String nodeId) {
		IDefinition definition = (IDefinition)this.definitionService.getById(defId);
		return getNodeName(definition.getActDefId(), nodeId);
	}
	
	
	//自定义脚本
	public String getScriptLink(long scrId)
	{
		ISysScript sysScript = (SysScript)this.sysScriptService.getById(Long.valueOf(scrId));
		return "<a href='####' hrefLink='../../system/sysScript/get.do?id=" + sysScript.getId() + "' class='ScriptLink' ScriptId='" + sysScript.getId() + "' >" + sysScript.getName() + "</a>";
	}
	
	public String getSysTemplateLink(long temId)
	{
		SysTemplate sysTemplate = (SysTemplate)this.sysTemplateService.getById(Long.valueOf(temId));
		return "<a href='####' hrefLink='../../system/sysTemplate/get.do?id=" + sysTemplate.getId() + "' class='SysTemplateLink' SysTemplateId='" + sysTemplate.getId() + "' >" + sysTemplate.getName() + "</a>";
	}
	//条件脚本
	public String getConditionScriptLink(long conId)
	{
		ConditionScript conditionScript = (ConditionScript)this.conditionScriptService.getById(Long.valueOf(conId));
		return "<a href='####' hrefLink='../../system/conditionScript/edit.do?id=" + conditionScript.getId() + "' class='ConditionScriptLink' ConditionScriptId='" + conditionScript.getId() + "' >" + conditionScript.getMethodName() + "</a>";
	}
	//自定义脚本模板
	public String getSysCodeTemplateLink(long codetemId)
	{
		SysCodeTemplate sysCodeTemplate = (SysCodeTemplate)this.sysCodeTemplateService.getById(Long.valueOf(codetemId));
		return "<a href='####' hrefLink='../../system/sysCodeTemplate/get.do?id=" + sysCodeTemplate.getId() + "' class='SysCodeTemplateLink' SysCodeTemplateId='" + sysCodeTemplate.getId() + "' >" + sysCodeTemplate.getTemplateName() + "</a>";
	}
	//分类类别
	public String getSysTypeKeyLink(long typeKeyId)
	{
		SysTypeKey sysTypeKey = (SysTypeKey)this.sysTypeKeyService.getById(Long.valueOf(typeKeyId));
		return "<a href='####' hrefLink='../../system/sysTypeKey/get.do?typeKeyId=" + typeKeyId + "' class='SysTypeKeyLink' SysTypeKeyId='" + typeKeyId + "' >" + sysTypeKey.getTypeName() + "</a>";
	}
	//数据字典
	public String getDictionaryLink(long dictId)
	{
		IDictionary dictionary = (IDictionary)this.dictionaryService.getById(Long.valueOf(dictId));
		return "<a href='####' hrefLink='../../system/dictionary/edit.do?dicId=" + dictId + "' class='DictionaryLink' DictionaryId='" + dictId + "' >" + dictionary.getItemName() + "</a>";
	}
	
	//印章
	public String getSealLink(long sealId)
	{
		Seal seal = (Seal)this.sealService.getById(Long.valueOf(sealId));
		return "<a href='####' hrefLink='../../system/seal/get.do?sealId=" + sealId + "' class='SealLink' SealId='" + sealId + "' >" + seal.getSealName() + "</a>";
	}
	
	//组织用户管理
	public String getOrgUserName(long orgId, String userIds)
	{
		ISysOrg sysOrg = (ISysOrg)this.sysOrgService.getById(Long.valueOf(orgId));
		String[] userids = userIds.split(",");
		String userName = "";
		for (int i = 0; i < userids.length; i++) {
			ISysUser sysUser = (ISysUser)this.sysUserService.getById(Long.valueOf(userids[i]));
			userName = userName + sysUser.getFullname() + ",";
		}
		return "向组织【" + sysOrg.getOrgName() + "】添加人员【" + userName + "】";
	}
	
	//系统参数属性
	public String getSysParamLink(long parId)
	{
		ISysParam sysParam = (ISysParam)this.sysParamService.getById(Long.valueOf(parId));
		return sysParam.getParamName()+"【"+sysParam.getParamKey()+"】";
	}
	
	//维度
	public String getDemensionLink(long demId)
	{
		IDemension demension = (IDemension)this.demensionService.getById(Long.valueOf(demId));
		return demension.getDemName()+"【"+demension.getDemDesc()+"】";
	}
	
	//系统参数
	public String getSysParameterLink(Long id)
	{
		ISysParameter sysParameter = (ISysParameter)this.sysParameterService.getById(id);
		return sysParameter.getParamname()+"【"+sysParameter.getId()+"】";
	}
	
	//系统日志开关
	public String getSysLogSwitchLink(Long id)
	{
		ISysLogSwitch sysLogSwitch = (ISysLogSwitch)this.sysLogSwitchService.getById(id);
		return "【"+sysLogSwitch.getModel()+"】";
	}
	
	//表单类别
	public String getRecTypeLink(Long typeId)
	{
		RecType recType = (RecType)this.recTypeService.getById(typeId);
		return recType.getTypeName()+"【"+recType.getAlias()+"】";
	}
	
	//表单类别
	public String getRecFunLink(Long funId)
	{
		RecFun RecFun = (RecFun)this.recFunService.getById(funId);
		return RecFun.getFunName()+"【"+RecFun.getAlias()+"】";
	}
	
	//表单角色
	public String getRecRoleLink(Long roleId)
	{
		RecRole recRole = (RecRole)this.recRoleService.getById(roleId);
		return recRole.getRoleName()+"【"+recRole.getAlias()+"】";
	}
		
	//岗位
	public String getPositionLink(long posId) {
		IPosition position = (IPosition)this.positionService.getById(Long.valueOf(posId));
		return position.getPosName()+"【"+position.getPosCode()+"】";
	}

	//组织结构类型
	public String getSysOrgTypeLink(long id) {
		ISysOrgType sysOrgType = (ISysOrgType)this.sysOrgTypeService.getById(Long.valueOf(id));
		return sysOrgType.getName()+"【"+sysOrgType.getId()+"】";
	}
	
	//新闻评论
	public String getInsNewsCmLink(long cmId) {
		InsNewsCm insNewsCm = (InsNewsCm)this.insNewsCmService.getById(Long.valueOf(cmId));
		return insNewsCm.getFullName()+"【"+insNewsCm.getContent()+"】";
	}
	
	//新闻记录
	public String getInsNewsLink(long newId) {
		InsNews InsNews = (InsNews)this.insNewsService.getById(Long.valueOf(newId));
		return InsNews.getSubject()+"【"+InsNews.getAuthor()+"】";
	}
	
	//组织角色授权信息
	public String getSysOrgRoleChange(String id) {
		if(BeanUtils.isNotEmpty(id)){
			String[] idList = id.split(",");
			String orgName = null;
			String roleName = "";
			for(int i=0;i<idList.length;i++){
				ISysOrgRole sysOrgRole = sysOrgRoleService.getById(Long.valueOf(idList[i]));
				orgName = sysOrgService.getById(sysOrgRole.getOrgid()).getOrgName();
				ISysRole sysRole = sysRoleService.getById(sysOrgRole.getRoleid());
				roleName += "【"+sysRole.getRoleName() + "】 ";
			}
			return "删除组织【"+orgName+"】下角色授权信息： "+roleName;
		}else{
			return null;
		}
	}
	
	//新增组织角色授权信息
	public String addSysOrgRoleChange(String roleIds,String orgId) {
		if(BeanUtils.isNotEmpty(roleIds)&&BeanUtils.isNotEmpty(orgId)){
			String[] roleIdList = roleIds.split(",");
			String orgName = sysOrgService.getById(Long.valueOf(orgId)).getOrgName();
			String roleName = "";
			for(int i=0;i<roleIdList.length;i++){
				ISysRole sysRole = sysRoleService.getById(Long.valueOf(roleIdList[i]));
				roleName += "【"+sysRole.getRoleName() + "】 ";
			}
			return "新增组织【"+orgName+"】下角色授权信息： "+roleName;
		}else{
			return null;
		}
	}
	
	//减少组织可以授权的角色范围
	public String getSysGradeOrgRoleChange(String id) {
		if(BeanUtils.isNotEmpty(id)){
			String[] idList = id.split(",");
			String orgName = null;
			String roleName = "";
			for(int i=0;i<idList.length;i++){
				ISysOrgRoleManage sysOrgRoleManage = sysOrgRoleManageService.getById(Long.valueOf(idList[i]));
				orgName = sysOrgService.getById(sysOrgRoleManage.getOrgid()).getOrgName();
				ISysRole sysRole = sysRoleService.getById(sysOrgRoleManage.getRoleid());
				roleName += "【"+sysRole.getRoleName() + "】 ";
			}
			return "删除组织【"+orgName+"】下可以授权的角色范围： "+roleName;
		}else{
			return null;
		}
	}
	
	//新增组织可以授权的角色范围
	public String addSysGradeOrgRoleChange(String roleIds,String orgId) {
		if(BeanUtils.isNotEmpty(roleIds)&&BeanUtils.isNotEmpty(orgId)){
			String[] roleIdList = roleIds.split(",");
			String orgName = sysOrgService.getById(Long.valueOf(orgId)).getOrgName();
			String roleName = "";
			for(int i=0;i<roleIdList.length;i++){
				ISysRole sysRole = sysRoleService.getById(Long.valueOf(roleIdList[i]));
				roleName += "【"+sysRole.getRoleName() + "】 ";
			}
			return "新增组织【"+orgName+"】下可以授权的角色范围： "+roleName;
		}else{
			return null;
		}
	}
	
	/**
	 * 用户系统全局属性Link
	 * @param paurId
	 * @return
	 */
	public String getSysPaurLink(long paurId)
	{
		SysPaur sysPaur = (SysPaur)this.sysPaurService.getById(Long.valueOf(paurId));
		return "<a href='####' hrefLink='../../system/sysPaur/get.do?paurid=" + paurId + "' class='SysPaurLink' SysPaurId='" + paurId + "' >" + sysPaur.getPaurname() + "</a>";
	}
	
	/**
	 * 流水号Link
	 * @param indId
	 * @return
	 */
	public String getSerialNumberLink(long indId)
	{
		ISerialNumber serialNumber = (ISerialNumber)this.serialNumberService.getById(Long.valueOf(indId));
		return "<a href='####' hrefLink='../../system/serialNumber/get.do?id=" + serialNumber.getId() + "' class='SerialNumberLink' SerialNumberId='" + serialNumber.getId() + "' >" + serialNumber.getName() + "</a>";
	}
	
	/**
	 *职位Link 	
	 * @param indId
	 * @return
	 */
	public String getJobLink(long indId)
	{
		IJob job = (IJob)this.jobService.getById(Long.valueOf(indId));
		return job.getJobname()+"【"+job.getJobcode()+"】";
	}
	
	/**
	 * 资源管理Link
	 * @param resId
	 * @return
	 */
	public String getResourcesLink(String alias)
	{
		IResources resources = (IResources)this.resourcesService.getByAlias(alias);
		return resources.getResName()+"【"+resources.getAlias()+"】";
	}
	/**
	 * 站内信发送信息Link
	 * @param mesendId
	 * @return
	 */
	public String getMessageSendLink(long mesendId)
	{
		IMessageSend messageSend = (IMessageSend)this.messageSendService.getById(Long.valueOf(mesendId));
		return "<a href='####' hrefLink='../../system/messageSend/get.do?id=" + mesendId + "' class='MessageSendLink' MessageSendId='" + mesendId + "' >" + messageSend.getSubject() + "</a>";
	}
	
	/**
	 * 站内信接收者信息Link
	 * @param mesendId
	 * @return
	 */
	public String getMessageReceiverLink(long mereId)
	{
		IMessageReceiver messageReceiver = (IMessageReceiver)this.messageReceiverService.getById(Long.valueOf(mereId));
		return "<a href='####' hrefLink='../../system/messageReceiver/get.do?id=" + mereId + "' class='MessageReceiverLink' MessageReceiverId='" + mereId + "' >" + messageReceiver.getReceiver() + "</a>";
	}
	
	/**
	 * 报表模板link
	 * @param reportId
	 * @return
	 */
	public String getReportTemplateLink(long reportId) {
		IReportTemplate reportTemplate = (IReportTemplate) this.reportTemplateService.getById(Long.valueOf(reportId));
		return "<a href='####' hrefLink='../../system/reportTemplate/get.do?reportId="+ reportTemplate.getReportid()+ "' class='ReportTemplateLink' ReportTemplateId='"+ reportTemplate.getReportid()+ "' >"+ reportTemplate.getTitle() + "</a>";
	}
	
	/**
	 * 报表模板link
	 * @param reportId
	 * @return
	 */
	public String getReportParamLink(long paramid) {
		IReportParam reportParam =this.reportParamService.getById(Long.valueOf(paramid));
		return "<a href='####' hrefLink='../../system/reportParam/get.do?paramid="+ reportParam.getParamid()+ "' class='ReportTemplateLink' ReportParamId='"+ reportParam.getParamid()+ "' >"+ reportParam.getName() + "</a>";
	}
	/**
	 * 外部邮件用户设置link
	 *@author YangBo
	 *@param outsetId
	 *@return
	 */
	public String getOutMailUserSetingLink(long outsetId) {
		IOutMailUserSeting outMailUserSeting = (IOutMailUserSeting)this.outMailUserSetingService.getById(Long.valueOf(outsetId));
		return "<a href='####' hrefLink='../../mail/outMailUserSeting/get.do?id=" + outsetId + "' class='OutMailUserSetingLink' OutMailUserSetingId='" + outsetId + "' >" + outMailUserSeting.getUserName() + "</a>";
	}
	
}
