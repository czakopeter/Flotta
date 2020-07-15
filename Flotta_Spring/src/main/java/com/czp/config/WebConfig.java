package com.czp.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
	//beállítás nélkül elvileg korlátlan a feltölthető fájl mérete
//	@Bean(name = "multipartResolver")
//	public CommonsMultipartResolver multipartResolver() {
//	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//	    multipartResolver.setMaxUploadSize(100000);
//	    return multipartResolver;
//	}
}
