package com.cssrc.ibms.core.web.conf;

import java.util.List;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import com.cssrc.ibms.core.util.json.DateMapper;

@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
// 指明controller所在的包名
@ComponentScan(basePackages = { "com.**.ibms.**.controller" })
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	
	@Override
	public void configureDefaultServletHandling(
			final DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}
	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> list){
		MappingJacksonHttpMessageConverter mapper=new MappingJacksonHttpMessageConverter();
		mapper.setObjectMapper(new DateMapper());
		list.add(mapper);
	}

}
