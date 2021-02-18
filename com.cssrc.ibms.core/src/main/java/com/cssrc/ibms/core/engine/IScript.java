package com.cssrc.ibms.core.engine;

/**
 * 脚本接口。<br>
 * <pre>
 * 作用：
 * 仅为一个标识接口，实现类可以直接被注入到脚本执行引擎，进行计算。
 * 注意：
 * 实现这个接口的类需要注册到spring中,可以在app-beans.xml中手工添加bean定义。
 * @author zhulongchao
 *
 */
public interface IScript {}
