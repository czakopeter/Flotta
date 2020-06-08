package com.sec.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
  @Bean(name = "localeResolver")
  public LocaleResolver getLocaleResolver()  {
      CookieLocaleResolver resolver= new CookieLocaleResolver();
      resolver.setCookieName("localeCookie");
      resolver.setCookieMaxAge(600);
      return resolver;
  } 

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
      LocaleChangeInterceptor localeInterceptor = new LocaleChangeInterceptor();
      localeInterceptor.setParamName("lang");
      registry.addInterceptor(localeInterceptor).addPathPatterns("/*");
  }
	
	//beállítás nélkül elvileg korlátlan a feltölthető fájl mérete
//	@Bean(name = "multipartResolver")
//	public CommonsMultipartResolver multipartResolver() {
//	    CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//	    multipartResolver.setMaxUploadSize(100000);
//	    return multipartResolver;
//	}
}
