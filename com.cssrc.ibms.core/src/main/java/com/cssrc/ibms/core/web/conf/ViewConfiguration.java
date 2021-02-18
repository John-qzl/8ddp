package com.cssrc.ibms.core.web.conf;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;
import org.springframework.web.servlet.view.UrlBasedViewResolver;

import com.cssrc.ibms.core.constant.system.SysConfConstant;
 
@Configuration
public class ViewConfiguration {
	
	@Bean
	public ViewResolver getViewResolver() {
		InternalResourceViewResolver resolver = new InternalResourceViewResolver();
		resolver.setPrefix(SysConfConstant.MVC_VIEW);
		resolver.setOrder(2);
		resolver.setSuffix("");
		resolver.setViewClass(JstlView.class);
		resolver.setContentType("text/html;charset=UTF-8");
		return resolver;
	}
	
    @Bean
    public ViewResolver urlBasedViewResolver() {
        UrlBasedViewResolver viewResolver;
        viewResolver = new UrlBasedViewResolver();
        viewResolver.setOrder(1);
        viewResolver.setPrefix("/WEB-INF/view/");
        viewResolver.setSuffix("");
        viewResolver.setViewClass(JstlView.class);
        // for debug envirment
        viewResolver.setCache(false);
        return viewResolver;
    }
/*    @Bean
    public ViewResolver tilesViewResolver() {
        UrlBasedViewResolver urlBasedViewResolver = new UrlBasedViewResolver();
        urlBasedViewResolver.setOrder(1);
        urlBasedViewResolver.setViewClass(TilesView.class);
        //urlBasedViewResolver.
        return urlBasedViewResolver;
    }*/
/*    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer tilesConfigurer = new TilesConfigurer();
        tilesConfigurer.setDefinitions(new String[] {"classpath:tiles.xml"});
        return tilesConfigurer;
    }*/
}