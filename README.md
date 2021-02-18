## com.cssrc.sun

---
### 八部定制
#### 关于签章(不是签署)
由于设计缺陷,签章这个功能点现在是多张表在同时作用
点击用户信息页后,显示的签章是 CWM_SYS_SIGN_MODEL 这张表里的信息,
 - 其中imagePath这个字段是签章图片在 CWM_SYS_FILE 表里的fileId
 
 在签章管理页面显示的记录是 W_PADHCQZB 里的信息
  - 其中id这个字段是签章图片在 CWM_SYS_FILE 表里的fileId
  
  要注意的是,签章管理页面的操作会同步到上述两张表中,具体为:
  - 如在此页面将某个签章分给某个人,则同时会把 CWM_SYS_SIGN_MODEL 和 W_PADHCQZB 的信息同步修改
  - 如在此页面删除了某个已经分配给了某个人的签章,则同时会把 CWM_SYS_SIGN_MODEL 和 W_PADHCQZB 的信息同步修改,也就说是该人会即刻失去签章
  - 如在此页面删除了某个没有分配到人的签章,则不影响CWM_SYS_SIGN_MODEL表
  即:当对W_PADHCQZB的操作涉及具体人的时候,会同步更改CWM_SYS_SIGN_MODEL表
 








ibms 迁移git
ibms 1.X 版本，主要目标保证平台功能稳定性
1.X后续版本命名规范 为1.X.X.X
直到版本稳定。
后续会根据平台计划升级2.X
2.X后续版本命名规范 为2.X.X.X

ibms 定期计划优化平台。此分支用于优化平台
------------1.1 版本计划------------
note-1:【修复bug】【947】【pageoffice打印出错】【张新光】 
note-2:【优化功能】【代号无】【系统首页看板读取报警任务】【张新光】 
note-3:【修复bug】【945】【链接会自动多出/ibms】【张新光】
note-4:【修复bug】【938】【主流程驳回子流程失败】【张新光】
note-5:【优化功能】【代号无】【表空间一致性代】【张新光】
note-6:【修复bug】【957】【张新光】
note-7:【修复bug】【944】【附件关系表隐藏权限控制问题】【邓文杰】
note-8:【修复bug】【代号无】【日志管理模块-业务数据查询时报错|表单模块-表单保存的后置事件报错】【宋晨晨】
note-9:【修复bug】【代号无】【日志管理模块】【全文检索中加入业务表单提交-导致业务日志所属模块解析时出错】【宋晨晨】
note-10:【修复bug】【代号无】【表单管理模块】【业务数据模板设置中配置按钮的前置后置事件补全】【邓文杰】
note-11:【优化功能】【代号无】【表单管理模块】【表单模板新增导入导出】【谢晨】
note-12:【修复bug】【代号无】【系统管理模块】【修复IE浏览器下角色人员分配-新增，删除时，包404错误的问题】【邓文杰】
note-13:【优化功能】【代号无】【表单管理-自定义sql】【sql验证出错、导出字段可以进行排序、下载文件时显示滚动条】【邓文杰】
note-14:【优化功能】【代号无】【表单管理-表单设计】【下载文件时显示滚动条】【邓文杰】
node-15：修改bug【844】【新闻发布模块状态bug】【张新光】
node-16：修改bug【代号无】【后添加的关系表在进行业务数据模板设置时的bug】【邓文杰】