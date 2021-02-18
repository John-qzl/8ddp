package com.cssrc.ibms.core.web.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;

import com.cssrc.ibms.core.util.annotion.Interceptor;

public class HandlerInterceptorCommon extends HandlerInterceptorAdapter {

	public Class<? extends HandlerInterceptorAdapter> _class;

	@SuppressWarnings("unchecked")
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {

		if (handler instanceof ResourceHttpRequestHandler) {
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		Interceptor annotation = method.getAnnotation(Interceptor.class);
		boolean result = true;
		if (annotation != null && annotation.interceptor() != null) {
			Class<? extends HandlerInterceptorAdapter>[] classList = annotation
					.interceptor();
			for (int i = 0, size = classList.length; i < size; i++) {
				_class = classList[i];
				result = _class.newInstance().preHandle(request, response,
						handlerMethod);
				if (result) {
					continue;
				} else {
					break;
				}
			}

		}
		return result;

	}

	@SuppressWarnings("unchecked")
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

		if (handler instanceof ResourceHttpRequestHandler) {
		} else {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Interceptor annotation = method.getAnnotation(Interceptor.class);
			if (annotation != null && annotation.interceptor() != null) {
				Class<? extends HandlerInterceptorAdapter>[] classList = annotation
						.interceptor();
				for (int i = 0, size = classList.length; i < size; i++) {
					_class = classList[i];
					_class.newInstance().postHandle(request, response,
							handlerMethod, modelAndView);
				}

			}
		}

	}

	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

		if (handler instanceof ResourceHttpRequestHandler) {
		} else {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			Interceptor annotation = method.getAnnotation(Interceptor.class);
			if (annotation != null && annotation.interceptor() != null) {
				Class<? extends HandlerInterceptorAdapter>[] classList = annotation
						.interceptor();
				for (int i = 0, size = classList.length; i < size; i++) {
					_class = classList[i];
					_class.newInstance().afterCompletion(request, response,
							handlerMethod, ex);
				}

			}
		}

	}

}
