DELETE FROM CWM_SYS_SCRIPT;
-- ----------------------------
INSERT INTO CWM_SYS_SCRIPT VALUES ('1', '获取当前日期。指定格式输出', 'return scriptImpl.getCurrentDate(\"yyyy-MM-dd HH:mm:ss\"); ', '当前用户脚本', '获取当前日期。指定格式输出: 返回日期类型如下： 2002-11-06');
INSERT INTO CWM_SYS_SCRIPT VALUES ('2', '获取发起用户所属角色', 'return scriptImpl.getUserRoles(strUserId);', '其他用户脚本', '获取发起用户所属角色');
INSERT INTO CWM_SYS_SCRIPT VALUES ('3', '获取当前用户组织的ID', 'return scriptImpl.getCurrentOrgId();', '当前用户脚本', '获取当前用户组织的ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('4', '取得当前用户组织的名称', 'return scriptImpl.getCurrentOrgName();', '当前用户脚本', '取得当前用户组织的名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('5', '取得当前用户主组织名称', 'return scriptImpl.getCurrentPrimaryOrgName();', '当前用户脚本', '取得当前用户主组织名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('6', '判断用户是否属于某角色', 'return scriptImpl.isUserInRole(userId,role);', '判断脚本', '判断用户是否属于某角色');
INSERT INTO CWM_SYS_SCRIPT VALUES ('7', '判断用户是否属于某组织', 'return scriptImpl.isUserInOrg(String userId, String orgName);', '判断脚本', '判断用户是否属于某组织');
INSERT INTO CWM_SYS_SCRIPT VALUES ('8', '取得当前登录用户id', 'return scriptImpl.getCurrentUserId();', '当前用户脚本', '取得当前登录用户id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('9', '启动某个流程', 'return scriptImpl.startFlow(String flowKey, String businnessKey,Map<String, Object> vars);', '流程脚本', '启动某个流程');
INSERT INTO CWM_SYS_SCRIPT VALUES ('10', '根据工号获取姓名', 'return scriptImpl.getFullNameByAccount(String accout);', '其他用户脚本', '根据工号获取姓名');
INSERT INTO CWM_SYS_SCRIPT VALUES ('11', '根据多工号获取多姓名', 'return scriptImpl.getFullNameByAccounts(String accouts);', '其他用户脚本', '根据多工号获取多姓名:工号字符串，多个以逗号隔开');
INSERT INTO CWM_SYS_SCRIPT VALUES ('12', '根据多工号获取多个用户ID', 'return scriptImpl.getUserIdsByAccounts(String accouts);', '其他用户脚本', '根据多工号获取多个用户ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('13', '根据工号获取用户ID', 'return scriptImpl.getUserIdByAccount(String accout);', '其他用户脚本', '根据工号获取用户ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('14', '根据工号获取组织名称', 'return scriptImpl.getOrgNameByAccount(String accout);', '其他用户脚本', '根据工号获取组织名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('15', '根据工号获取组织ID', 'return scriptImpl.getOrgIdByAccount(String account);', '其他用户脚本', '根据工号获取组织ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('16', '判断组织A是否为组织B的子组织', 'return scriptImpl.getOrgBelongTo(String sonOrgId,Long parentOrgId)', '判断脚本', '判断组织A是否为组织B的子组织');
INSERT INTO CWM_SYS_SCRIPT VALUES ('17', '根据组织id获取组织名称', 'return scriptImpl.getOrgNameById(Long orgId);', '其他用户脚本', '根据组织id获取组织名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('18', '返回当前组织的类型id', 'import com.ibms.oa.model.system.SysOrgType;SysOrgType sysOrgType =scriptImpl.getCurrentOrgType();return sysOrgType.getId();', '当前用户脚本', '返回当前组织的类型id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('19', '返回当前组织类型的名称', 'return scriptImpl.getCurrentOrgTypeName();', '当前用户脚本', '返回当前组织类型的名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('20', '判断用户是否该部门的负责人', 'return scriptImpl.isDepartmentLeader(String userId, String orgId) ;', '判断脚本', '判断用户是否该部门的负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('21', '获取用户领导id集合', 'return scriptImpl.getMyLeader();', '其他用户脚本', '获取用户领导id集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('22', '获取用户下属用户ID集合', 'return scriptImpl.getMyUnderUserId();', '其他用户脚本', '获取用户下属用户ID集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('23', '取得当前登录用户工号', 'return scriptImpl.getAccount() ;', '当前用户脚本', '取得当前登录用户工号');
INSERT INTO CWM_SYS_SCRIPT VALUES ('24', '取得当前登录用户名称', 'return scriptImpl.getCurrentName();', '当前用户脚本', '取得当前登录用户名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('25', '获取用户的主岗位名称', 'return scriptImpl.getUserPos(userId)', '其他用户脚本', '获取用户的主岗位名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('26', '获取当前日期', 'return scriptImpl.getCurrentDate();', '当前用户脚本', '获取当前日期');
INSERT INTO CWM_SYS_SCRIPT VALUES ('27', '判断当前用户是否属于该角色', 'return scriptImpl.hasRole(alias);', '判断脚本', '判断当前用户是否属于该角色');
INSERT INTO CWM_SYS_SCRIPT VALUES ('28', '取得当前用户主组织的ID', 'return scriptImpl.getCurrentPrimaryOrgId();', '当前用户脚本', '取得当前用户主组织的ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('29', '取得某用户主组织的Id', 'return scriptImpl.getUserOrgId(Long userId);', '其他用户脚本', '取得某用户主组织的Id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('30', '取得某用户主组织的名称', 'return scriptImpl.getUserOrgName(Long userId);', '其他用户脚本', '取得某用户主组织的名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('31', '获取当前用户所属角色', ' return scriptImpl.getCurrentUserRoles();', '当前用户脚本', '获取当前用户所属角色');
INSERT INTO CWM_SYS_SCRIPT VALUES ('32', '根据用户ID获取岗位Id', 'return scriptImpl.getUserPosId(userId)', '其他用户脚本', '根据用户ID获取岗位Id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('33', '获取当前用户的主岗位名称', 'return scriptImpl.getCurUserPos();', '当前用户脚本', '获取当前用户的主岗位名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('34', '根据工号获取岗位ID', ' return scriptImpl.getPosIdByAccount(account);', '其他用户脚本', '根据工号获取岗位ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('35', '根据工号获取岗位名称', 'return scriptImpl.getPosNameByAccount(account);', '其他用户脚本', '根据工号获取岗位名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('36', '获取当前用户的主岗位ID', 'return scriptImpl.getCurUserPosId();', '当前用户脚本', '获取当前用户的主岗位ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('37', '获取cmd对象', 'import com.ibms.core.bpm.model.ProcessCmd;return scriptImpl.getProcessCmd()', '其他用户脚本', '获取cmd对象');
INSERT INTO CWM_SYS_SCRIPT VALUES ('38', '通过用户账号设置任务执行人', '  return setAssigneeByAccount(TaskEntity task, String userAccout);', '流程脚本', '通过用户账号设置任务执行人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('39', '根据当前用户取得指定参数key的参数值', 'return scriptImpl.getParaValue(String paramKey);', '其他用户脚本', '根据当前用户取得指定参数key的参数值');
INSERT INTO CWM_SYS_SCRIPT VALUES ('40', '根据用户ID和参数key获取参数值', 'return scriptImpl.getParaValueByUser(Long userId, String paramKey);', '其他用户脚本', '根据用户ID和参数key获取参数值');
INSERT INTO CWM_SYS_SCRIPT VALUES ('41', '获取流程当前用户直属领导的主岗位名称', 'return scriptImpl.getCurDirectLeaderPos();', '当前用户脚本', '获取流程当前用户直属领导的主岗位名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('42', '获取用户的组织的直属领导', 'return scriptImpl.getDirectLeaderByUserId(String userId);', '当前用户脚本', '获取用户的组织的直属领导');
INSERT INTO CWM_SYS_SCRIPT VALUES ('43', '执行用户自定义的sql语句', 'return scriptImpl.executeSql(String sql);', '执行sql脚本', '执行用户自定义的sql语句');
INSERT INTO CWM_SYS_SCRIPT VALUES ('44', '获取当前分公司或集团', 'return scriptImpl.getCurrentCompany();', '当前用户脚本', '获取当前分公司或集团');
INSERT INTO CWM_SYS_SCRIPT VALUES ('45', '获取当前单位类型', 'return scriptImpl.getCurrentCompanyType();', '当前用户脚本', '获取当前单位类型');
INSERT INTO CWM_SYS_SCRIPT VALUES ('46', '判断当前登陆者部门是否拥有该部门属性', 'return scriptImpl.hasParamKeyForOrg(String paramKey);', '判断脚本', '判断当前登陆者部门是否拥有该部门属性');
INSERT INTO CWM_SYS_SCRIPT VALUES ('47', '判断当前登陆者是否拥有该用户属性', 'return scriptImpl.hasParamKeyForUser(String paramKey);', '判断脚本', '判断当前登陆者部门是否拥有该部门属性');
INSERT INTO CWM_SYS_SCRIPT VALUES ('48', '逐级审批跳转规则(当前登陆者分公司或集团就停止)', 'return scriptImpl.isTopUpserApproveForCurUser();', '判断脚本', '逐级审批跳转规则到当前登陆者分公司或集团就停止');
INSERT INTO CWM_SYS_SCRIPT VALUES ('49', '逐级审批跳转规则', 'return scriptImpl.isTopUpserApprove(Long orgType);', '判断脚本', '逐级审批跳转规则');
INSERT INTO CWM_SYS_SCRIPT VALUES ('50', '获取当前分公司或集团的ID', 'return scriptImpl.getCurrentCompanyOrgId();', '当前用户脚本', '获取当前分公司或集团的ID');
INSERT INTO CWM_SYS_SCRIPT VALUES ('51', '获取当前分公司或集团的名称', 'return scriptImpl.getCurrentCompanyOrgName();', '当前用户脚本', '获取当前分公司或集团的名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('52', '获取当前用户的岗位名称', 'return scriptImpl.getCurrentPosName();', '当前用户脚本', '获取当前用户的岗位名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('53', '获取当前用户的岗位id', 'return scriptImpl.getCurrentPosId();', '当前用户脚本', '获取当前用户的岗位id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('54', '获取当前用户的职务名称', 'return scriptImpl.getCurrentJob();', '当前用户脚本', '获取当前用户的职务名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('55', '获取当前用户的职务等级', 'return scriptImpl.getCurrentJobGrade();', '当前用户脚本', '获取当前用户的职务等级');
INSERT INTO CWM_SYS_SCRIPT VALUES ('56', '判断当前登陆者的分公司是否拥有该部门属性', 'return scriptImpl.hasParamKeyForCompany(String paramKey);', '判断脚本', '判断当前登陆者的分公司是否拥有该部门属性');



-- 20170405 zxg--------------------------
-- 修改脚本分类 -- 
UPDATE CWM_SYS_SCRIPT SET CWM_SYS_SCRIPT.CATEGORY='当前用户脚本' WHERE CATEGORY='1';
UPDATE CWM_SYS_SCRIPT SET CATEGORY='判断脚本' WHERE CATEGORY='2' ;
UPDATE CWM_SYS_SCRIPT SET CATEGORY='流程脚本' WHERE CATEGORY='3';
UPDATE CWM_SYS_SCRIPT SET CATEGORY='其他用户脚本' WHERE CATEGORY='4';

DELETE FROM cwm_sys_script WHERE name='系统脚本1';







INSERT INTO CWM_SYS_SCRIPT VALUES ('57', '判断当前用户是否属于某组织', 'return scriptImpl.isCurUserInOrg(String orgName);', '判断脚本', '判断当前用户是否属于某组织');
INSERT INTO CWM_SYS_SCRIPT VALUES ('58', '判断用户是否属于某组织（存在多级组织）', 'return scriptImpl.isUserInOrgs(String userId,String orgName);', '判断脚本', '判断用户是否属于某组织（存在多级组织）');
INSERT INTO CWM_SYS_SCRIPT VALUES ('60', '获取“当前系统登录”帐号用户SysUser对象。', 'return scriptImpl.getCurrentUser();', '当前用户脚本', '获取“当前系统登录”帐号用户SysUser对象。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('61', '获取“当前系统登录”用户的组织SysOrg对象。', 'return scriptImpl.getCurrentOrg();', '当前用户脚本', '获取“当前系统登录”用户的组织SysOrg对象。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('62', '获取“当前系统登录”用户的主岗位名称。', 'return scriptImpl.getCurUserPos();', '当前用户脚本', '获取“当前系统登录”用户的主岗位名称。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('63', '获取“当前系统登录”用户的主岗位名称。', 'return scriptImpl.getCurUserPosName();', '当前用户脚本', '获取“当前系统登录”用户的主岗位名称。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('64', '获取 用户ID集合set', 'return scriptImpl.getUserIdSetByAccount(String userName);', '其他用户脚本', '获取 用户ID集合set');
INSERT INTO CWM_SYS_SCRIPT VALUES ('65', '根据组织id获取组织id,判断当前组织id是否存', 'return scriptImpl.getOrgIdById(String orgId);', '判断脚本', '根据组织id获取组织id,即：判断当前组织id是否存在数据库中。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('66', '获取用户的主岗位负责人的岗位名称。', 'return scriptImpl.getDirectLeaderPosByUserId(String userId);', '其他用户脚本', '获取用户userid的主岗位负责人的岗位名称。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('67', '根据组织id获取组织负责人,构造taskExecutor列表', 'return scriptImpl.getExecutor(Long orgId);', '其他用户脚本', '根据组织id获取组织负责人,构造taskExecutor列表');
INSERT INTO CWM_SYS_SCRIPT VALUES ('68', '判断用户是否有该职务名称', 'return scriptImpl.isJobName(String userId,String jobName);', '判断脚本', '判断用户是否有该职务名称');

INSERT INTO CWM_SYS_SCRIPT VALUES ('71', '获取用户的组织的直属领导', 'return scriptImpl.getLeaderByUserId(Long userId);', '其他用户脚本', '获取用户userId的组织的直属领导');
INSERT INTO CWM_SYS_SCRIPT VALUES ('72', '更新记录的属性值', 'scriptImpl.updateByTableName(String businessKey,String tableName,Map<String, Object> map);', '其他用户脚本', '更新记录的属性值');
INSERT INTO CWM_SYS_SCRIPT VALUES ('73', '获得当前用户的的负责人', 'return scriptImpl.getMgrByOrgIds();', '当前用户脚本', '获得当前用户的的负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('74', '担任某组织岗位和具有某角色身份的用户(包括之前担任的)', 'return scriptImpl.getUserByCurOrgRoleAlias(String roleNameAlias);', '其他用户脚本', '担任某组织岗位和具有某角色身份的用户(包括之前担任的)');
INSERT INTO CWM_SYS_SCRIPT VALUES ('75', '担任某组织岗位和具有某角色身份的用户(包括之前担任的) 全名称', 'return scriptImpl.getUserFullnameByCurOrgRoleAlias(String roleNameAlias);', '其他用户脚本', '担任某组织岗位和具有某角色身份的用户(包括之前担任的) 全名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('76', '在组织里担当该岗位的用户集合', 'return scriptImpl.getUsersByOrgAndPos(Long startOrgId,String posName);', '其他用户脚本', '在组织里担当该岗位的用户集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('77', '在组织里担当该职务的用户集合', 'return scriptImpl.getLeaderByOrgName(String orgId,String jobCode);', '其他用户脚本', '在组织里担当该职务的用户集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('79', '获取用户的职务代号code。', 'return scriptImpl.getJobCodeByUserId(String userId);', '其他用户脚本', '获取用户的职务代号code。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('80', '获取当前登陆用户的职务job名称', 'return scriptImpl.getCurrentJobName();', '当前用户脚本', '获取当前登陆用户的职务job名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('81', '获取当前登陆用户的职务代号code。', 'return scriptImpl.getCurrentJobCode();', '当前用户脚本', '获取当前登陆用户的职务代号code。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('82', '获取当前用户组织的 指定组织类别的 组织id', 'return scriptImpl.getCurrUserOryByTypeId(Long orgType);', '当前用户脚本', '获取当前用户组织的 指定组织类别的 组织id');
INSERT INTO CWM_SYS_SCRIPT VALUES ('83', '获取当前用户组织的 指定组织类别的 组织名称', 'return scriptImpl.getCurrUserOrgNameByTypeId(Long orgType);', '当前用户脚本', '获取当前用户组织的 指定组织类别的 组织名称');
INSERT INTO CWM_SYS_SCRIPT VALUES ('84', '执行用户自定义的sql语句', 'return scriptImpl.executeSql(String sql);', '其他用户脚本', '执行用户自定义的sql语句');
INSERT INTO CWM_SYS_SCRIPT VALUES ('85', '获取系统登陆用户行政维度下，该组织类别下，该职务下， 对应岗位下的 人员。', 'return scriptImpl.getByOrgGradeAndJob(int grade,Long jobId);', '其他用户脚本', '获取系统登陆用户行政维度下，该组织类别下，该职务下， 对应岗位下的 人员。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('86', '获取属于该公司，并拥有该角色的（主管）用户列表', 'return scriptImpl.getByCompanyRole(String companyId,Long roleId,boolean ignoreCharge);', '其他用户脚本', '获取属于该公司，并拥有该角色的（主管）用户列表');
INSERT INTO CWM_SYS_SCRIPT VALUES ('87', '获取当前用户所在的公司（组织）。', 'return scriptImpl.getCurrentCompany();', '当前用户脚本', '获取当前用户所在的公司（组织）。');
INSERT INTO CWM_SYS_SCRIPT VALUES ('89', '获取系统登陆用户行政维度下，的该组织类别下，指定角色下的 人员', 'return scriptImpl.getByOrgGradeAndRole(int grade,Long roleId);', '当前用户脚本', '获取系统登陆用户行政维度下，的该组织类别下，指定角色下的 人员');
INSERT INTO CWM_SYS_SCRIPT VALUES ('90', '获取人员当前组织类别的组织 负责人的userid集合', 'return scriptImpl.getUserStep(ISysOrg startOrg,Long currentUserId,Long orgType);', '其他用户脚本', '获取人员currentUserId，当前组织类别orgType的组织 负责人的user id集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('91', '获取当前系统登陆用户，当前组织类别的组织 负责人的userid集合', 'return scriptImpl.getUserByStep(Long orgType);', '其他用户脚本', '获取当前系统登陆用户，当前组织类别的组织 负责人的userid集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('92', '获取当前流程变量，中参数fieldName，（组织或者人员值），当前组织类别orgType的组织 负责人的user id集合', 'return scriptImpl.getUserByFieldStep(String fieldName,Boolean isOrg,Long orgType);', '判断脚本', '获取当前流程变量，中参数fieldName，（组织或者人员值），当前组织类别orgType的组织 负责人的user id集合');
INSERT INTO CWM_SYS_SCRIPT VALUES ('93', '获取当前用户组织，该组织类别下，的主负责人', 'return scriptImpl.getChargerStep(Long orgType);', '当前用户脚本', '获取当前用户组织，该组织类别下，的主负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('94', '获取组织，该组织类别下，的主负责人', 'return scriptImpl.getChargerStepByOrgId(Long orgId,Long orgType);', '其他用户脚本', '获取组织，该组织类别下，的主负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('95', '获取系统登陆用户的组织，组织类别下的主负责人', 'return scriptImpl.getLeaderStep(Long orgType);', '当前用户脚本', '获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('96', '获取用户的组织，组织类别下的主负责人', 'return scriptImpl.getLeaderStepByUserId(Long userId,Long orgType);', '其他用户脚本', '获取用户的组织（orgId），该组织类别（orgType）下的，主负责人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('102', '判断子表是否有数据', 'return scriptImpl.isSubTableHasData(String tableName,Long fk);', '判断脚本', '判断子表是否有数据');



INSERT INTO CWM_SYS_SCRIPT VALUES ('127', '添加节点人员', 'taskUserAssignService.addNodeUser(String nodeId,List<TaskExecutor> executors);', '流程脚本', '添加节点人员');
INSERT INTO CWM_SYS_SCRIPT VALUES ('128', '设置某个节点的执行人', 'taskUserAssignService.addNodeUser(String nodeId,String userIds);', '流程脚本', '设置某个节点的执行人');
INSERT INTO CWM_SYS_SCRIPT VALUES ('129', ' 设置的任务执行人', 'taskUserAssignService.setExecutors(String users);', '流程脚本', '设置某个节点的执行人');


-- INSERT INTO CWM_SYS_SCRIPT VALUES ('101', '获取用户的组织，组织类别下的主负责人', 'return scriptImpl.getLeaderAndChargerStepByUserId(Long userId,Long orgType);', '其他用户脚本', '获取用户的组织（orgId），该组织类别（orgType）下的，主负责人');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('97', '获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人', 'return scriptImpl.getLeaderStepByUserId(Long orgId,Long userId,Long orgType,Set<String> userSet);', '其他用户脚本', '获取系统登陆用户的组织（orgId），该组织类别（orgType）下的，主负责人');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('98', '获取用户组织id，orgid列表。', 'return scriptImpl.getLeaderOrgId(List<IPosition> userPosList);', '其他用户脚本', '获取用户组织id，orgid列表。');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('99', '获取用户组织id，orgid列表', 'return scriptImpl.getLeaderAndChargerStep(Long orgType);', '其他用户脚本', '获取用户组织id，orgid列表');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('103', '获取当前用户组织，该组织类别下，的主负责人', 'return scriptImpl.getChargerStepByOrgId(Long orgId,Long orgType,Set<String> userSet);', '其他用户脚本', '获取当前用户组织，该组织类别下，的主负责人');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('104', '判断表formData.getFullTableName() 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在', 'return scriptImpl.validDataExist(IFormData formData,String fieldName,String messages);', '判断脚本', '判断表formData.getFullTableName() 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('105', '判断表formData.getFullTableName() 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在', 'return scriptImpl.validDataExist(IFormData formData,String fieldName);', '判断脚本', '判断表formData.getFullTableName() 中，指定字段fieldName的值formData.getMainField(fieldName)，的数据 是否存在');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('106', '判断用户是否有该职务名称', 'return scriptImpl.getByCompanyRole(String companyId,Long roleId);', '判断脚本', '判断用户是否有该职务名称');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('107', '获取子表数据', 'return scriptImpl.getByFk(String subTableName,Long fk);', '其他用户脚本', '获取子表数据');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('108', '获取主流程的某节点，最后一条审批意见的执行人', 'return scriptImpl.getAuditByMainInstId(Long flowRunId,String nodeId);', '其他用户脚本', '获取主流程的某节点，最后一条审批意见的执行人');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('109', '获取当前用户组织的 指定组织类别（3L室处）的 组织id', 'return scriptImpl.getCurrUserDeptId();', '当前用户脚本', '获取当前用户组织的 指定组织类别（3L室处）的 组织id');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('110', '获取当前用户组织的 指定组织类别（2L所队）的 组织id', 'return scriptImpl.getCurrUserUnitId();', '当前用户脚本', '获取当前用户组织的 指定组织类别（2L所队）的 组织id');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('111', '将list 转化为map', 'return scriptImpl.convertToMap(List<ISysOrgType> typeList);', '其他用户脚本', '将list 转化为map');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('112', '判断系统登陆用户的组织orgid或者企业companyId是否有该组织参数属性。', 'return scriptImpl.hasParamKeyForOrgAndCompany(String paramKey);', '判断脚本', '判断系统登陆用户的组织orgid或者企业companyId是否有该组织参数属性。');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('113', '获取该组织orgid是否有该组织参数属性值', 'return scriptImpl.getParamValueByOrgIdKey(String orgId,String paramKey);', '其他用户脚本', '获取该组织orgid是否有该组织参数属性 值');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('114', '拥有该用户参数属性和用户参数属性值的用户id集合', 'return scriptImpl.getUserByParamKeyValue(String paramKey,String paramValue);', '其他用户脚本', '拥有该用户参数属性和用户参数属性值的用户id集合');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('115', '一参数与另一组织参数值相同的用户id集合', 'return scriptImpl.getFgUsers(String orgId,String fgDept,String fgUser);', '其他用户脚本', '一参数与另一组织参数值相同的用户id集合');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('116', '清理流程当前节点的人员选择的流程变量信息', 'scriptImpl.clearNodeUserMap();', '流程脚本', '清理流程当前节点的人员选择的流程变量信息');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('117', '获取当前流程变量，中存储的子表的字段的值', 'return scriptImpl.getSubFieldUser(String tableName,String field);', '流程脚本', '获取当前流程变量，中存储的子表的字段的值');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('118', '根据主表记录id，获取子表记录中，用户字段值集合', 'return scriptImpl.getSubTableUser(String tableName,String fieldName,String businessKey);', '流程脚本', '根据主表记录id，获取子表记录中，用户字段值集合');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('119', '更新表记录字段值', 'scriptImpl.setFormDataValue(String dataId,String tableName,String[] fieldNameArray,Object[] valueArray);', '流程脚本', '更新表记录字段值');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('120', '更新表记录字段值where keyName=?', 'scriptImpl.setFormDataValue(String dataId, String tableName,String[] fieldNameArray, Object[] valueArray);', '流程脚本', '更新表记录字段值');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('121', '按照日期格式要求，格式化日期字段', 'scriptImpl.setFormDataValue(String dataId, String tableName,String[] fieldNameArray, Object[] valueArray, String keyName);', '流程脚本', '按照日期格式要求，格式化日期字段');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('122', '给关联表中的“日期”字段赋值。', 'scriptImpl.setRefFormDataValue(String curTableName, String curDataId,String curKeyName, String refTableName, String refDateFieldName,String refDateFieldValue, String refDateFormat);', '流程脚本', '给关联表中的“日期”字段赋值。');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('123', '给关联表中的“多个”字段赋值。', 'scriptImpl.setRefFormDataValue(String curTableName, String dataId,String curField, String refTableName, String[] refFieldNameArray,Object[] refValueArray);', '流程脚本', '给关联表中的“多个”字段赋值。');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('124', '根据主键ID值获取记录的其他字段值', 'return scriptImpl.getFormDataValue(String dataId, String tableName,String fieldName);', '流程脚本', '根据主键ID值获取记录的其他字段值');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('125', '根据主表记录id，获取子表记录中，用户字段值集合', 'return scriptImpl.getSubTableUser(String tableName, String fieldName,String businessKey);', '流程脚本', '根据主表记录id，获取子表记录中，用户字段值集合');
-- INSERT INTO CWM_SYS_SCRIPT VALUES ('126', '根据表tableName的字段fieldName的值fieldValue，查询所有列表记录后，获取返回字段的集合', 'return scriptImpl.getFieldValues(String tableName, String fieldName,String fieldValue, String orderFieldName, String retrunField);', '流程脚本', '根据表tableName的字段fieldName的值fieldValue，查询所有列表记录后，获取返回字段的集合');
-- -- 20170405 zxg----- end ---------------------



commit;