package com.cssrc.ibms.core.log;

import java.lang.reflect.Method;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.reflect.MethodSignature;

import com.cssrc.ibms.core.log.service.LogRouting;
import com.cssrc.ibms.core.util.annotion.Action;

/**
 *@Description 全局日志处理入口
 *@author vector
 *@date 2017年9月16日 上午9:34:44
 */
public class LogAspectEntrance {
	private Log logger = LogFactory.getLog(LogAspectEntrance.class);
	@Resource
	private LogRouting logRouting;
	/**
	 * 总体思路：根据切点获取方法和注解，判断日志记录是否需要，[oldData]执行方法体前/后获取数据[newData],进行数据比较，写入日志
	 * 
	 * 获取切点方法和注解、判断是否需要日志记录以及根据Controller层路径进行分类，转到不同的日志处理类中。
	 * @param point
	 * @return
	 * @throws Throwable
	 */
	public Object doAudit(ProceedingJoinPoint point) {
		try {
			// 根据切点获取方法
			Method method = getMethod(point);
			// 方法不存在，返回继续执行方法体
			if (method == null) {
				return point.proceed();
			}
			// 获取注解
			Action annotation = method.getAnnotation(Action.class);
			// 注解不存在，返回继续执行方法体
			if (annotation == null) {
				return point.proceed();
			}
			return logRouting.routing(method, annotation, point);
		} catch (Exception e) {
			logger.error(e.getMessage());
			e.printStackTrace();
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return null;
		
	}
	/**
	 * 根据切点获取方法
	 * @param point
	 * @return Method
	 * @author scc
	 */
	protected Method getMethod(ProceedingJoinPoint point){
		try {
			Signature sig=point.getSignature();
			//如果Controller层下有接口 也会被处理
			if(!(sig instanceof MethodSignature)){
				//throw new IllegalArgumentException("该注解只能用于方法，不能用于接口！");
				return null;
			}
			MethodSignature msig=(MethodSignature) sig;
			System.err.println(msig.getDeclaringType()+"."+msig.getName());
			Method currentMethod = null;
			try {
				//是同一个controller才转换  [防止父类方法进入]
				if(msig.getDeclaringType().equals(point.getTarget().getClass())){
					currentMethod=point.getTarget().getClass().getMethod(msig.getName(),msig.getParameterTypes());
				}
			} catch (NoSuchMethodException e) {
				logger.error("源:"+msig.getDeclaringType()+"."+msig.getName()+"转为:"+point.getTarget().getClass()+"失败\t"+e.getMessage());
				e.printStackTrace();
			} catch (SecurityException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			return currentMethod;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}
