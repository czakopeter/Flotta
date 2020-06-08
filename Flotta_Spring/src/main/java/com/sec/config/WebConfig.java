package com.sec.config;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.Ordered;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.CookieLocaleResolver;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.thymeleaf.templateresolver.ServletContextTemplateResolver;

@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
	
//	@Bean
//	public LocaleResolver localeResolver() {
//		SessionLocaleResolver slr = new SessionLocaleResolver();
//		slr.setDefaultLocale(Locale.US);
//		return slr;
//	}
//	
//	@Bean
//	public LocaleChangeInterceptor localeChangeInterceptor() {
//		LocaleChangeInterceptor lci = new LocaleChangeInterceptor();
//		lci.setParamName("lang");
//		return lci;
//	}
//	
//	@Override
//	public void addInterceptors(InterceptorRegistry registry) {
//		registry.addInterceptor(localeChangeInterceptor());
//	}
	
	
  @Bean(name = "localeResolver")
  public LocaleResolver getLocaleResolver()  {
      CookieLocaleResolver resolver= new CookieLocaleResolver();
      resolver.setCookieDomain("localeCookie");
      resolver.setCookieMaxAge(600); 
      return resolver;
  } 

  @Bean(name = "messageSource")
  public MessageSource messageSource()  {
      ReloadableResourceBundleMessageSource messageResource= new ReloadableResourceBundleMessageSource();         
      // For example: i18n/messages_en.properties
      // For example: i18n/messages_fr.properties
      messageResource.setBasename("classpath:/messages");
      messageResource.setDefaultEncoding("UTF-8");
      return messageResource;
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
